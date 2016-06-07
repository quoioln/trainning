/**
 * class use to test class Student 
 */
package com.vpquoi.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.vpquoi.hr.Human;
import com.vpquoi.hr.HumanComparator;
import com.vpquoi.hr.Student;
import com.vpquoi.hr.StudentComparator;
import com.vpquoi.hr.Worker;
import com.vpquoi.hr.WorkerComparator;

/**
 * @author vpquoi
 *
 */
public class HRTest {

	/**
	 * 
	 */
	
	// main test
	public static void main(String[] args) {
		String[] lastNameList = new String[]{"Vo", "Nguyen", "Dang", "Truong", "Ho", "Tran", "Le"};
		String[] firstNameList = new String[]{"Quoi", "Nhung", "Nhan", "Hung", "Tuan", "Nam", "Duong", "Tien", "Hang", "Nguyen", "Chau"};
		int sizeOfFirstName = firstNameList.length;
		int sizeOfLastName = lastNameList.length;
		float maxWeeksalary = 100f;
		float minWeeksalary = 50;
		float maxWorkHoursPerDay = 24;
		float minWorkHoursPerDay = 2;
		boolean[][] checkFullName = new boolean [sizeOfFirstName][sizeOfLastName];
		int sizeOfPerson = 10;
		int count = 0;
		Random random = new Random();
		
		ArrayList<Student> studentList = new ArrayList<Student>();
		String firstName = "";
		String lastName = "";
		int indexFirstName = 0;
		int indexLastName = 0;
		int grade = 0;
		
		Student student = new Student();
		while (count < sizeOfPerson) {
			indexFirstName = random.nextInt(sizeOfFirstName - 1);
			indexLastName = random.nextInt(sizeOfLastName - 1);
			while (true) {
				grade = random.nextInt(12);
				if (grade != 0)
					break;
			}
			if (!checkFullName[indexFirstName][indexLastName]) {
				firstName = firstNameList[indexFirstName];
				lastName = lastNameList[indexLastName];
//				student.setFirstName(firstName);
//				student.setLastName(lastName);
//				student.setGrade(grade);
				student = new Student(firstName, lastName, grade);
				studentList.add(student);
				checkFullName[indexFirstName][indexLastName] = true;
				count ++;
			}
		}
		int i = 0;
		/*
		// Display list student before sort
		System.out.println("List student before sort: ");
		
		for (Student s : studentList) {
			i++;
			System.out.println("Student " + i + ": ");
			System.out.println(s.toString());
		}
		*/
		// sort list student
		Collections.sort(studentList, new StudentComparator());
		// Display list student after sort
		System.out.println("List student before sort: ");
		i = 0;
		for (Student s : studentList) {
			i++;
			System.out.println("Student " + i + ": ");
			System.out.println(s.toString());
		}
		
		// Initial 10 worker
		ArrayList<Worker> workerList = new ArrayList<Worker>();
		firstName = "";
		lastName = "";
		count = 0;
		float weekSalary;
		// work hours per day
		float workHoursPerDay;
		Worker worker = new Worker();
		while (count < sizeOfPerson) {
			indexFirstName = random.nextInt(sizeOfFirstName - 1);
			indexLastName = random.nextInt(sizeOfLastName - 1);
			while (true) {
				weekSalary = random.nextFloat() * (maxWeeksalary - minWeeksalary) + minWeeksalary;
				if (grade != 0)
					break;
			}
			while (true) {
				workHoursPerDay = random.nextFloat() * (maxWorkHoursPerDay - minWorkHoursPerDay) + minWorkHoursPerDay;
				if (grade != 0)
					break;
			}
			if (!checkFullName[indexFirstName][indexLastName]) {
				firstName = firstNameList[indexFirstName];
				lastName = lastNameList[indexLastName];
				worker = new Worker(firstName, lastName, weekSalary, workHoursPerDay);
				workerList.add(worker);
				checkFullName[indexFirstName][indexLastName] = true;
				count ++;
			}
		}
		i = 0;
		// Diplay list worker before sort
		for (Worker w : workerList) {
			i++;
			System.out.println("Worker " + i + ": ");
			System.out.println(w.toString());
		}
		Collections.sort(workerList, new WorkerComparator());
		i = 0;
		// Diplay list worker after sort
		for (Worker w : workerList) {
			i++;
			System.out.println("Worker " + i + ": ");
			System.out.println(w.toString());
		}
		
		/*
		 *  Merge list student and list worker
		 */
		
		// declare list human
		ArrayList<Human> humanList = new ArrayList<Human>();
		Human human;
		for (Student s : studentList) {
			human = s;
			humanList.add(human);
		}
		
		for (Worker w : workerList) {
			human = w;
			humanList.add(human);
		}
		// sort human list by full name
		Collections.sort(humanList, new HumanComparator());
		/**
		 * Display human after sort by full name
		 */
		i = 0;
		// Diplay list worker after sort
		for (Human h : humanList) {
			i++;
			System.out.println("Human " + i + ": ");
			System.out.println(h.toString());
		}
		
	}

}
