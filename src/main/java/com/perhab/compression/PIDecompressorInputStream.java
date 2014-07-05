package com.perhab.compression;

import java.io.IOException;
import java.io.InputStream;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PIDecompressorInputStream extends InputStream {

	private final InputStream compressed;
	
	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
