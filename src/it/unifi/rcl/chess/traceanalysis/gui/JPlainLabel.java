/*******************************************************************************
 * Copyright (c) 2015-2017 Resilient Computing Lab, University of Firenze.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Leonardo Montecchi		lmontecchi@unifi.it
 ******************************************************************************/
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
