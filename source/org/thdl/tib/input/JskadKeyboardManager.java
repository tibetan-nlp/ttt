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

import java.util.Vector;

/** A JskadKeyboardManager maintains a list of JskadKeyboards.

    @author David Chandler
*/
public class JskadKeyboardManager {
    /** A Vector of JskadKeyboards.  Users will see the first
     *  keyboards most prominently. */
    private Vector keybds;

    /** Creates a manager without any keyboards in it, even the
     *  built-in extended Wylie keyboard. */
    public JskadKeyboardManager() {
        keybds = new Vector(5);
    }

    /** Creates a manager with the specified keyboards in it.  The
     *  keyboard at index 0 will be the most prominent in the user's
     *  eyes. */
    public JskadKeyboardManager(JskadKeyboard keyboards[])
        throws NullPointerException
    {
        this();
        for (int i = 0; i < keyboards.length; i++) {
            addKeyboard(keyboards[i]);
        }
    }

    /** Adds a JskadKeyboard to this manager.  It will be the least
     *  prominent in the user's eyes of any yet added. */
    public void addKeyboard(JskadKeyboard keybd)
        throws NullPointerException
    {
        if (null == keybd)
            throw new NullPointerException();
        keybds.add((Object)keybd);
    }
    
    /** Returns the JskadKeyboard at the zero-based index. */
    public JskadKeyboard elementAt(int index)
        throws ArrayIndexOutOfBoundsException
    {
        return (JskadKeyboard)keybds.elementAt(index);
    }

    /** Returns the number of JskadKeyboards being managed. */
    public int size() {
        return keybds.size();
    }

    /** Returns an array of the identifying strings associated with
     *  all managed keyboards. */
    
    public String[] getIdentifyingStrings() {
        String x[] = new String[size()];
        for (int i = 0; i < size(); i++) {
            x[i] = elementAt(i).getIdentifyingString();
        }
        return x;
    }
}
