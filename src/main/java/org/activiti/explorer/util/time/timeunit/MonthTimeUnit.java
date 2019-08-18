 package org.activiti.explorer.util.time.timeunit;
 
 import org.activiti.explorer.util.time.TimeUnit;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MonthTimeUnit
   implements TimeUnit
 {
   private static final Long MILLIS_PER_MONTH = Long.valueOf(2592000000L);
   
   public Long getNumberOfMillis() {
     return MILLIS_PER_MONTH;
   }
   
   public String getMessageKey(Long numberOfUnits) {
     if (numberOfUnits.longValue() == 1L) {
       return "time.unit.month";
     }
     return "time.unit.months";
   }
 }


