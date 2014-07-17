package dsp.unige.receiver;

public class Orbits {

	public static final int ORBIT_TYPE_LEO=0;
	public static final int ORBIT_TYPE_GEO=1;
	public static final int ORBIT_TYPE_MEO=2;
	public static final int ORBIT_TYPE_HEO=3;
	

	
	private static final String LEO_HRNAME="LEO orbit (36k km)";
	private static final String GEO_HRNAME="GEO orbit (??k km)";
	private static final String MEO_HRNAME="MEO orbit (??k km)";
	private static final String HEO_HRNAME="HEO orbit (??k km)";

	
	/**
	 * get the orbit_type attenuation. 
	 * @param ORBIT_TYPE , one of Orbit.ORBIT_TYPE_LEO, Orbit.ORBIT_TYPE_MEO, Orbit.ORBIT_TYPE_GEO, Orbit.ORBIT_TYPE_HEO
	 * @return the orbit type attenuation.
	 */
	public static double getLorbit(int ORBIT_TYPE){
		switch (ORBIT_TYPE) {
		case ORBIT_TYPE_LEO:
			return Constants.L_LEO;
			
		case ORBIT_TYPE_MEO:
			return Constants.L_MEO;
			
		case ORBIT_TYPE_GEO:
			return Constants.L_GEO;
			
		case ORBIT_TYPE_HEO:
			return Constants.L_HEO;

		default:
			return -1d;
		}
	}
	
	/**
	 * get the orbit_type human readable name. 
	 * @param ORBIT_TYPE , one of Orbit.ORBIT_TYPE_LEO, Orbit.ORBIT_TYPE_MEO, Orbit.ORBIT_TYPE_GEO, Orbit.ORBIT_TYPE_HEO
	 * @return the orbit type name.
	 */
	public static String getOrbitName(int ORBIT_TYPE){
		switch (ORBIT_TYPE) {
		case ORBIT_TYPE_LEO:
			return LEO_HRNAME;
			
		case ORBIT_TYPE_MEO:
			return MEO_HRNAME;
			
		case ORBIT_TYPE_GEO:
			return GEO_HRNAME;
			
		case ORBIT_TYPE_HEO:
			return HEO_HRNAME;

		default:
			return "Wrong ORBIT_TYPE";
		}
	}
	
}
