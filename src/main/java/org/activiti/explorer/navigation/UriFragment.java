 package org.activiti.explorer.navigation;
 
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Map;
 import org.activiti.explorer.util.UriUtility;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UriFragment
   implements Serializable
 {
   private List<String> uriParts;
   private Map<String, String> parameters;
   
   public UriFragment(String fragment)
   {
     String fragmentUri = UriUtility.extractUri(fragment);
     this.uriParts = UriUtility.getFragmentParts(fragmentUri);
     
 
     String queryString = UriUtility.extractQueryString(fragment);
     this.parameters = UriUtility.parseQueryParameters(queryString, null);
   }
   
   public UriFragment(List<String> uriParts, Map<String, String> parameters) {
     this.uriParts = uriParts;
     this.parameters = parameters;
   }
   
   public UriFragment(Map<String, String> parameters, String... uriParts) {
     this.uriParts = new ArrayList(Arrays.asList(uriParts));
     this.parameters = parameters;
   }
   
   public UriFragment(String... uriParts) {
     this(new LinkedHashMap(), uriParts);
   }
   
   public void addParameter(String name, String value) {
     this.parameters.put(name, value);
   }
   
   public void addUriPart(String part) {
     this.uriParts.add(part);
   }
   
   public List<String> getUriParts() {
     return this.uriParts;
   }
   
   public Map<String, String> getParameters() {
     return this.parameters;
   }
   
   public String getParameter(String name)
   {
     if (this.parameters != null) {
       return (String)this.parameters.get(name);
     }
     return null;
   }
   
   public String getUriPart(int index) {
     if ((index >= 0) && (index < this.uriParts.size())) {
       return (String)this.uriParts.get(index);
     }
     return null;
   }
   
   public String toString()
   {
     return UriUtility.getPath(this.uriParts) + UriUtility.getQueryString(this.parameters);
   }
 }


