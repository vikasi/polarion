addEvent(window, 'load', init); // register init method on page load
addEvent(window, 'focus', refresh); 

function init() {
	setTarget();
    createPlaceholders();
    
    var warning = document.getElementById('warning');
    if (warning != null) { // it is null for internal login
    	checkFlashPlugin();
    	checkLoginDisplayedInWA();
    	registerWarningHandlers();
    }
    
    document.getElementById('j_username').focus();
}

function refresh() {
		if (typeof document.visibilityState === 'undefined' || document.visibilityState === 'hidden') {
			return;
		}
		var loc = getTarget();
		$.ajax({
	        type: 'POST',
	        url: loc,
	        data: null,
	        dataType: 'html',
	        success: function (data, textStatus, request) {
	        	var allowOrigin = request.getResponseHeader('Access-Control-Allow-Origin');
	        	var authHeader = request.getResponseHeader('X-com-ibm-team-repository-web-auth-msg');
	        	if (authHeader == null && allowOrigin == null) {
	        		window.location = loc;
	        	}
	        }
		});
}

function getTarget() {
	var loc = document.getElementById('target').value;
	if (loc == null || loc.indexOf('/') != 0) {
		loc = '/polarion/';
	}
	return loc;
}

function setTarget() {
	var loc = window.location.pathname + window.location.search + window.location.hash;
	
	var hash = window.location.hash; 
	if (loc != null && loc.indexOf("/polarion/#/") != -1 && hash != null && hash.length != 0) { 
		loc = loc.replace(hash, "redirect" + hash.substring(1)); // use redirect servlet for portal links 
	} 
	
	if (document.getElementById('target').value === '') {
		document.getElementById('target').value = loc;
	}
	
	refresh();
}

function addEvent(element, event, func) {
    var callback = function(event) {
        // transform target for IE8 and lower that we use same target in every browser
        target = (event.currentTarget) ? event.currentTarget : event.srcElement;
        func.call(target);
    };
    if (element.addEventListener) {
        element.addEventListener(event, callback, false);
    } else if (element.attachEvent) {
        element.attachEvent('on' + event, callback);
    } else {
        element['on' + event] = callback;
    }
}

function createPlaceholders() {
    if (isPlaceholderSupported()) {
        var usernamePlaceholder = document.getElementById('username').children[0];
        document.getElementById('j_username').placeholder = usernamePlaceholder.innerHTML;
        var passwordPlaceholder = document.getElementById('password').children[0];
        document.getElementById('j_password').placeholder = passwordPlaceholder.innerHTML;
    } else {
        if (isTimerNeeded()) {
            setInterval(check, 100);
        }
        var containers = ['username', 'password'];
        for (var i in containers) {
            var container = document.getElementById(containers[i]);
            var placeholder = container.children[0];
            placeholder.className = ''; // make it visible
            var input = container.children[1];
            placeholder.input = input;
            input.pl = placeholder;
            addEvent(placeholder, 'click', function() { check(); this.input.focus(); });
            addEvent(input, 'change', check);
            addEvent(input, 'propertychange', check);
            addEvent(input, 'focus', check);
            addEvent(input, 'blur', function() {  if (this.value == '') this.pl.className = ''; });
            addEvent(input, 'keydown', function() { check(); this.pl.className = 'hidden';});
            addEvent(input, 'keyup', function() { check(); if (this.value == '') this.pl.className = ''; });
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
    var username = document.getElementById('username');
    var password = document.getElementById('password');
    if (username.children[1].value != '') {
        username.children[0].className = 'hidden';
    }
    if (password.children[1].value != '') {
        password.children[0].className = 'hidden';
    }
}

function registerWarningHandlers() {
    var moreinfo = document.getElementById('moreinfo');
    addEvent(moreinfo, 'click', function() { 
                    document.getElementById('warning').className = '';
                    document.getElementById('warningInfo').className = 'hidden';
     });
    var lessinfo = document.getElementById('lessinfo');
    addEvent(lessinfo, 'click', function() { 
                    document.getElementById('warning').className = 'less';
                    document.getElementById('warningInfo').className = '';
    });
}

function login() {
	var loginForm = document.getElementById('logInForm');
	if (loginForm != null && loginForm.action != null && loginForm.action.indexOf('#') == -1) {
		loginForm.action += window.location.hash;
	}
    switchLoginLabel();
    return true;
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

function checkFlashPlugin() {
    if (window.top == window.self) {
        checkFlash(null);
    }
    var warning = document.getElementById('warning');
    if (warning.getElementsByTagName('li').length != 1) {
        document.getElementById('warning').className = 'less';
    }
}

function checkLoginDisplayedInWA() {
    try {
        if (parent != null && parent.loginDisplayedInWA != null) {
            parent.loginDisplayedInWA();
        }
    } catch (e) {
        // nothing to do
    }
}
