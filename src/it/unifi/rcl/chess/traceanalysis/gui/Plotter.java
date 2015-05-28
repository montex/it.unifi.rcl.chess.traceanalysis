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
		XYSeries s = new XYSeries(name);
		for(int i = 0; i < values.length; i++) {
			s.add(i+1, values[i]);
		}
		return s;
	}
	
	public static XYSeries traceToSeries(Trace t) {
		XYSeries s = new XYSeries(t.getName());
		for(int i = 0; i < t.getSampleSize(); i++) {
			s.add(i+1, t.getValueAt(i));
		}
		return s;
	}
}