 package org.activiti.explorer.ui;
 
 import com.vaadin.ui.AbstractSelect;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.CustomComponent;
 import com.vaadin.ui.GridLayout;
 import org.activiti.explorer.ui.custom.ToolBar;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class AbstractPage
   extends CustomComponent
 {
   private static final long serialVersionUID = 1L;
   protected ToolBar toolBar;
   protected GridLayout grid;
   protected AbstractSelect select;
   protected boolean showEvents;
   
   public void attach()
   {
     initUi();
   }
   
 
 
   protected void initUi()
   {
     this.showEvents = (getEventComponent() != null);
     
     addMainLayout();
     setSizeFull();
     addMenuBar();
     addSearch();
     addSelectComponent();
     if (this.showEvents) {
       addEventComponent();
     }
   }
   
   protected void addEventComponent() {
     this.grid.addComponent(getEventComponent(), 2, 0, 2, 2);
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
       this.grid.addComponent(this.toolBar, 0, 0, 1, 0);
       
       if (activeEntry != null) {
         this.toolBar.setActiveEntry(activeEntry);
       }
     }
   }
   
   public ToolBar getToolBar() {
     return this.toolBar;
   }
   
   protected abstract ToolBar createMenuBar();
   
   protected void addMainLayout() {
     if (this.showEvents) {
       this.grid = new GridLayout(3, 3);
       this.grid.setColumnExpandRatio(0, 0.25F);
       this.grid.setColumnExpandRatio(1, 0.52F);
       this.grid.setColumnExpandRatio(2, 0.23F);
     } else {
       this.grid = new GridLayout(2, 3);
       
       this.grid.setColumnExpandRatio(0, 0.25F);
       this.grid.setColumnExpandRatio(1, 0.75F);
     }
     
     this.grid.addStyleName("small");
     this.grid.setSizeFull();
     
 
     this.grid.setRowExpandRatio(2, 1.0F);
     
     setCompositionRoot(this.grid);
   }
   
   protected void addSearch() {
     Component searchComponent = getSearchComponent();
     if (searchComponent != null) {
       this.grid.addComponent(searchComponent, 0, 1);
     }
   }
   
   protected void addSelectComponent() {
     AbstractSelect select = createSelectComponent();
     if (select != null) {
       this.grid.addComponent(select, 0, 2);
     }
   }
   
 
 
 
   protected abstract AbstractSelect createSelectComponent();
   
 
 
 
   public abstract void refreshSelectNext();
   
 
 
 
   public abstract void selectElement(int paramInt);
   
 
 
   protected void setDetailComponent(Component detail)
   {
     if (this.grid.getComponent(1, 1) != null) {
       this.grid.removeComponent(1, 1);
     }
     if (detail != null) {
       this.grid.addComponent(detail, 1, 1, 1, 2);
     }
   }
   
   protected Component getDetailComponent() {
     return this.grid.getComponent(1, 0);
   }
   
 
 
 
   public Component getSearchComponent()
   {
     return null;
   }
   
 
 
 
 
 
 
 
 
   protected Component getEventComponent()
   {
     return null;
   }
 }


