 package org.activiti.explorer.ui.management.admin;
 
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.impl.ProcessEngineImpl;
 import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AdminDatabaseSettingsPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected I18nManager i18nManager;
   protected ProcessEngineConfigurationImpl engineConfiguration;
   protected VerticalLayout panelLayout;
   protected HorizontalLayout detailLayout;
   protected GridLayout detailsGrid;
   
   public AdminDatabaseSettingsPanel()
   {
     ProcessEngineImpl processEngine = (ProcessEngineImpl)ProcessEngines.getDefaultProcessEngine();
     this.engineConfiguration = processEngine.getProcessEngineConfiguration();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     init();
   }
   
   protected void init() {
     setSizeFull();
     addStyleName("light");
     
     initPageTitle();
     initDatabaseSettingsDetails();
   }
   
   protected void initPageTitle() {
     HorizontalLayout layout = new HorizontalLayout();
     layout.setWidth(100.0F, 8);
     layout.addStyleName("title-block");
     layout.setSpacing(true);
     layout.setMargin(false, false, true, false);
     addDetailComponent(layout);
     
     Embedded databaseImage = new Embedded(null, Images.DATABASE_50);
     layout.addComponent(databaseImage);
     
     Label groupName = new Label(this.i18nManager.getMessage("database.title"));
     groupName.setSizeUndefined();
     groupName.addStyleName("h2");
     layout.addComponent(groupName);
     layout.setComponentAlignment(groupName, Alignment.MIDDLE_LEFT);
     layout.setExpandRatio(groupName, 1.0F);
   }
   
   protected void initDatabaseSettingsDetails() {
     Label settingsHeader = new Label(this.i18nManager.getMessage("management.menu.database"));
     settingsHeader.addStyleName("h3");
     settingsHeader.addStyleName("block-holder");
     
     addDetailComponent(settingsHeader);
     
     this.detailLayout = new HorizontalLayout();
     this.detailLayout.setSpacing(true);
     this.detailLayout.setMargin(true, false, true, false);
     addDetailComponent(this.detailLayout);
     
     initSettingsProperties();
   }
   
   protected void initSettingsProperties() {
     this.detailsGrid = new GridLayout(2, 3);
     this.detailsGrid.setSpacing(true);
     this.detailLayout.setMargin(true, true, true, false);
     this.detailLayout.addComponent(this.detailsGrid);
     
 
     Label typeLabel = new Label(this.i18nManager.getMessage("database.type") + ": ");
     typeLabel.addStyleName("bold");
     this.detailsGrid.addComponent(typeLabel);
     Label typeValueLabel = new Label(this.engineConfiguration.getDatabaseType());
     this.detailsGrid.addComponent(typeValueLabel);
     
 
     Label schemaUpdateLabel = new Label(this.i18nManager.getMessage("database.update") + ": ");
     schemaUpdateLabel.addStyleName("bold");
     this.detailsGrid.addComponent(schemaUpdateLabel);
     Label schemaUpdateValueLabel = new Label(this.engineConfiguration.getDatabaseSchemaUpdate());
     this.detailsGrid.addComponent(schemaUpdateValueLabel);
     
 
     Label configTypeLabel = new Label(this.i18nManager.getMessage("database.config.type") + ": ");
     configTypeLabel.addStyleName("bold");
     this.detailsGrid.addComponent(configTypeLabel);
     String databaseConfigType = getDatabaseType();
     Label configTypeValueLabel = new Label(databaseConfigType);
     this.detailsGrid.addComponent(configTypeValueLabel);
     
     if ("JNDI".equals(databaseConfigType))
     {
       Label jndiLabel = new Label(this.i18nManager.getMessage("database.jndi") + ": ");
       jndiLabel.addStyleName("bold");
       this.detailsGrid.addComponent(jndiLabel);
       Label jndiValueLabel = new Label(this.engineConfiguration.getDataSourceJndiName());
       this.detailsGrid.addComponent(jndiValueLabel);
     }
     else if ("Datasource".equals(databaseConfigType))
     {
 
       Label datasourceLabel = new Label(this.i18nManager.getMessage("database.datasource.class") + ": ");
       datasourceLabel.addStyleName("bold");
       this.detailsGrid.addComponent(datasourceLabel);
       Label datasourceValueLabel = new Label(this.engineConfiguration.getDataSource().getClass().getName());
       this.detailsGrid.addComponent(datasourceValueLabel);
 
     }
     else
     {
       Label jdbcURLLabel = new Label(this.i18nManager.getMessage("database.jdbc.url") + ": ");
       jdbcURLLabel.addStyleName("bold");
       this.detailsGrid.addComponent(jdbcURLLabel);
       Label jdbcURLValueLabel = new Label(this.engineConfiguration.getJdbcUrl());
       this.detailsGrid.addComponent(jdbcURLValueLabel);
     }
   }
   
   protected String getDatabaseType() {
     String databaseType = null;
     if (this.engineConfiguration.getDataSourceJndiName() != null) {
       databaseType = "JNDI";
     } else if (this.engineConfiguration.getDataSource() != null) {
       databaseType = "Datasource";
     } else {
       databaseType = "JDBC config";
     }
     return databaseType;
   }
 }


