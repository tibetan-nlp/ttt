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

import org.thdl.util.ThdlDebug;

/** A UnicodeGraphemeCluster is either a non-Tibetan codepoint (such
 *  as whitespace or control characters or a Latin "character"), or a
 *  vertically stacked set of Tibetan consonants, vowels, marks, and
 *  signs.  The Unicode string
 *  <code>"&#92;u0F40&#92;u0F0B&#92;u0F41&#92;u0F0B"</code> specifies
 *  four UnicodeGraphemeClusters (the name of the Tibetan alphabet,
 *  you might notice), while the Unicode string
 *  <code>"&#92;u0F66&#92;u0FA5&#92;u0F39&#92;u0F90&#92;u0FB5&#92;u0F71&#92;u0F80&#92;u0F7F"</code>
 *  is one Tibetan stack, sa over fa over ka over Sha with an a-chung,
 *  a reversed gi-gu, and a visarga, plus a ngas-bzung-sgor-rtags mark
 *  underneath all of that.  I assume the latter grapheme cluster is
 *  nonsense, but it is considered one grapheme cluster because all
 *  but the first char are combining chars.  See Unicode Technical
 *  Report 29.
 *
 *  <p>As the above example demonstrates, not all
 *  UnicodeGraphemeClusters are syntactically legal in the Tibetan
 *  language.  Not all of them are syntactically legal in Sanskrit
 *  transcribed in the Tibetan alphabet, either.</p>
 *
 *  <p>The Unicode 3.2 standard (see especially Technical Report 29)
 *  refers to "grapheme clusters."  A UnicodeGraphemeCluster is
 *  precisely a grapheme cluster as described by that standard.  We
 *  interpret the standard as saying that <code>U+0F3E</code> and
 *  <code>U+0F3F</code> are each grapheme clusters unto themselves,
 *  even though they are combining codepoints.</p>
 *
 *  @author David Chandler */
