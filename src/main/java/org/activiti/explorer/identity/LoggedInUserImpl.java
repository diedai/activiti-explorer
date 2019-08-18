 package org.activiti.explorer.identity;
 
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.User;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class LoggedInUserImpl
   implements LoggedInUser
 {
   private static final long serialVersionUID = 1L;
   protected User user;
   protected String password;
   protected String alternativeId;
   protected boolean isUser;
   protected boolean isAdmin;
   protected List<Group> securityRoles = new ArrayList();
   protected List<Group> groups = new ArrayList();
   
   public LoggedInUserImpl(User user, String password) {
     this.user = user;
     this.password = password;
   }
   
   public String getId() {
     if (this.user != null) {
       return this.user.getId();
     }
     return this.alternativeId;
   }
   
   public String getFirstName() { if (this.user != null) {
       return this.user.getFirstName();
     }
     return null;
   }
   
   public String getLastName() { if (this.user != null) {
       return this.user.getLastName();
     }
     return null;
   }
   
   public String getFullName() { if (this.user != null) {
       return getFirstName() + " " + getLastName();
     }
     return null;
   }
   
   public String getPassword() { return this.password; }
   
   public void setPassword(String password) {
     this.user.setPassword(password);
     this.password = password;
   }
   
   public boolean isUser() { return this.isUser; }
   
   public void setUser(boolean isUser) {
     this.isUser = isUser;
   }
   
   public boolean isAdmin() { return this.isAdmin; }
   
   public void setAdmin(boolean isAdmin) {
     this.isAdmin = isAdmin;
   }
   
   public void addSecurityRoleGroup(Group securityRoleGroup) { this.securityRoles.add(securityRoleGroup); }
   
   public List<Group> getSecurityRoles() {
     return this.securityRoles;
   }
   
   public List<Group> getGroups() { return this.groups; }
   
   public void addGroup(Group group) {
     this.groups.add(group);
   }
 }


