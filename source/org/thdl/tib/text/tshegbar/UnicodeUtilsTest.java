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

package org.thdl.tib.text.tshegbar;

import junit.framework.TestCase;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.tib.text.tshegbar.UnicodeUtils} at the unit level.
 */
public class UnicodeUtilsTest extends TestCase implements UnicodeConstants {
	/**
	 * Plain vanilla constructor for UnicodeUtilsTest.
	 * @param arg0
	 */
	public UnicodeUtilsTest(String arg0) {
		super(arg0);
	}

    /** Invokes a text UI and runs all this class's tests. */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(UnicodeUtilsTest.class);
	}

    /** Tests Unicode Normalization form KD for Tibetan codepoints.
     *  See Unicode, Inc.'s NormalizationTest-3.2.0.txt.  This
     *  contains all test cases for
     *  <code>U+0F00</code>-<code>U+0FFF</code> there, and a few more.
     *  Tests both {@link
     *  UnicodeUtils#toMostlyDecomposedUnicode(String, byte)} and
     *  {@link UnicodeUtils#toMostlyDecomposedUnicode(StringBuffer,
     *  byte)}.*/
    public void testMostlyNFKD() {
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0B", NORM_NFKD).equals("\u0F0B"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40", NORM_NFKD).equals("\u0F40"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90", NORM_NFKD).equals("\u0F90"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0C", NORM_NFKD).equals("\u0F0B"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F43", NORM_NFKD).equals("\u0F42\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F42\u0FB7", NORM_NFKD).equals("\u0F42\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4D", NORM_NFKD).equals("\u0F4C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4C\u0FB7", NORM_NFKD).equals("\u0F4C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F52", NORM_NFKD).equals("\u0F51\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F51\u0FB7", NORM_NFKD).equals("\u0F51\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F57", NORM_NFKD).equals("\u0F56\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F56\u0FB7", NORM_NFKD).equals("\u0F56\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5C", NORM_NFKD).equals("\u0F5B\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5B\u0FB7", NORM_NFKD).equals("\u0F5B\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F69", NORM_NFKD).equals("\u0F40\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40\u0FB5", NORM_NFKD).equals("\u0F40\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F73", NORM_NFKD).equals("\u0F71\u0F72"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F72", NORM_NFKD).equals("\u0F71\u0F72"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F75", NORM_NFKD).equals("\u0F71\u0F74"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F74", NORM_NFKD).equals("\u0F71\u0F74"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F76", NORM_NFKD).equals("\u0FB2\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F80", NORM_NFKD).equals("\u0FB2\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F78", NORM_NFKD).equals("\u0FB3\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F80", NORM_NFKD).equals("\u0FB3\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F81", NORM_NFKD).equals("\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F80", NORM_NFKD).equals("\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F93", NORM_NFKD).equals("\u0F92\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F92\u0FB7", NORM_NFKD).equals("\u0F92\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9D", NORM_NFKD).equals("\u0F9C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9C\u0FB7", NORM_NFKD).equals("\u0F9C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA2", NORM_NFKD).equals("\u0FA1\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA1\u0FB7", NORM_NFKD).equals("\u0FA1\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA7", NORM_NFKD).equals("\u0FA6\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA6\u0FB7", NORM_NFKD).equals("\u0FA6\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAC", NORM_NFKD).equals("\u0FAB\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAB\u0FB7", NORM_NFKD).equals("\u0FAB\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB9", NORM_NFKD).equals("\u0F90\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90\u0FB5", NORM_NFKD).equals("\u0F90\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F77", NORM_NFKD).equals("\u0FB2\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F81", NORM_NFKD).equals("\u0FB2\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F71\u0F80", NORM_NFKD).equals("\u0FB2\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F79", NORM_NFKD).equals("\u0FB3\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F81", NORM_NFKD).equals("\u0FB3\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F71\u0F80", NORM_NFKD).equals("\u0FB3\u0F71\u0F80"));
    }

    /** Tests Unicode Normalization form D for Tibetan codepoints.
     *  See Unicode, Inc.'s NormalizationTest-3.2.0.txt.  This
     *  contains all test cases for
     *  <code>U+0F00</code>-<code>U+0FFF</code> there, and a few more.
     *  Tests both {@link
     *  UnicodeUtils#toMostlyDecomposedUnicode(String, byte)} and
     *  {@link UnicodeUtils#toMostlyDecomposedUnicode(StringBuffer,
     *  byte)}.*/
    public void testMostlyNFD() {
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0B", NORM_NFD).equals("\u0F0B"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40", NORM_NFD).equals("\u0F40"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90", NORM_NFD).equals("\u0F90"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0C", NORM_NFD).equals("\u0F0C"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F43", NORM_NFD).equals("\u0F42\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F42\u0FB7", NORM_NFD).equals("\u0F42\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4D", NORM_NFD).equals("\u0F4C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4C\u0FB7", NORM_NFD).equals("\u0F4C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F52", NORM_NFD).equals("\u0F51\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F51\u0FB7", NORM_NFD).equals("\u0F51\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F57", NORM_NFD).equals("\u0F56\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F56\u0FB7", NORM_NFD).equals("\u0F56\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5C", NORM_NFD).equals("\u0F5B\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5B\u0FB7", NORM_NFD).equals("\u0F5B\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F69", NORM_NFD).equals("\u0F40\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40\u0FB5", NORM_NFD).equals("\u0F40\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F73", NORM_NFD).equals("\u0F71\u0F72"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F72", NORM_NFD).equals("\u0F71\u0F72"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F75", NORM_NFD).equals("\u0F71\u0F74"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F74", NORM_NFD).equals("\u0F71\u0F74"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F76", NORM_NFD).equals("\u0FB2\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F80", NORM_NFD).equals("\u0FB2\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F78", NORM_NFD).equals("\u0FB3\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F80", NORM_NFD).equals("\u0FB3\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F81", NORM_NFD).equals("\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F80", NORM_NFD).equals("\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F93", NORM_NFD).equals("\u0F92\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F92\u0FB7", NORM_NFD).equals("\u0F92\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9D", NORM_NFD).equals("\u0F9C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9C\u0FB7", NORM_NFD).equals("\u0F9C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA2", NORM_NFD).equals("\u0FA1\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA1\u0FB7", NORM_NFD).equals("\u0FA1\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA7", NORM_NFD).equals("\u0FA6\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA6\u0FB7", NORM_NFD).equals("\u0FA6\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAC", NORM_NFD).equals("\u0FAB\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAB\u0FB7", NORM_NFD).equals("\u0FAB\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB9", NORM_NFD).equals("\u0F90\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90\u0FB5", NORM_NFD).equals("\u0F90\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F77", NORM_NFD).equals("\u0F77"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F81", NORM_NFD).equals("\u0FB2\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F71\u0F80", NORM_NFD).equals("\u0FB2\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F79", NORM_NFD).equals("\u0F79"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F81", NORM_NFD).equals("\u0FB3\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F71\u0F80", NORM_NFD).equals("\u0FB3\u0F71\u0F80"));
    }

    /** Tests Unicode Normalization form THDL for Tibetan codepoints.
     *  See Unicode, Inc.'s NormalizationTest-3.2.0.txt.  This
     *  contains all test cases for
     *  <code>U+0F00</code>-<code>U+0FFF</code> there, and a few more.
     *  Tests both {@link
     *  UnicodeUtils#toMostlyDecomposedUnicode(String, byte)} and
     *  {@link UnicodeUtils#toMostlyDecomposedUnicode(StringBuffer,
     *  byte)}. */
    public void testMostlyNFTHDL() {
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0B", NORM_NFTHDL).equals("\u0F0B"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40", NORM_NFTHDL).equals("\u0F40"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90", NORM_NFTHDL).equals("\u0F90"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0C", NORM_NFTHDL).equals("\u0F0C"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F43", NORM_NFTHDL).equals("\u0F42\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F42\u0FB7", NORM_NFTHDL).equals("\u0F42\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4D", NORM_NFTHDL).equals("\u0F4C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4C\u0FB7", NORM_NFTHDL).equals("\u0F4C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F52", NORM_NFTHDL).equals("\u0F51\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F51\u0FB7", NORM_NFTHDL).equals("\u0F51\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F57", NORM_NFTHDL).equals("\u0F56\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F56\u0FB7", NORM_NFTHDL).equals("\u0F56\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5C", NORM_NFTHDL).equals("\u0F5B\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5B\u0FB7", NORM_NFTHDL).equals("\u0F5B\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F69", NORM_NFTHDL).equals("\u0F40\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40\u0FB5", NORM_NFTHDL).equals("\u0F40\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F73", NORM_NFTHDL).equals("\u0F71\u0F72"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F72", NORM_NFTHDL).equals("\u0F71\u0F72"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F75", NORM_NFTHDL).equals("\u0F71\u0F74"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F74", NORM_NFTHDL).equals("\u0F71\u0F74"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F76", NORM_NFTHDL).equals("\u0FB2\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F80", NORM_NFTHDL).equals("\u0FB2\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F78", NORM_NFTHDL).equals("\u0FB3\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F80", NORM_NFTHDL).equals("\u0FB3\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F81", NORM_NFTHDL).equals("\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F80", NORM_NFTHDL).equals("\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F93", NORM_NFTHDL).equals("\u0F92\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F92\u0FB7", NORM_NFTHDL).equals("\u0F92\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9D", NORM_NFTHDL).equals("\u0F9C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9C\u0FB7", NORM_NFTHDL).equals("\u0F9C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA2", NORM_NFTHDL).equals("\u0FA1\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA1\u0FB7", NORM_NFTHDL).equals("\u0FA1\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA7", NORM_NFTHDL).equals("\u0FA6\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA6\u0FB7", NORM_NFTHDL).equals("\u0FA6\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAC", NORM_NFTHDL).equals("\u0FAB\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAB\u0FB7", NORM_NFTHDL).equals("\u0FAB\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB9", NORM_NFTHDL).equals("\u0F90\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90\u0FB5", NORM_NFTHDL).equals("\u0F90\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F77", NORM_NFTHDL).equals("\u0FB2\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F81", NORM_NFTHDL).equals("\u0FB2\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F71\u0F80", NORM_NFTHDL).equals("\u0FB2\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F79", NORM_NFTHDL).equals("\u0FB3\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F81", NORM_NFTHDL).equals("\u0FB3\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F71\u0F80", NORM_NFTHDL).equals("\u0FB3\u0F71\u0F80"));


        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("", NORM_NFTHDL).equals(""));

        {
            StringBuffer sb = new StringBuffer("\u0FAC");
            UnicodeUtils.toMostlyDecomposedUnicode(sb, NORM_NFTHDL);
            assertTrue(sb.toString().equals("\u0FAB\u0FB7"));
        }
        {
            StringBuffer sb = new StringBuffer("\u0F66");
            UnicodeUtils.toMostlyDecomposedUnicode(sb, NORM_NFTHDL);
            assertTrue(sb.toString().equals("\u0F66"));
        }
        {
            StringBuffer sb = new StringBuffer("");
            UnicodeUtils.toMostlyDecomposedUnicode(sb, NORM_NFTHDL);
            assertTrue(sb.toString().equals(""));
        }
    }

    /** Tests the containsRa method. */
    public void testContainsRa() {
        assertTrue(!UnicodeUtils.containsRa('\u0F69'));
        assertTrue(!UnicodeUtils.containsRa('\u0FB1'));
        assertTrue(!UnicodeUtils.containsRa('\u0F48'));
        assertTrue(!UnicodeUtils.containsRa('\u0060'));
        assertTrue(!UnicodeUtils.containsRa('\uFFFF'));
        assertTrue(!UnicodeUtils.containsRa('\uFFFF'));

        assertTrue(UnicodeUtils.containsRa('\u0FB2'));
        assertTrue(UnicodeUtils.containsRa('\u0F77'));
        assertTrue(UnicodeUtils.containsRa('\u0F76'));
        assertTrue(UnicodeUtils.containsRa('\u0F6A'));
        assertTrue(UnicodeUtils.containsRa('\u0F62'));
        assertTrue(UnicodeUtils.containsRa('\u0FBC'));
    }

    /**
     * Tests the {@link UnicodeUtils#unicodeStringToString(String)}
     * method. */
    public void testUnicodeStringToString() {
        assertTrue(UnicodeUtils.unicodeStringToString("\u0000").equals("\\u0000"));
        assertTrue(UnicodeUtils.unicodeStringToString("\u0001").equals("\\u0001"));
        assertTrue(UnicodeUtils.unicodeStringToString("\u000F").equals("\\u000f"));
        assertTrue(UnicodeUtils.unicodeStringToString("\u001F").equals("\\u001f"));
        assertTrue(UnicodeUtils.unicodeStringToString("\u00fF").equals("\\u00ff"));
        assertTrue(UnicodeUtils.unicodeStringToString("\u01fF").equals("\\u01ff"));
        assertTrue(UnicodeUtils.unicodeStringToString("\u0ffF").equals("\\u0fff"));
        assertTrue(UnicodeUtils.unicodeStringToString("\u1ffF").equals("\\u1fff"));
        assertTrue(UnicodeUtils.unicodeStringToString("\ufffF").equals("\\uffff"));

        assertTrue(UnicodeUtils.unicodeStringToString("\u0F00\u0091\uABCD\u0FFF\u0Ff1\uFFFF\u0000").equals("\\u0f00\\u0091\\uabcd\\u0fff\\u0ff1\\uffff\\u0000"));
    }

    /**
     * Tests the {@link UnicodeUtils#unicodeCodepointToString(char)}
     * method. */
    public void testUnicodeCodepointToString() {
        assertTrue(UnicodeUtils.unicodeCodepointToString('\u0000', false).equals("\\u0000"));
        assertTrue(UnicodeUtils.unicodeCodepointToString('\u0001', false).equals("\\u0001"));
        assertTrue(UnicodeUtils.unicodeCodepointToString('\u000F', false).equals("\\u000f"));
        assertTrue(UnicodeUtils.unicodeCodepointToString('\u001F', false).equals("\\u001f"));
        assertTrue(UnicodeUtils.unicodeCodepointToString('\u00fF', false).equals("\\u00ff"));
        assertTrue(UnicodeUtils.unicodeCodepointToString('\u01fF', false).equals("\\u01ff"));
        assertTrue(UnicodeUtils.unicodeCodepointToString('\u0ffF', false).equals("\\u0fff"));
        assertTrue(UnicodeUtils.unicodeCodepointToString('\u1ffF', false).equals("\\u1fff"));
        assertTrue(UnicodeUtils.unicodeCodepointToString('\ufffF', false).equals("\\uffff"));
    }

    /**
     * Tests the {@link UnicodeUtils#isEntirelyTibetanUnicode(String)}
     * method. */
    public void testIsEntirelyTibetanUnicode() {
        assertTrue(UnicodeUtils.isEntirelyTibetanUnicode("\u0F00\u0FFF\u0F00\u0F1e\u0F48")); // U+0F48 is reserved, but in the range.
        assertTrue(!UnicodeUtils.isEntirelyTibetanUnicode("\u0F00\u1000\u0FFF\u0F00\u0F1e\u0F48")); // U+0F48 is reserved, but in the range.
    }

    /**
     * Tests the {@link UnicodeUtils#isTibetanConsonant(char)}
     * method. */
    public void testIsTibetanConsonant() {
        assertTrue(!UnicodeUtils.isTibetanConsonant('\u0000'));
        assertTrue(!UnicodeUtils.isTibetanConsonant('\uF000'));
        assertTrue(!UnicodeUtils.isTibetanConsonant('\u0EFF'));
        assertTrue(!UnicodeUtils.isTibetanConsonant('\u1000'));
        assertTrue(!UnicodeUtils.isTibetanConsonant('\u0F00'));
        assertTrue(!UnicodeUtils.isTibetanConsonant('\u0FFF'));

        assertTrue(UnicodeUtils.isTibetanConsonant('\u0FB2'));
        assertTrue(UnicodeUtils.isTibetanConsonant('\u0F6A'));
        assertTrue(UnicodeUtils.isTibetanConsonant('\u0F40'));
        assertTrue(UnicodeUtils.isTibetanConsonant('\u0F50'));
        assertTrue(UnicodeUtils.isTibetanConsonant('\u0FBC'));
        assertTrue(UnicodeUtils.isTibetanConsonant('\u0FB9'));
        assertTrue(UnicodeUtils.isTibetanConsonant('\u0FB0'));
        assertTrue(UnicodeUtils.isTibetanConsonant('\u0FAD'));
        assertTrue(UnicodeUtils.isTibetanConsonant('\u0FA6'));
        assertTrue(UnicodeUtils.isTibetanConsonant('\u0F90'));
        assertTrue(UnicodeUtils.isTibetanConsonant('\u0F91'));

        // reserved codepoints:
        assertTrue(!UnicodeUtils.isTibetanConsonant('\u0F48'));
        assertTrue(!UnicodeUtils.isTibetanConsonant('\u0F98'));
    }

    /**
     * Tests the {@link UnicodeUtils#isInTibetanRange(char)}
     * method. */
    public void testIsInTibetanRange() {
        assertTrue(!UnicodeUtils.isInTibetanRange('\u0000'));
        assertTrue(!UnicodeUtils.isInTibetanRange('\u0100'));
        assertTrue(!UnicodeUtils.isInTibetanRange('\u1000'));
        assertTrue(UnicodeUtils.isInTibetanRange('\u0F00'));
        assertTrue(UnicodeUtils.isInTibetanRange('\u0FF0'));
        assertTrue(UnicodeUtils.isInTibetanRange('\u0FFF'));
    }

    /**
     * Tests the {@link UnicodeUtils#fixSomeOrderingErrorsInTibetanUnicode(StringBuffer)}
     * method. */
    public void testFixSomeOrderingErrorsInTibetanUnicode() {
        // Test that "\u0f67\u0f72\u0f71" becomes "\u0f67\u0f71\u0f72", e.g:
        String tt[][] = {
            { "\u0f67\u0f72\u0f71", "\u0f67\u0f71\u0f72" },
            { "\u0f7a\u0f72\u0f71", "\u0f71\u0f7a\u0f72" },
            { "\u0f67\u0f7e\u0f71", "\u0f67\u0f71\u0f7e" },
            { "\u0f67\u0f74\u0f71", "\u0f67\u0f71\u0f74" },
            { "\u0f67\u0f7e\u0f72", "\u0f67\u0f72\u0f7e" },
            { "\u0f67\u0f7e\u0f74", "\u0f67\u0f74\u0f7e" },
        };
        for (int i = 0; i < tt.length; i++) {
            StringBuffer sb = new StringBuffer(tt[i][0]);
            assertTrue(true == UnicodeUtils.fixSomeOrderingErrorsInTibetanUnicode(sb));
            assertTrue(sb.toString().equals(tt[i][1]));
        }

        // Test that "\u0f67\u0f71\u0f72" stays the same, e.g.:
        String uu[] = { "\u0f67\u0f71\u0f72" };
        for (int i = 0; i < uu.length; i++) {
            StringBuffer sb = new StringBuffer(uu[i]);
            assertTrue(false == UnicodeUtils.fixSomeOrderingErrorsInTibetanUnicode(sb));
            assertTrue(sb.toString().equals(uu[i]));
        }
    }
}
