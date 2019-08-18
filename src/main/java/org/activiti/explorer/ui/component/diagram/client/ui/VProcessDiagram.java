 package org.activiti.explorer.ui.component.diagram.client.ui;
 
 import com.google.gwt.dom.client.DivElement;
 import com.google.gwt.dom.client.Document;
 import com.google.gwt.user.client.Element;
 import com.google.gwt.user.client.ui.Widget;
 import com.vaadin.terminal.gwt.client.ApplicationConnection;
 import com.vaadin.terminal.gwt.client.Paintable;
 import com.vaadin.terminal.gwt.client.UIDL;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class VProcessDiagram
   extends Widget
   implements Paintable
 {
   public static final String ATTRIBUTE_PROC_KEY = "definitionKey";
   public static final String CLASSNAME = "processdiagramWrapper";
   protected String paintableId;
   protected ApplicationConnection client;
   
   public native void drawDiagram(String paramString);
   
   public VProcessDiagram()
   {
     DivElement element = Document.get().createDivElement();
     setElement(element);
     element.setClassName("processdiagramWrapper");
     
     DivElement barElement = Document.get().createDivElement();
     barElement.setId("pb1");
     getElement().appendChild(barElement);
     
     DivElement diagramElement = Document.get().createDivElement();
     diagramElement.setId("overlayBox");
     getElement().appendChild(diagramElement);
     
     DivElement crumbsElement = Document.get().createDivElement();
     crumbsElement.setId("diagramBreadCrumbs");
     crumbsElement.setClassName("diagramBreadCrumbs");
     crumbsElement.setAttribute("onmousedown", "onmousedown");
     crumbsElement.setAttribute("onselectstart", "onmousedown");
     diagramElement.appendChild(crumbsElement);
     
     DivElement holderElement = Document.get().createDivElement();
     holderElement.setId("diagramHolder");
     holderElement.setClassName("diagramHolder");
     diagramElement.appendChild(holderElement);
     
     DivElement infoElement = Document.get().createDivElement();
     infoElement.setId("diagramInfo");
     infoElement.setClassName("diagram-info");
     diagramElement.appendChild(infoElement);
   }
   
 
 
 
   public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
   {
     if (client.updateComponent(this, uidl, true)) {
       return;
     }
     this.client = client;
     
 
     this.paintableId = uidl.getId();
     
     if (uidl.hasAttribute("definitionKey")) {
       drawDiagram(uidl.getStringAttribute("definitionKey"));
     }
   }
 }


