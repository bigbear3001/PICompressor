package com.perhab.pi.encoding;

import java.io.IOException;
import java.io.InputStream;

public class PIEncodingUtils {
	
	private static final int INT_SIZE_IN_BYTES = Integer.SIZE / Byte.SIZE;
	
	protected static byte[] toByteArray(int place) {
		
		byte[] bytes = new byte[INT_SIZE_IN_BYTES];
		for (int i = INT_SIZE_IN_BYTES; i-- > 0;) {
			bytes[i] = (byte) place;
			place >>= Byte.SIZE;
		}
		return bytes;
	}
	
	protected static int readFullInt(byte[] bytes) {
		assert bytes.length == INT_SIZE_IN_BYTES;
		int full = 0;
		for (int i = 0; i < INT_SIZE_IN_BYTES; i++) {
			full <<= Byte.SIZE;
			full += bytes[i];
		}
		return full;
	}
	
	protected static int readFullInt(InputStream in) throws IOException {
		int full = 0;
		for (int i = 0; i < INT_SIZE_IN_BYTES; i++) {
			full <<= Byte.SIZE;
			full += in.read();
		}
		if (full < 0) {
			return -1;
		}
		return full;
	}
}
