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

import java.io.File;

/** A FontConversion is an implementer of the conversions built into
 *  (@link #ConvertDialog}.
 *  @author Nathaniel Garson, Tibetan and Himalayan Digital Library */
interface FontConversion
{
    /** Returns the directory to be displayed when the user selects
        "Browse..." to look for either the new or old file.  May
        return null if no particular choice seems more appropriate
        than any other. */
    String getDefaultDirectory();

    /** Converts oldFile to newFile, yielding a modal dialog box
        displaying the results if you want happy users.  The
        conversion performed is specified by the interned String
        whichConversion, which must be one of the known conversions.
        If you want colors to be used in the output (which is only
        supported by a few conversions), then colors must be true.  If
        you want short error and warning messages for ACIP to Tibetan
        conversions, then shortMessages must be true.
        @return true on success, false otherwise */
    boolean doConversion(ConvertDialog cd, File oldFile,
                         File newFile, String whichConversion,
                         String warningLevel, boolean shortMessages,
                         boolean colors);
}
