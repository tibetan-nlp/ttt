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

package org.thdl.tib.text;

/**
* A wrapper object for a stretch of TibetanMachineWeb (TMW) or
* TibetanMachine (TM) data that shares the same font.  A piece of
* DuffData consists of a font number and a string.  The fact that this
* stretch is TMW vs. TM is not stored in this object; the client must
* remember that itself.
*
* For TMW, the font number is a number from one to ten, corresponding
* to the ten TibetanMachineWeb fonts, as follows:
* <p>
*    1 - TibetanMachineWeb<br>
*    2 - TibetanMachineWeb1<br>
*    ...<br>
*    10 - TibetanMachineWeb9<br>
* <p>
* For TM, the font number is a number from one to five, corresponding
* to the five TibetanMachineWeb fonts, as follows:
* <p>
*    1 - TibetanMachine<br>
*    2 - TibetanMachineSkt1<br>
*    3 - TibetanMachineSkt2<br>
*    4 - TibetanMachineSkt3<br>
*    5 - TibetanMachineSkt4<br>
* <p>
* The string represents a contiguous stretch of data in that font,
* i.e. a stretch of TibetanMachineWeb or TibetanMachine that doesn't
* require a font change.  */
public class DuffData {
/**
* a string of text
*/
    public String text;
/**
* the font number for this text (see class description)
*/
    public int font;

/**
* @param s a string of TibetanMachineWeb or TibetanMachine text
* @param i a TibetanMachineWeb or TibetanMachine font number
*/
    public DuffData(String s, int i) {
        text = s;
        font = i;
    }

    /** Default constructor.  The DuffData is invalid after this until
        you call setData or manipulate the public fields. */
    public DuffData() { }

    /** here for efficiency */
    private static char[] chars = new char[1];

/** Changes the text and font this DuffData represents.
* @param c a character of TibetanMachineWeb or TibetanMachine text
* @param i a TibetanMachineWeb or TibetanMachine font number
*/
    public void setData(char c, int i) {
        chars[0] = c;
        text = new String(chars);
        font = i;
    }

/** Changes the text and font this DuffData represents.
* @param s a String of TibetanMachineWeb or TibetanMachine text
* @param i a TibetanMachineWeb or TibetanMachine font number
*/
    public void setData(String s, int i) {
        text = s;
        font = i;
    }
}
