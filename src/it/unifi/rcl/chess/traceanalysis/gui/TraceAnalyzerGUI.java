package it.unifi.rcl.chess.traceanalysis.gui;

import it.unifi.rcl.chess.traceanalysis.Trace;
import it.unifi.rcl.chess.traceanalysis.Utils;

import javax.swing.JFrame;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
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
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class TraceAnalyzerGUI implements Runnable {

	private final int MIN_WIDTH = 500;
	private final int MIN_HEIGHT = 400;
	
	private static File fLastPath = new File(".").getAbsoluteFile();
	
	private JFrame frmChessProbabilisticTrace;
	private final JPanel pnlControls = new JPanel();
	private JTextField txtFile;
	private JTable table;
	private JButton btnLoadNewTrace;
	private JTabbedPane tabbedPane;
	
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
		
		btnLoadNewTrace = new JButton("Load New Trace");
		btnLoadNewTrace.addActionListener(new ButtonAction("Load New Trace", KeyEvent.VK_T));
		pnlControls.add(btnLoadNewTrace);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		pnlMain.add(tabbedPane, BorderLayout.CENTER);
		
//		JPanel pnlTrace1 = new TracePanel();
//		JPanel pnlTrace2 = new TracePanel();
//		tabbedPane.addTab("NewTrace1", pnlTrace1);
//		tabbedPane.addTab("NewTrace2", pnlTrace2);

	}
	
	private class ButtonAction extends AbstractAction {
		
		public ButtonAction(String name, Integer mnemonic) {
			super(name);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Object src = e.getSource();
			
			if(src == btnLoadNewTrace) {
				JFileChooser jfile = new JFileChooser(fLastPath);
				jfile.setMultiSelectionEnabled(true);
		        int returnVal = jfile.showOpenDialog(TraceAnalyzerGUI.this.frmChessProbabilisticTrace);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File[] files = jfile.getSelectedFiles();
		            TraceAnalyzerGUI.fLastPath = files[0];
		            
		            String shortTitle = "";
		            String tooltip = "";
		            
		            for(int i = 0; i < files.length; i++) {
		            	TracePanel tPanel = new TracePanel(files[i]);
		            	
		            	tooltip = files[i].getAbsolutePath();
		            	shortTitle = Utils.abbreviateMiddle(tooltip, "...", 20); 
		            	
			            tabbedPane.addTab(shortTitle, tPanel);
			            tabbedPane.setToolTipTextAt(tabbedPane.indexOfComponent(tPanel), tooltip);
			            tabbedPane.setSelectedComponent(tPanel);
		            }
		            
		            frmChessProbabilisticTrace.pack();
		        }else {

		        }
			}
		};
	}
}
