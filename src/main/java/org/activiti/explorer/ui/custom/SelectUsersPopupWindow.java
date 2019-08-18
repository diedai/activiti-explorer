 package org.activiti.explorer.ui.custom;
 
 import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.identity.LoggedInUser;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.event.SubmitEvent;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SelectUsersPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected String title;
   protected boolean multiSelect = true;
   protected boolean showRoles = true;
   
   protected Collection<String> ignoredUserIds;
   protected I18nManager i18nManager;
   protected VerticalLayout windowLayout;
   protected TextField searchField;
   protected HorizontalLayout userSelectionLayout;
   protected Table matchingUsersTable;
   protected Button selectUserButton;
   protected Table selectedUsersTable;
   protected Button doneButton;
   
   public SelectUsersPopupWindow(String title, boolean multiSelect)
   {
     this.title = title;
     this.multiSelect = multiSelect;
     this.i18nManager = ExplorerApp.get().getI18nManager();
   }
   
   public SelectUsersPopupWindow(String title, boolean multiSelect, Collection<String> ignoredUserIds) {
     this(title, multiSelect);
     this.ignoredUserIds = ignoredUserIds;
   }
   
   public SelectUsersPopupWindow(String title, boolean multiSelect, boolean showRoles, Collection<String> ignoredUserIds) {
     this(title, multiSelect);
     this.showRoles = showRoles;
     this.ignoredUserIds = ignoredUserIds;
   }
   
   public void attach()
   {
     super.attach();
     initUi();
   }
   
   protected void initUi() {
     setCaption(this.title);
     setModal(true);
     addStyleName("light");
     center();
     
     this.windowLayout = ((VerticalLayout)getContent());
     this.windowLayout.setSpacing(true);
     
     if ((this.multiSelect) && (this.showRoles)) {
       setWidth(820.0F, 0);
     } else if ((this.multiSelect) && (!this.showRoles)) {
       setWidth(685.0F, 0);
     } else {
       setWidth(340.0F, 0);
     }
     setHeight(350.0F, 0);
     
     initSearchField();
     initUserSelection();
     initDoneButton();
   }
   
   protected void initSearchField() {
     HorizontalLayout searchLayout = new HorizontalLayout();
     searchLayout.setSpacing(true);
     addComponent(searchLayout);
     
 
     this.searchField = new TextField();
     this.searchField.setInputPrompt(this.i18nManager.getMessage("people.search"));
     this.searchField.setWidth(180.0F, 0);
     this.searchField.focus();
     this.searchField.setTextChangeEventMode(TextChangeEventMode.EAGER);
     searchLayout.addComponent(this.searchField);
     
 
     this.searchField.addListener(new FieldEvents.TextChangeListener() {
       public void textChange(FieldEvents.TextChangeEvent event) {
         SelectUsersPopupWindow.this.searchPeople(event.getText());
       }
       
     });
     initSelectMyselfButton(searchLayout);
   }
   
   protected void initSelectMyselfButton(HorizontalLayout searchLayout) {
     final LoggedInUser loggedInUser = ExplorerApp.get().getLoggedInUser();
     if ((this.ignoredUserIds == null) || (!this.ignoredUserIds.contains(loggedInUser.getId()))) {
       Button meButton = new Button(this.i18nManager.getMessage("people.select.myself"));
       meButton.setIcon(Images.USER_16);
       searchLayout.addComponent(meButton);
       searchLayout.setComponentAlignment(meButton, Alignment.MIDDLE_LEFT);
       
       if (this.multiSelect) {
         meButton.addListener(new Button.ClickListener() {
           public void buttonClick(ClickEvent event) {
             SelectUsersPopupWindow.this.selectUser(loggedInUser.getId(), loggedInUser.getFullName());
           }

		@Override
		public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
         });
       } else {
         meButton.addListener(new Button.ClickListener() {
           public void buttonClick(ClickEvent event) {
             SelectUsersPopupWindow.this.addMatchingUser(loggedInUser.getId(), loggedInUser.getFullName());
             SelectUsersPopupWindow.this.matchingUsersTable.select(loggedInUser.getId());
             SelectUsersPopupWindow.this.fireEvent(new SubmitEvent(SelectUsersPopupWindow.this.doneButton, "submit"));
             SelectUsersPopupWindow.this.close();
           }

		@Override
		public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
         });
       }
     }
   }
   
   protected void searchPeople(String searchText) {
     if (searchText.length() >= 2) {
       this.matchingUsersTable.removeAllItems();
       
 
 
 
 
       List<User> results = ProcessEngines.getDefaultProcessEngine().getIdentityService().createUserQuery().userFullNameLike("%" + searchText + "%").list();
       
       for (User user : results) {
         if (((!this.multiSelect) || (!this.selectedUsersTable.containsId(user.getId()))) && (
           (this.ignoredUserIds == null) || (!this.ignoredUserIds.contains(user.getId())))) {
           addMatchingUser(user.getId(), user.getFirstName() + " " + user.getLastName());
         }
       }
     }
   }
   
   protected void addMatchingUser(String userId, String name)
   {
     if (!this.matchingUsersTable.containsId(userId)) {
       Item item = this.matchingUsersTable.addItem(userId);
       item.getItemProperty("userName").setValue(name);
     }
   }
   
   protected void initUserSelection() {
     this.userSelectionLayout = new HorizontalLayout();
     this.userSelectionLayout.setSpacing(true);
     addComponent(this.userSelectionLayout);
     
     initMatchingUsersTable();
     
 
 
     if (this.multiSelect) {
       initSelectUserButton();
       initSelectedUsersTable();
     }
   }
   
   protected void initMatchingUsersTable() {
     this.matchingUsersTable = new Table();
     this.matchingUsersTable.setColumnHeaderMode(-1);
     this.matchingUsersTable.setSelectable(true);
     this.matchingUsersTable.setEditable(false);
     this.matchingUsersTable.setImmediate(true);
     this.matchingUsersTable.setNullSelectionAllowed(false);
     this.matchingUsersTable.setSortDisabled(true);
     
     if (this.multiSelect) {
       this.matchingUsersTable.setMultiSelect(true);
     }
     
     this.matchingUsersTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.USER_16));
     this.matchingUsersTable.setColumnWidth("icon", 16);
     this.matchingUsersTable.addContainerProperty("userName", String.class, null);
     
     this.matchingUsersTable.setWidth(300.0F, 0);
     this.matchingUsersTable.setHeight(200.0F, 0);
     this.userSelectionLayout.addComponent(this.matchingUsersTable);
   }
   
   protected void initSelectUserButton() {
     this.selectUserButton = new Button(">");
     
     this.selectUserButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         for (String selectedItemId : (Set)SelectUsersPopupWindow.this.matchingUsersTable.getValue())
         {
           Item originalItem = SelectUsersPopupWindow.this.matchingUsersTable.getItem(selectedItemId);
           
 
           SelectUsersPopupWindow.this.selectUser(selectedItemId, (String)originalItem.getItemProperty("userName").getValue());
           
 
           SelectUsersPopupWindow.this.matchingUsersTable.removeItem(selectedItemId);
         }
         
       }

	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		// TODO Auto-generated method stub
		
	}
     });
     this.userSelectionLayout.addComponent(this.selectUserButton);
     this.userSelectionLayout.setComponentAlignment(this.selectUserButton, Alignment.MIDDLE_CENTER);
   }
   
   protected void initSelectedUsersTable() {
     this.selectedUsersTable = new Table();
     this.selectedUsersTable.setColumnHeaderMode(-1);
     this.selectedUsersTable.setEditable(false);
     this.selectedUsersTable.setSortDisabled(true);
     
 
     this.selectedUsersTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.USER_ADD));
     this.selectedUsersTable.setColumnWidth("icon", 16);
     
 
     this.selectedUsersTable.addContainerProperty("userName", String.class, null);
     
 
     if (this.showRoles) {
       this.selectedUsersTable.addContainerProperty("role", ComboBox.class, null);
     }
     
 
     this.selectedUsersTable.addGeneratedColumn("delete", new ThemeImageColumnGenerator(Images.DELETE, new MouseEvents.ClickListener()
     {
       public void click(ClickEvent event) {
         Object itemId = ((Embedded)event.getSource()).getData();
         
 
         String searchFieldValue = (String)SelectUsersPopupWindow.this.searchField.getValue();
         if ((searchFieldValue != null) && (searchFieldValue.length() >= 2)) {
           String userName = (String)SelectUsersPopupWindow.this.selectedUsersTable.getItem(itemId).getItemProperty("userName").getValue();
           if (SelectUsersPopupWindow.this.matchesSearchField(userName)) {
             Item item = SelectUsersPopupWindow.this.matchingUsersTable.addItem(itemId);
             item.getItemProperty("userName").setValue(userName);
           }
         }
         
 
         SelectUsersPopupWindow.this.selectedUsersTable.removeItem(itemId);
       }
     }));
     this.selectedUsersTable.setColumnWidth("icon", 16);
     
     if (this.showRoles) {
       this.selectedUsersTable.setWidth(420.0F, 0);
     } else {
       this.selectedUsersTable.setWidth(300.0F, 0);
     }
     this.selectedUsersTable.setHeight(200.0F, 0);
     this.userSelectionLayout.addComponent(this.selectedUsersTable);
   }
   
   protected boolean matchesSearchField(String text) {
     for (String userNameToken : text.split(" ")) {
       if (userNameToken.toLowerCase().startsWith(((String)this.searchField.getValue()).toLowerCase())) {
         return true;
       }
     }
     return false;
   }
   
   protected void selectUser(String userId, String userName) {
     if (!this.selectedUsersTable.containsId(userId)) {
       Item item = this.selectedUsersTable.addItem(userId);
       item.getItemProperty("userName").setValue(userName);
       
       if (this.showRoles) {
         ComboBox comboBox = new ComboBox(null, Arrays.asList(new String[] {this.i18nManager
           .getMessage("task.role.contributor"), this.i18nManager
           .getMessage("task.role.implementer"), this.i18nManager
           .getMessage("task.role.manager"), this.i18nManager
           .getMessage("task.role.sponsor") }));
         comboBox.select(this.i18nManager.getMessage("task.role.contributor"));
         comboBox.setNewItemsAllowed(true);
         item.getItemProperty("role").setValue(comboBox);
       }
     }
   }
   
   protected void initDoneButton() {
     this.doneButton = new Button("Done");
     
     this.doneButton.addListener(new Button.ClickListener()
     {
       public void buttonClick(ClickEvent event) {
         SelectUsersPopupWindow.this.fireEvent(new SubmitEvent(SelectUsersPopupWindow.this.doneButton, "submit"));
         
 
         SelectUsersPopupWindow.this.close();
       }

	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		// TODO Auto-generated method stub
		
	}
       
     });
     addComponent(this.doneButton);
     this.windowLayout.setComponentAlignment(this.doneButton, Alignment.MIDDLE_RIGHT);
   }
   
   public String getSelectedUserId() {
     if (this.multiSelect) {
       throw new ActivitiException("Only use getSelectedUserId in non-multiselect mode");
     }
     return (String)this.matchingUsersTable.getValue();
   }
   
   public Collection<String> getSelectedUserIds()
   {
     if (!this.multiSelect) {
       throw new ActivitiException("Only use getSelectedUserIds in multiselect mode");
     }
     return (Collection<String>) this.selectedUsersTable.getItemIds();
   }
   
   public String getSelectedUserRole(String userId) {
     if (!this.multiSelect) {
       throw new ActivitiException("Only use getSelectedUserIds in multiselect mode");
     }
     return (String)((ComboBox)this.selectedUsersTable.getItem(userId).getItemProperty("role").getValue()).getValue();
   }
 }


