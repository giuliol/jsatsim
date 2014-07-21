package dsp.netem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class NETEMctrl {

	private String netInterface;
	
	public NETEMctrl (String intf){
		netInterface=intf;
	}
	public StringBuffer setNetworkConditions(String rate, String ber, String loss, String delay, String pBuffer){
		Process p,p2;
		  try {
//			 System.out.println("setnetcondition: eseguo");
//			 System.out.println("tc qdisc change dev "+netInterface+" root netem rate "+rate+"kbit loss "+loss+"% corrupt "+ber+"% delay "+delay+"ms");
//			 System.out.println("\n");


			p = Runtime.getRuntime().exec("tc qdisc change dev "+netInterface+" root netem rate "+rate+"kbit loss "+loss+"% corrupt "+ber+"% delay "+delay+"ms"+" limit "+pBuffer);
			p.waitFor();
//			System.out.println("tc qdisc change dev "+netInterface+" root netem rate "+rate+"kbit loss "+loss+"% corrupt "+ber+"% delay "+delay+"ms"+" limit "+pBuffer);
			BufferedReader rr=new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuffer sb1=new StringBuffer();
			
			String line1=rr.readLine();
			sb1.append(line1);
			
			while (line1 != null) {
				line1 = rr.readLine();
				sb1.append(line1+"\n");
			}
			
//			System.out.print(sb1);

			p2= Runtime.getRuntime().exec("tc qdisc ls");
			p2.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
			p2.getInputStream()));
			
			StringBuffer sb = new StringBuffer();

			String line = reader.readLine();
			sb.append(line);

			while (line != null) {
				line = reader.readLine();
				sb.append(line+"\n");
			}
			
//			System.out.print(sb);
			return sb;
			
		  } catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public StringBuffer setNetworkConditions(String rate, String ber, String loss, String delay){
		Process p,p2;
		  try {
			 System.out.println("setnetcondition: eseguo");
			 System.out.println("tc qdisc change dev "+netInterface+" root netem rate "+rate+"kbit loss "+loss+"% corrupt "+ber+"% delay "+delay+"ms");
			 System.out.println("\n");


			p = Runtime.getRuntime().exec("tc qdisc change dev "+netInterface+" root netem rate "+rate+"kbit loss "+loss+"% corrupt "+ber+"% delay "+delay+"ms");
			p.waitFor();
			BufferedReader rr=new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuffer sb1=new StringBuffer();
			
			String line1=rr.readLine();
			sb1.append(line1);
			
			while (line1 != null) {
				line1 = rr.readLine();
				sb1.append(line1+"\n");
			}
			
//			System.out.print(sb1);

			p2= Runtime.getRuntime().exec("tc qdisc ls");
			p2.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
			p2.getInputStream()));
			
			StringBuffer sb = new StringBuffer();

			String line = reader.readLine();
			sb.append(line);

			while (line != null) {
				line = reader.readLine();
				sb.append(line+"\n");
			}
			
//			System.out.print(sb);
			return sb;
			
		  } catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
