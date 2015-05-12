package it.unifi.rcl.chess.traceanalysis.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import it.unifi.rcl.chess.traceanalysis.Trace;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TracePanel extends JPanel {
	
	private static final String TXT_NOFILE = "No file loaded.";
	
	private Trace trace;
	private File file;

	JTextField txtFile;
	JButton btnReload;
	JButton btnUnload;
	JLabel lblSize;
	JLabel lblPoints;	
	JProgressBar progressBar;
	JScrollPane scrollPane;
	JTable table;
	JLabel lblFileName;
	JLabel lblFilePath;
	
	public TracePanel() {
		initialize();
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
		trace = new Trace(file);
		this.file = file;
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
		txtFile.setText("");
		
		refresh();
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
	}
	
	private void initialize() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		txtFile = new JTextField();
		txtFile.setColumns(20);
		txtFile.setText("File");
		this.add(txtFile);
		
		btnReload = new JButton("Reload");
		btnReload.addActionListener(new ButtonAction("Reload", KeyEvent.VK_L));
		this.add(btnReload);
		
		btnUnload = new JButton("Unload");
		btnUnload.addActionListener(new ButtonAction("Unload", KeyEvent.VK_U));
		this.add(btnUnload);
		
		lblSize = new JLabel("Size");
		this.add(lblSize);
		
		lblPoints = new JLabel("#Points");
		this.add(lblPoints);
		
		lblFileName = new JLabel();
		this.add(lblFileName);
		
		lblFilePath = new JLabel();
		this.add(lblFilePath);
		
		progressBar = new JProgressBar();
		this.add(progressBar);
		
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
				Long.class, Long.class, Double.class, Object.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
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
			}else if(src == btnUnload) {
				reset();
			}
		};
	}
}
