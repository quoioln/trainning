package info;
public class Battery {
	public enum BatterType {
		LiIon, NiMH, NiCd
	}
	
	private String model;
	private int hoursIdel;
	private int hoursTalk;
	/**
	 * Contructor  
	 */
	public Battery() {
		model =  null;
		hoursIdel = 0;
		hoursTalk = 0;
	}
	/**
	 * Contructor include 3 param:
	 * @param model
	 * @param hoursIdle
	 * @param hourstalk
	 */
	public Battery(final String model, final  int hoursIdel, final int hoursTalk) {
		this.model = model;
		this.hoursIdel = hoursIdel;
		this.hoursTalk = hoursTalk;
	}
	
	public final String getModel() {
		return model;
	}

	public final void setModel(String model) {
		this.model = model;
	}

	public final int getHoursIdel() {
		return hoursIdel;
	}

	public final void setHoursIdel(int hoursIdel) {
		this.hoursIdel = hoursIdel;
	}

	public final int getHoursTalk() {
		return hoursTalk;
	}

	public final void setHourstalk(int hoursTalk) {
		this.hoursTalk = hoursTalk;
	}
	
	public final String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Infomation of battery:\n");
		result.append("\tHours Idle: " + hoursIdel + "\n");
		result.append("\tHours talk: " + hoursTalk);
		return result.toString();
	}
	
}