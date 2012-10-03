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
Library (THDL). Portions created by the THDL are Copyright 2003-2005 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

import java.math.BigInteger;
import java.util.ArrayList;

import org.thdl.tib.text.THDLWylieConstants;

/**
* This singleton class is able to break up Strings of EWTS text (for
* example, an entire sutra file) into tsheg bars, comments, etc.
* Non-Tibetan parts are segregated (so that consumers can ensure that
* they remain non-Tibetan), and Tibetan passages are broken up into
* tsheg bars.
*
* This is not public because you should use {@link EWTSTraits#scanner()}.
*
* @author David Chandler */
class EWTSTshegBarScanner extends TTshegBarScanner {

    /** Returns true iff ch can appear within an EWTS tsheg bar. */
    protected static boolean isValidInsideTshegBar(char ch) {
        // '\\' is absent, but should it be?  TODO(DLC)[EWTS->Tibetan]
        return ((ch >= '0' && ch <= '9')
                || (ch >= '\u0f71' && ch <= '\u0f84')
                || EWTSTraits.instance().isUnicodeConsonant(ch)
                || EWTSTraits.instance().isUnicodeWowel(ch)
                || (ch >= '\u0f20' && ch <= '\u0f33')
                || "khgncjytdpbmtstdzwzz'rlafvTDNSWYReuioIAUMHX?^\u0f39\u0f35\u0f37.+~'`-\u0f19\u0f18\u0f3f\u0f3e\u0f86\u0f87\u0f88".indexOf(ch) >= 0);
        // NOTE: We treat \u0f00 as punctuation, not something valid
        // inside a tsheg bar.  This is questionable, but since it is
        // a tsheg bar all by itself (almost always in practice,
        // anyway) and since it would've required code changes I
        // didn't want to make, that's how it is.
    }

  // TODO(dchandler): use jflex, javacc or something similar as much
  // as you can.  I don't think EWTS can be perfectly parsed by
  // javacc, by the way, but having several components in a pipeline
  // would likely make things more maintainable.
  //
  // NOTE: EWTS doesn't fully specify how Unicode escapes (e.g.,
  // [\\u0f20] should work).  When do you evaluate them?
  // Immediately like Java source files or later, say right before
  // outputting?  Our answer: immediately.  [\\u0f88+ka] becomes
  // hard to do otherwise.  This means we treat actual Unicode in a
  // way that a reader of the EWTS standard might not think about,
  // but actual Unicode is rare in the input
  // (TODO(DLC)[EWTS->Tibetan]: it's so rare that we ought to give a
  // warning/error when we see it).
  /** See the comment in TTshegBarScanner.  This does not find
      errors and warnings that you'd think of a parser finding (TODO(DLC)[EWTS->Tibetan]:
      DOES IT?). */
  public ArrayList scan(String s, StringBuffer errors, int maxErrors, // TODO(DLC)[EWTS->Tibetan]: ignored
                        boolean shortMessages, String warningLevel) {
    // the size depends on whether it's mostly Tibetan or mostly
    // Latin and a number of other factors.  This is meant to be
    // an underestimate, but not too much of an underestimate.
    ArrayList al = new ArrayList(s.length() / 10);

    StringBuffer sb = new StringBuffer(s);
    ExpandEscapeSequences(sb);
    int sl = sb.length();
    // TODO(DLC)[EWTS->Tibetan]:: '@#', in ewts->tmw, is not working (probably because)
    // TODO(DLC)[EWTS->Tibetan]:: '#', in ewts->tmw, is not working
    //
    // TODO(DLC)[EWTS->Tibetan]:: 'jamX one is not working in ewts->tmw mode in the sense that X appears under the last glyph of the three instead of the middle glyph
    //
    // TODO(DLC)[EWTS->Tibetan]:: dzaHsogs is not working
    for (int i = 0; i < sl; i++) {  // i is modified in the loop, also
      if (isValidInsideTshegBar(sb.charAt(i))) {
        StringBuffer tbsb = new StringBuffer();
        for (; i < sl; i++) {
          if (isValidInsideTshegBar(sb.charAt(i)))
            tbsb.append(sb.charAt(i));
          else {
            --i;
            break;
          }
        }
        al.add(new TString("EWTS", tbsb.toString(),
                           TString.TIBETAN_NON_PUNCTUATION));
      } else {
        // NOTE: It's questionable, but we treat
        // \u0f00 like punctuation because it was
        // easier coding that way.
        if (i + 1 < sl
            && sb.charAt(i) == '/'
            && sb.charAt(i + 1) == '/') {
          al.add(new TString("EWTS", "//",
                             TString.TIBETAN_PUNCTUATION));
          ++i;
        } else if ((sb.charAt(i) >= THDLWylieConstants.PUA_MIN
                    && sb.charAt(i) <= THDLWylieConstants.PUA_MAX)
                   || (sb.charAt(i) >= '\u0f00' && sb.charAt(i) <= '\u0f17')
                   || (sb.charAt(i) >= '\u0f1a' && sb.charAt(i) <= '\u0f1f')
                   || (sb.charAt(i) >= '\u0fbe' && sb.charAt(i) <= '\u0fcc')
                   || (sb.charAt(i) >= '\u0fcf' && sb.charAt(i) <= '\u0fd1')
                   || (THDLWylieConstants.SAUVASTIKA == sb.charAt(i))
                   || (THDLWylieConstants.SWASTIKA == sb.charAt(i))
                   || (" /;|!:=_@#$%<>(){}*&\r\n\t\u0f36\u0f38\u0f89\u0f8a\u0f8b\u00a0".indexOf(sb.charAt(i))
                       >= 0)) {
          al.add(new TString("EWTS", sb.substring(i, i+1),
                             TString.TIBETAN_PUNCTUATION));
        } else {
          String errMsg;
          al.add(new TString("EWTS",
                             errMsg
                             = ErrorsAndWarnings.getMessage(116,
                                                            shortMessages,
                                                            sb.substring(i, i + 1),
                                                            EWTSTraits.instance()),
                             TString.ERROR));
          if (null != errors) {
            errors.append("Offset " + i + ": ERROR " + errMsg + "\n");
          }
        }
      }
    }
    return al;
  }
    
