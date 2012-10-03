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

package org.thdl.tib.text.ttt;

import java.util.HashSet;

import org.thdl.tib.text.THDLWylieConstants;
import org.thdl.tib.text.tshegbar.UnicodeUtils;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlOptions;

/**
* An TString is some Latin text and a type, the type stating whether
* said text is Latin (usually English) or transliteration of Tibetan,
* which transliteration system (ACIP or EWTS), and which particular
* kind.  Scanning errors are also encoded as TStrings using a special
* type.
*
* <p><em>Note well</em> that when parsing ACIP, certain types of
* TStrings (corrections, comments, question, dd, bp, etc.) will not be
* encountered if {@link
* ACIPTshegBarScanner#BRACKETED_SECTIONS_PASS_THROUGH_UNMODIFIED} is
* true.</p>
*
* @author David Chandler */
public class TString {
    private int type;
    private String text;
    // "EWTS" or "ACIP", interned (for quick, '==' equality checking:
    private String encoding;

    /** Returns "EWTS" if this TString is encoded in EWTS, or,
        otherwise, "ACIP" if this TString is encoded in ACIP.  Returns
        an interned string for quick equality checking via the
        <code>==</code> operator. */
    public String getEncoding() {
	return encoding;
    }
	
    /** Returns true if and only if a TString with type <i>type</i>
     *  is to be converted to something other than Tibetan text.
     *  (Chinese Unicode, Latin, etc. all qualify as non-Tibetan.) */
    public boolean isLatin() {
        char ch;
        return (type != TIBETAN_NON_PUNCTUATION
                && type != TIBETAN_PUNCTUATION
                && type != TSHEG_BAR_ADORNMENT
                && type != START_PAREN
                && type != END_PAREN
                && type != START_SLASH
                && type != END_SLASH
                && (type != UNICODE_CHARACTER
                    || !(UnicodeUtils.isInTibetanRange(ch = getText().charAt(0))
                         || (ch >= THDLWylieConstants.PUA_MIN
                             && ch <= THDLWylieConstants.PUA_MAX))));
    }

    /** For ACIP [#COMMENTS] and EWTS (DLC FIXME: what are EWTS comments?) */
    public static final int COMMENT = 0;
    /** For Folio markers like @012B in ACIP */
    public static final int FOLIO_MARKER = 1;
    /** For Latin letters and numbers etc.  [*LINE BREAK?] uses this,
     *  for example.  Or in EWTS, \f uses this. */
    public static final int LATIN = 2;
    /** For Tibetan letters and numbers etc. */
    public static final int TIBETAN_NON_PUNCTUATION = 3;
    /** For tshegs, whitespace and the like, but not combining
     *  punctutation like ACIP %, o, :, m, and x */
    public static final int TIBETAN_PUNCTUATION = 4;
    /** For the start of a [*probable correction] or [*possible correction?] in ACIP */
    public static final int CORRECTION_START = 5;
    /** Denotes the end of a [*probable correction] in ACIP */
    public static final int PROBABLE_CORRECTION = 6;
    /** Denotes the end of a [*possible correction?] in ACIP*/
    public static final int POSSIBLE_CORRECTION = 7;
    /** For [BP] -- blank page in ACIP*/
    public static final int BP = 8;
    /** For [LS] -- Lanycha script on page in ACIP*/
    public static final int LS = 9;
    /** For [DR] -- picture (without caption) on page in ACIP*/
    public static final int DR = 10;
    /** For [DD], [DDD], [DD1], [DD2], et cetera -- picture with caption on page in ACIP */
    public static final int DD = 11;
    /** For [?] in ACIP */
    public static final int QUESTION = 12;
    /** For the first / in /NYA/ in ACIP */
    public static final int START_SLASH = 13;
    /** For the last / in /NYA/ in ACIP */
    public static final int END_SLASH = 14;
    /** For the opening ( in (NYA) in ACIP */
    public static final int START_PAREN = 15;
    /** For the closing ) in (NYA) in ACIP */
    public static final int END_PAREN = 16;
    /** For things that may not be legal syntax, such as {KA . KHA} */
    public static final int WARNING = 17;
    /** For ACIP %, o, and x or EWTS (DLC FIXME: what are EWTS adornments?) */
    public static final int TSHEG_BAR_ADORNMENT = 18;
    /** For "\\uMNOP", this TString will contain the string that has
        just the sole character "\\uMNOP". */
    public static final int UNICODE_CHARACTER = 19;
    /** For things that are not legal syntax, such as a file that
     *  contains just "[# HALF A COMMEN".  THIS MUST COME LAST. */
    public static final int ERROR = 20;

