package com.perhab.compression;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.Cleanup;

import org.junit.Test;

import com.perhab.pi.PIInputStream;
import com.perhab.streams.MarkableInputStream;

public class TestMarkableInputStream {
	
	private static final byte[] DATA = new byte[]{0,1,2,3,4,5,6,7,8,9,(byte) 129};

	@Test
	public void testWrapping() throws IOException {
		@Cleanup InputStream dataStream = new ByteArrayInputStream(DATA);
		assertSame("ByteArrayInputStream itself is markable so it shouldn't be wrapped.", dataStream, MarkableInputStream.ensureMarkable(dataStream));
		
		@Cleanup InputStream piStream = new PIInputStream();
		assertFalse("PIInputStream shouldn't support marking otherwhise the following assertions may be useless.", piStream.markSupported());
		@Cleanup InputStream markablePiStream = MarkableInputStream.ensureMarkable(piStream);
		assertNotSame("PIInputStream doesn't support marking therefore it should be wrapped.", piStream, markablePiStream);
		assertTrue("After wrapping marking should be supported.", markablePiStream.markSupported());
	}
	
	@Test
	public void testReset() throws IOException {
		@Cleanup InputStream dataStream = new MarkableInputStream(new ByteArrayInputStream(DATA));
		
		byte[] read = new byte[2];
		dataStream.read(read);
		assertArrayEquals(new byte[]{0, 1}, read);
		
		dataStream.mark(2);
		dataStream.read(read);
		assertArrayEquals(new byte[]{2, 3}, read);
		
		dataStream.reset();
		dataStream.read(read);
		assertArrayEquals(new byte[]{2, 3}, read);
		
		dataStream.read(read);
		assertArrayEquals(new byte[]{4, 5}, read);
	}
	
	@Test(expected=IOException.class)
	public void testResetWithoutMark() throws IOException {
		@Cleanup InputStream dataStream = new MarkableInputStream(new ByteArrayInputStream(DATA));
		
		dataStream.reset();
	}
	
	
	@Test
	public void testBufferOverflow() throws IOException {
		@Cleanup InputStream dataStream = new MarkableInputStream(new ByteArrayInputStream(DATA));
		
		byte[] read = new byte[2];
		dataStream.mark(2);
		assertEquals("Didn't skip 3 elements", 3,dataStream.skip(3));
		dataStream.read(read);
		assertArrayEquals(new byte[]{3, 4}, read);
		dataStream.reset();
		dataStream.read(read);
		assertArrayEquals(new byte[]{0, 1}, read);
		
		dataStream.read(read);
		assertArrayEquals(new byte[]{2, 3}, read);
		dataStream.read(read);
		assertArrayEquals(new byte[]{4, 5}, read);
		dataStream.read(read);
		assertArrayEquals(new byte[]{6, 7}, read);
	}
	
	@Test
	public void testMultipleMarks() throws IOException {
		@Cleanup InputStream dataStream = new MarkableInputStream(new ByteArrayInputStream(DATA));
		
		byte[] read = new byte[2];
		dataStream.mark(3);
		assertEquals("Didn't skip 3 elements", 3,dataStream.skip(3));
		dataStream.read(read);
		assertArrayEquals(new byte[]{3, 4}, read);
		
		dataStream.mark(3);
		dataStream.read(read);
		assertArrayEquals(new byte[]{5, 6}, read);
		
		dataStream.reset();
		dataStream.read(read);
		assertArrayEquals(new byte[]{5, 6}, read);

	}
	
	@Test
	public void testSingleByteRead() throws IOException {
		@Cleanup InputStream dataStream = new MarkableInputStream(new ByteArrayInputStream(DATA));
		ByteArrayInputStream comparison = new ByteArrayInputStream(DATA);
		for (int i = 0; i <= DATA.length; i++) {
			assertEquals("Difference for byte " + i, comparison.read(), dataStream.read());
		}
	}
	
}
