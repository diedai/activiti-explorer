package org.activiti.explorer;

import com.vaadin.ui.Window;
import org.activiti.workflow.simple.definition.WorkflowDefinition;

public abstract interface ViewManager
{
  public static final String MAIN_NAVIGATION_TASK = "task";
  public static final String MAIN_NAVIGATION_PROCESS = "process";
  public static final String MAIN_NAVIGATION_MANAGE = "manage";
  public static final String MAIN_NAVIGATION_REPORT = "report";
  
  public abstract void showLoginPage();
  
  public abstract void showDefaultPage();
  
  public abstract void showPopupWindow(Window paramWindow);
  
  public abstract void showTaskPage(String paramString);
  
  public abstract void showTasksPage();
  
  public abstract void showTasksPage(String paramString);
  
  public abstract void showInboxPage();
  
  public abstract void showInboxPage(String paramString);
  
  public abstract void showQueuedPage(String paramString);
  
  public abstract void showQueuedPage(String paramString1, String paramString2);
  
  public abstract void showInvolvedPage();
  
  public abstract void showInvolvedPage(String paramString);
  
  public abstract void showArchivedPage();
  
  public abstract void showArchivedPage(String paramString);
  
  public abstract void showDeployedProcessDefinitionPage();
  
  public abstract void showDeployedProcessDefinitionPage(String paramString);
  
  public abstract void showEditorProcessDefinitionPage();
  
  public abstract void showEditorProcessDefinitionPage(String paramString);
  
  public abstract void showMyProcessInstancesPage();
  
  public abstract void showMyProcessInstancesPage(String paramString);
  
  public abstract void showSimpleTableProcessEditor(String paramString1, String paramString2);
  
  public abstract void showSimpleTableProcessEditor(String paramString, WorkflowDefinition paramWorkflowDefinition);
  
  public abstract void showRunReportPage();
  
  public abstract void showRunReportPage(String paramString);
  
  public abstract void showSavedReportPage();
  
  public abstract void showSavedReportPage(String paramString);
  
  public abstract void showDatabasePage();
  
  public abstract void showDatabasePage(String paramString);
  
  public abstract void showDeploymentPage();
  
  public abstract void showDeploymentPage(String paramString);
  
  public abstract void showActiveProcessDefinitionsPage();
  
  public abstract void showActiveProcessDefinitionsPage(String paramString);
  
  public abstract void showSuspendedProcessDefinitionsPage();
  
  public abstract void showSuspendedProcessDefinitionsPage(String paramString);
  
  public abstract void showJobPage();
  
  public abstract void showJobPage(String paramString);
  
  public abstract void showUserPage();
  
  public abstract void showUserPage(String paramString);
  
  public abstract void showGroupPage();
  
  public abstract void showGroupPage(String paramString);
  
  public abstract void showProcessInstancePage();
  
  public abstract void showProcessInstancePage(String paramString);
  
  public abstract void showAdministrationPage();
  
  public abstract void showAdministrationPage(String paramString);
  
  public abstract void showCrystalBallPage();
  
  public abstract void showProfilePopup(String paramString);
}


