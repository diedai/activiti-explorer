 package org.activiti.explorer.ui.reports;
 
 import com.vaadin.data.Property;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import java.text.DateFormat;
 import java.util.Date;
 import org.activiti.engine.ActivitiIllegalArgumentException;
 import org.activiti.engine.history.HistoricProcessInstance;
 import org.activiti.engine.impl.identity.Authentication;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SavedReportListItem
   extends PropertysetItem
   implements Comparable<SavedReportListItem>
 {
   private static final long serialVersionUID = 1L;
   
   public SavedReportListItem(HistoricProcessInstance historicProcessInstance)
   {
     addItemProperty("id", new ObjectProperty(historicProcessInstance.getId(), String.class));
     addItemProperty("name", getNameProperty(historicProcessInstance));
     
     if (historicProcessInstance.getEndTime() == null) {
       throw new ActivitiIllegalArgumentException("The given process-instance is not ended yet");
     }
     addItemProperty("createTime", new ObjectProperty(historicProcessInstance.getEndTime(), Date.class));
   }
   
   public int compareTo(SavedReportListItem other) {
     Date createTime = (Date)getItemProperty("createTime").getValue();
     Date otherCreateTime = (Date)other.getItemProperty("createTime").getValue();
     
     return createTime.compareTo(otherCreateTime);
   }
   
   protected Property getNameProperty(HistoricProcessInstance historicProcessInstance) {
     return new ObjectProperty(getReportDisplayName(historicProcessInstance), String.class);
   }
   
   public static String getReportDisplayName(HistoricProcessInstance historicProcessInstance) {
     if ((historicProcessInstance.getBusinessKey() != null) && (!historicProcessInstance.getBusinessKey().isEmpty())) {
       if (Authentication.getAuthenticatedUserId() != null) {
         return historicProcessInstance.getBusinessKey().replaceFirst(Authentication.getAuthenticatedUserId() + "\\_", "");
       }
       return historicProcessInstance.getBusinessKey();
     }
     
     return DateFormat.getDateTimeInstance().format(historicProcessInstance.getEndTime());
   }
 }


