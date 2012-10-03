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
Library (THDL). Portions created by the THDL are Copyright 2003-2004 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.thdl.tib.text.DuffCode;
import org.thdl.tib.text.THDLWylieConstants;
import org.thdl.tib.text.TibTextUtils;
import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.util.ThdlOptions;


/** A singleton class that should contain (but due to laziness and
 *  ignorance probably does not contain) all the traits that make ACIP
 *  transliteration scheme different from other (say, EWTS)
 *  transliteration schemes.  This is not safe to use in concurrent
 *  programs but it would be easy to make it so. */
public final class ACIPTraits implements TTraits {
    /** sole instance of this class */
    private static ACIPTraits singleton = null;

    /** Just a constructor. */
    private ACIPTraits() { }

    /** Returns the singleton instance of this class. */
    public static /* synchronized */ ACIPTraits instance() {
        if (null == singleton) {
            singleton = new ACIPTraits();
        }
        return singleton;
    }

    /** Returns "-". */
    public String disambiguator() { return "-"; }

    /** Returns '-'. */
    public char disambiguatorChar() { return '-'; }

    public int maxConsonantLength() { return MAX_CONSONANT_LENGTH; }

    public int maxWowelLength() { return MAX_WOWEL_LENGTH; }

    public boolean hasSimpleError(TPair p) {
    	return (("A".equals(p.getLeft()) && null == p.getRight())
    			|| (null == p.getLeft()
    				&& !this.disambiguator().equals(p.getRight())));
    }

    public String aVowel() { return "A"; }

    public boolean isPostsuffix(String l) {
        return ("S".equals(l)
                || "D".equals(l));
    }

    public boolean isSuffix(String l) {
        return ("S".equals(l)
                || "G".equals(l)
                || "D".equals(l)
                || "M".equals(l)
                || "'".equals(l)
                || "B".equals(l)
                || "NG".equals(l)
                || "N".equals(l)
                || "L".equals(l)
                || "R".equals(l));
    }

    public boolean isPrefix(String l) {
        return ("'".equals(l)
                || "M".equals(l)
                || "B".equals(l)
                || "D".equals(l)
                || "G".equals(l));
    }

    private HashMap superACIP2unicode = null;
    private HashMap subACIP2unicode = null;

    public String getUnicodeForWowel(String wowel) {
        return getUnicodeFor(wowel, /* doesn't matter: */ true);
    }

