package it.unifi.rcl.chess.traceanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import monitoringService.MonitoringService;
import monitoringService.distributions.Distribution;
import monitoringService.mechanisms.MechanismAD_KS;
import monitoringService.util.MonitoringServiceException;

public class Trace {
	
	private static char SKIPLINE_CHAR = '#'; 		// character meaning that a line should be discarded
	private static String NO_NAME = "{unnamed}";
	
	private ArrayList<Double> data;					// the data
	private String name = NO_NAME;
	
	public Trace() {
		data = new ArrayList<Double>();
	}
	
	public Trace(File file) {
		this();
		name = file.getAbsolutePath();
		
		try {
			InputStream is = new FileInputStream(file);
			// check if the file is compressed with gzip
			boolean isZipped = Trace.checkGZIP(is);
			is.close();
			
			is = new FileInputStream(file);
			if(isZipped) {
				is = new GZIPInputStream(is);
			}
			
			loadFromStream(is);
			
			is.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Trace(InputStream is) {
		this();
		try {
			loadFromStream(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getName(int lenght) {
		return Utils.abbreviateMiddle(getName(), "...", lenght);
	}
	
	private void loadFromStream(InputStream is) throws IOException {
		int numLines = 0, numSkipped = 0;
		
		Reader decoder = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(decoder);
		
		String line = "";			
		double tmpValue = 0;
		
		while((line = br.readLine()) != null) {
//			System.out.println(line);
			line = line.trim();

			if(line.charAt(0) != SKIPLINE_CHAR) {
				try {
					tmpValue = Double.parseDouble(line);
					data.add(tmpValue);
				}
				catch(NumberFormatException nfe) {
					System.out.println("Warning: Line " + (numLines+1) + " in the dataset has been skipped");
					System.out.println(line);
					numSkipped++;
				}
			}else{
				numSkipped++;
			}
			numLines++;
		}		
	}
	
	public static void setSkipLineChar(char c) {
		SKIPLINE_CHAR = c;
	}
	
	public static char getSkipLineChar() {
		return SKIPLINE_CHAR;
	}
	
	public int getSampleSize() {
		return data.size();
	}
	
	public Trace getSubTrace(int start, int end) {
		Trace t = new Trace();
		
		// in List.subList the second parameter is exclusive, index start from 0
		t.data = new ArrayList<Double>(data.subList(start-1, end)); 
		
		return t;
	}
	
	public double getMin() {
		return Collections.min(data);
	}
	
	public double getMax() {
		return Collections.max(data);
	}
	
	public double getAverage() {
		double sum = 0;
		
		Iterator<Double> i = data.iterator();
		while(i.hasNext()) {
			sum += i.next();
		}
		
		return sum/data.size();
	}
	
	public double getBound(double coverage) {
		
		double ret = 0;
		forbidSystemExitCall();
		try {
			ret = MonitoringService.getBound(data, coverage, data.size());
		} catch (MonitoringServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExitTrappedException e) {
			ret = Double.NaN;
		} finally {
			enableSystemExitCall();
		}
		return ret;
	}
	
	public Distribution getDistribution(double coverage) {
		Distribution d = null;
		forbidSystemExitCall();
		try {
			MechanismAD_KS m = new MechanismAD_KS();
			d = m.getDistribution(data, coverage, data.size());
		} catch (MonitoringServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExitTrappedException e) {
			d = new Distribution();
		} finally {
			enableSystemExitCall();
		}
		return d;
	}
	
	public String getDistributionReadable(double coverage) {
		return Utils.distributionToString(getDistribution(coverage));
	}
	
	public Distribution[] getPhases(double coverage, int window) {
		int len = data.size();
		Distribution d = null;
		Distribution[] dArray = new Distribution[len];
		
		for(int i = 0; i + window < len; i++) {
			try {
				d = getSubTrace(i+1, i+window).getDistribution(coverage);
			}catch (Exception e) {
				e.printStackTrace();
			}
			dArray[i] = d;
			
			System.out.println(i + " > " +Utils.distributionToString(d));
		}
		
		return dArray;
	}
	
	public JPanel plotDynamicBound(double coverage, int window) {
		// Create a simple XY chart
		XYSeries series = new XYSeries("Samples");
		XYSeries bounds = new XYSeries("Bounds");
		
		for(int i = 0; i < data.size(); i++) {
			series.add(i+1, data.get(i));
		}

		int len = data.size();
		double b = 0;
		for(int i = 0; i + window < len; i++) {
			b = 0;
			try {
				b = getSubTrace(i+1, i+window).getBound(coverage);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			if(i == 0) {
				for(int j = i; j < i + window; j++) {
					bounds.add(j, b);
				}
			}else{
				bounds.add(i + window, b);
			}
//			for(int j = i; j < i + window; j++) {
//				bounds.add(j+1, b);
//				System.out.println((j+1)+" "+b);
//			}
			System.out.println("-------------------------");
		}
		
		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		dataset.addSeries(bounds);
		
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
		
		try {
			ChartUtilities.saveChartAsJPEG(new File("/home/montex/chart.jpg"), chart, 1000, 600);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ChartPanel(chart);
	}
	
	/**
	 * Checks if the input stream is compressed, and in case it returns a GZIPInputStream
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	private static boolean checkGZIP(InputStream stream) throws IOException {
	     PushbackInputStream pb = new PushbackInputStream( stream, 2 ); //we need a pushbackstream to look ahead
	     byte [] signature = new byte[2];
	     pb.read( signature ); //read the signature
	     pb.unread( signature ); //push back the signature to the stream
	     if( signature[ 0 ] == (byte) 0x1f && signature[ 1 ] == (byte) 0x8b ) //check if matches standard gzip magic number
	       return true;
	     else 
	       return false;
	}
	
	/* This is required to avoid ADAPTARE quit the application with certain inputs */
	private static class ExitTrappedException extends SecurityException { }

	private static void forbidSystemExitCall() {
	  final SecurityManager securityManager = new SecurityManager() {
	    public void checkPermission( Permission permission ) {
	      if( permission.getName().contains("exitVM") ) {
	        throw new ExitTrappedException() ;
	      }
	    }
	  } ;
	  System.setSecurityManager( securityManager ) ;
	}

	private static void enableSystemExitCall() {
	  System.setSecurityManager( null ) ;
	}
}
