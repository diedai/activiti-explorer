 package org.activiti.explorer.ui.process.simple.editor.table;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.CheckBox;
 import com.vaadin.ui.ComboBox;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Table;
 import java.util.Arrays;
 import java.util.Collection;
 import java.util.Iterator;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.process.simple.editor.listener.AddPropertyClickListener;
 import org.activiti.explorer.ui.process.simple.editor.listener.DeletePropertyClickListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class PropertyTable
   extends Table
 {
   private static final long serialVersionUID = 6521446909987945815L;
   public static final String ID_PROPERTY_NAME = "property";
   public static final String ID_PROPERTY_TYPE = "type";
   public static final String ID_PROPERTY_REQUIRED = "required";
   public static final String ID_PROPERTY_ACTIONS = "actions";
   private static final String DEFAULT_PROPERTY_NAME = "My property";
   protected I18nManager i18nManager;
   
   public PropertyTable()
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     setEditable(true);
     setColumnReorderingAllowed(true);
     setPageLength(size());
     
     addContainerProperty("property", String.class, null);
     addContainerProperty("type", ComboBox.class, null);
     addContainerProperty("required", CheckBox.class, null);
     addContainerProperty("actions", HorizontalLayout.class, null);
     
     setColumnHeader("property", this.i18nManager.getMessage("process.editor.property.name"));
     setColumnHeader("type", this.i18nManager.getMessage("process.editor.property.type"));
     setColumnHeader("required", this.i18nManager.getMessage("process.editor.property.required"));
     setColumnHeader("actions", this.i18nManager.getMessage("process.editor.actions"));
   }
   
   public void addPropertyRow() {
     addPropertyRow(null, null, null, null);
   }
   
   public void addPropertyRow(String propertyName, String propertyType, Boolean required) {
     addPropertyRow(null, propertyName, propertyType, required);
   }
   
   public void addPropertyRowAfter(Object itemId) {
     addPropertyRow(itemId, null, null, null);
   }
   
   protected void addPropertyRow(Object itemId, String propertyName, String propertyType, Boolean required) {
     Object newItemId = null;
     if (itemId == null) {
       newItemId = addItem();
     } else {
       newItemId = addItemAfter(itemId);
     }
     Item newItem = getItem(newItemId);
     
 
     newItem.getItemProperty("property").setValue(propertyName == null ? "My property" : propertyName);
     
 
     ComboBox typeComboBox = new ComboBox("", Arrays.asList(new String[] { "text", "number", "date" }));
     typeComboBox.setNullSelectionAllowed(false);
     if (propertyType == null) {
       typeComboBox.setValue(typeComboBox.getItemIds().iterator().next());
     } else {
       typeComboBox.setValue(propertyType);
     }
     newItem.getItemProperty("type").setValue(typeComboBox);
     
 
     CheckBox requiredCheckBox = new CheckBox();
     requiredCheckBox.setValue(Boolean.valueOf(required == null ? false : required.booleanValue()));
     newItem.getItemProperty("required").setValue(requiredCheckBox);
     
 
     HorizontalLayout actionButtons = new HorizontalLayout();
     
     Button deleteRowButton = new Button("-");
     deleteRowButton.setData(newItemId);
     deleteRowButton.addListener(new DeletePropertyClickListener(this));
     actionButtons.addComponent(deleteRowButton);
     
     Button addRowButton = new Button("+");
     addRowButton.setData(newItemId);
     addRowButton.addListener(new AddPropertyClickListener(this));
     actionButtons.addComponent(addRowButton);
     
     newItem.getItemProperty("actions").setValue(actionButtons);
   }
 }


