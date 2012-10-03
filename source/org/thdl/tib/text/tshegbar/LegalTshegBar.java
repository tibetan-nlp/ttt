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

package org.thdl.tib.text.tshegbar;

import java.util.Vector;

import org.thdl.tib.text.THDLWylieConstants;
import org.thdl.util.ThdlDebug;

/** <p>A LegalTshegBar is a simple Tibetan syllable or a syllable with
 *  syntactically legal {@link #getPossibleSuffixParticles() suffix
 *  particles}.  A legal tsheg-bar is not a transliteration of Chinese
 *  or some other language.  It obeys the following properties:</p>
 *
 *  <ul>
 *
 *  <li>It contains at most one prefix, which must be one of {EWC_ga,
 *  EWC_da, EWC_ba, EWC_ma, EWC_achung} and must be prefixable to the
 *  root letter.</li>
 *
 *  <li>It contains no vocalic modifications</li>
 *
 *  <li>It may or may not contain an a-chung
 *  (<code>&#92;u0F71</code>)</li>
 *
 *  <li>It contains at most one vowel from the set {EWV_a, EWV_i,
 *  EWV_e, EWV_u}, and that vowel is on the root stack.  The one
 *  exception is that 'i (i.e., the connective case marker), 'u, and
 *  'o suffixes are permitted.</li>
 *
 *  <li>It has at most one suffix, which is a single consonant (the
 *  common case) or a string consisting of 'i, 'u, 'o, 'am, and
 *  'ang.

<p>See Andres' e-mail below:</p>
<pre>
David,

['am] is a particle that means "or" as opposed to "dang" that means and.

"sgom pa'am" would mean "... or meditation"

You can also have "'ang" which would be equivalent to "yang" (also)

"sgom pa'ang" : even/also meditation.

And also there are cases where they combine. For ex you can have

"le'u'i'o". "le'u" means chapter. "le'u'i" means "of this chapter".
'o would mark the end of the sentence.

	Andres 
</pre>
</li>
 *
 *
 *  <li>It may contain a EWC_sa or EWC_da postsuffix iff there exists
 *  a suffix (and a suffix that is not based on 'i, 'o, 'u, 'am, and
 *  'ang).</li>
 *
 *  <li>The root stack follows the rules of Tibetan syntax, meaning
 *  that the following holds:
 *
 *    <ul>
 *
 *       <li>the ra-mgo, sa-mgo, la-mgo head letters appear only over
 *       root consonants (root letters) that take them, if they
 *       appear</li>
 *
 *       <li>the wa-zur, ra-btags, ya-btags, and la-btags subjoined
 *       letters appear only under root consonants (root letters) that
 *       take them</li>
 *
 *       <li>at most one subscribed letter, except for the special
 *       case that ra-btags and wa-zur or ya-btags and wa-zur
 *       sometimes appear together.</li>
 *
 *       <li>the root stack may contain at most one head letter</li>
 *
 *    </ul>
 *
 *  </li>
 *
 *  </ul>
 *
 *  <p>Note that this class uses only a subset of Unicode to represent
 *  consonants and vowels.  You should always use the nominal form of
 *  a letter, e.g. {@link #EWC_wa}, not {@link #EWSUB_wa_zur}, to
 *  represent letters.  (What if you mean to subscribe a fixed-form
 *  wa?  Well, that's not a legal tsheg-bar, so you don't mean to do
 *  that.)</p>
 *
 *  <p>For a pretty good, concise summary of the rules this class
 *  knows about, see Joe B. Wilson's <i>Translating Buddhism from
 *  Tibetan</i> from Snow Lion Publications, Appendix 1,
 *  e.g. p. 548.</p>
 *
 *  @author David Chandler */
