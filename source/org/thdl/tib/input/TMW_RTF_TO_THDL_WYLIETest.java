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

package org.thdl.tib.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

import org.apache.commons.jrcs.diff.Revision;
import org.apache.commons.jrcs.tools.JDiff;
import org.thdl.util.ThdlOptions;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.tib.input.TibetanConverter} at the unit
 * level.  The name is a misnomer; we test more than just
 * TMW.rtf->EWTS conversions.
 */
public class TMW_RTF_TO_THDL_WYLIETest extends TestCase {
	/**
	 * Plain vanilla constructor for TMW_RTF_TO_THDL_WYLIETest.
	 * @param arg0
	 */
	public TMW_RTF_TO_THDL_WYLIETest(String arg0) {
		super(arg0);
	}

    protected void setUp() {

        // Runs on Linux/Unix boxes without X11 servers:
        System.setProperty("java.awt.headless", "true");
        // FIXME: ant check still fails because it doesn't see the
        // above property early enough.

        // We don't want to use options.txt:
        ThdlOptions.forTestingOnlyInitializeWithoutDefaultOptionsFile();

        ThdlOptions.setUserPreference("thdl.acip.to.tibetan.warning.and.error.severities.are.built.in.defaults", "true");
        ThdlOptions.setUserPreference("thdl.acip.to.tibetan.warning.severity.507", "Most");
        org.thdl.tib.text.ttt.ErrorsAndWarnings.setupSeverityMap();

        // We do want debugging assertions:
        ThdlOptions.setUserPreference("thdl.debug", true);
    }


    /** Invokes a text UI and runs all this class's tests. */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TMW_RTF_TO_THDL_WYLIETest.class);
	}

    private static void testActualAndExpected(String testName) {
        // Now ensure that the expected result and the actual result
        // coincide.
        int rc;
        String actualFile
            = "bin" + File.separator
            + "for-junit" + File.separator
            + "TMW_RTF_TO_THDL_WYLIE" + testName + ".out";
        String expectedFile
            = "source" + File.separator
            + "org" + File.separator
            + "thdl" + File.separator
            + "tib" + File.separator
            + "input" + File.separator
            + "TMW_RTF_TO_THDL_WYLIE" + testName + ".expected";
        assertTrue("The file the converter should've produced doesn't exist: "
                   + actualFile,
                   new File(actualFile).exists());
        assertTrue("The baseline file, the file containing the expected results, doesn't exist: "
                   + expectedFile,
                   new File(expectedFile).exists());
        Revision rev = JDiff.getDiff(expectedFile, actualFile);
        assertTrue("JDiff.getDiff returned null", null != rev);
        String lineSep = System.getProperty("line.separator");
        boolean foundExpectedDiff = false;
        String expectedDiff
            = ("3c3" + lineSep
               + "< {\\stylesheet{\\s1\\li0\\ri0\\fi0\\ql\\sbasedon2\\snext1 Body Text;}{\\s2 default;}}\n"
               + "---" + lineSep
               + "> {\\stylesheet{\\s2 default;}{\\s1\\li0\\ri0\\fi0\\ql\\sbasedon2\\snext1 Body Text;}}\n");
        if (0 != rev.size()
            && !(foundExpectedDiff = expectedDiff.equals(rev.toString()))) {
            System.out.println("Oops! the diff is this:");
            System.out.print(rev.toString());
            assertTrue("There was a difference between the actual and expected results",
                       false);
        }
    }


    private void helper(String testName, String inputExtension, String mode,
                        String extension, int erc, String errorMessageLength) {
        String[] args = new String[] {
            "--colors",
            "no",
            "--warning-level",
            "All",
            "--acip-to-tibetan-warning-and-error-messages",
            errorMessageLength,
            mode,
            getTestFileName(testName, inputExtension)
        };
        boolean fileNotFound = false;
        try {
            int rc = TibetanConverter.realMain(args, new PrintStream(new FileOutputStream("bin/for-junit/TMW_RTF_TO_THDL_WYLIE" + testName + "Result" + extension + ".out")));
            if (erc != rc) System.out.println("erc: rc is " + rc);
            assertTrue("the return code of the real conversion was " + rc + " but we expected " + erc,
                       rc == erc);
        } catch (FileNotFoundException e) {
            fileNotFound = true;
        }
        assertTrue("file not found, probably the bin/for-junit/TMW*Result*.out file",
                   !fileNotFound);

        testActualAndExpected(testName + "Result" + extension);
    }

    private static String getTestFileName(String testName,
                                          String inputExtension) {
        return "source" + File.separator
            + "org" + File.separator
            + "thdl" + File.separator
            + "tib" + File.separator
            + "input" + File.separator
            + "TMW_RTF_TO_THDL_WYLIE" + testName + inputExtension;
    }


    /** Tests the --find-some-non-tmw mode of {@link
     *  org.thdl.tib.input.TibetanConverter}. */
    public void testFindSomeNonTMWMode() {
        helper("Test1", ".rtf", "--find-some-non-tmw", "FindSome", 1, "long");
    }

    /** Tests the --find-all-non-tmw mode of {@link
     *  org.thdl.tib.input.TibetanConverter}. */
    public void testFindAllNonTMWMode() {
        helper("Test1", ".rtf", "--find-all-non-tmw", "FindAll", 1, "long");
    }

    /** Tests the --to-wylie converter mode of {@link
     *  org.thdl.tib.input.TibetanConverter}. */
    public void testToWylieConverterMode() {
        helper("Test1", ".rtf", "--to-wylie", "Conversion", 0, "long");
        helper("Test2", ".rtf", "--to-wylie", "Conversion", 44, "long");
    }

    /** Tests the --to-tibetan-machine, --to-tibetan-machine-web,
     *  --to-acip, and --acip-to-tmw converter modes of {@link
     *  org.thdl.tib.input.TibetanConverter}. */
    public void testSomeConverters() {
        if (false) {  // DLC NOW: TODO(DLC)[EWTS->Tibetan]: this test used to work on thdl.org's servers, but no longer does.  Fix that.
            helper("Test4_aka_TD4222I1.INC", "", "--acip-to-unicode", "UNI", 46,
                   "short");
            helper("Test1", ".rtf", "--to-tibetan-machine", "TM", 0, "long");
            helper("Test2", ".rtf", "--to-tibetan-machine", "TM", 0, "long");
            helper("Test2", ".rtf", "--to-tibetan-machine-web", "TMW", 0, "long");
            helper("Test2", ".rtf", "--to-acip", "ACIP", 49, "long");
            helper("Test3", ".acip", "--acip-to-tmw", "TMW", 0, "long");

            /* FAQ: Getting a java.lang.OutOfMemoryError?  See
               http://thdltools.sourceforge.net/BuildSystems.html#oom. */
            helper("Test4_aka_TD4222I1.INC", "", "--acip-to-tmw", "TMW", 46,
                   "short");
        }
    }
}

// TODO(dchandler): put the line 'THIS IS ENGLISH' in
// TMW_RTF_TO_THDL_WYLIETest3.rtf; what would that mean?  I did this once but
// didn't check it in...
