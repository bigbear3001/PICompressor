package com.perhab.pi.encoding;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import com.perhab.compression.PIInputStream;
import com.perhab.math.BBP;

@Slf4j
@AllArgsConstructor
public class PIEncodingStream extends OutputStream {

	final OutputStream out;
	
	/**
	 * Mask to be able to ignore the higher 24 bits of an int. 
	 */
	private static final int MASK = Integer.MAX_VALUE >> (24 - 1);
	
	private static final HashMap<Integer, ByteArray> cache = new HashMap<Integer, ByteArray>(); 
	
	@Override
	public void write(int b) throws IOException {
		b &= MASK;
		byte[] placeBytes;
		if (cache.containsKey(b)) {
			placeBytes = cache.get(b).getBytes();
		} else {
			placeBytes =  findPlace(b);
			log.trace("Found {} at {}", b, PIEncodingUtils.readFullInt(placeBytes));
		}
		out.write(placeBytes);
	}

	private byte[] findPlace(int b) throws IOException {
		@Cleanup PIInputStream pi = new PIInputStream(BBP.Mode.TWO_DIGITS);
		int code = -1;
		int place = -1;
		while (code != b) {
			place++;
			code = pi.read();
			if (!cache.containsKey(code)) {
				cache.put(code, new ByteArray(PIEncodingUtils.toByteArray(place)));
			}
		}
		return PIEncodingUtils.toByteArray(place);
	}


}
