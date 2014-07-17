package dsp.unige.receiver;

/**
 * @author giulio
 *
 */
public class ChannelHelper {

	
	/**
	 * Returns hartley-Shannon capacity in bps, given Bandwidth (in Hz) and Signal to Noise Ratio.
	 * @param B the channel's bandwidth
	 * @param snr the channel's signal to noise ratio
	 * @return the channel's capacity C
	 */
	static double getHSCapacity(int B, double snr){
		return B*Math.log(1+snr)/Math.log(2); 
	}
	

	
	
	/** Returns the overall attenuation in dB given the state s, using 
	 * 	procedure from [1].
	 *  [1]. A prediction model that combines Rain Attenuation and Other Propagation Impairments Along Earth- Satellinte Paths
	 * @param s
	 * @return
	 */
	public static double getAttenuation(Station s){
	
		// ====================  step 1  ===========================
		// calculate freezing height (in km) from the absolute alue of station latitude
		double hFr;
		if(s.stationLatitude >= 0  && s.stationLatitude <23)
			hFr = 5.0d;
		else
			hFr = 5.0 - 0.075 * (s.stationLatitude - 23); 
		
		// ====================  step 2  ===========================
		// calculate slant-path length Ls below the freezing height
		double Ls = (hFr - s.stationAltitude) / Math.sin(s.elevationAngle);
		
		// ====================  step 3  ===========================
		// calculate horizontal projection Lg of the slant path length
		double Lg = Ls*Math.cos(s.elevationAngle);
		
		// ====================  step 4  ===========================
		// obtain rain attenuation 
		double gamma = s.RAIN_K*Math.pow(s.R001,s.RAIN_ALPHA);
		
		// ====================  step 5  ===========================
		// calculate horizontal path adjustment 
		double rh001= 1 / (1 + 0.78 * Math.sqrt(Lg*gamma/s.frequency) - 0.38 * (1- Math.exp(-2*Lg)) );
		
		// ====================  step 6  ===========================
		// calculate the adjusted rainy path length
		double ZETA= Math.atan( (hFr-s.stationAltitude) / ( Lg*rh001 ) );
		double Lr;
		
		if(ZETA>s.elevationAngle){
			Lr = (Lg * rh001) / (Math.cos(s.elevationAngle));
		}
		else{
			Lr = (hFr - s.stationAltitude) / (Math.sin(s.elevationAngle)) ;
		}
		
		// ====================  step 7  ===========================
		// calculate vertical path adjustment
		double CHI;
		if(Math.abs(s.elevationAngle)<36){
			CHI=36-s.elevationAngle;
		}
		else{
			CHI=0;
		}
		double rv001 = 1 / (1 + Math.sqrt(Math.sin(s.elevationAngle)) * (31 * (1 - Math.exp(-s.elevationAngle / (1 + CHI) ) ) * (Math.sqrt(Lr*gamma)/Math.pow(s.frequency,2)) - 0.45 ) ) ; 
		
		// ====================  step 8  ===========================
		// effective path length through rain
		double Le = Lr * rv001;
		
		// ====================  step 9  ===========================
		// get the attenuation exceded in 0.01% of average year time
		double A001 = gamma*Le;

		return A001;
	}
	
	public static class Station {
		
		public double stationLatitude;
		public double stationAltitude;
		public double elevationAngle;
		public int frequency;
		public double R001;
		
		public static double RAIN_K=0, RAIN_ALPHA=0;

		public Station(double sL, double sA, double r001, double eA, int f){
			stationLatitude=sL;
			stationAltitude=sA;
			R001=r001;
			elevationAngle=eA;
			frequency=f;
		}
	}
	
	public static class Satellite{
			public int txPower;
			public int ORBIT_TYPE;
			public int txBandwidth;
	}
	
	public static class Channel{
			public int noisePower;
	}
}
