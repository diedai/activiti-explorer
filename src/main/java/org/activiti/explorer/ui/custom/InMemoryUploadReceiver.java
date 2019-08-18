 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.ui.Upload;
 import com.vaadin.ui.Upload.ProgressListener;
 import com.vaadin.ui.Upload.Receiver;
 import com.vaadin.ui.Upload.StartedEvent;
 import com.vaadin.ui.Upload.StartedListener;
 import java.io.ByteArrayOutputStream;
 import java.io.OutputStream;
 import java.util.Collection;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.util.StringUtil;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class InMemoryUploadReceiver
   implements Upload.Receiver, Upload.StartedListener
 {
   private static final long serialVersionUID = 1L;
   protected NotificationManager notificationManager;
   protected Upload upload;
   protected boolean interrupted;
   protected String fileName;
   protected String mimeType;
   protected long maxFileSize;
   protected ByteArrayOutputStream outputStream;
   protected Collection<String> acceptedMimeTypes;
   
   public InMemoryUploadReceiver(Upload upload, long maxFileSize)
   {
     this.upload = upload;
     this.maxFileSize = maxFileSize;
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     
     upload.setReceiver(this);
     upload.addListener(this);
     upload.addListener(this);
   }
   
   public OutputStream receiveUpload(String filename, String mimeType) {
     this.fileName = filename;
     this.mimeType = mimeType;
     this.outputStream = new ByteArrayOutputStream();
     return this.outputStream;
   }
   
   public void uploadStarted(Upload.StartedEvent event) {
     checkFileSize(event.getContentLength());
     if (!this.interrupted) {
       checkMimeType(event.getMIMEType());
     }
   }
   
   public void updateProgress(long readBytes, long contentLength) {
     if (contentLength == -1L) {
       checkFileSize(readBytes);
     } else {
       checkFileSize(contentLength);
     }
   }
   
   public byte[] getBytes() {
     return this.outputStream.toByteArray();
   }
   
   public String getFileName() {
     return this.fileName;
   }
   
   public String getMimeType() {
     return this.mimeType;
   }
   
   public boolean isInterruped() {
     return this.interrupted;
   }
   
   protected void checkFileSize(long receivedLength) {
     if (receivedLength > this.maxFileSize) {
       interrupt();
       this.notificationManager.showWarningNotification("upload.failed", "upload.limit", new Object[] { this.maxFileSize / 1024L + "kb" });
     }
   }
   
   protected void checkMimeType(String mimeType)
   {
     if ((this.acceptedMimeTypes != null) && (!this.acceptedMimeTypes.contains(mimeType))) {
       interrupt();
       this.notificationManager.showWarningNotification("upload.failed", "upload.invalid.mimetype", new Object[] {
       
         StringUtil.toReadableString(this.acceptedMimeTypes) });
     }
   }
   
   protected void interrupt() {
     this.upload.interruptUpload();
     this.interrupted = true;
   }
   
 
 
 
   public void setAcceptedMimeTypes(Collection<String> acceptedMimeTypes)
   {
     this.acceptedMimeTypes = acceptedMimeTypes;
   }
   
   public void reset() {
     this.interrupted = false;
     this.outputStream = null;
     this.fileName = null;
     this.mimeType = null;
   }
 }


