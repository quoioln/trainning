package com.vpquoi.animal;

public class Frog extends Animal implements ISound{
	protected String mySound = "op op";
	public Frog() {
		super();
	}
	/**
	 * The contructor include 3 param:
	 * @param name
	 * @param age
	 * @param gender
	 */
	public Frog(String name, int age, String gender) {
		super(name, age, gender);
	}
	@Override
	public void sound() {
		System.out.println(mySound);
	}

}
