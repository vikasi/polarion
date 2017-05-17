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
package com.polarion.example.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.polarion.alm.projects.model.IProject;
import com.polarion.alm.tracker.ITrackerService;
import com.polarion.alm.tracker.model.IWorkItem;
import com.polarion.core.util.types.duration.DurationTime;
import com.polarion.platform.core.PlatformContext;
import com.polarion.platform.persistence.model.IPObject;
import com.polarion.platform.persistence.model.IPObjectList;
import com.polarion.platform.security.ISecurityService;

/**
 * This is the servlet which counts workload of current user. The code here calculates the workload
 * which is then rendered by currentUserWorkload.jsp.
 * 
 * The time is count according to these rules:
 *          1) time to resolve work item (TTRWI) = remaining estimation time (RMT)
 *          2) if RMT is not set, then TTRWI = initial estimate time (IET)
 *          3) if IET is not set, then TTRWI = 1 day
 * 
 * @author Stepan Roh
 */
public class CurrentUserWorkloadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static class ProjectTimePair {
        public String projectName;
        public long time = 0;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        ITrackerService trackerService = (ITrackerService) PlatformContext.getPlatform().lookupService(ITrackerService.class);
        ISecurityService securityService = (ISecurityService) PlatformContext.getPlatform().lookupService(ISecurityService.class);

        long dayLength = trackerService.getPlanningManager().getOneDayLength();
        String currentUser = securityService.getCurrentUser();
        
        IPObjectList listOfPrjs = trackerService.getProjectsService().searchProjects("");

        List<ProjectTimePair> pairs = new ArrayList<ProjectTimePair>();
        
        Iterator<IPObject> it = listOfPrjs.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof IProject) {
                IProject prj = (IProject) obj;

                IPObjectList items = trackerService.queryWorkItems(prj,
                        "assignee.id:" + currentUser,
                        "remainingEstimate");
                if (items.size() > 0) {
                    ProjectTimePair pair = new ProjectTimePair();
                    pair.projectName = prj.getName();

                    Iterator<IPObject> it2 = items.iterator();

                    while (it2.hasNext()) {
                        Object obj2 = it2.next();
                        if (obj2 instanceof IWorkItem) {
                            IWorkItem wi = (IWorkItem) obj2;
                            DurationTime t = wi.getRemainingEstimate();
                            if (t == null) {
                                t = wi.getInitialEstimate();
                                if (t == null)
                                    pair.time += dayLength; // 1 day
                                else
                                    pair.time += t.getLength();
                            } else
                                pair.time += t.getLength();
                        }

                    }
                    pairs.add(pair);

                }
            }
        }
        
        req.setAttribute("pairs", pairs);
        req.setAttribute("dayLength", dayLength);
        
        getServletContext().getRequestDispatcher("/currentUserWorkload.jsp").forward(req, resp);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
}
