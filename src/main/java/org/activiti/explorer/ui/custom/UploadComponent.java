 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.event.dd.DragAndDropEvent;
 import com.vaadin.event.dd.DropHandler;
 import com.vaadin.event.dd.acceptcriteria.AcceptAll;
 import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
 import com.vaadin.terminal.StreamVariable;
 import com.vaadin.terminal.StreamVariable.StreamingEndEvent;
 import com.vaadin.terminal.StreamVariable.StreamingErrorEvent;
 import com.vaadin.terminal.StreamVariable.StreamingProgressEvent;
 import com.vaadin.terminal.StreamVariable.StreamingStartEvent;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.DragAndDropWrapper;
 import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
 import com.vaadin.ui.Html5File;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Panel;
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
 import java.io.OutputStream;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UploadComponent
   extends VerticalLayout
   implements Upload.StartedListener, DropHandler
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected NotificationManager notificationManager;
   protected ProgressIndicator progressIndicator;
   protected Upload upload;
   protected Upload.Receiver receiver;
   protected Label descriptionLabel;
   protected boolean enableDrop = true;
   
 
   protected List<Upload.FinishedListener> finishedListeners = new ArrayList();
   protected List<Upload.StartedListener> startedListeners = new ArrayList();
   protected boolean showGenericFailureMessage = true;
   protected List<Upload.FailedListener> failedListeners = new ArrayList();
   protected List<Upload.ProgressListener> progressListeners = new ArrayList();
   
 
   public UploadComponent(String description, Upload.Receiver receiver)
   {
     this.receiver = receiver;
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     
     init();
   }
   
   public UploadComponent(boolean enableDrop) {
     this(null, null);
     enableDrop = true;
   }
   
   public void setDescription(String description) {
     if (description != null) {
       this.descriptionLabel.setValue(description);
       this.descriptionLabel.setVisible(true);
     } else {
       this.descriptionLabel.setVisible(false);
     }
   }
   
   public void setReceiver(Upload.Receiver receiver) {
     this.receiver = receiver;
     this.upload.setReceiver(receiver);
   }
   
 
   protected void init()
   {
     setSpacing(true);
     setSizeFull();
     
     addDescription();
     addUpload();
     
     if (this.enableDrop) {
       addOrLabel();
       addDropPanel();
     }
   }
   
   protected void addDescription() {
     this.descriptionLabel = new Label();
     this.descriptionLabel.addStyleName("light");
     this.descriptionLabel.addStyleName("upload-description");
     this.descriptionLabel.setVisible(false);
     addComponent(this.descriptionLabel);
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
   
   protected void addOrLabel() {
     Label orLabel = new Label("or");
     orLabel.setSizeUndefined();
     orLabel.addStyleName("light");
     addComponent(orLabel);
     setComponentAlignment(orLabel, Alignment.MIDDLE_CENTER);
   }
   
   protected void addDropPanel() {
     Panel dropPanel = new Panel();
     DragAndDropWrapper dragAndDropWrapper = new DragAndDropWrapper(dropPanel);
     dragAndDropWrapper.setDropHandler(this);
     dragAndDropWrapper.setWidth("80%");
     addComponent(dragAndDropWrapper);
     setComponentAlignment(dragAndDropWrapper, Alignment.MIDDLE_CENTER);
     
     Label dropLabel = new Label(this.i18nManager.getMessage("upload.drop"));
     dropLabel.setSizeUndefined();
     dropPanel.addComponent(dropLabel);
     ((VerticalLayout)dropPanel.getContent()).setComponentAlignment(dropLabel, Alignment.MIDDLE_CENTER);
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
   
 
   public void drop(DragAndDropEvent event)
   {
     DragAndDropWrapper.WrapperTransferable transferable = (DragAndDropWrapper.WrapperTransferable)event.getTransferable();
     Html5File[] files = transferable.getFiles();
     if ((files != null) && (files.length > 0)) {
       final Html5File file = files[0];
       file.setStreamVariable(new StreamVariable()
       {
         private static final long serialVersionUID = 1L;
         
         public void streamingStarted(StreamVariable.StreamingStartEvent event) {
           UploadComponent.this.uploadStarted(null);
         }
         
         public void streamingFinished(StreamVariable.StreamingEndEvent event) { UploadComponent.this.uploadFinished(null); }
         
         public void streamingFailed(StreamVariable.StreamingErrorEvent event) {
           UploadComponent.this.uploadFailed(null);
         }
         
         public void onProgress(StreamVariable.StreamingProgressEvent event) { UploadComponent.this.updateProgress(event.getBytesReceived(), event.getContentLength()); }
         
         public boolean listenProgress() {
           return true;
         }
         
         public boolean isInterrupted() { return false; }
         
         public OutputStream getOutputStream() {
           return UploadComponent.this.receiver.receiveUpload(file.getFileName(), file.getType());
         }
       });
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


