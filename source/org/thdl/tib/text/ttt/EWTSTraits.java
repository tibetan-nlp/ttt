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

    // TODO(DLC)[EWTS->Tibetan]: TibetanMachineWeb has duplication of much of this!

package org.thdl.tib.text.ttt;

import java.util.ArrayList;
import java.util.HashMap;

import org.thdl.tib.text.DuffCode;
import org.thdl.tib.text.THDLWylieConstants;
import org.thdl.tib.text.TibTextUtils;
import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.tib.text.tshegbar.UnicodeUtils;
import org.thdl.util.ThdlDebug;

/** A singleton class that should contain (but due to laziness and
 *  ignorance probably does not contain) all the traits that make EWTS
 *  transliteration different from other (say, ACIP) transliteration
 *  schemes. */
public final class EWTSTraits implements TTraits {
    /** sole instance of this class */
    private static EWTSTraits singleton = null;

    /** Just a constructor. */
    private EWTSTraits() { }

    /** */
    public static synchronized EWTSTraits instance() {
        if (null == singleton) {
            singleton = new EWTSTraits();
        }
        return singleton;
    }

    /** Returns ".". */
    public String disambiguator() { return "."; }

    /** Returns '.'. */
    public char disambiguatorChar() { return '.'; }

    // TODO(DLC)[EWTS->Tibetan]: isClearlyIllegal and hasSimpleError are different why?
    public boolean hasSimpleError(TPair p) {
        if (pairHasBadWowel(p)) return true;
        return (("a".equals(p.getLeft()) && null == p.getRight())
                || ("a".equals(p.getLeft())
                    && null != p.getRight()
                    && TibetanMachineWeb.isWylieVowel(p.getRight()))); // TODO(DLC)[EWTS->Tibetan]: or Unicode wowels?  test "a\u0f74" and "a\u0f7e"
        // TODO(DLC)[EWTS->Tibetan]: (a.e) is bad, one of (.a) or (a.) is bad
    }

    /** {tsh}, the longest consonant, has 3 characters, so this is
     *  three. */
    public int maxConsonantLength() { return 3; }

    /** Wowels can be arbitrarily long via stacking.  But each
     *  component is no longer, in characters, than this.  [~M`] is
     *  the current winner. */
    public int maxWowelLength() { return 3; }

    public boolean isUnicodeConsonant(char ch) {
        return ((ch != '\u0f48' && ch >= '\u0f40' && ch <= '\u0f6a')
                || (ch != '\u0f98' && ch >= '\u0f90' && ch <= '\u0fbc')
                // NOTE: \u0f88 is questionable, but we want EWTS
                // [\u0f88+kha] to become "\u0f88\u0f91" and this does
                // the trick.
                || ch == '\u0f88');
    }
    
    public boolean isUnicodeWowel(char ch) {
    	// TODO(DLC)[EWTS->Tibetan]: what about combiners that combine only with digits?  TEST
        return ((ch >= '\u0f71' && ch <= '\u0f84')
                || '\u0f39' == ch
                || isUnicodeWowelThatRequiresAChen(ch));
    }

// TODO(DLC)[EWTS->Tibetan]: u,e,i,o?  If not, document the special treatment in this function's comment
    public boolean isConsonant(String s) {
    	if (s.length() == 1 && isUnicodeConsonant(s.charAt(0))) return true;
    	if (aVowel().equals(s)) return false; // In EWTS, "a" is both a consonant and a vowel, but we treat it as just a vowel and insert the implied a-chen if you have a TPair ( . a) (TODO(DLC)[EWTS->Tibetan]: right?)

 // TODO(DLC)[EWTS->Tibetan]: numbers are consonants?

        // TODO(DLC)[EWTS->Tibetan]: just g for now
        return TibetanMachineWeb.isWylieChar(s);
    }

    public boolean isWowel(String s) {
        return (getUnicodeForWowel(s) != null);
    }

    public String aVowel() { return THDLWylieConstants.WYLIE_aVOWEL; }

    public boolean isPostsuffix(String s) {
        return ("s".equals(s) || "d".equals(s));
    }

    public boolean isPrefix(String l) {
        return (THDLWylieConstants.ACHUNG.equals(l)
                || THDLWylieConstants.MA.equals(l)
                || THDLWylieConstants.BA.equals(l)
                || THDLWylieConstants.DA.equals(l)
                || THDLWylieConstants.GA.equals(l));
    }

