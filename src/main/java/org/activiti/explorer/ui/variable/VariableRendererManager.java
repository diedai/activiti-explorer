 package org.activiti.explorer.ui.variable;
 
 import java.io.Serializable;
 import java.util.HashMap;
 import java.util.Map;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class VariableRendererManager
   implements Serializable
 {
   private Map<Class<?>, VariableRenderer> renderers = new HashMap();
   
 
 
 
   public void addVariableRenderer(VariableRenderer renderer)
   {
     this.renderers.put(renderer.getVariableType(), renderer);
   }
   
 
 
   public VariableRenderer getVariableRenderer(Class<?> variableType)
   {
     return (VariableRenderer)this.renderers.get(variableType);
   }
   
 
 
 
   public String getStringRepresentation(Object variableValue)
   {
     if (variableValue != null) {
       VariableRenderer renderer = getVariableRenderer(variableValue.getClass());
       if (renderer != null) {
         return renderer.getStringRepresentation(variableValue);
       }
       return variableValue.toString();
     }
     
     return null;
   }
 }


