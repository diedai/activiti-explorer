 package org.activiti.explorer.ui.content;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.data.Validator.InvalidValueException;
 import com.vaadin.terminal.Resource;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.Table.CellStyleGenerator;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.custom.PopupWindow;
 import org.activiti.explorer.ui.event.SubmitEvent;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class CreateAttachmentPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected String taskId;
   protected String processInstanceId;
   protected I18nManager i18nManager;
   protected AttachmentRendererManager attachmentRendererManager;
   protected transient TaskService taskService;
   protected HorizontalLayout layout;
   protected GridLayout detailLayout;
   protected AttachmentEditorComponent currentEditor;
   protected Table attachmentTypes;
   protected Button okButton;
   
   public CreateAttachmentPopupWindow()
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.attachmentRendererManager = ExplorerApp.get().getAttachmentRendererManager();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     
     setCaption(this.i18nManager.getMessage("related.content.add"));
     setWidth(700.0F, 0);
     setHeight(430.0F, 0);
     center();
     setModal(true);
     addStyleName("light");
     
     this.layout = new HorizontalLayout();
     this.layout.setSpacing(false);
     this.layout.setMargin(true);
     this.layout.setSizeFull();
     setContent(this.layout);
     
     initTable();
     
     this.detailLayout = new GridLayout(1, 2);
     this.detailLayout.setSizeFull();
     this.detailLayout.setMargin(true);
     this.detailLayout.setSpacing(true);
     this.detailLayout.addStyleName("related-content-create-detail");
     
     this.layout.addComponent(this.detailLayout);
     this.layout.setExpandRatio(this.detailLayout, 1.0F);
     
     this.detailLayout.setRowExpandRatio(0, 1.0F);
     this.detailLayout.setColumnExpandRatio(0, 1.0F);
     initActions();
   }
   
   public void attach()
   {
     super.attach();
     if (this.attachmentTypes.size() > 0) {
       this.attachmentTypes.select(this.attachmentTypes.firstItemId());
     }
   }
   
   protected void initActions() {
     this.okButton = new Button(this.i18nManager.getMessage("related.content.create"));
     this.detailLayout.addComponent(this.okButton, 0, 1);
     this.okButton.setEnabled(false);
     this.okButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         CreateAttachmentPopupWindow.this.saveAttachment();
       }
     });
     this.detailLayout.setComponentAlignment(this.okButton, Alignment.BOTTOM_RIGHT);
   }
   
   protected void initTable() {
     this.attachmentTypes = new Table();
     this.attachmentTypes.setSizeUndefined();
     this.attachmentTypes.setColumnHeaderMode(-1);
     this.attachmentTypes.setSelectable(true);
     this.attachmentTypes.setImmediate(true);
     this.attachmentTypes.setNullSelectionAllowed(false);
     this.attachmentTypes.setWidth(200.0F, 0);
     this.attachmentTypes.setHeight(100.0F, 8);
     
     this.attachmentTypes.setCellStyleGenerator(new Table.CellStyleGenerator() {
       private static final long serialVersionUID = 1L;
       
       public String getStyle(Object itemId, Object propertyId) { if ("name".equals(propertyId)) {
           return "related-last-column";
         }
         return null;
       }
       
     });
     this.attachmentTypes.addStyleName("related-content-create-list");
     
     this.attachmentTypes.addContainerProperty("type", Embedded.class, null);
     this.attachmentTypes.setColumnWidth("type", 16);
     this.attachmentTypes.addContainerProperty("name", String.class, null);
     
 
     for (AttachmentEditor editor : this.attachmentRendererManager.getAttachmentEditors()) {
       String name = editor.getTitle(this.i18nManager);
       Embedded image = null;
       
       Resource resource = editor.getImage();
       if (resource != null) {
         image = new Embedded(null, resource);
       }
       Item item = this.attachmentTypes.addItem(editor.getName());
       item.getItemProperty("type").setValue(image);
       item.getItemProperty("name").setValue(name);
     }
     
 
     this.attachmentTypes.addListener(new Property.ValueChangeListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) {
         String type = (String)event.getProperty().getValue();
         CreateAttachmentPopupWindow.this.selectType(type);
       }
       
     });
     this.layout.addComponent(this.attachmentTypes);
   }
   
   protected void selectType(String type) {
     if (type != null) {
       setCurrentEditor(this.attachmentRendererManager.getEditor(type));
     } else {
       setCurrentEditor(null);
     }
   }
   
   protected void setCurrentEditor(AttachmentEditor editor) {
     AttachmentEditorComponent component = editor.getEditor(null, this.taskId, this.processInstanceId);
     this.currentEditor = component;
     this.detailLayout.removeComponent(this.detailLayout.getComponent(0, 0));
     
     if (this.currentEditor != null) {
       this.currentEditor.setSizeFull();
       this.detailLayout.addComponent(this.currentEditor, 0, 0);
       this.okButton.setEnabled(true);
     } else {
       this.okButton.setEnabled(false);
     }
   }
   
   protected void saveAttachment()
   {
     try {
       Attachment attachment = this.currentEditor.getAttachment();
       
       fireEvent(new SubmitEvent(this, "submit", attachment));
       
 
       close();
     }
     catch (InvalidValueException localInvalidValueException) {}
   }
   
   public void setTaskId(String taskId)
   {
     this.taskId = taskId;
   }
   
   public void setProcessInstanceId(String processInstanceId) {
     this.processInstanceId = processInstanceId;
   }
 }


