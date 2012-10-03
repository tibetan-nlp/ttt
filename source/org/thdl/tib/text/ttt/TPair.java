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

import org.thdl.util.ThdlDebug;

/** An ordered pair used in ACIP/EWTS-to-TMW/Unicode conversion.  The
 *  left side is the consonant or empty; the right side is either the
 *  vowel or '+' (indicating stacking in both ACIP and EWTS) or a
 *  disambiguator (e.g., '-' in ACIP or '.' in EWTS).
 *  @author David Chandler */
/* BIG FIXME: make this package work for EWTS, not just ACIP.  (TODO(DLC)[EWTS->Tibetan]: does it?) */
class TPair {
    /** the part that knows ACIP from EWTS */
    private TTraits traits;

    /** Returns the part that knows ACIP from EWTS. */
    public TTraits getTraits() { return traits; }

    /** The left side, or null if there is no left side.  I.e., the
     *  non-wowel, non-disambiguator, non-'+' guy. */
    private String l;
    String getLeft() {
        ThdlDebug.verify(!"".equals(l));
        return l;
    }

    /** The right side. That is, the wowel or disambiguator or "+"
     *  (for stacking) or null otherwise. */
    private String r;
    String getRight() {
        ThdlDebug.verify(!"".equals(r));
        return r;
    }

    /** Constructs a new TPair with left side l and right side r.
     *  Use null or the empty string to represent an absence. */
    TPair(TTraits traits, String l, String r) {
        // Normalize:
        if (null != l && l.equals("")) l = null;
        if (null != r && r.equals("")) r = null;

        this.l = l;
        this.r = r;
        this.traits = traits;
    }

    /** Returns a nice String representation.  Returns "(D . E)" for
     *  ACIP {DE}, e.g., and (l . r) in general. */
    public String toString() {
        return "("
            + ((null == l) ? "" : l) + " . "
            + ((null == r) ? "" : r) + ")";
    }

    /** Returns the number of transliteration characters that make up
     *  this TPair. */
    int size() {
        return (((l == null) ? 0 : l.length())
                + ((r == null) ? 0 : r.length()));
    }

    /** Returns a TPair that is like this one except that it is
     *  missing N characters.  The characters are taken from r, the
     *  right side, first and from l, the left side, second.  The pair
     *  returned may be illegal, such as the (A . ') you can get from
     *  ACIP {A'AAMA}.
     *  @throws IllegalArgumentException if N is out of range */
    TPair minusNRightmostTransliterationCharacters(int N)
        throws IllegalArgumentException
    {
        int sz;
        String newL = l, newR = r;
        if (N > size())
            throw new IllegalArgumentException("Don't have that many to remove.");
        if (N < 1)
            throw new IllegalArgumentException("You shouldn't call this if you don't want to remove any.");
        if (null != r && (sz = r.length()) > 0) {
            int min = Math.min(sz, N);
            newR = r.substring(0, sz - min);
            N -= min;
        }
        if (N > 0) {
            sz = l.length();
            newL = l.substring(0, sz - N);
        }
        return new TPair(traits, newL, newR);
    }

    /** Returns true if and only if this is nonempty and if l, if
     *  present, is a legal consonant, and if r, if present, is a
     *  legal wowel. */
    boolean isLegal() {
        if (size() < 1)
            return false;
        if (null != l && !traits.isConsonant(l))
            return false;
        if (null != r && !traits.isWowel(r))
            return false;
        return true;
    }

    /** Returns true if and only if this pair could be a Tibetan
     *  prefix. */
    boolean isPrefix() {
        return (null != l
                && ((null == r || "".equals(r))
                    || traits.disambiguator().equals(r)
                    || traits.aVowel().equals(r)) // FIXME: though check for BASKYABS and warn because BSKYABS is more common
                && traits.isPrefix(l));
    }

    /** Returns true if and only if this pair could be a Tibetan
     *  secondary suffix. */
    boolean isPostSuffix() {
        return (null != l
                && ((null == r || "".equals(r))
                    || traits.disambiguator().equals(r)
                    || traits.aVowel().equals(r)) // FIXME: though warn about GAMASA vs. GAMS
                && traits.isPostsuffix(l));
    }

    /** Returns true if and only if this pair could be a Tibetan
     *  suffix. */
    boolean isSuffix() {
        return (null != l
                && ((null == r || "".equals(r))
                    || traits.disambiguator().equals(r)
                    || traits.aVowel().equals(r))
                && traits.isSuffix(l));
    }

    /** Returns true if and only if this pair is merely a
     *  disambiguator. */
    boolean isDisambiguator() {
        return (traits.disambiguator().equals(r) && getLeft() == null);
    }

