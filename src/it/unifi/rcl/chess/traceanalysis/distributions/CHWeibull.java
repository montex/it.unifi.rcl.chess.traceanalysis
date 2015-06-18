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
