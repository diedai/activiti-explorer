 package org.activiti.explorer.ui.process;
 
 import org.activiti.explorer.ComponentFactories;
 import org.activiti.explorer.ui.ComponentFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessDefinitionFilterFactory
   implements ComponentFactory<ProcessDefinitionFilter>
 {
   private ProcessDefinitionFilter processDefinitionFilter;
   
   public void initialise(String environment)
   {
     if (this.processDefinitionFilter == null) {
       this.processDefinitionFilter = new DefaultProcessDefinitionFilter();
     }
   }
   
 
 
 
   public void setComponentFactories(ComponentFactories componentFactories)
   {
     componentFactories.add(ProcessDefinitionFilterFactory.class, this);
   }
   
   public ProcessDefinitionFilter create() {
     return this.processDefinitionFilter;
   }
   
   public void setProcessDefinitionFilter(ProcessDefinitionFilter processDefinitionFilter)
   {
     this.processDefinitionFilter = processDefinitionFilter;
   }
 }


