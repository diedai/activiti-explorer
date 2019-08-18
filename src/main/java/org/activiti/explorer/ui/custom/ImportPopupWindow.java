 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedListener;
 import com.vaadin.ui.Upload.FinishedEvent;
 import com.vaadin.ui.Upload.FinishedListener;
 import com.vaadin.ui.Upload.ProgressListener;
 import com.vaadin.ui.Upload.Receiver;
 import com.vaadin.ui.Upload.StartedListener;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ImportPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected ImportComponent importComponent;
   
   public ImportPopupWindow(String caption, String description, Upload.Receiver receiver)
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     init(caption, description, receiver);
     
     this.importComponent.addFinishedListener(new Upload.FinishedListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void uploadFinished(Upload.FinishedEvent event) {
         ImportPopupWindow.this.close();
       }
     });
   }
   
   protected void init(String caption, String description, Upload.Receiver receiver)
   {
     this.importComponent = new ImportComponent(description, receiver);
     this.importComponent.setSizeFull();
     initWindow(caption);
   }
   
   protected void initWindow(String caption)
   {
     setWidth("300px");
     setHeight("200px");
     addStyleName("light");
     setModal(true);
     center();
     setCaption(caption);
     
     setContent(this.importComponent);
   }
   
 
   public void addFinishedListener(Upload.FinishedListener finishedListener)
   {
     this.importComponent.addFinishedListener(finishedListener);
   }
   
   public void addStartedListener(Upload.StartedListener startedListener) {
     this.importComponent.addStartedListener(startedListener);
   }
   
   public void addFailedListener(Upload.FailedListener failedListener) {
     this.importComponent.addFailedListener(failedListener);
   }
   
   public void addProgressListener(Upload.ProgressListener progressListener) {
     this.importComponent.addProgressListener(progressListener);
   }
 }


