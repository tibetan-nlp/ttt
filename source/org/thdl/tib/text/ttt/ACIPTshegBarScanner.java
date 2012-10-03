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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.thdl.util.ThdlOptions;

/**
* This singleton class is able to break up Strings of ACIP text (for
* example, an entire sutra file) into tsheg bars, comments, etc. Folio
* markers, comments, and the like are segregated (so that consumers
* can ensure that they remain in Latin), and Tibetan passages are
* broken up into tsheg bars.
*
* <p><b>FIXME:</b> We should be handling {KA\n\nKHA} vs. {KA\nKHA} in
* the parser, not here in the lexical analyzer.  That'd be cleaner,
* and more like how you'd do things if you used lex and yacc.
*
* This is not public because you should use {@link ACIPTraits#scanner()}.
*
* @author David Chandler */
class ACIPTshegBarScanner extends TTshegBarScanner {
    /** True if those ACIP snippets inside square brackets (e.g.,
        "[THIS]") are to be passed through into the output unmodified
        while retaining the brackets and if those ACIP snippets inside
        curly brackets (e.g., "{THAT}") are to be passed through into
        the output unmodified while dropping the brackets.  (Nesting
        of brackets is not allowed regardless.) */
    public static final boolean BRACKETED_SECTIONS_PASS_THROUGH_UNMODIFIED
        = true; // Robert Chilton's e-mail from April 2004 calls for 'true'

