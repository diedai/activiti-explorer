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
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UserPage
   extends ManagementPage
 {
   private static final long serialVersionUID = 1L;
   protected String userId;
   protected Table userTable;
   protected LazyLoadingQuery userListQuery;
   protected LazyLoadingContainer userListContainer;
   
   public UserPage()
   {
     ExplorerApp.get().setCurrentUriFragment(new UriFragment("user"));
   }
   
   public UserPage(String userId)
   {
     this.userId = userId;
   }
   
   protected void initUi()
   {
     super.initUi();
     
     if (this.userId == null) {
       selectElement(0);
     } else {
       selectElement(this.userListContainer.getIndexForObjectId(this.userId));
     }
   }
   
   protected Table createList() {
     this.userTable = new Table();
     
     this.userListQuery = new UserListQuery();
     this.userListContainer = new LazyLoadingContainer(this.userListQuery, 30);
     this.userTable.setContainerDataSource(this.userListContainer);
     
 
     this.userTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.USER_22));
     this.userTable.setColumnWidth("icon", 22);
     this.userTable.addContainerProperty("name", String.class, null);
     this.userTable.setColumnHeaderMode(-1);
     
 
     this.userTable.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) { Item item = UserPage.this.userTable.getItem(event.getProperty().getValue());
         if (item != null) {
           String userId = (String)item.getItemProperty("id").getValue();
           UserPage.this.setDetailComponent(new UserDetailPanel(UserPage.this, userId));
           
 
           ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "user", userId }));
         }
         else
         {
           UserPage.this.setDetailComponent(null);
           ExplorerApp.get().setCurrentUriFragment(new UriFragment("user"));
         }
         
       }
     });
     return this.userTable;
   }
   
 
 
 
   public void notifyUserChanged(String userId)
   {
     this.userTable.removeAllItems();
     this.userListContainer.removeAllItems();
     
     this.userTable.select(Integer.valueOf(this.userListContainer.getIndexForObjectId(userId)));
   }
 }


