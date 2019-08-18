package org.activiti.explorer.ui.login;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.explorer.identity.LoggedInUser;

public abstract interface LoginHandler
  extends Serializable
{
  public abstract LoggedInUser authenticate(String paramString1, String paramString2);
  
  public abstract LoggedInUser authenticate(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
  
  public abstract void logout(LoggedInUser paramLoggedInUser);
  
  public abstract void onRequestStart(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
  
  public abstract void onRequestEnd(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
}


