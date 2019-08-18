 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.ui.Button;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.MenuBar;
 import com.vaadin.ui.MenuBar.Command;
 import com.vaadin.ui.MenuBar.MenuItem;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ToolbarPopupEntry
   extends ToolbarEntry
 {
   private static final long serialVersionUID = 1L;
   protected MenuBar menuBar;
   protected MenuBar.MenuItem rootItem;
   
   public ToolbarPopupEntry(String key, String title)
   {
     super(key, title);
   }
   
 
 
   public MenuBar.MenuItem addMenuItem(String title)
   {
     return this.rootItem.addItem(title, null);
   }
   
 
 
   public MenuBar.MenuItem addMenuItem(String title, final ToolbarEntry.ToolbarCommand command)
   {
     return this.rootItem.addItem(title, new MenuBar.Command() {
       private static final long serialVersionUID = 1L;
       
       public void menuSelected(MenuBar.MenuItem selectedItem) { if (command != null) {
           command.toolBarItemSelected();
         }
       }
     });
   }
   
   public void setActive(boolean active)
   {
     if (this.active != active) {
       this.active = active;
       if (active) {
         this.menuBar.addStyleName("active");
         this.countButton.addStyleName("active");
       } else {
         this.menuBar.removeStyleName("active");
         this.countButton.removeStyleName("active");
       }
     }
   }
   
   protected void initLabelComponent() {
     this.menuBar = new MenuBar();
     this.menuBar.addStyleName("toolbar-popup");
     this.rootItem = this.menuBar.addItem(this.title, null);
     this.layout.addComponent(this.menuBar);
   }
 }


