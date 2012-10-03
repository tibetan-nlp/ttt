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
Library (THDL). Portions created by the THDL are Copyright 2002-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.tshegbar;

import junit.framework.TestCase;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.tib.text.tshegbar.LegalTshegBar} at the unit level.
 */
public class LegalTshegBarTest extends TestCase implements UnicodeConstants {
	/**
	 * Plain vanilla constructor for LegalTshegBarTest.
	 * @param arg0
	 */
	public LegalTshegBarTest(String arg0) {
		super(arg0);
	}
    /** Invokes a text UI and runs all this class's tests. */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(LegalTshegBarTest.class);
	}

    /** Tests the getThdlWylie() method to see if we 
        handle "le'u'i'o", "sgom pa'am", "sgom pa'ang", etc.
    */
    public void testGetThdlWylieForLongSuffixLikeThings() {
        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_la,
                                     EW_ABSENT, false, false,
                                     new String(new char[] {
                                         EWC_achung, EWV_u,
                                             EWC_achung, EWV_i,
                                             EWC_achung, EWV_o
                                             }),
                                     EW_ABSENT, EWV_e).getThdlWylie().toString().equals("le'u'i'o"));
        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_la,
                                     EW_ABSENT, false, false,
                                     new String(new char[] {
                                         EWC_achung, EWV_u,
                                             EWC_achung, EWV_i,
                                             EWC_achung, EWV_o,
                                             EWC_achung, EWC_ma,
                                             EWC_achung, EWC_nga,
                                             EWC_achung, EWV_o,
                                             EWC_achung, EWC_ma
                                             }),
                                     EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("la'u'i'o'am'ang'o'am"));
        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_pa,
                                     EW_ABSENT, false, false,
                                     new String(new char[] { EWC_achung, EWC_ma }),
                                     EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("pa'am"));
        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_pa,
                                     EW_ABSENT, false, false,
                                     new String(new char[] { EWC_achung, EWC_nga }),
                                     EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("pa'ang"));
    }

    /** Tests the getThdlWylie() method and one of the constructors. */
    public void testGetThdlWylie() {
        // do we disambiguate when needed?
        {
            assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_ga, EWC_ya,
                                         false, false, EW_ABSENT, EW_ABSENT, EWV_o).getThdlWylie().toString().equals("gyo"));
            assertTrue(new LegalTshegBar(EWC_ga, EW_ABSENT, EWC_ya, EW_ABSENT,
                                         false, false, EW_ABSENT, EW_ABSENT, EWV_o).getThdlWylie().toString().equals("g.yo"));
            assertTrue(new LegalTshegBar(EWC_ba, EW_ABSENT, EWC_la, EW_ABSENT,
                                         false, false, EWC_ga, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("b.lag"));
            assertTrue(new LegalTshegBar(EWC_ba, EW_ABSENT, EWC_la, EW_ABSENT,
                                         false, false, EWC_ga, EWC_sa, EW_ABSENT).getThdlWylie().toString().equals("b.lags"));
            assertTrue(new LegalTshegBar(EWC_ba, EW_ABSENT, EWC_ra, EW_ABSENT,
                                         false, false, EWC_ga, EWC_da, EW_ABSENT).getThdlWylie().toString().equals("b.ragd"));
            assertTrue(new LegalTshegBar(EWC_ba, EW_ABSENT, EWC_ra, EWC_la,
                                         false, false, EWC_ga, EWC_da, EW_ABSENT).getThdlWylie().toString().equals("brlagd"));
            assertTrue(new LegalTshegBar(EWC_ba, EWC_ra, EWC_ga, EW_ABSENT,
                                         false, false, EWC_ga, EWC_da, EW_ABSENT).getThdlWylie().toString().equals("brgagd"));
            assertTrue(new LegalTshegBar(EWC_ba, EWC_la, EWC_ha, EW_ABSENT,
                                         false, false, EWC_ga, EWC_da, EW_ABSENT).getThdlWylie().toString().equals("blhagd"));
            assertTrue(new LegalTshegBar(EWC_ba, EWC_la, EWC_da, EW_ABSENT,
                                         false, false, EWC_ga, EWC_da, EW_ABSENT).getThdlWylie().toString().equals("bldagd"));
        }

        assertTrue(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga, EWC_ra,
                                     false, true, EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrAols"));
        assertTrue(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                                     EWC_ra, true, true,
                                     EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrwAols"));
        assertTrue(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                                     EWC_ra, false, false,
                                     EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrols"));
        assertTrue(new LegalTshegBar(EWC_ba, EW_ABSENT, EWC_ta,
                                     EW_ABSENT, false, false,
                                     EWC_nga, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("btang"));

        // dga and dag are fun, as both are represented by "\u0F51\u0F42":
        {
            assertTrue(new LegalTshegBar(EWC_da, EW_ABSENT, EWC_ga,
                                         EW_ABSENT, false, false,
                                         EW_ABSENT, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("dga"));
            assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_da,
                                         EW_ABSENT, false, false,
                                         EWC_ga, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("dag"));
        }

        assertTrue(new LegalTshegBar(EW_ABSENT, EWC_ra, EWC_da,
                                     EW_ABSENT, false, false,
                                     EWC_ga, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("rdag"));
        assertTrue(new LegalTshegBar(EWC_ba, EWC_ra, EWC_da,
                                     EW_ABSENT, false, false,
                                     EWC_ga, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("brdag"));

        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_nga,
                                     EW_ABSENT, false, false,
                                     "\u0F60\u0F72", EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("nga'i"));

        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_nga,
                                     EW_ABSENT, false, false,
                                     null, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("nga"));

        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_sa,
                                     EWC_la, false, false,
                                     null, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("sla"));

        assertTrue(new LegalTshegBar(EW_ABSENT, EW_ABSENT, EWC_pa,
                                     EW_ABSENT, false, true,
                                     null, EW_ABSENT, EW_ABSENT).getThdlWylie().toString().equals("pA"));

        {
            boolean threw = false;
            try {
                new LegalTshegBar(EW_ABSENT, EWC_sa, EWC_la,
                                  EW_ABSENT, false, false,
                                  null, EW_ABSENT, EW_ABSENT);
            } catch (IllegalArgumentException e) {
                threw = true;
            }
            assertTrue(threw);
        }
    }

    /** Tests the formsLegalTshegBar(..) method. DLC FIXME: but
     * doesn't test it very well. */
    public void testFormsLegalTshegBar() {
        StringBuffer eb = new StringBuffer();

        // Ensure that EWTS's jskad is not legal:
        {
            assertTrue(!LegalTshegBar.formsLegalTshegBar(EWC_ja, EWC_sa,
                                                         EWC_ka, EW_ABSENT,
                                                         false, false,
                                                         EW_ABSENT, EWC_da,
                                                         EW_ABSENT, eb));
        }

        assertTrue(LegalTshegBar.formsLegalTshegBar(EWC_ba, EW_ABSENT,
                                                    EWC_ta, EW_ABSENT,
                                                    false, false,
                                                    EWC_da, EW_ABSENT,
                                                    EW_ABSENT, eb));
        
        // test that there's only one way to make dwa:
        assertTrue(!LegalTshegBar.formsLegalTshegBar(EW_ABSENT, EW_ABSENT,
                                                     EWC_da, EWSUB_wa_zur,
                                                     false, false,
                                                     EW_ABSENT, EW_ABSENT,
                                                     EW_ABSENT, eb));
        assertTrue(!LegalTshegBar.formsLegalTshegBar(EW_ABSENT, EW_ABSENT,
                                                     EWC_da, EWC_wa,
                                                     false, false,
                                                     EW_ABSENT, EW_ABSENT,
                                                     EW_ABSENT, eb));
        boolean result
            = LegalTshegBar.formsLegalTshegBar(EW_ABSENT, EW_ABSENT,
                                               EWC_da, EW_ABSENT,
                                               true, false,
                                               EW_ABSENT, EW_ABSENT,
                                               EW_ABSENT, eb);
        assertTrue(eb.toString(), result);
    }

    /** Tests the behavior of the constructors. */
    public void testConstructors() {
        boolean x;
        
        x = false;
        try {
            new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                              EWSUB_ra_btags, false, false,
                              EWC_la, EWC_sa, EWV_o);
        } catch (IllegalArgumentException e) {
            x = true;
        }
        assertTrue(x);

        x = false;
        try {
            new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                              EWSUB_ra_btags, false, false,
                              new String(new char[] { EWC_la }), EWC_sa,
                              EWV_o);
        } catch (IllegalArgumentException e) {
            x = true;
        }
        assertTrue(x);
    }

    /** Tests {@link
     * org.thdl.tib.text.tshegbar.LegalTshegBar#getTheTenSuffixes()}. */
    public void testGetTheTenSuffixes() {
        String x = LegalTshegBar.getTheTenSuffixes();
        assertTrue(x.length() == 10);
        assertTrue(x.charAt(0) == EWC_ga);
        assertTrue(x.charAt(4) == EWC_ba);
        assertTrue(x.charAt(9) == EWC_sa);
    }

    /** Tests {@link
     * org.thdl.tib.text.tshegbar.LegalTshegBar#isAchungBasedSuffix(String)}. */
    public void testIsAchungBasedSuffix() {
        assertTrue(LegalTshegBar.isAchungBasedSuffix(new String(new char[] {
            EWC_achung, EWC_nga
                })));
        assertTrue(LegalTshegBar.isAchungBasedSuffix(new String(new char[] {
            EWC_achung, EWC_ma
                })));
        assertTrue(LegalTshegBar.isAchungBasedSuffix(new String(new char[] {
            EWC_achung, EWV_i
                })));
        assertTrue(LegalTshegBar.isAchungBasedSuffix(new String(new char[] {
            EWC_achung, EWV_o
                })));
        assertTrue(LegalTshegBar.isAchungBasedSuffix(new String(new char[] {
            EWC_achung, EWV_u
                })));
        assertTrue(LegalTshegBar.isAchungBasedSuffix(new String(new char[] {
            EWC_achung, EWV_u,
            EWC_achung, EWV_i,
            EWC_achung, EWV_o
                })));
        assertTrue(!LegalTshegBar.isAchungBasedSuffix(new String(new char[] {
            EWC_achung, EWV_u,
            EWC_achung, EWV_i,
            EWC_achung, EWV_o, /* no EWC_achung, */ EWC_nga
                })));

        // syntactically illegal, I'd bet, but our algorithm allows it:
        assertTrue(LegalTshegBar.isAchungBasedSuffix(new String(new char[] {
            EWC_achung, EWC_ma,
            EWC_achung, EWV_i,
            EWC_achung, EWV_i,
            EWC_achung, EWV_i,
            EWC_achung, EWV_o,
            EWC_achung, EWC_nga,
            EWC_achung, EWV_o
                })));

        assertTrue(!LegalTshegBar.isAchungBasedSuffix(new String(new char[] {
            EWC_achung, EWC_la
                })));
        assertTrue(!LegalTshegBar.isAchungBasedSuffix(new String(new char[] {
            EWC_achung, EWV_e
                })));

        assertTrue(!LegalTshegBar.isAchungBasedSuffix(""));
    }

    /** Tests that the rules concerning "which root letters take which
     * prefixes?" are accurate.  I got a list of such rules from a
     * native Tibetan who has been kind enough to teach me the
     * fundamentals of the Tibetan language, but I'm not sure where he
     * got the list.
     */
    public void testPrefixRules() {
        // DLC FIXME how can we say that 0Fb2 is ok but 0fBc is not?
        assertTrue(LegalTshegBar.takesBao(EWC_sa, EWC_ka, EWC_ra));
        assertTrue(!LegalTshegBar.takesBao('\u0FB6', EWC_ka, EWC_ra));
        assertTrue(!LegalTshegBar.takesBao(EWC_sa, '\u0F90', EWC_ra));
        assertTrue(!LegalTshegBar.takesBao(EWC_sa, '\u0F90', '\u0FB2'));
        assertTrue(!LegalTshegBar.takesBao('\u0FB6', '\u0F90', EWC_ra));
        assertTrue(!LegalTshegBar.takesBao(EWC_sa, EWC_ka, '\u0FB2'));


        {
            assertTrue(LegalTshegBar.takesBao(EW_ABSENT, EWC_ka, EW_ABSENT));
            assertTrue(LegalTshegBar.takesBao(EWC_la, EWC_da, EW_ABSENT));
            assertTrue(LegalTshegBar.takesBao(EW_ABSENT, EWC_sa, EWC_ra));
            assertTrue(LegalTshegBar.takesBao(EW_ABSENT, EWC_ga, EWC_ra));
            assertTrue(LegalTshegBar.takesBao(EWC_ra, EWC_ga, EWC_ya));

            assertTrue(!LegalTshegBar.takesBao(EWC_la, EWC_nga, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesBao(EWC_ra, EWC_da, EWC_ya));
            assertTrue(!LegalTshegBar.takesBao(EW_ABSENT, EWC_ba, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesBao(EW_ABSENT, EWC_na, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesBao(EW_ABSENT, EWC_nga, EWC_ra));
        }

        {
            assertTrue(LegalTshegBar.takesGao(EW_ABSENT, EWC_ca, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesGao(EW_ABSENT, EWC_ka, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesGao(EW_ABSENT, EWC_ka, EWC_ya));
            assertTrue(!LegalTshegBar.takesGao(EWC_ra, EWC_ka, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesGao(EWC_ra, EWC_ka, EWC_ya));
        }


        {
            assertTrue(LegalTshegBar.takesDao(EW_ABSENT, EWC_ka, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesDao(EW_ABSENT, EWC_wa, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesDao(EW_ABSENT, EWC_nga, EWC_ya));
            assertTrue(!LegalTshegBar.takesDao(EWC_ra, EWC_ga, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesDao(EWC_ra, EWC_ga, EWC_ya));

            assertTrue(LegalTshegBar.takesDao(EW_ABSENT, EWC_ga, EWC_ya));
            assertTrue(LegalTshegBar.takesDao(EW_ABSENT, EWC_ka, EWC_ra));
        }

        {
            assertTrue(LegalTshegBar.takesMao(EW_ABSENT, EWC_ja, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesMao(EW_ABSENT, EWC_wa, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesMao(EW_ABSENT, EWC_nga, EWC_ya));
            assertTrue(!LegalTshegBar.takesMao(EWC_ra, EWC_ga, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesMao(EWC_ra, EWC_ga, EWC_ya));

            assertTrue(LegalTshegBar.takesMao(EW_ABSENT, EWC_kha, EWC_ya));
            assertTrue(LegalTshegBar.takesMao(EW_ABSENT, EWC_kha, EWC_ra));
        }

        {
            assertTrue(LegalTshegBar.takesAchungPrefix(EW_ABSENT, EWC_ga, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesAchungPrefix(EW_ABSENT, EWC_ka, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesAchungPrefix(EW_ABSENT, EWC_wa, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesAchungPrefix(EW_ABSENT, EWC_nga, EWC_ya));
            assertTrue(!LegalTshegBar.takesAchungPrefix(EWC_ra, EWC_ga, EW_ABSENT));
            assertTrue(!LegalTshegBar.takesAchungPrefix(EWC_ra, EWC_ga, EWC_ya));

            assertTrue(LegalTshegBar.takesAchungPrefix(EW_ABSENT, EWC_ba, EWC_ya));
            assertTrue(LegalTshegBar.takesAchungPrefix(EW_ABSENT, EWC_pha, EWC_ra));
        }
    }
}
