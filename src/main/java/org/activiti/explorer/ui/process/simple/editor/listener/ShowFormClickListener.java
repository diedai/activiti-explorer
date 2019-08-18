 package org.activiti.explorer.ui.process.simple.editor.listener;
 
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.process.simple.editor.FormPopupWindow;
 import org.activiti.explorer.ui.process.simple.editor.table.TaskFormModel;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ShowFormClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 3881133002111623189L;
   protected TaskFormModel formModel;
   protected Object taskItemId;
   
   public ShowFormClickListener(TaskFormModel formModel, Object taskItemId)
   {
     this.formModel = formModel;
     this.taskItemId = taskItemId;
   }
   
   public void buttonClick(ClickEvent event) {
     ExplorerApp.get().getViewManager().showPopupWindow(new FormPopupWindow(this.taskItemId, this.formModel));
   }
 }


