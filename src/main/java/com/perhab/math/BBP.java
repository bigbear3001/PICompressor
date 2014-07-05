package com.perhab.math;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Implementation of digit extraction of PI with the BBP Formula
 * @see http://en.wikipedia.org/w/index.php?title=Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula&oldid=609165265
 * @author bigbear3001
 */
public class BBP {
	/**
	 * Mode to use for returning digits of PI in {@link BBP#getPlace(int, Mode)} 
	 * @author bigbear3001
	 */
	@AllArgsConstructor
	public static enum Mode {
		/**
		 * returns one hexadecimal digit per step
		 */
		SINGLE_DIGIT(16, 1),
		/**
		 * returns two hexadecimal digits in one step to be easily handled as a byte
		 */
		TWO_DIGITS(256, 2);
		
		/**
		 * Multiplicator to use when extracting the return value from the calculated value
		 */
		private final int multiplicator;
		
		/**
		 * number of digits to return
		 */
		@Getter
		private final int returnDigits;
	}
	
	/**
	 * @return the sum (16 ^ (n - k)) / (8k + addition) for k = 0 to ∞
	 */
	private static double sum(int addition, int n) {
		double sum = 0.0;
		for (int k = 0; k <= n; k++) {
			int denominator = 8*k + addition;
			sum += MathUtils.modPow(16, n - k, denominator) / denominator;
		}
		
		for (int k = n + 1; true; k++) {
			double nextSum = sum;
			nextSum += Math.pow(16, n - k) / (8*k + addition);
			if (Double.isNaN(nextSum)) {
				break;
			} else if (nextSum != sum) {
				sum = nextSum;
			} else {
				break;
			}
		}
		return sum;
	}

	/**
	 * @param n - place of pi to return
	 * @param mode - defining the mode enables to return more than one hexadecimal digit at once
	 * @return nth digit of pi in hexadecimal system
	 */
	public static int getPlace(int n, Mode mode) {
		double result = (4 * sum(1, n)) - (2 * sum(4, n)) - sum(5, n) - sum(6, n);
		while (result < 0) {
			result += 1;
		}
		return (int) ((result % 1) * mode.multiplicator);
	}
}
