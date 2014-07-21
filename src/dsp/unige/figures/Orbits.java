package dsp.unige.figures;

public class Orbits {

	public static final int ORBIT_TYPE_LEO=0;
	public static final int ORBIT_TYPE_GEO=1;
	public static final int ORBIT_TYPE_MEO=2;
	public static final int ORBIT_TYPE_HEO=3;

	private static final int LEO_DELAY_MS=12;
	private static final int HEO_DELAY_MS=240;
	private static final int GEO_DELAY_MS=260;
	private static final int MEO_DELAY_MS=120;


	private static final String LEO_HRNAME="LEO orbit (1.000 km)";
	private static final String GEO_HRNAME="GEO orbit (35.786 km)";
	private static final String MEO_HRNAME="MEO orbit (10.000 km)";
	private static final String HEO_HRNAME="HEO orbit (20.000 km)";



	/**
	 * get the orbit_type attenuation. 
	 * @param ORBIT_TYPE , one of Orbit.ORBIT_TYPE_LEO, Orbit.ORBIT_TYPE_MEO, Orbit.ORBIT_TYPE_GEO, Orbit.ORBIT_TYPE_HEO
	 * @return the orbit type attenuation.
	 */
	public static double getDistance(int ORBIT_TYPE){
		switch (ORBIT_TYPE) {
		case ORBIT_TYPE_LEO:
			return SimConstants.L_LEO;

		case ORBIT_TYPE_MEO:
			return SimConstants.L_MEO;

		case ORBIT_TYPE_GEO:
			return SimConstants.L_GEO;

		case ORBIT_TYPE_HEO:
			return SimConstants.L_HEO;

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


	public static int getDelay(int ORBIT_TYPE) {
		switch (ORBIT_TYPE) {
		case ORBIT_TYPE_LEO:
			return LEO_DELAY_MS;

		case ORBIT_TYPE_MEO:
			return MEO_DELAY_MS;

		case ORBIT_TYPE_GEO:
			return GEO_DELAY_MS;

		case ORBIT_TYPE_HEO:
			return HEO_DELAY_MS;

		default:
			return -1;
		}
	}

}
