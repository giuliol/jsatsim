/*
 * 
 * 
 * 
 * 
		Copyright (C) 2014 Giulio Luzzati
		giulio.luzzati@edu.unige.it

    JSATEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSATEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSATEM.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */

package dsp.unige.figures;

import java.io.Serializable;

import org.apache.commons.math3.special.Erf;

/**
 * @author giulio
 * 
 */
public class ChannelHelper {

	/**
	 * Returns the noise density of a system given Satellite and Station
	 * 
	 * @param station
	 * @param satellite
	 * @return the noise power density in dBW/Hz
	 */
	public static double getN0dBW(Station sta, Satellite sat) {

		double Te = sta.antennaNoiseTemperature + sta.amplifierNoiseTemperature
				+ sta.getRainNoiseTemperature();
		return -228.6 + 10 * Math.log10(Te);
	}

	/**
	 * Returns hartley-Shannon capacity in bps, given Bandwidth (in Hz) and
	 * Signal to Noise Ratio.
	 * 
	 * @param B the channel's bandwidth
	 * @param s  Signal level in dB
	 * @param n  Noise level in dB
	 * @return Shannon max. theoretical information rate in bps.
	 */
	static public double getHSCapacity(int B, double s, double n) {
		System.out.println("ChannelHelper.getHSCapacity() B=" + B + " kHz");
		return B * Math.log(1 + Math.pow(10, (s - n) / 10)) / Math.log(2);
	}

	/**
	 * Returns the rain attenuation in dB given the state s, using procedure
	 * from [1]. [1]. A prediction model that combines Rain Attenuation and
	 * Other Propagation Impairments Along Earth- Satellinte Paths
	 * 
	 * @param s
	 * @return rain attenuation in dB.
	 */
	public static double getRainAttenuation(Station s) {

		// ==================== step 1 ===========================
		// calculate freezing height (in km) from the absolute value of station
		// latitude
		double hFr;
		if (s.stationLatitude >= 0 && s.stationLatitude < 23)
			hFr = 5.0d;
		else
			hFr = 5.0 - 0.075 * (s.stationLatitude - 23);

		// ==================== step 2 ===========================
		// calculate slant-path length Ls below the freezing height
		double Ls = (hFr - s.stationAltitude)
				/ Math.sin(s.elevationAngle * Math.PI / 180);

		// ==================== step 3 ===========================
		// calculate horizontal projection Lg of the slant path length
		double Lg = Ls * Math.cos(s.elevationAngle * Math.PI / 180);

		// ==================== step 4 ===========================
		// obtain rain attenuation
		double gamma = s.getRainK() * Math.pow(s.R001, s.getRainAlpha());

		// ==================== step 5 ===========================
		// calculate horizontal path adjustment
		double rh001 = 1 / (1 + 0.78 * Math.sqrt(Lg * gamma / s.frequency) - 0.38 * (1 - Math
				.exp(-2 * Lg)));

		// ==================== step 6 ===========================
		// calculate the adjusted rainy path length
		double ZETA = Math.atan((hFr - s.stationAltitude) / (Lg * rh001));
		double Lr;

		if (ZETA > s.elevationAngle) {
			Lr = (Lg * rh001) / (Math.cos(s.elevationAngle * Math.PI / 180));
		} else {
			Lr = (hFr - s.stationAltitude)
					/ (Math.sin(s.elevationAngle * Math.PI / 180));
		}

		// ==================== step 7 ===========================
		// calculate vertical path adjustment
		double CHI;
		if (Math.abs(s.elevationAngle) < 36) {
			CHI = 36 - s.elevationAngle;
		} else {
			CHI = 0;
		}
		double rv001 = 1 / (1 + Math.sqrt(Math.sin(s.elevationAngle * Math.PI
				/ 180))
				* (31 * (1 - Math.exp(-s.elevationAngle / (1 + CHI)))
						* (Math.sqrt(Lr * gamma) / Math.pow(s.frequency, 2)) - 0.45));

		// ==================== step 8 ===========================
		// effective path length through rain
		double Le = Lr * rv001;

		// ==================== step 9 ===========================
		// get the attenuation exceded in 0.01% of average year time
		double A001 = gamma * Le;

		return A001;
	}

	/**
	 * returns the freespace loss given a station and a satellite
	 * 
	 * @param station
	 * @param satellite
	 * @return The freespace loss, in dB
	 */
	public static double getFreeSpaceLoss(Station station, Satellite satellite) {

		return 20 * Math.log10(Orbits.getDistance(satellite.ORBIT_TYPE)) + 20
				* Math.log10(station.frequency) + 92.44;

	}

