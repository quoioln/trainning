package com.vpquoi.hr;

import java.util.Comparator;

/**
 * @author vpquoi
 *
 */
public class HumanComparator implements Comparator<Human>{

	@Override
	public int compare(Human human1, Human human2) {
		String firstName1 =  human1.getFirstName();
		String firstName2 =  human2.getFirstName();
		
		int check1 = firstName1.compareToIgnoreCase(firstName2);
		if (check1 < 0) {
			return -1;
		}
		if (check1 > 0)
			return 1;
		if (check1 == 0) {
			String lastName1 =  human1.getLastName();
			String lastName2 =  human2.getLastName();
			int check2 = lastName1.compareToIgnoreCase(lastName2);
			if (check2 < 0) {
				return -1;
			}
			if (check2 > 0)
				return 1;
		}
		return 0;
	}
}
