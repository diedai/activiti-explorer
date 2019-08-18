 package org.activiti.explorer.util.time.timeunit;
 
 import org.activiti.explorer.util.time.TimeUnit;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class YearTimeUnit
   implements TimeUnit
 {
   private static final Long MILLIS_PER_YEAR = Long.valueOf(31536000000L);
   
   public Long getNumberOfMillis() {
     return MILLIS_PER_YEAR;
   }
   
   public String getMessageKey(Long numberOfUnits) {
     if (numberOfUnits.longValue() == 1L) {
       return "time.unit.year";
     }
     return "time.unit.years";
   }
 }


