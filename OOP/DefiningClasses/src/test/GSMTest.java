package test;

import java.util.ArrayList;

import info.Battery;
import info.Display;
import info.GSM;;
public class GSMTest {
	public static void main(String[] args) {
		// initial list gsm
		ArrayList<GSM> gsmList = new ArrayList<GSM>();
		Battery battery1 = new Battery("Pin123", 2, 2);
		Battery battery2 = new Battery("Pin456", 3, 3);
		Display display1 = new Display(6, 48000);
		Display display2 = new Display(4, 16000);
		GSM gsm1 = new GSM("IP123", "US", 2000, "abc", battery1, display1);
		GSM gsm2 = new GSM("OP234", "CN", 1000, "nvc", battery2, display1);		
		GSM gsm3 = new GSM("SN234", "JP", 2000, "vpq", battery1, display2);
		gsmList.add(gsm1);
		gsmList.add(gsm2);
		gsmList.add(gsm3);
		
		// display infomation gsm list
		int i = 1;
		for (GSM gsm : gsmList) {
			System.out.println("Infomation GSM " + i + ":");
			gsm.showInfo();
			i++;
		}
		
		// Display the information about the static property IPhone4S.
		System.out.println("Display the information about the static property IPhone4S");
		GSM.IPhone4S.showInfo();
	}
}
