 package org.activiti.explorer.ui;
 
 import com.vaadin.ui.Component;
 import com.vaadin.ui.GridLayout;
 import org.activiti.explorer.ui.custom.ToolBar;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class AbstractOneViewPage
   extends AbstractPage
 {
   private static final long serialVersionUID = 1L;
   
   protected void initUi()
   {
     addMainLayout();
     setSizeFull();
     addMenuBar();
   }
   
 
 
 
 
   protected void addMenuBar()
   {
     String activeEntry = null;
     if (this.toolBar != null) {
       activeEntry = this.toolBar.getCurrentEntryKey();
       this.grid.removeComponent(this.toolBar);
     }
     
 
     ToolBar menuBar = createMenuBar();
     if (menuBar != null) {
       this.toolBar = createMenuBar();
       this.grid.addComponent(this.toolBar, 0, 0);
       
       if (activeEntry != null) {
         this.toolBar.setActiveEntry(activeEntry);
       }
     }
   }
   
   protected void addMainLayout() {
     this.grid = new GridLayout(1, 2);
     this.grid.setSizeFull();
     
 
     this.grid.setRowExpandRatio(1, 1.0F);
     
     setCompositionRoot(this.grid);
   }
   
   protected void setDetailComponent(Component detail) {
     if (this.grid.getComponent(0, 1) != null) {
       this.grid.removeComponent(0, 1);
     }
     if (detail != null) {
       this.grid.addComponent(detail, 0, 1);
     }
   }
   
   protected Component getDetailComponent() {
     return this.grid.getComponent(0, 1);
   }
 }


