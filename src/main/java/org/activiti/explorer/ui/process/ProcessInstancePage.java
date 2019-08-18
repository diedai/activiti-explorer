 package org.activiti.explorer.ui.process;
 
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
 import org.activiti.explorer.ui.management.processinstance.ProcessInstanceDetailPanel;
 import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class ProcessInstancePage
   extends AbstractTablePage
 {
   private static final long serialVersionUID = 1L;
   protected LazyLoadingContainer processInstanceListContainer;
   protected LazyLoadingQuery lazyLoadingQuery;
   
   protected ToolBar createMenuBar()
   {
     return new ProcessMenuBar();
   }
   
   protected Table createList()
   {
     final Table processInstanceTable = new Table();
     processInstanceTable.addStyleName("proc-inst-list");
     
 
     processInstanceTable.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 8811553575319455854L;
       
       public void valueChange(Property.ValueChangeEvent event) { Item item = processInstanceTable.getItem(event.getProperty().getValue());
         if (item != null) {
           String processInstanceId = (String)item.getItemProperty("id").getValue();
           ProcessInstancePage.this.setDetailComponent(new ProcessInstanceDetailPanel(processInstanceId, ProcessInstancePage.this));
           
           UriFragment taskFragment = ProcessInstancePage.this.getUriFragment(processInstanceId);
           ExplorerApp.get().setCurrentUriFragment(taskFragment);
         }
         else {
           ProcessInstancePage.this.setDetailComponent(null);
           UriFragment taskFragment = ProcessInstancePage.this.getUriFragment(null);
           ExplorerApp.get().setCurrentUriFragment(taskFragment);
         }
         
       }
     });
     this.lazyLoadingQuery = createLazyLoadingQuery();
     this.processInstanceListContainer = new LazyLoadingContainer(this.lazyLoadingQuery, 30);
     processInstanceTable.setContainerDataSource(this.processInstanceListContainer);
     
 
     processInstanceTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.PROCESS_22));
     processInstanceTable.setColumnWidth("icon", 22);
     
     processInstanceTable.addContainerProperty("name", String.class, null);
     processInstanceTable.setColumnHeaderMode(-1);
     
     return processInstanceTable;
   }
   
   protected abstract LazyLoadingQuery createLazyLoadingQuery();
   
   protected abstract UriFragment getUriFragment(String paramString);
 }


