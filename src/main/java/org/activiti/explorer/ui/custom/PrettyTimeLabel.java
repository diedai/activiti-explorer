 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.ui.Label;
 import java.text.DateFormat;
 import java.text.MessageFormat;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.util.time.HumanTime;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class PrettyTimeLabel
   extends Label
 {
   private static final long serialVersionUID = 1L;
   protected String labelTemplate;
   protected Date date;
   protected String noDateCaption;
   protected boolean showTime;
   
   public PrettyTimeLabel(Date date, boolean showTime)
   {
     this(date, "", showTime);
   }
   
   public PrettyTimeLabel(Date date, String noDateCaption, boolean showTime) {
     this(null, date, noDateCaption, showTime);
   }
   
 
 
 
 
 
 
 
 
 
 
 
 
   public PrettyTimeLabel(String labelTemplate, Date date, String noDateCaption, boolean showTime)
   {
     this.labelTemplate = labelTemplate;
     this.date = date;
     this.noDateCaption = noDateCaption;
     this.showTime = showTime;
     
     init();
   }
   
   protected void init()
   {
     I18nManager i18nManager = ExplorerApp.get().getI18nManager();
     if (this.date != null) {
       DateFormat dateFormat = null;
       if (this.showTime) {
         dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
       } else {
         dateFormat = new SimpleDateFormat("dd-MM-yyyy");
       }
       
       if (this.labelTemplate != null) {
         super.setValue(MessageFormat.format(this.labelTemplate, new Object[] { new HumanTime(i18nManager).format(this.date) }));
       } else {
         super.setValue(new HumanTime(i18nManager).format(this.date));
       }
       setDescription(dateFormat.format(this.date));
     } else {
       super.setValue(this.noDateCaption);
       setDescription(this.noDateCaption);
     }
   }
   
   public void setValue(Object newValue)
   {
     if ((newValue instanceof Date)) {
       this.date = ((Date)newValue);
       init();
     } else if ((newValue instanceof String)) {
       this.date = null;
       init();
     } else {
       throw new IllegalArgumentException("Can only set " + Date.class + " as new value for prettyTimeLabel");
     }
   }
 }


