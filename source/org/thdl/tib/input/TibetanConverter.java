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

package org.thdl.tib.input;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;

import org.thdl.tib.text.TibetanDocument;
import org.thdl.tib.text.reverter.Converter;
import org.thdl.tib.text.ttt.ACIPTraits;
import org.thdl.tib.text.ttt.EWTSTraits;
import org.thdl.tib.text.ttt.TConverter;
import org.thdl.tib.text.ttt.TTraits;
import org.thdl.util.RTFFixerInputStream;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlLazyException;
import org.thdl.util.ThdlOptions;
import org.thdl.util.ThdlVersion;
import org.thdl.tib.scanner.*;

/** TibetanConverter is a command-line utility for converting to and
 *  from Tibetan Machine Web (TMW).  It converts TMW to Wylie, ACIP,
 *  Unicode, or to Tibetan Machine (TM).  It also converts to TMW from
 *  TM or ACIP.  Some conversions use RTF (rich text format); some use
 *  text.  Invoke it with no parameters for usage information.  Full
 *  documentation is available at the website listed below.
 *
 *  @see <a href="http://thdltools.sourceforge.net/TMW_RTF_TO_THDL_WYLIE.html">End-user documentation</a>
 *
 *  @author David Chandler */
public class TibetanConverter implements FontConverterConstants {
    private static final boolean debug = false;

    /** Default constructor; does nothing */
    TibetanConverter() { }

    static final String rtfErrorMessage
        = "The Rich Text Format (RTF) file selected contains constructs that\nJskad cannot handle.  If you got the RTF file from saving a Word\ndocument as RTF, try saving that same document as RTF in\nWord 2000 instead of Word XP or in Word 97 instead of\nWord 2000.  Older versions of Word produce RTF that Jskad\ncan more easily deal with.  OpenOffice and StarOffice may also\nproduce better-behaved RTF.";

    /**
     *  Runs the converter. */
    public static void main(String[] args) {
        // No need for the TM or TMW fonts.
        System.setProperty("thdl.rely.on.system.tmw.fonts", "true");
        System.setProperty("thdl.rely.on.system.tm.fonts", "true");

        // Runs on Linux/Unix boxes without X11 servers:
        System.setProperty("java.awt.headless", "true");

        System.exit(realMain(args, System.out));
    }

