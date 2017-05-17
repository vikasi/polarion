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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

/**
 * @author <A HREF="mailto:dev@polarion.com">Jiri Banszel</A>, POLARION SOFTWARE
 */
@SuppressWarnings("nls")
public class LDAPClient implements ILDAPClient {

    private Logger log = org.apache.log4j.Logger.getLogger(LDAPClient.class);

    private Collection<Variable> vars;
    private long searchSizeLimit = 0;
    private int searchTimeLimit = 0;

    public static class Variable {

        private String name;
        private String value;

        public Variable() {
            super();
        }

        public Variable(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    public void setEnvironmentTemplate(Collection<Variable> vars) {
        this.vars = vars;
    }

    /* (non-Javadoc)
     * @see com.polarion.alm.projects.internal.ldap.ILDAPService#createContext(java.lang.String, java.lang.String)
     */
    @Override
    public DirContext createContext(String dn, String password, String ldapHost) throws NamingException {
        if (ldapHost == null) {
            throw new IllegalArgumentException("ldapHost is null");
        }
        Properties env = new Properties();
        if (vars != null) {
            for (Variable var : vars) {
                env.put(var.getName(), var.getValue());
            }
        }
        env.put(Context.PROVIDER_URL, ldapHost);
        if (notEmpty(dn)) {
            env.put(Context.SECURITY_PRINCIPAL, dn);
            env.put(Context.SECURITY_CREDENTIALS, password);
        }
        return new InitialDirContext(env);
    }

    private boolean notEmpty(String s) {
        return (s != null) && (s.trim().length() > 0);
    }

    /* (non-Javadoc)
     * @see com.polarion.alm.projects.internal.ldap.ILDAPService#closeContext(javax.naming.directory.DirContext)
     */
    @Override
    public boolean closeContext(DirContext ctx) {
        try {
            ctx.close();
            return true;
        } catch (Throwable e) {
            log.error("Failed to close LDAP context", e);
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.polarion.alm.projects.internal.ldap.ILDAPClient#getAttributes(javax.naming.directory.DirContext, java.lang.String, java.lang.String[])
     */
    @Override
    public Attributes getAttributes(DirContext ctx, String dn, String[] attrIds) throws NamingException {
        return ctx.getAttributes(dn, attrIds);
    }

    @Override
    public String getSimpleAttributeValue(String attrName, Attributes attrs) throws NamingException {
        if (attrs != null) {
            Attribute attr = attrs.get(attrName);
            if ((attr != null) && (attr.size() == 1)) {
                Object value = attr.get(0);
                if (value != null) {
                    return value.toString();
                }
            }
        }
        return null;
    }

    @Override
    public Collection<String> search(DirContext ctx, String searchDN, String searchFilter, boolean subtree) throws NamingException {
        if (log.isInfoEnabled()) {
            log.info("Executing search: DN=" + searchDN + ", filter=" + searchFilter + ", subtree=" + subtree);
        }
        int scope = subtree ? SearchControls.SUBTREE_SCOPE : SearchControls.ONELEVEL_SCOPE;
        Collection<String> dns = new ArrayList<String>();
        NamingEnumeration<SearchResult> results = doSearch(ctx, searchDN, searchFilter, scope);
        while (results.hasMore()) {
            SearchResult result = results.next();
            String entryDN = getDN(fixName(result.getName()), searchDN);
            dns.add(entryDN);
        }
        if (log.isInfoEnabled()) {
            log.info(dns.size() + " nodes found");
        }
        if (log.isDebugEnabled()) {
            log.debug("found DNs: " + dns);
        }
        return dns;
    }

    @Override
    public boolean matches(DirContext ctx, String searchDN, String searchFilter) throws NamingException {
        NamingEnumeration<SearchResult> results = doSearch(ctx, searchDN, searchFilter, SearchControls.OBJECT_SCOPE);
        boolean matches = results.hasMore();
        if (log.isInfoEnabled()) {
            log.info("matches: " + matches);
        }
        return matches;
    }

    private NamingEnumeration<SearchResult> doSearch(DirContext ctx, String searchDN, String searchFilter, int scope) throws NamingException {
        if (log.isInfoEnabled()) {
            String scopeStr;
            switch (scope) {
            case SearchControls.OBJECT_SCOPE:
                scopeStr = "object";
                break;
            case SearchControls.ONELEVEL_SCOPE:
                scopeStr = "onelevel";
                break;
            case SearchControls.SUBTREE_SCOPE:
                scopeStr = "subtree";
                break;
            default:
                scopeStr = String.valueOf(scope);
            }
            log.info("Executing search: DN=" + searchDN + ", filter=" + searchFilter + ", scope=" + scopeStr);
        }
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(scope);
        constraints.setCountLimit(searchSizeLimit);
        constraints.setTimeLimit(searchTimeLimit);
        return ctx.search(searchDN, searchFilter, constraints);
    }

    @Override
    public Collection<String> getGroupMembers(DirContext ctx, String groupDN, String memberAttrId) throws NamingException {
        if (log.isInfoEnabled()) {
            log.info("Get group members: groupDN=" + groupDN + ", memberAttrId=" + memberAttrId);
        }
        Attributes attrs = ctx.getAttributes(groupDN, new String[] { memberAttrId });
        Attribute attr = attrs.get(memberAttrId);
        NamingEnumeration<?> values = attr.getAll();
        Collection<String> members = new ArrayList<String>();
        while (values.hasMore()) {
            String value = (String) values.next();
            if (value != null) {
                members.add(value);
            }
        }
        if (log.isInfoEnabled()) {
            log.info(members.size() + " members found");
        }
        if (log.isDebugEnabled()) {
            log.debug("member DNs: " + members);
        }
        return members;
    }

    private String fixName(String name) {
        if (name.length() > 0 && name.charAt(0) == '"') {
            int size = name.length() - 1;
            StringBuffer buf = new StringBuffer();
            for (int i = 1; i < size; i++) {
                if (name.charAt(i) == '/') {
                    buf.append("\\");
                }
                buf.append(name.charAt(i));
            }
            return buf.toString();
        }
        return name;
    }

    private String getDN(String rdn, String base) {
        if (rdn.length() == 0) {
            return base;
        }
        if (base.length() == 0) {
            return rdn;
        }
        return rdn + "," + base;
    }

    @Override
    public String escapeValueForDN(String value) {
        String escaped = value;
        escaped = escaped.replaceAll("=", "\\\\=");
        escaped = escaped.replaceAll(",", "\\\\2C");
        return escaped;
    }

    @Override
    public String escapeValueForFilter(String value) {
        String escaped = value;
        escaped = escaped.replaceAll("\\)", "\\\\)");
        escaped = escaped.replaceAll("\\(", "\\\\(");
        escaped = escaped.replaceAll("=", "\\\\=");
        escaped = escaped.replaceAll(",", "\\\\,");
        return escaped;
    }

}
