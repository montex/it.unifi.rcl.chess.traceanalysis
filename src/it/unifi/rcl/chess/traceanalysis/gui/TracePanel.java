package it.unifi.rcl.chess.traceanalysis.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import it.unifi.rcl.chess.traceanalysis.Trace;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import monitoringService.MonitoringService;

public class TracePanel extends JPanel {
	
	private static final String TXT_NOFILE = "No file loaded.";
	
	private Trace trace;
	private File file;

	JTextField txtFile;
	JButton btnReload;
	JButton btnClose;
	JButton btnUpdate;
	JLabel lblSize;
	JLabel lblPoints;	
	JProgressBar progressBar;
	JScrollPane scrollPane;
	JTable table;
	JLabel lblFileName;
	JLabel lblFilePath;
	JLabel lblStat;
	JButton btnPlot;
	
	public TracePanel() {
		initialize();
	}
	
	public TracePanel(File f) {
		this();
		loadTrace(f);
	}
	
	public void setTitle(String t) {
		JTabbedPane parent = ((JTabbedPane)getParent());
		parent.setTitleAt(parent.indexOfComponent(TracePanel.this), t);
	}
	
	public String getTitle() {
		JTabbedPane parent = ((JTabbedPane)getParent());
		return parent.getTitleAt(parent.indexOfComponent(TracePanel.this));
	}

	public Trace getTrace() {
		return trace;
	}
	
	public void loadTrace(File file) {
		reset();
		trace = new Trace(file);
		this.file = file;
		
		table.setValueAt(1, 0, 0);
		table.setValueAt(trace.getSampleSize(), 0, 1);
		table.setValueAt(0.99, 0, 2);
				
		refresh();
	}
	
	public void loadTrace(String fileName) {
		loadTrace(new File(fileName));
	}
	
	public void unloadTrace() {
		reset();
	}
	
	private void reset() {
		trace = null;
		file = null;
		
		for(int i = 0; i < table.getRowCount(); i++) {
			for(int j = 0; j< table.getColumnCount(); j++) {
				table.setValueAt(null, i, j);
			}
		}
		
		refresh();
	}
	
	private void updateBounds() {
		int n1 = 0, n2 = 0;
		double c = 0;
		Trace tempTrace = null;
		
		for(int i = 0; i < table.getRowCount(); i++) {
			try {
				n1 = (Integer)table.getValueAt(i, 0);
				n2 = (Integer)table.getValueAt(i, 1);
				c = (Double)table.getValueAt(i, 2);
				
				tempTrace = trace.getSubTrace(n1, n2);
				table.setValueAt(tempTrace.getDistributionReadable(c), i, 3);
				table.setValueAt(tempTrace.getBound(c), i, 4);			
			}
			catch(Exception e) {
				table.setValueAt("Error", i, 3);
				table.setValueAt(null, i, 4);
				e.printStackTrace();
			}
		}
	}
	
	private void refresh() {		
		if(file == null) {
			lblFileName.setText(TXT_NOFILE);
			lblFilePath.setText("");			
		}else{
			lblFileName.setText(file.getName());
			lblFilePath.setText(file.getParent());
			lblFilePath.setToolTipText(file.getParent());
		}
		
		if(trace == null) {
			lblPoints.setText("No data.");
		}else{
			lblPoints.setText(trace.getSampleSize() + " points loaded.");
			
			lblStat.setText("Min: " + trace.getMin() + " Max: " + trace.getMax() + " Mean: " + trace.getAverage());
		}
		
		updateBounds();
	}

	public void dispose() {
		JTabbedPane parent = ((JTabbedPane)getParent());
		parent.removeTabAt(parent.indexOfComponent(this));
	}
	
	private void initialize() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
//		txtFile = new JTextField();
//		txtFile.setColumns(20);
//		txtFile.setText("File");
//		this.add(txtFile);
		
		btnReload = new JButton("Reload");
		btnReload.addActionListener(new ButtonAction("Reload", KeyEvent.VK_L));
		this.add(btnReload);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ButtonAction("Close", KeyEvent.VK_U));
		this.add(btnClose);
		
//		lblSize = new JLabel("Size");
//		this.add(lblSize);
		
		lblPoints = new JLabel("#Points");
		this.add(lblPoints);
		
		lblFileName = new JLabel();
		this.add(lblFileName);
		
		lblFilePath = new JLabel();
		this.add(lblFilePath);
		
		lblStat = new JLabel();
		this.add(lblStat);
		
//		progressBar = new JProgressBar();
//		this.add(progressBar);
		
		scrollPane = new JScrollPane();
		this.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"Start", "End", "Confidence", "Distribution*", "Bound*"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, Integer.class, Double.class, String.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ButtonAction("Update", KeyEvent.VK_U));
		this.add(btnUpdate);
		
		btnPlot = new JButton("Plot");
		btnPlot.addActionListener(new ButtonAction("Plot", KeyEvent.VK_P));
		this.add(btnPlot);
		
		reset();
	}
	
	private class ButtonAction extends AbstractAction {
		
		public ButtonAction(String name, Integer mnemonic) {
			super(name);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Object src = e.getSource();
			
			if(src == btnReload) {
				loadTrace(txtFile.getText());
				refresh();
			}else if(src == btnClose) {
				dispose();
			}else if(src == btnUpdate) {
				updateBounds();
			}else if(src == btnPlot) {
				JPanel plotPanel = trace.plotDynamicBound(0.99, 200); 
				JFrame plotFrame = new JFrame();
				plotFrame.add(plotPanel);
				plotFrame.setVisible(true);
				plotFrame.pack();
				plotFrame.setTitle(TracePanel.this.file.getName());
			}
		};
	}
}
