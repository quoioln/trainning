package com.vpquoi.animal;

import java.util.ArrayList;

public class Animal {
	protected String name;
	protected int age;
	protected String gender;
	
	/*
	 * The contructor
	 */
	public Animal() {
		name = "";
		age = 0;
		gender = "";
	}
	/**
	 * The contructor include 3 param:
	 * @param name
	 * @param age
	 * @param gender
	 */
	public Animal(String name, int age, String gender) {
		this.name = name;
		this.age = age;
		this.gender = gender;
	}
	/**
	 * Method use calculate the average age in list animal
	 */
	public static float averageAge(ArrayList<Animal> animalList) {
		float totalAge = 0;
		for (Animal animal : animalList) {
			totalAge += animal.getAge();
		}
		int sizeOfAnimal = animalList.size();
		float ageAverage = totalAge / sizeOfAnimal;
		return ageAverage;
	}
	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the age
	 */
	public final int getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public final void setAge(int age) {
		this.age = age;
	}
	/**
	 * @return the gender
	 */
	public final String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public final void setGender(String gender) {
		this.gender = gender;
	}

}
