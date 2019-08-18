 package org.activiti.explorer.ui.management.job;
 
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
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class JobPage
   extends ManagementPage
 {
   private static final long serialVersionUID = 1L;
   protected String jobId;
   protected Table jobTable;
   protected LazyLoadingContainer jobListContainer;
   
   public JobPage()
   {
     ExplorerApp.get().setCurrentUriFragment(new UriFragment("job"));
   }
   
   public JobPage(String jobId)
   {
     this();
     this.jobId = jobId;
   }
   
   protected void initUi()
   {
     super.initUi();
     
     if (this.jobId == null) {
       selectElement(0);
     } else {
       selectElement(this.jobListContainer.getIndexForObjectId(this.jobId));
     }
   }
   
   protected Table createList()
   {
     final Table jobTable = new Table();
     
     LazyLoadingQuery jobListQuery = new JobListQuery();
     this.jobListContainer = new LazyLoadingContainer(jobListQuery, 30);
     jobTable.setContainerDataSource(this.jobListContainer);
     
 
     jobTable.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 8811553575319455854L;
       
       public void valueChange(Property.ValueChangeEvent event) { Item item = jobTable.getItem(event.getProperty().getValue());
         if (item != null) {
           String jobId = (String)item.getItemProperty("id").getValue();
           
           JobPage.this.setDetailComponent(new JobDetailPanel(jobId, JobPage.this));
           
           ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "job", jobId }));
         }
         else
         {
           JobPage.this.setDetailComponent(null);
           ExplorerApp.get().setCurrentUriFragment(new UriFragment("job"));
         }
         
       }
       
     });
     jobTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.JOB_22));
     jobTable.setColumnWidth("icon", 22);
     
     jobTable.addContainerProperty("name", String.class, null);
     jobTable.setColumnHeaderMode(-1);
     
     return jobTable;
   }
   
   public void refreshCurrentJobDetails() {
     if (this.table.getValue() != null) {
       Item selectedJob = this.table.getItem(this.table.getValue());
       setDetailComponent(new JobDetailPanel((String)selectedJob.getItemProperty("id").getValue(), this));
     }
   }
 }


