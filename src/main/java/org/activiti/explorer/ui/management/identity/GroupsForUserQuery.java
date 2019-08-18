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
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.GroupQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 import org.activiti.explorer.ui.Images;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class GroupsForUserQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient IdentityService identityService;
   protected String userId;
   protected UserDetailPanel userDetailPanel;
   
   public GroupsForUserQuery(IdentityService identityService, UserDetailPanel userDetailPanel, String userId)
   {
     this.identityService = identityService;
     this.userDetailPanel = userDetailPanel;
     this.userId = userId;
   }
   
   public int size() {
     return (int)this.identityService.createGroupQuery().groupMember(this.userId).count();
   }
   
 
 
 
 
   public List<Item> loadItems(int start, int count)
   {
     List<Group> groups = ((GroupQuery)((GroupQuery)((GroupQuery)this.identityService.createGroupQuery().groupMember(this.userId).orderByGroupType().asc()).orderByGroupId().asc()).orderByGroupName().asc()).list();
     
     List<Item> groupItems = new ArrayList();
     for (Group group : groups) {
       groupItems.add(new GroupItem(group));
     }
     return groupItems;
   }
   
   public Item loadSingleResult(String id) {
     throw new UnsupportedOperationException();
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   class GroupItem extends PropertysetItem
   {
     private static final long serialVersionUID = 1L;
     
     public GroupItem(final Group group) {
       Button idButton = new Button(group.getId());
       idButton.addStyleName("link");
       idButton.addListener(new Button.ClickListener() {
         public void buttonClick(ClickEvent event) {
           ExplorerApp.get().getViewManager().showGroupPage(group.getId());
         }
       });
       addItemProperty("id", new ObjectProperty(idButton, Button.class));
       
       if (group.getName() != null) {
         addItemProperty("name", new ObjectProperty(group.getName(), String.class));
       }
       if (group.getType() != null) {
         addItemProperty("type", new ObjectProperty(group.getType(), String.class));
       }
       
       Embedded deleteIcon = new Embedded(null, Images.DELETE);
       deleteIcon.addStyleName("clickable");
       deleteIcon.addListener(new DeleteMembershipListener(GroupsForUserQuery.this.identityService, GroupsForUserQuery.this.userId, group.getId(), GroupsForUserQuery.this.userDetailPanel));
       addItemProperty("actions", new ObjectProperty(deleteIcon, Embedded.class));
     }
   }
 }


