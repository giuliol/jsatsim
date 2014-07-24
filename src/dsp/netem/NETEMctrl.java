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

package dsp.netem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NETEMctrl {

	private String netInterface;

	public NETEMctrl(String intf) {
		netInterface = intf;
	}

	public StringBuffer setNetworkConditions(String rate, String ber,
			String loss, String delay, String pBuffer) {
		Process p, p2;
		try {
			// System.out.println("setnetcondition: eseguo");
			// System.out.println("tc qdisc change dev "+netInterface+" root netem rate "+rate+"kbit loss "+loss+"% corrupt "+ber+"% delay "+delay+"ms");
			// System.out.println("\n");

			p = Runtime.getRuntime().exec(
					"tc qdisc change dev " + netInterface + " root netem rate "
							+ rate + "kbit loss " + loss + "% corrupt " + ber
							+ "% delay " + delay + "ms" + " limit " + pBuffer);
			p.waitFor();
			// System.out.println("tc qdisc change dev "+netInterface+" root netem rate "+rate+"kbit loss "+loss+"% corrupt "+ber+"% delay "+delay+"ms"+" limit "+pBuffer);
			BufferedReader rr = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			StringBuffer sb1 = new StringBuffer();

			String line1 = rr.readLine();
			sb1.append(line1);

			while (line1 != null) {
				line1 = rr.readLine();
				sb1.append(line1 + "\n");
			}

			// System.out.print(sb1);

			p2 = Runtime.getRuntime().exec("tc qdisc ls");
			p2.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p2.getInputStream()));

			StringBuffer sb = new StringBuffer();

			String line = reader.readLine();
			sb.append(line);

			while (line != null) {
				line = reader.readLine();
				sb.append(line + "\n");
			}

			// System.out.print(sb);
			return sb;

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getNICname() {
		return netInterface;
	}

	public StringBuffer setNetworkConditions(String rate, String ber,
			String loss, String delay) {
		Process p, p2;
		try {

			String command = "tc qdisc change dev " + netInterface
					+ " root netem rate " + rate + "kbit loss " + loss
					+ "% corrupt " + ber + "% delay " + delay + "ms";
			System.out.println("setnetcondition: eseguo");
			System.out.println(command);
			System.out.println("\n");

			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader rr = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			StringBuffer sb1 = new StringBuffer();

			String line1 = rr.readLine();
			sb1.append(line1);

			while (line1 != null) {
				line1 = rr.readLine();
				sb1.append(line1 + "\n");
			}

			// System.out.print(sb1);

			p2 = Runtime.getRuntime().exec("tc qdisc ls");
			p2.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p2.getInputStream()));

			StringBuffer sb = new StringBuffer();

			String line = reader.readLine();
			sb.append(line);

			while (line != null) {
				line = reader.readLine();
				sb.append(line + "\n");
			}

			// System.out.print(sb);
			return sb;

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void reset() {
		// TODO Auto-generated method stub
		try {
			Runtime.getRuntime().exec(
					"tc qdisc del dev " + netInterface + " root netem");
			Runtime.getRuntime().exec(
					"tc qdisc add dev " + netInterface + " root netem");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
