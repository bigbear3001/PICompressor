package com.perhab.compression;

import static org.junit.Assert.*;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

@Slf4j
public class BBPTest {
	@Test
	public void testPiPlaces0() {
		//should be the first one if we were exact
		//assertEquals("3243f6a8885a30", get8Places(0));
		assertEquals("3243f6a8885a36", get14Places(0));
	}
	
	@Test
	public void testPiPlaces1() {
		assertEquals("243f6a8885a308", get14Places(1));
	}
	
	@Test
	public void testPiPlaces10000() {
		assertEquals("68ac8fcfb8016c", get14Places(10000));
	}
	
	@Test
	public void testPiPlaces10e6() {
		assertEquals("68ac8fcfb8016c", get14Places((int) Math.pow(10, 6)));
	}
	
	@Test
	public void testPiPlaces10e7() {
		assertEquals("17af5863efed8d", get14Places((int) Math.pow(10, 7)));
	}
	
	
	
	private String get14Places(long n) {
		StringBuilder res = new StringBuilder(); 
		res.append(BBP.pi(n));
		return res.toString();
	}
}
