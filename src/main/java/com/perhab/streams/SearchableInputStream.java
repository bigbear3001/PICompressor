package com.perhab.streams;

import java.io.IOException;
import java.io.InputStream;

/**
 * Input stream that allows for searching bytes within the stream.
 * @author bigbear3001
 */
public class SearchableInputStream extends PositionCountingInputStream {

	/**
	 * Initialize a new searchable input stream that gets its data from the given input stream.
	 * @param is - input stream to wrap and search for data.
	 */
	public SearchableInputStream(InputStream is) {
		super(MarkableInputStream.ensureMarkable(is));
	}

	/**
	 * Search the bytes defined in needle in the input stream. if the wrapped input stream has mark supported it is
	 * ensured that the wrapped input stream is at the correct position (after the needle) when finished searching.
	 * @param needle - bytes to search
	 * @return position where the needle bytes where found (needle end position)
	 * @throws IOException - in case the bytes couldn't be found before the end of the stream
	 */
	public int search(byte[] needle) throws IOException {
		int read = 0;
		int needlepos = 0;
		byte[] buffer = new byte[needle.length];
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
						}
						return getPos();
					}
				} else {
					needlepos = 0;
				}
			}
		}
		throw new IOException("End of wrapped input stream reached");
	}
}
