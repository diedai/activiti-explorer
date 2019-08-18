 package org.activiti.editor.ui;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Model;
 import org.activiti.engine.repository.ModelQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.AbstractTablePage;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.process.ProcessMenuBar;
 import org.activiti.explorer.ui.process.listener.ImportModelClickListener;
 import org.activiti.explorer.ui.process.listener.NewModelClickListener;
 import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class EditorProcessDefinitionPage
   extends AbstractTablePage
 {
   private static final long serialVersionUID = 1L;
   protected transient RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   
   protected String modelId;
   protected EditorProcessDefinitionDetailPanel detailPanel;
   
   public EditorProcessDefinitionPage()
   {
     ExplorerApp.get().setCurrentUriFragment(new UriFragment("processmodel"));
   }
   
 
 
 
   public EditorProcessDefinitionPage(String modelId)
   {
     this();
     this.modelId = modelId;
   }
   
   protected void initUi()
   {
     super.initUi();
     if (this.modelId == null) {
       this.table.select(this.table.firstItemId());
     } else {
       this.table.select(this.modelId);
     }
   }
   
   protected ToolBar createMenuBar()
   {
     ProcessMenuBar menuBar = new ProcessMenuBar();
     Button newModelButton = new Button(ExplorerApp.get().getI18nManager().getMessage("process.new"));
     newModelButton.addListener((ClickListener) new NewModelClickListener());
     menuBar.addButton(newModelButton);
     Button importModelButton = new Button(ExplorerApp.get().getI18nManager().getMessage("process.import"));
     importModelButton.addListener((ClickListener) new ImportModelClickListener());
     menuBar.addButton(importModelButton);
     return menuBar;
   }
   
   protected Table createList()
   {
     Table processDefinitionTable = new Table();
     processDefinitionTable.addStyleName("proc-def-list");
     
 
     processDefinitionTable.setEditable(false);
     processDefinitionTable.setImmediate(true);
     processDefinitionTable.setSelectable(true);
     processDefinitionTable.setNullSelectionAllowed(false);
     processDefinitionTable.setSortDisabled(true);
     processDefinitionTable.setSizeFull();
     
 
     processDefinitionTable.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) {
         EditorProcessDefinitionPage.this.showProcessDefinitionDetail((String)event.getProperty().getValue());
       }
       
 
     });
     processDefinitionTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.PROCESS_22));
     processDefinitionTable.setColumnWidth("icon", 22);
     
     processDefinitionTable.addContainerProperty("name", String.class, null);
     processDefinitionTable.setColumnHeaderMode(-1);
     
     List<Model> modelList = this.repositoryService.createModelQuery().list();
     for (Model modelData : modelList) {
       Item item = processDefinitionTable.addItem(modelData.getId());
       item.getItemProperty("name").setValue(modelData.getName());
     }
     
 
     return processDefinitionTable;
   }
   
   protected void showProcessDefinitionDetail(String selectedModelId) {
     this.detailPanel = new EditorProcessDefinitionDetailPanel(selectedModelId, this);
     setDetailComponent(this.detailPanel);
     changeUrl("" + selectedModelId);
   }
   
   protected void changeUrl(String processDefinitionId) {
     UriFragment processDefinitionFragment = new UriFragment(new String[] { "processmodel", processDefinitionId });
     ExplorerApp.get().setCurrentUriFragment(processDefinitionFragment);
   }
 }


