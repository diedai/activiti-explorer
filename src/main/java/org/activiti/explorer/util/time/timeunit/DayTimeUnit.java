 package org.activiti.explorer.util.time.timeunit;
 
 import org.activiti.explorer.util.time.TimeUnit;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DayTimeUnit
   implements TimeUnit
 {
   private static final Long MILLIS_PER_DAY = Long.valueOf(86400000L);
   
   public Long getNumberOfMillis() {
     return MILLIS_PER_DAY;
   }
   
   public String getMessageKey(Long numberOfUnits) {
     if (numberOfUnits.longValue() == 1L) {
       return "time.unit.day";
     }
     return "time.unit.days";
   }
 }


