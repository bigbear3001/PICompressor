import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.perhab.compression.PICompressorOutputStream;
import com.perhab.compression.PIDecompressorInputStream;


@Slf4j
public class CompressionTest {
	
	public static final String UNCOMPRESSED_STRING = "This could be a normal text that can be compressed very well, i guess. "
			+ "That is if it's long enough to make a difference since the dictionary must eventually encoded as well. "
			+ "As this text is in english it really should work very well. The longer this paragraph gets the better it can be compressed. "
			+ "At least that is the theory but can the english language really challenge modern compression algorithms? "
			+ "Quick find a few more words so we can really demonstrate how those things work.";
	
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
	
	@Test
	public void testPI() throws IOException {
		testCompression("PI", PICompressorOutputStream.class, PIDecompressorInputStream.class);
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
