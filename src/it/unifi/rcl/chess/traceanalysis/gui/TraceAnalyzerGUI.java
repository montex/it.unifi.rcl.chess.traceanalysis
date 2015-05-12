package it.unifi.rcl.chess.traceanalysis.gui;

import it.unifi.rcl.chess.traceanalysis.Trace;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JToolBar;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JButton;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JTextField;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class TraceAnalyzerGUI implements Runnable {

	private final int MIN_WIDTH = 500;
	private final int MIN_HEIGHT = 400;
	
	private JFrame frmChessProbabilisticTrace;
	private final JPanel pnlControls = new JPanel();
	private JTextField txtFile;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		(new TraceAnalyzerGUI()).run();
	}
		
	/**
	 * Create the application.
	 */
	public TraceAnalyzerGUI() {
		initialize();
	}

	/**
	 * Run the form
	 */
	public void run() {
		TraceAnalyzerGUI window = new TraceAnalyzerGUI();
		window.frmChessProbabilisticTrace.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		window.frmChessProbabilisticTrace.pack();
		window.frmChessProbabilisticTrace.setVisible(true);
		window.frmChessProbabilisticTrace.setLocationRelativeTo(null);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChessProbabilisticTrace = new JFrame();
		frmChessProbabilisticTrace.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmChessProbabilisticTrace.setTitle("CHESS Probabilistic Trace Analyzer");
		
		JMenuBar menuBar = new JMenuBar();
		frmChessProbabilisticTrace.setJMenuBar(menuBar);
		
		JMenu mnfile = new JMenu("File");
		menuBar.add(mnfile);
		
		JMenuItem mntmQuit = new JMenuItem("Exit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TraceAnalyzerGUI.this.frmChessProbabilisticTrace.dispose();
			}
		});
		
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnfile.add(mntmQuit);
		
		JToolBar toolBar = new JToolBar();
		frmChessProbabilisticTrace.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JPanel pnlMain = new JPanel();
		frmChessProbabilisticTrace.getContentPane().add(pnlMain, BorderLayout.CENTER);
		pnlMain.setLayout(new BorderLayout(0, 0));
		pnlMain.add(pnlControls, BorderLayout.NORTH);
		
		JButton btnLoadNewTrace = new JButton("Load New Trace");
		pnlControls.add(btnLoadNewTrace);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		pnlMain.add(tabbedPane, BorderLayout.CENTER);
		
		JLabel lblNoDataLoaded = new JLabel("No data loaded yet.\n");
		tabbedPane.addTab("...", null, lblNoDataLoaded, null);
		
		JPanel pnlTemplateTab = new JPanel();
		tabbedPane.addTab("Template", null, pnlTemplateTab, null);
		pnlTemplateTab.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		txtFile = new JTextField();
		txtFile.setColumns(20);
		txtFile.setText("File");
		pnlTemplateTab.add(txtFile);
		
		JButton btnReload = new JButton("Reload");
		btnReload.addActionListener(new LoadTraceAction("Reload", KeyEvent.VK_L));
		pnlTemplateTab.add(btnReload);
		
		JButton btnUnload = new JButton("Unload");
		pnlTemplateTab.add(btnUnload);
		
		JLabel lblSize = new JLabel("Size");
		pnlTemplateTab.add(lblSize);
		
		JLabel lblPoints = new JLabel("#Points");
		pnlTemplateTab.add(lblPoints);
		
		JProgressBar progressBar = new JProgressBar();
		pnlTemplateTab.add(progressBar);
		
		JScrollPane scrollPane = new JScrollPane();
		pnlTemplateTab.add(scrollPane);
		
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
				Long.class, Long.class, Double.class, Object.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
	}
	
	private class LoadTraceAction extends AbstractAction {
		public LoadTraceAction(String name, Integer mnemonic) {
			super(name);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Trace t = new Trace("/home/montex/Lavoro/RCL/Progetti/CONCERTO/WP4/Adaptare/datasets/household_power_consumption.txt.gz");
		};
	}
}
