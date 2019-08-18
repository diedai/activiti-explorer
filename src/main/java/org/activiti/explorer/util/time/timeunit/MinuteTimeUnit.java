 package org.activiti.explorer.util.time.timeunit;
 
 import org.activiti.explorer.util.time.TimeUnit;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MinuteTimeUnit
   implements TimeUnit
 {
   private static final Long MILLIS_PER_MINUTE = Long.valueOf(60000L);
   
   public Long getNumberOfMillis() {
     return MILLIS_PER_MINUTE;
   }
   
   public String getMessageKey(Long numberOfUnits) {
     if (numberOfUnits.longValue() == 1L) {
       return "time.unit.minute";
     }
     return "time.unit.minutes";
   }
 }


