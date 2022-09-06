package com.api.jsonata4java.test.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.api.jsonata4java.expressions.RegularExpression;

public class RegularExpressionTest {

	@Test
	public void testToString() {
		assertEquals(".*c.*f.*", new RegularExpression("c.*f").toString());
		assertEquals("^.*$", new RegularExpression("^.*$").toString());
	}

	@Test
	public void matches() {
		assertTrue(new RegularExpression("^.*$").matches("asdfgh"));
		assertTrue(new RegularExpression("^ab.*ef$").matches("abcdef"));
		assertTrue(new RegularExpression("c.*f").matches("abcdefgh"));
	}

	@Test
	public void matchesCaseInsensitive() {
		assertFalse(new RegularExpression("^ab.*ef$").matches("ABCdef"));		
		assertTrue(new RegularExpression(RegularExpression.Type.CASEINSENSITIVE, "^ab.*ef$").matches("ABCdef"));		
	}
}
