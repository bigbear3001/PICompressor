package com.perhab.streams;

import java.io.IOException;
import java.io.InputStream;

import lombok.Delegate;

public class SearchableInputStream extends PositionCountingInputStream {
	
	private byte[] buffer;
	
	private int bufferpos;
	
	public SearchableInputStream(InputStream is) {
		super(MarkableInputStream.ensureMarkable(is));
	}

	/**
	 * Search the bytes defined in needle in the input stream.
	 * @param needle
	 * @return
	 * @throws IOException
	 */
	public int search(byte[] needle) throws IOException {
		int read = 0;
		int needlepos = 0;
		buffer = new byte[needle.length];
		while (read != -1) {
			int roundmatch = 0;
			mark(buffer.length);
			read = read(buffer);
			for (int i = 0; i < read; i++) {
				if (buffer[i] == needle[needlepos]) {
					needlepos++;
					roundmatch++;
					if (needlepos >= needle.length) {
						if (roundmatch != read) {
							reset();
							skip(roundmatch);
							return getPos();
						} else {
							return getPos();
						}
					}
				} else if (needlepos != 0) {
					needlepos = 0;
				}
			}
		}
		throw new IOException("End of wrapped input stream reached");
	}
}
