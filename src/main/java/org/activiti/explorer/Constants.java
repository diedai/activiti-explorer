 package org.activiti.explorer;
 
 import java.text.DateFormat;
 import java.text.SimpleDateFormat;
 import java.util.Arrays;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Map;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class Constants
 {
   public static final String RESOURCE_BUNDLE = "messages";
   public static final String SECURITY_ROLE = "security-role";
   public static final String SECURITY_ROLE_USER = "user";
   public static final String SECURITY_ROLE_ADMIN = "admin";
   public static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
   public static final String DEFAULT_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss";
   public static final DateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
   public static final DateFormat DEFAULT_TIME_FORMATTER = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
   
   public static final String DEFAULT_DIAGRAM_IMAGE_EXTENSION = "png";
   
   public static final int TASK_PRIORITY_LOW = 0;
   
   public static final int TASK_PRIORITY_MEDIUM = 50;
   
   public static final int TASK_PRIORITY_HIGH = 100;
   
   public static final String MIMETYPE_BPM = "image/bpm";
   
   public static final String MIMETYPE_GIF = "image/gif";
   public static final String MIMETYPE_JPEG = "image/jpeg";
   public static final String MIMETYPE_PNG = "image/png";
   public static final Collection<String> DEFAULT_IMAGE_MIMETYPES = Arrays.asList(new String[] { "image/bpm", "image/gif", "image/jpeg", "image/png" });
   public static Map<String, String> MIMETYPE_EXTENSION_MAPPING;
   public static final String USER_INFO_BIRTH_DATE = "birthDate";
   public static final String USER_INFO_JOB_TITLE = "jobTitle";
   
   static { Map<String, String> mapping = new HashMap();
     mapping.put("image/bpm", "bpm");
     mapping.put("image/gif", "gif");
     mapping.put("image/jpeg", "jpg");
     mapping.put("image/png", "png");
     MIMETYPE_EXTENSION_MAPPING = Collections.unmodifiableMap(mapping);
   }
   
   public static final String USER_INFO_LOCATION = "location";
   public static final String USER_INFO_PHONE = "phone";
   public static final String USER_INFO_TWITTER = "twitterName";
   public static final String USER_INFO_SKYPE = "skype";
   public static final String EMAIL_RECIPIENT = "recipients";
   public static final String EMAIL_SENT_DATE = "sentDate";
   public static final String EMAIL_RECEIVED_DATE = "receivedDate";
   public static final String EMAIL_SUBJECT = "subject";
   public static final String EMAIL_HTML_CONTENT = "htmlContent";
   public static final String AUTHENTICATED_USER_ID = "_currentUser";
 }