    /** Runs the converter without exiting the program.
     *  @return the exit code. */
    public static int realMain(String[] args, PrintStream out) {
        try {
            boolean convertTmwToTmwMode = false;
            boolean convertToUnicodeMode = false;
            boolean convertToTMMode = false;
            boolean convertACIPToUniMode = false;
            boolean convertACIPToTMWMode = false;
            boolean convertWylieToUniMode = false;
            boolean convertWylieToTMWMode = false;
            boolean convertToTMWMode = false;
            boolean convertToWylieRTFMode = false;
            boolean convertToWylieTextMode = false;
            boolean convertToACIPRTFMode = false;
            boolean convertToACIPTextMode = false;
            boolean convertUniToWylieTextMode = false;
            boolean findSomeNonTMWMode = false;
            boolean findAllNonTMWMode = false;
            boolean findSomeNonTMMode = false;
            boolean findAllNonTMMode = false;

            boolean colors = false;
            boolean shortMessages = false;

            String warningLevel = null;
            
            // Process arguments:
            final int numArgs = 8;
            if ((args.length != 1 && args.length != numArgs)
                || (args.length == 1
                    && !(args[0].equals("-v")
                         || args[0].equals("--version")))
                || (args.length == numArgs
                    && (!(args[numArgs - 8].equals("--colors"))
                        || !((colors = args[numArgs - 7].equals("yes"))
                             || args[numArgs - 7].equals("no"))
                        || !(args[numArgs - 6].equals("--warning-level"))
                        || !((warningLevel = args[numArgs - 5]).equals("Most")
                             || warningLevel.equals("Some")
                             || warningLevel.equals("All")
                             || warningLevel.equals("None"))
                        || !(args[numArgs - 4].equals("--acip-to-tibetan-warning-and-error-messages"))
                        || !((shortMessages = args[numArgs - 3].equals("short"))
                             || args[numArgs - 3].equals("long"))
                        || !((findAllNonTMWMode
                              = args[numArgs - 2].equals("--find-all-non-tmw"))
                             || (convertTmwToTmwMode
                                 = args[numArgs - 2].equals("--tmw-to-tmw-for-testing"))
                             || (convertToTMMode
                                 = args[numArgs - 2].equals("--to-tibetan-machine"))
                             || (convertUniToWylieTextMode
                                 = args[numArgs - 2].equals("--utf8-text-to-ewts-text"))
                             || (convertToTMWMode
                                 = args[numArgs - 2].equals("--to-tibetan-machine-web"))
                             || (convertACIPToUniMode
                                 = args[numArgs - 2].equals("--acip-to-unicode"))
                             || (convertACIPToTMWMode
                                 = args[numArgs - 2].equals("--acip-to-tmw"))
                             || (convertWylieToUniMode
                                 = args[numArgs - 2].equals("--wylie-to-unicode"))
                             || (convertWylieToTMWMode
                                 = args[numArgs - 2].equals("--wylie-to-tmw"))
                             || (convertToUnicodeMode
                                 = args[numArgs - 2].equals("--to-unicode"))
                             || (convertToWylieRTFMode
                                 = args[numArgs - 2].equals("--to-wylie"))
                             || (convertToWylieTextMode
                                 = args[numArgs - 2].equals("--to-wylie-text"))
                             || (convertToACIPRTFMode
                                 = args[numArgs - 2].equals("--to-acip"))
                             || (convertToACIPTextMode
                                 = args[numArgs - 2].equals("--to-acip-text"))
                             || (findSomeNonTMWMode
                                 = args[numArgs - 2].equals("--find-some-non-tmw"))
                             || (findSomeNonTMMode
                                 = args[numArgs - 2].equals("--find-some-non-tm"))
                             || (findAllNonTMMode
                                 = args[numArgs - 2].equals("--find-all-non-tm"))
                             )))) {
                if (args.length != numArgs) {
                    out.println("");
                    out.println("Wrong number of arguments; needs " + numArgs + " arguments.");
                    out.println("");
                }
                    
                out.println("TibetanConverter --colors yes|no");
                out.println("                 --warning-level None|Some|Most|All");
                out.println("                 --acip-to-tibetan-warning-and-error-messages short|long");  // TODO(DLC)[EWTS->Tibetan]: misnomer, ewts and acip both are affected
                out.println("                 --find-all-non-tmw | --find-some-non-tmw");
                out.println("                   | --tmw-to-tmw-for-testing");
                out.println("                   | --to-tibetan-machine | --to-tibetan-machine-web");
                out.println("                   | --to-unicode | --to-wylie | --to-acip");
                out.println("                   | --to-wylie-text | --to-acip-text");
                out.println("                   | --wylie-to-unicode | --wylie-to-tmw");
                out.println("                   | --acip-to-unicode | --acip-to-tmw RTF_file|TXT_file");
                out.println(" | TibetanConverter [--version | -v | --help | -h]");
                out.println("");
                out.println("Distributed under the terms of the THDL Open Community License Version 1.0.");
                out.println("");
                out.println("Usage:");
                out.println(" -v | --version for version info");
                out.println("");
                out.println(" -h | --help for this message");
                out.println("");
                out.println(" --wylie-to-unicode to convert an EWTS text file to a Unicode");
                out.println("");
                out.println(" --wylie-to-tmw to convert an EWTS text file to TibetanMachineWeb");
                out.println("");
                out.println(" --to-tibetan-machine to convert TibetanMachineWeb to TibetanMachine");
                out.println("");
                out.println(" --to-unicode to convert TibetanMachineWeb to Unicode");
                out.println("");
                out.println(" --to-tibetan-machine-web to convert TibetanMachine to TibetanMachineWeb");
                out.println("");
                out.println(" --to-wylie to convert TibetanMachineWeb to THDL Extended Wylie in RTF");
                out.println("");
                out.println(" --to-wylie-text to convert TibetanMachineWeb to THDL Extended Wylie in text");
                out.println("");
                out.println(" --to-acip to convert TibetanMachineWeb to ACIP in RTF");
                out.println("");
                out.println(" --to-acip-text to convert TibetanMachineWeb to ACIP in text");
                out.println("");
                out.println(" --acip-to-unicode to convert ACIP text file to Unicode text file");
                out.println("");
                out.println(" --acip-to-tmw to convert ACIP text file to Tibetan Machine Web RTF File.");
                out.println("");
                out.println(" --find-all-non-tmw to locate all characters in the input document that are");
                out.println("   not in Tibetan Machine Web fonts, exit zero if and only if none found");
                out.println("");
                out.println(" --find-some-non-tmw to locate all distinct characters in the input document");
                out.println("   not in Tibetan Machine Web fonts, exit zero if and only if none found");
                out.println("");
                out.println(" --find-all-non-tm to locate all characters in the input document that are");
                out.println("   not in Tibetan Machine fonts, exit zero if and only if none found");
                out.println("");
                out.println(" --find-some-non-tm to locate all distinct characters in the input document");
                out.println("   not in Tibetan Machine fonts, exit zero if and only if none found");
                out.println("");
                out.println("");
                out.println("In --to... and --acip-to... modes, needs one argument, the name of the");
                out.println("TibetanMachineWeb RTF file (for --to-wylie, --to-wylie-text, --to-acip-text,");
                out.println("--to-acip, --to-unicode, and --to-tibetan-machine) or the name of");
                out.println("the TibetanMachine RTF file (for --to-tibetan-machine-web) or the name of the");
                out.println("ACIP text file (for --acip-to-unicode or --acip-to-tmw).  Writes the");
                out.println("result to standard output (after dealing with the curly brace problem if");
                out.println("the input is TibetanMachineWeb).  Exit code is zero on success, 42 if some");
                out.println("glyphs couldn't be converted (in which case the output is just those glyphs),");
                out.println("44 if a TMW->Wylie conversion ran into some glyphs that couldn't be");
                out.println("converted, in which case ugly error messages like");
                out.println("   \"<<[[JSKAD_TMW_TO_WYLIE_ERROR_NO_SUCH_WYLIE: Cannot convert DuffCode...\"");
                out.println("are in your document waiting for your personal attention,");
                out.println("43 if not even one glyph found was eligible for this conversion, which means");
                out.println("that you probably selected the wrong conversion or the wrong document, or ");
                out.println("nonzero on some other error.");
                // TODO(dchandler): describe 47 48 50 etc.
                out.println("");
                out.println("You may find it helpful to use `--find-some-non-tmw' mode (or");
                out.println("`--find-some-non-tm' mode for Tibetan Machine input) before doing a");
                out.println("conversion so that you have confidence in the conversion's correctness.");
                out.println("");
                out.println("When using short error and warning messages for ACIP->Tibetan conversions,");
                out.println("i.e. when '--acip-to-tibetan-warning-and-error-messages short' is given,");
                out.println("the output will contain error and warning numbers.  The following are the");
                out.println("long forms of each warning and error:");
                out.println("");
                org.thdl.tib.text.ttt.ErrorsAndWarnings.printErrorAndWarningDescriptions(out);
                return 77;
            }
            if (args[0].equals("--version") || args[0].equals("-v")) {
                out.println("TibetanConverter version 0.84");
                out.println("Compiled at "
                            + ThdlVersion.getTimeOfCompilation());
                return 77;
            }
            String inputRtfPath = args[args.length - 1];

            InputStream in;
            if (inputRtfPath.equals("-"))
                in = System.in;
            else
                in = new FileInputStream(inputRtfPath);
            
            String conversionTag = null;
            if (findAllNonTMWMode) {
                conversionTag = FIND_ALL_NON_TMW;
            } else if (findSomeNonTMWMode) {
                conversionTag = FIND_SOME_NON_TMW;
            } else if (findSomeNonTMMode) {
                conversionTag = FIND_SOME_NON_TM;
            } else if (findAllNonTMMode) {
                conversionTag = FIND_ALL_NON_TM;
            } else { // conversion {to Wylie or TM} mode
                if (convertToWylieRTFMode) {
                    conversionTag = TMW_TO_WYLIE;
                } else if (convertToWylieTextMode) {
                    conversionTag = TMW_TO_WYLIE_TEXT;
                } else if (convertUniToWylieTextMode) {
                    conversionTag = UNI_TO_WYLIE_TEXT;
                } else if (convertToACIPRTFMode) {
                    conversionTag = TMW_TO_ACIP;
                } else if (convertToACIPTextMode) {
                    conversionTag = TMW_TO_ACIP_TEXT;
                } else if (convertToUnicodeMode) {
                    conversionTag = TMW_TO_UNI;
                } else if (convertTmwToTmwMode) {
                    conversionTag = TMW_TO_SAME_TMW;
                } else if (convertToTMWMode) {
                    conversionTag = TM_TO_TMW;
                } else if (convertACIPToUniMode) {
                    conversionTag = ACIP_TO_UNI_TEXT;
                } else if (convertACIPToTMWMode) {
                    conversionTag = ACIP_TO_TMW;
                } else if (convertWylieToUniMode) {
                    conversionTag = WYLIE_TO_UNI_TEXT;
                } else if (convertWylieToTMWMode) {
                    conversionTag = WYLIE_TO_TMW;
                } else {
                    ThdlDebug.verify(convertToTMMode);
                    conversionTag = TMW_TO_TM;
                }
            }
            return reallyConvert(in, out, conversionTag,
                                 warningLevel.intern(), shortMessages,
                                 colors);
        } catch (ThdlLazyException e) {
            out.println("TibetanConverter has a BUG:");
            e.getRealException().printStackTrace(out);
            System.err.println("TibetanConverter has a BUG:");
            e.getRealException().printStackTrace(System.err);
            return 7;
        } catch (IOException e) {
            e.printStackTrace(out);
            e.printStackTrace(System.err);
            return 4;
        } catch (OutOfMemoryError e) {
            e.printStackTrace(out);
            e.printStackTrace(System.err);
            throw e;
        }
    }