    public /* synchronized */ String getUnicodeFor(String acip, boolean subscribed) {
        if (superACIP2unicode == null) {
            final boolean compactUnicode
                = ThdlOptions.getBooleanOption("thdl.acip.to.unicode.conversions.use.0F52.et.cetera");
            superACIP2unicode = new HashMap(144);
            subACIP2unicode = new HashMap(42);

            // oddball:
            subACIP2unicode.put("V", "\u0FAD");

            superACIP2unicode.put("DH", (compactUnicode ? "\u0F52" : "\u0F51\u0FB7"));
            subACIP2unicode.put("DH", (compactUnicode ? "\u0FA2" : "\u0FA1\u0FB7"));
            superACIP2unicode.put("BH", (compactUnicode ? "\u0F57" : "\u0F56\u0FB7"));
            subACIP2unicode.put("BH", (compactUnicode ? "\u0FA7" : "\u0FA6\u0FB7"));
            superACIP2unicode.put("dH", (compactUnicode ? "\u0F4D" : "\u0F4C\u0FB7"));
            subACIP2unicode.put("dH", (compactUnicode ? "\u0F9D" : "\u0F9C\u0FB7"));
            superACIP2unicode.put("DZH", (compactUnicode ? "\u0F5C" : "\u0F5B\u0FB7"));
            subACIP2unicode.put("DZH", (compactUnicode ? "\u0FAC" : "\u0FAB\u0FB7"));
            superACIP2unicode.put("Ksh", (compactUnicode ? "\u0F69" : "\u0F40\u0FB5"));
            subACIP2unicode.put("Ksh", (compactUnicode ? "\u0FB9" : "\u0F90\u0FB5"));
            superACIP2unicode.put("GH", (compactUnicode ? "\u0F43" : "\u0F42\u0FB7"));
            subACIP2unicode.put("GH", (compactUnicode ? "\u0F93" : "\u0F92\u0FB7"));
            superACIP2unicode.put("K", "\u0F40");
            subACIP2unicode.put("K", "\u0F90");
            superACIP2unicode.put("KH", "\u0F41");
            subACIP2unicode.put("KH", "\u0F91");
            superACIP2unicode.put("G", "\u0F42");
            subACIP2unicode.put("G", "\u0F92");
            superACIP2unicode.put("NG", "\u0F44");
            subACIP2unicode.put("NG", "\u0F94");
            superACIP2unicode.put("C", "\u0F45");
            subACIP2unicode.put("C", "\u0F95");
            superACIP2unicode.put("CH", "\u0F46");
            subACIP2unicode.put("CH", "\u0F96");
            superACIP2unicode.put("J", "\u0F47");
            subACIP2unicode.put("J", "\u0F97");
            superACIP2unicode.put("NY", "\u0F49");
            subACIP2unicode.put("NY", "\u0F99");
            superACIP2unicode.put("T", "\u0F4F");
            subACIP2unicode.put("T", "\u0F9F");
            superACIP2unicode.put("TH", "\u0F50");
            subACIP2unicode.put("TH", "\u0FA0");
            superACIP2unicode.put("D", "\u0F51");
            subACIP2unicode.put("D", "\u0FA1");
            superACIP2unicode.put("N", "\u0F53");
            subACIP2unicode.put("N", "\u0FA3");
            superACIP2unicode.put("P", "\u0F54");
            subACIP2unicode.put("P", "\u0FA4");
            superACIP2unicode.put("PH", "\u0F55");
            subACIP2unicode.put("PH", "\u0FA5");
            superACIP2unicode.put("B", "\u0F56");
            subACIP2unicode.put("B", "\u0FA6");
            superACIP2unicode.put("M", "\u0F58");
            subACIP2unicode.put("M", "\u0FA8");
            superACIP2unicode.put("TZ", "\u0F59");
            subACIP2unicode.put("TZ", "\u0FA9");
            superACIP2unicode.put("TS", "\u0F5A");
            subACIP2unicode.put("TS", "\u0FAA");
            superACIP2unicode.put("DZ", "\u0F5B");
            subACIP2unicode.put("DZ", "\u0FAB");
            superACIP2unicode.put("W", "\u0F5D");
            subACIP2unicode.put("W", "\u0FBA"); // oddball
            superACIP2unicode.put("ZH", "\u0F5E");
            subACIP2unicode.put("ZH", "\u0FAE");
            superACIP2unicode.put("Z", "\u0F5F");
            subACIP2unicode.put("Z", "\u0FAF");
            superACIP2unicode.put("'", "\u0F60");
            subACIP2unicode.put("'", "\u0FB0");
            superACIP2unicode.put("Y", "\u0F61");
            subACIP2unicode.put("Y", "\u0FB1");
            superACIP2unicode.put("R", "\u0F62");
            subACIP2unicode.put("R", "\u0FB2");
            superACIP2unicode.put("L", "\u0F63");
            subACIP2unicode.put("L", "\u0FB3");
            superACIP2unicode.put("SH", "\u0F64");
            subACIP2unicode.put("SH", "\u0FB4");
            superACIP2unicode.put("S", "\u0F66");
            subACIP2unicode.put("S", "\u0FB6");
            superACIP2unicode.put("H", "\u0F67");
            subACIP2unicode.put("H", "\u0FB7");
            superACIP2unicode.put("A", "\u0F68");
            subACIP2unicode.put("A", "\u0FB8");
            superACIP2unicode.put("t", "\u0F4A");
            subACIP2unicode.put("t", "\u0F9A");
            superACIP2unicode.put("th", "\u0F4B");
            subACIP2unicode.put("th", "\u0F9B");
            superACIP2unicode.put("d", "\u0F4C");
            subACIP2unicode.put("d", "\u0F9C");
            superACIP2unicode.put("n", "\u0F4E");
            subACIP2unicode.put("n", "\u0F9E");
            superACIP2unicode.put("sh", "\u0F65");
            subACIP2unicode.put("sh", "\u0FB5");

            superACIP2unicode.put("I", "\u0F72");
            superACIP2unicode.put("E", "\u0F7A");
            superACIP2unicode.put("O", "\u0F7C");
            superACIP2unicode.put("U", "\u0F74");
            superACIP2unicode.put("OO", "\u0F7D");
            superACIP2unicode.put("EE", "\u0F7B");
            superACIP2unicode.put("i", "\u0F80");
            superACIP2unicode.put("'A", "\u0F71");
            superACIP2unicode.put("'I", "\u0F71\u0F72");
            superACIP2unicode.put("'E", "\u0F71\u0F7A");
            superACIP2unicode.put("'O", "\u0F71\u0F7C");
            superACIP2unicode.put("'U", "\u0F71\u0F74");
            superACIP2unicode.put("'OO", "\u0F71\u0F7D");
            superACIP2unicode.put("'EE", "\u0F71\u0F7B");
            superACIP2unicode.put("'i", "\u0F71\u0F80");

            superACIP2unicode.put("Im", "\u0F72\u0F7E");
            superACIP2unicode.put("Em", "\u0F7A\u0F7E");
            superACIP2unicode.put("Om", "\u0F7C\u0F7E");
            superACIP2unicode.put("Um", "\u0F74\u0F7E");
            superACIP2unicode.put("OOm", "\u0F7D\u0F7E");
            superACIP2unicode.put("EEm", "\u0F7B\u0F7E");
            superACIP2unicode.put("im", "\u0F80\u0F7E");
            superACIP2unicode.put("'Am", "\u0F71\u0F7E");
            superACIP2unicode.put("'Im", "\u0F71\u0F72\u0F7E");
            superACIP2unicode.put("'Em", "\u0F71\u0F7A\u0F7E");
            superACIP2unicode.put("'Om", "\u0F71\u0F7C\u0F7E");
            superACIP2unicode.put("'Um", "\u0F71\u0F74\u0F7E");
            superACIP2unicode.put("'OOm", "\u0F71\u0F7D\u0F7E");
            superACIP2unicode.put("'EEm", "\u0F71\u0F7B\u0F7E");
            superACIP2unicode.put("'im", "\u0F71\u0F80\u0F7E");

            superACIP2unicode.put("I:", "\u0F72\u0F7F");
            superACIP2unicode.put("E:", "\u0F7A\u0F7F");
            superACIP2unicode.put("O:", "\u0F7C\u0F7F");
            superACIP2unicode.put("U:", "\u0F74\u0F7F");
            superACIP2unicode.put("OO:", "\u0F7D\u0F7F");
            superACIP2unicode.put("EE:", "\u0F7B\u0F7F");
            superACIP2unicode.put("i:", "\u0F80\u0F7F");
            superACIP2unicode.put("'A:", "\u0F71\u0F7F");
            superACIP2unicode.put("'I:", "\u0F71\u0F72\u0F7F");
            superACIP2unicode.put("'E:", "\u0F71\u0F7A\u0F7F");
            superACIP2unicode.put("'O:", "\u0F71\u0F7C\u0F7F");
            superACIP2unicode.put("'U:", "\u0F71\u0F74\u0F7F");
            superACIP2unicode.put("'OO:", "\u0F71\u0F7D\u0F7F");
            superACIP2unicode.put("'EE:", "\u0F71\u0F7B\u0F7F");
            superACIP2unicode.put("'i:", "\u0F71\u0F80\u0F7F");

            superACIP2unicode.put("Im:", "\u0F72\u0F7E\u0F7F");
            superACIP2unicode.put("Em:", "\u0F7A\u0F7E\u0F7F");
            superACIP2unicode.put("Om:", "\u0F7C\u0F7E\u0F7F");
            superACIP2unicode.put("Um:", "\u0F74\u0F7E\u0F7F");
            superACIP2unicode.put("OOm:", "\u0F7D\u0F7E\u0F7F");
            superACIP2unicode.put("EEm:", "\u0F7B\u0F7E\u0F7F");
            superACIP2unicode.put("im:", "\u0F80\u0F7E\u0F7F");
            superACIP2unicode.put("'Am:", "\u0F71\u0F7E\u0F7F");
            superACIP2unicode.put("'Im:", "\u0F71\u0F72\u0F7E\u0F7F");
            superACIP2unicode.put("'Em:", "\u0F71\u0F7A\u0F7E\u0F7F");
            superACIP2unicode.put("'Om:", "\u0F71\u0F7C\u0F7E\u0F7F");
            superACIP2unicode.put("'Um:", "\u0F71\u0F74\u0F7E\u0F7F");
            superACIP2unicode.put("'OOm:", "\u0F71\u0F7D\u0F7E\u0F7F");
            superACIP2unicode.put("'EEm:", "\u0F71\u0F7B\u0F7E\u0F7F");
            superACIP2unicode.put("'im:", "\u0F71\u0F80\u0F7E\u0F7F");
            // :m does not appear, though you'd think it's as valid as m:.

            superACIP2unicode.put("m", "\u0F7E");
            superACIP2unicode.put(":", "\u0F7F");
            superACIP2unicode.put("m:", "\u0F7E\u0F7F");

            superACIP2unicode.put("Am", "\u0F7E");
            superACIP2unicode.put("A:", "\u0F7F");
            superACIP2unicode.put("Am:", "\u0F7E\u0F7F");

            superACIP2unicode.put("0", "\u0F20");
            superACIP2unicode.put("1", "\u0F21");
            superACIP2unicode.put("2", "\u0F22");
            superACIP2unicode.put("3", "\u0F23");
            superACIP2unicode.put("4", "\u0F24");
            superACIP2unicode.put("5", "\u0F25");
            superACIP2unicode.put("6", "\u0F26");
            superACIP2unicode.put("7", "\u0F27");
            superACIP2unicode.put("8", "\u0F28");
            superACIP2unicode.put("9", "\u0F29");

            // punctuation
            superACIP2unicode.put("&", "\u0F85");
            superACIP2unicode.put(",", "\u0F0D");
            superACIP2unicode.put(" ", "\u0F0B");
            superACIP2unicode.put(".", "\u0F0C");
            superACIP2unicode.put("`", "\u0F08");
            superACIP2unicode.put("`", "\u0F08");
            superACIP2unicode.put("*", "\u0F04\u0F05");
            superACIP2unicode.put("#", "\u0F04\u0F05\u0F05");
            superACIP2unicode.put("%", "\u0F35"); // but might be U+0F14, so we warn.
            superACIP2unicode.put("o", "\u0F37");
            superACIP2unicode.put(";", "\u0F11");
            superACIP2unicode.put("\r", "\r");
            superACIP2unicode.put("\t", "\t");
            superACIP2unicode.put("\r\n", "\r\n");
            superACIP2unicode.put("\n", "\n");
            superACIP2unicode.put("\\", "\u0F84");
            superACIP2unicode.put("^", "\u0F38");

            // DLC FIXME: "^ GONG" is "^GONG", right?
            // DLC FIXME: what's the Unicode for x? RC said there is none in plain-text Unicode for x.  But what about in RTF Unicode?
        }
        if (subscribed) {
            String u = (String)subACIP2unicode.get(acip);
            if (null != u) return u;
        }
        return (String)superACIP2unicode.get(acip);
    }

