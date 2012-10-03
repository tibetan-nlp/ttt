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

package org.thdl.tib.text;

import junit.framework.TestCase;

import org.thdl.util.ThdlOptions;

/**
 @author David Chandler

 Tests a tiny part of TibetanMachineWeb's functionality.  This class
 gets tested much better by other test classes, really, though it's
 not exactly at the unit level. */
public class TibetanMachineWebTest extends TestCase {
    public TibetanMachineWebTest(String a0) {
        super(a0);
    }

    /** Invokes a text UI and runs all this class's tests. */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(TibetanMachineWebTest.class);
    }

    protected void setUp() {
        // We don't want to use options.txt:
        ThdlOptions.forTestingOnlyInitializeWithoutDefaultOptionsFile();

        ThdlOptions.setUserPreference("thdl.acip.to.tibetan.warning.and.error.severities.are.built.in.defaults", "true");
        ThdlOptions.setUserPreference("thdl.acip.to.tibetan.warning.severity.507", "Most");

        // We don't want to load the TM or TMW font files ourselves:
        ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);
        ThdlOptions.setUserPreference("thdl.rely.on.system.tm.fonts", true);
        ThdlOptions.setUserPreference("thdl.debug", true);
    }

    /** Tests {@link
        TibetanMachineWeb#startsWithWylieVowelSequence(String)}. */
    public void testStartsWithWylieVowelSequence() {
        assertTrue(!org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("M"));
        assertTrue(!org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("HM"));
        assertTrue(!org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("?"));
        assertTrue(!org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("~"));
        assertTrue(!org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("~M"));
        assertTrue(!org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("~M`"));
        assertTrue(org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("u"));
        assertTrue(org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("e"));
        assertTrue(org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("eu"));
        assertTrue(org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("eu"));
        assertTrue(org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("-I"));
        assertTrue(org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("eieio"));
        assertTrue(org.thdl.tib.text.TibetanMachineWeb.startsWithWylieVowelSequence("auai-iAI"));
    }

    public void testTshegUnicode() {
        assertEquals(TibetanMachineWeb.getUnicodeForWylieForGlyph(" "),
                     "\u0f0b");
    }
}


