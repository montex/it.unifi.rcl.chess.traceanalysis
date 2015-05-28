package it.unifi.rcl.chess.traceanalysis.gui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

public class JDynamicTable extends JTable {
	
	private HashSet<Integer> triggerCols = new HashSet<Integer>();

	public void editingStopped(ChangeEvent e) {
		super.editingStopped(e);
		adaptRowCount();	
	}
	
	private void adaptRowCount() {	
		int iLastNonEmpty = -1;
		for(int i = 0; i < getRowCount(); i++) {
			if(rowHasEmptyInput(i)) {
				if(i != getRowCount()-1) {
					((DefaultTableModel)getModel()).removeRow(i);
					i--;
				}
			}else{
				iLastNonEmpty = i;
			}
		}
		
		if(iLastNonEmpty == getRowCount()-1) {
			((DefaultTableModel)getModel()).addRow(new Object[] { });
		}
	}
	
	public void setMonitoredColumn(int column) {
		triggerCols.add(column);
	}

	public void unsetMonitoredColumn(int column) {
		triggerCols.remove(column);
	}
	
	private boolean rowHasEmptyInput(int row) {
		boolean isEmpty = true;
		
		int value = 0;
		Iterator<Integer> it = triggerCols.iterator();
		while(it.hasNext()) {
			value = it.next();
			isEmpty = isEmpty && (getValueAt(row, value) == null);
		}

		return isEmpty;
	}
	
	public void clear() {
		for(int i = 0; i < getRowCount(); i++) {
			for(int j = 0; j < getColumnCount(); j++) {
				setValueAt(null, i, j);
			}
		}
		adaptRowCount();
	}
	
	@Override
	public void setValueAt(Object aValue, int row, int column) {
		super.setValueAt(aValue, row, column);
		adaptRowCount();
	}
}
