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
Library (THDL). Portions created by the THDL are Copyright 2001-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.tshegbar;

/** Tests ValidatingUnicodeReader.
 *  @author David Chandler */
class ValidatingUnicodeReaderTest {
    private static String skyagd = "\u0F66\u0F90\u0FB1\u0F42\u0F51";
    private static String bskyagd = "\u0F56" + skyagd;

    void testValidatingUnicodeReader() {
        // DLC these routines can be slow.
        assertTrue(ValidatingUnicodeReader.isSyntacticallyLegalTibetanUnicode(
                      bskyagd + "\u0F0C"));
        assertTrue(!ValidatingUnicodeReader.isSyntacticallyLegalTibetanUnicode(
                      "\u0F42" + skyagd + "\u0F0C"));
        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      bskyagd + "\u0F0C"));
        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F42" + skyagd + "\u0F0C"));

        assertTrue(ValidatingUnicodeReader.isSyntacticallyLegalTibetanUnicode(
                      bskyagd + "\u0F0C\u0F62\u0F0B" + bskyagd + "\u0F0F"));

        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F6A\u0F0B"));
        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F62\u0F0B"));
        assertTrue(!ValidatingUnicodeReader.isPerfectUnicode(
                      "\u0F6A\u0F0B"));
        assertTrue(ValidatingUnicodeReader.isPerfectUnicode(
                      "\u0F62\u0F0B"));
        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F6A\u0F90\u0F0B"));
        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F62\u0F90\u0F0B"));
        assertTrue(ValidatingUnicodeReader.isPerfectUnicode(
                      "\u0F62\u0F90\u0F0B"));
        assertTrue(!ValidatingUnicodeReader.isPerfectUnicode(
                      "\u0F6A\u0F90\u0F0B"));

        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F43"));
        assertTrue(!ValidatingUnicodeReader.isSyntacticallyLegalTibetanUnicode(
                      "\u0F43"));

        // The Unicode standard states that U+0F8A is always followed
        // by U+0F82.
        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F8A\u0F82"));
        assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F8A"));
        assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F8A\u0F40"));
        assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F8A\u0F83"));

        assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F74"));
        assertTrue(ValidatingUnicodeReader.isPerfectUnicode(
                      "\u0F40\u0F74"));
        assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F90\u0F74"));

        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F40\u0F77"));
        assertTrue(!ValidatingUnicodeReader.isPerfectUnicode(
                      "\u0F40\u0F77"));
        assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F90\u0F77"));

        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F40\u0F90\u0F7F"));
        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F40\u0F90\u0F7F\u0F35"));

        // Test that each singleton (except U+0F8A) in the Tibetan
        // range is legal, and that each combining char and empty
        // codepoint (and also U+0F8A) is illegal alone.
        {
            for (char cp = '\u0F00'; cp <= '\u0F17'; cp++)
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(cp));
            for (char cp = '\u0F1a'; cp <= '\u0F34'; cp++)
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(cp));
            for (char cp = '\u0F3a'; cp <= '\u0F3d'; cp++)
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(cp));
            for (char cp = '\u0F40'; cp <= '\u0F47'; cp++)
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(cp));
            for (char cp = '\u0F49'; cp <= '\u0F6a'; cp++)
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(cp));
            for (char cp = '\u0F88'; cp <= '\u0F89'; cp++)
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(cp));
            for (char cp = '\u0Fbe'; cp <= '\u0Fc5'; cp++)
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(cp));
            for (char cp = '\u0Fc7'; cp <= '\u0Fcc'; cp++)
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(cp));
            assertTrue(ValidatingUnicodeReader.isFullyValidUnicode("\u0F36"));
            assertTrue(ValidatingUnicodeReader.isFullyValidUnicode("\u0F38"));
            assertTrue(ValidatingUnicodeReader.isFullyValidUnicode("\u0F85"));
            assertTrue(ValidatingUnicodeReader.isFullyValidUnicode("\u0F8b"));
            assertTrue(ValidatingUnicodeReader.isFullyValidUnicode("\u0Fcf"));

            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F48"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F6b"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F6c"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F6d"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F6e"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F6f"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F70"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F8c"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F8d"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F8e"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F8f"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0F98"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0Fbd"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0Fcd"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0Fce"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0Fd0"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0Fe4"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0Ff0"));
            assertTrue(!ValidatingUnicodeReader.isFullyValidUnicode("\u0Fff"));
        }

        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                      "\u0F40\u0Fc6"));

        // Test that combining characters that combine with both
        // consonants and digits work.
        {
            String combiningMarks[] = new String[] {
                "\u0F71",
                "\u0F72",
                "\u0F73",
                "\u0F74",
                "\u0F75",
                "\u0F76",
                "\u0F77",
                "\u0F78",
                "\u0F79",
                "\u0F7a",
                "\u0F7b",
                "\u0F7c",
                "\u0F7d",
                "\u0F7e",
                "\u0F7f",
                "\u0F80",
                "\u0F81",
                "\u0F82",
                "\u0F83",
                "\u0F84",
                "\u0F86",
                "\u0F87"
            };
            for (int i = 0; i < combiningMarks.length(); i++) {
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                               "\u0F40" + combiningMarks[i]));
                // DLC have a group that works with both digits and consonants, cuz vowels plus digits is a no go, right?
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                               "\u0F20" + combiningMarks[i]));
                assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
                               "\u0F30" + combiningMarks[i]));
            }
        }

//        DLC;
//        assertTrue(ValidatingUnicodeReader.isFullyValidUnicode(
//                      "\u0F00\u0F00\u0F00\u0F00\u0F00"));
    }

    void testSyntacticallyLegalUnicodeToThdlWylie() {
        assertTrue("bskyagd"
                   .equals(ValidatingUnicodeReader.syntacticallyLegalTibetanUnicodeToThdlWylie(
                      bskyagd)));

        assertTrue("bskyagd bskyagd/"
                   .equals(ValidatingUnicodeReader.syntacticallyLegalTibetanUnicodeToThdlWylie(
                      bskyagd + "\u0F0B" + bskyagd + "\u0F0D")));
    }
}
