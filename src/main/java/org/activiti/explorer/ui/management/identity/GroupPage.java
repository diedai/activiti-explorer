 package org.activiti.explorer.ui.management.identity;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Table;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.management.ManagementPage;
 import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class GroupPage
   extends ManagementPage
 {
   private static final long serialVersionUID = 1L;
   protected String groupId;
   protected Table groupTable;
   protected LazyLoadingQuery groupListQuery;
   protected LazyLoadingContainer groupListContainer;
   
   public GroupPage()
   {
     ExplorerApp.get().setCurrentUriFragment(new UriFragment("group"));
   }
   
   public GroupPage(String groupId)
   {
     this.groupId = groupId;
   }
   
   protected void initUi()
   {
     super.initUi();
     
     if (this.groupId == null) {
       selectElement(0);
     } else {
       selectElement(this.groupListContainer.getIndexForObjectId(this.groupId));
     }
   }
   
   protected Table createList() {
     this.groupTable = new Table();
     
     this.groupTable.setEditable(false);
     this.groupTable.setImmediate(true);
     this.groupTable.setSelectable(true);
     this.groupTable.setNullSelectionAllowed(false);
     this.groupTable.setSortDisabled(true);
     this.groupTable.setSizeFull();
     
     this.groupListQuery = new GroupListQuery();
     this.groupListContainer = new LazyLoadingContainer(this.groupListQuery, 30);
     this.groupTable.setContainerDataSource(this.groupListContainer);
     
 
     this.groupTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.GROUP_22));
     this.groupTable.setColumnWidth("icon", 22);
     this.groupTable.addContainerProperty("name", String.class, null);
     this.groupTable.setColumnHeaderMode(-1);
     
 
     this.groupTable.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) { Item item = GroupPage.this.groupTable.getItem(event.getProperty().getValue());
         if (item != null) {
           String groupId = (String)item.getItemProperty("id").getValue();
           GroupPage.this.setDetailComponent(new GroupDetailPanel(GroupPage.this, groupId));
           
 
           ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "group", groupId }));
         }
         else
         {
           GroupPage.this.setDetailComponent(null);
           ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "group", GroupPage.this.groupId }));
         }
         
       }
     });
     return this.groupTable;
   }
   
   public void notifyGroupChanged(String groupId)
   {
     this.groupTable.removeAllItems();
     this.groupListContainer.removeAllItems();
     
 
     this.groupTable.select(Integer.valueOf(this.groupListContainer.getIndexForObjectId(groupId)));
   }
 }


