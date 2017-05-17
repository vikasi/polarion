function OSLC_delegatedUICallback(data) {
	if (!data) {
		return;
	}

	var oslc_callback_protocol = decodeURIComponent(data.params.oslc_callback_protocol);

	if (data.action == 'cancel') {
		OSLC_sendCancelResponse(oslc_callback_protocol);
	} else if (data.action == 'create' || data.action == 'select') {
		if (!window.location.origin) {
			  window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port: '');
		}
		var resourceUrl = window.location.origin + '/polarion/oslc/services/projects/' + data.projectId + '/workitems/' + data.itemId;
		OSLC_sendResponse(data.itemId, resourceUrl, oslc_callback_protocol);
	}
}

function OSLC_sendResponse(label, resourceUrl, oslc_callback_protocol) {
	var oslcResponse = 'oslc-response:{ "oslc:results": [ '
			+ ' { "oslc:label" : "' + label + '", "rdf:resource" : "'
			+ resourceUrl + '"} ' + ' ] }';
	OSLC_send(oslc_callback_protocol, oslcResponse);

}

function OSLC_sendCancelResponse(oslc_callback_protocol) {
	OSLC_send(oslc_callback_protocol, 'oslc-response:{ "oslc:results": [ ]}');
}

function OSLC_send(oslc_callback_protocol, oslcResponse) {
	if (oslc_callback_protocol == '#oslc-core-windowName-1.0') {
		// Window Name protocol in use
		respondWithWindowName(oslcResponse);
	} else {
		// default to Post Message protocol
		respondWithPostMessage(oslcResponse);
	}
}

function respondWithWindowName(/* string */response) {
	var returnURL = window.name;
	window.name = response;
	window.location.href = returnURL;

}

function respondWithPostMessage(/* string */response) {
	if (window.parent != null) {
		window.parent.postMessage(response, "*");
	} else {
		window.postMessage(response, "*");
	}
}
