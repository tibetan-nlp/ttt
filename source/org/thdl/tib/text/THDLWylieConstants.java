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

package org.thdl.tib.text;

/** This is where basic, static knowledge of THDL's Extended Wylie is
 *  housed.  <p>TODO(dchandler): tibwn.ini has all this, yes?  So
 *  extend TibetanMachineWeb if necessary and use a bunch of HashMaps
 *  there!  This is needless duplication.
 *  @see TibetanMachineWeb */
public interface THDLWylieConstants {
// TODO(DLC)[EWTS->Tibetan]: what about U+2638, mentioned in Section
// 9.11 "Tibetan" of the Unicode 4.0.1 standard?  Why doesn't EWTS
// mention it?  (Because TMW has no glyph for it, I bet.)  Do we
// handle it well?
/** The EWTS standard mentions this character specifically.  See
* http://www.symbols.com/encyclopedia/15/155.html to learn about
* its meaning as relates to Buddhism.
*/
	public static final char SAUVASTIKA = '\u534d';
/** The EWTS standard mentions this character specifically.  See
* http://www.symbols.com/encyclopedia/15/151.html to learn about
* its meaning as relates to Buddhism.
*/
	public static final char SWASTIKA = '\u5350';
/** EWTS has some glyphs not specified by Unicode in the
*  private-use area (PUA).  EWTS puts them in the range [PUA_MIN,
*  PUA_MAX].  (Note that \uf042 is the highest in use as of July
*  2, 2005.) */
	public static final char PUA_MIN = '\uf021';
/** EWTS has some glyphs not specified by Unicode in the
*  private-use area (PUA).  EWTS puts them in the range [PUA_MIN,
*  PUA_MAX].  (Note that \uf042 is the highest in use as of July
*  2, 2005.) */
	public static final char PUA_MAX = '\uf0ff';
/**
* the Wylie for U+0F3E
*/
	public static final String U0F3E = "}";
/**
* the Wylie for U+0F3F
*/
	public static final String U0F3F = "{";
/**
* the Wylie for U+0F86
*/
	public static final String U0F86 = "\\u0F86";
/**
* the Wylie for U+0F87
*/
	public static final String U0F87 = "\\u0F87";
/**
* the Wylie for U+0FC6
*/
	public static final String U0FC6 = "\\u0FC6";
/**
* the Wylie for U+0F18
*/
	public static final String U0F18 = "\\u0F18";
/**
* the Wylie for U+0F19
*/
	public static final String U0F19 = "\\u0F19";
/**
* the Wylie for U+0F84
*/
	public static final String U0F84 = "?";
/**
* the Wylie for U+0F7F
*/
	public static final String U0F7F = "H";
/**
* the Wylie for U+0F35
*/
	public static final String U0F35 = "~X";
/**
* the Wylie for U+0F37
*/
	public static final String U0F37 = "X";
/**
* the Wylie for U+0F82
*/
	public static final String U0F82 = "~M`";
/**
* the Wylie for U+0F83
*/
	public static final String U0F83 = "~M";
/**
* the Wylie for bindu/anusvara (U+0F7E)
*/
	public static final String BINDU = "M";
/**
* the Wylie for tsheg
*/
	public static final char TSHEG = ' '; //this character occurs in all ten TMW fonts
/**
* the Wylie for whitespace
*/
	public static final char SPACE = '_'; //this character occurs in all ten TMW fonts
/**
* the Sanskrit stacking separator used in Extended Wylie
*/
	public static final char WYLIE_SANSKRIT_STACKING_KEY = '+';
/**
* the Wylie disambiguating key, as a char
*/
	public static final char WYLIE_DISAMBIGUATING_KEY = '.';

/**
* the Wylie disambiguating key, as a String
*/
	public static final String WYLIE_DISAMBIGUATING_KEY_STRING
        = new String(new char[] { WYLIE_DISAMBIGUATING_KEY });
/**
* the Wylie for the invisible 'a' vowel
*/
	public static final String WYLIE_aVOWEL = "a";
/**
* the Wylie for U+0F39
*/
	public static final String WYLIE_TSA_PHRU = "^";
/**
* the Wylie for achung, \u0f60
*/
	public static final char ACHUNG_character = '\'';
/**
* the Wylie for achung, \u0f60
*/
	public static final String ACHUNG
        = new String(new char[] { ACHUNG_character });
/**
* the Wylie for the 28th of the 30 consonants, sa, \u0f66:
*/
	public static final String SA = "s";
/**
* the Wylie for the consonant ra, \u0f62:
*/
	public static final String RA = "r";
/**
* the Wylie for the 16th of the 30 consonants, ma, \u0f58:
*/
	public static final String MA = "m";
/**
* the Wylie for \u0f56:
*/
	public static final String BA = "b";
/**
* the Wylie for \u0f51:
*/
	public static final String DA = "d";
/**
* the Wylie for \u0f42:
*/
	public static final String GA = "g";
/**
* the Wylie for \u0f63:
*/
	public static final String LA = "l";
/**
* the Wylie for the 4th of the 30 consonants, nga, \u0f44:
*/
	public static final String NGA = "ng";
/**
* the Wylie for \u0f53:
*/
	public static final String NA = "n";
/**
* the Wylie for achen
*/
	public static final String ACHEN = "a";
/**
* the Wylie for gigu
*/
	public static final String i_VOWEL = "i";
/**
* the Wylie for zhebju
*/
	public static final String u_VOWEL = "u";
/**
* the Wylie for drengbu
*/
	public static final String e_VOWEL = "e";
/**
* the Wylie for naro
*/
	public static final String o_VOWEL = "o";
/**
* the Wylie for double drengbu
*/
	public static final String ai_VOWEL = "ai";
/**
* the Wylie for double naro
*/
	public static final String au_VOWEL = "au";
/**
* the Wylie for the subscript achung vowel
*/
	public static final String A_VOWEL = "A";
/**
* the Wylie for log yig gigu
*/
	public static final String reverse_i_VOWEL = "-i";
/**
* the Wylie for the vowel achung + gigu
*/
	public static final String I_VOWEL = "I";
/**
* the Wylie for the vowel achung + zhebju
*/
	public static final String U_VOWEL = "U";
/**
* the Wylie for the vowel achung + log yig gigu
*/
	public static final String reverse_I_VOWEL = "-I";
}
