 package org.activiti.explorer.ui.management.identity;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.VerticalLayout;
 import java.util.HashSet;
 import java.util.Set;
 import org.activiti.engine.IdentityService;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.ui.custom.PopupWindow;
 import org.activiti.explorer.ui.event.SubmitEvent;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class GroupSelectionPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected I18nManager i18nManager;
   protected String userId;
   protected Table groupTable;
   
   public GroupSelectionPopupWindow(IdentityService identityService, String userId)
   {
     this.identityService = identityService;
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.userId = userId;
     
     setCaption(this.i18nManager.getMessage("user.select.groups.popup", new Object[] { userId }));
     setModal(true);
     center();
     setWidth(500.0F, 0);
     setHeight(400.0F, 0);
     addStyleName("light");
     ((VerticalLayout)getContent()).setSpacing(true);
     
     initGroupTable();
     initSelectButton();
   }
   
   protected void initGroupTable() {
     this.groupTable = new Table();
     this.groupTable.setNullSelectionAllowed(false);
     this.groupTable.setSelectable(true);
     this.groupTable.setMultiSelect(true);
     this.groupTable.setSortDisabled(true);
     this.groupTable.setWidth(460.0F, 0);
     this.groupTable.setHeight(275.0F, 0);
     addComponent(this.groupTable);
     
     GroupSelectionQuery query = new GroupSelectionQuery(this.identityService, this.userId);
     LazyLoadingContainer container = new LazyLoadingContainer(query, 30);
     this.groupTable.setContainerDataSource(container);
     
     this.groupTable.addContainerProperty("id", String.class, null);
     this.groupTable.addContainerProperty("name", String.class, null);
     this.groupTable.addContainerProperty("type", String.class, null);
   }
   
   protected void initSelectButton() {
     final Button selectButton = new Button(this.i18nManager.getMessage("user.select.groups"));
     addComponent(selectButton);
     ((VerticalLayout)getContent()).setComponentAlignment(selectButton, Alignment.BOTTOM_RIGHT);
     
     selectButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         GroupSelectionPopupWindow.this.fireEvent(new SubmitEvent(selectButton, "submit"));
         GroupSelectionPopupWindow.this.close();
       }
     });
   }
   
   public Set<String> getSelectedGroupIds()
   {
     Set<String> groupIds = new HashSet();
     for (Object itemId : (Set)this.groupTable.getValue()) {
       groupIds.add((String)this.groupTable.getItem(itemId).getItemProperty("id").getValue());
     }
     return groupIds;
   }
 }


