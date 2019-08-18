 package org.activiti.explorer.ui.reports;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Table;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.AbstractTablePage;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SavedReportsPage
   extends AbstractTablePage
 {
   private static final long serialVersionUID = -5259331126409002997L;
   protected String reportId;
   protected Table reportTable;
   protected LazyLoadingQuery reportListQuery;
   protected LazyLoadingContainer reportListContainer;
   
   public SavedReportsPage(String reportId)
   {
     this.reportId = reportId;
   }
   
   public SavedReportsPage() {
     this(null);
   }
   
   protected Table createList() {
     this.reportTable = new Table();
     this.reportListQuery = new SavedReportsListQuery();
     this.reportListContainer = new LazyLoadingContainer(this.reportListQuery);
     this.reportTable.setContainerDataSource(this.reportListContainer);
     
 
     this.reportTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.REPORT_22));
     this.reportTable.setColumnWidth("icon", 22);
     
     this.reportTable.addContainerProperty("name", String.class, null);
     this.reportTable.setColumnHeaderMode(-1);
     
 
 
     this.reportTable.addListener(new Property.ValueChangeListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) {
         Item item = SavedReportsPage.this.reportTable.getItem(event.getProperty().getValue());
         if (item != null) {
           String historicProcessInstanceId = (String)item.getItemProperty("id").getValue();
           SavedReportsPage.this.setDetailComponent(new SavedReportDetailPanel(historicProcessInstanceId));
           
 
           ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "savedReport", historicProcessInstanceId }));
 
         }
         else
         {
           SavedReportsPage.this.setDetailComponent(null);
           ExplorerApp.get().setCurrentUriFragment(new UriFragment("savedReport"));
         }
         
       }
       
     });
     return this.reportTable;
   }
   
   protected ToolBar createMenuBar() {
     return new ReportsMenuBar();
   }
   
   protected void initUi()
   {
     super.initUi();
     
     if (this.reportId != null) {
       selectElement(this.reportListContainer.getIndexForObjectId(this.reportId));
     } else {
       selectElement(0);
     }
   }
 }