public final class LegalTshegBar
    extends TshegBar
    implements UnicodeConstants
{
    /** the prefixed consonant or EW_ABSENT */
    private char prefix;
    /** the consonant superscribed over the {@link #rootLetter} or
     *  EW_ABSENT */
    private char headLetter;
    /** the root consonant, never EW_ABSENT */
    private char rootLetter;
    /** subscribed letter, or EW_ABSENT */
    private char subjoinedLetter;
    /** true iff EWSUB_wa_zur is under the root syllable. */
    private boolean hasWaZur;
    /** true iff EW_wa_zur is under the root syllable. */
    private boolean hasAChung;
    /** If this is a string, it is of a single codepoint or is a
     * string formed from 'i, 'o, 'u, 'am, and 'ang. */
    private String suffix;
    /** EW_da, EW_sa, or EW_ABSENT */
    private char postsuffix;
    /** EWV_i, EWV_u, EWV_e, EWV_o, or EW_ABSENT */
    private char vowel;

    /** Do not use this constructor. */
    private LegalTshegBar() { super(); }

    /** Constructs a valid Tibetan syllable or throws an exception.
     *  Use EW_ABSENT (or null in the case of <code>suffix</code>) for
     *  those parts of the syllable that are absent.  The root letter
     *  must not be absent.  To learn about the arguments, and to be
     *  sure that your input won't cause an exception to be thrown,
     *  see {@link
     *  #formsLegalTshegBar(char,char,char,char,boolean,boolean,String,char,char,StringBuffer)}.
     *
     *  @exception IllegalArgumentException if the rootLetter is not
     *  one of the thirty consonants (and represented nominally, at
     *  that), or if one of the other arguments is not valid, or if
     *  postsuffix is present but suffix is absent, etc. */
    public LegalTshegBar(char prefix, char headLetter, char rootLetter,
                         char subjoinedLetter,
                         boolean hasWaZur,
                         boolean hasAChung,
                         String suffix, char postsuffix, char vowel)
        throws IllegalArgumentException
    {
        super();

        throwIfNotLegalTshegBar(prefix, headLetter, rootLetter,
                                subjoinedLetter, hasWaZur, hasAChung,
                                suffix, postsuffix, vowel);

        this.prefix = prefix;
        this.headLetter = headLetter;
        this.rootLetter = rootLetter;
        this.subjoinedLetter = subjoinedLetter;

        this.hasWaZur = hasWaZur;
        this.hasAChung = hasAChung;

        // copying is slightly inefficient because it is unnecessary
        // since Java strings are read-only, but translating this code
        // to C++ is easier this way.
        this.suffix = (suffix == null) ? null : new String(suffix);

        this.postsuffix = postsuffix;
        this.vowel = vowel;
    }

    /** Like {@link
     *  #LegalTshegBar(char,char,char,char,boolean,boolean,String,char,char)}
     *  but geared for the common case where the suffix is simply a
     *  consonant. */
    public LegalTshegBar(char prefix, char headLetter, char rootLetter,
                         char subjoinedLetter,
                         boolean hasWaZur,
                         boolean hasAChung,
                         char suffix, char postsuffix, char vowel)
        throws IllegalArgumentException
    {
        this(prefix, headLetter, rootLetter, subjoinedLetter,
             hasWaZur, hasAChung,
             (suffix == EW_ABSENT) ? null : new String(new char[] { suffix }),
             postsuffix, vowel);
    }


    /** Returns the prefixed consonant, or EW_ABSENT if there is no
     *  prefix. */
    public char getPrefix() {
        return prefix;
    }

    /** Returns true iff this syllable contains a prefixed
     *  consonant. */
    public boolean hasPrefix() {
        return (EW_ABSENT != prefix);
    }

    /** Returns the non-EWSUB_wa_zur consonant subscribed to the root
     *  consonant, or EW_ABSENT if none is.  If you want to know if
     *  there is a wa-zur, use {@link
     *  #hasWaZurSubjoinedToRootLetter()}.  This returns EWC_ra, not
     *  EWSUB_ra_btags, etc.  */
    public char getSubjoinedLetter() {
        return subjoinedLetter;
    }

    /** Returns true iff the root letter possesses a subscribed
     *  consonant ya-btags, ra-btags, la-btags, or wa-zur. */
    public boolean hasSubjoinedLetter() {
        return (EW_ABSENT != subjoinedLetter);
    }

    public boolean hasWaZurSubjoinedToRootLetter() {
        return hasWaZur;
    }

    public boolean hasAChungOnRootLetter() {
        return hasAChung;
    }

    /** Returns null if there is no suffix, or a string containing the
     *  one consonant or a string like <code>"&#92;u0F60&#92;u0F72"</code>
     *  in the case that the suffix
     *  is 'i, 'u'i'o, 'am, 'ang, etc. */
    public String getSuffix() {
        return suffix;
    }

    /** Returns true iff there is a suffixed consonant or a suffixed
     *  string consisting of 'i, 'u, 'o, 'am, and 'ang. */
    public boolean hasSuffix() {
        return (null != suffix);
    }

    /** Returns true iff there is a single, suffixed consonant.  This
        means that suffixes made from <code>'am</code>,
        <code>'ang</code> <code>'i</code>, <code>'u</code>, and
        <code>'o</code> are not present, but this does not rule out
        the presence of a postsuffix. */
    public boolean hasSimpleSuffix() {
        return ((null != suffix) && (1 == suffix.length()));
    }

    /** If this syllable {@link #hasSimpleSuffix() has a simple
        suffix}, this returns it.
        @exception Exception if {@link #hasSimpleSuffix()} is not true */
    public char getSimpleSuffix() throws Exception {
        if (!hasSimpleSuffix())
            throw new Exception("there isn't a simple suffix");
        return getSuffix().charAt(0);
    }

    /** Returns the secondary suffix, which is either
     *  EWC_da or EWC_sa, or EW_ABSENT if
     *  there is no postsuffix. */
    public char getPostsuffix() {
        return postsuffix;
    }

    /** Returns true iff there is a secondary suffix EWC_da or
     *  EWC_sa. */
    public boolean hasPostsuffix() {
        return (EW_ABSENT != postsuffix);
    }

    /** Returns the root consonant. */
    public char getRootLetter() {
        return rootLetter;
    }

    /** Returns the head letter of the root stack if it has one, or
     *  EW_ABSENT otherwise. */
    public char getHeadLetter() {
        return headLetter;
    }
    
    /** Returns true iff this syllable has a head letter. */
    public boolean hasHeadLetter() {
        return (EW_ABSENT != headLetter);
    }

    /** Returns the vowel, or EW_ABSENT if there is no {@link
     *  #hasExplicitVowel() explicit vowel} (the syllable has the
     *  built-in "ah" sound in this case). */
    public char getVowel() {
        // DLC assert this is one of { EWV_i, EWV_u, EWV_e, EWV_o }
        return vowel;
    }

    /** Returns false iff the implicit, built-in "ah" sound is the
        only vowel for the root stack. */
    public boolean hasExplicitVowel() {
        return (EW_ABSENT != vowel);
    }


    /** Returns a string of two codepoints, da and sa. */
    public static String getPossiblePostsuffixes() {
        return new String(new char[] { EWC_da, EWC_sa });
    }

    private final static String possibleSuffixes
        = new String(new char[] {
            EWC_ga, EWC_nga, EWC_da, EWC_na, EWC_ba, EWC_ma, EWC_achung,
            EWC_ra, EWC_la, EWC_sa
        });

    /** Returns a string of ten codepoints, each of which can be a
     *  suffix in Tibetan. */
    public static String getPossibleSuffixes() {
        return possibleSuffixes;

        // DLC unit test that each EWC is a nominal form of a consonant

        // you could use either &#92;u0F62 or &#92;u0F6A, but we won't confuse
        // this ra for a ra-mgo, so we use &#92;u0F62, EWC_ra, not
        // EWSUB_ra_btags.
    }

    private final static String thirtyConsonants
        = new String(new char[] {
            EWC_ga,  EWC_kha,  EWC_ga,     EWC_nga,
            EWC_ca,  EWC_cha,  EWC_ja,     EWC_nya,
            EWC_ta,  EWC_tha,  EWC_da,     EWC_na,
            EWC_pa,  EWC_pha,  EWC_ba,     EWC_ma,
            EWC_tsa, EWC_tsha, EWC_dza,    EWC_wa,
            EWC_zha, EWC_za,   EWC_achung, EWC_ya,
            EWC_ra,  EWC_la,   EWC_sha,    EWC_sa,
            EWC_ha,  EWC_a
        });

    /** Returns a String containing the nominal Unicode
     *  representations of the thirty consonants.  The consonants are
     *  in the usual order you find them in the 8 row by 4 column
     *  table that students of the language memorize.
     *  @see org.thdl.tib.text.tshegbar.UnicodeConstants */
    public static String getTheThirtyConsonants() {
        ThdlDebug.verify(thirtyConsonants.length() == 30); // DLC put this into a JUnit test to avoid the slow-down.
        return thirtyConsonants;
    }

    /** Returns true iff x is the preferred, nominal Unicode
     *  representation of one the thirty consonants. */
    public static boolean isNominalRepresentationOfConsonant(char x) {
        return (-1 != getTheThirtyConsonants().indexOf(x));
    }


    /** Returns an array of Unicode strings, all the legal suffix
        particles.  In THDL Extended Wylie, these are: <ul>
        <li>'i</li> <li>'o</li> <li>'u</li> <li>'am</li> </ul>
    
        <p>This is not very efficient.</p> */
    public static String[] getPossibleSuffixParticles() {
        return new String[] {
            new String(new char[] { EWC_achung, EWV_i }),
            new String(new char[] { EWC_achung, EWV_o }),
            new String(new char[] { EWC_achung, EWV_u }),
            new String(new char[] { EWC_achung, EWC_ma }),
        };
    }


    /** Returns a String containing the nominal Unicode
     *  representations of the five prefixes.  The prefixes are in
     *  dictionary order.
     *  @see org.thdl.tib.text.tshegbar.UnicodeConstants */
    public static String getTheFivePrefixes() {
        final String s = new String(new char[] {
            EWC_ga, EWC_da, EWC_ba, EWC_ma, EWC_achung
        });
        ThdlDebug.verify(s.length() == 5); // DLC put this into a JUnit test to avoid the slow-down.
        return s;
    }

    /** Returns true iff x is the preferred, nominal Unicode
     *  representation of one of the five prefixes. */
    public static boolean isNominalRepresentationOfPrefix(char x) {
        return (-1 != getTheFivePrefixes().indexOf(x));
    }

    /** Returns a String containing the nominal Unicode
     *  representations of the ten suffixes.  The suffixes are in
     *  dictionary order.  This doesn't include oddballs like suffixes
     *  based on 'i, 'u, 'o, 'am, and 'ang.
     *  @see org.thdl.tib.text.tshegbar.UnicodeConstants */
    public static String getTheTenSuffixes() {
        final String s = new String(new char[] {
            EWC_ga, EWC_nga, EWC_da, EWC_na, EWC_ba,
            EWC_ma, EWC_achung, EWC_ra, EWC_la, EWC_sa
        });
        return s;
    }

    /** Returns true iff x is the preferred, nominal Unicode
     *  representation of one of the ten suffixes.
     */
    public static boolean isNominalRepresentationOfSimpleSuffix(char x) {
        return (-1 != getTheTenSuffixes().indexOf(x));
    }


    /** Legal suffix-like particles, excluding the ten suffixes.  If
     *  you add one, be sure that a tsheg-bar with it has the extended
     *  wylie you wish by adding the correct extended Wylie with it. */
    private static final String[][] oddball_suffixes = new String[][] {
        {
            // connective case marker:
            new String( new char[] {
                EWC_achung, EWV_i
            }),
            THDLWylieConstants.ACHUNG + THDLWylieConstants.i_VOWEL
        },
        {
            new String( new char[] {
                EWC_achung, EWV_u
            }),
            THDLWylieConstants.ACHUNG + THDLWylieConstants.u_VOWEL
        },
        {
            // in at least one context, this shows end of sentence:
            new String( new char[] {
                EWC_achung, EWV_o
            }),
            THDLWylieConstants.ACHUNG + THDLWylieConstants.o_VOWEL
        },
        {
            // as in sgom pa'am:
            new String( new char[] {
                EWC_achung, EWC_ma
            }),
            THDLWylieConstants.ACHUNG + THDLWylieConstants.WYLIE_aVOWEL
            + THDLWylieConstants.MA
        },
        {
            // meaning or, as opposed to and:
            new String( new char[] {
                EWC_achung, EWC_nga
            }),
            THDLWylieConstants.ACHUNG + THDLWylieConstants.WYLIE_aVOWEL
            + THDLWylieConstants.NGA
        }
    };

    /** Returns true iff suffix is 'i, 'o, 'u, 'am, 'ang, or a
     *  concatenation like 'u'i'o.  Returns false otherwise (including
     *  the case that suffix is the empty string). */
    public static boolean isAchungBasedSuffix(String suffix) {
        // TODO(dchandler): use java.util.regex
        int i = 0; // so that the empty string causes false to be returned.
        while (i == 0 || !suffix.equals("")) {
            boolean startsWithOneOfThem = false;
            for (int x = 0; x < oddball_suffixes.length; x++) {
                if (suffix.startsWith(oddball_suffixes[x][0])) {
                    startsWithOneOfThem = true;
                    suffix = suffix.substring(oddball_suffixes[x][0].length());
                    break;
                }
            }
            if (!startsWithOneOfThem)
                return false;
            ++i;
        }
        return true;
    }

    private static String getTHDLWylieForOddballSuffix(String suffix) {
        // FIXME: assert that isAchungBasedSuffix
        StringBuffer wylie = new StringBuffer();
        while (!suffix.equals("")) {
            for (int x = 0; x < oddball_suffixes.length; x++) {
                if (suffix.startsWith(oddball_suffixes[x][0])) {
                    wylie.append(oddball_suffixes[x][1]);
                    suffix = suffix.substring(oddball_suffixes[x][0].length());
                    break;
                }
            }
        }
        return wylie.toString();
    }


    /** Returns true iff the given (rootLetter, subjoinedLetter)
        combination can accept an additional wa-zur.  Only g-r-w,
        d-r-w, and ph-y-w fall into this category according to
        tibwn.ini. (DLC FIXME: are these all legal?  are any others?)

        @param rootLetter the root consonant (in {@link
        UnicodeUtils#isPreferredFormOfConsonant(char) preferred form} in
        you expect true to be returned)
        @param subjoinedLetter the letter subscribed to rootLetter,
        which should not {@link UnicodeUtils#isWa(char) be wa} if you
        expect true to be returned
        @return true iff (rootLetter, subjoinedLetter, wa-zur) is a
        legal stack. */
    public static boolean takesWaZur(char rootLetter,
                                     char subjoinedLetter) {

        // DLC NOW use this test

        if (EW_ABSENT == subjoinedLetter) {
            return isConsonantThatTakesWaZur(rootLetter);
        }
        if (EWC_ra == subjoinedLetter) {
            if (EWC_ga == rootLetter
                    || EWC_da == rootLetter)
                return true;
        } else if (EWC_ya == subjoinedLetter) {
            if (EWC_pha == rootLetter)
                return true;
        }
        return false;
    }

    /** Returns true iff rootLetter is a consonant to which wa-zur can
     *  be subjoined (perhaps in addition to another subjoined
     *  ra-btags or ya-btags. */
    public static boolean isConsonantThatTakesWaZur(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_kha != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_nya != rootLetter
                 && EWC_da != rootLetter
                 && EWC_tsa != rootLetter
                 && EWC_tsha != rootLetter
                 && EWC_zha != rootLetter
                 && EWC_za != rootLetter
                 && EWC_ra != rootLetter
                 && EWC_la != rootLetter
                 && EWC_sha != rootLetter
                 && EWC_pha != rootLetter /* ph-y-w is legal. */
                 && EWC_ha != rootLetter);
    }

    /** Returns true iff rootLetter is a consonant to which ya-btags
     *  can be subjoined. */
    public static boolean isConsonantThatTakesYaBtags(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_kha != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_pa != rootLetter
                 && EWC_pha != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ma != rootLetter
                 && EWC_ha != rootLetter);
    }

    /** Returns true iff rootLetter is a consonant to which la-btags
     *  can be subjoined. */
    public static boolean isConsonantThatTakesLaBtags(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ra != rootLetter
                 && EWC_sa != rootLetter

                 // this combination is pronounced as a
                 // prenasaling, low-tone <i>da</i> in my opinion:
                 && EWC_za != rootLetter);
    }


    /** Returns true iff rootLetter is a consonant to which ra-btags
     *  can be subjoined. */
    public static boolean isConsonantThatTakesRaBtags(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_kha != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_ta != rootLetter
                 && EWC_tha != rootLetter
                 && EWC_da != rootLetter
                 && EWC_na != rootLetter
                 && EWC_pa != rootLetter
                 && EWC_pha != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ma != rootLetter
                 && EWC_sa != rootLetter
                 && EWC_ha != rootLetter);
    }

    /** Returns true iff rootLetter is a consonant that takes a ra-mgo
     *  (pronounced <i>rango</i> because ma is a prenasaling prefix)
     *  head letter */
    public static boolean isConsonantThatTakesRaMgo(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_nga != rootLetter
                 && EWC_ja != rootLetter
                 && EWC_nya != rootLetter
                 && EWC_ta != rootLetter
                 && EWC_da != rootLetter
                 && EWC_na != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ma != rootLetter
                 && EWC_tsa != rootLetter
                 && EWC_dza != rootLetter);
    }

    /** Returns true iff rootLetter is a consonant that takes a la-mgo
     *  (pronounced <i>lango</i> because ma is a prenasaling prefix)
     *  head letter */
    public static boolean isConsonantThatTakesLaMgo(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_nga != rootLetter
                 && EWC_ca != rootLetter
                 && EWC_ja != rootLetter
                 && EWC_ta != rootLetter
                 && EWC_da != rootLetter
                 && EWC_pa != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ha != rootLetter); // pronunciation exception, btw
    }

    /** Returns true iff rootLetter is a consonant that takes a sa-mgo
     *  (pronounced <i>sango</i> because ma is a prenasaling prefix)
     *  head letter */
    public static boolean isConsonantThatTakesSaMgo(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_nga != rootLetter
                 && EWC_nya != rootLetter
                 && EWC_ta != rootLetter
                 && EWC_da != rootLetter
                 && EWC_na != rootLetter
                 && EWC_pa != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ma != rootLetter
                 && EWC_tsa != rootLetter);
    }

    /** Returns true iff the given arguments form a legal Tibetan
     *  syllable.
     *
     *  @param prefix the optional, prefixed consonant
     *  @param headLetter the optional superscribed consonant
     *  @param rootLetter the mandatory root consonant
     *  @param subjoinedLetter the optional, subscribed consonant
     *  @param suffix the optional suffix, which is null, a String
     *  consisting of a single consonant (i.e. a single,
     *  nondecomposable codepoint), or a string of 'i (U+0F, 'u, 'o, 'am,
     *  and 'ang.
     *  @param postsuffix the optional postsuffix, which should be
     *  EWC_sa or EWC_da
     *  @param errorBuffer if non-null, and if the return code is
     *  false, then the reason that this is not a legal tsheg-bar will
     *  be appended to errorBuffer.
     *  @param vowel the optional vowel */
    public static boolean formsLegalTshegBar(char prefix,
                                             char headLetter,
                                             char rootLetter,
                                             char subjoinedLetter,
                                             boolean hasWaZur,
                                             boolean hasAChung,
                                             String suffix,
                                             char postsuffix,
                                             char vowel,
                                             StringBuffer errorBuffer)
    {
        try {
            return internalLegalityTest(prefix, headLetter, rootLetter,
                                        subjoinedLetter, hasWaZur, hasAChung,
                                        suffix, postsuffix, vowel, false,
                                        errorBuffer);
        } catch (IllegalArgumentException e) {
            throw new Error("This simply cannot happen, but it did.");
        }
    }

    /** Like {@link
     *  #formsLegalTshegBar(char,char,char,char,boolean,boolean,String,char,char,StringBuffer)}
     *  but geared for the common case where the suffix is simply a
     *  consonant. */
    public static boolean formsLegalTshegBar(char prefix,
                                             char headLetter,
                                             char rootLetter,
                                             char subjoinedLetter,
                                             boolean hasWaZur,
                                             boolean hasAChung,
                                             char suffix,
                                             char postsuffix,
                                             char vowel,
                                             StringBuffer errorBuffer)
    {
        return formsLegalTshegBar(prefix, headLetter, rootLetter,
                                  subjoinedLetter, hasWaZur, hasAChung,
                                  ((suffix == EW_ABSENT)
                                   ? null
                                   : new String(new char[] { suffix })),
                                  postsuffix, vowel, errorBuffer);
    }


    /** If you get through this gauntlet without having an exception
     *  thrown, then this combination makes a legal Tibetan syllable.
     *  @exception IllegalArgumentException if the syllable does not
     *  follow the rules of a Tibetan syllable.  To learn about the
     *  arguments, see {@link
     *  #formsLegalTshegBar(char,char,char,char,boolean,boolean,String,char,char,StringBuffer)}. */
    private static void throwIfNotLegalTshegBar(char prefix,
                                                char headLetter,
                                                char rootLetter,
                                                char subjoinedLetter,
                                                boolean hasWaZur,
                                                boolean hasAChung,
                                                String suffix,
                                                char postsuffix,
                                                char vowel)
        throws IllegalArgumentException
    {
        internalLegalityTest(prefix, headLetter, rootLetter,
                             subjoinedLetter, hasWaZur, hasAChung,
                             suffix, postsuffix, vowel, true, null);
    }

    /** Voodoo.  Stand back. */
    private static boolean internalThrowThing(boolean doThrow,
                                              StringBuffer errorBuf,
                                              String msg)
    {
        if (errorBuf != null) {
            errorBuf.append(msg);
        }
        if (doThrow)
            throw new IllegalArgumentException(msg);
        return false;
    }

    /** If you get through this gauntlet without having an exception
     *  thrown, then this combination makes a legal Tibetan syllable.
     *  To learn about the arguments, see {@link
     *  #formsLegalTshegBar(char,char,char,char,boolean,boolean,String,char,char,StringBuffer)}.
     *  @param errorBuf if non-null, the reason this is illegal will
     *  be written here, if this is illegal
     *  @return true if this syllable is legal, false if this syllable
     *  is illegal and throwIfIllegal is false, does not return if
     *  this syllable is illegal and throwIfIllegal is true
     *  @exception IllegalArgumentException if the syllable does not
     *  follow the rules of a Tibetan syllable and throwIfIllegal is
     *  true */
    private static boolean internalLegalityTest(char prefix,
                                                char headLetter,
                                                char rootLetter,
                                                char subjoinedLetter,
                                                boolean hasWaZur,
                                                boolean hasAChung,
                                                String suffix,
                                                char postsuffix,
                                                char vowel,
                                                boolean throwIfIllegal,
                                                StringBuffer errorBuf)
        throws IllegalArgumentException
    {
        if (!isNominalRepresentationOfConsonant(rootLetter))
            return internalThrowThing(throwIfIllegal,
                                      errorBuf,
                                      "The root letter must be one of the standard thirty Tibetan consonants, and must be represented nominally, not, for example, by FIXED-FORM RA (&#92;u0F6A)");

        if (EW_ABSENT != prefix) {
            // Ensure that this prefix is one of the five prefixes,
            // and that it can go with this root letter:
            if (!isNominalRepresentationOfPrefix(prefix))
                return internalThrowThing(throwIfIllegal,
                                          errorBuf,
                                          "The prefix is not absent, so it must be one of the five possible prefixes.");
            // DLC test that it can go with the root letter.
        }

        if (EW_ABSENT != subjoinedLetter) {
            if (EWC_ya == subjoinedLetter) {
                if (!isConsonantThatTakesYaBtags(rootLetter)) {
                    return internalThrowThing(throwIfIllegal,
                                              errorBuf,
                                              "Cannot subscribe ya-btags to that root letter.");
                }
            } else if (EWC_ra == subjoinedLetter) {
                if (!isConsonantThatTakesRaBtags(rootLetter)) {
                    return internalThrowThing(throwIfIllegal,
                                              errorBuf,
                                              "Cannot subscribe ra-btags to that root letter.");
                }
            } else if (EWC_la == subjoinedLetter) {
                if (!isConsonantThatTakesLaBtags(rootLetter)) {
                    return internalThrowThing(throwIfIllegal,
                                              errorBuf,
                                              "Cannot subscribe la-btags to that root letter.");
                }
            } else if (EWC_wa == subjoinedLetter) {
                return internalThrowThing(throwIfIllegal,
                                          errorBuf,
                                          "The presence of wa-zur must be specified via a boolean parameter.");
            } else {
                // check for a common mistake:
                if ('\u0FBA' == subjoinedLetter
                    || '\u0FBB' == subjoinedLetter
                    || '\u0FBC' == subjoinedLetter)
                    {
                        return internalThrowThing(throwIfIllegal,
                                                  errorBuf,
                                                  "The subjoined letter given is subjoinable, but you gave the fixed-form variant, which is not used in Tibetan syllables but is sometimes used in Tibetan transliteration of Sanskrit, Chinese, or some non-Tibetan language.");
                    }
                return internalThrowThing(throwIfIllegal,
                                          errorBuf,
                                          "The subjoined letter given is not one of the four consonants that may be subscribed.");
            }
        } // subjoinedLetter tests

        // Suffix tests:
        if (null != suffix) {
            if (!isAchungBasedSuffix(suffix)) {
                if (suffix.length() != 1) {
                    return internalThrowThing(throwIfIllegal,
                                              errorBuf,
                                              "Illegal suffix -- not one of the legal complex suffixes like 'u, 'o, 'i, 'am, 'ang.");
                }
                if (!isNominalRepresentationOfSimpleSuffix(suffix.charAt(0))) {
                    return internalThrowThing(throwIfIllegal,
                                              errorBuf,
                                              "Illegal suffix -- not one of the ten legal suffixes: "
                                              + UnicodeUtils.unicodeCodepointToString(suffix.charAt(0), false));
                }
            }
        }
        if (EW_ABSENT != postsuffix) {
            if (null == suffix)
                return internalThrowThing(throwIfIllegal,
                                          errorBuf,
                                          "You cannot have a postsuffix unless you also have a suffix.");
            if (isAchungBasedSuffix(suffix))
                return internalThrowThing(throwIfIllegal,
                                          errorBuf,
                                          "You cannot have a postsuffix if you have a suffix based on 'i, 'o, 'u, 'am, and 'ang.");
        }

        if (EW_ABSENT != headLetter) {
            if (EWC_ra == headLetter) {
                if (!isConsonantThatTakesRaMgo(rootLetter)) {
                    return internalThrowThing(throwIfIllegal,
                                              errorBuf,
                                              "The head letter ra cannot be used with that root letter.");
                }
            } else if (EWC_la == headLetter) {
                if (!isConsonantThatTakesLaMgo(rootLetter)) {
                    return internalThrowThing(throwIfIllegal,
                                              errorBuf,
                                              "The head letter la cannot be used with that root letter.");
                }
            } else if (EWC_sa == headLetter) {
                if (!isConsonantThatTakesSaMgo(rootLetter)) {
                    // handle a common error specially:
                    if (EWC_la == rootLetter)
                        return internalThrowThing(throwIfIllegal,
                                                  errorBuf,
                                                  "sa cannot be a head letter atop the root letter la.  You probably meant to have sa the root letter and la the subjoined letter.");

                    return internalThrowThing(throwIfIllegal,
                                              errorBuf,
                                              "The head letter sa cannot be used with that root letter.");
                }
            } else {
                // Illegal head letter.
                //
                // Note: U+0F6A is not a valid head letter, even for
                // "rnya".  Use EWC_ra instead.
                return internalThrowThing(throwIfIllegal,
                                          errorBuf,
                                          "The head letter given is not valid.");
            }
        } // headLetter tests

        // Now see if the vowel is valid:
        if (EW_ABSENT /* built-in "ah" sound */ != vowel) {
            if (EWV_i != vowel
                && EWV_u != vowel
                && EWV_e != vowel
                && EWV_o != vowel)
                {
                    if (EWC_achung == vowel)
                        return internalThrowThing(throwIfIllegal,
                                                  errorBuf,
                                                  "The vowel given is not valid.  Use EW_ABSENT for the EWC_achung sound.");
                    if ('\u0F71' == vowel)
                        return internalThrowThing(throwIfIllegal,
                                                  errorBuf,
                                                  "a-chung can be used, but there is a flag for it; you don't call it the vowel.");
                    return internalThrowThing(throwIfIllegal,
                                              errorBuf,
                                              "The vowel given is not valid.");
                }
        }

        // Phew.  We got here, so this combination of inputs is valid.
        // Do nothing to errorBuf.
        return true;
    }


    /*
      DLC put in a method that gets pronunciation using Unicode
      diacritical marks.  And another using just US Roman.  Note that
      pronunciation is contextual, so have these methods return all
      valid pronunciations, such as both "pa" and "wa" for EWC_ba.

      DLC would be nice in the appropriate class: boolean
      isTransliteratedSanskrit(), boolean isTransliteratedChinese()
      (design: contains fa or va, maybe?). */

    /** Returns a StringBuffer that holds the THDL extended wylie
     *  representation of this syllable. */
    public StringBuffer getThdlWylie() {
        StringBuffer sb = new StringBuffer();
        char rootLetter = getRootLetter();
        if (hasPrefix()) {
            // if there is a prefix but no head letter and (prefix,
            // rootLetter) is ambiguous, i.e. if it could be mistaken
            // for a legal (rootLetter, subjoinedLetter) combination,
            // then put out prefix,disambiguator.  else just put out
            // prefix.

            boolean disambiguatorNeeded = false;
            char prefix = getPrefix();
            sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(prefix));
            if (!hasHeadLetter() && !hasSubjoinedLetter()) {
                if (EWC_ya == rootLetter) {
                    if (isConsonantThatTakesYaBtags(prefix))
                        disambiguatorNeeded = true;
                } else if (EWC_ra == rootLetter) {
                    if (isConsonantThatTakesRaBtags(prefix))
                        disambiguatorNeeded = true;
                } else if (EWC_la == rootLetter) {
                    if (isConsonantThatTakesLaBtags(prefix))
                        disambiguatorNeeded = true;
                } else if (EWC_wa == rootLetter) {
                    if (isConsonantThatTakesWaZur(prefix))
                        disambiguatorNeeded = true;
                }
            }
            if (disambiguatorNeeded)
                sb.append(THDLWylieConstants.WYLIE_DISAMBIGUATING_KEY);
        }
        if (hasHeadLetter())
            sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getHeadLetter()));
        sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(rootLetter));
        if (hasSubjoinedLetter())
            sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getSubjoinedLetter()));
        if (hasWaZurSubjoinedToRootLetter())
            sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(EWSUB_wa_zur));

        // a-chung is treated, in THDL Extended Wylie, like a vowel.
        // I.e., you don't have 'pAa', you have 'pA'.
        if (hasAChungOnRootLetter()) {
            if (hasExplicitVowel()) {
                if (EWV_i == getVowel()) {
                    sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint('\u0F73'));
                } else if (EWV_u == getVowel()) {
                    sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint('\u0F75'));
                } else if (EWV_e == getVowel() || EWV_o == getVowel()) {
                    // The exception to the rule for a-chung and vowels...

                    // DLC FIXME: are these allowed in legal Tibetan?
                    // EWTS would have special cases for them if so,
                    // I'd wager, so I bet they're not.
                    sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(EW_achung_vowel));
                    sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getVowel()));
                } else {
                    ThdlDebug.abort("only simple vowels occur in this class, how did this get past internalLegalityTest(..)?");
                }
            } else {
                sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(EW_achung_vowel));
            }
        } else {
            if (hasExplicitVowel())
                sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getVowel()));
            else
                sb.append("a");
        }

        String suf = null;
        if (hasSuffix()) {
            suf = getSuffix();
            if (suf.length() > 1) {
                // pa'am, not pa'm or pa'ama!
                sb.append(getTHDLWylieForOddballSuffix(suf));
            } else {
                sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(suf.charAt(0)));
            }
        }
        if (hasPostsuffix()) {
            // lar.d, la-ra-da, needs a disambiguator.  EWC_sa doesn't
            // take any head letters, but EWC_da does.
            boolean disambiguatorNeeded = false;
            if (getPostsuffix() == EWC_da) {
                if (suf.length() == 1) {
                    char simpleSuffix = suf.charAt(0);
                    if (EWC_ra == simpleSuffix
                        || EWC_la == simpleSuffix
                        || EWC_sa == simpleSuffix) {
                        disambiguatorNeeded = true;
                    }
                }
            }
            if (disambiguatorNeeded)
                sb.append(THDLWylieConstants.WYLIE_DISAMBIGUATING_KEY);
            sb.append(UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getPostsuffix()));
        }
        return sb;
    }


    /** Returns a <legalTibetanSyllable> element that contains only
     *  the THDL Extended Wylie transliteration for the whole syllable
     *  and a note about the transliteration. */
    public String toConciseXML() {
        // DLC version-control the EWTS document. 0.5 is used below:
        return ("<legalTibetanSyllable "
                + "transliterationType=\"THDL Extended Wylie 0.5\" "
                + "transliteration=\"" + getThdlWylie() + "\"" + "/>");
    }

    /** Returns a <legalTibetanSyllable> element that contains the
     *  syllable broken-down into its constituent vowel and
     *  consonants. */
    public String toVerboseXML() {
        // DLC version-control the EWTS document. 0.5 is used below:
        return ("<legalTibetanSyllable "
                + "transliterationType=\"THDL Extended Wylie 0.5\" "
                + (hasPrefix()
                   ? ("prefix=\""
                      + UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getPrefix()) + "\" ")
                   : "")
                + (hasHeadLetter()
                   ? ("headLetter=\""
                      + UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getHeadLetter())
                      + "\" ")
                   : "")
                + ("rootLetter=\""
                   + UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getRootLetter()) + "\" ")
                + (hasSubjoinedLetter()
                   ? ("subjoinedLetter=\""
                      + UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getSubjoinedLetter())
                      + "\" ")
                   : "")
                + (hasWaZurSubjoinedToRootLetter()
                   ? "hasWaZurSubjoinedToRootLetter=\"true\""
                   : "")
                + (hasAChungOnRootLetter()
                   ? "hasAChungOnRootLetter=\"true\""
                   : "")

                // DLC NOW FIXME: what about the root letter a, i.e. &#92;u0F68 ?  do we want the EWTS to be 'aa' ?
                + ("vowel=\""
                   + (hasExplicitVowel()
                      ? UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getVowel())
                      : "a")
                   + "\" ")
                + (hasSuffix()
                   ? ("suffix=\""
                      + UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeString(getSuffix())
                      + "\" ")
                   : "")
                + (hasPostsuffix()
                   ? ("postsuffix=\""
                      + UnicodeCodepointToThdlWylie.getThdlWylieForUnicodeCodepoint(getPostsuffix())
                      + "\" ")
                   : "")
                + "/>");
    }


    /** Overrides {@link org.thdl.tib.text.tshegbar.UnicodeReadyThunk}
        method to return {@link
        UnicodeUtils#toMostlyDecomposedUnicode(String, byte)
        NFKD-normalized Unicode}.
        @exception UnsupportedOperationException is never thrown */
    public String getUnicodeRepresentation() {
        StringBuffer sb = new StringBuffer();
        if (hasPrefix()) {
            ThdlDebug.verify(UnicodeUtils.isNonSubjoinedConsonant(getPrefix()));
            sb.append(getPrefix());
        }
        if (hasHeadLetter()) {
            // DLC NOW FIXME this crap won't be true... it's what we must
            // convert to, though.  Do it.
            ThdlDebug.verify(UnicodeUtils.isNonSubjoinedConsonant(getPrefix()));
            ThdlDebug.verify(UnicodeUtils.isSubjoinedConsonant(getRootLetter()));
            sb.append(getHeadLetter());
        } else {
            ThdlDebug.verify(UnicodeUtils.isNonSubjoinedConsonant(getRootLetter()));
        }
        sb.append(getRootLetter());
        if (hasSubjoinedLetter()) {
            ThdlDebug.verify(UnicodeUtils.isSubjoinedConsonant(getSubjoinedLetter()));
            sb.append(getSubjoinedLetter());
        }
        if (hasWaZurSubjoinedToRootLetter()) {
            ThdlDebug.verify(UnicodeUtils.isSubjoinedConsonant(EWSUB_wa_zur));
            sb.append(EWSUB_wa_zur);
        }
        if (hasAChungOnRootLetter()) {
            ThdlDebug.verify('\u0F71' == EW_achung_vowel);
            sb.append(EW_achung_vowel);
        }
        if (hasExplicitVowel()) {
            sb.append(getVowel());
        }
        if (hasSuffix()) {
            ThdlDebug.verify(UnicodeUtils.isNonSubjoinedConsonant(getSuffix().charAt(0)));
            sb.append(getSuffix());
        }
        if (hasPostsuffix()) {
            ThdlDebug.verify(UnicodeUtils.isNonSubjoinedConsonant(getPostsuffix()));
            sb.append(getPostsuffix());
        }
        return sb.toString();
    }

    /** Overrides {@link org.thdl.tib.text.tshegbar.UnicodeReadyThunk}
        method to return true. */
    public boolean hasUnicodeRepresentation() {
        return true;
    }


    /** Returns a descriptive XML element. */
    public String toString() {
        return toConciseXML();
    }

    /** FIXMEDOC a shortcut */
    private static boolean formsLegalTshegBar(Vector grcls) {
        return formsLegalTshegBar(grcls, 0, grcls.size());
    }

    /** FIXMEDOC DLC
     *  
     *  Returns true iff the given UnicodeGraphemeClusters form a
     *  syntactically legal Tibetan syllable.  If one is null, it
     *  means that it is not present.
     *
     *  @exception IllegalArgumentException if root is null, or if
     *  postsuffix is non-null and suffix is null (these being clearly
     *  illegal)
     */
    private static boolean formsLegalTshegBar(UnicodeGraphemeCluster prefix,
                                              UnicodeGraphemeCluster root,
                                              UnicodeGraphemeCluster suffix,
                                              UnicodeGraphemeCluster postsuffix)
        throws IllegalArgumentException
    {
        // reality checks:
        if (null == root)
            throw new IllegalArgumentException("root letter is not present");
        if (null != postsuffix && null == suffix)
            throw new IllegalArgumentException("a postsuffix cannot occur without a suffix");

        // handle root:
        if (!root.isLegalTibetan())
            return false;
        char headLetter = root.getSuperscribedLetter();
        char rootLetter = root.getRootCP();
        char subjoinedLetter = root.getSoleNonWazurSubjoinedLetter();
        char vowel = root.getVowel();
        boolean hasAchung = root.hasAchung();
        boolean hasWazur = root.hasWazur();

        // handle prefix:
        char prefixLetter = prefix.getSoleTibetanUnicodeCP();

        // handle suffix:
        String suffixString = null;
        if (null != suffix) {
            // DLC FIXME            suffixString = suffix.getUnicodeInUsualOrder();
            throw new Error("DLC FIXME");
        }

        // handle postsuffix:
        char postsuffixLetter = postsuffix.getSoleTibetanUnicodeCP();

        return formsLegalTshegBar(prefixLetter, headLetter, rootLetter,
                                  subjoinedLetter, hasWazur, hasAchung,
                                  suffixString, postsuffixLetter, vowel, null);
    }

    /** Returns true iff the UnicodeGraphemeClusters in grcls with
     *  indices in the range [start, end) form a syntactically legal
     *  syllable.  If start is as large as end, false is returned. */
    private static boolean formsLegalTshegBar(Vector grcls,
                                              int start,
                                              int end)
    {
        int numGrcls = start - end;
        if (numGrcls <= 0)
            return false;
        if (numGrcls == 1) {
            // Option 1: (root)
            // else: return false;

            return formsLegalTshegBar(null,
                                      (UnicodeGraphemeCluster)grcls.elementAt(start),
                                      null, null);
        } else if (numGrcls == 2) {
            // Option 1: (prefix, root)
            // Option 2: (root, suffix)
            // else: return false;

            return (formsLegalTshegBar((UnicodeGraphemeCluster)grcls.elementAt(start),
                                       (UnicodeGraphemeCluster)grcls.elementAt(start + 1),
                                       null,
                                       null)
                    || formsLegalTshegBar(null,
                                          (UnicodeGraphemeCluster)grcls.elementAt(start),
                                          (UnicodeGraphemeCluster)grcls.elementAt(start + 1),
                                          null));
        } else if (numGrcls == 3) {
            // Option 1: (prefix, root, suffix)
            // Option 2: (root, suffix, postsuffix)
            // else: return false;

            return (formsLegalTshegBar((UnicodeGraphemeCluster)grcls.elementAt(start),
                                       (UnicodeGraphemeCluster)grcls.elementAt(start + 1),
                                       (UnicodeGraphemeCluster)grcls.elementAt(start + 2),
                                       null)
                    || formsLegalTshegBar(null,
                                          (UnicodeGraphemeCluster)grcls.elementAt(start),
                                          (UnicodeGraphemeCluster)grcls.elementAt(start + 1),
                                          (UnicodeGraphemeCluster)grcls.elementAt(start + 2)));
        } else if (numGrcls == 4) {
            return (formsLegalTshegBar((UnicodeGraphemeCluster)grcls.elementAt(start),
                                       (UnicodeGraphemeCluster)grcls.elementAt(start + 1),
                                       (UnicodeGraphemeCluster)grcls.elementAt(start + 2),
                                       (UnicodeGraphemeCluster)grcls.elementAt(start + 3)));
        } else {
            // the largest has 'i ... DLC FIXME rethink -- even the case where numGrcls == 3 could be pa'am
            return false;
        }
    }



    /** Returns true if the given Tibetan consonant stack (i.e., the
     *  combination of superscribed, root, and subscribed letters)
     *  takes an EWC_ga prefix.
     *  @param head the {@link
     *  #isNominalRepresentationOfConsonant(char) nominal
     *  representation} of the superscribed letter, or EW_ABSENT if
     *  not present
     *  @param root the {@link
     *  #isNominalRepresentationOfConsonant(char) nominal
     *  representation} of the root letter
     *  @param sub the {@link #isNominalRepresentationOfConsonant(char)
     *  nominal representation} of the subjoined letter, or EW_ABSENT
     *  if not present */
    // NOTE WELL: THE ACIP->TIBETAN CONVERTER USER GUIDE LISTS ALL
    // PREFIX RULES; KEEP IT IN SYNC.
    public static boolean takesGao(char head, char root, char sub) {
        if (EW_ABSENT == head) {
            if (EW_ABSENT == sub) {
                return (EWC_ca == root
                        || EWC_da == root
                        || EWC_na == root
                        || EWC_nya == root
                        || EWC_sa == root
                        || EWC_sha == root
                        || EWC_ta == root
                        || EWC_tsa == root
                        || EWC_ya == root
                        || EWC_za == root
                        || EWC_zha == root);
            }
        }
        return false;
    }

    /** Returns true if the given Tibetan consonant stack (i.e., the
     *  combination of superscribed, root, and subscribed letters)
     *  takes an EWC_da prefix.
     *  @param head the {@link
     *  #isNominalRepresentationOfConsonant(char) nominal
     *  representation} of the superscribed letter, or EW_ABSENT if
     *  not present
     *  @param root the {@link
     *  #isNominalRepresentationOfConsonant(char) nominal
     *  representation} of the root letter
     *  @param sub the {@link #isNominalRepresentationOfConsonant(char)
     *  nominal representation} of the subjoined letter, or EW_ABSENT
     *  if not present */
    // NOTE WELL: THE ACIP->TIBETAN CONVERTER USER GUIDE LISTS ALL
    // PREFIX RULES; KEEP IT IN SYNC.
    public static boolean takesDao(char head, char root, char sub) {
        if (EW_ABSENT == head) {
            if (EW_ABSENT == sub) {
                return (
                        EWC_ba == root
                        || EWC_ga == root
                        || EWC_ka == root
                        || EWC_ma == root
                        || EWC_nga == root
                        || EWC_pa == root
                        );
            } else {
                return (
                        (EWC_ba == root && EWC_ra == sub)
                        || (EWC_ba == root && EWC_ya == sub)
                        || (EWC_ga == root && EWC_ra == sub)
                        || (EWC_ga == root && EWC_ya == sub)
                        || (EWC_ka == root && EWC_ra == sub)
                        || (EWC_ka == root && EWC_ya == sub) // dkyil, for example
                        || (EWC_ma == root && EWC_ya == sub)
                        || (EWC_pa == root && EWC_ra == sub)
                        || (EWC_pa == root && EWC_ya == sub)
                        );
            }
        } else {
            return false;
        }
    }

    /** Returns true if the given Tibetan consonant stack (i.e., the
     *  combination of superscribed, root, and subscribed letters)
     *  takes an EWC_achung prefix.
     *  @param head the {@link
     *  #isNominalRepresentationOfConsonant(char) nominal
     *  representation} of the superscribed letter, or EW_ABSENT if
     *  not present
     *  @param root the {@link
     *  #isNominalRepresentationOfConsonant(char) nominal
     *  representation} of the root letter
     *  @param sub the {@link #isNominalRepresentationOfConsonant(char)
     *  nominal representation} of the subjoined letter, or EW_ABSENT
     *  if not present */
    // NOTE WELL: THE ACIP->TIBETAN CONVERTER USER GUIDE LISTS ALL
    // PREFIX RULES; KEEP IT IN SYNC.
    public static boolean takesAchungPrefix(char head, char root, char sub) {
        if (EW_ABSENT == head) {
            if (EW_ABSENT == sub) {
                return (EWC_ga == root
                        || EWC_ja == root
                        || EWC_da == root
                        || EWC_ba == root
                        || EWC_dza == root
                        || EWC_kha == root
                        || EWC_cha == root
                        || EWC_tha == root
                        || EWC_pha == root
                        || EWC_tsha == root);
            } else {
                return ((EWC_pha == root && EWC_ya == sub)
                        || (EWC_ba == root && EWC_ya == sub)
                        || (EWC_kha == root && EWC_ya == sub)
                        || (EWC_ga == root && EWC_ya == sub)

                        || (EWC_ba == root && EWC_ra == sub)
                        || (EWC_kha == root && EWC_ra == sub)
                        || (EWC_ga == root && EWC_ra == sub)
                        || (EWC_da == root && EWC_ra == sub)
                        || (EWC_pha == root && EWC_ra == sub));
            }
        } else {
            return false;
        }
    }

    /** Returns true if the given Tibetan consonant stack (i.e., the
     *  combination of superscribed, root, and subscribed letters)
     *  takes an EWC_ma prefix.
     *  @param head the {@link
     *  #isNominalRepresentationOfConsonant(char) nominal
     *  representation} of the superscribed letter, or EW_ABSENT if
     *  not present
     *  @param root the {@link
     *  #isNominalRepresentationOfConsonant(char) nominal
     *  representation} of the root letter
     *  @param sub the {@link #isNominalRepresentationOfConsonant(char)
     *  nominal representation} of the subjoined letter, or EW_ABSENT
     *  if not present */
    // NOTE WELL: THE ACIP->TIBETAN CONVERTER USER GUIDE LISTS ALL
    // PREFIX RULES; KEEP IT IN SYNC.
    public static boolean takesMao(char head, char root, char sub) {
        if (EW_ABSENT == head) {
            if (EW_ABSENT == sub) {
                return (EWC_kha == root
                        || EWC_ga == root
                        || EWC_cha == root
                        || EWC_ja == root
                        || EWC_tha == root
                        || EWC_tsha == root
                        || EWC_da == root
                        || EWC_dza == root
                        || EWC_nga == root
                        || EWC_nya == root
                        || EWC_na == root);
            } else {
                return ((EWC_kha == root && EWC_ya == sub)
                        || (EWC_ga == root && EWC_ya == sub)

                        || (EWC_kha == root && EWC_ra == sub)
                        || (EWC_ga == root && EWC_ra == sub));
            }
        } else {
            return false;
        }
    }

    /** Returns true if the given Tibetan consonant stack (i.e., the
     *  combination of superscribed, root, and subscribed letters)
     *  takes an EWC_ba prefix.
     *  @param head the {@link
     *  #isNominalRepresentationOfConsonant(char) nominal
     *  representation} of the superscribed letter, or EW_ABSENT if
     *  not present
     *  @param root the {@link
     *  #isNominalRepresentationOfConsonant(char) nominal
     *  representation} of the root letter
     *  @param sub the {@link #isNominalRepresentationOfConsonant(char)
     *  nominal representation} of the subjoined letter, or EW_ABSENT
     *  if not present */
    // NOTE WELL: THE ACIP->TIBETAN CONVERTER USER GUIDE LISTS ALL
    // PREFIX RULES; KEEP IT IN SYNC.
    public static boolean takesBao(char head, char root, char sub) {

        if (EW_ABSENT == head) {
            if (EW_ABSENT == sub) {
                return (EWC_ka == root
                        || EWC_sa == root // bsams, for example
                        || EWC_ca == root
                        || EWC_ta == root
                        || EWC_tsa == root
                        || EWC_ga == root
                        || EWC_da == root
                        || EWC_zha == root
                        || EWC_za == root
                        || EWC_sha == root
                        );
            } else {
                // kra, e.g.
                return ((EWC_ka == root && EWC_ya == sub)
                        || (EWC_ga == root && EWC_ya == sub)

                        || (EWC_ka == root && EWC_ra == sub)
                        || (EWC_ga == root && EWC_ra == sub)
                        || (EWC_sa == root && EWC_ra == sub)

                        || (EWC_ga == root && EWC_la == sub) // BGLANG in TD4112, says RC, is native Tibetan
                        || (EWC_ka == root && EWC_la == sub)
                        || (EWC_za == root && EWC_la == sub)
                        || (EWC_ra == root && EWC_la == sub)
                        || (EWC_sa == root && EWC_la == sub));
            }
        } else {
            if (EW_ABSENT == sub) {
                // ska, e.g.
                return ((EWC_sa == head && EWC_ka == root)
                        || (EWC_sa == head && EWC_ga == root)
                        || (EWC_sa == head && EWC_nga == root)
                        || (EWC_sa == head && EWC_nya == root)
                        || (EWC_sa == head && EWC_ta == root)
                        || (EWC_sa == head && EWC_da == root)
                        || (EWC_sa == head && EWC_na == root)
                        || (EWC_sa == head && EWC_tsa == root)

                        || (EWC_ra == head && EWC_ka == root)
                        || (EWC_ra == head && EWC_ga == root)
                        || (EWC_ra == head && EWC_nga == root)
                        || (EWC_ra == head && EWC_ja == root)
                        || (EWC_ra == head && EWC_nya == root)
                        || (EWC_ra == head && EWC_ta == root)
                        || (EWC_ra == head && EWC_da == root)
                        || (EWC_ra == head && EWC_na == root)
                        || (EWC_ra == head && EWC_tsa == root)
                        || (EWC_ra == head && EWC_dza == root)

                        || (EWC_la == head && EWC_ca == root) // e.g., BLCAG and BLCAGS says RC
                        || (EWC_la == head && EWC_ta == root)
                        || (EWC_la == head && EWC_da == root));
            } else {
                return ((EWC_ra == head && EWC_ka == root && EWC_ya == sub)
                        || (EWC_ra == head && EWC_ga == root && EWC_ya == sub)
                        || (EWC_sa == head && EWC_ka == root && EWC_ya == sub)
                        || (EWC_sa == head && EWC_ga == root && EWC_ya == sub)
                        || (EWC_sa == head && EWC_ka == root && EWC_ra == sub)
                        || (EWC_sa == head && EWC_ga == root && EWC_ra == sub));
            }
        }
    }
}
