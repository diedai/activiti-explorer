 package org.activiti.explorer.ui.login;
 
 import java.util.List;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.http.HttpSession;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.GroupQuery;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.engine.impl.identity.Authentication;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.identity.LoggedInUserImpl;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DefaultLoginHandler
   implements LoginHandler
 {
   private transient IdentityService identityService;
   
   public LoggedInUserImpl authenticate(String userName, String password)
   {
     LoggedInUserImpl loggedInUser = null;
     try
     {
       if (this.identityService.checkPassword(userName, password)) {
         User user = (User)this.identityService.createUserQuery().userId(userName).singleResult();
         
         loggedInUser = new LoggedInUserImpl(user, password);
         List<Group> groups = this.identityService.createGroupQuery().groupMember(user.getId()).list();
         for (Group group : groups)
         {
           if ("security-role".equals(group.getType())) {
             loggedInUser.addSecurityRoleGroup(group);
             if ("user".equals(group.getId())) {
               loggedInUser.setUser(true);
             }
             if ("admin".equals(group.getId())) {
               loggedInUser.setAdmin(true);
             }
           } else if ((ExplorerApp.get().getAdminGroups() != null) && 
             (ExplorerApp.get().getAdminGroups().contains(group.getId()))) {
             loggedInUser.addSecurityRoleGroup(group);
             loggedInUser.setAdmin(true);
           } else if ((ExplorerApp.get().getUserGroups() != null) && 
             (ExplorerApp.get().getUserGroups().contains(group.getId()))) {
             loggedInUser.addSecurityRoleGroup(group);
             loggedInUser.setUser(true);
           } else {
             loggedInUser.addGroup(group);
           }
         }
       }
     }
     catch (Exception localException) {}
     
 
 
 
     return loggedInUser;
   }
   
   public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
     if ((ExplorerApp.get().getLoggedInUser() != null) && (request.getSession(false) != null))
     {
       request.getSession().setAttribute("_currentUser", ExplorerApp.get().getLoggedInUser().getId());
     }
   }
   
 
   public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {}
   
 
   public LoggedInUser authenticate(HttpServletRequest request, HttpServletResponse response)
   {
     return null;
   }
   
   public void logout(LoggedInUser userToLogout)
   {
     Authentication.setAuthenticatedUserId(null);
   }
   
   public void setIdentityService(IdentityService identityService) {
     this.identityService = identityService;
   }
 }