    /** Reads from in, closes in, converts (or finds some/all
        non-TM/TMW), writes the result to out, does not close out.
        The action taken depends on ct, which must be one of a set
        number of strings -- see the code.  Uses short error and
        warning messages if shortMessages is true; gives no warnings
        or many warnings depending on warningLevel.  Returns an
        appropriate return code so that TibetanConverter's usage
        message is honored. */
    static int reallyConvert(InputStream in, PrintStream out, String ct,
                             String warningLevel, boolean shortMessages,
                             boolean colors) {
        if (UNI_TO_WYLIE_TEXT == ct || WYLIE_TO_ACIP_TEXT == ct || ACIP_TO_WYLIE_TEXT == ct) {
            try {
                /*String uniText;
                {
                    // TODO(dchandler): use, here and elsewhere in the
                    // codebase,
                    // org.apache.commons.io.IOUtils.toString(InputStream,
                    // encoding)
                    StringBuffer s = new StringBuffer();
                    char ch[] = new char[8192];
                    BufferedReader bin
                        = new BufferedReader(new InputStreamReader(in,
                                                                   "UTF-8"));
                    int amt;
                    while (-1 != (amt = bin.read(ch))) {
                        s.append(ch, 0, amt);
                    }
                    bin.close();
                    uniText = s.toString();
                }
                StringBuffer errors = new StringBuffer();
                // TODO(dchandler): DLC: use human-friendly EWTS, not
                // computer-friendly!
                String ewtsText = Converter.convertToEwtsForComputers(uniText,
                                                                      errors);
                // TODO(dchandler): is 51 the right choice?
                return (errors.length() > 0) ? 51 : 0;*/
            	BasicTibetanTranscriptionConverter bc = null;
            	if (UNI_TO_WYLIE_TEXT == ct) bc = new BasicTibetanTranscriptionConverter(new BufferedReader(new InputStreamReader(in, "UTF16")), new PrintWriter(out));
            	else bc = new BasicTibetanTranscriptionConverter(new BufferedReader(new InputStreamReader(in)), new PrintWriter(out));
            	bc.run(ct);
            	return 0;
            } catch (IOException e) {
                // TODO(dchandler): print it?  where to?
                return 48;
            }
        } else if (ACIP_TO_UNI_TEXT == ct || ACIP_TO_TMW == ct
                   || WYLIE_TO_UNI_TEXT == ct || WYLIE_TO_TMW == ct) {
            try {
                ArrayList al
                    = ((ACIP_TO_UNI_TEXT == ct || ACIP_TO_TMW == ct)
                       ? (TTraits)ACIPTraits.instance()
                       : (TTraits)EWTSTraits.instance()).scanner().scanStream(in, null,
                                                                              ThdlOptions.getIntegerOption((ACIP_TO_UNI_TEXT == ct || ACIP_TO_TMW == ct)
                                                                                                           ? "thdl.most.errors.a.tibetan.acip.document.can.have"
                                                                                                           : "thdl.most.errors.a.tibetan.ewts.document.can.have",
                                                                                                           1000 - 1),
                                                                              shortMessages,
                                                                              warningLevel);
                if (null == al)
                    return 47;
                boolean embeddedWarnings = (warningLevel != "None");
                boolean hasWarnings[] = new boolean[] { false };
                if (ACIP_TO_UNI_TEXT == ct
                    || WYLIE_TO_UNI_TEXT == ct) {
                    if (!TConverter.convertToUnicodeText((WYLIE_TO_UNI_TEXT == ct)
                                                         ? (TTraits)EWTSTraits.instance()
                                                         : (TTraits)ACIPTraits.instance(),
                                                         al, out, null,
                                                         null, hasWarnings,
                                                         embeddedWarnings,
                                                         warningLevel,
                                                         shortMessages))
                        return 46;
                } else {
                    if (!TConverter.convertToTMW((WYLIE_TO_TMW == ct)
                                                 ? (TTraits)EWTSTraits.instance()
                                                 : (TTraits)ACIPTraits.instance(),
                                                 al, out, null, null,
                                                 hasWarnings,
                                                 embeddedWarnings,
                                                 warningLevel, shortMessages,
                                                 colors))
                        return 46;
                }
                if (embeddedWarnings && hasWarnings[0])
                    return 45;
                else
                    return 0;
            } catch (IOException e) {
                // TODO(dchandler): print it?  where to?
                return 48;
            }
        } else {
            TibetanDocument tdoc = new TibetanDocument();
            {
                SimpleAttributeSet ras = new SimpleAttributeSet();
                StyleConstants.setFontFamily(ras,
                                             ThdlOptions.getStringOption("thdl.default.roman.font.face",
                                                                         "Serif"));
                StyleConstants.setFontSize(ras,
                                           ThdlOptions.getIntegerOption("thdl.default.roman.font.size",
                                                                        14));
                tdoc.setRomanAttributeSet(ras);
            }
            try {
                // Read in the rtf file.
                if (debug) System.err.println("Start: reading in old RTF file");
                if (!ThdlOptions.getBooleanOption("thdl.do.not.fix.rtf.hex.escapes"))
                    in = new RTFFixerInputStream(in);
                (new RTFEditorKit()).read(in, tdoc, 0);
                if (debug) System.err.println("End  : reading in old RTF file");
            } catch (Exception e) {
                out.println("TibetanConverter:\n"
                            + rtfErrorMessage);
                return 3;
            }
            try {
                in.close();
            } catch (IOException e) {
                // silently ignore; we don't care about the input so much...
                ThdlDebug.noteIffyCode();
            }


            if (FIND_ALL_NON_TMW == ct) {
                // 0, -1 is the entire document.
                int exitCode
                    = tdoc.findAllNonTMWCharacters(0, -1, out);
                if (out.checkError())
                    exitCode = 41;
                return exitCode;
            } else if (FIND_SOME_NON_TMW == ct) {
                // 0, -1 is the entire document.
                int exitCode
                    = tdoc.findSomeNonTMWCharacters(0, -1, out);
                if (out.checkError())
                    exitCode = 41;
                return exitCode;
            } else if (FIND_SOME_NON_TM == ct) {
                // 0, -1 is the entire document.
                int exitCode
                    = tdoc.findSomeNonTMCharacters(0, -1, out);
                if (out.checkError())
                    exitCode = 41;
                return exitCode;
            } else if (FIND_ALL_NON_TM == ct) {
                // 0, -1 is the entire document.
                int exitCode
                    = tdoc.findAllNonTMCharacters(0, -1, out);
                if (out.checkError())
                    exitCode = 41;
                return exitCode;
            } else { // conversion {to Wylie or TM} mode
                // Fix curly braces in the entire document if the input is TMW:
                if (TM_TO_TMW != ct) {
                    // DLC make me optional
                    if (debug) System.err.println("Start: solving curly brace problem");
                    tdoc.replaceTahomaCurlyBracesAndBackslashes(0, -1);
                    if (debug) System.err.println("End  : solving curly brace problem");
                }

                int exitCode = 0;
                ThdlDebug.verify(((TMW_TO_TM == ct) ? 1 : 0)
                                 + ((TMW_TO_SAME_TMW == ct) ? 1 : 0)
                                 + ((TMW_TO_UNI == ct) ? 1 : 0)
                                 + ((TM_TO_TMW == ct) ? 1 : 0)
                                 + ((TMW_TO_ACIP == ct) ? 1 : 0)
                                 + ((TMW_TO_ACIP_TEXT == ct) ? 1 : 0)
                                 + ((TMW_TO_WYLIE == ct) ? 1 : 0)
                                 + ((TMW_TO_WYLIE_TEXT == ct) ? 1 : 0)
                                 == 1);
                long numAttemptedReplacements[] = new long[] { 0 };
                if (TMW_TO_SAME_TMW == ct) {
                    // Identity conversion for testing
                    if (tdoc.identityTmwToTmwConversion(0,
                                                        tdoc.getLength(),
                                                        numAttemptedReplacements)) {
                        exitCode = 50;
                    }
                } else if (TMW_TO_WYLIE == ct || TMW_TO_WYLIE_TEXT == ct) {
                    // Convert to THDL Wylie:
                    if (!tdoc.toWylie(0,
                                      tdoc.getLength(),
                                      numAttemptedReplacements)) {
                        exitCode = 44;
                    }
                } else if (TMW_TO_ACIP == ct || TMW_TO_ACIP_TEXT == ct) {
                    // Convert to ACIP:
                    if (!tdoc.toACIP(0,
                                     tdoc.getLength(),
                                     numAttemptedReplacements)) {
                        exitCode = 49;
                    }
                } else if (TMW_TO_UNI == ct) {
                    StringBuffer errors = new StringBuffer();
                    // Convert to Unicode:
                    if (tdoc.convertToUnicode(0,
                                              tdoc.getLength(),
                                              errors,
                                              ThdlOptions.getStringOption("thdl.tmw.to.unicode.font").intern(),
                                              numAttemptedReplacements)) {
                        System.err.println(errors);
                        exitCode = 42;
                    }
                } else if (TM_TO_TMW == ct) {
                    StringBuffer errors = new StringBuffer();
                    // Convert to TibetanMachineWeb:
                    if (tdoc.convertToTMW(0, tdoc.getLength(), errors,
                                          numAttemptedReplacements)) {
                        System.err.println(errors);
                        exitCode = 42;
                    }
                } else {
                    ThdlDebug.verify(TMW_TO_TM == ct);
                    StringBuffer errors = new StringBuffer();
                    // Convert to TibetanMachine:
                    if (tdoc.convertToTM(0, tdoc.getLength(), errors,
                                         numAttemptedReplacements)) {
                        System.err.println(errors);
                        exitCode = 42;
                    }
                }

                // Write to standard output the result:
                if (TMW_TO_WYLIE_TEXT == ct || TMW_TO_ACIP_TEXT == ct) {
                    try {
                        BufferedWriter bw
                            = new BufferedWriter(new OutputStreamWriter(out,
                                                                        "UTF-8"));
                        tdoc.writeTextOutput(bw);
                        bw.flush();
                    } catch (IOException e) {
                        exitCode = 40;
                    }
                } else {
                    try {
                        tdoc.writeRTFOutputStream(out);
                    } catch (IOException e) {
                        exitCode = 40;
                    }
                }
                if (out.checkError())
                    exitCode = 41;
                if (numAttemptedReplacements[0] < 1)
                    exitCode = 43;

                return exitCode;
            }
        }
    }
}
