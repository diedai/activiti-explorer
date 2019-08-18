 package org.activiti.explorer.ui.content;
 
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.activiti.engine.ActivitiException;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.ui.content.email.EmailAttachmentRenderer;
 import org.activiti.explorer.ui.content.file.FileAttachmentEditor;
 import org.activiti.explorer.ui.content.file.ImageAttachmentRenderer;
 import org.activiti.explorer.ui.content.file.PdfAttachmentRenderer;
 import org.activiti.explorer.ui.content.url.UrlAttachmentEditor;
 import org.activiti.explorer.ui.content.url.UrlAttachmentRenderer;
 import org.springframework.beans.factory.InitializingBean;
 import org.springframework.stereotype.Component;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 @Component
 public class AttachmentRendererManager
   implements InitializingBean, Serializable
 {
   private static final long serialVersionUID = 1L;
   private final List<AttachmentRenderer> renderers = new ArrayList();
   private final List<AttachmentEditor> editors = new ArrayList();
   
   private final Map<String, AttachmentEditor> editorMap = new HashMap();
   private final AttachmentRenderer defaultAttachmentRenderer = new GenericAttachmentRenderer();
   
   public void addAttachmentRenderer(AttachmentRenderer renderer) {
     this.renderers.add(renderer);
   }
   
   public void addAttachmentEditor(AttachmentEditor editor) {
     this.editors.add(editor);
     this.editorMap.put(editor.getName(), editor);
   }
   
   public AttachmentRenderer getRenderer(Attachment attachment) {
     return getRenderer(attachment.getType());
   }
   
   public AttachmentRenderer getRenderer(String type) {
     for (AttachmentRenderer renderer : this.renderers) {
       if (renderer.canRenderAttachment(type)) {
         return renderer;
       }
     }
     return this.defaultAttachmentRenderer;
   }
   
 
 
 
   public List<AttachmentEditor> getAttachmentEditors()
   {
     return Collections.unmodifiableList(this.editors);
   }
   
   public AttachmentEditor getEditor(String type) {
     AttachmentEditor editor = (AttachmentEditor)this.editorMap.get(type);
     if (editor == null) {
       throw new ActivitiException("No editor defined with the given name: " + editor);
     }
     return editor;
   }
   
   public void afterPropertiesSet() throws Exception
   {
     addAttachmentRenderer(new UrlAttachmentRenderer());
     addAttachmentEditor(new UrlAttachmentEditor());
     
 
     addAttachmentEditor(new FileAttachmentEditor());
     
 
     addAttachmentRenderer(new PdfAttachmentRenderer());
     addAttachmentRenderer(new ImageAttachmentRenderer());
     addAttachmentRenderer(new EmailAttachmentRenderer());
   }
 }


