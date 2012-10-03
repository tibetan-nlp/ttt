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
Library (THDL). Portions created by the THDL are Copyright 2004 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text;


/**
* A stretch of Roman transliteration all in a certain font size.
*
* @author David Chandler */
class TranslitTuple {
    private final StringBuffer sb;
    private final int sz;

    /** Creates a TranslitTuple representing the transliteration s,
        which has font size sz. */
    public TranslitTuple(String s, int sz) {
        this.sb = new StringBuffer(s);
        this.sz = sz;
        // FIXME: assert(s.length() > 0);
    }

    /** Appends the transliteration s to this tuple and returns this
        tuple if sz, the font size for s, is the same as this tuple's
        font size.  Returns a new tuple for s otherwise. */
    public TranslitTuple getPossiblyCombinedTranslitTuple(String s, int sz) {
        if (this.sz == sz) {
            sb.append(s);
            return this;
        } else {
            return new TranslitTuple(s, sz);
        }
    }

    /** Returns the stretch of Roman transliteration. */
    public String getTranslit() { return sb.toString(); }

    /** Returns the font size of the Roman transliteration. */
    public int getFontSize() { return sz; }

    /** Do not call this -- it throws an error. */
    public String toString() {
        throw new Error("There was a bug where this was called, so don't call this.");
    }
}
