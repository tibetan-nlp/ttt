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

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.thdl.tib.text.DuffCode;
import org.thdl.tib.text.THDLWylieConstants;
import org.thdl.tib.text.TibetanDocument;
import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlOptions;

// TODO(DLC)[EWTS->Tibetan]: THis class is broken for ewts.  But kill this class unless it needs to exist.
/**
* This class is able to convert an ACIP file into Tibetan Machine Web
* and an ACIP file into Unicode.  ACIP->Unicode should yield the same
* results as ACIP->TMW followed by TMW->Unicode (FIXME: test it!)
* @author David Chandler
*/
public class TConverter {

    /** Command-line converter for testing only -- use
     *  org.thdl.tib.input.TibetanConverter for production work.
     *  Gives error messages on standard output about why we can't
     *  convert the document perfectly and exits with non-zero return
     *  code, or is silent otherwise and exits with code zero.
     *
     *  <p>FIXME: not so efficient; copies the whole file into memory
     *  first. */
    public static void main(String[] args)
        throws IOException
    {
        // We don't want to load the TM or TMW font files ourselves:
        ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);
        ThdlOptions.setUserPreference("thdl.rely.on.system.tm.fonts", true);
        ThdlOptions.setUserPreference("thdl.debug", true);

        // Only developers should use this.
        if (!ThdlOptions.getBooleanOption("thdl.debug")) {
            System.err.println("Use org.thdl.tib.input.TibetanConverter for production work, not TConverter.");
            System.exit(1);
        }

        boolean verbose = true;
        if (args.length != 1) {
            System.out.println("Bad args!  Need just the name of the ACIP text file.");
        }
        StringBuffer errors = new StringBuffer();
        int maxErrors = 1000; // FIXME: make this PER CAPITA or else large ACIP Tibetan files are not converted for fear that they are English
        boolean shortMessages = false;
        String warningLevel = "Most";
        ArrayList al
            = ACIPTraits.instance().scanner().scanFile(args[0], errors,
                                                       maxErrors - 1,
                                                       shortMessages,
                                                       warningLevel);

        if (null == al) {
            System.err.println(maxErrors + " or more lexical errors occurred while scanning ACIP input file; is this");
            System.err.println("Tibetan or English input?");
            System.err.println("");
            if (false) {
                // Nobody wants to see this.  FIXME: maybe somebody; have an option.
                System.err.println("First " + maxErrors + " lexical errors scanning ACIP input file: ");
                System.err.println(errors);
            }
            System.err.println("Exiting with " + maxErrors + " or more lexical errors; please fix input file and try again.");
            System.exit(1);
        }
        final boolean abortUponScanningError = false;
        if (errors.length() > 0) {
            System.err.println("Errors scanning ACIP input file: ");
            System.err.println(errors);
            if (abortUponScanningError) {
                System.err.println("Exiting; please fix input file and try again.");
                System.exit(1);
            }
        }

