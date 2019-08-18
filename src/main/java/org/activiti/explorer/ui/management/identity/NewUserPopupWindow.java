 package org.activiti.explorer.ui.management.identity;
 
 import com.vaadin.data.Validator;
 import com.vaadin.data.Validator.InvalidValueException;
 import com.vaadin.data.validator.StringLengthValidator;
 import com.vaadin.event.Action;
 import com.vaadin.event.Action.Handler;
 import com.vaadin.event.ShortcutAction;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Field;
 import com.vaadin.ui.Form;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Layout;
 import com.vaadin.ui.PasswordField;
 import com.vaadin.ui.TextField;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.PopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class NewUserPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected I18nManager i18nManager;
   protected Form form;
   
   public NewUserPopupWindow()
   {
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     setCaption(this.i18nManager.getMessage("user.create"));
     setModal(true);
     center();
     setResizable(false);
     setWidth(275.0F, 0);
     setHeight(300.0F, 0);
     addStyleName("light");
     
     initEnterKeyListener();
     initForm();
   }
   
   protected void initEnterKeyListener() {
     addActionHandler(new Action.Handler() {
       public void handleAction(Action action, Object sender, Object target) {
         NewUserPopupWindow.this.handleFormSubmit();
       }
       
       public Action[] getActions(Object target, Object sender) { return new Action[] { new ShortcutAction("enter", 13, null) }; }
     });
   }
   
   protected void initForm()
   {
     this.form = new Form();
     this.form.setValidationVisibleOnCommit(true);
     this.form.setImmediate(true);
     addComponent(this.form);
     
     initInputFields();
     initCreateButton();
   }
   
   protected void initInputFields()
   {
     this.form.addField("id", new TextField(this.i18nManager.getMessage("user.id")));
     
 
     this.form.getField("id").setRequired(true);
     this.form.getField("id").setRequiredError(this.i18nManager.getMessage("user.id.required"));
     this.form.getField("id").focus();
     
 
     this.form.getField("id").addValidator(new Validator() {
       public void validate(Object value) throws Validator.InvalidValueException {
         if (!isValid(value))
           throw new Validator.InvalidValueException(NewUserPopupWindow.this.i18nManager.getMessage("user.id.unique"));
       }
       
       public boolean isValid(Object value) {
         if (value != null) {
           return NewUserPopupWindow.this.identityService.createUserQuery().userId(value.toString()).singleResult() == null;
         }
         return false;
       }
       
 
     });
     this.form.addField("password", new PasswordField(this.i18nManager.getMessage("user.password")));
     this.form.getField("password").setRequired(true);
     this.form.getField("password").setRequiredError(this.i18nManager.getMessage("user.password.required"));
     
 
     StringLengthValidator passwordLengthValidator = new StringLengthValidator(this.i18nManager.getMessage("user.password.min.lenth", new Object[] { Integer.valueOf(5) }), 5, -1, false);
     this.form.getField("password").addValidator(passwordLengthValidator);
     
     this.form.addField("firstName", new TextField(this.i18nManager.getMessage("user.firstname")));
     this.form.addField("lastName", new TextField(this.i18nManager.getMessage("user.lastname")));
     this.form.addField("email", new TextField(this.i18nManager.getMessage("user.email")));
   }
   
   protected void initCreateButton() {
     HorizontalLayout buttonLayout = new HorizontalLayout();
     buttonLayout.setWidth(100.0F, 8);
     this.form.getFooter().setWidth(100.0F, 8);
     this.form.getFooter().addComponent(buttonLayout);
     
     Button createButton = new Button(this.i18nManager.getMessage("user.create"));
     buttonLayout.addComponent(createButton);
     buttonLayout.setComponentAlignment(createButton, Alignment.BOTTOM_RIGHT);
     
     createButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         NewUserPopupWindow.this.handleFormSubmit();
       }
     });
   }
   
   protected void handleFormSubmit()
   {
     try {
       this.form.commit();
       User user = createUser();
       
 
       close();
       ExplorerApp.get().getViewManager().showUserPage(user.getId());
     }
     catch (InvalidValueException e) {
       setHeight(340.0F, 0);
     }
   }
   
   protected User createUser() {
     User user = this.identityService.newUser(this.form.getField("id").getValue().toString());
     user.setPassword(this.form.getField("password").getValue().toString());
     if (this.form.getField("firstName").getValue() != null) {
       user.setFirstName(this.form.getField("firstName").getValue().toString());
     }
     if (this.form.getField("lastName").getValue() != null) {
       user.setLastName(this.form.getField("lastName").getValue().toString());
     }
     if (this.form.getField("email").getValue() != null) {
       user.setEmail(this.form.getField("email").getValue().toString());
     }
     this.identityService.saveUser(user);
     return user;
   }
 }


