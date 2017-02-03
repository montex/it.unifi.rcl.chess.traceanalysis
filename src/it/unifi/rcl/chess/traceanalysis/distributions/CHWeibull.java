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

public class CHWeibull extends CHDistribution {
	
	private double gamma;
	private double alpha;
	
	public CHWeibull(double gamma, double alpha) {
		this.gamma = gamma;
		this.alpha = alpha;
	}
	
	public boolean equals(CHDistribution d) {
		if(this.isSameDistribution(d)) {
			return (this.gamma == ((CHWeibull)d).gamma) 
					&& (this.alpha == ((CHWeibull)d).alpha);
		}else{
			return false;
		}
	}
	
	public String toString() {
		return "Weibull(" + String.format("%.2E", gamma) + "," + String.format("%.2E", alpha) + ")";
	}
}