    private HashMap acipOther2wylie = null;
    public /* synchronized */ String getEwtsForOther(String acip) {
        if (acipOther2wylie == null) {
            acipOther2wylie = new HashMap(20);

            // don't use putMapping for this.  We don't want TMW->ACIP
            // to produce "." for a U+0F0C because ACIP doesn't say
            // that "." means U+0F0C.  It just seems to in practice
            // for ACIP Release IV texts.
            acipOther2wylie.put(".", "*");

            putMapping(acipOther2wylie, "m", "M");
            putMapping(acipOther2wylie, ":", "H");
            putMapping(acipOther2wylie, ",", "/");
            putMapping(acipOther2wylie, " ", " ");
            putMapping(acipOther2wylie, ";", "|");
            putMapping(acipOther2wylie, "`", "!");
            putMapping(acipOther2wylie, "*", "@#");
            // There is no glyph in TMW with the EWTS @##, so we don't do this: putMapping(acipOther2wylie, "#", "@##");
            putMapping(acipOther2wylie, "%", "~X");
            putMapping(acipOther2wylie, "o", "X");
            putMapping(acipOther2wylie, "&", "&");
            putMapping(acipOther2wylie, "^", "\\u0F38");

            putMapping(acipOther2wylie, "0", "0");
            putMapping(acipOther2wylie, "1", "1");
            putMapping(acipOther2wylie, "2", "2");
            putMapping(acipOther2wylie, "3", "3");
            putMapping(acipOther2wylie, "4", "4");
            putMapping(acipOther2wylie, "5", "5");
            putMapping(acipOther2wylie, "6", "6");
            putMapping(acipOther2wylie, "7", "7");
            putMapping(acipOther2wylie, "8", "8");
            putMapping(acipOther2wylie, "9", "9");
        }
        return (String)acipOther2wylie.get(acip);
    }

