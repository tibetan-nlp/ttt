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

import java.util.Vector;

/**
* A mutable representation of Roman transliteration with font size
* information for each character of transliteration.
*
* @author David Chandler */
class TranslitList {
    /** Invariant: For all 0<=i<length(),
        ((TranslitTuple)vec.get(i)).getFontSize() !=
        ((TranslitTuple)vec.get(i+1)).getFontSize(). */
    private final Vector /* of TranslitTuple */ vec;

    /** Creates an empty list. */
    public TranslitList() {
        this.vec = new Vector();
    }

    /** Returns the number of TranslitTuples in this list. */
    public int length() {
        return vec.size();
    }

    public TranslitTuple get(int i) throws ArrayIndexOutOfBoundsException {
        return (TranslitTuple)vec.get(i);
    }

    /** Appends to the end of this list a single character of Roman
        transliteration with the given font size.  The last element of
        this list will have s appended to it if font sizes are the
        same; otherwise this list grows by an element. */
    public void append(char ch, int fontSize) {
        append(new String(new char[] { ch }), fontSize);
    }

    /** Appends to the end of this list a stretch s of Roman
        transliteration that has font size fontSize.  The last element
        of this list will have s appended to it if font sizes are the
        same; otherwise this list grows by an element. */
    public void append(String s, int fontSize) {
        if (vec.isEmpty()) {
            vec.add(new TranslitTuple(s, fontSize));
        } else {
            TranslitTuple tt = (TranslitTuple)vec.lastElement();
            TranslitTuple newtt
                = tt.getPossiblyCombinedTranslitTuple(s, fontSize);
            if (tt != newtt)
                vec.add(newtt);
        }
    }

    /** Appends to the end of this list another TranslitList.  The
        length of this list may or may not increase; the first element
        of tb and the last element of this list will be merged if
        their font sizes are the same. */
    public void append(TranslitList tb) {
        if (this == tb)
            throw new IllegalArgumentException("Cannot be this list, that would be bad!");
        if (this.vec.isEmpty() || tb.vec.isEmpty()) {
            this.vec.addAll(tb.vec);
        } else {
            int lbefore = this.length();
            this.vec.addAll(tb.vec);
            if (((TranslitTuple)tb.vec.firstElement()).getFontSize()
                == ((TranslitTuple)this.vec.lastElement()).getFontSize()) {
                // merge stretches with the same font size.
                TranslitTuple a = (TranslitTuple)this.vec.remove(lbefore-1);
                this.vec.set(lbefore-1,
                             a.getPossiblyCombinedTranslitTuple(((TranslitTuple)this.vec.get(lbefore-1)).getTranslit(),
                                                                ((TranslitTuple)this.vec.get(lbefore-1)).getFontSize()));
            }
        }
    }

    /** Do not call this -- it throws an error. */
    public String toString() {
        throw new Error("There was a bug where this was called, so don't call this.");
    }

    /** Returns the full Roman transliteration.  You don't get font
        size information this way, of course. */
    public String getString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length(); i++) {
            sb.append(get(i).getTranslit());
        }
        return sb.toString();
    }
}
