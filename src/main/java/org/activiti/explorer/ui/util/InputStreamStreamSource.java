 package org.activiti.explorer.ui.util;
 
 import com.vaadin.terminal.StreamResource.StreamSource;
 import java.io.InputStream;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class InputStreamStreamSource
   implements StreamSource
 {
   private static final long serialVersionUID = -860417435916179985L;
   protected transient InputStream inputStream;
   
   public InputStreamStreamSource(InputStream inputStream)
   {
     this.inputStream = inputStream;
   }
   
   public InputStream getStream() {
     return this.inputStream;
   }
 }


