 package org.activiti.explorer.ui.management.processinstance;
 
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
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessInstancePage
   extends ManagementPage
 {
   private static final long serialVersionUID = 1L;
   protected LazyLoadingContainer processInstanceContainer;
   protected String processInstanceId;
   
   public ProcessInstancePage()
   {
     ExplorerApp.get().setCurrentUriFragment(new UriFragment("deployment"));
   }
   
   public ProcessInstancePage(String processInstanceId)
   {
     this();
     this.processInstanceId = processInstanceId;
   }
   
   protected void initUi()
   {
     super.initUi();
     
     if (this.processInstanceId == null) {
       selectElement(0);
     } else {
       selectElement(this.processInstanceContainer.getIndexForObjectId(this.processInstanceId));
     }
   }
   
   protected Table createList() {
     final Table table = new Table();
     
     LazyLoadingQuery query = new ProcessInstanceListQuery();
     this.processInstanceContainer = new LazyLoadingContainer(query);
     table.setContainerDataSource(this.processInstanceContainer);
     
     table.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) { Item item = table.getItem(event.getProperty().getValue());
         if (item != null) {
           String processInstanceId = (String)item.getItemProperty("id").getValue();
           ProcessInstancePage.this.setDetailComponent(new AlfrescoProcessInstanceDetailPanel(processInstanceId, ProcessInstancePage.this));
           
 
           ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "processinstance", processInstanceId }));
         }
         else
         {
           ProcessInstancePage.this.setDetailComponent(null);
           ExplorerApp.get().setCurrentUriFragment(new UriFragment("processinstance"));
         }
         
       }
       
     });
     table.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.PROCESS_22));
     table.setColumnWidth("icon", 22);
     
     table.addContainerProperty("name", String.class, null);
     table.setColumnHeaderMode(-1);
     
     return table;
   }
 }


