/*
 * Copyright (C) 2004-2014 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2014 Polarion Software
 * All Rights Reserved.  No use, copying or distribution of this
 * work may be made except in accordance with a valid license
 * agreement from Polarion Software.  This notice must be
 * included on all copies, modifications and derivatives of this
 * work.
 *
 * POLARION SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES 
 * ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. POLARION SOFTWARE
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.polarion.example.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.polarion.alm.projects.IProjectService;
import com.polarion.alm.projects.model.IProject;
import com.polarion.alm.projects.model.IUser;
import com.polarion.alm.tracker.ITrackerService;
import com.polarion.alm.tracker.model.ITimePoint;
import com.polarion.alm.tracker.model.IWorkItem;
import com.polarion.core.util.types.DateOnly;
import com.polarion.platform.announce.Announcement;
import com.polarion.platform.announce.IAnnouncerService;
import com.polarion.platform.context.IContext;
import com.polarion.platform.core.PlatformContext;
import com.polarion.platform.jobs.GenericJobException;
import com.polarion.platform.jobs.IJobDescriptor;
import com.polarion.platform.jobs.IJobStatus;
import com.polarion.platform.jobs.IJobUnit;
import com.polarion.platform.jobs.IJobUnitFactory;
import com.polarion.platform.jobs.IProgressMonitor;
import com.polarion.platform.jobs.spi.AbstractJobUnit;
import com.polarion.platform.jobs.spi.BasicJobDescriptor;
import com.polarion.platform.jobs.spi.JobParameterPrimitiveType;
import com.polarion.platform.jobs.spi.SimpleJobParameter;

/**
 * @author Polarion Software
 */
public class OverdueJobUnitFactory implements IJobUnitFactory {

    /**
     * Create new job unit implementation
     * @param name
     * @see com.polarion.platform.jobs.IJobUnitFactory#createJobUnit(java.lang.String)
     */
    public IJobUnit createJobUnit(String name) throws GenericJobException {
        return new OverdueJobUnit(name, this);
    }

    /**
     * Job Descriptor define parameters for job 
     * @param jobUnit
     * @see com.polarion.platform.jobs.IJobUnitFactory#getJobDescriptor(com.polarion.platform.jobs.IJobUnit)
     */
    public IJobDescriptor getJobDescriptor(IJobUnit jobUnit) {
        BasicJobDescriptor desc = new BasicJobDescriptor("Overdue job", jobUnit);
        JobParameterPrimitiveType stringType = new JobParameterPrimitiveType(
                "String",
                String.class);
        JobParameterPrimitiveType integerType = new JobParameterPrimitiveType(
                "Integer",
                Integer.class);
        desc.addParameter(new SimpleJobParameter(
                desc.getRootParameterGroup(),
                "query",
                "Query for workitems which will be calculated",
                stringType));
        desc.addParameter(new SimpleJobParameter(
                desc.getRootParameterGroup(),
                "sort",
                "Sort order for query",
                stringType));
        desc.addParameter(new SimpleJobParameter(
                desc.getRootParameterGroup(),
                "notificationSubjectPrefix",
                "Notification Subject Prefix",
                stringType));
        desc.addParameter(new SimpleJobParameter(
                desc.getRootParameterGroup(),
                "notificationSender",
                "Notification Sender",
                stringType));
        desc.addParameter(new SimpleJobParameter(
        		desc.getRootParameterGroup(),
        		"notificationRecipients",
        		"Notification Recipients (author or email addresses with comma delimiter)",
        		stringType));
        desc.addParameter(new SimpleJobParameter(
        		desc.getRootParameterGroup(),
        		"allowedDelay",
        		"Allowed delay in days (used with following formula: currentDate > plannedDate + allowedDelay)",
        		integerType));
        desc.addParameter(new SimpleJobParameter(
        		desc.getRootParameterGroup(),
        		"planningConstraint",
        		"Defines planning constraint (arguments: timepoint, plannedTo, dueDate; default is timepoint)",
        		stringType));
        return desc;
    }

    /**
     * Returns name for job implementation
     * @see com.polarion.platform.jobs.IJobUnitFactory#getName()
     */
    public String getName() {
        return IOverdueJobUnit.JOB_NAME;
    }
    
    private final class OverdueJobUnit extends AbstractJobUnit implements IOverdueJobUnit {
    	
    	private static final String PLANNING_TIMEPOINT = "timepoint";
    	private static final String PLANNING_PLANNEDTO = "plannedTo";
    	private static final String PLANNING_DUEDATE = "dueDate";
    	
    	private static final String NOTIFICATION_ASSIGNEE = "assignee";
        private static final String NOTIFICATION_DEFAULT_SENDER = "polarion@example.com";

    	private String query;
    	private String sort;
        private String notificationSubjectPrefix;
        private String notificationSender;
        private String notificationRecipients;
        private int allowedDelay = 0;
        private String planningConstraint;
    	
