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

public class CHPareto extends CHDistribution {
	
	private double k;
	private double alpha;
	
	public CHPareto(double k, double alpha) {
		this.k = k;
		this.alpha = alpha;
	}
	
	public boolean equals(CHDistribution d) {
		if(this.isSameDistribution(d)) {
			return (this.k == ((CHPareto)d).k) 
					&& (this.alpha == ((CHPareto)d).alpha);
		}else{
			return false;
		}
	}
	
	public String toString() {
		return "Pareto(" + String.format("%.2E", k) + "," + String.format("%.2E", alpha) + ")";
	}
}
