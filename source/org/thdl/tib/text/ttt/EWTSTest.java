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
Library (THDL). Portions created by the THDL are Copyright 2004 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

import java.io.PrintStream;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.thdl.tib.text.TibetanDocument;
import org.thdl.tib.text.tshegbar.UnicodeUtils;
import org.thdl.util.ThdlOptions;

/** Tests this package's ability to understand EWTS and turn it into
 *  the appropriate TMW or Unicode.
 *
 *  @author David Chandler */
public class EWTSTest extends TestCase {

    /** Invokes a text UI and runs all this class's tests. */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(EWTSTest.class);
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


    public EWTSTest() { }

    /** Prints a human-readable explanation of how actual and expected
     *  differ to out.  Precondition: expected is non-null, out is
     *  non-null */
    static void explainInequality(String actual, String expected, PrintStream out) {
        if (null == actual)
            out.println("Expected \""
                        + UnicodeUtils.unicodeStringToPrettyString(expected)
                        + "\" but found the null string");
        if (actual.length() != expected.length()) {
            out.println("Expected a string with " + expected.length()
                        + " characters but found a string with "
                        + actual.length() + " characters");
            return;
        }
        for (int i = 0; i < actual.length(); i++) {
            if (actual.charAt(i) != expected.charAt(i)) {
                out.println("Expected string \"" + UnicodeUtils.unicodeStringToPrettyString(expected) + "\" but found the string \""
                            + UnicodeUtils.unicodeStringToPrettyString(actual)
                            + "\" which differs at character " + i + " (counting from zero, not one)");
            }
        }
    }

    /** Returns the Unicode corresponding to the TMW to which ewts
     *  corresponds, or null if we couldn't push through, even with
     *  errors, from EWTS->TMW->Unicode. */
    private static String ewts2tmw2uni(String ewts) {
        TTraits traits = EWTSTraits.instance();
        StringBuffer errors = new StringBuffer();
        boolean shortMessages = true;
        String warningLevel = "All";  // slow but exercises more code paths
        ArrayList scan
            = traits.scanner().scan(ewts, errors, -1,
                                     shortMessages,
                                     warningLevel);
        if (null == scan) {
            System.out.println("EWTS->TMW->Uni ho");
            return null;
        }
        if (errors.length() > 0) {
            System.out.println("EWTS->TMW->Uni ho: " + errors);
            return null;
        }
        errors = new StringBuffer();
        TibetanDocument tdoc = new TibetanDocument();
        boolean rv;
        try {
            rv = TConverter.convertToTMW(traits,
                                         scan, tdoc, errors, null, null,
                                         false, warningLevel,
                                         shortMessages, true,
                                         new int[] { tdoc.getLength() });
        } catch (java.io.IOException e) {
            // I doubt this can happen.
            throw new Error(e.toString());
        }
        if (!rv) {
            System.out.println("EWTS->TMW->Uni ho2");
            return null;
        }
        if (tdoc.getLength() < 1 && ewts.length() > 0) {
            System.out.println("EWTS->TMW->Uni ho3");
            return null;
        }
        errors = new StringBuffer();
        long numAttemptedReplacements[] = new long[] { 0 };
        tdoc.convertToUnicode(0, tdoc.getLength(), errors, null,
                              numAttemptedReplacements);
        if (errors.length() > 0) {
            System.out.println("EWTS->TMW->Uni ho4: " + errors);
            return null;
        }
        if (numAttemptedReplacements[0] < 1) {
            System.out.println("NOTE: During TMW->Unicode for the EWTS '"
                               + ewts + "', we made no replacements.");
        }
        
        try {
            return tdoc.getText(0, tdoc.getLength());
        } catch (javax.swing.text.BadLocationException e) {
            throw new Error("I know this won't happen: " + e);
        }
    }

    static void ewts2uni_test(String ewts, String expectedUnicode) {
        ewts2uni_test(ewts, expectedUnicode, true);
    }

    /** Tests EWTS->Unicode but not EWTS->TMW[->Unicode]. */
    static void just_ewts2uni_test(String ewts, String expectedUnicode) {
        ewts2uni_test(ewts, expectedUnicode, false);
    }

    /** Causes a JUnit test case failure unless the EWTS document ewts
     *  converts to the unicode expectedUnicode.  If doEwts2tmw2uni is
     *  true, then this causes a test case failure if an
     *  EWTS->TMW->Unicode trip doesn't give the same
     *  expectedUnicode. */
    static void ewts2uni_test(String ewts, String expectedUnicode,
                              boolean doEwts2tmw2uni) {
        StringBuffer errors = new StringBuffer();
        String unicode
            = TConverter.convertToUnicodeText(EWTSTraits.instance(),
                                              ewts, errors,
                                              null, true,
                                              "None", // TODO(DLC)[EWTS->Tibetan]: ???
                                              false /* short warnings */);
        help_ewts2uni_test("EWTS->Unicode: ",
                           ewts, expectedUnicode, unicode, errors);
        if (doEwts2tmw2uni) {
            help_ewts2uni_test("EWTS->TMW->Unicode: ",
                               ewts, expectedUnicode, ewts2tmw2uni(ewts),
                               new StringBuffer());
        }
    }

    /** Doing EWTS->Unicode conversions yields one answer out of many
     *  for some inputs, such as "b+ha".  This function checks for
     *  equality between two pieces of Unicode modulo such acceptable
     *  changes.  It's only complete enough to handle the test cases
     *  we have.  Why do we make two choices?  TMW->Unicode is
     *  different source code from EWTS->Unicode; that's why. */
    private static boolean ewts2uni_unicode_equality(String expectedUnicode,
                                                     String actualUnicode) {
        // TODO(dchandler): replaceAll is a 1.4-ism.  Will users balk?
        if (actualUnicode
            .replaceAll("\u0f0d\u0f0d", "\u0f0e")  // TMW has no \u0f0e glyph
            .replaceAll("\u0f69", "\u0f40\u0fb5")  // equivalent and neither are discouraged
            .replaceAll("\u0f43", "\u0f42\u0fb7")  // ditto...
            .replaceAll("\u0f4d", "\u0f4c\u0fb7")
            .replaceAll("\u0f52", "\u0f51\u0fb7")
            .replaceAll("\u0f57", "\u0f56\u0fb7")
            .replaceAll("\u0f5c", "\u0f5b\u0fb7")
            .replaceAll("\u0fb9", "\u0f90\u0fb5")
            .replaceAll("\u0f93", "\u0f92\u0fb7")
            .replaceAll("\u0f9d", "\u0f9c\u0fb7")
            .replaceAll("\u0fa2", "\u0fa1\u0fb7")
            .replaceAll("\u0fa7", "\u0fa6\u0fb7")  // ...
            .replaceAll("\u0fac", "\u0fab\u0fb7")  // equivalent and neither are discouraged

            .equals(expectedUnicode)) {
            return true;
        }
        return expectedUnicode.equals(actualUnicode);
    }

    private static void help_ewts2uni_test(String prefix,
                                           String ewts,
                                           String expectedUnicode,
                                           String actualUnicode,
                                           StringBuffer errors) {
        if (null == actualUnicode) {
            if (null != expectedUnicode && "none" != expectedUnicode) {
                System.out.println(prefix + "No unicode exists for " + ewts
                                   + " but you expected "
                                   + UnicodeUtils.unicodeStringToPrettyString(expectedUnicode));
                assertTrue(false);
            }
            System.out.println(prefix + "Unicode for " + ewts + " can't be had; errors are " + errors);
        } else {
            if (null != expectedUnicode
                && !ewts2uni_unicode_equality(expectedUnicode, actualUnicode)) {
                explainInequality(actualUnicode, expectedUnicode, System.out);
                if (UnicodeUtils.unicodeStringToPrettyString(actualUnicode).equals(UnicodeUtils.unicodeStringToPrettyString(expectedUnicode))) {
                    System.out.println(prefix + "UGLY strings: The unicode for\n  \"" + ewts
                                       + "\"\nis\n  \""
                                       + actualUnicode
                                       + "\",\nbut you expected\n  \""
                                       + expectedUnicode
                                       + "\"");
                } else {
                    System.out.println(prefix + "The unicode for\n  \"" + ewts
                                       + "\"\nis\n  \""
                                       + UnicodeUtils.unicodeStringToPrettyString(actualUnicode)
                                       + "\",\nbut you expected\n  \""
                                       + UnicodeUtils.unicodeStringToPrettyString(expectedUnicode)
                                       + "\"");
                }
                {
                    StringBuffer sb = new StringBuffer(ewts);
                    EWTSTshegBarScanner.ExpandEscapeSequences(sb);
                    TPairList[] la
                        = EWTSTraits.instance().breakTshegBarIntoChunks(sb.toString(), false);
                    assertTrue(la[1] == null);
                    System.out.println(prefix + "EWTS=" + ewts + " and l'=" + la[0].toString2());
                }
                assertTrue(false);
            }
        }
    }

    /** Returns true iff ewts is not a valid EWTS string. */
    static boolean hasEwtsError(String ewts) {
        StringBuffer errors = new StringBuffer();
        String unicode = TConverter.convertToUnicodeText(EWTSTraits.instance(),
                                                         ewts, errors,
                                                         null, true,
                                                         "None", // TODO(DLC)[EWTS->Tibetan]: ???
                                                         true);
        // TODO(DLC)[EWTS->Tibetan]: Is this sufficient?
        return (null == unicode || errors.length() > 0);
    }

    /** Causes a JUnit test case failure iff the EWTS document ewts is
     *  legal EWTS transliteration. */
    static void assert_EWTS_error(String ewts) {
        boolean ewts_error = hasEwtsError(ewts);
        if (!ewts_error) {
            System.out.println("assert_EWTS_error: We expected a conversion"
                               + " error for the EWTS snippet '"
                               + ewts + "' but found none.");
            assertTrue(ewts_error);
        }
    }

