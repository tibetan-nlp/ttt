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
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.tshegbar;

/** Provides handy Extended Wylie-inspired names for Unicode
 *  codepoints commonly used to represent Tibetan.  The consonant that
 *  the Extended Wylie text "ka" refers to is named EWC_ka as in "The
 *  Extended Wylie Consonant ka", the vowel represented in Extended
 *  Wylie by "i" is EWV_i, and so on.  There is at least one exception
 *  to the naming scheme, but exceptions are well-commented.
 *
 *  @see org.thdl.tib.text.tshegbar.LegalTshegBar
 *
 *  @author David Chandler */
public interface UnicodeConstants {

    /** Refers to unnormalized Unicode: */
    static final byte NORM_UNNORMALIZED = 0;
    /** Refers to Normalization Form C: */
    static final byte NORM_NFC = 1;
    /** Refers to Normalization Form KC: */
    static final byte NORM_NFKC = 2;
    /** Refers to Normalization Form D: */
    static final byte NORM_NFD = 3;
    /** Refers to Normalization Form KD: */
    static final byte NORM_NFKD = 4;
    /** Refers to Normalization Form THDL, which is NFD except for
        <code>U+0F77</code> and <code>U+0F79</code>, which are
        normalized according to NFKD.  This is the One True
        Normalization Form, as it leaves no precomposed codepoints and
        does not normalize <code>U+0F0C</code>. */
    static final byte NORM_NFTHDL = 5;


    /** for those times when you need a char to represent a
        non-existent codepoint */
    static final char EW_ABSENT = '\u0000';


    //
    // the thirty consonants, in alphabetical order:
    //

    /** first letter of the alphabet: */
    static final char EWC_ka = '\u0F40';

    static final char EWC_kha = '\u0F41';
    static final char EWC_ga = '\u0F42';
    static final char EWC_nga = '\u0F44';
    static final char EWC_ca = '\u0F45';
    static final char EWC_cha = '\u0F46';
    static final char EWC_ja = '\u0F47';
    static final char EWC_nya = '\u0F49';
    static final char EWC_ta = '\u0F4F';
    static final char EWC_tha = '\u0F50';
    static final char EWC_da = '\u0F51';
    static final char EWC_na = '\u0F53';
    static final char EWC_pa = '\u0F54';
    static final char EWC_pha = '\u0F55';
    static final char EWC_ba = '\u0F56';
    static final char EWC_ma = '\u0F58';
    static final char EWC_tsa = '\u0F59';
    static final char EWC_tsha = '\u0F5A';
    static final char EWC_dza = '\u0F5B';
    static final char EWC_wa = '\u0F5D';
    static final char EWC_zha = '\u0F5E';
    static final char EWC_za = '\u0F5F';
    /** Note the irregular name.  The Extended Wylie representation is
        <code>'a</code>. */
    static final char EWC_achung = '\u0F60';
    static final char EWC_ya = '\u0F61';
    static final char EWC_ra = '\u0F62';
    static final char EWC_la = '\u0F63';
    static final char EWC_sha = '\u0F64';
    static final char EWC_sa = '\u0F66';
    static final char EWC_ha = '\u0F67';
    /** achen, the 30th consonant (and, some say, the fifth vowel) DLC NOW FIXME: rename to EWC_achen */
    static final char EWC_a = '\u0F68';


    /** In the word for father, "pA lags", there is an a-chung (i.e.,
        <code>\u0F71</code>).  This is the constant for that little
        guy. */
    static final char EW_achung_vowel = '\u0F71';


    /* Four of the five vowels, some say, or, others say, "the four
       vowels": */
    /** "gi gu", the 'i' sound in the English word keep: */
    static final char EWV_i = '\u0F72';
    /** "zhabs kyu", the 'u' sound in the English word tune: */
    static final char EWV_u = '\u0F74';
    /** "'greng bu" (also known as "'greng po", and pronounced <i>dang-bo</i>), the 'a' sound in the English word gate: */
    static final char EWV_e = '\u0F7A';
    /** "na ro", the 'o' sound in the English word bone: */
    static final char EWV_o = '\u0F7C';


    /** subscribed form of EWC_wa, also known as wa-btags */
    static final char EWSUB_wa_zur = '\u0FAD';
    /** subscribed form of EWC_ya */
    static final char EWSUB_ya_btags = '\u0FB1';
    /** subscribed form of EWC_ra */
    static final char EWSUB_ra_btags = '\u0FB2';
    /** subscribed form of EWC_la */
    static final char EWSUB_la_btags = '\u0FB3';
}
