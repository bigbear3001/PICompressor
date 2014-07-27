package com.perhab.streams;

import java.io.IOException;
import java.io.InputStream;

import lombok.Delegate;
import lombok.Getter;

public class PositionCountingInputStream extends InputStream {
	
	private final InputStream in;
	
	@Getter
	private int pos = 0;
	
	private int markPos = 0;
	
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
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int read = in.read(b, off, len);
		pos+=read;
		return read;
	}
	
	@Override
	public synchronized void mark(int readlimit) {
		markPos = pos;
		in.mark(readlimit);
	}
	
	@Override
	public synchronized void reset() throws IOException {
		in.reset();
		pos = markPos;
	}
	
	@Override
	public boolean markSupported() {
		return in.markSupported();
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
