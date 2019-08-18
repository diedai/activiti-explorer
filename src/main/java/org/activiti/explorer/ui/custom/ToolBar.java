 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ToolBar
   extends HorizontalLayout
 {
   private static final long serialVersionUID = 7957488256766569264L;
   protected Map<String, ToolbarEntry> entryMap;
   protected String currentEntryKey;
   protected ToolbarEntry currentEntry;
   protected List<Button> actionButtons;
   protected List<Component> additionalComponents;
   
   public ToolBar()
   {
     this.entryMap = new HashMap();
     this.actionButtons = new ArrayList();
     this.additionalComponents = new ArrayList();
     
     setWidth("100%");
     setHeight(36.0F, 0);
     addStyleName("toolbar");
     setSpacing(true);
     setMargin(false, true, false, true);
     
 
     Label spacer = new Label();
     spacer.setContentMode(3);
     spacer.setValue("&nbsp;");
     addComponent(spacer);
     setExpandRatio(spacer, 1.0F);
   }
   
 
 
   public ToolbarEntry addToolbarEntry(String key, String title, ToolbarCommand command)
   {
     if (this.entryMap.containsKey(key)) {
       throw new IllegalArgumentException("Toolbar already contains entry for key: " + key);
     }
     
     ToolbarEntry entry = new ToolbarEntry(key, title);
     if (command != null) {
       entry.setCommand(command);
     }
     
     this.entryMap.put(key, entry);
     addEntryComponent(entry);
     return entry;
   }
   
 
 
 
   public ToolbarPopupEntry addPopupEntry(String key, String title)
   {
     if (this.entryMap.containsKey(key)) {
       throw new IllegalArgumentException("Toolbar already contains entry for key: " + key);
     }
     
     ToolbarPopupEntry entry = new ToolbarPopupEntry(key, title);
     this.entryMap.put(key, entry);
     addEntryComponent(entry);
     return entry;
   }
   
 
 
 
   public void addButton(Button button)
   {
     button.addStyleName("toolbar-button");
     
     this.actionButtons.add(button);
     
     addComponent(button);
     setComponentAlignment(button, Alignment.MIDDLE_RIGHT);
   }
   
   public void removeAllButtons() {
     for (Button b : this.actionButtons) {
       removeComponent(b);
     }
   }
   
   public void addAdditionalComponent(Component component) {
     this.additionalComponents.add(component);
     addComponent(component);
   }
   
   public void removeAllAdditionalComponents() {
     for (Component c : this.additionalComponents) {
       removeComponent(c);
     }
   }
   
   public long getCount(String key) {
     ToolbarEntry toolbarEntry = (ToolbarEntry)this.entryMap.get(key);
     if (toolbarEntry == null) {
       throw new IllegalArgumentException("Toolbar doesn't contain an entry for key: " + key);
     }
     return toolbarEntry.getCount().longValue();
   }
   
 
 
   public void setCount(String key, Long count)
   {
     ToolbarEntry toolbarEntry = (ToolbarEntry)this.entryMap.get(key);
     if (toolbarEntry == null) {
       throw new IllegalArgumentException("Toolbar doesn't contain an entry for key: " + key);
     }
     toolbarEntry.setCount(count);
   }
   
 
 
   public ToolbarEntry getEntry(String key)
   {
     return (ToolbarEntry)this.entryMap.get(key);
   }
   
 
 
 
   public synchronized void setActiveEntry(String key)
   {
     if (this.currentEntry != null) {
       this.currentEntry.setActive(false);
     }
     
     this.currentEntryKey = key;
     
     this.currentEntry = ((ToolbarEntry)this.entryMap.get(key));
     if (this.currentEntry != null) {
       this.currentEntry.setActive(true);
     }
   }
   
   protected void addEntryComponent(ToolbarEntry entry) {
     addComponent(entry, getComponentCount() - 1 - this.actionButtons.size());
     setComponentAlignment(entry, Alignment.MIDDLE_LEFT);
   }
   
   public String getCurrentEntryKey() {
     return this.currentEntryKey;
   }
 }


