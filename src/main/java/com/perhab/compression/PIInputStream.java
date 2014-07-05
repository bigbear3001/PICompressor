package com.perhab.compression;

import java.io.IOException;
import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PIInputStream extends InputStream {
	
	@AllArgsConstructor
	public static enum Mode {
		SINGLE_DIGIT(16, 1),
		TWO_DIGITS(256, 2);
		
		private final int multiplicator;
		
		private final int step;

	}
	
	public PIInputStream() {
		this(Mode.SINGLE_DIGIT);
	}
	
	public PIInputStream(Mode outputMode) {
		mode = outputMode;
	}
	
	private final Mode mode;

	int nextN = 0;
	
	@Override
	public int read() throws IOException {
		log.trace("Calculate place {}", nextN);
		double result = (4 * sum(1, nextN)) - (2 * sum(4, nextN)) - sum(5, nextN) - sum(6, nextN);
		while (result < 0) {
			result += 1;
		}
		nextN += mode.step;
		return (int) ((result % 1) * mode.multiplicator);
	}

	private double sum(int addition, int n) {
		double sum = 0.0;
		for (int k = 0; k <= n; k++) {
			int denominator = 8*k + addition;
			sum += modPow(16, n - k, denominator) / denominator;
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
	 * base ^ e % m
	 * according to http://en.wikipedia.org/wiki/Modular_exponentiation#Right-to-left_binary_method
	 * @param base 
	 * @param e
	 * @param m
	 * @return
	 */
	private double modPow(long base, long e, long m) {
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
