/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *  
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *  
 *  Contributors:
 *  
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
require([ "dojo/dom", "dojo/dom-construct", "dojo/on", "dojo/_base/event",
		"dojo/_base/xhr", "dojo/ready" ], function(dom, domConstruct, on,
		event, xhr, ready) {

	function init() {
	    createPlaceholders();
	    document.getElementById('id').focus();
	}

	function createPlaceholders() {
	    if (isPlaceholderSupported()) {
	        var usernamePlaceholder = document.getElementById('usernameDiv').children[0];
	        document.getElementById('id').placeholder = usernamePlaceholder.innerHTML;
	        var passwordPlaceholder = document.getElementById('passwordDiv').children[0];
	        document.getElementById('passwordInput').placeholder = passwordPlaceholder.innerHTML;
	    } else {
	        if (isTimerNeeded()) {
	            setInterval(check, 100);
	        }
	        var containers = ['usernameDiv', 'passwordDiv'];
	        for (var i in containers) {
	            var container = document.getElementById(containers[i]);
	            var placeholder = container.children[0];
	            placeholder.className = ''; // make it visible
	            var input = container.children[1];
	            placeholder.input = input;
	            input.pl = placeholder;
	            placeholder.addEventListener("click", function() { check(); this.input.focus(); }, false);
	            input.addEventListener('change', check, false);
	            input.addEventListener('propertychange', check, false);
	            input.addEventListener('focus', check, false);
	            input.addEventListener('blur', function() {  if (this.value == '') this.pl.className = ''; }, false);
	            input.addEventListener('keydown', function() { check(); this.pl.className = 'hidden';}, false);
	            input.addEventListener('keyup', function() { check(); if (this.value == '') this.pl.className = ''; }, false);
	            if (input.value != '') {
	                placeholder.className = 'hidden';
	            }
	        }
	    }
	}

	function isPlaceholderSupported() {
	    var ua = navigator == null ? '' : navigator.userAgent.toLowerCase();
	    return ua.indexOf('webkit') != -1 || (ua.indexOf('gecko') != -1 && ua.indexOf("trident/") == -1) || ua.indexOf("opera") != -1;
	}

	function isTimerNeeded() {
	    var ua = navigator == null ? '' : navigator.userAgent.toLowerCase();
	    return ua.indexOf('msie') != -1 && (ua.indexOf('trident/6.0') != -1 || ua.indexOf('trident/5.0') != -1);
	}

	function check() {
	    var username = document.getElementById('usernameDiv');
	    var password = document.getElementById('passwordDiv');
	    if (username.children[1].value != '') {
	        username.children[0].className = 'hidden';
	    }
	    if (password.children[1].value != '') {
	        password.children[0].className = 'hidden';
	    }
	}

	function switchLoginLabel() {
	    var submitMsg = document.getElementById('submitMsg');
	    var submitButton = document.getElementById('submitButton');
	    if (submitMsg != null && submitButton != null) {
	        var current = submitButton.value;
	        submitButton.value = submitMsg.innerHTML;
	        submitMsg.innerHTML = current;
	    }
	}

	function encode() {
	    document.getElementById('password').value = encodeURIComponent(document.getElementById('passwordInput').value);
	}
	
	function showError(message) {
		var errorNode = dom.byId('error');
		if (message) {
			domConstruct.empty(errorNode);
			errorNode.appendChild(document.createTextNode(message));
		} else {
			errorNode.innerHTML = 'An error occurred.';
		}
		errorNode.style.display = 'block';
	}

	function returnToConsumer() {
		var callback = dom.byId('callback').value;
		if (callback) {
			window.location = callback;
		} else {
			dom.byId('container').innerHTML =
				'<div class="message">Request authorized. Close the browser window to continue.</div>';
		}
	}
	
	function submit() {
		switchLoginLabel();
		encode();
		xhr.post({
			url : 'adminLogin',
			form : 'loginForm',
			headers : {
				'X-CSRF-Prevent': lyoOAuthConfig.csrfPrevent
			},
			load : function() {
				returnToConsumer();
			},
			error : function(error, ioArgs) {
				switchLoginLabel();
				showError(ioArgs.xhr.responseText);
			}
		});
	}

	ready(function() {
		
		init();
		
		on(dom.byId('loginForm'), 'submit', function(e) {
			event.stop(e);
			submit();
		});
	});
});