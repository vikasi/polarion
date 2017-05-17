/*
 * Copyright (C) 2004-2011 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2011 Polarion Software
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
package com.polarion.platform.repository.external.github;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.polarion.core.util.ObjectUtils;
import com.polarion.platform.repository.external.AbstractExternalRepositoryConfiguration;
import com.polarion.platform.repository.external.ExternalRepositoryCredentials;
import com.polarion.platform.repository.external.ExternalRepositoryUtils;
import com.polarion.platform.repository.external.IExternalRepositoryProvider;
import com.polarion.platform.repository.external.ExternalRepositoryCredentials.FieldType;
import com.polarion.platform.service.repository.ILocationChangeMetaData;
import com.polarion.platform.service.repository.IRevisionMetaData;
import com.polarion.platform.service.repository.RepositoryException;
import com.polarion.subterra.base.data.identification.IContextId;
import com.polarion.subterra.base.location.ILocation;
import com.polarion.subterra.base.location.Location;

/**
 * @author <a href="mailto:dev@polarion.com">svirecm</a>, Polarion Software
 */
public class GitHubExternalRepositoryProvider implements IExternalRepositoryProvider {

    private static final String ID = "github";
    

    /* (non-Javadoc)
     * @see com.polarion.platform.repository.external.IExternalRepositoryProvider#getProviderId()
     */
    @Override
    public String getProviderId() {
        return ID;
    }

    /* (non-Javadoc)
     * @see com.polarion.platform.repository.external.IExternalRepositoryProvider#createEmptyConfiguration()
     */
    @Override
    public IExternalRepositoryConfiguration createEmptyConfiguration() {
        return new GitHubRepositoryConfiguration();
    }

    /* (non-Javadoc)
     * @see com.polarion.platform.repository.external.IExternalRepositoryProvider#getRepository(com.polarion.subterra.base.data.identification.IContextId, com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepositoryConfiguration, com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepositoryCallback)
     */
    @Override
    public IExternalRepository getRepository(IContextId contextId, IExternalRepositoryConfiguration configuration,
            IExternalRepositoryCallback callback) {
        return new GitHubRepository(contextId, (GitHubRepositoryConfiguration) configuration, callback);
    }

    private static class GitHubRepository implements IExternalRepository {

        private final IContextId contextId;
        private GitHubRepositoryConfiguration config;
        private final IExternalRepositoryCallback callback;

        /**
         * @param contextId
         * @param configuration
         * @param callback
         */
        public GitHubRepository(IContextId contextId,
                GitHubRepositoryConfiguration configuration,
                IExternalRepositoryCallback callback) {
            this.contextId = contextId;
            config = configuration;
            this.callback = callback;
        }

        /* (non-Javadoc)
         * @see com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepository#getContextId()
         */
        @Override
        public IContextId getContextId() {
            return contextId;
        }

        /* (non-Javadoc)
         * @see com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepository#getRevisionViewURL(java.lang.String)
         */
        @Override
        public String getRevisionViewURL(String revision) {
            if (config.getViewURL() != null) {
                return config.getViewURL().replace("$revision$", revision);
            }
            return null;
        }

        @Override
        public String getViewLocationDiffURL(ILocationChangeMetaData locationChangeMetaData) {
            String locationDiffViewURL = config.getViewLocationDiffURL();
            if (locationDiffViewURL != null) {
                return ExternalRepositoryUtils.expandVariables(locationDiffViewURL, locationChangeMetaData, null);
            }
            return null;
        }

        @Override
        public String getViewLocationURL(ILocationChangeMetaData locationChangeMetaData) {
            String locationViewURL = config.getViewLocationURL();
            if (locationViewURL != null) {
                return ExternalRepositoryUtils.expandVariables(locationViewURL, locationChangeMetaData, null);
            }
            return null;
        }

