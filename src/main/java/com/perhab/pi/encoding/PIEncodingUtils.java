package com.perhab.pi.encoding;

import java.io.IOException;
import java.io.InputStream;

public class PIEncodingUtils {
	
	private static final int INT_SIZE_IN_BYTES = Integer.SIZE / Byte.SIZE;
	
	/**
	 * Mask to be able to ignore the higher 24 bits of an int. 
	 */
	public static final int MASK = Integer.MAX_VALUE >> (24 - 1);
	
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
			int next = in.read();
			if (next == -1) {
				if (i == 0) {
					return next;
				} else {
					throw new IOException("Incomplete Integer Sequence at the end of the file");
				}
			}
			full <<= Byte.SIZE;
			full += next;
		}
		if (full < 0) {
			return -1;
		}
		return full;
	}

	public static byte[] diff(byte[] base, byte[] changed) {
		assert base.length == changed.length;
		byte[] diff = new byte[base.length];
		for(int i = 0; i < base.length; i++) {
			diff[i] = (byte) (changed[i] ^ base[i]);
		}
		return diff;
	}
}