    public boolean isSuffix(String l) {
        return (isPrefix(l)
                || THDLWylieConstants.SA.equals(l)
                || THDLWylieConstants.NGA.equals(l)
                || THDLWylieConstants.NA.equals(l)
                || THDLWylieConstants.LA.equals(l)
                || THDLWylieConstants.RA.equals(l));
    }

    /** Returns the best EWTS for l, which is often l but not always
     *  thanks to Unicode escapes.  NOTE: For "\u0f42", you don't want
     *  to return "g" lest "\\u0f42ya " become the wrong thing under
     *  EWTS->Unicode. */
    public String getEwtsForConsonant(String l) {
        return helpGetEwts(l);
    }

    /** Returns the best EWTS for l, which is often l but not always
     *  thanks to Unicode escapes. */
    public String getEwtsForOther(String l) {
        return helpGetEwts(l);
    }

    private String helpGetEwts(String l) {
        if (l.length() == 1
            && ((l.charAt(0) >= THDLWylieConstants.PUA_MIN
                 && l.charAt(0) <= THDLWylieConstants.PUA_MAX)
                || 0 <= "\u0F01\u0F09\u0F0A\u0F10\u0F12\u0F13\u0F15\u0F16\u0F17\u0F18\u0F19\u0F1A\u0F1B\u0F1C\u0F1D\u0F1E\u0F1F\u0F2A\u0F2B\u0F2C\u0F2D\u0F2E\u0F2F\u0F30\u0F31\u0F32\u0F33\u0F36\u0F38\u0F86\u0F87\u0F88\u0F89\u0F8A\u0F8B\u0FBE\u0FBF\u0FC0\u0FC1\u0FC2\u0FC3\u0FC4\u0FC5\u0FC6\u0FC7\u0FC8\u0FC9\u0FCA\u0FCB\u0FCC\u0FCF\u5350\u534D".indexOf(l.charAt(0)))) {
            return UnicodeUtils.unicodeCodepointToString(l.charAt(0), false, "\\u", true);
        }
        if (false) {  // TODO(dchandler): it's too late in the game to do this.  EWTS->TMW is broken for \u0f00, \u0f02, and \u0f03 right now, fix that.
            if ("\u0f02".equals(l)) return "u~M`H";  // too long for a single hash key, see?
            if ("\u0f03".equals(l)) return "u~M`:";  // ditto
        }
        return l;
    }

    /** Returns l, since this is EWTS's traits class. */
    public String getEwtsForWowel(String l) { return l; }

    public TTshegBarScanner scanner() { return EWTSTshegBarScanner.instance(); }

    /** If needle is found in haystack, then haystack without the
     *  first instance of needle is returned.  Otherwise haystack
     *  itself is returned. */
    private static String removeFirstMatch(String haystack, String needle) {
        int ix;
        if ((ix = haystack.indexOf(needle)) >= 0) {
            StringBuffer sb = new StringBuffer(haystack);
            sb.replace(ix, ix + needle.length(), "");
            return sb.toString();
        }
        return haystack;
    }
    
    private static HashMap bestEwtsMap = null;
    private static String getBestEwtsForSingleWowel(String wowel) {
        // NOTE: Not MT-safe
        if (null == bestEwtsMap) {
            bestEwtsMap = new HashMap(20);
            // Unicode-escape sequences are handled early.  To be
            // correct, we must "unescape" here any Unicode escape to
            // whatever tibwn.ini has.  (TODO(dchandler): tibwn.ini
            // has this info, use that instead of duplicating it in
            // this code.)
            bestEwtsMap.put("\u0f18", THDLWylieConstants.U0F18);
            bestEwtsMap.put("\u0f19", THDLWylieConstants.U0F19);
            bestEwtsMap.put("\u0f35", THDLWylieConstants.U0F35);
            bestEwtsMap.put("\u0f37", THDLWylieConstants.U0F37);
            bestEwtsMap.put("\u0f39", THDLWylieConstants.WYLIE_TSA_PHRU);
            bestEwtsMap.put("\u0f3e", THDLWylieConstants.U0F3E);
            bestEwtsMap.put("\u0f3f", THDLWylieConstants.U0F3F);
            bestEwtsMap.put("\u0f84", THDLWylieConstants.U0F84);
            bestEwtsMap.put("\u0f86", THDLWylieConstants.U0F86);
            bestEwtsMap.put("\u0f87", THDLWylieConstants.U0F87);
            bestEwtsMap.put("\u0fc6", THDLWylieConstants.U0FC6);

            bestEwtsMap.put("\u0f71", THDLWylieConstants.A_VOWEL);
            bestEwtsMap.put("\u0f72", THDLWylieConstants.i_VOWEL);
            bestEwtsMap.put("\u0f74", THDLWylieConstants.u_VOWEL);
            bestEwtsMap.put("\u0f7a", THDLWylieConstants.e_VOWEL);
            bestEwtsMap.put("\u0f7b", THDLWylieConstants.ai_VOWEL);
            bestEwtsMap.put("\u0f7c", THDLWylieConstants.o_VOWEL);
            bestEwtsMap.put("\u0f7d", THDLWylieConstants.au_VOWEL);
            bestEwtsMap.put("\u0f7e", THDLWylieConstants.BINDU);
            bestEwtsMap.put("\u0f80", THDLWylieConstants.reverse_i_VOWEL);
            bestEwtsMap.put("\u0f81", THDLWylieConstants.reverse_I_VOWEL);

            bestEwtsMap.put("\u0f73", THDLWylieConstants.I_VOWEL);  // not in tibwn.ini
            bestEwtsMap.put("\u0f75", THDLWylieConstants.U_VOWEL);  // not in tibwn.ini
        }
        String mapping = (String)bestEwtsMap.get(wowel);
        if (null != mapping)
            return mapping;
        else
            return wowel;
    }

