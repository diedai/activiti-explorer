 package org.activiti.explorer.ui.management.admin;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Table;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.management.ManagementPage;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AdministrationPage
   extends ManagementPage
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected String managementId;
   protected Table managementTable;
   
   public AdministrationPage()
   {
     ExplorerApp.get().setCurrentUriFragment(new UriFragment("admin_management"));
     
     this.i18nManager = ExplorerApp.get().getI18nManager();
   }
   
   public AdministrationPage(String managementId) {
     this.managementId = managementId;
   }
   
   protected void initUi()
   {
     super.initUi();
     int index = 0;
     if (this.managementId != null) {
       index = Integer.valueOf(this.managementId).intValue();
     }
     this.managementTable.select(Integer.valueOf(index));
     this.managementTable.setCurrentPageFirstItemId(Integer.valueOf(index));
   }
   
   protected Table createList() {
     this.managementTable = new Table();
     
     this.managementTable.setEditable(false);
     this.managementTable.setImmediate(true);
     this.managementTable.setSelectable(true);
     this.managementTable.setNullSelectionAllowed(false);
     this.managementTable.setSortDisabled(true);
     this.managementTable.setSizeFull();
     
 
     this.managementTable.addContainerProperty("name", String.class, null);
     this.managementTable.setColumnHeaderMode(-1);
     
     this.managementTable.addItem(new String[] { this.i18nManager.getMessage("admin.menu.running") }, Integer.valueOf(0));
     this.managementTable.addItem(new String[] { this.i18nManager.getMessage("admin.menu.completed") }, Integer.valueOf(1));
     this.managementTable.addItem(new String[] { this.i18nManager.getMessage("admin.menu.database") }, Integer.valueOf(2));
     
 
     this.managementTable.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) { Item item = AdministrationPage.this.managementTable.getItem(event.getProperty().getValue());
         if (item != null)
         {
           if ("0".equals(event.getProperty().getValue().toString())) {
             AdministrationPage.this.setDetailComponent(new AdminRunningInstancesPanel());
           } else if ("1".equals(event.getProperty().getValue().toString())) {
             AdministrationPage.this.setDetailComponent(new AdminCompletedInstancesPanel());
           } else if ("2".equals(event.getProperty().getValue().toString())) {
             AdministrationPage.this.setDetailComponent(new AdminDatabaseSettingsPanel());
           }
           
 
           ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "admin_management", event
             .getProperty().getValue().toString() }));
         }
         else {
           AdministrationPage.this.setDetailComponent(null);
           ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "admin_management", AdministrationPage.this.managementId }));
         }
         
       }
     });
     return this.managementTable;
   }
   
   public void notifyGroupChanged(String managementId)
   {
     this.managementTable.removeAllItems();
     
 
     this.managementTable.select(Integer.valueOf(managementId));
   }
 }


