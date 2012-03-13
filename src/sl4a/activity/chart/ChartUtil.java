/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sl4a.activity.chart;


import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * An abstract class for the demo charts to extend.
 * It contains some methods for building datasets and renderers.
 */
//public abstract class AbstractDemoChart implements IDemoChart {
public class ChartUtil {
  /**
   * Builds an XY multiple dataset using the provided values.
   * 
   * @param titles the series titles
   * @param xValues the values for the X axis
   * @param yValues the values for the Y axis
   * @return the XY multiple dataset
   */
  public static XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
      List<double[]> yValues) {
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    addXYSeries(dataset, titles, xValues, yValues, 0);
    return dataset;
  }

  public static void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
      List<double[]> yValues, int scale) {
    int length = titles.length;
    for (int i = 0; i < length; i++) {
      XYSeries series = new XYSeries(titles[i], scale);
      double[] xV = xValues.get(i);
      double[] yV = yValues.get(i);
      int seriesLength = xV.length;
      for (int k = 0; k < seriesLength; k++) {
        series.add(xV[k], yV[k]);
      }
      dataset.addSeries(series);
    }
  }

  /**
   * Builds an XY multiple series renderer.
   * 
   * @param colors the series rendering colors
   * @param styles the series point styles
   * @return the XY multiple series renderers
   */
  public static XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
    setRenderer(renderer, colors, styles);
    return renderer;
  }

  protected static void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
    renderer.setAxisTitleTextSize(16);
    renderer.setChartTitleTextSize(20);
    renderer.setLabelsTextSize(15);
    renderer.setLegendTextSize(15);
    renderer.setPointSize(5f);
    renderer.setMargins(new int[] { 20, 30, 15, 20 });
    int length = colors.length;
    for (int i = 0; i < length; i++) {
      XYSeriesRenderer r = new XYSeriesRenderer();
      r.setColor(colors[i]);
      r.setPointStyle(styles[i]);
      renderer.addSeriesRenderer(r);
    }
  }


  /**
   * Builds a category renderer to use the provided colors.
   * 
   * @param colors the colors
   * @return the category renderer
   */
  public static DefaultRenderer buildCategoryRenderer(int[] colors) {
    DefaultRenderer renderer = new DefaultRenderer();
    renderer.setLabelsTextSize(15);
    renderer.setLegendTextSize(15);
//    renderer.setMargins(new int[] { 20, 30, 15, 0 });
    for (int color : colors) {
      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
      r.setColor(color);
      renderer.addSeriesRenderer(r);
    }
    return renderer;
  }


  /**
   * Builds a bar multiple series renderer to use the provided colors.
   * 
   * @param colors the series renderers colors
   * @return the bar multiple series renderer
   */
  public static XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
    renderer.setAxisTitleTextSize(16);
    renderer.setChartTitleTextSize(20);
    renderer.setLabelsTextSize(15);
    renderer.setLegendTextSize(15);
    int length = colors.length;
    for (int i = 0; i < length; i++) {
      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
      r.setColor(colors[i]);
      renderer.addSeriesRenderer(r);
    }
    return renderer;
  }

}
