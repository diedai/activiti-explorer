package org.activiti.explorer.navigation;

import java.io.Serializable;

public abstract interface Navigator
  extends Serializable
{
  public abstract String getTrigger();
  
  public abstract void handleNavigation(UriFragment paramUriFragment);
}


