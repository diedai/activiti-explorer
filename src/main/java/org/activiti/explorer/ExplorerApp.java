 package org.activiti.explorer;
 
 import com.vaadin.Application;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.Terminal.ErrorEvent;
 import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
 import java.util.List;
 import java.util.Locale;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.http.HttpSession;
 import org.activiti.crystalball.simulator.SimulationDebugger;
 import org.activiti.crystalball.simulator.SimulationEvent;
 import org.activiti.engine.ActivitiException;
 import org.activiti.engine.impl.identity.Authentication;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.ComponentFactory;
 import org.activiti.explorer.ui.MainWindow;
 import org.activiti.explorer.ui.content.AttachmentRendererManager;
 import org.activiti.explorer.ui.form.FormPropertyRendererManager;
 import org.activiti.explorer.ui.login.LoginHandler;
 import org.activiti.explorer.ui.variable.VariableRendererManager;
 import org.activiti.workflow.simple.converter.WorkflowDefinitionConversionFactory;
 import org.activiti.workflow.simple.converter.json.SimpleWorkflowJsonConverter;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ExplorerApp
   extends Application
   implements HttpServletRequestListener
 {
   private static final long serialVersionUID = -1L;
   protected static transient ThreadLocal<ExplorerApp> current = new ThreadLocal();
   
   protected String environment;
   
   protected boolean useJavascriptDiagram;
   
   protected MainWindow mainWindow;
   protected ViewManager viewManager;
   protected NotificationManager notificationManager;
   protected I18nManager i18nManager;
   protected AttachmentRendererManager attachmentRendererManager;
   protected FormPropertyRendererManager formPropertyRendererManager;
   protected VariableRendererManager variableRendererManager;
   protected LoginHandler loginHandler;
   protected ComponentFactories componentFactories;
   protected WorkflowDefinitionConversionFactory workflowDefinitionConversionFactory;
   protected SimpleWorkflowJsonConverter simpleWorkflowJsonConverter;
   protected boolean invalidatedSession = false;
   
   protected List<String> adminGroups;
   
   protected List<String> userGroups;
   protected String crystalBallCurrentDefinitionId = null;
   protected String crystalBallCurrentInstanceId = null;
   protected List<SimulationEvent> crystalBallSimulationEvents = null;
   protected transient SimulationDebugger crystalBallSimulationDebugger = null;
   
   public void init() {
     setMainWindow(this.mainWindow);
     this.mainWindow.showLoginPage();
   }
   
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   public void close()
   {
     LoggedInUser theUser = getLoggedInUser();
     
 
     setUser(null);
     
 
     getLoginHandler().logout(theUser);
     
     this.invalidatedSession = false;
     super.close();
   }
   
   public static ExplorerApp get() {
     return (ExplorerApp)current.get();
   }
   
   public LoggedInUser getLoggedInUser() {
     return (LoggedInUser)getUser();
   }
   
   public String getEnvironment() {
     return this.environment;
   }
   
 
   public ViewManager getViewManager()
   {
     return this.viewManager;
   }
   
   public I18nManager getI18nManager() {
     return this.i18nManager;
   }
   
   public NotificationManager getNotificationManager() {
     return this.notificationManager;
   }
   
 
   public AttachmentRendererManager getAttachmentRendererManager()
   {
     return this.attachmentRendererManager;
   }
   
   public FormPropertyRendererManager getFormPropertyRendererManager() {
     return this.formPropertyRendererManager;
   }
   
   public void setFormPropertyRendererManager(FormPropertyRendererManager formPropertyRendererManager) {
     this.formPropertyRendererManager = formPropertyRendererManager;
   }
   
   public <T> ComponentFactory<T> getComponentFactory(Class<? extends ComponentFactory<T>> clazz) {
     return this.componentFactories.get(clazz);
   }
   
   public LoginHandler getLoginHandler() {
     return this.loginHandler;
   }
   
   public void setVariableRendererManager(VariableRendererManager variableRendererManager) {
     this.variableRendererManager = variableRendererManager;
   }
   
   public VariableRendererManager getVariableRendererManager() {
     return this.variableRendererManager;
   }
   
   public WorkflowDefinitionConversionFactory getWorkflowDefinitionConversionFactory() {
     return this.workflowDefinitionConversionFactory;
   }
   
   public void setLocale(Locale locale) {
     super.setLocale(locale);
     if (this.i18nManager != null) {
       this.i18nManager.setLocale(locale);
     }
   }
   
 
 
   public void onRequestStart(HttpServletRequest request, HttpServletResponse response)
   {
     current.set(this);
     
 
     LoggedInUser user = (LoggedInUser)getUser();
     if (user == null)
     {
       user = this.loginHandler.authenticate(request, response);
       if (user == null) {
         if ((this.mainWindow != null) && (!this.mainWindow.isShowingLoginPage())) {
           this.viewManager.showLoginPage();
         }
       } else {
         setUser(user);
       }
     }
     
     if (user != null) {
       Authentication.setAuthenticatedUserId(user.getId());
       if ((this.mainWindow != null) && (this.mainWindow.isShowingLoginPage())) {
         this.viewManager.showDefaultPage();
       }
     }
     
 
     this.loginHandler.onRequestStart(request, response);
   }
   
   public void onRequestEnd(HttpServletRequest request, HttpServletResponse response)
   {
     current.remove();
     
 
     Authentication.setAuthenticatedUserId(null);
     
 
     this.loginHandler.onRequestEnd(request, response);
     
     if ((!isRunning()) && (!this.invalidatedSession))
     {
 
 
       if (request.getSession(false) != null) {
         request.getSession().invalidate();
         this.invalidatedSession = true;
       }
     }
   }
   
 
 
   public void terminalError(Terminal.ErrorEvent event)
   {
     super.terminalError(event);
     
 
 
     Throwable exception = event.getThrowable().getCause();
     int depth = 0;
     while ((exception != null) && (depth < 20) && (!(exception instanceof ActivitiException))) {
       exception = exception.getCause();
       depth++;
     }
     
     if (exception == null) {
       exception = event.getThrowable().getCause();
     }
     this.notificationManager.showErrorNotification("uncaught.exception", exception.getMessage());
   }
   
 
   public void setCurrentUriFragment(UriFragment fragment)
   {
     this.mainWindow.setCurrentUriFragment(fragment);
   }
   
   public UriFragment getCurrentUriFragment() { return this.mainWindow.getCurrentUriFragment(); }
   
 
 
   public void setEnvironment(String environment)
   {
     this.environment = environment;
   }
   
   public boolean isUseJavascriptDiagram() { return this.useJavascriptDiagram; }
   
   public void setUseJavascriptDiagram(boolean useJavascriptDiagram) {
     this.useJavascriptDiagram = useJavascriptDiagram;
   }
   
   public void setApplicationMainWindow(MainWindow mainWindow) { this.mainWindow = mainWindow; }
   
   public void setViewManager(ViewManager viewManager) {
     this.viewManager = viewManager;
   }
   
   public void setNotificationManager(NotificationManager notificationManager) { this.notificationManager = notificationManager; }
   
   public void setI18nManager(I18nManager i18nManager) {
     this.i18nManager = i18nManager;
   }
   
   public void setAttachmentRendererManager(AttachmentRendererManager attachmentRendererManager) { this.attachmentRendererManager = attachmentRendererManager; }
   
   public void setComponentFactories(ComponentFactories componentFactories) {
     this.componentFactories = componentFactories;
   }
   
   public void setLoginHandler(LoginHandler loginHandler) { this.loginHandler = loginHandler; }
   
   public void setWorkflowDefinitionConversionFactory(WorkflowDefinitionConversionFactory workflowDefinitionConversionFactory) {
     this.workflowDefinitionConversionFactory = workflowDefinitionConversionFactory;
   }
   
   public List<String> getAdminGroups() { return this.adminGroups; }
   
   public void setAdminGroups(List<String> adminGroups) {
     this.adminGroups = adminGroups;
   }
   
   public List<String> getUserGroups() { return this.userGroups; }
   
   public void setUserGroups(List<String> userGroups) {
     this.userGroups = userGroups;
   }
   
   public SimpleWorkflowJsonConverter getSimpleWorkflowJsonConverter() { return this.simpleWorkflowJsonConverter; }
   
   public void setSimpleWorkflowJsonConverter(SimpleWorkflowJsonConverter simpleWorkflowJsonConverter) {
     this.simpleWorkflowJsonConverter = simpleWorkflowJsonConverter;
   }
   
   public String getCrystalBallCurrentDefinitionId() {
     return this.crystalBallCurrentDefinitionId;
   }
   
   public void setCrystalBallCurrentDefinitionId(String crystalBallCurrentDefinitionId) {
     this.crystalBallCurrentDefinitionId = crystalBallCurrentDefinitionId;
   }
   
   public String getCrystalBallCurrentInstanceId() {
     return this.crystalBallCurrentInstanceId;
   }
   
   public void setCrystalBallCurrentInstanceId(String crystalBallCurrentInstanceId) {
     this.crystalBallCurrentInstanceId = crystalBallCurrentInstanceId;
   }
   
   public List<SimulationEvent> getCrystalBallSimulationEvents() {
     return this.crystalBallSimulationEvents;
   }
   
   public void setCrystalBallSimulationEvents(List<SimulationEvent> crystalBallSimulationEvents) {
     this.crystalBallSimulationEvents = crystalBallSimulationEvents;
   }
   
   public SimulationDebugger getCrystalBallSimulationDebugger() {
     return this.crystalBallSimulationDebugger;
   }
   
   public void setCrystalBallSimulationDebugger(SimulationDebugger crystalBallSimulationDebugger) {
     this.crystalBallSimulationDebugger = crystalBallSimulationDebugger;
   }
 }