    /** Returns the type of this string, which is one of the
        enumerated integer static final members of this class. */
    public int getType() {
        return type;
    }

    /** Returns the non-null, non-empty String of text associated with
     *  this string. */
    public String getText() {
        return text;
    }

    private void setType(int t) {
        if (t < COMMENT || t > ERROR)
            throw new IllegalArgumentException("Bad type");
        type = t;
    }

    private void setText(String t) {
        if (t == null || "".equals(t))
            throw new IllegalArgumentException("null or empty text, DD should have text [DD] e.g.");
        text = t;
    }

    /** Don't instantiate using this constructor. */
    private TString() { }

    /** Creates a new TString with source text <i>text</i>, encoded
     *  using the Roman transliteration system specified by
     *  <i>encoding</i> (see {@link #getEncoding()}) and type
     *  <i>type</i> being a characterization like {@link #DD}. */
    public TString(String encoding, String text, int type) {
	this.encoding = encoding;
        setType(type);
        String ftext = (TIBETAN_NON_PUNCTUATION == type)
            ? MidLexSubstitution.getFinalValueForTibetanNonPunctuationToken(text)
            : text;
        // FIXME: assert these
        ThdlDebug.verify(type != UNICODE_CHARACTER || text.length() == 1);
        ThdlDebug.verify("EWTS" == encoding || "ACIP" == encoding);
        setText(ftext);
        if ((outputAllTshegBars || outputUniqueTshegBars) && TIBETAN_NON_PUNCTUATION == type)
            outputTshegBar(ftext);
    }

    /** Prints x to standard error if and only if we have never
        encountered x before. */
    private static void outputTshegBar(String x) {
        if (outputAllTshegBars) {
            System.err.println(outputTshegBarsPrefix + x);
        } else if (outputUniqueTshegBars) {
            if (!tshegBars.contains(x)) {
                tshegBars.add(x);
                System.err.println(outputTshegBarsPrefix + x);
            }
        }
    }

    /** For generating frequency info: */
    private static boolean outputAllTshegBars
        = ThdlOptions.getBooleanOption("org.thdl.tib.text.ttt.OutputAllTshegBars");

    /** For generating info about which tsheg bars were converted, but
        not how many times: */
    private static boolean outputUniqueTshegBars
        = ThdlOptions.getBooleanOption("org.thdl.tib.text.ttt.OutputUniqueTshegBars");

    /** Affects what appears on the console when either {@link
        #outputUniqueTshegBars} or {@link #outputAllTshegBars} is in
        use. */
    private static String outputTshegBarsPrefix
        = ThdlOptions.getStringOption("org.thdl.tib.text.ttt.PrefixForOutputTshegBars", "");

    private static final HashSet tshegBars = new HashSet();

    public String toString() {
        String typeString = "HUH?????";
        if (type == COMMENT) typeString = "COMMENT";
        if (type == FOLIO_MARKER) typeString = "FOLIO_MARKER";
        if (type == LATIN) typeString = "LATIN";
        if (type == TIBETAN_NON_PUNCTUATION) typeString = "TIBETAN_NON_PUNCTUATION";
        if (type == TIBETAN_PUNCTUATION) typeString = "TIBETAN_PUNCTUATION";
        if (type == CORRECTION_START) typeString = "CORRECTION_START";
        if (type == PROBABLE_CORRECTION) typeString = "PROBABLE_CORRECTION";
        if (type == POSSIBLE_CORRECTION) typeString = "POSSIBLE_CORRECTION";
        if (type == BP) typeString = "BP";
        if (type == LS) typeString = "LS";
        if (type == DR) typeString = "DR";
        if (type == DD) typeString = "DD";
        if (type == QUESTION) typeString = "QUESTION";
        if (type == START_SLASH) typeString = "START_SLASH";
        if (type == END_SLASH) typeString = "END_SLASH";
        if (type == START_PAREN) typeString = "START_PAREN";
        if (type == END_PAREN) typeString = "END_PAREN";
        if (type == WARNING) typeString = "WARNING";
        if (type == TSHEG_BAR_ADORNMENT) typeString = "TSHEG_BAR_ADORNMENT";
        if (type == UNICODE_CHARACTER) typeString = "UNICODE_CHARACTER";
        if (type == ERROR) typeString = "ERROR";
        return typeString + ":{" + getText() + "}";
    }
}
