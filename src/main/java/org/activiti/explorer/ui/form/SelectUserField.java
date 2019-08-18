 package org.activiti.explorer.ui.form;
 
 import com.vaadin.data.Buffered.SourceException;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ConversionException;
 import com.vaadin.data.Property.ReadOnlyException;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.data.Validator;
 import com.vaadin.data.Validator.InvalidValueException;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Field;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.TextField;
 import java.util.Collection;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.SelectUsersPopupWindow;
 import org.activiti.explorer.ui.event.SubmitEvent;
 import org.activiti.explorer.ui.event.SubmitEventListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SelectUserField
   extends HorizontalLayout
   implements Field
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected TextField wrappedField;
   protected Label selectedUserLabel;
   protected Button selectUserButton;
   protected User selectedUser;
   
   public SelectUserField(String caption)
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     setSpacing(true);
     setCaption(caption);
     
     this.selectedUserLabel = new Label();
     this.selectedUserLabel.setValue(this.i18nManager.getMessage("form.user.no.user.selected"));
     this.selectedUserLabel.addStyleName("formprop-no-user-selected");
     addComponent(this.selectedUserLabel);
     
     this.selectUserButton = new Button();
     this.selectUserButton.addStyleName("small");
     this.selectUserButton.setCaption(this.i18nManager.getMessage("form.user.select"));
     addComponent(this.selectUserButton);
     
     this.selectUserButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         final SelectUsersPopupWindow window = new SelectUsersPopupWindow(SelectUserField.this.i18nManager.getMessage("form.user.select"), false);
         window.addListener(new SubmitEventListener()
         {
           private static final long serialVersionUID = 1L;
           
           protected void submitted(SubmitEvent event) {
             String userId = window.getSelectedUserId();
             SelectUserField.this.setValue(userId);
           }
           
 
 
           protected void cancelled(SubmitEvent event) {}
         });
         ExplorerApp.get().getViewManager().showPopupWindow(window);
       }
       
 
     });
     this.wrappedField = new TextField();
     this.wrappedField.setVisible(false);
     addComponent(this.wrappedField);
   }
   
   public boolean isInvalidCommitted() {
     return this.wrappedField.isInvalidCommitted();
   }
   
   public void setInvalidCommitted(boolean isCommitted) {
     this.wrappedField.setInvalidCommitted(isCommitted);
   }
   
   public void commit() throws SourceException, Validator.InvalidValueException {
     this.wrappedField.commit();
   }
   
   public void discard() throws SourceException {
     this.wrappedField.discard();
   }
   
   public boolean isWriteThrough() {
     return this.wrappedField.isWriteThrough();
   }
   
   public void setWriteThrough(boolean writeThrough) throws SourceException, Validator.InvalidValueException {
     this.wrappedField.setWriteThrough(true);
   }
   
   public boolean isReadThrough() {
     return this.wrappedField.isReadThrough();
   }
   
   public void setReadThrough(boolean readThrough) throws SourceException {
     this.wrappedField.setReadThrough(readThrough);
   }
   
   public boolean isModified() {
     return this.wrappedField.isModified();
   }
   
   public void addValidator(Validator validator) {
     this.wrappedField.addValidator(validator);
   }
   
   public void removeValidator(Validator validator) {
     this.wrappedField.removeValidator(validator);
   }
   
   public Collection<Validator> getValidators() {
     return this.wrappedField.getValidators();
   }
   
   public boolean isValid() {
     return this.wrappedField.isValid();
   }
   
   public void validate() throws Validator.InvalidValueException {
     this.wrappedField.validate();
   }
   
   public boolean isInvalidAllowed() {
     return this.wrappedField.isInvalidAllowed();
   }
   
   public void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {
     this.wrappedField.setInvalidAllowed(invalidValueAllowed);
   }
   
   public Object getValue() {
     return this.wrappedField.getValue();
   }
   
   public void setValue(Object newValue) throws Property.ReadOnlyException, Property.ConversionException {
     this.wrappedField.setValue(newValue);
     
 
     if (newValue != null) {
       if ((this.selectedUser == null) || (!this.selectedUser.getId().equals(newValue))) {
         this.selectedUser = ((User)ProcessEngines.getDefaultProcessEngine().getIdentityService().createUserQuery().userId((String)newValue).singleResult());
       }
       this.selectedUserLabel.setValue(getSelectedUserLabel());
       this.selectedUserLabel.addStyleName("formprop-user-selected");
       this.selectedUserLabel.removeStyleName("formprop-no-user-selected");
     } else {
       this.selectedUser = null;
       this.selectedUserLabel.setValue(this.i18nManager.getMessage("form.user.no.user.selected"));
       this.selectedUserLabel.addStyleName("formprop-no-user-selected");
       this.selectedUserLabel.removeStyleName("formprop-user-selected");
     }
   }
   
   protected Object getSelectedUserLabel() {
     if (this.selectedUser != null) {
       return this.selectedUser.getFirstName() + " " + this.selectedUser.getLastName();
     }
     return this.wrappedField.getValue();
   }
   
   public Class<?> getType()
   {
     return this.wrappedField.getType();
   }
   
   public void addListener(Property.ValueChangeListener listener) {
     this.wrappedField.addListener(listener);
   }
   
   public void removeListener(Property.ValueChangeListener listener) {
     this.wrappedField.removeListener(listener);
   }
   
   public void valueChange(Property.ValueChangeEvent event) {
     this.wrappedField.valueChange(event);
   }
   
   public void setPropertyDataSource(Property newDataSource) {
     this.wrappedField.setPropertyDataSource(newDataSource);
   }
   
   public Property getPropertyDataSource() {
     return this.wrappedField.getPropertyDataSource();
   }
   
   public int getTabIndex() {
     return this.wrappedField.getTabIndex();
   }
   
   public void setTabIndex(int tabIndex) {
     this.wrappedField.setTabIndex(tabIndex);
   }
   
   public boolean isRequired() {
     return this.wrappedField.isRequired();
   }
   
   public void setRequired(boolean required) {
     this.wrappedField.setRequired(required);
   }
   
   public void setRequiredError(String requiredMessage) {
     this.wrappedField.setRequiredError(requiredMessage);
   }
   
   public String getRequiredError() {
     return this.wrappedField.getRequiredError();
   }
   
   public void focus()
   {
     this.wrappedField.focus();
   }
 }


