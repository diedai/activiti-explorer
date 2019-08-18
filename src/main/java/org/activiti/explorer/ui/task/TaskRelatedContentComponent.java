 package org.activiti.explorer.ui.task;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.event.MouseEvents.ClickEvent;
 import com.vaadin.event.MouseEvents.ClickListener;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.ComponentContainer;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.VerticalLayout;
 import java.util.Collection;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Attachment;
 import org.activiti.engine.task.Task;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.content.AttachmentDetailPopupWindow;
 import org.activiti.explorer.ui.content.AttachmentRenderer;
 import org.activiti.explorer.ui.content.AttachmentRendererManager;
 import org.activiti.explorer.ui.content.CreateAttachmentPopupWindow;
 import org.activiti.explorer.ui.content.RelatedContentComponent;
 import org.activiti.explorer.ui.custom.ConfirmationDialogPopupWindow;
 import org.activiti.explorer.ui.event.ConfirmationEvent;
 import org.activiti.explorer.ui.event.ConfirmationEventListener;
 import org.activiti.explorer.ui.event.SubmitEvent;
 import org.activiti.explorer.ui.event.SubmitEventListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskRelatedContentComponent
   extends VerticalLayout
   implements RelatedContentComponent
 {
   private static final long serialVersionUID = 1L;
   protected transient TaskService taskService;
   protected I18nManager i18nManager;
   protected AttachmentRendererManager attachmentRendererManager;
   protected Task task;
   protected VerticalLayout contentLayout;
   protected Table table;
   protected TaskDetailPanel taskDetailPanel;
   protected Label noContentLabel;
   
   public TaskRelatedContentComponent(Task task, TaskDetailPanel taskDetailPanel)
   {
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.attachmentRendererManager = ExplorerApp.get().getAttachmentRendererManager();
     
     this.task = task;
     this.taskDetailPanel = taskDetailPanel;
     
     addStyleName("block-holder");
     
     initActions();
     initAttachmentTable();
   }
   
   public void showAttachmentDetail(Attachment attachment)
   {
     AttachmentDetailPopupWindow popup = new AttachmentDetailPopupWindow(attachment);
     ExplorerApp.get().getViewManager().showPopupWindow(popup);
   }
   
   protected void initActions() {
     HorizontalLayout actionsContainer = new HorizontalLayout();
     actionsContainer.setSizeFull();
     
 
     Label processTitle = new Label(this.i18nManager.getMessage("task.related.content"));
     processTitle.addStyleName("h3");
     processTitle.setSizeFull();
     actionsContainer.addComponent(processTitle);
     actionsContainer.setComponentAlignment(processTitle, Alignment.MIDDLE_LEFT);
     actionsContainer.setExpandRatio(processTitle, 1.0F);
     
 
     Button addRelatedContentButton = new Button();
     addRelatedContentButton.addStyleName("add");
     addRelatedContentButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         CreateAttachmentPopupWindow popup = new CreateAttachmentPopupWindow();
         
         if (TaskRelatedContentComponent.this.task.getProcessInstanceId() != null) {
           popup.setProcessInstanceId(TaskRelatedContentComponent.this.task.getProcessInstanceId());
         } else {
           popup.setTaskId(TaskRelatedContentComponent.this.task.getId());
         }
         
 
         popup.addListener(new SubmitEventListener()
         {
           private static final long serialVersionUID = 1L;
           
           protected void submitted(SubmitEvent event)
           {
             TaskRelatedContentComponent.this.taskDetailPanel.notifyRelatedContentChanged();
           }
           
 
 
 
 
           protected void cancelled(SubmitEvent event) {}
         });
         ExplorerApp.get().getViewManager().showPopupWindow(popup);
       }
       
     });
     actionsContainer.addComponent(addRelatedContentButton);
     actionsContainer.setComponentAlignment(processTitle, Alignment.MIDDLE_RIGHT);
     
 
     addComponent(actionsContainer);
   }
   
   protected void initAttachmentTable() {
     this.contentLayout = new VerticalLayout();
     addComponent(this.contentLayout);
     
     this.table = new Table();
     this.table.setWidth(100.0F, 8);
     this.table.addStyleName("related-content-list");
     
 
     this.table.setVisible(false);
     this.table.setColumnHeaderMode(-1);
     
     addContainerProperties();
     
 
     refreshTaskAttachments();
     this.contentLayout.addComponent(this.table);
   }
   
   protected void addContainerProperties() {
     this.table.addContainerProperty("type", Embedded.class, null);
     this.table.setColumnWidth("type", 16);
     
     this.table.addContainerProperty("name", Component.class, null);
     
     this.table.addContainerProperty("delete", Embedded.class, null);
     this.table.setColumnWidth("delete", 16);
   }
   
   public void refreshTaskAttachments() {
     this.table.removeAllItems();
     if (this.noContentLabel != null) {
       this.contentLayout.removeComponent(this.noContentLabel);
     }
     
     List<Attachment> attachments = null;
     if (this.task.getProcessInstanceId() != null) {
       attachments = this.taskService.getProcessInstanceAttachments(this.task.getProcessInstanceId());
     } else {
       attachments = this.taskService.getTaskAttachments(this.task.getId());
     }
     
     if (!attachments.isEmpty()) {
       addAttachmentsToTable(attachments);
     } else {
       this.table.setVisible(false);
       this.noContentLabel = new Label(this.i18nManager.getMessage("task.no.related.content"));
       this.noContentLabel.addStyleName("light");
       this.contentLayout.addComponent(this.noContentLabel);
     }
   }
   
   protected void addAttachmentsToTable(List<Attachment> attachments) {
     for (Attachment attachment : attachments) {
       AttachmentRenderer renderer = this.attachmentRendererManager.getRenderer(attachment);
       Item attachmentItem = this.table.addItem(attachment.getId());
       attachmentItem.getItemProperty("name").setValue(renderer.getOverviewComponent(attachment, this));
       attachmentItem.getItemProperty("type").setValue(new Embedded(null, renderer.getImage(attachment)));
       
       Embedded deleteButton = new Embedded(null, Images.DELETE);
       deleteButton.addStyleName("clickable");
       deleteButton.addListener(new DeleteClickedListener(attachment));
       attachmentItem.getItemProperty("delete").setValue(deleteButton);
     }
     
     if (!this.table.getItemIds().isEmpty()) {
       this.table.setVisible(true);
     }
     this.table.setPageLength(this.table.size());
   }
   
   protected void addEmptySpace(ComponentContainer container) {
     Label emptySpace = new Label("&nbsp;", 3);
     emptySpace.setSizeUndefined();
     container.addComponent(emptySpace);
   }
   
   private class DeleteClickedListener extends ConfirmationEventListener implements ClickListener
   {
     private static final long serialVersionUID = 1L;
     private Attachment attachment;
     
     public DeleteClickedListener(Attachment attachment) {
       this.attachment = attachment;
     }
     
     public void click(ClickEvent event)
     {
       ConfirmationDialogPopupWindow confirm = new ConfirmationDialogPopupWindow(TaskRelatedContentComponent.this.i18nManager.getMessage("related.content.confirm.delete", new Object[] { this.attachment.getName() }));
       
       confirm.addListener(this);
       confirm.showConfirmation();
     }
     
     protected void confirmed(ConfirmationEvent event)
     {
       TaskRelatedContentComponent.this.taskService.deleteAttachment(this.attachment.getId());
       TaskRelatedContentComponent.this.taskDetailPanel.notifyRelatedContentChanged();
     }
     
     protected void rejected(ConfirmationEvent event) {}
   }
 }


