 package org.activiti.explorer.ui.custom;
 
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
   private Resource image;
   
   public ThemeImageColumnGenerator(String imageName)
   {
     this.image = new ThemeResource(imageName);
   }
   
   public ThemeImageColumnGenerator(Resource image) {
     this.image = image;
   }
   
   public Component generateCell(Table source, Object itemId, Object columnId) {
     return new Embedded(null, this.image);
   }
 }