    public void test0F39() {
        ewts2uni_test("v", "\u0F56\u0F39");
        ewts2uni_test("f", "\u0F55\u0F39");
        just_ewts2uni_test("f+beM", "\u0f55\u0f39\u0fa6\u0f7a\u0f7e");
        ewts2uni_test("faM", "\u0f55\u0f39\u0f7e");
        ewts2uni_test("vaM", "\u0f56\u0f39\u0f7e");
        just_ewts2uni_test("k+fa", "\u0f40\u0fa5\u0f39");
        just_ewts2uni_test("f+va", "\u0f55\u0f39\u0fa6\u0f39");
        just_ewts2uni_test("ph+veM", "\u0f55\u0fa6\u0f39\u0f7a\u0f7e");

        ewts2uni_test("a^", "\u0f68\u0f39");
        ewts2uni_test("hUM^", "\u0f67\u0f39\u0f71\u0f74\u0f7e");
        ewts2uni_test("ph^", "\u0f55\u0f39");
        ewts2uni_test("phe^", "\u0f55\u0f39\u0f7a");
        ewts2uni_test("ph^e", "\u0f55\u0f39\u0f68\u0f7a");  // TODO(DLC)[EWTS->Tibetan]: This is no good!  We don't even warn, do we!?  EWTSTraits.isWowelThatRequiresAChen(..) might be to blame


        ewts2uni_test("a\u0f39", "\u0f68\u0f39");
        ewts2uni_test("hUM\u0f39", "\u0f67\u0f39\u0f71\u0f74\u0f7e");
        ewts2uni_test("ph\u0f39", "\u0f55\u0f39");
        ewts2uni_test("phe\u0f39", "\u0f55\u0f39\u0f7a");
        ewts2uni_test("ph\u0f39e", "\u0f55\u0f39\u0f68\u0f7a");  // TODO(DLC)[EWTS->Tibetan]: This is no good!  We don't even warn, do we!?  EWTSTraits.isWowelThatRequiresAChen(..) might be to blame

        if (RUN_FAILING_TESTS) ewts2uni_test("ph^+beM", "\u0f55\u0f39\u0fa6\u0f7a\u0f7e");
    }

    /** Tests that the EWTS->unicode converter isn't completely
        braindead. */
    public void testEwtsBasics() {
        ewts2uni_test("\n\t\nga\nha\nha\tga\r",
                      "\n\t\n\u0f42\n\u0f67\n\u0f67\t\u0f42\r");
        ewts2uni_test("\n", "\n");
        ewts2uni_test("\r\n", "\r\n");
        ewts2uni_test("\n\r", "\n\r");
        ewts2uni_test("\r", "\r");
        ewts2uni_test("\t", "\t");
        ewts2uni_test("\t\n\n", "\t\n\n");

        just_ewts2uni_test("r+sa", "\u0f62\u0fb6");
        ewts2uni_test("R+s", "\u0f6a\u0fb6");

        ewts2uni_test("k?e", "\u0f40\u0f84\u0f68\u0f7a");
        ewts2uni_test("ko+o", "\u0f40\u0f7c\u0f7c");
        ewts2uni_test("kau+u", "\u0f40\u0f74\u0f7d");
        
        ewts2uni_test("g.yogs", "\u0f42\u0f61\u0f7c\u0f42\u0f66");
        ewts2uni_test("brgyad", "\u0f56\u0f62\u0f92\u0fb1\u0f51");
        ewts2uni_test("brjod", "\u0f56\u0f62\u0f97\u0f7c\u0f51");
        ewts2uni_test("drwa", "\u0f51\u0fb2\u0fad");
        ewts2uni_test("rwa", "\u0f62\u0fad");
        ewts2uni_test("ug_pha ", "\u0f68\u0f74\u0f42\u00a0\u0f55\u0f0b");
        ewts2uni_test("a ", "\u0f68\u0f0b");
        ewts2uni_test("g.a ", "\u0f42\u0f68\u0f0b");
        ewts2uni_test("khyAH", "\u0f41\u0fb1\u0f71\u0f7f");
        ewts2uni_test("'ajamH", "\u0f60\u0f47\u0f58\u0f7f");
        assert_EWTS_error("'jamH");  // If we decide this should be legal, TPairList.populateWithTGCPairs is easily modified.
        ewts2uni_test("'jam~X", "\u0f60\u0f47\u0f58\u0f35");
        ewts2uni_test("'jam~XX", "\u0f60\u0f47\u0f58\u0f35\u0f37");
        ewts2uni_test("'jamX~X", "\u0f60\u0f47\u0f58\u0f37\u0f35");
        ewts2uni_test("'jamX", "\u0f60\u0f47\u0f58\u0f37");

        // prefix rules say this is illegal.  use [bana] or [b.na] if
        // you want those.
        assert_EWTS_error("bna ");

        ewts2uni_test("ma", "\u0f58");
        ewts2uni_test("mi", "\u0f58\u0f72");
        ewts2uni_test("mi ", "\u0f58\u0f72\u0f0b");
        ewts2uni_test("mi/", "\u0f58\u0f72\u0f0d");

        // ra does not take a ba prefix, no, but b+ra is a native Tibetan stack.
        ewts2uni_test("bra ", "\u0f56\u0fb2\u0f0b");
        ewts2uni_test("b+ra ", "\u0f56\u0fb2\u0f0b");

        ewts2uni_test("bka", "\u0f56\u0f40");
        ewts2uni_test("bs+ra ", "\u0f56\u0f66\u0fb2\u0f0b");
        ewts2uni_test("bsra ", "\u0f56\u0f66\u0fb2\u0f0b");
        ewts2uni_test("bsrag", "\u0f56\u0f66\u0fb2\u0f42");
        ewts2uni_test("bsragd", "\u0f56\u0f66\u0fb2\u0f42\u0f51");
        assert_EWTS_error("bsragde");
        ewts2uni_test("bsrU*", "\u0f56\u0f66\u0fb2\u0f71\u0f74\u0f0c");

        ewts2uni_test("b.ra ", "\u0f56\u0f62\u0f0b");
        ewts2uni_test("bara ", "\u0f56\u0f62\u0f0b");
        just_ewts2uni_test("b+Ra ", "\u0f56\u0fbc\u0f0b");
    }

