 package org.activiti.explorer.ui.management.processinstance;
 
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RuntimeService;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.AbstractTablePage;
 import org.activiti.explorer.ui.custom.ConfirmationDialogPopupWindow;
 import org.activiti.explorer.ui.event.ConfirmationEvent;
 import org.activiti.explorer.ui.event.ConfirmationEventListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeleteProcessInstanceClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 1L;
   protected String processInstanceId;
   protected AbstractTablePage processInstancePage;
   
   public DeleteProcessInstanceClickListener(String processInstanceId, AbstractTablePage processInstancePage)
   {
     this.processInstanceId = processInstanceId;
     this.processInstancePage = processInstancePage;
   }
   
   public void buttonClick(ClickEvent event) {
     I18nManager i18nManager = ExplorerApp.get().getI18nManager();
     ViewManager viewManager = ExplorerApp.get().getViewManager();
     
 
 
     ConfirmationDialogPopupWindow confirmPopup = new ConfirmationDialogPopupWindow(i18nManager.getMessage("process.instance.delete.popup.title", new Object[] { this.processInstanceId }), i18nManager.getMessage("process.instance.delete.popup.description", new Object[] { this.processInstanceId }));
     
     confirmPopup.addListener(new ConfirmationEventListener() {
       private static final long serialVersionUID = 1L;
       
       protected void confirmed(ConfirmationEvent event) { RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
         runtimeService.deleteProcessInstance(DeleteProcessInstanceClickListener.this.processInstanceId, null);
         DeleteProcessInstanceClickListener.this.processInstancePage.refreshSelectNext();
       }
       
     });
     viewManager.showPopupWindow(confirmPopup);
   }
 }


