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
Library (THDL). Portions created by the THDL are Copyright 2005 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

import junit.framework.TestCase;

import org.thdl.util.ThdlOptions;

/** Tests EwtsToUnicodeForXslt at the unit level.  For such a class, a
 *  much more important test is one that actually uses XSLT.
 *  TODO(dchandler): write such a test.  You may even be able to use
 *  JUnit for it.
 *
 *  @author David Chandler */
public class EwtsToUnicodeForXsltTest extends TestCase {

    /** Invokes a text UI and runs all this class's tests. */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(EwtsToUnicodeForXsltTest.class);
    }

    protected void setUp() {
        // We don't want to use options.txt:
        ThdlOptions.forTestingOnlyInitializeWithoutDefaultOptionsFile();

        ThdlOptions.setUserPreference("thdl.acip.to.tibetan.warning.and.error.severities.are.built.in.defaults", "true");
        ThdlOptions.setUserPreference("thdl.acip.to.tibetan.warning.severity.507", "Most");
        ErrorsAndWarnings.setupSeverityMap();

        // We don't want to load the TM or TMW font files ourselves:
        ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);
        ThdlOptions.setUserPreference("thdl.rely.on.system.tm.fonts", true);
        ThdlOptions.setUserPreference("thdl.debug", true);
    }


    public EwtsToUnicodeForXsltTest() { }

    private static void help(String ewts, String expected) {
        String actual = EwtsToUnicodeForXslt.convertEwtsTo(ewts);
        assertEquals(expected, actual);
    }

    public void testIt() throws java.io.IOException {
        help("ga",
             "\u0f42");
        help("\u0f00\u0f01\u0f02 \u0f03 \u0fcf",
             "\u0f00\u0f01\u0f02\u0f0b\u0f03\u0f0b"
             + "\u0fcf");
// TODO(dchandler): I think EWTS->Tibetan ought to not give errors
// about the disambiguators here:
//         help("\u0f00.\u0f01.\u0f02 \u0f03 \u0fcf",
//              "\u0f00\u0f01\u0f02\u0f0b\u0f03\u0f0b"
//              + "\u0fcf");
        help("k+Shu+A+i+o+eHM",
             "\u0f40\u0fb5\u0f71\u0f74\u0f72\u0f7a\u0f7c\u0f7e"
             + "\u0f7f");
        help(" . ",
             "\u0f0b[#ERROR 130: The tsheg bar (\"syllable\") {.} is"
             + " essentially nothing.]\u0f0b");
    }
}

