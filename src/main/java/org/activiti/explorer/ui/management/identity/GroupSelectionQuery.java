 package org.activiti.explorer.ui.management.identity;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import java.util.ArrayList;
 import java.util.HashSet;
 import java.util.List;
 import java.util.Set;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.GroupQuery;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class GroupSelectionQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient IdentityService identityService;
   protected String userId;
   
   public GroupSelectionQuery(IdentityService identityService, String userId)
   {
     this.identityService = identityService;
     this.userId = userId;
   }
   
   public int size()
   {
     return (int)(this.identityService.createGroupQuery().count() - this.identityService.createGroupQuery().groupMember(this.userId).count());
   }
   
   public List<Item> loadItems(int start, int count) {
     List<Item> groupItems = new ArrayList();
     Set<String> currentGroups = getCurrentGroups();
     
     int nrFound = 0;
     int tries = 0;
     while ((nrFound < count) && (tries < 5))
     {
 
 
 
 
       List<Group> groups = ((GroupQuery)((GroupQuery)((GroupQuery)this.identityService.createGroupQuery().orderByGroupType().asc()).orderByGroupId().asc()).orderByGroupName().asc()).listPage(start + tries * count, count);
       
       for (Group group : groups) {
         if (!currentGroups.contains(group.getId())) {
           nrFound++;
           groupItems.add(new GroupSelectionItem(group));
         }
       }
       
       tries++;
     }
     
     return groupItems;
   }
   
   protected Set<String> getCurrentGroups() {
     Set<String> groupIds = new HashSet();
     List<Group> currentGroups = this.identityService.createGroupQuery().groupMember(this.userId).list();
     for (Group group : currentGroups) {
       groupIds.add(group.getId());
     }
     return groupIds;
   }
   
   public Item loadSingleResult(String id) {
     throw new UnsupportedOperationException();
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   class GroupSelectionItem extends PropertysetItem
   {
     private static final long serialVersionUID = 1L;
     
     public GroupSelectionItem(Group group) {
       addItemProperty("id", new ObjectProperty(group.getId(), String.class));
       if (group.getName() != null) {
         addItemProperty("name", new ObjectProperty(group.getName(), String.class));
       }
       if (group.getType() != null) {
         addItemProperty("type", new ObjectProperty(group.getType(), String.class));
       }
     }
   }
 }


