package com.perhab.compression;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BBP {
	private static double Sigma(long j, long n) {
		// Left sum
		double sum1 = 0.0;
		for (long k = 0; k <= n; k++) {
			long r = 8 * k + j;
			sum1 = (sum1 + (Math.pow(16, n - k - 1) % r) / r) % 1.0;
		}

		// Right sum
		double t = 0.0;
		long k = n + 1;
		while(true) {
			double nextT = t + Math.pow(16, n - k) / (8 * k + j);
			// Iterate until t no longer changes
			if (t == nextT) {
				break;
			} else {
				t = nextT;
			}
			k += 1;
		}
		return sum1 + t;
	}
	
	private static final double HEXADEZIMAL_PLACES_TO_GET_AT_ONCE = 14;
	
	public static String pi(long n) {
		n -= 1;
		double x = (4 * Sigma(1, n) - 2 * Sigma(4, n) - Sigma(5, n) - Sigma(6, n)) % 1.0;
		// return "%014x" % int(x * 16**14)
		long place = ((long) (x * (long) Math.pow(16, HEXADEZIMAL_PLACES_TO_GET_AT_ONCE)));
		return String.format("%014x", place);
	}
}
