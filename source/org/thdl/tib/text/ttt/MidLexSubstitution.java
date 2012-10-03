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
import java.util.StringTokenizer;

import org.thdl.util.ThdlOptions;

/** MidLexSubstitution is a hack that lets the end user clumsily fix
 *  the EWTS-to-Tibetan and ACIP-to-Tibetan converters without having
 *  to modify the source code.
 *
 *  <p>If the converter isn't giving you what you want for some
 *  tsheg bar, then set up a replacement.
 *
 *  <p>To do so, set the system property
 *  org.thdl.tib.text.ttt.ReplacementMap to be a comma-delimited list
 *  of "x=&gt;y" pairs.  For example, if you think BLKU, which parses
 *  as B+L+KU, should parse as B-L+KU, and you want KAsh to be parsed
 *  as K+sh because the input operators mistyped it, then set
 *  org.thdl.tib.text.ttt.ReplacementMap to
 *  "BLKU=&gt;B-L+KU,KAsh=&gt;K+sh".  Note that this will not cause
 *  B+L+KU to become B-L+KU -- we are doing the replacement during
 *  lexical analysis of the input file, not during parsing.  And it
 *  will cause SBLKU to become SB-L+KU, which is parsed as S+B-L+KU,
 *  probably not what you wanted.  If you fear such things, you can
 *  see if they happen by setting the system property
 *  org.thdl.tib.text.ttt.VerboseReplacementMap to "true", which will
 *  cause an informational message to be printed on the Java console
 *  every time a replacement is made.
 *
 *  <p>Furthermore, you can use the regexp notation
 *  "^BLKU$=&gt;B-L+KU".  Note that regular expressions are not
 *  supported -- we're just borrowing the notation.
 *  "^BLKU=&gt;B-L+KU" means that BLKUM and BLKU will both be
 *  replaced, but SBLKU and SBLKUM will not be.  The caret, '^', means
 *  that we only match if BLKU is at the beginning.  The dollar sign,
 *  '$', means that we only match if the pattern is at the end.
 *  "BLKU$=&gt;B-L+KU" will cause SBLKU to be replaced, but not BLKUM.
 *  Note that performance is far better for ^FOO$ than for ^FOO, FOO$,
 *  or FOO alone.
 *
 *  <p>Only one substitution is made per tsheg bar.  ^FOO$-type
 *  mappings will be tried first, then ^FOO, then FOO$, then FOO.
 *
 *  <p>Note that you cannot literally replace FOO with BAR using this
 *  -- F is not an ACIP character, so the lex will not get far enough
 *  to use this substitution mechanism.  This is not a design flaw --
 *  serious errors require user intervention (and our user can use an
 *  awk script if he or she likes).
 *
 * @author David Chandler */
final class MidLexSubstitution {

    private MidLexSubstitution() { throw new Error("not instantiable"); }

    /** substitutions that apply to whole tsheg bars only,
        i.e. ^FOO$=&gt;BAR substitutions */
    private static HashMap wholeSubstMap = null;

    /** ^FOO=&gt;BAR (but not ^FOO$=&gt;BAR) substitutions */
    private static ArrayList startSubstMap = null;

    /** FOO$=&gt;BAR (but not ^FOO$=&gt;BAR) substitutions */
    private static ArrayList endSubstMap = null;

    /** FOO=&gt;BAR (but not ^FOO$=&gt;BAR or FOO$=&gt;BAR or
        ^FOO=&gt;BAR) substitutions */
    private static ArrayList anywhereSubstMap = null;
    
    private static boolean verbose = false;

    private static boolean inited = false;

    private static final String ARROW = "=>";

