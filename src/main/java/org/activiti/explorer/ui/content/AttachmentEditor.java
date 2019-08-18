package org.activiti.explorer.ui.content;

import com.vaadin.terminal.Resource;
import java.io.Serializable;
import org.activiti.engine.task.Attachment;
import org.activiti.explorer.I18nManager;

public abstract interface AttachmentEditor
  extends Serializable
{
  public abstract String getName();
  
  public abstract String getTitle(I18nManager paramI18nManager);
  
  public abstract Resource getImage();
  
  public abstract AttachmentEditorComponent getEditor(Attachment paramAttachment, String paramString1, String paramString2);
}