    /** Useful for testing.  Gives error messages on standard output
     *  about why we can't scan the document perfectly and exits with
     *  non-zero return code, or says "Good scan!" otherwise and exits
     *  with code zero.  <p>FIXME: not so efficient; copies the whole
     *  file into memory first. */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Bad args!  Need just the name of the ACIP text file.");
            System.exit(1);
        }
        StringBuffer errors = new StringBuffer();
        int maxErrors = 1000;
        ArrayList al = instance().scanFile(args[0], errors, maxErrors - 1,
                                           "true".equals(System.getProperty("org.thdl.tib.text.ttt.ACIPTshegBarScanner.shortMessages")),
                                           "All" /* memory hog */);

        if (null == al) {
            System.out.println(maxErrors + " or more errors occurred while scanning ACIP input file; is this");
            System.out.println("Tibetan or English input?");
            System.out.println("");
            System.out.println("First " + maxErrors + " errors scanning ACIP input file: ");
            System.out.println(errors);
            System.out.println("Exiting with " + maxErrors + " or more errors; please fix input file and try again.");
            System.exit(1);
        }
        if (errors.length() > 0) {
            System.out.println("Errors scanning ACIP input file: ");
            System.out.println(errors);
            System.out.println("Exiting; please fix input file and try again.");
            System.exit(1);
        }

        System.out.println("Good scan!");
        System.exit(0);
    }

    /** Helper.  Here because ACIP {MTHAR%\nKHA} should be treated the
        same w.r.t. tsheg insertion regardless of the lex errors and
        lex warnings found. */
    private static boolean lastNonExceptionalThingWasAdornmentOr(ArrayList al, int kind) {
        int i = al.size() - 1;
        while (i >= 0 && (((TString)al.get(i)).getType() == TString.WARNING
                          || ((TString)al.get(i)).getType() == TString.ERROR))
            --i;
        return (i >= 0 && // FIXME: or maybe i < 0 || ...
                (((TString)al.get(i)).getType() == kind
                 || ((TString)al.get(i)).getType() == TString.TSHEG_BAR_ADORNMENT));
    }

    /** Helper function that increments numErrorsArray[0] by one and
        adds an ERROR to the end of al and appends to the end of
        errors if it is nonnull.  (Nothing else is mutated.)
        @return true if and only if the error count has gone too high
        and caller should abort scanning */
    private static boolean queueError(int code,
                                      String translit,
                                      boolean shortMessages,
                                      int i,
                                      int numNewlines,
                                      int maxErrors,
                                      ArrayList al,
                                      StringBuffer errors,
                                      int numErrorsArray[]) {
        String errMsg;
        al.add(new TString("ACIP",
                           errMsg = ErrorsAndWarnings.getMessage(code,
                                                                 shortMessages,
                                                                 translit,
                                                                 ACIPTraits.instance()),
                           TString.ERROR));
        if (null != errors)
            errors.append("Offset " + ((i < 0) ? "END" : ("" + i))
                          + ((numNewlines == 0)
                             ? ""
                             : (" or maybe " + (i-numNewlines)))
                          + ": ERROR "
                          + errMsg + "\n");
        if (maxErrors >= 0 && ++numErrorsArray[0] >= maxErrors)
            return true;
        else
            return false;
    }


    // DLC FIXME "H:\n\n" becomes "H: \n\n", wrongly I think.  See
    // Tibetan! 5.1 section on formatting Tibetan texts.

    /** See the comment in TTshegBarScanner.  And note that this not
     *  only scans; it finds all the errors and warnings a parser
     *  would too, like "NYA x" and "(" and ")" and "/NYA" etc.  */
    public ArrayList scan(String s, StringBuffer errors, int maxErrors,
                          boolean shortMessages, String warningLevel) {
        // FIXME: Use less memory and time by not adding in the
        // warnings that are below threshold.

        // the size depends on whether it's mostly Tibetan or mostly
        // Latin and a number of other factors.  This is meant to be
        // an underestimate, but not too much of an underestimate.
        ArrayList al = new ArrayList(s.length() / 10);
        
        int numErrorsArray[] = new int[] { 0 };
        boolean waitingForMatchingIllegalClose = false;
        int sl = s.length();
        int currentType = TString.ERROR;
        int startOfString = 0;
        Stack bracketTypeStack = new Stack();
        int startSlashIndex = -1;
        int startParenIndex = -1;
        int numNewlines = 0;
        for (int i = 0; i < sl; i++) {
            if (i < startOfString) throw new Error("bad reset");
            char ch;
            ch = s.charAt(i);
            if (ch == '\n') ++numNewlines;
            if (TString.COMMENT == currentType && ch != ']') {
                if ('[' == ch) {
                    if (queueError(102, "" + ch,
                                   shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                        return null;
                }
                continue;
            }
            switch (ch) {
            case '}': // fall through...
            case ']':
                if (bracketTypeStack.empty()) {
                    // Error.
                    if (startOfString < i) {
                        al.add(new TString("ACIP", s.substring(startOfString, i),
                                           currentType));
                    }
                    if (!waitingForMatchingIllegalClose) {
                        if (queueError(103, "" + ch,
                                       shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                            return null;
                    }
                    waitingForMatchingIllegalClose = false;
                    if (queueError(BRACKETED_SECTIONS_PASS_THROUGH_UNMODIFIED ? 140 : 104,
                                   "" + ch,
                                   shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                        return null;
                    startOfString = i+1;
                    currentType = TString.ERROR;
                } else {
                    int stackTop = ((Integer)bracketTypeStack.pop()).intValue();

                    int end = startOfString;
                    if (TString.CORRECTION_START == stackTop) {

                        // This definitely indicates a new token.
                        char prevCh = s.charAt(i-1);
                        if (prevCh == '?')
                            end = i - 1;
                        else
                            end = i;
                        if (startOfString < end) {
                            al.add(new TString("ACIP", s.substring(startOfString, end),
                                               currentType));
                        }

                        if ('?' != prevCh) {
                            currentType = TString.PROBABLE_CORRECTION;
                        } else {
                            currentType = TString.POSSIBLE_CORRECTION;
                        }
                    }
                    al.add(new TString("ACIP", s.substring(end, i+1), currentType));
                    startOfString = i+1;
                    currentType = TString.ERROR;
                }
                break; // end ']','}' case

            case '{': // NOTE WELL: KX0016I.ACT, KD0095M.ACT, and a
                      // host of other ACIP files use {} brackets like
                      // [] brackets.  I treat both the same if
                      // BRACKETED_SECTIONS_PASS_THROUGH_UNMODIFIED
                      // is false.
                
                // fall through...
            case '[':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new TString("ACIP", s.substring(startOfString, i),
                                       currentType));
                    startOfString = i;
                    currentType = TString.ERROR;
                }
                if (BRACKETED_SECTIONS_PASS_THROUGH_UNMODIFIED) {
                    int indexPastCloseBracket = i;
                    boolean foundClose = false;
                    while (++indexPastCloseBracket < sl) {
                        if ((('[' == ch) ? '[' : '{')
                            == s.charAt(indexPastCloseBracket)) { // "[i am [nested], you see]" is not allowed.
                            waitingForMatchingIllegalClose = true;
                            if (queueError(141, "" + ch,
                                           shortMessages, indexPastCloseBracket, numNewlines, maxErrors, al, errors, numErrorsArray))
                                return null;
                        } else if ((('[' == ch) ? ']' : '}') == s.charAt(indexPastCloseBracket)) {
                            al.add(new TString("ACIP",
                                               s.substring(startOfString + (('[' == ch) ? 0 : 1),
                                                           indexPastCloseBracket + (('[' == ch) ? 1 : 0)),
                                               TString.LATIN));
                            startOfString = indexPastCloseBracket + 1;
                            i = startOfString - 1;
                            currentType = TString.ERROR;
                            foundClose = true;
                            break;
                        }
                    }
                    if (!foundClose) {
                        // FIXME: duplciated code, search for 106:
                        {
                            String inContext = s.substring(i, i+Math.min(sl-i, 10));
                            if (inContext.indexOf("\r") >= 0) {
                                inContext = inContext.substring(0, inContext.indexOf("\r"));
                            } else if (inContext.indexOf("\n") >= 0) {
                                inContext = inContext.substring(0, inContext.indexOf("\n"));
                            } else {
                                if (sl-i > 10) {
                                    inContext = inContext + "...";
                                }
                            }
                            if (queueError(139, inContext,
                                           shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                                return null;
                        }
                        if (queueError(117, "-*-END OF FILE-*-",
                                       shortMessages, -1, numNewlines, maxErrors, al, errors, numErrorsArray))
                            return null;
                        // we're done here:
                        {
                            i = sl;
                            startOfString = sl;
                        }
                    }
                } else {
                    String thingy = null;

                    if (i + "[DD]".length() <= sl
                        && (s.substring(i, i + "[DD]".length()).equals("[DD]")
                            || s.substring(i, i + "[DD]".length()).equals("{DD}"))) {
                        thingy = "[DD]";
                        currentType = TString.DD;
                    } else if (i + "[DD1]".length() <= sl
                               && (s.substring(i, i + "[DD1]".length()).equals("[DD1]")
                                   || s.substring(i, i + "[DD1]".length()).equals("{DD1}"))) {
                        thingy = "[DD1]";
                        currentType = TString.DD;
                    } else if (i + "[DD2]".length() <= sl
                               && (s.substring(i, i + "[DD2]".length()).equals("[DD2]")
                                   || s.substring(i, i + "[DD2]".length()).equals("{DD2}"))) {
                        thingy = "[DD2]";
                        currentType = TString.DD;
                    } else if (i + "[DDD]".length() <= sl
                               && (s.substring(i, i + "[DDD]".length()).equals("[DDD]")
                                   || s.substring(i, i + "[DDD]".length()).equals("{DDD}"))) {
                        thingy = "[DDD]";
                        currentType = TString.DD;
                    } else if (i + "[DR]".length() <= sl
                               && (s.substring(i, i + "[DR]".length()).equals("[DR]")
                                   || s.substring(i, i + "[DR]".length()).equals("{DR}"))) {
                        thingy = "[DR]";
                        currentType = TString.DR;
                    } else if (i + "[LS]".length() <= sl
                               && (s.substring(i, i + "[LS]".length()).equals("[LS]")
                                   || s.substring(i, i + "[LS]".length()).equals("{LS}"))) {
                        thingy = "[LS]";
                        currentType = TString.LS;
                    } else if (i + "[BP]".length() <= sl
                               && (s.substring(i, i + "[BP]".length()).equals("[BP]")
                                   || s.substring(i, i + "[BP]".length()).equals("{BP}"))) {
                        thingy = "[BP]";
                        currentType = TString.BP;
                    } else if (i + "[BLANK PAGE]".length() <= sl
                               && (s.substring(i, i + "[BLANK PAGE]".length()).equals("[BLANK PAGE]")
                                   || s.substring(i, i + "[BLANK PAGE]".length()).equals("{BLANK PAGE}"))) {
                        thingy = "[BLANK PAGE]";
                        currentType = TString.BP;
                    } else if (i + "[ BP ]".length() <= sl
                               && (s.substring(i, i + "[ BP ]".length()).equals("[ BP ]")
                                   || s.substring(i, i + "[ BP ]".length()).equals("{ BP }"))) {
                        thingy = "{ BP }"; // found in TD3790E2.ACT
                        currentType = TString.BP;
                    } else if (i + "[ DD ]".length() <= sl
                               && (s.substring(i, i + "[ DD ]".length()).equals("[ DD ]")
                                   || s.substring(i, i + "[ DD ]".length()).equals("{ DD }"))) {
                        thingy = "{ DD }"; // found in TD3790E2.ACT
                        currentType = TString.DD;
                    } else if (i + "[?]".length() <= sl
                               && (s.substring(i, i + "[?]".length()).equals("[?]")
                                   || s.substring(i, i + "[?]".length()).equals("{?}"))) {
                        thingy = "[?]";
                        currentType = TString.QUESTION;
                    } else {
                        //  We see comments appear not as [#COMMENT], but
                        //  as [COMMENT] sometimes.  We make special cases
                        //  for some English comments.  There's no need to
                        //  make this mechanism extensible, because you
                        //  can easily edit the ACIP text so that it uses
                        //  [#COMMENT] notation instead of [COMMENT].

                        String[] englishComments = new String[] {
                            "FIRST", "SECOND", // S5274I.ACT
                            "Additional verses added by Khen Rinpoche here are", // S0216M.ACT
                            "ADDENDUM: The text of", // S0216M.ACT
                            "END OF ADDENDUM", // S0216M.ACT
                            "Some of the verses added here by Khen Rinpoche include:", // S0216M.ACT
                            "Note that, in the second verse, the {YUL LJONG} was orignally {GANG LJONG},\nand is now recited this way since the ceremony is not only taking place in Tibet.", // S0216M.ACT
                            "Note that, in the second verse, the {YUL LJONG} was orignally {GANG LJONG},\r\nand is now recited this way since the ceremony is not only taking place in Tibet.", // S0216M.ACT
                            "text missing", // S6954E1.ACT
                            "INCOMPLETE", // TD3817I.INC
                            "MISSING PAGE", // S0935m.act
                            "MISSING FOLIO", // S0975I.INC
                            "UNCLEAR LINE", // S0839D1I.INC
                            "THE FOLLOWING TEXT HAS INCOMPLETE SECTIONS, WHICH ARE ON ORDER", // SE6260A.INC
                            "@DATA INCOMPLETE HERE", // SE6260A.INC
                            "@DATA MISSING HERE", // SE6260A.INC
                            "LINE APPARENTLY MISSING THIS PAGE", // TD4035I.INC
                            "DATA INCOMPLETE HERE", // TD4226I2.INC
                            "DATA MISSING HERE", // just being consistent
                            "FOLLOWING SECTION WAS NOT AVAILABLE WHEN THIS EDITION WAS\nPRINTED, AND IS SUPPLIED FROM ANOTHER, PROBABLY THE ORIGINAL:", // S0018N.ACT
                            "FOLLOWING SECTION WAS NOT AVAILABLE WHEN THIS EDITION WAS\r\nPRINTED, AND IS SUPPLIED FROM ANOTHER, PROBABLY THE ORIGINAL:", // S0018N.ACT
                            "THESE PAGE NUMBERS RESERVED IN THIS EDITION FOR PAGES\nMISSING FROM ORIGINAL ON WHICH IT WAS BASED", // S0018N.ACT
                            "THESE PAGE NUMBERS RESERVED IN THIS EDITION FOR PAGES\r\nMISSING FROM ORIGINAL ON WHICH IT WAS BASED", // S0018N.ACT
                            "PAGE NUMBERS RESERVED FROM THIS EDITION FOR MISSING\nSECTION SUPPLIED BY PRECEDING", // S0018N.ACT
                            "PAGE NUMBERS RESERVED FROM THIS EDITION FOR MISSING\r\nSECTION SUPPLIED BY PRECEDING", // S0018N.ACT
                            "SW: OK", // S0057M.ACT
                            "m:ok", // S0057M.ACT
                            "A FIRST ONE\nMISSING HERE?", // S0057M.ACT
                            "A FIRST ONE\r\nMISSING HERE?", // S0057M.ACT
                            "THE INITIAL PART OF THIS TEXT WAS INPUT BY THE SERA MEY LIBRARY IN\nTIBETAN FONT AND NEEDS TO BE REDONE BY DOUBLE INPUT", // S0195A1.INC
                            "THE INITIAL PART OF THIS TEXT WAS INPUT BY THE SERA MEY LIBRARY IN\r\nTIBETAN FONT AND NEEDS TO BE REDONE BY DOUBLE INPUT", // S0195A1.INC
                        };
                        boolean foundOne = false;
                        for (int ec = 0; ec < englishComments.length; ec++) {
                            if (i + 2 + englishComments[ec].length() <= sl
                                && (s.substring(i, i + 2 + englishComments[ec].length()).equals("[" + englishComments[ec] + "]")
                                    || s.substring(i, i + 2 + englishComments[ec].length()).equals("[" + englishComments[ec] + "]"))) {
                                al.add(new TString("ACIP", "[#" + englishComments[ec] + "]",
                                                   TString.COMMENT));
                                startOfString = i + 2 + englishComments[ec].length();
                                i = startOfString - 1;
                                foundOne = true;
                                break;
                            }
                        }
                        if (!foundOne && i+1 < sl && s.charAt(i+1) == '*') {
                            // Identify [*LINE BREAK?] as an English
                            // correction.  Every correction not on this
                            // list is considered to be Tibetan.
                            // FIXME: make this extensible via a config
                            // file or at least a System property (which
                            // could be a comma-separated list of these
                            // creatures.
                        
                            // If "LINE" is in the list below, then [*
                            // LINE], [* LINE?], [*LINE], [*LINE?], [*
                            // LINE OUT ?], etc. will be considered
                            // English corrections.  I.e., whitespace
                            // before and anything after doesn't prevent a
                            // match.
                            String[] englishCorrections = new String[] {
                                "LINE", // KD0001I1.ACT
                                "DATA", // KL0009I2.INC
                                "BLANK", // KL0009I2.INC
                                "NOTE", // R0001F.ACM
                                "alternate", // R0018F.ACE
                                "02101-02150 missing", // R1003A3.INC
                                "51501-51550 missing", // R1003A52.ACT
                                "BRTAGS ETC", // S0002N.ACT
                                "TSAN, ETC", // S0015N.ACT
                                "SNYOMS, THROUGHOUT", // S0016N.ACT
                                "KYIS ETC", // S0019N.ACT
                                "MISSING", // S0455M.ACT
                                "this", // S6850I1B.ALT
                                "THIS", // S0057M.ACT
                            };
                            int begin;
                            for (begin = i+2; begin < sl; begin++) {
                                if (!isWhitespace(s.charAt(begin)))
                                    break;
                            }
                            int end;
                            for (end = i+2; end < sl; end++) {
                                if (s.charAt(end) == ']')
                                    break;
                            }
                            int realEnd = end;
                            if (end < sl && s.charAt(end-1) == '?')
                                --realEnd;
                            if (end < sl && begin < realEnd) {
                                String interestingSubstring
                                    = s.substring(begin, realEnd);
                                for (int ec = 0; ec < englishCorrections.length; ec++) {
                                    if (interestingSubstring.startsWith(englishCorrections[ec])) {
                                        al.add(new TString("ACIP", s.substring(i, i+2),
                                                           TString.CORRECTION_START));
                                        al.add(new TString("ACIP", s.substring(i+2, realEnd),
                                                           TString.LATIN));
                                        if (s.charAt(end - 1) == '?') {
                                            al.add(new TString("ACIP", s.substring(end-1, end+1),
                                                               TString.POSSIBLE_CORRECTION));
                                        } else {
                                            al.add(new TString("ACIP", s.substring(end, end+1),
                                                               TString.PROBABLE_CORRECTION));
                                        }
                                        foundOne = true;
                                        startOfString = end+1;
                                        i = startOfString - 1;
                                        break;
                                    }
                                }
                            }
                        }
                        if (foundOne)
                            break;
                    }
                    if (null != thingy) {
                        al.add(new TString("ACIP", thingy,
                                           currentType));
                        startOfString = i + thingy.length();
                        i = startOfString - 1;
                    } else {
                        if (i + 1 < sl) {
                            char nextCh = s.charAt(i+1);
                            if ('*' == nextCh) {
                                currentType = TString.CORRECTION_START;
                                bracketTypeStack.push(new Integer(currentType));
                                al.add(new TString("ACIP", s.substring(i, i+2),
                                                   TString.CORRECTION_START));
                                currentType = TString.ERROR;
                                startOfString = i+2;
                                i = startOfString - 1;
                                break;
                            } else if ('#' == nextCh) {
                                currentType = TString.COMMENT;
                                bracketTypeStack.push(new Integer(currentType));
                                break;
                            }
                        }
                        // This is an error.  Sometimes [COMMENTS APPEAR
                        // WITHOUT # MARKS].  Though "... [" could cause
                        // this too.
                        if (waitingForMatchingIllegalClose) {
                            if (queueError(105, "" + ch,
                                           shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                                return null;
                        }
                        waitingForMatchingIllegalClose = true;
                        // FIXME: duplciated code, search for 139:
                        {
                            String inContext = s.substring(i, i+Math.min(sl-i, 10));
                            if (inContext.indexOf("\r") >= 0) {
                                inContext = inContext.substring(0, inContext.indexOf("\r"));
                            } else if (inContext.indexOf("\n") >= 0) {
                                inContext = inContext.substring(0, inContext.indexOf("\n"));
                            } else {
                                if (sl-i > 10) {
                                    inContext = inContext + "...";
                                }
                            }
                            if (queueError(106, inContext,
                                           shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                                return null;
                        }
                        startOfString = i + 1;
                        currentType = TString.ERROR;
                    }
                }
                break; // end '[','{' case

            case '@':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new TString("ACIP", s.substring(startOfString, i),
                                       currentType));
                    startOfString = i;
                    currentType = TString.ERROR;
                }

                // We look for {@N{AB}, @NN{AB}, ..., @NNNNNN{AB}},
                // {@[N{AB}], @[NN{AB}], ..., @[NNNNNN{AB}]},
                // {@N{AB}.N, @NN{AB}.N, ..., @NNNNNN{AB}.N}, {@N,
                // @NN, ..., @NNNNNN}, and {@{AB}N, @{AB}NN,
                // ... @{AB}NNNNNN} only, that is from one to six
                // digits.  Each of these folio marker format occurs
                // in practice.
                for (int numdigits = 6; numdigits >= 1; numdigits--) {
                    // @NNN{AB} and @NNN{AB}.N cases:
                    if (i+numdigits+1 < sl
                        && (s.charAt(i+numdigits+1) == 'A' || s.charAt(i+numdigits+1) == 'B')) {
                        boolean allAreNumeric = true;
                        for (int k = 1; k <= numdigits; k++) {
                            if (!isNumeric(s.charAt(i+k))) {
                                allAreNumeric = false;
                                break;
                            }
                        }
                        if (allAreNumeric) {
                            // Is this "@012B " or "@012B.3 "?
                            int extra;
                            if (i+numdigits+2 < sl && s.charAt(i+numdigits+2) == '.') {
                                if (!(i+numdigits+4 < sl && isNumeric(s.charAt(i+numdigits+3))
                                      && !isNumeric(s.charAt(i+numdigits+4)))) {
                                    String inContext = s.substring(i, i+Math.min(sl-i, 10));
                                    if (inContext.indexOf("\r") >= 0) {
                                        inContext = inContext.substring(0, inContext.indexOf("\r"));
                                    } else if (inContext.indexOf("\n") >= 0) {
                                        inContext = inContext.substring(0, inContext.indexOf("\n"));
                                    } else {
                                        if (sl-i > 10) {
                                            inContext = inContext + "...";
                                        }
                                    }
                                    if (queueError(107, inContext,
                                                   shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                                        return null;
                                    startOfString = i+numdigits+3;
                                    i = startOfString - 1;
                                    currentType = TString.ERROR;
                                    break;
                                }
                                if (i+numdigits+4 < sl
                                    && (s.charAt(i+numdigits+4) == '.'
                                        || s.charAt(i+numdigits+4) == 'A'
                                        || s.charAt(i+numdigits+4) == 'B'
                                        || s.charAt(i+numdigits+4) == 'a'
                                        || s.charAt(i+numdigits+4) == 'b'
                                        || isNumeric(s.charAt(i+numdigits+4)))) {
                                    String inContext = s.substring(i, i+Math.min(sl-i, 10));
                                    if (inContext.indexOf("\r") >= 0) {
                                        inContext = inContext.substring(0, inContext.indexOf("\r"));
                                    } else if (inContext.indexOf("\n") >= 0) {
                                        inContext = inContext.substring(0, inContext.indexOf("\n"));
                                    } else {
                                        if (sl-i > 10) {
                                            inContext = inContext + "...";
                                        }
                                    }
                                    if (queueError(108, inContext,
                                                   shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                                        return null;
                                    startOfString = i+1; // FIXME: skip over more?  test this code.
                                    currentType = TString.ERROR;
                                    break;
                                }
                                extra = 4;
                            } else {
                                extra = 2;
                            }
                            al.add(new TString("ACIP", s.substring(i, i+numdigits+extra),
                                               TString.FOLIO_MARKER));
                            startOfString = i+numdigits+extra;
                            i = startOfString - 1;
                            currentType = TString.ERROR;
                            break;
                        }
                    }
                    
                    // @{AB}NNN case:
                    if (i+numdigits+1 < sl
                        && (s.charAt(i+1) == 'A' || s.charAt(i+1) == 'B')) {
                        boolean allAreNumeric = true;
                        for (int k = 1; k <= numdigits; k++) {
                            if (!isNumeric(s.charAt(i+1+k))) {
                                allAreNumeric = false;
                                break;
                            }
                        }
                        if (allAreNumeric) {
                            al.add(new TString("ACIP", s.substring(i, i+numdigits+2),
                                               TString.FOLIO_MARKER));
                            startOfString = i+numdigits+2;
                            i = startOfString - 1;
                            currentType = TString.ERROR;
                            break;
                        }
                    }
                    
                    // @[NNN{AB}] case:
                    if (i+numdigits+3 < sl
                        && s.charAt(i+1) == '[' && s.charAt(i+numdigits+3) == ']'
                        && (s.charAt(i+numdigits+2) == 'A' || s.charAt(i+numdigits+2) == 'B')) {
                        boolean allAreNumeric = true;
                        for (int k = 1; k <= numdigits; k++) {
                            if (!isNumeric(s.charAt(i+1+k))) {
                                allAreNumeric = false;
                                break;
                            }
                        }
                        if (allAreNumeric) {
                            al.add(new TString("ACIP", s.substring(i, i+numdigits+4),
                                               TString.FOLIO_MARKER));
                            startOfString = i+numdigits+4;
                            i = startOfString - 1;
                            currentType = TString.ERROR;
                            break;
                        }
                    }
                    
                    // This case, @NNN, must come after the @NNN{AB} case.
                    if (i+numdigits+1 < sl && (s.charAt(i+numdigits+1) == ' '
                                               || s.charAt(i+numdigits+1) == '\n'
                                               || s.charAt(i+numdigits+1) == '\r')) {
                        boolean allAreNumeric = true;
                        for (int k = 1; k <= numdigits; k++) {
                            if (!isNumeric(s.charAt(i+k))) {
                                allAreNumeric = false;
                                break;
                            }
                        }
                        if (allAreNumeric) {
                            al.add(new TString("ACIP", s.substring(i, i+numdigits+1),
                                               TString.FOLIO_MARKER));
                            startOfString = i+numdigits+1;
                            i = startOfString - 1;
                            currentType = TString.ERROR;
                            break;
                        }
                    }
                }
                if (startOfString == i) {
                    String inContext = s.substring(i, i+Math.min(sl-i, 10));
                    if (inContext.indexOf("\r") >= 0) {
                        inContext = inContext.substring(0, inContext.indexOf("\r"));
                    } else if (inContext.indexOf("\n") >= 0) {
                        inContext = inContext.substring(0, inContext.indexOf("\n"));
                    } else {
                        if (sl-i > 10) {
                            inContext = inContext + "...";
                        }
                    }
                    if (queueError(109, inContext,
                                   shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                        return null;
                    startOfString = i+1;
                    currentType = TString.ERROR;
                }
                break; // end '@' case

            case '/':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new TString("ACIP", s.substring(startOfString, i),
                                       currentType));
                    startOfString = i;
                    currentType = TString.ERROR;
                }

                if (startSlashIndex >= 0) {
                    if (startSlashIndex + 1 == i) {
                        if (queueError(110, "" + ch,
                                       shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                            return null;
                    }
                    al.add(new TString("ACIP", s.substring(i, i+1),
                                       TString.END_SLASH));
                    startOfString = i+1;
                    currentType = TString.ERROR;
                    startSlashIndex = -1;
                } else {
                    startSlashIndex = i;
                    al.add(new TString("ACIP", s.substring(i, i+1),
                                       TString.START_SLASH));
                    startOfString = i+1;
                    currentType = TString.ERROR;
                }
                break; // end '/' case

            case '(':
            case ')':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new TString("ACIP", s.substring(startOfString, i),
                                       currentType));
                    startOfString = i;
                    currentType = TString.ERROR;
                }

                // We do not support nesting like (NYA (BA)).

                if (startParenIndex >= 0) {
                    if (ch == '(') {
                        if (queueError(111, "" + ch,
                                       shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                            return null;
                    } else {
                        al.add(new TString("ACIP", s.substring(i, i+1), TString.END_PAREN));
                        startParenIndex = -1;
                    }
                    startOfString = i+1;
                    currentType = TString.ERROR;
                } else {
                    if (ch == ')') {
                        if (queueError(112, "" + ch,
                                       shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                            return null;
                    } else {
                        startParenIndex = i;
                        al.add(new TString("ACIP", s.substring(i, i+1), TString.START_PAREN));
                    }
                    startOfString = i+1;
                    currentType = TString.ERROR;
                }
                break; // end '(',')' case

            case '?':
                if (bracketTypeStack.empty() || i+1>=sl
                    || (s.charAt(i+1) != ']' && s.charAt(i+1) != '}')) {
                    // The tsheg bar ends here; new token.
                    if (startOfString < i) {
                        al.add(new TString("ACIP", s.substring(startOfString, i),
                                           currentType));
                    }
                    if (queueError(113, "" + ch,
                                   shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                        return null;
                    startOfString = i+1;
                    currentType = TString.ERROR;
                } // else this is [*TR'A ?] or the like.
                break; // end '?' case


            case '.':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new TString("ACIP", s.substring(startOfString, i),
                                       currentType));
                    startOfString = i;
                    currentType = TString.ERROR;
                }
                // . is used for a non-breaking tsheg, such as in
                // {NGO.,} and {....,DAM}.  We give a warning unless ,
                // or ., or [A-Za-z] follows '.'.
                al.add(new TString("ACIP", s.substring(i, i+1),
                                   TString.TIBETAN_PUNCTUATION));
                if (ErrorsAndWarnings.isEnabled(510, warningLevel)
                    && (!(i + 1 < sl
                          && (s.charAt(i+1) == '.' || s.charAt(i+1) == ','
                              || (s.charAt(i+1) == '\r' || s.charAt(i+1) == '\n')
                              || (s.charAt(i+1) >= 'a' && s.charAt(i+1) <= 'z')
                              || (s.charAt(i+1) >= 'A' && s.charAt(i+1) <= 'Z'))))) {
                    al.add(new TString("ACIP",
                                       ErrorsAndWarnings.getMessage(510,
                                                                    shortMessages,
                                                                    "" + ch,
                                                                    ACIPTraits.instance()),
                                       TString.WARNING));
                }
                startOfString = i+1;
                break; // end '.' case

            // Classic tsheg bar enders:
            case ' ':
            case '\t':
            case '\r':
            case '\n':
            case ',':
            case '*':
            case ';':
            case '`':
            case '#':
            case '%':
            case 'x':
            case 'o':
            case '^':
            case '&':

                boolean legalTshegBarAdornment = false;
                // The tsheg bar ends here; new token.
                if (startOfString < i) {
                    if (currentType == TString.TIBETAN_NON_PUNCTUATION
                        && isTshegBarAdornment(ch))
                        legalTshegBarAdornment = true;
                    al.add(new TString("ACIP", s.substring(startOfString, i),
                                       currentType));
                }

                // Insert a tsheg if necessary.  ACIP files aren't
                // careful, so "KA\r\n" and "GA\n" appear where "KA
                // \r\n" and "GA \n" should appear.
                if (('\r' == ch
                     || ('\n' == ch && i > 0 && s.charAt(i - 1) != '\r'))
                    && !al.isEmpty()
                    && lastNonExceptionalThingWasAdornmentOr(al, TString.TIBETAN_NON_PUNCTUATION)) {
                    al.add(new TString("ACIP", " ", TString.TIBETAN_PUNCTUATION));
                }

                // "DANG,\nLHAG" is really "DANG, LHAG".  But always?  Not if you have "MDO,\n\nKA...".
                if (('\r' == ch
                     || ('\n' == ch && i > 0 && s.charAt(i - 1) != '\r'))
                    && !al.isEmpty()
                    && lastNonExceptionalThingWasAdornmentOr(al, TString.TIBETAN_PUNCTUATION)
                    && ((TString)al.get(al.size() - 1)).getText().equals(",")
                    && s.charAt(i-1) == ','
                    && (i + (('\r' == ch) ? 2 : 1) < sl
                        && (s.charAt(i+(('\r' == ch) ? 2 : 1)) != ch))) {
                    al.add(new TString("ACIP", " ", TString.TIBETAN_PUNCTUATION));
                }

                if ('^' == ch) {
                    // "^ GONG SA" is the same as "^GONG SA" or
                    // "^\r\nGONG SA".  But "^\n\nGONG SA" is
                    // different -- that has a true line break in the
                    // output between ^ and GONG.  We give an error if
                    // ^ isn't followed by an alphabetical character.
                    
                    boolean bad = false;
                    if (i + 1 < sl && isAlpha(s.charAt(i+1))) {
                        // leave i alone
                    } else if (i + 2 < sl && (' ' == s.charAt(i+1)
                                              || '\r' == s.charAt(i+1)
                                              || '\n' == s.charAt(i+1))
                               && isAlpha(s.charAt(i+2))) {
                        ++i;
                    } else if (i + 3 < sl && '\r' == s.charAt(i+1)
                               && '\n' == s.charAt(i+2)
                               && isAlpha(s.charAt(i+3))) {
                        i += 2;
                    } else {
                        bad = true;
                    }
                    if (!bad)
                        al.add(new TString("ACIP", "^", TString.TIBETAN_PUNCTUATION));
                    else {
                        if (queueError(131, "^",
                                       shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                            return null;
                    }
                } else {
                    // Don't add in a "\r\n" or "\n" unless there's a
                    // blank line.
                    boolean rn = false;
                    boolean realNewline = false;
                    if (('\n' != ch && '\r' != ch)
                        || (realNewline
                            = ((rn = ('\n' == ch && i >= 3 && s.charAt(i-3) == '\r' && s.charAt(i-2) == '\n' && s.charAt(i-1) == '\r'))
                               || ('\n' == ch && i >= 1 && s.charAt(i-1) == '\n')))) {
                        for (int h = 0; h < (realNewline ? 2 : 1); h++) {
                            if (isTshegBarAdornment(ch) && !legalTshegBarAdornment) {
                                if (queueError(132, "" + ch,
                                               shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                                    return null;
                            } else {
                                al.add(new TString("ACIP", rn ? s.substring(i - 1, i+1) : s.substring(i, i+1),
                                                   (legalTshegBarAdornment
                                                    ? TString.TSHEG_BAR_ADORNMENT
                                                    : TString.TIBETAN_PUNCTUATION)));
                            }
                        }
                    }
                    if ('%' == ch
                        && ErrorsAndWarnings.isEnabled(504, warningLevel)) {
                        al.add(new TString("ACIP",
                                           ErrorsAndWarnings.getMessage(504,
                                                                        shortMessages,
                                                                        "" + ch,
                                                                        ACIPTraits.instance()),
                                           TString.WARNING));
                    }
                }
                startOfString = i+1;
                currentType = TString.ERROR;
                break; // end TIBETAN_PUNCTUATION | TSHEG_BAR_ADORNMENT case

            default:
                if (!bracketTypeStack.empty()) {
                    int stackTop = ((Integer)bracketTypeStack.peek()).intValue();
                    if (TString.CORRECTION_START == stackTop && '?' == ch) {
                        // allow it through...
                        break;
                    }
                }
                if (i+1 == sl && 26 == (int)ch)
                    // Silently allow the last character to be
                    // control-Z (sometimes printed as ^Z), which just
                    // marks end of file.
                    break;
                if (!(isNumeric(ch) || isAlpha(ch))) {
                    if (startOfString < i) {
                        al.add(new TString("ACIP", s.substring(startOfString, i),
                                           currentType));
                    }
                    if ((int)ch == 65533) {
                        if (queueError(114, "unknown character",
                                       shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                            return null;
                    } else if ('\\' == ch) {
                        int x = -1;
                        if (!ThdlOptions.getBooleanOption("thdl.tib.text.disallow.unicode.character.escapes.in.acip")
                            && i + 5 < sl && 'u' == s.charAt(i+1)) {
                            try {
                                if (!((x = Integer.parseInt(s.substring(i+2, i+6), 16)) >= 0x0000 && x <= 0xFFFF))
                                    x = -1;
                            } catch (NumberFormatException e) {
                                // Though this is unlikely to be
                                // legal, we allow it through.
                                // (FIXME: warn.)
                            }
                        }
                        if (x >= 0) {
                            al.add(new TString("ACIP", new String(new char[] { (char)x }),
                                               TString.UNICODE_CHARACTER));
                            i += "uXXXX".length();
                            startOfString = i+1;
                            break;
                        } else {
                            if (queueError(115, "\\",
                                           shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                                return null;
                        }
                    } else {
                        if (queueError(116, "" + ch,
                                       shortMessages, i, numNewlines, maxErrors, al, errors, numErrorsArray))
                            return null;
                    }
                    startOfString = i+1;
                    currentType = TString.ERROR;
                } else {
                    // Continue through the loop.
                    if (TString.ERROR == currentType)
                        currentType = TString.TIBETAN_NON_PUNCTUATION;
                }
                break; // end default case
            }
        }
        if (startOfString < sl) {
            al.add(new TString("ACIP", s.substring(startOfString, sl),
                               currentType));
        }
        if (waitingForMatchingIllegalClose) {
            if (queueError(117, "-*-END OF FILE-*-",
                           shortMessages, -1, numNewlines, maxErrors, al, errors, numErrorsArray))
                return null;
        }
        if (!bracketTypeStack.empty()) {
            if (queueError(((TString.COMMENT == currentType) ? 118 : 119),
                           "-*-END OF FILE-*-",
                           shortMessages, -1, numNewlines, maxErrors, al, errors, numErrorsArray))
                return null;
        }
        if (startSlashIndex >= 0) {
            if (queueError(120, "-*-END OF FILE-*-",
                           shortMessages, -1, numNewlines, maxErrors, al, errors, numErrorsArray))
                return null;
        }
        if (startParenIndex >= 0) {
            if (queueError(121, "-*-END OF FILE-*-",
                           shortMessages, -1, numNewlines, maxErrors, al, errors, numErrorsArray))
                return null;
        }
        return al;
    }

    /** See implementation. */
    private static boolean isNumeric(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /** See implementation. */
    private static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    /** See implementation. */
    private static boolean isTshegBarAdornment(char ch) {
        return (ch == '%' || ch == 'o' || ch == 'x');
        // ^ is a pre-adornment; these are post-adornments.
    }

    /** See implementation. */
    private static boolean isAlpha(char ch) {
        return ch == '\'' // 23rd consonant

            // combining punctuation, vowels:
            || ch == 'm'
            || ch == ':'
            // FIXME: we must treat this guy like a vowel, a special vowel that numerals can take on.  Until then, warn.  See bug 838588          || ch == '\\'

            || ch == '-'
            || ch == '+'
            || ((ch >= 'A' && ch <= 'Z') && ch != 'X' && ch != 'Q' && ch != 'F')
            || ch == 'i'
            || ch == 't'
            || ch == 'h'
            || ch == 'd'
            || ch == 'n'
            || ch == 's'
            || ch == 'h';
    }

    /** non-public because this is a singleton */
    protected ACIPTshegBarScanner() { }
    private static ACIPTshegBarScanner singleton = null;
    /** Returns the sole instance of this class. */
    public synchronized static ACIPTshegBarScanner instance() {
        if (null == singleton) {
            singleton = new ACIPTshegBarScanner();
        }
        return singleton;
    }
}