	public static class Station implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private static final double APERTURE_EFFICIENCY = 0.55d;
		public double stationLatitude;
		public double stationAltitude;
		public double elevationAngle;
		public double frequency;
		public double R001;
		public double tilt = 45;
		public int antennaNoiseTemperature;
		public int amplifierNoiseTemperature;
		public double antennaDiameter;

		private int fIdx;

		/**
		 * @return The noise temperature due to rain fall (in °K)
		 */
		public double getRainNoiseTemperature() {
			return 275d * (1d - Math.pow(10, -getRainAttenuation(this) / 10d));
		}

		/**
		 * @param stationLatitude
		 *            station latitude in degrees
		 * @param stationAltitude
		 *            station altitude in km
		 * @param r001
		 *            rainfall rate
		 * @param elevationAngle
		 *            station elevation angle in degrees
		 * @param frequency
		 *            carrier frequency in GHz
		 * @param antennaNoiseTemperature
		 *            antenna equivalent noise temperature
		 * @param amplifierNoiseTemperature
		 *            active circuitry equivalent noise temperature (state of
		 *            art NASA equipm. ~30°K, typical eq. ~ 60 - 500 °K
		 * @param antennaDiameter
		 *            diameter of the antenna (NOT considering efficiency)
		 */
		public Station(double stationLatitude, double stationAltitude,
				double r001, double elevationAngle, double frequency,
				int antennaNoiseTemperature, int amplifierNoiseTemperature,
				double antennaDiameter) {
			this.stationLatitude = stationLatitude;
			this.stationAltitude = stationAltitude;
			this.R001 = r001;
			this.elevationAngle = elevationAngle;
			this.frequency = frequency;
			fIdx = getFIndex(frequency);
			this.antennaNoiseTemperature = antennaNoiseTemperature;
			this.amplifierNoiseTemperature = amplifierNoiseTemperature;
			this.antennaDiameter = antennaDiameter;
		}

		/**
		 * @return the antenna gain in dBi
		 */
		public double getAntennaGain() {
			return 10 * Math.log10(109.66 * frequency * frequency
					* antennaDiameter * antennaDiameter * APERTURE_EFFICIENCY);
		}

		private double getRainK() {
			return (KH[fIdx] + KV[fIdx]) / 2;
		}

		private double getRainAlpha() {
			return (KH[fIdx] * ALPHAH[fIdx] + KV[fIdx] * ALPHAV[fIdx])
					/ (2 * getRainK());
		}

		private int getFIndex(double freq) {
			double delta = freq - F[0];
			int idx = 0;
			int out;
			while (delta > 0) {
				idx++;
				delta = freq - F[idx];
			}
			if (idx == 0)
				out = 0;
			if (Math.abs(delta) < (freq - F[idx - 1]))
				out = idx;
			else
				out = idx - 1;

			return out;
		}

		private static final double[] KH = { 0.0000259, 0.0000847, 0.0001071,
				0.007056, 0.001915, 0.004115, 0.01217, 0.02386, 0.04481,
				0.09164, 0.1571, 0.2403, 0.3374, 0.4431, 0.5521, 0.6600,
				0.8606, 1.0315, 1.2807, 1.3671 };

		private static final double[] KV = { 0.0000308, 0.0000998, 0.0002461,
				0.0004878, 0.001425, 0.003450, 0.01129, 0.02455, 0.05008,
				0.09611, 0.1533, 0.2291, 0.3224, 0.4274, 0.5375, 0.6472,
				0.8515, 1.0253, 1.1668, 1.2795, 1.3680 };

		private static final double[] ALPHAH = { 0.9691, 1.0664, 1.6009,
				1.5900, 1.4810, 1.3905, 1.2571, 1.1825, 1.1233, 1.0586, 0.9991,
				0.9485, 0.9047, 0.8673, 0.8355, 0.8084, 0.7656, 0.7345, 0.7115,
				0.6944, 0.6815 };

		private static final double[] ALPHAV = {

		0.8592, 0.9490, 1.2476, 1.5882, 1.4745, 1.3797, 1.2156, 1.1216, 1.0440,
				0.9847, 0.9491, 0.9129, 0.8761, 0.8421, 0.8123, 0.7871, 0.7486,
				0.7215, 0.7021, 0.6876, 0.6765 };

		private static final int[] F = {

		1, 2, 4, 6, 7, 8, 10, 12, 15, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80,
				90, 100 };

