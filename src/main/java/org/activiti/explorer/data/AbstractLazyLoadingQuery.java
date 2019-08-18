 package org.activiti.explorer.data;
 
 
 
 
 
 
 
 
 
 public abstract class AbstractLazyLoadingQuery
   implements LazyLoadingQuery
 {
   protected LazyLoadingContainer lazyLoadingContainer;
   
 
 
 
 
 
 
 
   public void setLazyLoadingContainer(LazyLoadingContainer lazyLoadingContainer)
   {
     this.lazyLoadingContainer = lazyLoadingContainer;
   }
 }


