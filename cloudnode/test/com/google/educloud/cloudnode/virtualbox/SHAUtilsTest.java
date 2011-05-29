package com.google.educloud.cloudnode.virtualbox;

import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SHAUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateHash() throws NoSuchAlgorithmException {
		String generateHash = SHAUtils.generateHash("educloud");
		System.out.println(generateHash);
	}

}
