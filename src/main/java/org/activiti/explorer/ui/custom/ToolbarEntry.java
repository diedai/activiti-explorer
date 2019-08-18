 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.CustomComponent;
 import com.vaadin.ui.HorizontalLayout;
 import java.io.Serializable;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ToolbarEntry
   extends CustomComponent
 {
   private static final long serialVersionUID = 1L;
   protected String title;
   protected Long count;
   protected boolean active;
   protected ToolbarCommand command;
   protected String name;
   protected Button titleButton;
   protected Button countButton;
   protected HorizontalLayout layout;
   
   public ToolbarEntry(String key, String title)
   {
     this.name = key;
     this.title = title;
     addStyleName("clickable");
     this.layout = new HorizontalLayout();
     setCompositionRoot(this.layout);
     setSizeUndefined();
     initLabelComponent();
     initCountComponent();
   }
   
 
 
 
   public void setCount(Long count)
   {
     this.count = count;
     if (count != null) {
       this.countButton.setCaption(count + "");
       if (!this.countButton.isVisible()) {
         this.countButton.setVisible(true);
       }
     } else {
       this.countButton.setVisible(true);
     }
   }
   
   public Long getCount() {
     return this.count;
   }
   
   public void setActive(boolean active) {
     if (this.active != active) {
       this.active = active;
       if (active) {
         this.titleButton.addStyleName("active");
         this.countButton.addStyleName("active");
       } else {
         this.titleButton.removeStyleName("active");
         this.countButton.removeStyleName("active");
       }
     }
   }
   
   public void setCommand(ToolbarCommand command) {
     this.command = command;
   }
   
   protected void initLabelComponent() {
     this.titleButton = new Button(this.title);
     this.titleButton.addStyleName("link");
     this.layout.addComponent(this.titleButton);
     this.layout.setComponentAlignment(this.titleButton, Alignment.MIDDLE_LEFT);
     
     this.titleButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) { if (ToolbarEntry.this.command != null) {
           ToolbarEntry.this.command.toolBarItemSelected();
         }
       }
     });
   }
   
   protected void initCountComponent() {
     this.countButton = new Button(this.count + "");
     this.countButton.addStyleName("link");
     this.countButton.addStyleName("toolbar-count");
     
 
     this.countButton.setVisible(false);
     
     this.layout.addComponent(this.countButton);
     this.layout.setComponentAlignment(this.countButton, Alignment.MIDDLE_LEFT);
   }
   
   public static abstract interface ToolbarCommand
     extends Serializable
   {
     public abstract void toolBarItemSelected();
   }
 }


