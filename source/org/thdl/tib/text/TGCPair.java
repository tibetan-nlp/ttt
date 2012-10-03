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

package org.thdl.tib.text;

import org.thdl.util.ThdlDebug;


/** An ordered pair consisting of a Tibetan grapheme cluster's (see
    {@link org.thdl.tib.text.tshegbar.UnicodeGraphemeCluster} for a
    definition of the term}) classification and its
    context-insensitive THDL Extended Wylie representation.  NOTE
    WELL: this is not a real grapheme cluster; I'm misusing the term
    (FIXME).  It's actually whole or part of one.  It's part of one
    when this is U+0F7F alone.

    @author David Chandler */
public class TGCPair implements THDLWylieConstants {
    public static final int OTHER = 1;
    // a standalone achen would fall into this category:
    public static final int CONSONANTAL_WITHOUT_VOWEL = 2;
    public static final int CONSONANTAL_WITH_VOWEL = 3;
    public static final int LONE_VOWEL = 4;
    public static final int SANSKRIT_WITHOUT_VOWEL = 5;
    public static final int SANSKRIT_WITH_VOWEL = 6;

    /** Returns a human-readable (well, programmer-readable)
        representation of one of the public enumerations in this
        class. */
    public static final String enumToString(int cls) {
        if (OTHER == cls) return "OTHER";
        if (LONE_VOWEL == cls) return "LONE_VOWEL";
        if (SANSKRIT_WITH_VOWEL == cls) return "SANSKRIT_WITH_VOWEL";
        if (SANSKRIT_WITHOUT_VOWEL == cls) return "SANSKRIT_WITHOUT_VOWEL";
        if (CONSONANTAL_WITH_VOWEL == cls) return "CONSONANTAL_WITH_VOWEL";
        if (CONSONANTAL_WITHOUT_VOWEL == cls) return "CONSONANTAL_WITHOUT_VOWEL";

        if (TYPE_OTHER == cls) return "TYPE_OTHER";
        if (TYPE_SANSKRIT == cls) return "TYPE_SANSKRIT";
        if (TYPE_TIBETAN == cls) return "TYPE_TIBETAN";
        return null;
    }

    public static final int TYPE_OTHER = 31;
    public static final int TYPE_SANSKRIT = 32;
    public static final int TYPE_TIBETAN = 33;

