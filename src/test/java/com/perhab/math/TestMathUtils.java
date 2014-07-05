package com.perhab.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestMathUtils {
	@Test
	public void testModPow() {
		assertEquals("10 ^ 100 % 10 = 0", 0.0, MathUtils.modPow(10, 100, 10), 0.0);
		assertEquals("15 ^ 231 % 17 = 8.0", 8.0, MathUtils.modPow(15, 231, 17), 0.0);
	}
}
