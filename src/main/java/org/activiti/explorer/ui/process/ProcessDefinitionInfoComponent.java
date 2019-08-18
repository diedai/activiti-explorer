 package org.activiti.explorer.ui.process;
 
 import com.vaadin.terminal.ExternalResource;
 import com.vaadin.terminal.StreamResource;
 import com.vaadin.ui.ComponentContainer;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.VerticalLayout;
 import java.io.InputStream;
 import java.net.URL;
 import java.text.DateFormat;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import javax.xml.stream.XMLInputFactory;
 import javax.xml.stream.XMLStreamReader;
 import org.activiti.bpmn.converter.BpmnXMLConverter;
 import org.activiti.bpmn.model.BpmnModel;
 import org.activiti.bpmn.model.GraphicInfo;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.impl.persistence.entity.JobEntity;
 import org.activiti.engine.repository.Deployment;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.runtime.Job;
 import org.activiti.engine.runtime.JobQuery;
 import org.activiti.explorer.Constants;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.util.XmlUtil;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessDefinitionInfoComponent
   extends VerticalLayout
 {
   private static final long serialVersionUID = 1L;
   protected static final Logger LOGGER = LoggerFactory.getLogger(ProcessDefinitionInfoComponent.class);
   
   protected transient RepositoryService repositoryService;
   
   protected transient ManagementService managementService;
   
   protected I18nManager i18nManager;
   
   protected ProcessDefinition processDefinition;
   
   protected Deployment deployment;
   
   protected HorizontalLayout timeDetails;
   
   protected VerticalLayout processImageContainer;
   
   public ProcessDefinitionInfoComponent(ProcessDefinition processDefinition, Deployment deployment)
   {
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     this.managementService = ProcessEngines.getDefaultProcessEngine().getManagementService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     this.processDefinition = processDefinition;
     this.deployment = deployment;
     
     addStyleName("block-holder");
     
     initSuspensionStateInformation();
     initImage();
   }
   
   protected void initSuspensionStateInformation()
   {
     List<Job> jobs = ((JobQuery)this.managementService.createJobQuery().processDefinitionId(this.processDefinition.getId()).orderByJobDuedate().asc()).list();
     List<JobEntity> suspensionStateJobs = new ArrayList();
     
 
     for (Iterator localIterator = jobs.iterator(); localIterator.hasNext();) { Job job = (Job)localIterator.next();
       JobEntity jobEntity = (JobEntity)job;
       if ((jobEntity.getJobHandlerType().equals("suspend-processdefinition")) || 
         (jobEntity.getJobHandlerType().equals("activate-processdefinition"))) {
         suspensionStateJobs.add(jobEntity);
       }
     }
     Job job;
     if (!suspensionStateJobs.isEmpty())
     {
 
       Label suspensionStateTitle = new Label(this.i18nManager.getMessage("process.header.suspension.state"));
       suspensionStateTitle.addStyleName("h3");
       addComponent(suspensionStateTitle);
       addEmptySpace(this);
       
 
       for (JobEntity jobEntity : suspensionStateJobs) {
         if (jobEntity.getJobHandlerType().equals("suspend-processdefinition")) {
           Label suspendLabel = new Label(this.i18nManager.getMessage("process.scheduled.suspend", new Object[] {Constants.DEFAULT_TIME_FORMATTER
             .format(jobEntity.getDuedate()) }), 3);
           addComponent(suspendLabel);
         } else if (jobEntity.getJobHandlerType().equals("activate-processdefinition")) {
           Label suspendLabel = new Label(this.i18nManager.getMessage("process.scheduled.activate", new Object[] {Constants.DEFAULT_TIME_FORMATTER
             .format(jobEntity.getDuedate()) }), 3);
           addComponent(suspendLabel);
         }
       }
     }
     
     addEmptySpace(this);
   }
   
   protected void initImage() {
     this.processImageContainer = new VerticalLayout();
     
     Label processTitle = new Label(this.i18nManager.getMessage("process.header.diagram"));
     processTitle.addStyleName("h3");
     this.processImageContainer.addComponent(processTitle);
     
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
           
           URL url = new URL(explorerURL.getProtocol(), explorerURL.getHost(), explorerURL.getPort(), explorerURL.getPath().replace("/ui", "") + "diagram-viewer/index.html?processDefinitionId=" + this.processDefinition.getId());
           Embedded browserPanel = new Embedded("", new ExternalResource(url));
           browserPanel.setType(2);
           browserPanel.setWidth(maxX + 350 + "px");
           browserPanel.setHeight(maxY + 220 + "px");
           
           HorizontalLayout panelLayout = new HorizontalLayout();
           panelLayout.setSizeUndefined();
           imagePanel.setContent(panelLayout);
           imagePanel.addComponent(browserPanel);
           
           this.processImageContainer.addComponent(imagePanel);
           
           didDrawImage = true;
         }
       }
       catch (Exception e) {
         LOGGER.error("Error loading process diagram component", e);
       }
     }
     
     if (!didDrawImage)
     {
       StreamResource diagram = null;
       
 
       if (this.processDefinition.getDiagramResourceName() != null)
       {
         diagram = new ProcessDefinitionImageStreamResourceBuilder().buildStreamResource(this.processDefinition, this.repositoryService);
       }
       
       if (diagram != null)
       {
         Embedded embedded = new Embedded(null, diagram);
         embedded.setType(1);
         embedded.setSizeUndefined();
         
         Panel imagePanel = new Panel();
         imagePanel.addStyleName("light");
         imagePanel.setWidth(100.0F, 8);
         imagePanel.setHeight(100.0F, 8);
         HorizontalLayout panelLayout = new HorizontalLayout();
         panelLayout.setSizeUndefined();
         imagePanel.setContent(panelLayout);
         imagePanel.addComponent(embedded);
         this.processImageContainer.addComponent(imagePanel);
         
         didDrawImage = true;
       }
     }
     
     if (!didDrawImage) {
       Label noImageAvailable = new Label(this.i18nManager.getMessage("process.no.diagram"));
       this.processImageContainer.addComponent(noImageAvailable);
     }
     addComponent(this.processImageContainer);
   }
   
   protected void addEmptySpace(ComponentContainer container) {
     Label emptySpace = new Label("&nbsp;", 3);
     emptySpace.setSizeUndefined();
     container.addComponent(emptySpace);
   }
 }


