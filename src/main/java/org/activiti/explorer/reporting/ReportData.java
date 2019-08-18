 package org.activiti.explorer.reporting;
 
 import com.fasterxml.jackson.annotation.JsonInclude;
 import com.fasterxml.jackson.annotation.JsonInclude.Include;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.SerializationFeature;
 import java.io.UnsupportedEncodingException;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.ActivitiException;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ReportData
 {
   protected String title;
   protected List<Dataset> datasets = new ArrayList();
   
   public String getTitle() {
     return this.title;
   }
   
   public void setTitle(String title) {
     this.title = title;
   }
   
   public List<Dataset> getDatasets() {
     return this.datasets;
   }
   
   public void setDatasets(List<Dataset> datasets) {
     this.datasets = datasets;
   }
   
   public void addDataset(Dataset dataset) {
     this.datasets.add(dataset);
   }
   
   public Dataset newDataset() {
     Dataset dataset = new Dataset();
     addDataset(dataset);
     return dataset;
   }
   
   public String toString() {
     try {
       return new String(toBytes(), "UTF-8");
     } catch (UnsupportedEncodingException e) {
       throw new ActivitiException("Could not convert report data to json", e);
     }
   }
   
   public byte[] toBytes() {
     try {
       ObjectMapper objectMapper = new ObjectMapper();
       objectMapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, false);
       objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
       objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
       return objectMapper.writeValueAsBytes(this);
     } catch (Exception e) {
       throw new ActivitiException("Could not convert report data to json", e);
     }
   }
 }


