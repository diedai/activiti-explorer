package org.activiti.explorer.servlet;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.ui.Window;
import java.io.BufferedWriter;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.activiti.explorer.ExplorerApp;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ExplorerApplicationServlet extends AbstractApplicationServlet {
	private static final long serialVersionUID = 1L;
	protected WebApplicationContext applicationContext;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		this.applicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletConfig.getServletContext());
	}

	protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
		return ExplorerApp.class;
	}

	protected Application getNewApplication(HttpServletRequest request) {
		return (Application) this.applicationContext.getBean(ExplorerApp.class);
	}

	protected void writeAjaxPageHtmlVaadinScripts(Window window, String themeName, Application application,
			BufferedWriter page, String appUrl, String themeUri, String appId, HttpServletRequest request)
			throws ServletException, IOException {
		super.writeAjaxPageHtmlVaadinScripts(window, themeName, application, page, appUrl, themeUri, appId, request);

		String browserDependentCss = "<script type=\"text/javascript\">//<![CDATA[var mobi = ['opera', 'iemobile', 'webos', 'android', 'blackberry', 'ipad', 'safari'];var midp = ['blackberry', 'symbian'];var ua = navigator.userAgent.toLowerCase();if ((ua.indexOf('midp') != -1) || (ua.indexOf('mobi') != -1) || ((ua.indexOf('ppc') != -1) && (ua.indexOf('mac') == -1)) || (ua.indexOf('webos') != -1)) {  document.write('<link rel=\"stylesheet\" href=\""
				+ themeUri + "/allmobile.css\" type=\"text/css\" media=\"all\"/>');"
				+ "  if (ua.indexOf('midp') != -1) {" + "    for (var i = 0; i < midp.length; i++) {"
				+ "      if (ua.indexOf(midp[i]) != -1) {" + "        document.write('<link rel=\"stylesheet\" href=\""
				+ themeUri + "' + midp[i] + '.css\" type=\"text/css\"/>');" + "      }" + "    }" + "  }" + "   else {"
				+ "     if ((ua.indexOf('mobi') != -1) || (ua.indexOf('ppc') != -1) || (ua.indexOf('webos') != -1)) {"
				+ "       for (var i = 0; i < mobi.length; i++) {" + "         if (ua.indexOf(mobi[i]) != -1) {"
				+ "           if ((mobi[i].indexOf('blackberry') != -1) && (ua.indexOf('6.0') != -1)) {"
				+ "             document.write('<link rel=\"stylesheet\" href=\"" + themeUri
				+ "' + mobi[i] + '6.0.css\" type=\"text/css\"/>');" + "           }" + "           else {"
				+ "             document.write('<link rel=\"stylesheet\" href=\"" + themeUri
				+ "' + mobi[i] + '.css\" type=\"text/css\"/>');" + "           }" + "          break;" + "         }"
				+ "       }" + "     }" + "   }" + " }"
				+ "if ((navigator.userAgent.indexOf('iPhone') != -1) || (navigator.userAgent.indexOf('iPad') != -1)) {"
				+ " document.write('<meta name=\"viewport\" content=\"width=device-width\" />');" + "}" + "  //]]>"
				+ "</script>" + "<!--[if lt IE 7]><link rel=\"stylesheet\" type=\"text/css\" href=\"" + themeUri
				+ "/lt7.css\" /><![endif]-->";

		page.write(browserDependentCss);
	}
}
