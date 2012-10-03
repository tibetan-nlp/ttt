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

package org.thdl.tib.input;

import java.net.URL;

import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.util.RTFPane;
import org.thdl.util.ThdlLazyException;

/** A JskadKeyboard is the high-level view of a Tibetan-input keyboard
    that Jskad has.  Each keyboard is associated with a .ini file
    (except for the built-in, extended Wylie keyboard), an RTF
    document for end users, and a short identifying string.

    @author David Chandler
 */
public class JskadKeyboard {
    /** Cached RTFPane displaying the contents of the .rtf "quick
        reference" file associated with this keyboard. */
    private RTFPane keybdRTFPane = null;

    /* the .rtf file named here better be included in the jar in the
       same directory as 'Jskad.class': */
    /** an optional RTF document */
    private String keybdQuickRefFile;

    /** a short identifying string */
    private String keybdId;

    /** the name of the .ini file for this keyboard */
    private String keybdIniFile;

    /** the associated .ini file, which is read in only when needed
        and only once */
    private URL tibKeybdURL = null;

    /** Creates a new JskadKeyboard. 
     *  @param identifyingString a short string used in the GUI to
     *  identify this keyboard
     *  @param dotIniResourceName the name of the .ini file used to
     *  initialize this keyboard, or null for the built-in extended
     *  Wylie keyboard
     *  @param RTFResourceName the optional name of the .rtf file that
     *  gives users a quick reference to this keyboard (null if no
     *  such file is available) */
    public JskadKeyboard(String identifyingString,
                         String dotIniResourceName,
                         String RTFResourceName) {
        keybdId = identifyingString;
        keybdIniFile = dotIniResourceName;
        keybdQuickRefFile = RTFResourceName;
    }

    /** Returns an RTFPane displaying the contents of the "Quick
     *  Reference" .rtf file associated with this keyboard, or null if
     *  no such file is associated with this keyboard. */
    public RTFPane getQuickRefPane() {
        if (!hasQuickRefFile())
            return null;
        if (keybdRTFPane == null) {
            try {
                keybdRTFPane = new RTFPane(Jskad.class, keybdQuickRefFile);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                throw new ThdlLazyException(e); /* FIXME--handle this better. */
            }
        }
        return keybdRTFPane;
    }
    
    /** Returns true iff there is a "Quick Reference" document
        associated with this keyboard. */
    public boolean hasQuickRefFile() {
        return (keybdQuickRefFile != null);
    }

    /** Returns the short identifying string associated with this
     *  keyboard. */
    public String getIdentifyingString() {
        return keybdId;
    }

    /** Activates this keyboard for the given DuffPane.
     *  @param dp the DuffPane for which this keyboard will be made
     *  the active keyboard */
    public void activate(DuffPane dp) {
        if (keybdIniFile != null) {
            URL tibKeybdURL
                = TibetanMachineWeb.class.getResource(keybdIniFile);
            if (null == tibKeybdURL)
                throw new Error("Cannot load the keyboard initialization resource "
                                + keybdIniFile);
            dp.registerKeyboard(tibKeybdURL);
        } else {
            dp.registerKeyboard();
        }
    }
}
