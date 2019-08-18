 package org.activiti.explorer.navigation;
 
 import com.vaadin.ui.UriFragmentUtility;
 import com.vaadin.ui.UriFragmentUtility.FragmentChangedEvent;
 import com.vaadin.ui.UriFragmentUtility.FragmentChangedListener;
 import java.util.List;
 import org.activiti.explorer.ExplorerApp;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class NavigationFragmentChangeListener
   implements UriFragmentUtility.FragmentChangedListener
 {
   private static final long serialVersionUID = 1L;
   @Autowired
   protected NavigatorManager navigatorManager;
   
   public void fragmentChanged(UriFragmentUtility.FragmentChangedEvent source)
   {
     String fragment = source.getUriFragmentUtility().getFragment();
     
     if (StringUtils.isNotEmpty(fragment)) {
       UriFragment uriFragment = new UriFragment(fragment);
       
 
       Navigator navigationHandler = null;
       if ((uriFragment.getUriParts() != null) && (!uriFragment.getUriParts().isEmpty())) {
         navigationHandler = this.navigatorManager.getNavigator((String)uriFragment.getUriParts().get(0));
       }
       
       if (navigationHandler == null) {
         navigationHandler = this.navigatorManager.getDefaultNavigator();
       }
       
 
       navigationHandler.handleNavigation(uriFragment);
     }
     else if ((ExplorerApp.get().getCurrentUriFragment() != null) && 
       (ExplorerApp.get().getCurrentUriFragment().getUriParts() != null) && 
       (!ExplorerApp.get().getCurrentUriFragment().getUriParts().isEmpty()))
     {
       Navigator navigationHandler = this.navigatorManager.getNavigator((String)ExplorerApp.get().getCurrentUriFragment().getUriParts().get(0));
       if ((navigationHandler instanceof ProcessModelNavigator)) {
         navigationHandler.handleNavigation(ExplorerApp.get().getCurrentUriFragment());
       }
     }
   }
   
 
   public void setNavigatorManager(NavigatorManager navigatorManager)
   {
     this.navigatorManager = navigatorManager;
   }
 }


