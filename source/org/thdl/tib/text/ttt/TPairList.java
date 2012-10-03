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
import java.util.HashMap;

import org.thdl.tib.text.TGCPair;
import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.util.ThdlDebug;

/** A list of {@link TPair TPairs}, typically corresponding to
 *  one tsheg bar.  <i>l</i>' in the design doc is a TPairList.
 *
 *  @author David Chandler */
class TPairList {
    /** the part that knows ACIP from EWTS */
    private TTraits traits;

    /** FIXME: change me and see if performance improves. */
    private static final int INITIAL_SIZE = 1;

    /** a fast, non-thread-safe, random-access list implementation: */
    private ArrayList al;

    /** Creates a new list containing just p. */
    public TPairList(TPair p) {
        this.traits = p.getTraits();
        al = new ArrayList(1);
        add(p);
    }

    /** Creates an empty list. */
    public TPairList(TTraits traits) {
        this.traits = traits;
        al = new ArrayList(INITIAL_SIZE);
    }

    /** Creates an empty list with the capacity to hold N items. */
    public TPairList(TTraits traits, int N) {
        this.traits = traits;
        al = new ArrayList(N);
    }

    /** Returns the ith pair in this list. */
    public TPair get(int i) { return (TPair)al.get(i); }

    /** Returns the ith non-disambiguator pair in this list. This is
     *  O(size()). */
    public TPair getNthNonDisambiguatorPair(int n) {
        TPair p;
        int count = 0;
        for (int i = 0; i < size(); i++) {
            p = get(i);
            if (!p.isDisambiguator())
                if (count++ == n)
                    return p;
        }
        throw new IllegalArgumentException("n, " + n + " is too big for this list of pairs, " + toString());
    }

    /** Returns the number of pairs in this list that are not entirely
     *  disambiguators. */
    public int sizeMinusDisambiguators() {
        int count = 0;
        for (int i = 0; i < size(); i++) {
            if (!get(i).isDisambiguator())
                ++count;
        }
        return count;
    }

    /** Adds p to the end of this list. */
    public void add(TPair p) {
        if (p == null || (p.getLeft() == null && p.getRight() == null))
            throw new IllegalArgumentException("p is weird");
        al.add(p);
    }

    /** Prepends p to the current list of TPairs. */
    public void prepend(TPair p) {
        al.add(0, p);
    }

    /** Appends p to the current list of TPairs. */
    public void append(TPair p) {
        al.add(p);
    }

    /** Returns the number of TPairs in this list. */
    public int size() { return al.size(); }

    /** Returns a human-readable representation.
     *  @return something like [(R . ), (D . O)] */
    public String toString2() {
        return al.toString();
    }