    public TTshegBarScanner scanner() { return ACIPTshegBarScanner.instance(); }

    /** Registers acip->wylie mappings in toWylie; registers
        wylie->acip mappings in {@link #wylieToACIP}. */
    private /* synchronized */ void putMapping(HashMap toWylie, String ACIP, String EWTS) {
        toWylie.put(ACIP, EWTS);
        if (null == wylieToACIP) {
            wylieToACIP = new HashMap(75);

            // We don't want to put "/" in toWylie:
            wylieToACIP.put("(", "/");
            wylieToACIP.put(")", "/");
            wylieToACIP.put("?", "\\");

            wylieToACIP.put("_", " "); // oddball.
            wylieToACIP.put("o'i", "O'I"); // oddball for TMW9.61.
        }
        wylieToACIP.put(EWTS, ACIP);
    }

    /** A map from EWTS to ACIP.  Note that the EWTS "w" maps to both
        "V" and "W" in reality but this map will only give one or the
        other. */
    private HashMap wylieToACIP = null;
    /** Returns the ACIP transliteration corresponding to the THDL
        Extended Wylie <em>atom</em> EWTS, or null if EWTS is not
        recognized. */
    public String getACIPForEWTS(String EWTS) {
        getEwtsForConsonant(null); // inits wylieToACIP
        getEwtsForOther(null); // inits wylieToACIP
        getEwtsForWowel(null); // inits wylieToACIP
        String ans = (String)wylieToACIP.get(EWTS);
        boolean useCapitalW = false;
        if (EWTS.startsWith("w"))
            useCapitalW = true; // We want W+NA, not V+NA; we want WA, not VA.
        if (null == ans) {
            StringBuffer finalAns = new StringBuffer(EWTS.length());
            StringTokenizer sTok = new StringTokenizer(EWTS, "-+", true);
            while (sTok.hasMoreTokens()) {
                String part, tok = sTok.nextToken();
                if (tok.equals("-") || tok.equals("+"))
                    part = tok;
                else {
                    if ("w".equals(tok)) {
                        // There are only two stacks in TMW that have
                        // U+0FBA: R+Wa and w+Wa.  TMW->ACIP fails for
                        // these unless we handle it here.  (FIXME:
                        // add an automated test for this).
                        if ("R+W".equals(EWTS) || "w+W".equals(EWTS)) {
                            part = "W";
                        } else {
                            part = "V";
                        }
                    } else {
                        part = (String)wylieToACIP.get(tok);
                    }
                }
                if (null == part) return null;
                finalAns.append(part);
            }
            if (useCapitalW)
                finalAns.setCharAt(0, 'W');
            return finalAns.toString();
        }
        if (useCapitalW)
            return "W" + ans.substring(1);
        else
            return ans;
    }

