spectrumView = function() {

	var handler = {};
	var _representation, _value;
	var canvas;
	var ctx;
	var canvasWidth = 500;
	var canvasHeight = 500;
	var mzScale, rtScale;
	var features = [];
	var olTopLeft, olTopRight, olBottomLeft, olBottomRight;
	var rtMult = 1;
	var mzMult = 1;

	selected = null;
	var curTransform = d3.zoomIdentity;
	var points = [];
	var pointsTimeout = null;

	const MAX_POINT_SIZE = 6;
	const MIN_POINT_SIZE = 2;
	const SCALE_THRESHOLD = 5;

	// Enable lazy loading
	if (knimeService.isViewRequestsSupported()) {
		knimeService.loadConditionally(["de/openms/knime/views/spectrum/spectrumLazyLoad"],
		function() {
			spectrumView.initRequest({type: "FEATURES"});
		});
	}

	// Color scale for features and points
	const colorScale = d3.scaleLinear()
                .domain([0, 0.125, 0.25, 0.5, 0.625, 0.75, 0.875, 1])
                .range(['#1a9850', '#66bd63', '#a6d96a', '#d9ef8b', '#fee08b', '#fdae61', '#f46d43', '#d73027'])
                .interpolate(d3.interpolateHcl);

	// Finds a feature that contains the given point
	function getFeatureAtPoint(pt) {
		const tpt = curTransform.invert(pt);
		const mz = mzScale.invert(tpt[0]);
		const rt = rtScale.invert(tpt[1]);
		for (let f of features) {
			if (mz >= f.mzStart && mz <= f.mzEnd && rt >= f.rtStart && rt <= f.rtEnd) {
				return f;
			}
		}
	}

	// Given a point on the canvas, calculates the corresponding mz and rt
	function invertPoint(pt) {
		const tpt = curTransform.invert(pt);
		const mz = mzScale.invert(tpt[0]);
		const rt = rtScale.invert(tpt[1]);
		return [mz, rt];
	}

	// Loads points for the current viewport
	function loadFeatureData() {
		if (_representation.hasDB) {
			const start = invertPoint([-200, -200]);
			const end = invertPoint([canvasWidth + 200, canvasHeight + 200]);
			// Debounce
			if (pointsTimeout) {
				clearTimeout(pointsTimeout);
			}
			pointsTimeout = setTimeout(function() {
				olTopRight.text("Loading intensities...");
				const r = {
					type: "FEATURE_DATA",
					mzStart: end[1],
					mzEnd: start[1],
					rtStart: start[0],
					rtEnd: end[0]
				};
				spectrumView.initRequest(r);
			}, 100);
		}
	}

	handler.init = function(representation, value) {
		_representation = representation;
		_value = value;
		// Some padding
		representation.minMz -= 10;
		representation.maxMz += 10;
		representation.minRt -= 10;
		representation.maxRt += 10;

		const body = d3.select("body");
		// Avoid scrolling the document
		d3.selectAll("html, body, document")
			.style("width", "100%")
			.style("height", "100%")
			.style("overflow", "hidden");

		// Create main drawing area with zooming enabled
		canvas = body.append("canvas")
			.attr("id", "main-canvas")
			.style("width", "100%")
			.style("height", "100%")
			.call(d3.zoom().scaleExtent([1, 8]).on("zoom", zoom));
			/*
			.on("click", function() {
				const f = getFeatureAtPoint(d3.mouse(this));
				if (f) {
					if (selected) selected.selected = false;
					f.selected = true;
					selected = f;
					draw();
				}
			})*/
		ctx = canvas.node().getContext("2d");
		updateCanvasSize();

		// User can scale with arrow keys
		d3.select("body")
	    .on("keydown", function() {
				if(d3.event.keyCode === 37) {
					mzMult += 0.01;
				} else if (d3.event.keyCode == 39) {
					mzMult = Math.max(0, mzMult - 0.01);
				}
				if(d3.event.keyCode === 38) {
					rtMult += 0.01;
				} else if (d3.event.keyCode == 40) {
					rtMult = Math.max(0, rtMult - 0.01);
				}
				updateCanvasSize();
			});

		// Loading indicator
		olTopRight = body.append("div")
			.style("position", "absolute")
			.style("right", "10px")
			.style("top", "5px")
			.style("background-color", "#dddddd99")
			.style("padding", "3px")
			.style("border-radius", "3px");

		// React to resize by redrawing with proper ratios
		window.addEventListener("resize", updateCanvasSize);
	}

	// Updates the canvas on a resize
	function updateCanvasSize() {
		const rect = canvas.node().getBoundingClientRect();
		canvasWidth = rect.width;
		canvasHeight = rect.height;
		canvas.attr("width", canvasWidth).attr("height", canvasHeight);
		mzScale = d3.scaleLinear()
								.domain([_representation.minMz, _representation.maxMz])
								.range([50, canvasWidth / mzMult]);
		rtScale = d3.scaleLinear()
								.domain([_representation.minRt, _representation.maxRt])
								.range([canvasHeight / rtMult - 25, 0]);
		draw();
	}

	// d3 zoom callback
	function zoom() {
		curTransform = d3.event.transform;
		// If zoomed in enough, load the points of feature data
		if (curTransform.k > SCALE_THRESHOLD) {
			loadFeatureData();
		}
		draw();
	}

	// Main drawing function
	function draw() {
		ctx.clearRect(0, 0, canvasWidth, canvasHeight);
		// Draw points
		if (curTransform.k > SCALE_THRESHOLD) {
			drawPoints();
		}
		drawFeatures();
		drawYAxis();
		drawXAxis();
	}

	// Draw intensity points
	function drawPoints() {
		for (let pt of points) {
			const t = curTransform.apply([mzScale(pt.mass), rtScale(pt.rt)]);
			const i = pt.intensity;
			ctx.fillStyle = colorScale(i);
			const size = MIN_POINT_SIZE + i * (MAX_POINT_SIZE - MIN_POINT_SIZE);
			ctx.fillRect(t[0] - size / 2, t[1] - size / 2, size, size);
		}
	}

	// Draws feature rectangles
	function drawFeatures() {
		for (let feature of features) {
			const start = curTransform.apply([mzScale(feature.mzStart), rtScale(feature.rtStart)]);
			const end = curTransform.apply([mzScale(feature.mzEnd), rtScale(feature.rtEnd)]);
			const i = feature.intensity;
			if (curTransform.k <= SCALE_THRESHOLD) {
				ctx.fillStyle = colorScale(i);
				ctx.fillRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
				ctx.strokeStyle = "#999";
				if (feature.selected) {
					ctx.strokeStyle = "#000";
				}
				ctx.strokeRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
			} else {
				const color = colorScale(i);
				ctx.strokeStyle = feature.selected ? "#000" : color;
				ctx.strokeRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
			}
		}
	}

	function drawYAxis() {
		ctx.fillStyle = "#ffffff99";
		ctx.fillRect(0, 0, 50, canvasHeight);
		ctx.beginPath();
		ctx.moveTo(50, 10);
		ctx.lineTo(50, canvasHeight - 40);
		ctx.strokeStyle = "#000";
		ctx.stroke();

		ctx.fillStyle = "#000";
		const yStep = (canvasHeight - 50) / 9;
		for (let i = 0; i < 10; i++) {
			const y = 10 + i * yStep;
			ctx.moveTo(45, y);
			ctx.lineTo(55, y);
			ctx.stroke();

			const rt = invertPoint([0, y])[1];
			ctx.fillText(Math.round(rt * 100) / 100, 5, y + 4);
		}
	}

	function drawXAxis() {
		ctx.fillStyle = "#ffffff99";
		ctx.fillRect(0, canvasHeight - 30, canvasWidth, 30);
		ctx.beginPath();
		ctx.moveTo(50, canvasHeight - 25);
		ctx.lineTo(canvasWidth - 10, canvasHeight - 25);
		ctx.strokeStyle = "#000";
		ctx.stroke();

		ctx.fillStyle = "#000";
		const xStep = (canvasWidth - 60) / 9;
		for (let i = 0; i < 10; i++) {
			const x = 50 + i * xStep;
			ctx.moveTo(x, canvasHeight - 20);
			ctx.lineTo(x, canvasHeight - 30);
			ctx.stroke();

			const mz = invertPoint([x, 0])[0];
			ctx.fillText(Math.round(mz * 100) / 100, x - 30, canvasHeight - 10);
		}
	}

/*
	function staggeredDrawFeatures(idx, num) {
		const endIdx = Math.min(idx + num, features.length);
		// Draw feature rectangles
		for (let i = idx; i < endIdx; i++) {
			const feature = features[i];
			const start = curTransform.apply([mzScale(feature.mzStart), rtScale(feature.rtStart)]);
			const end = curTransform.apply([mzScale(feature.mzEnd), rtScale(feature.rtEnd)]);
			const i = feature.intensity;
			if (curTransform.k <= SCALE_THRESHOLD) {
				ctx.fillStyle = colorScale(i);
				ctx.fillRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
				ctx.strokeStyle = "#999";
				if (feature.selected) {
					ctx.strokeStyle = "#000";
				}
				ctx.strokeRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
			} else {
				ctx.strokeStyle = colorScale(i);
				ctx.strokeRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
			}
		}
		if (endIdx < features.length) {
			requestAnnimationFrame(() => staggeredDrawFeatures(endIdx, num));
		}
	}
*/

	handler.handleProgress = function(monitor) {
		if (!monitor.progress) {
			return;
		}
		console.log(monitor.progress);
	}

	handler.handleResponse = function(response) {
		if (response.response && response.response.features) {
			// Received features chunk
			features = features.concat(response.response.features);
			draw();
			// If the backend sends an empty list, all features have been loaded.
			// Otherwise we request more
			if (response.response.features.length > 0) {
				spectrumView.initRequest({});
			}
		} else {
			// Received intensity points
			olTopRight.text("");
			// Sort so points with high intensity are on top of ones with lower intensity
			points = response.response.points
				.sort(function(a,b) {
					return a.intensity - b.intensity;
				});

			// log and normalize
			let min = Number.POSITIVE_INFINITY;
			let max = Number.NEGATIVE_INFINITY;
			for (let p of points) {
				p.intensity = Math.log(p.intensity);
				min = Math.min(p.intensity, min);
				max = Math.max(p.intensity, max);
			}
			for (let p of points) {
				p.intensity = (p.intensity - min) / (max - min);
			}
			draw();
		}
	}

	handler.handleError = function(error) {
		console.log(error);
	}

	handler.validate = function() {
		return true;
	}

	handler.setValidationError = function() {}

	handler.getComponentValue = function() {
		return _value;
	}

	return handler;

}();
