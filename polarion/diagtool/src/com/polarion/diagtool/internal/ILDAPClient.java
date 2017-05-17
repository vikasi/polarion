/*
 * Copyright (C) 2004-2007 POLARION SOFTWARE
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2007 POLARION SOFTWARE
 * All Rights Reserved.  No use, copying or distribution of this
 * work may be made except in accordance with a valid license
 * agreement from POLARION SOFTWARE. This notice must be
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

package com.polarion.diagtool.internal;

import java.util.Collection;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

/**
 * @author <A HREF="mailto:dev@polarion.com">Jiri Banszel</A>, POLARION SOFTWARE
 */
public interface ILDAPClient {

    /**
     * Creates new directory context for use with other methods on this interface.
     * After the work with directory context is finished, it has to be closed 
     * (see {@link #closeContext(DirContext)}).
     * Distinguished name and password may be <code>null</code> 
     * for anonymous access.
     * 
     * @param dn distinguished name
     * @param password
     * @param ldapHost LDAP server URL, e.g. ldap://localhost:389 (not <code>null</code>)
     * @throws IllegalArgumentException if ldapHost is <code>null</code>
     * @see #closeContext(DirContext)
     */
    DirContext createContext(String dn, String password, String ldapHost) throws NamingException;

    /**
     * Closes the context, releases context's resources.
     * Should not ever throw any exception or error.
     * 
     * @param ctx
     * @return <code>false</code> in case an error occurred
     * @see #createContext(String, String, String)
     */
    boolean closeContext(DirContext ctx);

    /**
     * Reads attributes of the node.
     *  
     * @param ctx
     * @param dn distinguished name
     * @param attrIds ids of attributes that should be read; can be <code>null</code> in which case 
     *     all attributes are read
     */
    Attributes getAttributes(DirContext ctx, String dn, String[] attrIds) throws NamingException;

    String getSimpleAttributeValue(String attrName, Attributes attrs) throws NamingException;

    /**
     * Returns found distinguished names. Search is done using the filter 
     * in the given node and optionally in the subtree.
     * 
     * @param searchDN 
     * @param searchFilter
     * @param subtree if whole subtree should be searched
     * 
     * @return collection of {@link String}s  
     */
    Collection<String> search(DirContext ctx, String searchDN, String searchFilter, boolean subtree) throws NamingException;

    boolean matches(DirContext ctx, String dn, String searchFilter) throws NamingException;

    Collection<String> getGroupMembers(DirContext ctx, String groupDN, String memberAttrId) throws NamingException;

    String escapeValueForFilter(String value);

    String escapeValueForDN(String value);

}
