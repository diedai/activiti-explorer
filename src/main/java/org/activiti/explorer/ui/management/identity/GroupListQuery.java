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
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.GroupQuery;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class GroupListQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient IdentityService identityService;
   
   public GroupListQuery()
   {
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
   }
   
   public int size() {
     return (int)this.identityService.createGroupQuery().count();
   }
   
 
 
   public List<Item> loadItems(int start, int count)
   {
     List<Group> groups = ((GroupQuery)((GroupQuery)this.identityService.createGroupQuery().orderByGroupId().asc()).orderByGroupName().asc()).listPage(start, count);
     
     List<Item> groupListItems = new ArrayList();
     for (Group group : groups) {
       groupListItems.add(new GroupListItem(group));
     }
     return groupListItems;
   }
   
   public Item loadSingleResult(String id) {
     return new GroupListItem((Group)this.identityService.createGroupQuery().groupId(id).singleResult());
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   class GroupListItem extends PropertysetItem implements Comparable<GroupListItem>
   {
     private static final long serialVersionUID = 1L;
     
     public GroupListItem(Group group) {
       addItemProperty("id", new ObjectProperty(group.getId(), String.class));
       if (group.getName() != null) {
         addItemProperty("name", new ObjectProperty(group.getName() + " (" + group
           .getName() + ")", String.class));
       } else {
         addItemProperty("name", new ObjectProperty("(" + group.getId() + ")", String.class));
       }
     }
     
     public int compareTo(GroupListItem other) {
       String id = (String)getItemProperty("id").getValue();
       String otherId = (String)other.getItemProperty("id").getValue();
       return id.compareTo(otherId);
     }
   }
 }


