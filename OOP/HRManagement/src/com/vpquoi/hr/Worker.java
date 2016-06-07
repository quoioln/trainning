/**
 * 
 */
package com.vpquoi.hr;

/**
 * @author vpquoi
 *
 */
public class Worker extends Human {
	private final int daysWork = 7;
	// week salary
	private float weekSalary;
	// work hours per day
	private float workHoursPerDay;
	
	/**
	 * The contructor 
	 */
	public Worker() {
		super();
	}

	/**
	 * The contructor include 2 param
	 * @param weekSalary
	 * @param workHoursPerDay
	 */
	public Worker(final float weekSalary, final float workHoursPerDay) {
		super();
		this.weekSalary = weekSalary;
		this.workHoursPerDay = workHoursPerDay;
	}

	/**
	 * The contructor include 4 param
	 * @param firstName
	 * @param lastName
	 * @param weekSalary
	 * @param workHoursPerDay
	 */
	public Worker(final String firstName, final String lastName, final float weekSalary, final float workHoursPerDay) {
		super(firstName, lastName);
		this.weekSalary = weekSalary;
		this.workHoursPerDay = workHoursPerDay;
	}
	/**
	 * Method use to convert object to string   
	 */
	public String toString() {
		String fullName = super.toString();
		StringBuilder builder = new StringBuilder(fullName);
//		builder.append("-First name: " + firstName);
//		builder.append("\t" + "-Last name: " + lastName);
		builder.append("\t-Week Salary: " + weekSalary);
		builder.append("\t-work hours per day: " + workHoursPerDay);
		builder.append("\t-Money per hour: " + moneyPerHour());
		String result = builder.toString();
		return result;
	}
	/**
	 * The method use to calculate money earned by hour by the worker
	 */
	public final float moneyPerHour() {
		float money = weekSalary / (daysWork * workHoursPerDay);
		return money;
	}
}