    /** Yep, this works for TPairs. */
    public boolean equals(Object x) {
        if (x instanceof TPair) {
            TPair p = (TPair)x;
            return ((getLeft() == p.getLeft() || (getLeft() != null && getLeft().equals(p.getLeft())))
                    || (getRight() == p.getRight() || (getRight() != null && getRight().equals(p.getRight()))));
        }
        return false;
    }

    /** Returns a TPair that is like this pair except that it has a
     *  "+" on the right if this pair is empty on the right and, when
     *  appropriate, is empty on the right if this pair has a
     *  disambiguator on the right.  May return itself (but never
     *  mutates this instance). */
    TPair insideStack() {
        if (null == getRight())
            return new TPair(traits, getLeft(), "+");
        else if (traits.disambiguator().equals(getRight())
                 && !traits.stackingMustBeExplicit())
            return new TPair(traits, getLeft(), null);
        else
            return this;
    }

    /** Returns true if this pair contains a Tibetan number. */
    boolean isNumeric() {
        if (l != null && l.length() == 1) {
        	char ch = l.charAt(0);
        	return ((ch >= '0' && ch <= '9')
		            || (ch >= '\u0f18' && ch <= '\u0f33')
					|| ch == '\u0f3e' || ch == '\u0f3f');
        }
        return false;
        // TODO(DLC)[EWTS->Tibetan]: what about half-numbers?
    }

    String getWylie() {
        return getWylie(false, false);
    }

    /** Returns the EWTS Wylie that corresponds to this pair if
     *  justLeft is false, or the EWTS Wylie that corresponds to just
     *  {@link #getLeft()} if justLeft is true.  If dropDisambiguator
     *  is true and the right component is a disambiguator, then the
     *  Wylie will not contain '.'.
     *
     *  <p>Returns "W" for ACIP "W", "r" for ACIP "R", y for ACIP "Y",
     *  even though sometimes the EWTS for those is "w", "R", or "Y".
     *  Handle that in the caller. */
    String getWylie(boolean justLeft, boolean dropDisambiguator) {
        String leftWylie = null;
        if (getLeft() != null) {
            leftWylie = traits.getEwtsForConsonant(getLeft());
            if (leftWylie == null) {
                if (isNumeric())
                    leftWylie = getLeft();
            }
        }
        if (null == leftWylie) leftWylie = "";
        if (justLeft) return leftWylie;
        String rightWylie = null;
        if (!dropDisambiguator && traits.disambiguator().equals(getRight()))
            rightWylie = ".";
        else if ("+".equals(getRight()))
            rightWylie = "+";
        else if (getRight() != null)
            rightWylie = traits.getEwtsForWowel(getRight());
        if (null == rightWylie) rightWylie = "";
        return leftWylie + rightWylie;
    }

    /** Appends legal Unicode corresponding to this (possible
     *  subscribed) pair to sb.  FIXME: which normalization form,
     *  if any? */
    void getUnicode(StringBuffer sb, boolean subscribed) {
        getUnicode(sb, sb, subscribed);
    }

    /** Appends legal Unicode corresponding to this (possible
     *  subscribed) pair to consonantSB (for the non-vowel part) and
     *  vowelSB (for the vowelish part ({'EEm:}, e.g.).  FIXME: which
     *  normalization form, if any? */
    void getUnicode(StringBuffer consonantSB, StringBuffer vowelSB,
                    boolean subscribed) {
        if (null != getLeft()) {
            String x = traits.getUnicodeFor(getLeft(), subscribed);
            if (null == x) throw new Error("TPair: " + getLeft() + " has no Uni");
            consonantSB.append(x);
        }
        if (null != getRight()
            && !(traits.disambiguator().equals(getRight())
                 || "+".equals(getRight()) || traits.aVowel().equals(getRight()))) {
            String x = traits.getUnicodeForWowel(getRight());
            if (null == x) throw new Error("TPair: " + getRight() + " has no Uni");
            vowelSB.append(x);
        }
    }

    /** For ACIP: Returns true if this pair is surely the last pair in
     *  an ACIP stack. Stacking continues through (* . ) and (* . +),
     *  but stops anywhere else.
     *
     *  <p>For EWTS: Returns true if this pair is probably the last
     *  pair in an EWTS stack.  For natives stacks like that found in
     *  [bra], this is not really true. */
    boolean endsStack() {
        final boolean explicitlyStacks = "+".equals(getRight());
        if (!traits.stackingMustBeExplicit())
            return (getRight() != null && !explicitlyStacks);
        else
            return (!explicitlyStacks);
    }
}