    // Sanskrit or Tibetan consonant, or number, or oddball:
    private String consonantWylie;
    private String vowelWylie;
    public String getConsonantWylie() {
        return consonantWylie;
    }
    public String getVowelWylie() {
        return vowelWylie;
    }
    /** Cludge. */
    public void setWylie(String x) {
        consonantWylie = x;
        vowelWylie = null;
    }
    public String getWylie() {
        return getWylie(null);
    }
    /** Returns the EWTS for this pair, given that, if and only if
        this pair is part of an appendaged tsheg bar like ma'ongs or
        pa'am or spre'ur, previousTranslitIfAppendaged is non-null.
        If it is non-null, then it must be equal to the EWTS
        transliteration of the previous pair.
        @see #getACIP(String)
    */
    public String getWylie(String previousTranslitIfAppendaged) {
        if (ACHEN.equals(consonantWylie)) {
            // Unlike ACIP, EWTS uses e for achen with e vowel, not ae.
            if (null == vowelWylie)
                return ACHEN;
            else {
                if (TibetanMachineWeb.startsWithWylieVowelSequence(vowelWylie))
                    return vowelWylie;
                else
                    return ACHEN + vowelWylie;
            }
        }
        StringBuffer b = new StringBuffer();
        if (consonantWylie != null) {
            // Think of pa'am...  we want 'am, not 'm; 'ang, not 'ng.  But we want 'ur, not 'uar, 'is, not 'ias.
            if (null != previousTranslitIfAppendaged
                && "'".equals(previousTranslitIfAppendaged)) {
                b.append("a");
            }

            // we may have {p-y}, but the user wants to see {py}.
            for (int i = 0; i < consonantWylie.length(); i++) {
                char ch = consonantWylie.charAt(i);
                if ('-' != ch)
                    b.append(ch);
            }
        }
        if (vowelWylie != null) {
            if (consonantWylie != null // we need a stack to put an 'a' onto
                && !TibetanMachineWeb.startsWithWylieVowelSequence(vowelWylie)) {
                b.append("a"); // we want laM, not lM -- see bug 998476
            }
            b.append(vowelWylie);
        }
        return b.toString();
    }
    public String getACIP() {
        return getACIP(null);
    }
    /** Like {@link #getWylie(String)} but for ACIP transliteration,
        not EWTS. */
    public String getACIP(String previousTranslitIfAppendaged) {
        // DLC FIXME: has the EWTS change affected Manipulate.acipToWylie?
        StringBuffer b = new StringBuffer();
        if (consonantWylie != null) {
            String consonantACIP
                = null;
            if ("w".equals(consonantWylie)
                && (SANSKRIT_WITHOUT_VOWEL == classification
                    || SANSKRIT_WITH_VOWEL == classification))
                consonantACIP = "V";
            else
                consonantACIP
                    = org.thdl.tib.text.ttt.ACIPTraits.instance().getACIPForEWTS(consonantWylie);
            if (null == consonantACIP) {
                if (null != consonantWylie && consonantWylie.startsWith("R+"))
                    return TibetanMachineWeb.getTMWToACIPErrorString("glyph with THDL Extended Wylie " + consonantWylie, " because the ACIP R+... could imply the short superscribed form, but this most likely intends the full form (i.e., Unicode character U+0F6A)");
                return TibetanMachineWeb.getTMWToACIPErrorString("glyph with THDL Extended Wylie " + consonantWylie, "");
            } else {
                // Think of pa'am...  we want 'am, not 'm; 'ang, not
                // 'ng.  But we want 'ur, not 'uar, 'is, not 'ias.
                if (null != previousTranslitIfAppendaged
                    && "'".equals(previousTranslitIfAppendaged)) {
                    b.append("A");
                }

                // we may have {P-Y}, but the user wants to see {PY}.
                for (int i = 0; i < consonantACIP.length(); i++) {
                    char ch = consonantACIP.charAt(i);
                    if ('-' != ch)
                        b.append(ch);
                }
            }
        }
        if (vowelWylie != null) {
            String vowelACIP
                = org.thdl.tib.text.ttt.ACIPTraits.instance().getACIPForEWTS(vowelWylie);
            if (null == vowelACIP) {
                return TibetanMachineWeb.getTMWToACIPErrorString("glyph with THDL Extended Wylie " + vowelWylie, "");
            } else {
                b.append(vowelACIP);
            }
        }
        return b.toString();
    }
    public int classification;
    /** Constructs a new TGCPair with (Tibetan or Sanskrit) consonant
     *  consonantWylie and vowel vowelWylie.  Use
     *  classification==TYPE_OTHER for numbers, lone vowels, marks,
     *  etc.  Use classification==TYPE_TIBETAN for Tibetan (not
     *  Tibetanized Sanskrit) and classification=TYPE_SANSKRIT for
     *  Tibetanized Sanskrit. */
    public TGCPair(String consonantWylie, String vowelWylie, int classification) {
        if ("".equals(vowelWylie))
            vowelWylie = null;
        // Technically, we don't need the following check, but it's
        // nice for consistency's sake.
        if ("".equals(consonantWylie))
            consonantWylie = null;

        // DLC FIXME: for speed, make these assertions:
        if (classification != TYPE_OTHER
            && classification != TYPE_TIBETAN
            && classification != TYPE_SANSKRIT) {
            throw new IllegalArgumentException("Bad classification " + classification + ".");
        }
        int realClassification = -37;
        if (vowelWylie == null && classification == TYPE_TIBETAN)
            realClassification = CONSONANTAL_WITHOUT_VOWEL;
        if (vowelWylie != null && classification == TYPE_TIBETAN) {
            realClassification = CONSONANTAL_WITH_VOWEL;
        }
        if (vowelWylie == null && classification == TYPE_SANSKRIT)
            realClassification = SANSKRIT_WITHOUT_VOWEL;
        if (vowelWylie != null && classification == TYPE_SANSKRIT)
            realClassification = SANSKRIT_WITH_VOWEL;
        if (consonantWylie == null) {
            if (classification != TYPE_OTHER)
                throw new IllegalArgumentException("That's the very definition of a lone vowel.");
            realClassification = LONE_VOWEL;
        } else {
            if (classification == TYPE_OTHER)
                realClassification = OTHER;
        }

        this.consonantWylie = consonantWylie;
        if (null != vowelWylie) {
            if (vowelWylie.equals("iA") || vowelWylie.equals("Ai"))
                vowelWylie = "I";
            if (vowelWylie.equals("uA") || vowelWylie.equals("Au"))
                vowelWylie = "U";
        }
        // Normalize vowelWylie such that any real vowel (i.e., a
        // vowel for which TibetanMachineWeb.isWylieVowel(..) returns
        // true) comes before any non-vowel but vowelish combining
        // character like 'M', '?', '~M', '~M`', or 'H':
        this.vowelWylie = normalizedVowel(vowelWylie);
        this.classification = realClassification;
    }

