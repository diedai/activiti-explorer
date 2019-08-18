package org.activiti.explorer.data;

import com.vaadin.data.Item;
import java.io.Serializable;
import java.util.List;

public abstract interface LazyLoadingQuery
  extends Serializable
{
  public abstract int size();
  
  public abstract List<Item> loadItems(int paramInt1, int paramInt2);
  
  public abstract Item loadSingleResult(String paramString);
  
  public abstract void setLazyLoadingContainer(LazyLoadingContainer paramLazyLoadingContainer);
  
  public abstract void setSorting(Object[] paramArrayOfObject, boolean[] paramArrayOfBoolean);
}


