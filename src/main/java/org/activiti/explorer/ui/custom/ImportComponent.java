 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.event.dd.acceptcriteria.AcceptAll;
 import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.ProgressIndicator;
 import com.vaadin.ui.Upload;
 import com.vaadin.ui.Upload.FailedEvent;
 import com.vaadin.ui.Upload.FailedListener;
 import com.vaadin.ui.Upload.FinishedEvent;
 import com.vaadin.ui.Upload.FinishedListener;
 import com.vaadin.ui.Upload.ProgressListener;
 import com.vaadin.ui.Upload.Receiver;
 import com.vaadin.ui.Upload.StartedEvent;
 import com.vaadin.ui.Upload.StartedListener;
 import com.vaadin.ui.VerticalLayout;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ImportComponent
   extends VerticalLayout
   implements Upload.StartedListener
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected NotificationManager notificationManager;
   protected ProgressIndicator progressIndicator;
   protected Upload upload;
   protected Upload.Receiver receiver;
   protected List<Upload.FinishedListener> finishedListeners = new ArrayList();
   protected List<Upload.StartedListener> startedListeners = new ArrayList();
   protected boolean showGenericFailureMessage = true;
   protected List<Upload.FailedListener> failedListeners = new ArrayList();
   protected List<Upload.ProgressListener> progressListeners = new ArrayList();
   
   public ImportComponent(String description, Upload.Receiver receiver)
   {
     this.receiver = receiver;
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     
     init(description);
   }
   
 
   protected void init(String description)
   {
     setSpacing(true);
     setSizeFull();
     
     addDescription(description);
     addUpload();
   }
   
   protected void addDescription(String description) {
     if (description != null) {
       Label descriptionLabel = new Label(description);
       descriptionLabel.addStyleName("light");
       descriptionLabel.addStyleName("upload-description");
       addComponent(descriptionLabel);
     }
   }
   
   protected void addUpload() {
     this.upload = new Upload(null, this.receiver);
     this.upload.setButtonCaption(this.i18nManager.getMessage("upload.select"));
     this.upload.setImmediate(true);
     addComponent(this.upload);
     setComponentAlignment(this.upload, Alignment.MIDDLE_CENTER);
     
 
     this.upload.addListener(this);
     this.upload.addListener(this);
     this.upload.addListener(this);
     this.upload.addListener(this);
   }
   
 
   public void uploadStarted(Upload.StartedEvent event)
   {
     removeAllComponents();
     
     this.progressIndicator = new ProgressIndicator();
     this.progressIndicator.setPollingInterval(500);
     addComponent(this.progressIndicator);
     setComponentAlignment(this.progressIndicator, Alignment.MIDDLE_CENTER);
     
     for (Upload.StartedListener startedListener : this.startedListeners) {
       startedListener.uploadStarted(event);
     }
   }
   
   public void updateProgress(long readBytes, long contentLength) {
     this.progressIndicator.setValue(new Float((float)readBytes / (float)contentLength));
     
     for (Upload.ProgressListener progressListener : this.progressListeners) {
       progressListener.updateProgress(readBytes, contentLength);
     }
   }
   
   public void uploadFinished(Upload.FinishedEvent event)
   {
     this.progressIndicator.setVisible(false);
     for (Upload.FinishedListener finishedListener : this.finishedListeners) {
       finishedListener.uploadFinished(event);
     }
   }
   
   public void uploadFailed(Upload.FailedEvent event) {
     for (Upload.FailedListener failedListener : this.failedListeners) {
       failedListener.uploadFailed(event);
     }
   }
   
   public AcceptCriterion getAcceptCriterion() {
     return AcceptAll.get();
   }
   
 
   public void addFinishedListener(Upload.FinishedListener finishedListener)
   {
     this.finishedListeners.add(finishedListener);
   }
   
   public void addStartedListener(Upload.StartedListener startedListener) {
     this.startedListeners.add(startedListener);
   }
   
   public void addFailedListener(Upload.FailedListener failedListener) {
     this.failedListeners.add(failedListener);
   }
   
   public void addProgressListener(Upload.ProgressListener progressListener) {
     this.progressListeners.add(progressListener);
   }
 }


