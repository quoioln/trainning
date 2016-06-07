/**
 * 
 */
package com.vpquoi.animal;

/**
 * @author vpquoi
 *
 */
public class Cat extends Animal implements ISound {

	protected String mySound = "meo meo";
	/**
	 * The contructor
	 */
	public Cat() {
		super();
	}
	/**
	 * The contructor include 3 param:
	 * @param name
	 * @param age
	 * @param gender
	 */
	public Cat(String name, int age, String gender) {
		super(name, age, gender);
	}
	
	@Override
	public void sound() {
		System.out.println(mySound);
	}
}
