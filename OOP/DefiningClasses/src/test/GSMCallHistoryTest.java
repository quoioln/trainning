package test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import info.Battery;
import info.Call;
import info.Display;
import info.GSM;

/**
 * @author vpquoi
 *
 */
public class GSMCallHistoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Initial GSM 
		Battery battery = new Battery("PinAP", 24, 24);
		Display display = new Display(4, 16000);
		GSM gsm = new GSM("IP123", "US", 2000, "abc", battery, display);
		// Inittial call
		Date date1 = new Date(2016, 5, 20);
		Date date2 = new Date(2016, 5, 22);
		Date date3 = new Date(2016, 5, 25);
		Time time1 = new Time(9, 20, 20);
		Time time2 = new Time(6, 40, 30);
		Time time3 = new Time(7, 10, 40);
		
		String phoneNum1 = "0979921380";
		String phoneNum2 = "0963864271";
		String phoneNum3 = "01234567890";
		String phoneNum4 = "01234689111";
		String phoneNum5 = "01264654546";
		String phoneNum6 = "01255545454";
		int duration = 150;
		Call call1 = new Call(date1, time1, phoneNum1, duration);
		Call call2 = new Call(date1, time1, phoneNum2, duration + 10);
		Call call3 = new Call(date1, time1, phoneNum3, duration - 20);
		Call call4 = new Call(date1, time1, phoneNum4, duration + 1000);
		Call call5 = new Call(date1, time1, phoneNum5, duration + 110);
		Call call6 = new Call(date1, time1, phoneNum6, duration + 120);
		
		// add call
		gsm.addCall(call1);
		gsm.addCall(call2);
		gsm.addCall(call3);
		gsm.addCall(call4);
		gsm.addCall(call5);
		gsm.addCall(call6);
		
		// Display information call
		System.out.println("Call history:");
		ArrayList<Call> callHistory = gsm.getCallHistory();
		int i = 0;
		for (Call callInfo : callHistory) {
			i++;
			System.out.println("Call " + i + ": ");
			callInfo.showInfo();
		}
		
		// total Price
		double cost = 0.37;
		double totalPrice = gsm.totalPrice(cost);
		System.out.println("Total price of GSM: " + totalPrice +"$");
		
		// Remove the longest call from the history
		int indexLongestCall = gsm.getIndexLongestCall();
		if (!gsm.deleteCall(indexLongestCall)) {
			System.out.println("Can not delete call.");
		}
		
		// calculate the total price after delete longest call
		cost = 0.37;
		totalPrice = gsm.totalPrice(cost);
		System.out.println("Total price of GSM: " + totalPrice +"$");
		
		// Clear call history
		gsm.clearCallHistory();
		// Display information call
		System.out.println("Call history after clear:");
		callHistory = gsm.getCallHistory();
		i = 0;
		for (Call callInfo : callHistory) {
			i++;
			System.out.println("Call " + i + ": ");
			callInfo.showInfo();
		}
	}

}
