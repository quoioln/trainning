package info;

import java.sql.Time;
import java.util.Date;

import util.DateUtil;

/**
 * @author vpquoi
 *
 */
public class Call {
	private Date date;
	private Time time;
	private String phoneNum;
	private long duration;
	
	/**
	 * Contructor
	 */
	public Call() {
		
	}
	/**
	 * Contructor include 4 param:
	 * @param date
	 * @param time
	 * @param phoneNum
	 * @param duration
	 */
	public Call(Date date, Time time, String phoneNum, long duration) {
		this.date = date;
		this.time = time;
		this.phoneNum = phoneNum;
		this.duration = duration;
	}
	/**
	 * @return the duration
	 */
	public final long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public final void setDuration(final long duration) {
		this.duration = duration;
	}
	/**
	 * @return the date
	 */
	public final Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public final void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the time
	 */
	public final Time getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public final void setTime(Time time) {
		this.time = time;
	}
	/**
	 * @return the phoneNum
	 */
	public final String getPhoneNum() {
		return phoneNum;
	}
	/**
	 * @param phoneNum the phoneNum to set
	 */
	public final void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	// Method use to convert to string
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		String dateInfo = DateUtil.toString(date);
		builder.append("\tDate: " + dateInfo + "\n");
		builder.append("\tTime: " + time.toString() + "\n");
		builder.append("\tPhone number: " + phoneNum + "\n");
		builder.append("\tDuration: " + duration);
		return builder.toString();
	}
	
	// Method use to show call information
	public final void showInfo() {
		String info = toString();
		System.out.println(info);
	}
}
			