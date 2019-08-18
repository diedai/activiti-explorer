 package org.activiti.explorer.ui.login;
 
 import com.vaadin.Application;
 import com.vaadin.ui.LoginForm;
 import com.vaadin.ui.Window;
 import java.io.UnsupportedEncodingException;
 import java.net.URL;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.springframework.web.util.HtmlUtils;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ExplorerLoginForm
   extends LoginForm
 {
   private static final long serialVersionUID = 1L;
   protected String usernameCaption;
   protected String passwordCaption;
   protected String submitCaption;
   protected static final String STYLE_LOGIN_FIELD = "login-field";
   protected static final String STYLE_LOGIN_FIELD_CAPTION = "login-field-caption";
   protected static final String STYLE_LOGIN_BUTTON = "login-button";
   
   public ExplorerLoginForm()
   {
     I18nManager i18nManager = ExplorerApp.get().getI18nManager();
     this.usernameCaption = HtmlUtils.htmlEscape(i18nManager.getMessage("login.username"));
     this.passwordCaption = HtmlUtils.htmlEscape(i18nManager.getMessage("login.password"));
     this.submitCaption = HtmlUtils.htmlEscape(i18nManager.getMessage("login.button"));
   }
   
 
 
   protected byte[] getLoginHTML()
   {
     String appUri = getApplication().getURL().toString() + getWindow().getName() + "/";
     
 
 
 
     appUri = appUri.replaceFirst("^(http://|https://)", "//");
     
 
 
     String x = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
     
 
 
 
     String h = "<head><script type='text/javascript'>var setTarget = function() {  var uri = '" + appUri + "loginHandler';" + "  var f = document.getElementById('loginf');" + "  document.forms[0].action = uri;" + "  document.forms[0].username.focus();" + "};" + "" + "var styles = window.parent.document.styleSheets;" + "for(var j = 0; j < styles.length; j++) {\n" + "  if(styles[j].href) {" + "    var stylesheet = document.createElement('link');\n" + "    stylesheet.setAttribute('rel', 'stylesheet');\n" + "    stylesheet.setAttribute('type', 'text/css');\n" + "    stylesheet.setAttribute('href', styles[j].href);\n" + "    document.getElementsByTagName('head')[0]" + "                .appendChild(stylesheet);\n" + "  }" + "}\n" + "function submitOnEnter(e) {" + "  var keycode = e.keyCode || e.which;" + "  if (keycode == 13) {document.forms[0].submit();}" + "}\n" + "</script>" + "</head>";
     
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
     String b = "<body onload='setTarget();'  class='login-general'><div><iframe name='logintarget' style='width:0;height:0;border:0;margin:0;padding:0;'></iframe><form id='loginf' target='logintarget'      onkeypress='submitOnEnter(event)'      method='post'><table width='100%'><tr><td class='login-field-caption'>" + this.usernameCaption + "</td>" + "<td width='80%'><input class='" + "login-field" + "' type='text' name='username'></td></tr>" + "<tr><td class='" + "login-field-caption" + "'>" + this.passwordCaption + "</td>" + "    <td><input class='" + "login-field" + "'" + "          type='password'" + "          name='password'></td></tr>" + "</table>" + "<div>" + "<div onclick='document.forms[0].submit();'" + "     tabindex='0' class='" + "login-button" + "' role='button'>" + "<span>" + this.submitCaption + "</span>" + "</span></div></div></form></div></body>";
     
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
     String encoding = "UTF-8";
     try {
       return (x + "<html>" + h + b + "</html>").getBytes(encoding);
     }
     catch (UnsupportedEncodingException e) {
       throw new RuntimeException("Unsupported encoding: " + encoding);
     }
   }
 }


