/**
 * 
 */
package com.vpquoi.hr;

/**
 * @author vpquoi
 *
 */
public class Student extends Human{

	private int grade;
	/**
	 * The conrtuctor
	 */
	public Student() {
		super();
		grade = 0;
	}
	
	/**
	 * The contructor include 1 param
	 * @param grade
	 */
	public Student(final int grade) {
		super();
		this.grade = grade;
	}
	
	/**
	 * The contructor include 3 param
	 * @param firstName
	 * @param lastName
	 * @param grade
	 */
	public Student(final String firstName, final String lastName, final int grade) {
		super(firstName, lastName);
		this.grade = grade;
	}
	
	/**
	 * @return the grade
	 */
	public int getGrade() {
		return grade;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	public String toString() {
		String fullName = super.toString();
		StringBuilder builder = new StringBuilder(fullName);
		String result = "";
//		builder.append("-First name: " + firstName);
//		builder.append("\t" + "-Last name: " + lastName);
		builder.append("\t" + "-Grade: " + grade);
		result = builder.toString();
		return result;
	}
}
