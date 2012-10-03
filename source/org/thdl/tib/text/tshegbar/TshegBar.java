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

/** A TshegBar (pronounced <i>tsek bar</i>) is roughly a Tibetan
 *  syllable.  In truth, it is the stuff between two <i>tsek</i>s.
 *
 *  <p> First, some terminology.</p>
 *
 *  <ul> <li>When we talk about a <i>grapheme cluster</i> (or
 *  <i>grcl</i>), we mean what the Unicode standard calls a "grapheme
 *  cluster".  Most glyphs (i.e., pictures) found in a font are
 *  grapheme clusters, but the picture corresponding to the Unicode
 *  codepoint <code>&#92;u0F74</code> is not a grapheme cluster.  In
 *  addition, in English, many fonts have a single glyph (a
 *  "ligature") for the combination of two grapheme clusters,
 *  e.g. "fi".  A single grapheme cluster may have one or more
 *  representations by sequences of Unicode codepoints, or it may not
 *  be representable becuase it is only part of one Unicode codepoint
 *  or pictures a nonstandard character.</li> <li>We will attempt to
 *  avoid using the word "character", as it sometimes refers to a
 *  codepoint and sometimes refers to a glyph in a font and yet other
 *  times refers to a grapheme cluster.</li> <li>We'll try to avoid
 *  using the word "stack" because it sometimes refers to a sequence
 *  of stacked Tibetan consonants and sometimes refers to an entire
 *  grapheme cluster.</li> <li>A <i>Tibetan stack</i> is or one or
 *  more consonants stacked vertically, plus an optional vocalic
 *  modification such as an anusvara (DLC what do we call a bindu?) or
 *  visarga, plus zero or more signs like <code>&#92;u0F35</code>,
 *  plus an optional a-chung (<code>&#92;u0F71</code>), plus an
 *  optional simple vowel.</li> <li>By <i>simple vowel</i>, we mean
 *  any of <code>&#92;u0F72</code>, <code>&#92;u0F74</code>,
 *  <code>&#92;u0F7A</code>, <code>&#92;u0F7B</code>,
 *  <code>&#92;u0F7C</code>, <code>&#92;u0F7D</code>, or
 *  <code>&#92;u0F80</code>.</li> </ul>
 *
 *  <p>(Note: The string <code>"&#92;u0F68&#92;u0F7E&#92;u0F7C"</code>
 *  seems to equal <code>"&#92;u0F00"</code>, though the Unicode
 *  standard does not indicate that it is so.  This code treats it
 *  that way.)</p>
 *
 *  <p> This class allows for invalid tsheg bars, like those
 *  containing more than one prefix, more than two suffixes, an
 *  invalid postsuffix (secondary suffix), more than one consonant
 *  stack (excluding the special case of what we call in THDL Extended
 *  Wylie "'i", which is technically a consonant stack but is used in
 *  Tibetan like a suffix).</p>.
 *
 *  <p>Subclasses exist for valid, grammatically correct tsheg bars,
 *  and for invalid tsheg bars.  Note that correctness is at the tsheg
 *  bar level only; it may be grammatically incorrect to concatenate
 *  two valid tsheg bars.  Some subclasses can be represented in
 *  Unicode, but others contain nonstandard glyphs/characters and
 *  cannot be.</p>
 *
 *  @author David Chandler */
public abstract class TshegBar implements UnicodeReadyThunk {
    /** Returns true, as we consider a transliteration in the Tibetan
     *  alphabet of a non-Tibetan language, say Chinese, as being
     *  Tibetan.
     *  @return true */
    public boolean isTibetan() { return true; }
}
