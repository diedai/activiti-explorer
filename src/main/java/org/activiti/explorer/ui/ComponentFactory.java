package org.activiti.explorer.ui;

import java.io.Serializable;

public abstract interface ComponentFactory<T>
  extends Serializable
{
  public abstract void initialise(String paramString);
  
  public abstract T create();
}


