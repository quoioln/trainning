package util;

import java.util.Date;

/**
 * @author vpquoi
 *
 */
public class DateUtil {
	public static String toString(Date date) {
		StringBuilder builder = new StringBuilder();
		builder.append(date.getDate());
		builder.append("-");
		builder.append(date.getMonth());
		builder.append("-");
		builder.append(date.getYear());
		return builder.toString();
	}
}
