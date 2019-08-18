 package org.activiti.explorer.ui.alfresco;
 
 import com.vaadin.data.Property;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Component;
 import org.activiti.engine.runtime.ProcessInstance;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.Images;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 class AlfrescoProcessInstanceTableItem
   extends PropertysetItem
   implements Comparable<AlfrescoProcessInstanceTableItem>
 {
   private static final long serialVersionUID = 1L;
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_BUSINESSKEY = "businessKey";
   public static final String PROPERTY_ACTIONS = "actions";
   
   public AlfrescoProcessInstanceTableItem(final ProcessInstance processInstance)
   {
     addItemProperty("id", new ObjectProperty(processInstance.getId(), String.class));
     
     if (processInstance.getBusinessKey() != null) {
       addItemProperty("businessKey", new ObjectProperty(processInstance.getBusinessKey(), String.class));
     }
     
     Button viewProcessInstanceButton = new Button(ExplorerApp.get().getI18nManager().getMessage("process.action.view"));
     viewProcessInstanceButton.addStyleName("link");
     viewProcessInstanceButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) { ExplorerApp.get().getViewManager().showProcessInstancePage(processInstance.getId());
       }
 
     });
     viewProcessInstanceButton.setIcon(Images.MAGNIFIER_16);
     addItemProperty("actions", new ObjectProperty(viewProcessInstanceButton, Component.class));
   }
   
   public int compareTo(AlfrescoProcessInstanceTableItem other)
   {
     String id = (String)getItemProperty("id").getValue();
     String otherId = (String)other.getItemProperty("id").getValue();
     return id.compareTo(otherId);
   }
 }


