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

class ValidatingUnicodeReader implements UnicodeReadingStateMachineConstants {
    /** Don't instantiate this class. */
    private ValidatingUnicodeReader() { super(); }

    /** This table tells how to transition from state to state upon
     *  encountering certain classes of Unicode codepoints.  There are
     *  6 legal states + an error state. */
    private static final TransitionInstruction
        transitionTable[/* 6 is the number of STATEs */][/* 11 is the number of CC classes */]
        = {
            // STATE_START:
            {
                /* upon seeing CC_SIN in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_MCWD in this state: */
                null,
                /* upon seeing CC_CM in this state: */
                null,
                /* upon seeing CC_SJC in this state: */
                null,
                /* upon seeing CC_CON in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_V in this state: */
                null,
                /* upon seeing CC_0F8A in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F82 in this state: */
                null,
                /* upon seeing CC_0F39 in this state: */
                null,
                /* upon seeing CC_SUBSCRIBED_ACHUNG in this state: */
                null,
                /* upon seeing CC_DIGIT in this state: */
                new TransitionInstruction(STATE_DIGIT,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER)
            },

            // STATE_READY:
            {
                /* upon seeing CC_SIN in this state: */
                new TransitionInstruction(STATE_READY, // self
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_MCWD in this state: */
                null,
                /* upon seeing CC_CM in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_SJC in this state: */
                null,
                /* upon seeing CC_CON in this state: */
                new TransitionInstruction(STATE_STACKING,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_V in this state: */
                null,
                /* upon seeing CC_0F8A in this state: */
                new TransitionInstruction(STATE_PARTIALMARK,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F82 in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F39 in this state: */
                null,
                /* upon seeing CC_SUBSCRIBED_ACHUNG in this state: */
                null, // because 0F71 comes after SJCs, before Vs, and
                      // before CMs.
                /* upon seeing CC_DIGIT in this state: */
                new TransitionInstruction(STATE_DIGIT,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER)
            },
            // STATE_DIGIT:
            {
                /* upon seeing CC_SIN in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_MCWD in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_CM in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_SJC in this state: */
                null,
                /* upon seeing CC_CON in this state: */
                new TransitionInstruction(STATE_STACKING,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_V in this state: */
                null,
                /* upon seeing CC_0F8A in this state: */
                new TransitionInstruction(STATE_PARTIALMARK,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F82 in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F39 in this state: */
                null,
                /* upon seeing CC_SUBSCRIBED_ACHUNG in this state: */
                null,
                /* upon seeing CC_DIGIT in this state: */
                new TransitionInstruction(STATE_DIGIT,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER) /* DLC although consider the meaning of 0F22,0F22,0F3F */
            },
            // STATE_STACKING:
            {
                /* upon seeing CC_SIN in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_MCWD in this state: */
                null,
                /* upon seeing CC_CM in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_SJC in this state: */
                new TransitionInstruction(STATE_STACKING,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_CON in this state: */
                new TransitionInstruction(STATE_STACKING,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_V in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F8A in this state: */
                new TransitionInstruction(STATE_PARTIALMARK,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F82 in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F39 in this state: */
                new TransitionInstruction(STATE_STACKING,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_SUBSCRIBED_ACHUNG in this state: */
                new TransitionInstruction(STATE_STACKPLUSACHUNG,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_DIGIT in this state: */
                new TransitionInstruction(STATE_DIGIT,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER)
            },
            // STATE_STACKPLUSACHUNG:
            {
                /* upon seeing CC_SIN in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_MCWD in this state: */
                null,
                /* upon seeing CC_CM in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_SJC in this state: */
                null,
                /* upon seeing CC_CON in this state: */
                new TransitionInstruction(STATE_STACKING,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_V in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F8A in this state: */
                new TransitionInstruction(STATE_PARTIALMARK,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F82 in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F39 in this state: */
                null,
                /* upon seeing CC_SUBSCRIBED_ACHUNG in this state: */
                null,
                /* upon seeing CC_DIGIT in this state: */
                new TransitionInstruction(STATE_DIGIT,
                                          ACTION_BEGINS_NEW_GRAPHEME_CLUSTER)
            },
            // STATE_PARTIALMARK:
            {
                /* upon seeing CC_SIN in this state: */
                null,
                /* upon seeing CC_MCWD in this state: */
                null,
                /* upon seeing CC_CM in this state: */
                null,
                /* upon seeing CC_SJC in this state: */
                null,
                /* upon seeing CC_CON in this state: */
                null,
                /* upon seeing CC_V in this state: */
                null,
                /* upon seeing CC_0F8A in this state: */
                null,
                /* upon seeing CC_0F82 in this state: */
                new TransitionInstruction(STATE_READY,
                                          ACTION_CONTINUES_GRAPHEME_CLUSTER),
                /* upon seeing CC_0F39 in this state: */
                null,
                /* upon seeing CC_SUBSCRIBED_ACHUNG in this state: */
                null,
                /* upon seeing CC_DIGIT in this state: */
                null
            }
        };

    /* DLC NOW FIXME -- clearly, we need LegalTshegBar to be convertable to and from UnicodeGraphemeClusters; */

    /** Breaks a sequence of UnicodeGraphemeClusters into LegalTshegBars.
        @param grcls a sequence of nonnull UnicodeGraphemeClusters
        @return a sequence of nonnull LegalTshegBars
        @exception TibetanSyntaxException if grcls does not consist
        entirely of legal Tibetan syllables
        @see UnicodeGraphemeCluster
        @see LegalTshegBar
    */
    private static Vector breakGraphemeClustersIntoOnlyTshegBars(Vector grcls)
        throws TibetanSyntaxException
    {
        return breakGraphemeClustersIntoTshegBarsAndGraphemeClusters(grcls,
                                                                     true);
    }

    private static Vector breakLegalGraphemeClustersIntoOnlyTshegBars(Vector grcls) {
        try {
            return breakGraphemeClustersIntoTshegBarsAndGraphemeClusters(grcls,
                                                                         false);
        } catch (TibetanSyntaxException ex) {
            throw new Error("This can never happen, because the second parameter, validating, was false.");
        }
    }

    /** 
     @param grcls a Vector consisting entirely of UnicodeGraphemeClusters
     @param validate true iff you wish to have a
     TibetanSyntaxException thrown upon encountering a sequence of
     UnicodeGraphemeClusters that is syntactically incorrect Tibetan
     @return if validate is true, a Vector consisting entirely of
     LegalTshegBars, else a vector of LegalTshegBars and
     UnicodeGraphemeClusters */
    private static Vector breakGraphemeClustersIntoTshegBarsAndGraphemeClusters(Vector grcls,
                                                                                boolean validate)
        throws TibetanSyntaxException
    {
        Vector syllables = new Vector();
        int grcls_len = grcls.size();
        int beginning_of_cluster = 0;
        for (int i = 0; i < grcls_len; i++) {
            UnicodeGraphemeCluster current_grcl
                = (UnicodeGraphemeCluster)grcls.elementAt(i);
            if (current_grcl.isTshegLike()) {
                if (beginning_of_cluster < i) {
                    // One or more non-tsheg-like grapheme clusters is
                    // here between tsheg-like grapheme clusters.  Is
                    // it a legal syllable?
                    if (LegalTshegBar.formsLegalTshegBar(grcls,
                                                         beginning_of_cluster,
                                                         i))
                        {
                            syllables.add(new LegalTshegBar(grcls,
                                                            beginning_of_cluster,
                                                            i, tsheg=current_grcl));
                        }
                    else
                        {
                            if (validating) {
                                TibetanSyntaxException ex
                                    = new TibetanSyntaxException(grcls,
                                                                 beginning_of_cluster,
                                                                 i);
                                // DLC: return an int -1 for "all good" or
                                // 3 for "the fourth element is the first
                                // bad one" but then you don't know that
                                // 3-6 were the bad ones
                                throw ex;
                            } else {
                                for (int j = beginning_of_cluster; j <= i; j++) {
                                    syllables.add(grcls.elementAt(j));
                                }
                            }
                        }
                }
                beginning_of_cluster = i + 1;
            } // else add current_grcl to the waiting list, in a sense
        }
        return syllables;
    }

    /** Breaks a string of perfectly-formed Unicode into
        UnicodeGraphemeClusters.
        @param nfthdl_unicode a String of NFTHDL-normalized Unicode
        codepoints
        @exception Exception if the input is not perfectly formed
        @return a vector of UnicodeGraphemeClusters
        @see UnicodeGraphemeCluster
    */
    private static Vector nonErrorCorrectingReader(String nfthdl_unicode)
        throws Exception
    {
        // a vector of UnicodeGraphemeClusters that we build up little by
        // little:
        Vector grcls = new Vector();
        int currentState = STATE_START;
        StringBuffer holdingPen = new StringBuffer();

        int ilen = nfthdl_unicode.length();
        for (int i = 0; i < ilen; i++) {
            char current_cp = nfthdl_unicode.charAt(i);
            int cc_of_current_cp = getCCForCP(current_cp);
            final TransitionInstruction ti
                = transitionTable[currentState][cc_of_current_cp];
            if (null == ti) {
                throw new Exception("Bad Unicode.  DLC improve these messages");
            } else {
                switch (ti.getAction()) {
                case ACTION_BEGINS_NEW_GRAPHEME_CLUSTER:
                    grcls.add(new UnicodeGraphemeCluster(holdingPen));
                    holdingPen = new StringBuffer();
                    break;
                case ACTION_CONTINUES_GRAPHEME_CLUSTER:
                    holdingString.append(current_cp);
                    break;
                case ACTION_PREPEND_WITH_0F68:
                    throw new Error("This never happens inside the validating scanner.");
                default:
                    throw new Error("Famous last words: This won't happen.");
                }
                currentState = ti.getNextState();
            }
        }
        return grcls;
    }
}
