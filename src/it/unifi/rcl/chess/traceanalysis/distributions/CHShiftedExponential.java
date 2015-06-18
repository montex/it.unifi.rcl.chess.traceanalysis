package it.unifi.rcl.chess.traceanalysis.distributions;

public class CHShiftedExponential extends CHDistribution {
	
	private double gamma;
	private double mi;
	
	public CHShiftedExponential(double gamma, double mi) {
		this.gamma = gamma;
		this.mi = mi;
	}
	
	public boolean equals(CHDistribution d) {
		if(this.isSameDistribution(d)) {
			return (this.gamma == ((CHShiftedExponential)d).gamma) 
					&& (this.mi == ((CHShiftedExponential)d).mi);
		}else{
			return false;
		}
	}
	
	public String toString() {
		return "ShiftedExponential(" + String.format("%.2E", gamma) + "," + String.format("%.2E", mi) + ")";
	}
}
