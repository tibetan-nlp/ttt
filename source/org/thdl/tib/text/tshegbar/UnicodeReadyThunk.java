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

/** A UnicodeReadyThunk represents a string of codepoints.  While
 *  there are ways to turn a string of Unicode codepoints into a list
 *  of UnicodeReadyThunks (DLC reference it), you cannot
 *  necessarily recover the exact sequence of Unicode codepoints from
 *  a UnicodeReadyThunk.  For codepoints that are not Tibetan
 *  Unicode and are not one of a handful of other known codepoints,
 *  only the most primitive operations are available.  Generally in
 *  this case you can recover the exact string of Unicode codepoints,
 *  but don't bank on it.
 *
 *  @author David Chandler
 */
public interface UnicodeReadyThunk {

    /** Returns true iff this thunk is entirely Tibetan (regardless of
        whether or not all codepoints come from the Tibetan range of
        Unicode 3, i.e. <code>U+0F00</code>-<code>U+0FFF</code>, and
        regardless of whether or not this thunk is syntactically legal
        Tibetan). */
    public boolean isTibetan();
    
    /** Returns a sequence of Unicode codepoints that is equivalent to
     *  this thunk if possible.  It is only possible if {@link
     *  #hasUnicodeRepresentation()} is true.  Unicode has more than one
     *  way to refer to the same language element, so this is just one
     *  method.  When more than one Unicode sequence exists, and when
     *  the thunk {@link #isTibetan() is Tibetan}, this method returns
     *  sequences that the Unicode 3.2 standard does not discourage.
     *  @exception UnsupportedOperationException if {@link
     *  #hasUnicodeRepresentation()} is false
     *  @return a String of Unicode codepoints */
    public String getUnicodeRepresentation() throws UnsupportedOperationException;
    
    /** Returns true iff there exists a sequence of Unicode codepoints
     *  that correctly represents this thunk.  This will not be the
     *  case if the thunk contains Tibetan characters for which the
     *  Unicode standard does not provide.  See the Extended Wylie
     *  Transliteration System (EWTS) document (DLC ref, DLC mention
     *  Dza,fa,va doc bug) for more info, and see the Unicode 3
     *  standard section 9.13.  The presence of head marks or multiple
     *  vowels in the thunk would cause this to return false, for
     *  example.  */
    public boolean hasUnicodeRepresentation();
}

