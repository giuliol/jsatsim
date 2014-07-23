package dsp.unige.figures;

import org.junit.Test;

public class ChannelHelperTest {

	@Test
	public void test() {
		ChannelHelper.Satellite sat= new ChannelHelper.Satellite(45,600000,Orbits.ORBIT_TYPE_LEO, Modulation.MOD_BPSK);
		sat.ORBIT_TYPE=Orbits.ORBIT_TYPE_LEO;
		
		ChannelHelper.Station sta = new ChannelHelper.Station(39, 0, 25.0, 47.1 , 15, 60,100, 0);
		sat.EIRP=45;
		
		double rain= ChannelHelper.getRainAttenuation(sta);
		double fsl=ChannelHelper.getFreeSpaceLoss(sta, sat);
		
		
		System.out.println("ChannelHelperTest.test() ==============================   ");
		
		System.out.println(""+Orbits.getOrbitName(sat.ORBIT_TYPE));
		
		System.out.println("rain attenuation att="+rain +"\nFSL = "+fsl);
		
		double s = sat.EIRP - fsl - rain;
		double n = ChannelHelper.getN0dBW(sta, sat);
				
		double bw=ChannelHelper.getHSCapacity(600000000, s  , n);
		
		System.out.println("ChannelHelperTest.test() Signal level: "+ s +" dBW. Noise level "+ n+" dBW");
		System.out.println("SNR (dB) = " +(s-n));

		System.out.println("Shannon max bw. "+String.format("%6.2f",bw)+" bit/s");
		
	}

}
