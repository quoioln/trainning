package com.vpquoi.animal;

public class TomCat extends Cat implements ISound {
	
	public TomCat() {
		super();
	}
	/**
	 * The contructor include 3 param:
	 * @param name
	 * @param age
	 * @param gender
	 */
	public TomCat(String name, int age) {
		super(name, age, "Male");
	}
	@Override
	public void sound() {
		System.out.println(mySound);
	}
}
