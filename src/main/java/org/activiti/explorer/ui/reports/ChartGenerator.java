 package org.activiti.explorer.ui.reports;
 
 import com.fasterxml.jackson.databind.JsonNode;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.Label;
 import java.util.Iterator;
 import org.activiti.engine.ActivitiException;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.dussan.vaadin.dcharts.DCharts;
 import org.dussan.vaadin.dcharts.base.elements.XYaxis;
 import org.dussan.vaadin.dcharts.base.elements.XYseries;
 import org.dussan.vaadin.dcharts.base.renderers.MarkerRenderer;
 import org.dussan.vaadin.dcharts.data.DataSeries;
 import org.dussan.vaadin.dcharts.data.Ticks;
 import org.dussan.vaadin.dcharts.metadata.LegendPlacements;
 import org.dussan.vaadin.dcharts.metadata.XYaxes;
 import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
 import org.dussan.vaadin.dcharts.metadata.renderers.LabelRenderers;
 import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
 import org.dussan.vaadin.dcharts.metadata.styles.MarkerStyles;
 import org.dussan.vaadin.dcharts.options.Axes;
 import org.dussan.vaadin.dcharts.options.AxesDefaults;
 import org.dussan.vaadin.dcharts.options.Highlighter;
 import org.dussan.vaadin.dcharts.options.Legend;
 import org.dussan.vaadin.dcharts.options.Options;
 import org.dussan.vaadin.dcharts.options.Series;
 import org.dussan.vaadin.dcharts.options.SeriesDefaults;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ChartGenerator
 {
   public static final String CHART_TYPE_BAR_CHART = "barChart";
   public static final String CHART_TYPE_PIE_CHART = "pieChart";
   public static final String CHART_TYPE_LINE_CHART = "lineChart";
   public static final String CHART_TYPE_LIST = "list";
   
   public static ChartComponent generateChart(byte[] reportData)
   {
     JsonNode jsonNode = convert(reportData);
     
 
     JsonNode titleNode = jsonNode.get("title");
     String title = null;
     if (titleNode != null) {
       title = titleNode.textValue();
     }
     
     ChartComponent chartComponent = new ChartComponent(title);
     
 
 
     JsonNode datasetsNode = jsonNode.get("datasets");
     
 
     if (datasetsNode.size() == 0) {
       chartComponent.addChart(null, null, ExplorerApp.get().getI18nManager().getMessage("reporting.error.not.enough.data"));
       return chartComponent;
     }
     
     if ((datasetsNode != null) && (datasetsNode.isArray()))
     {
       Iterator<JsonNode> dataIterator = datasetsNode.iterator();
       while (dataIterator.hasNext())
       {
         JsonNode datasetNode = (JsonNode)dataIterator.next();
         
         JsonNode descriptionNode = datasetNode.get("description");
         String description = null;
         if (descriptionNode != null) {
           description = descriptionNode.textValue();
         }
         JsonNode dataNode = datasetNode.get("data");
         
         if ((dataNode == null) || (dataNode.size() == 0)) {
           chartComponent.addChart(description, null, ExplorerApp.get().getI18nManager().getMessage("reporting.error.not.enough.data"));
         }
         else {
           String[] names = new String[dataNode.size()];
           Number[] values = new Number[dataNode.size()];
           
           int index = 0;
           Iterator<String> fieldIterator = dataNode.fieldNames();
           while (fieldIterator.hasNext()) {
             String field = (String)fieldIterator.next();
             names[index] = field;
             values[index] = dataNode.get(field).numberValue();
             index++;
           }
           
 
           if (names.length > 0) {
             Component chart = createChart(datasetNode, names, values);
             chartComponent.addChart(description, chart, null);
           } else {
             chartComponent.addChart(description, null, ExplorerApp.get().getI18nManager().getMessage("reporting.error.not.enough.data"));
           }
         }
       }
     }
     
 
 
 
 
     return chartComponent;
   }
   
   protected static Component createChart(JsonNode dataNode, String[] names, Number[] values) {
     String type = dataNode.get("type").textValue();
     
     JsonNode xAxisNode = dataNode.get("xaxis");
     String xAxis = null;
     if (xAxisNode != null) {
       xAxis = xAxisNode.textValue();
     }
     
     JsonNode yAxisNode = dataNode.get("yaxis");
     String yAxis = null;
     if (yAxisNode != null) {
       yAxis = yAxisNode.textValue();
     }
     
     Component chart = null;
     if ("barChart".equals(type))
     {
       DataSeries dataSeries = (DataSeries)new DataSeries().add((Object[])values);
       SeriesDefaults seriesDefaults = new SeriesDefaults().setRenderer(SeriesRenderers.BAR);
       Axes axes = new Axes().addAxis(new XYaxis().setRenderer(AxisRenderers.CATEGORY).setTicks((Ticks)new Ticks().add((Object[])names)));
       Highlighter highlighter = new Highlighter().setShow(false);
       
       Options options = new Options().setSeriesDefaults(seriesDefaults).setAxes(axes).setHighlighter(highlighter);
       options.setAnimate(true);
       options.setAnimateReplot(true);
       
       chart = new DCharts().setDataSeries(dataSeries).setOptions(options);
     }
     else if ("pieChart".equals(type))
     {
       DataSeries dataSeries = (DataSeries)new DataSeries().newSeries();
       for (int i = 0; i < names.length; i++) {
         dataSeries.add(new Object[] { names[i], values[i] });
       }
       SeriesDefaults seriesDefaults = new SeriesDefaults().setRenderer(SeriesRenderers.PIE);
       
       Options options = new Options().setSeriesDefaults(seriesDefaults);
       options.setAnimate(true);
       options.setAnimateReplot(true);
       
       Legend legend = new Legend().setShow(true).setPlacement(LegendPlacements.INSIDE);
       options.setLegend(legend);
       
       Highlighter highlighter = new Highlighter().setShow(true);
       options.setHighlighter(highlighter);
       
       chart = new DCharts().setDataSeries(dataSeries).setOptions(options);
     }
     else if ("lineChart".equals(type))
     {
       AxesDefaults axesDefaults = new AxesDefaults().setLabelRenderer(LabelRenderers.CANVAS);
       
 
       Axes axes = new Axes().addAxis(new XYaxis().setLabel(xAxis != null ? xAxis : "").setMin(names[0]).setMax(names[(values.length - 1)]).setDrawMajorTickMarks(true)).addAxis(new XYaxis(XYaxes.Y).setLabel(yAxis != null ? yAxis : "").setDrawMajorTickMarks(true));
       Options options = new Options().setAxesDefaults(axesDefaults).setAxes(axes);
       DataSeries dataSeries = (DataSeries)new DataSeries().newSeries();
       for (int i = 0; i < names.length; i++)
       {
 
 
 
 
 
 
 
 
         dataSeries.add(new Object[] { names[i], values[i] });
       }
       
 
       Series series = new Series().addSeries(new XYseries()
         .setShowLine(true).setMarkerOptions(new MarkerRenderer().setShadow(true).setSize(7.0F).setStyle(MarkerStyles.CIRCLE)));
       options.setSeries(series);
       
       options.setAnimate(true);
       options.setAnimateReplot(true);
       
       Highlighter highlighter = new Highlighter().setShow(true);
       options.setHighlighter(highlighter);
       
       chart = new DCharts().setDataSeries(dataSeries).setOptions(options);
     }
     else if ("list".equals(type))
     {
       GridLayout grid = new GridLayout(2, names.length);
       grid.setSpacing(true);
       
       for (int i = 0; i < names.length; i++) {
         String name = names[i];
         Label nameLabel = new Label(name);
         nameLabel.addStyleName("bold");
         grid.addComponent(nameLabel, 0, i);
         
         Number value = values[i];
         Label valueLabel = new Label(value + "");
         grid.addComponent(valueLabel, 1, i);
       }
       
       chart = grid;
     }
     
 
     if ((chart instanceof DCharts))
     {
       ((DCharts)chart).show();
     }
     
     return chart;
   }
   
   protected static JsonNode convert(byte[] jsonBytes) {
     try {
       ObjectMapper objectMapper = new ObjectMapper();
       return objectMapper.readTree(jsonBytes);
     } catch (Exception e) {
       throw new ActivitiException("Report dataset contains invalid json", e);
     }
   }
   
   protected static Long parseLong(String s) {
     try {
       return Long.valueOf(Long.parseLong(s));
     }
     catch (NumberFormatException e) {}
     return null;
   }
   
   protected static Double parseDouble(String s)
   {
     try {
       return Double.valueOf(Double.parseDouble(s));
     }
     catch (NumberFormatException e) {}
     return null;
   }
 }


