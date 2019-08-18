 package org.activiti.explorer.reporting;
 
 import java.util.HashMap;
 import java.util.Map;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class Dataset
 {
   protected String type;
   protected String description;
   protected String xaxis;
   protected String yaxis;
   protected Map<String, Number> data = new HashMap();
   
   public String getType() {
     return this.type;
   }
   
   public void setType(String type) {
     this.type = type;
   }
   
   public String getDescription() {
     return this.description;
   }
   
   public void setDescription(String description) {
     this.description = description;
   }
   
   public Map<String, Number> getData() {
     return this.data;
   }
   
   public String getXaxis() {
     return this.xaxis;
   }
   
   public void setXaxis(String xaxis) {
     this.xaxis = xaxis;
   }
   
   public String getYaxis() {
     return this.yaxis;
   }
   
   public void setYaxis(String yaxis) {
     this.yaxis = yaxis;
   }
   
   public void setData(Map<String, Number> data) {
     this.data = data;
   }
   
   public void add(String key, Number value) {
     this.data.put(key, value);
   }
 }


