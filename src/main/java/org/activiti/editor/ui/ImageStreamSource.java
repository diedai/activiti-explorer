 package org.activiti.editor.ui;
 
 import com.vaadin.Application;
 import com.vaadin.terminal.StreamResource;
 import com.vaadin.terminal.StreamResource.StreamSource;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ImageStreamSource
   extends StreamResource
 {
   private static final long serialVersionUID = 1L;
   
   public ImageStreamSource(StreamResource.StreamSource streamSource, Application application)
   {
     super(streamSource, null, application);
     SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
     String filename = "myfilename-" + df.format(new Date()) + ".png";
     setFilename(filename);
     setCacheTime(0L);
   }
 }


