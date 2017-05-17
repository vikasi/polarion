/*
 * Copyright (C) 2004-2010 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2010 Polarion Software
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

import com.polarion.alm.install.utils.Utils

public class PolarionProperties {

    private static final String PROP_LOGIN = "login"
    private static final String PROP_PASSWORD = "password"
    private static final String PROP_ADMIN_USER = "adminUser"
    private static final String PROP_HTPASSWD_PATH = "htpasswd.path"
    private static final String PROP_FEEDBACK_EMAIL = "feedback.email"
    private static final String PROP_SUPPORT_EMAIL = "support.email"
    private static final String PROP_NOTIFICATIONS_DISABLED = "com.polarion.platform.persistence.notifications.disabled"
    private static final String PROP_SMTP_HOST = "announcer.smtp.host"
    private static final String PROP_IS_SMTP_AUTHORIZATION = "announcer.smtp.auth"
    private static final String PROP_SMTP_ACCOUNT_NAME = "announcer.smtp.user"
    private static final String PROP_SMTP_PASSWORD = "announcer.smtp.password"
    private static final String PROP_ENABLE_CREATE_ACCOUNT_FORM = "enableCreateAccountForm"
    private static final String PROP_TOMCAT_PORT = "TomcatService.ajp13-port"
    private static final String PROP_NODE_ID = "com.polarion.nodeId"
    private static final String PROP_APPLICATION = "com.polarion.application"
    private static final String PROP_SHARED = "com.polarion.shared"

    public static final String PROP_PG_CONNECTION = "com.polarion.platform.internalPG"

    private Properties props;

    public PolarionProperties(File file) {
        props = Utils.loadProperties(file)
    }

    public String getBaseURL() {
        return props.get(PolarionInstallation.PROP_BASE_URL)
    }

    public String getServerPort() {
        URL baseURL = new URL(getBaseURL())
        int port = baseURL.getPort()
        return String.valueOf((port < 0) ? baseURL.getDefaultPort() : port)
    }

    public String getLogin() {
        return props.get(PROP_LOGIN)
    }

    public String getPassword() {
        return props.get(PROP_PASSWORD)
    }

    public String getAdminUser() {
        return props.get(PROP_ADMIN_USER)
    }

    public String getControlPort() {
        String controlPort = props.get(PolarionInstallation.PROP_CONTROL_PORT)
        if (controlPort == null) {
            controlPort = props.get(PolarionInstallation.PROP_SHUTDOWN_PORT)
        }
        return controlPort
    }

    public String getControlHostname() {
        String controlHostname = props.get(PolarionInstallation.PROP_CONTROL_HOSTNAME)
        if (controlHostname == null) {
            controlHostname = props.get(PolarionInstallation.PROP_SHUTDOWN_HOSTNAME)
        }
        return controlHostname
    }

    public String getHtpasswdPath() {
        return props.get(PROP_HTPASSWD_PATH)
    }

    public String getFeedbackEmail() {
        return props.get(PROP_FEEDBACK_EMAIL)
    }

    public String getSupportEmail() {
        return props.get(PROP_SUPPORT_EMAIL)
    }

    public String getNotificationsDisabled() {
        return props.get(PROP_NOTIFICATIONS_DISABLED)
    }

    public String getSMTPHost() {
        return props.get(PROP_SMTP_HOST)
    }

    public String getIsSMTPAuthorization() {
        return props.get(PROP_IS_SMTP_AUTHORIZATION)
    }

    public String getSMTPAccountName() {
        return props.get(PROP_SMTP_ACCOUNT_NAME)
    }

    public String getSMTPPassword() {
        return props.get(PROP_SMTP_PASSWORD)
    }

    public String getEnableCreateAccountForm() {
        return props.get(PROP_ENABLE_CREATE_ACCOUNT_FORM)
    }

    public String getTomcatPort() {
        return props.get(PROP_TOMCAT_PORT)
    }

    public String getNodeId() {
        return props.get(PROP_NODE_ID)
    }

    public String getApplication() {
        return props.get(PROP_APPLICATION)
    }

    public String getSharedFolder() {
        return props.get(PROP_SHARED)
    }

    public String getPGConnection(){
        return props.get(PROP_PG_CONNECTION);
    }
}
