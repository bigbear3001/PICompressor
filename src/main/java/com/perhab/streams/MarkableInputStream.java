package com.perhab.streams;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.perhab.pi.encoding.PIEncodingUtils;

import lombok.AllArgsConstructor;
import lombok.ToString;

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
		byte[] b = new byte[1];
		int read = read(b, 0, 1);
		if (read == -1) {
			return read;
		}
		return b[0] & PIEncodingUtils.MASK;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (bufferBlocks == null) {
			return in.read(b, off, len);
		} else {
			int read = 0;
			if (stillToReadFromBuffer > 0) {
				read = readFromBuffer(b, off, Math.min(len, stillToReadFromBuffer));
				stillToReadFromBuffer -= read;
			}
			if (read < len) {
				off += read;
				read += writeToBuffer(b, off, in.read(b, off, len - read));
			}
			return read;
		}
	}
	
	private int writeToBuffer(final byte[] b, final int off, final int len) {
		final int startBlock = bufferpos / blockSize;
		int startBlockPos = bufferpos % blockSize;
		int pos = off;
		int remaining = len;
		for (int block = startBlock; pos < b.length && remaining > 0; block++) {
			if (bufferBlocks.size() <= block) {
				bufferBlocks.add(new BufferBlock(new byte[blockSize]));
				startBlockPos = 0;
			}
			for (int blockPos = startBlockPos; blockPos < blockSize && pos < b.length && remaining > 0; blockPos++) {
				bufferBlocks.get(block).data[blockPos] = b[pos++];
				remaining--;
			}
		}
		bufferpos += len;
		return len;
	}

	private int readFromBuffer(byte[] b, int off, int len) {
		final int startBlock = bufferpos / blockSize;
		final int startBlockPos = bufferpos % blockSize;
		int pos = off;
		int remaining = len;
		for (int block = startBlock; pos < b.length && remaining > 0; block++) {
			for (int blockPos = startBlockPos; blockPos < blockSize && pos < b.length && remaining > 0; blockPos++) {
				b[pos++] = bufferBlocks.get(block).data[blockPos];
				remaining--;
			}
		}
		bufferpos += (len - remaining);
		return len;
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
	@ToString
	private class BufferBlock {
		private final byte[] data;
	}
	
	
	

}