    private HashMap acipConsonant2wylie = null;
    /** Returns "W" for ACIP "W", "r" for ACIP "R", y for ACIP "Y",
     *  even though sometimes the EWTS for those is "w", "R", or "Y".
     *  Handle that in the caller. */
    public /* synchronized */ String getEwtsForConsonant(String acip) {
        if (acipConsonant2wylie == null) {
            acipConsonant2wylie = new HashMap(37);

            // oddball:
            putMapping(acipConsonant2wylie, "V", "w");

            // more oddballs:
            putMapping(acipConsonant2wylie, "DH", "d+h");
            putMapping(acipConsonant2wylie, "BH", "b+h");
            putMapping(acipConsonant2wylie, "dH", "D+h");
            putMapping(acipConsonant2wylie, "DZH", "dz+h"); // longest, MAX_CONSONANT_LENGTH characters
            putMapping(acipConsonant2wylie, "Ksh", "k+Sh"); // longest, MAX_CONSONANT_LENGTH characters
            putMapping(acipConsonant2wylie, "GH", "g+h");


            putMapping(acipConsonant2wylie, "K", "k");
            putMapping(acipConsonant2wylie, "KH", "kh");
            putMapping(acipConsonant2wylie, "G", "g");
            putMapping(acipConsonant2wylie, "NG", "ng");
            putMapping(acipConsonant2wylie, "C", "c");
            putMapping(acipConsonant2wylie, "CH", "ch");
            putMapping(acipConsonant2wylie, "J", "j");
            putMapping(acipConsonant2wylie, "NY", "ny");
            putMapping(acipConsonant2wylie, "T", "t");
            putMapping(acipConsonant2wylie, "TH", "th");
            putMapping(acipConsonant2wylie, "D", "d");
            putMapping(acipConsonant2wylie, "N", "n");
            putMapping(acipConsonant2wylie, "P", "p");
            putMapping(acipConsonant2wylie, "PH", "ph");
            putMapping(acipConsonant2wylie, "B", "b");
            putMapping(acipConsonant2wylie, "M", "m");
            putMapping(acipConsonant2wylie, "TZ", "ts");
            putMapping(acipConsonant2wylie, "TS", "tsh");
            putMapping(acipConsonant2wylie, "DZ", "dz");
            putMapping(acipConsonant2wylie, "W", "W"
                       /* NOTE WELL: sometimes "w", sometimes "W".
                          Handle this in the caller.
                          
                          Reasoning for "W" instead of "w": r-w and
                          r+w are both known hash keys.  We sort 'em
                          out this way.  (They are the only things
                          like this according to bug report #800166.)  */
                       );
            putMapping(acipConsonant2wylie, "ZH", "zh");
            putMapping(acipConsonant2wylie, "Z", "z");
            putMapping(acipConsonant2wylie, "'", "'");
            putMapping(acipConsonant2wylie, "Y", "y");
            putMapping(acipConsonant2wylie, "R", "r");
            putMapping(acipConsonant2wylie, "L", "l");
            putMapping(acipConsonant2wylie, "SH", "sh");
            putMapping(acipConsonant2wylie, "S", "s");
            putMapping(acipConsonant2wylie, "H", "h");
            putMapping(acipConsonant2wylie, "A", "a");
            putMapping(acipConsonant2wylie, "t", "T");
            putMapping(acipConsonant2wylie, "th", "Th");
            putMapping(acipConsonant2wylie, "d", "D");
            putMapping(acipConsonant2wylie, "n", "N");
            putMapping(acipConsonant2wylie, "sh", "Sh");
        }
        return (String)acipConsonant2wylie.get(acip);
    }

