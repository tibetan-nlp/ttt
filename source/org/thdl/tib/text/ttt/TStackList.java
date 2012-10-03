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

import org.thdl.tib.text.TGCList;
import org.thdl.tib.text.TibTextUtils;

/** A list of {@link TPairList TPairLists}, each of which is for
 *  a stack (a grapheme cluster), typically corresponding to one tsheg
 *  bar.
 *
 *  @author David Chandler */
class TStackList {
    /** FIXME: change me and see if performance improves. */
    private static final int INITIAL_SIZE = 1;

    /** a fast, non-thread-safe, random-access list implementation: */
    private ArrayList al;

    /** Creates an empty list. */
    public TStackList() { al = new ArrayList(INITIAL_SIZE); }

    /** Creates a list containing just p. */
    public TStackList(TPairList p) {
        al = new ArrayList(1);
        add(p);
    }

    /** Creates an empty list with the capacity to hold N items. */
    public TStackList(int N) {
        al = new ArrayList(N);
    }

    /** Returns the ith pair in this list. */
    public TPairList get(int i) { return (TPairList)al.get(i); }

    /** Adds p to the end of this list. */
    public void add(TPairList p) { al.add(p); }

    /** Adds all the stacks in c to the end of this list. */
    public void addAll(TStackList c) { al.addAll(c.al); }

    /** Adds all the stacks in c to this list, inserting them at
     *  position k. */
    public void addAll(int k, TStackList c) { al.addAll(k, c.al); }

    /** Returns the number of TPairLists in this list. */
    public int size() { return al.size(); }

    /** Returns true if and only if this list is empty. */
    public boolean isEmpty() { return al.isEmpty(); }

    /** Returns something akin to the transliteration that was input
     *  (okay, maybe 1-2-3-4 instead of 1234, and maybe AUTPA instead
     *  of AUT-PA [ACIP examples]) corresponding to this stack list. */
    public String recoverTranslit() {
        return toStringHelper(false);
    }

    /** Returns a human-readable representation like {G}{YA} or
     *  {GYA}. */
    public String toString() {
        return toStringHelper(true);
    }