    /** Miscellaneous tests of EWTS->Unicode conversion. */
    public void test__EWTS__miscellany() {
        just_ewts2uni_test("ga\\u0f02ha", "\u0f42\u0f02\u0f67"); // TODO(DLC)[EWTS->Tibetan]: ewts->tmw is broken
        just_ewts2uni_test("g.\\u0f03\u0f0b", "\u0f42\u0f03\u0f0b"); // TODO(DLC)[EWTS->Tibetan]: ewts->tmw is broken

        just_ewts2uni_test("k+\u0fb2e", "\u0f40\u0fb2\u0f7a");
        assert_EWTS_error("\u0f42ya");
        just_ewts2uni_test("\u0f42+ya", "\u0f42\u0fb1");
        just_ewts2uni_test("\u0f42.ya", "\u0f42\u0f61");

        just_ewts2uni_test("", "");

        ewts2uni_test("0\\u0f19", "\u0f20\u0f19");
        ewts2uni_test("0\\u0f18", "\u0f20\u0f18");
        ewts2uni_test("0\\u0f3e", "\u0f20\u0f3e"); // TODO(DLC)[EWTS->Tibetan]: test ewts->tmw
        ewts2uni_test("0\\u0f3f", "\u0f20\u0f3f"); // TODO(DLC)[EWTS->Tibetan]: test ewts->tmw

        just_ewts2uni_test("R", "\u0f6A");
        just_ewts2uni_test("Ra", "\u0f6A");

        just_ewts2uni_test("R+ka", "\u0F6A\u0f90");
        just_ewts2uni_test("k+Wa", "\u0f40\u0FBA");
        just_ewts2uni_test("k+Ya", "\u0f40\u0FBB");
        just_ewts2uni_test("k+Ra", "\u0f40\u0FBC");
        ewts2uni_test("k+wa", "\u0f40\u0Fad");
        ewts2uni_test("k+la", "\u0f40\u0Fb3");
        ewts2uni_test("k+ya", "\u0f40\u0Fb1");
        ewts2uni_test("k+ra", "\u0f40\u0Fb2");

        ewts2uni_test("r-I", "\u0f62\u0f71\u0f80");
        ewts2uni_test("l-I", "\u0f63\u0f71\u0f80");
        ewts2uni_test("r-i", "\u0f62\u0f80");
        ewts2uni_test("l-i", "\u0f63\u0f80");
        ewts2uni_test("gr-i", "\u0f42\u0fb2\u0f80");
        ewts2uni_test("gr-I", "\u0f42\u0fb2\u0f71\u0f80");
        ewts2uni_test("gl-i", "\u0f42\u0fb3\u0f80");
        ewts2uni_test("gl-I", "\u0f42\u0fb3\u0f71\u0f80");
    }



    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop achen (U+0F68) is correct. */
    public void test__EWTS__wowels_on_achen() {

        assert_EWTS_error("+yo");
        ewts2uni_test("a+yo", "\u0f68\u0fb1\u0f7c");
        ewts2uni_test("a+yo+o", "\u0f68\u0fb1\u0f7c\u0f7c");
        ewts2uni_test("a+ya.una", "\u0f68\u0fb1\u0f68\u0f74\u0f53");
        ewts2uni_test("a+yauna", "\u0f68\u0fb1\u0f7d\u0f53"); // TODO(DLC)[EWTS->Tibetan]: warn that '.' might have been needed
        ewts2uni_test("a+yoona", "\u0f68\u0fb1\u0f7c\u0f68\u0f7c\u0f53"); // TODO(DLC)[EWTS->Tibetan]: warn!
        ewts2uni_test("a+yoon", "\u0f68\u0fb1\u0f7c\u0f68\u0f7c\u0f53"); // TODO(DLC)[EWTS->Tibetan]: warn!
//        ewts2uni_test("a+yo+ona", "TODO(DLC)[EWTS->Tibetan]");

        ewts2uni_test("A", "\u0f68\u0f71");
        ewts2uni_test("i", "\u0f68\u0f72");
        ewts2uni_test("I", "\u0f68\u0f71\u0f72");
        ewts2uni_test("u", "\u0f68\u0f74");
        ewts2uni_test("U", "\u0f68\u0f71\u0f74");
        ewts2uni_test("a+r-i", "\u0f68\u0fb2\u0f80");
        ewts2uni_test("a+r-I", "\u0f68\u0fb2\u0f71\u0f80");
        just_ewts2uni_test("a+l-i", "\u0f68\u0fb3\u0f80");
        just_ewts2uni_test("a+l-I", "\u0f68\u0fb3\u0f71\u0f80");
        ewts2uni_test("e", "\u0f68\u0f7a");
        ewts2uni_test("ai", "\u0f68\u0f7b");
       // ewts2uni_test("ao", "\u0f68\u0f68\u0f7c"); // TODO(DLC)[EWTS->Tibetan]:
        // assert_EWTS_error("ao"); // TODO(DLC)[EWTS->Tibetan]:
        ewts2uni_test("o", "\u0f68\u0f7c");
        ewts2uni_test("au", "\u0f68\u0f7d");
       // ewts2uni_test("aM", "\u0f68\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
       // ewts2uni_test("aH", "\u0f68\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("-i", "\u0f68\u0f80");
        ewts2uni_test("-I", "\u0f68\u0f71\u0f80");
      //  ewts2uni_test("a~M`", "\u0f68\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
      //  ewts2uni_test("a~M", "\u0f68\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
//        ewts2uni_test("a?", "\u0f68\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        just_ewts2uni_test("\\u0f68", "\u0f68");
        ewts2uni_test("\\u0f86", "\u0f68\u0f86"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("a\\u0f86", "\u0f68\u0f86");
        ewts2uni_test("a\\U0f86", "\u0f68\u0f86");
        ewts2uni_test("a\\U0F86", "\u0f68\u0f86");
        ewts2uni_test("a\\u0F86", "\u0f68\u0f86");
        ewts2uni_test("a\\u00000f86", "\u0f68\u0f86");
        ewts2uni_test("a\\u00000f86", "\u0f68\u0f86");
        ewts2uni_test("a\\u00000F86", "\u0f68\u0f86");
        ewts2uni_test("a\\u00000F86", "\u0f68\u0f86");
        ewts2uni_test("a\\u0f87", "\u0f68\u0f87");

//        ewts2uni_test("aMH", "\u0f68\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
//        ewts2uni_test("aHM", "\u0f68\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("a", "\u0f68");

    }
    
    public void test__EWTS__stacked_wowels_on_achen() {
        if (RUN_FAILING_TESTS) { // TODO(DLC)[EWTS->Tibetan]: make this true ASAP
        ewts2uni_test("o+o", "\u0f68\u0f7c\u0f7c");
        assert_EWTS_error("a+o"); // TODO(DLC)[EWTS->Tibetan]:?
        assert_EWTS_error("o+a"); // TODO(DLC)[EWTS->Tibetan]:?
        assert_EWTS_error("ka+o"); // TODO(DLC)[EWTS->Tibetan]:?
        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("A+i", "\u0f68\u0f71\u0f72");
        ewts2uni_test("e+e", "\u0f68\u0f7a\u0f7a");
        ewts2uni_test("e+e+e", "\u0f68\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("e+e+e+e", "\u0f68\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("e+e+e+e+e", "\u0f68\u0f7a\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("o+e", "\u0f68\u0f7c\u0f7a");
        ewts2uni_test("u+A+i+o+e", "\u0f68\u0f74\u0f71\u0f72\u0f7c\u0f7a");
        ewts2uni_test("u+A+i+o+eHM", "\u0f68\u0f74\u0f71\u0f72\u0f7c\u0f7a\u0f7e\u0f7f");
        ewts2uni_test("u+A", "\u0f68\u0f74\u0f71");

        ewts2uni_test("o+-I", "DLC");
        }
    }

    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop ka (U+0F40) is correct. */
    public void test__EWTS__wowels_on_ka() {
        ewts2uni_test("kA", "\u0f40\u0f71");
        ewts2uni_test("ki", "\u0f40\u0f72");
        ewts2uni_test("kI", "\u0f40\u0f71\u0f72");
        ewts2uni_test("ku", "\u0f40\u0f74");
        ewts2uni_test("kU", "\u0f40\u0f71\u0f74");
        ewts2uni_test("k+r-i", "\u0f40\u0fb2\u0f80");
        ewts2uni_test("k+r-I", "\u0f40\u0fb2\u0f71\u0f80");
        ewts2uni_test("k+l-i", "\u0f40\u0fb3\u0f80");
        ewts2uni_test("k+l-I", "\u0f40\u0fb3\u0f71\u0f80");
        ewts2uni_test("ke", "\u0f40\u0f7a");
        ewts2uni_test("e", "\u0f68\u0f7a");
        ewts2uni_test("a", "\u0f68");
        ewts2uni_test("kai", "\u0f40\u0f7b");
        ewts2uni_test("ko", "\u0f40\u0f7c");
        ewts2uni_test("kau", "\u0f40\u0f7d");
        ewts2uni_test("kaM", "\u0f40\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("kaH", "\u0f40\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k-i", "\u0f40\u0f80");
        ewts2uni_test("k-I", "\u0f40\u0f71\u0f80");
        ewts2uni_test("ka~M`", "\u0f40\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("ka~M", "\u0f40\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("ka?", "\u0f40\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("ka\\u0f86", "\u0f40\u0f86");
        ewts2uni_test("ka\\U0f86", "\u0f40\u0f86");
        ewts2uni_test("ka\\U0F86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u0F86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u00000f86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u00000f86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u00000F86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u00000F86", "\u0f40\u0f86");
        ewts2uni_test("ka\\u0f87", "\u0f40\u0f87");

        ewts2uni_test("kaMH", "\u0f40\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("kaHM", "\u0f40\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("kA+i", "\u0f40\u0f71\u0f72");
        ewts2uni_test("ko+o", "\u0f40\u0f7c\u0f7c");
        ewts2uni_test("ke+e", "\u0f40\u0f7a\u0f7a");
        ewts2uni_test("ke+e+e", "\u0f40\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("ke+e+e+e", "\u0f40\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("ke+e+e+e+e", "\u0f40\u0f7a\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("ko+e", "\u0f40\u0f7a\u0f7c");
        ewts2uni_test("ku+A+i+o+e", "\u0f40\u0f71\u0f74\u0f72\u0f7a\u0f7c");
        ewts2uni_test("ku+A+i+o+eHM", "\u0f40\u0f71\u0f74\u0f72\u0f7a\u0f7c\u0f7e\u0f7f");
        ewts2uni_test("ku+A", "\u0f40\u0f71\u0f74");

        ewts2uni_test("k", "\u0f40");
        ewts2uni_test("ka", "\u0f40");

        assert_EWTS_error("ka+r-i"); // TODO(DLC)[EWTS->Tibetan]: right?
        assert_EWTS_error("ka+r-I");
        assert_EWTS_error("ka+l-i");
        assert_EWTS_error("ka+l-I");

        assert_EWTS_error("ko+a");
        assert_EWTS_error("ka+o");
    }

    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop achung (U+0F60) is correct. */
    public void test__EWTS__wowels_on_achung() {
        ewts2uni_test("'a", "\u0f60");
        ewts2uni_test("'A", "\u0f60\u0f71");
        ewts2uni_test("'i", "\u0f60\u0f72");
        ewts2uni_test("'I", "\u0f60\u0f71\u0f72");
        ewts2uni_test("'u", "\u0f60\u0f74");
        ewts2uni_test("'U", "\u0f60\u0f71\u0f74");
        ewts2uni_test("'e", "\u0f60\u0f7a");
        ewts2uni_test("'ai", "\u0f60\u0f7b");
        ewts2uni_test("'o", "\u0f60\u0f7c");
        ewts2uni_test("'au", "\u0f60\u0f7d");
        ewts2uni_test("'aM", "\u0f60\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'aH", "\u0f60\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'-i", "\u0f60\u0f80");
        ewts2uni_test("'-I", "\u0f60\u0f71\u0f80");
        ewts2uni_test("'a~M`", "\u0f60\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'a~M", "\u0f60\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'a?", "\u0f60\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'a\\u0f86", "\u0f60\u0f86");
        ewts2uni_test("'a\\U0f86", "\u0f60\u0f86");
        ewts2uni_test("'a\\U0F86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u0F86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u00000f86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u00000f86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u00000F86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u00000F86", "\u0f60\u0f86");
        ewts2uni_test("'a\\u0f87", "\u0f60\u0f87");

        ewts2uni_test("'aMH", "\u0f60\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("'aHM", "\u0f60\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("'A+i", "\u0f60\u0f71\u0f72");
        ewts2uni_test("'o+o", "\u0f60\u0f7c\u0f7c");
        ewts2uni_test("'e+e", "\u0f60\u0f7a\u0f7a");
        ewts2uni_test("'e+e+e", "\u0f60\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("'e+e+e+e", "\u0f60\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("'e+e+e+e+e", "\u0f60\u0f7a\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("'o+e", "\u0f60\u0f7a\u0f7c");
        ewts2uni_test("'u+A+i+o+e", "\u0f60\u0f71\u0f74\u0f72\u0f7a\u0f7c");
        ewts2uni_test("'u+A+i+o+eHM", "\u0f60\u0f71\u0f74\u0f72\u0f7a\u0f7c\u0f7e\u0f7f");

        ewts2uni_test("'u+A", "\u0f60\u0f71\u0f74");

        ewts2uni_test("'", "\u0f60");
        ewts2uni_test("'a", "\u0f60");

        just_ewts2uni_test("'+r-i", "\u0f60\u0fb2\u0f80");
        just_ewts2uni_test("'+r-I", "\u0f60\u0fb2\u0f71\u0f80"); 
        just_ewts2uni_test("'+l-i", "\u0f60\u0fb3\u0f80");
        just_ewts2uni_test("'+l-I", "\u0f60\u0fb3\u0f71\u0f80");
    }

    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop k+Sh (U+0F69) is correct. */
    public void test__EWTS__wowels_on_kSh() {
        ewts2uni_test("k+ShA", "\u0f40\u0fb5\u0f71");
        ewts2uni_test("k+Shi", "\u0f40\u0fb5\u0f72");
        ewts2uni_test("k+ShI", "\u0f40\u0fb5\u0f71\u0f72");
        ewts2uni_test("k+Shu", "\u0f40\u0fb5\u0f74");
        ewts2uni_test("k+ShU", "\u0f40\u0fb5\u0f71\u0f74");
        ewts2uni_test("k+She", "\u0f40\u0fb5\u0f7a");
        ewts2uni_test("k+Shai", "\u0f40\u0fb5\u0f7b");
        ewts2uni_test("k+Sho", "\u0f40\u0fb5\u0f7c");
        ewts2uni_test("k+Shau", "\u0f40\u0fb5\u0f7d");
        ewts2uni_test("k+ShaM", "\u0f40\u0fb5\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+ShaH", "\u0f40\u0fb5\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+Sh-i", "\u0f40\u0fb5\u0f80");
        ewts2uni_test("k+Sh-I", "\u0f40\u0fb5\u0f71\u0f80");
        ewts2uni_test("k+Sha~M`", "\u0f40\u0fb5\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+Sha~M", "\u0f40\u0fb5\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+Sha?", "\u0f40\u0fb5\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+Sha\\u0f86", "\u0f40\u0fb5\u0f86");
        ewts2uni_test("k+Sha\\U0f86", "\u0f40\u0fb5\u0f86");
        ewts2uni_test("k+Sha\\U0F86", "\u0f40\u0fb5\u0f86");
        ewts2uni_test("k+Sha\\u0F86", "\u0f40\u0fb5\u0f86");
        ewts2uni_test("k+Sha\\u00000f86", "\u0f40\u0fb5\u0f86");
        ewts2uni_test("k+Sha\\u00000f86", "\u0f40\u0fb5\u0f86");
        ewts2uni_test("k+Sha\\u00000F86", "\u0f40\u0fb5\u0f86");
        ewts2uni_test("k+Sha\\u00000F86", "\u0f40\u0fb5\u0f86");
        ewts2uni_test("k+Sha\\u0f87", "\u0f40\u0fb5\u0f87");

        ewts2uni_test("k+ShaMH", "\u0f40\u0fb5\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("k+ShaHM", "\u0f40\u0fb5\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("k+ShA+i", "\u0f40\u0fb5\u0f71\u0f72");
        ewts2uni_test("k+Sho+o", "\u0f40\u0fb5\u0f7c\u0f7c");
        ewts2uni_test("k+She+e", "\u0f40\u0fb5\u0f7a\u0f7a");
        ewts2uni_test("k+She+e+e", "\u0f40\u0fb5\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("k+She+e+e+e", "\u0f40\u0fb5\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("k+She+e+e+e+e", "\u0f40\u0fb5\u0f7a\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("k+Sho+e", "\u0f40\u0fb5\u0f7a\u0f7c");
        ewts2uni_test("k+Shu+A+i+o+e", "\u0f40\u0fb5\u0f71\u0f74\u0f72\u0f7a\u0f7c");
        ewts2uni_test("k+Shu+A+i+o+eHM", "\u0f40\u0fb5\u0f71\u0f74\u0f72\u0f7a\u0f7c\u0f7e\u0f7f");
        ewts2uni_test("k+Shu+A", "\u0f40\u0fb5\u0f71\u0f74");

        ewts2uni_test("k+Sh", "\u0f40\u0fb5");
        ewts2uni_test("k+Sha", "\u0f40\u0fb5");

        just_ewts2uni_test("k+Sh+r-i", "\u0f40\u0fb5\u0fb2\u0f80");
        just_ewts2uni_test("k+Sh+r-I", "\u0f40\u0fb5\u0fb2\u0f71\u0f80");
        ewts2uni_test("k+Sh+l-i", "\u0f40\u0fb5\u0fb3\u0f80");
        ewts2uni_test("k+Sh+l-I", "\u0f40\u0fb5\u0fb3\u0f71\u0f80");
    }

    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop phyw (U+0F55,0FB1,0FAD) is
     *  correct. */
    public void test__EWTS__wowels_on_phyw() {
        ewts2uni_test("phywA", "\u0f55\u0fb1\u0fad\u0f71");
        ewts2uni_test("phywi", "\u0f55\u0fb1\u0fad\u0f72");
        ewts2uni_test("phywI", "\u0f55\u0fb1\u0fad\u0f71\u0f72");
        ewts2uni_test("phywu", "\u0f55\u0fb1\u0fad\u0f74");
        ewts2uni_test("phywU", "\u0f55\u0fb1\u0fad\u0f71\u0f74");
        ewts2uni_test("phywe", "\u0f55\u0fb1\u0fad\u0f7a");
        ewts2uni_test("phywai", "\u0f55\u0fb1\u0fad\u0f7b");
        ewts2uni_test("phywo", "\u0f55\u0fb1\u0fad\u0f7c");
        ewts2uni_test("phywau", "\u0f55\u0fb1\u0fad\u0f7d");
        ewts2uni_test("phyw-i", "\u0f55\u0fb1\u0fad\u0f80");
        ewts2uni_test("phyw-I", "\u0f55\u0fb1\u0fad\u0f71\u0f80");
        ewts2uni_test("phyw\\u0f86", "\u0f55\u0fb1\u0fad\u0f86");
        assertEquals(EWTSTraits.instance().getUnicodeForWowel("\u0f86+\u0f84"), "\u0f86\u0f84");

        ewts2uni_test("phyw\\u0f84\\u0f86", "\u0f55\u0fb1\u0fad\u0f86\u0f84");
        ewts2uni_test("phyw\\u0f84\u0f86", "\u0f55\u0fb1\u0fad\u0f86\u0f84");
        ewts2uni_test("phywa\\u0f86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u0f86\u0f84", "\u0f55\u0fb1\u0fad\u0f86\u0f84");
        ewts2uni_test("phywa\\U0f86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\U0F86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u0F86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u00000f86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u00000f86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u00000F86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u00000F86", "\u0f55\u0fb1\u0fad\u0f86");
        ewts2uni_test("phywa\\u0f87", "\u0f55\u0fb1\u0fad\u0f87");


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        ewts2uni_test("phywA+i", "\u0f55\u0fb1\u0fad\u0f71\u0f72");
        ewts2uni_test("phywo+o", "\u0f55\u0fb1\u0fad\u0f7c\u0f7c");
        ewts2uni_test("phywe+e", "\u0f55\u0fb1\u0fad\u0f7a\u0f7a");
        ewts2uni_test("phywe+e+e", "\u0f55\u0fb1\u0fad\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("phywe+e+e+e", "\u0f55\u0fb1\u0fad\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("phywe+e+e+e+e", "\u0f55\u0fb1\u0fad\u0f7a\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        ewts2uni_test("phywo+e", "\u0f55\u0fb1\u0fad\u0f7a\u0f7c");
        ewts2uni_test("phywu+A+i+o+e", "\u0f55\u0fb1\u0fad\u0f71\u0f74\u0f72\u0f7a\u0f7c");
        ewts2uni_test("phywu+A+i+o+eHM", "\u0f55\u0fb1\u0fad\u0f71\u0f74\u0f72\u0f7a\u0f7c\u0f7e\u0f7f");
        ewts2uni_test("phywu+A", "\u0f55\u0fb1\u0fad\u0f71\u0f74");

        ewts2uni_test("phyw", "\u0f55\u0fb1\u0fad");
        ewts2uni_test("phywa", "\u0f55\u0fb1\u0fad");

        ewts2uni_test("phywaM", "\u0f55\u0fb1\u0fad\u0f7e"); /* TODO(DLC)[EWTS->Tibetan]: NOW: aM is not a wowel! */ // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywaH", "\u0f55\u0fb1\u0fad\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywa~M`", "\u0f55\u0fb1\u0fad\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywa~M", "\u0f55\u0fb1\u0fad\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywa?", "\u0f55\u0fb1\u0fad\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywaMH", "\u0f55\u0fb1\u0fad\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        ewts2uni_test("phywaHM", "\u0f55\u0fb1\u0fad\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say

        assert_EWTS_error("phywr-i");
        assert_EWTS_error("phyw+r-i");
        assert_EWTS_error("phyw+l-i");
    }

    /** Tests that our implementation of EWTS's wowels are correct,
     *  mostly by testing that the Unicode generated for a single
     *  wowel or set of wowels atop k+j+j+k+k+j
     *  (U+0F40,U+0F97,U+0F97,U+0F90,U+0F90,U+0F97) is correct.  I
     *  chose this stack as an example of an absurd stack. */
    public void test__EWTS__wowels_on_kjjkkj() {
        just_ewts2uni_test("k+j+j+k+k+jA", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f71");
        just_ewts2uni_test("k+j+j+k+k+ji", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f72");
        just_ewts2uni_test("k+j+j+k+k+jI", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f71\u0f72");
        just_ewts2uni_test("k+j+j+k+k+ju", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f74");
        just_ewts2uni_test("k+j+j+k+k+jU", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f71\u0f74");
        just_ewts2uni_test("k+j+j+k+k+je", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7a");
        just_ewts2uni_test("k+j+j+k+k+jai", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7b");
        just_ewts2uni_test("k+j+j+k+k+jo", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7c");
        just_ewts2uni_test("k+j+j+k+k+jau", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7d");
        just_ewts2uni_test("k+j+j+k+k+jaM", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7e"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        just_ewts2uni_test("k+j+j+k+k+jaH", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        just_ewts2uni_test("k+j+j+k+k+j-i", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f80");
        just_ewts2uni_test("k+j+j+k+k+j-I", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f71\u0f80");
        just_ewts2uni_test("k+j+j+k+k+ja~M`", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f82"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        just_ewts2uni_test("k+j+j+k+k+ja~M", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f83"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        just_ewts2uni_test("k+j+j+k+k+ja?", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f84"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        just_ewts2uni_test("k+j+j+k+k+ja\\u0f86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        just_ewts2uni_test("k+j+j+k+k+ja\\U0f86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        just_ewts2uni_test("k+j+j+k+k+ja\\U0F86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        just_ewts2uni_test("k+j+j+k+k+ja\\u0F86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        just_ewts2uni_test("k+j+j+k+k+ja\\u00000f86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        just_ewts2uni_test("k+j+j+k+k+ja\\u00000f86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        just_ewts2uni_test("k+j+j+k+k+ja\\u00000F86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        just_ewts2uni_test("k+j+j+k+k+ja\\u00000F86", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f86");
        just_ewts2uni_test("k+j+j+k+k+ja\\u0f87", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f87");

        just_ewts2uni_test("k+j+j+k+k+jaMH", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say
        just_ewts2uni_test("k+j+j+k+k+jaHM", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7e\u0f7f"); // TODO(DLC)[EWTS->Tibetan]: than needs to say


        // Than's e-mails of Aug 10 and Aug 11, 2004 say that A+i is
        // the same as I and o+o is the same as au.
        just_ewts2uni_test("k+j+j+k+k+jA+i", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f71\u0f72");
        just_ewts2uni_test("k+j+j+k+k+jo+o", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7c\u0f7c");
        just_ewts2uni_test("k+j+j+k+k+je+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7a\u0f7a");
        just_ewts2uni_test("k+j+j+k+k+je+e+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        just_ewts2uni_test("k+j+j+k+k+je+e+e+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        just_ewts2uni_test("k+j+j+k+k+je+e+e+e+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7a\u0f7a\u0f7a\u0f7a\u0f7a"); // TODO(DLC)[EWTS->Tibetan]:?
        just_ewts2uni_test("k+j+j+k+k+jo+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f7a\u0f7c");
        just_ewts2uni_test("k+j+j+k+k+ju+A+i+o+e", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f71\u0f74\u0f72\u0f7a\u0f7c");
        just_ewts2uni_test("k+j+j+k+k+ju+A+i+o+eHM", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f71\u0f74\u0f72\u0f7a\u0f7c\u0f7e\u0f7f");
        just_ewts2uni_test("k+j+j+k+k+ju+A", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0f71\u0f74");

        just_ewts2uni_test("k+j+j+k+k+j", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97");
        just_ewts2uni_test("k+j+j+k+k+ja", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97");
        just_ewts2uni_test("k+j+j+k+k+j+r-i", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0fb2\u0f80");
        just_ewts2uni_test("k+j+j+k+k+j+r-I", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0fb2\u0f71\u0f80");
        just_ewts2uni_test("k+j+j+k+k+j+l-i", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0fb3\u0f80");
        just_ewts2uni_test("k+j+j+k+k+j+l-I", "\u0f40\u0f97\u0f97\u0f90\u0f90\u0f97\u0fb3\u0f71\u0f80");
    }

    /** Tests that the EWTS that the spec says corresponds to each
     *  codepoint really does. */
    public void test__EWTS__tags_each_unicode_value() {
        if (RUN_FAILING_TESTS) {
            ewts2uni_test("\\u0ef0", "\u0ef0");
            for (char i = '\u0ef0'; i < '\u1010'; i++) {
                // invalid codepoint like U+0F48?  No problem!  TODO(DLC)[EWTS->Tibetan]: NOTE: use a unicode "spell checker" to find such problems
                String s = new String(new char[] { i });
                ewts2uni_test(UnicodeUtils.unicodeStringToPrettyString(s), s);
                ewts2uni_test("\\" + UnicodeUtils.unicodeStringToPrettyString(s), s);
            }
            ewts2uni_test("\\u0000", "\u0000");
            ewts2uni_test("\\u0eff", "\u0eff");
        }
        just_ewts2uni_test("\\u0f00", "\u0f00"); // TODO(DLC)[EWTS->Tibetan]: ewts->tmw is broken
        just_ewts2uni_test("\\u0F02", "\u0F02"); // TODO(DLC)[EWTS->Tibetan]: ewts->tmw is broken
        just_ewts2uni_test("\\u0F03", "\u0F03"); // TODO(DLC)[EWTS->Tibetan]: ewts->tmw is broken
        just_ewts2uni_test("\\u0f40", "\u0f40");
        if (RUN_FAILING_TESTS) {
            assert_EWTS_error("\\u0f70"); // reserved codepoint
            assert_EWTS_error("\\u0fff"); // reserved codepoint
            just_ewts2uni_test("\\uf000", "\uf000");
            just_ewts2uni_test("\\uf01f", "\uf01f");
            just_ewts2uni_test("\\uefff", "\uefff");
        }

        ewts2uni_test("kaHH", "\u0F40\u0f7f\u0f7f");

        // Below was semiautomatically generated from the EWTS spec's
        // 'ewts.xml' representation (early August 2004 edition):
        ewts2uni_test("v", "\u0F56\u0F39");
        ewts2uni_test("f", "\u0F55\u0F39");
        ewts2uni_test("\u0f88+ka", "\u0f88\u0f90");
        ewts2uni_test("\u0f88+kha", "\u0f88\u0f91");
        ewts2uni_test("\\u0f88+ka", "\u0f88\u0f90");
        ewts2uni_test("\\u0f88+kha", "\u0f88\u0f91");
        ewts2uni_test("oM",
                      false ? "\u0F00" : "\u0f68\u0f7c\u0f7e");  // TODO(DLC)[EWTS->Tibetan]: which is correct?  see e-mail (maybe it was cfynn who thought \u0F00 ought not be generated?
        ewts2uni_test("\\u0F01", "\u0F01");
        ewts2uni_test("@", "\u0F04");
        ewts2uni_test("#", "\u0F05");  // TODO(DLC)[EWTS->Tibetan]: warning/error?  [#] alone is nonsense.
        ewts2uni_test("$", "\u0F06");
        ewts2uni_test("%", "\u0F07");
        ewts2uni_test("!", "\u0F08");
        ewts2uni_test("\\u0F09", "\u0F09");
        ewts2uni_test("\\u0F0A", "\u0F0A");
        ewts2uni_test(" ", "\u0F0B");
        ewts2uni_test("*", "\u0F0C");
        ewts2uni_test("/", "\u0F0D");
        ewts2uni_test("//", "\u0F0E");
        ewts2uni_test("////", "\u0F0E\u0f0e");
        ewts2uni_test("/////", "\u0F0E\u0f0e\u0f0d");
        ewts2uni_test(";", "\u0F0F");
        ewts2uni_test("\\u0F10", "\u0F10");
        ewts2uni_test("|", "\u0F11");
        ewts2uni_test("\\u0F12", "\u0F12");
        ewts2uni_test("\\u0F13", "\u0F13");
        ewts2uni_test(":", "\u0F14");
        ewts2uni_test("\\u0F15", "\u0F15");
        ewts2uni_test("\\u0F16", "\u0F16");
        ewts2uni_test("\\u0F17", "\u0F17");
        if (RUN_FAILING_TESTS) ewts2uni_test("\\u0F18", "\u0F18"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        if (RUN_FAILING_TESTS) ewts2uni_test("\\u0F19", "\u0F19"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        ewts2uni_test("\\u0F1A", "\u0F1A");
        ewts2uni_test("\\u0F1B", "\u0F1B");
        ewts2uni_test("\\u0F1C", "\u0F1C");
        ewts2uni_test("\\u0F1D", "\u0F1D");
        ewts2uni_test("\\u0F1E", "\u0F1E");
        ewts2uni_test("\\u0F1F", "\u0F1F");
        ewts2uni_test("0", "\u0F20");
        ewts2uni_test("1", "\u0F21");
        ewts2uni_test("2", "\u0F22");
        ewts2uni_test("3", "\u0F23");
        ewts2uni_test("4", "\u0F24");
        ewts2uni_test("5", "\u0F25");
        ewts2uni_test("6", "\u0F26");
        ewts2uni_test("7", "\u0F27");
        ewts2uni_test("8", "\u0F28");
        ewts2uni_test("9", "\u0F29");
        ewts2uni_test("\\u0F2A", "\u0F2A");
        ewts2uni_test("\\u0F2B", "\u0F2B");
        ewts2uni_test("\\u0F2C", "\u0F2C");
        ewts2uni_test("\\u0F2D", "\u0F2D");
        ewts2uni_test("\\u0F2E", "\u0F2E");
        ewts2uni_test("\\u0F2F", "\u0F2F");
        ewts2uni_test("\\u0F30", "\u0F30");
        ewts2uni_test("\\u0F31", "\u0F31");
        ewts2uni_test("\\u0F32", "\u0F32");
        ewts2uni_test("\\u0F33", "\u0F33");
        ewts2uni_test("=", "\u0F34");
        if (RUN_FAILING_TESTS) ewts2uni_test("~X", "\u0F35");
        ewts2uni_test("\\u0F36", "\u0F36");
        if (RUN_FAILING_TESTS) ewts2uni_test("X", "\u0F37"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        ewts2uni_test("\\u0F38", "\u0F38");
        if (RUN_FAILING_TESTS) assert_EWTS_error("^");  // If you want \u0f68\u0f39, use [a^]
        ewts2uni_test("<", "\u0F3A");
        ewts2uni_test(">", "\u0F3B");
        ewts2uni_test("(", "\u0F3C");
        ewts2uni_test(")", "\u0F3D");
        if (RUN_FAILING_TESTS) ewts2uni_test("\\u0F3E", "\u0F3E"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        if (RUN_FAILING_TESTS) ewts2uni_test("\\u0F3F", "\u0F3F"); // TODO(DLC)[EWTS->Tibetan]: error combiner
        ewts2uni_test("k", "\u0F40");
        ewts2uni_test("kh", "\u0F41");
        ewts2uni_test("g", "\u0F42");
        ewts2uni_test("g+h", false ? "\u0F43" : "\u0f42\u0fb7");  // TODO(DLC)[EWTS->Tibetan]: either is acceptable, yes?
        ewts2uni_test("ng", "\u0F44");
        ewts2uni_test("c", "\u0F45");
        ewts2uni_test("ch", "\u0F46");
        ewts2uni_test("j", "\u0F47");
        ewts2uni_test("ny", "\u0F49");
        ewts2uni_test("T", "\u0F4A");
        ewts2uni_test("Th", "\u0F4B");
        ewts2uni_test("D", "\u0F4C");
        ewts2uni_test("D+h", false ? "\u0F4D" : "\u0f4c\u0fb7");  // TODO(DLC)[EWTS->Tibetan]: either is acceptable, yes?
        ewts2uni_test("N", "\u0F4E");
        ewts2uni_test("t", "\u0F4F");
        ewts2uni_test("th", "\u0F50");
        ewts2uni_test("d", "\u0F51");
        ewts2uni_test("d+h", false ? "\u0F52" : "\u0f51\u0fb7");  // TODO(DLC)[EWTS->Tibetan]: either is acceptable, yes?
        ewts2uni_test("n", "\u0F53");
        ewts2uni_test("p", "\u0F54");
        ewts2uni_test("ph", "\u0F55");
        ewts2uni_test("b", "\u0F56");
        ewts2uni_test("b+h", false ? "\u0F57" : "\u0f56\u0fb7");  // TODO(DLC)[EWTS->Tibetan]: either is acceptable, yes?
        ewts2uni_test("m", "\u0F58");
        ewts2uni_test("ts", "\u0F59");
        ewts2uni_test("tsh", "\u0F5A");
        ewts2uni_test("dz", "\u0F5B");
        ewts2uni_test("dz+h", false ? "\u0F5C" : "\u0f5b\u0fb7");  // TODO(DLC)[EWTS->Tibetan]: either is acceptable, yes?
        ewts2uni_test("w", "\u0F5D");
        ewts2uni_test("zh", "\u0F5E");
        ewts2uni_test("z", "\u0F5F");
        ewts2uni_test("'", "\u0F60");
        ewts2uni_test("y", "\u0F61");
        ewts2uni_test("r", "\u0F62");
        ewts2uni_test("l", "\u0F63");
        ewts2uni_test("sh", "\u0F64");
        ewts2uni_test("Sh", "\u0F65");
        ewts2uni_test("s", "\u0F66");
        ewts2uni_test("h", "\u0F67");
        ewts2uni_test("a", "\u0F68");
        ewts2uni_test("k+Sh", "\u0f40\u0fb5"); // there is no way in EWTS to specify \u0f69 in particular without using \\u0f69
        if (RUN_FAILING_TESTS) ewts2uni_test("R+", "\u0F6A"); // TODO(DLC)[EWTS->Tibetan]: move to illegal test
        final String achen = "\u0f68";  // TODO(DLC)[EWTS->Tibetan]: "i" is "\u0f68\u0f72" for sure, but must you say [aA] instead of [A] to get "\u0f68\u0f71"?  What about [?], [&], [~M`]?  Every place this variable is used, please consider.
        ewts2uni_test("A", achen + "\u0F71");
        ewts2uni_test("i", achen + "\u0F72");
        ewts2uni_test("I", achen + "\u0F71\u0F72");
        ewts2uni_test("u", achen + "\u0F74");
        ewts2uni_test("U", achen + "\u0F71\u0F74");
        ewts2uni_test("a+r-i", achen + "\u0fb2\u0f80");  // not 0F76, which is discouraged by the Unicode standard
        ewts2uni_test("a+r-I", achen + "\u0fb2\u0f71\u0f80");  // not 0F77, which is discouraged by the Unicode standard
        just_ewts2uni_test("a+l-i", achen + "\u0fb3\u0f80");  // not 0F78, which is discouraged by the Unicode standard
        just_ewts2uni_test("a+l-I", achen + "\u0fb3\u0f71\u0f80");  // not 0F79, which is discouraged by the Unicode standard
        ewts2uni_test("e", achen + "\u0F7A");
        ewts2uni_test("ai", achen + "\u0F7B");
        ewts2uni_test("o", achen + "\u0F7C");
        ewts2uni_test("au", achen + "\u0F7D");
        ewts2uni_test("M", achen + "\u0F7E");
        ewts2uni_test("H", achen + "\u0F7F");
        ewts2uni_test("-i", achen + "\u0F80");
        ewts2uni_test("-I", achen + "\u0F71\u0F80");
        ewts2uni_test("~M`", achen + "\u0F82");
        ewts2uni_test("~M", achen + "\u0F83");
        ewts2uni_test("?", achen + "\u0F84");  // \u0f84 is a combiner
        ewts2uni_test("&", "\u0F85");  // I'm pretty sure this should be without achen.
        ewts2uni_test("\\u0F86", achen + "\u0F86");
        ewts2uni_test("\\u0F87", achen + "\u0F87");  // \u0f87 is a combiner
        ewts2uni_test("\\u0F88", "\u0F88");
        ewts2uni_test("\\u0F89", "\u0F89");
        ewts2uni_test("\\u0F8A", "\u0F8A");
        ewts2uni_test("\\u0F8B", "\u0F8B");

        final String ewts_for_superscript = "r+";
        final String unicode_for_superscript = "\u0f62";
        ewts2uni_test(ewts_for_superscript + "k",
                      unicode_for_superscript + "\u0F90");
        ewts2uni_test(ewts_for_superscript + "kh",
                      unicode_for_superscript + "\u0F91");
        ewts2uni_test(ewts_for_superscript + "g",
                      unicode_for_superscript + "\u0F92");
        ewts2uni_test(ewts_for_superscript + "g+h",
                      unicode_for_superscript
                      + (false ? "\u0F93" : "\u0f92\u0fb7"));
        ewts2uni_test(ewts_for_superscript + "ng",
                      unicode_for_superscript + "\u0F94");
        just_ewts2uni_test(ewts_for_superscript + "c",
                           unicode_for_superscript + "\u0F95");
        just_ewts2uni_test(ewts_for_superscript + "ch",
                           unicode_for_superscript + "\u0F96");
        ewts2uni_test(ewts_for_superscript + "j",
                      unicode_for_superscript + "\u0F97");
        ewts2uni_test(ewts_for_superscript + "ny",
                      unicode_for_superscript + "\u0F99");
        ewts2uni_test(ewts_for_superscript + "T",
                      unicode_for_superscript + "\u0F9A");
        ewts2uni_test(ewts_for_superscript + "Th",
                      unicode_for_superscript + "\u0F9B");
        ewts2uni_test(ewts_for_superscript + "D",
                      unicode_for_superscript + "\u0F9C");
        just_ewts2uni_test(ewts_for_superscript + "D+h",
                           unicode_for_superscript
                           + (false ? "\u0F9D" : "\u0f9c\u0fb7"));
        ewts2uni_test(ewts_for_superscript + "N",
                      unicode_for_superscript + "\u0F9E");
        ewts2uni_test(ewts_for_superscript + "t",
                      unicode_for_superscript + "\u0F9F");
        ewts2uni_test(ewts_for_superscript + "th",
                      unicode_for_superscript + "\u0FA0");
        ewts2uni_test(ewts_for_superscript + "d",
                      unicode_for_superscript + "\u0FA1");
        ewts2uni_test(ewts_for_superscript + "d+h",
                      unicode_for_superscript
                      + (false ? "\u0FA2" : "\u0fa1\u0fb7"));
        ewts2uni_test(ewts_for_superscript + "n",
                      unicode_for_superscript + "\u0FA3");
        ewts2uni_test(ewts_for_superscript + "p",
                      unicode_for_superscript + "\u0FA4");
        just_ewts2uni_test(ewts_for_superscript + "ph",
                           unicode_for_superscript + "\u0FA5");
        ewts2uni_test(ewts_for_superscript + "b",
                      unicode_for_superscript + "\u0FA6");
        ewts2uni_test(ewts_for_superscript + "b+h",
                      unicode_for_superscript
                      + (false ? "\u0FA7" : "\u0fa6\u0fb7"));
        ewts2uni_test(ewts_for_superscript + "m",
                      unicode_for_superscript + "\u0FA8");
        ewts2uni_test(ewts_for_superscript + "ts",
                      unicode_for_superscript + "\u0FA9");
        ewts2uni_test(ewts_for_superscript + "tsh",
                      unicode_for_superscript + "\u0FAA");
        ewts2uni_test(ewts_for_superscript + "dz",
                      unicode_for_superscript + "\u0FAB");
        just_ewts2uni_test(ewts_for_superscript + "dz+h",
                           unicode_for_superscript
                           + (false ? "\u0FAC" : "\u0fab\u0fb7"));
        ewts2uni_test(ewts_for_superscript + "w",
                      unicode_for_superscript + "\u0FAD");
        just_ewts2uni_test(ewts_for_superscript + "zh",
                           unicode_for_superscript + "\u0FAE");
        just_ewts2uni_test(ewts_for_superscript + "z",
                           unicode_for_superscript + "\u0FAF");
        just_ewts2uni_test(ewts_for_superscript + "'",
                           unicode_for_superscript + "\u0FB0");
        just_ewts2uni_test(ewts_for_superscript + "y",
                           unicode_for_superscript + "\u0FB1");
        just_ewts2uni_test(ewts_for_superscript + "r",
                           unicode_for_superscript + "\u0FB2");
        ewts2uni_test(ewts_for_superscript + "l",
                      unicode_for_superscript + "\u0FB3");
        just_ewts2uni_test(ewts_for_superscript + "sh",
                           unicode_for_superscript + "\u0FB4");
        just_ewts2uni_test(ewts_for_superscript + "Sh",
                           unicode_for_superscript + "\u0FB5");
        just_ewts2uni_test(ewts_for_superscript + "s",
                           unicode_for_superscript + "\u0FB6");
        ewts2uni_test(ewts_for_superscript + "h",
                      unicode_for_superscript + "\u0FB7");
        just_ewts2uni_test(ewts_for_superscript + "a",
                           unicode_for_superscript + "\u0FB8");
        ewts2uni_test(ewts_for_superscript + "k+Sh",
                      unicode_for_superscript
                      + (false ? "\u0FB9" : "\u0f90\u0fb5"));
        just_ewts2uni_test(ewts_for_superscript + "W",
                           unicode_for_superscript + "\u0FBA");
        just_ewts2uni_test(ewts_for_superscript + "Y",
                           unicode_for_superscript + "\u0FBB");
        just_ewts2uni_test(ewts_for_superscript + "R",
                           unicode_for_superscript + "\u0FBC");

        just_ewts2uni_test("\\u0FBE", "\u0FBE");
        just_ewts2uni_test("\\u0FBF", "\u0FBF");
        just_ewts2uni_test("\\u0FC0", "\u0FC0");
        just_ewts2uni_test("\\u0FC1", "\u0FC1");
        just_ewts2uni_test("\\u0FC2", "\u0FC2");
        just_ewts2uni_test("\\u0FC3", "\u0FC3");
        just_ewts2uni_test("\\u0FC4", "\u0FC4");
        just_ewts2uni_test("\\u0FC5", "\u0FC5");
        just_ewts2uni_test("\\u0FC6", achen + "\u0FC6");  // \u0fc6 is a combiner
        just_ewts2uni_test("\\u0FC7", "\u0FC7");
        just_ewts2uni_test("\\u0FC8", "\u0FC8");
        just_ewts2uni_test("\\u0FC9", "\u0FC9");
        just_ewts2uni_test("\\u0FCA", "\u0FCA");
        just_ewts2uni_test("\\u0FCB", "\u0FCB");
        just_ewts2uni_test("\\u0FCC", "\u0FCC");
        just_ewts2uni_test("\\u0FCF", "\u0FCF");
        just_ewts2uni_test("\\u0FD0", "\u0FD0");
        just_ewts2uni_test("\\u0FD1", "\u0FD1");
        ewts2uni_test("_", "\u00a0");  // tibwn.ini says that the Unicode spec wants a non-breaking space.
        ewts2uni_test("\\u534D", "\u534D");
        ewts2uni_test("\\u5350", "\u5350");
        ewts2uni_test("\u534D", "\u534D");
        ewts2uni_test("\u5350", "\u5350");
        ewts2uni_test("\\u0F88+k", "\u0F88\u0F90");
        ewts2uni_test("\\u0F88+kh", "\u0F88\u0F91");
        /* TODO(DLC)[EWTS->Tibetan]:

           Do we want to ever generate \uf021? (NOT \u0f21, but the
           private-use area (PUA) of Unicode).  EWTS->TMW and this
           makes sense, but EWTS->Unicode?  Shouldn't we match the
           behavior of TMW->Unicode, regardless?  */
        just_ewts2uni_test("\\uF021", "\uF021");
        just_ewts2uni_test("\\uF022", "\uF022");
        just_ewts2uni_test("\\uF023", "\uF023");
        just_ewts2uni_test("\\uF024", "\uF024");
        just_ewts2uni_test("\\uF025", "\uF025");
        just_ewts2uni_test("\\uF026", "\uF026");
        just_ewts2uni_test("\\uF027", "\uF027");
        just_ewts2uni_test("\\uF028", "\uF028");
        just_ewts2uni_test("\\uF029", "\uF029");
        just_ewts2uni_test("\\uF02A", "\uF02A");
        just_ewts2uni_test("\\uF02B", "\uF02B");
        just_ewts2uni_test("\\uF02C", "\uF02C");
        just_ewts2uni_test("\\uF02D", "\uF02D");
        just_ewts2uni_test("\\uF02E", "\uF02E");
        just_ewts2uni_test("\\uF02F", "\uF02F");
        just_ewts2uni_test("\\uF030", "\uF030");
        just_ewts2uni_test("\\uF031", "\uF031");
        just_ewts2uni_test("\\uF032", "\uF032");
        just_ewts2uni_test("\\uF033", "\uF033");
        just_ewts2uni_test("\\uF034", "\uF034");
        just_ewts2uni_test("\\uF035", "\uF035");
        just_ewts2uni_test("\\uF036", "\uF036");
        just_ewts2uni_test("\\uF037", "\uF037");
        just_ewts2uni_test("\\uF038", "\uF038");
        just_ewts2uni_test("\\uF039", "\uF039");
        just_ewts2uni_test("\\uF03A", "\uF03A");
        just_ewts2uni_test("\\uF03B", "\uF03B");
        just_ewts2uni_test("\\uF03C", "\uF03C");
        just_ewts2uni_test("\\uF03D", "\uF03D");
        just_ewts2uni_test("\\uF03E", "\uF03E");
        just_ewts2uni_test("\\uF03F", "\uF03F");
        just_ewts2uni_test("\\uF040", "\uF040");
        just_ewts2uni_test("\\uF041", "\uF041");
        just_ewts2uni_test("\\uF042", "\uF042");
    }
    
    public void test__EWTS__long_wowels() {
        ewts2uni_test("k-I~M`~X", "\u0f40\u0f71\u0f80\u0f82\u0f35"); // TODO(DLC)[EWTS->Tibetan]: actually the 0f68 stuff could be true... ask
    }

    public void test__EWTS__32bit_unicode_escapes() {
        assert_EWTS_error("\\u00010000"); // TODO(dchandler): make it work
        just_ewts2uni_test("\\uF0010000",
                           "[#ERROR 116: Found an illegal character, '\\', with ordinal (in decimal) 92.]\u0f68\u0f74[#ERROR 116: Found an illegal character, 'F', with ordinal (in decimal) 70.]\u0f20\u0f20\u0f21\u0f20\u0f20\u0f20\u0f20");
        // TODO(DLC)[EWTS->Tibetan]: make the following work:
        assert_EWTS_error("\\uF0010000");
        if (RUN_FAILING_TESTS) {
        just_ewts2uni_test("\\ucafe0000",
                           "[#ERROR Sorry, we don't yet support Unicode escape sequences above 0x0000FFFF!  File a bug.]");
        // TODO(dchandler): make it "\ucafe0000");
        ewts2uni_test("\\ucafe0eff", "\ucafe0eff");
        ewts2uni_test("\\ucafe0eff", "\ucafe0eff");
        ewts2uni_test("\\ucafe0f00", "\ucafe0f00");
        ewts2uni_test("\\ucafe0f40", "\ucafe0f40");
        ewts2uni_test("\\ucafe0f70", "\ucafe0f70");
        ewts2uni_test("\\ucafe0fff", "\ucafe0fff");
        ewts2uni_test("\\ucafef000", "\ucafef000");
        ewts2uni_test("\\ucafef01f", "\ucafef01f");
        ewts2uni_test("\\ucafeefff", "\ucafeefff");
        
        ewts2uni_test("\\uffffffff", "\uffffffff");
        ewts2uni_test("\\ueeeeeee2", "\ueeeeeee2");

        ewts2uni_test("\\u00000000", "\u00000000");
        ewts2uni_test("\\u00000eff", "\u00000eff");
        ewts2uni_test("\\u00000eff", "\u00000eff");
        }
        if (RUN_FAILING_TESTS) {
            assertEquals("\u0f00", "\u00000f00");  // TODO(DLC)[EWTS->Tibetan]: this is why other test cases are failing.  I think these tests rely on java 5.0 features (a.k.a., Tiger, 1.5) -- see http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
            ewts2uni_test("\\u00000f00", "\u00000f00");
            ewts2uni_test("\\u00000f40", "\u00000f40");
            ewts2uni_test("\\u00000f70", "\u00000f70");
            ewts2uni_test("\\u00000fff", "\u00000fff");
            ewts2uni_test("\\u0000f000", "\u0000f000");
            ewts2uni_test("\\u0000f01f", "\u0000f01f");
            ewts2uni_test("\\u0000efff", "\u0000efff");

            ewts2uni_test("\\u00000000", "\u0000");
            ewts2uni_test("\\u00000eff", "\u0eff");
        }
        just_ewts2uni_test("\\u00000f00", "\u0f00");  // TODO(DLC)[EWTS->Tibetan]: EWTS->TMW is broken for this
        just_ewts2uni_test("\\u00000f40", "\u0f40");
        if (RUN_FAILING_TESTS) {
            ewts2uni_test("\\u00000f70", "\u0f70");
            ewts2uni_test("\\u00000fff", "\u0fff");
            ewts2uni_test("\\u0000f000", "\uf000");
            ewts2uni_test("\\u0000f01f", "\uf01f");
            ewts2uni_test("\\u0000efff", "\uefff");
        }

        assert_EWTS_error("\\UcaFe0000");
        if (RUN_FAILING_TESTS) { // TODO(dchandler): make these work
            ewts2uni_test("\\UcaFe0000", "\ucaFe0000");
            ewts2uni_test("\\UcaFe0eff", "\ucaFe0eff");
            ewts2uni_test("\\UcaFe0eff", "\ucaFe0eff");
            ewts2uni_test("\\UcaFe0f00", "\ucaFe0f00");
            ewts2uni_test("\\UcaFe0f40", "\ucaFe0f40");
            ewts2uni_test("\\UcaFe0f70", "\ucaFe0f70");
            ewts2uni_test("\\UcaFe0fff", "\ucaFe0fff");
            ewts2uni_test("\\UcaFef000", "\ucaFef000");
            ewts2uni_test("\\UcaFef01f", "\ucaFef01f");
            ewts2uni_test("\\UcaFeefff", "\ucaFeefff");
        }

    }

    // TODO(DLC)[EWTS->Tibetan]: test that "\[JAVA_SOURCE_WILL_NOT_COMPILE_WITHOUT_ME]uxxxx " works out well

    /** Tests that certain strings are not legal EWTS. */
    public void test__EWTS__illegal_things() {
        assert_EWTS_error("m+");

        assert_EWTS_error("kSha"); // use "k+Sha" instead

        ewts2uni_test("pM", "\u0f54\u0f7e");  // TODO(DLC)[EWTS->Tibetan]: should this be an EWTS error, forcing the use of "paM" instead?
        ewts2uni_test("pH", "\u0f54\u0f7f");  // TODO(DLC)[EWTS->Tibetan]: should this be an EWTS error, forcing the use of "paH" instead?
        assert_EWTS_error("kja"); // use "kaja" or "k.ja" instead

        ewts2uni_test("kA+u", "\u0f40\u0f71\u0f74");  // TODO(DLC)[EWTS->Tibetan]: should this be an EWTS error, forcing the use of either "ku+A" (bottom-to-top) or "kU"?


        {
            ewts2uni_test("bsna", "\u0f56\u0f66\u0fa3");  // [bs+na]/[bsna] is legal, but [bna] is not according to prefix rules.
            assert_EWTS_error("bna");  // use "b+na" or "bana" instead, depending on what you mean 
            // TODO(DLC)[EWTS->Tibetan]: tell D. Chapman about this; an old e-mail said my test cases would be brutal and here's brutal
            assert_EWTS_error("bn?");
            assert_EWTS_error("bni");
            assert_EWTS_error("bnA");
            assert_EWTS_error("bn-I");
        }

        if (RUN_FAILING_TESTS) {
            // These should be errors...  a+r is not a standard stack;
            // neither is a+l.  [a.r-i] is how you get
            // \u0f68\u0f62\u0f80, not [ar-i].
            assert_EWTS_error("ar-i");
            assert_EWTS_error("ar-I");
            assert_EWTS_error("al-i");
            assert_EWTS_error("al-I");
        }

        if (RUN_FAILING_TESTS) assert_EWTS_error("g..ya"); // use "g.ya" instead for \u0f42\u0f61
        if (RUN_FAILING_TESTS) assert_EWTS_error("m..");
        if (RUN_FAILING_TESTS) assert_EWTS_error("..m");
        assert_EWTS_error(".");
        if (RUN_FAILING_TESTS) assert_EWTS_error(".ma");
        if (RUN_FAILING_TESTS) assert_EWTS_error("g"); // use "ga" instead.   TODO(DLC)[EWTS->Tibetan]: Really?
        if (RUN_FAILING_TESTS) {
            {  // only numbers combine with f19,f18,f3e,f3f
                assert_EWTS_error("k\\u0f19");
                assert_EWTS_error("k\\u0f18");
                assert_EWTS_error("k\\u0f3e");
                assert_EWTS_error("k\\u0f3f");
            }
        }
    }
    
    public void testDLCFailingNow() { // TODO(DLC)[EWTS->Tibetan]
        if (RUN_FAILING_TESTS) {
            assert_EWTS_error("\\u0f19");
            assert_EWTS_error("\\u0f18");
        }
        assert_EWTS_error("\\u0f19\u0f20"); // wrong order...

        if (RUN_FAILING_TESTS) {
            ewts2uni_test("'a+r-i", "\u0f60\u0fb2\u0f80"); // TODO(DLC)[EWTS->Tibetan]: NOW: prefix rules should make this invalid!
            ewts2uni_test("'a+r-I", "\u0f60\u0fb2\u0f71\u0f80"); 
            ewts2uni_test("'a+l-i", "\u0f60\u0fb3\u0f80");// TODO(DLC)[EWTS->Tibetan]: NOW error handling is CRAP
            ewts2uni_test("'a+l-I", "\u0f60\u0fb3\u0f71\u0f80");
        }

    }

    public void testMoreMiscellany() {
        ewts2uni_test("k+Sh+R-i", "\u0f40\u0fb5\u0fbc\u0f80");

        ewts2uni_test("k\\u0f35", "\u0f40\u0f35");
        ewts2uni_test("k\\u0f72", "\u0f40\u0f72");
        ewts2uni_test("k\\u0f73", "\u0f40\u0f71\u0f72");
        ewts2uni_test("k\\u0f75", "\u0f40\u0f71\u0f74");
        ewts2uni_test("k\\u0f3e", "\u0f40\u0f3e");
        ewts2uni_test("k\\u0f3f", "\u0f40\u0f3f");

        ewts2uni_test("kHai", "\u0f40\u0f7f\u0f68\u0f7b");  // TODO(DLC)[EWTS->Tibetan]: Is this correct?

        ewts2uni_test("r-i", "\u0f62\u0f80");
        ewts2uni_test("r-I", "\u0f62\u0f71\u0f80");
        ewts2uni_test("l-i", "\u0f63\u0f80");
        ewts2uni_test("l-I", "\u0f63\u0f71\u0f80");
        just_ewts2uni_test("ga\u0f0bga ga\\u0F0bga",
                           "\u0f42\u0f0b\u0f42\u0f0b\u0f42\u0f0b\u0f42");
        just_ewts2uni_test("ga\u0f0cga*ga\\u0f0Cga",
                           "\u0f42\u0f0c\u0f42\u0f0c\u0f42\u0f0c\u0f42");
        ewts2uni_test("'jam",
                      "\u0f60\u0f47\u0f58");
        ewts2uni_test("jamX 'jam~X",
                      "\u0f47\u0f58\u0f37\u0f0b\u0f60\u0f47\u0f58\u0f35");
        ewts2uni_test("@#", "\u0f04\u0f05");
        assert_EWTS_error("dzaHsogs");  // TODO(DLC)[EWTS->Tibetan]:  Ask.  If H is punctuation-like then perhaps we need to implement a lexical conversion from H to H<invisible punct>
    }

    /** TODO(DLC)[EWTS->Tibetan]: set this to true and fix the code or
     * the test cases until things are green. */
    private static final boolean RUN_FAILING_TESTS = false;
}

        // TODO(DLC)[EWTS->Tibetan]: if 'k' were illegal, then would you have to say
        // 'ka\u0f84' or would 'k\u0f84' be legal?


        // TODO(DLC)[EWTS->Tibetan]: ask than what's the order, top to bottom, of
        // u,i,o,e,M,A,I,-i,-I,ai,au,etc.?  TODO(DLC)[EWTS->Tibetan]: ANSWER: Basically, there are a few classes -- above, below, both.

        // TODO(DLC)[EWTS->Tibetan]: NOW: write a tool that takes Tibetan Unicode and finds
        // flaws in it.  E.g., if Unicode 4.0 says that
        // \u0f40\u0f7a\u0f74 is illegal (thus \u0f40\u0f74\u0f7a is
        // what you probably intended), have it find \u0f7a\u0f74.
        //
        // TODO(DLC)[EWTS->Tibetan]: and \u0f7f\u0f7e is probably illegal and should be switched?

        // TODO(DLC)[EWTS->Tibetan]: flesh out \[JAVA_SOURCE_WILL_NOT_COMPILE_WITHOUT_ME]u rules in lexing, is it like Java (where in Java source code, escapes are done in a pre-lexing pass)? no, right, \u0060 causes \u0060 in the output...  and \u0f40a is not like ka.  escapes separate tsheg bars as far as lexing is concerned, yes?  But we use them (and only them, i.e. there is no other transliteration available) for some Tibetan Unicode characters, and then ka\[JAVA_SOURCE_WILL_NOT_COMPILE_WITHOUT_ME]u0fXX may need to seem Java-ish, maybe?

        // TODO(DLC)[EWTS->Tibetan]: spell-check ewts spec, puncutation e.g.


        // TODO(DLC)[EWTS->Tibetan]: ask than aM, not M, is legal, what else is like this? ~M`?  0f84?

        // TODO(DLC)[EWTS->Tibetan]: NOW 0f84 ? not a? but ? according to rule n=7
        /* TODO(DLC)[EWTS->Tibetan]: make a method that tests the unicode directly and by going from ewts/acip->tmw->unicode. */

    // TODO(DLC)[EWTS->Tibetan]: s/anyways/anyway/g in ewts spec
    // TODO(DLC)[EWTS->Tibetan]: s/(Spacebar)/(Space)/g
/* TODO(DLC)[EWTS->Tibetan]: in spec, inconsistency:
   <code>0F880F90</code>
        <code>0F880F91</code>
            <code rend="U+0F55 U+0F39">\u0F55\u0F39</code>
            <code rend="U+0F56 U+0F39">\u0F56\u0F39</code>

TODO(DLC)[EWTS->Tibetan]:: also, <equiv>F042</equiv> is inconsistent with <equiv></equiv> for U+0f01.
 */
