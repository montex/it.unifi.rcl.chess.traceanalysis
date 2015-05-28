package it.unifi.rcl.chess.traceanalysis.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import it.unifi.rcl.chess.traceanalysis.Trace;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
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
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import monitoringService.MonitoringService;

public class TracePanel extends JPanel {
	
	private static final String TXT_NOFILE = "No file loaded.";
	
	private Trace trace;

	JTextField txtFile;
	JButton btnReload, btnClose;
	JButton btnUpdateBoundsTable, btnClearBoundsTable;
	JButton btnPhaseDetection;
	JLabel lblSize;
	JLabel lblPoints;	
	JProgressBar progressBar;
	JScrollPane scrollTabBounds, scrollTabWSize;
	BoundsTable tableBounds;
	JDynamicTable tableWindowSize;
	JLabel lblTraceName;
	JLabel lblStat;
	JButton btnPlot, btnClearWSizeTable;
	
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
	
	public void loadTrace(File file) {;
		loadTrace(new Trace(file), file.getAbsolutePath());
	}
	
	public void loadTrace(Trace t, String name) {	
		trace = t;
		
		tableBounds.setTrace(trace);
		
		refresh();
	}	

	private void refresh() {		
		lblTraceName.setText(trace.getName(30));
		lblTraceName.setToolTipText(trace.getName());;
		
		if(trace == null) {
			lblPoints.setText("No data.");
		}else{
			lblPoints.setText(trace.getSampleSize() + " points loaded.");
			
			lblStat.setText("<html>Min: " + trace.getMin() + 
							"<br/>Max: " + trace.getMax() + 
							"<br/>Average: " + trace.getAverage() + "</html>");
		}
		
		tableBounds.updateValues();
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
		
		JPanel pnlInfo = new JPanel();
		JPanel pnlBound = new JPanel();
		JPanel pnlPlot = new JPanel();
		JPanel pnlPhases = new JPanel();
		
		pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		pnlBound.setLayout(new BoxLayout(pnlBound, BoxLayout.Y_AXIS));
		pnlPlot.setLayout(new BoxLayout(pnlPlot, BoxLayout.Y_AXIS));
		pnlPhases.setLayout(new BoxLayout(pnlPhases, BoxLayout.Y_AXIS));
		
		this.add(pnlInfo);
		this.add(pnlBound);
		this.add(pnlPlot);
		this.add(pnlPhases);
		
		lblPoints = new JLabel("#Points");
		pnlInfo.add(lblPoints);
		
		lblTraceName = new JLabel();
		pnlInfo.add(lblTraceName);
				
		btnReload = new JButton("Reload");
		btnReload.addActionListener(new ButtonAction("Reload", KeyEvent.VK_L));
		pnlInfo.add(btnReload);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ButtonAction("Close", KeyEvent.VK_U));
		pnlInfo.add(btnClose);
		
		lblStat = new JLabel();
		pnlInfo.add(lblStat);
		
//		progressBar = new JProgressBar();
//		this.add(progressBar);
		
		scrollTabBounds = new JScrollPane();
		scrollTabBounds.setPreferredSize(new Dimension(400,100));
		pnlBound.add(scrollTabBounds);
		
		tableBounds = new BoundsTable();
		scrollTabBounds.setViewportView(tableBounds);
		
		
		btnUpdateBoundsTable = new JButton("Update");
		btnUpdateBoundsTable.addActionListener(new ButtonAction("Update", KeyEvent.VK_U));
		pnlBound.add(btnUpdateBoundsTable);
		
		btnClearBoundsTable = new JButton("Clear Table");
		btnClearBoundsTable.addActionListener(new ButtonAction("Clear Table", KeyEvent.VK_C));
		pnlBound.add(btnClearBoundsTable);
		
		scrollTabWSize = new JScrollPane();
		scrollTabWSize.setPreferredSize(new Dimension(100,100));
		pnlPlot.add(scrollTabWSize);
		
		tableWindowSize = new JDynamicTable();
		tableWindowSize.setModel(new DefaultTableModel(
				new Object[][] {
						{100,0.99},
						{null,null}
					},
					new String[] {
						"WindowSize", "Confidence"
					}
				) {
			
					Class[] columnTypes = new Class[] {
						Integer.class, Double.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
		tableWindowSize.setMonitoredColumn(0);
		tableWindowSize.setMonitoredColumn(1);
		scrollTabWSize.setViewportView(tableWindowSize);

		btnPlot = new JButton("Plot");
		btnPlot.addActionListener(new ButtonAction("Plot", KeyEvent.VK_P));
		pnlPlot.add(btnPlot);
		
		btnClearWSizeTable = new JButton("Clear Table");
		btnClearWSizeTable.addActionListener(new ButtonAction("Clear Table", KeyEvent.VK_C));
		pnlPlot.add(btnClearWSizeTable);	
		
		btnPhaseDetection = new JButton("Phases Detection");;
		btnPhaseDetection.addActionListener(new ButtonAction("PhasesDetection", KeyEvent.VK_P));
		pnlPhases.add(btnPhaseDetection);
	}
	
	private void phaseDetection() {
		trace.getPhases(0.99, 20);
	}
	
	private void plotTrace() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		dataset.addSeries(Plotter.traceToSeries(trace));
		
		int rows = tableWindowSize.getRowCount();
		double coverage;
		int wsize;
		for(int i = 0; i < rows; i++) {
			try {
				wsize = (Integer)tableWindowSize.getValueAt(i, 0);
				coverage = (Double)tableWindowSize.getValueAt(i, 1);
			
				dataset.addSeries(Plotter.arrayToSeries(trace.getDynamicBound(coverage, wsize), "c="+coverage+",w="+wsize));
			}catch(NullPointerException e) {
				;
			}
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
		
		JPanel plotPanel = new ChartPanel(chart);
		JFrame plotFrame = new JFrame();
		plotFrame.add(plotPanel);
		plotFrame.setVisible(true);
		plotFrame.pack();
		plotFrame.setTitle(TracePanel.this.trace.getName());
		plotFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
				loadTrace(new File(txtFile.getText()));
				refresh();
			}else if(src == btnClose) {
				dispose();
			}else if(src == btnUpdateBoundsTable) {
				tableBounds.updateValues();
			}else if(src == btnClearBoundsTable) {
				tableBounds.reset();
			}else if(src == btnPlot) {
				plotTrace();
			}else if(src == btnClearWSizeTable) {
				tableWindowSize.clear();
				tableWindowSize.setValueAt(100,0,0);
				tableWindowSize.setValueAt(0.99,0,1);
			}else if(src == btnPhaseDetection) {
				phaseDetection();
			}
		};
	}	
}