    private HashMap acipWowel2wylie = null;
    public /* synchronized */ String getEwtsForWowel(String acip) {
        if (acipWowel2wylie == null) {
            acipWowel2wylie = new HashMap(baseVowels.length * 4);

            for (int i = 0; i < baseVowels.length; i++) {
                putMapping(acipWowel2wylie, baseVowels[i][0], baseVowels[i][1]);
                putMapping(acipWowel2wylie, '\'' + baseVowels[i][0], baseVowels[i][2]);
                putMapping(acipWowel2wylie, baseVowels[i][0] + 'm', baseVowels[i][1] + 'M');
                putMapping(acipWowel2wylie, '\'' + baseVowels[i][0] + 'm', baseVowels[i][2] + 'M');
                putMapping(acipWowel2wylie, baseVowels[i][0] + ':', baseVowels[i][1] + 'H');
                putMapping(acipWowel2wylie, '\'' + baseVowels[i][0] + ':', baseVowels[i][2] + 'H');
                putMapping(acipWowel2wylie, baseVowels[i][0] + "m:", baseVowels[i][1] + "MH");
                putMapping(acipWowel2wylie, '\'' + baseVowels[i][0] + "m:", baseVowels[i][2] + "MH");
            }
            // {Pm} is treated just like {PAm}; {P:} is treated just
            // like {PA:}; {Pm:} is treated just like {PAm:}.  But
            // that happens thanks to
            // TPairListFactory.getFirstConsonantAndVowel(StringBuffer,int[]).

            // Keep this code in sync with getUnicodeFor.
        }
        return (String)acipWowel2wylie.get(acip);
    }

