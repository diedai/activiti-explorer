 package org.activiti.explorer.ui.management;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ui.AbstractTablePage;
 import org.activiti.explorer.ui.ComponentFactory;
 import org.activiti.explorer.ui.custom.ToolBar;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class ManagementPage
   extends AbstractTablePage
 {
   private static final long serialVersionUID = 1L;
   
   protected ToolBar createMenuBar()
   {
     return (ToolBar)ExplorerApp.get().getComponentFactory(ManagementMenuBarFactory.class).create();
   }
 }