        /**
         * Implementation of job
         * @param name
         * @param creator
         */
        public OverdueJobUnit(String name, IJobUnitFactory creator) {
            super(name, creator);
        }
        
        /**
         * Query, which select workitems for calculation
         * @param project
         */
        public void setQuery(String query) {
        	this.query = query;
        }
        
        /**
         * Sort parameter for workitem query
         * @param sort
         */
        public void setSort(String sort) {
        	this.sort = sort;
        }
        
        /**
         * Prefix for notification email
         * @param notificationSubjectPrefix
         */
        public void setNotificationSubjectPrefix(String notificationSubjectPrefix) {
        	this.notificationSubjectPrefix = notificationSubjectPrefix;
        }
        
        /**
         * Sender email address
         * @param notificationSender
         */
        public void setNotificationSender(String notificationSender) {
        	this.notificationSender = notificationSender;
        }
        
        /**
         * Comma separated list of recipient emails or 'author' constant 
         * @param notificationRecipients
         */
        public void setNotificationRecipients(String notificationRecipients) {
        	this.notificationRecipients = notificationRecipients;
        }
        
        /**
         * Allowed delay in days used for following formula: dueDate = plannedDate + allowedDelay
         * @param allowedDelay
         */
        public void setAllowedDelay(Integer allowedDelay) {
        	this.allowedDelay = allowedDelay;
        }
        
        /**
         * Planning constraint parameter
         * @param planningConstraint
         */
        public void setPlanningConstraint(String planningConstraint) {
        	this.planningConstraint = planningConstraint;
        }
        
        /**
         * The main job method
         * @param progress
         */
        @SuppressWarnings("unchecked")
        protected IJobStatus runInternal(IProgressMonitor progress) {
            IProjectService projectService = (IProjectService) PlatformContext.getPlatform().lookupService(IProjectService.class);
            ITrackerService trackerService = (ITrackerService) PlatformContext.getPlatform().lookupService(ITrackerService.class);
            IContext scope = getScope();
            
            progress.beginTask(getName(), 0);
            try {
                IProject project = projectService.getProjectForContextId(scope.getId());
                if (project == null) {
                    return getStatusFailed("Scope '" + scope.getId() + "' is not project.", null);
                }
                getLogger().info("Scope is '" + scope.getId() + "', query is '" + query + "', sort is '" + sort + "'");
                Collection<IWorkItem> workItems = (Collection<IWorkItem>) trackerService.queryWorkItems(project, query, sort);
                processWorkItems(workItems);
                return getStatusOK(null);
            } finally {
                progress.done();
            }
        }
        
        /**
         * Collects delayed workitems
         * @param workItems
         */
        private void processWorkItems(Collection<IWorkItem> workItems) {
        	Collection<IWorkItem> delayedWorkItems = new LinkedList<IWorkItem>();
        	for (IWorkItem workItem: workItems) {
            	if (checkConstraint(workItem)) {
            		delayedWorkItems.add(workItem);
            	}
            }
        	processNotifications(delayedWorkItems);
        }
        
        /**
         * Global handler for planning constraint
         * @param workItem
         */
        private boolean checkConstraint(IWorkItem workItem) {
        	if (planningConstraint == null || PLANNING_TIMEPOINT.equalsIgnoreCase(planningConstraint)) {
        		return checkTimePoint(workItem);
        	} else if (PLANNING_DUEDATE.equalsIgnoreCase(planningConstraint)) {
        		return checkDueDate(workItem);
        	} else if (PLANNING_PLANNEDTO.equalsIgnoreCase(planningConstraint)) {
        		return checkPlannedTo(workItem);
        	} else {
        		getLogger().info("Incorrect planning constraint.");
        		return false;
        	}
        }
        
        /**
         * Check for overdued workitems based on timepoint field
         * @param workItem
         */
        private boolean checkTimePoint(IWorkItem workItem) {
        	ITimePoint timepoint = workItem.getTimePoint();
        	if (timepoint == null || timepoint.getTime() == null) {
        		getLogger().info("WorkItem '" + workItem.getId() + "' has no timepoint defined. Will be skipped.");
        		return false;
        	}
        	Date timepointDate = timepoint.getTime().getDate();
        	long diff = getTimeDiff(getMillisFromDate(timepointDate), System.currentTimeMillis());
        	return diff < 0;
        }
        
        /**
         * Check for overdued workitems based on dueDate field
         * @param workItem
         */
        private boolean checkDueDate(IWorkItem workItem) {
        	DateOnly dueDateOnly = workItem.getDueDate();
        	if (dueDateOnly == null || dueDateOnly.getDate() == null) {
        		getLogger().info("WorkItem '" + workItem.getId() + "' has no dueDate defined. Will be skipped.");
        		return false;
        	}
        	Date dueDate = dueDateOnly.getDate();
        	long diff = getTimeDiff(getMillisFromDate(dueDate), System.currentTimeMillis());
        	return diff < 0;
        }
        
