 package org.activiti.explorer.ui.management.identity;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Embedded;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 import org.activiti.explorer.ui.Images;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class GroupMembersQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient IdentityService identityService;
   protected String groupId;
   protected MemberShipChangeListener memberShipChangeListener;
   protected String sortby;
   protected boolean ascending;
   
   public GroupMembersQuery(String groupId, MemberShipChangeListener memberShipChangeListener)
   {
     this.groupId = groupId;
     this.memberShipChangeListener = memberShipChangeListener;
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
   }
   
   public int size() {
     return (int)this.identityService.createUserQuery().memberOfGroup(this.groupId).count();
   }
   
   public List<Item> loadItems(int start, int count) {
     UserQuery query = this.identityService.createUserQuery().memberOfGroup(this.groupId);
     
     if ((this.sortby == null) || ("id".equals(this.sortby))) {
       query.orderByUserId();
     } else if ("firstName".equals(this.sortby)) {
       query.orderByUserFirstName();
     } else if ("lastName".equals(this.sortby)) {
       query.orderByUserLastName();
     } else if ("email".equals(this.sortby)) {
       query.orderByUserEmail();
     }
     
     if ((this.sortby == null) || (this.ascending)) {
       query.asc();
     } else {
       query.desc();
     }
     
     List<User> users = query.listPage(start, count);
     
     List<Item> items = new ArrayList();
     for (User user : users) {
       items.add(new GroupMemberItem(user));
     }
     return items;
   }
   
   public Item loadSingleResult(String id) {
     throw new UnsupportedOperationException();
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     if (propertyIds.length > 0) {
       this.sortby = propertyIds[0].toString();
       this.ascending = ascending[0];
     }
   }
   
   class GroupMemberItem extends PropertysetItem
   {
     private static final long serialVersionUID = 1L;
     
     public GroupMemberItem(final User user)
     {
       Button idButton = new Button(user.getId());
       idButton.addStyleName("link");
       idButton.addListener(new Button.ClickListener() {
         public void buttonClick(ClickEvent event) {
           ExplorerApp.get().getViewManager().showUserPage(user.getId());
         }
       });
       addItemProperty("id", new ObjectProperty(idButton, Button.class));
       
 
       if (user.getFirstName() != null) {
         addItemProperty("firstName", new ObjectProperty(user.getFirstName(), String.class));
       }
       if (user.getLastName() != null) {
         addItemProperty("lastName", new ObjectProperty(user.getLastName(), String.class));
       }
       
 
       if (user.getEmail() != null) {
         addItemProperty("email", new ObjectProperty(user.getEmail(), String.class));
       }
       
 
       Embedded deleteIcon = new Embedded(null, Images.DELETE);
       deleteIcon.addStyleName("clickable");
       deleteIcon.addListener(new DeleteMembershipListener(GroupMembersQuery.this.identityService, user.getId(), GroupMembersQuery.this.groupId, GroupMembersQuery.this.memberShipChangeListener));
       addItemProperty("actions", new ObjectProperty(deleteIcon, Embedded.class));
     }
   }
 }


