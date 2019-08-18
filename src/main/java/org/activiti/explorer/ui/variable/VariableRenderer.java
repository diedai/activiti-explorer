package org.activiti.explorer.ui.variable;

public abstract interface VariableRenderer
{
  public abstract Class<?> getVariableType();
  
  public abstract String getStringRepresentation(Object paramObject);
}


