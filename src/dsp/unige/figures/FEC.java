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

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.CombinatoricsUtils;

public class FEC {

	public static final int FEC_BCH_63_57 = 0;
	public static final int FEC_BCH_63_45 = 1;
	public static final int FEC_BCH_63_30 = 2;
	public static final int FEC_BCH_255_231 = 3;
	public static final int FEC_BCH_255_187 = 4;
	public static final int FEC_BCH_255_123 = 5;


	public static final int fecs = 6;

	private static final String FEC_BCH_63_57_HRNAME = "0.9 Rate BCH(63,57)";
	private static final String FEC_BCH_63_45_HRNAME = "0.714 Rate BCH(64,45)";
	private static final String FEC_BCH_63_30_HRNAME = "0.476 Rate BCH(63,30)";
	private static final String FEC_BCH_255_231_HRNAME = "0.9 Rate BCH(255,231)";
	private static final String FEC_BCH_255_187_HRNAME = "0.73 Rate BCH(255,187)";
	private static final String FEC_BCH_255_123_HRNAME = "0.482 Rate BCH(255,123)";


	/** 
	 * Returns a human readable string identifying the FEC code
	 * @param FEC_TYPE
	 * @return the human readable string
	 */
	public static String getHRname(int FEC_TYPE) {
		switch (FEC_TYPE) {
		case FEC_BCH_63_57:
			return FEC_BCH_63_57_HRNAME;
		case FEC_BCH_63_45:
			return FEC_BCH_63_45_HRNAME;
		case FEC_BCH_63_30:
			return FEC_BCH_63_30_HRNAME;
		case FEC_BCH_255_231:
			return FEC_BCH_255_231_HRNAME;
		case FEC_BCH_255_187:
			return FEC_BCH_255_187_HRNAME;
		case FEC_BCH_255_123:
			return FEC_BCH_255_123_HRNAME;
			
		default:
			return "UNKNOWN_FEC";
		}
	}

	public static FECparams getFECParams(int FEC_TYPE) {
		switch (FEC_TYPE) {
		case FEC_BCH_63_57:
			return new FECparams(63, 57, 1);
		case FEC_BCH_63_45:
			return new FECparams(63, 45, 3);
		case FEC_BCH_63_30:
			return new FECparams(63, 30, 6);
		case FEC_BCH_255_231:
			return new FECparams(255, 231, 3);
		case FEC_BCH_255_187:
			return new FECparams(255, 187, 9);
		case FEC_BCH_255_123:
			return new FECparams(255, 123, 19);
		default:
			return new FECparams(0, 0, 0);
		}
	}

	static class FECparams {
		public int n;
		public int k;
		public int t;

		private FECparams(int nn, int kk, int tt) {
			t = tt;
			n = nn;
			k = kk;
		}
	}

	/**
	 * returns the error probability
	 * @param gammaB avg. bit energy (Eb/N0)
	 * @param n FEC block length
	 * @param k	information bits in block
	 * @param t FEC correction capability
	 * @return the error probability
	 */
	public static double getBlockCodePE(double gammaB, int n, int k, int t) {
		return CombinatoricsUtils.binomialCoefficientDouble(n - 1, t)
				* Math.pow(Q(Math.sqrt(2 * (double) k / (double) n * gammaB)),
						t + 1);
	}

	private static double Q(double arg) {
		return 0.5 * Erf.erfc(arg / Math.sqrt(2d));
	}

}
