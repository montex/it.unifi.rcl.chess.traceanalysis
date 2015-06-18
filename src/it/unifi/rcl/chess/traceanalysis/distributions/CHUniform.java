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
