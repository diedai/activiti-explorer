 package org.activiti.explorer.ui.alfresco;
 
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.ui.process.ProcessDefinitionDetailPanel;
 import org.activiti.explorer.ui.process.ProcessDefinitionPage;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AlfrescoProcessDefinitionDetailPanel
   extends ProcessDefinitionDetailPanel
 {
   private static final long serialVersionUID = 1L;
   
   public AlfrescoProcessDefinitionDetailPanel(String processDefinitionId, ProcessDefinitionPage processDefinitionPage)
   {
     super(processDefinitionId, processDefinitionPage);
   }
   
 
   protected void initActions() {}
   
   protected void initUi()
   {
     super.initUi();
     initProcessInstancesTable();
   }
   
   protected void initProcessInstancesTable() {
     ProcessInstanceTableLazyQuery query = new ProcessInstanceTableLazyQuery(this.processDefinition.getId());
     
 
     Label instancesTitle = new Label(this.i18nManager.getMessage("process.instances") + " (" + query.size() + ")");
     instancesTitle.addStyleName("h3");
     instancesTitle.addStyleName("block-holder");
     instancesTitle.addStyleName("no-line");
     this.detailPanelLayout.addComponent(instancesTitle);
     
     if (query.size() > 0)
     {
       Label emptySpace = new Label("&nbsp;", 3);
       this.detailPanelLayout.addComponent(emptySpace);
       
       Table instancesTable = new Table();
       instancesTable.setWidth(400.0F, 0);
       if (query.size() > 6) {
         instancesTable.setPageLength(6);
       } else {
         instancesTable.setPageLength(query.size());
       }
       
       LazyLoadingContainer container = new LazyLoadingContainer(query);
       instancesTable.setContainerDataSource(container);
       
 
       instancesTable.addContainerProperty("id", String.class, null);
       instancesTable.addContainerProperty("businessKey", String.class, null);
       instancesTable.addContainerProperty("actions", Component.class, null);
       
 
       instancesTable.setColumnAlignment("actions", "c");
       
 
       instancesTable.setColumnHeader("id", this.i18nManager.getMessage("process.instance.id"));
       instancesTable.setColumnHeader("businessKey", this.i18nManager.getMessage("process.instance.businesskey"));
       instancesTable.setColumnHeader("actions", this.i18nManager.getMessage("process.instance.actions"));
       
       instancesTable.setEditable(false);
       instancesTable.setSelectable(true);
       instancesTable.setNullSelectionAllowed(false);
       instancesTable.setSortDisabled(true);
       this.detailPanelLayout.addComponent(instancesTable);
     }
     else {
       Label noInstances = new Label(this.i18nManager.getMessage("process.no.instances"));
       this.detailPanelLayout.addComponent(noInstances);
     }
   }
 }


