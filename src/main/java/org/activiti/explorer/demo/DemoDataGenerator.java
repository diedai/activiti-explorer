 package org.activiti.explorer.demo;
 
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.node.ObjectNode;
 import java.io.InputStream;
 import java.util.Arrays;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;
 import org.activiti.editor.constants.ModelDataJsonConstants;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.GroupQuery;
 import org.activiti.engine.identity.Picture;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.engine.impl.ProcessEngineImpl;
 import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
 import org.activiti.engine.impl.jobexecutor.JobExecutor;
 import org.activiti.engine.impl.util.IoUtil;
 import org.activiti.engine.repository.Deployment;
 import org.activiti.engine.repository.DeploymentBuilder;
 import org.activiti.engine.repository.DeploymentQuery;
 import org.activiti.engine.repository.Model;
 import org.activiti.engine.repository.ModelQuery;
 import org.activiti.engine.runtime.Clock;
 import org.activiti.engine.runtime.Job;
 import org.activiti.engine.runtime.JobQuery;
 import org.activiti.engine.task.Task;
 import org.activiti.engine.task.TaskQuery;
 import org.apache.commons.io.IOUtils;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 
 
 
 
 
 public class DemoDataGenerator
   implements ModelDataJsonConstants
 {
   protected static final Logger LOGGER = LoggerFactory.getLogger(DemoDataGenerator.class);
   
   protected transient ProcessEngine processEngine;
   protected transient IdentityService identityService;
   protected transient RepositoryService repositoryService;
   protected boolean createDemoUsersAndGroups;
   protected boolean createDemoProcessDefinitions;
   protected boolean createDemoModels;
   protected boolean generateReportData;
   
   public void init()
   {
     this.identityService = this.processEngine.getIdentityService();
     this.repositoryService = this.processEngine.getRepositoryService();
     
     if (this.createDemoUsersAndGroups) {
       LOGGER.info("Initializing demo groups");
       initDemoGroups();
       LOGGER.info("Initializing demo users");
       initDemoUsers();
     }
     
     if (this.createDemoProcessDefinitions) {
       LOGGER.info("Initializing demo process definitions");
       initProcessDefinitions();
     }
     
     if (this.createDemoModels) {
       LOGGER.info("Initializing demo models");
       initModelData();
     }
     
     if (this.generateReportData) {
       LOGGER.info("Initializing demo report data");
       generateReportData();
     }
   }
   
   public void setProcessEngine(ProcessEngine processEngine) {
     this.processEngine = processEngine;
   }
   
   public void setCreateDemoUsersAndGroups(boolean createDemoUsersAndGroups) {
     this.createDemoUsersAndGroups = createDemoUsersAndGroups;
   }
   
   public void setCreateDemoProcessDefinitions(boolean createDemoProcessDefinitions) {
     this.createDemoProcessDefinitions = createDemoProcessDefinitions;
   }
   
   public void setCreateDemoModels(boolean createDemoModels) {
     this.createDemoModels = createDemoModels;
   }
   
   public void setGenerateReportData(boolean generateReportData) {
     this.generateReportData = generateReportData;
   }
   
   protected void initDemoGroups() {
     String[] assignmentGroups = { "management", "sales", "marketing", "engineering" };
		String[] arrayOfString1 = assignmentGroups;
		int i = arrayOfString1.length;
		String groupId;
		for (int str1 = 0; str1 < i; str1++) { groupId = arrayOfString1[str1];
       createGroup(groupId, "assignment");
     }
     
     String[] securityGroups = { "user", "admin" };
     String[] arrayOfString2 = securityGroups;int str1 = arrayOfString2.length; 
     for (str1 = 0; str1 < i; str1++) { groupId = arrayOfString1[str1];
       createGroup(groupId, "security-role");
     }
   }
   
   protected void createGroup(String groupId, String type) {
     if (this.identityService.createGroupQuery().groupId(groupId).count() == 0L) {
       Group newGroup = this.identityService.newGroup(groupId);
       newGroup.setName(groupId.substring(0, 1).toUpperCase() + groupId.substring(1));
       newGroup.setType(type);
       this.identityService.saveGroup(newGroup);
     }
   }
   
   protected void initDemoUsers() {
     createUser("kermit", "Kermit", "The Frog", "kermit", "kermit@activiti.org", "org/activiti/explorer/images/kermit.jpg", 
     
       Arrays.asList(new String[] { "management", "sales", "marketing", "engineering", "user", "admin" }), 
       Arrays.asList(new String[] { "birthDate", "10-10-1955", "jobTitle", "Muppet", "location", "Hollywoord", "phone", "+123456789", "twitterName", "alfresco", "skype", "activiti_kermit_frog" }));
     
 
     createUser("gonzo", "Gonzo", "The Great", "gonzo", "gonzo@activiti.org", "org/activiti/explorer/images/gonzo.jpg", 
     
       Arrays.asList(new String[] { "management", "sales", "marketing", "user" }), null);
     
     createUser("fozzie", "Fozzie", "Bear", "fozzie", "fozzie@activiti.org", "org/activiti/explorer/images/fozzie.jpg", 
     
       Arrays.asList(new String[] { "marketing", "engineering", "user" }), null);
   }
   
 
 
   protected void createUser(String userId, String firstName, String lastName, String password, String email, String imageResource, List<String> groups, List<String> userInfo)
   {
     if (this.identityService.createUserQuery().userId(userId).count() == 0L)
     {
 
 
       User user = this.identityService.newUser(userId);
       user.setFirstName(firstName);
       user.setLastName(lastName);
       user.setPassword(password);
       user.setEmail(email);
       this.identityService.saveUser(user);
       
       if (groups != null) {
         for (String group : groups) {
           this.identityService.createMembership(userId, group);
         }
       }
     }
     
 
 
 
     if (imageResource != null) {
       byte[] pictureBytes = IoUtil.readInputStream(getClass().getClassLoader().getResourceAsStream(imageResource), null);
       Picture picture = new Picture(pictureBytes, "image/jpeg");
       this.identityService.setUserPicture(userId, picture);
     }
     
 
     if (userInfo != null) {
       for (int i = 0; i < userInfo.size(); i += 2) {
         this.identityService.setUserInfo(userId, (String)userInfo.get(i), (String)userInfo.get(i + 1));
       }
     }
   }
   
 
   protected void initProcessDefinitions()
   {
     String deploymentName = "Demo processes";
     List<Deployment> deploymentList = this.repositoryService.createDeploymentQuery().deploymentName(deploymentName).list();
     
     if ((deploymentList == null) || (deploymentList.isEmpty()))
     {
 
 
 
 
 
 
 
 
 
 
       this.repositoryService.createDeployment().name(deploymentName).addClasspathResource("org/activiti/explorer/demo/process/createTimersProcess.bpmn20.xml").addClasspathResource("org/activiti/explorer/demo/process/VacationRequest.bpmn20.xml").addClasspathResource("org/activiti/explorer/demo/process/VacationRequest.png").addClasspathResource("org/activiti/explorer/demo/process/FixSystemFailureProcess.bpmn20.xml").addClasspathResource("org/activiti/explorer/demo/process/FixSystemFailureProcess.png").addClasspathResource("org/activiti/explorer/demo/process/simple-approval.bpmn20.xml").addClasspathResource("org/activiti/explorer/demo/process/Helpdesk.bpmn20.xml").addClasspathResource("org/activiti/explorer/demo/process/Helpdesk.png").addClasspathResource("org/activiti/explorer/demo/process/reviewSalesLead.bpmn20.xml").deploy();
     }
     
     String reportDeploymentName = "Demo reports";
     deploymentList = this.repositoryService.createDeploymentQuery().deploymentName(reportDeploymentName).list();
     if ((deploymentList == null) || (deploymentList.isEmpty()))
     {
 
 
 
 
 
       this.repositoryService.createDeployment().name(reportDeploymentName).addClasspathResource("org/activiti/explorer/demo/process/reports/taskDurationForProcessDefinition.bpmn20.xml").addClasspathResource("org/activiti/explorer/demo/process/reports/processInstanceOverview.bpmn20.xml").addClasspathResource("org/activiti/explorer/demo/process/reports/helpdeskFirstLineVsEscalated.bpmn20.xml").addClasspathResource("org/activiti/explorer/demo/process/reports/employeeProductivity.bpmn20.xml").deploy();
     }
   }
   
   protected void generateReportData()
   {
     if (this.generateReportData)
     {
 
 
       Thread thread = new Thread(new Runnable()
       {
 
         public void run()
         {
           ((ProcessEngineImpl)DemoDataGenerator.this.processEngine).getProcessEngineConfiguration().getJobExecutor().shutdown();
           
           Random random = new Random();
           
           Date now = new Date(new Date().getTime() - 86400000L);
           ((ProcessEngineImpl)DemoDataGenerator.this.processEngine).getProcessEngineConfiguration().getClock().setCurrentTime(now);
           
           for (int i = 0; i < 50; i++)
           {
             if (random.nextBoolean()) {
               DemoDataGenerator.this.processEngine.getRuntimeService().startProcessInstanceByKey("fixSystemFailure");
             }
             
             if (random.nextBoolean()) {
               DemoDataGenerator.this.processEngine.getIdentityService().setAuthenticatedUserId("kermit");
               Map<String, Object> variables = new HashMap();
               variables.put("customerName", "testCustomer");
               variables.put("details", "Looks very interesting!");
               variables.put("notEnoughInformation", Boolean.valueOf(false));
               DemoDataGenerator.this.processEngine.getRuntimeService().startProcessInstanceByKey("reviewSaledLead", variables);
             }
             
             if (random.nextBoolean()) {
               DemoDataGenerator.this.processEngine.getRuntimeService().startProcessInstanceByKey("escalationExample");
             }
             
             if (random.nextInt(100) < 20) {
               now = new Date(now.getTime() - 82800000L);
               ((ProcessEngineImpl)DemoDataGenerator.this.processEngine).getProcessEngineConfiguration().getClock().setCurrentTime(now);
             }
           }
           
           List<Job> jobs = DemoDataGenerator.this.processEngine.getManagementService().createJobQuery().list();
           for (int i = 0; i < jobs.size() / 2; i++) {
             ((ProcessEngineImpl)DemoDataGenerator.this.processEngine).getProcessEngineConfiguration().getClock().setCurrentTime(((Job)jobs.get(i)).getDuedate());
             DemoDataGenerator.this.processEngine.getManagementService().executeJob(((Job)jobs.get(i)).getId());
           }
           
           List<Task> tasks = DemoDataGenerator.this.processEngine.getTaskService().createTaskQuery().list();
           while (!tasks.isEmpty()) {
             for (Task task : tasks)
             {
               if (task.getAssignee() == null) {
                 String assignee = random.nextBoolean() ? "kermit" : "fozzie";
                 DemoDataGenerator.this.processEngine.getTaskService().claim(task.getId(), assignee);
               }
               
               ((ProcessEngineImpl)DemoDataGenerator.this.processEngine).getProcessEngineConfiguration().getClock().setCurrentTime(new Date(task.getCreateTime().getTime() + random.nextInt(3600000)));
               
               DemoDataGenerator.this.processEngine.getTaskService().complete(task.getId());
             }
             
             tasks = DemoDataGenerator.this.processEngine.getTaskService().createTaskQuery().list();
           }
           
           ((ProcessEngineImpl)DemoDataGenerator.this.processEngine).getProcessEngineConfiguration().getClock().reset();
           
           ((ProcessEngineImpl)DemoDataGenerator.this.processEngine).getProcessEngineConfiguration().getJobExecutor().start();
           DemoDataGenerator.LOGGER.info("Demo report data generated");
         }
         
       });
       thread.start();
     }
   }
   
   protected void initModelData()
   {
     createModelData("Demo model", "This is a demo model", "org/activiti/explorer/demo/model/test.model.json");
   }
   
   protected void createModelData(String name, String description, String jsonFile) {
     List<Model> modelList = this.repositoryService.createModelQuery().modelName("Demo model").list();
     
     if ((modelList == null) || (modelList.isEmpty()))
     {
       Model model = this.repositoryService.newModel();
       model.setName(name);
       
       ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
       modelObjectNode.put("name", name);
       modelObjectNode.put("description", description);
       model.setMetaInfo(modelObjectNode.toString());
       
       this.repositoryService.saveModel(model);
       try
       {
         InputStream svgStream = getClass().getClassLoader().getResourceAsStream("org/activiti/explorer/demo/model/test.svg");
         this.repositoryService.addModelEditorSourceExtra(model.getId(), IOUtils.toByteArray(svgStream));
       } catch (Exception e) {
         LOGGER.warn("Failed to read SVG", e);
       }
       try
       {
         InputStream editorJsonStream = getClass().getClassLoader().getResourceAsStream(jsonFile);
         this.repositoryService.addModelEditorSource(model.getId(), IOUtils.toByteArray(editorJsonStream));
       } catch (Exception e) {
         LOGGER.warn("Failed to read editor JSON", e);
       }
     }
   }
 }


