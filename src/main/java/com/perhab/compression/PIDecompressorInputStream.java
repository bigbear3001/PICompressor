package com.perhab.compression;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

import lombok.Delegate;

import com.perhab.pi.encoding.PIDecodingStream;

public class PIDecompressorInputStream extends InputStream {

	@Delegate
	private final InputStream decompressed;
	
	public PIDecompressorInputStream(InputStream in) throws IOException {
		decompressed = new PIDecodingStream(new GZIPInputStream(in));
	}
	
}
