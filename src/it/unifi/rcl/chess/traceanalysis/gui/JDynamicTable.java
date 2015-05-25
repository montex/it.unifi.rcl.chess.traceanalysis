package it.unifi.rcl.chess.traceanalysis.gui;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

public class JDynamicTable extends JTable {

	public void editingStopped(ChangeEvent e) {
		super.editingStopped(e);
		adaptRowCount();
	}
	
	private void adaptRowCount() {	
		int iLastNonEmpty = 0;
		for(int i = 0; i < getRowCount(); i++) {
			if(getValueAt(i, 0) == null && getValueAt(i, 1) == null) {
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
}
