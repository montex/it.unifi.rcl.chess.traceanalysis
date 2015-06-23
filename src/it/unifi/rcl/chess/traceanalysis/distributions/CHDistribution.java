package it.unifi.rcl.chess.traceanalysis.distributions; 

import it.unifi.rcl.chess.traceanalysis.Trace;

import java.util.ArrayList;

import monitoringService.distributions.Distribution;
import monitoringService.distributions.ExponentialDistribution;
import monitoringService.distributions.ParetoDistribution;
import monitoringService.distributions.ShiftedExponentialDistribution;
import monitoringService.distributions.UniformDistribution;
import monitoringService.distributions.WeibullDistribution;

public class CHDistribution {
	
	public boolean isSameDistribution(CHDistribution d) {
		return this.getClass().getName() == d.getClass().getName();
	}
	
	public static CHDistribution toCHDistribution(Distribution d, ArrayList<Double> data) {
		double param1, param2;
		CHDistribution ret = null;
		
		if(d.getClass() == ExponentialDistribution.class) {
			param1 = ((ExponentialDistribution)d).getMean(data, data.size());
			ret = new CHExponential(param1);
		}else if(d.getClass() == ParetoDistribution.class) {
			param1 = ((ParetoDistribution)d).getK(data, data.size());
			param2 = ((ParetoDistribution)d).getAlpha(data, data.size(), param1);
			ret = new CHPareto(param1, param2);
		}else if(d.getClass() == ShiftedExponentialDistribution.class) {
			param1 = ((ShiftedExponentialDistribution)d).getGamma(data, data.size());
			param2 = ((ShiftedExponentialDistribution)d).getMi(data, data.size(), param1);
			ret = new CHShiftedExponential(param1, param2);
		}else if(d.getClass() == UniformDistribution.class) {
			param1 = ((UniformDistribution)d).getA(data, data.size());
			param2 = ((UniformDistribution)d).getB(data, data.size());
			ret = new CHUniform(param1, param2);
		}else if(d.getClass() == WeibullDistribution.class) {
			param1 = ((WeibullDistribution)d).getGamma(data, data.size());
			param2 = ((WeibullDistribution)d).getAlpha(data, data.size(), param1);
			ret = new CHWeibull(param1, param2);
		}else{
			ret = new CHDistribution();
		}
		
		return ret;
	}
	
	public static CHDistribution toCHDistribution(Trace t, double coverage) {
		return toCHDistribution(t.getDistribution(coverage), t.getSamples());
	}
	
	public boolean equals(CHDistribution d) {
		return this.isSameDistribution(d);
	}
	
	public String toString() {
		return "{unknown}";
	}
	
}