    private String toStringHelper(boolean brackets) {
        int sz = size();
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < sz; i++) {
            if (brackets) b.append('{');
            b.append(get(i).recoverTranslit());
            if (brackets) b.append('}');
        }
        return b.toString();
    }

    /** Returns a human-readable representation.
     *  @return something like [[(R . ), (D . O)], [(R . ), (J . E)]] */
    public String toString2() {
        return al.toString();
    }

    /** Returns true if and only if either x is a TStackList
     *  object representing the same TPairLists in the same
     *  order or x is a String that is equals to the result of {@link
     *  #toString()}. */
    public boolean equals(Object x) {
        if (x instanceof TStackList) {
            return al.equals(((TStackList)x).al);
        } else if (x instanceof String) {
            return toString().equals(x) || toString2().equals(x);
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

    /** Returns a pair with {@link BoolTriple#isLegal} true if and
     *  only if this list of stacks is a legal tsheg bar by the rules
     *  of Tibetan syntax (sometimes called rules of spelling).  If
     *  this is legal, then {@link
     *  BoolTriple#isLegalAndHasAVowelOnRoot} will be true if and only
     *  if there is an explicit {A} vowel on the root stack.
     *  @param noPrefixTests true if you want to pretend that every
     *  stack can take every prefix, which is not the case in
     *  reality */
    public BoolTriple isLegalTshegBar(boolean noPrefixTests) {
        // FIXME: Should we handle PADMA and other Tibetanized Sanskrit fellows consistently?  Right now we only treat single-stack Sanskrit guys as legal.

        TTGCList tgcList = new TTGCList(this);
        StringBuffer warnings = new StringBuffer();
        String candidateType
            = TibTextUtils.getClassificationOfTshegBar(tgcList, warnings, noPrefixTests);
        if (ddebug) System.out.println("ddebug: tgclist is " + tgcList + "\n  warnings is " + warnings + "\n candidateType is " + candidateType);

        // preliminary answer:
        boolean isLegal = (candidateType != "invalid");

        if (isLegal) {
            if (isClearlyIllegal(candidateType))
                isLegal = false;
            TPairList firstStack = this.get(0);
            // NOTE: In ewts, [([b'dgm] . ) (...] is illegal unless
            // this is a legal tsheg bar featuring a prefix.  (I'm not
            // sure this is enforced here, though...)
            if (1 == firstStack.size()
                && firstStack.get(0).isPrefix()
                && null == firstStack.get(0).getRight()  // ACIP {GAM}/EWTS {gam} is legal
                && !(candidateType.startsWith("prefix")
                     || candidateType.startsWith("appendaged-prefix"))) {
                isLegal = false;
            }
        }

        boolean isLegalAndHasAVowelOnRoot = false;
        if (isLegal) {
            int rootIndices[]
                = TibTextUtils.getIndicesOfRootForCandidateType(candidateType);
            for (int i = 0; i < 2; i++) {
                if (rootIndices[i] >= 0) {
                    int pairListIndex = tgcList.getTPairListIndex(rootIndices[i]);
                    TPairList pl = get(pairListIndex);
                    TPair p = pl.get(pl.size() - 1);
                    isLegalAndHasAVowelOnRoot
                        = (p.getRight() != null
                           && p.getRight().startsWith(p.getTraits().aVowel())); // could be ACIP {A:}, e.g.
                    if (isLegalAndHasAVowelOnRoot)
                        break;
                }
            }
        }
        return new BoolTriple(isLegal,
                              isLegalAndHasAVowelOnRoot,
                              candidateType);
    }

    private static final boolean ddebug = false;

    /** Returns true if and only if this stack list contains a clearly
     *  illegal construct.  An example of such is a TPair (V . something). */
    boolean isClearlyIllegal(String candidateType) {
        if (isVeryClearlyIllegal())
            return true;
        int choices[]
            = TibTextUtils.getIndicesOfRootForCandidateType(candidateType);
        int max = size() - 1;  // TODO(DLC)[EWTS->Tibetan]:
                               // optionally, use just size().  This
                               // will make [g] and [bad+man] illegal,
                               // e.g.
        for (int i = 0; i < max; i++) {
            // We want EWTS [gga] to be illegal because ga does not
            // takes a gao prefix and we want EWTS [trna] to be
            // illegal because a disambiguator or wowel is required to
            // end a stack unless that stack is a prefix, suffix, or
            // postsuffix.
            if ((choices[0] < 0 && choices[1] < 0)
                || (choices[0] == i && choices[1] < 0)) {
                TPair last = get(i).get(get(i).size() - 1);
                if (last.getTraits().stackingMustBeExplicit()
                    && last.getRight() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isVeryClearlyIllegal() {
        // check for {D}{VA} sorts of things:
        for (int i = 0; i < size(); i++) {
            if (get(i).getACIPError("THIS MAKES IT FASTER AND IS SAFE, DON'T WORRY",
                                    true /* faster... */)
                != null) {
                if (ddebug) System.out.println("ddebug: error is " + get(i).getACIPError("THIS MAKES IT FASTER AND IS SAFE, DON'T WORRY", false));
                return true;
            }
        }
        return false;
    }

    /** Returns true if and only if this stack list contains a stack
     *  that does not end in a vowel or disambiguator.  Note that this
     *  is not erroneous for legal Tibetan like ACIP {BRTAN}, where {B} has
     *  no vowel, but it is a warning sign for Sanskrit stacks.
     *  @param isACIP true iff opl is ACIP (not EWTS)
     *  @param opl the pair list from which this stack list
     *  originated
     *  @param isLastStack if non-null, then isLastStack[0] will be
     *  set to true if and only if the very last stack is the only
     *  stack not to have a vowel or disambiguator on it */
    boolean hasStackWithoutVowel(boolean isACIP, TPairList opl, boolean[] isLastStack) {
        int runningSize = 0;
        // FIXME: MARDA is MARD==MAR-D to us, but is probably MAR+DA, warn -- see 838470
        for (int i = 0; i < size(); i++) {
            TPairList pl = get(i);
            String l;
            TPair lastPair = opl.getNthNonDisambiguatorPair(runningSize + pl.size() - 1);
            runningSize += pl.size();
            if (null == lastPair.getRight()
                && !((l = lastPair.getLeft()) != null && l.length() == 1
                     && l.charAt(0) >= '0' && l.charAt(0) <= '9')) {
                if (null != isLastStack) {
                    isLastStack[0] = (i + 1 == size());
                    if (!isLastStack[0] && isACIP) {
                        throw new Error("But we now stack greedily!");
                    }
                }
                return true;
            }
        }
        if (runningSize != opl.sizeMinusDisambiguators()) {
            throw new IllegalArgumentException("runningSize = " + runningSize + "; opl.sizeMinusDisambiguators = " + opl.sizeMinusDisambiguators() + "; opl (" + opl + ") is bad for this stack list (" + toString() + ")");
        }
        return false;
    }

    /** Returns legal Unicode corresponding to this tsheg bar.  FIXME: which normalization form, if any? */
    String getUnicode() {
        StringBuffer u = new StringBuffer(size());
        for (int i = 0; i < size(); i++) {
            get(i).getUnicode(u);
        }
        return u.toString();
    }

    /** Returns the DuffCodes and errors corresponding to this stack
        list. Each element of the array is a DuffCode or a String, the
        latter if and only if the TMW font cannot represent the
        corresponding stack in this list.  Iff shortMessages is true,
        the String elements will be shorter messages. */
    Object[] getDuff(boolean shortMessages,
                     boolean noCorrespondingTMWGlyphIsError) {
        ArrayList al = new ArrayList(size()*2); // rough estimate
        int count = 0;
        for (int i = 0; i < size(); i++) {
            get(i).getDuff(al, shortMessages, noCorrespondingTMWGlyphIsError);
        }
        if (size() > 0 && al.size() == 0) {
            throw new Error("But this stack list, " + this + ", contains " + size() + " stacks!  How can it not have DuffCodes associated with it?");
        }
        return al.toArray();
    }
}

/** A BoolTriple is used to convey the legality of a particular tsheg
 *  bar.  (FIXME: This class is misnamed.)
 *  @author David Chandler */
class BoolTriple implements Comparable {

    /** candidateType is a {@link
        org.thdl.tib.text.TibTextUtils#getClassificationOfTshegBar(TGCList,StringBuffer,boolean)}
        concept.  You cannot derive isLegal() from it because {@link
        TStackList#isClearlyIllegal()} and more (think {BNA}) comes
        into play. */
    String candidateType;


    /** True if and only if the tsheg bar is a native Tibetan tsheg
        bar or is a single Sanskrit grapheme cluster.
        @see #isLegalButSanskrit() */
    boolean isLegal;


    /** Some subset of tsheg bars are legal but legal Sanskrit -- the
        single sanskrit stacks are this way, such as B+DE.  We treat
        such a thing as legal because B+DE is the perfect way to input
        such a thing.  But then, we treat B+DEB+DE as illegal, even
        though it too is perfect.  So we're inconsistent (LOW-PRIORITY
        FIXME), but you really have to watch what happens to
        coloration and warning messages if you change this. */
    boolean isLegalButSanskrit() {
        return (candidateType == "single-sanskrit-gc");
    }

    /** True if and only if {@link #isLegal} is true and there may be
        an TTraits.aVowel() on the root stack. */
    boolean isLegalAndHasAVowelOnRoot;
    BoolTriple(boolean isLegal,
               boolean isLegalAndHasAVowelOnRoot,
               String candidateType) {
        this.isLegal = isLegal;
        this.isLegalAndHasAVowelOnRoot = isLegalAndHasAVowelOnRoot;
        this.candidateType = candidateType;
        if (!isLegal && (isLegalButSanskrit() || isLegalAndHasAVowelOnRoot))
            throw new IllegalArgumentException();
    }

    /** The more legal and standard a tsheg bar is, the higher score
        it has. */
    private int score() {
        int score = 0;
        if (isLegalAndHasAVowelOnRoot) {
            score += 5;
        }
        if (isLegal) {
            score += 5;
        }
        if (isLegalButSanskrit()) {
            score -= 3;
        }
        return score;
    }


    /** The "most legal" BoolTriple compares higher.  Native Tibetan
        beats Sanskrit; native tibetan with a vowel on the root stack
        beats native Tibetan without. */
    public int compareTo(Object o) {
        BoolTriple b = (BoolTriple)o;
        return score() - b.score();
    }

    // NOTE: TibTextUtils.getIndicesOfRootForCandidateType(candidateType)
    // is useful.
}
