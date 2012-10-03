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

package org.thdl.tib.text;

import java.util.ArrayList;

/** A list of pseudo-grapheme clusters (vowels appear alone, FIXME:
 *  change the name) all in TibetanMachineWeb.
 *  @author David Chandler */
class TMWGCList implements TGCList {
    private ArrayList al;

    /** Constructs an empty TMWGCList. */
    TMWGCList() {
        al = new ArrayList();
    }

    /** Constructs an empty TMWGCList ready to hold size TGCPairs. */
    TMWGCList(int size) {
        al = new ArrayList(size);
    }

    public int size() { return al.size(); }

    public TGCPair get(int i) {
        return (TGCPair)al.get(i);
    }

    void add(TGCPair tp) {
        al.add(tp);
    }

    public String toString() {
        return al.toString();
    }
}
