/*
 * Copyright (C) 2004-2013 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2013 Polarion Software
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
package com.polarion.diagtool.internal

import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import org.apache.log4j.Logger


class FileSender {

    private final Logger log
    private final FileSenderConfiguration config

    def FileSender(Logger log, FileSenderConfiguration config) {
        this.log = log
        this.config = config
    }

    def sendFile(File f) {
        if (f.length() < config.mailAttachmentThreshold) {
            sendMail(f, "Attached Polarion Diagnostic Tool results file ${f}.")
        } else {
            if (ftpFile(f)) {
                sendMail(null, "Polarion Diagnostic Tool results file ${f} stored on ${config.ftpHost}.")
            }
        }
    }

    def sendMail(File attachment, String text) {
        log.info("Sending mail as ${config.mailSender} to ${config.mailRecipient} with subject \"${config.mailSubject}\"" +
                ((attachment != null) ? " and attachment ${attachment}" : ""))
        Session session = Session.getDefaultInstance(config.mailProtocolSettings)
        Transport transport = session.getTransport(config.mailProtocol)
        try {
            Message msg = new MimeMessage(session)
            msg.setSubject(config.mailSubject)
            msg.setFrom(new InternetAddress(config.mailSender))
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(config.mailRecipient))

            Multipart multipart = new MimeMultipart()

            MimeBodyPart textPart = new MimeBodyPart()
            textPart.setText(text)
            multipart.addBodyPart(textPart)

            if (attachment != null) {
                MimeBodyPart filePart = new MimeBodyPart()
                filePart.attachFile(attachment)
                multipart.addBodyPart(filePart)
            }

            msg.setContent(multipart)
            msg.saveChanges()

            transport.connect(config.mailHost, config.mailPort, config.mailAuth ? config.mailUser : null, config.mailAuth ? config.mailPassword : null)
            transport.sendMessage(msg, msg.getAllRecipients())
        } finally {
            transport.close()
        }
    }

    boolean ftpFile(File f) {
        log.info("Storing ${f} on FTP server ${config.ftpHost}")
        return new FTPClient().with {
            try {
                connect(config.ftpHost)
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    log.fatal("FAILED - Unable to connect to FTP server: " + replyString)
                    return false
                }
                enterLocalPassiveMode()
                if (!login(config.ftpUser, config.ftpPassword)) {
                    log.fatal("FAILED - Unable to login to FTP server: " + replyString)
                    return false
                }
                setFileType(FTP.BINARY_FILE_TYPE)
                return f.withInputStream { is ->
                    if (!storeFile(f.name, is)) {
                        log.fatal("FAILED - Unable to send file to FTP server: " + replyString)
                        return false
                    }
                    return true
                }
            } finally {
                try {
                    logout()
                } catch (Exception e) {
                    // ignored
                }
                try {
                    disconnect()
                } catch (Exception e) {
                    // ignored
                }
            }
        }
    }
}