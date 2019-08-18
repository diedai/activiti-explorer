package org.activiti.explorer.ui.content;

import org.activiti.engine.task.Attachment;

public abstract interface RelatedContentComponent
{
  public abstract void showAttachmentDetail(Attachment paramAttachment);
}


