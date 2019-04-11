if (spectrumView) {
	spectrumView.initRequest = function(request) {

		/* this is where the magic happens */
		var promise = knimeService.requestViewUpdate(request, false);
		promise.progress(monitor => spectrumView.handleProgress(monitor))
			.then(response => spectrumView.handleResponse(response))
			.catch(error => spectrumView.handleError(error));
		/* end magic */
	}
}