    public void getDuffForWowel(ArrayList duff, DuffCode preceding,
                                String wowel)
            throws ConversionException
    {
        boolean preceding_added[] = new boolean[] { false };
        String[] wowels = wowel.split("\\+");
        for (int i = 0; i < wowels.length; i++) {
            getDuffForSingleWowel(duff, preceding,
                                  getBestEwtsForSingleWowel(wowels[i]),
                                  preceding_added);
        }
    }

    /** Wowels can stack.  This works on a single wowel. */
    private void getDuffForSingleWowel(ArrayList duff, DuffCode preceding,
                                       String wowel, boolean preceding_added[])
            throws ConversionException
    {
        if (wowel.equals(THDLWylieConstants.WYLIE_aVOWEL)) { // TODO(dchandler): ka+o deserves at least a warning.  kaM, though, does not.  Do we handle it?
            TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.WYLIE_aVOWEL, preceding_added);
            wowel = "";
        } else {
            // We call these combining because the TMW font treats
            // such a vowel specially depending on the preceding glyph
            // with which it combines.
            String combining_wowels[] = new String[] {
                // order does not matter
                THDLWylieConstants.U_VOWEL,
                THDLWylieConstants.reverse_I_VOWEL,
                THDLWylieConstants.I_VOWEL,
                THDLWylieConstants.A_VOWEL,
                THDLWylieConstants.ai_VOWEL,
                THDLWylieConstants.reverse_i_VOWEL,
                THDLWylieConstants.i_VOWEL,
                THDLWylieConstants.e_VOWEL,
                THDLWylieConstants.o_VOWEL,
                THDLWylieConstants.au_VOWEL,
                THDLWylieConstants.u_VOWEL
            };
            for (int i = 0; i < combining_wowels.length; i++) {
                if (wowel.equals(combining_wowels[i])) {
                    TibTextUtils.getVowel(duff, preceding, combining_wowels[i],
                                          preceding_added);
                    wowel = removeFirstMatch(wowel, combining_wowels[i]);
                }
            }
        }
        // FIXME: Use TMW9.61, the "o'i" special combination, when appropriate.

        if (wowel.equals(THDLWylieConstants.BINDU)) {
            DuffCode last = null;
            if (!preceding_added[0]) {
                last = preceding;
            } else if (duff.size() > 0) {
                last = (DuffCode)duff.get(duff.size() - 1);
                duff.remove(duff.size() - 1); // getBindu will add it back...
                // TODO(DLC)[EWTS->Tibetan]: is this okay????  when is a bindu okay to be alone???
            }
            TibTextUtils.getBindu(duff, last);
            preceding_added[0] = true;
            wowel = removeFirstMatch(wowel, THDLWylieConstants.BINDU);
        }

        if (!preceding_added[0]) {
            duff.add(preceding);
            preceding_added[0] = true;
        }

