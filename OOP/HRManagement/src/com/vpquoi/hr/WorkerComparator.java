package com.vpquoi.hr;

import java.util.Comparator;

/**
 * @author vpquoi
 *
 */
public class WorkerComparator implements Comparator<Worker>{

	@Override
	public int compare(Worker worker1, Worker worker2) {
		float grade1 =  worker1.moneyPerHour();
		float grade2 =  worker2.moneyPerHour();
		if (grade1 < grade2)
			return -1;
		if (grade1 > grade2)
			return 1;
		return 0;
	}
}