		/**
		 * @return the figure of merit of the RX system
		 */
		public double getFigureofMerit() {
			return getAntennaGain()
					- 10
					* Math.log10(antennaNoiseTemperature
							+ amplifierNoiseTemperature
							+ getRainNoiseTemperature());
		}
	}

	public static class Satellite implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * default constructor.
		 * 
		 * @param EIRP the equivalent irradiated power (including antenna TX gain)
		 * @param transp_bandwidth
		 * @param ORBIT_TYPE see Orbits.ORBIT_TYPE_XXX
		 * @param modulation see Modulation.MOD_XXXX
		 * @param FECtype see FEC.FEC_XXX_XXX_XXX
		 */
		public Satellite(double EIRP, int transponderBandwidth, int ORBIT_TYPE,
				int modulation, int FEC) {

			this.EIRP = EIRP;
			this.transponderBandwidth = transponderBandwidth;
			this.ORBIT_TYPE = ORBIT_TYPE;
			this.modulation = modulation;
			this.FEC = FEC;
		}

		public int txPower;
		public int ORBIT_TYPE;
		public int transponderBandwidth;
		public double EIRP;
		public int modulation;
		public int FEC;
	}

	/**
	 * Returns the received power, after freespace loss, weather attenuation and
	 * RX system figure of merit
	 * 
	 * @param sta  the station object
	 * @param sat the satellite object
	 * @return the received carrier level in [dBW]
	 */
	public static double getSdBW(Station sta, Satellite sat) {
		return sat.EIRP - getFreeSpaceLoss(sta, sat) - getRainAttenuation(sta)
				+ sta.getFigureofMerit();
	}

	/**
	 * Returns the actual bitrate (information rate times the coderate^-1)
	 * 
	 * @param sta
	 * @param sat
	 * @return the bitrate in [kbps]
	 */
	public static int getRate(Station sta, Satellite sat) {

		double br = sat.transponderBandwidth
				* Modulation.getSpectralEfficiency(sat.modulation)
				* FEC.getFECParams(sat.FEC).n / FEC.getFECParams(sat.FEC).k;

		return (int) br;
	}

	/**
	 * Returns the information rate
	 * 
	 * @param sta  the station object
	 * @param sat  the satellite object
	 * @return the information rate in [kbps]
	 */
	public static int getInfoRate(Station sta, Satellite sat) {
		return sat.transponderBandwidth
				* Modulation.getSpectralEfficiency(sat.modulation);
	}

	/**
	 * Returns the noise level in dBW
	 * 
	 * @param sta
	 *            the station object
	 * @param sat
	 *            the satellite object
	 * @return the noise level in [dBW]
	 */
	public static double getNdBW(Station sta, Satellite sat) {
		return getN0dBW(sta, sat) + 10 * Math.log10(sat.transponderBandwidth)
				+ 30; // KHz to Hz
	}

	/**
	 * Returns the bit error rate given the information rate
	 * 
	 * @param sta the station object
	 * @param sat the satellite object
	 * @param rate the information rate
	 * @return the error probability
	 */
	public static double getBER(Station sta, Satellite sat, double rate) {
		double SdBW, Eb, N0, EbN0;
		SdBW = getSdBW(sta, sat);
		Eb = 10 * Math.log10(Math.pow(10, SdBW / 10d) / (rate * 1000d));
		N0 = getN0dBW(sta, sat);
		EbN0 = Eb - N0;
		double ber = FEC.getBlockCodePE(EbN0, FEC.getFECParams(sat.FEC).n,
				FEC.getFECParams(sat.FEC).k, FEC.getFECParams(sat.FEC).t);
		if (!Double.isNaN(ber) && ber<0.5)
			return ber;
		else
			return 0.5;
	}

	/**
	 * Returns the bit error rate when NOT using any FEC code
	 * 
	 * @param sta  the station object
	 * @param sat the satellite object
	 * @param rate the information rate
	 * @return the ber rate in (0,0.5)
	 */
	public static double getUncodedBER(Station sta, Satellite sat, double rate) {
		double SdBW, Eb, N0, EbN0;
		SdBW = getSdBW(sta, sat);
		Eb = 10 * Math.log10(Math.pow(10, SdBW / 10d) / (rate * 1000d));
		N0 = getN0dBW(sta, sat);
		EbN0 = Eb - N0;
		double ber = 0.5 * Erf.erfc(Math.sqrt(EbN0));
		if (!Double.isNaN(ber) && ber<0.5)
			return ber;
		else
			return 0.5;
	}

}
