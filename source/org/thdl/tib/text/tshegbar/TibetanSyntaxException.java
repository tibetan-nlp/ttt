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
Library (THDL). Portions created by the THDL are Copyright 2001-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.tshegbar;

import java.util.Vector;

/** DLC FIXMEDOC: says "this isn't legal Tibetan", not "this isn't a valid sequence of Unicode" */
class TibetanSyntaxException extends Exception {
    /** This constructor creates an exception with a less than helpful
     *  message for the end user.  Please don't use this constructor
     *  for production code. */
    TibetanSyntaxException() {
        super("A Unicode input stream had a syntactically incorrect run of Tibetan.  For example, kha, i.e., U+0F41, is not an allowed prefix.  This run of Tibetan was not expected.");
        // we can tell it wasn't expected, because this error message
        // isn't very helpful, and one of the other constructors
        // should've been used.
    }

    /** DLC FIXMEDOC */
    TibetanSyntaxException(String x) {
        super(x);
    }

    /** DLC FIXMEDOC

        @param grcls a Vector whose elements x are GraphemeClusters
        where x is in the range [start, end)
        @param start grcls.elementAt(start) is the first
        GraphemeCluster in the syntactically incorrect stretch of
        Tibetan.
        @param end grcls.elementAt(end - 1) is the last
        GraphemeCluster in the syntactically incorrect stretch of
        Tibetan. */
    TibetanSyntaxException(Vector grcls, int start, int end) {
        throw new Error("DLC NOW");
    }
}
