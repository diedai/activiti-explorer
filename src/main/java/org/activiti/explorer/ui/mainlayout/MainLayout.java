 package org.activiti.explorer.ui.mainlayout;
 
 import com.vaadin.ui.Component;
 import com.vaadin.ui.CssLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.ComponentFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MainLayout
   extends VerticalLayout
 {
   private static final long serialVersionUID = 1L;
   protected ViewManager viewManager;
   protected I18nManager i18nManager;
   protected MainMenuBar mainMenuBar;
   protected CssLayout header;
   protected CssLayout main;
   protected CssLayout footer;
   
   public MainLayout()
   {
     this.viewManager = ExplorerApp.get().getViewManager();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     setSizeFull();
     addStyleName("main");
     
     initHeader();
     initMainMenuBar();
     initMain();
     initFooter();
   }
   
   public void setMainContent(Component mainContent) {
     this.main.removeAllComponents();
     this.main.addComponent(mainContent);
   }
   
   public void setFooter(Component footerContent) {
     this.footer.removeAllComponents();
     this.footer.addComponent(footerContent);
   }
   
   public void setMainNavigation(String navigation) {
     this.mainMenuBar.setMainNavigation(navigation);
   }
   
   protected void initHeader() {
     this.header = new CssLayout();
     this.header.addStyleName("header");
     this.header.setWidth(100.0F, 8);
     addComponent(this.header);
   }
   
   protected void initMain() {
     this.main = new CssLayout();
     this.main.setSizeFull();
     this.main.addStyleName("main-content");
     addComponent(this.main);
     setExpandRatio(this.main, 1.0F);
   }
   
   protected void initFooter() {
     this.footer = new CssLayout();
     this.footer.setWidth(100.0F, 8);
     this.footer.addStyleName("footer");
     addComponent(this.footer);
     
     Label footerLabel = new Label();
     footerLabel.setContentMode(3);
     footerLabel.setValue(this.i18nManager.getMessage("footer.message"));
     footerLabel.setWidth(100.0F, 8);
     this.footer.addComponent(footerLabel);
   }
   
   protected void initMainMenuBar() {
     this.mainMenuBar = ((MainMenuBar)ExplorerApp.get().getComponentFactory(MainMenuBarFactory.class).create());
     this.header.addComponent(this.mainMenuBar);
   }
 }


