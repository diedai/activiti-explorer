 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessModelNavigator
   implements Navigator
 {
   public static final String PROCESS_MODEL_URI_PART = "processmodel";
   
   public String getTrigger()
   {
     return "processmodel";
   }
   
   public void handleNavigation(UriFragment uriFragment) {
     String modelId = uriFragment.getUriPart(1);
     
     if (modelId != null) {
       ExplorerApp.get().getViewManager().showEditorProcessDefinitionPage(modelId);
     } else {
       ExplorerApp.get().getViewManager().showEditorProcessDefinitionPage();
     }
   }
 }


