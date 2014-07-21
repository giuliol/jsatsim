package dsp.unige.figures;


/**
 * @author giulio
 *
 */
public class ChannelHelper {


	
	/** Returns the noise level of a system given the bandwidth (in Hz) and the system temperature (kelvin degrees)
	 * 
	 * @param noiseTemperature 
	 * @param bandwidthHz
	 * @return the noise level in dB
	 */
	public static double getNoisePower(Station sta, Satellite sat){
		
		
		return -228.6 + 10*Math.log10(sta.noiseTemperature) +10*Math.log10(sat.transponderBandwidth);
	}
	
	/**
	 * Returns hartley-Shannon capacity in bps, given Bandwidth (in Hz) and Signal to Noise Ratio.
	 * @param B the channel's bandwidth
	 * @param s Signal level in dB
	 * @param n Noise level in dB
	 * @return Shannon max. theoretical bandwidth in bps.
	 */
	static double getHSCapacity(int B, double s, double n){
		System.out.println("ChannelHelper.getHSCapacity() debug; "+(1+ Math.pow(10,s/10d) / Math.pow(10,n/10d)));
		return B*Math.log(1+ Math.pow(10,s/10d) / Math.pow(10,n/10d))/Math.log(2); 
	}



	/** Returns the rain attenuation in dB given the state s, using 
	 * 	procedure from [1].
	 *  [1]. A prediction model that combines Rain Attenuation and Other Propagation Impairments Along Earth- Satellinte Paths
	 * @param s
	 * @return rain attenuation in dB.
	 */
	public static double getRainAttenuation(Station s){

		// ====================  step 1  ===========================
		// calculate freezing height (in km) from the absolute alue of station latitude
		double hFr;
		if(s.stationLatitude >= 0  && s.stationLatitude <23)
			hFr = 5.0d;
		else
			hFr = 5.0 - 0.075 * (s.stationLatitude - 23); 

		// ====================  step 2  ===========================
		// calculate slant-path length Ls below the freezing height
		double Ls = (hFr - s.stationAltitude) / Math.sin(s.elevationAngle * Math.PI/180);

		// ====================  step 3  ===========================
		// calculate horizontal projection Lg of the slant path length
		double Lg = Ls*Math.cos(s.elevationAngle * Math.PI/180);

		// ====================  step 4  ===========================
		// obtain rain attenuation 
		// TODO : controllare le reference 9 e 31 per capire bene cosa sono K e alpha
		double gamma = s.getRainK()*Math.pow(s.R001,s.getRainAlpha());

		// ====================  step 5  ===========================
		// calculate horizontal path adjustment 
		double rh001= 1 / (1 + 0.78 * Math.sqrt(Lg*gamma/s.frequency) - 0.38 * (1- Math.exp(-2*Lg)) );

		// ====================  step 6  ===========================
		// calculate the adjusted rainy path length
		double ZETA= Math.atan( (hFr-s.stationAltitude) / ( Lg*rh001 ) );
		double Lr;

		if(ZETA>s.elevationAngle){
			Lr = (Lg * rh001) / (Math.cos(s.elevationAngle* Math.PI/180));
		}
		else{
			Lr = (hFr - s.stationAltitude) / (Math.sin(s.elevationAngle* Math.PI/180)) ;
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
		double rv001 = 1 / (1 + Math.sqrt(Math.sin(s.elevationAngle *  Math.PI/180)) * (31 * (1 - Math.exp(-s.elevationAngle / (1 + CHI) ) ) * (Math.sqrt(Lr*gamma)/Math.pow(s.frequency,2)) - 0.45 ) ) ; 

		// ====================  step 8  ===========================
		// effective path length through rain
		double Le = Lr * rv001;

		// ====================  step 9  ===========================
		// get the attenuation exceded in 0.01% of average year time
		double A001 = gamma*Le;

		return A001;
	}
	

	/** returns the freespace loss given a station and a satellite
	 * @param station
	 * @param satellite
	 * @return The freespace loss, in dB
	 */
	public static double getFreeSpaceLoss(Station station, Satellite satellite){
		
		return 20*Math.log10(Orbits.getDistance(satellite.ORBIT_TYPE)) + 20*Math.log10(station.frequency*1000) + 32.45;
		
	}

	public static class Station {

		public double stationLatitude;
		public double stationAltitude;
		public double elevationAngle;
		public double frequency;
		public double R001;
		public double tilt=45;
		public int noiseTemperature;
		
		private int fIdx;


		/**
		 * @param sL station latitude in degrees
		 * @param sA station altitude in km
		 * @param r001 point rainfall rate for 0.01% of an avg. year in mm/hr
		 * @param eA station elevation angle in degrees
		 * @param f carrier frequency in GHz
		 */
		public Station(double sL, double sA, double r001, double eA, double f, int noiseTemp){
			stationLatitude=sL;
			stationAltitude=sA;
			R001=r001;
			elevationAngle=eA;
			frequency=f;
			fIdx=getFIndex(frequency);
			noiseTemperature=noiseTemp;
		}

		private double getRainK() {
			System.out.println("ChannelHelper.Station.getRainK()  N.B. circular polarization assumed!" );
			return (KH[fIdx]+KV[fIdx])/2;
		}

		private double getRainAlpha() {
			System.out.println("ChannelHelper.Station.getRainAlpha() N.B. circular polarization assumed!");
			return (KH[fIdx]*ALPHAH[fIdx] + KV[fIdx]* ALPHAV[fIdx])/ (2*getRainK());
		}


		private int getFIndex(double freq){
			double delta=freq-F[0];
			int idx=0;
			int out;
			while(delta>0){
				idx++;
				delta=freq-F[idx];
			}
			if(idx==0)
				out= 0;
			if(Math.abs(delta)<(freq-F[idx-1]))
				out= idx;
			else 
				out= idx-1;
			
			System.out.println("ChannelHelper.Station.getFIndex() closest freq. F["+out+"] ="+F[out]);
			return out;
		}

		private static final double [] KH={
			0.0000259,
			0.0000847,
			0.0001071,
			0.007056,
			0.001915,
			0.004115,
			0.01217,
			0.02386,
			0.04481,
			0.09164,
			0.1571,
			0.2403,
			0.3374,
			0.4431,
			0.5521,
			0.6600,
			0.8606,
			1.0315,
			1.2807,
			1.3671
		};

		private static final double [] KV={
			0.0000308,
			0.0000998,
			0.0002461,
			0.0004878,
			0.001425,
			0.003450,
			0.01129,
			0.02455,
			0.05008,
			0.09611,
			0.1533,
			0.2291,
			0.3224,
			0.4274,
			0.5375,
			0.6472,
			0.8515,
			1.0253,
			1.1668,
			1.2795,
			1.3680
		};

		private  static final double [] ALPHAH={
			0.9691,
			1.0664,
			1.6009,
			1.5900,
			1.4810,
			1.3905,
			1.2571,
			1.1825,
			1.1233,
			1.0586,
			0.9991,
			0.9485,
			0.9047,
			0.8673,
			0.8355,
			0.8084,
			0.7656,
			0.7345,
			0.7115,
			0.6944,
			0.6815
		};

		private static final double [] ALPHAV={

			0.8592,
			0.9490,
			1.2476,
			1.5882,
			1.4745,
			1.3797,
			1.2156,
			1.1216,
			1.0440,
			0.9847,
			0.9491,
			0.9129,
			0.8761,
			0.8421,
			0.8123,
			0.7871,
			0.7486,
			0.7215,
			0.7021,
			0.6876,
			0.6765
		};

		private static final int [] F={

			1,
			2,
			4,
			6,
			7,
			8,
			10,
			12,
			15,
			20,
			25,
			30,
			35,
			40,
			45,
			50,
			60,
			70,
			80,
			90,
			100
		};
	}

	public static class Satellite{
		public Satellite(double eIRP2, int transpBW, int orbType,
				int modul) {
			
			EIRP=eIRP2;
			transponderBandwidth=transpBW;
			ORBIT_TYPE=orbType;
			
			
		}
		
		public double getBER(double snr){
			System.out.println("ChannelHelper.Satellite.getERF() TBI");
			if(modulation == Modulation.BPSK)
				return 0;
			else 
				return 0;
		}
		
		public int txPower;
		public int ORBIT_TYPE;
		public int transponderBandwidth;
		public double EIRP;
		public int modulation;
	}

	public static double getSdB(Station sta, Satellite sat){
		return  sat.EIRP - getFreeSpaceLoss(sta, sat) - getRainAttenuation(sta);
	}
	
	public static int getRate(Station sta, Satellite sat) {
		
		double s = getSdB(sta,sat);
		double n = getNoisePower(sta, sat);
		double bw = getHSCapacity(sat.transponderBandwidth, s , n);
		return (int) bw;
	}

	public static double getBER(Station sta, Satellite sat) {
		double s = sat.EIRP - getFreeSpaceLoss(sta, sat) - getRainAttenuation(sta);
		double n = getNoisePower(sta,sat);
		return	sat.getBER(s-n);

	}

}
