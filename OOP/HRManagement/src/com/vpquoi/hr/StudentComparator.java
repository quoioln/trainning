package com.vpquoi.hr;

import java.util.Comparator;

/**
 * @author vpquoi
 *
 */
@SuppressWarnings("hiding")
public class StudentComparator implements Comparator<Student>{

	@Override
	public int compare(Student student1, Student student2) {
		int grade1 =  student1.getGrade();
		int grade2 =  student2.getGrade();
		if (grade1 < grade2)
			return -1;
		if (grade1 > grade2)
			return 1;
		return 0;
	}
}
