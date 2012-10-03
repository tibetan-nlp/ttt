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

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlLazyException;
import org.thdl.util.ThdlOptions;

/** The ConverterGUI is a Swing GUI application.  It is used for
 *  converting TibetanMachine-, TibetanMachineWeb-, and THDL Extended
 *  Wylie-encoded Rich Text Files to any of several formats, including
 *  Unicode.  The TibetanMachine-to-TibetanMachineWeb conversion is
 *  flawless, and is used as an intermediate step in other
 *  conversions, for example the TibetanMachine-to-Unicode conversion.
 *  @author David Chandler */
public class ConverterGUI implements FontConversion, FontConverterConstants {
    /** Default constructor; does nothing */
    ConverterGUI() { }

    /**
     *  Runs the converter. */
	public static void main(String[] args) {
        // No need for the TM or TMW fonts.
        System.setProperty("thdl.rely.on.system.tmw.fonts", "true");
        System.setProperty("thdl.rely.on.system.tm.fonts", "true");

        System.exit(realMain(args, System.out, null));
    }

    public boolean doConversion(ConvertDialog cd, File oldFile, File newFile,
                                String whichConversion, String warningLevel,
                                boolean shortMessages, boolean colors) {
        PrintStream ps;
        try {
            if (whichConversion == ACIP_TO_UNI_TEXT) {
                JOptionPane.showMessageDialog(cd,
                                              "This conversion will lose information about relative font sizes.\n{KA (KHA) GA} will be treated like {KA KHA GA}, that is.",
                                              "Loss of information may result",
                                              JOptionPane.WARNING_MESSAGE);
            }
            returnCode
                = TibetanConverter.reallyConvert(new FileInputStream(oldFile),
                                                 ps = new PrintStream(new FileOutputStream(newFile),
                                                                      false),
                                                 whichConversion,
                                                 warningLevel,
                                                 shortMessages,
                                                 colors);
            ps.close();
        } catch (FileNotFoundException e) {
            returnCode = 39;
            JOptionPane.showMessageDialog(cd,
                                          "The conversion failed because either the old\nfile could not be found or the new file could\nnot be written (because it was open\nelsewhere or read-only or what have you).",
                                          "Conversion failed",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (3 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          TibetanConverter.rtfErrorMessage,
                                          "Conversion failed",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (44 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "Though an output file has been created, it contains ugly\nerror messages like\n\"<<[[JSKAD_TMW_TO_WYLIE_ERROR_NO_SUCH_WYLIE:\n    Cannot convert DuffCode...\".\nPlease edit the output by hand to replace all such\ncreatures with the correct EWTS transliteration.",
                                          "Attention required",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (49 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "Though an output file has been created, it contains ugly\nerror messages like\n\"<<[[JSKAD_TMW_TO_ACIP_ERROR_NO_SUCH_ACIP:\n    Cannot convert DuffCode...\".\nPlease edit the output by hand to replace all such\ncreatures with the correct ACIP transliteration.",
                                          "Attention required",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (50 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "Error doing RTF->RTF identity copy.",
                                          "Attention required",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (43 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "Though an output file has been created, this conversion did nothing.\nDid you choose the correct original file?\nDid you choose the correct type of conversion?",
                                          "Nothing to do",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (42 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "Some of the document cannot be converted.  The output\ncontains the problem glyphs.  E-mail David Chandler\nwith your suggestions about the proper way to handle\nsuch a document.",
                                          "Errors in Conversion",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (45 == returnCode) {
            if (warningLevel == "None") throw new Error("FIXME: make this an assertion");
            JOptionPane.showMessageDialog(cd,
                                          "No errors occurred, but some warnings are embedded in\nthe output as [#WARNING...].",
                                          "Warnings in Conversion",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (46 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "Errors occurred, and are embedded in the output\nas [#ERROR...]."
                                          + ((warningLevel == "None")
                                             ? ""
                                             : "  Warnings may have occurred; if so,\nthey are embedded in the output as [#WARNING...]."),
                                          "Errors in Conversion",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (47 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "So many errors occurred that the document is likely\nEnglish, not Tibetan.  No output was produced.",
                                          "Many Errors in Conversion",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (1 == returnCode) {
            if (FIND_SOME_NON_TMW == whichConversion
                || FIND_ALL_NON_TMW == whichConversion) {
                JOptionPane.showMessageDialog(cd,
                                              "Something besides TibetanMachineWeb was found; see output file.",
                                              "Not entirely TMW",
                                              JOptionPane.PLAIN_MESSAGE);
            } else if (FIND_SOME_NON_TM == whichConversion
                       || FIND_ALL_NON_TM == whichConversion) {
                JOptionPane.showMessageDialog(cd,
                                              "Something besides TibetanMachine was found; see output file.",
                                              "Not entirely TM",
                                              JOptionPane.PLAIN_MESSAGE);
            } else {
                throw new Error("Who returned this??");
            }
            return false;
        } else if (0 != returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "The conversion failed with code " + returnCode + "; please e-mail\ndchandler@users.sourceforge.net to learn what that means if\nyou can't find out from the output.",
                                          "Conversion failed",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            if (!ThdlOptions.getBooleanOption("thdl.skip.conversion.success.message")) {
                if (FIND_SOME_NON_TMW == whichConversion
                    || FIND_ALL_NON_TMW == whichConversion) {
                    JOptionPane.showMessageDialog(cd,
                                                  "Nothing except TibetanMachineWeb was found.",
                                                  "All TMW",
                                                  JOptionPane.PLAIN_MESSAGE);
                } else if (FIND_SOME_NON_TM == whichConversion
                           || FIND_ALL_NON_TM == whichConversion) {
                    JOptionPane.showMessageDialog(cd,
                                                  "Nothing except TibetanMachine was found.",
                                                  "All TM",
                                                  JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(cd,
                                                  "The conversion went perfectly.",
                                                  "Conversion succeeded",
                                                  JOptionPane.PLAIN_MESSAGE);
                }
            }
            return true;
        }
    }

    public String getDefaultDirectory() {
        return ThdlOptions.getStringOption("thdl.Jskad.working.directory",
                                           null);
    }

    /** the exit status of this application */
    private static int returnCode = 0;

    /** Runs the converter without exiting the program.
     *  @return the exit code. */
    public static int realMain(String[] args, PrintStream out, Frame owner) {
        returnCode = 0;
        try {
            final ConvertDialog convDialog;
            if (null == owner) {
                convDialog
                    = new ConvertDialog(new ConverterGUI(),
                                        ThdlOptions.getBooleanOption("thdl.debug")
                                        ? DEBUG_CHOICES : CHOICES,
                                        true);
            } else {
                convDialog
                    = new ConvertDialog(owner,
                                        new ConverterGUI(),
                                        ThdlOptions.getBooleanOption("thdl.debug")
                                        ? DEBUG_CHOICES : CHOICES,
                                        true);
            }

            /* Make it so that any time the user exits this program by
             * (almost) any means, the user's preferences are saved if
             * the SecurityManager allows it and the path is correct.
             * This means that the program used to "Open Document"
             * will be remembered. */
            Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        try {
                            if (!ThdlOptions.saveUserPreferences()) {
                                JOptionPane.showMessageDialog(convDialog,
                                                              "You previously cleared preferences,\nso you cannot now save them.",
                                                              "Cannot Save User Preferences",
                                                              JOptionPane.PLAIN_MESSAGE);
                            }
                        } catch (IOException ioe) {
                            System.out.println("IO Exception saving user preferences to " + ThdlOptions.getUserPreferencesPath());
                            ioe.printStackTrace();
                            ThdlDebug.noteIffyCode();
                        }
                        
                    }
                });

			convDialog.setVisible(true);
            return returnCode;
        } catch (ThdlLazyException e) {
            out.println("ConverterGUI has a BUG:");
            e.getRealException().printStackTrace(out);
            return 7;
        }
	}
}
