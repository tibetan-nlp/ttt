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
 * Tests {@link org.thdl.tib.text.tshegbar.UnicodeCodepointToThdlWylie}
 * at the unit level.
 */
public class UnicodeCodepointToThdlWylieTest
    extends TestCase implements UnicodeConstants
{
	/**
	 * Plain vanilla constructor for UnicodeCodepointToThdlWylieTest.
	 * @param arg0
	 */
	public UnicodeCodepointToThdlWylieTest(String arg0) {
		super(arg0);
	}
    /** Invokes a text UI and runs all this class's tests. */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(UnicodeCodepointToThdlWylieTest.class);
	}

    /**
     * Tests the {@link
     * UnicodeCodepointToThdlWylie#getThdlWylieForUnicodeString(String)}
     * method. */
    public void testGetThdlWylieForUnicodeString() {
        assertTrue(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeString("\u0F00\u0F00").toString().equals("oMoM"));
        assertTrue(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeString("").toString().equals(""));

        // This illustrates that getThdlWylieForUnicodeString doesn't
        // work correctly when context is required.
        assertTrue(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeString("\u0F56\u0F4F\u0F44").toString().equals("btng")); // "btng", not "btang"

        // test subscribed and superscribed letters:
        assertTrue(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeString("\u0F62\u0F92\u0FB2").toString().equals("rgr"));
        assertTrue(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeString("\u0F66\u0F91\u0FB2").toString().equals("skhr"));
    }

    /**
     * Tests the {@link
     * UnicodeCodepointToThdlWylie#getThdlWylieForUnicodeCodepoint(char)}
     * method. */
    public void testGetThdlWylieForUnicodeCodepoint() {
        assertTrue(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint('\u0F00').equals("oM"));

        assertTrue(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint('\u0FB2').equals("r"));
        assertTrue(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint('\u0FBC').equals("r"));
        assertTrue(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint('\u0F6A').equals("r"));
        assertTrue(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint('\u0F62').equals("r"));
    }
}
