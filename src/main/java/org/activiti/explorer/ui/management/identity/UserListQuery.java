 package org.activiti.explorer.ui.management.identity;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UserListQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient IdentityService identityService;
   
   public UserListQuery()
   {
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
   }
   
   public int size() {
     return (int)this.identityService.createUserQuery().count();
   }
   
 
 
 
   public List<Item> loadItems(int start, int count)
   {
     List<User> users = ((UserQuery)((UserQuery)((UserQuery)this.identityService.createUserQuery().orderByUserFirstName().asc()).orderByUserLastName().asc()).orderByUserId().asc()).listPage(start, count);
     
     List<Item> userListItems = new ArrayList();
     for (User user : users) {
       userListItems.add(new UserListItem(user));
     }
     return userListItems;
   }
   
   public Item loadSingleResult(String id) {
     return new UserListItem((User)this.identityService.createUserQuery().userId(id).singleResult());
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   class UserListItem extends PropertysetItem implements Comparable<UserListItem>
   {
     private static final long serialVersionUID = 1L;
     
     public UserListItem(User user) {
       addItemProperty("id", new ObjectProperty(user.getId(), String.class));
       addItemProperty("name", new ObjectProperty(user.getFirstName() + " " + user
         .getLastName() + " (" + user.getId() + ")", String.class));
     }
     
     public int compareTo(UserListItem other)
     {
       String name = (String)getItemProperty("name").getValue();
       String otherName = (String)other.getItemProperty("name").getValue();
       
       int comparison = name.compareTo(otherName);
       if (comparison != 0) {
         return comparison;
       }
       String id = (String)getItemProperty("id").getValue();
       String otherId = (String)other.getItemProperty("id").getValue();
       return id.compareTo(otherId);
     }
   }
 }


