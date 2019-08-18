 package org.activiti.explorer.ui.management.processinstance;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.terminal.ExternalResource;
 import com.vaadin.terminal.StreamResource;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.VerticalLayout;
 import java.io.InputStream;
 import java.net.URL;
 import java.util.List;
 import java.util.Map;
 import java.util.TreeMap;
 import javax.xml.stream.XMLInputFactory;
 import javax.xml.stream.XMLStreamReader;
 import org.activiti.bpmn.converter.BpmnXMLConverter;
 import org.activiti.bpmn.model.BpmnModel;
 import org.activiti.bpmn.model.GraphicInfo;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngineConfiguration;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.history.HistoricProcessInstance;
 import org.activiti.engine.history.HistoricProcessInstanceQuery;
 import org.activiti.engine.history.HistoricTaskInstance;
 import org.activiti.engine.history.HistoricTaskInstanceQuery;
 import org.activiti.engine.impl.RepositoryServiceImpl;
 import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.engine.runtime.ProcessInstance;
 import org.activiti.engine.runtime.ProcessInstanceQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.AbstractTablePage;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.PrettyTimeLabel;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.custom.UserProfileLink;
 import org.activiti.explorer.ui.process.ProcessDefinitionImageStreamResourceBuilder;
 import org.activiti.explorer.ui.variable.VariableRendererManager;
 import org.activiti.explorer.util.XmlUtil;
 import org.activiti.image.ProcessDiagramGenerator;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessInstanceDetailPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected static final Logger LOGGER = LoggerFactory.getLogger(ProcessInstanceDetailPanel.class);
   
   protected transient RuntimeService runtimeService;
   
   protected transient RepositoryService repositoryService;
   
   protected transient TaskService taskService;
   
   protected transient HistoryService historyService;
   protected transient IdentityService identityService;
   protected I18nManager i18nManager;
   protected VariableRendererManager variableRendererManager;
   protected ProcessInstance processInstance;
   protected AbstractTablePage processInstancePage;
   protected HistoricProcessInstance historicProcessInstance;
   protected ProcessDefinition processDefinition;
   protected VerticalLayout panelLayout;
   
   public ProcessInstanceDetailPanel(String processInstanceId, AbstractTablePage processInstancePage)
   {
     this.processInstancePage = processInstancePage;
     
     this.runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.variableRendererManager = ExplorerApp.get().getVariableRendererManager();
     
     this.processInstance = getProcessInstance(processInstanceId);
     this.processDefinition = getProcessDefinition(this.processInstance.getProcessDefinitionId());
     this.historicProcessInstance = getHistoricProcessInstance(processInstanceId);
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     
     init();
   }
   
   protected void init() {
     addStyleName("white");
     setSizeFull();
     
     this.panelLayout = new VerticalLayout();
     this.panelLayout.setWidth(100.0F, 8);
     this.panelLayout.setMargin(true);
     setDetailContainer(this.panelLayout);
     
     addHeader();
     addProcessImage();
     addTasks();
     addVariables();
     addDeleteButton();
   }
   
   protected void addHeader() {
     GridLayout header = new GridLayout(3, 2);
     header.setWidth(100.0F, 8);
     header.addStyleName("title-block");
     header.setSpacing(true);
     header.setMargin(false, false, true, false);
     
 
     Embedded image = new Embedded(null, Images.PROCESS_50);
     header.addComponent(image, 0, 0, 0, 1);
     
 
     Label nameLabel = new Label(getProcessDisplayName(this.processDefinition, this.processInstance));
     nameLabel.addStyleName("h2");
     header.addComponent(nameLabel, 1, 0, 2, 0);
     
 
 
     PrettyTimeLabel startTimeLabel = new PrettyTimeLabel(this.i18nManager.getMessage("process.start.time"), this.historicProcessInstance.getStartTime(), null, true);
     startTimeLabel.addStyleName("process-start-time");
     header.addComponent(startTimeLabel, 1, 1);
     
     header.setColumnExpandRatio(1, 1.0F);
     header.setColumnExpandRatio(2, 1.0F);
     
     this.panelLayout.addComponent(header);
   }
   
   protected void addProcessImage()
   {
     ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)((RepositoryServiceImpl)this.repositoryService).getDeployedProcessDefinition(this.processDefinition.getId());
     
 
     if (processDefinitionEntity != null)
     {
       boolean didDrawImage = false;
       
       if (ExplorerApp.get().isUseJavascriptDiagram()) {
         try
         {
           InputStream definitionStream = this.repositoryService.getResourceAsStream(this.processDefinition
             .getDeploymentId(), this.processDefinition.getResourceName());
           XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
           XMLStreamReader xtr = xif.createXMLStreamReader(definitionStream);
           BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
           
           if (!bpmnModel.getFlowLocationMap().isEmpty())
           {
             int maxX = 0;
             int maxY = 0;
             for (String key : bpmnModel.getLocationMap().keySet()) {
               GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(key);
               double elementX = graphicInfo.getX() + graphicInfo.getWidth();
               if (maxX < elementX) {
                 maxX = (int)elementX;
               }
               double elementY = graphicInfo.getY() + graphicInfo.getHeight();
               if (maxY < elementY) {
                 maxY = (int)elementY;
               }
             }
             
             Panel imagePanel = new Panel();
             imagePanel.addStyleName("light");
             imagePanel.setWidth(100.0F, 8);
             imagePanel.setHeight(100.0F, 8);
             URL explorerURL = ExplorerApp.get().getURL();
             
             URL url = new URL(explorerURL.getProtocol(), explorerURL.getHost(), explorerURL.getPort(), explorerURL.getPath().replace("/ui", "") + "diagram-viewer/index.html?processDefinitionId=" + this.processDefinition.getId() + "&processInstanceId=" + this.processInstance.getId());
             Embedded browserPanel = new Embedded("", new ExternalResource(url));
             browserPanel.setType(2);
             browserPanel.setWidth(maxX + 350 + "px");
             browserPanel.setHeight(maxY + 220 + "px");
             
             HorizontalLayout panelLayoutT = new HorizontalLayout();
             panelLayoutT.setSizeUndefined();
             imagePanel.setContent(panelLayoutT);
             imagePanel.addComponent(browserPanel);
             
             this.panelLayout.addComponent(imagePanel);
             
             didDrawImage = true;
           }
         }
         catch (Exception e) {
           LOGGER.error("Error loading process diagram component", e);
         }
       }
       
       if ((!didDrawImage) && (processDefinitionEntity.isGraphicalNotationDefined())) {
         ProcessEngineConfiguration processEngineConfiguration = ProcessEngines.getDefaultProcessEngine().getProcessEngineConfiguration();
         ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
         
         StreamResource diagram = new ProcessDefinitionImageStreamResourceBuilder().buildStreamResource(this.processInstance, this.repositoryService, this.runtimeService, diagramGenerator, processEngineConfiguration);
         
         if (diagram != null) {
           Label header = new Label(this.i18nManager.getMessage("process.header.diagram"));
           header.addStyleName("h3");
           header.addStyleName("block-holder");
           header.addStyleName("no-line");
           this.panelLayout.addComponent(header);
           
           Embedded embedded = new Embedded(null, diagram);
           embedded.setType(1);
           embedded.setSizeUndefined();
           
           Panel imagePanel = new Panel();
           imagePanel.setScrollable(true);
           imagePanel.addStyleName("light");
           imagePanel.setWidth(100.0F, 8);
           imagePanel.setHeight(100.0F, 8);
           
           HorizontalLayout panelLayoutT = new HorizontalLayout();
           panelLayoutT.setSizeUndefined();
           imagePanel.setContent(panelLayoutT);
           imagePanel.addComponent(embedded);
           
           this.panelLayout.addComponent(imagePanel);
         }
       }
     }
   }
   
   protected String getProcessDisplayName(ProcessDefinition processDefinition, ProcessInstance processInstance) {
     if (processDefinition.getName() != null) {
       return processDefinition.getName() + " (" + processInstance.getId() + ")";
     }
     return processDefinition.getKey() + " (" + processInstance.getId() + ")";
   }
   
   protected void addTasks()
   {
     Label header = new Label(this.i18nManager.getMessage("process.instance.header.tasks"));
     header.addStyleName("h3");
     header.addStyleName("block-holder");
     header.addStyleName("no-line");
     this.panelLayout.addComponent(header);
     
     this.panelLayout.addComponent(new Label("&nbsp;", 3));
     
     Table taskTable = new Table();
     taskTable.addStyleName("proc-inst-task-list");
     taskTable.setWidth(100.0F, 8);
     
 
 
 
 
 
     List<HistoricTaskInstance> tasks = ((HistoricTaskInstanceQuery)((HistoricTaskInstanceQuery)((HistoricTaskInstanceQuery)this.historyService.createHistoricTaskInstanceQuery().processInstanceId(this.processInstance.getId())).orderByHistoricTaskInstanceEndTime().desc()).orderByHistoricTaskInstanceStartTime().desc()).list();
     
     if (!tasks.isEmpty())
     {
 
       taskTable.addContainerProperty("finished", Component.class, null, "", null, "c");
       taskTable.setColumnWidth("finished", 22);
       
       taskTable.addContainerProperty("name", String.class, null, this.i18nManager.getMessage("task.name"), null, "b");
       
       taskTable.addContainerProperty("priority", Integer.class, null, this.i18nManager.getMessage("task.priority"), null, "b");
       
       taskTable.addContainerProperty("assignee", Component.class, null, this.i18nManager.getMessage("task.assignee"), null, "b");
       
       taskTable.addContainerProperty("dueDate", Component.class, null, this.i18nManager.getMessage("task.duedate"), null, "b");
       
       taskTable.addContainerProperty("startDate", Component.class, null, this.i18nManager.getMessage("task.create.time"), null, "b");
       
       taskTable.addContainerProperty("endDate", Component.class, null, this.i18nManager.getMessage("task.complete.time"), null, "b");
       
 
       this.panelLayout.addComponent(taskTable);
       this.panelLayout.setExpandRatio(taskTable, 1.0F);
       
       for (HistoricTaskInstance task : tasks) {
         addTaskItem(task, taskTable);
       }
       
       taskTable.setPageLength(taskTable.size());
     }
     else {
       Label noTaskLabel = new Label(this.i18nManager.getMessage("process.instance.no.tasks"));
       this.panelLayout.addComponent(noTaskLabel);
     }
   }
   
   protected void addTaskItem(HistoricTaskInstance task, Table taskTable) {
     Item item = taskTable.addItem(task.getId());
     
     if (task.getEndTime() != null) {
       item.getItemProperty("finished").setValue(new Embedded(null, Images.TASK_FINISHED_22));
     } else {
       item.getItemProperty("finished").setValue(new Embedded(null, Images.TASK_22));
     }
     
     item.getItemProperty("name").setValue(task.getName());
     item.getItemProperty("priority").setValue(Integer.valueOf(task.getPriority()));
     
     item.getItemProperty("startDate").setValue(new PrettyTimeLabel(task.getStartTime(), true));
     item.getItemProperty("endDate").setValue(new PrettyTimeLabel(task.getEndTime(), true));
     
     if (task.getDueDate() != null) {
       Label dueDateLabel = new PrettyTimeLabel(task.getEndTime(), this.i18nManager.getMessage("task.not.finished.yet"), true);
       item.getItemProperty("dueDate").setValue(dueDateLabel);
     }
     
     if (task.getAssignee() != null) {
       Component taskAssigneeComponent = getTaskAssigneeComponent(task.getAssignee());
       if (taskAssigneeComponent != null) {
         item.getItemProperty("assignee").setValue(taskAssigneeComponent);
       }
     }
   }
   
   protected Component getTaskAssigneeComponent(String assignee) {
     return new UserProfileLink(this.identityService, true, assignee);
   }
   
   protected void addVariables() {
     Label header = new Label(this.i18nManager.getMessage("process.instance.header.variables"));
     header.addStyleName("h3");
     header.addStyleName("block-holder");
     header.addStyleName("no-line");
     this.panelLayout.addComponent(header);
     
     this.panelLayout.addComponent(new Label("&nbsp;", 3));
     
 
     Map<String, Object> variables = new TreeMap(this.runtimeService.getVariables(this.processInstance.getId()));
     
     if (!variables.isEmpty())
     {
       Table variablesTable = new Table();
       variablesTable.setWidth(60.0F, 8);
       variablesTable.addStyleName("proc-inst-task-list");
       
       variablesTable.addContainerProperty("name", String.class, null, this.i18nManager.getMessage("process.instance.variable.name"), null, "b");
       variablesTable.addContainerProperty("value", String.class, null, this.i18nManager.getMessage("process.instance.variable.value"), null, "b");
       
       for (String variable : variables.keySet()) {
         Item variableItem = variablesTable.addItem(variable);
         variableItem.getItemProperty("name").setValue(variable);
         
 
         String theValue = this.variableRendererManager.getStringRepresentation(variables.get(variable));
         variableItem.getItemProperty("value").setValue(theValue);
       }
       
       variablesTable.setPageLength(variables.size());
       this.panelLayout.addComponent(variablesTable);
     } else {
       Label noVariablesLabel = new Label(this.i18nManager.getMessage("process.instance.no.variables"));
       this.panelLayout.addComponent(noVariablesLabel);
     }
   }
   
   protected void addDeleteButton() {
     Button deleteProcessInstanceButton = new Button(this.i18nManager.getMessage("process.instance.delete"));
     deleteProcessInstanceButton.setIcon(Images.DELETE);
     deleteProcessInstanceButton.addListener(new DeleteProcessInstanceClickListener(this.processInstance.getId(), this.processInstancePage));
     
 
     this.processInstancePage.getToolBar().removeAllButtons();
     this.processInstancePage.getToolBar().addButton(deleteProcessInstanceButton);
   }
   
   protected ProcessInstance getProcessInstance(String processInstanceId) {
     return (ProcessInstance)this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
   }
   
   protected HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
     return (HistoricProcessInstance)this.historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
   }
   
   protected ProcessDefinition getProcessDefinition(String processDefinitionId) {
     return (ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
   }
 }


