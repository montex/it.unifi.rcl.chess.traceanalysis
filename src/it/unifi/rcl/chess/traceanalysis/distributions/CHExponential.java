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

public class CHExponential extends CHDistribution {
	
	private double lambda;
	
	public CHExponential(double lambda) {
		this.lambda = lambda;
	}
	
	public boolean equals(CHDistribution d) {
		if(this.isSameDistribution(d)) {
			return this.lambda == ((CHExponential)d).lambda;
		}else{
			return false;
		}
	}
	
	public String toString() {
		return "Exponential(" + String.format("%.2E", lambda) + ")";
	}
}
