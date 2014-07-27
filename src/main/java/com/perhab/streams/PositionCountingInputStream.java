package com.perhab.streams;

import java.io.IOException;
import java.io.InputStream;

import lombok.Getter;

/**
 * InputStream that allows to get the current position in the input stream as integer.
 * @author bigbear3001
 */
public class PositionCountingInputStream extends InputStream {

	/**
	 * Encapsulated input stream to get the data from.
	 */
	private final InputStream in;
	
	/**
	 * Current position in the input stream.
	 */
	@Getter
	private int pos = 0;
	
	/**
	 * Position of the mark set for the input stream.
	 */
	private int markPos = 0;
	
	/**
	 * Initialize a new counting input stream that uses the given input stream to get data.
	 * @param is - input stream to get the data from
	 */
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
	
	/**
	 * Wrap the given input stream into a position counting input stream if needed.
	 * @param is - input stream to wrap.
	 * @return position counting input stream
	 */
	public static PositionCountingInputStream wrap(InputStream is) {
		if (is instanceof PositionCountingInputStream) {
			return (PositionCountingInputStream) is;
		} else {
			return new PositionCountingInputStream(is);
		}
	}
	
}