        boolean colors = true;
        StringBuffer warnings = null;
        boolean putWarningsInOutput = false;
        if ("None" != warningLevel) {
            warnings = new StringBuffer();
            putWarningsInOutput = true;
        }
        convertToTMW(ACIPTraits.instance(), al, System.out, errors, warnings,
                     null, putWarningsInOutput, warningLevel, shortMessages,
                     colors);
        int retCode = 0;
        if (errors.length() > 0) {
            System.err.println("Errors converting ACIP input file: ");
            System.err.println(errors);
            System.err.println("The output contains these errors.");
            System.err.println("Exiting; please fix input file and try again.");
            retCode = 2;
        }
        if (null != warnings && warnings.length() > 0) {
            System.err.println("Warnings converting ACIP input file: ");
            System.err.println(warnings);
            if (putWarningsInOutput)
                System.err.println("The output contains these warnings.");
            retCode = 2;
        }
        if (0 == retCode) {
            if (verbose) System.err.println("Converted " + args[0] + " perfectly.");
        }
        System.exit(retCode);
    }

    /** Writes TMW/Latin to out.  If errors occur in converting a
     *  tsheg bar, then they are written into the output, and also
     *  appended to errors if errors is non-null.  If warnings occur
     *  in converting a tsheg bar, then they are written into the
     *  output if writeWarningsToResult is true, and also appended to
     *  warnings if warnings is non-null.  Returns true upon perfect
     *  success or if there were merely warnings, false if errors
     *  occurred.
     *  @param colors true if and only if you want Sanskrit in one
     *  color, errors/warnings in another, and tsheg-bars affected by
     *  prefix rules in another
     *  @throws IOException if we cannot write to out
     */
    public static boolean convertToTMW(TTraits ttraits,
                                       ArrayList scan,
                                       OutputStream out,
                                       StringBuffer errors,
                                       StringBuffer warnings,
                                       boolean[] hasWarnings,
                                       boolean writeWarningsToResult,
                                       String warningLevel,
                                       boolean shortMessages,
                                       boolean colors)
        throws IOException
    {
        TibetanDocument tdoc = new TibetanDocument();
        boolean rv
            = convertToTMW(ttraits,
                           scan, tdoc, errors, warnings, hasWarnings,
                           writeWarningsToResult, warningLevel,
                           shortMessages, colors,
                           new int[] { tdoc.getLength() });
        tdoc.writeRTFOutputStream(out);
        return rv;
    }


    /** Turns the list of TStrings scan into TibetanMachineWeb and
        Roman warnings and error messages that are inserted at
        position loc in tdoc.  FIXME: DOC better
    
        @param loc an input-output parameter.  On input, loc[0] is the
        offset from zero inside tdoc at which conversion results will
        be placed.  On output, loc[0] is one past the offset of the
        last of the conversion results. */
    public static boolean convertToTMW(TTraits ttraits,
                                       ArrayList scan,
                                       TibetanDocument tdoc,
                                       StringBuffer errors,
                                       StringBuffer warnings,
                                       boolean[] hasWarnings,
                                       boolean writeWarningsToResult,
                                       String warningLevel,
                                       boolean shortMessages,
                                       boolean colors,
                                       int[] loc)
        throws IOException
    {
        return convertTo(false, true,
                         ttraits, scan, null, tdoc, errors, warnings,
                         hasWarnings, writeWarningsToResult, warningLevel,
                         shortMessages, colors, loc,
                         loc[0] == tdoc.getLength());
    }

    /** Returns the Unicode that the given translit corresponds to.  A
     *  bit indirect, so use this for testing only if performance is a
     *  concern.  If errors occur in scanning the transliteration or
     *  in converting a tsheg bar, then they are appended to errors if
     *  errors is non-null, as well as written to the result.  If
     *  warnings occur in scanning the transliteration or in
     *  converting a tsheg bar, then they are appended to warnings if
     *  warnings is non-null, and they are written to the result if
     *  writeWarningsToResult is true.  Error and warning messages are
     *  long and self-contained unless shortMessages is true.  Returns
     *  the conversion upon perfect success or if there were merely
     *  warnings, null if errors occurred. */
    public static String convertToUnicodeText(TTraits ttraits,
                                              String translit,
                                              StringBuffer errors,
                                              StringBuffer warnings,
                                              boolean writeWarningsToResult,
                                              String warningLevel,
                                              boolean shortMessages) {
        ByteArrayOutputStream sw = new ByteArrayOutputStream();
        ArrayList al
            = ttraits.scanner().scan(translit, errors, -1, shortMessages,
                                     warningLevel);
        try {
            if (null != al) {
                convertToUnicodeText(ttraits, al, sw, errors,
                                     warnings, null, writeWarningsToResult,
                                     warningLevel, shortMessages);
                return sw.toString("UTF-8");
            } else {
                return null;
            }
        } catch (IOException e) {
            // Won't happen.  UTF-8 is guaranteed to be a supported
            // encoding, and ByteArrayOutputStreams don't have such
            // problems I don't think.
            throw new Error(e.toString());
        }
    }

    /** Writes Unicode text (not RTF) to out.  <em>NOTE WELL: This
     *  inherently cannot describe the ACIP {(KA) KHA} properly, as
     *  that requires showing KA in a smaller font than KHA, which is
     *  not possible in plain text.</em> If errors occur in converting
     *  a tsheg bar, then they are appended to errors if errors is
     *  non-null.  Furthermore, errors are written to out.  If
     *  writeWarningsToOut is true, then warnings also will be written
     *  to out.
     *  @return true upon perfect success, false if errors occurred.
     *  @param scan result of using ttraits.scanner() to break up the
     *  original string of transliteration
     *  @param out stream to which to write converted text
     *  @param errors if non-null, all error messages are appended
     *  @param warnings if non-null, all warning messages appropriate
     *  to warningLevel are appended
     *  @param hasWarnings if non-null, then hasWarnings[0] will be
     *  updated to true if and only if warnings are encountered and
     *  false otherwise
     *  @param writeWarningsToOut if true, then all warning messages
     *  are written to out in the appropriate places
     *  @throws IOException if we cannot write to out */
    public static boolean convertToUnicodeText(TTraits ttraits,
                                               ArrayList scan,
                                               OutputStream out,
                                               StringBuffer errors,
                                               StringBuffer warnings,
                                               boolean[] hasWarnings,
                                               boolean writeWarningsToOut,
                                               String warningLevel,
                                               boolean shortMessages)
        throws IOException
    {
        return convertTo(true, false,
                         ttraits, scan, out, null, errors, warnings,
                         hasWarnings, writeWarningsToOut, warningLevel,
                         shortMessages, false, new int[] { -1 } , true);
    }

    private static boolean peekaheadFindsSpacesAndComma(ArrayList /* of TString */ scan,
                                                        int pos) {
        int sz = scan.size();
        while (pos < sz) {
            TString s = (TString)scan.get(pos++);
            if (s.getType() == TString.TIBETAN_PUNCTUATION && s.getText().equals(" ")) {
                // keep going
            } else {
                if (s.getType() == TString.TIBETAN_PUNCTUATION && s.getText().equals(",")) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private static boolean convertTo(boolean toUnicode, // else to TMW
                                     boolean toRTF, // else to UTF-8-encoded text
                                     TTraits ttraits,
                                     ArrayList scan,
                                     OutputStream out, // for (toUnicode && !toRTF) mode
                                     TibetanDocument tdoc, // for !toUnicode mode or (toUnicode && toRTF) mode
                                     StringBuffer errors,
                                     StringBuffer warnings,
                                     boolean[] hasWarnings,
                                     boolean writeWarningsToOut,
                                     String warningLevel,
                                     boolean shortMessages,
                                     boolean colors,
                                     // tdocLocation[0] is an
                                     // input-output parameter.  It's
                                     // the starting location on input
                                     // and the location just past the
                                     // end on output.
                                     int[] tdocLocation,
                                     boolean isCleanDoc)
        throws IOException
    {
        try {
        if (null != tdoc && (toUnicode && !toRTF))
            throw new IllegalArgumentException("Doing both at once might work, but it's not been tested.  I bet some 'continue;' statements will need to go.");
        if (toUnicode && toRTF)
            throw new IllegalArgumentException("FIXME: support this ACIP->Unicode.rtf mode so that KA (GA) shows up in two different font sizes.  See RFE 838591.");
        if (!toUnicode && !toRTF)
            throw new IllegalArgumentException("ACIP->Uni.rtf, ACIP->Uni.txt, and ACIP->TMW.rtf are supported, but not ACIP->TMW.txt");
        if (toUnicode && toRTF && null == tdoc)
            throw new IllegalArgumentException("ACIP->Uni.rtf requires a TibetanDocument");
        if (null != out && !(toUnicode && !toRTF))
            throw new IllegalArgumentException("That stream is only used in ACIP->Uni.txt mode");
        if (null != out && null != tdoc)
            throw new IllegalArgumentException("Errors are not treated properly yet; do one conversion and then the other.  Is performance important enough to risk improper output for you?");
        if (null == out && null == tdoc)
            throw new IllegalArgumentException("Why would you?");
        int smallFontSize = -1;
        int regularFontSize = -1;
        if (null != tdoc) {
            String latinFont
                = ThdlOptions.getStringOption("thdl.acip.to.x.latin.font",
                                              "Times New Roman");
            int latinFontSize
                = ThdlOptions.getIntegerOption("thdl.acip.to.x.latin.font.size",
                                               18);
            tdoc.setRomanAttributeSet(latinFont, latinFontSize);

            regularFontSize = tdoc.getTibetanFontSize();
            smallFontSize = (int)(0.75*regularFontSize);
            if (smallFontSize >= regularFontSize)
                smallFontSize = regularFontSize - 1;
            if (colors)
                TibetanDocument.enableColors();
            else
                TibetanDocument.disableColors();
        }

        int sz = scan.size();
        boolean hasErrors = false;
        if (null != hasWarnings) hasWarnings[0] = false;
        BufferedWriter writer = null;
        if (toUnicode && !toRTF)
            writer
                = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        boolean lastGuyWasNonPunct = false;
        TStackList lastGuy = null;
        Color lastColor = Color.black;
        Color color = Color.black;
        boolean outputCurlyBracketsAroundFolioMarkers
            = ThdlOptions.getBooleanOption("thdl.acip.to.x.output.curly.brackets.around.folio.markers");

        for (int i = 0; i < sz; i++) {
            TString s = (TString)scan.get(i);
            int stype = s.getType();
            if (stype == TString.ERROR) {
                // leave lastGuyWasNonPunct and lastGuy alone; WARNINGs and ERRORs are invisible.
                hasErrors = true;
                String text = "[#ERROR " + s.getText() + "]";
                if (null != writer) writer.write(text);
                if (null != tdoc) {
                    tdoc.appendRoman(tdocLocation[0], text, Color.RED);
                    tdocLocation[0] += text.length();
                }
            } else if (stype == TString.TSHEG_BAR_ADORNMENT) {
                if (lastGuyWasNonPunct) {
                    String err = "[#ERROR " + ErrorsAndWarnings.getMessage(133, shortMessages, s.getText(),
                                                                           ttraits) + "]";
                    if (null != writer) {
                        String uni = ttraits.getUnicodeFor(s.getText(), false);
                        if (null == uni) {
                            hasErrors = true;
                            uni = err;
                        }
                        writer.write(uni);
                    }
                    if (null != tdoc) {
                        String wylie
                            = ttraits.getEwtsForOther(s.getText());
                        if (null == wylie) {
                            hasErrors = true;
                            tdoc.appendRoman(tdocLocation[0], err, Color.RED);
                            tdocLocation[0] += err.length();
                        } else {
                            tdoc.appendDuffCode(tdocLocation[0]++,
                                                TibetanMachineWeb.getGlyph(wylie),
                                                Color.black);
                        }
                    }
                } else {
                    hasErrors = true;
                }
                lastGuyWasNonPunct = true; // this stuff is not really punctuation
                lastGuy = null;
            } else if (stype == TString.WARNING) {
                // leave lastGuyWasNonPunct and lastGuy alone; WARNINGs and ERRORs are invisible.
                if (writeWarningsToOut) {
                    String text = "[#WARNING " + s.getText() + "]";
                    if (null != writer) writer.write(text);
                    if (null != tdoc) {
                        tdoc.appendRoman(tdocLocation[0], text, Color.RED);
                        tdocLocation[0] += text.length();
                    }
                }
                if (null != hasWarnings) hasWarnings[0] = true;
                if (null != warnings) {
                    warnings.append("Warning: ");
                    warnings.append(s.getText());
                    warnings.append('\n');
                }
            } else {
                if (s.isLatin()) {
                    lastGuyWasNonPunct = false;
                    lastGuy = null;
                    String text
                        = (((outputCurlyBracketsAroundFolioMarkers
                             && stype == TString.FOLIO_MARKER) ? "{" : "")
                           + s.getText()
                           + ((outputCurlyBracketsAroundFolioMarkers
                               && stype == TString.FOLIO_MARKER) ? "}" : ""));
                    if (null != writer) writer.write(text);
                    if (null != tdoc) {
                        tdoc.appendRoman(tdocLocation[0], text, Color.black);
                        tdocLocation[0] += text.length();
                    }
                } else {
                    String unicode = null;
                    Object[] duff = null;
                    if (stype == TString.TIBETAN_NON_PUNCTUATION) {
                        lastGuyWasNonPunct = true;
                        TPairList pls[]
                            = ttraits.breakTshegBarIntoChunks(s.getText(),
                                                              false);
                        String acipError;

                        if ((acipError = pls[0].getACIPError(s.getText(), shortMessages)) != null
                            && (null == pls[1] || pls[1].getACIPError(s.getText(), shortMessages) != null)) {
                            hasErrors = true;
                            String errorMessage = "[#ERROR " + acipError + "]";
                            if (null != writer) writer.write(errorMessage);
                            if (null != tdoc) {
                                tdoc.appendRoman(tdocLocation[0], errorMessage,
                                                 Color.RED);
                                tdocLocation[0] += errorMessage.length();
                            }
                            if (null != errors)
                                errors.append(errorMessage + "\n");
                        } else {
                            TParseTree pt0 = pls[0].getParseTree();
                            TParseTree pt1 = ((null == pls[1])
                                              ? null : pls[1].getParseTree());
                            if (null == pt0 && null == pt1) {
                                hasErrors = true;
                                String errorMessage
                                    = ("[#ERROR "
                                       + ErrorsAndWarnings.getMessage(130, shortMessages, s.getText(),
                                                                      ttraits)
                                       + "]");
                                if (null != writer) writer.write(errorMessage);
                                if (null != tdoc) {
                                    tdoc.appendRoman(tdocLocation[0], errorMessage,
                                                     Color.RED);
                                    tdocLocation[0] += errorMessage.length();
                                }
                                if (null != errors)
                                    errors.append(errorMessage + "\n");
                            } else {
                                TStackList sl0 = pt0.getBestParse();
                                TStackList sl1 = ((null == pt1)
                                                  ? null : pt1.getBestParse());
                                if (null == sl0 && null == sl1) {
                                    // ACIP {A-DZU} causes this, for example.
                                    hasErrors = true;
                                    String errorMessage =
                                        "[#ERROR "
                                        + ErrorsAndWarnings.getMessage(134,
                                                                       shortMessages,
                                                                       s.getText(),
                                                                       ttraits)
                                        + "]";
                                    if (null != writer)
                                        writer.write(errorMessage);
                                    if (null != tdoc) {
                                        tdoc.appendRoman(tdocLocation[0],
                                                         errorMessage,
                                                         Color.RED);
                                        tdocLocation[0] += errorMessage.length();
                                    }
                                    if (null != errors)
                                        errors.append(errorMessage + "\n");
                                } else {
                                    TStackList sl = sl0;
                                    TPairList pl = pls[0];
                                    TParseTree pt = pt0;
                                    // set sl equal to the best choice of sl0 and sl1.
                                    if (null != sl1) {
                                        BoolTriple sl0bt = sl0.isLegalTshegBar(false);
                                        BoolTriple sl1bt = sl1.isLegalTshegBar(false);
                                        int ct;
                                        if ((ct = sl0bt.compareTo(sl1bt)) < 0) {
                                            sl = sl1;
                                            pl = pls[1];
                                            pt = pt1;
                                        } else if (0 == ct) {
                                            // sl remains sl0 -- '* is
                                            // a vowel unless it's
                                            // clearly part of an
                                            // appendage like 'AM.
                                        }
                                    }
                                    lastGuy = sl;
                                    String warning = null;
                                    if ("None" != warningLevel) {
                                        warning = pt.getWarning(warningLevel,
                                                                pl,
                                                                s.getText(),
                                                                shortMessages,
                                                                ttraits);
                                    }
                                    if (null != warning) {
                                        if (writeWarningsToOut) {
                                            String text
                                                = ("[#WARNING "
                                                   + warning + "]");
                                            if (null != writer) writer.write(text);
                                            if (null != tdoc) {
                                                tdoc.appendRoman(tdocLocation[0],
                                                                 text,
                                                                 Color.RED);
                                                tdocLocation[0] += text.length();
                                            }
                                        }
                                        if (null != hasWarnings) hasWarnings[0] = true;
                                        if (null != warnings) {
                                            warnings.append(warning);
                                            warnings.append('\n');
                                        }
                                    }
                                    if (null != writer) {
                                        unicode = sl.getUnicode();
                                        if (null == unicode) throw new Error("FIXME: make this an assertion 4");
                                        // Warn if any of the stacks
                                        // in this tsheg bar do not
                                        // have corresponding glyphs
                                        // in TMW.  That means there
                                        // was probably a typo in the
                                        // input.
                                        if (ErrorsAndWarnings.isEnabled(511, warningLevel)) {
                                            Object[] trialDuff
                                                = sl.getDuff(shortMessages,
                                                             false);
                                            for (int ii = 0; ii < trialDuff.length; ii++) {
                                                if (trialDuff[ii] instanceof String) {
                                                    if (!((String)trialDuff[ii]).startsWith("511"))
                                                        throw new Error("I thought 511 was the only beast like this; FIXME: make this an assertion 324xd3");
                                                    String bwarning
                                                        = "[#WARNING "
                                                        + (String)trialDuff[ii] + "]";
                                                    unicode = bwarning + unicode;
                                                    if (null != hasWarnings) hasWarnings[0] = true;
                                                    if (null != warnings) {
                                                        warnings.append(bwarning);
                                                        warnings.append('\n');
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (null != tdoc) {
                                        duff = sl.getDuff(shortMessages, true);
                                        BoolTriple bt;
                                        if (colors && sl.isLegalTshegBar(true).isLegal && !sl.isLegalTshegBar(false).isLegal) {
                                            color = Color.YELLOW;
                                        } else if (colors && (bt = sl.isLegalTshegBar(false)).isLegal && !bt.isLegalButSanskrit()) {
                                            color = Color.black;
                                        } else {
                                            // Sanskrit.

                                            // FIXME: should a funny
                                            // vowel cause green to
                                            // appear too?  G'EEm is
                                            // black, not green, right
                                            // now, though GA: is
                                            // green.
                                            color = Color.GREEN;
                                        }

                                        if (0 == duff.length) {
                                            throw new Error("No DuffCodes for stack list " + sl); // FIXME: make this an assertion
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        color = Color.black;
                        if (stype == TString.START_SLASH) {
                            if (null != writer) unicode = "\u0F3C";
                            if (null != tdoc) duff = new Object[] { TibetanMachineWeb.getGlyph("(") };
                        } else if (stype == TString.END_SLASH) {
                            if (null != writer) unicode = "\u0F3D";
                            if (null != tdoc) duff = new Object[] { TibetanMachineWeb.getGlyph(")") };
                        } else if (stype == TString.TIBETAN_PUNCTUATION) {
                            // For ACIP, tshegs are used as both
                            // tshegs and whitespace.  We treat a
                            // space as a tsheg if and only if it
                            // occurs after TIBETAN_NON_PUNCTUATION.
                            // But "SHIG ,MDO" is an example of a
                            // special case, needed because a tsheg is
                            // not used after a GA in Tibetan
                            // typesetting.
                            boolean done = false;
                            // what about after numbers?  marks? FIXME: test
                            TPairList lpl = null;
                            if (ttraits.isACIP() && s.getText().equals(" ")) {
                                if (!lastGuyWasNonPunct
                                    || (null != lastGuy
                                        && (lpl = lastGuy.get(lastGuy.size() - 1)).size() == 1
                                        // "GU ," and "KU ," each have
                                        // tshegs, but "GI ," and "KI
                                        // ," each have a Tibetan
                                        // space.
                                        && ((lpl.get(0).getLeft().equals("G")
                                             || lpl.get(0).getLeft().equals("K"))
                                            && (null == lpl.get(0).getRight()
                                                || lpl.get(0).getRight().indexOf('U') < 0))
                                        &&
                                        // it's (G . anything)
                                        // followed by some number of
                                        // spaces (at least one, this
                                        // one) and then a comma:
                                        peekaheadFindsSpacesAndComma(scan, i+1))) {
                                    if (null != writer) {
                                        unicode = " "; // TODO(DLC)[EWTS->Tibetan]: FIXME: allow for U+00A0 between two <i>shad</i>s (0F0D or 0F0E), and optionally insert a U+200B after the <i>shad</i> following the whitespace so that stupid software will break lines more nicely
                                        done = true;
                                    }
                                    if (null != tdoc) {
                                        DuffCode spaceDuff = TibetanMachineWeb.getGlyph("_");
                                        if (null == spaceDuff) throw new Error("whitespace duff");
                                        tdoc.appendDuffCode(tdocLocation[0]++,
                                                            spaceDuff, Color.black);
                                        continue; // FIXME: if null != writer, output was just dropped.
                                    }
                                }
                            } else if (ttraits.isACIP()
                                       && s.getText().equals(",")
                                       && lastGuyWasNonPunct
                                       && null != lastGuy
                                       && (lpl = lastGuy.get(lastGuy.size() - 1)).size() == 1
                                       && lpl.get(0).getLeft().equals("NG")) {
                                // {NGO,} is not acceptable;
                                // typesetting requires we treat this
                                // like {NGO\u0F0C,}.
                                if (null != writer) {
                                    writer.write("\u0F0C");
                                }
                                if (null != tdoc) {
                                    DuffCode tshegDuff = TibetanMachineWeb.getGlyph("*");
                                    if (null == tshegDuff) throw new Error("non-breaking tsheg duff");
                                    tdoc.appendDuffCode(tdocLocation[0]++,
                                                        tshegDuff, lastColor);
                                }
                            }

                            if (!done) {
                                if (null != writer) unicode = ttraits.getUnicodeFor(s.getText(), false);
                                if (null != tdoc) {
                                    if (s.getText().equals("\r")
                                        || s.getText().equals("\t")
                                        || s.getText().equals("\n")
                                        || s.getText().equals("\r\n")) {
                                        tdoc.appendRoman(tdocLocation[0], s.getText(),
                                                         Color.black);
                                        tdocLocation[0] += s.getText().length();
                                        continue; // FIXME: this means the unicode above doesn't go into the output if null != writer && null != tdoc?
                                    } else {
                                        if (ttraits.isACIP()
                                            && "#".equals(s.getText())) { // hard-coded ACIP value
                                            duff = new Object[] {
                                                TibetanMachineWeb.getGlyph("@#"),  // hard-coded EWTS
                                                TibetanMachineWeb.getGlyph("#")  // hard-coded EWTS
                                            }; // hard-coded EWTS values
                                        } else if (!ttraits.isACIP()
                                                   && "//".equals(s.getText())) {
                                            duff = new Object[] {
                                                TibetanMachineWeb.getGlyph("/"),  // hard-coded EWTS
                                                TibetanMachineWeb.getGlyph("/")   // hard-coded EWTS
                                            }; // hard-coded EWTS values
                                        } else {
                                            String wy = ttraits.getEwtsForOther(s.getText());
                                            if (null == wy) throw new Error("No wylie for ACIP " + s.getText());
                                            duff = new Object[] { TibetanMachineWeb.maybeGetGlyph(wy) };
                                            if (null == duff[0]) {
                                              duff[0] =
                                                ErrorsAndWarnings.getMessage(
                                                    137, shortMessages,
                                                    s.getText(), ttraits);
                                            }
                                        }
                                    }
                                }
                            } // TODO(DLC)[EWTS->Tibetan]: change this to have a "parse" phase that puts out error messagesf like 142 and figures out what a space means.  This is a very long function that is difficult to maintain, and we want EWTS->Tibetan to be clean.
                        } else if (stype == TString.START_PAREN) {
                            if (null != writer)
                                writer.write("[ERROR "
                                             + ErrorsAndWarnings.getMessage(142,
                                                                            shortMessages,
                                                                            "(" /* hard-coded ACIP value */,
                                                                            ttraits) + "]");
                            if (null != tdoc) {
                                tdoc.setTibetanFontSize(smallFontSize);
                            }
                            continue;
                        } else if (stype == TString.END_PAREN) {
                            if (null != writer)
                                writer.write("[ERROR "
                                             + ErrorsAndWarnings.getMessage(143,
                                                                            shortMessages,
                                                                            ")" /* hard-coded ACIP value. TODO(DLC)[EWTS->Tibetan]: and above*/,
                                                                            ttraits) + "]");
                            if (null != tdoc) {
                                tdoc.setTibetanFontSize(regularFontSize);
                            }
                            continue;
                        } else if (stype == TString.UNICODE_CHARACTER) {
                            ThdlDebug.verify(1 == s.getText().length());
                            if (null != writer) {
                                char ch = s.getText().charAt(0);
                                if (ch >= THDLWylieConstants.PUA_MIN
                                    && ch <= THDLWylieConstants.PUA_MAX) {
                                    hasErrors = true;
                                    String errorMessage =
                                        "[#ERROR "
                                        + ErrorsAndWarnings.getMessage(135,
                                                                       shortMessages,
                                                                       "" + ch,
                                                                       ttraits)
                                        + "]";
                                    writer.write(errorMessage);
                                    if (null != errors)
                                        errors.append(errorMessage + "\n");
                                    continue; // FIXME: dropping output if null != tdoc
                                } else if (org.thdl.tib.text.tshegbar.UnicodeUtils.isReservedTibetanCode(ch)) {
                                    hasErrors = true;
                                    String errorMessage =
                                        "[#ERROR "
                                        + ErrorsAndWarnings.getMessage(138,
                                                                       shortMessages,
                                                                       "" + ch,
                                                                       ttraits)
                                        + "]";
                                    writer.write(errorMessage);
                                    if (null != errors)
                                        errors.append(errorMessage + "\n");
                                    continue; // FIXME: dropping output if null != tdoc
                                } else
                                    unicode = s.getText();
                            }
                            if (null != tdoc) {
                                duff = TibetanMachineWeb.mapUnicodeToTMW(s.getText().charAt(0));
                                if (null == duff) {
                                    hasErrors = true;
                                    String errorMessage =
                                        "[#ERROR "
                                        + ErrorsAndWarnings.getMessage(136,
                                                                       shortMessages,
                                                                       s.getText(),
                                                                       ttraits)
                                        + "]";
                                    tdoc.appendRoman(tdocLocation[0],
                                                     errorMessage,
                                                     Color.RED);
                                    tdocLocation[0] += errorMessage.length();
                                    if (null != errors)
                                        errors.append(errorMessage + "\n");
                                    continue; // FIXME: if null != writer, we dropped some output.
                                }
                            }
                        } else {
                            throw new Error("forgot a case");
                        }
                        if (null != writer && null == unicode)
                            throw new Error("FIXME: make this an assertion 1");
                        if (null != tdoc && (null == duff || 0 == duff.length))
                            throw new Error("FIXME: make this an assertion 2");
                        lastGuyWasNonPunct = false;
                        lastGuy = null;
                    }
                    if (null != writer && null != unicode) writer.write(unicode);
                    if (null != tdoc) {
                        if (null != duff && 0 != duff.length) {
                            for (int j = 0; j < duff.length; j++) {
                                if (duff[j] instanceof DuffCode)
                                    tdoc.appendDuffCode(tdocLocation[0]++,
                                                        (DuffCode)duff[j],
                                                        color);
                                else {
                                    hasErrors = true;
                                    String emsg
                                        = "[ERROR " + (String)duff[j] + "]";
                                    if (null != errors)
                                        errors.append(emsg + "\n");
                                    tdoc.appendRoman(tdocLocation[0],
                                                     emsg,
                                                     Color.RED);
                                    tdocLocation[0] += emsg.length();
                                }
                            }
                        } else {
                            // this happens when you have an
                            // [#ERROR]-producing tsheg bar.
                            
                            // System.err.println("Bad tsheg bar with ACIP {" + s.getText() + "}");
                        }
                    }
                }
            }
            lastColor = color;
        }
        if (null != writer) {
            writer.close();
        }
        if (isCleanDoc && null != tdoc && tdocLocation[0] != tdoc.getLength())
            throw new Error("Oops -- we dropped something from the output!  tdocLocation[0]++; and tdocLocation[0]+=xyz; are not being used correctly.");
        return !hasErrors;
        } catch (javax.swing.text.BadLocationException e) {
            throw new IllegalArgumentException("tdocLocation[0] is no good: " + tdocLocation[0]);
        }
    }
}


