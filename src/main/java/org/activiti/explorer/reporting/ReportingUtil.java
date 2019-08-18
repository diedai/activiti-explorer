 package org.activiti.explorer.reporting;
 
 import java.sql.Connection;
 import java.sql.ResultSet;
 import java.sql.Statement;
 import org.activiti.bpmn.BpmnAutoLayout;
 import org.activiti.bpmn.model.BpmnModel;
 import org.activiti.bpmn.model.Process;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.delegate.DelegateExecution;
 import org.activiti.engine.impl.context.Context;
 import org.activiti.engine.impl.db.DbSqlSession;
 import org.activiti.engine.impl.interceptor.CommandContext;
 import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
 import org.activiti.engine.repository.DeploymentBuilder;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.workflow.simple.converter.WorkflowDefinitionConversion;
 import org.activiti.workflow.simple.converter.WorkflowDefinitionConversionFactory;
 import org.activiti.workflow.simple.definition.WorkflowDefinition;
 import org.apache.ibatis.session.SqlSession;
 
 
 
 
 
 
 
 
 
 
 public class ReportingUtil
 {
   public static Connection getCurrentDatabaseConnection()
   {
     return Context.getCommandContext().getDbSqlSession().getSqlSession().getConnection();
   }
   
   public static ResultSet executeSelectSqlQuery(String sql) throws Exception
   {
     Connection connection = getCurrentDatabaseConnection();
     Statement select = connection.createStatement();
     return select.executeQuery(sql);
   }
   
   public static ProcessDefinition getProcessDefinition(DelegateExecution delegateExecution) {
     ExecutionEntity executionEntity = (ExecutionEntity)delegateExecution;
     if (executionEntity.getProcessDefinition() != null) {
       return (ProcessDefinition)executionEntity.getProcessDefinition();
     }
     
 
 
 
     return (ProcessDefinition)ProcessEngines.getDefaultProcessEngine().getRepositoryService().createProcessDefinitionQuery().processDefinitionId(delegateExecution.getProcessDefinitionId()).singleResult();
   }
   
 
 
   public static void generateTaskDurationReport(String processDefinitionId)
   {
     ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
     RepositoryService repositoryService = processEngine.getRepositoryService();
     
 
 
     ProcessDefinition processDefinition = (ProcessDefinition)repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
     
 
     String reportDescription = "Average task duration report for process definition " + processDefinition.getName() + " ( version " + processDefinition.getVersion() + ")";
     
 
     String script = "importPackage(java.sql);importPackage(java.lang);importPackage(org.activiti.explorer.reporting);var processDefinitionId = '" + processDefinitionId + "';" + "" + "var result = ReportingUtil.executeSelectSqlQuery(\"select NAME_, avg(DURATION_) from ACT_HI_TASKINST where PROC_DEF_ID_ = '" + processDefinitionId + "' and END_TIME_ is not null group by NAME_\");" + "" + "var reportData = new ReportData();" + "var dataset = reportData.newDataset();" + "dataset.type = 'pieChart';" + "dataset.description = '" + reportDescription + "';" + "" + "while (result.next()) { " + "  var name = result.getString(1);" + "  var val = result.getLong(2) / 1000;" + "  dataset.add(name, val);" + "}" + "" + "execution.setVariable('reportData', reportData.toBytes());";
     
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
     WorkflowDefinition workflowDefinition = (WorkflowDefinition)new WorkflowDefinition().name(processDefinition.getName() + " task duration report").description(reportDescription).addScriptStep(script);
     
 
 
     WorkflowDefinitionConversion conversion = ExplorerApp.get().getWorkflowDefinitionConversionFactory().createWorkflowDefinitionConversion(workflowDefinition);
     conversion.convert();
     conversion.getBpmnModel().setTargetNamespace("activiti-report");
     
 
     BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(conversion.getBpmnModel());
     bpmnAutoLayout.execute();
     
 
     repositoryService.createDeployment()
       .name(processDefinition.getName() + " - task duration report")
       .addString(conversion.getProcess().getId() + ".bpmn20.xml", conversion.getBpmn20Xml())
       .deploy();
   }
 }


