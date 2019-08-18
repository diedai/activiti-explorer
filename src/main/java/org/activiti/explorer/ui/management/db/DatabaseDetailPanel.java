 package org.activiti.explorer.ui.management.db;
 
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Table;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.management.TableMetaData;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DatabaseDetailPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected transient ManagementService managementService;
   protected I18nManager i18nManager;
   protected String tableName;
   
   public DatabaseDetailPanel(String tableName)
   {
     this.tableName = tableName;
     this.managementService = ProcessEngines.getDefaultProcessEngine().getManagementService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     addStyleName("white");
     setSizeFull();
     
     addTableName();
     addTableData();
   }
   
   protected void addTableName() {
     HorizontalLayout header = new HorizontalLayout();
     header.setWidth(100.0F, 8);
     header.addStyleName("title-block");
     header.setSpacing(true);
     
 
     Embedded image = new Embedded(null, Images.DATABASE_50);
     header.addComponent(image);
     header.setComponentAlignment(image, Alignment.MIDDLE_LEFT);
     header.setMargin(false, false, true, false);
     
     Label name = new Label(this.tableName);
     name.addStyleName("h2");
     header.addComponent(name);
     
     header.setExpandRatio(name, 1.0F);
     header.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
     addDetailComponent(header);
     
     Label spacer = new Label();
     spacer.setWidth(100.0F, 8);
     spacer.addStyleName("block-holder");
     addDetailComponent(spacer);
   }
   
   protected void addTableData() {
     LazyLoadingQuery lazyLoadingQuery = new TableDataQuery(this.tableName, this.managementService);
     LazyLoadingContainer lazyLoadingContainer = new LazyLoadingContainer(lazyLoadingQuery, 30);
     Table data;
     if (lazyLoadingContainer.size() > 0)
     {
       data = new Table();
       data.setContainerDataSource(lazyLoadingContainer);
       data.setEditable(false);
       data.setSelectable(true);
       data.setColumnReorderingAllowed(true);
       if (lazyLoadingQuery.size() < 10) {
         data.setPageLength(0);
       } else {
         data.setPageLength(10);
       }
       addDetailComponent(data);
       
       data.setWidth(100.0F, 8);
       data.setHeight(100.0F, 8);
       data.addStyleName("database-table");
       setDetailExpandRatio(data, 1.0F);
       
 
       TableMetaData metaData = this.managementService.getTableMetaData(this.tableName);
       for (String columnName : metaData.getColumnNames()) {
         data.addContainerProperty(columnName, String.class, null);
       }
     }
     else {
       Label noDataLabel = new Label(this.i18nManager.getMessage("database.no.rows"));
       noDataLabel.addStyleName("light");
       addDetailComponent(noDataLabel);
       setDetailExpandRatio(noDataLabel, 1.0F);
     }
   }
 }


