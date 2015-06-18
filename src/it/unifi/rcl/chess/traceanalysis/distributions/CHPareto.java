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