    /** {Ksh}, the longest consonant, has 3 characters, so this is
     *  three. */
    private static int MAX_CONSONANT_LENGTH = 3;

    /** {'EEm:}, the longest wowel, has 5 characters, so this is
     *  five. */
    private static int MAX_WOWEL_LENGTH = 5;

    private static String[][] baseVowels = new String[][] {
        // { ACIP, EWTS, EWTS for ACIP {'\'' + baseVowels[][0]}, vowel
        // numbers (see TibetanMachineWeb's VOWEL_A, VOWEL_o, etc.) 
        // for ACIP, vowel numbers for ACIP {'\'' + baseVowels[][0]}
        { "A", "a", "A" },
        { "I", "i", "I" },
        { "U", "u", "U" },
        { "E", "e", "Ae" },
        { "O", "o", "Ao" },
        { "EE", "ai", "Aai" },
        { "OO", "au", "Aau" },
        { "i", "-i", "A-i" }
    };

    /** Returns true if and only if s is an ACIP wowel.  You can't
     *  just call this any time -- A is both a consonant and a vowel
     *  in ACIP, so you have to call this in the right context. */
    public boolean isWowel(String s) {
        // I'm on my own with 'O and 'E and 'OO and 'EE, but GANG'O
        // appears and I wonder... so here they are.  It's consistent
        // with 'I and 'A and 'U, at least: all the vowels may appear
        // as K'vowel.  DLC FIXME: ask.
        return (null != getEwtsForWowel(s));
    }

    /** Returns true if and only if s is an ACIP consonant. */
    public boolean isConsonant(String s) {
        return (null != getEwtsForConsonant(s));
    }

