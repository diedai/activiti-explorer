 package org.activiti.explorer.ui.management.identity;
 
 import com.vaadin.terminal.StreamResource;
 import com.vaadin.terminal.StreamResource.StreamSource;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.PasswordField;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.TextField;
 import com.vaadin.ui.VerticalLayout;
 import java.io.InputStream;
 import java.util.Map;
 import java.util.Set;
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
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.ConfirmationDialogPopupWindow;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.event.ConfirmationEvent;
 import org.activiti.explorer.ui.event.ConfirmationEventListener;
 import org.activiti.explorer.ui.event.SubmitEvent;
 import org.activiti.explorer.ui.event.SubmitEventListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UserDetailPanel
   extends DetailPanel
   implements MemberShipChangeListener
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected I18nManager i18nManager;
   protected UserPage userPage;
   protected User user;
   protected boolean editingDetails;
   protected HorizontalLayout userDetailsLayout;
   protected TextField firstNameField;
   protected TextField lastNameField;
   protected TextField emailField;
   protected PasswordField passwordField;
   protected HorizontalLayout groupLayout;
   protected Table groupTable;
   protected LazyLoadingContainer groupContainer;
   protected GroupsForUserQuery groupsForUserQuery;
   protected Label noGroupsLabel;
   
   public UserDetailPanel(UserPage userPage, String userId)
   {
     this.userPage = userPage;
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.user = ((User)this.identityService.createUserQuery().userId(userId).singleResult());
     
     init();
   }
   
   protected void init() {
     setSizeFull();
     addStyleName("light");
     
     initPageTitle();
     initUserDetails();
     initGroups();
     
     initActions();
   }
   
   protected void initActions() {
     Button createUserButton = new Button(this.i18nManager.getMessage("user.create"));
     createUserButton.setIcon(Images.USER_16);
     
     createUserButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) { NewUserPopupWindow newUserPopupWindow = new NewUserPopupWindow();
         ExplorerApp.get().getViewManager().showPopupWindow(newUserPopupWindow);
       }
       
     });
     this.userPage.getToolBar().removeAllButtons();
     this.userPage.getToolBar().addButton(createUserButton);
   }
   
   protected void initPageTitle() {
     HorizontalLayout layout = new HorizontalLayout();
     layout.setWidth(100.0F, 8);
     layout.setSpacing(true);
     layout.setMargin(false, false, true, false);
     layout.addStyleName("title-block");
     addDetailComponent(layout);
     
     Embedded userImage = new Embedded(null, Images.USER_50);
     layout.addComponent(userImage);
     
     Label userName = new Label(this.user.getFirstName() + " " + this.user.getLastName());
     userName.setSizeUndefined();
     userName.addStyleName("h2");
     layout.addComponent(userName);
     layout.setComponentAlignment(userName, Alignment.MIDDLE_LEFT);
     layout.setExpandRatio(userName, 1.0F);
   }
   
   protected void initUserDetails() {
     Label userDetailsHeader = new Label(this.i18nManager.getMessage("user.header.details"));
     userDetailsHeader.addStyleName("h3");
     userDetailsHeader.addStyleName("block-holder");
     addDetailComponent(userDetailsHeader);
     
 
     this.userDetailsLayout = new HorizontalLayout();
     this.userDetailsLayout.setSpacing(true);
     this.userDetailsLayout.setMargin(false, false, true, false);
     addDetailComponent(this.userDetailsLayout);
     
     populateUserDetails();
   }
   
   protected void populateUserDetails() {
     loadPicture();
     loadUserDetails();
     initDetailsActions();
   }
   
   protected void loadPicture() {
     Component pictureComponent = null;
     final Picture userPicture = this.identityService.getUserPicture(this.user.getId());
     if (userPicture != null)
     {
 
 
 
 
       StreamResource imageresource = new StreamResource(new StreamResource.StreamSource()
       {
         private static final long serialVersionUID = 1L;
         
         public InputStream getStream()
         {
           return userPicture.getInputStream();
         }
       }, this.user.getId() + "." + (String)Constants.MIMETYPE_EXTENSION_MAPPING.get(userPicture.getMimeType()), ExplorerApp.get());
       pictureComponent = new Embedded(null, imageresource);
     } else {
       pictureComponent = new Label("");
     }
     pictureComponent.setHeight("200px");
     pictureComponent.setWidth("200px");
     pictureComponent.addStyleName("profile-picture");
     this.userDetailsLayout.addComponent(pictureComponent);
     this.userDetailsLayout.setComponentAlignment(pictureComponent, Alignment.MIDDLE_CENTER);
   }
   
   protected void loadUserDetails()
   {
     GridLayout detailGrid = new GridLayout();
     detailGrid.setColumns(2);
     detailGrid.setSpacing(true);
     detailGrid.setMargin(true, true, false, true);
     this.userDetailsLayout.addComponent(detailGrid);
     
 
     addUserDetail(detailGrid, this.i18nManager.getMessage("user.id"), new Label(this.user.getId()));
     if (!this.editingDetails) {
       addUserDetail(detailGrid, this.i18nManager.getMessage("user.firstname"), new Label(this.user.getFirstName()));
       addUserDetail(detailGrid, this.i18nManager.getMessage("user.lastname"), new Label(this.user.getLastName()));
       addUserDetail(detailGrid, this.i18nManager.getMessage("user.email"), new Label(this.user.getEmail()));
     } else {
       this.firstNameField = new TextField(null, this.user.getFirstName() != null ? this.user.getFirstName() : "");
       addUserDetail(detailGrid, this.i18nManager.getMessage("user.firstname"), this.firstNameField);
       this.firstNameField.focus();
       
       this.lastNameField = new TextField(null, this.user.getLastName() != null ? this.user.getLastName() : "");
       addUserDetail(detailGrid, this.i18nManager.getMessage("user.lastname"), this.lastNameField);
       
       this.emailField = new TextField(null, this.user.getEmail() != null ? this.user.getEmail() : "");
       addUserDetail(detailGrid, this.i18nManager.getMessage("user.email"), this.emailField);
       
       this.passwordField = new PasswordField();
       Label cautionLabel = new Label(this.i18nManager.getMessage("user.reset.password"));
       cautionLabel.addStyleName("light");
       HorizontalLayout passwordLayout = new HorizontalLayout();
       passwordLayout.setSpacing(true);
       passwordLayout.addComponent(this.passwordField);
       passwordLayout.addComponent(cautionLabel);
       passwordLayout.setComponentAlignment(cautionLabel, Alignment.MIDDLE_LEFT);
       addUserDetail(detailGrid, this.i18nManager.getMessage("user.password"), passwordLayout);
     }
   }
   
   protected void addUserDetail(GridLayout detailLayout, String detail, Component value) {
     Label label = new Label(detail + ": ");
     label.addStyleName("bold");
     detailLayout.addComponent(label);
     detailLayout.addComponent(value);
   }
   
   protected void initDetailsActions() {
     VerticalLayout actionLayout = new VerticalLayout();
     actionLayout.setSpacing(true);
     actionLayout.setMargin(false, false, false, true);
     this.userDetailsLayout.addComponent(actionLayout);
     
     if (!this.editingDetails) {
       initEditButton(actionLayout);
       initDeleteButton(actionLayout);
     } else {
       initSaveButton(actionLayout);
     }
   }
   
   protected void initEditButton(VerticalLayout actionLayout) {
     Button editButton = new Button(this.i18nManager.getMessage("user.edit"));
     editButton.addStyleName("small");
     actionLayout.addComponent(editButton);
     editButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         UserDetailPanel.this.editingDetails = true;
         UserDetailPanel.this.userDetailsLayout.removeAllComponents();
         UserDetailPanel.this.populateUserDetails();
       }
     });
   }
   
   protected void initSaveButton(VerticalLayout actionLayout) {
     Button saveButton = new Button(this.i18nManager.getMessage("user.save"));
     saveButton.addStyleName("small");
     actionLayout.addComponent(saveButton);
     saveButton.addListener(new Button.ClickListener()
     {
       public void buttonClick(ClickEvent event) {
         String originalFirstName = UserDetailPanel.this.user.getFirstName();
         String originalLastName = UserDetailPanel.this.user.getLastName();
         
 
         UserDetailPanel.this.user.setFirstName(UserDetailPanel.this.firstNameField.getValue().toString());
         UserDetailPanel.this.user.setLastName(UserDetailPanel.this.lastNameField.getValue().toString());
         UserDetailPanel.this.user.setEmail(UserDetailPanel.this.emailField.getValue().toString());
         if ((UserDetailPanel.this.passwordField.getValue() != null) && (!"".equals(UserDetailPanel.this.passwordField.getValue().toString()))) {
           UserDetailPanel.this.user.setPassword(UserDetailPanel.this.passwordField.getValue().toString());
         }
         UserDetailPanel.this.identityService.saveUser(UserDetailPanel.this.user);
         
 
         UserDetailPanel.this.editingDetails = false;
         UserDetailPanel.this.userDetailsLayout.removeAllComponents();
         UserDetailPanel.this.populateUserDetails();
         
 
         if (UserDetailPanel.this.nameChanged(originalFirstName, originalLastName)) {
           UserDetailPanel.this.userPage.notifyUserChanged(UserDetailPanel.this.user.getId());
         }
       }
     });
   }
   
   protected boolean nameChanged(String originalFirstName, String originalLastName) {
     boolean nameChanged = false;
     if (originalFirstName != null) {
       nameChanged = !originalFirstName.equals(this.user.getFirstName());
     } else {
       nameChanged = this.user.getFirstName() != null;
     }
     
     if (!nameChanged) {
       if (originalLastName != null) {
         nameChanged = !originalLastName.equals(this.user.getLastName());
       } else {
         nameChanged = this.user.getLastName() != null;
       }
     }
     return nameChanged;
   }
   
   protected void initDeleteButton(VerticalLayout actionLayout) {
     Button deleteButton = new Button(this.i18nManager.getMessage("user.delete"));
     deleteButton.addStyleName("small");
     actionLayout.addComponent(deleteButton);
     deleteButton.addListener(new Button.ClickListener()
     {
       public void buttonClick(ClickEvent event) {
         ConfirmationDialogPopupWindow confirmPopup = new ConfirmationDialogPopupWindow(UserDetailPanel.this.i18nManager.getMessage("user.confirm.delete", new Object[] { UserDetailPanel.this.user.getId() }));
         
         confirmPopup.addListener(new ConfirmationEventListener()
         {
           protected void rejected(ConfirmationEvent event) {}
           
           protected void confirmed(ConfirmationEvent event) {
             UserDetailPanel.this.identityService.deleteUser(UserDetailPanel.this.user.getId());
             
 
             UserDetailPanel.this.userPage.refreshSelectNext();
           }
           
         });
         ExplorerApp.get().getViewManager().showPopupWindow(confirmPopup);
       }
     });
   }
   
   protected void initGroups() {
     HorizontalLayout groupHeader = new HorizontalLayout();
     groupHeader.setWidth(100.0F, 8);
     groupHeader.setSpacing(true);
     groupHeader.setMargin(false, false, true, false);
     groupHeader.addStyleName("block-holder");
     addDetailComponent(groupHeader);
     
     initGroupTitle(groupHeader);
     initAddGroupsButton(groupHeader);
     
     this.groupLayout = new HorizontalLayout();
     this.groupLayout.setWidth(100.0F, 8);
     addDetailComponent(this.groupLayout);
     initGroupsTable();
   }
   
   protected void initGroupTitle(HorizontalLayout groupHeader) {
     Label groupsTitle = new Label(this.i18nManager.getMessage("user.header.groups"));
     groupsTitle.addStyleName("h3");
     groupHeader.addComponent(groupsTitle);
   }
   
   protected void initAddGroupsButton(HorizontalLayout groupHeader) {
     Button addRelatedContentButton = new Button();
     addRelatedContentButton.addStyleName("add");
     groupHeader.addComponent(addRelatedContentButton);
     groupHeader.setComponentAlignment(addRelatedContentButton, Alignment.MIDDLE_RIGHT);
     
     addRelatedContentButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         final GroupSelectionPopupWindow selectionPopup = new GroupSelectionPopupWindow(UserDetailPanel.this.identityService, UserDetailPanel.this.user.getId());
         selectionPopup.addListener(new SubmitEventListener() {
           private static final long serialVersionUID = 1L;
           
           protected void submitted(SubmitEvent event) { Set<String> selectedGroups = selectionPopup.getSelectedGroupIds();
             if (!selectedGroups.isEmpty()) {
               for (String groupId : selectedGroups) {
                 UserDetailPanel.this.identityService.createMembership(UserDetailPanel.this.user.getId(), groupId);
               }
               UserDetailPanel.this.notifyMembershipChanged();
             }
           }
           
           protected void cancelled(SubmitEvent event) {}
         });
         ExplorerApp.get().getViewManager().showPopupWindow(selectionPopup);
       }
     });
   }
   
   protected void initGroupsTable() {
     this.groupsForUserQuery = new GroupsForUserQuery(this.identityService, this, this.user.getId());
     if (this.groupsForUserQuery.size() > 0) {
       this.groupTable = new Table();
       this.groupTable.setSortDisabled(true);
       this.groupTable.setHeight(150.0F, 0);
       this.groupTable.setWidth(100.0F, 8);
       this.groupLayout.addComponent(this.groupTable);
       
       this.groupContainer = new LazyLoadingContainer(this.groupsForUserQuery, 30);
       this.groupTable.setContainerDataSource(this.groupContainer);
       
       this.groupTable.addContainerProperty("id", Button.class, null);
       this.groupTable.setColumnExpandRatio("id", 22.0F);
       this.groupTable.addContainerProperty("name", String.class, null);
       this.groupTable.setColumnExpandRatio("name", 45.0F);
       this.groupTable.addContainerProperty("type", String.class, null);
       this.groupTable.setColumnExpandRatio("type", 22.0F);
       this.groupTable.addContainerProperty("actions", Component.class, null);
       this.groupTable.setColumnExpandRatio("actions", 11.0F);
       this.groupTable.setColumnAlignment("actions", "c");
     }
     else {
       this.noGroupsLabel = new Label(this.i18nManager.getMessage("user.no.groups"));
       this.groupLayout.addComponent(this.noGroupsLabel);
     }
   }
   
   public void notifyMembershipChanged() {
     this.groupLayout.removeAllComponents();
     initGroupsTable();
   }
 }