    /** Returns a human-readable representation like {G}{YA} or
     *  {G-}{YA}. */
    public String toString() {
        int sz = size();
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < sz; i++) {
            b.append('{');
            if (null != get(i).getLeft())
                b.append(get(i).getLeft());
            if (null != get(i).getRight())
                b.append(get(i).getRight());
            b.append('}');
        }
        return b.toString();
    }

    /** Returns the transliteration corresponding to this TPairList.
     *  It will be as ambiguous as the input.  It may have more
     *  disambiguators than the original, such as in the case of the
     *  ACIP {1234}. */
    String recoverTranslit() {
        StringBuffer original = new StringBuffer();
        int sz = size();
        for (int i = 0; i < sz; i++) {
            TPair p = get(i);
            if (p.getLeft() != null)
                original.append(p.getLeft());
            if (p.getRight() != null)
                original.append(p.getRight());
        }
        return original.toString();
    }

    /** Returns true if this list contains an obvious error.  For
     *  example, with ACIP this returns true if ( . <vowel>) or (A . ) 
     *  appears, which are two simple errors you encounter if you
     *  interpret (ACIP) DAA or TAA or DAI or DAE the wrong way. */
    boolean hasSimpleError() {
        int sz = size();
        for (int i = 0; i < sz; i++) {
            TPair p = get(i);
            if (traits.hasSimpleError(p))
                return true;
        }
        return false;
    }

    /** Finds errors so simple that they can be detected without using
     *  the rules of Tibetan spelling (i.e., tsheg bar syntax).
     *  Returns an error message, or null if there is no error that
     *  you can find without the help of tsheg bar syntax rules. */
    // FIXME: This is needlessly ACIP specific -- rename and change text of messages
    String getACIPError(String originalACIP, boolean shortMessages) { // TODO(DLC)[EWTS->Tibetan] misnomer.
        // FIXME: this returns just the first error.  List all errors
        // at once.
        int sz = size();
        if (0 == sz) // FIXME: see if you can make this happen...
            return ErrorsAndWarnings.getMessage(122, shortMessages,
                                                ((null != originalACIP)
                                                 ? originalACIP
                                                 : ""),
                                                traits);
        String translit
            = (null != originalACIP) ? originalACIP : recoverTranslit();
        boolean mustBeEntirelyNumeric = get(0).isNumeric();
        for (int i = 0; i < sz; i++) {
            TPair p = get(i);
            if (mustBeEntirelyNumeric != p.isNumeric())
                return ErrorsAndWarnings.getMessage(123, shortMessages, translit, traits);

            if (traits.isACIP()
                && ((i == 0 && "V".equals(p.getLeft()))
                    || (i > 0 && "V".equals(p.getLeft())
                        && (null != get(i - 1).getRight()
                            && !"+".equals(get(i - 1).getRight()))))) {
                return ErrorsAndWarnings.getMessage(124, shortMessages, translit, traits);
            } else if (traits.aVowel().equals(p.getLeft())
                       && (null == p.getRight()
                           || "".equals(p.getRight()))) {
                return ErrorsAndWarnings.getMessage(125, shortMessages, translit, traits);
            } else if ((null == p.getLeft()
                        && (!traits.disambiguator().equals(p.getRight())
                        	&& (!traits.vowelAloneImpliesAChen()
                        		|| !traits.aVowel().equals(p.getRight()))))
                       || (null != p.getLeft()
                           && (!traits.isConsonant(p.getLeft()) && (!traits.vowelAloneImpliesAChen() || !traits.aVowel().equals(p.getLeft())))
                           && !p.isNumeric())) {
                // FIXME: stop handling this outside of ErrorsAndWarnings:
                if (null == p.getLeft()) {
                    if (shortMessages)
                        return "128: {" + translit + "}";
                    else
                        return "128: Cannot convert " + traits.shortTranslitName() + " {" + translit + "} because " + p.getRight() + " is a \"vowel\" without an associated consonant.";
                } else {
                    if (shortMessages)
                        return "129: {" + translit + "}";
                    else
                        return "129: Cannot convert " + traits.shortTranslitName() + " {" + translit + "} because " + p.getLeft() + " is not an " + traits.shortTranslitName() + " consonant.";
                }
            }
        }
        if ("+".equals(get(sz - 1).getRight())) {
            return ErrorsAndWarnings.getMessage(126, shortMessages, translit, traits);
        }
        // FIXME: really this is a warning, not an error:
        if (traits.disambiguator().equals(get(sz - 1).getRight())
            && !traits.stackingMustBeExplicit()) {
            return ErrorsAndWarnings.getMessage(127, shortMessages, translit, traits);
        }
        return null;
    }

    /** Returns true if and only if either x is a TPairList object
     *  representing the same TPairs in the same order or x is a
     *  String that is equals to the result of {@link #toString()}. */
    public boolean equals(Object x) {
        if (x instanceof TPairList) {
            return al.equals(((TPairList)x).al);
        } else if (x instanceof String) {
            return toString().equals(x) || toString2().equals(x);
        }
        return false;
    }

    /** Returns true if and only if this list is empty. */
    public boolean isEmpty() { return al.isEmpty(); }

    /** Returns a hashCode appropriate for use with our {@link
     *  #equals(Object)} method. */
    public int hashCode() { return al.hashCode(); }

    private static final int STOP_STACK = 0;
    private static final int KEEP_STACKING = 1;
    private static final int ALWAYS_KEEP_STACKING = 2;
    private static final int ALWAYS_STOP_STACKING = 3;

    /** Returns a set (as as ArrayList) of all possible TStackLists.
     *  Uses knowledge of Tibetan spelling rules (i.e., tsheg bar
     *  syntax) to do so.  If this list of pairs has something clearly
     *  illegal in it, or is empty, or is merely a list of
     *  disambiguators etc., then this returns null.  Never returns an
     *  empty parse tree.
     */
    public TParseTree getParseTree() {
        // TODO(DLC)[EWTS->Tibetan]: EWTS NOTE: this is still useful for EWTS: In EWTS, bkra
        // is b.k+ra, smra is s+m+ra, and tshmra is invalid.

        // We treat [(B . ), (G . +), (K . ), (T . A)] as if it could
        // be {B+G+K+T} or {B}{G+K+T}; we handle prefixes specially
        // this way.  [(T . ), (G . +), (K . ), (T . A)] is clearly
        // {T+G+K+TA}
        //
        // We don't care if T+G+K+T is in TMW or not -- there is no
        // master list of stacks.

        int sz = size();
        for (int i = 0; i < sz; i++)
            if (traits.isClearlyIllegal(get(i)))
                return null;

        if (sz < 1) return null;

        // When we see a stretch of ACIP (TODO(DLC)[EWTS->Tibetan]:
        // this works for EWTS, but differently) without a
        // disambiguator or a vowel, that stretch is taken to be one
        // stack unless it may be prefix-root or suffix-postsuffix or
        // suffix/postsuffix-' -- the latter necessary because GAMS'I
        // is GAM-S-'I, not GAM-S+'I.  'UR, 'US, 'ANG, 'AM, 'I, 'O, 'U
        // -- all begin with '.  So we can have zero, one, two, or
        // three special break locations.  (The kind that aren't
        // special are the break after G in G-DAMS, or the break after
        // G in GADAMS or GEDAMS.)
        //
        // If a nonnegative number appears in breakLocations[i], it
        // means that pair i may or may not be stacked with pair i+1.
        int nextBreakLoc = 0;
        int breakLocations[] = { -1, -1, -1 };

        boolean mayHavePrefix = get(0).isPrefix();

        // Handle the first pair specially -- it could be a prefix.
        if (ddebug) System.out.println("i is " + 0);
        if (mayHavePrefix
            && !traits.stackingMustBeExplicit()
            && sz > 1
            && null == get(0).getRight()) {
            // special case: we must have a branch in the parse tree
            // for the initial part of this pair list.  For example,
            // is DKHYA D+KH+YA or D-KH+YA?  It depends on prefix
            // rules (can KH+YA take a DA prefix?), so the parse tree
            // includes both.
            breakLocations[nextBreakLoc++] = 0;
        }

        // stack numbers start at 1.
        int stackNumber = (get(0).endsStack()) ? 2 : 1;
        // this starts at 0.
        int stackStart = (get(0).endsStack()) ? 1 : 0;

        int numeric = get(0).isNumeric() ? 1 : (get(0).isDisambiguator() ? 0 : -1);

        for (int i = 1; i < sz; i++) {
            if (ddebug) System.out.println("i is " + i);
            TPair p = get(i);

            // GA-YOGS should be treated like GAYOGS or G-YOGS:
            if (p.isDisambiguator()) continue;

            boolean nn;
            if ((nn = p.isNumeric()) && ("+".equals(get(i-1).getRight())
                                         || "+".equals(p.getRight())))
                return null; // clearly illegal.  You can't stack numbers.
            if (nn) {
                if (-1 == numeric)
                    return null; // you can't mix numbers and letters.
                else if (0 == numeric)
                    numeric = 1;
            } else if (!p.isDisambiguator()) {
                if (numeric == 1)
                    return null; // you can't mix numbers and letters.
                else if (0 == numeric)
                    numeric = -1;
            }

            if (i+1==sz || p.endsStack()) {
                if (/* the stack ending here might really be
                       suffix-postsuffix or
                       suffix-appendage or
                       suffix-postsuffix-appendage */
                    (mayHavePrefix && (stackNumber == 2 || stackNumber == 3))
                    || (!mayHavePrefix && (stackNumber == 2))) {
                    if (i > stackStart) {
                        if (get(stackStart).isSuffix()
                            && (get(stackStart+1).isPostSuffix() // suffix-postsuffix
                                || "'".equals(get(stackStart+1).getLeft()))) { // suffix-appendage
                            breakLocations[nextBreakLoc++] = stackStart;
                        }
                        if (i > stackStart + 1) {
                            // three to play with, maybe it's
                            // suffix-postsuffix-appendage.
                            if (get(stackStart).isSuffix()
                                && get(stackStart+1).isPostSuffix()
                                && "'".equals(get(stackStart+2).getLeft())) {
                                breakLocations[nextBreakLoc++] = stackStart+1;
                            }
                        }
                    }
                    // else no need to insert a breakLocation, we're
                    // breaking hard.
                }
                if (/* the stack ending here might really be
                       postsuffix-appendage (e.g., GDAM-S'O) */
                    (mayHavePrefix && (stackNumber == 3 || stackNumber == 4))
                    || (!mayHavePrefix && (stackNumber == 3))) {
                    if (i == stackStart+1) { // because GDAM--S'O is illegal, and because it's 'ANG, not 'NG, 'AM, not 'M -- ' always ends the stack
                        if (get(stackStart).isPostSuffix()
                            && "'".equals(get(stackStart+1).getLeft())) {
                            breakLocations[nextBreakLoc++] = stackStart;
                        }
                    }
                }
                ++stackNumber;
                stackStart = i+1;
            }
        }
        // FIXME: we no longer need all these breakLocations -- we can handle SAM'AM'ANG without them.

        // Now go from hard break (i.e., (* . VOWEL or -)) to hard
        // break (and there's a hard break after the last pair, of
        // course, even if it is (G . ) or (G . +) [the latter being
        // hideously illegal]).  Between the hard breaks, there will
        // be 1, 2, or 4 (can you see why 8 isn't possible, though
        // numBreaks can be 3?) possible parses.  There are two of DGA
        // in DGAMS'O -- D-GA and D+GA.  There are 4 of MS'O in
        // DGAMS'O -- M-S-'O, M-S+'O, M+S-'O, and M+S+'O.  Add one
        // TStackListList per hard break to pt, the parse tree.
        int startLoc = 0; // which pair starts this hard break?

        // FIXME: assert this
        if ((breakLocations[1] >= 0 && breakLocations[1] <= breakLocations[0])
            || (breakLocations[2] >= 0 && breakLocations[2] <= breakLocations[1]))
            throw new Error("breakLocations is monotonically increasing, ain't it?");
        TParseTree pt = new TParseTree();
        for (int i = 0; i < sz; i++) {
            if (ddebug) System.out.println("getParseTree: second loop i is " + i);
            if (i+1 == sz || get(i).endsStack()) {
                TStackListList sll = new TStackListList(4); // maximum is 4.

                int numBreaks = 0;
                int breakStart = -1;
                for (int jj = 0; jj < breakLocations.length; jj++) {
                    if (breakLocations[jj] >= startLoc
                        && breakLocations[jj] <= i) {
                        if (breakStart < 0)
                            breakStart = jj;
                        ++numBreaks;
                    }
                }

                // Count from [0, 1<<numBreaks).  At each point,
                // counter equals b2b1b0 in binary.  1<<numBreaks is
                // the number of stack lists there are in this stack
                // list list of the parse tree.  Break at location
                // breakLocations[breakStart+0] if and only if b0 is
                // one, at location breakLocations[breakStart+1] if
                // and only if b1 is one, etc.
                for (int counter = 0; counter < (1<<numBreaks); counter++) {
                    if (ddebug) System.out.println("getParseTree: counter is " + counter);
                    TStackList sl = new TStackList();
                    boolean slIsInvalid = false;
                    TPairList currentStack = new TPairList(traits);
                    TPairList currentStackUnmodified = new TPairList(traits);
                    for (int k = startLoc; k <= i; k++) {
                        if (!get(k).isDisambiguator()) {
                            if (get(k).isNumeric()
                                || (get(k).getLeft() != null
                                    && (traits.isConsonant(get(k).getLeft())
                                        || traits.vowelAloneImpliesAChen() && traits.aVowel().equals(get(k).getLeft())))) {
                                currentStack.add(get(k).insideStack());
                                currentStackUnmodified.add(get(k));
                            } else {
                                return null; // sA, for example, is illegal.
                            }
                        }
                        if (k == i || get(k).endsStack()) {
                            if (!currentStack.isEmpty()) {
                                if (traits.couldBeValidStack(currentStackUnmodified)) {
                                    sl.add(currentStack.asStack());
                                } else {
                                    slIsInvalid = true;
                                    break;
                                }
                            }
                            currentStack = new TPairList(traits);
                            currentStackUnmodified = new TPairList(traits);
                        } else {
                            if (numBreaks > 0) {
                                for (int j = 0; breakStart+j < 3; j++) {
                                    if (k == breakLocations[breakStart+j]
                                        && 1 == ((counter >> j) & 1)) {
                                        if (!currentStack.isEmpty()) {
                                            if (traits.couldBeValidStack(currentStackUnmodified)) {
                                                sl.add(currentStack.asStack());
                                            } else {
                                                slIsInvalid = true;
                                                break;
                                            }
                                        }
                                        currentStack = new TPairList(traits);
                                        currentStackUnmodified = new TPairList(traits);
                                        break; // shouldn't matter, but you never know
                                    }
                                }
                            }
                        }
                    }
                    if (!slIsInvalid && !sl.isEmpty()) {
                        sll.add(sl);
                    }
                }

                if (!sll.isEmpty())
                    pt.add(sll);
                startLoc = i+1;
            }
        }


        if (ddebug) System.out.println("getParseTree: parse tree for " + toString() + " is " + pt);
        if (pt.isEmpty()) return null;
        return pt;
    }

    private static final boolean ddebug = false;

    /** Mutates this TPairList object such that the last pair is empty
     *  or is a vowel, but is never the stacking operator ('+') or (in
     *  ACIP, but not in EWTS) a disambiguator (i.e., an ACIP '-' or
     *  EWTS '.' on the right).
     *  @return this instance */
    private TPairList asStack() {
        if (!isEmpty()) {
            TPair lastPair = get(size() - 1);
            if ("+".equals(lastPair.getRight())) {
                al.set(size() - 1, new TPair(traits, lastPair.getLeft(), null));
            } else if (traits.disambiguator().equals(lastPair.getRight())
                       && !traits.stackingMustBeExplicit()) {
                al.set(size() - 1, new TPair(traits, lastPair.getLeft(), null));
            }
        }
        return this;
    }

    /** Adds the TGCPairs corresponding to this list to the end of pl.
     *  Some TPairs correspond to more than one TGCPair ({AA:}); some
     *  TGCPairs correspond to more than one TPair ({G+YA}).  To keep
     *  track, indexList will be appended to in lockstep with pl.
     *  index (wrapped as an {@link java.lang#Integer}) will be
     *  appended to indexList once each time we append to pl.  This
     *  assumes that this TPairList corresponds to exactly one Tibetan
     *  grapheme cluster (i.e., stack).  Note that U+0F7F, U+0F35, and
     *  U+0F37 get special treatment because the sole client of this
     *  code is TTGCList, and its sole client is to test for legality
     *  of a tsheg bar. */
    void populateWithTGCPairs(ArrayList pl,
                              ArrayList indexList, int index) {
        int sz = size();
        if (sz == 0) {
            return;
        } else {
            boolean isNumeric = false;
            StringBuffer lWylie = new StringBuffer();
            int i;
            // All pairs but the last:
            for (i = 0; i + 1 < sz; i++) {
                lWylie.append(get(i).getWylie());
                if (get(i).isNumeric())
                    isNumeric = true;
            }

            // The last pair:
            TPair p = get(i);
            ThdlDebug.verify(!"+".equals(p.getRight()));
            final String specialCases[] = new String[] {
                traits.U0F7F(),
                traits.U0F35(),
                traits.U0F37()
            };
            final String specialCaseEwts[] = new String[] {
                EWTSTraits.instance().U0F7F(),
                EWTSTraits.instance().U0F35(),
                EWTSTraits.instance().U0F37()
            };
            final boolean ignoreSpecialCase[] = new boolean[] {
                false,  // Don't ignore this -- it's Sanskrit.
                        // ['jamH] should be illegal EWTS.
                        // (TODO(dchandler): ask)
                true,
                true,
            };
            boolean hasSpecialCase[] = new boolean[] { false, false, false, };
            for (int j = 0; j < specialCases.length; j++) {
                if (null != specialCases[j]) {
                    int where;
                    if (p.getRight() != null
                        && (where = p.getRight().indexOf(specialCases[j])) >= 0) {
                        // this guy is his own TGCPair.
                        hasSpecialCase[j] = true;
                        StringBuffer rr = new StringBuffer(p.getRight());
                        rr.replace(where, where + specialCases[j].length(), "");
                        if (rr.length() > where && '+' == rr.charAt(where)) {
                            rr.deleteCharAt(where);
                        } else if (where > 0 && rr.length() > where - 1
                                   && '+' == rr.charAt(where - 1)) {
                            rr.deleteCharAt(where - 1);
                        }
                        p = new TPair(traits, p.getLeft(), rr.toString());
                    }
                }
            }
            boolean hasNonAVowel
                = (!traits.aVowel().equals(p.getRight())
                   && null != p.getRight()
                   && !traits.disambiguator().equals(p.getRight()));  // [g.yogs] needs this, e.g.
            String thislWylie = traits.getEwtsForConsonant(p.getLeft());
            if (thislWylie == null) {
                char ch;
                if (p.isNumeric()) {
                    thislWylie = p.getLeft();
                    isNumeric = true;
                }
            }

            if (null == thislWylie)
                throw new Error("BADNESS AT MAXIMUM: p is " + p + " and thislWylie is " + thislWylie);
            lWylie.append(thislWylie);
            StringBuffer ll = new StringBuffer(lWylie.toString());
            int ww;
            while ((ww = ll.indexOf("+")) >= 0)
                ll.deleteCharAt(ww);
            boolean isTibetan = TibetanMachineWeb.isWylieTibetanConsonantOrConsonantStack(ll.toString());
            boolean isSanskrit = TibetanMachineWeb.isWylieSanskritConsonantStack(lWylie.toString());
            if (ddebug && !isTibetan && !isSanskrit && !isNumeric) {
                System.out.println("OTHER for " + lWylie + " with vowel " + traits.getEwtsForWowel(p.getRight()) + " and p.getRight()=" + p.getRight());
            }
            if (isTibetan && isSanskrit) {
                 // RVA, e.g.  It must be Tibetan because RWA is what
                 // you'd use for RA over fixed-form WA.
                isSanskrit = false;
            }
            if (ddebug && hasNonAVowel && traits.getEwtsForWowel(p.getRight()) == null) {
                System.out.println("vowel " + traits.getEwtsForWowel(p.getRight()) + " and p.getRight()=" + p.getRight());
            }
            TGCPair tp;
            indexList.add(new Integer(index));
            tp = new TGCPair(lWylie.toString(),
                             (hasNonAVowel
                              ? traits.getEwtsForWowel(p.getRight())
                              : ""),
                             (isNumeric
                              ? TGCPair.TYPE_OTHER
                              : (isSanskrit
                                 ? TGCPair.TYPE_SANSKRIT
                                 : (isTibetan
                                    ? TGCPair.TYPE_TIBETAN
                                    : TGCPair.TYPE_OTHER))));
            pl.add(tp);
            for (int j = 0; j < specialCases.length; j++) {
                if (hasSpecialCase[j] && !ignoreSpecialCase[j]) {
                    indexList.add(new Integer(index));
                    pl.add(new TGCPair(specialCaseEwts[j],
                                       null, TGCPair.TYPE_OTHER));
                }
            }
        }
    }

    private static HashMap unicodeExceptionsMap = null;

    /** Appends legal Unicode corresponding to this stack to sb.
     *  FIXME: which normalization form, if any? */
    void getUnicode(StringBuffer sb) {
        // The question is this: U+0FB1 or U+0FBB?  U+0FB2 or U+0FBC?
        // The answer: always the usual form, not the full form,
        // except for a few known stacks (all the ones with full-form,
        // non-WA subjoined consonants in TMW: [in EWTS, they are:]
        // r+Y, N+D+Y, N+D+R+y, k+Sh+R).  Note that wa-zur, U+0FAD, is
        // never confused for U+0FBA because "V" and "W" are different
        // transliterations.  EWTS {r+W} thus needs no special
        // treatment during ACIP->Unicode.

        StringBuffer nonVowelSB = new StringBuffer();
        int beginningIndex = sb.length();
        boolean subscribed = false;
        int szz = size();
        int i;
        for (i = 0; i + ((1 == szz) ? 0 : 1) < szz; i++) {
            TPair p = get(i);

            // FIXME: change this to an assertion:
            if ((1 != szz) && null != p.getRight() && !"+".equals(p.getRight()))
                throw new Error("Oops -- this stack (i.e., " + toString() + ") is funny, so we can't generate proper Unicode for it.  i is " + i + " and size is " + szz);

            p.getUnicode(nonVowelSB, subscribed);
            subscribed = true;
        }
        if (szz > 1) {
            TPair p = get(i);
            StringBuffer vowelSB = new StringBuffer();
            p.getUnicode(nonVowelSB, vowelSB, subscribed /* which is true */);

            if (null == unicodeExceptionsMap) {
                unicodeExceptionsMap = new HashMap();
                unicodeExceptionsMap.put("\u0f69\u0fb2", "\u0f69\u0fbc"); // KshR (variety 1)
                unicodeExceptionsMap.put("\u0f40\u0fb5\u0fb2", "\u0f40\u0fb5\u0fbc"); // KshR (variety 2)
                unicodeExceptionsMap.put("\u0f4e\u0f9c\u0fb2\u0fb1", "\u0f4e\u0f9c\u0fbc\u0fb1"); // ndRY
                unicodeExceptionsMap.put("\u0f4e\u0f9c\u0fb1", "\u0f4e\u0f9c\u0fbb"); // ndY
                unicodeExceptionsMap.put("\u0f61\u0fb1", "\u0f61\u0fbb"); // YY
                unicodeExceptionsMap.put("\u0f62\u0fb1", "\u0f6a\u0fbb"); // RY
                unicodeExceptionsMap.put("\u0f62\u0fba", "\u0f6a\u0fba"); // RW
                unicodeExceptionsMap.put("\u0f62\u0fb4", "\u0f6a\u0fb4"); // RSHA
                unicodeExceptionsMap.put("\u0f62\u0fb4\u0fb1", "\u0f6a\u0fb4\u0fb1"); // RSHYA
                unicodeExceptionsMap.put("\u0f62\u0fb5", "\u0f6a\u0fb5"); // Rsh
                unicodeExceptionsMap.put("\u0f62\u0fb5\u0f9e", "\u0f6a\u0fb5\u0f9e"); // Rshn
                unicodeExceptionsMap.put("\u0f62\u0fb5\u0f9e\u0fb1", "\u0f6a\u0fb5\u0f9e\u0fb1"); // RshnY
                unicodeExceptionsMap.put("\u0f62\u0fb5\u0fa8", "\u0f6a\u0fb5\u0fa8"); // RshM
                unicodeExceptionsMap.put("\u0f62\u0fb5\u0fb1", "\u0f6a\u0fb5\u0fb1"); // RshY
                unicodeExceptionsMap.put("\u0f62\u0fb6", "\u0f6a\u0fb6"); // RS
            }
            String mapEntry = (String)unicodeExceptionsMap.get(nonVowelSB.toString());
            if (traits.isACIP() && null != mapEntry)
                sb.append(mapEntry);
            else
                sb.append(nonVowelSB);
            sb.append(vowelSB);
        } else {
            sb.append(nonVowelSB);
        }
    }

    /** Appends the DuffCodes that correspond to this grapheme cluster
     *  to duffsAndErrors, or appends a String that is an error or
     *  warning message (a short one iff shortMessages is true) saying
     *  that TMW cannot represent this grapheme cluster.  The message
     *  is Error 137 if noCorrespondingTMWGlyphIsError is true;
     *  otherwise, it's Warning 511. */
    void getDuff(ArrayList duffsAndErrors,
                 boolean shortMessages,
                 boolean noCorrespondingTMWGlyphIsError) {
        int previousSize = duffsAndErrors.size();
        StringBuffer wylieForConsonant = new StringBuffer();
        for (int x = 0; x + 1 < size(); x++) {
            wylieForConsonant.append(get(x).getWylie(false, true));
        }
        TPair lastPair = get(size() - 1);
        wylieForConsonant.append(lastPair.getWylie(true, false));
        String hashKey = wylieForConsonant.toString();

        if (traits.isACIP()) {
            // Because EWTS has special handling for full-formed
            // subjoined consonants, we have special handling here.
            if ("r+y".equals(hashKey))
                hashKey = "r+Y";
            else if ("y+y".equals(hashKey))
                hashKey = "y+Y";
            else if ("N+D+y".equals(hashKey))
                hashKey = "N+D+Y";
            else if ("N+D+r+y".equals(hashKey))
                hashKey = "N+D+R+y";
            else if ("k+Sh+r".equals(hashKey))
                hashKey = "k+Sh+R";
        
            // TPair.getWylie(..) returns "W" sometimes when "w" is what
            // really should be returned.  ("V" always causes "w" to be
            // returned, which is fine.)  We'll change "W" to "w" here if
            // we need to.  We do it only for a few known stacks (the ones
            // in TMW).
            if ("W".equals(hashKey))
                hashKey = "w";
            else if ("W+y".equals(hashKey))
                hashKey = "w+y";
            else if ("W+r".equals(hashKey))
                hashKey = "w+r";
            else if ("W+n".equals(hashKey))
                hashKey = "w+n";
            else if ("W+W".equals(hashKey))
                hashKey = "w+W";

            if ("r+Y".equals(hashKey)
                || "r+W".equals(hashKey)
                || "r+sh".equals(hashKey)
                || "r+sh+y".equals(hashKey)
                || "r+Sh".equals(hashKey)
                || "r+Sh+N".equals(hashKey)
                || "r+Sh+N+y".equals(hashKey)
                || "r+Sh+m".equals(hashKey)
                || "r+Sh+y".equals(hashKey)
                || "r+s".equals(hashKey)
                ) {
                hashKey = "R" + hashKey.substring(1); // r+Y => R+Y, etc.
            }
        }

        if (!TibetanMachineWeb.isKnownHashKey(hashKey)) {
            hashKey = hashKey.replace('+', '-');
            if (!TibetanMachineWeb.isKnownHashKey(hashKey)) {
                duffsAndErrors.add(ErrorsAndWarnings.getMessage(noCorrespondingTMWGlyphIsError
                                                                ? 137
                                                                : 511,
                                                                shortMessages,
                                                                recoverTranslit(),
                                                                traits));
                return;
            }
        }
        if (lastPair.getRight() == null
            || lastPair.getRight().equals(traits.disambiguator())
            || lastPair.equals(traits.disambiguator())) {
            duffsAndErrors.add(TibetanMachineWeb.getGlyph(hashKey));
        } else {
            try {
                traits.getDuffForWowel(duffsAndErrors,
                                       TibetanMachineWeb.getGlyph(hashKey),
                                       lastPair.getRight());
            } catch (ConversionException e) {
                // TODO(dchandler): Error 137 isn't the perfect
                // message.  Try EWTS [RAM], e.g. to see why.  It acts
                // like we're trying to find a single glyph for (R
                // . A+M) in that case.
                duffsAndErrors.add(ErrorsAndWarnings.getMessage(noCorrespondingTMWGlyphIsError
                                                                ? 137
                                                                : 511,
                                                                shortMessages,
                                                                recoverTranslit(),
                                                                traits));
                return;
            }
        }
        if (previousSize == duffsAndErrors.size())
            throw new Error("TPairList with no duffs? " + toString() + " has hash key " + hashKey + " and previous size is " + previousSize); // FIXME: change to assertion.
    }
}

