/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.0.30
 * Generated at: 2017-05-15 10:09:35 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.polarion.platform.i18n.Localization;
import com.polarion.core.util.EscapeChars;
import com.polarion.psvn.launcher.internal.activation.ActivationData;

public final class entry_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("com.polarion.core.util.EscapeChars");
    _jspx_imports_classes.add("com.polarion.psvn.launcher.internal.activation.ActivationData");
    _jspx_imports_classes.add("com.polarion.platform.i18n.Localization");
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
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

 
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setDateHeader("Expires", 0);

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
 request.setCharacterEncoding("utf-8"); 
      out.write("\r\n");
      out.write("\r\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "includes/head.jsp" + "?" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("title", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode(String.valueOf(EscapeChars.forHTMLTag(Localization.getString("activation.entry.title"))), request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("submitLabel", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode(String.valueOf( EscapeChars.forJavascriptString(Localization.getString("activation.entry.startTrial")) ), request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("submittingLabel", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode(String.valueOf( EscapeChars.forJavascriptString(Localization.getString("activation.entry.startingTrial")) ), request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("successLabel", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode(String.valueOf( EscapeChars.forJavascriptString(Localization.getString("activation.message.trialStarted", "/polarion")) ), request.getCharacterEncoding()), out, false);
      out.write("\r\n");
      out.write("\r\n");
      out.write("<script>\r\n");
      out.write("\r\n");
      out.write("function submitForm() {\r\n");
      out.write("   $.ajax({\r\n");
      out.write("       type: 'POST',\r\n");
      out.write("       url: \"/polarion/activate/entry\",\r\n");
      out.write("       data: $(\"#activationForm\").serialize(),\r\n");
      out.write("       success: function(result, textStatus, jqXHR) {\r\n");
      out.write("\t\t\t\t\tshowActivating(false);\r\n");
      out.write("\t\t\t\t\tresult = $.parseJSON(result);\r\n");
      out.write("\t    \t\t\thandleResult(result);\r\n");
      out.write("               },\r\n");
      out.write("       error: function(jqXHR, textStatus, errorThrown) {\r\n");
      out.write("       \t\t\tshowActivating(false)\r\n");
      out.write("       \t\t\tsetMessage(\"");
      out.print( EscapeChars.forJavascriptString(Localization.getString("activation.message.serverCommunicationFailed")) );
      out.write("\", true);\r\n");
      out.write("               },\r\n");
      out.write("       cache: false,\r\n");
      out.write("       contentType: false,\r\n");
      out.write("       processData: false\r\n");
      out.write("   });\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("$(document).ready(function(){\r\n");
      out.write("    $('input[id=activateButton]').on('click', function() {\r\n");
      out.write("    \twindow.location.href = \"/polarion/activate/online\";\r\n");
      out.write("    });\r\n");
      out.write("    $(\"#productKeyInput\").keydown(function() {\r\n");
      out.write("    \tvalidate();\r\n");
      out.write("    });\r\n");
      out.write("    $(\"#productKeyInput\").bind('input propertychange', function() {\r\n");
      out.write("    \tvalidate();\r\n");
      out.write("\t});\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("function validate() {\r\n");
      out.write("    setMessage(\"\", true);\r\n");
      out.write("    return true;\r\n");
      out.write("}\r\n");
      out.write("</script> \r\n");
      out.write("\r\n");
      out.write("<div id=\"heading\">");
      out.print(Localization.getString("activation.title"));
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("<form id=\"activationForm\" action=\"/polarion/activate/entry\" method=\"post\" enctype=\"application/x-www-form-urlencoded\">\r\n");
      out.write("\r\n");
      out.write("    <div id=\"submit\" class=\"noSpaceBefore\">\r\n");
      out.write("    \t<input id=\"submitButton\" class=\"bigButton\" name=\"submit\" type=\"button\" ");
      out.print(!ActivationData.getInstance().canActivateTrial() ? "disabled" : "");
      out.write(" value=\"");
      out.print(Localization.getString("activation.entry.startTrial"));
      out.write("\" />\r\n");
      out.write("    </div>\r\n");
      out.write("\t<div class=\"boxInfoShort\">\r\n");
      out.write("        ");

            if (ActivationData.getInstance().canActivateTrial()) {
        
      out.write("\t\r\n");
      out.write("\t\t\t");
      out.print( EscapeChars.forHTMLTag(Localization.getString("activation.entry.startTrial.info")) );
      out.write("\r\n");
      out.write("        ");
} else {
      out.write("\t\r\n");
      out.write("\t\t\t");
      out.print( ActivationData.getInstance().getTrialNotAvailableMessage() );
      out.write("\r\n");
      out.write("        ");
}
      out.write("\t\r\n");
      out.write("    </div>\r\n");
      out.write("\r\n");
      out.write("\t<div class=\"boxOr\">\r\n");
      out.write("\t\t<table>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td style=\"width: 50%;\"><div class=\"hr\"></div></td>\r\n");
      out.write("\t\t\t\t<td>");
      out.print( EscapeChars.forHTMLTag(Localization.getString("activation.entry.or")) );
      out.write("</td>\r\n");
      out.write("\t\t\t\t<td style=\"width: 50%;\"><div class=\"hr\"></div></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t    </table>\r\n");
      out.write("    </div>\r\n");
      out.write("\r\n");
      out.write("\t<div class=\"box\">\r\n");
      out.write("    \t<input id=\"activateButton\" class=\"bigButton\" type=\"button\" value=\"");
      out.print( Localization.getString("activation.entry.activate") );
      out.write("\" />\r\n");
      out.write("    </div>\r\n");
      out.write("\t<div class=\"boxInfoShort\">\r\n");
      out.write("    \t");
      out.print( EscapeChars.forHTMLTag(Localization.getString("activation.entry.activate.info")) );
      out.write("\r\n");
      out.write("    </div>\r\n");
      out.write("    \r\n");
      out.write("    \r\n");
      out.write("          \r\n");
      out.write("</form>\r\n");
      out.write("<div id=\"copyright\">");
      out.print( Localization.getString("login.copyRightMessage") );
      out.write("</div>\r\n");
      out.write("\r\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "includes/tail.jsp", out, false);
      out.write(' ');
      out.write('\r');
      out.write('\n');
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
