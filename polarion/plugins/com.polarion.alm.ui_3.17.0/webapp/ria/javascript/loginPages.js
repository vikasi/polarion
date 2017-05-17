function hideCompanyLogo(){
	var siemensLogo=document.getElementById("siemensLogo");
	var companyLogo=document.getElementById("companyLogo");
	var companyAndContainer=document.getElementById("companyAndContainer");
	
	if(companyLogo){
		var float=document.defaultView.getComputedStyle(companyLogo,'').getPropertyValue('float');
		var distance=getDistance(companyLogo,siemensLogo);
		if(distance < 10){
			if(float == "left"){
				companyLogo.style.cssFloat="right";
				if(getDistance(companyLogo,siemensLogo) > 10){
					companyLogo.style.visibility="visible";
				}
			}else{
				companyLogo.style.visibility="hidden";
			}
		}else{
			companyLogo.style.visibility="visible";
			if(float == "right" && (getDistance(companyAndContainer,siemensLogo) > 10)){
				companyLogo.style.cssFloat="left";
			}
			
		}		
	}
}

function getDistance(leftElement,rightElement){
	return leftElement.getBoundingClientRect().left - rightElement.getBoundingClientRect().right;
}