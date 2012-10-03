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
Library (THDL). Portions created by the THDL are Copyright 2004 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

import junit.framework.TestCase;

import org.thdl.util.ThdlOptions;


/** Tests this package's ability to understand EWTS and turn it into
 *  the appropriate TMW or Unicode by throwing a lot of
 *  semiautomatically generated test cases, based on data in the
 *  <tt>tibwn.ini</tt> configuration file, at the converter.
 *
 *  @author David Chandler */
public class EWTStibwniniTest extends TestCase {

    /** Invokes a text UI and runs all this class's tests. */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(EWTStibwniniTest.class);
    }

    protected void setUp() {
        // We don't want to use options.txt:
        ThdlOptions.forTestingOnlyInitializeWithoutDefaultOptionsFile();

        ThdlOptions.setUserPreference("thdl.acip.to.tibetan.warning.and.error.severities.are.built.in.defaults", "true");
        ThdlOptions.setUserPreference("thdl.acip.to.tibetan.warning.severity.507", "Most");
        ErrorsAndWarnings.setupSeverityMap();

        // We don't want to load the TM or TMW font files ourselves:
        ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);
        ThdlOptions.setUserPreference("thdl.rely.on.system.tm.fonts", true);
        ThdlOptions.setUserPreference("thdl.debug", true);
    }


    public EWTStibwniniTest() { }

    /** Wrapper around {@link
     *  EWTSTest.ewts2uni_test(String,String)}. */
    private static void ewts2uni_test(String ewts, String expectedUnicode) {
        EWTSTest.ewts2uni_test(ewts, expectedUnicode);
    }

    /** Wrapper around {@link
     *  EWTSTest.ewts2uni_test(String,String)}. */
    private static void assert_EWTS_error(String ewts) {
        EWTSTest.assert_EWTS_error(ewts);
    }

    /** Asserts that ewts is valid EWTS.  Call this for those strings
        that someone might intend a stack in TMW for, but that really
        mean two or more stacks in EWTS thanks to prefix rules. g+ga,
        for example, might be mistakenly input as gga.  If so, it's
        legal EWTS because ga takes a ga prefix. */
    private static void special_case(String ewts) {
        assertTrue(!EWTSTest.hasEwtsError(ewts));
    }

    /** Tests that all of the standard stacks are treated like
     *  standard stacks and that none of the non-standard stacks in
     *  the TMW font are treated like standard stacks.  I generated
     *  this from tibwn.ini (August 10, 2004 edition) with some
     *  off-the-cuff emacs macros. */
    public void test__EWTS__standard_and_nonstandard_stacks() {
        ewts2uni_test("k","\u0F40");
        ewts2uni_test("kh","\u0F41");
        ewts2uni_test("g","\u0F42");
        ewts2uni_test("ng","\u0F44");
        ewts2uni_test("c","\u0F45");
        ewts2uni_test("ch","\u0F46");
        ewts2uni_test("j","\u0F47");
        ewts2uni_test("ny","\u0F49");
        ewts2uni_test("t","\u0F4F");
        ewts2uni_test("th","\u0F50");
        ewts2uni_test("d","\u0F51");
        ewts2uni_test("n","\u0F53");
        ewts2uni_test("p","\u0F54");
        ewts2uni_test("ph","\u0F55");
        ewts2uni_test("b","\u0F56");
        ewts2uni_test("m","\u0F58");
        ewts2uni_test("ts","\u0F59");
        ewts2uni_test("tsh","\u0F5A");
        ewts2uni_test("dz","\u0F5B");
        ewts2uni_test("w","\u0F5D");
        ewts2uni_test("zh","\u0F5E");
        ewts2uni_test("z","\u0F5F");
        ewts2uni_test("'","\u0F60");
        ewts2uni_test("y","\u0F61");
        ewts2uni_test("r","\u0F62");
        ewts2uni_test("l","\u0F63");
        ewts2uni_test("sh","\u0F64");
        ewts2uni_test("s","\u0F66");
        ewts2uni_test("h","\u0f67");
        ewts2uni_test("a","\u0F68");
        ewts2uni_test("r+k","\u0f62\u0f90");
        ewts2uni_test("r+g","\u0f62\u0f92");
        ewts2uni_test("r+ng","\u0f62\u0f94");
        ewts2uni_test("r+j","\u0f62\u0f97");
        ewts2uni_test("r+ny","\u0f62\u0f99");
        ewts2uni_test("r+t","\u0f62\u0f9f");
        ewts2uni_test("r+d","\u0f62\u0fa1");
        ewts2uni_test("r+n","\u0f62\u0fa3");
        ewts2uni_test("r+b","\u0f62\u0fa6");
        ewts2uni_test("r+m","\u0f62\u0fa8");
        ewts2uni_test("r+ts","\u0f62\u0fa9");
        ewts2uni_test("r+dz","\u0f62\u0fab");
        ewts2uni_test("l+k","\u0f63\u0f90");
        ewts2uni_test("l+g","\u0f63\u0f92");
        ewts2uni_test("l+ng","\u0f63\u0f94");
        ewts2uni_test("l+c","\u0f63\u0f95");
        ewts2uni_test("l+j","\u0f63\u0f97");
        ewts2uni_test("l+t","\u0f63\u0f9f");
        ewts2uni_test("l+d","\u0f63\u0fa1");
        ewts2uni_test("l+p","\u0f63\u0fa4");
        ewts2uni_test("l+b","\u0f63\u0fa6");
        ewts2uni_test("l+h","\u0f63\u0fb7");
        ewts2uni_test("s+k","\u0f66\u0f90");
        ewts2uni_test("s+g","\u0f66\u0f92");
        ewts2uni_test("s+ng","\u0f66\u0f94");
        ewts2uni_test("s+ny","\u0f66\u0f99");
        ewts2uni_test("s+t","\u0f66\u0f9f");
        ewts2uni_test("s+d","\u0f66\u0fa1");
        ewts2uni_test("s+n","\u0f66\u0fa3");
        ewts2uni_test("s+p","\u0f66\u0fa4");
        ewts2uni_test("s+b","\u0f66\u0fa6");
        ewts2uni_test("s+m","\u0f66\u0fa8");
        ewts2uni_test("s+ts","\u0f66\u0fa9");
        ewts2uni_test("k+w","\u0f40\u0fad");
        ewts2uni_test("kh+w","\u0f41\u0fad");
        ewts2uni_test("g+w","\u0f42\u0fad");
        ewts2uni_test("c+w","\u0f45\u0fad");
        ewts2uni_test("ny+w","\u0f49\u0fad");
        ewts2uni_test("t+w","\u0f4f\u0fad");
        ewts2uni_test("d+w","\u0f51\u0fad");
        ewts2uni_test("ts+w","\u0f59\u0fad");
        ewts2uni_test("tsh+w","\u0f5a\u0fad");
        ewts2uni_test("zh+w","\u0f5e\u0fad");
        ewts2uni_test("z+w","\u0f5f\u0fad");
        ewts2uni_test("r+w","\u0f62\u0fad");
        ewts2uni_test("sh+w","\u0f64\u0fad");
        ewts2uni_test("s+w","\u0f66\u0fad");
        ewts2uni_test("h+w","\u0f67\u0fad");
        ewts2uni_test("k+y","\u0f40\u0fb1");
        ewts2uni_test("kh+y","\u0f41\u0fb1");
        ewts2uni_test("g+y","\u0f42\u0fb1");
        ewts2uni_test("p+y","\u0f54\u0fb1");
        ewts2uni_test("ph+y","\u0f55\u0fb1");
        ewts2uni_test("b+y","\u0f56\u0fb1");
        ewts2uni_test("m+y","\u0f58\u0fb1");
        ewts2uni_test("k+r","\u0f40\u0fb2");
        ewts2uni_test("kh+r","\u0f41\u0fb2");
        ewts2uni_test("g+r","\u0f42\u0fb2");
        ewts2uni_test("t+r","\u0f4f\u0fb2");
        ewts2uni_test("th+r","\u0f50\u0fb2");
        ewts2uni_test("d+r","\u0f51\u0fb2");
        ewts2uni_test("p+r","\u0f54\u0fb2");
        ewts2uni_test("ph+r","\u0f55\u0fb2");
        ewts2uni_test("b+r","\u0f56\u0fb2");
        ewts2uni_test("m+r","\u0f58\u0fb2");
        ewts2uni_test("sh+r","\u0f64\u0fb2");
        ewts2uni_test("s+r","\u0f66\u0fb2");
        ewts2uni_test("h+r","\u0f67\u0fb2");
        ewts2uni_test("k+l","\u0f40\u0fb3");
        ewts2uni_test("g+l","\u0f42\u0fb3");
        ewts2uni_test("b+l","\u0f56\u0fb3");
        ewts2uni_test("z+l","\u0f5f\u0fb3");
        ewts2uni_test("r+l","\u0f62\u0fb3");
        ewts2uni_test("s+l","\u0f66\u0fb3");
        ewts2uni_test("r+k+y","\u0f62\u0f90\u0fb1");
        ewts2uni_test("r+g+y","\u0f62\u0f92\u0fb1");
        ewts2uni_test("r+m+y","\u0f62\u0fa8\u0fb1");
        ewts2uni_test("r+g+w","\u0f62\u0f92\u0fad");
        ewts2uni_test("r+ts+w","\u0f62\u0fa9\u0fad");
        ewts2uni_test("s+k+y","\u0f66\u0f90\u0fb1");
        ewts2uni_test("s+g+y","\u0f66\u0f92\u0fb1");
        ewts2uni_test("s+p+y","\u0f66\u0fa4\u0fb1");
        ewts2uni_test("s+b+y","\u0f66\u0fa6\u0fb1");
        ewts2uni_test("s+m+y","\u0f66\u0fa8\u0fb1");
        ewts2uni_test("s+k+r","\u0f66\u0f90\u0fb2");
        ewts2uni_test("s+g+r","\u0f66\u0f92\u0fb2");
        ewts2uni_test("s+n+r","\u0f66\u0fa3\u0fb2");
        ewts2uni_test("s+p+r","\u0f66\u0fa4\u0fb2");
        ewts2uni_test("s+b+r","\u0f66\u0fa6\u0fb2");
        ewts2uni_test("s+m+r","\u0f66\u0fa8\u0fb2");
        ewts2uni_test("g+r+w","\u0f42\u0fb2\u0fad");
        ewts2uni_test("d+r+w","\u0f51\u0fb2\u0fad");
        ewts2uni_test("ph+y+w","\u0f55\u0fb1\u0fad");
        
        ewts2uni_test("rk","\u0f62\u0f90");
        ewts2uni_test("rg","\u0f62\u0f92");
        ewts2uni_test("rng","\u0f62\u0f94");
        ewts2uni_test("rj","\u0f62\u0f97");
        ewts2uni_test("rny","\u0f62\u0f99");
        ewts2uni_test("rt","\u0f62\u0f9f");
        ewts2uni_test("rd","\u0f62\u0fa1");
        ewts2uni_test("rn","\u0f62\u0fa3");
        ewts2uni_test("rb","\u0f62\u0fa6");
        ewts2uni_test("rm","\u0f62\u0fa8");
        ewts2uni_test("rts","\u0f62\u0fa9");
        ewts2uni_test("rdz","\u0f62\u0fab");
        ewts2uni_test("lk","\u0f63\u0f90");
        ewts2uni_test("lg","\u0f63\u0f92");
        ewts2uni_test("lng","\u0f63\u0f94");
        ewts2uni_test("lc","\u0f63\u0f95");
        ewts2uni_test("lj","\u0f63\u0f97");
        ewts2uni_test("lt","\u0f63\u0f9f");
        ewts2uni_test("ld","\u0f63\u0fa1");
        ewts2uni_test("lp","\u0f63\u0fa4");
        ewts2uni_test("lb","\u0f63\u0fa6");
        ewts2uni_test("lh","\u0f63\u0fb7");
        ewts2uni_test("sk","\u0f66\u0f90");
        ewts2uni_test("sg","\u0f66\u0f92");
        ewts2uni_test("sng","\u0f66\u0f94");
        ewts2uni_test("sny","\u0f66\u0f99");
        ewts2uni_test("st","\u0f66\u0f9f");
        ewts2uni_test("sd","\u0f66\u0fa1");
        ewts2uni_test("sn","\u0f66\u0fa3");
        ewts2uni_test("sp","\u0f66\u0fa4");
        ewts2uni_test("sb","\u0f66\u0fa6");
        ewts2uni_test("sm","\u0f66\u0fa8");
        ewts2uni_test("sts","\u0f66\u0fa9");
        ewts2uni_test("kw","\u0f40\u0fad");
        ewts2uni_test("khw","\u0f41\u0fad");
        ewts2uni_test("gw","\u0f42\u0fad");
        ewts2uni_test("cw","\u0f45\u0fad");
        ewts2uni_test("nyw","\u0f49\u0fad");
        ewts2uni_test("tw","\u0f4f\u0fad");
        ewts2uni_test("dw","\u0f51\u0fad");
        ewts2uni_test("tsw","\u0f59\u0fad");
        ewts2uni_test("tshw","\u0f5a\u0fad");
        ewts2uni_test("zhw","\u0f5e\u0fad");
        ewts2uni_test("zw","\u0f5f\u0fad");
        ewts2uni_test("rw","\u0f62\u0fad");
        ewts2uni_test("shw","\u0f64\u0fad");
        ewts2uni_test("sw","\u0f66\u0fad");
        ewts2uni_test("hw","\u0f67\u0fad");
        ewts2uni_test("ky","\u0f40\u0fb1");
        ewts2uni_test("khy","\u0f41\u0fb1");
        ewts2uni_test("gy","\u0f42\u0fb1");
        ewts2uni_test("py","\u0f54\u0fb1");
        ewts2uni_test("phy","\u0f55\u0fb1");
        ewts2uni_test("by","\u0f56\u0fb1");
        ewts2uni_test("my","\u0f58\u0fb1");
        ewts2uni_test("kr","\u0f40\u0fb2");
        ewts2uni_test("khr","\u0f41\u0fb2");
        ewts2uni_test("gr","\u0f42\u0fb2");
        ewts2uni_test("tr","\u0f4f\u0fb2");
        ewts2uni_test("thr","\u0f50\u0fb2");
        ewts2uni_test("dr","\u0f51\u0fb2");
        ewts2uni_test("pr","\u0f54\u0fb2");
        ewts2uni_test("phr","\u0f55\u0fb2");
        ewts2uni_test("br","\u0f56\u0fb2");
        ewts2uni_test("mr","\u0f58\u0fb2");
        ewts2uni_test("shr","\u0f64\u0fb2");
        ewts2uni_test("sr","\u0f66\u0fb2");
        ewts2uni_test("hr","\u0f67\u0fb2");
        ewts2uni_test("kl","\u0f40\u0fb3");
        ewts2uni_test("gl","\u0f42\u0fb3");
        ewts2uni_test("bl","\u0f56\u0fb3");
        ewts2uni_test("zl","\u0f5f\u0fb3");
        ewts2uni_test("rl","\u0f62\u0fb3");
        ewts2uni_test("sl","\u0f66\u0fb3");
        ewts2uni_test("rky","\u0f62\u0f90\u0fb1");
        ewts2uni_test("rgy","\u0f62\u0f92\u0fb1");
        ewts2uni_test("rmy","\u0f62\u0fa8\u0fb1");
        ewts2uni_test("rgw","\u0f62\u0f92\u0fad");
        ewts2uni_test("rtsw","\u0f62\u0fa9\u0fad");
        ewts2uni_test("sky","\u0f66\u0f90\u0fb1");
        ewts2uni_test("sgy","\u0f66\u0f92\u0fb1");
        ewts2uni_test("spy","\u0f66\u0fa4\u0fb1");
        ewts2uni_test("sby","\u0f66\u0fa6\u0fb1");
        ewts2uni_test("smy","\u0f66\u0fa8\u0fb1");
        ewts2uni_test("skr","\u0f66\u0f90\u0fb2");
        ewts2uni_test("sgr","\u0f66\u0f92\u0fb2");
        ewts2uni_test("snr","\u0f66\u0fa3\u0fb2");
        ewts2uni_test("spr","\u0f66\u0fa4\u0fb2");
        ewts2uni_test("sbr","\u0f66\u0fa6\u0fb2");
        ewts2uni_test("smr","\u0f66\u0fa8\u0fb2");
        ewts2uni_test("grw","\u0f42\u0fb2\u0fad");
        ewts2uni_test("drw","\u0f51\u0fb2\u0fad");
        ewts2uni_test("phyw","\u0f55\u0fb1\u0fad");
        
        ewts2uni_test("rka","\u0f62\u0f90");
        ewts2uni_test("rga","\u0f62\u0f92");
        ewts2uni_test("rnga","\u0f62\u0f94");
        ewts2uni_test("rja","\u0f62\u0f97");
        ewts2uni_test("rnya","\u0f62\u0f99");
        ewts2uni_test("rta","\u0f62\u0f9f");
        ewts2uni_test("rda","\u0f62\u0fa1");
        ewts2uni_test("rna","\u0f62\u0fa3");
        ewts2uni_test("rba","\u0f62\u0fa6");
        ewts2uni_test("rma","\u0f62\u0fa8");
        ewts2uni_test("rtsa","\u0f62\u0fa9");
        ewts2uni_test("rdza","\u0f62\u0fab");
        ewts2uni_test("lka","\u0f63\u0f90");
        ewts2uni_test("lga","\u0f63\u0f92");
        ewts2uni_test("lnga","\u0f63\u0f94");
        ewts2uni_test("lca","\u0f63\u0f95");
        ewts2uni_test("lja","\u0f63\u0f97");
        ewts2uni_test("lta","\u0f63\u0f9f");
        ewts2uni_test("lda","\u0f63\u0fa1");
        ewts2uni_test("lpa","\u0f63\u0fa4");
        ewts2uni_test("lba","\u0f63\u0fa6");
        ewts2uni_test("lha","\u0f63\u0fb7");
        ewts2uni_test("ska","\u0f66\u0f90");
        ewts2uni_test("sga","\u0f66\u0f92");
        ewts2uni_test("snga","\u0f66\u0f94");
        ewts2uni_test("snya","\u0f66\u0f99");
        ewts2uni_test("sta","\u0f66\u0f9f");
        ewts2uni_test("sda","\u0f66\u0fa1");
        ewts2uni_test("sna","\u0f66\u0fa3");
        ewts2uni_test("spa","\u0f66\u0fa4");
        ewts2uni_test("sba","\u0f66\u0fa6");
        ewts2uni_test("sma","\u0f66\u0fa8");
        ewts2uni_test("stsa","\u0f66\u0fa9");
        ewts2uni_test("kwa","\u0f40\u0fad");
        ewts2uni_test("khwa","\u0f41\u0fad");
        ewts2uni_test("gwa","\u0f42\u0fad");
        ewts2uni_test("cwa","\u0f45\u0fad");
        ewts2uni_test("nywa","\u0f49\u0fad");
        ewts2uni_test("twa","\u0f4f\u0fad");
        ewts2uni_test("dwa","\u0f51\u0fad");
        ewts2uni_test("tswa","\u0f59\u0fad");
        ewts2uni_test("tshwa","\u0f5a\u0fad");
        ewts2uni_test("zhwa","\u0f5e\u0fad");
        ewts2uni_test("zwa","\u0f5f\u0fad");
        ewts2uni_test("rwa","\u0f62\u0fad");
        ewts2uni_test("shwa","\u0f64\u0fad");
        ewts2uni_test("swa","\u0f66\u0fad");
        ewts2uni_test("hwa","\u0f67\u0fad");
        ewts2uni_test("kya","\u0f40\u0fb1");
        ewts2uni_test("khya","\u0f41\u0fb1");
        ewts2uni_test("gya","\u0f42\u0fb1");
        ewts2uni_test("pya","\u0f54\u0fb1");
        ewts2uni_test("phya","\u0f55\u0fb1");
        ewts2uni_test("bya","\u0f56\u0fb1");
        ewts2uni_test("mya","\u0f58\u0fb1");
        ewts2uni_test("kra","\u0f40\u0fb2");
        ewts2uni_test("khra","\u0f41\u0fb2");
        ewts2uni_test("gra","\u0f42\u0fb2");
        ewts2uni_test("tra","\u0f4f\u0fb2");
        ewts2uni_test("thra","\u0f50\u0fb2");
        ewts2uni_test("dra","\u0f51\u0fb2");
        ewts2uni_test("pra","\u0f54\u0fb2");
        ewts2uni_test("phra","\u0f55\u0fb2");
        ewts2uni_test("bra","\u0f56\u0fb2");
        ewts2uni_test("mra","\u0f58\u0fb2");
        ewts2uni_test("shra","\u0f64\u0fb2");
        ewts2uni_test("sra","\u0f66\u0fb2");
        ewts2uni_test("hra","\u0f67\u0fb2");
        ewts2uni_test("kla","\u0f40\u0fb3");
        ewts2uni_test("gla","\u0f42\u0fb3");
        ewts2uni_test("bla","\u0f56\u0fb3");
        ewts2uni_test("zla","\u0f5f\u0fb3");
        ewts2uni_test("rla","\u0f62\u0fb3");
        ewts2uni_test("sla","\u0f66\u0fb3");
        ewts2uni_test("rkya","\u0f62\u0f90\u0fb1");
        ewts2uni_test("rgya","\u0f62\u0f92\u0fb1");
        ewts2uni_test("rmya","\u0f62\u0fa8\u0fb1");
        ewts2uni_test("rgwa","\u0f62\u0f92\u0fad");
        ewts2uni_test("rtswa","\u0f62\u0fa9\u0fad");
        ewts2uni_test("skya","\u0f66\u0f90\u0fb1");
        ewts2uni_test("sgya","\u0f66\u0f92\u0fb1");
        ewts2uni_test("spya","\u0f66\u0fa4\u0fb1");
        ewts2uni_test("sbya","\u0f66\u0fa6\u0fb1");
        ewts2uni_test("smya","\u0f66\u0fa8\u0fb1");
        ewts2uni_test("skra","\u0f66\u0f90\u0fb2");
        ewts2uni_test("sgra","\u0f66\u0f92\u0fb2");
        ewts2uni_test("snra","\u0f66\u0fa3\u0fb2");
        ewts2uni_test("spra","\u0f66\u0fa4\u0fb2");
        ewts2uni_test("sbra","\u0f66\u0fa6\u0fb2");
        ewts2uni_test("smra","\u0f66\u0fa8\u0fb2");
        ewts2uni_test("grwa","\u0f42\u0fb2\u0fad");
        ewts2uni_test("drwa","\u0f51\u0fb2\u0fad");
        ewts2uni_test("phywa","\u0f55\u0fb1\u0fad");
        
        ewts2uni_test("f", "\u0F55\u0F39");
        ewts2uni_test("v", "\u0F56\u0f39");
        ewts2uni_test("T", "\u0F4A");
        ewts2uni_test("Th", "\u0F4B");
        ewts2uni_test("D", "\u0F4C");
        ewts2uni_test("N", "\u0F4E");
        ewts2uni_test("Sh", "\u0F65");
        
        ewts2uni_test("k+Sh", "\u0f40\u0fb5"); // TODO(DLC)[EWTS->Tibetan]: \u0F69 instead?  Shouldn't matter by the unicode standard's terms, and a tiny, separate translator on unicode-to-unicode ought to be better.  But maybe change tibwn.ini?
        ewts2uni_test("k+k", "\u0f40\u0f90");
        ewts2uni_test("k+kh", "\u0f40\u0f91");
        ewts2uni_test("k+ng", "\u0f40\u0f94");
        ewts2uni_test("k+ts", "\u0f40\u0fa9");
        ewts2uni_test("k+t", "\u0f40\u0f9f");
        ewts2uni_test("k+t+y", "\u0f40\u0f9f\u0fb1");
        ewts2uni_test("k+t+r", "\u0f40\u0f9f\u0fb2");
        ewts2uni_test("k+t+r+y", "\u0f40\u0f9f\u0fb2\u0fb1");
        ewts2uni_test("k+t+w", "\u0f40\u0f9f\u0fad");
        ewts2uni_test("k+th", "\u0f40\u0fa0");
        ewts2uni_test("k+th+y", "\u0f40\u0fa0\u0fb1");
        ewts2uni_test("k+N", "\u0f40\u0f9e");
        ewts2uni_test("k+n", "\u0f40\u0fa3");
        ewts2uni_test("k+n+y", "\u0f40\u0fa3\u0fb1");
        ewts2uni_test("k+ph", "\u0f40\u0fa5");
        ewts2uni_test("k+m", "\u0f40\u0fa8");
        ewts2uni_test("k+m+y", "\u0f40\u0fa8\u0fb1");
        ewts2uni_test("k+r+y", "\u0f40\u0fb2\u0fb1");
        ewts2uni_test("k+w+y", "\u0f40\u0fad\u0fb1");
        ewts2uni_test("k+sh", "\u0f40\u0fb4");
        ewts2uni_test("k+s", "\u0f40\u0fb6");
        ewts2uni_test("k+s+n", "\u0f40\u0fb6\u0fa3");
        ewts2uni_test("k+s+m", "\u0f40\u0fb6\u0fa8");
        ewts2uni_test("k+s+y", "\u0f40\u0fb6\u0fb1");
        ewts2uni_test("k+s+w", "\u0f40\u0fb6\u0fad");
        ewts2uni_test("kh+kh", "\u0f41\u0f91");
        ewts2uni_test("kh+n", "\u0f41\u0fa3");
        ewts2uni_test("kh+l", "\u0f41\u0fb3");
        ewts2uni_test("g+g", "\u0f42\u0f92");
        ewts2uni_test("g+g+h", "\u0f42\u0f92\u0fb7");
        ewts2uni_test("g+ny", "\u0f42\u0f99");
        ewts2uni_test("g+d", "\u0f42\u0fa1");
        ewts2uni_test("g+d+h", "\u0f42\u0fa1\u0fb7");
        ewts2uni_test("g+d+h+y", "\u0f42\u0fa1\u0fb7\u0fb1");
        ewts2uni_test("g+d+h+w", "\u0f42\u0fa1\u0fb7\u0fad");
        ewts2uni_test("g+n", "\u0f42\u0fa3");
        ewts2uni_test("g+n+y", "\u0f42\u0fa3\u0fb1");
        ewts2uni_test("g+p", "\u0f42\u0fa4");
        ewts2uni_test("g+b+h", "\u0f42\u0fa6\u0fb7");
        ewts2uni_test("g+b+h+y", "\u0f42\u0fa6\u0fb7\u0fb1");
        ewts2uni_test("g+m", "\u0f42\u0fa8");
        ewts2uni_test("g+m+y", "\u0f42\u0fa8\u0fb1");
        ewts2uni_test("g+r+y", "\u0f42\u0fb2\u0fb1");
        ewts2uni_test("g+h", "\u0f42\u0fb7");  // TODO(DLC)[EWTS->Tibetan]: \u0F43 instead?  Shouldn't matter by the unicode standard's terms, and a tiny, separate translator on unicode-to-unicode ought to be better.  But maybe change tibwn.ini?  (Same goes for every occurrence of \u0f42\u0fb7 in this file.)
        ewts2uni_test("g+h+g+h", "\u0f42\u0fb7\u0f92\u0fb7");
        ewts2uni_test("g+h+ny", "\u0f42\u0fb7\u0f99");
        ewts2uni_test("g+h+n", "\u0f42\u0fb7\u0fa3");
        ewts2uni_test("g+h+n+y", "\u0f42\u0fb7\u0fa3\u0fb1");
        ewts2uni_test("g+h+m", "\u0f42\u0fb7\u0fa8");
        ewts2uni_test("g+h+l", "\u0f42\u0fb7\u0fb3");
        ewts2uni_test("g+h+y", "\u0f42\u0fb7\u0fb1");
        ewts2uni_test("g+h+r", "\u0f42\u0fb7\u0fb2");
        ewts2uni_test("g+h+w", "\u0f42\u0fb7\u0fad");
        ewts2uni_test("ng+k", "\u0f44\u0f90");
        ewts2uni_test("ng+k+t", "\u0f44\u0f90\u0f9f");
        ewts2uni_test("ng+k+t+y", "\u0f44\u0f90\u0f9f\u0fb1");
        ewts2uni_test("ng+k+y", "\u0f44\u0f90\u0fb1");
        ewts2uni_test("ng+kh", "\u0f44\u0f91");
        ewts2uni_test("ng+kh+y", "\u0f44\u0f91\u0fb1");
        ewts2uni_test("ng+g", "\u0f44\u0f92");
        ewts2uni_test("ng+g+r", "\u0f44\u0f92\u0fb2");
        ewts2uni_test("ng+g+y", "\u0f44\u0f92\u0fb1");
        ewts2uni_test("ng+g+h", "\u0f44\u0f92\u0fb7");
        ewts2uni_test("ng+g+h+y", "\u0f44\u0f92\u0fb7\u0fb1");
        ewts2uni_test("ng+g+h+r", "\u0f44\u0f92\u0fb7\u0fb2");
        ewts2uni_test("ng+ng", "\u0f44\u0f94");
        ewts2uni_test("ng+t", "\u0f44\u0f9f");
        ewts2uni_test("ng+n", "\u0f44\u0fa3");
        ewts2uni_test("ng+m", "\u0f44\u0fa8");
        ewts2uni_test("ng+y", "\u0f44\u0fb1");
        ewts2uni_test("ng+l", "\u0f44\u0fb3");
        ewts2uni_test("ng+sh", "\u0f44\u0fb4");
        ewts2uni_test("ng+h", "\u0f44\u0fb7");
        ewts2uni_test("ng+k+Sh", "\u0f44\u0f90\u0fb5");
        ewts2uni_test("ng+k+Sh+w", "\u0f44\u0f90\u0fb5\u0fad");
        ewts2uni_test("ng+k+Sh+y", "\u0f44\u0f90\u0fb5\u0fb1");
        ewts2uni_test("ts+ts", "\u0f59\u0fa9");
        ewts2uni_test("ts+tsh", "\u0f59\u0faa");
        ewts2uni_test("ts+tsh+w", "\u0f59\u0faa\u0fad");
        ewts2uni_test("ts+tsh+r", "\u0f59\u0faa\u0fb2");
        ewts2uni_test("ts+ny", "\u0f59\u0f99");
        ewts2uni_test("ts+n+y", "\u0f59\u0fa3\u0fb1");
        ewts2uni_test("ts+m", "\u0f59\u0fa8");
        ewts2uni_test("ts+y", "\u0f59\u0fb1");
        ewts2uni_test("ts+r", "\u0f59\u0fb2");
        ewts2uni_test("ts+l", "\u0f59\u0fb3");
        ewts2uni_test("ts+h+y", "\u0f59\u0fb7\u0fb1");
        ewts2uni_test("tsh+th", "\u0f5a\u0fa0");
        ewts2uni_test("tsh+tsh", "\u0f5a\u0faa");
        ewts2uni_test("tsh+y", "\u0f5a\u0fb1");
        ewts2uni_test("tsh+r", "\u0f5a\u0fb2");
        ewts2uni_test("tsh+l", "\u0f5a\u0fb3");
        ewts2uni_test("dz+dz", "\u0f5b\u0fab");
        ewts2uni_test("dz+dz+ny", "\u0f5b\u0fab\u0f99");
        ewts2uni_test("dz+dz+w", "\u0f5b\u0fab\u0fad");
        ewts2uni_test("dz+dz+h", "\u0f5b\u0fab\u0fb7");
        ewts2uni_test("dz+h+dz+h", "\u0f5b\u0fb7\u0fab\u0fb7");
        ewts2uni_test("dz+ny", "\u0f5b\u0f99");
        ewts2uni_test("dz+ny+y", "\u0f5b\u0f99\u0fb1");
        ewts2uni_test("dz+n", "\u0f5b\u0fa3");
        ewts2uni_test("dz+n+w", "\u0f5b\u0fa3\u0fad");
        ewts2uni_test("dz+m", "\u0f5b\u0fa8");
        ewts2uni_test("dz+y", "\u0f5b\u0fb1");
        ewts2uni_test("dz+r", "\u0f5b\u0fb2");
        ewts2uni_test("dz+w", "\u0f5b\u0fad");
        ewts2uni_test("dz+h", "\u0F5B\u0FB7");  // TODO(DLC)[EWTS->Tibetan]: 0f5c is what tibwn.ini has
        ewts2uni_test("dz+h+y", "\u0f5b\u0fb7\u0fb1");  // TODO(DLC)[EWTS->Tibetan]: 0f5c is what tibwn.ini has
        ewts2uni_test("dz+h+r", "\u0f5b\u0fb7\u0fb2");  // TODO(DLC)[EWTS->Tibetan]: 0f5c is what tibwn.ini has
        ewts2uni_test("dz+h+l", "\u0f5b\u0fb7\u0fb3");  // TODO(DLC)[EWTS->Tibetan]: 0f5c is what tibwn.ini has
        ewts2uni_test("dz+h+w", "\u0f5b\u0fb7\u0fad");  // TODO(DLC)[EWTS->Tibetan]: 0f5c is what tibwn.ini has
        ewts2uni_test("ny+ts", "\u0f49\u0fa9");
        ewts2uni_test("ny+ts+m", "\u0f49\u0fa9\u0fa8");
        ewts2uni_test("ny+ts+y", "\u0f49\u0fa9\u0fb1");
        ewts2uni_test("ny+tsh", "\u0f49\u0faa");
        ewts2uni_test("ny+dz", "\u0f49\u0fab");
        ewts2uni_test("ny+dz+y", "\u0f49\u0fab\u0fb1");
        ewts2uni_test("ny+dz+h", "\u0f49\u0fab\u0fb7");
        ewts2uni_test("ny+ny", "\u0f49\u0f99");
        ewts2uni_test("ny+p", "\u0f49\u0fa4");
        ewts2uni_test("ny+ph", "\u0f49\u0fa5");
        ewts2uni_test("ny+y", "\u0f49\u0fb1");
        ewts2uni_test("ny+r", "\u0f49\u0fb2");
        ewts2uni_test("ny+l", "\u0f49\u0fb3");
        ewts2uni_test("ny+sh", "\u0f49\u0fb4");
        ewts2uni_test("T+k", "\u0f4a\u0f90");
        ewts2uni_test("T+T", "\u0f4a\u0f9a");
        ewts2uni_test("T+T+h", "\u0f4a\u0f9a\u0fb7");
        ewts2uni_test("T+n", "\u0f4a\u0fa3");
        ewts2uni_test("T+p", "\u0f4a\u0fa4");
        ewts2uni_test("T+m", "\u0f4a\u0fa8");
        ewts2uni_test("T+y", "\u0f4a\u0fb1");
        ewts2uni_test("T+w", "\u0f4a\u0fad");
        ewts2uni_test("T+s", "\u0f4a\u0fb6");
        ewts2uni_test("Th+y", "\u0f4b\u0fb1");
        ewts2uni_test("Th+r", "\u0f4b\u0fb2");
        ewts2uni_test("D+g", "\u0f4c\u0f92");
        ewts2uni_test("D+g+y", "\u0f4c\u0f92\u0fb1");
        ewts2uni_test("D+g+h", "\u0f4c\u0f92\u0fb7");
        ewts2uni_test("D+g+h+r", "\u0f4c\u0f92\u0fb7\u0fb2");
        ewts2uni_test("D+D", "\u0f4c\u0f9c");
        ewts2uni_test("D+D+h", "\u0f4c\u0f9c\u0fb7");
        ewts2uni_test("D+D+h+y", "\u0f4c\u0f9c\u0fb7\u0fb1");
        ewts2uni_test("D+n", "\u0f4c\u0fa3");
        ewts2uni_test("D+m", "\u0f4c\u0fa8");
        ewts2uni_test("D+y", "\u0f4c\u0fb1");
        ewts2uni_test("D+r", "\u0f4c\u0fb2");
        ewts2uni_test("D+w", "\u0f4c\u0fad");
        ewts2uni_test("D+h", "\u0F4C\u0FB7");  // TODO(DLC)[EWTS->Tibetan]: 0f4d is what tibwn.ini has
        {
            // TODO(DLC)[EWTS->Tibetan]: 0f4d is what tibwn.ini has
            ewts2uni_test("D+h+D+h", "\u0f4c\u0fb7\u0f9c\u0fb7");
            // TODO(DLC)[EWTS->Tibetan]: 0f9d is what tibwn.ini has
        }
        ewts2uni_test("D+h+m", "\u0f4c\u0fb7\u0fa8");  // TODO(DLC)[EWTS->Tibetan]: 0f4d is what tibwn.ini has
        ewts2uni_test("D+h+y", "\u0f4c\u0fb7\u0fb1");  // TODO(DLC)[EWTS->Tibetan]: 0f4d is what tibwn.ini has
        ewts2uni_test("D+h+r", "\u0f4c\u0fb7\u0fb2");  // TODO(DLC)[EWTS->Tibetan]: 0f4d is what tibwn.ini has
        ewts2uni_test("D+h+w", "\u0f4c\u0fb7\u0fad");  // TODO(DLC)[EWTS->Tibetan]: 0f4d is what tibwn.ini has
        ewts2uni_test("N+T", "\u0f4e\u0f9a");
        ewts2uni_test("N+Th", "\u0f4e\u0f9b");
        ewts2uni_test("N+D", "\u0f4e\u0f9c");
        ewts2uni_test("N+D+Y", "\u0f4e\u0f9c\u0fbb");
        ewts2uni_test("N+D+r", "\u0f4e\u0f9c\u0fb2");
        ewts2uni_test("N+D+R+y", "\u0f4e\u0f9c\u0fbc\u0fb1");
        ewts2uni_test("N+D+h", "\u0f4e\u0f9c\u0fb7");
        ewts2uni_test("N+N", "\u0f4e\u0f9e");
        ewts2uni_test("N+d+r", "\u0f4e\u0fa1\u0fb2");
        ewts2uni_test("N+m", "\u0f4e\u0fa8");
        ewts2uni_test("N+y", "\u0f4e\u0fb1");
        ewts2uni_test("N+w", "\u0f4e\u0fad");
        ewts2uni_test("t+k", "\u0f4f\u0f90");
        ewts2uni_test("t+k+r", "\u0f4f\u0f90\u0fb2");
        ewts2uni_test("t+k+w", "\u0f4f\u0f90\u0fad");
        ewts2uni_test("t+k+s", "\u0f4f\u0f90\u0fb6");
        ewts2uni_test("t+g", "\u0f4f\u0f92");
        ewts2uni_test("t+ny", "\u0f4f\u0f99");
        ewts2uni_test("t+Th", "\u0f4f\u0f9b");
        ewts2uni_test("t+t", "\u0f4f\u0f9f");
        ewts2uni_test("t+t+y", "\u0f4f\u0f9f\u0fb1");
        ewts2uni_test("t+t+r", "\u0f4f\u0f9f\u0fb2");
        ewts2uni_test("t+t+w", "\u0f4f\u0f9f\u0fad");
        ewts2uni_test("t+th", "\u0f4f\u0fa0");
        ewts2uni_test("t+th+y", "\u0f4f\u0fa0\u0fb1");
        ewts2uni_test("t+n", "\u0f4f\u0fa3");
        ewts2uni_test("t+n+y", "\u0f4f\u0fa3\u0fb1");
        ewts2uni_test("t+p", "\u0f4f\u0fa4");
        ewts2uni_test("t+p+r", "\u0f4f\u0fa4\u0fb2");
        ewts2uni_test("t+ph", "\u0f4f\u0fa5");
        ewts2uni_test("t+m", "\u0f4f\u0fa8");
        ewts2uni_test("t+m+y", "\u0f4f\u0fa8\u0fb1");
        ewts2uni_test("t+y", "\u0f4f\u0fb1");
        ewts2uni_test("t+r+n", "\u0f4f\u0fb2\u0fa3");
        ewts2uni_test("t+s", "\u0f4f\u0fb6");
        ewts2uni_test("t+s+th", "\u0f4f\u0fb6\u0fa0");
        ewts2uni_test("t+s+n", "\u0f4f\u0fb6\u0fa3");
        ewts2uni_test("t+s+n+y", "\u0f4f\u0fb6\u0fa3\u0fb1");
        ewts2uni_test("t+s+m", "\u0f4f\u0fb6\u0fa8");
        ewts2uni_test("t+s+m+y", "\u0f4f\u0fb6\u0fa8\u0fb1");
        ewts2uni_test("t+s+y", "\u0f4f\u0fb6\u0fb1");
        ewts2uni_test("t+s+r", "\u0f4f\u0fb6\u0fb2");
        ewts2uni_test("t+s+w", "\u0f4f\u0fb6\u0fad");
        ewts2uni_test("t+r+y", "\u0f4f\u0fb2\u0fb1");
        ewts2uni_test("t+w+y", "\u0f4f\u0fad\u0fb1");
        ewts2uni_test("t+k+Sh", "\u0f4f\u0f90\u0fb5");  // TODO(DLC)[EWTS->Tibetan]: 0fb9 is what tibwn.ini has

        ewts2uni_test("th+y", "\u0f50\u0fb1");
        ewts2uni_test("th+w", "\u0f50\u0fad");
        ewts2uni_test("d+g", "\u0f51\u0f92");
        ewts2uni_test("d+g+y", "\u0f51\u0f92\u0fb1");
        ewts2uni_test("d+g+r", "\u0f51\u0f92\u0fb2");
        ewts2uni_test("d+g+h", "\u0f51\u0f92\u0fb7");
        ewts2uni_test("d+g+h+r", "\u0f51\u0f92\u0fb7\u0fb2");
        ewts2uni_test("d+dz", "\u0f51\u0fab");
        ewts2uni_test("d+d", "\u0f51\u0fa1");
        ewts2uni_test("d+d+y", "\u0f51\u0fa1\u0fb1");
        ewts2uni_test("d+d+r", "\u0f51\u0fa1\u0fb2");
        ewts2uni_test("d+d+w", "\u0f51\u0fa1\u0fad");
        ewts2uni_test("d+d+h", "\u0f51\u0fa1\u0fb7");
        ewts2uni_test("d+d+h+n", "\u0f51\u0fa1\u0fb7\u0fa3");
        ewts2uni_test("d+d+h+y", "\u0f51\u0fa1\u0fb7\u0fb1");
        ewts2uni_test("d+d+h+r", "\u0f51\u0fa1\u0fb7\u0fb2");
        ewts2uni_test("d+d+h+w", "\u0f51\u0fa1\u0fb7\u0fad");
        ewts2uni_test("d+n", "\u0f51\u0fa3");
        ewts2uni_test("d+b", "\u0f51\u0fa6");
        ewts2uni_test("d+b+r", "\u0f51\u0fa6\u0fb2");
        ewts2uni_test("d+b+h", "\u0f51\u0fa6\u0fb7");
        ewts2uni_test("d+b+h+y", "\u0f51\u0fa6\u0fb7\u0fb1");
        ewts2uni_test("d+b+h+r", "\u0f51\u0fa6\u0fb7\u0fb2");
        ewts2uni_test("d+m", "\u0f51\u0fa8");
        ewts2uni_test("d+y", "\u0f51\u0fb1");
        ewts2uni_test("d+r+y", "\u0f51\u0fb2\u0fb1");
        ewts2uni_test("d+w+y", "\u0f51\u0fad\u0fb1");
        ewts2uni_test("d+h", "\u0F51\u0fb7");  // TODO(DLC)[EWTS->Tibetan]: 0f52 is what tibwn.ini has
        ewts2uni_test("d+h+n", "\u0f51\u0fb7\u0fa3");  // TODO(DLC)[EWTS->Tibetan]: 0f52 is what tibwn.ini has
        ewts2uni_test("d+h+n+y", "\u0f51\u0fb7\u0fa3\u0fb1");  // TODO(DLC)[EWTS->Tibetan]: 0f52 is what tibwn.ini has
        ewts2uni_test("d+h+m", "\u0f51\u0fb7\u0fa8");  // TODO(DLC)[EWTS->Tibetan]: 0f52 is what tibwn.ini has
        ewts2uni_test("d+h+y", "\u0f51\u0fb7\u0fb1");  // TODO(DLC)[EWTS->Tibetan]: 0f52 is what tibwn.ini has
        ewts2uni_test("d+h+r", "\u0f51\u0fb7\u0fb2");  // TODO(DLC)[EWTS->Tibetan]: 0f52 is what tibwn.ini has
        ewts2uni_test("d+h+r+y", "\u0f51\u0fb7\u0fb2\u0fb1");  // TODO(DLC)[EWTS->Tibetan]: 0f52 is what tibwn.ini has
        ewts2uni_test("d+h+w", "\u0f51\u0fb7\u0fad");  // TODO(DLC)[EWTS->Tibetan]: 0f52 is what tibwn.ini has
        ewts2uni_test("n+k", "\u0f53\u0f90");
        ewts2uni_test("n+k+t", "\u0f53\u0f90\u0f9f");
        ewts2uni_test("n+g+h", "\u0f53\u0f92\u0fb7");
        ewts2uni_test("n+ng", "\u0f53\u0f94");
        ewts2uni_test("n+dz", "\u0f53\u0fab");
        ewts2uni_test("n+dz+y", "\u0f53\u0fab\u0fb1");
        ewts2uni_test("n+D", "\u0f53\u0f9c");
        ewts2uni_test("n+t", "\u0f53\u0f9f");
        ewts2uni_test("n+t+y", "\u0f53\u0f9f\u0fb1");
        ewts2uni_test("n+t+r", "\u0f53\u0f9f\u0fb2");
        ewts2uni_test("n+t+r+y", "\u0f53\u0f9f\u0fb2\u0fb1");
        ewts2uni_test("n+t+w", "\u0f53\u0f9f\u0fad");
        ewts2uni_test("n+t+s", "\u0f53\u0f9f\u0fb6");
        ewts2uni_test("n+th", "\u0f53\u0fa0");
        ewts2uni_test("n+d", "\u0f53\u0fa1");
        ewts2uni_test("n+d+d", "\u0f53\u0fa1\u0fa1");
        ewts2uni_test("n+d+d+r", "\u0f53\u0fa1\u0fa1\u0fb2");
        ewts2uni_test("n+d+y", "\u0f53\u0fa1\u0fb1");
        ewts2uni_test("n+d+r", "\u0f53\u0fa1\u0fb2");
        ewts2uni_test("n+d+h", "\u0f53\u0fa1\u0fb7");
        ewts2uni_test("n+d+h+r", "\u0f53\u0fa1\u0fb7\u0fb2");
        ewts2uni_test("n+d+h+y", "\u0f53\u0fa1\u0fb7\u0fb1");
        ewts2uni_test("n+n", "\u0f53\u0fa3");
        ewts2uni_test("n+n+y", "\u0f53\u0fa3\u0fb1");
        ewts2uni_test("n+p", "\u0f53\u0fa4");
        ewts2uni_test("n+p+r", "\u0f53\u0fa4\u0fb2");
        ewts2uni_test("n+ph", "\u0f53\u0fa5");
        ewts2uni_test("n+m", "\u0f53\u0fa8");
        ewts2uni_test("n+b+h+y", "\u0f53\u0fa6\u0fb7\u0fb1");
        ewts2uni_test("n+ts", "\u0f53\u0fa9");
        ewts2uni_test("n+y", "\u0f53\u0fb1");
        ewts2uni_test("n+r", "\u0f53\u0fb2");
        ewts2uni_test("n+w", "\u0f53\u0fad");
        ewts2uni_test("n+w+y", "\u0f53\u0fad\u0fb1");
        ewts2uni_test("n+s", "\u0f53\u0fb6");
        ewts2uni_test("n+s+y", "\u0f53\u0fb6\u0fb1");
        ewts2uni_test("n+h", "\u0f53\u0fb7");
        ewts2uni_test("n+h+r", "\u0f53\u0fb7\u0fb2");
        ewts2uni_test("p+t", "\u0f54\u0f9f");
        ewts2uni_test("p+t+y", "\u0f54\u0f9f\u0fb1");
        ewts2uni_test("p+t+r+y", "\u0f54\u0f9f\u0fb2\u0fb1");
        ewts2uni_test("p+d", "\u0f54\u0fa1");
        ewts2uni_test("p+n", "\u0f54\u0fa3");
        ewts2uni_test("p+n+y", "\u0f54\u0fa3\u0fb1");
        ewts2uni_test("p+p", "\u0f54\u0fa4");
        ewts2uni_test("p+m", "\u0f54\u0fa8");
        ewts2uni_test("p+l", "\u0f54\u0fb3");
        ewts2uni_test("p+w", "\u0f54\u0fad");
        ewts2uni_test("p+s", "\u0f54\u0fb6");
        ewts2uni_test("p+s+n+y", "\u0f54\u0fb6\u0fa3\u0fb1");
        ewts2uni_test("p+s+w", "\u0f54\u0fb6\u0fad");
        ewts2uni_test("p+s+y", "\u0f54\u0fb6\u0fb1");
        ewts2uni_test("b+g+h", "\u0f56\u0f92\u0fb7");
        ewts2uni_test("b+dz", "\u0f56\u0fab");
        ewts2uni_test("b+d", "\u0f56\u0fa1");
        ewts2uni_test("b+d+dz", "\u0f56\u0fa1\u0fab");
        ewts2uni_test("b+d+h", "\u0f56\u0fa1\u0fb7");
        ewts2uni_test("b+d+h+w", "\u0f56\u0fa1\u0fb7\u0fad");
        ewts2uni_test("b+t", "\u0f56\u0f9f");
        ewts2uni_test("b+n", "\u0f56\u0fa3");
        ewts2uni_test("b+b", "\u0f56\u0fa6");
        ewts2uni_test("b+b+h", "\u0f56\u0fa6\u0fb7");
        ewts2uni_test("b+b+h+y", "\u0f56\u0fa6\u0fb7\u0fb1");
        ewts2uni_test("b+m", "\u0f56\u0fa8");
        ewts2uni_test("b+h", "\u0F56\u0fb7");  // TODO(DLC)[EWTS->Tibetan]: 0f57 is what tibwn.ini has
        ewts2uni_test("b+h+N", "\u0f56\u0fb7\u0f9e");  // TODO(DLC)[EWTS->Tibetan]: 0f57 is what tibwn.ini has
        ewts2uni_test("b+h+n", "\u0f56\u0fb7\u0fa3");  // TODO(DLC)[EWTS->Tibetan]: 0f57 is what tibwn.ini has
        ewts2uni_test("b+h+m", "\u0f56\u0fb7\u0fa8");  // TODO(DLC)[EWTS->Tibetan]: 0f57 is what tibwn.ini has
        ewts2uni_test("b+h+y", "\u0f56\u0fb7\u0fb1");  // TODO(DLC)[EWTS->Tibetan]: 0f57 is what tibwn.ini has
        ewts2uni_test("b+h+r", "\u0f56\u0fb7\u0fb2");  // TODO(DLC)[EWTS->Tibetan]: 0f57 is what tibwn.ini has
        ewts2uni_test("b+h+w", "\u0f56\u0fb7\u0fad");  // TODO(DLC)[EWTS->Tibetan]: 0f57 is what tibwn.ini has
        ewts2uni_test("m+ny", "\u0f58\u0f99");
        ewts2uni_test("m+N", "\u0f58\u0f9e");
        ewts2uni_test("m+n", "\u0f58\u0fa3");
        ewts2uni_test("m+n+y", "\u0f58\u0fa3\u0fb1");
        ewts2uni_test("m+p", "\u0f58\u0fa4");
        ewts2uni_test("m+p+r", "\u0f58\u0fa4\u0fb2");
        ewts2uni_test("m+ph", "\u0f58\u0fa5");
        ewts2uni_test("m+b", "\u0f58\u0fa6");
        ewts2uni_test("m+b+h", "\u0f58\u0fa6\u0fb7");
        ewts2uni_test("m+b+h+y", "\u0f58\u0fa6\u0fb7\u0fb1");
        ewts2uni_test("m+m", "\u0f58\u0fa8");
        ewts2uni_test("m+l", "\u0f58\u0fb3");
        ewts2uni_test("m+w", "\u0f58\u0fad");
        ewts2uni_test("m+s", "\u0f58\u0fb6");
        ewts2uni_test("m+h", "\u0f58\u0fb7");
        ewts2uni_test("y+Y", "\u0f61\u0fbb");
        ewts2uni_test("y+r", "\u0f61\u0fb2");
        ewts2uni_test("y+w", "\u0f61\u0fad");
        ewts2uni_test("y+s", "\u0f61\u0fb6");
        ewts2uni_test("r+kh", "\u0f62\u0f91");
        ewts2uni_test("r+g+h", "\u0f62\u0f92\u0fb7");
        ewts2uni_test("r+g+h+y", "\u0f62\u0f92\u0fb7\u0fb1");
        ewts2uni_test("r+ts+y", "\u0f62\u0fa9\u0fb1");
        ewts2uni_test("r+tsh", "\u0f62\u0faa");
        ewts2uni_test("r+dz+ny", "\u0f62\u0fab\u0f99");
        ewts2uni_test("r+dz+y", "\u0f62\u0fab\u0fb1");
        ewts2uni_test("r+T", "\u0f62\u0f9a");
        ewts2uni_test("r+Th", "\u0f62\u0f9b");
        ewts2uni_test("r+D", "\u0f62\u0f9c");
        ewts2uni_test("r+N", "\u0f62\u0f9e");
        ewts2uni_test("r+t+w", "\u0f62\u0f9f\u0fad");
        ewts2uni_test("r+t+t", "\u0f62\u0f9f\u0f9f");
        ewts2uni_test("r+t+s", "\u0f62\u0f9f\u0fb6");
        ewts2uni_test("r+t+s+n", "\u0f62\u0f9f\u0fb6\u0fa3");
        ewts2uni_test("r+t+s+n+y", "\u0f62\u0f9f\u0fb6\u0fa3\u0fb1");
        ewts2uni_test("r+th", "\u0f62\u0fa0");
        ewts2uni_test("r+th+y", "\u0f62\u0fa0\u0fb1");
        ewts2uni_test("r+d+d+h", "\u0f62\u0fa1\u0fa1\u0fb7");  // TODO(DLC)[EWTS->Tibetan]: 0fa2 is what tibwn.ini has
        ewts2uni_test("r+d+d+h+y", "\u0f62\u0fa1\u0fa1\u0fb7\u0fb1");  // TODO(DLC)[EWTS->Tibetan]: 0fa2 is what tibwn.ini has
        ewts2uni_test("r+d+y", "\u0f62\u0fa1\u0fb1");
        ewts2uni_test("r+d+h", "\u0f62\u0fa1\u0fb7");  // TODO(DLC)[EWTS->Tibetan]: 0fa2 is what tibwn.ini has
        ewts2uni_test("r+d+h+m", "\u0f62\u0fa1\u0fb7\u0fa8");  // TODO(DLC)[EWTS->Tibetan]: 0fa2 is what tibwn.ini has
        ewts2uni_test("r+d+h+y", "\u0f62\u0fa1\u0fb7\u0fb1");  // TODO(DLC)[EWTS->Tibetan]: 0fa2 is what tibwn.ini has
        ewts2uni_test("r+d+h+r", "\u0f62\u0fa1\u0fb7\u0fb2");  // TODO(DLC)[EWTS->Tibetan]: 0fa2 is what tibwn.ini has
        ewts2uni_test("r+p", "\u0f62\u0fa4");
        ewts2uni_test("r+b+p", "\u0f62\u0fa6\u0fa4");
        ewts2uni_test("r+b+b", "\u0f62\u0fa6\u0fa6");
        ewts2uni_test("r+b+h", "\u0f62\u0fa6\u0fb7");
        ewts2uni_test("r+m+m", "\u0f62\u0fa8\u0fa8");

        ewts2uni_test("r+m+ma", "\u0f62\u0fa8\u0fa8");
        
        assert_EWTS_error("kSha");
        assert_EWTS_error("kka");
        assert_EWTS_error("kkha");
        assert_EWTS_error("knga");
        assert_EWTS_error("ktsa");
        assert_EWTS_error("kta");
        assert_EWTS_error("ktya");
        assert_EWTS_error("ktra");
        assert_EWTS_error("ktrya");
        assert_EWTS_error("ktwa");
        assert_EWTS_error("ktha");
        assert_EWTS_error("kthya");
        assert_EWTS_error("kNa");
        assert_EWTS_error("kna");
        assert_EWTS_error("knya");
        assert_EWTS_error("kpha");
        assert_EWTS_error("kma");
        assert_EWTS_error("kmya");
        assert_EWTS_error("krya");
        assert_EWTS_error("kwya");
        assert_EWTS_error("ksha");
        assert_EWTS_error("ksa");
        assert_EWTS_error("ksna");
        assert_EWTS_error("ksma");
        assert_EWTS_error("ksya");
        assert_EWTS_error("kswa");
        assert_EWTS_error("khkha");
        assert_EWTS_error("khna");
        assert_EWTS_error("khla");
        assert_EWTS_error("gga");
        assert_EWTS_error("ggha");
        special_case("gnya");
        special_case("gda");
        assert_EWTS_error("gdha");
        assert_EWTS_error("gdhya");
        assert_EWTS_error("gdhwa");
        special_case("gna");
        special_case("gnya");
        assert_EWTS_error("gpa");
        assert_EWTS_error("gbha");
        assert_EWTS_error("gbhya");
        assert_EWTS_error("gma");
        assert_EWTS_error("gmya");
        assert_EWTS_error("grya");
        assert_EWTS_error("gha");
        assert_EWTS_error("ghgha");
        assert_EWTS_error("ghnya");
        assert_EWTS_error("ghna");
        assert_EWTS_error("ghnya");
        assert_EWTS_error("ghma");
        assert_EWTS_error("ghla");
        assert_EWTS_error("ghya");
        assert_EWTS_error("ghra");
        assert_EWTS_error("ghwa");
        assert_EWTS_error("ngka");
        assert_EWTS_error("ngkta");
        assert_EWTS_error("ngktya");
        assert_EWTS_error("ngkya");
        assert_EWTS_error("ngkha");
        assert_EWTS_error("ngkhya");
        assert_EWTS_error("ngga");
        assert_EWTS_error("nggra");
        assert_EWTS_error("nggya");
        assert_EWTS_error("nggha");
        assert_EWTS_error("ngghya");
        assert_EWTS_error("ngghra");
        assert_EWTS_error("ngnga");
        assert_EWTS_error("ngta");
        assert_EWTS_error("ngna");
        assert_EWTS_error("ngma");
        assert_EWTS_error("ngya");
        assert_EWTS_error("ngla");
        assert_EWTS_error("ngsha");
        assert_EWTS_error("ngha");
        assert_EWTS_error("ngkSha");
        assert_EWTS_error("ngkShwa");
        assert_EWTS_error("ngkShya");
        assert_EWTS_error("tstsa");
        assert_EWTS_error("tstsha");
        assert_EWTS_error("tstshwa");
        assert_EWTS_error("tstshra");
        assert_EWTS_error("tsnya");
        assert_EWTS_error("tsnya");
        assert_EWTS_error("tsma");
        assert_EWTS_error("tsya");
        assert_EWTS_error("tsra");
        assert_EWTS_error("tsla");
        assert_EWTS_error("tshya");
        assert_EWTS_error("tshtha");
        assert_EWTS_error("tshtsha");
        assert_EWTS_error("tshya");
        assert_EWTS_error("tshra");
        assert_EWTS_error("tshla");
        assert_EWTS_error("dzdza");
        assert_EWTS_error("dzdznya");
        assert_EWTS_error("dzdzwa");
        assert_EWTS_error("dzdzha");
        assert_EWTS_error("dzhdzha");
        assert_EWTS_error("dznya");
        assert_EWTS_error("dznyya");
        assert_EWTS_error("dzna");
        assert_EWTS_error("dznwa");
        assert_EWTS_error("dzma");
        assert_EWTS_error("dzya");
        assert_EWTS_error("dzra");
        assert_EWTS_error("dzwa");
        assert_EWTS_error("dzha");
        assert_EWTS_error("dzhya");
        assert_EWTS_error("dzhra");
        assert_EWTS_error("dzhla");
        assert_EWTS_error("dzhwa");
        assert_EWTS_error("nytsa");
        assert_EWTS_error("nytsma");
        assert_EWTS_error("nytsya");
        assert_EWTS_error("nytsha");
        assert_EWTS_error("nydza");
        assert_EWTS_error("nydzya");
        assert_EWTS_error("nydzha");
        assert_EWTS_error("nynya");
        assert_EWTS_error("nypa");
        assert_EWTS_error("nypha");
        assert_EWTS_error("nyya");
        assert_EWTS_error("nyra");
        assert_EWTS_error("nyla");
        assert_EWTS_error("nysha");
        assert_EWTS_error("Tka");
        assert_EWTS_error("TTa");
        assert_EWTS_error("TTha");
        assert_EWTS_error("Tna");
        assert_EWTS_error("Tpa");
        assert_EWTS_error("Tma");
        assert_EWTS_error("Tya");
        assert_EWTS_error("Twa");
        assert_EWTS_error("Tsa");
        assert_EWTS_error("Thya");
        assert_EWTS_error("Thra");
        assert_EWTS_error("Dga");
        assert_EWTS_error("Dgya");
        assert_EWTS_error("Dgha");
        assert_EWTS_error("Dghra");
        assert_EWTS_error("DDa");
        assert_EWTS_error("DDha");
        assert_EWTS_error("DDhya");
        assert_EWTS_error("Dna");
        assert_EWTS_error("Dma");
        assert_EWTS_error("Dya");
        assert_EWTS_error("Dra");
        assert_EWTS_error("Dwa");
        assert_EWTS_error("Dha");
        assert_EWTS_error("DhDha");
        assert_EWTS_error("Dhma");
        assert_EWTS_error("Dhya");
        assert_EWTS_error("Dhra");
        assert_EWTS_error("Dhwa");
        assert_EWTS_error("NTa");
        assert_EWTS_error("NTha");
        assert_EWTS_error("NDa");
        assert_EWTS_error("NDYa");
        assert_EWTS_error("NDra");
        assert_EWTS_error("NDRya");
        assert_EWTS_error("NDha");
        assert_EWTS_error("NNa");
        assert_EWTS_error("Ndra");
        assert_EWTS_error("Nma");
        assert_EWTS_error("Nya");
        assert_EWTS_error("Nwa");
        assert_EWTS_error("tka");
        assert_EWTS_error("tkra");
        assert_EWTS_error("tkwa");
        assert_EWTS_error("tksa");
        assert_EWTS_error("tga");
        assert_EWTS_error("tnya");
        assert_EWTS_error("tTha");
        assert_EWTS_error("tta");
        assert_EWTS_error("ttya");
        assert_EWTS_error("ttra");
        assert_EWTS_error("ttwa");
        assert_EWTS_error("ttha");
        assert_EWTS_error("tthya");
        assert_EWTS_error("tna");
        assert_EWTS_error("tnya");
        assert_EWTS_error("tpa");
        assert_EWTS_error("tpra");
        assert_EWTS_error("tpha");
        assert_EWTS_error("tma");
        assert_EWTS_error("tmya");
        assert_EWTS_error("tya");
        assert_EWTS_error("trna");
        special_case("tsa");
        assert_EWTS_error("tstha");
        assert_EWTS_error("tsna");
        assert_EWTS_error("tsnya");
        assert_EWTS_error("tsma");
        assert_EWTS_error("tsmya");
        assert_EWTS_error("tsya");
        assert_EWTS_error("tsra");
        special_case("tswa");
        assert_EWTS_error("trya");
        assert_EWTS_error("twya");
        assert_EWTS_error("tkSha");
        assert_EWTS_error("thya");
        assert_EWTS_error("thwa");
        special_case("dga");
        special_case("dgya");
        special_case("dgra");
        assert_EWTS_error("dgha");
        assert_EWTS_error("dghra");
        assert_EWTS_error("ddza");
        assert_EWTS_error("dda");
        assert_EWTS_error("ddya");
        assert_EWTS_error("ddra");
        assert_EWTS_error("ddwa");
        assert_EWTS_error("ddha");
        assert_EWTS_error("ddhna");
        assert_EWTS_error("ddhya");
        assert_EWTS_error("ddhra");
        assert_EWTS_error("ddhwa");
        assert_EWTS_error("dna");
        special_case("dba");
        special_case("dbra");
        assert_EWTS_error("dbha");
        assert_EWTS_error("dbhya");
        assert_EWTS_error("dbhra");
        special_case("dma");
        assert_EWTS_error("dya");
        assert_EWTS_error("drya");
        assert_EWTS_error("dwya");
        assert_EWTS_error("dha");
        assert_EWTS_error("dhna");
        assert_EWTS_error("dhnya");
        assert_EWTS_error("dhma");
        assert_EWTS_error("dhya");
        assert_EWTS_error("dhra");
        assert_EWTS_error("dhrya");
        assert_EWTS_error("dhwa");
        assert_EWTS_error("nka");
        assert_EWTS_error("nkta");
        assert_EWTS_error("ngha");
        assert_EWTS_error("nnga");
        assert_EWTS_error("ndza");
        assert_EWTS_error("ndzya");
        assert_EWTS_error("nDa");
        assert_EWTS_error("nta");
        assert_EWTS_error("ntya");
        assert_EWTS_error("ntra");
        assert_EWTS_error("ntrya");
        assert_EWTS_error("ntwa");
        assert_EWTS_error("ntsa");
        assert_EWTS_error("ntha");
        assert_EWTS_error("nda");
        assert_EWTS_error("ndda");
        assert_EWTS_error("nddra");
        assert_EWTS_error("ndya");
        assert_EWTS_error("ndra");
        assert_EWTS_error("ndha");
        assert_EWTS_error("ndhra");
        assert_EWTS_error("ndhya");
        assert_EWTS_error("nna");
        assert_EWTS_error("nnya");
        assert_EWTS_error("npa");
        assert_EWTS_error("npra");
        assert_EWTS_error("npha");
        assert_EWTS_error("nma");
        assert_EWTS_error("nbhya");
        assert_EWTS_error("ntsa");
        special_case("nya");
        assert_EWTS_error("nra");
        assert_EWTS_error("nwa");
        assert_EWTS_error("nwya");
        assert_EWTS_error("nsa");
        assert_EWTS_error("nsya");
        assert_EWTS_error("nha");
        assert_EWTS_error("nhra");
        assert_EWTS_error("pta");
        assert_EWTS_error("ptya");
        assert_EWTS_error("ptrya");
        assert_EWTS_error("pda");
        assert_EWTS_error("pna");
        assert_EWTS_error("pnya");
        assert_EWTS_error("ppa");
        assert_EWTS_error("pma");
        assert_EWTS_error("pla");
        assert_EWTS_error("pwa");
        assert_EWTS_error("psa");
        assert_EWTS_error("psnya");
        assert_EWTS_error("pswa");
        assert_EWTS_error("psya");
        assert_EWTS_error("bgha");
        assert_EWTS_error("bdza");
        special_case("bda");
        assert_EWTS_error("bddza");
        assert_EWTS_error("bdha");
        assert_EWTS_error("bdhwa");
        special_case("bta");
        assert_EWTS_error("bna");
        assert_EWTS_error("bba");
        assert_EWTS_error("bbha");
        assert_EWTS_error("bbhya");
        assert_EWTS_error("bma");
        assert_EWTS_error("bha");
        assert_EWTS_error("bhNa");
        assert_EWTS_error("bhna");
        assert_EWTS_error("bhma");
        assert_EWTS_error("bhya");
        assert_EWTS_error("bhra");
        assert_EWTS_error("bhwa");
        special_case("mnya");
        assert_EWTS_error("mNa");
        special_case("mna");
        special_case("mnya");
        assert_EWTS_error("mpa");
        assert_EWTS_error("mpra");
        assert_EWTS_error("mpha");
        assert_EWTS_error("mba");
        assert_EWTS_error("mbha");
        assert_EWTS_error("mbhya");
        assert_EWTS_error("mma");
        assert_EWTS_error("mla");
        assert_EWTS_error("mwa");
        assert_EWTS_error("msa");
        assert_EWTS_error("mha");
        assert_EWTS_error("yYa");
        assert_EWTS_error("yra");
        assert_EWTS_error("ywa");
        assert_EWTS_error("ysa");
        assert_EWTS_error("rkha");
        assert_EWTS_error("rgha");
        assert_EWTS_error("rghya");
        assert_EWTS_error("rtsya");
        assert_EWTS_error("rtsha");
        assert_EWTS_error("rdznya");
        assert_EWTS_error("rdzya");
        assert_EWTS_error("rTa");
        assert_EWTS_error("rTha");
        assert_EWTS_error("rDa");
        assert_EWTS_error("rNa");
        assert_EWTS_error("rtwa");
        assert_EWTS_error("rtta");
        special_case("rtsa");
        assert_EWTS_error("rtsna");
        assert_EWTS_error("rtsnya");
        assert_EWTS_error("rtha");
        assert_EWTS_error("rthya");
        assert_EWTS_error("rddha");
        assert_EWTS_error("rddhya");
        assert_EWTS_error("rdya");
        assert_EWTS_error("rdha");
        assert_EWTS_error("rdhma");
        assert_EWTS_error("rdhya");
        assert_EWTS_error("rdhra");
        assert_EWTS_error("rpa");
        assert_EWTS_error("rbpa");
        assert_EWTS_error("rbba");
        assert_EWTS_error("rbha");
        assert_EWTS_error("rmma");
    }
}
