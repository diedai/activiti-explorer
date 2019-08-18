 package org.activiti.explorer.ui.util;
 
 import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickListener;
 import com.vaadin.terminal.Resource;
 import com.vaadin.terminal.ThemeResource;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.Table.ColumnGenerator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ThemeImageColumnGenerator
   implements Table.ColumnGenerator
 {
   private static final long serialVersionUID = -7742412844347541389L;
   protected Resource image;
   protected MouseEvents.ClickListener clickListener;
   
   public ThemeImageColumnGenerator(String imageName)
   {
     this.image = new ThemeResource(imageName);
   }
   
   public ThemeImageColumnGenerator(Resource image) {
     this.image = image;
   }
   
   public ThemeImageColumnGenerator(Resource image, MouseEvents.ClickListener clickListener) {
     this(image);
     this.clickListener = clickListener;
   }
   
   public Component generateCell(Table source, Object itemId, Object columnId) {
     Embedded embedded = new Embedded(null, this.image);
     
     if (this.clickListener != null) {
       embedded.addStyleName("clickable");
       embedded.setData(itemId);
       embedded.addListener(this.clickListener);
     }
     
     return embedded;
   }
 }


