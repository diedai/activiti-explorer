 package org.activiti.editor.ui;
 
 import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
 import com.vaadin.event.LayoutEvents.LayoutClickListener;
 import com.vaadin.ui.AbstractLayout;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SelectEditorComponent
   extends VerticalLayout
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected boolean enableHighlightWhenClicked;
   protected HorizontalLayout modelerLayout;
   protected Button modelerButton;
   protected Label modelerLabel;
   protected Label modelerDescriptionLabel;
   protected HorizontalLayout tableEditorLayout;
   protected Button tableEditorButton;
   protected Label tableEditorLabel;
   protected Label tableEditorDescriptionLabel;
   protected boolean modelerPreferred;
   protected EditorSelectedListener editorSelectedListener;
   
   public SelectEditorComponent()
   {
     this(true);
   }
   
   public SelectEditorComponent(boolean enableHighlightWhenClicked) {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.enableHighlightWhenClicked = enableHighlightWhenClicked;
     
     createModelerEditorChoice();
     addComponent(new Label("&nbsp;", 3));
     createTableDrivenEditorChoice();
     
     preferModeler();
   }
   
   protected void createModelerEditorChoice() {
     this.modelerLayout = new HorizontalLayout();
     this.modelerLayout.setWidth("300px");
     this.modelerLayout.addStyleName("clickable");
     addComponent(this.modelerLayout);
     
     this.modelerButton = new Button();
     this.modelerButton.setIcon(Images.PROCESS_EDITOR_BPMN);
     this.modelerButton.setStyleName("link");
     this.modelerLayout.addComponent(this.modelerButton);
     this.modelerLayout.setComponentAlignment(this.modelerButton, Alignment.MIDDLE_LEFT);
     
     VerticalLayout modelerTextLayout = new VerticalLayout();
     this.modelerLayout.addComponent(modelerTextLayout);
     this.modelerLayout.setExpandRatio(modelerTextLayout, 1.0F);
     
     this.modelerLabel = new Label(this.i18nManager.getMessage("process.editor.modeler"));
     this.modelerLabel.addStyleName("clickable");
     modelerTextLayout.addComponent(this.modelerLabel);
     
     this.modelerDescriptionLabel = new Label(this.i18nManager.getMessage("process.editor.modeler.description"));
     this.modelerDescriptionLabel.addStyleName("light");
     this.modelerDescriptionLabel.addStyleName("clickable");
     modelerTextLayout.addComponent(this.modelerDescriptionLabel);
     
     this.modelerLayout.addListener(new LayoutEvents.LayoutClickListener() {
       public void layoutClick(LayoutEvents.LayoutClickEvent event) {
         SelectEditorComponent.this.preferModeler();
       }
       
     });
     this.modelerButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         SelectEditorComponent.this.preferModeler();
       }
     });
   }
   
   protected void createTableDrivenEditorChoice() {
     this.tableEditorLayout = new HorizontalLayout();
     this.tableEditorLayout.setWidth("300px");
     this.tableEditorLayout.addStyleName("clickable");
     addComponent(this.tableEditorLayout);
     
     this.tableEditorButton = new Button();
     this.tableEditorButton.setIcon(Images.PROCESS_EDITOR_TABLE);
     this.tableEditorButton.setStyleName("link");
     this.tableEditorLayout.addComponent(this.tableEditorButton);
     this.tableEditorLayout.setComponentAlignment(this.tableEditorButton, Alignment.MIDDLE_LEFT);
     
     VerticalLayout tableEditorTextLayout = new VerticalLayout();
     this.tableEditorLayout.addComponent(tableEditorTextLayout);
     this.tableEditorLayout.setExpandRatio(tableEditorTextLayout, 1.0F);
     
     this.tableEditorLabel = new Label(this.i18nManager.getMessage("process.editor.table"));
     this.tableEditorLabel.addStyleName("clickable");
     tableEditorTextLayout.addComponent(this.tableEditorLabel);
     
     this.tableEditorDescriptionLabel = new Label(this.i18nManager.getMessage("process.editor.table.description"));
     this.tableEditorDescriptionLabel.addStyleName("light");
     this.tableEditorDescriptionLabel.addStyleName("clickable");
     tableEditorTextLayout.addComponent(this.tableEditorDescriptionLabel);
     
 
     this.tableEditorLayout.addListener(new LayoutEvents.LayoutClickListener() {
       public void layoutClick(LayoutEvents.LayoutClickEvent event) {
         SelectEditorComponent.this.preferTableDrivenEditor();
       }
       
     });
     this.tableEditorButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         SelectEditorComponent.this.preferTableDrivenEditor();
       }
     });
   }
   
   public void preferModeler() {
     if (!this.modelerPreferred) {
       this.modelerPreferred = true;
       
       if (this.enableHighlightWhenClicked) {
         selectEditor(this.modelerLayout);
         deselectEditor(this.tableEditorLayout);
         
         this.modelerLabel.addStyleName("bold");
         this.tableEditorLabel.removeStyleName("bold");
       }
     }
     
     if (this.editorSelectedListener != null) {
       this.editorSelectedListener.editorSelectionChanged();
     }
   }
   
   public void preferTableDrivenEditor() {
     if (this.modelerPreferred) {
       this.modelerPreferred = false;
       
       if (this.enableHighlightWhenClicked) {
         selectEditor(this.tableEditorLayout);
         deselectEditor(this.modelerLayout);
         
         this.tableEditorLabel.addStyleName("bold");
         this.modelerLabel.removeStyleName("bold");
       }
     }
     
     if (this.editorSelectedListener != null) {
       this.editorSelectedListener.editorSelectionChanged();
     }
   }
   
   protected void selectEditor(AbstractLayout editorLayout) {
     editorLayout.addStyleName("process-definition-editor-choice");
   }
   
   protected void deselectEditor(AbstractLayout editorLayout) {
     editorLayout.removeStyleName("process-definition-editor-choice");
   }
   
   public HorizontalLayout getModelerLayout() {
     return this.modelerLayout;
   }
   
   public Button getModelerButton() {
     return this.modelerButton;
   }
   
   public HorizontalLayout getTableEditorLayout() {
     return this.tableEditorLayout;
   }
   
   public Button getTableEditorButton() {
     return this.tableEditorButton;
   }
   
   public Label getModelerLabel()
   {
     return this.modelerLabel;
   }
   
   public Label getModelerDescriptionLabel()
   {
     return this.modelerDescriptionLabel;
   }
   
   public Label getTableEditorLabel()
   {
     return this.tableEditorLabel;
   }
   
   public Label getTableEditorDescriptionLabel()
   {
     return this.tableEditorDescriptionLabel;
   }
   
   public boolean isModelerPreferred()
   {
     return this.modelerPreferred;
   }
   
   public EditorSelectedListener getEditorSelectedListener() {
     return this.editorSelectedListener;
   }
   
   public void setEditorSelectedListener(EditorSelectedListener editorSelectedListener) {
     this.editorSelectedListener = editorSelectedListener;
   }
   
   public static abstract interface EditorSelectedListener
   {
     public abstract void editorSelectionChanged();
   }
 }


