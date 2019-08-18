 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Window;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class PopupWindow
   extends Window
 {
   private static final long serialVersionUID = 1L;
   
   public PopupWindow() {}
   
   public PopupWindow(String caption)
   {
     super(caption);
   }
   
   public void attach()
   {
     super.attach();
   }
   
 
   public void setParent(Component parent)
   {
     super.setParent(parent);
   }
 }


