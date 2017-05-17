/*
 * Copyright (C) 2004-2015 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2015 Polarion Software
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
package com.polarion.example.velocitycontext;

import org.jetbrains.annotations.NotNull;

import com.polarion.platform.service.repository.IRepositoryReadOnlyConnection;
import com.polarion.platform.service.repository.IRepositoryService;

public class RepositoryUtilImpl implements RepositoryUtil {

    @NotNull
    private final IRepositoryService service;

    public RepositoryUtilImpl(@NotNull IRepositoryService service) {
        this.service = service;
    }

    @NotNull
    @Override
    public IRepositoryService getRepositoryService() {
        return service;
    }

    @NotNull
    @Override
    public String getLastRootRevision() {
        IRepositoryReadOnlyConnection readOnlyConnection = service.getReadOnlyConnection(IRepositoryService.DEFAULT);
        return readOnlyConnection.getLastRevision(readOnlyConnection.getRepositoryInfo().getRootLocation());
    }

}
