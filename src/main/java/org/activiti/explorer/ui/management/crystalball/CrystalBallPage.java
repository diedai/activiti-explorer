 package org.activiti.explorer.ui.management.crystalball;
 
 import com.vaadin.ui.AbstractSelect;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.AbstractOneViewPage;
 import org.activiti.explorer.ui.ComponentFactory;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.management.ManagementMenuBarFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class CrystalBallPage
   extends AbstractOneViewPage
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected String managementId;
   
   public CrystalBallPage()
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
   }
   
   protected void initUi()
   {
     super.initUi();
     setDetailComponent(new EventOverviewPanel());
   }
   
   protected ToolBar createMenuBar()
   {
     return (ToolBar)ExplorerApp.get().getComponentFactory(ManagementMenuBarFactory.class).create();
   }
   
   protected AbstractSelect createSelectComponent()
   {
     return null;
   }
   
   public void refreshSelectNext() {}
   
   public void selectElement(int index) {}
 }


