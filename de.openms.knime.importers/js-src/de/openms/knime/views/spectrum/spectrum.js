spectrumView = function() {

<<<<<<< HEAD
	//Constants for the zoom mode
	const PAN_ZOOM_MODE = 0;
	const RECT_MODE = 1;

	// Constants for the feature coloring
	const COLOR_BY_INTENSITY = 0;
	const COLOR_BY_QUALITY = 1;

	// Diagonal size that the zoom rectangle needs to have to zoom
	const MIN_RECT_ZOOM_SIZE = 2;

	// Max. diagonal size the window needs to have to load intensities
	const DETAIL_SIZE = 500;

	const RT_MARGIN = 50;
	const MZ_MARGIN = 25;
=======
	var handler = {};
	var _representation, _value;
	var canvas;
	var ctx;
	var canvasWidth = 500;
	var canvasHeight = 500;
	var mzScale, rtScale;
	var features = [];
	var olTopRight;
	var rtMult = 1;
	var mzMult = 1;
	var curTransform = d3.zoomIdentity;
	var _zoom;
	var points = [];
	var pointsTimeout = null;
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6

	const MAX_POINT_SIZE = 6;
	const MIN_POINT_SIZE = 2;
	const SCALE_THRESHOLD = 5;

<<<<<<< HEAD
	let handler = {};
	let _representation, _value;
	let canvas;
	let ctx;
	let canvasWidth = 500;
	let canvasHeight = 500;
	let mzScale, rtScale;
	let features = [];
	let olTopRight;
	let selected = {};
	let curTransform = d3.zoomIdentity;
	let _zoom;
	let points = [];
	let pointsTimeout = null;
	let zoomRect = null;
	let viewRect = null;
	let mode = PAN_ZOOM_MODE;
	let colorMode = COLOR_BY_INTENSITY;

	// Color scale for features and points
	const colorScale = d3.scaleLinear()
    .domain([0, 0.125, 0.25, 0.5, 0.625, 0.75, 0.875, 1])
    .range(["#1a9850", "#66bd63", "#a6d96a", "#d9ef8b",
						"#fee08b", "#fdae61", "#f46d43", "#d73027"])
    .interpolate(d3.interpolateHcl);

	// Creates the controls in the top right corner
	function createControls() {
		// Reset view button
		if (_representation.panAndZoomAllowed || _representation.rectZoomAllowed) {
			knimeService.addButton("scatter-zoom-reset-button", "search-minus", "Reset View", resetTransform);
		}
		// Selection subscription handling
		knimeService.addMenuItem(
      "Subscribe to selection",
      knimeService.createStackedIcon("check-square-o", "angle-double-right", "faded right sm", "left bold"),
      knimeService.createMenuCheckbox("subscribeSelectionCheckbox", _value.subscribeToSelection,
        function () {
            _value.subscribeToSelection = this.checked;
            toggleSubscribeToSelection();
        })
    );

		if (_representation.panAndZoomAllowed && _representation.rectZoomAllowed) {
			const zoomOptions = ["Pan and Zoom", "Rectangle"];
			const modeRadios = knimeService.createInlineMenuRadioButtons(
				"zoom-mode", "zoom-mode", mode === PAN_ZOOM_MODE ? zoomOptions[0] : zoomOptions[1],
				zoomOptions, toggleMode);
			knimeService.addMenuItem("Zoom Mode", null, modeRadios);
		}

		const colorOptions = ["Intensity", "Quality"];
		const colorRadios = knimeService.createInlineMenuRadioButtons(
			"color-mode", "color-mode", colorMode === COLOR_BY_INTENSITY ? colorOptions[0] : colorOptions[1],
			colorOptions, toggleColorMode);
		knimeService.addMenuItem("Color Mode", null, colorRadios);
	}

	function toggleColorMode() {
		colorMode = 1 - colorMode;
		draw();
	}

	function toggleMode() {
		mode = 1 - mode;
		if (mode === PAN_ZOOM_MODE) {
			disableRect();
			enableZoom();
		} else {
			disableZoom();
			enableRect();
		}
=======
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
                .range(['#1a9850', '#66bd63', '#a6d96a', '#d9ef8b',
												'#fee08b', '#fdae61', '#f46d43', '#d73027'])
                .interpolate(d3.interpolateHcl);

	// Creates the controls in the top right corner
	function createControls() {
		knimeService.addButton('scatter-zoom-reset-button', 'search-minus', 'Reset Zoom', resetTransform);
	}

	// Finds a feature that contains the given point
	function getFeatureAtPoint(pt) {
		const tpt = curTransform.invert(pt);
		const mz = mzScale.invert(tpt[0]);
		const rt = rtScale.invert(tpt[1]);
		for (let f of features) {
			if (mz >= f.mzStart && mz <= f.mzEnd &&
					rt >= f.rtStart && rt <= f.rtEnd) {
				return f;
			}
		}
		return null;
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
	}

	// Given a point on the canvas, calculates the corresponding mz and rt
	function invertPoint(pt) {
		const tpt = curTransform.invert(pt);
<<<<<<< HEAD
		const mz = mzScale.invert(tpt[1]);
		const rt = rtScale.invert(tpt[0]);
		return [rt, mz];
=======
		const mz = mzScale.invert(tpt[0]);
		const rt = rtScale.invert(tpt[1]);
		return [mz, rt];
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
	}

	// Resets pan and zoom to default
	function resetTransform() {
<<<<<<< HEAD
		if (_zoom) {
			canvas.call(_zoom.transform, d3.zoomIdentity);
		}
		zoomRect = null;
		viewRect = {x: _representation.minRt,
								y: _representation.minMz,
								w: _representation.maxRt - _representation.minRt,
								h: _representation.maxMz - _representation.minMz};
		updateCanvasSize();
		updateDetails();
		draw();
=======
		rtMult = 1;
		mzMult = 1;
		canvas.call(_zoom.transform, d3.zoomIdentity);
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
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
<<<<<<< HEAD
				pointsTimeout = null;
				olTopRight.text("Loading intensities...");
				const r = {
					type: "FEATURE_DATA",
					rtStart: Math.min(end[0], start[0]),
					rtEnd: Math.max(end[0], start[0]),
					mzStart: Math.min(start[1], end[1]),
					mzEnd: Math.max(start[1], end[1])
				};
				spectrumView.initRequest(r);
			}, 500);
		}
	}

	function enableZoom() {
		updateCanvasSize();
		updateDetails();
		_zoom = d3.zoom().on("zoom", zoom);
		canvas.call(_zoom);

		// Set transform to value given in the view value
		//const t = d3.zoomIdentity.translate(_value.mzPosition, _value.rtPosition).scale(_value.scale);
		//_zoom.transform(canvas, t);
	}

	function disableZoom() {
		canvas.on('.zoom', null);
	}

	function enableRect() {
		updateCanvasSize();
		updateDetails();
		canvas.on("mousedown", function() {
			const mp = d3.mouse(this);
			zoomRect = {x: mp[0], y: mp[1], w: 0, h: 0};
		});
		canvas.on("mousemove", function() {
			if (zoomRect) {
				const mp = d3.mouse(this);
				zoomRect.w = mp[0] - zoomRect.x;
				zoomRect.h = mp[1] - zoomRect.y;
				draw();
			}
		});
		canvas.on("mouseup", function() {
			const x1 = Math.min(zoomRect.x, zoomRect.x + zoomRect.w);
			const x2 = Math.max(zoomRect.x, zoomRect.x + zoomRect.w);
			const y1 = Math.min(zoomRect.y, zoomRect.y + zoomRect.h);
			const y2 = Math.max(zoomRect.y, zoomRect.y + zoomRect.h);

			const a = invertPoint([x1, y1]);
			const b = invertPoint([x2, y2]);
			const size = Math.min(Math.abs(b[0] - a[0]), Math.abs(b[1] - a[1]));
			zoomRect = null;
			if (size > MIN_RECT_ZOOM_SIZE) {
				// Reset pan and zoom. It will be done by scaling.
				if (_zoom) {
					_zoom.transform(canvas, d3.zoomIdentity);
				}
				viewRect = {x: a[0], y: b[1], w: b[0] - a[0], h: a[1] - b[1]};
				updateCanvasSize();
				updateDetails();
			} else {
				draw();
			}
		});
		canvas.on("mouseleave", function() {
			zoomRect = null;
			draw();
		});
	}

	function disableRect() {
		canvas.on("mousedown", null);
		canvas.on("mousemove", null);
		canvas.on("mouseup", null);
	}

	handler.init = function(representation, value) {
		if (!representation || typeof representation.minMz !== 'number' || !value) {
        return;
    }

		// Enable lazy loading
		if (knimeService.isViewRequestsSupported()) {
			knimeService.loadConditionally(["de/openms/knime/views/spectrum/spectrumLazyLoad"],
			function() {
				spectrumView.initRequest({type: "FEATURES"});
			});
		}

		_representation = representation;
		_value = value;

		mode = ((_value.zoomMode === "PAN_AND_ZOOM" || !_representation.rectZoomAllowed)
							&& _representation.panAndZoomAllowed)
						? PAN_ZOOM_MODE : RECT_MODE;
		colorMode = _value.colorMode === "INTENSITY" ? COLOR_BY_INTENSITY : COLOR_BY_QUALITY;

		// TODO: Check if _value.minMz is set
		if (_value.useCustomBounds) {
			viewRect = {x: _value.minRt,
									y: _value.minMz,
									w: _value.maxRt - _value.minRt,
									h: _value.maxMz - _value.minMz};
		} else {
			viewRect = {x: _representation.minRt,
									y: _representation.minMz,
									w: _representation.maxRt - _representation.minRt,
									h: _representation.maxMz - _representation.minMz};
		}
=======
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
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6

		const body = d3.select("body");
		// Avoid scrolling the document
		d3.selectAll("html, body, document")
			.style("width", "100%")
			.style("height", "100%")
			.style("overflow", "hidden");

<<<<<<< HEAD
=======
		_zoom = d3.zoom().scaleExtent([1, 8]).on("zoom", zoom);
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
		// Create main drawing area with zooming enabled
		canvas = body.append("canvas")
			.attr("id", "main-canvas")
			.style("width", "100%")
<<<<<<< HEAD
			.style("height", "100%");

		ctx = canvas.node().getContext("2d");
		if (mode === PAN_ZOOM_MODE) {
			enableZoom();
		} else if (mode === RECT_MODE && _representation.rectZoomAllowed) {
			enableRect();
		} else {
			updateCanvasSize();
			updateDetails();
		}
=======
			.style("height", "100%")
			.call(_zoom);

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
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6

		// Loading indicator
		olTopRight = body.append("div")
			.style("position", "absolute")
			.style("right", "10px")
			.style("top", "30px")
			.style("background-color", "#dddddd99")
			.style("padding", "3px")
			.style("border-radius", "3px");

		// React to resize by redrawing with proper ratios
		window.addEventListener("resize", updateCanvasSize);
		createControls();

<<<<<<< HEAD
		if (value.subscribeToSelection && knimeService.isInteractivityAvailable()) {
			subscribeToSelection();
		}
	}

	function toggleSubscribeToSelection() {
		if (_value.subscribeToSelection) {
	      subscribeToSelection();
	  } else {
	      unsubscibeFromSelection();
	  }
	}

	function unsubscibeFromSelection() {
		knimeService.unsubscribeSelection(_representation.tableId, selectionChanged);
	}

	function subscribeToSelection() {
		knimeService.subscribeToSelection(_representation.tableId, selectionChanged);
	}

	function selectionChanged(data) {
		if (data.changeSet) {
			if (data.changeSet.added) {
				for (let added of data.changeSet.added) {
					selected[added] = true;
				}
			}
			if (data.changeSet.removed) {
				for (let rem of data.changeSet.removed) {
					delete selected[rem];
				}
			}
			draw();
=======
		if (knimeService.isInteractivityAvailable()) {
			knimeService.subscribeToSelection(_representation.tableId, function(data) {
				alert("DATA!");
			});
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
		}
	}

	// Updates the canvas on a resize
	function updateCanvasSize() {
		const rect = canvas.node().getBoundingClientRect();
		canvasWidth = rect.width;
		canvasHeight = rect.height;
		canvas.attr("width", canvasWidth).attr("height", canvasHeight);
<<<<<<< HEAD
		if (viewRect) {
			rtScale = d3.scaleLinear()
									.domain([viewRect.x, viewRect.x + viewRect.w])
									.range([RT_MARGIN, canvasWidth - 10]);
			mzScale = d3.scaleLinear()
									.domain([viewRect.y, viewRect.y + viewRect.h])
									.range([canvasHeight - MZ_MARGIN, 0]);
		}
		draw();
	}

	function getSize() {
		const a = invertPoint([0, 0]);
		const b = invertPoint([canvasWidth - RT_MARGIN, canvasHeight - MZ_MARGIN]);
		return Math.max(Math.abs(b[0] - a[0]), Math.abs(b[1] - a[1]));
	}

	function updateDetails() {
		const size = getSize();
		if (size < DETAIL_SIZE) {
			loadFeatureData();
		} else {
			points = [];
		}
	}

=======
		mzScale = d3.scaleLinear()
								.domain([_representation.minMz, _representation.maxMz])
								.range([50, canvasWidth / mzMult]);
		rtScale = d3.scaleLinear()
								.domain([_representation.minRt, _representation.maxRt])
								.range([canvasHeight / rtMult - 25, 0]);
		draw();
	}

