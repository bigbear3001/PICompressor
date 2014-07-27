package com.perhab.compression;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.perhab.streams.SearchableInputStream;

import lombok.Cleanup;

public class TestSearchableInputStream {
	
	private final byte[] HAYSTACK = new byte[]{00,00,00,33,66,99,00,66,99,33,99,66,33,66};
	
	@Test
	public void testSearchOneByte() throws IOException {
		@Cleanup SearchableInputStream searchable = initSearchable();
		
		assertEquals(1, searchable.search(new byte[]{00}));
		assertEquals(4, searchable.search(new byte[]{33}));
		assertEquals(6, searchable.search(new byte[]{99}));
		assertEquals(7, searchable.search(new byte[]{00}));
	}
	
	@Test
	public void testSearchMultiByte() throws IOException {
		@Cleanup SearchableInputStream searchable = initSearchable();
		
		assertEquals(4, searchable.search(new byte[]{00, 33}));
		assertEquals(6, searchable.search(new byte[]{66, 99}));
		assertEquals(9, searchable.search(new byte[]{66, 99}));
		assertEquals(11, searchable.search(new byte[]{33, 99}));
	}
	
	
	@Test(expected=IOException.class)
	public void testNotFound() throws IOException {
		@Cleanup SearchableInputStream searchable = initSearchable();
		
		searchable.search(new byte[]{44});
	}


	private SearchableInputStream initSearchable() {
		InputStream in = new ByteArrayInputStream(HAYSTACK);
		SearchableInputStream searchable = new SearchableInputStream(in);
		return searchable;
	}
}
