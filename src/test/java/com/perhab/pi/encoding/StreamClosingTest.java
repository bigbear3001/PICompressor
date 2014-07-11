package com.perhab.pi.encoding;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Test;

public class StreamClosingTest {
	@Test
	public void testClosingEmbeddedOutputStream() throws IOException {
		OutputStream stream = createStream(OutputStream.class);
		
		PIEncodingStream es = new PIEncodingStream(stream);
		es.close();
		
		EasyMock.verify(stream);
	}
	
	@Test
	public void testClosingEmbeddedInputStream() throws IOException {
		InputStream stream = createStream(InputStream.class);
		
		PIDecodingStream es = new PIDecodingStream(stream);
		es.close();
		
		EasyMock.verify(stream);
	}

	private <T extends Closeable> T createStream(Class<T> clazz) throws IOException {
		T stream = EasyMock.createMock(clazz);
		stream.close();
		EasyMock.expectLastCall().andAnswer(new IAnswer<Void>() {
			@Override
			public Void answer() throws Throwable {
				return null;
			}
		});
		EasyMock.replay(stream);
		return stream;
	}
}
