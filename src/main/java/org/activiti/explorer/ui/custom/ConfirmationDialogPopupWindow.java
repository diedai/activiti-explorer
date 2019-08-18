 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.Label;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.event.ConfirmationEvent;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ConfirmationDialogPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected GridLayout layout;
   protected Label descriptionLabel;
   protected Button yesButton;
   protected Button noButton;
   
   public ConfirmationDialogPopupWindow(String title, String description)
   {
     setWidth(400.0F, 0);
     setModal(true);
     setResizable(false);
     
     addStyleName("light");
     
     this.layout = new GridLayout(2, 2);
     this.layout.setMargin(true);
     this.layout.setSpacing(true);
     this.layout.setSizeFull();
     
     setContent(this.layout);
     
     I18nManager i18nManager = ExplorerApp.get().getI18nManager();
     if (title != null) {
       setCaption(title);
     } else {
       setCaption(i18nManager.getMessage("confirmation.dialog.default.title"));
     }
     
     initLabel(description);
     initButtons(i18nManager);
   }
   
   public ConfirmationDialogPopupWindow(String description) {
     this(null, description);
   }
   
 
 
   public void showConfirmation()
   {
     this.yesButton.focus();
     ExplorerApp.get().getViewManager().showPopupWindow(this);
   }
   
   protected void initButtons(I18nManager i18nManager)
   {
     this.yesButton = new Button(i18nManager.getMessage("confirmation.dialog.yes"));
     this.layout.addComponent(this.yesButton, 0, 1);
     this.layout.setComponentAlignment(this.yesButton, Alignment.BOTTOM_RIGHT);
     this.yesButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) { ConfirmationDialogPopupWindow.this.close();
         ConfirmationDialogPopupWindow.this.fireEvent(new ConfirmationEvent(ConfirmationDialogPopupWindow.this, true));
       }
       
     });
     this.noButton = new Button(i18nManager.getMessage("confirmation.dialog.no"));
     this.layout.addComponent(this.noButton, 1, 1);
     this.layout.setComponentAlignment(this.noButton, Alignment.BOTTOM_LEFT);
     this.noButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) { ConfirmationDialogPopupWindow.this.close();
         ConfirmationDialogPopupWindow.this.fireEvent(new ConfirmationEvent(ConfirmationDialogPopupWindow.this, false));
       }
     });
   }
   
   protected void initLabel(String description) {
     this.descriptionLabel = new Label(description, 3);
     this.descriptionLabel.setSizeFull();
     this.layout.addComponent(this.descriptionLabel, 0, 0, 1, 0);
     this.layout.setRowExpandRatio(0, 1.0F);
   }
 }


