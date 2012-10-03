/*
The contents of this file are subject to the THDL Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the THDL web site 
(http://www.thdl.org/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is the Tibetan and Himalayan Digital
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.util;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author David Chandler
 *
 * Tests the ThdlLazyException class.
 */
public class ThdlLazyExceptionTest extends TestCase {
	
	/**
	 * A helper class that has methods that throw both
	 * checked and unchecked exceptions.
	 */
	private class Helper {
		Helper() { }
		void throwChecked() throws IOException {
			throw new java.io.IOException("sure");
		}
		void saysItThrowsNothing() {
			try {
				throwChecked();
			} catch (IOException e) {
				throw new ThdlLazyException(e);
			}
		}
	}
	
	static Helper helper;
	protected void setUp() throws Exception {
		helper = new Helper();
	}
	protected void tearDown() throws Exception {
		helper = null;
	}
	
	/**
	 * Constructor for ThdlLazyExceptionTest.
	 * @param arg0
	 */
	public ThdlLazyExceptionTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ThdlLazyExceptionTest.class);
	}

	/**
	 * Test for void ThdlLazyException()
	 */
	public void testThdlLazyException() {
		Helper x = new Helper();
		try {
			x.saysItThrowsNothing();
			fail("No exception was thrown.");
		} catch (ThdlLazyException jle) {
			/* good. */
			assertTrue(jle.getRealException() instanceof IOException);
		} /* don't catch anything else. */
	}

	/**
	 * Test for void ThdlLazyException(String)
	 */
	public void testThdlLazyExceptionString() {
		String msg = "foo";
		ThdlLazyException e = new ThdlLazyException(msg);
		assertTrue("Oops: " + e.getMessage(),
                           "ThdlLazyException [foo] wrapping nothing".equals(e.getMessage()));
		assertTrue(null == e.getRealException());
	}

	/**
	 * Test for void ThdlLazyException(String, Throwable)
	 */
	public void testThdlLazyExceptionStringThrowable() {
		String msg = "foo";
		IOException ioe = new IOException("bah");
		ThdlLazyException e = new ThdlLazyException(msg, ioe);
		assertTrue("oops: " + e.getMessage(),
                           "ThdlLazyException [foo] wrapping bah".equals(e.getMessage()));
		assertTrue(ioe.equals(e.getRealException()));
		assertTrue("bah".equals(e.getRealException().getMessage()));
	}

	/**
	 * Test for void ThdlLazyException(Throwable)
	 */
	public void testThdlLazyExceptionThrowable() {
		IOException ioe = new IOException("bah");
		ThdlLazyException e = new ThdlLazyException(ioe);
		assertTrue(ioe.equals(e.getRealException()));
		assertTrue("bah".equals(e.getRealException().getMessage()));
	}

	public void testGetRealException() {
		testThdlLazyExceptionThrowable();
	}

}
