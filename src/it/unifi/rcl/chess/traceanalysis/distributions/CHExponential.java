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
