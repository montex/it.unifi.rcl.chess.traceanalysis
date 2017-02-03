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
package it.unifi.rcl.chess.traceanalysis.distributions;

public class CHUniform extends CHDistribution {
	
	private double a;
	private double b;
	
	public CHUniform(double a, double b) {
		this.a = a;
		this.b = b;
	}
	
	public boolean equals(CHDistribution d) {
		if(this.isSameDistribution(d)) {
			return (this.a == ((CHUniform)d).a) 
					&& (this.b == ((CHUniform)d).b);
		}else{
			return false;
		}
	}
	
	public String toString() {
		return "Uniform(" + String.format("%.2E", a) +"," + String.format("%.2E", b) +")";
	}
}
