 package org.activiti.explorer.ui.alfresco;
 
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.process.ProcessDefinitionPage;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AlfrescoProcessDefinitionPage
   extends ProcessDefinitionPage
 {
   private static final long serialVersionUID = 1L;
   
   public AlfrescoProcessDefinitionPage() {}
   
   public AlfrescoProcessDefinitionPage(String processDefinitionId)
   {
     super(processDefinitionId);
   }
   
   protected ToolBar createMenuBar()
   {
     return new AlfrescoManagementMenuBar();
   }
   
   protected void showProcessDefinitionDetail(String processDefinitionId) {
     this.detailPanel = new AlfrescoProcessDefinitionDetailPanel(processDefinitionId, this);
     setDetailComponent(this.detailPanel);
     changeUrl(processDefinitionId);
   }
 }


