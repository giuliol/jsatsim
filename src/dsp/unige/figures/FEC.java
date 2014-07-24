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

	public static final int fecs = 1;

	private static final String FEC_BCH_63_57_HRNAME = "1/2 Rate CC + RS(255,233)";

	public static String getHRname(int FEC_TYPE) {
		switch (FEC_TYPE) {
		case FEC_BCH_63_57:
			return FEC_BCH_63_57_HRNAME;

		default:
			return "UNKNOWN_FEC";
		}
	}

	public static FECparams getFECParams(int FEC_TYPE) {
		switch (FEC_TYPE) {
		case FEC_BCH_63_57:
			return new FECparams(63, 57, 1);

		default:
			return new FECparams(0, 0, 0);
		}
	}

	public static class FECparams {
		public int n;
		public int k;
		public int t;

		public FECparams(int nn, int kk, int tt) {
			t = tt;
			n = nn;
			k = kk;
		}
	}

	public static double getBlockCodePE(double gamB, int n, int k, int t) {

		System.out.println("FEC.getBlockCodePE() n=" + n + ", k=" + k);
		return CombinatoricsUtils.binomialCoefficientDouble(n - 1, t)
				* Math.pow(Q(Math.sqrt(2 * (double) k / (double) n * gamB)),
						t + 1);
	}

	private static double Q(double arg) {
		// TODO Auto-generated method stub
		return 0.5 * Erf.erfc(arg / Math.sqrt(2d));
	}

}
