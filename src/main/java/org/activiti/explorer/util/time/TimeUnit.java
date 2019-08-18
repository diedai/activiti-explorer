package org.activiti.explorer.util.time;

public abstract interface TimeUnit
{
  public abstract String getMessageKey(Long paramLong);
  
  public abstract Long getNumberOfMillis();
}


