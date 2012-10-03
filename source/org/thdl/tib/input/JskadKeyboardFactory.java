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

import java.util.Properties;

import org.thdl.util.ThdlOptions;

/** A JskadKeyboardFactory determines which Tibetan keyboards Jskad
    supports.  Adding a new one is as easy as adding 3 lines of text
    to this class's source code.
*/
public class JskadKeyboardFactory {
    private static final String keyIniPath = "/keyboards.ini";

    /** Reads /keyboards.ini to learn which Tibetan keyboards are
     *  available.  Returns them. */
    public static JskadKeyboard[] getAllAvailableJskadKeyboards()
        throws Exception
    {
        Properties keyboardProps
            = ThdlOptions.getPropertiesFromResource(JskadKeyboardFactory.class,
                                                    keyIniPath,
                                                    false,
                                                    null);
        String numberOfKeyboardsString
            = keyboardProps.getProperty("number.of.keyboards", null);
        if (null == numberOfKeyboardsString) {
            throw new Exception(keyIniPath
                                + " doesn't contain a number.of.keyboards property");
        }
        int numberOfKeyboards;
        try {
			Integer num = new Integer(numberOfKeyboardsString);
            numberOfKeyboards = num.intValue();
            if (numberOfKeyboards < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new Exception(keyIniPath
                                + " has a number.of.keyboards property, but it's not a nonnegative integer.");
        }

        JskadKeyboard[] keyboards;
        keyboards = new JskadKeyboard[numberOfKeyboards];
        for (int i = 1; i <= numberOfKeyboards; i++) {
            String description
                = keyboardProps.getProperty("keyboard.name.for.popup." + i,
                                            null);
            String iniFile
                = keyboardProps.getProperty("keyboard.ini.file." + i,
                                            null);
            String rtfFile
                = keyboardProps.getProperty("keyboard.rtf.help.file." + i,
                                            null);
            if (null == description)
                throw new Exception(keyIniPath
                                    + ": keyboard.name.for.popup." + i + " not defined.");
            if (null == iniFile)
                throw new Exception(keyIniPath
                                    + ": keyboard.ini.file." + i + " not defined.");
            if (null == rtfFile)
                throw new Exception(keyIniPath
                                    + ": keyboard.rtf.help.file." + i + " not defined.");
            if (iniFile.equals("nil"))
                iniFile = null;
            if (rtfFile.equals("nil"))
                rtfFile = null;
            keyboards[i - 1] = new JskadKeyboard(description,
                                                 iniFile,
                                                 rtfFile);
        }
        return keyboards;
    }
}

