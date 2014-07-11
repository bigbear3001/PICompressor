package com.perhab.pi.encoding;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import lombok.Cleanup;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class TestPIEncoding {
	@Test
	public void testFindingDigits() throws IOException {
		byte[] message = get256Bytes();
		byte[] encoded = encode(message);
		byte[] decoded = decode(encoded);
		
		assertArrayEquals(message, decoded);
	}
	
	@Test
	public void testIncompleteData() throws IOException {
		byte[] message = new byte[]{(byte) 255};
		byte[] encoded = encode(message);
		try {
			byte[] decoded = decode(Arrays.copyOfRange(encoded, 0, 3));
		} catch (IOException e) {
			if(!e.getMessage().contains("Incomplete Integer Sequence")) {
				throw e;
			}
			return;
		}
		throw new RuntimeException("test was expected to throw exception because of incomplete data.");
	}

	private byte[] decode(byte[] encodedMessage) throws IOException {
		@Cleanup ByteArrayInputStream inEncoded = new ByteArrayInputStream(encodedMessage);
		@Cleanup PIDecodingStream piDecode = new PIDecodingStream(inEncoded);
		@Cleanup ByteArrayOutputStream outDecoded = new ByteArrayOutputStream();
		IOUtils.copy(piDecode, outDecoded);
		byte[] decoded = outDecoded.toByteArray();
		return decoded;
	}

	private byte[] encode(byte[] message) throws IOException {
		@Cleanup ByteArrayOutputStream outEncoded = new ByteArrayOutputStream();
		@Cleanup PIEncodingStream piEncode = new PIEncodingStream(outEncoded);
		piEncode.write(message);
		byte[] encoded = outEncoded.toByteArray();
		return encoded;
	}

	private byte[] get256Bytes() {
		byte[] bytes = new byte[256];
		for(int i = 0; i < 256; i++) {
			bytes[i] = (byte) i;
		}
		return bytes;
	}
}
