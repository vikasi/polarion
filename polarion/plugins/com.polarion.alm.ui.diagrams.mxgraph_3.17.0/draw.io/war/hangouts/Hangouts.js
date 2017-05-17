gapi.hangout.onApiReady.add(function(eventObj) 
{
	var fileLoaded = false;
	var editorFrame = document.getElementById('editorFrame');
	var editorURL = 'https://drive.draw.io';
	
	var hangoutsObj = {};
	hangoutsObj.parentIframeDomain = window.location.origin;//send the origin for postMessage
	hangoutsObj.userID = gapi.hangout.getLocalParticipant().person.id;//and send the user ID for auto account selection
	
	if(gapi.hangout.data.getValue('mxFileId') != null) //some already set the editor URL so use that one
	{
		editorFrame.src = editorURL + '/?' + gapi.hangout.data.getValue('mxFileId'); 
	}
	
	if(editorFrame.src == '') //initial state, URL is updated later when we get a fileID
	{
		
		editorFrame.src = editorURL + '/?hangouts=' + encodeURIComponent(JSON.stringify(hangoutsObj));// + '&dev=1';
	}
	
	//receive a message from draw.io iframe that contains URL with a file ID in it
	function receiveMessage(event) 
	{
		if (event.origin == editorURL && event.data.indexOf('fileId') != -1)
		{ 
			if(event.data != gapi.hangout.data.getValue('mxFileId'))
			{
				gapi.hangout.data.setValue('mxFileId', event.data);
			}
		}
	};
	
	gapi.hangout.data.onStateChanged.add(function(event)//editor URL has changed for hangout initiator and now it has a fileID in it 
	{
		for(var i=0;i< event.addedKeys.length;i++) 
		{
			var obj = event.addedKeys[i];
			if(obj.key == 'mxFileId' && obj.value.indexOf('fileId=') != -1) 
			{
				var url = editorURL + '/?' + obj.value + '&hangouts=' + encodeURIComponent(JSON.stringify(hangoutsObj));
				if(editorFrame.src != null) 
				{
					editorFrame.src = url;
				}
				break;
			}
		}
	});

	window.addEventListener("message", receiveMessage, false);
});