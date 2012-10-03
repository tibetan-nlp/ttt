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

package org.thdl.tib.text.ttt;

import java.util.ArrayList;

import org.thdl.tib.text.TGCList;
import org.thdl.tib.text.TGCPair;

/** A list of grapheme clusters.  If you use this for anything other
 *  than testing the legality (the Tibetanness, if you will) of a
 *  tsheg-bar, then you'll probably fail because U+0F7F, U+0F35, and
 *  U+0F37 get special treatment.
 *
 *  @author David Chandler */
class TTGCList implements TGCList {
    // I could use one list of an ordered pair (TGCPair, int), but I
    // use two lists.
    private ArrayList al;
    private ArrayList stackIndices;

    /** Don't use this. */
    private TTGCList() { }

    /** Creates a TGCList.  Note that U+0F7F, U+0F35, and U+0F37 get
     *  special treatment because the sole use of this class is for
     *  testing the legality of a tsheg bar. */
    public TTGCList(TStackList sl) {
        al = new ArrayList();
        stackIndices = new ArrayList();
        int sz = sl.size();
        for (int i = 0; i < sz; i++) {
            sl.get(i).populateWithTGCPairs(al, stackIndices, i);
        }
    }

    /** Returns the ith pair in this list. */
    public TGCPair get(int i) {
        return (TGCPair)al.get(i);
    }

    /** Returns the number of TGCPairs in this list. */
    public int size() { return al.size(); }

    /** Returns a zero-based index of a TPairList inside the stack
     *  list from which this list was constructed.  This pair list is
     *  the one that caused the TGCPair at index tgcPairIndex to come
     *  into existence. */
    public int getTPairListIndex(int tgcPairIndex) {
        return ((Integer)stackIndices.get(tgcPairIndex)).intValue();
    }

    public String toString() {
        return ("<a TTGCList of " + size() + " TGCPairs: " + al.toString()
                + " with stack indices " + stackIndices.toString() + ">");
    }
}