    /** Modifies the EWTS in sb such that Unicode escape sequences are
     *  expanded. */
    public static void ExpandEscapeSequences(StringBuffer sb) {
    	int sl;
        for (int i = 0; i < (sl = sb.length()); i++) {
        	if (i + "\\u00000000".length() <= sl) {
                if (sb.charAt(i) == '\\' && sb.charAt(i + 1) == 'u' || sb.charAt(i + 1) == 'U') {
                    boolean isEscape = true;
                    for (int j = 0; j < "00000000".length(); j++) {
                        char ch =  sb.charAt(i + "\\u".length() + j);
                        if (!((ch <= '9' && ch >= '0')
                              || (ch <= 'F' && ch >= 'A')
                              || (ch <= 'f' && ch >= 'a'))) {
                            isEscape = false;
                            break;
                        }
                    }
                    if (isEscape) {
                    	long x = -1;
                    	try {
                    		BigInteger bigx = new java.math.BigInteger(sb.substring(i+2, i+10), 16);
                    		x = bigx.longValue();
							if (!(bigx.compareTo(new BigInteger("0", 16)) >= 0
								  && bigx.compareTo(new BigInteger("FFFFFFFF", 16)) <= 0))
								x = -1;
                    	} catch (NumberFormatException e) {
                    		// leave x == -1
                    	}
                        if (x >= 0 && x <= 0xFFFF) {
                            sb.replace(i, i + "\\uXXXXyyyy".length(), new String(new char[] { (char)x }));
                            continue;
                        } else if (x >= 0x00000000L
								   && x <= 0xFFFFFFFFL) {
// TODO(DLC)[EWTS->Tibetan]: do nothing?  test errors                        	al.add(new TString("EWTS", "Sorry, we don't yet support Unicode escape sequences above 0x0000FFFF!  File a bug.",
                        		   //TString.ERROR));
                        	i += "uXXXXYYYY".length();
                            continue;
                        }
                    }
                }
            }
            if (i + "\\u0000".length() <= sl) {
                if (sb.charAt(i) == '\\' && sb.charAt(i + 1) == 'u' || sb.charAt(i + 1) == 'U') {
                    boolean isEscape = true;
                    for (int j = 0; j < "0000".length(); j++) {
                        char ch =  sb.charAt(i + "\\u".length() + j);
                        if (!((ch <= '9' && ch >= '0')
                              || (ch <= 'F' && ch >= 'A')
                              || (ch <= 'f' && ch >= 'a'))) {
                            isEscape = false;
                            break;
                        }
                    }
                    if (isEscape) {
                        int x = -1;
                        try {
                            if (!((x = Integer.parseInt(sb.substring(i+2, i+6), 16)) >= 0x0000
                                  && x <= 0xFFFF))
                                x = -1;
                        } catch (NumberFormatException e) {
                            // leave x == -1
                        }
                        if (x >= 0) {
                            String replacement = String.valueOf((char)x);

                            if (false) {
                                // This would ruin EWTS->Unicode to
                                // help EWTS->TMW, so we don't do it.
                                // TODO(dchandler): Fix EWTS->TMW for
                                // \u0f02 and \u0f03.

                                // A nasty little HACK for you:
                                //
                                // TODO(dchandler): we may create "ga..u~M`H..ha" which may cause errors
                                String hack = null;
                                if ('\u0f02' == x) {
                                    hack = "u~M`H";  // hard-coded EWTS
                                } else if ('\u0f03' == x) {
                                    hack = "u~M`:";  // hard-coded EWTS
                                } else if ('\u0f00' == x) {
                                    hack = "oM";  // hard-coded EWTS
                                }
                                if (null != hack) {
                                    replacement = "." + hack + ".";  // hard-coded EWTS disambiguators
                                    i += replacement.length() - 1;
                                }
                            }
                            sb.replace(i, i + "\\uXXXX".length(), replacement);
                            continue;
                        }
                    }
                }
            }
        }
    }

    /** non-public because this is a singleton */
    protected EWTSTshegBarScanner() { }
    private static EWTSTshegBarScanner singleton = null;
    /** Returns the sole instance of this class. */
    public synchronized static EWTSTshegBarScanner instance() {
        if (null == singleton) {
            singleton = new EWTSTshegBarScanner();
        }
        return singleton;
    }
}
