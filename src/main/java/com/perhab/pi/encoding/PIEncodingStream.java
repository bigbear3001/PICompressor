package com.perhab.pi.encoding;

import static com.perhab.pi.encoding.PIEncodingUtils.MASK;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import com.perhab.math.BBP;
import com.perhab.pi.PIInputStream;

@Slf4j
public class PIEncodingStream extends OutputStream {
	
	final OutputStream out;
	
	private static final HashMap<Integer, ByteArray> cache = new HashMap<Integer, ByteArray>();
	
	public PIEncodingStream(OutputStream write) {
		out = write;
	}
	
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
	
	@Override
	public void close() throws IOException {
		out.close();
		super.close();
	}


}
