package org.activiti.explorer.ui.content;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Component;
import org.activiti.engine.task.Attachment;

public abstract interface AttachmentEditorComponent
  extends Component
{
  public abstract Attachment getAttachment()
    throws InvalidValueException;
}


