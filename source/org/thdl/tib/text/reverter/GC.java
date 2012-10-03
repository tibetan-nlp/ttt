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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.thdl.util.ThdlDebug;
import org.thdl.tib.text.THDLWylieConstants;
import org.thdl.tib.text.tshegbar.UnicodeUtils;
import org.thdl.tib.text.tshegbar.UnicodeCodepointToThdlWylie;

/** Grapheme cluster backed by a String of Unicode.  For the most part
 *  these are <em>combining character sequences</em> as defined by
 *  Unicode, but (U+0F04 U+0F05+) [TODO(dchandler): not yet handled as
 *  a single GC] is an example of a grapheme cluster that is not a
 *  combining character sequence.
 *  @author David Chandler
 */
class GC {
    /** NFTHDL-decomposed Unicode */
    private String nfthdl;

    /** True if valid.  True for digits w/ digit combiners, character
     *  stack plus optional wowels, a standalone mark.  False for
     *  anything else, e.g. "\u0f0b\u0f90". */
    private boolean valid;

    /** Constructor that takes the NFTHDL-decomposed Unicode for the
     *  grapheme cluster. */
    public GC(String nfthdl) {
        setNfthdl(nfthdl);
    }

    /** A regex that matches the NFTHDL Unicode for a consonant stack
     *  with optional wowels. */
    public static String consonantStackRegexString
    = "[\u0f40-\u0f47\u0f49-\u0f6a]"  // base consonant
    +  "[\u0f90-\u0f97\u0f99-\u0fbc\u0f39]*"  // subjoined cons.
    +  "\u0f71?"  // a-chung
    +  "[\u0f72\u0f73\u0f74\u0f7a-\u0f7d\u0f80]*"  // vowel proper
    +  "[\u0f35\u0f37\u0f7e\u0f7f\u0f82-\u0f84"  // wowels
    +   "\u0f86\u0f87\u0fc6]*";

    private static Pattern validGcRegex = Pattern.compile(
            "^"
            // numeric:
            + "([\u0f20-\u0f33][\u0f18\u0f19]*)|"

            // consonant w/ optional wowels:
            + "(" + consonantStackRegexString + ")|"

            // other symbol with optional U+0FC6
            + "([\u0f00-\u0f17\u0f1a-\u0f1f\u0f34\u0f36\u0f38"
            +   "\u0f3a-\u0f3d\u0f85\u0f88-\u0f8b\u0fbe-\u0fc5"
            +   "\u0fc7-\u0fcc\u0fcf-\u0fd1]\u0fc6?)|"

            // other symbol that does not take U+0FC6.
            // TODO(dchandler): include 0f0b etc. in this group?
            + "([ \t\u00a0\n\r]{1,})"  // DLC handling of English... [0-9\\.:a-zA-Z] etc.  what to do?

            + "$");

    private static final boolean debug = false;

    /** Returns NFTHDL-decomposed Unicode representing this grapheme
     *  cluster. */
    private void setNfthdl(String nfthdl) {
        if (debug) {
            System.out.println("debug: GC is "
                               + UnicodeUtils.unicodeStringToPrettyString(nfthdl));
        }
        this.nfthdl = nfthdl;
        ThdlDebug.verify(nfthdl.length() > 0);  // TODO(dchandler): assert only
        if (nfthdl.length() < 1)
            valid = false;
        valid = validGcRegex.matcher(nfthdl).matches();
    }

    /** Returns NFTHDL-decomposed Unicode representing this grapheme
     *  cluster. */
    public String getNfthdl() { return nfthdl; }

    /** Returns true iff ch is a vowel proper, not a wowel */
    private boolean isVowel(char ch) {
        // (We won't see \u0f76 etc. in NFTHDL, but the handling of
        // them is suspect.)
        return ((ch >= '\u0f71' && ch <= '\u0f75')
                || (ch >= '\u0f7a' && ch <= '\u0f7d')
                || (ch >= '\u0f81' && ch <= '\u0f82'));
    }

