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

package org.thdl.tib.text.reverter;

import junit.framework.TestCase;

import org.thdl.tib.text.tshegbar.UnicodeUtils;
import org.thdl.tib.text.ttt.ErrorsAndWarnings;
import org.thdl.util.ThdlOptions;

/** Tests the Converter class.
 *
 *  @author David Chandler */
public class ConverterTest extends TestCase {

    /** Invokes a text UI and runs all this class's tests. */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ConverterTest.class);
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

    /** Asserts that converting s from Unicode to EWTS yields an
     *  error. */
    private void err(String s) {
        StringBuffer sb = new StringBuffer();
        String ewts = Converter.convertToEwtsForComputers(s, sb);
        boolean error = (sb.length() > 0);
        if (!error) {
            System.out.println("expected error but got EWTS '" + ewts
                               + "' for "
                               + UnicodeUtils.unicodeStringToPrettyString(s));
        }
        assertTrue(error);
    }

    /** Tests Converter.convertToEwtsForHumans. */
    private void hconv(String uni, String ewts) {
        System.out.println("TODO(dchandler): DLC: implement me");
    }

    /** Tests Converter.convertToEwtsForComputers. */
    private void conv(String uni, String ewts) {
        StringBuffer sb = new StringBuffer();
        String actualEwts = Converter.convertToEwtsForComputers(uni, sb);
        assertEquals("Expected " + ewts + " but got " + actualEwts + ":\n",
                     ewts, actualEwts);
        boolean error = (sb.length() > 0);
        assertTrue(!error);
    }

    public ConverterTest() { }

    public void testUnicodeToEwts() {
        conv("\u0f56\u0f62\u0f9f\u0f42\u0f66\u0f0b", "bar+tagasa ");
        conv("\u0f40", "ka");
        // TODO(dchandler): DLC Tibetans use Arabic numerals and English punctuation.
        // conv("0123456789.\u0f40", "0123456789.ka");
        conv("\u0f40\u0f7b", "kai");
        conv("\u0f40\u0f76", "k+r-i");
        conv("\u0f40\u0020\u0f40", "ka_ka");
        conv("\u0f40\n\u0f40\t\u0f40\r\n", "ka\nka\tka\r\n");
        conv("\u0f04\u0f05\u0f40\u0f0c\u00a0\u0f42", "@#ka*_ga");
        conv("\u0f42\u0f61", "gaya");
        hconv("\u0f42\u0f61", "g.ya");
        conv("\u0f42\u0fb1", "g+ya");
        hconv("\u0f42\u0fb1", "gya");
        conv("\u0f54\u0f7e", "paM");
        conv("\u0f54\u0f71\u0f7e", "pAM");
        conv("\u0f54\u0f7e", "paM");
        conv("\u0f54\u0f74\u0f7e", "puM");
        conv("\u0f54\u0fc6", "p\\u0FC6");
        conv("\u0f40\u0f72\u0f74", "ku+i");  // bottom-to-top
        conv("\u0f40\u0f72\u0f74\u0f39", "k^u+i");  // 0f39 first
        conv("\u0f40\u0f73", "kI");
        conv("\u0f40\u0f71\u0f72", "kI");
        conv("\u0f40\u0f72\u0f71", "kI");
        conv("\u0f40\u0f73\u0f74", "kU+i");
        err("\u0f48");
        err("\u0f32\u0f39");
        err("\u0f47\u0f98");
        conv("\u0fcc", "\\u0FCC");
        err("\u0fcd");
        err("\u0f90");
        err("\u0f90\u0fc6");
        conv("\u0f0b\u0fc6", " \\u0FC6");  // ugly but legal...
        err("\u0f0b\u0f90");
        err("\u0f0b\u0f74");
        err("\u0f0b\u0f7f");
        err("\u0f0b\u0f3e");
        conv("\u0f32\u0f18", "\\u0F32\\u0F18");
        conv("\u0f54\u0fa4\u0f90", "p+p+ka");
        // TODO(dchandler): warn("\u0f54\u0fa4\u0f90\u0f39"); (or do
        // CCCVs work for this?)
        if (false) {
            // 0f39 could go with any of the three, so we give an error:
            err("\u0f54\u0fa4\u0f90\u0f74\u0f39");
        } else {
            // TODO(dchandler): I want an error, not this:
            conv("\u0f54\u0fa4\u0f90\u0f74\u0f39", "p+p+k^u");
        }
        conv("\u0f54\u0fa4\u0f90\u0f39", "p+p+k^a");
        conv("\u0f55\u0f39", "fa");
        conv("\u0f55\u0f74\u0f39", "fu");
        conv("\u0f56\u0f39", "va");
        conv("\u0f56\u0f74\u0f39", "vu");
        conv("\u0f54\u0f39\u0fa4\u0f90", "p^+p+ka");
        conv("\u0f40\u0f7e", "kaM");
        conv("\u0f40\u0f83", "ka~M");
        conv("\u0f40\u0f82", "ka~M`");
        conv("\u0f40\u0f84", "ka?");
        conv("\u0f40\u0f85\u0f40", "ka&ka");
        err("\u0f7f");
        conv("\u0f40\u0f7f", "kaH");
        conv("\u0f40\u0f7f\u0f72", "kiH");
        conv("\u0f40\u0f7f\u0f7f\u0f72\u0f7f", "kiHHH");
        conv("\u0f40\u0f7f\u0f7e", "kaHM");
        conv("\u0f40\u0f7e\u0f7f", "kaMH");
        conv("\u0f40\u0f7f\u0f7e\u0f72", "kiHM");
        conv("\u0f04\u0f05", "@#");
        conv("\u0f04\u0f05\u0f05", "@##");
        conv("\u0f04", "@");  // TODO(dchandler): Is this ever seen
                              // alone?  warn/error otherwise.
        conv("\u0f05", "#");  // TODO(dchandler): warn or error
    }
}
// TODO(dchandler): DLC: test all these round-trip, i.e. assert that
// Uni->EWTS->Uni produces the same Uni.

// TODO(dchandler): test with ZWSP or joiners or whatever weird crap
// you can throw in legally to alter boundaries
