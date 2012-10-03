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
Library (THDL). Portions created by the THDL are Copyright 2005 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

/** A class for use in XSL transformations that converts EWTS
 *  transliteration to Unicode.  Note that the syntax for
 *  calling Java extensions from XSL is vendor-specific; 
 *  for more details, please consult the documentation for the
 *  XSLT processor you use, for example Saxon or 
 *  Xalan-Java.
 *  @author David Chandler
 */
public class EwtsToUnicodeForXslt {
    /** Static methods provide all the fun! */
    private EwtsToUnicodeForXslt() {
        throw new Error("There's no point in instantiating this class.");
    }

    /** Converts EWTS transliteration into Tibetan Unicode.
     */
    public static String convertEwtsTo(String ewts) {
        return TConverter.convertToUnicodeText(EWTSTraits.instance(),
                                               ewts,
                                               new StringBuffer(),
                                               null,
                                               false,
                                               "None",
                                               false);
    }
}