    private boolean isWowelRequiringPrecedingVowel(char ch) {
        // not 0f39 0f18 0f19 e.g.
        return ("\u0f35\u0f37\u0f7e\u0f7f\u0f82\u0f83\u0f84\u0f86\u0f87".indexOf(ch) >= 0);

        // NOTE: 0f7f is questionable 0fc6 too... we assume [k\\u0fc6]
        // is good EWTS.
    }

    /** Returns EWTS that is valid but not beautiful.  It's better
     *  suited for consumption by computer programs than by humans,
     *  though it'll do in a pinch.  (Humans like to see [rnams] instead
     *  of [r+namasa].)
     *  @return null if this grapheme cluster has no valid EWTS
     *  representation or valid-but-ugly EWTS otherwise */
    public StringBuffer getEwtsForComputers() {
        if (!valid) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        // We use ch after the loop.  Initialization is not really
        // needed; it's just to avoid compiler errors.
        char ch = 'X';
        boolean seenVowel = false;
        String lastEwts = "";
        boolean added_aVOWEL = false;
        for (int i = 0; i < nfthdl.length(); i++) {
            ch = nfthdl.charAt(i);
            String ewts
                = UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(ch);
            if (i + 1 < nfthdl.length()) {  // lookahead
                // Even computers want to see kI because the spec
                // isn't (or at least hasn't always been) crystal
                // clear that kA+i is equivalent to kI.
                if (('\u0f55' == ch || '\u0fa5' == ch)
                    && '\u0f39' == nfthdl.charAt(i + 1)) {
                    ++i;
                    ewts = "f";  // TODO(dchandler): hard-coded EWTS
                } else if (('\u0f56' == ch || '\u0fa6' == ch)
                           && '\u0f39' == nfthdl.charAt(i + 1)) {
                    ++i;
                    ewts = "v";  // TODO(dchandler): hard-coded EWTS
                } else if ('\u0f71' == ch && '\u0f72' == nfthdl.charAt(i + 1)) {
                    ++i;
                    ewts = THDLWylieConstants.I_VOWEL;
                    // NOTE: we could normalize to 0f73 and 0f75 when
                    // possible in NFTHDL.  That's closer to EWTS and
                    // would avoid these two special cases.
                } else if ('\u0f71' == ch && '\u0f74' == nfthdl.charAt(i + 1)) {
                    ++i;
                    ewts = THDLWylieConstants.U_VOWEL;
                }
            }
            if (null == ewts && UnicodeUtils.isInTibetanRange(ch)) {
                return null;
            }
            if (UnicodeUtils.isSubjoinedConsonant(ch)
                || (seenVowel && isVowel(ch)))
                sb.append(THDLWylieConstants.WYLIE_SANSKRIT_STACKING_KEY);
            if (isWowelRequiringPrecedingVowel(ch) && !seenVowel) {
                if (!added_aVOWEL) {
                    added_aVOWEL = true;
                    sb.append(THDLWylieConstants.WYLIE_aVOWEL);  // paM, no pM
                }
            }
            if (isVowel(ch)) {
                seenVowel = true;
                if (lastEwts=="a")
                {
                	sb.deleteCharAt(sb.length()-1);
                }
            }
            sb.append(ewts);
            lastEwts = ewts;
        }
        if ((UnicodeUtils.isNonSubjoinedConsonant(ch)
            || UnicodeUtils.isSubjoinedConsonant(ch)
            || '\u0f39' == ch) && '\u0f68' != ch) {
            ThdlDebug.verify(!added_aVOWEL);
            sb.append(THDLWylieConstants.WYLIE_aVOWEL);
        }
        return sb;
    }

    public int hashCode() { return nfthdl.hashCode(); }

    public boolean equals(Object o) {
        return (o instanceof GC && ((GC)o).getNfthdl().equals(getNfthdl()));
    }

    /** Quasi-XML for humans */
    public String toString() {
        return "<GC valid=" + valid + " pretty=\""
            + UnicodeUtils.unicodeStringToPrettyString(getNfthdl())
            + "\"/>";
    }
}
