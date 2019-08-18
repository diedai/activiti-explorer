 package org.activiti.explorer.ui.management.db;
 
 import com.vaadin.data.Item;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.management.TablePage;
 import org.activiti.engine.management.TablePageQuery;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 import org.activiti.explorer.data.MapItem;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TableDataQuery
   extends AbstractLazyLoadingQuery
 {
   protected String tableName;
   protected transient ManagementService managementService;
   protected Object[] sortPropertyIds;
   protected boolean[] sortPropertyIdsAscending;
   
   public TableDataQuery(String tableName, ManagementService managementService)
   {
     this.tableName = tableName;
     this.managementService = managementService;
   }
   
   public List<Item> loadItems(int start, int count) {
     TablePageQuery query = this.managementService.createTablePageQuery().tableName(this.tableName);
     
     if ((this.sortPropertyIds != null) && (this.sortPropertyIds.length > 0)) {
       for (int i = 0; i < this.sortPropertyIds.length; i++) {
         String column = (String)this.sortPropertyIds[i];
         if (this.sortPropertyIdsAscending[i] == 1) {
           query.orderAsc(column);
         } else {
           query.orderDesc(column);
         }
       }
     }
     
     List<Map<String, Object>> rows = query.listPage(start, count).getRows();
     List<Item> items = new ArrayList();
     for (Map<String, Object> row : rows)
     {
       HashMap<String, Object> newRow = new HashMap();
       for (Map.Entry<String, Object> rowEntry : row.entrySet()) {
         String key = (String)rowEntry.getKey();
         if (key != null) {
           key = key.toUpperCase();
         }
         newRow.put(key, rowEntry.getValue());
       }
       
       items.add(new MapItem(newRow));
     }
     return items;
   }
   
   public int size() {
     return ((Long)this.managementService.getTableCount().get(this.tableName)).intValue();
   }
   
   public void setSorting(Object[] propertyId, boolean[] ascending) {
     this.sortPropertyIds = propertyId;
     this.sortPropertyIdsAscending = ascending;
   }
   
   public Item loadSingleResult(String id) {
     throw new UnsupportedOperationException();
   }
 }


