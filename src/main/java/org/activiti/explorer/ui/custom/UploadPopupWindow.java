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
 import org.activiti.explorer.ui.ComponentFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UploadPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected UploadComponent uploadComponent;
   
   public UploadPopupWindow(String caption, String description, Upload.Receiver receiver)
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     init(caption, description, receiver);
     
     this.uploadComponent.addFinishedListener(new Upload.FinishedListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void uploadFinished(Upload.FinishedEvent event) {
         UploadPopupWindow.this.close();
       }
     });
   }
   
   protected void init(String caption, String description, Upload.Receiver receiver)
   {
     this.uploadComponent = ((UploadComponent)ExplorerApp.get().getComponentFactory(UploadComponentFactory.class).create());
     this.uploadComponent.setReceiver(receiver);
     this.uploadComponent.setDescription(description);
     this.uploadComponent.setSizeFull();
     initWindow(caption);
   }
   
   protected void initWindow(String caption)
   {
     setWidth("300px");
     setHeight("300px");
     addStyleName("light");
     setModal(true);
     center();
     setCaption(caption);
     
     setContent(this.uploadComponent);
   }
   
 
   public void addFinishedListener(Upload.FinishedListener finishedListener)
   {
     this.uploadComponent.addFinishedListener(finishedListener);
   }
   
   public void addStartedListener(Upload.StartedListener startedListener) {
     this.uploadComponent.addStartedListener(startedListener);
   }
   
   public void addFailedListener(Upload.FailedListener failedListener) {
     this.uploadComponent.addFailedListener(failedListener);
   }
   
   public void addProgressListener(Upload.ProgressListener progressListener) {
     this.uploadComponent.addProgressListener(progressListener);
   }
 }


