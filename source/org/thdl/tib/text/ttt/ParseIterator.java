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
import java.util.NoSuchElementException;

/** An object that can iterate over an {@link TParseTree}.  NOTE: This
 *  constructs the list over which it iterates when it is constructed,
 *  so you pay upfront.
 *
 *  @author David Chandler */
class ParseIterator {
    private ArrayList al = null;
    private int sz;
    private ListIterator[] iterators;
    private boolean first = true;
    private boolean hasNextParse = true;
    /** Constructs a new ParseIterator that iterates over a list of
     *  TStackListLists. */
    ParseIterator(ArrayList al) {
        this.al = al;
        sz = al.size();
        iterators = new ListIterator[sz];
        hasNextParse = false;
        for (int i = 0; i < sz; i++) {
            iterators[i] = ((TStackListList)al.get(i)).listIterator();
            if (iterators[i].hasNext())
                hasNextParse = true;
        }
    }

    /** Returns true if and only if there is another parse
     *  available. */
    boolean hasNext() {
        return hasNextParse;
    }

    /** Returns the next available parse. */
    TStackList next() {
        if (!hasNextParse)
            throw new NoSuchElementException("no parses left");
        if (first) {
            first = false;
            TStackList x = new TStackList();
            for (int i = 0; i < sz; i++) {
                TStackList nextSL = (TStackList)iterators[i].next();
                x.addAll(nextSL);
            }

            // The next guy is found by taking the previous item of
            // each iterator.
            hasNextParse = false;
            for (int i = sz - 1; i >= 0; i--) {
                if (iterators[i].hasNext()) {
                    iterators[i].next();
                    hasNextParse = true;
                    break;
                }
            }
            return x;
        }

        // Up the rightmost iterator you can.  If you can, reset all
        // guys to the right of it.  If you can't, we're done.
        TStackList x = new TStackList(sz);
        hasNextParse = false;
        for (int i = sz - 1; i >= 0; i--) {
            TStackList prevSL = (TStackList)iterators[i].previous();
            x.addAll(0, prevSL);
            iterators[i].next();
            if (!hasNextParse && iterators[i].hasNext()) {
                hasNextParse = true;
                iterators[i].next();
                // Reset all iterators to the right of i.
                for (int j = i + 1; j < sz; j++) {
                    while (iterators[j].hasPrevious())
                        iterators[j].previous();
                    iterators[j].next();
                }
            }
        }
        return x;
    }
}