public class UnicodeGraphemeCluster
    implements UnicodeReadyThunk, UnicodeConstants
{
    /** @see #getCPHeight(char) */
    private static final int MIN_HEIGHT = -6;
    /** @see #getCPHeight(char) */
    private static final int MAX_HEIGHT = 3;

    /** The Unicode codepoints that compose this grapheme cluster.
        This is legal, i.e. if there is a Tibetan vowel, it is the
        last codepoint.  It is in Normalization Form THDL (NFTHDL). */
    private String unicodeString;

    /** Do not use this constructor. */
    private UnicodeGraphemeCluster() { super(); }

    /** Creates a new GraphemeCluster given a legal sequence of
        Unicode codepoints corresponding to a single grapheme
        cluster.
        @exception IllegalArgumentException if unicodeString is not a
        syntactically correct Unicode 3.2 sequence (if it begins with
        a combining codepoint or has a Tibetan vowel before another
        combining character, for example, or if it is more than one
        grapheme cluster.  Note that syntactical correctness for
        non-Tibetan codepoints is not likely to be known by this
        routine. */
    public UnicodeGraphemeCluster(String unicodeString)
        throws IllegalArgumentException
    {
        // check legality:
        // DLC NOW FIXME

        // convert to NFTHDL:
        this.unicodeString
            = UnicodeUtils.toMostlyDecomposedUnicode(unicodeString, NORM_NFTHDL);
    }

    /** Returns a string of codepoints in NFTHDL form. */
    public String getUnicodeRepresentation() {
        return unicodeString;
    }

    /** Returns true. */
    public boolean hasUnicodeRepresentation() {
        return true;
    }

    /** Returns true iff this stack could occur in syntactically
     *  correct, run-of-the-mill Tibetan (as opposed to Tibetanized
     *  Sanksrit, Chinese, et cetera).  sga is a legal Tibetan stack,
     *  but g+g is not, for example. */
    public boolean isLegalTibetan() {
        // DLC FIXME: for those odd head marks etc., return true even
        // though hasUnicodeRepresentation() will return false.
        
        // Note that ra-btags and wa-zur both be present in legal
        // Tibetan.

        throw new Error("DLC FIXME: not yet implemented.");
    }

    /** Returns a <unicodeGraphemeCluster> element that contains the
     *  THDL Extended Wylie transliteration for this cluster. */
    public String toConciseXML() {
        throw new Error("DLC NOW unimplemented");
    }

    /** Returns a <unicodeGraphemeCluster> element that contains this
     *  cluster broken down into its constituent decomposed
     *  codepoints. */
    public String toVerboseXML() {
        throw new Error("DLC NOW unimplemented");
    }

    /** Returns the THDL Extended Wylie transliteration of this
        grapheme cluster, or null if there is none (which happens for
        a few Tibetan codepoints, if you'll recall). If needsVowel is
        true, then an "a" will be appended when there is no
        EW_achung_vowel or explicit simple vowel.  If there is an
        explicit vowel or EW_achung_vowel, it will always be present.
        Note that needsVowel is provided because btags is the
        preferred THDL Extended Wylie for the four contiguous grapheme
        clusters
        <code>"&#92;u0F56&#92;u0F4F&#92;u0F42&#92;u0F66"</code>, and
        needsVowel must be set to false for all but the grapheme
        cluster corresponding to <code>&#92;u0F4F</code> if you wish
        to get the preferred THDL Extended Wylie. */
    public String getThdlWylie(boolean needsVowel) {
        throw new Error("DLC NOW unimplemented");
    }

    /** Given some (possibly unnormalized) Unicode 3.2 string unicode,
        appends grapheme clusters to the vector of GraphemeClusters
        grcls if grcls is nonnulla.  Performs good error checking if
        validate is true.  If an error is found, grcls may have been
        modified if nonnull.  Setting grcls to null and setting
        validate to true is sometimes useful for testing the validity
        of a Unicode string.
        @return the number of grapheme clusters that were or would
        have been added to grcls
        @exception BadTibetanUnicodeException if the unicode is not
        syntactically legal
        @exception IllegalArgumentException if correctErrors and
        validate are both true
        @exception NullPointerException if unicode is null */
    public static int breakUnicodeIntoGraphemeClusters(Vector grcls,
                                                       String unicode,
                                                       boolean validate,
                                                       boolean correctErrors)
        throws // DLC SOON: BadTibetanUnicodeException, 
               IllegalArgumentException, NullPointerException
    {
        if (validate && correctErrors) {
            throw new IllegalArgumentException("validate and correctErrors cannot both be true.");
        }
        throw new Error("DLC NOW unimplemented");
        /*
            if (start == i) {
                // special tests at the beginning of input.
                if (0 != height || UnicodeUtils.combinesLeftToRight(cp)) {
                    throw new BadTibetanUnicodeException("A combining codepoint was found at the start of input or after a mark that ends a grapheme cluster.");
                }
            }
            if (height == last_height) {
                if ('\u0F39' == cp) {
                    if (!UnicodeUtils.isTibetanConsonant(last_cp)) {
                        throw new BadTibetanUnicodeException("U+0F39 can only occur after a (possibly subjoined) Tibetan consonant");
                    }
                } else {
                    // DLC:                    cp BEGINS A NEW GRAPHEME CLUSTER!!!
                }
            }

            // Test to see if this last character has ended this
            // grapheme cluster:
            if (UnicodeUtils.isTibetanTerminatingVowel(cp)) {
                // DLC: cp ENDS A GRAPHEME CLUSTER!!!
            }
        */
    }

    /** FIXMEDOC */
    public String getTopToBottomCodepoints() {
        return getTopToBottomCodepoints(new StringBuffer(unicodeString),
                                        0, unicodeString.length()).toString();
    }

    /** Returns a new StringBuffer consisting of the codepoints in
        NFTHDLString at indices [start, end) sorted in top-to-bottom
        order, or null on some occasions when NFTHDLString is already
        sorted.  A top-to-bottom ordering is a useful form for
        applications wishing to render the grapheme cluster.  Note
        that this method is only useful if NFTHDLString is part of or
        an entire grapheme cluster.  Does no error checking on
        NFTHDLString.
        @param NFTHDLString a buffer with characters at indices i,
        where start <= i < end, being the Unicode codepoints for a
        single grapheme cluster or part of a grapheme cluster
        @param start NFTHDLString.charAt(start) is the first codepoint
        dealt with
        @param end NFTHDLString.charAt(end) is the first codepoint NOT
        dealt with
        @return null only if (but not necessarily if) NFTHDLString is
        already sorted top-to-bottom, or the sorted form of
        NFTHDLString */
    private static StringBuffer getTopToBottomCodepoints(StringBuffer NFTHDLString, /* DLC FIXME: for efficiency, use a ThdlCharIterator. */
                                                         int start, int end)
    {
        if (end <= start) /* 0-length string. */
            return null;
        if (start + 1 == end) /* 1-length string. */
            return null;
        // else we have a string of length >= 2.

        // We'll use the world's fastest sorting algorithm.  Linear
        // time, baby.  Here are the ten or so mailboxes for our
        // postman's sort:
        StringBuffer chunksAtCommonHeights[]
            = new StringBuffer[(MAX_HEIGHT + 1) - MIN_HEIGHT];

        for (int i = start; i < end; i++) {
            char cp = NFTHDLString.charAt(i);
            int height = getCPHeight(cp);

            // initialize mailbox if necessary.
            if (null == chunksAtCommonHeights[height - MIN_HEIGHT]) {
                chunksAtCommonHeights[height - MIN_HEIGHT]
                    = new StringBuffer(2);
            }

            // put this cp into the correct mailbox.
            chunksAtCommonHeights[height - MIN_HEIGHT].append(cp);
        }

        // Now concatenate together the mailboxes:
        StringBuffer sb = new StringBuffer(end - start);
        for (int h = MAX_HEIGHT; h >= MIN_HEIGHT; h--) {
            if (null != chunksAtCommonHeights[h - MIN_HEIGHT]) {
                sb.append(chunksAtCommonHeights[h - MIN_HEIGHT]);
            }
        }
        return sb;
    }


    /** Returns the <i>height</i> for the Tibetan Unicode codepoint x.
        This relative height is 0 for a base consonant, digit,
        punctuation, mark, or sign.  It is -1 for a subjoined
        consonant, -2 for EWSUB_wa_zur, -3 for EW_achung_vowel, +1 for
        EWV_gigu, and so on according to the height these codepoints
        appear relative to one another when on the same stack.  If two
        codepoints have equal height, they should not exist in the
        same grapheme cluster unless one is <code>U+0F39</code>, which
        is an integral part of a consonant when tacked on to, e.g.,
        EWC_PHA.

        <p>If x is not a Unicode 3.2 codepoint in the Tibetan range,
        or if x is not in NFTHDL form, 0 is returned.  The height code
        of <code>U+0F76</code> is not valid, and it is not an accident
        that <code>U+0F76</code> is not in NFTHDL form.</p> */
    private static int getCPHeight(char x) {
        // DLC make this an assertion:
        ThdlDebug.verify(null == UnicodeUtils.toNormalizedForm(x, NORM_NFTHDL));

        if (x >= '\u0F90' && x <= '\u0FAC'
            || x >= '\u0FAE' && x <= '\u0FBC') {
            // subjoined consonant.  Note that wa-zur is an exception.
            return -1;
        } else if (x >= '\u0F00' && x <= '\u0F17'
                   || x >= '\u0F1A' && x <= '\u0F34'
                   || x >= '\u0F3A' && x <= '\u0F3D'
                   || x >= '\u0F40' && x <= '\u0F6A' // consonants
                   || x >= '\u0F88' && x <= '\u0F8B'
                   || x >= '\u0FBE' && x <= '\u0FCF') {
            // neutral height:
            return 0;
        } else { // Oddballs.
            switch (x) {
            //
            // non-combining:
            //
            case '\u0F36':
            case '\u0F38':
            case '\u0F85':
                return 0;


            //
            // combining, but left-to-right combining:
            //
            case '\u0F3E':
            case '\u0F3F':
            case '\u0F7F':
                return 0;


            //
            // combining by coming below:
            //
            case '\u0FAD':
                return -2; // wa-zur
            case '\u0F71':
                return -3; // a-chung
            case '\u0F74':
            case '\u0F84':
                return -4; // DLC CHECKME
            case '\u0F18': // combines with digits
            case '\u0F19': // combines with digits
                return -5;
            case '\u0F35':
            case '\u0F37':
            case '\u0FC6': {
                ThdlDebug.verify(-6 == MIN_HEIGHT);
                return -6; // min height
            }


            //
            // combining by coming above:
            //
            case '\u0F72':
            case '\u0F7A':
            case '\u0F7B':
            case '\u0F7C':
            case '\u0F7D':
            case '\u0F80':
                return 1;
            case '\u0F7E':
            case '\u0F82':
            case '\u0F83':
                return 2; // these three come above 0F7C, right? (DLC CHECKME)
            case '\u0F86':
            case '\u0F87': {
                ThdlDebug.verify(3 == MAX_HEIGHT);
                return 3; // max height
            }


            //
            // exceptional case:
            //
            // some would say +1, but then "\u0F40\u0FA5\u0F39" will
            // not have a5 combine with 39.  Unicode could well have
            // put in a single codepoint for "\u0FA5\u0F39" IMO.
            case '\u0F39': return 0;


            default: {
                if (x >= '\u0F00' && x <= '\u0FFF') {
                    // This wasn't explicitly handled?  Hmmm... This
                    // won't ever happen for NFTHDL-formed input.
                    ThdlDebug.noteIffyCode();
                }

                // This codepoint is not in the Tibetan range.
                return 0;
            }
            } // end switch
        }
    }
    /** DLC SOON */
    public boolean isTibetan() {
        throw new Error("DLC FIXME: not yet implemented.");
    }

    public char getSoleTibetanUnicodeCP() {
//         DLC FIXME -- if has only one and it is in 0f00-0fff, return it.  else,

        throw new Error("DLC FIXME");
        //        return EW_ABSENT;
    }

    char getSuperscribedLetter() {
//         DLC FIXME
        throw new Error("DLC FIXME");
    }

    // DLC NOW -- LegalTshegBar doesn't handle digits w/ underlining, etc.

    /** If this is a Tibetan consonant stack, this returns the root
     *  letter.  If this is a Tibetan digit (perhaps with other
     *  codepoints like U+0F18), this returns the digit.  If this is a
     *  non-Tibetan codepoint, this returns that.
     *
     *  <p>If a consonant stack consists of exactly two consonants,
     *  determining which is the root letter depends on knowing
     *  whether sa (U+0F66) can be superscribed on la (U+0F63) or
     *  whether it is instead the case that la can be subscribed to
     *  sa.  The rules of Tibetan syntax do not permit ambiguity in
     *  this area.</p> */
    char getRootCP() {
        throw new Error("DLC FIXME");
    }
    char getSoleNonWazurSubjoinedLetter() {
        throw new Error("DLC FIXME");
    }
    char getVowel() {
        throw new Error("DLC FIXME");
    }
    boolean hasAchung() {
        throw new Error("DLC FIXME");
    }
    boolean hasWazur() {
        throw new Error("DLC FIXME");
    }
}

