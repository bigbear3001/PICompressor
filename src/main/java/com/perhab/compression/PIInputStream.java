package com.perhab.compression;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class PIInputStream extends InputStream {
	
	private static final MathContext MC = new MathContext(256, RoundingMode.HALF_UP);

	/** 1024 */
	private static final BigDecimal BD1024 = BigDecimal.valueOf(1024);
	
	/** 712 */
	private static final BigDecimal BD712 = BigDecimal.valueOf(712);
	
	/** 512 */
	private static final BigDecimal BD512 = BigDecimal.valueOf(512);
	
	/** 206 */
	private static final BigDecimal BD206 = BigDecimal.valueOf(206);
	
	/** 120 */
	private static final BigDecimal BD120 = BigDecimal.valueOf(120);
	
	/** 89 */
	private static final BigDecimal BD89 = BigDecimal.valueOf(89);

	/** 21 */
	private static final BigDecimal BD21 = BigDecimal.valueOf(21);
	
	/** 16 */
	private static final BigDecimal BD16 = BigDecimal.valueOf(16);
	
	BigDecimal x = BigDecimal.ZERO;
	
	BigDecimal n = BigDecimal.ONE;
	
	@Override
	public int read() throws IOException {
		BigDecimal part120n2 = BD120.multiply(n.pow(2)).subtract(BD89.multiply(n)).add(BD16);
		BigDecimal part512n4 = BD512.multiply(n.pow(4)).subtract(BD1024.multiply(n.pow(3))).add(BD712.multiply(n.pow(2))).subtract(BD206.multiply(n)).add(BD21);
		BigDecimal p = part120n2.divide(part512n4, MC);
		x = BD16.multiply(x).add(p).remainder(BigDecimal.ONE);
		n = n.add(BigDecimal.ONE);
		return BD16.multiply(x).intValue();
	}

}
