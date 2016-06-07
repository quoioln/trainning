package com.vpquoi.animal;

public class Kitten extends Cat implements ISound {
	
	/**
	 * The contructor include 3 param:
	 * @param name
	 * @param age
	 * @param gender
	 */
	public Kitten(String name, int age) {
		super(name, age, "Female");	
	}
	@Override
	public void sound() {
		System.out.println(mySound);
	}
}
