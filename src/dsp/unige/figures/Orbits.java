/*
 * 
 * 
 * 
 * 
		Copyright (C) 2014 Giulio Luzzati
		giulio.luzzati@edu.unige.it

    JSATSIM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSATSIM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSATSIM.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */

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
	 * get the orbital distance. 
	 * @param ORBIT_TYPE , one of Orbit.ORBIT_TYPE_LEO, Orbit.ORBIT_TYPE_MEO, Orbit.ORBIT_TYPE_GEO, Orbit.ORBIT_TYPE_HEO
	 * @return the orbital distance in [km].
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


	/**
	 * get the propagation time in [ms] for the given orbit
	 * @param ORBIT_TYPE
	 * @return the propagation time in [ms]
	 */
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
