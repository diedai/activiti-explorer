 package org.activiti.explorer.ui.process.listener;
 
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import org.activiti.editor.ui.NewModelPopupWindow;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 public class NewModelClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 1L;
   
   public void buttonClick(ClickEvent event)
   {
     ExplorerApp.get().getViewManager().showPopupWindow(new NewModelPopupWindow());
   }
 }


