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
Library (THDL). Portions created by the THDL are Copyright 2002-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.util;

import junit.framework.TestCase;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.util.Trie} at the unit level.
 */
public class TrieTest extends TestCase {

	/**
	 * Constructor for TrieTest.
	 * @param arg0
	 */
	public TrieTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TrieTest.class);
	}

	/**
	 * Tests that the trie distinguishes between uppercase and
	 * lowercase.  */
	public void testCaseSupport() {
        Trie t = new Trie();
        t.put("S", "S value");
        assertTrue(t.get("S").equals("S value"));
        assertTrue(null == t.get("s"));
        t.put("Sh", "Sh value");
        assertTrue(t.get("Sh").equals("Sh value"));
        assertTrue(t.get("S").equals("S value"));
        assertTrue(null == t.get("s"));
        assertTrue(null == t.get("sh"));
        assertTrue(null == t.get("SH"));
        assertTrue(null == t.get("sH"));
    }

	/**
	 * Test for put(String, Object)
	 */
	public void testReplacement() {
        Trie t = new Trie();
        t.put("S", "S value 1");
        assertTrue(t.get("S").equals("S value 1"));
        t.put("S", "S value 2");
        assertTrue(t.get("S").equals("S value 2"));
    }

	/**
	 * Tests that put(*, null) throws a NullPointerException.
	 */
	public void testPuttingNull() {
        Trie t = new Trie();
        boolean threw = false;
        try {
            t.put("heya", null);
        } catch (NullPointerException e) {
            threw = true;
        }
        assertTrue(threw);
    }

	/**
	 * Test for put(String, Object)
	 */
	public void testPrefix() {
        Trie t = new Trie();
        t.put("t", "t value");
        t.put("ts", "ts value");
        t.put("tsh", "tsh value");
        assertTrue(t.get("t").equals("t value"));
        assertTrue(t.get("ts").equals("ts value"));
        assertTrue(t.get("tsh").equals("tsh value"));
        assertTrue(t.hasPrefix("t"));
        assertTrue(t.hasPrefix("ts"));
        assertTrue(!t.hasPrefix("tsh"));
	}
}
