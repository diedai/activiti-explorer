 package org.activiti.explorer.ui.management.processdefinition;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Table;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.management.ManagementPage;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SuspendedProcessDefinitionPage
   extends ManagementPage
 {
   private static final long serialVersionUID = 1L;
   protected String processDefinitionId;
   protected Table processDefinitionTable;
   protected LazyLoadingQuery processDefinitionListQuery;
   protected LazyLoadingContainer processDefinitionListContainer;
   
   public SuspendedProcessDefinitionPage()
   {
     ExplorerApp.get().setCurrentUriFragment(new UriFragment("suspendedProcessDefinition"));
   }
   
   public SuspendedProcessDefinitionPage(String processDefinitionId)
   {
     this.processDefinitionId = processDefinitionId;
   }
   
   protected void initUi()
   {
     super.initUi();
     
     if (this.processDefinitionId == null) {
       selectElement(0);
     } else {
       selectElement(this.processDefinitionListContainer.getIndexForObjectId(this.processDefinitionId));
     }
   }
   
   protected Table createList() {
     this.processDefinitionTable = new Table();
     
     this.processDefinitionListQuery = new SuspendedProcessDefinitionListQuery();
     this.processDefinitionListContainer = new LazyLoadingContainer(this.processDefinitionListQuery);
     this.processDefinitionTable.setContainerDataSource(this.processDefinitionListContainer);
     
 
     this.processDefinitionTable.addContainerProperty("name", String.class, null);
     this.processDefinitionTable.setColumnHeaderMode(-1);
     
 
     this.processDefinitionTable.addListener(new Property.ValueChangeListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) {
         Item item = SuspendedProcessDefinitionPage.this.processDefinitionTable.getItem(event.getProperty().getValue());
         if (item != null) {
           String processDefinitionId = (String)item.getItemProperty("id").getValue();
           SuspendedProcessDefinitionPage.this.setDetailComponent(new SuspendedProcessDefinitionDetailPanel(processDefinitionId, SuspendedProcessDefinitionPage.this));
           
 
           ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "suspendedProcessDefinition", processDefinitionId }));
 
         }
         else
         {
           SuspendedProcessDefinitionPage.this.setDetailComponent(null);
           ExplorerApp.get().setCurrentUriFragment(new UriFragment("suspendedProcessDefinition"));
         }
         
       }
       
     });
     return this.processDefinitionTable;
   }
 }