        /* (non-Javadoc)
         * @see com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepository#poll(com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepository.PollMode)
         */
        @Override
        public void poll(PollMode mode) {
            if (mode == null) {
                throw new IllegalArgumentException("mode is null");
            }
            String lastRevision = callback.getRememberedState(this);

            try {
            	
            	URL repoUrl = new URL(config.API_URL + "/repos/" + config.getName() + "/commits");
            	JSONParser parser = new JSONParser();
            	
            	HttpsURLConnection repoConnection = (HttpsURLConnection) repoUrl.openConnection();
            	repoConnection.setRequestMethod("GET");
            	repoConnection.setRequestProperty("Accept", config.ACCEPT_HEADER);
            	
            	if (!config.getUsername().isEmpty() && !config.getPassword().isEmpty()){
            		String basicAuth = new String(Base64.getEncoder().encode((config.getUsername() + ":" + config.getPassword()).getBytes()));
            		repoConnection.setRequestProperty("Authorization", "Basic " + basicAuth);
            	}

            	repoConnection.connect();
            	
            	if (repoConnection.getResponseCode()/100 >= 4){
            		throw new RepositoryException(repoUrl + " returned response code " + repoConnection.getResponseCode());
            	}
            	
            	InputStream is = repoConnection.getInputStream();
            	
        		InputStreamReader isr = new InputStreamReader(is);
        		JSONArray commitsArray = (JSONArray) parser.parse(isr);
        		
        		Stack<JSONObject> commitStack = new Stack<JSONObject>();
        		
        		for (Object commit : commitsArray){
        			JSONObject jsonCommit = (JSONObject) commit;
                    if (mode.equals(PollMode.CONTINUE) && lastRevision != null) {
                        if ((String) jsonCommit.get("sha") == lastRevision) {
                            break;
                        }
                    }
                    
                    commitStack.push(jsonCommit);
                    
                    if (mode.equals(PollMode.FROM_CURRENT)) {
                        break;
                    }
        		}
        		
        		while (!commitStack.isEmpty()){
        			lastRevision = (String) commitStack.pop().get("sha");
        			callback.revisionAdded(this, (String) lastRevision);
        		}

            } catch (IOException e) {
                throw new RepositoryException(e);
            } catch (ParseException e) {
				throw new RepositoryException(e);
			} finally {
				callback.setRememberedState(this, lastRevision != null ? lastRevision : null);
            }
        }

        /* (non-Javadoc)
         * @see com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepository#allowsAutoPolling()
         */
        @Override
        public boolean allowsAutoPolling() {
            return true;
        }

        /* (non-Javadoc)
         * @see com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepository#startAutoPolling()
         */
        @Override
        public void startAutoPolling() {
            // empty
        }

        /* (non-Javadoc)
         * @see com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepository#stopAutoPolling()
         */
        @Override
        public void stopAutoPolling() {
            // empty
        }

        /* (non-Javadoc)
         * @see com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepository#getRevisionMetaData(java.lang.String)
         */
        @Override
        public IRevisionMetaData getRevisionMetaData(String revision) {

            try {
            	
            	URL repoUrl = new URL(config.API_URL + "/repos/" + config.getName() + "/commits/" + revision);
            	JSONParser parser = new JSONParser();
            	
            	HttpsURLConnection repoConnection = (HttpsURLConnection) repoUrl.openConnection();
            	repoConnection.setRequestMethod("GET");
            	repoConnection.setRequestProperty("Accept", config.ACCEPT_HEADER);
            	
            	if (!config.getUsername().isEmpty() && !config.getPassword().isEmpty()){
            		String basicAuth = new String(Base64.getEncoder().encode((config.getUsername() + ":" + config.getPassword()).getBytes()));
            		repoConnection.setRequestProperty("Authorization", "Basic " + basicAuth);
            	}
            	
            	repoConnection.connect();
            	
            	InputStream is = repoConnection.getInputStream();
            	
        		InputStreamReader isr = new InputStreamReader(is);
        		
        		JSONObject commit = (JSONObject) parser.parse(isr);
        		JSONObject commitObj = (JSONObject) commit.get("commit");
        		JSONObject authorObj = (JSONObject) commit.get("author");
            	
            	return new IRevisionMetaData() {
            		
            		@Override
                    public String getName() {
                        return (String) commit.get("sha");
                    }

                    @Override
                    public String getDescription() {
                    	return (String) commitObj.get("message");
                    }

                    @Override
                    public Date getDate() {
                    	JSONObject committerObj = (JSONObject) commitObj.get("committer");
                    	return javax.xml.bind.DatatypeConverter.parseDateTime((String) committerObj.get("date")).getTime(); //using the commit date here, should we use authoring date?
                    }

                    @Override
                    public String getAuthor() {
                        return (String) authorObj.get("login"); // we're using the author here, should we be using the committer?
                    }
                };
                
                
            } catch (IOException e) {
                throw new RepositoryException(e);
            } catch (ParseException e) {
				throw new RepositoryException(e);
			} finally {

            }

        }

