 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.Table.CellStyleGenerator;
 import java.util.HashMap;
 import java.util.Map;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TabbedSelectionWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected HorizontalLayout windowLayout;
   protected Table selectionTable;
   protected String currentSelection;
   protected Component currentComponent;
   protected Map<String, Component> components = new HashMap();
   protected Map<String, Button.ClickListener> listeners = new HashMap();
   protected GridLayout selectedComponentLayout;
   protected Button okButton;
   
   public TabbedSelectionWindow(String title) {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     initWindow(title);
     initWindowLayout();
     initSelectionTable();
     initComponentLayout();
     initActions();
   }
   
   protected void initWindow(String title) {
     setCaption(title);
     center();
     setModal(true);
     addStyleName("light");
   }
   
   protected void initWindowLayout() {
     this.windowLayout = new HorizontalLayout();
     this.windowLayout.setSpacing(false);
     this.windowLayout.setMargin(true);
     this.windowLayout.setSizeFull();
     setContent(this.windowLayout);
   }
   
   protected void initComponentLayout() {
     this.selectedComponentLayout = new GridLayout(1, 2);
     this.selectedComponentLayout.setSizeFull();
     this.selectedComponentLayout.setMargin(true);
     this.selectedComponentLayout.setSpacing(true);
     this.selectedComponentLayout.addStyleName("related-content-create-detail");
     
     this.windowLayout.addComponent(this.selectedComponentLayout);
     this.windowLayout.setExpandRatio(this.selectedComponentLayout, 1.0F);
     
     this.selectedComponentLayout.setRowExpandRatio(0, 1.0F);
     this.selectedComponentLayout.setColumnExpandRatio(0, 1.0F);
   }
   
   protected void initActions() {
     this.okButton = new Button(this.i18nManager.getMessage("button.ok"));
     this.selectedComponentLayout.addComponent(this.okButton, 0, 1);
     this.okButton.setEnabled(false);
     this.okButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) { ((Button.ClickListener)TabbedSelectionWindow.this.listeners.get(TabbedSelectionWindow.this.currentSelection)).buttonClick(event);
         TabbedSelectionWindow.this.close();
       }
     });
     this.selectedComponentLayout.setComponentAlignment(this.okButton, Alignment.BOTTOM_RIGHT);
   }
   
   protected void initSelectionTable() {
     this.selectionTable = new Table();
     this.selectionTable.setSizeUndefined();
     this.selectionTable.setColumnHeaderMode(-1);
     this.selectionTable.setSelectable(true);
     this.selectionTable.setImmediate(true);
     this.selectionTable.setNullSelectionAllowed(false);
     this.selectionTable.setWidth(150.0F, 0);
     this.selectionTable.setHeight(100.0F, 8);
     
     this.selectionTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
       private static final long serialVersionUID = 1L;
       
       public String getStyle(Object itemId, Object propertyId) { if ("name".equals(propertyId)) {
           return "related-last-column";
         }
         return null;
       }
       
     });
     this.selectionTable.addStyleName("related-content-create-list");
     
     this.selectionTable.addContainerProperty("type", Embedded.class, null);
     this.selectionTable.setColumnWidth("type", 22);
     this.selectionTable.addContainerProperty("name", String.class, null);
     
 
     this.selectionTable.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) { String name = (String)event.getProperty().getValue();
         if (name != null) {
           TabbedSelectionWindow.this.currentSelection = name;
           TabbedSelectionWindow.this.currentComponent = ((Component)TabbedSelectionWindow.this.components.get(name));
           TabbedSelectionWindow.this.selectedComponentLayout.removeComponent(TabbedSelectionWindow.this.selectedComponentLayout.getComponent(0, 0));
           if (TabbedSelectionWindow.this.currentComponent != null) {
             TabbedSelectionWindow.this.currentComponent.setSizeFull();
             TabbedSelectionWindow.this.selectedComponentLayout.addComponent(TabbedSelectionWindow.this.currentComponent, 0, 0);
             TabbedSelectionWindow.this.okButton.setEnabled(true);
           } else {
             TabbedSelectionWindow.this.okButton.setEnabled(false);
           }
         }
       }
     });
     this.windowLayout.addComponent(this.selectionTable);
   }
   
 
 
 
 
 
 
   public void addSelectionItem(Embedded icon, String name, Component component, Button.ClickListener clickListener)
   {
     Item item = this.selectionTable.addItem(name);
     item.getItemProperty("type").setValue(icon);
     item.getItemProperty("name").setValue(name);
     this.components.put(name, component);
     this.listeners.put(name, clickListener);
   }
 }


