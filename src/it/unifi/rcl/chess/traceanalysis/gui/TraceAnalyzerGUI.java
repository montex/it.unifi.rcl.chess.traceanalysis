package it.unifi.rcl.chess.traceanalysis.gui;

import it.unifi.rcl.chess.traceanalysis.Trace;
import it.unifi.rcl.chess.traceanalysis.Utils;

import javax.management.ReflectionException;
import javax.swing.JFrame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.List;
import java.awt.Toolkit;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.FrameworkUtil;

public class TraceAnalyzerGUI implements Runnable {

	private final int MIN_WIDTH = 500;
	private final int MIN_HEIGHT = 400;
	private final String RES_EXAMPLES_PATH = "examples";
	private final String RES_EXAMPLESLIST_PATH = RES_EXAMPLES_PATH + "/list.txt";
	private final String MNTAG_FILES = "files";
	
	private static File fLastPath = new File(".").getAbsoluteFile();
	
	private JFrame frmChessProbabilisticTrace;
	private final JPanel pnlControls = new JPanel();
	private JTextField txtFile;
	private JTable table;
	private JButton btnLoadNewTrace;
	private JTabbedPane tabbedPane;
	
	private JMenu mnLoadExamples;
	
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
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight()); 
		Dimension dim = new Dimension(Math.round(0.8f * xSize), Math.round(0.8f * ySize));

		frmChessProbabilisticTrace.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		frmChessProbabilisticTrace.setPreferredSize(dim);
		frmChessProbabilisticTrace.pack();
		frmChessProbabilisticTrace.setVisible(true);
		frmChessProbabilisticTrace.setLocationRelativeTo(null);
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
		
		JMenuItem mntmLoadTrace = new JMenuItem("Load New Trace");
		mntmLoadTrace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TraceAnalyzerGUI.this.showLoadTraceDialog();;
			}
		});
		mnfile.add(mntmLoadTrace);
		
		JMenuItem mntmClosePanel = new JMenuItem("Close Selected Panel");
		mntmClosePanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((TracePanel)TraceAnalyzerGUI.this.tabbedPane.getSelectedComponent()).dispose();
			}
		});
		mnfile.add(mntmClosePanel);
		
		JMenuItem mntmQuit = new JMenuItem("Exit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TraceAnalyzerGUI.this.frmChessProbabilisticTrace.dispose();
			}
		});
		
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnfile.add(mntmQuit);
		
		mnLoadExamples = new JMenu("Examples");
		menuBar.add(mnLoadExamples);
		
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
		
		loadExampleTracesList();
	}
	
	private TracePanel loadTrace(File f) {     
        String shortTitle = "";
        String tooltip = "";
        
      	TracePanel tPanel = new TracePanel(f);
        	
        tooltip = f.getAbsolutePath();
        shortTitle = Utils.abbreviateMiddle(tooltip, "...", 20); 
        	
        tabbedPane.addTab(shortTitle, tPanel);
        tabbedPane.setToolTipTextAt(tabbedPane.indexOfComponent(tPanel), tooltip);
        tabbedPane.setSelectedComponent(tPanel);
                      
        return tPanel;
	}
	
	private TracePanel loadTrace(String file) {
		return loadTrace(new File(file));
	}

	private class ExampleTraceInfo {
		private String folder;
		private String fileName;
		
		public ExampleTraceInfo(String path, String name) {
			folder = path;
			fileName = name;
		}
		
		public ExampleTraceInfo(String fullPath) {
			String[] pieces = fullPath.split("/");
			folder = pieces[0];
			fileName = pieces[1];
		}
		
		public String getFullPath() {
			return folder + "/" + fileName;
		}
		
		public String getFileName() { return fileName; }
		
		public String getFolder() { return folder; }
		
		public String toString() { return getFullPath(); }
	}
	
	private void loadExampleTracesList() {
	
		InputStream s = getClass().getClassLoader().getResourceAsStream(RES_EXAMPLESLIST_PATH);
		BufferedReader in = new BufferedReader(new InputStreamReader(s));
		
		HashMap<String, ArrayList<ExampleTraceInfo>> map = new HashMap<String, ArrayList<ExampleTraceInfo>>();
		ArrayList<ExampleTraceInfo> tmpList = new ArrayList<ExampleTraceInfo>();
		
		// read the file with the list of traces and put into the map,
		// using the folder as key
		try {
			ExampleTraceInfo info = null;
			String line = in.readLine();
			while(line != null) {
				info = new ExampleTraceInfo(line);
				tmpList = map.get(info.getFolder());
				if(tmpList == null) {
					tmpList = new ArrayList<ExampleTraceInfo>();
				}
				tmpList.add(info);
				map.put(info.getFolder(), tmpList);
				
				line = in.readLine();
			}
		} catch(IOException e) {
			//TODO
			e.printStackTrace();
		}
			
		Iterator<String> i = map.keySet().iterator();
		ArrayList<ExampleTraceInfo> filesOfThisGroup = null;
		
		JMenuItem mntmTemp = null;
		JMenuItem mntmInner = null;
		JSeparator sep;
		
		String key = null;
		ExampleTraceInfo info = null;
		ExampleTraceInfo[] tmpInfoArray = new ExampleTraceInfo[1];

		while(i.hasNext()) {
			key = i.next();
			mntmTemp = new JMenu(key);
			mnLoadExamples.add(mntmTemp);
			
			filesOfThisGroup = map.get(key);
			Iterator<ExampleTraceInfo> j = filesOfThisGroup.iterator();
			
			while(j.hasNext()) {
				info = j.next();
						
				mntmInner = new JMenuItem(info.getFileName());
				mntmInner.putClientProperty(MNTAG_FILES, new ExampleTraceInfo[] { info } );
				mntmInner.addActionListener(new ExamplesMenuAction(info.getFileName(), KeyEvent.VK_F));
				mntmTemp.add(mntmInner);
			}
			
			sep = new JSeparator();
			mntmTemp.add(sep);

			mntmInner = new JMenuItem("#all");
			mntmInner.putClientProperty(MNTAG_FILES, filesOfThisGroup.toArray(tmpInfoArray));
			mntmInner.addActionListener(new ExamplesMenuAction("#all", KeyEvent.VK_A));
			mntmTemp.add(mntmInner);
		}
		sep = new JSeparator();
		mnLoadExamples.add(sep);
		
		mntmTemp = new JMenuItem("#all");
		
		Iterator<ArrayList<ExampleTraceInfo>> infoLists = map.values().iterator();
		filesOfThisGroup = new ArrayList<ExampleTraceInfo>();
		
		while(infoLists.hasNext()) {
			filesOfThisGroup.addAll(infoLists.next());			
		}
		
		mntmTemp.putClientProperty(MNTAG_FILES, filesOfThisGroup.toArray(tmpInfoArray));
		mntmTemp.addActionListener(new ExamplesMenuAction("#all", KeyEvent.VK_A));
		mnLoadExamples.add(mntmTemp);	
	}
	
	private void showLoadTraceDialog() {
		JFileChooser jfile = new JFileChooser(fLastPath);
		jfile.setMultiSelectionEnabled(true);
        int returnVal = jfile.showOpenDialog(TraceAnalyzerGUI.this.frmChessProbabilisticTrace);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = jfile.getSelectedFiles();
            TraceAnalyzerGUI.fLastPath = files[0];
            
            for(int i = 0; i < files.length; i++) {
            	loadTrace(files[i]);
            }
        }else {

        }		
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
				showLoadTraceDialog();
			}
		};
	}

	private class ExamplesMenuAction extends AbstractAction {
		
		public ExamplesMenuAction(String name, Integer mnemonic) {
			super(name);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Object src = e.getSource();
			ExampleTraceInfo[] filesToOpen = 
					(ExampleTraceInfo[]) ((JMenuItem)src).getClientProperty(MNTAG_FILES);
			
			Trace t = null;
			InputStream is = null;

			if(filesToOpen != null) {
				for(int i = 0; i < filesToOpen.length; i++) {

					is = ClassLoader.getSystemClassLoader().getResourceAsStream(RES_EXAMPLES_PATH + "/" + filesToOpen[i].getFullPath());
										
					t = new Trace(is);
					t.setName("examples/" + filesToOpen[i].getFullPath());
					
					TracePanel tPanel = new TracePanel();
			        tabbedPane.addTab(t.getName(25), tPanel);
			        tabbedPane.setToolTipTextAt(tabbedPane.indexOfComponent(tPanel), t.getName());
			        tabbedPane.setSelectedComponent(tPanel);
					tPanel.loadTrace(t, t.getName());
					frmChessProbabilisticTrace.pack();
				}
			}
				
		};
	}
}
