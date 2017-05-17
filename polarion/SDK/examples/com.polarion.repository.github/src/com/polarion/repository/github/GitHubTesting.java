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
package com.polarion.repository.github;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
public class GitHubTesting extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static class CommitInformation {
    	
    	public CommitInformation(String sha, String author, String date){
    		this.sha = sha;
    		this.author = author;
    		this.date = date;
    	}
        public String sha;
        public String author;
        public String date;
    }
    

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
    	System.out.println("I am in GitHubTesting");
    	URL repoUrl = new URL("https://api.github.com/repos/andyholton/lighterpack/commits");
    	JSONObject commitsJSON = new JSONObject();
    	JSONParser parser = new JSONParser();
    	
    	HttpsURLConnection repoConnection = (HttpsURLConnection) repoUrl.openConnection();
    	repoConnection.setRequestMethod("GET");
    	repoConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
    	repoConnection.connect();
    	
    	InputStream is = repoConnection.getInputStream();
    	try {
    		InputStreamReader isr = new InputStreamReader(is);
    		JSONArray commitsArray = (JSONArray) parser.parse(isr);
    		commitsJSON.put("commits", commitsArray);
    		resp.setStatus(200);
    	} catch (ParseException e) {
			commitsJSON.put("errors", e.getMessage());
			resp.setStatus(500);
		} finally {
    	  is.close();
    	}
        
        ArrayList<CommitInformation> commits = new ArrayList<CommitInformation>();

        req.setAttribute("commits", commits);
        req.setAttribute("json", commitsJSON);
        getServletContext().getRequestDispatcher("/githubtesting.jsp").forward(req, resp);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	resp.setCharacterEncoding("UTF-8");
    	resp.setContentType("application/json");
    	
    	PrintWriter out = resp.getWriter();
        JSONObject jsonResponse = new JSONObject();
        JSONParser parser = new JSONParser();
        
        try {
        	JSONObject reqJSON = (JSONObject) parser.parse(new InputStreamReader(req.getInputStream()));
        	
        } catch (Exception e) {
        	
        }
    }
    
}
