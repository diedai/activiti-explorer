 package org.activiti.explorer.ui.mainlayout;
 
 import org.activiti.explorer.ui.NoParamComponentFactory;
 import org.activiti.explorer.ui.alfresco.AlfrescoMainMenuBar;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MainMenuBarFactory
   extends NoParamComponentFactory<MainMenuBar>
 {
   protected Class<AlfrescoMainMenuBar> getAlfrescoComponentClass()
   {
     return AlfrescoMainMenuBar.class;
   }
   
   protected Class<MainMenuBar> getDefaultComponentClass()
   {
     return MainMenuBar.class;
   }
 }


