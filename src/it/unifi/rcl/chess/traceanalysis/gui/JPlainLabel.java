package it.unifi.rcl.chess.traceanalysis.gui;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class JPlainLabel extends JLabel {
	
	public JPlainLabel() {
		super();
		setFont(new Font("Dialog", Font.PLAIN, 12));
	}

	public JPlainLabel(String text) {
		this();
		setText(text);
	}
	
	public JPlainLabel(JPanel owner) {
		owner.add(this);
	}
	
	public JPlainLabel(String text, JPanel owner) {
		this(text);
		owner.add(this);
	}
}
