package org.activiti.explorer.ui.process.simple.editor.listener;

import java.io.Serializable;

public abstract interface TaskFormModelListener
  extends Serializable
{
  public abstract void formAdded(Object paramObject);
  
  public abstract void formRemoved(Object paramObject);
}


