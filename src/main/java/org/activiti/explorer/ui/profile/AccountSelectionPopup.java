 package org.activiti.explorer.ui.profile;
 
 import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.CheckBox;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.Form;
 import com.vaadin.ui.Layout;
 import com.vaadin.ui.PasswordField;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.TextField;
 import java.util.HashMap;
 import java.util.Map;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.TabbedSelectionWindow;
 import org.activiti.explorer.ui.event.SubmitEvent;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AccountSelectionPopup
   extends TabbedSelectionWindow
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected Form imapForm;
   protected Button.ClickListener imapClickListener;
   protected Form alfrescoForm;
   protected Button.ClickListener alfrescoClickListener;
   
   public AccountSelectionPopup(String title)
   {
     super(title);
     setWidth(600.0F, 0);
     setHeight(400.0F, 0);
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
 
 
 
     initImapComponent();
     String imap = this.i18nManager.getMessage("profile.account.imap");
     addSelectionItem(new Embedded(null, Images.IMAP), imap, this.imapForm, this.imapClickListener);
     
 
     initAlfrescoComponent();
     addSelectionItem(new Embedded(null, Images.ALFRESCO), this.i18nManager
       .getMessage("profile.account.alfresco"), this.alfrescoForm, this.alfrescoClickListener);
     
 
     this.selectionTable.select(imap);
   }
   
   protected void initImapComponent() {
     this.imapForm = new Form();
     this.imapForm.setDescription(this.i18nManager.getMessage("imap.description"));
     
     final TextField imapServer = new TextField(this.i18nManager.getMessage("imap.server"));
     this.imapForm.getLayout().addComponent(imapServer);
     
     final TextField imapPort = new TextField(this.i18nManager.getMessage("imap.port"));
     imapPort.setWidth(30.0F, 0);
     imapPort.setValue(Integer.valueOf(143));
     this.imapForm.getLayout().addComponent(imapPort);
     
     final CheckBox useSSL = new CheckBox(this.i18nManager.getMessage("imap.ssl"));
     useSSL.setValue(Boolean.valueOf(false));
     useSSL.setImmediate(true);
     this.imapForm.getLayout().addComponent(useSSL);
     useSSL.addListener(new Property.ValueChangeListener() {
       public void valueChange(Property.ValueChangeEvent event) {
         imapPort.setValue(Integer.valueOf(((Boolean)useSSL.getValue()).booleanValue() ? 993 : 143));
       }
       
     });
     final TextField imapUserName = new TextField(this.i18nManager.getMessage("imap.username"));
     this.imapForm.getLayout().addComponent(imapUserName);
     
     final PasswordField imapPassword = new PasswordField(this.i18nManager.getMessage("imap.password"));
     this.imapForm.getLayout().addComponent(imapPassword);
     
 
     this.imapClickListener = new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         Map<String, Object> accountDetails = AccountSelectionPopup.this.createAccountDetails("imap", imapUserName
         
           .getValue().toString(), imapPassword
           .getValue().toString(), new String[] { "server", imapServer
           .getValue().toString(), "port", imapPort
           .getValue().toString(), "ssl", imapPort
           .getValue().toString() });
         
         AccountSelectionPopup.this.fireEvent(new SubmitEvent(AccountSelectionPopup.this, "submit", accountDetails));
       }
     };
   }
   
   protected void initAlfrescoComponent() {
     this.alfrescoForm = new Form();
     this.alfrescoForm.setDescription(this.i18nManager.getMessage("alfresco.description"));
     
     final TextField alfrescoServer = new TextField(this.i18nManager.getMessage("alfresco.server"));
     this.alfrescoForm.getLayout().addComponent(alfrescoServer);
     
     final TextField alfrescoUserName = new TextField(this.i18nManager.getMessage("alfresco.username"));
     this.alfrescoForm.getLayout().addComponent(alfrescoUserName);
     
     final PasswordField alfrescoPassword = new PasswordField(this.i18nManager.getMessage("alfresco.password"));
     this.alfrescoForm.getLayout().addComponent(alfrescoPassword);
     
 
     this.alfrescoClickListener = new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         Map<String, Object> accountDetails = AccountSelectionPopup.this.createAccountDetails("alfresco", alfrescoUserName
         
           .getValue().toString(), alfrescoPassword
           .getValue().toString(), new String[] { "server", alfrescoServer
           .getValue().toString() });
         
         AccountSelectionPopup.this.fireEvent(new SubmitEvent(AccountSelectionPopup.this, "submit", accountDetails));
       }
     };
   }
   
   protected Map<String, Object> createAccountDetails(String acountName, String userName, String password, String... additionalDetails)
   {
     Map<String, Object> accountDetails = new HashMap();
     accountDetails.put("accountName", acountName);
     accountDetails.put("userName", userName);
     accountDetails.put("password", password);
     
     if ((additionalDetails != null) && (additionalDetails.length > 0)) {
       Map<String, String> additional = new HashMap();
       for (int i = 0; i < additionalDetails.length; i += 2) {
         additional.put(additionalDetails[i], additionalDetails[(i + 1)]);
       }
       accountDetails.put("additional", additional);
     }
     
     return accountDetails;
   }
 }


