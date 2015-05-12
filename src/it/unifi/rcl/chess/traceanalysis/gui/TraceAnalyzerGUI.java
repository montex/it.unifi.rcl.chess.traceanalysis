package it.unifi.rcl.chess.traceanalysis.gui;

import it.unifi.rcl.chess.traceanalysis.Trace;

import javax.swing.JFrame;

import java.awt.Container;
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
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

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
	
	private Map<Container,Trace> pnlToTrace = new HashMap<Container, Trace>();

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
		
//		JLabel lblNoDataLoaded = new JLabel("No data loaded yet.\n");
//		tabbedPane.addTab("...", null, lblNoDataLoaded, null);
		
		JPanel pnlTrace1 = new TracePanel();
		JPanel pnlTrace2 = new TracePanel();
		tabbedPane.addTab("NewTrace1", pnlTrace1);
		tabbedPane.addTab("NewTrace2", pnlTrace2);

	}
}
