package com.vpquoi.hr;

/**
 * @author vpquoi
 *
 */
public abstract class Human {

	// first name
	private String firstName;
	// last name
	private String lastName;
	/**
	 * The contructor
	 */
	public Human() {
		firstName = "";
		lastName = "";
	}
	/**
	 * The contructor include 2 param
	 * @param firstName
	 * @param lastName
	 */
	public Human(final String firstName, final String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	/**
	 * @return the firstName
	 */
	public final String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public final String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
	/**
	 * Method use to convert object to string   
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("-First name: " + firstName);
		builder.append("\t" + "-Last name: " + lastName);
		String result = builder.toString();
		return result;
	}
}
