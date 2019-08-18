 package org.activiti.explorer.ui.management.identity;
 
 import com.vaadin.data.Validator;
 import com.vaadin.data.Validator.InvalidValueException;
 import com.vaadin.event.Action;
 import com.vaadin.event.Action.Handler;
 import com.vaadin.event.ShortcutAction;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.ComboBox;
 import com.vaadin.ui.Field;
 import com.vaadin.ui.Form;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Layout;
 import com.vaadin.ui.TextField;
 import java.util.Arrays;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.GroupQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.PopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class NewGroupPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected I18nManager i18nManager;
   protected Form form;
   
   public NewGroupPopupWindow()
   {
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     setCaption(this.i18nManager.getMessage("group.create"));
     setModal(true);
     center();
     setResizable(false);
     setWidth(265.0F, 0);
     setHeight(240.0F, 0);
     addStyleName("light");
     
     initEnterKeyListener();
     initForm();
   }
   
   protected void initEnterKeyListener() {
     addActionHandler(new Action.Handler() {
       public void handleAction(Action action, Object sender, Object target) {
         NewGroupPopupWindow.this.handleFormSubmit();
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
     this.form.addField("id", new TextField(this.i18nManager.getMessage("group.id")));
     
 
     this.form.getField("id").setRequired(true);
     this.form.getField("id").setRequiredError(this.i18nManager.getMessage("group.id.required"));
     this.form.getField("id").focus();
     
 
     this.form.getField("id").addValidator(new Validator() {
       public void validate(Object value) throws Validator.InvalidValueException {
         if (!isValid(value))
           throw new Validator.InvalidValueException(NewGroupPopupWindow.this.i18nManager.getMessage("group.id.unique"));
       }
       
       public boolean isValid(Object value) {
         if (value != null) {
           return NewGroupPopupWindow.this.identityService.createGroupQuery().groupId(value.toString()).singleResult() == null;
         }
         return false;
       }
       
     });
     this.form.addField("name", new TextField(this.i18nManager.getMessage("group.name")));
     
     ComboBox typeComboBox = new ComboBox(this.i18nManager.getMessage("group.type"), Arrays.asList(new String[] { "assignment", "security-role" }));
     typeComboBox.select("assignment");
     this.form.addField("type", typeComboBox);
   }
   
   protected void initCreateButton() {
     HorizontalLayout buttonLayout = new HorizontalLayout();
     buttonLayout.setWidth(100.0F, 8);
     this.form.getFooter().setWidth(100.0F, 8);
     this.form.getFooter().addComponent(buttonLayout);
     
     Button createButton = new Button(this.i18nManager.getMessage("group.create"));
     buttonLayout.addComponent(createButton);
     buttonLayout.setComponentAlignment(createButton, Alignment.BOTTOM_RIGHT);
     
     createButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         NewGroupPopupWindow.this.handleFormSubmit();
       }
     });
   }
   
   protected void handleFormSubmit()
   {
     try {
       this.form.commit();
       Group group = createGroup();
       
 
       close();
       ExplorerApp.get().getViewManager().showGroupPage(group.getId());
     }
     catch (InvalidValueException e)
     {
       setHeight(270.0F, 0);
     }
   }
   
   protected Group createGroup() {
     Group group = this.identityService.newGroup(this.form.getField("id").getValue().toString());
     group.setName(this.form.getField("name").getValue().toString());
     group.setType(this.form.getField("type").getValue().toString());
     this.identityService.saveGroup(group);
     return group;
   }
 }


