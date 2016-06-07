/**
 * 
 */
package com.vpquoi.animal;

/**
 * @author vpquoi
 *
 */
public class Dog extends Animal implements ISound {
	private String mySound = "gau gau";
	/**
	 * The contructor
	 */
	public Dog() {
		super();
	}
	
	/**
	 * The contructor include 3 param:
	 * @param name
	 * @param age
	 * @param gender
	 */
	public Dog(String name, int age, String gender) {
		super(name, age, gender);
	}

	@Override
	public void sound() {
		System.out.println(mySound);
	}

}
