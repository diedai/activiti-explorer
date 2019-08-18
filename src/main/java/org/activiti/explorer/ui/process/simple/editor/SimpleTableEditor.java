 package org.activiti.explorer.ui.process.simple.editor;
 
 import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.terminal.StreamResource;
 import com.vaadin.terminal.StreamResource.StreamSource;
 import com.vaadin.ui.AbstractSelect;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.TextField;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.OutputStreamWriter;
 import java.util.List;
 import java.util.UUID;
 import org.activiti.engine.ProcessEngineConfiguration;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.impl.ProcessEngineImpl;
 import org.activiti.engine.repository.Model;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.AbstractPage;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.ToolBar;
import org.activiti.explorer.ui.custom.ToolbarEntry;
import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
 import org.activiti.explorer.ui.process.simple.editor.table.TaskTable;
 import org.activiti.image.ProcessDiagramGenerator;
 import org.activiti.workflow.simple.converter.WorkflowDefinitionConversion;
 import org.activiti.workflow.simple.converter.WorkflowDefinitionConversionFactory;
 import org.activiti.workflow.simple.converter.json.SimpleWorkflowJsonConverter;
 import org.activiti.workflow.simple.definition.HumanStepDefinition;
 import org.activiti.workflow.simple.definition.ListStepDefinition;
 import org.activiti.workflow.simple.definition.ParallelStepsDefinition;
 import org.activiti.workflow.simple.definition.StepDefinition;
 import org.activiti.workflow.simple.definition.StepDefinitionContainer;
 import org.activiti.workflow.simple.definition.WorkflowDefinition;
 import org.apache.commons.io.IOUtils;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SimpleTableEditor
   extends AbstractPage
 {
   private static final long serialVersionUID = -4430424035744622992L;
   private static final Logger logger = LoggerFactory.getLogger(SimpleTableEditor.class);
   
   private static final String KEY_EDITOR = "editor";
   
   private static final String KEY_PREVIEW = "preview";
   
   protected String workflowName;
   
   protected String description;
   
   protected String modelId;
   
   protected WorkflowDefinition workflowDefinition;
   
   protected DetailPanel mainLayout;
   
   protected GridLayout editorGrid;
   
   protected TextField nameField;
   protected TextField descriptionField;
   protected TaskTable taskTable;
   protected Panel imagePanel;
   
   public SimpleTableEditor(String workflowName, String description)
   {
     this.workflowName = workflowName;
     this.description = description;
   }
   
 
 
   public SimpleTableEditor(String modelId, WorkflowDefinition workflowDefinition)
   {
     this(workflowDefinition.getName(), workflowDefinition.getDescription());
     this.modelId = modelId;
     this.workflowDefinition = workflowDefinition;
   }
   
   protected void initUi()
   {
     super.initUi();
     setSizeFull();
     this.grid.setColumnExpandRatio(0, 0.0F);
     
     this.mainLayout = new DetailPanel();
     setDetailComponent(this.mainLayout);
     
 
     this.editorGrid = new GridLayout(2, 7);
     this.editorGrid.setSizeFull();
     this.editorGrid.setMargin(true);
     this.editorGrid.setColumnExpandRatio(0, 1.0F);
     this.editorGrid.setColumnExpandRatio(1, 9.0F);
     this.editorGrid.setSpacing(true);
     this.mainLayout.addComponent(this.editorGrid);
     
     initNameField(this.editorGrid);
     initDescriptionField(this.editorGrid);
     initTaskTable(this.editorGrid);
     initButtons(this.editorGrid);
     
     this.toolBar.setActiveEntry("editor");
   }
   
   protected ToolBar initToolbar() {
     this.toolBar = new ToolBar();
     
     this.toolBar.addToolbarEntry("editor", ExplorerApp.get().getI18nManager().getMessage("process.editor.title"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         if (SimpleTableEditor.this.imagePanel != null) {
           SimpleTableEditor.this.imagePanel.setVisible(false);
           SimpleTableEditor.this.editorGrid.setVisible(true);
           SimpleTableEditor.this.toolBar.setActiveEntry("editor");
         }
         
       }
     });
     this.toolBar.addToolbarEntry("preview", ExplorerApp.get().getI18nManager().getMessage("process.editor.bpmn.preview"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         SimpleTableEditor.this.editorGrid.setVisible(false);
         SimpleTableEditor.this.showDiagram();
         SimpleTableEditor.this.toolBar.setActiveEntry("preview");
       }
       
     });
     return this.toolBar;
   }
   
   protected void initNameField(GridLayout layout) {
     this.nameField = new TextField();
     this.nameField.setWriteThrough(true);
     this.nameField.setImmediate(true);
     
     layout.addComponent(new Label(ExplorerApp.get().getI18nManager().getMessage("process.editor.name")));
     layout.addComponent(this.nameField);
     if (this.workflowName != null) {
       this.nameField.setValue(this.workflowName);
       this.workflowName = null;
     }
   }
   
   protected void initDescriptionField(GridLayout layout) {
     this.descriptionField = new TextField();
     this.descriptionField.setRows(4);
     this.descriptionField.setColumns(35);
     this.descriptionField.addStyleName("noResizeTextArea");
     layout.addComponent(new Label(ExplorerApp.get().getI18nManager().getMessage("process.editor.description")));
     layout.addComponent(this.descriptionField);
     
     if (this.description != null) {
       this.descriptionField.setValue(this.description);
       this.description = null;
     }
   }
   
   protected void initTaskTable(GridLayout layout) {
     this.taskTable = new TaskTable();
     
 
     if (this.workflowDefinition != null) {
       loadTaskRows(this.workflowDefinition, this.taskTable);
     } else {
       this.taskTable.addDefaultTaskRow();
     }
     
     layout.addComponent(new Label(ExplorerApp.get().getI18nManager().getMessage("process.editor.tasks")));
     layout.addComponent(this.taskTable);
   }
   
   protected void loadTaskRows(StepDefinitionContainer<?> container, TaskTable taskTable) {
     for (StepDefinition stepDefinition : container.getSteps()) {
       if ((stepDefinition instanceof HumanStepDefinition)) {
         HumanStepDefinition humanStepDefinition = (HumanStepDefinition)stepDefinition;
         taskTable.addTaskRow(humanStepDefinition);
       } else if ((stepDefinition instanceof StepDefinitionContainer)) {
         loadTaskRows((StepDefinitionContainer)stepDefinition, taskTable);
       }
     }
   }
   
   protected void initButtons(GridLayout layout) {
     final Button saveButton = new Button(ExplorerApp.get().getI18nManager().getMessage("process.editor.save"));
     saveButton.setEnabled((this.nameField.getValue() != null) && (!"".equals((String)this.nameField.getValue())));
     this.toolBar.addButton(saveButton);
     
     saveButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) { SimpleTableEditor.this.save();
       }
       
 
     });
     this.nameField.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) {
         if ((SimpleTableEditor.this.nameField.getValue() != null) && (!"".equals((String)SimpleTableEditor.this.nameField.getValue()))) {
           saveButton.setEnabled(true);
         } else {
           saveButton.setEnabled(false);
         }
       }
     });
   }
   
   public TextField getNameTextField()
   {
     return this.nameField;
   }
   
   protected void showDiagram() {
     StreamResource.StreamSource streamSource = new StreamResource.StreamSource()
     {
       private static final long serialVersionUID = 6993112534181068935L;
       
       public InputStream getStream()
       {
         WorkflowDefinitionConversion workflowDefinitionConversion = ExplorerApp.get().getWorkflowDefinitionConversionFactory().createWorkflowDefinitionConversion(SimpleTableEditor.this.createWorkflow());
         ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl)ProcessEngines.getDefaultProcessEngine();
         ProcessEngineConfiguration processEngineConfiguration = defaultProcessEngine.getProcessEngineConfiguration();
         ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
         
         return diagramGenerator.generateDiagram(workflowDefinitionConversion.getBpmnModel(), "png", processEngineConfiguration.getActivityFontName(), processEngineConfiguration
           .getLabelFontName(), processEngineConfiguration.getAnnotationFontName(), processEngineConfiguration.getClassLoader());
       }
       
 
     };
     StreamResource imageresource = new StreamResource(streamSource, UUID.randomUUID() + ".png", ExplorerApp.get());
     Embedded diagram = new Embedded("", imageresource);
     diagram.setType(1);
     diagram.setSizeUndefined();
     
     this.imagePanel = new Panel();
     this.imagePanel.setScrollable(true);
     this.imagePanel.addStyleName("light");
     this.imagePanel.setWidth(100.0F, 8);
     this.imagePanel.setHeight("100%");
     this.mainLayout.addComponent(this.imagePanel);
     
     HorizontalLayout panelLayout = new HorizontalLayout();
     panelLayout.setSizeUndefined();
     this.imagePanel.setContent(panelLayout);
     this.imagePanel.addComponent(diagram);
   }
   
   protected void save() {
     WorkflowDefinition workflowDefinition = createWorkflow();
     
     ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl)ProcessEngines.getDefaultProcessEngine();
     RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
     ProcessEngineConfiguration processEngineConfiguration = defaultProcessEngine.getProcessEngineConfiguration();
     ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
     
     Model model = null;
     if (this.modelId == null) {
       model = repositoryService.newModel();
     } else {
       model = repositoryService.getModel(this.modelId);
     }
     
     model.setName(workflowDefinition.getName());
     model.setCategory("table-editor");
     repositoryService.saveModel(model);
     
 
 
     WorkflowDefinitionConversion conversion = ExplorerApp.get().getWorkflowDefinitionConversionFactory().createWorkflowDefinitionConversion(workflowDefinition);
     conversion.convert();
     
     try
     {
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       ExplorerApp.get().getSimpleWorkflowJsonConverter().writeWorkflowDefinition(workflowDefinition, new OutputStreamWriter(baos));
       repositoryService.addModelEditorSource(model.getId(), baos.toByteArray());
       
 
 
       repositoryService.addModelEditorSourceExtra(model.getId(), IOUtils.toByteArray(diagramGenerator
         .generateDiagram(conversion.getBpmnModel(), "png", processEngineConfiguration.getActivityFontName(), processEngineConfiguration
         .getLabelFontName(), processEngineConfiguration.getAnnotationFontName(), processEngineConfiguration.getClassLoader())));
     } catch (IOException e) {
       logger.warn("Could not generate process image. Image is not stored and will not be shown.", e);
     }
     
     ExplorerApp.get().getViewManager().showEditorProcessDefinitionPage(model.getId());
   }
   
   protected WorkflowDefinition createWorkflow() {
     WorkflowDefinition workflow = new WorkflowDefinition();
     workflow.setName((String)this.nameField.getValue());
     
     String description = (String)this.descriptionField.getValue();
     if ((description != null) && (description.length() > 0)) {
       workflow.setDescription(description);
     }
     
     List<HumanStepDefinition> steps = this.taskTable.getSteps();
     for (int i = 0; i < steps.size(); i++) {
       HumanStepDefinition currentStep = (HumanStepDefinition)steps.get(i);
       
 
       int nextIndex = i + 1;
       ParallelStepsDefinition parallelStepsDefinition = null;
       while ((nextIndex < steps.size()) && (((HumanStepDefinition)steps.get(nextIndex)).isStartsWithPrevious())) {
         if (parallelStepsDefinition == null) {
           parallelStepsDefinition = new ParallelStepsDefinition();
           ListStepDefinition<ParallelStepsDefinition> listStepDef = new ListStepDefinition();
           listStepDef.addStep(currentStep);
           parallelStepsDefinition.addStepList(listStepDef);
         }
         
         ListStepDefinition<ParallelStepsDefinition> listStepDef = new ListStepDefinition();
         listStepDef.addStep((StepDefinition)steps.get(nextIndex));
         parallelStepsDefinition.addStepList(listStepDef);
         nextIndex++;
       }
       
       if (parallelStepsDefinition != null) {
         workflow.addStep(parallelStepsDefinition);
         i = nextIndex - 1;
       } else {
         workflow.addStep(currentStep);
       }
     }
     
     return workflow;
   }
   
 
 
   protected ToolBar createMenuBar()
   {
     return initToolbar();
   }
   
   protected AbstractSelect createSelectComponent()
   {
     return null;
   }
   
   public void refreshSelectNext() {}
   
   public void selectElement(int index) {}
 }


