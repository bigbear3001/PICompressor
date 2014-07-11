package com.perhab.compression;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import lombok.Delegate;

import com.perhab.pi.encoding.PIEncodingStream;

public class PICompressorOutputStream extends OutputStream {

	@Delegate
	private OutputStream compressed;
	
	public PICompressorOutputStream(OutputStream out) throws IOException {
		compressed = new PIEncodingStream(new GZIPOutputStream(out));
	}
	

}
