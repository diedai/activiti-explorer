 package org.activiti.explorer.ui;
 
 import com.vaadin.data.Container;
 import com.vaadin.ui.AbstractSelect;
 import com.vaadin.ui.Table;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class AbstractTablePage
   extends AbstractPage
 {
   private static final long serialVersionUID = 1L;
   protected Table table;
   
   protected AbstractSelect createSelectComponent()
   {
     this.table = createList();
     
 
     this.table.setEditable(false);
     this.table.setImmediate(true);
     this.table.setSelectable(true);
     this.table.setNullSelectionAllowed(false);
     this.table.setSortDisabled(true);
     this.table.setSizeFull();
     return this.table;
   }
   
 
 
 
 
   protected abstract Table createList();
   
 
 
 
 
   public void refreshSelectNext()
   {
     Integer pageIndex = (Integer)this.table.getCurrentPageFirstItemId();
     Integer selectedIndex = (Integer)this.table.getValue();
     this.table.removeAllItems();
     
 
     this.table.getContainerDataSource().removeAllItems();
     
 
     Integer max = Integer.valueOf(this.table.getContainerDataSource().size());
     if (max.intValue() != 0) {
       if (pageIndex.intValue() > max.intValue()) {
         pageIndex = Integer.valueOf(max.intValue() - 1);
       }
       if (selectedIndex.intValue() > max.intValue()) {
         selectedIndex = Integer.valueOf(max.intValue() - 1);
       }
       this.table.setCurrentPageFirstItemIndex(pageIndex.intValue());
       selectElement(selectedIndex.intValue());
     } else {
       this.table.setCurrentPageFirstItemIndex(0);
     }
   }
   
   public void selectElement(int index) {
     if (this.table.getContainerDataSource().size() > index) {
       this.table.select(Integer.valueOf(index));
       this.table.setCurrentPageFirstItemId(Integer.valueOf(index));
     }
   }
 }


