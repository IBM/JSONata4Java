package com.api.jsonata4java.test.expressions;

import static com.api.jsonata4java.text.expressions.utils.Utils.test;

import org.junit.Test;

public class ContainsFunctionErrorTest {

	@Test
	public void runTest() throws Exception {
		test("$contains(\"abracadabra\", /^abracadabra$/)", "true", null, null);
	}
}