        String standalone_wowels[] = new String[] {
            // order does not matter

            // This likely won't look good!  TMW has glyphs for [va]
            // and [fa], so use that transliteration if you care, not
            // [ph^] or [b^].
            THDLWylieConstants.WYLIE_TSA_PHRU,
            THDLWylieConstants.U0F35,
            THDLWylieConstants.U0F37,
            THDLWylieConstants.U0F7F,
            THDLWylieConstants.U0F82,
            THDLWylieConstants.U0F83,
            THDLWylieConstants.U0F86,
            THDLWylieConstants.U0F87,
            THDLWylieConstants.U0F19,
            THDLWylieConstants.U0F18,
            THDLWylieConstants.U0FC6,
            THDLWylieConstants.U0F3E,
            THDLWylieConstants.U0F3F,
            THDLWylieConstants.U0F84,
        };
        for (int i = 0; i < standalone_wowels.length; i++) {
            if (wowel.equals(standalone_wowels[i])) {
                ThdlDebug.verify(preceding_added[0]);
                duff.add(TibetanMachineWeb.getGlyph(standalone_wowels[i]));
                wowel = removeFirstMatch(wowel, standalone_wowels[i]);
            }
        }

        // We verify that no part of wowel is discarded.
        if (wowel.length() > 0) {
            throw new ConversionException(
                    "Full wowel was not handled, there remains: " + wowel);
        }

