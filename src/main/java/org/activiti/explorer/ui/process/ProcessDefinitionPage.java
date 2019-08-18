 package org.activiti.explorer.ui.process;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Table;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.form.StartFormData;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.AbstractTablePage;
 import org.activiti.explorer.ui.ComponentFactory;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessDefinitionPage
   extends AbstractTablePage
 {
   private static final long serialVersionUID = 1L;
   protected transient RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   
   protected String processDefinitionId;
   
   protected LazyLoadingContainer processDefinitionContainer;
   protected Table processDefinitionTable;
   protected ProcessDefinitionDetailPanel detailPanel;
   private ProcessDefinitionFilter definitionFilter;
   
   public ProcessDefinitionPage()
   {
     ExplorerApp.get().setCurrentUriFragment(new UriFragment("process"));
     
 
 
 
     ComponentFactory<ProcessDefinitionFilter> factory = ExplorerApp.get().getComponentFactory(ProcessDefinitionFilterFactory.class);
     this.definitionFilter = ((ProcessDefinitionFilter)factory.create());
   }
   
 
 
 
   public ProcessDefinitionPage(String processDefinitionId)
   {
     this();
     this.processDefinitionId = processDefinitionId;
   }
   
   protected void initUi()
   {
     super.initUi();
     if (this.processDefinitionId == null) {
       selectElement(0);
     } else {
       selectElement(this.processDefinitionContainer.getIndexForObjectId(this.processDefinitionId));
     }
   }
   
   protected ToolBar createMenuBar()
   {
     return new ProcessMenuBar();
   }
   
   protected Table createList()
   {
     final Table processDefinitionTable = new Table();
     processDefinitionTable.addStyleName("proc-def-list");
     
 
     processDefinitionTable.setEditable(false);
     processDefinitionTable.setImmediate(true);
     processDefinitionTable.setSelectable(true);
     processDefinitionTable.setNullSelectionAllowed(false);
     processDefinitionTable.setSortDisabled(true);
     processDefinitionTable.setSizeFull();
     
 
     LazyLoadingQuery lazyLoadingQuery = new ProcessDefinitionListQuery(this.repositoryService, this.definitionFilter);
     this.processDefinitionContainer = new LazyLoadingContainer(lazyLoadingQuery, 30);
     processDefinitionTable.setContainerDataSource(this.processDefinitionContainer);
     
 
     processDefinitionTable.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) {
         Item item = processDefinitionTable.getItem(event.getProperty().getValue());
         String processDefinitionId = (String)item.getItemProperty("id").getValue();
         ProcessDefinitionPage.this.showProcessDefinitionDetail(processDefinitionId);
       }
       
 
     });
     processDefinitionTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.PROCESS_22));
     processDefinitionTable.setColumnWidth("icon", 22);
     
     processDefinitionTable.addContainerProperty("name", String.class, null);
     processDefinitionTable.setColumnHeaderMode(-1);
     
     return processDefinitionTable;
   }
   
   protected void showProcessDefinitionDetail(String processDefinitionId) {
     this.detailPanel = new ProcessDefinitionDetailPanel(processDefinitionId, this);
     setDetailComponent(this.detailPanel);
     changeUrl(processDefinitionId);
   }
   
   protected void changeUrl(String processDefinitionId) {
     UriFragment processDefinitionFragment = new UriFragment(new String[] { "process", processDefinitionId });
     ExplorerApp.get().setCurrentUriFragment(processDefinitionFragment);
   }
   
   public void showStartForm(ProcessDefinition processDefinition, StartFormData startFormData) {
     if (this.detailPanel != null) {
       showProcessDefinitionDetail(processDefinition.getId());
     }
     this.detailPanel.showProcessStartForm(startFormData);
   }
 }


