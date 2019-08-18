 package org.activiti.explorer.data;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.util.ObjectProperty;
 import java.util.Collection;
 import java.util.Map;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MapItem
   implements Item
 {
   private static final long serialVersionUID = 5079849025046231994L;
   protected Map<? extends Object, Object> map;
   
   public MapItem(Map<? extends Object, Object> map)
   {
     this.map = map;
   }
   
   public Property getItemProperty(Object id) {
     Object obj = this.map.get(id);
     if (obj == null) {
       return null;
     }
     return new ObjectProperty(obj);
   }
   
   public Collection<?> getItemPropertyIds() {
     return this.map.keySet();
   }
   
   public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
     throw new UnsupportedOperationException();
   }
   
   public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
     throw new UnsupportedOperationException();
   }
 }


