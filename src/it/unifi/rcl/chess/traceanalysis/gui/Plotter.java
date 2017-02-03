/*******************************************************************************
 * Copyright (c) 2015-2017 Resilient Computing Lab, University of Firenze.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Leonardo Montecchi		lmontecchi@unifi.it
 ******************************************************************************/
package it.unifi.rcl.chess.traceanalysis.gui;

import it.unifi.rcl.chess.traceanalysis.Trace;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Plotter {

	public static JPanel assemblePlot(XYSeries series[]) {	
		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		for(int i = 0; i < series.length; i++){
			dataset.addSeries(series[i]);
		}
			
		// Generate the graph
		JFreeChart chart = ChartFactory.createXYLineChart(
		"XY Chart",
		// Title
		"x-axis",
		// x-axis Labels
		"y-axis",
		// y-axis Label
		dataset,
		// Dataset
		PlotOrientation.VERTICAL, // Plot Orientation
		false,
		// Show Legend
		true,
		// Use tooltips
		false
		// Configure chart to generate URLs?
		);
		
		return new ChartPanel(chart);
	}
	
	public static XYSeries arrayToSeries(double[] values, String name) {
		return arrayToSeries(values, name, 0);
	}
	
	public static XYSeries valueToSeries(double value, String name, int lenght) {
		double[] values = new double[lenght];
		for(int i = 0; i < lenght; i++) {
			values[i] = value;
		}
		return arrayToSeries(values, name, 0);
	}
	
	public static XYSeries arrayToSeries(double[] values, String name, int offset) {
		XYSeries s = new XYSeries(name);
		
		for(int i = 0; i < offset; i++) {
			s.add(i+1,null);
		}
		
		for(int i = 0 + offset ; i < values.length + offset; i++) {
			s.add(i+1, values[i-offset]);
		}
		return s;
	}

	public static XYSeries arrayToSeriesInvert(double[] values, String name, int offset) {
		for(int i = 0; i < values.length; i++) {
			values[i] = -values[i];
		}
		return arrayToSeries(values, name, offset);
	}
	
	public static XYSeries traceToSeries(Trace t) {
		XYSeries s = new XYSeries(t.getName());
		for(int i = 0; i < t.getSampleSize(); i++) {
			s.add(i+1, t.getValueAt(i));
		}
		return s;
	}
}