        /**
         * Check for overdued workitems based on plannedTo field
         * @param workItem
         */
        private boolean checkPlannedTo(IWorkItem workItem) {
        	Date plannedEnd = workItem.getPlannedEnd();
        	if (plannedEnd == null) {
        		getLogger().info("WorkItem '" + workItem.getId() + "' has no plannedEnd defined. Will be skipped.");
        		return false;
        	}
        	long diff = getTimeDiff(getMillisFromDate(plannedEnd), System.currentTimeMillis());
        	return diff < 0;
        }
        
        /**
         * Computes difference between two timestamps
         * @param time1
         * @param time2
         */
        private long getTimeDiff(long time1, long time2) {
        	return (time1 + getAllowedDelay()) - time2;
        }
        
        /**
         * Returns a allowed delay in milliseconds
         * @return
         */
        private long getAllowedDelay() {
        	return allowedDelay * 24 * 60 * 60 * 1000;
        }
        
        /**
         * Returns number of milliseconds from date
         * @param date
         */
        private long getMillisFromDate(Date date) {
        	return date.getTime();
        }
        
        /**
         * Collects delayed workitems and sends them via email 
         * @param delayedWorkItems
         */
        private void processNotifications(Collection<IWorkItem> delayedWorkItems) {
        	Map<String, StringBuilder> emails = new HashMap<String, StringBuilder>();
        	for (IWorkItem workItem: delayedWorkItems) {
        		Collection<String> recipients = getRecipients(workItem);
        		StringBuilder content = new StringBuilder();
        		StringBuilder contentForLog = new StringBuilder();
        		contentForLog.append("WorkItem '").append(workItem.getId()).append("'")
        			         .append(" in project '").append(workItem.getProject().getName()).append("'")
        			         .append(" is not resolved after due date.");
        		getLogger().info(contentForLog.toString());
        		content.append("<span>").append(contentForLog).append("</span><br/>\n");
        		for (String email: recipients) {
        			StringBuilder contentBuilder = emails.get(email);
        			if (contentBuilder == null) {
        				contentBuilder = new StringBuilder();
        				emails.put(email, contentBuilder);
        			}
        			contentBuilder.append(content);
        		}
        	}
        	for (String recipient: emails.keySet()) {
        		sendNotification(recipient, emails.get(recipient));
        	}
        }
        
        /**
         * Sends notification via Polarion Annoucements API
         * @param recipient
         * @param content
         */
        private void sendNotification(String recipient, StringBuilder content) {
        	IAnnouncerService announcerService = (IAnnouncerService) PlatformContext.getPlatform().lookupService(IAnnouncerService.class);

            content = content != null ? content : new StringBuilder();
            String subjectPrefix = notificationSubjectPrefix != null ? notificationSubjectPrefix : "";
            subjectPrefix = subjectPrefix.length() > 0 ? subjectPrefix + " ": subjectPrefix;
            
            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append("<html><body><b>Overdue WorkItems:</b><br/><br/>\n")
            			  .append("<p>\n").append(content.toString()).append("</p>\n")
            			  .append("</body></html>");
            
            
            Announcement announcement = new Announcement();
            announcement.setSender(notificationSender != null && isMailAddress(notificationSender) ? notificationSender : NOTIFICATION_DEFAULT_SENDER);
            announcement.setReceivers(new String[] {recipient});
            announcement.setContentType("text/html");
            announcement.setSubject(subjectPrefix + "Overdue job notification");
            announcement.setContent(contentBuilder.toString());
            try {
            	announcerService.sendAnnouncement(IAnnouncerService.SMTP_TRANSPORT, announcement);
            } catch (Exception ex) {
            	getLogger().error("Could not send notification. Please, check mail settings for Polarion.");
            }
        }
        
        /**
         * Returns recipients for list of overdued workitems
         * @param workItem
         */
        @SuppressWarnings("unchecked")
        private Collection<String> getRecipients(IWorkItem workItem) {
        	Collection<String> rcpts = new ArrayList<String>();
        	if (notificationRecipients == null || NOTIFICATION_ASSIGNEE.equalsIgnoreCase(notificationRecipients)) {
               Collection<IUser> assignees = (Collection<IUser>) workItem.getAssignees();
               for (IUser assignee: assignees) {
            	   if (assignee != null && !"".equals(assignee.getEmail()) && isMailAddress(assignee.getEmail())) {
            		   rcpts.add(assignee.getEmail());
            	   }
               }
        	} else {
            	String[] recipients = notificationRecipients.split(",");
                for (String recipient: recipients) {
                    recipient = recipient.trim(); 
                    if (recipient.length() > 0 && isMailAddress(recipient)) {
                        rcpts.add(recipient);
                    }
                }
            }
            return rcpts;
        }
        
        /**
         * Check for valid email address
         * @param address
         */
        private boolean isMailAddress(String address) {
        	Pattern emailPattern = Pattern.compile("\\b([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4})\\b");
            Matcher matcher = emailPattern.matcher(address);
            return matcher.find();
        }

    }

}
