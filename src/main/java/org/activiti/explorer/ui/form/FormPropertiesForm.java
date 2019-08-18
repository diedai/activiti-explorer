 package org.activiti.explorer.ui.form;
 
 import com.vaadin.data.Validator.InvalidValueException;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Component.Event;
 import com.vaadin.ui.ComponentContainer;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import java.util.List;
 import java.util.Map;
 import org.activiti.engine.FormService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class FormPropertiesForm
   extends VerticalLayout
 {
   private static final long serialVersionUID = -3197331726904715949L;
   protected transient FormService formService;
   protected I18nManager i18nManager;
   protected Label formTitle;
   protected Button submitFormButton;
   protected Button cancelFormButton;
   protected FormPropertiesComponent formPropertiesComponent;
   
   public FormPropertiesForm()
   {
     this.formService = ProcessEngines.getDefaultProcessEngine().getFormService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     addStyleName("block-holder");
     addStyleName("formprops");
     
     initTitle();
     initFormPropertiesComponent();
     initButtons();
     initListeners();
   }
   
   public void setFormProperties(List<FormProperty> formProperties)
   {
     this.formPropertiesComponent.setFormProperties(formProperties);
   }
   
   public void setSubmitButtonCaption(String caption) {
     this.submitFormButton.setCaption(caption);
   }
   
   public void setCancelButtonCaption(String caption) {
     this.cancelFormButton.setCaption(caption);
   }
   
   public void setFormHelp(String caption) {
     this.formTitle.setValue(caption);
     this.formTitle.setVisible(caption != null);
   }
   
 
 
   public void clear()
   {
     this.formPropertiesComponent.setFormProperties(this.formPropertiesComponent.getFormProperties());
   }
   
   protected void initTitle() {
     this.formTitle = new Label();
     this.formTitle.addStyleName("h4");
     this.formTitle.setVisible(false);
     addComponent(this.formTitle);
   }
   
   protected void initButtons() {
     this.submitFormButton = new Button();
     this.cancelFormButton = new Button();
     
     HorizontalLayout buttons = new HorizontalLayout();
     buttons.setSpacing(true);
     buttons.setWidth(100.0F, 8);
     buttons.addStyleName("block-holder");
     buttons.addComponent(this.submitFormButton);
     buttons.setComponentAlignment(this.submitFormButton, Alignment.BOTTOM_RIGHT);
     
     buttons.addComponent(this.cancelFormButton);
     buttons.setComponentAlignment(this.cancelFormButton, Alignment.BOTTOM_RIGHT);
     
     Label buttonSpacer = new Label();
     buttons.addComponent(buttonSpacer);
     buttons.setExpandRatio(buttonSpacer, 1.0F);
     addComponent(buttons);
   }
   
   protected void initFormPropertiesComponent() {
     this.formPropertiesComponent = new FormPropertiesComponent();
     addComponent(this.formPropertiesComponent);
   }
   
   protected void initListeners() {
     this.submitFormButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = -6091586145870618870L;
       
       public void buttonClick(ClickEvent event)
       {
         try {
           Map<String, String> formProperties = FormPropertiesForm.this.formPropertiesComponent.getFormPropertyValues();
           FormPropertiesForm.this.fireEvent(new FormPropertiesForm.FormPropertiesEvent(FormPropertiesForm.this, FormPropertiesForm.this, "SUBMIT", formProperties));
           FormPropertiesForm.this.submitFormButton.setComponentError(null);
 
         }
         catch (InvalidValueException localInvalidValueException) {}
       }
       
     });
     this.cancelFormButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = -8980500491522472381L;
       
       public void buttonClick(ClickEvent event) {
         FormPropertiesForm.this.fireEvent(new FormPropertiesForm.FormPropertiesEvent(FormPropertiesForm.this, FormPropertiesForm.this, "CANCEL"));
         FormPropertiesForm.this.submitFormButton.setComponentError(null);
       }
     });
   }
   
   public void hideCancelButton() {
     this.cancelFormButton.setVisible(false);
   }
   
   protected void addEmptySpace(ComponentContainer container) {
     Label emptySpace = new Label("&nbsp;", 3);
     emptySpace.setSizeUndefined();
     container.addComponent(emptySpace);
   }
   
 
   public class FormPropertiesEvent
     extends Component.Event
   {
     private static final long serialVersionUID = -410814526942034125L;
     
     public static final String TYPE_SUBMIT = "SUBMIT";
     
     public static final String TYPE_CANCEL = "CANCEL";
     
     private String type;
     
     private Map<String, String> formProperties;
     
 
     public FormPropertiesEvent(Component source, String type)
     {
       super(source);
       this.type = type;
     }
     
     public FormPropertiesEvent(String source, Map<String, String> type) {
       this(source, type);
       this.formProperties = formProperties;
     }
     
     public String getType() {
       return this.type;
     }
     
     public Map<String, String> getFormProperties() {
       return this.formProperties;
     }
   }
 }


