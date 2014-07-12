package com.perhab.pi.encoding;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class PIEncodingUtilsTest {
	@Test
	public void testIntByteArrayConversion() throws IOException {
		byte[] bytes = PIEncodingUtils.toByteArray(31517);
		assertEquals(31517, PIEncodingUtils.readFullInt(bytes));
		
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		assertEquals(31517, PIEncodingUtils.readFullInt(in));
	}
	
	@Test
	public void testDiff() {
		byte[] first = new byte[]{0, -127, 3, 127};
		byte[] second = new byte[]{4, 22, -127, 127};
		
		byte[] diff = PIEncodingUtils.diff(first, second);
		
		byte[] recovered = PIEncodingUtils.diff(first, diff);
		
		assertArrayEquals(second, recovered);
	}
}
