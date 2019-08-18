 package org.activiti.explorer.ui.management.job;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.List;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.impl.persistence.entity.MessageEntity;
 import org.activiti.engine.impl.persistence.entity.TimerEntity;
 import org.activiti.engine.runtime.Job;
 import org.activiti.engine.runtime.JobQuery;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class JobListQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient ManagementService managementService;
   
   public JobListQuery()
   {
     this.managementService = ProcessEngines.getDefaultProcessEngine().getManagementService();
   }
   
   public int size() {
     return (int)this.managementService.createJobQuery().count();
   }
   
 
 
   public List<Item> loadItems(int start, int count)
   {
     List<Job> jobs = ((JobQuery)((JobQuery)this.managementService.createJobQuery().orderByJobDuedate().asc()).orderByJobId().asc()).list();
     
     List<Item> items = new ArrayList();
     for (Job job : jobs) {
       items.add(new JobListItem(job));
     }
     return items;
   }
   
   public Item loadSingleResult(String id) {
     Job job = (Job)this.managementService.createJobQuery().jobId(id).singleResult();
     if (job != null) {
       return new JobListItem(job);
     }
     return null;
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   class JobListItem extends PropertysetItem implements Comparable<JobListItem>
   {
     private static final long serialVersionUID = 1L;
     
     public JobListItem(Job job)
     {
       addItemProperty("id", new ObjectProperty(job.getId(), String.class));
       addItemProperty("dueDate", new ObjectProperty(job.getDuedate(), Date.class));
       addItemProperty("name", new ObjectProperty(getName(job), String.class));
     }
     
     private String getName(Job theJob) {
       if ((theJob instanceof TimerEntity))
         return "Timer job " + theJob.getId();
       if ((theJob instanceof MessageEntity)) {
         return "Message job " + theJob.getId();
       }
       return "Job " + theJob.getId();
     }
     
     public int compareTo(JobListItem other)
     {
       Date dueDate = (Date)getItemProperty("dueDate").getValue();
       Date otherDueDate = (Date)other.getItemProperty("dueDate").getValue();
       
       int comparison = compareObjects(dueDate, otherDueDate);
       if (comparison != 0) {
         return comparison;
       }
       String id = (String)getItemProperty("id").getValue();
       String otherId = (String)other.getItemProperty("id").getValue();
       return id.compareTo(otherId);
     }
     
 
     private <T> int compareObjects(Comparable<T> object, Comparable<T> other)
     {
       if (object != null) {
         if (other != null) {
           return object.compareTo((T) other);
         }
         return 1;
       }
       
       if (other == null) {
         return 0;
       }
       
       return -1;
     }
   }
 }


