package com.perhab.math;

/**
 * Some math functions
 * @author bigbear3001
 *
 */
public class MathUtils {
	/**
	 * base ^ e % m
	 * according to http://en.wikipedia.org/wiki/Modular_exponentiation#Right-to-left_binary_method
	 * @param base 
	 * @param e
	 * @param m
	 * @return
	 */
	public static double modPow(long base, long e, long m) {
		assert (m - 1) * (base % m) < base;
		long result = 1;
		base = base % m;
		while (e > 0) {
			if (e % 2 == 1) {
				result = (result * base) % m;
			}
			e = e >> 1;
			base = (base * base) % m;
		}
		return result; 
	}
}
