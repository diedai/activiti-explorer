 package org.activiti.explorer.ui.reports;
 
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import org.dussan.vaadin.dcharts.DCharts;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ChartComponent
   extends VerticalLayout
 {
   private static final long serialVersionUID = 1L;
   
   public ChartComponent(String title)
   {
     if (title != null) {
       Label label = new Label(title);
       label.addStyleName("h2");
       addComponent(label);
     }
   }
   
 
   public void addChart(String description, Component chart, String errorMessage)
   {
     addComponent(new Label("&nbsp;", 3));
     addComponent(new Label("&nbsp;", 3));
     
 
     if (description != null) {
       Label label = new Label(description);
       label.addStyleName("h2");
       addComponent(label);
       
       addComponent(new Label("&nbsp;", 3));
     }
     
 
     if (chart != null) {
       if ((chart instanceof DCharts))
       {
         chart.setWidth(600.0F, 0);
         chart.setHeight(450.0F, 0);
         ((DCharts)chart).show();
       }
       addComponent(chart);
     }
     
 
     if (errorMessage != null) {
       Label errorLabel = new Label(errorMessage);
       addComponent(errorLabel);
     }
   }
 }


