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
import java.util.ListIterator;

/** A list of {@link TStackList} objects, each of which is for a
 *  stack (a grapheme cluster), typically corresponding to one
 *  ambiguous section of a tsheg bar.
 *
 *  @author David Chandler */
class TStackListList {
    /** a fast, non-thread-safe, random-access list implementation: */
    private ArrayList al;

    /** Creates an empty list. */
    public TStackListList() { al = new ArrayList(); }

    /** Creates a list containing just p. */
    public TStackListList(TStackList p) {
        al = new ArrayList(1);
        add(p);
    }

    /** Creates an empty list with the capacity to hold N items. */
    public TStackListList(int N) {
        al = new ArrayList(N);
    }

    /** Returns the ith pair in this list. */
    public TStackList get(int i) { return (TStackList)al.get(i); }

    /** Adds p to the end of this list. */
    public void add(TStackList p) { al.add(p); }

    /** Returns the number of TStackList objects in this list. */
    public int size() { return al.size(); }

    /** Returns true if and only if this list is empty. */
    public boolean isEmpty() { return al.isEmpty(); }

    /** Returns a human-readable representation.
     *  @return something like [[[(R . ), (D . O)], [(R . ), (J . E)]]] */
    public String toString() {
        return al.toString();
    }

    /** Returns true if and only if either x is a TStackListList
     *  object representing the same TStackList objects in the same
     *  order or x is a String that is equals to the result of {@link
     *  #toString()}. */
    public boolean equals(Object x) {
        if (x instanceof TStackListList) {
            return al.equals(((TStackListList)x).al);
        } else if (x instanceof String) {
            return toString().equals(x);
        }
        return false;
    }

    /** Returns a hashCode appropriate for use with our {@link
     *  #equals(Object)} method. */
    public int hashCode() { return al.hashCode(); }

    /** Returns an iterator for this list. Mutate this list while
     *  iterating and you'll have to read the code to know what will
     *  happen. */
    public ListIterator listIterator() { return al.listIterator(); }

    /** Returns something akin to the transliteration that was input
     *  (okay, maybe 1-2-3-4 instead of 1234, and maybe AUTPA instead
     *  of AUT-PA [ACIP examples]) corresponding to this stack list
     *  list. */
    public String recoverTranslit() {
        if (isEmpty()) return null;
        return get(0).recoverTranslit();
    }
}
