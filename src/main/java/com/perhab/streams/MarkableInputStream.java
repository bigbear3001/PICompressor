package com.perhab.streams;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

public class MarkableInputStream extends InputStream {

	private final PositionCountingInputStream in;
	
	private List<BufferBlock> bufferBlocks;
	
	private int bufferpos = 0;
	
	private int stillToReadFromBuffer = 0; 

	private int mark;

	private int blockSize;
	
	public MarkableInputStream(InputStream is) {
		in = PositionCountingInputStream.wrap(is);
	}
	
	@Override
	public boolean markSupported() {
		return true;
	}
	
	@Override
	public synchronized void mark(int readlimit) {
		bufferpos = 0;
		bufferBlocks = new ArrayList<BufferBlock>(1);
		bufferBlocks.add(new BufferBlock(new byte[readlimit]));
		blockSize = readlimit;
		mark = in.getPos();
	}
	
	
	@Override
	public int read() throws IOException {
		if (bufferBlocks == null) {
			return in.read();
		} else {
			if (stillToReadFromBuffer > 0){
				stillToReadFromBuffer--;
				return readFromBuffer();
			} else {
				return writeToBuffer(in.read());
			}
		}
	}
	
	private int writeToBuffer(int read) {
		int block = bufferpos / blockSize;
		int pos = bufferpos++ % blockSize;
		if (bufferBlocks.size() <= block) {
			bufferBlocks.add(new BufferBlock(new byte[blockSize]));
		}
		bufferBlocks.get(block).data[pos] = (byte) read;
		return read;
	}

	private int readFromBuffer() {
		int block = bufferpos / blockSize;
		int pos = bufferpos++ % blockSize;
		return bufferBlocks.get(block).data[pos];
	}

	@Override
	public synchronized void reset() throws IOException {
		if (bufferBlocks == null) {
			throw new IOException("InputStream has no marked position yet.");
		}
		stillToReadFromBuffer = (in.getPos() - mark);
		bufferpos = 0;
	}

	public static InputStream ensureMarkable(InputStream is) {
		if (is.markSupported()) {
			return is;
		} else {
			return new MarkableInputStream(is);
		}
	}
	
	@AllArgsConstructor
	private class BufferBlock {
		private final byte[] data;
	}
	
	
	

}
