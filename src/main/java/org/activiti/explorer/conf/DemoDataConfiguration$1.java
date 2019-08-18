 package org.activiti.explorer.conf;
 
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.impl.asyncexecutor.AsyncExecutor;
 import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
 import org.activiti.engine.impl.jobexecutor.JobExecutor;
 import org.activiti.engine.runtime.Clock;
 import org.activiti.engine.runtime.Job;
 import org.activiti.engine.runtime.JobQuery;
 import org.activiti.engine.task.Task;
 import org.activiti.engine.task.TaskQuery;
 import org.slf4j.Logger;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 class DemoDataConfiguration$1
   implements Runnable
 {
   DemoDataConfiguration$1(DemoDataConfiguration this$0) {}
   
   public void run()
   {
     if ((this.this$0.processEngineConfiguration.isAsyncExecutorEnabled()) && (this.this$0.processEngineConfiguration.getAsyncExecutor() != null)) {
       this.this$0.processEngineConfiguration.getAsyncExecutor().shutdown();
     } else if ((!this.this$0.processEngineConfiguration.isAsyncExecutorEnabled()) && (this.this$0.processEngineConfiguration.getJobExecutor() != null)) {
       this.this$0.processEngineConfiguration.getJobExecutor().shutdown();
     }
     
     Random random = new Random();
     
     Date now = new Date(new Date().getTime() - 86400000L);
     this.this$0.processEngineConfiguration.getClock().setCurrentTime(now);
     
     for (int i = 0; i < 50; i++)
     {
       if (random.nextBoolean()) {
         this.this$0.runtimeService.startProcessInstanceByKey("fixSystemFailure");
       }
       
       if (random.nextBoolean()) {
         this.this$0.identityService.setAuthenticatedUserId("kermit");
         Map<String, Object> variables = new HashMap();
         variables.put("customerName", "testCustomer");
         variables.put("details", "Looks very interesting!");
         variables.put("notEnoughInformation", Boolean.valueOf(false));
         this.this$0.runtimeService.startProcessInstanceByKey("reviewSaledLead", variables);
       }
       
       if (random.nextBoolean()) {
         this.this$0.runtimeService.startProcessInstanceByKey("escalationExample");
       }
       
       if (random.nextInt(100) < 20) {
         now = new Date(now.getTime() - 82800000L);
         this.this$0.processEngineConfiguration.getClock().setCurrentTime(now);
       }
     }
     
     List<Job> jobs = this.this$0.managementService.createJobQuery().list();
     for (int i = 0; i < jobs.size() / 2; i++) {
       this.this$0.processEngineConfiguration.getClock().setCurrentTime(((Job)jobs.get(i)).getDuedate());
       this.this$0.managementService.executeJob(((Job)jobs.get(i)).getId());
     }
     
     List<Task> tasks = this.this$0.taskService.createTaskQuery().list();
     while (!tasks.isEmpty()) {
       for (Task task : tasks)
       {
         if (task.getAssignee() == null) {
           String assignee = random.nextBoolean() ? "kermit" : "fozzie";
           this.this$0.taskService.claim(task.getId(), assignee);
         }
         
         this.this$0.processEngineConfiguration.getClock().setCurrentTime(new Date(task
           .getCreateTime().getTime() + random.nextInt(3600000)));
         
         this.this$0.taskService.complete(task.getId());
       }
       
       tasks = this.this$0.taskService.createTaskQuery().list();
     }
     
     this.this$0.processEngineConfiguration.getClock().reset();
     
     if ((this.this$0.processEngineConfiguration.isAsyncExecutorEnabled()) && (this.this$0.processEngineConfiguration.getAsyncExecutor() != null)) {
       this.this$0.processEngineConfiguration.getAsyncExecutor().start();
     } else if ((!this.this$0.processEngineConfiguration.isAsyncExecutorEnabled()) && (this.this$0.processEngineConfiguration.getJobExecutor() != null)) {
       this.this$0.processEngineConfiguration.getJobExecutor().start();
     }
     DemoDataConfiguration.LOGGER.info("Demo report data generated");
   }
 }


