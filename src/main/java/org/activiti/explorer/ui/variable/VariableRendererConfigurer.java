 package org.activiti.explorer.ui.variable;
 
 import java.io.Serializable;
 import org.springframework.beans.factory.InitializingBean;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class VariableRendererConfigurer
   implements InitializingBean, Serializable
 {
   private VariableRenderer renderer;
   private VariableRendererManager variableRendererManager;
   
   public void afterPropertiesSet()
     throws Exception
   {
     if ((this.variableRendererManager != null) && (this.renderer != null)) {
       this.variableRendererManager.addVariableRenderer(this.renderer);
     } else {
       throw new IllegalStateException("Both renderer and variableRenderManager should be set");
     }
   }
   
   public void setRenderer(VariableRenderer renderer) {
     this.renderer = renderer;
   }
   
   public void setVariableRendererManager(VariableRendererManager variableRendererManager) {
     this.variableRendererManager = variableRendererManager;
   }
 }


