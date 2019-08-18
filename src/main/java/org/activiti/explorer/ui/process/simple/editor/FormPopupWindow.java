 package org.activiti.explorer.ui.process.simple.editor;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.CheckBox;
 import com.vaadin.ui.ComboBox;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import com.vaadin.ui.Window;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.process.simple.editor.table.PropertyTable;
 import org.activiti.explorer.ui.process.simple.editor.table.TaskFormModel;
 import org.activiti.workflow.simple.definition.form.DatePropertyDefinition;
 import org.activiti.workflow.simple.definition.form.FormDefinition;
 import org.activiti.workflow.simple.definition.form.FormPropertyDefinition;
 import org.activiti.workflow.simple.definition.form.NumberPropertyDefinition;
 import org.activiti.workflow.simple.definition.form.TextPropertyDefinition;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class FormPopupWindow
   extends Window
 {
   protected static final long serialVersionUID = -1754225937375971709L;
   protected static final String TITLE = "Define form";
   protected static final String DESCRIPTION = "Define the form properties that will be shown with the task";
   protected Object taskItemId;
   protected TaskFormModel formModel;
   protected PropertyTable propertyTable;
   
   public FormPopupWindow(Object taskItemId, TaskFormModel formModel)
   {
     this.taskItemId = taskItemId;
     this.formModel = formModel;
     
     setModal(true);
     setWidth("50%");
     center();
     setCaption("Define form");
     
     initUi();
   }
   
   protected void initUi() {
     VerticalLayout layout = new VerticalLayout();
     layout.setSpacing(true);
     addComponent(layout);
     
 
     layout.addComponent(new Label("Define the form properties that will be shown with the task"));
     
 
     this.propertyTable = new PropertyTable();
     layout.addComponent(this.propertyTable);
     fillFormFields();
     
 
     HorizontalLayout buttons = new HorizontalLayout();
     buttons.setSpacing(true);
     
 
     Button saveButton = new Button(ExplorerApp.get().getI18nManager().getMessage("button.save"));
     buttons.addComponent(saveButton);
     saveButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = -2906886872414089331L;
       
       public void buttonClick(ClickEvent event) {
         FormDefinition form = FormPopupWindow.this.createForm();
         FormPopupWindow.this.formModel.addForm(FormPopupWindow.this.taskItemId, form);
         FormPopupWindow.this.close();
       }
       
 
     });
     Button deleteButton = new Button(ExplorerApp.get().getI18nManager().getMessage("button.delete"));
     buttons.addComponent(deleteButton);
     deleteButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 5267967369680365653L;
       
       public void buttonClick(ClickEvent event) {
         FormPopupWindow.this.formModel.removeForm(FormPopupWindow.this.taskItemId);
         FormPopupWindow.this.close();
       }
       
     });
     layout.addComponent(new Label(""));
     layout.addComponent(buttons);
   }
   
   public FormDefinition createForm() {
     FormDefinition formDefinition = new FormDefinition();
     for (Object itemId : this.propertyTable.getItemIds())
     {
       Item item = this.propertyTable.getItem(itemId);
       FormPropertyDefinition formPropertyDefinition = getFormPropertyDefinition(item);
       formDefinition.addFormProperty(formPropertyDefinition);
     }
     return formDefinition;
   }
   
   protected FormPropertyDefinition getFormPropertyDefinition(Item item) {
     String type = (String)((ComboBox)item.getItemProperty("type").getValue()).getValue();
     
     FormPropertyDefinition result = null;
     if (type.equals("number")) {
       result = new NumberPropertyDefinition();
     } else if (type.equals("date")) {
       result = new DatePropertyDefinition();
     } else {
       result = new TextPropertyDefinition();
     }
     
 
     result.setName((String)item.getItemProperty("property").getValue());
     result.setMandatory(((Boolean)((CheckBox)item.getItemProperty("required").getValue()).getValue()).booleanValue());
     
     return result;
   }
   
   protected void fillFormFields() {
     FormDefinition form = this.formModel.getForm(this.taskItemId);
     if (form == null) {
       this.propertyTable.addPropertyRow();
     } else {
       for (FormPropertyDefinition property : form.getFormPropertyDefinitions()) {
         this.propertyTable.addPropertyRow(property.getName(), getPropertyTypeDisplay(property), Boolean.valueOf(property.isMandatory()));
       }
     }
   }
   
   protected String getPropertyTypeDisplay(FormPropertyDefinition definition) {
     if ((definition instanceof NumberPropertyDefinition))
       return "number";
     if ((definition instanceof DatePropertyDefinition)) {
       return "date";
     }
     
     return "text";
   }
 }


