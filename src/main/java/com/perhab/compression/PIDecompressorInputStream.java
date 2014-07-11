package com.perhab.compression;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import com.perhab.pi.encoding.PIDecodingStream;

public class PIDecompressorInputStream extends InputStream {

	private final InputStream decompressed;
	
	public PIDecompressorInputStream(InputStream in) throws IOException {
		decompressed = new PIDecodingStream(new GzipCompressorInputStream(in));
	}
	
	@Override
	public int read() throws IOException {
		return decompressed.read();
	}
	
}
