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


/** Constants used by ConvertDialog.

    @author Nathaniel Garson, Tibetan and Himalayan Digital Library */
public interface FontConverterConstants
{
    final String UNI_TO_WYLIE_TEXT = "Unicode to Wylie (UTF-16 Text->Text)";
    final String WYLIE_TO_UNI_TEXT = "Wylie to Unicode (Text->Text)";
    final String WYLIE_TO_TMW = "Wylie to TMW (Text->RTF)";
    final String WYLIE_TO_ACIP_TEXT = "Wylie to ACIP (Text->Text)";    
    final String TMW_TO_SAME_TMW = "TMW to the same TMW (for testing only) (RTF->RTF)";
    final String ACIP_TO_UNI_TEXT = "ACIP to Unicode (Text->Text)";
    final String ACIP_TO_WYLIE_TEXT = "ACIP to Wylie (Text->Text)";
    final String ACIP_TO_TMW = "ACIP to TMW (Text->RTF)";
    final String TMW_TO_ACIP = "TMW to ACIP (RTF->RTF)";
    final String TMW_TO_ACIP_TEXT = "TMW to ACIP (RTF->Text)";
    final String TM_TO_TMW = "TM to TMW (RTF->RTF)";
    final String TMW_TO_TM = "TMW to TM (RTF->RTF)";
    final String TMW_TO_UNI = "TMW to Unicode (RTF->RTF)";
    final String TMW_TO_WYLIE = "TMW to Wylie (RTF->RTF)";
    final String TMW_TO_WYLIE_TEXT = "TMW to Wylie (RTF->Text)";
    final String FIND_SOME_NON_TMW = "Find some non-TMW (in RTF)";
    final String FIND_SOME_NON_TM = "Find some non-TM (in RTF)";
    final String FIND_ALL_NON_TMW = "Find all non-TMW (in RTF)";
    final String FIND_ALL_NON_TM = "Find all non-TM (in RTF)";

    final String[] CHOICES = new String[] {
        WYLIE_TO_UNI_TEXT,
        WYLIE_TO_TMW,
        //WYLIE_TO_ACIP_TEXT,
        ACIP_TO_UNI_TEXT,
        ACIP_TO_WYLIE_TEXT,
        ACIP_TO_TMW,
        TMW_TO_ACIP,
        TMW_TO_ACIP_TEXT,
        TM_TO_TMW,
        TMW_TO_TM,
        TMW_TO_UNI,
        TMW_TO_WYLIE,
        TMW_TO_WYLIE_TEXT,
    	UNI_TO_WYLIE_TEXT,
        FIND_SOME_NON_TMW,  // TODO(dchandler): should this be in DEBUG_CHOICES only?
        FIND_SOME_NON_TM,   // TODO(dchandler): should this be in DEBUG_CHOICES only?
        FIND_ALL_NON_TMW,   // TODO(dchandler): should this be in DEBUG_CHOICES only?
        FIND_ALL_NON_TM     // TODO(dchandler): should this be in DEBUG_CHOICES only?
    };

    final String[] DEBUG_CHOICES = new String[] {
        TMW_TO_SAME_TMW,
        WYLIE_TO_UNI_TEXT,
        WYLIE_TO_TMW,
        ACIP_TO_UNI_TEXT,
        ACIP_TO_WYLIE_TEXT,
        ACIP_TO_TMW,
        TMW_TO_ACIP,
        TMW_TO_ACIP_TEXT,
        TM_TO_TMW,
        TMW_TO_TM,
        TMW_TO_UNI,
        TMW_TO_WYLIE,
        TMW_TO_WYLIE_TEXT,
        UNI_TO_WYLIE_TEXT,
        FIND_SOME_NON_TMW,
        FIND_SOME_NON_TM,
        FIND_ALL_NON_TMW,
        FIND_ALL_NON_TM
    };

    final String suggested_WYLIE_prefix = "THDL_Wylie_";
    final String suggested_ACIP_prefix = "ACIP_";
    final String suggested_TO_TMW_prefix = "TMW_";
    final String suggested_TO_UNI_prefix = "Uni_";
    final String suggested_TO_TM_prefix = "TM_";

    // String Constants
    public final String PROGRAM_TITLE = "THDL Tibetan Converters -- featuring Jskad Technology";
}