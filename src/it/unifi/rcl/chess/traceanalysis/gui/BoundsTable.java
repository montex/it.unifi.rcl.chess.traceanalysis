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

import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

public class BoundsTable extends JDynamicTable {

	private Trace trace;
	
	public BoundsTable() {
		super();
		
		initialize();
		
		setMonitoredColumn(0);
		setMonitoredColumn(1);
		setMonitoredColumn(2);
	}
	
	public BoundsTable(Trace t) {
		this();
		setTrace(t);
	}
	
	public void editingStopped(ChangeEvent e) {
		super.editingStopped(e);
		updateValues();
	}
	
	public void clear() {
		initialize();
	}
	
	private void initialize() {
		setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null, null, null},
					{null, null, null, null, null}
				},
				new String[] {
					"Start", "End", "Confidence", "Distribution*", "Bound*"
				}
			) {
			    @Override
			    public boolean isCellEditable(int row, int column) {
			       return (column < 3);
			    }
			
				Class[] columnTypes = new Class[] {
					Integer.class, Integer.class, Double.class, String.class, Double.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
		
		updateValues();
	}
	
	public void updateValues(int row) {
		int n1 = 0, n2 = 0;
		double c = 0;
		Trace tempTrace = null;
		
		if(getValueAt(row, 0) == null || getValueAt(row, 1) == null || getValueAt(row, 2) == null) {
			setValueAt(null, row, 3);
			setValueAt(null, row, 4);
		}else {
			try {
				n1 = (Integer)getValueAt(row, 0);
				n2 = (Integer)getValueAt(row, 1);
				c = (Double)getValueAt(row, 2);
				
				tempTrace = trace.getSubTrace(n1-1, n2-1);
				setValueAt(tempTrace.getDistributionReadable(c), row, 3);
				setValueAt(tempTrace.getBound(c), row, 4);			
			}
			catch(Exception e) {
				setValueAt(e, row, 3);
				setValueAt(null, row, 4);
				e.printStackTrace();
			}
		}		
	}
	
	public void updateValues() {
		for(int i = 0; i < getRowCount(); i++) {
			updateValues(i);
		}
	}
	
	public void setTrace(Trace t) {
		trace = t;
		reset();
	}
	
	public void reset() {
		clear();

		setValueAt(1, 0, 0);
		setValueAt(trace.getSampleSize(), 0, 1);
		setValueAt(0.99, 0, 2);
		
		updateValues();
	}
}
