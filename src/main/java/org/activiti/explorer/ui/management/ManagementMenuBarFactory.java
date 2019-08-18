 package org.activiti.explorer.ui.management;
 
 import org.activiti.explorer.ui.NoParamComponentFactory;
 import org.activiti.explorer.ui.alfresco.AlfrescoManagementMenuBar;
 import org.activiti.explorer.ui.custom.ToolBar;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ManagementMenuBarFactory
   extends NoParamComponentFactory<ToolBar>
 {
   protected Class<? extends ToolBar> getAlfrescoComponentClass()
   {
     return AlfrescoManagementMenuBar.class;
   }
   
   protected Class<? extends ToolBar> getDefaultComponentClass() {
     return ManagementMenuBar.class;
   }
 }


