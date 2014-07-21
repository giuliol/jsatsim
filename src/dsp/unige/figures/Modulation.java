package dsp.unige.figures;

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
}