    /** Reads the system properties and initializes based on them. */
    private static void init() {
        inited = true;

        verbose = ThdlOptions.getBooleanOption("org.thdl.tib.text.ttt.VerboseReplacementMap");
        if (verbose) {
            System.out.println("You have set org.thdl.tib.text.ttt.VerboseReplacementMap to true.  You must be a power user.");
        }

        String rm = ThdlOptions.getStringOption("org.thdl.tib.text.ttt.ReplacementMap", null);
        if (null != rm) {
            StringTokenizer stok = new StringTokenizer(rm, ",");
            while (stok.hasMoreElements()) {
                String mapping = stok.nextToken();
                String from, to;
                int arrowIndex = mapping.indexOf(ARROW);
                if (arrowIndex < 0) {
                    System.err.println("You went to the trouble of setting the property org.thdl.tib.text.ttt.ReplacementMap, but you had a mapping, \"" + mapping + "\", in it without an arrow (" + ARROW + ").  Aborting.");
                    System.exit(1);
                }

                from = mapping.substring(0, arrowIndex);
                to = mapping.substring(arrowIndex + ARROW.length());

                boolean atStartOnly = false;
                boolean atEndOnly = false;
                if (from.length() > 0 && from.charAt(0) == '^') {
                    atStartOnly = true;
                    from = from.substring(1);
                }
                if (from.length() > 0 && from.charAt(from.length() - 1) == '$') {
                    atEndOnly = true;
                    from = from.substring(0, from.length() - 1);
                }
                if (from.length() == 0) {
                    System.err.println("You went to the trouble of setting the property org.thdl.tib.text.ttt.ReplacementMap, but you had a mapping, \"" + mapping + "\", in it from the empty string to something.  That's nonsense.  Aborting.");
                    System.exit(1);
                }

                if (atStartOnly) {
                    if (atEndOnly) {
                        if (null == wholeSubstMap)
                            wholeSubstMap = new HashMap(2);
                        wholeSubstMap.put(from, to);
                        if (verbose)
                            System.out.println("You have set org.thdl.tib.text.ttt.VerboseReplacementMap to true, so you will want to know that wholeSubstMap maps " + from + " to " + to + ".");
                    } else {
                        if (null == startSubstMap)
                            startSubstMap = new ArrayList(2);
                        startSubstMap.add(new StringMapping(from, to));
                        if (verbose)
                            System.out.println("You have set org.thdl.tib.text.ttt.VerboseReplacementMap to true, so you will want to know that startSubstMap maps " + from + " to " + to + ".");
                    }
                } else {
                    if (atEndOnly) {
                        if (null == endSubstMap)
                            endSubstMap = new ArrayList(2);
                        endSubstMap.add(new StringMapping(from, to));
                        if (verbose)
                            System.out.println("You have set org.thdl.tib.text.ttt.VerboseReplacementMap to true, so you will want to know that endSubstMap maps " + from + " to " + to + ".");
                    } else {
                        if (null == anywhereSubstMap)
                            anywhereSubstMap = new ArrayList(2);
                        anywhereSubstMap.add(new StringMapping(from, to));
                        if (verbose)
                            System.out.println("You have set org.thdl.tib.text.ttt.VerboseReplacementMap to true, so you will want to know that anywhereSubstMap maps " + from + " to " + to + ".");

                    }
                }
            }
        }
    }

    /** Returns the post-substitution value for tok, most often tok
        itself.  See the class comment to understand when tok will
        change. */
    public static String getFinalValueForTibetanNonPunctuationToken(String tok) {
        if (!inited) init();
        String subst = null;
        if (null != wholeSubstMap)
            subst = (String)wholeSubstMap.get(tok);
        if (null == subst && null != startSubstMap) {
            for (int i = 0; i < startSubstMap.size(); i++) {
                StringMapping sm = (StringMapping)startSubstMap.get(i);
                if (tok.startsWith(sm.from)) {
                    subst = sm.to + tok.substring(sm.from.length());
                    break;
                }
            }
        }
        if (null == subst && null != endSubstMap) {
            for (int i = 0; i < endSubstMap.size(); i++) {
                StringMapping sm = (StringMapping)endSubstMap.get(i);
                if (tok.endsWith(sm.from)) {
                    subst = tok.substring(0, tok.length() - sm.from.length()) + sm.to;
                    break;
                }
            }
        }
        if (null == subst && null != anywhereSubstMap) {
            for (int i = 0; i < anywhereSubstMap.size(); i++) {
                StringMapping sm = (StringMapping)anywhereSubstMap.get(i);
                int toki = tok.indexOf(sm.from);
                if (toki >= 0) {
                    subst = tok.substring(0, toki) + sm.to + tok.substring(toki+sm.from.length(), tok.length());
                    break;
                }
            }
        }
        if (null != subst) {
            if (verbose && null != subst) {
                System.out.println("Because org.thdl.tib.text.ttt.VerboseReplacementMap is true, you're being notified that " + tok + " is being replaced with " + subst);
            }
            return subst;
        } else {
            return tok;
        }
    }
}

/** Simple from=&gt;to mapping for non-null Strings. */
class StringMapping {
    public String from, to;
    public StringMapping(String from, String to) {
        this.from = from;
        this.to = to;
    }
}
// DLC NOW: defaults: KAsh=>K+sh, A=>?, '=>? (THESE ARE {A} AND {'} ALONE, NOT AS COMPONENTS OF A TSHEG-BAR.)

