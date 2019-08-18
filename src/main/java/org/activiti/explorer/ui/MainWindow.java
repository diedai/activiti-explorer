 package org.activiti.explorer.ui;
 
 import com.vaadin.ui.Component;
 import com.vaadin.ui.UriFragmentUtility;
 import com.vaadin.ui.Window;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.navigation.NavigationFragmentChangeListener;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.login.LoginPage;
 import org.activiti.explorer.ui.mainlayout.MainLayout;
 import org.springframework.beans.factory.annotation.Autowired;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MainWindow
   extends Window
 {
   private static final long serialVersionUID = 1L;
   @Autowired
   protected I18nManager i18nManager;
   @Autowired
   protected NavigationFragmentChangeListener navigationFragmentChangeListener;
   protected MainLayout mainLayout;
   protected UriFragmentUtility uriFragmentUtility;
   protected UriFragment currentUriFragment;
   protected boolean showingLoginPage;
   
   public MainWindow()
   {
     setTheme("activiti");
   }
   
   public void attach()
   {
     super.attach();
     setCaption(this.i18nManager.getMessage("app.title"));
   }
   
   public void showLoginPage() {
     this.showingLoginPage = true;
     addStyleName("login-general");
     setContent(new LoginPage());
   }
   
   public void showDefaultContent() {
     this.showingLoginPage = false;
     removeStyleName("login-general");
     addStyleName("Default style");
     
 
     this.mainLayout = new MainLayout();
     setContent(this.mainLayout);
     
 
     initHiddenComponents();
   }
   
 
   public void switchView(Component component)
   {
     this.mainLayout.setMainContent(component);
   }
   
   public void setMainNavigation(String navigation) {
     this.mainLayout.setMainNavigation(navigation);
   }
   
 
 
   protected void initHiddenComponents()
   {
     this.uriFragmentUtility = new UriFragmentUtility();
     this.mainLayout.addComponent(this.uriFragmentUtility);
     
 
     this.uriFragmentUtility.addListener(this.navigationFragmentChangeListener);
   }
   
   public UriFragment getCurrentUriFragment() {
     return this.currentUriFragment;
   }
   
 
 
 
   public void setCurrentUriFragment(UriFragment fragment)
   {
     this.currentUriFragment = fragment;
     
     if (fragmentChanged(fragment))
     {
       if (fragment != null) {
         this.uriFragmentUtility.setFragment(fragment.toString(), false);
       } else {
         this.uriFragmentUtility.setFragment("", false);
       }
     }
   }
   
   private boolean fragmentChanged(UriFragment fragment) {
     String fragmentString = fragment.toString();
     if (fragmentString == null) {
       return this.uriFragmentUtility.getFragment() != null;
     }
     return !fragmentString.equals(this.uriFragmentUtility.getFragment());
   }
   
   public boolean isShowingLoginPage()
   {
     return this.showingLoginPage;
   }
   
   public void setNavigationFragmentChangeListener(NavigationFragmentChangeListener navigationFragmentChangeListener) {
     this.navigationFragmentChangeListener = navigationFragmentChangeListener;
   }
   
   public void setI18nManager(I18nManager i18nManager) {
     this.i18nManager = i18nManager;
   }
 }