        /* (non-Javadoc)
         * @see com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepository#getConfiguration()
         */
        @Override
        public IExternalRepositoryConfiguration getConfiguration() {
            return config;
        }

        /* (non-Javadoc)
         * @see com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepository#reconfigure(com.polarion.platform.repository.external.IExternalRepositoryProvider.IExternalRepositoryConfiguration)
         */
        @Override
        public void reconfigure(IExternalRepositoryConfiguration newConfiguration) {
            config = (GitHubRepositoryConfiguration) newConfiguration;
        }

        @Override
        public List<ILocationChangeMetaData> getChangedLocations(String revision) {

            try {
            	URL repoUrl = new URL(config.API_URL + "/repos/" + config.getName() + "/commits");
            	JSONParser parser = new JSONParser();
            	
            	HttpsURLConnection repoConnection = (HttpsURLConnection) repoUrl.openConnection();
            	repoConnection.setRequestMethod("GET");
            	repoConnection.setRequestProperty("Accept", config.ACCEPT_HEADER);

            	if (!config.getUsername().isEmpty() && !config.getPassword().isEmpty()){
            		String basicAuth = new String(Base64.getEncoder().encode((config.getUsername() + ":" + config.getPassword()).getBytes()));
            		repoConnection.setRequestProperty("Authorization", "Basic " + basicAuth);
            	}
            	
            	repoConnection.connect();
            	
            	InputStream is = repoConnection.getInputStream();
            	
        		InputStreamReader isr = new InputStreamReader(is);
            	
            } catch (IOException e) {
                throw new RepositoryException(e);
            }
            return new ArrayList<ILocationChangeMetaData>();
        }

        @SuppressWarnings("nls")
        private ILocation getLocation(String path, String revision) {
            if (path != null && !path.startsWith("/")) {
                path = "/" + path;
            }
            if (ObjectUtils.emptyString(revision)) {
                return Location.getLocationWithRepository(config.getId(), path);
            }
            return Location.getLocationWithRepositoryAndRevision(config.getId(), path, revision);
        }

        @SuppressWarnings("nls")
        private List<ILocationChangeMetaData> getFilesInCommit()
                throws IOException {

            try {

            } finally {

            }
            return new ArrayList<ILocationChangeMetaData>();
        }
    }

    public static class GitHubRepositoryConfiguration extends AbstractExternalRepositoryConfiguration {
 
    	public final String API_URL = "https://api.github.com";
    	public final String ACCEPT_HEADER = "application/vnd.github.v3+json";
    	public final String BASIC_AUTH = "";

        @ExternalRepositoryCredentials(fieldType = FieldType.USERNAME, credentialId = "credentials")
        private String username;
        @ExternalRepositoryCredentials(fieldType = FieldType.PASSWORD, credentialId = "credentials")
        private String password;
        private String viewURL;
        private String viewLocationURL;
        private String viewLocationDiffURL;

        /* (non-Javadoc)
         * @see com.polarion.platform.repository.external.AbstractExternalRepositoryConfiguration#getProviderId()
         */
        @Override
        public String getProviderId() {
            return ID;
        }
        
        public String getUsername() {
        	if (username != null){
        		return username;
        	} else return "";
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password != null ? password : "";
        }

        public void setPassword(String password) {
            this.password = password;
        }

        /**
         * @return the viewURL
         */
        public String getViewURL() {
            return viewURL;
        }

        /**
         * @param viewURL the viewURL to set
         */
        public void setViewURL(String viewURL) {
            this.viewURL = viewURL;
        }

        public String getViewLocationURL() {
            return viewLocationURL;
        }

        public void setViewLocationURL(String viewLocationURL) {
            this.viewLocationURL = viewLocationURL;
        }

        public String getViewLocationDiffURL() {
            return viewLocationDiffURL;
        }

        public void setViewLocationDiffURL(String viewLocationDiffURL) {
            this.viewLocationDiffURL = viewLocationDiffURL;
        }

    }
}
