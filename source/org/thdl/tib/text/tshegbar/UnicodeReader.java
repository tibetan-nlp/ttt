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

package org.thdl.tib.text.tshegbar;


/** A UnicodeReader attempts to read from an InputStream and forms our
 *  <i>TTBIR</i>, our Tibetan Tsheg Bar Internal Representation.  You
 *  have the choice of requiring syntactically correct Tibetan,
 *  allowing all stacks, of allowing Unicode with erroneous (i.e.,
 *  syntactically incorrect) constructs, and even of turning a few
 *  erroneous Unicode constructs into best guesses.  Useful error
 *  messages are generated for less-than-perfect input.
 *
 *  @author David Chandler */
public class UnicodeReader {
    /** You cannot instantiate this class. */
    private UnicodeReader() { }
    // DLC NOW
    
//      public static TTBIR parsePerfectUnicode() {
//      }
}
