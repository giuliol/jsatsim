package dsp.unige.figures;

import org.apache.commons.math3.special.Erf;

public class Modulation {

	public static final int MOD_BPSK=0;
	public static final int MOD_QPSK=1;
	public static final int MOD_8PSK=2;



	private static final String MOD_BPSK_HRNAME = "BPSK";
	private static final String MOD_QPSK_HRNAME = "QPSK";
	private static final String MOD_8PSK_HRNAME = "8PSK";

	public static final String getHRname(int MODULATION_TYPE){
		switch (MODULATION_TYPE) {
		case MOD_BPSK:
			return MOD_BPSK_HRNAME;

		case MOD_QPSK:
			return MOD_QPSK_HRNAME;
			
		case MOD_8PSK:
			return MOD_8PSK_HRNAME;
		default:
			return "UNDEFINED_MODULATION";
		}
	}
	public static int mods = 3;


	private static double BPSKBER(double ebn0) {
		return 0.5*Erf.erfc(Math.sqrt(ebn0));
	}

	public static int getSpectralEfficiency(int modulation) {
		System.out.println("Modulation.getSpectralEfficiency() modulation "+getHRname(modulation));
		switch (modulation) {
		case MOD_BPSK:
			return 1;

		case MOD_QPSK:
			return 2;
			
		case MOD_8PSK:
			return 4;

		default:
		return 0;
		}
	}
}
