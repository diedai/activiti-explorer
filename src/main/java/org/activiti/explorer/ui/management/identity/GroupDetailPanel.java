 package org.activiti.explorer.ui.management.identity;
 
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.ComboBox;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.TextField;
 import com.vaadin.ui.VerticalLayout;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collection;
 import java.util.List;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.GroupQuery;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.ConfirmationDialogPopupWindow;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.SelectUsersPopupWindow;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.event.ConfirmationEvent;
 import org.activiti.explorer.ui.event.ConfirmationEventListener;
 import org.activiti.explorer.ui.event.SubmitEvent;
 import org.activiti.explorer.ui.event.SubmitEventListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class GroupDetailPanel
   extends DetailPanel
   implements MemberShipChangeListener
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected I18nManager i18nManager;
   protected GroupPage groupPage;
   protected Group group;
   protected VerticalLayout panelLayout;
   protected boolean editingDetails;
   protected HorizontalLayout detailLayout;
   protected GridLayout detailsGrid;
   protected TextField nameTextField;
   protected ComboBox typeCombobox;
   protected HorizontalLayout membersLayout;
   protected Table membersTable;
   protected Label noMembersTable;
   
   public GroupDetailPanel(GroupPage groupPage, String groupId)
   {
     this.groupPage = groupPage;
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.group = ((Group)this.identityService.createGroupQuery().groupId(groupId).singleResult());
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     init();
   }
   
   protected void init() {
     setSizeFull();
     addStyleName("light");
     
     initPageTitle();
     initGroupDetails();
     initMembers();
     
     initActions();
   }
   
   protected void initActions() {
     Button createGroupButton = new Button(this.i18nManager.getMessage("group.create"));
     createGroupButton.setIcon(Images.GROUP_16);
     createGroupButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         NewGroupPopupWindow popup = new NewGroupPopupWindow();
         ExplorerApp.get().getViewManager().showPopupWindow(popup);
       }
     });
     this.groupPage.getToolBar().removeAllButtons();
     this.groupPage.getToolBar().addButton(createGroupButton);
   }
   
   protected void initPageTitle() {
     HorizontalLayout layout = new HorizontalLayout();
     layout.setWidth(100.0F, 8);
     layout.addStyleName("title-block");
     layout.setSpacing(true);
     layout.setMargin(false, false, true, false);
     addDetailComponent(layout);
     
     Embedded groupImage = new Embedded(null, Images.GROUP_50);
     layout.addComponent(groupImage);
     
     Label groupName = new Label(getGroupName(this.group));
     groupName.setSizeUndefined();
     groupName.addStyleName("h2");
     layout.addComponent(groupName);
     layout.setComponentAlignment(groupName, Alignment.MIDDLE_LEFT);
     layout.setExpandRatio(groupName, 1.0F);
   }
   
   protected String getGroupName(Group theGroup) {
     if (theGroup.getName() == null) {
       return theGroup.getId();
     }
     return this.group.getName();
   }
   
   protected void initGroupDetails() {
     Label groupDetailsHeader = new Label(this.i18nManager.getMessage("group.header.details"));
     groupDetailsHeader.addStyleName("h3");
     groupDetailsHeader.addStyleName("block-holder");
     
     addDetailComponent(groupDetailsHeader);
     
     this.detailLayout = new HorizontalLayout();
     this.detailLayout.setSpacing(true);
     this.detailLayout.setMargin(true, false, true, false);
     addDetailComponent(this.detailLayout);
     
     populateGroupDetails();
   }
   
   protected void populateGroupDetails() {
     initGroupProperties();
     initGroupActions();
   }
   
   protected void initGroupProperties() {
     this.detailsGrid = new GridLayout(2, 3);
     this.detailsGrid.setSpacing(true);
     this.detailLayout.setMargin(true, true, true, false);
     this.detailLayout.addComponent(this.detailsGrid);
     
 
     Label idLabel = new Label(this.i18nManager.getMessage("group.id") + ": ");
     idLabel.addStyleName("bold");
     this.detailsGrid.addComponent(idLabel);
     Label idValueLabel = new Label(this.group.getId());
     this.detailsGrid.addComponent(idValueLabel);
     
 
     Label nameLabel = new Label(this.i18nManager.getMessage("group.name") + ": ");
     nameLabel.addStyleName("bold");
     this.detailsGrid.addComponent(nameLabel);
     if (!this.editingDetails) {
       Label nameValueLabel = new Label(this.group.getName());
       this.detailsGrid.addComponent(nameValueLabel);
     } else {
       this.nameTextField = new TextField(null, this.group.getName());
       this.detailsGrid.addComponent(this.nameTextField);
     }
     
 
     Label typeLabel = new Label(this.i18nManager.getMessage("group.type") + ": ");
     typeLabel.addStyleName("bold");
     this.detailsGrid.addComponent(typeLabel);
     if (!this.editingDetails) {
       Label typeValueLabel = new Label(this.group.getType());
       this.detailsGrid.addComponent(typeValueLabel);
     } else {
       this.typeCombobox = new ComboBox(null, Arrays.asList(new String[] { "assignment", "security-role" }));
       this.typeCombobox.setNullSelectionAllowed(false);
       this.typeCombobox.setInvalidAllowed(false);
       this.typeCombobox.select(this.group.getType());
       this.detailsGrid.addComponent(this.typeCombobox);
     }
   }
   
   protected void initGroupActions() {
     VerticalLayout actionsLayout = new VerticalLayout();
     actionsLayout.setSpacing(true);
     actionsLayout.setMargin(false, false, false, true);
     this.detailLayout.addComponent(actionsLayout);
     
     if (this.editingDetails) {
       initSaveButton(actionsLayout);
     } else {
       initEditButton(actionsLayout);
       initDeleteButton(actionsLayout);
     }
   }
   
   protected void initEditButton(VerticalLayout actionsLayout) {
     Button editButton = new Button(this.i18nManager.getMessage("user.edit"));
     editButton.addStyleName("small");
     actionsLayout.addComponent(editButton);
     
     editButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         GroupDetailPanel.this.editingDetails = true;
         GroupDetailPanel.this.detailLayout.removeAllComponents();
         GroupDetailPanel.this.populateGroupDetails();
       }
     });
   }
   
   protected void initSaveButton(VerticalLayout actionsLayout) {
     Button saveButton = new Button(this.i18nManager.getMessage("user.save"));
     saveButton.addStyleName("small");
     actionsLayout.addComponent(saveButton);
     
     saveButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         String originalName = GroupDetailPanel.this.group.getName();
         
 
         if (GroupDetailPanel.this.nameTextField.getValue() != null) {
           GroupDetailPanel.this.group.setName(GroupDetailPanel.this.nameTextField.getValue().toString());
           GroupDetailPanel.this.group.setType(GroupDetailPanel.this.typeCombobox.getValue().toString());
         }
         GroupDetailPanel.this.identityService.saveGroup(GroupDetailPanel.this.group);
         
 
         GroupDetailPanel.this.editingDetails = false;
         GroupDetailPanel.this.detailLayout.removeAllComponents();
         GroupDetailPanel.this.populateGroupDetails();
         
 
         if (((originalName != null) && (!originalName.equals(GroupDetailPanel.this.group.getName()))) || ((originalName == null) && 
           (GroupDetailPanel.this.group.getName() != null))) {
           GroupDetailPanel.this.groupPage.notifyGroupChanged(GroupDetailPanel.this.group.getId());
         }
       }
     });
   }
   
   protected void initDeleteButton(VerticalLayout actionsLayout) {
     Button deleteButton = new Button(this.i18nManager.getMessage("group.delete"));
     deleteButton.addStyleName("small");
     actionsLayout.addComponent(deleteButton);
     
     deleteButton.addListener(new Button.ClickListener()
     {
       public void buttonClick(ClickEvent event) {
         ConfirmationDialogPopupWindow confirmPopup = new ConfirmationDialogPopupWindow(GroupDetailPanel.this.i18nManager.getMessage("group.confirm.delete", new Object[] { GroupDetailPanel.this.group.getId() }));
         confirmPopup.addListener(new ConfirmationEventListener()
         {
           protected void rejected(ConfirmationEvent event) {}
           
           protected void confirmed(ConfirmationEvent event) {
             GroupDetailPanel.this.identityService.deleteGroup(GroupDetailPanel.this.group.getId());
             
 
             GroupDetailPanel.this.groupPage.refreshSelectNext();
           }
           
         });
         ExplorerApp.get().getViewManager().showPopupWindow(confirmPopup);
       }
     });
   }
   
   protected void initMembers() {
     HorizontalLayout membersHeader = new HorizontalLayout();
     membersHeader.setSpacing(true);
     membersHeader.setWidth(100.0F, 8);
     membersHeader.addStyleName("block-holder");
     addDetailComponent(membersHeader);
     
     initMembersTitle(membersHeader);
     initAddMembersButton(membersHeader);
     
     this.membersLayout = new HorizontalLayout();
     this.membersLayout.setWidth(100.0F, 8);
     addDetailComponent(this.membersLayout);
     initMembersTable();
   }
   
   protected void initMembersTitle(HorizontalLayout membersHeader) {
     Label usersHeader = new Label(this.i18nManager.getMessage("group.header.users"));
     usersHeader.addStyleName("h3");
     membersHeader.addComponent(usersHeader);
   }
   
   protected void initAddMembersButton(HorizontalLayout membersHeader) {
     Button addButton = new Button();
     addButton.addStyleName("add");
     membersHeader.addComponent(addButton);
     membersHeader.setComponentAlignment(addButton, Alignment.MIDDLE_RIGHT);
     
     addButton.addListener(new Button.ClickListener()
     {
       public void buttonClick(ClickEvent event)
       {
         final SelectUsersPopupWindow selectUsersPopup = new SelectUsersPopupWindow(GroupDetailPanel.this.i18nManager.getMessage("group.select.members", new Object[] { GroupDetailPanel.this.group.getId() }), true, false, GroupDetailPanel.this.getCurrentMembers());
         ExplorerApp.get().getViewManager().showPopupWindow(selectUsersPopup);
         
 
         selectUsersPopup.addListener(new SubmitEventListener() {
           protected void submitted(SubmitEvent event) {
             Collection<String> userIds = selectUsersPopup.getSelectedUserIds();
             if (!userIds.isEmpty()) {
               for (String userId : userIds) {
                 GroupDetailPanel.this.identityService.createMembership(userId, GroupDetailPanel.this.group.getId());
               }
               GroupDetailPanel.this.notifyMembershipChanged();
             }
           }
           
           protected void cancelled(SubmitEvent event) {}
         });
       }
     });
   }
   
   protected List<String> getCurrentMembers()
   {
     List<User> users = this.identityService.createUserQuery().memberOfGroup(this.group.getId()).list();
     List<String> userIds = new ArrayList();
     for (User user : users) {
       userIds.add(user.getId());
     }
     return userIds;
   }
   
   protected void initMembersTable() {
     LazyLoadingQuery query = new GroupMembersQuery(this.group.getId(), this);
     if (query.size() > 0) {
       this.membersTable = new Table();
       this.membersTable.setWidth(100.0F, 8);
       this.membersTable.setHeight(400.0F, 0);
       
       this.membersTable.setEditable(false);
       this.membersTable.setSelectable(false);
       this.membersTable.setSortDisabled(false);
       
       LazyLoadingContainer container = new LazyLoadingContainer(query, 30);
       this.membersTable.setContainerDataSource(container);
       
       this.membersTable.addContainerProperty("id", Button.class, null);
       this.membersTable.addContainerProperty("firstName", String.class, null);
       this.membersTable.addContainerProperty("lastName", String.class, null);
       this.membersTable.addContainerProperty("email", String.class, null);
       this.membersTable.addContainerProperty("actions", Component.class, null);
       
       this.membersLayout.addComponent(this.membersTable);
     } else {
       this.noMembersTable = new Label(this.i18nManager.getMessage("group.no.members"));
       this.membersLayout.addComponent(this.noMembersTable);
     }
   }
   
   public void notifyMembershipChanged() {
     this.membersLayout.removeAllComponents();
     initMembersTable();
   }
 }


