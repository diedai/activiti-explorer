 package org.activiti.explorer.ui.process;
 
 import com.vaadin.terminal.StreamResource;
 import com.vaadin.terminal.StreamResource.StreamSource;
 import java.io.InputStream;
 import java.util.Collections;
 import java.util.UUID;
 import org.activiti.bpmn.model.BpmnModel;
 import org.activiti.engine.ProcessEngineConfiguration;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.impl.RepositoryServiceImpl;
 import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.runtime.ProcessInstance;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ui.util.InputStreamStreamSource;
 import org.activiti.image.ProcessDiagramGenerator;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessDefinitionImageStreamResourceBuilder
 {
   protected static final Logger LOGGER = LoggerFactory.getLogger(ProcessDefinitionImageStreamResourceBuilder.class);
   
   public StreamResource buildStreamResource(ProcessDefinition processDefinition, RepositoryService repositoryService)
   {
     StreamResource imageResource = null;
     
     if (processDefinition.getDiagramResourceName() != null) {
       InputStream definitionImageStream = repositoryService.getResourceAsStream(processDefinition
         .getDeploymentId(), processDefinition.getDiagramResourceName());
       
       StreamResource.StreamSource streamSource = new InputStreamStreamSource(definitionImageStream);
       
 
 
       String imageExtension = extractImageExtension(processDefinition.getDiagramResourceName());
       String fileName = processDefinition.getId() + "." + imageExtension;
       
       imageResource = new StreamResource(streamSource, fileName, ExplorerApp.get());
     }
     
     return imageResource;
   }
   
 
   public StreamResource buildStreamResource(ProcessInstance processInstance, RepositoryService repositoryService, RuntimeService runtimeService, ProcessDiagramGenerator diagramGenerator, ProcessEngineConfiguration processEngineConfig)
   {
     StreamResource imageResource = null;
     
     ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processInstance
       .getProcessDefinitionId());
     
     if ((processDefinition != null) && (processDefinition.isGraphicalNotationDefined())) {
       try
       {
         BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
         InputStream definitionImageStream = diagramGenerator.generateDiagram(bpmnModel, "png", runtimeService
           .getActiveActivityIds(processInstance.getId()), Collections.emptyList(), processEngineConfig
           .getActivityFontName(), processEngineConfig.getLabelFontName(), processEngineConfig.getAnnotationFontName(), processEngineConfig
           .getClassLoader(), 1.0D);
         
         if (definitionImageStream != null) {
           StreamResource.StreamSource streamSource = new InputStreamStreamSource(definitionImageStream);
           
 
           String imageExtension = extractImageExtension(processDefinition.getDiagramResourceName());
           String fileName = processInstance.getId() + UUID.randomUUID() + "." + imageExtension;
           
           imageResource = new StreamResource(streamSource, fileName, ExplorerApp.get());
         }
       }
       catch (Throwable t) {
         LOGGER.warn("Process image cannot be generated due to exception: {} - {}", t.getClass().getName(), t.getMessage());
       }
     }
     return imageResource;
   }
   
 
 
   public StreamResource buildStreamResource(String processInstanceId, String processDefinitionId, RepositoryService repositoryService, RuntimeService runtimeService, ProcessDiagramGenerator diagramGenerator, ProcessEngineConfiguration processEngineConfig)
   {
     StreamResource imageResource = null;
     
     ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);
     
     if ((processDefinition != null) && (processDefinition.isGraphicalNotationDefined()))
     {
       BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
       InputStream definitionImageStream = diagramGenerator.generateDiagram(bpmnModel, "png", runtimeService
         .getActiveActivityIds(processInstanceId), Collections.emptyList(), processEngineConfig
         .getActivityFontName(), processEngineConfig.getLabelFontName(), processEngineConfig.getAnnotationFontName(), processEngineConfig
         .getClassLoader(), 1.0D);
       
       StreamResource.StreamSource streamSource = new InputStreamStreamSource(definitionImageStream);
       
 
       String imageExtension = extractImageExtension(processDefinition.getDiagramResourceName());
       String fileName = processInstanceId + UUID.randomUUID() + "." + imageExtension;
       
       imageResource = new StreamResource(streamSource, fileName, ExplorerApp.get());
     }
     return imageResource;
   }
   
   protected String extractImageExtension(String diagramResourceName) {
     String[] parts = diagramResourceName.split(".");
     if (parts.length > 1) {
       return parts[(parts.length - 1)];
     }
     return "png";
   }
 }


