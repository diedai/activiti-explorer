 package org.activiti.explorer.util;
 
 import java.io.UnsupportedEncodingException;
 import java.net.URLDecoder;
 import java.net.URLEncoder;
 import java.util.ArrayList;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.Scanner;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UriUtility
 {
   private static final String PARAMETER_SEPARATOR = "&";
   private static final String QUERY_STRING_SEPARATOR = "?";
   private static final String NAME_VALUE_SEPARATOR = "=";
   private static final String URL_PART_SEPARATOR = "/";
   
   public static List<String> getFragmentParts(String url)
   {
     if (url != null) {
       List<String> parts = new ArrayList();
       String[] partsArray = url.split("/");
       for (String part : partsArray) {
         if (part.length() > 0) {
           parts.add(part);
         }
       }
       return parts;
     }
     return null;
   }
   
 
 
   public static String extractQueryString(String fragment)
   {
     if (fragment != null) {
       int firstIndex = fragment.indexOf("?");
       if (firstIndex >= 0) {
         return fragment.substring(firstIndex);
       }
     }
     return null;
   }
   
 
 
   public static String extractUri(String fragment)
   {
     if (fragment != null) {
       int firstIndex = fragment.indexOf("?");
       if (firstIndex >= 0) {
         return fragment.substring(0, firstIndex);
       }
       
       return fragment;
     }
     
     return null;
   }
   
 
 
 
 
   public static Map<String, String> parseQueryParameters(String queryString, String encoding)
   {
     Map<String, String> parameters = new LinkedHashMap();
     if (queryString != null)
     {
       if (queryString.startsWith("?")) {
         queryString = queryString.substring(1);
       }
       
       Scanner scanner = new Scanner(queryString);
       scanner.useDelimiter("&");
       while (scanner.hasNext()) {
         String[] nameValue = scanner.next().split("=");
         if ((nameValue.length == 0) || (nameValue.length > 2)) {
           throw new IllegalArgumentException("bad parameter");
         }
         String name = decode(nameValue[0], encoding);
         String value = null;
         if (nameValue.length == 2) {
           value = decode(nameValue[1], encoding);
         }
         parameters.put(name, value);
       }
     }
     return parameters;
   }
   
 
 
   public static String getQueryString(Map<String, String> parameters)
   {
     StringBuilder result = new StringBuilder();
     
     if (parameters != null) {
       for (Map.Entry<String, String> param : parameters.entrySet()) {
         String encodedName = encode((String)param.getKey(), null);
         String value = (String)param.getValue();
         String encodedValue = value != null ? encode(value, null) : "";
         if (result.length() > 0) {
           result.append("&");
         } else {
           result.append("?");
         }
         result.append(encodedName);
         result.append("=");
         result.append(encodedValue);
       }
     }
     
     return result.toString();
   }
   
 
 
   public static String getPath(List<String> parts)
   {
     if (parts != null) {
       StringBuilder result = new StringBuilder();
       for (String part : parts) {
         if (result.length() > 0) {
           result.append("/");
         }
         result.append(part);
       }
       return result.toString();
     }
     return "";
   }
   
   private static String decode(String content, String encoding) {
     try {
       return URLDecoder.decode(content, encoding != null ? encoding : "UTF-8");
     } catch (UnsupportedEncodingException problem) {
       throw new IllegalArgumentException(problem);
     }
   }
   
   private static String encode(String content, String encoding) {
     try {
       return URLEncoder.encode(content, encoding != null ? encoding : "UTF-8");
     } catch (UnsupportedEncodingException problem) {
       throw new IllegalArgumentException(problem);
     }
   }
 }


