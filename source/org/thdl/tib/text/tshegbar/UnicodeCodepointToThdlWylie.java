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

import org.thdl.tib.text.TibetanMachineWeb;

/** This noninstantiable class allows for converting from Unicode
 *  codepoints to THDL Extended Wylie.  It cannot be used for long
 *  stretches of text, though, as it is unaware of context, which is
 *  essential to understanding a non-trivial string of Tibetan
 *  Unicode.
 *
 *  <p>See the document by Nathaniel Garson and David Germano entitled
 *  <i>Extended Wylie Transliteration Scheme</i>.  Note that there are
 *  a couple of issues with the November 18, 2001 revision of that
 *  document; these issues are in the Bugs tracker at our SourceForge
 *  site.</p>
 *
 *  @see <a href="http://sourceforge.net/projects/thdltools">SourceForge site</a>
 *
 *  @author David Chandler */
public class UnicodeCodepointToThdlWylie {

    /** Returns the THDL extended Wylie for the very simple sequence
     *  x.  Returns null iff some (Unicode) char in s has no THDL
     *  extended Wylie representation.  This is unaware of context, so
     *  use it sparingly. */
    public static StringBuffer getThdlWylieForUnicodeString(String x) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < x.length(); i++) {
            String ew = getThdlWylieForUnicodeCodepoint(x.charAt(i));
            if (null == ew)
                return null;
            sb.append(ew);
        }
        return sb;
    }

    /** Returns the THDL extended Wylie for x, or null if there is
     *  none.  Understand that multiple Unicode code points (chars)
     *  map to the same THDL Extended Wylie representation.
     *  Understand also that the scrap of THDL Extended Wylie returned
     *  is only valid in certain contexts.  For example, not all
     *  consonants take ra-btags.  DLC NOW what about
     *  canonicalization? */
    public static String getThdlWylieForUnicodeCodepoint(char x) {
        // DLC FIXME: use tibwn.ini for this; don't duplicate effort!
        // See TibetanMachineWeb.java's UnicodeToTMW mapping and
        // follow that example.  At the very least, test it thoroughly
        // so that changes to tibwn.ini will cause these tests to
        // fail.

        switch (x) {
        case '\t': return "\t";
        case '\n': return "\n";
        case '\r': return "\r";
        case ' ': return "_";
        case '\u00a0': return "_";

        case '\u0F00': return "oM";
        case '\u0F01': return "\\u0F01";
        case '\u0F02': return "\\u0F02";
        case '\u0F03': return "\\u0F03";
        case '\u0F04': return "@";
        case '\u0F05': return "#";
        case '\u0F06': return "$";
        case '\u0F07': return "%";
        case '\u0F08': return "!";
        case '\u0F09': return "\\u0F09";
        case '\u0F0A': return "\\u0F0A";
        case '\u0F0B': return " ";
        case '\u0F0C': return "\\u00A0"; // AMP: Non-break space. Does Jskad support this?
        case '\u0F0D': return "/";
        case '\u0F0E': return "//"; // DLC FIXME: this is kind of a hack-- the Unicode standard says the spacing for this construct is different than the spacing for "\u0F0D\u0F0D"
        case '\u0F0F': return ";";

        case '\u0F10': return "\\u0F10";
        case '\u0F11': return "|";
        case '\u0F12': return "\\u0F12";
        case '\u0F13': return "\\u0F13";
        case '\u0F14': return ":";
        case '\u0F15': return "\\u0F15";
        case '\u0F16': return "\\u0F16";
        case '\u0F17': return "\\u0F17";
        case '\u0F18': return "\\u0F18";
        case '\u0F19': return "\\u0F19";
        case '\u0F1A': return "\\u0F1A";
        case '\u0F1B': return "\\u0F1B";
        case '\u0F1C': return "\\u0F1C";
        case '\u0F1D': return "\\u0F1D";
        case '\u0F1E': return "\\u0F1E";
        case '\u0F1F': return "\\u0F1F";

        case '\u0F20': return "0";
        case '\u0F21': return "1";
        case '\u0F22': return "2";
        case '\u0F23': return "3";
        case '\u0F24': return "4";
        case '\u0F25': return "5";
        case '\u0F26': return "6";
        case '\u0F27': return "7";
        case '\u0F28': return "8";
        case '\u0F29': return "9";
        case '\u0F2A': return "\\u0F2A";
        case '\u0F2B': return "\\u0F2B";
        case '\u0F2C': return "\\u0F2C";
        case '\u0F2D': return "\\u0F2D";
        case '\u0F2E': return "\\u0F2E";
        case '\u0F2F': return "\\u0F2F";

        case '\u0F30': return "\\u0F30";
        case '\u0F31': return "\\u0F31";
        case '\u0F32': return "\\u0F32";
        case '\u0F33': return "\\u0F33";
        case '\u0F34': return "=";
        case '\u0F35': return "~X";
        case '\u0F36': return "\\u0F36";
        case '\u0F37': return "X";
        case '\u0F38': return "\\u0F38";
        case '\u0F39': return "^";
        case '\u0F3A': return "<";
        case '\u0F3B': return ">";
        case '\u0F3C': return "(";
        case '\u0F3D': return ")";
        case '\u0F3E': return "}";
        case '\u0F3F': return "{";

        case '\u0F40': return "k";
        case '\u0F41': return "kh";
        case '\u0F42': return "g";
        case '\u0F43': return (getThdlWylieForUnicodeCodepoint('\u0F42')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY // DLC FIXME: is this right?
                               + getThdlWylieForUnicodeCodepoint('\u0FB7'));
        case '\u0F44': return "ng";
        case '\u0F45': return "c";
        case '\u0F46': return "ch";
        case '\u0F47': return "j";
            // skip
        case '\u0F49': return "ny";
        case '\u0F4A': return "T";
        case '\u0F4B': return "Th";
        case '\u0F4C': return "D";
        case '\u0F4D': return (getThdlWylieForUnicodeCodepoint('\u0F4C')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY // DLC FIXME: is this right?
                               + getThdlWylieForUnicodeCodepoint('\u0FB7'));
        case '\u0F4E': return "N";
        case '\u0F4F': return "t";

        case '\u0F50': return "th";
        case '\u0F51': return "d";
        case '\u0F52': return (getThdlWylieForUnicodeCodepoint('\u0F51')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY // DLC FIXME: is this right?
                               + getThdlWylieForUnicodeCodepoint('\u0FB7'));
        case '\u0F53': return "n";
        case '\u0F54': return "p";
        case '\u0F55': return "ph";
        case '\u0F56': return "b";
        case '\u0F57': return (getThdlWylieForUnicodeCodepoint('\u0F56')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY // DLC FIXME: is this right?
                               + getThdlWylieForUnicodeCodepoint('\u0FB7'));
        case '\u0F58': return "m";
        case '\u0F59': return "ts";
        case '\u0F5A': return "tsh";
        case '\u0F5B': return "dz";
        case '\u0F5C': return (getThdlWylieForUnicodeCodepoint('\u0F5B')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY // DLC FIXME: is this right?
                               + getThdlWylieForUnicodeCodepoint('\u0FB7'));
        case '\u0F5D': return "w";
        case '\u0F5E': return "zh";
        case '\u0F5F': return "z";

        case '\u0F60': return "'";
        case '\u0F61': return "y";
        case '\u0F62': return "r";
        case '\u0F63': return "l";
        case '\u0F64': return "sh";
        case '\u0F65': return "Sh";
        case '\u0F66': return "s";
        case '\u0F67': return "h";
        case '\u0F68': return "a"; // DLC: maybe the empty string is OK here because typing just 'i' into Jskad causes root letter \u0F68 to appear... yuck...
        case '\u0F69': return (getThdlWylieForUnicodeCodepoint('\u0F40')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY // DLC FIXME: is this right?
                               + getThdlWylieForUnicodeCodepoint('\u0FB5'));
        case '\u0F6A': return "r";
            // skip
            // skip
            // skip
            // skip
            // skip

            // skip
        case '\u0F71': return "A";
        case '\u0F72': return "i";
        case '\u0F73': return "I";
        case '\u0F74': return "u";
        case '\u0F75': return "U";
        case '\u0F76': return "r-i"; // DLC Ri or r-i?  I put in a bug report.
        case '\u0F77': return "r-I"; // DLC or RI?
        case '\u0F78': return "l-i";
        case '\u0F79': return "l-I";
        case '\u0F7A': return "e";
        case '\u0F7B': return "ai";
        case '\u0F7C': return "o";
        case '\u0F7D': return "au";
        case '\u0F7E': return "M";
        case '\u0F7F': return "H";

        case '\u0F80': return "-i";
        case '\u0F81': return "-I";
        case '\u0F82': return "~M`";
        case '\u0F83': return "~M"; // DLC unsupported in Jskad, and 0F82 too probably
        case '\u0F84': return "?";
        case '\u0F85': return "&";
        case '\u0F86': return "\\u0F86";
        case '\u0F87': return "\\u0F87";
        case '\u0F88': return "\\u0F88";
        case '\u0F89': return "\\u0F89";
        case '\u0F8A': return "\\u0F8A";
        case '\u0F8B': return "\\u0F8B";
            // skip
            // skip
            // skip
            // skip

        case '\u0F90': return "k";
        case '\u0F91': return "kh";
        case '\u0F92': return "g";
        case '\u0F93': return (getThdlWylieForUnicodeCodepoint('\u0F92')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY
                               + getThdlWylieForUnicodeCodepoint('\u0FB7'));
        case '\u0F94': return "ng";
        case '\u0F95': return "c";
        case '\u0F96': return "ch";
        case '\u0F97': return "j";
            // skip
        case '\u0F99': return "ny";
        case '\u0F9A': return "T";
        case '\u0F9B': return "Th";
        case '\u0F9C': return "D";
        case '\u0F9D': return (getThdlWylieForUnicodeCodepoint('\u0F9C')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY
                               + getThdlWylieForUnicodeCodepoint('\u0FB7'));
        case '\u0F9E': return "N";
        case '\u0F9F': return "t";

        case '\u0FA0': return "th";
        case '\u0FA1': return "d";
        case '\u0FA2': return (getThdlWylieForUnicodeCodepoint('\u0FA1')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY
                               + getThdlWylieForUnicodeCodepoint('\u0FB7'));
        case '\u0FA3': return "n";
        case '\u0FA4': return "p";
        case '\u0FA5': return "ph";
        case '\u0FA6': return "b";
        case '\u0FA7': return (getThdlWylieForUnicodeCodepoint('\u0FA6')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY
                               + getThdlWylieForUnicodeCodepoint('\u0FB7'));
        case '\u0FA8': return "m";
        case '\u0FA9': return "ts";
        case '\u0FAA': return "tsh";
        case '\u0FAB': return "dz";
        case '\u0FAC': return (getThdlWylieForUnicodeCodepoint('\u0FAB')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY
                               + getThdlWylieForUnicodeCodepoint('\u0FB7'));
        case '\u0FAD': return "w";
        case '\u0FAE': return "zh";
        case '\u0FAF': return "z";

        case '\u0FB0': return "'";
        case '\u0FB1': return "y";
        case '\u0FB2': return "r";
        case '\u0FB3': return "l";
        case '\u0FB4': return "sh";
        case '\u0FB5': return "Sh";
        case '\u0FB6': return "s";
        case '\u0FB7': return "h";
        case '\u0FB8': return "a"; // DLC see note on \u0F68 ...
        case '\u0FB9': return (getThdlWylieForUnicodeCodepoint('\u0F90')
                               + TibetanMachineWeb.WYLIE_SANSKRIT_STACKING_KEY
                               + getThdlWylieForUnicodeCodepoint('\u0FB5'));
        case '\u0FBA': return "w";
        case '\u0FBB': return "y";
        case '\u0FBC': return "r";
            // skip
        case '\u0FBE': return "\\u0FBE";
        case '\u0FBF': return "\\u0FBF";

        case '\u0FC0': return "\\u0FC0";
        case '\u0FC1': return "\\u0FC1";
        case '\u0FC2': return "\\u0FC2";
        case '\u0FC3': return "\\u0FC3";
        case '\u0FC4': return "\\u0FC4";
        case '\u0FC5': return "\\u0FC5";
        case '\u0FC6': return "\\u0FC6";
        case '\u0FC7': return "\\u0FC7";
        case '\u0FC8': return "\\u0FC8";
        case '\u0FC9': return "\\u0FC9";
        case '\u0FCA': return "\\u0FCA";
        case '\u0FCB': return "\\u0FCB";
        case '\u0FCC': return "\\u0FCC";
            // skip
            // skip
        case '\u0FCF': return "\\u0FCF"; // DLC i added this to the 'EWTS document misspeaks' bug report... null I think...

        default: {
            // This codepoint is in the range 0FD0-0FFF or is not in
            // the Tibetan range at all.  In either case, there is no
            // corresponding THDL Extended Wylie.
            return null;
        }
        } // end switch
    }
}

