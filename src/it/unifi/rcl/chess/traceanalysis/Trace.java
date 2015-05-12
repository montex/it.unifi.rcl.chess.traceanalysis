package it.unifi.rcl.chess.traceanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

public class Trace {
	
	private static char SKIPLINE_CHAR = '#'; 		// character meaning that a line should be discarded
	
	private ArrayList<Double> data;					// the data
	private int numLines = 0;
	private int numSkipped = 0;
	
	public Trace() {
		data = new ArrayList<Double>();
	}
	
	public Trace(String fileName) {
		this(new File(fileName));
	}
	
	public Trace(File file) {
		this();
		
		try {
			InputStream is = new FileInputStream(file);
			// check if the file is compressed with gzip
			boolean isZipped = Trace.checkGZIP(is);
			is.close();
			
			is = new FileInputStream(file);
			if(isZipped) {
				is = new GZIPInputStream(is);
			}
			
			Reader decoder = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(decoder);
			
			String line = "";			
			double tmpValue = 0;
			
			while((line = br.readLine()) != null) {
				System.out.println(line);
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
			
			br.close();
			is.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setSkipLineChar(char c) {
		SKIPLINE_CHAR = c;
	}
	public static char getSkipLineChar() {
		return SKIPLINE_CHAR;
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
}
