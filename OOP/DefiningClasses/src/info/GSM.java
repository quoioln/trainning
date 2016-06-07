package info;
import java.io.*;
import java.util.ArrayList;
//public enum BatteryType
public class GSM {
	public static GSM IPhone4S= new GSM("IPHONE4S", "US");
	// mode of phone
	private String model;
	// manufacturer of phone
	private String manufacturer;
	// price of phone
	private long price;
	// owner of phone
	private String owner;
	// Battery of phone 
	Battery battery;
	// info display of phone
	Display display;
	// list call history
	ArrayList<Call> callHistory;
	/**
	 * contructor
	 */
	public GSM() {
		model = null;
		manufacturer = null;
		price =  0;
		battery = new Battery();
		display = new Display();
		this.callHistory = new ArrayList<Call>();
	}
	/**
	 * contructor include 4 param:
	 * @param model
	 * @param manufacturer
	 * @param price
	 * @param owner
	 */
	public GSM(final String model, final String manufacturer, final long price, final String owner, final Battery battery, final Display display) {
		this.model = model;
		this.manufacturer = manufacturer;
		this.price = price;
		this.owner = owner;
		this.battery = battery;
		this.display = display;
		this.callHistory = new ArrayList<Call>();
	}
	/**
	 * contructor include 2 param:
	 * @param model
	 * @param manufacturer
	 * @param price
	 * @param owner
	 */
	public GSM(final String model, final String manufacturer) {
		this.model = model;
		this.manufacturer = manufacturer;
		this.price = 0;
		this.owner = null;
		this.battery = new Battery();
		this.display = new Display();
	}
	/**
	 * @return the model
	 */
	public final String getModel() {
		return model;
	}
	/**
	 * @param model the model to set
	 */
	public final void setModel(String model) {
		this.model = model;
	}
	/**
	 * @return the manufacturer
	 */
	public final String getManufacturer() {
		return manufacturer;
	}
	/**
	 * @param manufacturer the manufacturer to set
	 */
	public final void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	/**
	 * @return the price
	 */
	public final long getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public final void setPrice(long price) {
		this.price = price;
	}
	/**
	 * @return the owner
	 */
	public final String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public final void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the battery
	 */
	public final Battery getBattery() {
		return battery;
	}
	/**
	 * @param battery the battery to set
	 */
	public final void setBattery(Battery battery) {
		this.battery = battery;
	}
	/**
	 * @return the display
	 */
	public final Display getDisplay() {
		return display;
	}
	/**
	 * @param display the display to set
	 */
	public final void setDisplay(Display display) {
		this.display = display;
	}
	
	
	/**
	 * @return the callHistory
	 */
	public final ArrayList<Call> getCallHistory() {
		return callHistory;
	}
	/**
	 * @param callHistory the callHistory to set
	 */
	public final void setCallHistory(ArrayList<Call> callHistory) {
		this.callHistory = callHistory;
	}
	// method to convert GSM information to string
	public final String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Model: " + model + "\n");
		result.append("Manufacturer: " + manufacturer + "\n");
		result.append("Price: " + price + "\n");
		result.append("Owner: " + owner + "\n");
		result.append("Battery: " + battery.toString() + "\n");
		result.append("Display: " + display.toString() + "\n");
		return result.toString();
	}
	
	// Method use to show GSM information 
	public final void showInfo() {
		System.out.println(toString());
	}
	
	// Method use to add a call 
	public final void addCall(final Call call) {
		callHistory.add(call);
	}
	// Method use to delete a call by index
	public final boolean deleteCall(final int index) {
		if (index < (callHistory.size() - 1)) {
			callHistory.remove(index);
			return true;
		} else {
			return false;
		}
	}
	
	// Method use to clear call history list
	public final void clearCallHistory() {
		callHistory.clear();
	}

	// Method use to total time
	public final double totalTime() {
		double result = 0;
		for (Call call : callHistory) {
			result += (long) call.getDuration();
		}
		result /= (double)60;
		return result;
	}
	// Method use to total price
	public final double totalPrice(double cost) {
		double totalPrice = 0;
		double timeTotal = totalTime();
		totalPrice = timeTotal * cost;
		return totalPrice;
	}
	// Method use to get index longest call 
	public final int getIndexLongestCall() {
		int index = 0;
		
		long longestDuration = 0; 
		int i = 0;
		for (Call call : callHistory) {
			long duration = call.getDuration(); 
			if (duration > longestDuration) {
				longestDuration = duration;
				index = i; 
			}
			i ++;
		}
		return index;
	}
}