    private static final int MAX_CHARACTERS_IN_VOWELISH = 3; // ~M` has 3 characters


    /** Returns v such that all the normal vowels in v come first and
        the combining marks (Sanskrit-ish) come last.  Relative order
        in each of the two groups is preserved. */
    private static String normalizedVowel(String v) {
        if (null == v || "".equals(v)) return null;
        StringBuffer vowel_sb = new StringBuffer();
        StringBuffer vowelish_sb = new StringBuffer();
        int mark = 0;
        int maxVowelishLength
            = Math.max(TibetanMachineWeb.getMaxEwtsVowelLength(),
                       TGCPair.MAX_CHARACTERS_IN_VOWELISH);
        for (int i = 0; i < v.length(); i++) {
            // Grab ~M` if it's there because you don't want to grab
            // ~M and then have ` left over, which is not vowelish.
            for (int j = maxVowelishLength; j > 0; j--) {
                if (i + j <= v.length()) {
                    String x = v.substring(mark, i + j);
                    if (TibetanMachineWeb.isWylieVowel(x)) {
                        mark = i + j;
                        i += j - 1;
                        vowel_sb.append(x);
                    } else if (isWylieVowelishButNotVowel(x)) {
                        mark = i + j;
                        i += j - 1;
                        vowelish_sb.append(x);
                    }
                }
            }
        }
        if (mark < v.length()) {
            vowelish_sb.append(v.substring(mark));
            // TODO(DLC)[EWTS->Tibetan]:  ThdlDebug.noteIffyCode();
            // FIXME(dchandler): what should I do here?  I doubt v is
            // valid.
        }
        if (vowelish_sb.length() > 0) { // just an optimization
            vowel_sb.append(vowelish_sb);
            return vowel_sb.toString();
        } else {
            ThdlDebug.verify(vowel_sb.toString().equals(v));
            return v;
        }
    }

    /** Returns true if v is in the set { "M", "H", "~M", "~M`", "?" }. */
    private static boolean isWylieVowelishButNotVowel(String v) {
        boolean ans = (v.equals("M")
                       || v.equals("H")
                       || v.equals("~M`")
                       || v.equals("~M")
                       || v.equals("?"));
        ThdlDebug.verify(!ans || TGCPair.MAX_CHARACTERS_IN_VOWELISH >= v.length());
        return ans;
    }

    public String toString() {
        return "<TGCPair wylie=" + getWylie() + " classification="
            + classification + "/>";
    }
}
