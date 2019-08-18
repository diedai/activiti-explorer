 package org.activiti.explorer.ui.process.listener;
 
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import org.activiti.editor.ui.ImportUploadReceiver;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.ImportPopupWindow;
 
 
 
 public class ImportModelClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 1L;
   
   public void buttonClick(ClickEvent event)
   {
     ImportUploadReceiver receiver = new ImportUploadReceiver();
     
 
     ImportPopupWindow importPopupWindow = new ImportPopupWindow(ExplorerApp.get().getI18nManager().getMessage("model.import"), ExplorerApp.get().getI18nManager().getMessage("model.import.description"), receiver);
     
     importPopupWindow.addFinishedListener(receiver);
     ExplorerApp.get().getViewManager().showPopupWindow(importPopupWindow);
   }
 }


