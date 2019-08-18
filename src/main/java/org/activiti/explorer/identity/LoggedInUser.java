package org.activiti.explorer.identity;

import java.io.Serializable;
import java.util.List;
import org.activiti.engine.identity.Group;

public abstract interface LoggedInUser
  extends Serializable
{
  public abstract String getId();
  
  public abstract String getFirstName();
  
  public abstract String getLastName();
  
  public abstract String getFullName();
  
  public abstract String getPassword();
  
  public abstract boolean isAdmin();
  
  public abstract boolean isUser();
  
  public abstract List<Group> getSecurityRoles();
  
  public abstract List<Group> getGroups();
}


