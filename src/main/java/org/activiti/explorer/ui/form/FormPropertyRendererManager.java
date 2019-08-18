 package org.activiti.explorer.ui.form;
 
 import java.io.Serializable;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.activiti.engine.ActivitiIllegalArgumentException;
 import org.activiti.engine.form.FormType;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class FormPropertyRendererManager
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   protected Map<Class<? extends FormType>, FormPropertyRenderer> propertyRenderers = new HashMap();
   
 
   protected FormPropertyRenderer noTypePropertyRenderer;
   
 
   public void addFormPropertyRenderer(FormPropertyRenderer renderer)
   {
     this.propertyRenderers.put(renderer.getFormType(), renderer);
   }
   
 
 
 
 
   public FormPropertyRenderer getPropertyRendererForType(FormType formType)
   {
     Class<? extends FormType> clazz = formType.getClass();
     FormPropertyRenderer renderer = (FormPropertyRenderer)this.propertyRenderers.get(clazz);
     
     if (renderer == null)
     {
       throw new ActivitiIllegalArgumentException("No property renderer found for type: " + formType.getName() + ", " + formType.getClass());
     }
     return renderer;
   }
   
   public FormPropertyRenderer getTypeLessFormPropertyRenderer() {
     return this.noTypePropertyRenderer;
   }
   
   public void setNoTypePropertyRenderer(FormPropertyRenderer noTypePropertyRenderer) {
     this.noTypePropertyRenderer = noTypePropertyRenderer;
   }
   
   public void setPropertyRenderers(List<FormPropertyRenderer> propertyRenderers)
   {
     for (FormPropertyRenderer propertyRenderer : propertyRenderers) {
       addFormPropertyRenderer(propertyRenderer);
     }
   }
 }


