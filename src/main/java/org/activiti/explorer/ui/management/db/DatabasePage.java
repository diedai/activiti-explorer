 package org.activiti.explorer.ui.management.db;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.terminal.Resource;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.Table;
 import java.util.TreeMap;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.management.ManagementPage;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DatabasePage
   extends ManagementPage
 {
   private static final long serialVersionUID = 1L;
   protected transient ManagementService managementService = ProcessEngines.getDefaultProcessEngine().getManagementService();
   protected String tableName;
   
   public DatabasePage() {
     ExplorerApp.get().setCurrentUriFragment(new UriFragment("database"));
   }
   
   public DatabasePage(String tableName)
   {
     this();
     this.tableName = tableName;
   }
   
   protected void initUi()
   {
     super.initUi();
     
     populateTableList();
     if (this.tableName == null) {
       selectElement(0);
     } else {
       this.table.select(this.tableName);
     }
   }
   
   protected Table createList()
   {
     Table tableList = new Table();
     
 
     tableList.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 8811553575319455854L;
       
       public void valueChange(Property.ValueChangeEvent event) {
         String tableName = (String)event.getProperty().getValue();
         DatabasePage.this.setDetailComponent(new DatabaseDetailPanel(tableName));
         
 
         ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "database", tableName }));
 
       }
       
 
     });
     tableList.addContainerProperty("icon", Embedded.class, null);
     tableList.setColumnWidth("icon", 22);
     tableList.addContainerProperty("tableName", String.class, null);
     tableList.setColumnHeaderMode(-1);
     
     return tableList;
   }
   
   protected void populateTableList() {
     TreeMap<String, Long> tables = new TreeMap(this.managementService.getTableCount());
     for (String tableName : tables.keySet()) {
       Item item = this.table.addItem(tableName);
       item.getItemProperty("icon").setValue(determineTableIcon(tableName));
       item.getItemProperty("tableName").setValue(tableName + " (" + tables.get(tableName) + ")");
     }
   }
   
   protected Embedded determineTableIcon(String tableName) {
     Resource image = null;
     if (tableName.contains("ACT_HI")) {
       image = Images.DATABASE_HISTORY;
     } else if (tableName.contains("ACT_RU")) {
       image = Images.DATABASE_RUNTIME;
     } else if (tableName.contains("ACT_RE")) {
       image = Images.DATABASE_REPOSITORY;
     } else if (tableName.contains("ACT_ID")) {
       image = Images.DATABASE_IDENTITY;
     } else {
       image = Images.DATABASE_22;
     }
     return new Embedded(null, image);
   }
 }