    /** Gets the duffcodes for wowel, such that they look good with
     *  the preceding glyph, and appends them to duff. */
    public void getDuffForWowel(ArrayList duff, DuffCode preceding, String wowel)
            throws ConversionException
    {
        if (null == wowel) return;
        if (null == getEwtsForWowel(wowel)) // FIXME: expensive assertion!  Use assert.
            throw new ConversionException("Wowel " + wowel + " isn't in the small set of wowels we handle correctly.");

        // Order matters here.
        boolean context_added[] = new boolean[] { false };
        if (wowel.startsWith("A")) {
            TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.WYLIE_aVOWEL, context_added);
        } else if (wowel.indexOf("'U") >= 0) {
            TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.U_VOWEL, context_added);
        } else if (wowel.indexOf("'I") >= 0) {
            TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.I_VOWEL, context_added);
        } else {
            // TODO(dchandler): I don't understand why we go from else ifs to this form...
            if (wowel.indexOf('\'') >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.A_VOWEL, context_added);
            }
            if (wowel.indexOf("EE") >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.ai_VOWEL, context_added);
            } else if (wowel.indexOf('E') >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.e_VOWEL, context_added);
            }
            if (wowel.indexOf("OO") >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.au_VOWEL, context_added);
            } else if (wowel.indexOf('O') >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.o_VOWEL, context_added);
            }
            if (wowel.indexOf('I') >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.i_VOWEL, context_added);
            }
            if (wowel.indexOf('U') >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.u_VOWEL, context_added);
            }
            if (wowel.indexOf('i') >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.reverse_i_VOWEL, context_added);
            }
        }
        // FIXME: Use TMW9.61, the "o'i" special combination, when appropriate.

        if (wowel.indexOf('m') >= 0) {
            DuffCode last = (DuffCode)duff.get(duff.size() - 1);
            duff.remove(duff.size() - 1); // getBindu will add it back...
            TibTextUtils.getBindu(duff, last);
        }
        if (wowel.indexOf(':') >= 0)
            duff.add(TibetanMachineWeb.getGlyph(getEwtsForOther(":")));
    }

    public String shortTranslitName() { return "ACIP"; }

    public boolean isClearlyIllegal(TPair p) {
        if (p.getLeft() == null
            && !disambiguator().equals(p.getRight()))
            return true;
        if ("+".equals(p.getLeft()))
            return true;
        if (isWowel(p.getLeft())
            && !aVowel().equals(p.getLeft())) // achen
            return true;
        if (":".equals(p.getLeft()))
            return true;
        if ("m".equals(p.getLeft()))
            return true;
        if ("m:".equals(p.getLeft()))
            return true;
        return false;
    }

    public TPairList[] breakTshegBarIntoChunks(String tt, boolean sh) {
        try {
            return TPairListFactory.breakACIPIntoChunks(tt, sh);
        } catch (StackOverflowError e) {
            // TODO(dchandler): use ConversionException?  Stop catching these?
            throw new IllegalArgumentException("Input too large[1]: " + tt);
        } catch (OutOfMemoryError e) {
            // TODO(dchandler): use ConversionException?  Stop catching these?
            throw new IllegalArgumentException("Input too large[2]: " + tt);
        }
    }

    public boolean isACIP() { return true; }
    
    public boolean vowelAloneImpliesAChen() { return false; }
    
    public boolean vowelsMayStack() { return false; }
    
    public boolean isUnicodeWowel(char ch) { return false; }

    public boolean couldBeValidStack(TPairList pl) { return true; }

    public boolean stackingMustBeExplicit() { return false; }

    public String U0F7F() { return ":"; }

    /** Test cases show that we don't need special-case treatment of this. */
    public String U0F35() { return null; }

    /** Test cases show that we don't need special-case treatment of this. */
    public String U0F37() { return null; }
}

