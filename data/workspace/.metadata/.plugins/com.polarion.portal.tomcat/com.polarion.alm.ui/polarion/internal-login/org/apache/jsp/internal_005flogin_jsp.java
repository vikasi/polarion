/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.0.30
 * Generated at: 2017-05-15 12:55:18 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.polarion.platform.i18n.Localization;
import com.polarion.core.boot.BootPlugin;
import com.polarion.portal.tomcat.session.PolarionSingleSignOn;
import com.polarion.portal.tomcat.internal.auth.LoginPageDispatcher;

public final class internal_005flogin_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(1);
    _jspx_dependants.put("/internal_login_success.jsp", Long.valueOf(1489424122000L));
  }

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("com.polarion.platform.i18n.Localization");
    _jspx_imports_classes.add("com.polarion.core.boot.BootPlugin");
    _jspx_imports_classes.add("com.polarion.portal.tomcat.internal.auth.LoginPageDispatcher");
    _jspx_imports_classes.add("com.polarion.portal.tomcat.session.PolarionSingleSignOn");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

final java.lang.String _jspx_method = request.getMethod();
if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
return;
}

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
if (!PolarionSingleSignOn.getInstance().requiresInternalLogin(request)) {
      out.write('\r');
      out.write('\n');
      out.write("<!DOCTYPE html \r\n");
      out.write("     PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\r\n");
      out.write("    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\r\n");
      out.write("\t<head>\r\n");
      out.write("\t\t<script type=\"text/javascript\" >\r\n");
      out.write("\t\t//<![CDATA[\r\n");
      out.write("\t\t\tif (parent != null && parent.loginSucessfull != null){\r\n");
      out.write("\t\t\t    parent.loginSucessfull();  \r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t//]]>\r\n");
      out.write("\t\t</script>\r\n");
      out.write("  \t</head>\r\n");
      out.write("\t<body>\r\n");
      out.write("\t\tOK\r\n");
      out.write("\t</body>\r\n");
      out.write("</html>");
      out.write('\r');
      out.write('\n');
 return; } 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\r\n");
      out.write("  ");
      out.print(com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request));
      out.write("\r\n");
      out.write("  <link rel=\"stylesheet\" type=\"text/css\" href=\"/polarion/login.css?buildId=");
      out.print( BootPlugin.getPolarionBuildNumber() );
      out.write("\" />\r\n");
      out.write("  <link rel=\"stylesheet\" type=\"text/css\" href=\"/polarion/internal-login/internal_login.css?buildId=");
      out.print( BootPlugin.getPolarionBuildNumber() );
      out.write("\" />\r\n");
      out.write("  <script src=\"/polarion/ria/javascript/jquery-3.0.0.min.js?buildId=");
      out.print( BootPlugin.getPolarionBuildNumber() );
      out.write("\"></script>\r\n");
      out.write("  <script src=\"/polarion/login.js?buildId=");
      out.print( BootPlugin.getPolarionBuildNumber() );
      out.write("\" id=\"loginScript\" type=\"text/javascript\"></script>\r\n");
      out.write("  <title>Login</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body id=\"internalBody\">\r\n");
      out.write("  \t<div id=\"internalHeader\">\r\n");
      out.write("  \t\t<img src=\"/polarion/ria/images/logos/small/repoLogo.png\" class=\"logo\" />\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<div id=\"internalLogin\">\r\n");
      out.write("\t\t");
	if (request.getAttribute(LoginPageDispatcher.LOGIN_FAILURE_ATTRIBUTE) != null) { 
      out.write("\r\n");
      out.write("\t\t<span class=\"loginFailure\" style=\"color:#ff0000;\">");
      out.print( request.getAttribute(LoginPageDispatcher.LOGIN_FAILURE_ATTRIBUTE) );
      out.write("</span>\r\n");
      out.write("\t\t<script type=\"text/javascript\">switchLoginLabel();</script>\r\n");
      out.write("\t\t");
 request.removeAttribute(LoginPageDispatcher.LOGIN_FAILURE_ATTRIBUTE); 
      out.write("\r\n");
      out.write("\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t<script type=\"text/javascript\">\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t ");
 
		 boolean isChromeOrEdge = com.polarion.portal.tomcat.SupportedBrowsers.isChromeOrEdge(request);
		 if (isChromeOrEdge) { 
		 
      out.write(" \r\n");
      out.write("\t\t \tsetTimeout(function(){ \r\n");
      out.write("\t\t    \t//prevents chrome autofill \r\n");
      out.write("\t\t        document.getElementById('j_username').value='';\r\n");
      out.write("\t\t        document.getElementById('j_username').blur(); \r\n");
      out.write("\t\t    \tdocument.getElementById('j_username').focus(); \r\n");
      out.write("\t\t    }, 100);  \r\n");
      out.write("\t\t ");
 } 
      out.write(" \r\n");
      out.write("        </script>\r\n");
      out.write("            \r\n");
      out.write("\t\t<form method=\"post\" action=\"");
      out.print( response.encodeURL(LoginPageDispatcher.INTERNAL_LOGIN_CONTEXT + "/j_security_check") );
      out.write("\" enctype=\"application/x-www-form-urlencoded\" onsubmit=\"return login();\">\r\n");
      out.write("\t\t\t");
 
				String target = (String) request.getParameter("target");
				target = target == null ? "" : target; 
			
      out.write("\r\n");
      out.write("\t\t\t<input id=\"target\" name=\"target\" type=\"hidden\" value=\"");
      out.print( target );
      out.write("\"/>\r\n");
      out.write("\t        <div id=\"username\" style=\"padding: 12px 0px;\">\r\n");
      out.write("\t        \t<label for=\"j_username\" class=\"hidden\">");
      out.print( Localization.getString("login.userName") );
      out.write("</label>\r\n");
      out.write("\t            <input id=\"j_username\" name=\"j_username\" size=\"12\" type=\"text\" class=\"input\" ");
 if(isChromeOrEdge) { 
      out.write(" value=\"Username\" ");
 } 
      out.write("/>\r\n");
      out.write("\t       \t</div>\r\n");
      out.write("\t                \r\n");
      out.write("\t        <div id=\"password\">\r\n");
      out.write("\t        \t<label for=\"j_password\" class=\"hidden\">");
      out.print( Localization.getString("login.password") );
      out.write("</label>\r\n");
      out.write("\t            <input id=\"j_password\" name=\"j_password\" size=\"12\" value=\"\" type=\"password\" class=\"input\" />\r\n");
      out.write("\t        </div>\r\n");
      out.write("\t                \r\n");
      out.write("\t        <div id=\"internalSubmit\">\r\n");
      out.write("\t        \t<label id=\"submitMsg\" class=\"hidden\" for=\"submitButton\">");
      out.print( Localization.getString("login.loggingIn") );
      out.write("</label>\r\n");
      out.write("\t            <input id=\"submitButton\" name=\"submit\" type=\"submit\" value=\"");
      out.print( Localization.getString("login.logIn") );
      out.write("\" />\r\n");
      out.write("\t            <label id=\"rememberme\"><input name=\"rememberme\" value=\"true\" type=\"checkbox\" /><span class=\"remember\">");
      out.print( Localization.getString("login.stayLoggedIn") );
      out.write("</span></label>\r\n");
      out.write("\t        </div>\r\n");
      out.write("\t                \r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</div>\r\n");
      out.write("</body>\r\n");
      out.write("</html> \r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
