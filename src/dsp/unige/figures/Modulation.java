package dsp.unige.figures;

import org.apache.commons.math3.special.Erf;

public class Modulation {

	public static final int BPSK=0;
	
	private static final String BPSK_HRNAME = "BPSK";
	
	public static final String getHRname(int MODULATION_TYPE){
		switch (MODULATION_TYPE) {
		case BPSK:
			return BPSK_HRNAME;

		default:
			return "UNDEFINED_MODULATION";
		}
	}
	public static int mods = 1;

	public static double getBER(double ebn0, int mODULATION_TYPE) {
		
		switch (mODULATION_TYPE) {
		case BPSK:
			return BPSKBER(ebn0);

		default:
			return 0;
		}
	}

	private static double BPSKBER(double ebn0) {
		return 0.5*Erf.erfc(Math.sqrt(ebn0));
	}
}
