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
Library (THDL). Portions created by the THDL are Copyright 2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.util.RTFFixerInputStream} at the unit level.
 */
public class RTFFixerInputStreamTest extends TestCase {

	/**
	 * Constructor for RTFFixerInputStreamTest.
	 * @param arg0
	 */
	public RTFFixerInputStreamTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(RTFFixerInputStreamTest.class);
	}

    private byte[] testData1 = null;
    private byte[] expected1 = null;
    private byte[] testData2 = null;
    private byte[] expected2 = null;
	public void setUp() {
        try {
            testData1
                = "\\'80\\'81\\'82\\'83\\'84\\'85\\'86\\'87\\'88\\'89\\'8a\\'8A\\'8b\\'8B\\'8c\\'8C\\'8d\\'8D\\'8e\\'8E\\'8f\\'8F\\'90\\'91\\'92\\'93\\'94\\'95\\'96\\'97\\'98\\'99\\'9a\\'9A\\'9b\\'9B\\'9c\\'9C\\'9d\\'9D\\'9e\\'9E\\'9f\\'9F".getBytes("US-ASCII");
            expected1
                = "\\u128 ?\\u129 ?\\u130 ?\\u131 ?\\u132 ?\\u133 ?\\u134 ?\\u135 ?\\u136 ?\\u137 ?\\u138 ?\\u138 ?\\u139 ?\\u139 ?\\u140 ?\\u140 ?\\u141 ?\\u141 ?\\u142 ?\\u142 ?\\u143 ?\\u143 ?\\u144 ?\\u145 ?\\u146 ?\\u147 ?\\u148 ?\\u149 ?\\u150 ?\\u151 ?\\u152 ?\\u153 ?\\u154 ?\\u154 ?\\u155 ?\\u155 ?\\u156 ?\\u156 ?\\u157 ?\\u157 ?\\u158 ?\\u158 ?\\u159 ?\\u159 ?".getBytes("US-ASCII");
            testData2
                = "'80\\'a0\\'Af\\'8g\\'79\\'80\\'81\\'82\\'83\\'84\\'85\\'86\\'87\\'88\\'89\\'8a\\'8A\\'8b\\'8B\\'8c\\'8C\\'8d\\'8D\\'8e\\'8E\\'8f\\'8F\\'90\\'91\\'92\\'93\\'94\\'95\\'96\\'97\\'98\\'99\\'9a\\'9A\\'9b\\'9B\\'9c\\'9C\\'9d\\'9D\\'9e\\'9E\\'9f\\'9F\\'8".getBytes("US-ASCII");
            expected2
                = "'80\\'a0\\'Af\\'8g\\'79\\u128 ?\\u129 ?\\u130 ?\\u131 ?\\u132 ?\\u133 ?\\u134 ?\\u135 ?\\u136 ?\\u137 ?\\u138 ?\\u138 ?\\u139 ?\\u139 ?\\u140 ?\\u140 ?\\u141 ?\\u141 ?\\u142 ?\\u142 ?\\u143 ?\\u143 ?\\u144 ?\\u145 ?\\u146 ?\\u147 ?\\u148 ?\\u149 ?\\u150 ?\\u151 ?\\u152 ?\\u153 ?\\u154 ?\\u154 ?\\u155 ?\\u155 ?\\u156 ?\\u156 ?\\u157 ?\\u157 ?\\u158 ?\\u158 ?\\u159 ?\\u159 ?\\'8".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            assertTrue(false);
        }
    }

	public void testIt1_1() throws IOException {
        helper1(testData1, expected1, true);
    }
	public void testIt1_2() throws IOException {
        helper1(testData2, expected2, false);
    }
	private void helper1(byte []testData, byte []expected, boolean firstByteCausesSubstitution) throws IOException {
        RTFFixerInputStream fixer = new RTFFixerInputStream(new ByteArrayInputStream(testData));
        byte[] totalOutput = new byte[((testData.length
                                        + RTFFixerInputStream.bytesInOldEscape)
                                       * RTFFixerInputStream.bytesInNewEscape)
                                     / RTFFixerInputStream.bytesInOldEscape];
        int totalOutputUsed = 0;
        int byteRead;
        boolean first = true;
        do {
            byteRead = fixer.read();
            if (byteRead != -1)
                totalOutput[totalOutputUsed++] = (byte)byteRead;
            if (first) {
                assertTrue(fixer.performedSubstitutions() == firstByteCausesSubstitution);
                first = false;
            }
        } while (byteRead != -1);
        ensureEqualsExpected(totalOutput, totalOutputUsed, expected);
        assertTrue(fixer.performedSubstitutions());
    }

    private void ensureEqualsExpected(byte[] totalOutput, int totalOutputUsed, byte[] expected) {
        byte[] finalOutput = new byte[totalOutputUsed];
        System.arraycopy(totalOutput, 0, finalOutput, 0, totalOutputUsed);
        assertTrue(java.util.Arrays.equals(finalOutput, expected));
    }

	public void testIt2_1() throws IOException {
        helper2(testData1, expected1, true);
    }
	public void testIt2_2() throws IOException {
        helper2(testData2, expected2, false);
    }
	private void helper2(byte []testData, byte []expected, boolean isDataSet1) throws IOException {
        RTFFixerInputStream fixer = new RTFFixerInputStream(new ByteArrayInputStream(testData));
        byte[] totalOutput = new byte[((testData.length
                                        + RTFFixerInputStream.bytesInOldEscape)
                                       * RTFFixerInputStream.bytesInNewEscape)
                                     / RTFFixerInputStream.bytesInOldEscape];
        int totalOutputUsed = 0;
        int byteRead;
        byteRead = fixer.read();
        if (byteRead != -1)
            totalOutput[totalOutputUsed++] = (byte)byteRead;
        else
            assertTrue(false);
        int rv2 = fixer.read(totalOutput, totalOutputUsed, totalOutput.length - totalOutputUsed);
        if (rv2 > 0) totalOutputUsed += rv2;
        assertTrue(!isDataSet1 || rv2 == RTFFixerInputStream.bytesInNewEscape - 1);
        int rv3 = fixer.read(totalOutput, totalOutputUsed, totalOutput.length - totalOutputUsed);
        if (rv3 > 0) totalOutputUsed += rv3;
        assertTrue(!isDataSet1 || rv3 == 172);
        int rv4 = fixer.read(totalOutput, totalOutputUsed, totalOutput.length - totalOutputUsed);
        if (rv4 > 0) totalOutputUsed += rv4;
        int rv5 = fixer.read(totalOutput, totalOutputUsed, totalOutput.length - totalOutputUsed);
        if (rv5 > 0) totalOutputUsed += rv5;

        ensureEqualsExpected(totalOutput, totalOutputUsed, expected);
        assertTrue(fixer.performedSubstitutions());
    }

	public void testIt3_1() throws IOException {
        // We'll make the stream return "\\\'9" to RTFFixerInputStream, forcing it to read ahead.
        helper3(testData1, expected1, 3);
        // We'll make the stream return "\\\'" to RTFFixerInputStream, forcing it to read ahead.
        helper3(testData1, expected1, 2);
        // We'll make the stream return "\\" to RTFFixerInputStream, forcing it to read ahead.
        helper3(testData1, expected1, 1);
        helper3(testData1, expected1, 0);
        helper3(testData1, expected1, 4);
        helper3(testData1, expected1, 5);
        helper3(testData1, expected1, 6);
        helper3(testData1, expected1, 7);
        helper3(testData1, expected1, 8);
    }

	public void testIt3_2() throws IOException {
        helper3(testData2, expected2, 3);
        helper3(testData2, expected2, 2);
        helper3(testData2, expected2, 1);
        helper3(testData2, expected2, 0);
        helper3(testData2, expected2, 4);
        helper3(testData2, expected2, 5);
        helper3(testData2, expected2, 6);
        helper3(testData2, expected2, 7);
        helper3(testData2, expected2, 8);
    }
    private void helper3(byte []testData, byte []expected, int k) throws IOException {
        RTFFixerInputStream fixer = new RTFFixerInputStream(new ByteArrayInputStream(testData));
        assertTrue(!fixer.performedSubstitutions());
        byte[] totalOutput = new byte[((testData.length
                                        + RTFFixerInputStream.bytesInOldEscape)
                                       * RTFFixerInputStream.bytesInNewEscape)
                                     / RTFFixerInputStream.bytesInOldEscape];
        int totalOutputUsed = 0;
        int rv1 = fixer.read(totalOutput, totalOutputUsed, k);
        if (rv1 > 0) totalOutputUsed += rv1;
        assertTrue(rv1 == k);

        int rv2 = fixer.read(totalOutput, totalOutputUsed, totalOutput.length - totalOutputUsed);
        if (rv2 > 0) totalOutputUsed += rv2;
        int rv3 = fixer.read(totalOutput, totalOutputUsed, totalOutput.length - totalOutputUsed);
        if (rv3 > 0) totalOutputUsed += rv3;
        int rv4 = fixer.read(totalOutput, totalOutputUsed, totalOutput.length - totalOutputUsed);
        if (rv4 > 0) totalOutputUsed += rv4;
        int rv5 = fixer.read(totalOutput, totalOutputUsed, totalOutput.length - totalOutputUsed);
        if (rv5 > 0) totalOutputUsed += rv5;
        ensureEqualsExpected(totalOutput, totalOutputUsed, expected);
        assertTrue(fixer.performedSubstitutions());
    }
}
