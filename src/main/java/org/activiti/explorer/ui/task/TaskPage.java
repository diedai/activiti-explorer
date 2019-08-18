 package org.activiti.explorer.ui.task;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Table;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Task;
 import org.activiti.engine.task.TaskQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.AbstractTablePage;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.TaskListHeader;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class TaskPage
   extends AbstractTablePage
 {
   private static final long serialVersionUID = 1L;
   protected transient TaskService taskService;
   protected String taskId;
   protected Table taskTable;
   protected LazyLoadingContainer taskListContainer;
   protected LazyLoadingQuery lazyLoadingQuery;
   protected TaskEventsPanel taskEventPanel;
   
   public TaskPage()
   {
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
   }
   
   public TaskPage(String taskId) {
     this();
     this.taskId = taskId;
   }
   
   protected void initUi()
   {
     super.initUi();
     if (this.taskId == null) {
       selectElement(0);
     } else {
       int index = this.taskListContainer.getIndexForObjectId(this.taskId);
       selectElement(index);
     }
     
     if (this.taskListContainer.size() == 0) {
       ExplorerApp.get().setCurrentUriFragment(getUriFragment(null));
     }
   }
   
   protected ToolBar createMenuBar()
   {
     return new TaskMenuBar();
   }
   
   protected Table createList()
   {
     this.taskTable = new Table();
     this.taskTable.addStyleName("task-list");
     this.taskTable.addStyleName("scrollable");
     
 
     this.taskTable.addListener(getListSelectionListener());
     
     this.lazyLoadingQuery = createLazyLoadingQuery();
     this.taskListContainer = new LazyLoadingContainer(this.lazyLoadingQuery, 30);
     this.taskTable.setContainerDataSource(this.taskListContainer);
     
 
     this.taskTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.TASK_22));
     this.taskTable.setColumnWidth("icon", 22);
     
     this.taskTable.addContainerProperty("name", String.class, null);
     this.taskTable.setColumnHeaderMode(-1);
     
     return this.taskTable;
   }
   
   protected Property.ValueChangeListener getListSelectionListener() {
     return new Property.ValueChangeListener() {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) { Item item = TaskPage.this.taskTable.getItem(event.getProperty().getValue());
         
         if (item != null) {
           String id = (String)item.getItemProperty("id").getValue();
           TaskPage.this.setDetailComponent(TaskPage.this.createDetailComponent(id));
           
           UriFragment taskFragment = TaskPage.this.getUriFragment(id);
           ExplorerApp.get().setCurrentUriFragment(taskFragment);
         }
         else {
           TaskPage.this.setDetailComponent(null);
           TaskPage.this.taskEventPanel.setTaskId(null);
           ExplorerApp.get().setCurrentUriFragment(TaskPage.this.getUriFragment(null));
         }
       }
     };
   }
   
   protected Component createDetailComponent(String id) {
     Task task = (Task)((TaskQuery)this.taskService.createTaskQuery().taskId(id)).singleResult();
     Component detailComponent = new TaskDetailPanel(task, this);
     this.taskEventPanel.setTaskId(task.getId());
     return detailComponent;
   }
   
   protected Component getEventComponent()
   {
     return getTaskEventPanel();
   }
   
   public TaskEventsPanel getTaskEventPanel() {
     if (this.taskEventPanel == null) {
       this.taskEventPanel = new TaskEventsPanel();
     }
     return this.taskEventPanel;
   }
   
   public Component getSearchComponent()
   {
     return new TaskListHeader();
   }
   
 
 
   public void refreshSelectNext()
   {
     super.refreshSelectNext();
     
 
     addMenuBar();
   }
   
   protected abstract LazyLoadingQuery createLazyLoadingQuery();
   
   protected abstract UriFragment getUriFragment(String paramString);
 }


