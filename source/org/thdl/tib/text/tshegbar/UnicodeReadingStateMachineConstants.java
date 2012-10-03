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

/** Constants useful in writing state machines for transforming
 *  Unicode input into other forms.
 *
 *  @see TransitionInstruction#getCCForCP(char)
 *
 *  @author David Chandler
 */
interface UnicodeReadingStateMachineConstants {

    // Codepoint classes (CC_...) follow.  These are mutually
    // exclusive, and their union is the whole of Unicode.

    /** for everything else, that is to say non-Tibetan characters
     *  like U+0E00 and also Tibetan characters like U+0FCF and U+0F05
     *  (DLC rename SIN[GLETON] to OTHER as combining marks from
     *  outside the Tibetan range count as this) but not U+0F8A */
    static final int CC_SIN = 0;

    /** for combining marks in the Tibetan range of Unicode that
     *  combine with digits alone */
    static final int CC_MCWD = 1;

    /** for combining marks in the Tibetan range of Unicode, minus
     *  CC_MCWD, U+0F82, (DLC U+0F83???) and U+0F39 */
    static final int CC_CM = 2;

    /** for combining consonants, that is to say U+0F90-U+0FBC minus
     *  U+0F98 minus the decomposable entries like U+0F93, U+0F9D,
     *  U+0FA2, etc. */
    static final int CC_SJC = 3;

    /** for noncombining consonants, that is to say U+0F40-U+0F6A
     *  minus U+0F48 minus the decomposable entries like U+0F43,
     *  U+0F4D, U+0F52, etc. */
    static final int CC_CON = 4;

    /** for simple, nondecomposable vowels, that is to say U+0F72,
     *  U+0F74, U+0F7A, U+0F7B, U+0F7C, U+0F7D, U+0F80 */
    static final int CC_V = 5;

    /** for U+0F8A */
    static final int CC_0F8A = 6;

    /** for U+0F82, which is treated like {@link #CC_CM} except after
     *  U+0F8A (DLC FIXME -- do we need similar treatment for 0F83???) */
    static final int CC_0F82 = 7;

    /** for U+0F39, an integral part of a consonant when it directly
     *  follows a member of CM_CONS or CM_SJC */
    static final int CC_0F39 = 8;

    /** for U+0F71 */
    static final int CC_SUBSCRIBED_ACHUNG = 9;

    /** for digits, that is to say U+0F20-U+0F33 */
    static final int CC_DIGIT = 10;



    // states STATE_...:

    /** initial state */
    static final int STATE_START = 0;

    /** ready state, that is to say the state in which some non-empty
     *  Unicode String is in the holding area, <i>ready</i> to receive
     *  combining marks like U+0F35 */
    static final int STATE_READY = 1;

    /** digit state, that is to say the state in which some non-empty
     *  Unicode String consisting entirely of digits is in the holding
     *  area, ready to receive marks that combine only with digits */
    static final int STATE_DIGIT = 2;

    /** state in which CC_SJC are welcomed and treated as consonants
     *  to be subscribed to the GraphemeCluster in holding. */
    static final int STATE_STACKING = 3;

    /** state in which one or more consonants have been seen and also
     *  an achung (U+0F71) has been seen */
    static final int STATE_STACKPLUSACHUNG = 4;

    /** state that seeing U+0F8A (when that's not an error) puts you
     *  in.  Needed because U+0F8A is always followed by U+0F82, and
     *  we check for the exceptional case that U+0F8A is followed by
     *  something else. */
    static final int STATE_PARTIALMARK = 5;

    /* DLC we should have many error states or none. */


    /** the present codepoint marks the start of a new
     *  GraphemeCluster */
    static final int ACTION_BEGINS_NEW_GRAPHEME_CLUSTER = 0;
    /** the present codepoint is a continuation of the current
     *  GraphemeCluster */
    static final int ACTION_CONTINUES_GRAPHEME_CLUSTER = 1;
    /** there is an error in the input stream, which we are correcting
     *  (as we are in error-correcting mode) by starting a new
     *  GraphemeCluster with U+0F68 as the first codepoint and the
     *  current codepoint as the second */
    static final int ACTION_PREPEND_WITH_0F68 = 2;
}
