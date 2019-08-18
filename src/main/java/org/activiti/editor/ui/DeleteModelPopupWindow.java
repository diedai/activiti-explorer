 package org.activiti.editor.ui;
 
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.editor.constants.ModelDataJsonConstants;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Model;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.PopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeleteModelPopupWindow
   extends PopupWindow
   implements ModelDataJsonConstants
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected VerticalLayout windowLayout;
   protected Model modelData;
   protected transient RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   
   public DeleteModelPopupWindow(Model model) {
     this.modelData = model;
     this.windowLayout = ((VerticalLayout)getContent());
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     initWindow();
     addDeleteWarning();
     addButtons();
   }
   
   protected void initWindow() {
     this.windowLayout.setSpacing(true);
     addStyleName("light");
     setModal(true);
     setWidth("300px");
     center();
     
     setCaption(this.i18nManager.getMessage("process.delete.popup.caption", new Object[] { this.modelData.getName() }));
   }
   
   protected void addDeleteWarning() {
     Label deleteLabel = new Label(this.i18nManager.getMessage("process.delete.popup.message"));
     deleteLabel.addStyleName("light");
     addComponent(deleteLabel);
     
 
     Label emptySpace = new Label("&nbsp;", 3);
     addComponent(emptySpace);
   }
   
   protected void addButtons()
   {
     Button cancelButton = new Button(this.i18nManager.getMessage("button.cancel"));
     cancelButton.addStyleName("small");
     cancelButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         DeleteModelPopupWindow.this.close();
       }
       
 
     });
     Button deleteButton = new Button(this.i18nManager.getMessage("process.delete.popup.delete.button"));
     deleteButton.addStyleName("small");
     deleteButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         DeleteModelPopupWindow.this.repositoryService.deleteModel(DeleteModelPopupWindow.this.modelData.getId());
         DeleteModelPopupWindow.this.close();
         ExplorerApp.get().getViewManager().showEditorProcessDefinitionPage();
       }
       
 
     });
     HorizontalLayout buttonLayout = new HorizontalLayout();
     buttonLayout.setSpacing(true);
     buttonLayout.addComponent(cancelButton);
     buttonLayout.addComponent(deleteButton);
     addComponent(buttonLayout);
     this.windowLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
   }
 }


