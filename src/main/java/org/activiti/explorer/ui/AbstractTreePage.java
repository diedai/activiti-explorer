 package org.activiti.explorer.ui;
 
 import com.vaadin.ui.AbstractSelect;
 import com.vaadin.ui.Tree;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class AbstractTreePage
   extends AbstractPage
 {
   private static final long serialVersionUID = 1L;
   
   protected AbstractSelect createSelectComponent()
   {
     Tree tree = createTree();
     tree.setSizeFull();
     return tree;
   }
   
   protected abstract Tree createTree();
   
   public void refreshSelectNext()
   {
     throw new UnsupportedOperationException();
   }
   
   public void selectElement(int index)
   {
     throw new UnsupportedOperationException();
   }
 }


