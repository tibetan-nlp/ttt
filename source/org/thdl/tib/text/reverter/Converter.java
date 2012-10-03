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
Library (THDL). Portions created by the THDL are Copyright 2005 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.reverter;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.thdl.tib.text.tshegbar.UnicodeUtils;

/** Static methods for converting Unicode to EWTS and
 *  (TODO(dchandler): ACIP).
 *  @author David Chandler
 */
public class Converter {
    /** Static methods provide all the fun! */
    private Converter() {
        throw new Error("There's no point in instantiating this class.");
    }

    /** Finds combining character sequences. */
    private static BreakIterator breaker
    = BreakIterator.getCharacterInstance(new Locale("bo"));


    private static final boolean debug = false;

    // TODO(dchandler): use this to create LegalTshegBar objects, it's
    // unused right now.
    private static Pattern mightBeLegalTshegBarRegex = Pattern.compile(
            "^"
            + "([\u0f42\u0f51\u0f56\u0f58\u0f60])?"
            // root stack: consonant w/ optional wowels:
            + "(" + GC.consonantStackRegexString + ")"
            + "(([\u0f42\u0f51\u0f56\u0f58\u0f60\u0f44\u0f53\u0f62\u0f63\u0f66][\u0f51\u0f66]?)"
            +  "|(\u0f60[\u0f72\u0f74\u0f7c\u0f44\u0f58])+)?"
            + "$");

    /** Splits nfthdl into grapheme clusters.  Let's define a grapheme
     *  cluster as something an end user would say cannot be
     *  decomposed into two separate pieces sensibly.  For the most
     *  part this is just figuring out the <em>combining character
     *  sequences</em> as defined by Unicode, but (U+0F04 U+0F05*) is
     *  an example of a grapheme cluster that is not a combining
     *  character sequence (TODO(dchandler): (0f04 0f05*), is it
     *  really worth it?  We don't handle it right now, might be good
     *  for Unicode->ACIP anyway.)
     *  @param nfthdl Unicode in NFTHDL decomposition form
     *  @return List of GC objects */
    private static List/*<GC>*/ SplitGC(String nfthdl) {
        
        if (debug) {
            System.out.println("debug: "
                               + UnicodeUtils.unicodeStringToPrettyString(nfthdl));
        }
        ArrayList al = new ArrayList();
        breaker.setText(nfthdl);
        int start = breaker.first();
        boolean just_saw_0f7f = false;
        for (int end = breaker.next();
             end != BreakIterator.DONE;
             start = end, end = breaker.next()) {
            if ((just_saw_0f7f
                 && (Character.getType(nfthdl.charAt(start))
                     == Character.NON_SPACING_MARK))
                || (end > start && '\u0f7f' == nfthdl.charAt(start)
                    && !al.isEmpty())) {
                // U+0F7F is a COMBINING_SPACING_MARK, not a
                // NON_SPACING_MARK, but we want to treat it like a
                // NON_SPACING_MARK.
                GC gc = new GC(((GC)al.get(al.size() - 1)).getNfthdl()
                               + nfthdl.substring(start,end));
                if (debug) {
                    System.out.println("debug: setting last el, "
                                       + al.get(al.size() - 1) + " to " + gc);
                }
                al.set(al.size() - 1, gc);
            } else {
                al.add(new GC(nfthdl.substring(start,end)));
            }
            just_saw_0f7f
                = (end > start && '\u0f7f' == nfthdl.charAt(end - 1));
        }
        return al;
    }

    /** Converts Tibetan Unicode to computer-friendly EWTS
     *  transliteration.  Computer-friendly is not human-friendly but
     *  hopefully even poorly written EWTS->Tibetan converters could
     *  handle the output.  If errors is non-null, error messages are
     *  appended to it.  (Errors are always inline.) */
    public static String convertToEwtsForComputers(String unicode,
                                                   StringBuffer errors) {

        // First, normalize as much as we can to reduce the number of
        // cases we must handle.
        String decomposed
            = UnicodeUtils.toMostlyDecomposedUnicode(unicode,
                                                     UnicodeUtils.NORM_NFTHDL);

        // TODO(dchandler): optionally warn if we see
        // "\u0f40\u0f74\u0f71" which is in the wrong order.

        List gcs = SplitGC(decomposed);

        StringBuffer sb = new StringBuffer();
        for (Iterator it = gcs.iterator(); it.hasNext(); ) {
            GC gc = (GC)it.next();
            StringBuffer ewts = gc.getEwtsForComputers();
            if (null == ewts) {
                // TODO(dchandler): use ErrorsAndWarnings?
                ewts = new StringBuffer("[#ERROR 301: The Unicode '"
                                        + gc.getNfthdl()
                                        + "' (has no EWTS transliteration]");
                if (null != errors) {
                    errors.append(ewts);
                    errors.append('\n');
                }
            }
            sb.append(ewts);
        }
        return sb.toString();
    }
}

// TODO(dchandler): give a mode where an error is given if non-Tibetan
// or at least non-EWTS (think U+534D, e.g.) is found
