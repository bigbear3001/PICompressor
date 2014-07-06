package com.perhab.compression;

import java.io.IOException;
import java.io.OutputStream;

import lombok.Cleanup;

import com.perhab.math.BBP;
import com.perhab.pi.PIInputStream;

public class PICompressorOutputStream extends OutputStream {

	private OutputStream compressed;
	
	public PICompressorOutputStream(OutputStream out) {
		compressed = out;
	}
	
	private final int blocksize = 2;
	
	private final byte[] block = new byte[blocksize];
	
	private int blockpos = 0;
	
	private int pos = 0;
	
	@Override
	public void write(int arg0) throws IOException {
		block[blockpos++] = (byte) arg0;
		if (blockpos >= blocksize) {
			if(pos == 0) {
				compressed.write(blocksize);
			}
			byte[] positionInformation = findBlock(block);
			compressed.write(positionInformation);
			blockpos = 0;
			pos++;
		}
	}

	private byte[] findBlock(byte[] block) throws IOException {
		@Cleanup
		PIInputStream pi = new PIInputStream(BBP.Mode.TWO_DIGITS);
		byte[] buffer = new byte[blocksize];
		int searchpos = 0;
		int bufferpos = 0;
		int blockpos = 0;
		while (true) {
			int next = pi.read();
			if (next == block[blockpos]) {
				buffer[bufferpos++] = (byte) next;
				if (bufferpos >= blocksize) {
					return intToByteArray(searchpos);
				}
				blockpos++;
			} else {
				if(bufferpos != 0) {
					searchpos += bufferpos;
					blockpos = 0;
					bufferpos = 0;
				}
				searchpos++;
			}
		}
	}

	private byte[] intToByteArray(int intValue) {
		byte[] result = new byte[Integer.SIZE / 8];
		for (int i = 0; i < Integer.SIZE / 8; i++) {
			result[i] = (byte) (intValue % 256);
			intValue = intValue / 256;
		}
		return result;
	}
	
}
