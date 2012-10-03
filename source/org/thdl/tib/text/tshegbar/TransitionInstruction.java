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

import org.thdl.util.ThdlDebug;


/** DLC FIXMEDOC */
class TransitionInstruction implements UnicodeReadingStateMachineConstants {
    private TransitionInstruction() { super(); }
    TransitionInstruction(int nextState, int action) {
        super();

        ThdlDebug.verify(action == ACTION_CONTINUES_GRAPHEME_CLUSTER
                         || action == ACTION_BEGINS_NEW_GRAPHEME_CLUSTER
                         || action == ACTION_PREPEND_WITH_0F68); // DLC FIXME: assert this.

        ThdlDebug.verify(nextState == STATE_START
                         || nextState == STATE_READY
                         || nextState == STATE_DIGIT
                         || nextState == STATE_STACKING
                         || nextState == STATE_STACKPLUSACHUNG
                         || nextState == STATE_PARTIALMARK); // DLC FIXME: assert this.

        // we start in the start state, but we can never return to it.
        ThdlDebug.verify(nextState != STATE_START); // DLC FIXME: assert this.
        
        this.nextState = nextState;
        this.action = action;
    }

    /** the state (e.g., {@link #STATE_READY}) to which to transition
     *  next */
    private int nextState;
    
    /** the action to perform upon transition, either {@link
     *  #ACTION_CONTINUES_GRAPHEME_CLUSTER}, {@link
     *  #ACTION_BEGINS_NEW_GRAPHEME_CLUSTER}, or {@link
     *  #ACTION_PREPEND_WITH_0F68} */
    private int action;

    int getAction() { return action; }
    int getNextState() { return nextState; }


    /** Returns the codepoint class for cp, e.g. {@link
     *  UnicodeReadingStateMachineConstants#CC_SJC}.
     *  @param cp a Unicode codepoint, which MUST be nondecomposable
     *  if it is in the Tibetan range but can be from outside the
     *  Tibetan range of Unicode */
    static int getCCForCP(char cp) {
      // DLC does not compile:        ThdlDebug.verify(getNFTHDL(cp) == null); // DLC FIXME: assert this
        if ('\u0F82' == cp) {
            return CC_0F82;
        } else if ('\u0F8A' == cp) {
            return CC_0F8A;
        } else if ('\u0F39' == cp) {
            return CC_0F39;
        } else if ('\u0F71' == cp) {
            return CC_SUBSCRIBED_ACHUNG;
        } else if ('\u0F40' <= cp && cp <= '\u0F6A') {
            ThdlDebug.verify(cp != '\u0F48'); // DLC FIXME: assert this
            return CC_CON;
        } else if ('\u0F90' <= cp && cp <= '\u0FBC') {
            ThdlDebug.verify(cp != '\u0F98'); // DLC FIXME: assert this
            return CC_SJC;
        } else if ('\u0F20' <= cp && cp <= '\u0F33') {
            return CC_DIGIT;
        } else if (/* DLC NOW do these combine ONLY with digits, or do CC_CM just NOT combine with digits? */
                   '\u0F3E' == cp
                   || '\u0F3F' == cp
                   || '\u0F18' == cp
                   || '\u0F19' == cp) {
            return CC_MCWD;
        } else if ('\u0FC6' == cp
                   || '\u0F87' == cp
                   || '\u0F86' == cp
                   || '\u0F84' == cp
                   || '\u0F83' == cp
                   || '\u0F82' == cp
                   || '\u0F7F' == cp
                   || '\u0F7E' == cp
                   || '\u0F37' == cp /* DLC NOW NORMALIZATION OF 0F10, 11 to 0F0F ??? */
                   || '\u0F35' == cp) {
            return CC_CM;
        } else if ('\u0F72' == cp
                   || '\u0F74' == cp
                   || '\u0F7A' == cp
                   || '\u0F7B' == cp
                   || '\u0F7C' == cp
                   || '\u0F7D' == cp
                   || '\u0F80' == cp) {
            // DLC what about U+0F84 ??? CC_V or CC_CM ?
            return CC_V;
        } else {
            return CC_SIN;
        }
    }

}
