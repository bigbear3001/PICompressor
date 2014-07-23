package com.perhab.streams;

import java.io.IOException;
import java.io.InputStream;

import lombok.Getter;

public class PositionCountingInputStream extends InputStream {
	
	private final InputStream in;
	
	@Getter
	private int pos = 0;
	
	public PositionCountingInputStream(InputStream is) {
		in = is;
	}

	@Override
	public int read() throws IOException {
		try {
			return in.read();
		} finally {
			pos++;
		}
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int read = in.read(b, off, len);
		pos+=read;
		return read;
	}
	
	@Override
	public void close() throws IOException {
		in.close();
		super.close();
	}

	public static PositionCountingInputStream wrap(InputStream is) {
		if (is instanceof PositionCountingInputStream) {
			return (PositionCountingInputStream) is;
		} else {
			return new PositionCountingInputStream(is);
		}
	}
	
}
