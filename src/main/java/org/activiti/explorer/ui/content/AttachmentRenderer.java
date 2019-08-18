package org.activiti.explorer.ui.content;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import java.io.Serializable;
import org.activiti.engine.task.Attachment;
import org.activiti.explorer.I18nManager;

public abstract interface AttachmentRenderer
  extends Serializable
{
  public abstract boolean canRenderAttachment(String paramString);
  
  public abstract String getName(I18nManager paramI18nManager);
  
  public abstract Resource getImage(Attachment paramAttachment);
  
  public abstract Component getOverviewComponent(Attachment paramAttachment, RelatedContentComponent paramRelatedContentComponent);
  
  public abstract Component getDetailComponent(Attachment paramAttachment);
}