>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
	// d3 zoom callback
	function zoom() {
		curTransform = d3.event.transform;
		// If zoomed in enough, load the points of feature data
<<<<<<< HEAD
		updateDetails();
=======
		if (curTransform.k > SCALE_THRESHOLD) {
			loadFeatureData();
		}
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
		draw();
	}

	// Main drawing function
	function draw() {
		ctx.clearRect(0, 0, canvasWidth, canvasHeight);
		// Draw points
<<<<<<< HEAD
		drawPoints();
		drawFeatures();
		drawYAxis();
		drawXAxis();
		if (zoomRect) {
			const a = invertPoint([zoomRect.x, zoomRect.y]);
			const b = invertPoint([zoomRect.x + zoomRect.w, zoomRect.y + zoomRect.h]);
			const size = Math.min(Math.abs(b[0] - a[0]), Math.abs(b[1] - a[1]));

			ctx.strokeStyle = size > MIN_RECT_ZOOM_SIZE ? "#000" : "#f00";
			ctx.strokeRect(zoomRect.x, zoomRect.y, zoomRect.w, zoomRect.h);
		}
=======
		if (curTransform.k > SCALE_THRESHOLD) {
			drawPoints();
		}
		drawFeatures();
		drawYAxis();
		drawXAxis();
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
	}

	// Draw intensity points
	function drawPoints() {
		for (let pt of points) {
<<<<<<< HEAD
			const t = curTransform.apply([rtScale(pt.rt), mzScale(pt.mass)]);
=======
			const t = curTransform.apply([mzScale(pt.mass), rtScale(pt.rt)]);
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
			const i = pt.intensity;
			ctx.fillStyle = colorScale(i);
			const size = MIN_POINT_SIZE + i * (MAX_POINT_SIZE - MIN_POINT_SIZE);
			ctx.fillRect(t[0] - size / 2, t[1] - size / 2, size, size);
		}
	}

	// Draws feature rectangles
	function drawFeatures() {
		for (let feature of features) {
<<<<<<< HEAD
			const start = curTransform.apply([rtScale(feature.rtStart), mzScale(feature.mzStart)]);
			const end = curTransform.apply([rtScale(feature.rtEnd), mzScale(feature.mzEnd)]);
			const i = colorMode === COLOR_BY_INTENSITY ? feature.intensity : feature.quality;

			const a = invertPoint([0, 0]);
			const b = invertPoint([canvasWidth, canvasHeight]);
			const size = Math.max(Math.abs(b[0] - a[0]), Math.abs(b[1] - a[1]));

			if (size >= DETAIL_SIZE || !_representation.hasDB) {
				ctx.fillStyle = colorScale(i);
				ctx.fillRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
				ctx.strokeStyle = "#999";
				if (selected[feature.id]) {
=======
			const start = curTransform.apply([mzScale(feature.mzStart), rtScale(feature.rtStart)]);
			const end = curTransform.apply([mzScale(feature.mzEnd), rtScale(feature.rtEnd)]);
			const i = feature.intensity;
			if (curTransform.k <= SCALE_THRESHOLD) {
				ctx.fillStyle = colorScale(i);
				ctx.fillRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
				ctx.strokeStyle = "#999";
				if (feature.selected) {
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
					ctx.strokeStyle = "#000";
				}
				ctx.strokeRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
			} else {
				const color = colorScale(i);
<<<<<<< HEAD
				ctx.strokeStyle = color;
				const width = end[0] - start[0];
				const height = end[1] - start[1];
				ctx.strokeRect(start[0], start[1], width, height);
				if (selected[feature.id]) {
					ctx.strokeStyle = "#000";
					ctx.strokeRect(start[0] - 2, start[1] + 2, width + 4, height - 4);
				}
=======
				ctx.strokeStyle = feature.selected ? "#000" : color;
				ctx.strokeRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
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

<<<<<<< HEAD
			const mz = invertPoint([0, y])[1];
			ctx.fillText(Math.round(mz * 10) / 10, 5, y + 4);
=======
			const rt = invertPoint([0, y])[1];
			ctx.fillText(Math.round(rt * 100) / 100, 5, y + 4);
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
		}
	}

	function drawXAxis() {
		ctx.fillStyle = "#ffffff99";
		ctx.fillRect(0, canvasHeight - 30, canvasWidth, 30);
		ctx.beginPath();
<<<<<<< HEAD
		ctx.moveTo(RT_MARGIN, canvasHeight - MZ_MARGIN);
		ctx.lineTo(canvasWidth - 10, canvasHeight - MZ_MARGIN);
=======
		ctx.moveTo(50, canvasHeight - 25);
		ctx.lineTo(canvasWidth - 10, canvasHeight - 25);
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
		ctx.strokeStyle = "#000";
		ctx.stroke();

		ctx.fillStyle = "#000";
		const xStep = (canvasWidth - 60) / 9;
		for (let i = 0; i < 10; i++) {
			const x = 50 + i * xStep;
			ctx.moveTo(x, canvasHeight - 20);
			ctx.lineTo(x, canvasHeight - 30);
			ctx.stroke();

<<<<<<< HEAD
			const rt = invertPoint([x, 0])[0];
			ctx.fillText(Math.round(rt * 10) / 10, x - 25, canvasHeight - 10);
		}
	}
=======
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
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6

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
<<<<<<< HEAD
			if (getSize() < DETAIL_SIZE) {
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
			} else {
				points = [];
			}
=======
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
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
		}
	}

	handler.handleError = function(error) {
<<<<<<< HEAD
		console.error(error);
=======
		console.log(error);
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
	}

	handler.validate = function() {
		return true;
	}

	handler.setValidationError = function() {}

<<<<<<< HEAD
	function round4(x) {
		return Math.round(x * 10000) / 10000;
	}

	handler.getComponentValue = function() {
		const start = invertPoint([RT_MARGIN, 0]);
		const end = invertPoint([canvasWidth, canvasHeight - MZ_MARGIN]);
		return {..._value, useCustomBounds: true,
			zoomMode: mode === PAN_ZOOM_MODE ? "PAN_AND_ZOOM" : "RECTANGLE",
			colorMode: colorMode === COLOR_BY_INTENSITY ? "INTENSITY" : "QUALITY",
			minMz: round4(end[1]), maxMz: round4(start[1]),
			minRt: round4(start[0]), maxRt: round4(end[0])};
=======
	handler.getComponentValue = function() {
		return _value;
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
	}

	return handler;

}();