        // TODO(DLC)[EWTS->Tibetan]:: are bindus are screwed up in the
        // unicode output?  i see (with tmuni font) lone bindus
        // without glyphs to stack on
    }

    public String getUnicodeForWowel(String wowel) {
        if (THDLWylieConstants.WYLIE_aVOWEL.equals(wowel))
            return "";
        return helpGetUnicodeForWowel(wowel);
    }

    private String helpGetUnicodeForWowel(String wowel) {
        if (THDLWylieConstants.WYLIE_aVOWEL.equals(wowel))
            return null; // ko+a+e is invalid, e.g.
        if (wowel.length() == 1 && isUnicodeWowel(wowel.charAt(0))) {
            if ("\u0f75".equals(wowel))
                return "\u0f71\u0f74"; // \u0f75 is discouraged
            if ("\u0f81".equals(wowel))
                return "\u0f71\u0f80"; // \u0f81 is discouraged
            if ("\u0f73".equals(wowel))
                return "\u0f71\u0f72"; // \u0f73 is discouraged
            if ("\u0f79".equals(wowel))
                return "\u0fb3\u0f81"; // \u0f79 is discouraged
            if ("\u0f78".equals(wowel))
                return "\u0fb3\u0f80"; // \u0f78 is discouraged
            return wowel;
        }
        // handle o+u, etc.
        int i;
        if ((i = wowel.indexOf("+")) >= 0) {
            // recurse.

            // Chris Fynn says \u0f7c\u0f7c is different from \u0f7d.
            // So o+o is not the same as au.  e+e is not the same as
            // ai.
            String left = helpGetUnicodeForWowel(wowel.substring(0, i));
            String right = helpGetUnicodeForWowel(wowel.substring(i + 1));
            if (null != left && null != right)
                return left + right;
            else
                return null;
        } else {
            // Handle vowels.  (TODO(dchandler): tibwn.ini has this
            // info, use that instead of duplicating it in this code.)
            if (THDLWylieConstants.i_VOWEL.equals(wowel)) return "\u0f72";
            if (THDLWylieConstants.u_VOWEL.equals(wowel)) return "\u0f74";
            if (THDLWylieConstants.A_VOWEL.equals(wowel)) return "\u0f71";
            if (THDLWylieConstants.U_VOWEL.equals(wowel)) return "\u0f71\u0f74";  // \u0f75 is discouraged
            if (THDLWylieConstants.e_VOWEL.equals(wowel)) return "\u0f7a";
            if (THDLWylieConstants.o_VOWEL.equals(wowel)) return "\u0f7c";
            if (THDLWylieConstants.reverse_i_VOWEL.equals(wowel)) return "\u0f80";
            if (THDLWylieConstants.ai_VOWEL.equals(wowel)) return "\u0f7b";
            if (THDLWylieConstants.au_VOWEL.equals(wowel)) return "\u0f7d";
            if (THDLWylieConstants.reverse_I_VOWEL.equals(wowel)) return "\u0f71\u0f80";  // \u0f81 is discouraged
            if (THDLWylieConstants.I_VOWEL.equals(wowel)) return "\u0f71\u0f72";  // \u0f73 is discouraged

            // TODO(DLC)[EWTS->Tibetan]: what about \u0f3e and \u0f3f!!!!
            if (THDLWylieConstants.BINDU.equals(wowel)) return "\u0f7e";
            if (THDLWylieConstants.U0F7F.equals(wowel)) return "\u0f7f";
            if (THDLWylieConstants.U0F84.equals(wowel)) return "\u0f84";
            if (THDLWylieConstants.U0F83.equals(wowel)) return "\u0f83";
            if (THDLWylieConstants.U0F82.equals(wowel)) return "\u0f82";
            if (THDLWylieConstants.U0F37.equals(wowel)) return "\u0f37";
            if (THDLWylieConstants.U0F35.equals(wowel)) return "\u0f35";
            if (THDLWylieConstants.WYLIE_TSA_PHRU.equals(wowel)) return "\u0f39";

            return null;
        }
    }

    public String getUnicodeFor(String l, boolean subscribed) {

        // First, handle "\u0f71\u0f84\u0f86", "", "\u0f74", etc.
        {
            boolean already_done = true;
            for (int i = 0; i < l.length(); i++) {
                char ch = l.charAt(i);
                if ((ch < '\u0f00' || ch > '\u0fff')
                    && THDLWylieConstants.SAUVASTIKA != ch
                    && THDLWylieConstants.SWASTIKA != ch
                    && (ch < THDLWylieConstants.PUA_MIN || ch > THDLWylieConstants.PUA_MAX)  // TODO(DLC)[EWTS->Tibetan]: give a warning, though?  PUA isn't specified by the unicode standard after all.
                    && '\t' != ch
                    && '\n' != ch
                    && '\r' != ch) {
                    // TODO(DLC)[EWTS->Tibetan]: Is this the place
                    // where we want to interpret how newlines work???
                    already_done = false;
                    break;
                }
            }
            if (already_done)
                return l; // TODO(dchandler): \u0fff etc. are not valid code points, though.  Do we handle that well?
        }

 // TODO(DLC)[EWTS->Tibetan]:: vowels !subscribed could mean (a . i)???? I doubt it but test "i"->"\u0f68\u0f72" etc.

        if (subscribed) {
            if ("R".equals(l)) return "\u0fbc";
            if ("Y".equals(l)) return "\u0fbb";
            if ("W".equals(l)) return "\u0fba";

            // TODO(dchandler): use tibwn.ini -- it has this same info.

            // g+h etc. should not be inputs to this function, but for
            // completeness they're here.
            if ("k".equals(l)) return "\u0F90";
            if ("kh".equals(l)) return "\u0F91";
            if ("g".equals(l)) return "\u0F92";
            if ("g+h".equals(l)) return "\u0F93";
            if ("ng".equals(l)) return "\u0F94";
            if ("c".equals(l)) return "\u0F95";
            if ("ch".equals(l)) return "\u0F96";
            if ("j".equals(l)) return "\u0F97";
            if ("ny".equals(l)) return "\u0F99";
            if ("T".equals(l)) return "\u0F9A";
            if ("Th".equals(l)) return "\u0F9B";
            if ("D".equals(l)) return "\u0F9C";
            if ("D+h".equals(l)) return "\u0F9D";
            if ("N".equals(l)) return "\u0F9E";
            if ("t".equals(l)) return "\u0F9F";
            if ("th".equals(l)) return "\u0FA0";
            if ("d".equals(l)) return "\u0FA1";
            if ("d+h".equals(l)) return "\u0FA2";
            if ("n".equals(l)) return "\u0FA3";
            if ("p".equals(l)) return "\u0FA4";
            if ("ph".equals(l)) return "\u0FA5";
            if ("b".equals(l)) return "\u0FA6";
            if ("b+h".equals(l)) return "\u0FA7";
            if ("m".equals(l)) return "\u0FA8";
            if ("ts".equals(l)) return "\u0FA9";
            if ("tsh".equals(l)) return "\u0FAA";
            if ("dz".equals(l)) return "\u0FAB";
            if ("dz+h".equals(l)) return "\u0FAC";
            if ("w".equals(l)) return "\u0FAD"; // TODO(DLC)[EWTS->Tibetan]:: ???
            if ("zh".equals(l)) return "\u0FAE";
            if ("z".equals(l)) return "\u0FAF";
            if ("'".equals(l)) return "\u0FB0";
            if ("y".equals(l)) return "\u0FB1";
            if ("r".equals(l)) return "\u0FB2";
            if ("l".equals(l)) return "\u0FB3";
            if ("sh".equals(l)) return "\u0FB4";
            if ("Sh".equals(l)) return "\u0FB5";
            if ("s".equals(l)) return "\u0FB6";
            if ("h".equals(l)) return "\u0FB7";
            if ("a".equals(l)) return "\u0FB8";
            if ("k+Sh".equals(l)) return "\u0FB9";

            if ("f".equals(l)) return "\u0FA5\u0F39";
            if ("v".equals(l)) return "\u0FA6\u0F39";
            return null;
        } else {
            if ("R".equals(l)) return "\u0f6a";
            if ("Y".equals(l)) return "\u0f61";
            if ("W".equals(l)) return "\u0f5d";
            if ("//".equals(l)) return "\u0f0e";
            
            if (!TibetanMachineWeb.isKnownHashKey(l)) {
//                 System.err.println("Getting unicode for the following is hard: '"
//                                    + l + "' (pretty string: '"
//                                    + UnicodeUtils.unicodeStringToPrettyString(l)
//                                    + "'");
                ThdlDebug.noteIffyCode();
                return null;
            }
            String s = TibetanMachineWeb.getUnicodeForWylieForGlyph(l);
            if (null == s)
                ThdlDebug.noteIffyCode();
            return s;
        }
    }

    public String shortTranslitName() { return "EWTS"; }

    private boolean pairHasBadWowel(TPair p) {
        return (null != p.getRight()
                && !disambiguator().equals(p.getRight())
                && !"+".equals(p.getRight())
                && null == getUnicodeForWowel(p.getRight()));
    }
    public boolean isClearlyIllegal(TPair p) {
        if (pairHasBadWowel(p)) return true;
        if (p.getLeft() == null
        	&& (p.getRight() == null ||
        		(!disambiguator().equals(p.getRight())
        		 && !isWowel(p.getRight()))))
            return true;
        if ("+".equals(p.getLeft()))
            return true;
        if (p.getLeft() != null && isWowel(p.getLeft())
            && !aVowel().equals(p.getLeft())) // achen
            return true;
        return false;
    }

    public TPairList[] breakTshegBarIntoChunks(String tt, boolean sh) {
        if (sh) throw new IllegalArgumentException("Don't do that, silly!");
        try {
            return TPairListFactory.breakEWTSIntoChunks(tt);
        } catch (StackOverflowError e) {
            throw new IllegalArgumentException("Input too large[1]: " + tt);
        } catch (OutOfMemoryError e) {
            throw new IllegalArgumentException("Input too large[2]: " + tt);
        }
    }
    
    public boolean isACIP() { return false; }
    
    public boolean vowelAloneImpliesAChen() { return true; }
    
    public boolean vowelsMayStack() { return true; }

    public boolean isWowelThatRequiresAChen(String s) {
        // TODO(DLC)[EWTS->Tibetan]: not sure why we pick this subset.
        // Why don't we use a negative set of regular vowels like "i",
        // "o", etc.?
        return ((s.length() == 1
                 && (isUnicodeWowelThatRequiresAChen(s.charAt(0))))
                || THDLWylieConstants.BINDU.equals(s)
                || THDLWylieConstants.U0F35.equals(s)
                || THDLWylieConstants.U0F37.equals(s)
                || THDLWylieConstants.U0F7F.equals(s)
                || THDLWylieConstants.U0F82.equals(s)
                || THDLWylieConstants.U0F83.equals(s)
                || THDLWylieConstants.U0F84.equals(s)
                || THDLWylieConstants.WYLIE_TSA_PHRU.equals(s));
    }

    public boolean isUnicodeWowelThatRequiresAChen(char ch) {
        // TODO(DLC)[EWTS->Tibetan]: ask if 18 19 3e 3f combine only with digits
        return ("\u0f39\u0f35\u0f37\u0f18\u0f19\u0f3e\u0f3f\u0f86\u0f87\u0fc6".indexOf(ch) >= 0);
    }

    public boolean couldBeValidStack(TPairList pl) {
        StringBuffer hashKey = new StringBuffer();
        boolean allHavePlus = true;
        for (int i = 0; i < pl.size(); i++) {
            if (i + 1 < pl.size() && !"+".equals(pl.get(i).getRight()))
                allHavePlus = false;
            if (0 != hashKey.length())
                hashKey.append('-');
            hashKey.append(pl.get(i).getLeft());
        }
        return (allHavePlus
                || TibetanMachineWeb.hasGlyph(hashKey.toString())); // TODO(DLC)[EWTS->Tibetan]: test with smra and tsma and bdgya
    }

    public boolean stackingMustBeExplicit() { return true; }

    public String U0F7F() { return THDLWylieConstants.U0F7F; }

    public String U0F35() { return THDLWylieConstants.U0F35; }

    public String U0F37() { return THDLWylieConstants.U0F37; }
}
