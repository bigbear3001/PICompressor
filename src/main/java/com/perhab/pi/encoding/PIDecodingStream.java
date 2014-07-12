package com.perhab.pi.encoding;

import java.io.IOException;
import java.io.InputStream;

import com.perhab.math.BBP;
import com.perhab.math.BBP.Mode;

public class PIDecodingStream extends InputStream {

	private final InputStream in;
	
	public PIDecodingStream(InputStream read) {
		in = read;
	}

	@Override
	public int read() throws IOException {
		int place = PIEncodingUtils.readFullInt(in);
		if (place == -1) {
			return place;
		}
		Mode mode = BBP.Mode.TWO_DIGITS;
		return BBP.getPlace(place * mode.getReturnDigits(), mode);
	}
	
	@Override
	public void close() throws IOException {
		in.close();
		super.close();
	}
	
}
