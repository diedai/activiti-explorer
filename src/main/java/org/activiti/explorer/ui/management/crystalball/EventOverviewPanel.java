 package org.activiti.explorer.ui.management.crystalball;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.NativeSelect;
 import com.vaadin.ui.Table;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.activiti.crystalball.simulator.EventCalendar;
 import org.activiti.crystalball.simulator.ReplaySimulationRun;
 import org.activiti.crystalball.simulator.SimpleEventCalendar;
 import org.activiti.crystalball.simulator.SimulationDebugger;
 import org.activiti.crystalball.simulator.SimulationEvent;
 import org.activiti.crystalball.simulator.SimulationEventComparator;
 import org.activiti.crystalball.simulator.SimulationEventHandler;
 import org.activiti.crystalball.simulator.SimulationRunContext;
 import org.activiti.crystalball.simulator.delegate.event.Function;
 import org.activiti.crystalball.simulator.delegate.event.impl.EventLogProcessInstanceCreateTransformer;
 import org.activiti.crystalball.simulator.delegate.event.impl.EventLogTransformer;
 import org.activiti.crystalball.simulator.delegate.event.impl.EventLogUserTaskCompleteTransformer;
 import org.activiti.crystalball.simulator.impl.StartReplayLogEventHandler;
 import org.activiti.crystalball.simulator.impl.replay.ReplayUserTaskCompleteEventHandler;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngineConfiguration;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.event.EventLogEntry;
 import org.activiti.engine.history.HistoricProcessInstance;
 import org.activiti.engine.history.HistoricProcessInstanceQuery;
 import org.activiti.engine.impl.el.NoExecutionVariableScope;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.engine.runtime.ProcessInstance;
 import org.activiti.engine.runtime.ProcessInstanceQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.variable.VariableRendererManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class EventOverviewPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   private static final String PROCESS_INSTANCE_START_EVENT_TYPE = "PROCESS_INSTANCE_START";
   private static final String PROCESS_DEFINITION_ID_KEY = "processDefinitionId";
   private static final String VARIABLES_KEY = "variables";
   private static final String USER_TASK_COMPLETED_EVENT_TYPE = "USER_TASK_COMPLETED";
   private static final String SIMULATION_BUSINESS_KEY = "testBusinessKey";
   protected transient HistoryService historyService;
   protected transient RepositoryService repositoryService;
   protected transient RuntimeService runtimeService;
   protected transient IdentityService identityService;
   protected transient ManagementService managementService;
   protected I18nManager i18nManager;
   protected VariableRendererManager variableRendererManager;
   protected HorizontalLayout instanceLayout;
   protected NativeSelect definitionSelect;
   protected Button replayButton;
   protected Table instanceTable;
   protected HorizontalLayout eventLayout;
   protected Button stepButton;
   protected Button showProcessInstanceButton;
   protected Table eventTable;
   protected Label noMembersTable;
   protected List<ProcessDefinition> definitionList;
   protected Map<String, ProcessDefinition> definitionMap = new HashMap();
   protected List<HistoricProcessInstance> instanceList;
   protected List<SimulationEvent> simulationEvents;
   protected HistoricProcessInstance replayHistoricInstance;
   protected SimulationDebugger simulationDebugger;
   
   public EventOverviewPanel() {
     this.runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
     this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.managementService = ProcessEngines.getDefaultProcessEngine().getManagementService();
     this.variableRendererManager = ExplorerApp.get().getVariableRendererManager();
     this.definitionList = ((ProcessDefinitionQuery)this.repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionName().asc()).list();
     this.instanceList = ((HistoricProcessInstanceQuery)this.historyService.createHistoricProcessInstanceQuery().orderByProcessInstanceStartTime().desc()).list();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     initializeDefinitionMap();
     init();
     initializeCurrentValues();
   }
   
   protected void initializeDefinitionMap() {
     for (ProcessDefinition definition : this.definitionList) {
       this.definitionMap.put(definition.getId(), definition);
     }
   }
   
   protected void initializeCurrentValues() {
     if (ExplorerApp.get().getCrystalBallSimulationDebugger() != null) {
       this.simulationDebugger = ExplorerApp.get().getCrystalBallSimulationDebugger();
       this.simulationEvents = ExplorerApp.get().getCrystalBallSimulationEvents();
       
       String selectedDefinitionId = ExplorerApp.get().getCrystalBallCurrentDefinitionId();
       if (selectedDefinitionId != null) {
         this.definitionSelect.setValue(selectedDefinitionId);
       }
       
       String selectedInstanceId = ExplorerApp.get().getCrystalBallCurrentInstanceId();
       if (selectedInstanceId != null) {
         this.instanceTable.setValue(selectedInstanceId);
       }
       
 
 
 
 
       List<HistoricProcessInstance> replayProcessInstanceList = ((HistoricProcessInstanceQuery)this.historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey("testBusinessKey").orderByProcessInstanceStartTime().desc()).list();
       if ((replayProcessInstanceList != null) && (!replayProcessInstanceList.isEmpty())) {
         this.replayHistoricInstance = ((HistoricProcessInstance)replayProcessInstanceList.get(0));
       }
       
       refreshEvents();
     }
   }
   
   protected void init() {
     setSizeFull();
     addStyleName("light");
     
     initProcessInstances();
     initEvents();
   }
   
   protected void initProcessInstances() {
     HorizontalLayout instancesHeader = new HorizontalLayout();
     instancesHeader.setSpacing(false);
     instancesHeader.setMargin(false);
     instancesHeader.setWidth(100.0F, 8);
     instancesHeader.addStyleName("block-holder");
     addDetailComponent(instancesHeader);
     
     initProcessInstanceTitle(instancesHeader);
     
     HorizontalLayout selectLayout = new HorizontalLayout();
     selectLayout.setSpacing(true);
     selectLayout.setMargin(true);
     selectLayout.setWidth(50.0F, 8);
     addDetailComponent(selectLayout);
     
     this.definitionSelect = new NativeSelect(this.i18nManager.getMessage("deployment.header.definitions"));
     this.definitionSelect.setImmediate(true);
     for (ProcessDefinition definition : this.definitionList) {
       this.definitionSelect.addItem(definition.getId());
       this.definitionSelect.setItemCaption(definition.getId(), definition.getName());
     }
     this.definitionSelect.addListener(new Property.ValueChangeListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event)
       {
         if (EventOverviewPanel.this.definitionSelect.getValue() != null) {
           String selectedDefinitionId = (String)EventOverviewPanel.this.definitionSelect.getValue();
           ExplorerApp.get().setCrystalBallCurrentDefinitionId(selectedDefinitionId);
           EventOverviewPanel.this.refreshInstances(selectedDefinitionId);
         }
         
       }
     });
     selectLayout.addComponent(this.definitionSelect);
     
     this.replayButton = new Button(this.i18nManager.getMessage("crystalball.button.replay"));
     this.replayButton.setEnabled(false);
     this.replayButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event)
       {
         if (EventOverviewPanel.this.instanceTable.getValue() != null) {
           String processInstanceId = (String)EventOverviewPanel.this.instanceTable.getValue();
           ExplorerApp.get().setCrystalBallCurrentInstanceId(processInstanceId);
           List<EventLogEntry> eventLogEntries = EventOverviewPanel.this.managementService.getEventLogEntriesByProcessInstanceId(processInstanceId);
           if ((eventLogEntries == null) || (eventLogEntries.isEmpty())) return;
           EventLogTransformer transformer = new EventLogTransformer(EventOverviewPanel.this.getTransformers());
           EventOverviewPanel.this.simulationEvents = transformer.transform(eventLogEntries);
           ExplorerApp.get().setCrystalBallSimulationEvents(EventOverviewPanel.this.simulationEvents);
           
 
           SimpleEventCalendar eventCalendar = new SimpleEventCalendar(ProcessEngines.getDefaultProcessEngine().getProcessEngineConfiguration().getClock(), new SimulationEventComparator());
           
           eventCalendar.addEvents(EventOverviewPanel.this.simulationEvents);
           
 
 
           EventOverviewPanel.this.simulationDebugger = new ReplaySimulationRun(ProcessEngines.getDefaultProcessEngine(), eventCalendar, EventOverviewPanel.this.getReplayHandlers(processInstanceId));
           ExplorerApp.get().setCrystalBallSimulationDebugger(EventOverviewPanel.this.simulationDebugger);
           
           EventOverviewPanel.this.simulationDebugger.init(new NoExecutionVariableScope());
           
           EventOverviewPanel.this.simulationDebugger.step();
           
 
 
 
 
 
           List<HistoricProcessInstance> replayProcessInstanceList = ((HistoricProcessInstanceQuery)EventOverviewPanel.this.historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey("testBusinessKey").orderByProcessInstanceStartTime().desc()).list();
           if ((replayProcessInstanceList != null) && (!replayProcessInstanceList.isEmpty())) {
             EventOverviewPanel.this.replayHistoricInstance = ((HistoricProcessInstance)replayProcessInstanceList.get(0));
           }
           
           EventOverviewPanel.this.refreshEvents();
         }
       }
     });
     selectLayout.addComponent(this.replayButton);
     selectLayout.setComponentAlignment(this.replayButton, Alignment.MIDDLE_LEFT);
     
     this.instanceLayout = new HorizontalLayout();
     this.instanceLayout.setWidth(100.0F, 8);
     addDetailComponent(this.instanceLayout);
     
     initInstancesTable();
   }
   
   protected void initProcessInstanceTitle(HorizontalLayout instancesHeader) {
     Label titleHeader = new Label(this.i18nManager.getMessage("process.instances"));
     titleHeader.addStyleName("h3");
     instancesHeader.addComponent(titleHeader);
   }
   
   protected void initInstancesTable() {
     if ((this.instanceList == null) || (this.instanceList.isEmpty())) {
       this.noMembersTable = new Label(this.i18nManager.getMessage("admin.running.none.found"));
       this.instanceLayout.addComponent(this.noMembersTable);
     }
     else
     {
       this.instanceTable = new Table();
       this.instanceTable.setWidth(100.0F, 8);
       this.instanceTable.setHeight(200.0F, 0);
       
       this.instanceTable.setEditable(false);
       this.instanceTable.setImmediate(true);
       this.instanceTable.setSelectable(true);
       this.instanceTable.setSortDisabled(false);
       
       this.instanceTable.addContainerProperty("id", String.class, null, this.i18nManager.getMessage("process.instance.id"), null, "b");
       this.instanceTable.addContainerProperty("definitionName", String.class, null, this.i18nManager.getMessage("process.instance.name"), null, "b");
       this.instanceTable.addContainerProperty("started", String.class, null, this.i18nManager.getMessage("process.instance.started"), null, "b");
       this.instanceTable.addContainerProperty("ended", String.class, null, this.i18nManager.getMessage("process.instance.ended"), null, "b");
       
       fillInstanceValues();
       
       this.instanceTable.addListener(new Property.ValueChangeListener() {
         private static final long serialVersionUID = 1L;
         
         public void valueChange(Property.ValueChangeEvent event) { Item item = EventOverviewPanel.this.instanceTable.getItem(event.getProperty().getValue());
           if (item != null) {
             EventOverviewPanel.this.replayButton.setEnabled(true);
           } else {
             EventOverviewPanel.this.replayButton.setEnabled(false);
           }
           
         }
       });
       this.instanceLayout.addComponent(this.instanceTable);
     }
   }
   
 
 
 
   protected void refreshInstances(String processDefinitionId)
   {
     this.instanceList = ((HistoricProcessInstanceQuery)this.historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).orderByProcessInstanceStartTime().desc()).list();
     this.instanceTable.removeAllItems();
     fillInstanceValues();
   }
   
   protected void fillInstanceValues() {
     for (HistoricProcessInstance processInstance : this.instanceList) {
       ProcessDefinition definition = (ProcessDefinition)this.definitionMap.get(processInstance.getProcessDefinitionId());
       String definitionName = "";
       if (definition != null) {
         if (definition.getName() != null) {
           definitionName = definition.getName();
         } else {
           definitionName = definition.getId();
         }
         
         definitionName = definitionName + " (v" + definition.getVersion() + ")";
       }
       
       this.instanceTable.addItem(new String[] { processInstance.getId(), definitionName, processInstance
         .getStartTime().toString(), processInstance
         .getEndTime() != null ? processInstance.getEndTime().toString() : "" }, processInstance.getId());
     }
   }
   
   protected void initEvents() {
     HorizontalLayout eventsHeader = new HorizontalLayout();
     eventsHeader.setSpacing(true);
     eventsHeader.setWidth(80.0F, 8);
     eventsHeader.addStyleName("block-holder");
     addDetailComponent(eventsHeader);
     
     initEventTitle(eventsHeader);
     
     this.stepButton = new Button(this.i18nManager.getMessage("crystalball.button.nextevent"));
     this.stepButton.setEnabled(false);
     this.stepButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         if (!SimulationRunContext.getEventCalendar().getEvents().isEmpty()) {
           EventOverviewPanel.this.simulationDebugger.step();
           EventOverviewPanel.this.refreshEvents();
         }
       }
     });
     eventsHeader.addComponent(this.stepButton);
     eventsHeader.setComponentAlignment(this.stepButton, Alignment.MIDDLE_LEFT);
     
     this.showProcessInstanceButton = new Button();
     this.showProcessInstanceButton.addStyleName("link");
     this.showProcessInstanceButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         if (EventOverviewPanel.this.replayHistoricInstance != null) {
           ExplorerApp.get().getViewManager().showMyProcessInstancesPage(EventOverviewPanel.this.replayHistoricInstance.getId());
         }
         
       }
     });
     eventsHeader.addComponent(this.showProcessInstanceButton);
     eventsHeader.setComponentAlignment(this.showProcessInstanceButton, Alignment.MIDDLE_LEFT);
     
     this.eventLayout = new HorizontalLayout();
     this.eventLayout.setWidth(100.0F, 8);
     addDetailComponent(this.eventLayout);
     initEventsTable();
   }
   
   protected void initEventTitle(HorizontalLayout eventsHeader) {
     Label usersHeader = new Label(this.i18nManager.getMessage("admin.definitions"));
     usersHeader.addStyleName("h3");
     eventsHeader.addComponent(usersHeader);
   }
   
   protected void initEventsTable() {
     this.eventTable = new Table();
     this.eventTable.setVisible(false);
     this.eventTable.setWidth(100.0F, 8);
     this.eventTable.setHeight(250.0F, 0);
     
     this.eventTable.setEditable(false);
     this.eventTable.setImmediate(true);
     this.eventTable.setSelectable(true);
     this.eventTable.setSortDisabled(false);
     
     this.eventTable.addContainerProperty("type", String.class, null, this.i18nManager.getMessage("crystalball.event.type"), null, "b");
     this.eventTable.addContainerProperty("executed", String.class, null, this.i18nManager.getMessage("crystalball.event.executed"), null, "b");
     
     this.eventLayout.addComponent(this.eventTable);
   }
   
   protected void refreshEvents() {
     this.stepButton.setEnabled(false);
     this.showProcessInstanceButton.setVisible(false);
     this.eventTable.removeAllItems();
     fillEventValues();
   }
   
   protected void fillEventValues() {
     for (SimulationEvent originalEvent : this.simulationEvents) {
       boolean executed = true;
       if ((SimulationRunContext.getEventCalendar() != null) && (SimulationRunContext.getEventCalendar().getEvents() != null)) {
         for (SimulationEvent event : SimulationRunContext.getEventCalendar().getEvents()) {
           if (originalEvent.equals(event)) {
             executed = false;
             this.stepButton.setEnabled(true);
             break;
           }
         }
       }
       
       Object itemId = this.eventTable.addItem();
       this.eventTable.getItem(itemId).getItemProperty("type").setValue(originalEvent.getType());
       this.eventTable.getItem(itemId).getItemProperty("executed").setValue(Boolean.valueOf(executed));
     }
     
     if ((this.replayHistoricInstance != null) && (this.replayHistoricInstance.getId() != null)) {
       ProcessInstance testInstance = (ProcessInstance)this.runtimeService.createProcessInstanceQuery().processInstanceId(this.replayHistoricInstance.getId()).singleResult();
       if (testInstance != null) {
         this.showProcessInstanceButton.setCaption(this.i18nManager.getMessage("task.part.of.process", new Object[] {this.definitionMap
           .get(this.replayHistoricInstance.getProcessDefinitionId()) }));
         this.showProcessInstanceButton.setVisible(true);
       }
     }
     
     this.eventTable.setVisible(true);
   }
   
   protected List<Function<EventLogEntry, SimulationEvent>> getTransformers() {
     List<Function<EventLogEntry, SimulationEvent>> transformers = new ArrayList();
     transformers.add(new EventLogProcessInstanceCreateTransformer("PROCESS_INSTANCE_START", "processDefinitionId", "testBusinessKey", "variables"));
     transformers.add(new EventLogUserTaskCompleteTransformer("USER_TASK_COMPLETED"));
     return transformers;
   }
   
   protected Map<String, SimulationEventHandler> getReplayHandlers(String processInstanceId) {
     Map<String, SimulationEventHandler> handlers = new HashMap();
     handlers.put("PROCESS_INSTANCE_START", new StartReplayLogEventHandler(processInstanceId, "processDefinitionId", "testBusinessKey", "variables"));
     handlers.put("USER_TASK_COMPLETED", new ReplayUserTaskCompleteEventHandler());
     return handlers;
   }
 }


