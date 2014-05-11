import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;


@Slf4j
public class CompressionTest {
	
	public static final String UNCOMPRESSED_STRING = "This could be a normal text that can be compressed very well i guess. That is if it's long enough to make a difference since the dictionary must eventually transfered as well.";
	
	@Test
	public void testGzip() throws IOException {
		testCompression("GZIP", GZIPOutputStream.class, GZIPInputStream.class);
	}
	
	@Test
	public void testDeflate() throws IOException {
		testCompression("Deflate", DeflaterOutputStream.class, InflaterInputStream.class);
	}
	
	@Test
	public void testBZip2() throws IOException {
		testCompression("BZip2", BZip2CompressorOutputStream.class, BZip2CompressorInputStream.class);
	}
	
	
	private void testCompression(String name, Class<? extends OutputStream> outClass, Class<? extends InputStream> inClass) throws IOException {
		ByteArrayOutputStream compressedOs = new ByteArrayOutputStream();
		OutputStream os = getInstance(outClass, compressedOs);
		IOUtils.write(UNCOMPRESSED_STRING, os);
		os.close();
		compressedOs.close();
		byte[] compressedBytes = compressedOs.toByteArray();
		
		analyse(name, compressedBytes, UNCOMPRESSED_STRING.getBytes());
		
		ByteArrayInputStream is = new ByteArrayInputStream(compressedBytes);
		InputStream uncompressedIs = getInstance(inClass, is);
		String uncompressed = IOUtils.toString(uncompressedIs);
		assertEquals(UNCOMPRESSED_STRING, uncompressed);
	}

	private <T> T getInstance(Class<T> inClass,
			InputStream is) {
		return getInstance(inClass, is, InputStream.class);
	}

	private <T> T getInstance(Class<T> outClass,
			OutputStream os) {
		return getInstance(outClass, os, OutputStream.class);
	}
	
	private <T, R> T getInstance(Class<T> clazz,
			R constructorParameter, Class<R> constructorParameterClass) {
		try {
			Constructor<T> constructor = clazz.getConstructor(constructorParameterClass);
			return constructor.newInstance(constructorParameter);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private void analyse(String name, byte[] compressed, byte[] uncompressed) {
		double compressionRate =  1.0 - (compressed.length * 1.0 / uncompressed.length);
		log.info("Compression Rate for {}: {}", name, compressionRate);
		assertTrue("Compressed String was longer than uncompressed string.", compressed.length < uncompressed.length);
	}
}
