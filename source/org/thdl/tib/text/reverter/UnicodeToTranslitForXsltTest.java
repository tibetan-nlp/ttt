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

import org.thdl.util.ThdlOptions;
import org.thdl.tib.text.ttt.ErrorsAndWarnings;

/** Tests the UnicodeToTranslitForXslt class.
 *
 *  @author David Chandler */
public class UnicodeToTranslitForXsltTest extends TestCase {

    /** Invokes a text UI and runs all this class's tests. */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(UnicodeToTranslitForXsltTest.class);
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

    public UnicodeToTranslitForXsltTest() { }

    public void testUnicodeToEwts() {
        assertEquals("ka", UnicodeToTranslitForXslt.unicodeToEwtsForComputers("\u0f40"));
        assertEquals("g+ya", UnicodeToTranslitForXslt.unicodeToEwtsForComputers("\u0f42\u0fb1"));
        // TODO(dchandler): assertEquals("brtags ", UnicodeToTranslitForXslt.unicodeToEwtsForHumans("\u0f56\u0f62\u0f9f\u0f42\u0f66\u0f0b"));
    }

    public void testUnicodeToAcip() {
        if (false) {
            assertEquals("KA", UnicodeToTranslitForXslt.unicodeToAcip("\u0f40"));
            assertEquals("BRTAGS ", UnicodeToTranslitForXslt.unicodeToAcip("\u0f56\u0f62\u0f9f\u0f42\u0f66\u0f0b"));
        }
    }
}
