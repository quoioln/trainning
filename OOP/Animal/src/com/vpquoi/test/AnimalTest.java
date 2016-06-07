package com.vpquoi.test;

import java.util.ArrayList;

import com.vpquoi.animal.Animal;
import com.vpquoi.animal.Cat;
import com.vpquoi.animal.Dog;
import com.vpquoi.animal.Frog;
import com.vpquoi.animal.ISound;
import com.vpquoi.animal.Kitten;
import com.vpquoi.animal.TomCat;

public class AnimalTest {

	public static void main(String[] args) {
		
		ArrayList<ISound> animalList = new ArrayList<ISound>();
		Cat cat1 = new Cat("Cat 1", 2, "Male");
		Cat cat2 = new Cat("Cat 2", 5, "Male");
		Cat cat3 = new Cat("Cat 3", 6, "Male");
		
		Kitten kitten1 = new Kitten("Kitten 1", 2);
		Kitten kitten2 = new Kitten("Kitten 2", 6);
		
		TomCat tomcat1 = new TomCat("Tomcat 1", 1);
		TomCat tomcat2 = new TomCat("Tomcat 2", 7);
		
		Dog dog1 = new Dog("Dog 1", 3, "Male");
		Dog dog2 = new Dog("Dog 2", 4, "Male");
		Dog dog3 = new Dog("Dog 3", 1, "Male");
		
		Frog frog1 = new Frog("Frog 1", 5, "Male");
		Frog frog2 = new Frog("Frog 2", 7, "Male");
		Frog frog3 = new Frog("Frog 2", 1, "Male");
		animalList.add(cat1);
		animalList.add(dog1);
		animalList.add(frog1);
		animalList.add(kitten1);
		animalList.add(tomcat1);
		animalList.add(dog2);
		animalList.add(tomcat2);
		animalList.add(kitten2);
		animalList.add(frog2);
		animalList.add(cat2);
		animalList.add(dog3);
		animalList.add(cat3);
		animalList.add(frog3);
		 
		
		// init list cat
		ArrayList<Animal> catList = new ArrayList<Animal>();
		catList.add(cat1);
		catList.add(cat2);
		catList.add(cat3);
		// init list tom cat
		ArrayList<Animal> tomCatList = new ArrayList<Animal>();
		tomCatList.add(tomcat1);
		tomCatList.add(tomcat2);
		
		// init list kitten
		ArrayList<Animal> kittenList = new ArrayList<Animal>();
		kittenList.add(kitten1);
		kittenList.add(kitten2);
		// init list dog
		ArrayList<Animal> dogList = new ArrayList<Animal>();
		dogList.add(dog1);
		dogList.add(dog2);
		dogList.add(dog3);
		// init list frog
		ArrayList<Animal> frogList = new ArrayList<Animal>();
		frogList.add(frog1);
		frogList.add(frog2);
		frogList.add(frog3);
		
		/**
		 * Animal sound
		 */
		
		for (ISound iSound : animalList) {
//			System.out.print("Name: " + animal.getName() + "\tSound: ");
			iSound.sound();
		}
		
		// Diplay age average
		System.out.println("Age Average of Cat: " + Animal.averageAge(catList));
		System.out.println("Age Average of Tomcat: " + Animal.averageAge(tomCatList));
		System.out.println("Age Average of Kitten: " + Animal.averageAge(kittenList));
		System.out.println("Age Average of Dog: " + Animal.averageAge(dogList));
		System.out.println("Age Average of Frog: " + Animal.averageAge(frogList));
		
		
	}

}
