 package org.activiti.editor.ui;
 
 import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
 import com.vaadin.ui.ComponentContainer;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.VerticalLayout;
 import java.io.ByteArrayInputStream;
 import java.io.InputStream;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Model;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class EditorProcessDefinitionInfoComponent
   extends VerticalLayout
 {
   protected static final Logger LOGGER = LoggerFactory.getLogger(EditorProcessDefinitionInfoComponent.class);
   
   private static final long serialVersionUID = 1L;
   
   protected transient RepositoryService repositoryService;
   
   protected I18nManager i18nManager;
   
   protected Model modelData;
   
   protected HorizontalLayout timeDetails;
   
   protected VerticalLayout processImageContainer;
   
 
   public EditorProcessDefinitionInfoComponent(Model model)
   {
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     this.modelData = model;
     
     addStyleName("block-holder");
     
     initImage();
   }
   
   protected void initImage() {
     this.processImageContainer = new VerticalLayout();
     
     Label processTitle = new Label(this.i18nManager.getMessage("process.header.diagram"));
     processTitle.addStyleName("h3");
     this.processImageContainer.addComponent(processTitle);
     
     StreamResource.StreamSource streamSource = null;
     final byte[] editorSourceExtra = this.repositoryService.getModelEditorSourceExtra(this.modelData.getId());
     if (editorSourceExtra != null) {
       streamSource = new StreamResource.StreamSource() {
         private static final long serialVersionUID = 1L;
         
         public InputStream getStream() {
           InputStream inStream = null;
           try {
             inStream = new ByteArrayInputStream(editorSourceExtra);
           } catch (Exception e) {
             EditorProcessDefinitionInfoComponent.LOGGER.warn("Error reading PNG in StreamSource", e);
           }
           return inStream;
         }
       };
     }
     
     if (streamSource != null) {
       Embedded embedded = new Embedded(null, new ImageStreamSource(streamSource, ExplorerApp.get()));
       embedded.setType(1);
       embedded.setSizeUndefined();
       
       Panel imagePanel = new Panel();
       imagePanel.addStyleName("light");
       imagePanel.setWidth(100.0F, 8);
       imagePanel.setHeight(700.0F, 0);
       HorizontalLayout panelLayout = new HorizontalLayout();
       panelLayout.setSizeUndefined();
       imagePanel.setContent(panelLayout);
       imagePanel.addComponent(embedded);
       
       this.processImageContainer.addComponent(imagePanel);
     } else {
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


