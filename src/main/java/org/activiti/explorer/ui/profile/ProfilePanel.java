 package org.activiti.explorer.ui.profile;
 
 import com.vaadin.terminal.ExternalResource;
 import com.vaadin.terminal.StreamResource;
 import com.vaadin.terminal.StreamResource.StreamSource;
 import com.vaadin.ui.AbstractField;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.DateField;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Link;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.PasswordField;
 import com.vaadin.ui.TextField;
 import com.vaadin.ui.Upload;
 import com.vaadin.ui.Upload.FinishedEvent;
 import com.vaadin.ui.Upload.FinishedListener;
 import com.vaadin.ui.VerticalLayout;
 import java.io.InputStream;
 import java.text.SimpleDateFormat;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.identity.Picture;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.explorer.Constants;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.InMemoryUploadReceiver;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProfilePanel
   extends Panel
 {
   private static final long serialVersionUID = -4274649964206760400L;
   protected transient IdentityService identityService;
   protected I18nManager i18nManager;
   protected ViewManager viewManager;
   protected String userId;
   protected User user;
   protected Picture picture;
   protected String birthDate;
   protected String jobTitle;
   protected String location;
   protected String phone;
   protected String twitterName;
   protected String skypeId;
   protected boolean isCurrentLoggedInUser;
   protected boolean editable = false;
   protected HorizontalLayout profilePanelLayout;
   protected VerticalLayout imageLayout;
   protected VerticalLayout infoPanelLayout;
   protected TextField firstNameField;
   protected TextField lastNameField;
   protected PasswordField passwordField;
   protected TextField jobTitleField;
   protected DateField birthDateField;
   protected TextField locationField;
   protected TextField emailField;
   protected TextField phoneField;
   protected TextField twitterField;
   protected TextField skypeField;
   
   public ProfilePanel(String userId) {
     this.userId = userId;
     this.isCurrentLoggedInUser = userId.equals(ExplorerApp.get().getLoggedInUser().getId());
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.viewManager = ExplorerApp.get().getViewManager();
     
     loadProfileData();
     initUi();
   }
   
   protected void loadProfileData() {
     this.user = ((User)this.identityService.createUserQuery().userId(this.userId).singleResult());
     this.picture = this.identityService.getUserPicture(this.user.getId());
     this.birthDate = this.identityService.getUserInfo(this.user.getId(), "birthDate");
     this.jobTitle = this.identityService.getUserInfo(this.user.getId(), "jobTitle");
     this.location = this.identityService.getUserInfo(this.user.getId(), "location");
     this.phone = this.identityService.getUserInfo(this.user.getId(), "phone");
     this.twitterName = this.identityService.getUserInfo(this.user.getId(), "twitterName");
     this.skypeId = this.identityService.getUserInfo(this.user.getId(), "skype");
   }
   
   protected void initUi() {
     removeAllComponents();
     addStyleName("light");
     addStyleName("profile-layout");
     setSizeFull();
     
 
 
     this.profilePanelLayout = new HorizontalLayout();
     this.profilePanelLayout.setSizeFull();
     setContent(this.profilePanelLayout);
     
 
     initImagePanel();
     
     Label emptySpace = new Label("&nbsp;", 3);
     emptySpace.setWidth(50.0F, 0);
     this.profilePanelLayout.addComponent(emptySpace);
     
     initInformationPanel();
   }
   
   protected void initImagePanel() {
     this.imageLayout = new VerticalLayout();
     this.imageLayout.setSpacing(true);
     this.imageLayout.setHeight("100%");
     this.profilePanelLayout.addComponent(this.imageLayout);
     initPicture();
   }
   
 
 
 
 
   protected void initPicture()
   {
     StreamResource imageresource = new StreamResource(new StreamResource.StreamSource()
     {
       private static final long serialVersionUID = 1L;
       
       public InputStream getStream()
       {
         return ProfilePanel.this.picture.getInputStream();
       }
     }, this.user.getId(), ExplorerApp.get());
     imageresource.setCacheTime(0L);
     
     Embedded picture = new Embedded(null, imageresource);
     picture.setType(1);
     picture.setHeight(200.0F, 0);
     picture.setWidth(200.0F, 0);
     picture.addStyleName("profile-picture");
     
     this.imageLayout.addComponent(picture);
     this.imageLayout.setWidth(picture.getWidth() + 5.0F, picture.getWidthUnits());
     
 
     if (this.isCurrentLoggedInUser) {
       Upload changePictureButton = initChangePictureButton();
       this.imageLayout.addComponent(changePictureButton);
       this.imageLayout.setComponentAlignment(changePictureButton, Alignment.MIDDLE_CENTER);
     }
   }
   
   protected Upload initChangePictureButton() {
     Upload changePictureUpload = new Upload();
     changePictureUpload.setImmediate(true);
     changePictureUpload.setButtonCaption(this.i18nManager.getMessage("profile.change.picture"));
     
     final InMemoryUploadReceiver receiver = initPictureReceiver(changePictureUpload);
     changePictureUpload.addListener(new Upload.FinishedListener() {
       private static final long serialVersionUID = 1L;
       
       public void uploadFinished(Upload.FinishedEvent event) { if (!receiver.isInterruped()) {
           ProfilePanel.this.picture = new Picture(receiver.getBytes(), receiver.getMimeType());
           ProfilePanel.this.identityService.setUserPicture(ProfilePanel.this.userId, ProfilePanel.this.picture);
           
 
           ProfilePanel.this.imageLayout.removeAllComponents();
           ProfilePanel.this.initPicture();
         } else {
           receiver.reset();
         }
         
       }
     });
     return changePictureUpload;
   }
   
   protected InMemoryUploadReceiver initPictureReceiver(Upload upload) {
     InMemoryUploadReceiver receiver = new InMemoryUploadReceiver(upload, 102400L);
     upload.setReceiver(receiver);
     receiver.setAcceptedMimeTypes(Constants.DEFAULT_IMAGE_MIMETYPES);
     return receiver;
   }
   
   protected void initInformationPanel() {
     Panel infoPanel = new Panel();
     infoPanel.addStyleName("light");
     infoPanel.setSizeFull();
     
     this.profilePanelLayout.addComponent(infoPanel);
     this.profilePanelLayout.setExpandRatio(infoPanel, 1.0F);
     
 
     this.infoPanelLayout = new VerticalLayout();
     infoPanel.setContent(this.infoPanelLayout);
     
     initAboutSection();
     initContactSection();
   }
   
 
   protected void initAboutSection()
   {
     HorizontalLayout header = new HorizontalLayout();
     header.setWidth(100.0F, 8);
     header.addStyleName("block-holder");
     this.infoPanelLayout.addComponent(header);
     
     Label aboutLabel = createProfileHeader(this.infoPanelLayout, this.i18nManager.getMessage("profile.about"));
     header.addComponent(aboutLabel);
     header.setExpandRatio(aboutLabel, 1.0F);
     
 
     if (this.isCurrentLoggedInUser) {
       Button actionButton = null;
       if (!this.editable) {
         actionButton = initEditProfileButton();
       } else {
         actionButton = initSaveProfileButton();
       }
       header.addComponent(actionButton);
       header.setComponentAlignment(actionButton, Alignment.MIDDLE_RIGHT);
     }
     
 
     GridLayout aboutLayout = createInfoSectionLayout(2, 4);
     
 
     if ((!this.editable) && ((isDefined(this.user.getFirstName())) || (isDefined(this.user.getLastName())))) {
       addProfileEntry(aboutLayout, this.i18nManager.getMessage("profile.name"), this.user.getFirstName() + " " + this.user.getLastName());
     } else if (this.editable) {
       this.firstNameField = new TextField();
       this.firstNameField.focus();
       addProfileInputField(aboutLayout, this.i18nManager.getMessage("profile.firstname"), this.firstNameField, this.user.getFirstName());
       this.lastNameField = new TextField();
       addProfileInputField(aboutLayout, this.i18nManager.getMessage("profile.lastname"), this.lastNameField, this.user.getLastName());
     }
     
 
     if ((!this.editable) && (isDefined(this.jobTitle))) {
       addProfileEntry(aboutLayout, this.i18nManager.getMessage("profile.jobtitle"), this.jobTitle);
     } else if (this.editable) {
       this.jobTitleField = new TextField();
       addProfileInputField(aboutLayout, this.i18nManager.getMessage("profile.jobtitle"), this.jobTitleField, this.jobTitle);
     }
     
 
     if ((!this.editable) && (isDefined(this.birthDate))) {
       addProfileEntry(aboutLayout, this.i18nManager.getMessage("profile.birthdate"), this.birthDate);
     } else if (this.editable) {
       this.birthDateField = new DateField();
       this.birthDateField.setDateFormat("dd-MM-yyyy");
       this.birthDateField.setResolution(4);
       try {
         this.birthDateField.setValue(new SimpleDateFormat("dd-MM-yyyy").parse(this.birthDate));
       } catch (Exception localException) {}
       addProfileInputField(aboutLayout, this.i18nManager.getMessage("profile.birthdate"), this.birthDateField, null);
     }
     
 
     if ((!this.editable) && (isDefined(this.location))) {
       addProfileEntry(aboutLayout, this.i18nManager.getMessage("profile.location"), this.location);
     } else if (this.editable) {
       this.locationField = new TextField();
       addProfileInputField(aboutLayout, this.i18nManager.getMessage("profile.location"), this.locationField, this.location);
     }
   }
   
   protected Button initEditProfileButton() {
     Button editProfileButton = new Button(this.i18nManager.getMessage("profile.edit"));
     editProfileButton.setIcon(Images.EDIT);
     editProfileButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         ProfilePanel.this.editable = true;
         ProfilePanel.this.initUi();
       }
     });
     return editProfileButton;
   }
   
   protected Button initSaveProfileButton() {
     Button saveProfileButton = new Button(this.i18nManager.getMessage("profile.save"));
     saveProfileButton.setIcon(Images.SAVE);
     saveProfileButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         ProfilePanel.this.user.setFirstName((String)ProfilePanel.this.firstNameField.getValue());
         ProfilePanel.this.user.setLastName((String)ProfilePanel.this.lastNameField.getValue());
         ProfilePanel.this.user.setEmail((String)ProfilePanel.this.emailField.getValue());
         ProfilePanel.this.identityService.saveUser(ProfilePanel.this.user);
         
         ProfilePanel.this.identityService.setUserInfo(ProfilePanel.this.user.getId(), "jobTitle", ProfilePanel.this.jobTitleField.getValue().toString());
         if ((ProfilePanel.this.birthDateField.getValue() != null) && (!"".equals(ProfilePanel.this.birthDateField.getValue().toString()))) {
           ProfilePanel.this.identityService.setUserInfo(ProfilePanel.this.user.getId(), "birthDate", new SimpleDateFormat("dd-MM-yyyy").format(ProfilePanel.this.birthDateField.getValue()));
         }
         ProfilePanel.this.identityService.setUserInfo(ProfilePanel.this.user.getId(), "location", ProfilePanel.this.locationField.getValue().toString());
         ProfilePanel.this.identityService.setUserInfo(ProfilePanel.this.user.getId(), "phone", ProfilePanel.this.phoneField.getValue().toString());
         ProfilePanel.this.identityService.setUserInfo(ProfilePanel.this.user.getId(), "twitterName", ProfilePanel.this.twitterField.getValue().toString());
         ProfilePanel.this.identityService.setUserInfo(ProfilePanel.this.user.getId(), "skype", ProfilePanel.this.skypeField.getValue().toString());
         
 
         ProfilePanel.this.editable = false;
         ProfilePanel.this.loadProfileData();
         ProfilePanel.this.initUi();
       }
     });
     return saveProfileButton;
   }
   
   protected void initContactSection() {
     Label header = createProfileHeader(this.infoPanelLayout, this.i18nManager.getMessage("profile.contact"));
     header.addStyleName("h3");
     header.addStyleName("block-holder");
     this.infoPanelLayout.addComponent(header);
     
     GridLayout contactLayout = createInfoSectionLayout(2, 4);
     
     if ((!this.editable) && (isDefined(this.user.getEmail()))) {
       addProfileEntry(contactLayout, this.i18nManager.getMessage("profile.email"), this.user.getEmail());
     } else if (this.editable) {
       this.emailField = new TextField();
       addProfileInputField(contactLayout, this.i18nManager.getMessage("profile.email"), this.emailField, this.user.getEmail());
     }
     
 
     if ((!this.editable) && (isDefined(this.phone))) {
       addProfileEntry(contactLayout, this.i18nManager.getMessage("profile.phone"), this.phone);
     } else if (this.editable) {
       this.phoneField = new TextField();
       addProfileInputField(contactLayout, this.i18nManager.getMessage("profile.phone"), this.phoneField, this.phone);
     }
     
 
     if ((!this.editable) && (isDefined(this.twitterName))) {
       Link twitterLink = new Link(this.twitterName, new ExternalResource("http://www.twitter.com/" + this.twitterName));
       addProfileEntry(contactLayout, this.i18nManager.getMessage("profile.twitter"), twitterLink);
     } else if (this.editable) {
       this.twitterField = new TextField();
       addProfileInputField(contactLayout, this.i18nManager.getMessage("profile.twitter"), this.twitterField, this.twitterName);
     }
     
 
     if ((!this.editable) && (isDefined(this.skypeId)))
     {
       GridLayout skypeLayout = new GridLayout(2, 1);
       skypeLayout.setSpacing(true);
       skypeLayout.setSizeUndefined();
       
       Label skypeIdLabel = new Label(this.skypeId);
       skypeIdLabel.setSizeUndefined();
       skypeLayout.addComponent(skypeIdLabel);
       
       addProfileEntry(contactLayout, this.i18nManager.getMessage("profile.skype"), skypeLayout);
     } else if (this.editable) {
       this.skypeField = new TextField();
       addProfileInputField(contactLayout, this.i18nManager.getMessage("profile.skype"), this.skypeField, this.skypeId);
     }
   }
   
   protected boolean isDefined(String information) {
     return (information != null) && (!"".equals(information));
   }
   
   protected Label createProfileHeader(VerticalLayout infoLayout, String headerName) {
     Label label = new Label(headerName);
     label.setWidth(100.0F, 8);
     label.addStyleName("h3");
     return label;
   }
   
   protected GridLayout createInfoSectionLayout(int columns, int rows) {
     GridLayout layout = new GridLayout(columns, rows);
     layout.setSpacing(true);
     layout.setWidth(100.0F, 8);
     layout.setMargin(true, false, true, false);
     this.infoPanelLayout.addComponent(layout);
     return layout;
   }
   
   protected void addProfileEntry(GridLayout layout, String name, String value) {
     addProfileEntry(layout, name, new Label(value));
   }
   
   protected void addProfileEntry(GridLayout layout, String name, Component value) {
     addProfileEntry(layout, new Label(name + ": "), value);
   }
   
   protected void addProfileEntry(GridLayout layout, Component name, Component value) {
     name.addStyleName("profile-field");
     name.setSizeUndefined();
     layout.addComponent(name);
     
     value.setSizeUndefined();
     layout.addComponent(value);
   }
   
   protected void addProfileInputField(GridLayout layout, String name, AbstractField inputField, String inputFieldValue) {
     Label label = new Label(name + ": ");
     label.addStyleName("profile-field");
     label.setSizeUndefined();
     layout.addComponent(label);
     layout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
     
     if (inputFieldValue != null) {
       inputField.setValue(inputFieldValue);
     }
     layout.addComponent(inputField);
     layout.setComponentAlignment(inputField, Alignment.MIDDLE_LEFT);
   }
 }


