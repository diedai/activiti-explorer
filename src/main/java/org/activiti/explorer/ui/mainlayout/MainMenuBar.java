 package org.activiti.explorer.ui.mainlayout;
 
 import com.vaadin.terminal.Resource;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.MenuBar;
 import com.vaadin.ui.MenuBar.Command;
 import com.vaadin.ui.MenuBar.MenuItem;
 import java.util.HashMap;
 import java.util.Map;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.profile.ChangePasswordPopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MainMenuBar
   extends HorizontalLayout
 {
   private static final long serialVersionUID = 1L;
   protected ViewManager viewManager;
   protected I18nManager i18nManager;
   protected Map<String, Button> menuItemButtons;
   protected String currentMainNavigation;
   
   public MainMenuBar()
   {
     this.viewManager = ExplorerApp.get().getViewManager();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     this.menuItemButtons = new HashMap();
     init();
   }
   
 
 
   public synchronized void setMainNavigation(String navigation)
   {
     if (this.currentMainNavigation != null) {
       ((Button)this.menuItemButtons.get(this.currentMainNavigation)).removeStyleName("active");
     }
     this.currentMainNavigation = navigation;
     
     Button current = (Button)this.menuItemButtons.get(navigation);
     if (current != null) {
       current.addStyleName("active");
     }
   }
   
   protected void init() {
     setHeight(54.0F, 0);
     setWidth(100.0F, 8);
     
     setMargin(false, true, false, false);
     
     initTitle();
     initButtons();
     initProfileButton();
   }
   
   protected void initButtons()
   {
     Button taskButton = addMenuButton("task", this.i18nManager.getMessage("main.menu.tasks"), Images.MAIN_MENU_TASKS, false, 80.0F);
     taskButton.addListener(new ShowTasksClickListener());
     this.menuItemButtons.put("task", taskButton);
     
     Button processButton = addMenuButton("process", this.i18nManager.getMessage("main.menu.process"), Images.MAIN_MENU_PROCESS, false, 80.0F);
     processButton.addListener(new ShowProcessDefinitionsClickListener());
     this.menuItemButtons.put("process", processButton);
     
     Button reportingButton = addMenuButton("report", this.i18nManager.getMessage("main.menu.reports"), Images.MAIN_MENU_REPORTS, false, 80.0F);
     reportingButton.addListener(new ShowReportsClickListener());
     this.menuItemButtons.put("report", reportingButton);
     
     if (ExplorerApp.get().getLoggedInUser().isAdmin()) {
       Button manageButton = addMenuButton("manage", this.i18nManager.getMessage("main.menu.management"), Images.MAIN_MENU_MANAGE, false, 90.0F);
       manageButton.addListener(new ShowManagementClickListener());
       this.menuItemButtons.put("manage", manageButton);
     }
   }
   
   protected void initTitle() {
     Label title = new Label();
     title.addStyleName("h1");
     
     if (ExplorerApp.get().getEnvironment().equals("alfresco")) {
       title.addStyleName("workflow-console-logo");
     } else {
       title.addStyleName("logo");
     }
     
     addComponent(title);
     
     setExpandRatio(title, 1.0F);
   }
   
   protected Button addMenuButton(String type, String label, Resource icon, boolean active, float width) {
     Button button = new Button(label);
     button.addStyleName(type);
     button.addStyleName("main-menu-button");
     button.addStyleName("link");
     button.setHeight(54.0F, 0);
     button.setIcon(icon);
     button.setWidth(width, 0);
     
     addComponent(button);
     setComponentAlignment(button, Alignment.TOP_CENTER);
     
     return button;
   }
   
   protected void initProfileButton() {
     final LoggedInUser user = ExplorerApp.get().getLoggedInUser();
     
 
     MenuBar profileMenu = new MenuBar();
     profileMenu.addStyleName("person");
     MenuBar.MenuItem rootItem = profileMenu.addItem(user.getFirstName() + " " + user.getLastName(), null);
     rootItem.setStyleName("person-menu");
     
     if (useProfile())
     {
       rootItem.addItem(this.i18nManager.getMessage("profile.show"), new MenuBar.Command() {
         public void menuSelected(MenuBar.MenuItem selectedItem) {
           ExplorerApp.get().getViewManager().showProfilePopup(user.getId());
         }
         
 
       });
       rootItem.addItem(this.i18nManager.getMessage("profile.edit"), new MenuBar.Command()
       {
         public void menuSelected(MenuBar.MenuItem selectedItem)
         {
           ExplorerApp.get().getViewManager().showProfilePopup(user.getId());
         }
         
 
       });
       rootItem.addItem(this.i18nManager.getMessage("password.change"), new MenuBar.Command() {
         public void menuSelected(MenuBar.MenuItem selectedItem) {
           ExplorerApp.get().getViewManager().showPopupWindow(new ChangePasswordPopupWindow());
         }
         
       });
       rootItem.addSeparator();
     }
     
 
     rootItem.addItem(this.i18nManager.getMessage("header.logout"), new MenuBar.Command() {
       public void menuSelected(MenuBar.MenuItem selectedItem) {
         ExplorerApp.get().close();
       }
       
     });
     addComponent(profileMenu);
     setComponentAlignment(profileMenu, Alignment.TOP_RIGHT);
     setExpandRatio(profileMenu, 1.0F);
   }
   
   protected boolean useProfile() {
     return true;
   }
   
   private class ShowTasksClickListener implements ClickListener {
     private ShowTasksClickListener() {}
     
     public void buttonClick(ClickEvent event) { ExplorerApp.get().getViewManager().showInboxPage(); }
   }
   
   private class ShowProcessDefinitionsClickListener implements ClickListener {
     private ShowProcessDefinitionsClickListener() {}
     
     public void buttonClick(ClickEvent event) { ExplorerApp.get().getViewManager().showDeployedProcessDefinitionPage(); }
   }
   
   private class ShowReportsClickListener implements ClickListener {
     private ShowReportsClickListener() {}
     
     public void buttonClick(ClickEvent event) { ExplorerApp.get().getViewManager().showRunReportPage(); }
   }
   
   private class ShowManagementClickListener implements ClickListener {
     private ShowManagementClickListener() {}
     
     public void buttonClick(ClickEvent event) { ExplorerApp.get().getViewManager().showDatabasePage(); }
   }
 }


