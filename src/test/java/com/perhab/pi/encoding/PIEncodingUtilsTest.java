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
}
