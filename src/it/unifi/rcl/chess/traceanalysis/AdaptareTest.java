package it.unifi.rcl.chess.traceanalysis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import monitoringService.MonitoringService;
import monitoringService.util.MonitoringServiceException;

public class AdaptareTest {
	
	private static int MAX_LINES = 10000;
	
	private static double[] P_BOUND = { .8,.9,.92,.95,.98,.99,.999,(1-1E-6),(1-1E-9) };
//	private static int WINDOW_SIZE = MAX_LINES;

	public static void main(String[] args) {
		
		String filename = "/home/montex/Lavoro/RCL/Progetti/CONCERTO/WP4/Adaptare/datasets/household_power_consumption.txt.gz";
		
		try {
			InputStream fileStream = new FileInputStream(filename);
			InputStream gzipStream = new GZIPInputStream(fileStream);
			Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
			BufferedReader br = new BufferedReader(decoder);
			
			String line = "";
			String[] tmpSplit = new String[9];
						
			br.readLine();		// discards headers line
			int numLines = 0;
			int numSkipped = 0;
			
			ArrayList<Double> alActivePower = new ArrayList<Double>();
			ArrayList<Double> alReactivePower = new ArrayList<Double>();
			ArrayList<Double> alVoltage = new ArrayList<Double>();
			ArrayList<Double> alIntensity = new ArrayList<Double>();
			ArrayList<Double> alSubMetering1 = new ArrayList<Double>();
			ArrayList<Double> alSubMetering2 = new ArrayList<Double>();
			ArrayList<Double> alSubMetering3 = new ArrayList<Double>();
			double tmpActivePower = 0;
			double tmpReactivePower = 0;
			double tmpVoltage = 0;
			double tmpIntensity = 0;
			double tmpSubMetering1 = 0;
			double tmpSubMetering2 = 0;
			double tmpSubMetering3 = 0;
			
			while((line = br.readLine()) != null && numLines < MAX_LINES) {
//				System.out.println(line);
				
				tmpSplit = line.split(";");
				try {
					tmpActivePower = Double.parseDouble(tmpSplit[2]);
					tmpReactivePower= Double.parseDouble(tmpSplit[3]);
					tmpVoltage = Double.parseDouble(tmpSplit[4]);
					tmpIntensity = Double.parseDouble(tmpSplit[5]);
					tmpSubMetering1 = Double.parseDouble(tmpSplit[6]);
					tmpSubMetering2 = Double.parseDouble(tmpSplit[7]);
					tmpSubMetering3 = Double.parseDouble(tmpSplit[8]);
				
					alActivePower.add(tmpActivePower);
					alReactivePower.add(tmpReactivePower);
					alVoltage.add(tmpVoltage);
					alIntensity.add(tmpIntensity);
					alSubMetering1.add(tmpSubMetering1);
					alSubMetering2.add(tmpSubMetering2);
					alSubMetering3.add(tmpSubMetering3);
				}
				catch(NumberFormatException nfe) {
					System.out.println("Warning: Line " + (numLines+2) + " in the dataset has been skipped");
					System.out.println(line);
					numSkipped++;
				}
				
				numLines++;
			}
			
			br.close();
			
			analyzeSeries(alActivePower, "Active Power", P_BOUND);
			analyzeSeries(alReactivePower, "Reactive Power", P_BOUND);
			analyzeSeries(alVoltage, "Voltage", P_BOUND);
			analyzeSeries(alIntensity, "Intensity", P_BOUND);
			analyzeSeries(alSubMetering1, "Sub Metering 1", P_BOUND);
			analyzeSeries(alSubMetering2, "Sub Metering 2", P_BOUND);
			analyzeSeries(alSubMetering3, "Sub Metering 3", P_BOUND);
			
		}
		catch(IOException e) {
			e.printStackTrace();			
		}
	}
	
	private static void analyzeSeries(ArrayList<Double> data, String name, double[] coverage) {
		
		try {
				
			double average = 0;
			Iterator<Double> it = data.iterator();
			while (it.hasNext()) {
				average += it.next();
			}
			average = average/data.size();
			
			System.out.println("==================================================");
			System.out.println(">>> " + name + " <<<");
			
			System.out.println("Max: " + Collections.max(data));
			System.out.println("Min: " + Collections.min(data));
			System.out.println("Average: " + average);
			
			for(int i=0; i<coverage.length; i++) {
				double bound = MonitoringService.getBound(data, coverage[i], data.size());
				System.out.println("Bound (" + coverage[i]*100 + "%): " + bound);
			}
			
			
		}catch(MonitoringServiceException mse) {
			mse.printStackTrace();
		}
	}

}
