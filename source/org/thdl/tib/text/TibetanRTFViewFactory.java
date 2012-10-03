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

package org.thdl.tib.text;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import org.thdl.util.ThdlOptions;

/** A ViewFactory that is cognizant of the line-wrapping rules for
 *  Tibetan text.  That is, this class knows about the tsheg and other
 *  Tibetan punctuation.
 *  @author David Chandler */
class TibetanRTFViewFactory implements ViewFactory {

    /** the delegatee */
    private ViewFactory delegatee = null;

    /** true iff the Tibetan-aware views that we create should include
        copious debugging output */
    private static boolean debugLog = false;

    /** true if we've already inquired about the preference and stored
        it in debugLog */
    private static boolean debugLogInitialized = false;

    /** Returns true iff the Tibetan-aware views that we create should
        include copious debugging output. */
    private static boolean getDebugLog() {
        if (false == debugLogInitialized) {
            debugLogInitialized = true;
            debugLog
                = ThdlOptions.getBooleanOption("thdl.log.line.breaking.algorithm");
        }
        return debugLog;
    }
    
    
    /** Do not call this. */
    private TibetanRTFViewFactory() {
        super();
    }

    /** Creates a new TibetanRTFViewFactory that delegates to vf when
     *  unknown elements are encountered.
     *  @throws NullPointerException if d is null */
    public TibetanRTFViewFactory(ViewFactory d)
        throws NullPointerException
    {
        if (null == d) throw new NullPointerException();
        delegatee = d;
    }
    
    /** Returns a View that will break correctly at Tibetan
     *  punctuation. */
    public View create(Element el) {
        String elName = el.getName();
        if (null != elName
            && elName.equals(AbstractDocument.ContentElementName)) { // FIXME: is this right?  what about paragraph-level stuff?
            return new TibetanLabelView(el, getDebugLog());
        } else {
            // We don't know what to do, so delegate:
            return delegatee.create(el);
        }
    }
}
