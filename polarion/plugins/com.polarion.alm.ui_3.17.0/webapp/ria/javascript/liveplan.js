function ShowTask(sTaskCaption, sTaskUrl, sTaskProject, sTaskId){
			 
			  publishFromFrame("live_plan_flash_show_wi", [sTaskProject, sTaskId]);
			    return 1;
			}
			function resourceClicked(sResourceUrl, blsProject){
			    publishFromFrame("live_plan_flash_resource_clicked", [sResourceUrl, blsProject]);
			    return 1;
			}
			function timePointClicked(sTimePointId){
			    publishFromFrame("live_plan_flash_timepoint_clicked", sTimePointId);
			    return 1;
			}
			function contentHeightChanged(sNewContentHeight){
			    publishFromFrame("live_plan_flash_content_height_changed", sNewContentHeight);
			    return 1;
			}