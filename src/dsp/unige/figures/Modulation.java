
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

public class Modulation {

	public static final int MOD_BPSK = 0;
	public static final int MOD_QPSK = 1;
	public static final int MOD_8PSK = 2;

	private static final String MOD_BPSK_HRNAME = "BPSK";
	private static final String MOD_QPSK_HRNAME = "QPSK";
	private static final String MOD_8PSK_HRNAME = "8PSK";

	public static final String getHRname(int MODULATION_TYPE) {
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

	public static int getSpectralEfficiency(int modulation) {
		System.out.println("Modulation.getSpectralEfficiency() modulation "
				+ getHRname(modulation));
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
