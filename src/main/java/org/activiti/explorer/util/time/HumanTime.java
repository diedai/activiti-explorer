 package org.activiti.explorer.util.time;
 
 import java.util.Arrays;
 import java.util.Date;
 import java.util.List;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.util.time.timeunit.DayTimeUnit;
 import org.activiti.explorer.util.time.timeunit.HourTimeUnit;
 import org.activiti.explorer.util.time.timeunit.MinuteTimeUnit;
 import org.activiti.explorer.util.time.timeunit.MonthTimeUnit;
 import org.activiti.explorer.util.time.timeunit.WeekTimeUnit;
 import org.activiti.explorer.util.time.timeunit.YearTimeUnit;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class HumanTime
 {
   private static final List<TimeUnit> timeUnits = Arrays.asList(new TimeUnit[] { new YearTimeUnit(), new MonthTimeUnit(), new WeekTimeUnit(), new DayTimeUnit(), new HourTimeUnit(), new MinuteTimeUnit() });
   
 
 
 
   private Long baseDate;
   
 
 
   private I18nManager i18nManager;
   
 
 
 
   public HumanTime(I18nManager i18nManager)
   {
     this(null, i18nManager);
   }
   
 
 
   public HumanTime(Date date, I18nManager i18nManager)
   {
     if (i18nManager == null) {
       throw new IllegalArgumentException("I18NManager is required!");
     }
     
     this.i18nManager = i18nManager;
     if (date != null) {
       this.baseDate = Long.valueOf(date.getTime());
     } else {
       this.baseDate = Long.valueOf(new Date().getTime());
     }
   }
   
 
 
 
 
   public String format(Date date)
   {
     boolean future = true;
     Long difference = Long.valueOf(date.getTime() - this.baseDate.longValue());
     if (difference.longValue() < 0L) {
       future = false;
       difference = Long.valueOf(-difference.longValue());
     } else if (difference.longValue() == 0L) {
       return this.i18nManager.getMessage("time.unit.just.now");
     }
     
     String unitMessage = getUnitMessage(difference);
     
     String messageKey = null;
     if (future) {
       messageKey = "time.unit.future";
     } else {
       messageKey = "time.unit.past";
     }
     
     return this.i18nManager.getMessage(messageKey, new Object[] { unitMessage });
   }
   
   private String getUnitMessage(Long difference) {
     String unitMessage = null;
     TimeUnit unitToUse = null;
     TimeUnit currentUnit = null;
     
     for (int i = 0; (i < timeUnits.size()) && (unitToUse == null); i++) {
       currentUnit = (TimeUnit)timeUnits.get(i);
       
       if (currentUnit.getNumberOfMillis().longValue() <= difference.longValue()) {
         unitToUse = currentUnit;
       }
     }
     
     if (unitToUse == null)
     {
       unitMessage = this.i18nManager.getMessage("time.unit.moments");
     }
     else {
       Long numberOfUnits = Long.valueOf((difference.longValue() - difference.longValue() % unitToUse.getNumberOfMillis().longValue()) / unitToUse.getNumberOfMillis().longValue());
       unitMessage = this.i18nManager.getMessage(unitToUse.getMessageKey(numberOfUnits), new Object[] { numberOfUnits });
     }
     
     return unitMessage;
   }
 }


