 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.ui.AbstractOrderedLayout;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.ComponentContainer;
 import com.vaadin.ui.CssLayout;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.VerticalLayout;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DetailPanel
   extends VerticalLayout
 {
   private static final long serialVersionUID = 1L;
   protected Panel mainPanel;
   
   public DetailPanel()
   {
     setSizeFull();
     addStyleName("detail-panel");
     setMargin(true);
     
     CssLayout cssLayout = new CssLayout();
     cssLayout.addStyleName("detail-panel");
     cssLayout.setSizeFull();
     super.addComponent(cssLayout);
     
     this.mainPanel = new Panel();
     this.mainPanel.addStyleName("light");
     this.mainPanel.setSizeFull();
     cssLayout.addComponent(this.mainPanel);
     
 
     VerticalLayout verticalLayout = new VerticalLayout();
     verticalLayout.setWidth(100.0F, 8);
     verticalLayout.setMargin(true);
     this.mainPanel.setContent(verticalLayout);
   }
   
 
 
   public void setDetailContainer(ComponentContainer component)
   {
     this.mainPanel.setContent(component);
   }
   
 
 
 
 
   public void setFixedButtons(Component component)
   {
     if (getComponentCount() == 2) {
       removeComponent(getComponent(1));
     }
     addComponent(component);
   }
   
   public void addComponent(Component c)
   {
     this.mainPanel.addComponent(c);
   }
   
 
 
   public void addDetailComponent(Component c)
   {
     this.mainPanel.addComponent(c);
   }
   
   public void addComponent(Component c, int index)
   {
     throw new UnsupportedOperationException("Cannot add components directly. Use addDetailComponent or setDetailContainer");
   }
   
 
 
   public void addDetailComponent(Component c, int index)
   {
     if ((this.mainPanel.getContent() instanceof AbstractOrderedLayout)) {
       ((AbstractOrderedLayout)this.mainPanel.getContent()).addComponent(c, index);
     } else {
       throw new UnsupportedOperationException("Cannot add components indexed component, detail content is not AbstractOrderedLayout");
     }
   }
   
 
 
   public void setDetailExpandRatio(Component component, float ratio)
   {
     if ((this.mainPanel.getContent() instanceof AbstractOrderedLayout)) {
       ((AbstractOrderedLayout)this.mainPanel.getContent()).setExpandRatio(component, ratio);
     } else {
       throw new UnsupportedOperationException("Cannot set ExpandRatio, detail content is not AbstractOrderedLayout");
     }
   }
   
   public void addComponentAsFirst(Component c)
   {
     addComponent(c, 0);
   }
   
   public Panel getMainPanel() {
     return this.mainPanel;
   }
 }


