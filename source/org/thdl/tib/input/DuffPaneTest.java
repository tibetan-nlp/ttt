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

package org.thdl.tib.input;


/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.tib.input.Duffpane} at the unit level to see
 * that the various keyboards work as expected.  Tests the TMW->EWTS
 * conversion too, which {@link org.thdl.tib.text.ttt.PackageTest}
 * also tests.  */
public class DuffPaneTest extends DuffPaneTestBase {
    /**
     * Plain vanilla constructor for DuffPaneTest.
     * @param arg0
     */
    public DuffPaneTest(String arg0) {
        super(arg0);
    }
    /** Invokes a text UI and runs all this class's tests. */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(DuffPaneTest.class);
    }

    /** Tests that we tibwn.ini is consistent.  If t+r+ne actually
        produces a na-ro due to an error in tibwn.ini (this one
        happened -- see the errata for the Tibetan! 5.1 docs), this
        will catch it. */
    public void testTibwnIni() {
        enableEWTSKeyboard();
        /* <?Input:Tibetan?>: */
        e("ka"); e("k", "ka"); e("ki"); e("ku"); e("ke"); e("ko"); e("kU"); e("kaM"); e("kau"); e("kai"); e("kI"); e("k-i");
        e("kha"); e("kh", "kha"); e("khi"); e("khu"); e("khe"); e("kho"); e("khU"); e("khaM"); e("khau"); e("khai"); e("khI"); e("kh-i");
        e("ga"); e("g", "ga"); e("gi"); e("gu"); e("ge"); e("go"); e("gU"); e("gaM"); e("gau"); e("gai"); e("gI"); e("g-i");
        e("nga"); e("ng", "nga"); e("ngi"); e("ngu"); e("nge"); e("ngo"); e("ngU"); e("ngaM"); e("ngau"); e("ngai"); e("ngI"); e("ng-i");
        e("ca"); e("c", "ca"); e("ci"); e("cu"); e("ce"); e("co"); e("cU"); e("caM"); e("cau"); e("cai"); e("cI"); e("c-i");
        e("cha"); e("ch", "cha"); e("chi"); e("chu"); e("che"); e("cho"); e("chU"); e("chaM"); e("chau"); e("chai"); e("chI"); e("ch-i");
        e("ja"); e("j", "ja"); e("ji"); e("ju"); e("je"); e("jo"); e("jU"); e("jaM"); e("jau"); e("jai"); e("jI"); e("j-i");
        e("nya"); e("ny", "nya"); e("nyi"); e("nyu"); e("nye"); e("nyo"); e("nyU"); e("nyaM"); e("nyau"); e("nyai"); e("nyI"); e("ny-i");
        e("ta"); e("t", "ta"); e("ti"); e("tu"); e("te"); e("to"); e("tU"); e("taM"); e("tau"); e("tai"); e("tI"); e("t-i");
        e("tha"); e("th", "tha"); e("thi"); e("thu"); e("the"); e("tho"); e("thU"); e("thaM"); e("thau"); e("thai"); e("thI"); e("th-i");
        e("da"); e("d", "da"); e("di"); e("du"); e("de"); e("do"); e("dU"); e("daM"); e("dau"); e("dai"); e("dI"); e("d-i");
        e("na"); e("n", "na"); e("ni"); e("nu"); e("ne"); e("no"); e("nU"); e("naM"); e("nau"); e("nai"); e("nI"); e("n-i");
        e("pa"); e("p", "pa"); e("pi"); e("pu"); e("pe"); e("po"); e("pU"); e("paM"); e("pau"); e("pai"); e("pI"); e("p-i");
        e("pha"); e("ph", "pha"); e("phi"); e("phu"); e("phe"); e("pho"); e("phU"); e("phaM"); e("phau"); e("phai"); e("phI"); e("ph-i");
        e("ba"); e("b", "ba"); e("bi"); e("bu"); e("be"); e("bo"); e("bU"); e("baM"); e("bau"); e("bai"); e("bI"); e("b-i");
        e("ma"); e("m", "ma"); e("mi"); e("mu"); e("me"); e("mo"); e("mU"); e("maM"); e("mau"); e("mai"); e("mI"); e("m-i");
        e("tsa"); e("ts", "tsa"); e("tsi"); e("tsu"); e("tse"); e("tso"); e("tsU"); e("tsaM"); e("tsau"); e("tsai"); e("tsI"); e("ts-i");
        e("tsha"); e("tsh", "tsha"); e("tshi"); e("tshu"); e("tshe"); e("tsho"); e("tshU"); e("tshaM"); e("tshau"); e("tshai"); e("tshI"); e("tsh-i");
        e("dza"); e("dz", "dza"); e("dzi"); e("dzu"); e("dze"); e("dzo"); e("dzU"); e("dzaM"); e("dzau"); e("dzai"); e("dzI"); e("dz-i");
        e("wa"); e("w", "wa"); e("wi"); e("wu"); e("we"); e("wo"); e("wU"); e("waM"); e("wau"); e("wai"); e("wI"); e("w-i");
        e("zha"); e("zh", "zha"); e("zhi"); e("zhu"); e("zhe"); e("zho"); e("zhU"); e("zhaM"); e("zhau"); e("zhai"); e("zhI"); e("zh-i");
        e("za"); e("z", "za"); e("zi"); e("zu"); e("ze"); e("zo"); e("zU"); e("zaM"); e("zau"); e("zai"); e("zI"); e("z-i");
        e("'a"); e("'", "'a"); e("'i"); e("'u"); e("'e"); e("'o"); e("'U"); e("'aM"); e("'au"); e("'ai"); e("'I"); e("'-i");
        e("ya"); e("y", "ya"); e("yi"); e("yu"); e("ye"); e("yo"); e("yU"); e("yaM"); e("yau"); e("yai"); e("yI"); e("y-i");
        e("ra"); e("r", "ra"); e("ri"); e("ru"); e("re"); e("ro"); e("rU"); e("raM"); e("rau"); e("rai"); e("rI"); e("r-i");
        e("la"); e("l", "la"); e("li"); e("lu"); e("le"); e("lo"); e("lU"); e("laM"); e("lau"); e("lai"); e("lI"); e("l-i");
        e("sha"); e("sh", "sha"); e("shi"); e("shu"); e("she"); e("sho"); e("shU"); e("shaM"); e("shau"); e("shai"); e("shI"); e("sh-i");
        e("sa"); e("s", "sa"); e("si"); e("su"); e("se"); e("so"); e("sU"); e("saM"); e("sau"); e("sai"); e("sI"); e("s-i");
        e("ha"); e("h", "ha"); e("hi"); e("hu"); e("he"); e("ho"); e("hU"); e("haM"); e("hau"); e("hai"); e("hI"); e("h-i");

        // SPECIAL CASE: achen:
        e("a"); e("i"); e("u"); e("e"); e("o"); e("U");
        e("aM");
        e("au"); e("ai"); e("I"); e("-i");

        e("rka"); e("rk", "rka"); e("rki"); e("rku"); e("rke"); e("rko"); e("rkU"); e("rkaM"); e("rkau"); e("rkai"); e("rkI"); e("rk-i");
        e("rga"); e("rg", "rga"); e("rgi"); e("rgu"); e("rge"); e("rgo"); e("rgU"); e("rgaM"); e("rgau"); e("rgai"); e("rgI"); e("rg-i");
        e("rnga"); e("rng", "rnga"); e("rngi"); e("rngu"); e("rnge"); e("rngo"); e("rngU"); e("rngaM"); e("rngau"); e("rngai"); e("rngI"); e("rng-i");
        e("rja"); e("rj", "rja"); e("rji"); e("rju"); e("rje"); e("rjo"); e("rjU"); e("rjaM"); e("rjau"); e("rjai"); e("rjI"); e("rj-i");
        e("rnya"); e("rny", "rnya"); e("rnyi"); e("rnyu"); e("rnye"); e("rnyo"); e("rnyU"); e("rnyaM"); e("rnyau"); e("rnyai"); e("rnyI"); e("rny-i");
        e("rta"); e("rt", "rta"); e("rti"); e("rtu"); e("rte"); e("rto"); e("rtU"); e("rtaM"); e("rtau"); e("rtai"); e("rtI"); e("rt-i");
        e("rda"); e("rd", "rda"); e("rdi"); e("rdu"); e("rde"); e("rdo"); e("rdU"); e("rdaM"); e("rdau"); e("rdai"); e("rdI"); e("rd-i");
        e("rna"); e("rn", "rna"); e("rni"); e("rnu"); e("rne"); e("rno"); e("rnU"); e("rnaM"); e("rnau"); e("rnai"); e("rnI"); e("rn-i");
        e("rba"); e("rb", "rba"); e("rbi"); e("rbu"); e("rbe"); e("rbo"); e("rbU"); e("rbaM"); e("rbau"); e("rbai"); e("rbI"); e("rb-i");
        e("rma"); e("rm", "rma"); e("rmi"); e("rmu"); e("rme"); e("rmo"); e("rmU"); e("rmaM"); e("rmau"); e("rmai"); e("rmI"); e("rm-i");
        e("rtsa"); e("rts", "rtsa"); e("rtsi"); e("rtsu"); e("rtse"); e("rtso"); e("rtsU"); e("rtsaM"); e("rtsau"); e("rtsai"); e("rtsI"); e("rts-i");
        e("rdza"); e("rdz", "rdza"); e("rdzi"); e("rdzu"); e("rdze"); e("rdzo"); e("rdzU"); e("rdzaM"); e("rdzau"); e("rdzai"); e("rdzI"); e("rdz-i");
        e("lka"); e("lk", "lka"); e("lki"); e("lku"); e("lke"); e("lko"); e("lkU"); e("lkaM"); e("lkau"); e("lkai"); e("lkI"); e("lk-i");
        e("lga"); e("lg", "lga"); e("lgi"); e("lgu"); e("lge"); e("lgo"); e("lgU"); e("lgaM"); e("lgau"); e("lgai"); e("lgI"); e("lg-i");
        e("lnga"); e("lng", "lnga"); e("lngi"); e("lngu"); e("lnge"); e("lngo"); e("lngU"); e("lngaM"); e("lngau"); e("lngai"); e("lngI"); e("lng-i");
        e("lca"); e("lc", "lca"); e("lci"); e("lcu"); e("lce"); e("lco"); e("lcU"); e("lcaM"); e("lcau"); e("lcai"); e("lcI"); e("lc-i");
        e("lja"); e("lj", "lja"); e("lji"); e("lju"); e("lje"); e("ljo"); e("ljU"); e("ljaM"); e("ljau"); e("ljai"); e("ljI"); e("lj-i");
        e("lta"); e("lt", "lta"); e("lti"); e("ltu"); e("lte"); e("lto"); e("ltU"); e("ltaM"); e("ltau"); e("ltai"); e("ltI"); e("lt-i");
        e("lda"); e("ld", "lda"); e("ldi"); e("ldu"); e("lde"); e("ldo"); e("ldU"); e("ldaM"); e("ldau"); e("ldai"); e("ldI"); e("ld-i");
        e("lpa"); e("lp", "lpa"); e("lpi"); e("lpu"); e("lpe"); e("lpo"); e("lpU"); e("lpaM"); e("lpau"); e("lpai"); e("lpI"); e("lp-i");
        e("lba"); e("lb", "lba"); e("lbi"); e("lbu"); e("lbe"); e("lbo"); e("lbU"); e("lbaM"); e("lbau"); e("lbai"); e("lbI"); e("lb-i");
        e("lha"); e("lh", "lha"); e("lhi"); e("lhu"); e("lhe"); e("lho"); e("lhU"); e("lhaM"); e("lhau"); e("lhai"); e("lhI"); e("lh-i");
        e("ska"); e("sk", "ska"); e("ski"); e("sku"); e("ske"); e("sko"); e("skU"); e("skaM"); e("skau"); e("skai"); e("skI"); e("sk-i");
        e("sga"); e("sg", "sga"); e("sgi"); e("sgu"); e("sge"); e("sgo"); e("sgU"); e("sgaM"); e("sgau"); e("sgai"); e("sgI"); e("sg-i");
        e("snga"); e("sng", "snga"); e("sngi"); e("sngu"); e("snge"); e("sngo"); e("sngU"); e("sngaM"); e("sngau"); e("sngai"); e("sngI"); e("sng-i");
        e("snya"); e("sny", "snya"); e("snyi"); e("snyu"); e("snye"); e("snyo"); e("snyU"); e("snyaM"); e("snyau"); e("snyai"); e("snyI"); e("sny-i");
        e("sta"); e("st", "sta"); e("sti"); e("stu"); e("ste"); e("sto"); e("stU"); e("staM"); e("stau"); e("stai"); e("stI"); e("st-i");
        e("sda"); e("sd", "sda"); e("sdi"); e("sdu"); e("sde"); e("sdo"); e("sdU"); e("sdaM"); e("sdau"); e("sdai"); e("sdI"); e("sd-i");
        e("sna"); e("sn", "sna"); e("sni"); e("snu"); e("sne"); e("sno"); e("snU"); e("snaM"); e("snau"); e("snai"); e("snI"); e("sn-i");
        e("spa"); e("sp", "spa"); e("spi"); e("spu"); e("spe"); e("spo"); e("spU"); e("spaM"); e("spau"); e("spai"); e("spI"); e("sp-i");
        e("sba"); e("sb", "sba"); e("sbi"); e("sbu"); e("sbe"); e("sbo"); e("sbU"); e("sbaM"); e("sbau"); e("sbai"); e("sbI"); e("sb-i");
        e("sma"); e("sm", "sma"); e("smi"); e("smu"); e("sme"); e("smo"); e("smU"); e("smaM"); e("smau"); e("smai"); e("smI"); e("sm-i");
        e("stsa"); e("sts", "stsa"); e("stsi"); e("stsu"); e("stse"); e("stso"); e("stsU"); e("stsaM"); e("stsau"); e("stsai"); e("stsI"); e("sts-i");
        e("kwa"); e("kw", "kwa"); e("kwi"); e("kwu"); e("kwe"); e("kwo"); e("kwU"); e("kwaM"); e("kwau"); e("kwai"); e("kwI"); e("kw-i");
        e("khwa"); e("khw", "khwa"); e("khwi"); e("khwu"); e("khwe"); e("khwo"); e("khwU"); e("khwaM"); e("khwau"); e("khwai"); e("khwI"); e("khw-i");
        e("gwa"); e("gw", "gwa"); e("gwi"); e("gwu"); e("gwe"); e("gwo"); e("gwU"); e("gwaM"); e("gwau"); e("gwai"); e("gwI"); e("gw-i");
        e("cwa"); e("cw", "cwa"); e("cwi"); e("cwu"); e("cwe"); e("cwo"); e("cwU"); e("cwaM"); e("cwau"); e("cwai"); e("cwI"); e("cw-i");
        e("nywa"); e("nyw", "nywa"); e("nywi"); e("nywu"); e("nywe"); e("nywo"); e("nywU"); e("nywaM"); e("nywau"); e("nywai"); e("nywI"); e("nyw-i");
        e("twa"); e("tw", "twa"); e("twi"); e("twu"); e("twe"); e("two"); e("twU"); e("twaM"); e("twau"); e("twai"); e("twI"); e("tw-i");
        e("dwa"); e("dw", "dwa"); e("dwi"); e("dwu"); e("dwe"); e("dwo"); e("dwU"); e("dwaM"); e("dwau"); e("dwai"); e("dwI"); e("dw-i");
        e("tswa"); e("tsw", "tswa"); e("tswi"); e("tswu"); e("tswe"); e("tswo"); e("tswU"); e("tswaM"); e("tswau"); e("tswai"); e("tswI"); e("tsw-i");
        e("tshwa"); e("tshw", "tshwa"); e("tshwi"); e("tshwu"); e("tshwe"); e("tshwo"); e("tshwU"); e("tshwaM"); e("tshwau"); e("tshwai"); e("tshwI"); e("tshw-i");
        e("zhwa"); e("zhw", "zhwa"); e("zhwi"); e("zhwu"); e("zhwe"); e("zhwo"); e("zhwU"); e("zhwaM"); e("zhwau"); e("zhwai"); e("zhwI"); e("zhw-i");
        e("zwa"); e("zw", "zwa"); e("zwi"); e("zwu"); e("zwe"); e("zwo"); e("zwU"); e("zwaM"); e("zwau"); e("zwai"); e("zwI"); e("zw-i");
        e("rwa"); e("rw", "rwa"); e("rwi"); e("rwu"); e("rwe"); e("rwo"); e("rwU"); e("rwaM"); e("rwau"); e("rwai"); e("rwI"); e("rw-i");
        e("shwa"); e("shw", "shwa"); e("shwi"); e("shwu"); e("shwe"); e("shwo"); e("shwU"); e("shwaM"); e("shwau"); e("shwai"); e("shwI"); e("shw-i");
        e("swa"); e("sw", "swa"); e("swi"); e("swu"); e("swe"); e("swo"); e("swU"); e("swaM"); e("swau"); e("swai"); e("swI"); e("sw-i");
        e("hwa"); e("hw", "hwa"); e("hwi"); e("hwu"); e("hwe"); e("hwo"); e("hwU"); e("hwaM"); e("hwau"); e("hwai"); e("hwI"); e("hw-i");
        e("kya"); e("ky", "kya"); e("kyi"); e("kyu"); e("kye"); e("kyo"); e("kyU"); e("kyaM"); e("kyau"); e("kyai"); e("kyI"); e("ky-i");
        e("khya"); e("khy", "khya"); e("khyi"); e("khyu"); e("khye"); e("khyo"); e("khyU"); e("khyaM"); e("khyau"); e("khyai"); e("khyI"); e("khy-i");
        e("gya"); e("gy", "gya"); e("gyi"); e("gyu"); e("gye"); e("gyo"); e("gyU"); e("gyaM"); e("gyau"); e("gyai"); e("gyI"); e("gy-i");
        e("pya"); e("py", "pya"); e("pyi"); e("pyu"); e("pye"); e("pyo"); e("pyU"); e("pyaM"); e("pyau"); e("pyai"); e("pyI"); e("py-i");
        e("phya"); e("phy", "phya"); e("phyi"); e("phyu"); e("phye"); e("phyo"); e("phyU"); e("phyaM"); e("phyau"); e("phyai"); e("phyI"); e("phy-i");
        e("bya"); e("by", "bya"); e("byi"); e("byu"); e("bye"); e("byo"); e("byU"); e("byaM"); e("byau"); e("byai"); e("byI"); e("by-i");
        e("mya"); e("my", "mya"); e("myi"); e("myu"); e("mye"); e("myo"); e("myU"); e("myaM"); e("myau"); e("myai"); e("myI"); e("my-i");
        e("kra"); e("kr", "kra"); e("kri"); e("kru"); e("kre"); e("kro"); e("krU"); e("kraM"); e("krau"); e("krai"); e("krI"); e("kr-i");
        e("khra"); e("khr", "khra"); e("khri"); e("khru"); e("khre"); e("khro"); e("khrU"); e("khraM"); e("khrau"); e("khrai"); e("khrI"); e("khr-i");
        e("gra"); e("gr", "gra"); e("gri"); e("gru"); e("gre"); e("gro"); e("grU"); e("graM"); e("grau"); e("grai"); e("grI"); e("gr-i");
        e("tra"); e("tr", "tra"); e("tri"); e("tru"); e("tre"); e("tro"); e("trU"); e("traM"); e("trau"); e("trai"); e("trI"); e("tr-i");
        e("thra"); e("thr", "thra"); e("thri"); e("thru"); e("thre"); e("thro"); e("thrU"); e("thraM"); e("thrau"); e("thrai"); e("thrI"); e("thr-i");
        e("dra"); e("dr", "dra"); e("dri"); e("dru"); e("dre"); e("dro"); e("drU"); e("draM"); e("drau"); e("drai"); e("drI"); e("dr-i");
        e("pra"); e("pr", "pra"); e("pri"); e("pru"); e("pre"); e("pro"); e("prU"); e("praM"); e("prau"); e("prai"); e("prI"); e("pr-i");
        e("phra"); e("phr", "phra"); e("phri"); e("phru"); e("phre"); e("phro"); e("phrU"); e("phraM"); e("phrau"); e("phrai"); e("phrI"); e("phr-i");
        e("bra"); e("br", "bra"); e("bri"); e("bru"); e("bre"); e("bro"); e("brU"); e("braM"); e("brau"); e("brai"); e("brI"); e("br-i");
        e("mra"); e("mr", "mra"); e("mri"); e("mru"); e("mre"); e("mro"); e("mrU"); e("mraM"); e("mrau"); e("mrai"); e("mrI"); e("mr-i");
        e("shra"); e("shr", "shra"); e("shri"); e("shru"); e("shre"); e("shro"); e("shrU"); e("shraM"); e("shrau"); e("shrai"); e("shrI"); e("shr-i");
        e("sra"); e("sr", "sra"); e("sri"); e("sru"); e("sre"); e("sro"); e("srU"); e("sraM"); e("srau"); e("srai"); e("srI"); e("sr-i");
        e("hra"); e("hr", "hra"); e("hri"); e("hru"); e("hre"); e("hro"); e("hrU"); e("hraM"); e("hrau"); e("hrai"); e("hrI"); e("hr-i");
        e("kla"); e("kl", "kla"); e("kli"); e("klu"); e("kle"); e("klo"); e("klU"); e("klaM"); e("klau"); e("klai"); e("klI"); e("kl-i");
        e("gla"); e("gl", "gla"); e("gli"); e("glu"); e("gle"); e("glo"); e("glU"); e("glaM"); e("glau"); e("glai"); e("glI"); e("gl-i");
        e("bla"); e("bl", "bla"); e("bli"); e("blu"); e("ble"); e("blo"); e("blU"); e("blaM"); e("blau"); e("blai"); e("blI"); e("bl-i");
        e("zla"); e("zl", "zla"); e("zli"); e("zlu"); e("zle"); e("zlo"); e("zlU"); e("zlaM"); e("zlau"); e("zlai"); e("zlI"); e("zl-i");
        e("rla"); e("rl", "rla"); e("rli"); e("rlu"); e("rle"); e("rlo"); e("rlU"); e("rlaM"); e("rlau"); e("rlai"); e("rlI"); e("rl-i");
        e("sla"); e("sl", "sla"); e("sli"); e("slu"); e("sle"); e("slo"); e("slU"); e("slaM"); e("slau"); e("slai"); e("slI"); e("sl-i");
        e("rkya"); e("rky", "rkya"); e("rkyi"); e("rkyu"); e("rkye"); e("rkyo"); e("rkyU"); e("rkyaM"); e("rkyau"); e("rkyai"); e("rkyI"); e("rky-i");
        e("rgya"); e("rgy", "rgya"); e("rgyi"); e("rgyu"); e("rgye"); e("rgyo"); e("rgyU"); e("rgyaM"); e("rgyau"); e("rgyai"); e("rgyI"); e("rgy-i");
        e("rmya"); e("rmy", "rmya"); e("rmyi"); e("rmyu"); e("rmye"); e("rmyo"); e("rmyU"); e("rmyaM"); e("rmyau"); e("rmyai"); e("rmyI"); e("rmy-i");
        e("rgwa"); e("rgw", "rgwa"); e("rgwi"); e("rgwu"); e("rgwe"); e("rgwo"); e("rgwU"); e("rgwaM"); e("rgwau"); e("rgwai"); e("rgwI"); e("rgw-i");
        e("rtswa"); e("rtsw", "rtswa"); e("rtswi"); e("rtswu"); e("rtswe"); e("rtswo"); e("rtswU"); e("rtswaM"); e("rtswau"); e("rtswai"); e("rtswI"); e("rtsw-i");
        e("skya"); e("sky", "skya"); e("skyi"); e("skyu"); e("skye"); e("skyo"); e("skyU"); e("skyaM"); e("skyau"); e("skyai"); e("skyI"); e("sky-i");
        e("sgya"); e("sgy", "sgya"); e("sgyi"); e("sgyu"); e("sgye"); e("sgyo"); e("sgyU"); e("sgyaM"); e("sgyau"); e("sgyai"); e("sgyI"); e("sgy-i");
        e("spya"); e("spy", "spya"); e("spyi"); e("spyu"); e("spye"); e("spyo"); e("spyU"); e("spyaM"); e("spyau"); e("spyai"); e("spyI"); e("spy-i");
        e("sbya"); e("sby", "sbya"); e("sbyi"); e("sbyu"); e("sbye"); e("sbyo"); e("sbyU"); e("sbyaM"); e("sbyau"); e("sbyai"); e("sbyI"); e("sby-i");
        e("smya"); e("smy", "smya"); e("smyi"); e("smyu"); e("smye"); e("smyo"); e("smyU"); e("smyaM"); e("smyau"); e("smyai"); e("smyI"); e("smy-i");
        e("skra"); e("skr", "skra"); e("skri"); e("skru"); e("skre"); e("skro"); e("skrU"); e("skraM"); e("skrau"); e("skrai"); e("skrI"); e("skr-i");
        e("sgra"); e("sgr", "sgra"); e("sgri"); e("sgru"); e("sgre"); e("sgro"); e("sgrU"); e("sgraM"); e("sgrau"); e("sgrai"); e("sgrI"); e("sgr-i");
        e("snra"); e("snr", "snra"); e("snri"); e("snru"); e("snre"); e("snro"); e("snrU"); e("snraM"); e("snrau"); e("snrai"); e("snrI"); e("snr-i");
        e("spra"); e("spr", "spra"); e("spri"); e("spru"); e("spre"); e("spro"); e("sprU"); e("spraM"); e("sprau"); e("sprai"); e("sprI"); e("spr-i");
        e("sbra"); e("sbr", "sbra"); e("sbri"); e("sbru"); e("sbre"); e("sbro"); e("sbrU"); e("sbraM"); e("sbrau"); e("sbrai"); e("sbrI"); e("sbr-i");
        e("smra"); e("smr", "smra"); e("smri"); e("smru"); e("smre"); e("smro"); e("smrU"); e("smraM"); e("smrau"); e("smrai"); e("smrI"); e("smr-i");
        e("grwa"); e("grw", "grwa"); e("grwi"); e("grwu"); e("grwe"); e("grwo"); e("grwU"); e("grwaM"); e("grwau"); e("grwai"); e("grwI"); e("grw-i");
        e("drwa"); e("drw", "drwa"); e("drwi"); e("drwu"); e("drwe"); e("drwo"); e("drwU"); e("drwaM"); e("drwau"); e("drwai"); e("drwI"); e("drw-i");
        e("phywa"); e("phyw", "phywa"); e("phywi"); e("phywu"); e("phywe"); e("phywo"); e("phywU"); e("phywaM"); e("phywau"); e("phywai"); e("phywI"); e("phyw-i");

        /* DLC FIXME: when bug 847460 is fixed:
        e("r+ka"); e("r+k", "r+ka"); e("r+ki"); e("r+ku"); e("r+ke"); e("r+ko"); e("r+kU"); e("r+kaM"); e("r+kau"); e("r+kai"); e("r+kI"); e("r+k-i");
        e("r+ga"); e("r+g", "r+ga"); e("r+gi"); e("r+gu"); e("r+ge"); e("r+go"); e("r+gU"); e("r+gaM"); e("r+gau"); e("r+gai"); e("r+gI"); e("r+g-i");
        e("r+nga"); e("r+ng", "r+nga"); e("r+ngi"); e("r+ngu"); e("r+nge"); e("r+ngo"); e("r+ngU"); e("r+ngaM"); e("r+ngau"); e("r+ngai"); e("r+ngI"); e("r+ng-i");
        e("r+ja"); e("r+j", "r+ja"); e("r+ji"); e("r+ju"); e("r+je"); e("r+jo"); e("r+jU"); e("r+jaM"); e("r+jau"); e("r+jai"); e("r+jI"); e("r+j-i");
        e("r+nya"); e("r+ny", "r+nya"); e("r+nyi"); e("r+nyu"); e("r+nye"); e("r+nyo"); e("r+nyU"); e("r+nyaM"); e("r+nyau"); e("r+nyai"); e("r+nyI"); e("r+ny-i");
        e("r+ta"); e("r+t", "r+ta"); e("r+ti"); e("r+tu"); e("r+te"); e("r+to"); e("r+tU"); e("r+taM"); e("r+tau"); e("r+tai"); e("r+tI"); e("r+t-i");
        e("r+da"); e("r+d", "r+da"); e("r+di"); e("r+du"); e("r+de"); e("r+do"); e("r+dU"); e("r+daM"); e("r+dau"); e("r+dai"); e("r+dI"); e("r+d-i");
        e("r+na"); e("r+n", "r+na"); e("r+ni"); e("r+nu"); e("r+ne"); e("r+no"); e("r+nU"); e("r+naM"); e("r+nau"); e("r+nai"); e("r+nI"); e("r+n-i");
        e("r+ba"); e("r+b", "r+ba"); e("r+bi"); e("r+bu"); e("r+be"); e("r+bo"); e("r+bU"); e("r+baM"); e("r+bau"); e("r+bai"); e("r+bI"); e("r+b-i");
        e("r+ma"); e("r+m", "r+ma"); e("r+mi"); e("r+mu"); e("r+me"); e("r+mo"); e("r+mU"); e("r+maM"); e("r+mau"); e("r+mai"); e("r+mI"); e("r+m-i");
        e("r+tsa"); e("r+ts", "r+tsa"); e("r+tsi"); e("r+tsu"); e("r+tse"); e("r+tso"); e("r+tsU"); e("r+tsaM"); e("r+tsau"); e("r+tsai"); e("r+tsI"); e("r+ts-i");
        e("r+dza"); e("r+dz", "r+dza"); e("r+dzi"); e("r+dzu"); e("r+dze"); e("r+dzo"); e("r+dzU"); e("r+dzaM"); e("r+dzau"); e("r+dzai"); e("r+dzI"); e("r+dz-i");
        e("l+ka"); e("l+k", "l+ka"); e("l+ki"); e("l+ku"); e("l+ke"); e("l+ko"); e("l+kU"); e("l+kaM"); e("l+kau"); e("l+kai"); e("l+kI"); e("l+k-i");
        e("l+ga"); e("l+g", "l+ga"); e("l+gi"); e("l+gu"); e("l+ge"); e("l+go"); e("l+gU"); e("l+gaM"); e("l+gau"); e("l+gai"); e("l+gI"); e("l+g-i");
        e("l+nga"); e("l+ng", "l+nga"); e("l+ngi"); e("l+ngu"); e("l+nge"); e("l+ngo"); e("l+ngU"); e("l+ngaM"); e("l+ngau"); e("l+ngai"); e("l+ngI"); e("l+ng-i");
        e("l+ca"); e("l+c", "l+ca"); e("l+ci"); e("l+cu"); e("l+ce"); e("l+co"); e("l+cU"); e("l+caM"); e("l+cau"); e("l+cai"); e("l+cI"); e("l+c-i");
        e("l+ja"); e("l+j", "l+ja"); e("l+ji"); e("l+ju"); e("l+je"); e("l+jo"); e("l+jU"); e("l+jaM"); e("l+jau"); e("l+jai"); e("l+jI"); e("l+j-i");
        e("l+ta"); e("l+t", "l+ta"); e("l+ti"); e("l+tu"); e("l+te"); e("l+to"); e("l+tU"); e("l+taM"); e("l+tau"); e("l+tai"); e("l+tI"); e("l+t-i");
        e("l+da"); e("l+d", "l+da"); e("l+di"); e("l+du"); e("l+de"); e("l+do"); e("l+dU"); e("l+daM"); e("l+dau"); e("l+dai"); e("l+dI"); e("l+d-i");
        e("l+pa"); e("l+p", "l+pa"); e("l+pi"); e("l+pu"); e("l+pe"); e("l+po"); e("l+pU"); e("l+paM"); e("l+pau"); e("l+pai"); e("l+pI"); e("l+p-i");
        e("l+ba"); e("l+b", "l+ba"); e("l+bi"); e("l+bu"); e("l+be"); e("l+bo"); e("l+bU"); e("l+baM"); e("l+bau"); e("l+bai"); e("l+bI"); e("l+b-i");
        e("l+ha"); e("l+h", "l+ha"); e("l+hi"); e("l+hu"); e("l+he"); e("l+ho"); e("l+hU"); e("l+haM"); e("l+hau"); e("l+hai"); e("l+hI"); e("l+h-i");
        e("s+ka"); e("s+k", "s+ka"); e("s+ki"); e("s+ku"); e("s+ke"); e("s+ko"); e("s+kU"); e("s+kaM"); e("s+kau"); e("s+kai"); e("s+kI"); e("s+k-i");
        e("s+ga"); e("s+g", "s+ga"); e("s+gi"); e("s+gu"); e("s+ge"); e("s+go"); e("s+gU"); e("s+gaM"); e("s+gau"); e("s+gai"); e("s+gI"); e("s+g-i");
        e("s+nga"); e("s+ng", "s+nga"); e("s+ngi"); e("s+ngu"); e("s+nge"); e("s+ngo"); e("s+ngU"); e("s+ngaM"); e("s+ngau"); e("s+ngai"); e("s+ngI"); e("s+ng-i");
        e("s+nya"); e("s+ny", "s+nya"); e("s+nyi"); e("s+nyu"); e("s+nye"); e("s+nyo"); e("s+nyU"); e("s+nyaM"); e("s+nyau"); e("s+nyai"); e("s+nyI"); e("s+ny-i");
        e("s+ta"); e("s+t", "s+ta"); e("s+ti"); e("s+tu"); e("s+te"); e("s+to"); e("s+tU"); e("s+taM"); e("s+tau"); e("s+tai"); e("s+tI"); e("s+t-i");
        e("s+da"); e("s+d", "s+da"); e("s+di"); e("s+du"); e("s+de"); e("s+do"); e("s+dU"); e("s+daM"); e("s+dau"); e("s+dai"); e("s+dI"); e("s+d-i");
        e("s+na"); e("s+n", "s+na"); e("s+ni"); e("s+nu"); e("s+ne"); e("s+no"); e("s+nU"); e("s+naM"); e("s+nau"); e("s+nai"); e("s+nI"); e("s+n-i");
        e("s+pa"); e("s+p", "s+pa"); e("s+pi"); e("s+pu"); e("s+pe"); e("s+po"); e("s+pU"); e("s+paM"); e("s+pau"); e("s+pai"); e("s+pI"); e("s+p-i");
        e("s+ba"); e("s+b", "s+ba"); e("s+bi"); e("s+bu"); e("s+be"); e("s+bo"); e("s+bU"); e("s+baM"); e("s+bau"); e("s+bai"); e("s+bI"); e("s+b-i");
        e("s+ma"); e("s+m", "s+ma"); e("s+mi"); e("s+mu"); e("s+me"); e("s+mo"); e("s+mU"); e("s+maM"); e("s+mau"); e("s+mai"); e("s+mI"); e("s+m-i");
        e("s+tsa"); e("s+ts", "s+tsa"); e("s+tsi"); e("s+tsu"); e("s+tse"); e("s+tso"); e("s+tsU"); e("s+tsaM"); e("s+tsau"); e("s+tsai"); e("s+tsI"); e("s+ts-i");
        e("k+wa"); e("k+w", "k+wa"); e("k+wi"); e("k+wu"); e("k+we"); e("k+wo"); e("k+wU"); e("k+waM"); e("k+wau"); e("k+wai"); e("k+wI"); e("k+w-i");
        e("kh+wa"); e("kh+w", "kh+wa"); e("kh+wi"); e("kh+wu"); e("kh+we"); e("kh+wo"); e("kh+wU"); e("kh+waM"); e("kh+wau"); e("kh+wai"); e("kh+wI"); e("kh+w-i");
        e("g+wa"); e("g+w", "g+wa"); e("g+wi"); e("g+wu"); e("g+we"); e("g+wo"); e("g+wU"); e("g+waM"); e("g+wau"); e("g+wai"); e("g+wI"); e("g+w-i");
        e("c+wa"); e("c+w", "c+wa"); e("c+wi"); e("c+wu"); e("c+we"); e("c+wo"); e("c+wU"); e("c+waM"); e("c+wau"); e("c+wai"); e("c+wI"); e("c+w-i");
        e("ny+wa"); e("ny+w", "ny+wa"); e("ny+wi"); e("ny+wu"); e("ny+we"); e("ny+wo"); e("ny+wU"); e("ny+waM"); e("ny+wau"); e("ny+wai"); e("ny+wI"); e("ny+w-i");
        e("t+wa"); e("t+w", "t+wa"); e("t+wi"); e("t+wu"); e("t+we"); e("t+wo"); e("t+wU"); e("t+waM"); e("t+wau"); e("t+wai"); e("t+wI"); e("t+w-i");
        e("d+wa"); e("d+w", "d+wa"); e("d+wi"); e("d+wu"); e("d+we"); e("d+wo"); e("d+wU"); e("d+waM"); e("d+wau"); e("d+wai"); e("d+wI"); e("d+w-i");
        e("ts+wa"); e("ts+w", "ts+wa"); e("ts+wi"); e("ts+wu"); e("ts+we"); e("ts+wo"); e("ts+wU"); e("ts+waM"); e("ts+wau"); e("ts+wai"); e("ts+wI"); e("ts+w-i");
        e("tsh+wa"); e("tsh+w", "tsh+wa"); e("tsh+wi"); e("tsh+wu"); e("tsh+we"); e("tsh+wo"); e("tsh+wU"); e("tsh+waM"); e("tsh+wau"); e("tsh+wai"); e("tsh+wI"); e("tsh+w-i");
        e("zh+wa"); e("zh+w", "zh+wa"); e("zh+wi"); e("zh+wu"); e("zh+we"); e("zh+wo"); e("zh+wU"); e("zh+waM"); e("zh+wau"); e("zh+wai"); e("zh+wI"); e("zh+w-i");
        e("z+wa"); e("z+w", "z+wa"); e("z+wi"); e("z+wu"); e("z+we"); e("z+wo"); e("z+wU"); e("z+waM"); e("z+wau"); e("z+wai"); e("z+wI"); e("z+w-i");
        e("r+wa"); e("r+w", "r+wa"); e("r+wi"); e("r+wu"); e("r+we"); e("r+wo"); e("r+wU"); e("r+waM"); e("r+wau"); e("r+wai"); e("r+wI"); e("r+w-i");
        e("sh+wa"); e("sh+w", "sh+wa"); e("sh+wi"); e("sh+wu"); e("sh+we"); e("sh+wo"); e("sh+wU"); e("sh+waM"); e("sh+wau"); e("sh+wai"); e("sh+wI"); e("sh+w-i");
        e("s+wa"); e("s+w", "s+wa"); e("s+wi"); e("s+wu"); e("s+we"); e("s+wo"); e("s+wU"); e("s+waM"); e("s+wau"); e("s+wai"); e("s+wI"); e("s+w-i");
        e("h+wa"); e("h+w", "h+wa"); e("h+wi"); e("h+wu"); e("h+we"); e("h+wo"); e("h+wU"); e("h+waM"); e("h+wau"); e("h+wai"); e("h+wI"); e("h+w-i");
        e("k+ya"); e("k+y", "k+ya"); e("k+yi"); e("k+yu"); e("k+ye"); e("k+yo"); e("k+yU"); e("k+yaM"); e("k+yau"); e("k+yai"); e("k+yI"); e("k+y-i");
        e("kh+ya"); e("kh+y", "kh+ya"); e("kh+yi"); e("kh+yu"); e("kh+ye"); e("kh+yo"); e("kh+yU"); e("kh+yaM"); e("kh+yau"); e("kh+yai"); e("kh+yI"); e("kh+y-i");
        e("g+ya"); e("g+y", "g+ya"); e("g+yi"); e("g+yu"); e("g+ye"); e("g+yo"); e("g+yU"); e("g+yaM"); e("g+yau"); e("g+yai"); e("g+yI"); e("g+y-i");
        e("p+ya"); e("p+y", "p+ya"); e("p+yi"); e("p+yu"); e("p+ye"); e("p+yo"); e("p+yU"); e("p+yaM"); e("p+yau"); e("p+yai"); e("p+yI"); e("p+y-i");
        e("ph+ya"); e("ph+y", "ph+ya"); e("ph+yi"); e("ph+yu"); e("ph+ye"); e("ph+yo"); e("ph+yU"); e("ph+yaM"); e("ph+yau"); e("ph+yai"); e("ph+yI"); e("ph+y-i");
        e("b+ya"); e("b+y", "b+ya"); e("b+yi"); e("b+yu"); e("b+ye"); e("b+yo"); e("b+yU"); e("b+yaM"); e("b+yau"); e("b+yai"); e("b+yI"); e("b+y-i");
        e("m+ya"); e("m+y", "m+ya"); e("m+yi"); e("m+yu"); e("m+ye"); e("m+yo"); e("m+yU"); e("m+yaM"); e("m+yau"); e("m+yai"); e("m+yI"); e("m+y-i");
        e("k+ra"); e("k+r", "k+ra"); e("k+ri"); e("k+ru"); e("k+re"); e("k+ro"); e("k+rU"); e("k+raM"); e("k+rau"); e("k+rai"); e("k+rI"); e("k+r-i");
        e("kh+ra"); e("kh+r", "kh+ra"); e("kh+ri"); e("kh+ru"); e("kh+re"); e("kh+ro"); e("kh+rU"); e("kh+raM"); e("kh+rau"); e("kh+rai"); e("kh+rI"); e("kh+r-i");
        e("g+ra"); e("g+r", "g+ra"); e("g+ri"); e("g+ru"); e("g+re"); e("g+ro"); e("g+rU"); e("g+raM"); e("g+rau"); e("g+rai"); e("g+rI"); e("g+r-i");
        e("t+ra"); e("t+r", "t+ra"); e("t+ri"); e("t+ru"); e("t+re"); e("t+ro"); e("t+rU"); e("t+raM"); e("t+rau"); e("t+rai"); e("t+rI"); e("t+r-i");
        e("th+ra"); e("th+r", "th+ra"); e("th+ri"); e("th+ru"); e("th+re"); e("th+ro"); e("th+rU"); e("th+raM"); e("th+rau"); e("th+rai"); e("th+rI"); e("th+r-i");
        e("d+ra"); e("d+r", "d+ra"); e("d+ri"); e("d+ru"); e("d+re"); e("d+ro"); e("d+rU"); e("d+raM"); e("d+rau"); e("d+rai"); e("d+rI"); e("d+r-i");
        e("p+ra"); e("p+r", "p+ra"); e("p+ri"); e("p+ru"); e("p+re"); e("p+ro"); e("p+rU"); e("p+raM"); e("p+rau"); e("p+rai"); e("p+rI"); e("p+r-i");
        e("ph+ra"); e("ph+r", "ph+ra"); e("ph+ri"); e("ph+ru"); e("ph+re"); e("ph+ro"); e("ph+rU"); e("ph+raM"); e("ph+rau"); e("ph+rai"); e("ph+rI"); e("ph+r-i");
        e("b+ra"); e("b+r", "b+ra"); e("b+ri"); e("b+ru"); e("b+re"); e("b+ro"); e("b+rU"); e("b+raM"); e("b+rau"); e("b+rai"); e("b+rI"); e("b+r-i");
        e("m+ra"); e("m+r", "m+ra"); e("m+ri"); e("m+ru"); e("m+re"); e("m+ro"); e("m+rU"); e("m+raM"); e("m+rau"); e("m+rai"); e("m+rI"); e("m+r-i");
        e("sh+ra"); e("sh+r", "sh+ra"); e("sh+ri"); e("sh+ru"); e("sh+re"); e("sh+ro"); e("sh+rU"); e("sh+raM"); e("sh+rau"); e("sh+rai"); e("sh+rI"); e("sh+r-i");
        e("s+ra"); e("s+r", "s+ra"); e("s+ri"); e("s+ru"); e("s+re"); e("s+ro"); e("s+rU"); e("s+raM"); e("s+rau"); e("s+rai"); e("s+rI"); e("s+r-i");
        e("h+ra"); e("h+r", "h+ra"); e("h+ri"); e("h+ru"); e("h+re"); e("h+ro"); e("h+rU"); e("h+raM"); e("h+rau"); e("h+rai"); e("h+rI"); e("h+r-i");
        e("k+la"); e("k+l", "k+la"); e("k+li"); e("k+lu"); e("k+le"); e("k+lo"); e("k+lU"); e("k+laM"); e("k+lau"); e("k+lai"); e("k+lI"); e("k+l-i");
        e("g+la"); e("g+l", "g+la"); e("g+li"); e("g+lu"); e("g+le"); e("g+lo"); e("g+lU"); e("g+laM"); e("g+lau"); e("g+lai"); e("g+lI"); e("g+l-i");
        e("b+la"); e("b+l", "b+la"); e("b+li"); e("b+lu"); e("b+le"); e("b+lo"); e("b+lU"); e("b+laM"); e("b+lau"); e("b+lai"); e("b+lI"); e("b+l-i");
        e("z+la"); e("z+l", "z+la"); e("z+li"); e("z+lu"); e("z+le"); e("z+lo"); e("z+lU"); e("z+laM"); e("z+lau"); e("z+lai"); e("z+lI"); e("z+l-i");
        e("r+la"); e("r+l", "r+la"); e("r+li"); e("r+lu"); e("r+le"); e("r+lo"); e("r+lU"); e("r+laM"); e("r+lau"); e("r+lai"); e("r+lI"); e("r+l-i");
        e("s+la"); e("s+l", "s+la"); e("s+li"); e("s+lu"); e("s+le"); e("s+lo"); e("s+lU"); e("s+laM"); e("s+lau"); e("s+lai"); e("s+lI"); e("s+l-i");
        e("r+k+ya"); e("r+k+y", "r+k+ya"); e("r+k+yi"); e("r+k+yu"); e("r+k+ye"); e("r+k+yo"); e("r+k+yU"); e("r+k+yaM"); e("r+k+yau"); e("r+k+yai"); e("r+k+yI"); e("r+k+y-i");
        e("r+g+ya"); e("r+g+y", "r+g+ya"); e("r+g+yi"); e("r+g+yu"); e("r+g+ye"); e("r+g+yo"); e("r+g+yU"); e("r+g+yaM"); e("r+g+yau"); e("r+g+yai"); e("r+g+yI"); e("r+g+y-i");
        e("r+m+ya"); e("r+m+y", "r+m+ya"); e("r+m+yi"); e("r+m+yu"); e("r+m+ye"); e("r+m+yo"); e("r+m+yU"); e("r+m+yaM"); e("r+m+yau"); e("r+m+yai"); e("r+m+yI"); e("r+m+y-i");
        e("r+g+wa"); e("r+g+w", "r+g+wa"); e("r+g+wi"); e("r+g+wu"); e("r+g+we"); e("r+g+wo"); e("r+g+wU"); e("r+g+waM"); e("r+g+wau"); e("r+g+wai"); e("r+g+wI"); e("r+g+w-i");
        e("r+ts+wa"); e("r+ts+w", "r+ts+wa"); e("r+ts+wi"); e("r+ts+wu"); e("r+ts+we"); e("r+ts+wo"); e("r+ts+wU"); e("r+ts+waM"); e("r+ts+wau"); e("r+ts+wai"); e("r+ts+wI"); e("r+ts+w-i");
        e("s+k+ya"); e("s+k+y", "s+k+ya"); e("s+k+yi"); e("s+k+yu"); e("s+k+ye"); e("s+k+yo"); e("s+k+yU"); e("s+k+yaM"); e("s+k+yau"); e("s+k+yai"); e("s+k+yI"); e("s+k+y-i");
        e("s+g+ya"); e("s+g+y", "s+g+ya"); e("s+g+yi"); e("s+g+yu"); e("s+g+ye"); e("s+g+yo"); e("s+g+yU"); e("s+g+yaM"); e("s+g+yau"); e("s+g+yai"); e("s+g+yI"); e("s+g+y-i");
        e("s+p+ya"); e("s+p+y", "s+p+ya"); e("s+p+yi"); e("s+p+yu"); e("s+p+ye"); e("s+p+yo"); e("s+p+yU"); e("s+p+yaM"); e("s+p+yau"); e("s+p+yai"); e("s+p+yI"); e("s+p+y-i");
        e("s+b+ya"); e("s+b+y", "s+b+ya"); e("s+b+yi"); e("s+b+yu"); e("s+b+ye"); e("s+b+yo"); e("s+b+yU"); e("s+b+yaM"); e("s+b+yau"); e("s+b+yai"); e("s+b+yI"); e("s+b+y-i");
        e("s+m+ya"); e("s+m+y", "s+m+ya"); e("s+m+yi"); e("s+m+yu"); e("s+m+ye"); e("s+m+yo"); e("s+m+yU"); e("s+m+yaM"); e("s+m+yau"); e("s+m+yai"); e("s+m+yI"); e("s+m+y-i");
        e("s+k+ra"); e("s+k+r", "s+k+ra"); e("s+k+ri"); e("s+k+ru"); e("s+k+re"); e("s+k+ro"); e("s+k+rU"); e("s+k+raM"); e("s+k+rau"); e("s+k+rai"); e("s+k+rI"); e("s+k+r-i");
        e("s+g+ra"); e("s+g+r", "s+g+ra"); e("s+g+ri"); e("s+g+ru"); e("s+g+re"); e("s+g+ro"); e("s+g+rU"); e("s+g+raM"); e("s+g+rau"); e("s+g+rai"); e("s+g+rI"); e("s+g+r-i");
        e("s+n+ra"); e("s+n+r", "s+n+ra"); e("s+n+ri"); e("s+n+ru"); e("s+n+re"); e("s+n+ro"); e("s+n+rU"); e("s+n+raM"); e("s+n+rau"); e("s+n+rai"); e("s+n+rI"); e("s+n+r-i");
        e("s+p+ra"); e("s+p+r", "s+p+ra"); e("s+p+ri"); e("s+p+ru"); e("s+p+re"); e("s+p+ro"); e("s+p+rU"); e("s+p+raM"); e("s+p+rau"); e("s+p+rai"); e("s+p+rI"); e("s+p+r-i");
        e("s+b+ra"); e("s+b+r", "s+b+ra"); e("s+b+ri"); e("s+b+ru"); e("s+b+re"); e("s+b+ro"); e("s+b+rU"); e("s+b+raM"); e("s+b+rau"); e("s+b+rai"); e("s+b+rI"); e("s+b+r-i");
        e("s+m+ra"); e("s+m+r", "s+m+ra"); e("s+m+ri"); e("s+m+ru"); e("s+m+re"); e("s+m+ro"); e("s+m+rU"); e("s+m+raM"); e("s+m+rau"); e("s+m+rai"); e("s+m+rI"); e("s+m+r-i");
        e("g+r+wa"); e("g+r+w", "g+r+wa"); e("g+r+wi"); e("g+r+wu"); e("g+r+we"); e("g+r+wo"); e("g+r+wU"); e("g+r+waM"); e("g+r+wau"); e("g+r+wai"); e("g+r+wI"); e("g+r+w-i");
        e("d+r+wa"); e("d+r+w", "d+r+wa"); e("d+r+wi"); e("d+r+wu"); e("d+r+we"); e("d+r+wo"); e("d+r+wU"); e("d+r+waM"); e("d+r+wau"); e("d+r+wai"); e("d+r+wI"); e("d+r+w-i");
        e("ph+y+wa"); e("ph+y+w", "ph+y+wa"); e("ph+y+wi"); e("ph+y+wu"); e("ph+y+we"); e("ph+y+wo"); e("ph+y+wU"); e("ph+y+waM"); e("ph+y+wau"); e("ph+y+wai"); e("ph+y+wI"); e("ph+y+w-i");
*/


        /* <?Input:Sanskrit?>: */
        e("fa"); e("f", "fa"); e("fi"); e("fu"); e("fe"); e("fo"); e("fU"); e("faM"); e("fau"); e("fai"); e("fI"); e("f-i");
        e("va"); e("v", "va"); e("vi"); e("vu"); e("ve"); e("vo"); e("vU"); e("vaM"); e("vau"); e("vai"); e("vI"); e("v-i");
        e("Ta"); e("T", "Ta"); e("Ti"); e("Tu"); e("Te"); e("To"); e("TU"); e("TaM"); e("Tau"); e("Tai"); e("TI"); e("T-i");
        e("Tha"); e("Th", "Tha"); e("Thi"); e("Thu"); e("The"); e("Tho"); e("ThU"); e("ThaM"); e("Thau"); e("Thai"); e("ThI"); e("Th-i");
        e("Da"); e("D", "Da"); e("Di"); e("Du"); e("De"); e("Do"); e("DU"); e("DaM"); e("Dau"); e("Dai"); e("DI"); e("D-i");
        e("Na"); e("N", "Na"); e("Ni"); e("Nu"); e("Ne"); e("No"); e("NU"); e("NaM"); e("Nau"); e("Nai"); e("NI"); e("N-i");
        e("Sha"); e("Sh", "Sha"); e("Shi"); e("Shu"); e("She"); e("Sho"); e("ShU"); e("ShaM"); e("Shau"); e("Shai"); e("ShI"); e("Sh-i");
        e("k+Sha"); e("k+Sh", "k+Sha"); e("k+Shi"); e("k+Shu"); e("k+She"); e("k+Sho"); e("k+ShU"); e("k+ShaM"); e("k+Shau"); e("k+Shai"); e("k+ShI"); e("k+Sh-i");
        e("k+ka"); e("k+k", "k+ka"); e("k+ki"); e("k+ku"); e("k+ke"); e("k+ko"); e("k+kU"); e("k+kaM"); e("k+kau"); e("k+kai"); e("k+kI"); e("k+k-i");
        e("k+kha"); e("k+kh", "k+kha"); e("k+khi"); e("k+khu"); e("k+khe"); e("k+kho"); e("k+khU"); e("k+khaM"); e("k+khau"); e("k+khai"); e("k+khI"); e("k+kh-i");
        e("k+nga"); e("k+ng", "k+nga"); e("k+ngi"); e("k+ngu"); e("k+nge"); e("k+ngo"); e("k+ngU"); e("k+ngaM"); e("k+ngau"); e("k+ngai"); e("k+ngI"); e("k+ng-i");
        e("k+tsa"); e("k+ts", "k+tsa"); e("k+tsi"); e("k+tsu"); e("k+tse"); e("k+tso"); e("k+tsU"); e("k+tsaM"); e("k+tsau"); e("k+tsai"); e("k+tsI"); e("k+ts-i");
        e("k+ta"); e("k+t", "k+ta"); e("k+ti"); e("k+tu"); e("k+te"); e("k+to"); e("k+tU"); e("k+taM"); e("k+tau"); e("k+tai"); e("k+tI"); e("k+t-i");
        e("k+t+ya"); e("k+t+y", "k+t+ya"); e("k+t+yi"); e("k+t+yu"); e("k+t+ye"); e("k+t+yo"); e("k+t+yU"); e("k+t+yaM"); e("k+t+yau"); e("k+t+yai"); e("k+t+yI"); e("k+t+y-i");
        e("k+t+ra"); e("k+t+r", "k+t+ra"); e("k+t+ri"); e("k+t+ru"); e("k+t+re"); e("k+t+ro"); e("k+t+rU"); e("k+t+raM"); e("k+t+rau"); e("k+t+rai"); e("k+t+rI"); e("k+t+r-i");
        e("k+t+r+ya"); e("k+t+r+y", "k+t+r+ya"); e("k+t+r+yi"); e("k+t+r+yu"); e("k+t+r+ye"); e("k+t+r+yo"); e("k+t+r+yU"); e("k+t+r+yaM"); e("k+t+r+yau"); e("k+t+r+yai"); e("k+t+r+yI"); e("k+t+r+y-i");
        e("k+t+wa"); e("k+t+w", "k+t+wa"); e("k+t+wi"); e("k+t+wu"); e("k+t+we"); e("k+t+wo"); e("k+t+wU"); e("k+t+waM"); e("k+t+wau"); e("k+t+wai"); e("k+t+wI"); e("k+t+w-i");
        e("k+tha"); e("k+th", "k+tha"); e("k+thi"); e("k+thu"); e("k+the"); e("k+tho"); e("k+thU"); e("k+thaM"); e("k+thau"); e("k+thai"); e("k+thI"); e("k+th-i");
        e("k+th+ya"); e("k+th+y", "k+th+ya"); e("k+th+yi"); e("k+th+yu"); e("k+th+ye"); e("k+th+yo"); e("k+th+yU"); e("k+th+yaM"); e("k+th+yau"); e("k+th+yai"); e("k+th+yI"); e("k+th+y-i");
        e("k+Na"); e("k+N", "k+Na"); e("k+Ni"); e("k+Nu"); e("k+Ne"); e("k+No"); e("k+NU"); e("k+NaM"); e("k+Nau"); e("k+Nai"); e("k+NI"); e("k+N-i");
        e("k+na"); e("k+n", "k+na"); e("k+ni"); e("k+nu"); e("k+ne"); e("k+no"); e("k+nU"); e("k+naM"); e("k+nau"); e("k+nai"); e("k+nI"); e("k+n-i");
        e("k+n+ya"); e("k+n+y", "k+n+ya"); e("k+n+yi"); e("k+n+yu"); e("k+n+ye"); e("k+n+yo"); e("k+n+yU"); e("k+n+yaM"); e("k+n+yau"); e("k+n+yai"); e("k+n+yI"); e("k+n+y-i");
        e("k+pha"); e("k+ph", "k+pha"); e("k+phi"); e("k+phu"); e("k+phe"); e("k+pho"); e("k+phU"); e("k+phaM"); e("k+phau"); e("k+phai"); e("k+phI"); e("k+ph-i");
        e("k+ma"); e("k+m", "k+ma"); e("k+mi"); e("k+mu"); e("k+me"); e("k+mo"); e("k+mU"); e("k+maM"); e("k+mau"); e("k+mai"); e("k+mI"); e("k+m-i");
        e("k+m+ya"); e("k+m+y", "k+m+ya"); e("k+m+yi"); e("k+m+yu"); e("k+m+ye"); e("k+m+yo"); e("k+m+yU"); e("k+m+yaM"); e("k+m+yau"); e("k+m+yai"); e("k+m+yI"); e("k+m+y-i");
        e("k+r+ya"); e("k+r+y", "k+r+ya"); e("k+r+yi"); e("k+r+yu"); e("k+r+ye"); e("k+r+yo"); e("k+r+yU"); e("k+r+yaM"); e("k+r+yau"); e("k+r+yai"); e("k+r+yI"); e("k+r+y-i");
        e("k+w+ya"); e("k+w+y", "k+w+ya"); e("k+w+yi"); e("k+w+yu"); e("k+w+ye"); e("k+w+yo"); e("k+w+yU"); e("k+w+yaM"); e("k+w+yau"); e("k+w+yai"); e("k+w+yI"); e("k+w+y-i");
        e("k+sha"); e("k+sh", "k+sha"); e("k+shi"); e("k+shu"); e("k+she"); e("k+sho"); e("k+shU"); e("k+shaM"); e("k+shau"); e("k+shai"); e("k+shI"); e("k+sh-i");
        e("k+sa"); e("k+s", "k+sa"); e("k+si"); e("k+su"); e("k+se"); e("k+so"); e("k+sU"); e("k+saM"); e("k+sau"); e("k+sai"); e("k+sI"); e("k+s-i");
        e("k+s+na"); e("k+s+n", "k+s+na"); e("k+s+ni"); e("k+s+nu"); e("k+s+ne"); e("k+s+no"); e("k+s+nU"); e("k+s+naM"); e("k+s+nau"); e("k+s+nai"); e("k+s+nI"); e("k+s+n-i");
        e("k+s+ma"); e("k+s+m", "k+s+ma"); e("k+s+mi"); e("k+s+mu"); e("k+s+me"); e("k+s+mo"); e("k+s+mU"); e("k+s+maM"); e("k+s+mau"); e("k+s+mai"); e("k+s+mI"); e("k+s+m-i");
        e("k+s+ya"); e("k+s+y", "k+s+ya"); e("k+s+yi"); e("k+s+yu"); e("k+s+ye"); e("k+s+yo"); e("k+s+yU"); e("k+s+yaM"); e("k+s+yau"); e("k+s+yai"); e("k+s+yI"); e("k+s+y-i");
        e("k+s+wa"); e("k+s+w", "k+s+wa"); e("k+s+wi"); e("k+s+wu"); e("k+s+we"); e("k+s+wo"); e("k+s+wU"); e("k+s+waM"); e("k+s+wau"); e("k+s+wai"); e("k+s+wI"); e("k+s+w-i");
        e("kh+kha"); e("kh+kh", "kh+kha"); e("kh+khi"); e("kh+khu"); e("kh+khe"); e("kh+kho"); e("kh+khU"); e("kh+khaM"); e("kh+khau"); e("kh+khai"); e("kh+khI"); e("kh+kh-i");
        e("kh+na"); e("kh+n", "kh+na"); e("kh+ni"); e("kh+nu"); e("kh+ne"); e("kh+no"); e("kh+nU"); e("kh+naM"); e("kh+nau"); e("kh+nai"); e("kh+nI"); e("kh+n-i");
        e("kh+la"); e("kh+l", "kh+la"); e("kh+li"); e("kh+lu"); e("kh+le"); e("kh+lo"); e("kh+lU"); e("kh+laM"); e("kh+lau"); e("kh+lai"); e("kh+lI"); e("kh+l-i");
        e("g+ga"); e("g+g", "g+ga"); e("g+gi"); e("g+gu"); e("g+ge"); e("g+go"); e("g+gU"); e("g+gaM"); e("g+gau"); e("g+gai"); e("g+gI"); e("g+g-i");
        e("g+g+ha"); e("g+g+h", "g+g+ha"); e("g+g+hi"); e("g+g+hu"); e("g+g+he"); e("g+g+ho"); e("g+g+hU"); e("g+g+haM"); e("g+g+hau"); e("g+g+hai"); e("g+g+hI"); e("g+g+h-i");
        e("g+nya"); e("g+ny", "g+nya"); e("g+nyi"); e("g+nyu"); e("g+nye"); e("g+nyo"); e("g+nyU"); e("g+nyaM"); e("g+nyau"); e("g+nyai"); e("g+nyI"); e("g+ny-i");
        e("g+da"); e("g+d", "g+da"); e("g+di"); e("g+du"); e("g+de"); e("g+do"); e("g+dU"); e("g+daM"); e("g+dau"); e("g+dai"); e("g+dI"); e("g+d-i");
        e("g+d+ha"); e("g+d+h", "g+d+ha"); e("g+d+hi"); e("g+d+hu"); e("g+d+he"); e("g+d+ho"); e("g+d+hU"); e("g+d+haM"); e("g+d+hau"); e("g+d+hai"); e("g+d+hI"); e("g+d+h-i");
        e("g+d+h+ya"); e("g+d+h+y", "g+d+h+ya"); e("g+d+h+yi"); e("g+d+h+yu"); e("g+d+h+ye"); e("g+d+h+yo"); e("g+d+h+yU"); e("g+d+h+yaM"); e("g+d+h+yau"); e("g+d+h+yai"); e("g+d+h+yI"); e("g+d+h+y-i");
        e("g+d+h+wa"); e("g+d+h+w", "g+d+h+wa"); e("g+d+h+wi"); e("g+d+h+wu"); e("g+d+h+we"); e("g+d+h+wo"); e("g+d+h+wU"); e("g+d+h+waM"); e("g+d+h+wau"); e("g+d+h+wai"); e("g+d+h+wI"); e("g+d+h+w-i");
        e("g+na"); e("g+n", "g+na"); e("g+ni"); e("g+nu"); e("g+ne"); e("g+no"); e("g+nU"); e("g+naM"); e("g+nau"); e("g+nai"); e("g+nI"); e("g+n-i");
        e("g+n+ya"); e("g+n+y", "g+n+ya"); e("g+n+yi"); e("g+n+yu"); e("g+n+ye"); e("g+n+yo"); e("g+n+yU"); e("g+n+yaM"); e("g+n+yau"); e("g+n+yai"); e("g+n+yI"); e("g+n+y-i");
        e("g+pa"); e("g+p", "g+pa"); e("g+pi"); e("g+pu"); e("g+pe"); e("g+po"); e("g+pU"); e("g+paM"); e("g+pau"); e("g+pai"); e("g+pI"); e("g+p-i");
        e("g+b+ha"); e("g+b+h", "g+b+ha"); e("g+b+hi"); e("g+b+hu"); e("g+b+he"); e("g+b+ho"); e("g+b+hU"); e("g+b+haM"); e("g+b+hau"); e("g+b+hai"); e("g+b+hI"); e("g+b+h-i");
        e("g+b+h+ya"); e("g+b+h+y", "g+b+h+ya"); e("g+b+h+yi"); e("g+b+h+yu"); e("g+b+h+ye"); e("g+b+h+yo"); e("g+b+h+yU"); e("g+b+h+yaM"); e("g+b+h+yau"); e("g+b+h+yai"); e("g+b+h+yI"); e("g+b+h+y-i");
        e("g+ma"); e("g+m", "g+ma"); e("g+mi"); e("g+mu"); e("g+me"); e("g+mo"); e("g+mU"); e("g+maM"); e("g+mau"); e("g+mai"); e("g+mI"); e("g+m-i");
        e("g+m+ya"); e("g+m+y", "g+m+ya"); e("g+m+yi"); e("g+m+yu"); e("g+m+ye"); e("g+m+yo"); e("g+m+yU"); e("g+m+yaM"); e("g+m+yau"); e("g+m+yai"); e("g+m+yI"); e("g+m+y-i");
        e("g+r+ya"); e("g+r+y", "g+r+ya"); e("g+r+yi"); e("g+r+yu"); e("g+r+ye"); e("g+r+yo"); e("g+r+yU"); e("g+r+yaM"); e("g+r+yau"); e("g+r+yai"); e("g+r+yI"); e("g+r+y-i");
        e("g+ha"); e("g+h", "g+ha"); e("g+hi"); e("g+hu"); e("g+he"); e("g+ho"); e("g+hU"); e("g+haM"); e("g+hau"); e("g+hai"); e("g+hI"); e("g+h-i");
        e("g+h+g+ha"); e("g+h+g+h", "g+h+g+ha"); e("g+h+g+hi"); e("g+h+g+hu"); e("g+h+g+he"); e("g+h+g+ho"); e("g+h+g+hU"); e("g+h+g+haM"); e("g+h+g+hau"); e("g+h+g+hai"); e("g+h+g+hI"); e("g+h+g+h-i");
        e("g+h+nya"); e("g+h+ny", "g+h+nya"); e("g+h+nyi"); e("g+h+nyu"); e("g+h+nye"); e("g+h+nyo"); e("g+h+nyU"); e("g+h+nyaM"); e("g+h+nyau"); e("g+h+nyai"); e("g+h+nyI"); e("g+h+ny-i");
        e("g+h+na"); e("g+h+n", "g+h+na"); e("g+h+ni"); e("g+h+nu"); e("g+h+ne"); e("g+h+no"); e("g+h+nU"); e("g+h+naM"); e("g+h+nau"); e("g+h+nai"); e("g+h+nI"); e("g+h+n-i");
        e("g+h+n+ya"); e("g+h+n+y", "g+h+n+ya"); e("g+h+n+yi"); e("g+h+n+yu"); e("g+h+n+ye"); e("g+h+n+yo"); e("g+h+n+yU"); e("g+h+n+yaM"); e("g+h+n+yau"); e("g+h+n+yai"); e("g+h+n+yI"); e("g+h+n+y-i");
        e("g+h+ma"); e("g+h+m", "g+h+ma"); e("g+h+mi"); e("g+h+mu"); e("g+h+me"); e("g+h+mo"); e("g+h+mU"); e("g+h+maM"); e("g+h+mau"); e("g+h+mai"); e("g+h+mI"); e("g+h+m-i");
        e("g+h+la"); e("g+h+l", "g+h+la"); e("g+h+li"); e("g+h+lu"); e("g+h+le"); e("g+h+lo"); e("g+h+lU"); e("g+h+laM"); e("g+h+lau"); e("g+h+lai"); e("g+h+lI"); e("g+h+l-i");
        e("g+h+ya"); e("g+h+y", "g+h+ya"); e("g+h+yi"); e("g+h+yu"); e("g+h+ye"); e("g+h+yo"); e("g+h+yU"); e("g+h+yaM"); e("g+h+yau"); e("g+h+yai"); e("g+h+yI"); e("g+h+y-i");
        e("g+h+ra"); e("g+h+r", "g+h+ra"); e("g+h+ri"); e("g+h+ru"); e("g+h+re"); e("g+h+ro"); e("g+h+rU"); e("g+h+raM"); e("g+h+rau"); e("g+h+rai"); e("g+h+rI"); e("g+h+r-i");
        e("g+h+wa"); e("g+h+w", "g+h+wa"); e("g+h+wi"); e("g+h+wu"); e("g+h+we"); e("g+h+wo"); e("g+h+wU"); e("g+h+waM"); e("g+h+wau"); e("g+h+wai"); e("g+h+wI"); e("g+h+w-i");
        e("ng+ka"); e("ng+k", "ng+ka"); e("ng+ki"); e("ng+ku"); e("ng+ke"); e("ng+ko"); e("ng+kU"); e("ng+kaM"); e("ng+kau"); e("ng+kai"); e("ng+kI"); e("ng+k-i");
        e("ng+k+ta"); e("ng+k+t", "ng+k+ta"); e("ng+k+ti"); e("ng+k+tu"); e("ng+k+te"); e("ng+k+to"); e("ng+k+tU"); e("ng+k+taM"); e("ng+k+tau"); e("ng+k+tai"); e("ng+k+tI"); e("ng+k+t-i");
        e("ng+k+t+ya"); e("ng+k+t+y", "ng+k+t+ya"); e("ng+k+t+yi"); e("ng+k+t+yu"); e("ng+k+t+ye"); e("ng+k+t+yo"); e("ng+k+t+yU"); e("ng+k+t+yaM"); e("ng+k+t+yau"); e("ng+k+t+yai"); e("ng+k+t+yI"); e("ng+k+t+y-i");
        e("ng+k+ya"); e("ng+k+y", "ng+k+ya"); e("ng+k+yi"); e("ng+k+yu"); e("ng+k+ye"); e("ng+k+yo"); e("ng+k+yU"); e("ng+k+yaM"); e("ng+k+yau"); e("ng+k+yai"); e("ng+k+yI"); e("ng+k+y-i");
        e("ng+kha"); e("ng+kh", "ng+kha"); e("ng+khi"); e("ng+khu"); e("ng+khe"); e("ng+kho"); e("ng+khU"); e("ng+khaM"); e("ng+khau"); e("ng+khai"); e("ng+khI"); e("ng+kh-i");
        e("ng+kh+ya"); e("ng+kh+y", "ng+kh+ya"); e("ng+kh+yi"); e("ng+kh+yu"); e("ng+kh+ye"); e("ng+kh+yo"); e("ng+kh+yU"); e("ng+kh+yaM"); e("ng+kh+yau"); e("ng+kh+yai"); e("ng+kh+yI"); e("ng+kh+y-i");
        e("ng+ga"); e("ng+g", "ng+ga"); e("ng+gi"); e("ng+gu"); e("ng+ge"); e("ng+go"); e("ng+gU"); e("ng+gaM"); e("ng+gau"); e("ng+gai"); e("ng+gI"); e("ng+g-i");
        e("ng+g+ra"); e("ng+g+r", "ng+g+ra"); e("ng+g+ri"); e("ng+g+ru"); e("ng+g+re"); e("ng+g+ro"); e("ng+g+rU"); e("ng+g+raM"); e("ng+g+rau"); e("ng+g+rai"); e("ng+g+rI"); e("ng+g+r-i");
        e("ng+g+ya"); e("ng+g+y", "ng+g+ya"); e("ng+g+yi"); e("ng+g+yu"); e("ng+g+ye"); e("ng+g+yo"); e("ng+g+yU"); e("ng+g+yaM"); e("ng+g+yau"); e("ng+g+yai"); e("ng+g+yI"); e("ng+g+y-i");
        e("ng+g+ha"); e("ng+g+h", "ng+g+ha"); e("ng+g+hi"); e("ng+g+hu"); e("ng+g+he"); e("ng+g+ho"); e("ng+g+hU"); e("ng+g+haM"); e("ng+g+hau"); e("ng+g+hai"); e("ng+g+hI"); e("ng+g+h-i");
        e("ng+g+h+ya"); e("ng+g+h+y", "ng+g+h+ya"); e("ng+g+h+yi"); e("ng+g+h+yu"); e("ng+g+h+ye"); e("ng+g+h+yo"); e("ng+g+h+yU"); e("ng+g+h+yaM"); e("ng+g+h+yau"); e("ng+g+h+yai"); e("ng+g+h+yI"); e("ng+g+h+y-i");
        e("ng+g+h+ra"); e("ng+g+h+r", "ng+g+h+ra"); e("ng+g+h+ri"); e("ng+g+h+ru"); e("ng+g+h+re"); e("ng+g+h+ro"); e("ng+g+h+rU"); e("ng+g+h+raM"); e("ng+g+h+rau"); e("ng+g+h+rai"); e("ng+g+h+rI"); e("ng+g+h+r-i");
        e("ng+nga"); e("ng+ng", "ng+nga"); e("ng+ngi"); e("ng+ngu"); e("ng+nge"); e("ng+ngo"); e("ng+ngU"); e("ng+ngaM"); e("ng+ngau"); e("ng+ngai"); e("ng+ngI"); e("ng+ng-i");
        e("ng+ta"); e("ng+t", "ng+ta"); e("ng+ti"); e("ng+tu"); e("ng+te"); e("ng+to"); e("ng+tU"); e("ng+taM"); e("ng+tau"); e("ng+tai"); e("ng+tI"); e("ng+t-i");
        e("ng+na"); e("ng+n", "ng+na"); e("ng+ni"); e("ng+nu"); e("ng+ne"); e("ng+no"); e("ng+nU"); e("ng+naM"); e("ng+nau"); e("ng+nai"); e("ng+nI"); e("ng+n-i");
        e("ng+ma"); e("ng+m", "ng+ma"); e("ng+mi"); e("ng+mu"); e("ng+me"); e("ng+mo"); e("ng+mU"); e("ng+maM"); e("ng+mau"); e("ng+mai"); e("ng+mI"); e("ng+m-i");
        e("ng+ya"); e("ng+y", "ng+ya"); e("ng+yi"); e("ng+yu"); e("ng+ye"); e("ng+yo"); e("ng+yU"); e("ng+yaM"); e("ng+yau"); e("ng+yai"); e("ng+yI"); e("ng+y-i");
        e("ng+la"); e("ng+l", "ng+la"); e("ng+li"); e("ng+lu"); e("ng+le"); e("ng+lo"); e("ng+lU"); e("ng+laM"); e("ng+lau"); e("ng+lai"); e("ng+lI"); e("ng+l-i");
        e("ng+sha"); e("ng+sh", "ng+sha"); e("ng+shi"); e("ng+shu"); e("ng+she"); e("ng+sho"); e("ng+shU"); e("ng+shaM"); e("ng+shau"); e("ng+shai"); e("ng+shI"); e("ng+sh-i");
        e("ng+ha"); e("ng+h", "ng+ha"); e("ng+hi"); e("ng+hu"); e("ng+he"); e("ng+ho"); e("ng+hU"); e("ng+haM"); e("ng+hau"); e("ng+hai"); e("ng+hI"); e("ng+h-i");
        e("ng+k+Sha"); e("ng+k+Sh", "ng+k+Sha"); e("ng+k+Shi"); e("ng+k+Shu"); e("ng+k+She"); e("ng+k+Sho"); e("ng+k+ShU"); e("ng+k+ShaM"); e("ng+k+Shau"); e("ng+k+Shai"); e("ng+k+ShI"); e("ng+k+Sh-i");
        e("ng+k+Sh+wa"); e("ng+k+Sh+w", "ng+k+Sh+wa"); e("ng+k+Sh+wi"); e("ng+k+Sh+wu"); e("ng+k+Sh+we"); e("ng+k+Sh+wo"); e("ng+k+Sh+wU"); e("ng+k+Sh+waM"); e("ng+k+Sh+wau"); e("ng+k+Sh+wai"); e("ng+k+Sh+wI"); e("ng+k+Sh+w-i");
        e("ng+k+Sh+ya"); e("ng+k+Sh+y", "ng+k+Sh+ya"); e("ng+k+Sh+yi"); e("ng+k+Sh+yu"); e("ng+k+Sh+ye"); e("ng+k+Sh+yo"); e("ng+k+Sh+yU"); e("ng+k+Sh+yaM"); e("ng+k+Sh+yau"); e("ng+k+Sh+yai"); e("ng+k+Sh+yI"); e("ng+k+Sh+y-i");
        e("ts+tsa"); e("ts+ts", "ts+tsa"); e("ts+tsi"); e("ts+tsu"); e("ts+tse"); e("ts+tso"); e("ts+tsU"); e("ts+tsaM"); e("ts+tsau"); e("ts+tsai"); e("ts+tsI"); e("ts+ts-i");
        e("ts+tsha"); e("ts+tsh", "ts+tsha"); e("ts+tshi"); e("ts+tshu"); e("ts+tshe"); e("ts+tsho"); e("ts+tshU"); e("ts+tshaM"); e("ts+tshau"); e("ts+tshai"); e("ts+tshI"); e("ts+tsh-i");
        e("ts+tsh+wa"); e("ts+tsh+w", "ts+tsh+wa"); e("ts+tsh+wi"); e("ts+tsh+wu"); e("ts+tsh+we"); e("ts+tsh+wo"); e("ts+tsh+wU"); e("ts+tsh+waM"); e("ts+tsh+wau"); e("ts+tsh+wai"); e("ts+tsh+wI"); e("ts+tsh+w-i");
        e("ts+tsh+ra"); e("ts+tsh+r", "ts+tsh+ra"); e("ts+tsh+ri"); e("ts+tsh+ru"); e("ts+tsh+re"); e("ts+tsh+ro"); e("ts+tsh+rU"); e("ts+tsh+raM"); e("ts+tsh+rau"); e("ts+tsh+rai"); e("ts+tsh+rI"); e("ts+tsh+r-i");
        e("ts+nya"); e("ts+ny", "ts+nya"); e("ts+nyi"); e("ts+nyu"); e("ts+nye"); e("ts+nyo"); e("ts+nyU"); e("ts+nyaM"); e("ts+nyau"); e("ts+nyai"); e("ts+nyI"); e("ts+ny-i");
        e("ts+n+ya"); e("ts+n+y", "ts+n+ya"); e("ts+n+yi"); e("ts+n+yu"); e("ts+n+ye"); e("ts+n+yo"); e("ts+n+yU"); e("ts+n+yaM"); e("ts+n+yau"); e("ts+n+yai"); e("ts+n+yI"); e("ts+n+y-i");
        e("ts+ma"); e("ts+m", "ts+ma"); e("ts+mi"); e("ts+mu"); e("ts+me"); e("ts+mo"); e("ts+mU"); e("ts+maM"); e("ts+mau"); e("ts+mai"); e("ts+mI"); e("ts+m-i");
        e("ts+ya"); e("ts+y", "ts+ya"); e("ts+yi"); e("ts+yu"); e("ts+ye"); e("ts+yo"); e("ts+yU"); e("ts+yaM"); e("ts+yau"); e("ts+yai"); e("ts+yI"); e("ts+y-i");
        e("ts+ra"); e("ts+r", "ts+ra"); e("ts+ri"); e("ts+ru"); e("ts+re"); e("ts+ro"); e("ts+rU"); e("ts+raM"); e("ts+rau"); e("ts+rai"); e("ts+rI"); e("ts+r-i");
        e("ts+la"); e("ts+l", "ts+la"); e("ts+li"); e("ts+lu"); e("ts+le"); e("ts+lo"); e("ts+lU"); e("ts+laM"); e("ts+lau"); e("ts+lai"); e("ts+lI"); e("ts+l-i");
        e("ts+h+ya"); e("ts+h+y", "ts+h+ya"); e("ts+h+yi"); e("ts+h+yu"); e("ts+h+ye"); e("ts+h+yo"); e("ts+h+yU"); e("ts+h+yaM"); e("ts+h+yau"); e("ts+h+yai"); e("ts+h+yI"); e("ts+h+y-i");
        e("tsh+tha"); e("tsh+th", "tsh+tha"); e("tsh+thi"); e("tsh+thu"); e("tsh+the"); e("tsh+tho"); e("tsh+thU"); e("tsh+thaM"); e("tsh+thau"); e("tsh+thai"); e("tsh+thI"); e("tsh+th-i");
        e("tsh+tsha"); e("tsh+tsh", "tsh+tsha"); e("tsh+tshi"); e("tsh+tshu"); e("tsh+tshe"); e("tsh+tsho"); e("tsh+tshU"); e("tsh+tshaM"); e("tsh+tshau"); e("tsh+tshai"); e("tsh+tshI"); e("tsh+tsh-i");
        e("tsh+ya"); e("tsh+y", "tsh+ya"); e("tsh+yi"); e("tsh+yu"); e("tsh+ye"); e("tsh+yo"); e("tsh+yU"); e("tsh+yaM"); e("tsh+yau"); e("tsh+yai"); e("tsh+yI"); e("tsh+y-i");
        e("tsh+ra"); e("tsh+r", "tsh+ra"); e("tsh+ri"); e("tsh+ru"); e("tsh+re"); e("tsh+ro"); e("tsh+rU"); e("tsh+raM"); e("tsh+rau"); e("tsh+rai"); e("tsh+rI"); e("tsh+r-i");
        e("tsh+la"); e("tsh+l", "tsh+la"); e("tsh+li"); e("tsh+lu"); e("tsh+le"); e("tsh+lo"); e("tsh+lU"); e("tsh+laM"); e("tsh+lau"); e("tsh+lai"); e("tsh+lI"); e("tsh+l-i");
        e("dz+dza"); e("dz+dz", "dz+dza"); e("dz+dzi"); e("dz+dzu"); e("dz+dze"); e("dz+dzo"); e("dz+dzU"); e("dz+dzaM"); e("dz+dzau"); e("dz+dzai"); e("dz+dzI"); e("dz+dz-i");
        e("dz+dz+nya"); e("dz+dz+ny", "dz+dz+nya"); e("dz+dz+nyi"); e("dz+dz+nyu"); e("dz+dz+nye"); e("dz+dz+nyo"); e("dz+dz+nyU"); e("dz+dz+nyaM"); e("dz+dz+nyau"); e("dz+dz+nyai"); e("dz+dz+nyI"); e("dz+dz+ny-i");
        e("dz+dz+wa"); e("dz+dz+w", "dz+dz+wa"); e("dz+dz+wi"); e("dz+dz+wu"); e("dz+dz+we"); e("dz+dz+wo"); e("dz+dz+wU"); e("dz+dz+waM"); e("dz+dz+wau"); e("dz+dz+wai"); e("dz+dz+wI"); e("dz+dz+w-i");
        e("dz+dz+ha"); e("dz+dz+h", "dz+dz+ha"); e("dz+dz+hi"); e("dz+dz+hu"); e("dz+dz+he"); e("dz+dz+ho"); e("dz+dz+hU"); e("dz+dz+haM"); e("dz+dz+hau"); e("dz+dz+hai"); e("dz+dz+hI"); e("dz+dz+h-i");
        e("dz+h+dz+ha"); e("dz+h+dz+h", "dz+h+dz+ha"); e("dz+h+dz+hi"); e("dz+h+dz+hu"); e("dz+h+dz+he"); e("dz+h+dz+ho"); e("dz+h+dz+hU"); e("dz+h+dz+haM"); e("dz+h+dz+hau"); e("dz+h+dz+hai"); e("dz+h+dz+hI"); e("dz+h+dz+h-i");
        e("dz+nya"); e("dz+ny", "dz+nya"); e("dz+nyi"); e("dz+nyu"); e("dz+nye"); e("dz+nyo"); e("dz+nyU"); e("dz+nyaM"); e("dz+nyau"); e("dz+nyai"); e("dz+nyI"); e("dz+ny-i");
        e("dz+ny+ya"); e("dz+ny+y", "dz+ny+ya"); e("dz+ny+yi"); e("dz+ny+yu"); e("dz+ny+ye"); e("dz+ny+yo"); e("dz+ny+yU"); e("dz+ny+yaM"); e("dz+ny+yau"); e("dz+ny+yai"); e("dz+ny+yI"); e("dz+ny+y-i");
        e("dz+na"); e("dz+n", "dz+na"); e("dz+ni"); e("dz+nu"); e("dz+ne"); e("dz+no"); e("dz+nU"); e("dz+naM"); e("dz+nau"); e("dz+nai"); e("dz+nI"); e("dz+n-i");
        e("dz+n+wa"); e("dz+n+w", "dz+n+wa"); e("dz+n+wi"); e("dz+n+wu"); e("dz+n+we"); e("dz+n+wo"); e("dz+n+wU"); e("dz+n+waM"); e("dz+n+wau"); e("dz+n+wai"); e("dz+n+wI"); e("dz+n+w-i");
        e("dz+ma"); e("dz+m", "dz+ma"); e("dz+mi"); e("dz+mu"); e("dz+me"); e("dz+mo"); e("dz+mU"); e("dz+maM"); e("dz+mau"); e("dz+mai"); e("dz+mI"); e("dz+m-i");
        e("dz+ya"); e("dz+y", "dz+ya"); e("dz+yi"); e("dz+yu"); e("dz+ye"); e("dz+yo"); e("dz+yU"); e("dz+yaM"); e("dz+yau"); e("dz+yai"); e("dz+yI"); e("dz+y-i");
        e("dz+ra"); e("dz+r", "dz+ra"); e("dz+ri"); e("dz+ru"); e("dz+re"); e("dz+ro"); e("dz+rU"); e("dz+raM"); e("dz+rau"); e("dz+rai"); e("dz+rI"); e("dz+r-i");
        e("dz+wa"); e("dz+w", "dz+wa"); e("dz+wi"); e("dz+wu"); e("dz+we"); e("dz+wo"); e("dz+wU"); e("dz+waM"); e("dz+wau"); e("dz+wai"); e("dz+wI"); e("dz+w-i");
        e("dz+ha"); e("dz+h", "dz+ha"); e("dz+hi"); e("dz+hu"); e("dz+he"); e("dz+ho"); e("dz+hU"); e("dz+haM"); e("dz+hau"); e("dz+hai"); e("dz+hI"); e("dz+h-i");
        e("dz+h+ya"); e("dz+h+y", "dz+h+ya"); e("dz+h+yi"); e("dz+h+yu"); e("dz+h+ye"); e("dz+h+yo"); e("dz+h+yU"); e("dz+h+yaM"); e("dz+h+yau"); e("dz+h+yai"); e("dz+h+yI"); e("dz+h+y-i");
        e("dz+h+ra"); e("dz+h+r", "dz+h+ra"); e("dz+h+ri"); e("dz+h+ru"); e("dz+h+re"); e("dz+h+ro"); e("dz+h+rU"); e("dz+h+raM"); e("dz+h+rau"); e("dz+h+rai"); e("dz+h+rI"); e("dz+h+r-i");
        e("dz+h+la"); e("dz+h+l", "dz+h+la"); e("dz+h+li"); e("dz+h+lu"); e("dz+h+le"); e("dz+h+lo"); e("dz+h+lU"); e("dz+h+laM"); e("dz+h+lau"); e("dz+h+lai"); e("dz+h+lI"); e("dz+h+l-i");
        e("dz+h+wa"); e("dz+h+w", "dz+h+wa"); e("dz+h+wi"); e("dz+h+wu"); e("dz+h+we"); e("dz+h+wo"); e("dz+h+wU"); e("dz+h+waM"); e("dz+h+wau"); e("dz+h+wai"); e("dz+h+wI"); e("dz+h+w-i");
        e("ny+tsa"); e("ny+ts", "ny+tsa"); e("ny+tsi"); e("ny+tsu"); e("ny+tse"); e("ny+tso"); e("ny+tsU"); e("ny+tsaM"); e("ny+tsau"); e("ny+tsai"); e("ny+tsI"); e("ny+ts-i");
        e("ny+ts+ma"); e("ny+ts+m", "ny+ts+ma"); e("ny+ts+mi"); e("ny+ts+mu"); e("ny+ts+me"); e("ny+ts+mo"); e("ny+ts+mU"); e("ny+ts+maM"); e("ny+ts+mau"); e("ny+ts+mai"); e("ny+ts+mI"); e("ny+ts+m-i");
        e("ny+ts+ya"); e("ny+ts+y", "ny+ts+ya"); e("ny+ts+yi"); e("ny+ts+yu"); e("ny+ts+ye"); e("ny+ts+yo"); e("ny+ts+yU"); e("ny+ts+yaM"); e("ny+ts+yau"); e("ny+ts+yai"); e("ny+ts+yI"); e("ny+ts+y-i");
        e("ny+tsha"); e("ny+tsh", "ny+tsha"); e("ny+tshi"); e("ny+tshu"); e("ny+tshe"); e("ny+tsho"); e("ny+tshU"); e("ny+tshaM"); e("ny+tshau"); e("ny+tshai"); e("ny+tshI"); e("ny+tsh-i");
        e("ny+dza"); e("ny+dz", "ny+dza"); e("ny+dzi"); e("ny+dzu"); e("ny+dze"); e("ny+dzo"); e("ny+dzU"); e("ny+dzaM"); e("ny+dzau"); e("ny+dzai"); e("ny+dzI"); e("ny+dz-i");
        e("ny+dz+ya"); e("ny+dz+y", "ny+dz+ya"); e("ny+dz+yi"); e("ny+dz+yu"); e("ny+dz+ye"); e("ny+dz+yo"); e("ny+dz+yU"); e("ny+dz+yaM"); e("ny+dz+yau"); e("ny+dz+yai"); e("ny+dz+yI"); e("ny+dz+y-i");
        e("ny+dz+ha"); e("ny+dz+h", "ny+dz+ha"); e("ny+dz+hi"); e("ny+dz+hu"); e("ny+dz+he"); e("ny+dz+ho"); e("ny+dz+hU"); e("ny+dz+haM"); e("ny+dz+hau"); e("ny+dz+hai"); e("ny+dz+hI"); e("ny+dz+h-i");
        e("ny+nya"); e("ny+ny", "ny+nya"); e("ny+nyi"); e("ny+nyu"); e("ny+nye"); e("ny+nyo"); e("ny+nyU"); e("ny+nyaM"); e("ny+nyau"); e("ny+nyai"); e("ny+nyI"); e("ny+ny-i");
        e("ny+pa"); e("ny+p", "ny+pa"); e("ny+pi"); e("ny+pu"); e("ny+pe"); e("ny+po"); e("ny+pU"); e("ny+paM"); e("ny+pau"); e("ny+pai"); e("ny+pI"); e("ny+p-i");
        e("ny+pha"); e("ny+ph", "ny+pha"); e("ny+phi"); e("ny+phu"); e("ny+phe"); e("ny+pho"); e("ny+phU"); e("ny+phaM"); e("ny+phau"); e("ny+phai"); e("ny+phI"); e("ny+ph-i");
        e("ny+ya"); e("ny+y", "ny+ya"); e("ny+yi"); e("ny+yu"); e("ny+ye"); e("ny+yo"); e("ny+yU"); e("ny+yaM"); e("ny+yau"); e("ny+yai"); e("ny+yI"); e("ny+y-i");
        e("ny+ra"); e("ny+r", "ny+ra"); e("ny+ri"); e("ny+ru"); e("ny+re"); e("ny+ro"); e("ny+rU"); e("ny+raM"); e("ny+rau"); e("ny+rai"); e("ny+rI"); e("ny+r-i");
        e("ny+la"); e("ny+l", "ny+la"); e("ny+li"); e("ny+lu"); e("ny+le"); e("ny+lo"); e("ny+lU"); e("ny+laM"); e("ny+lau"); e("ny+lai"); e("ny+lI"); e("ny+l-i");
        e("ny+sha"); e("ny+sh", "ny+sha"); e("ny+shi"); e("ny+shu"); e("ny+she"); e("ny+sho"); e("ny+shU"); e("ny+shaM"); e("ny+shau"); e("ny+shai"); e("ny+shI"); e("ny+sh-i");
        e("T+ka"); e("T+k", "T+ka"); e("T+ki"); e("T+ku"); e("T+ke"); e("T+ko"); e("T+kU"); e("T+kaM"); e("T+kau"); e("T+kai"); e("T+kI"); e("T+k-i");
        e("T+Ta"); e("T+T", "T+Ta"); e("T+Ti"); e("T+Tu"); e("T+Te"); e("T+To"); e("T+TU"); e("T+TaM"); e("T+Tau"); e("T+Tai"); e("T+TI"); e("T+T-i");
        e("T+T+ha"); e("T+T+h", "T+T+ha"); e("T+T+hi"); e("T+T+hu"); e("T+T+he"); e("T+T+ho"); e("T+T+hU"); e("T+T+haM"); e("T+T+hau"); e("T+T+hai"); e("T+T+hI"); e("T+T+h-i");
        e("T+na"); e("T+n", "T+na"); e("T+ni"); e("T+nu"); e("T+ne"); e("T+no"); e("T+nU"); e("T+naM"); e("T+nau"); e("T+nai"); e("T+nI"); e("T+n-i");
        e("T+pa"); e("T+p", "T+pa"); e("T+pi"); e("T+pu"); e("T+pe"); e("T+po"); e("T+pU"); e("T+paM"); e("T+pau"); e("T+pai"); e("T+pI"); e("T+p-i");
        e("T+ma"); e("T+m", "T+ma"); e("T+mi"); e("T+mu"); e("T+me"); e("T+mo"); e("T+mU"); e("T+maM"); e("T+mau"); e("T+mai"); e("T+mI"); e("T+m-i");
        e("T+ya"); e("T+y", "T+ya"); e("T+yi"); e("T+yu"); e("T+ye"); e("T+yo"); e("T+yU"); e("T+yaM"); e("T+yau"); e("T+yai"); e("T+yI"); e("T+y-i");
        e("T+wa"); e("T+w", "T+wa"); e("T+wi"); e("T+wu"); e("T+we"); e("T+wo"); e("T+wU"); e("T+waM"); e("T+wau"); e("T+wai"); e("T+wI"); e("T+w-i");
        e("T+sa"); e("T+s", "T+sa"); e("T+si"); e("T+su"); e("T+se"); e("T+so"); e("T+sU"); e("T+saM"); e("T+sau"); e("T+sai"); e("T+sI"); e("T+s-i");
        e("Th+ya"); e("Th+y", "Th+ya"); e("Th+yi"); e("Th+yu"); e("Th+ye"); e("Th+yo"); e("Th+yU"); e("Th+yaM"); e("Th+yau"); e("Th+yai"); e("Th+yI"); e("Th+y-i");
        e("Th+ra"); e("Th+r", "Th+ra"); e("Th+ri"); e("Th+ru"); e("Th+re"); e("Th+ro"); e("Th+rU"); e("Th+raM"); e("Th+rau"); e("Th+rai"); e("Th+rI"); e("Th+r-i");
        e("D+ga"); e("D+g", "D+ga"); e("D+gi"); e("D+gu"); e("D+ge"); e("D+go"); e("D+gU"); e("D+gaM"); e("D+gau"); e("D+gai"); e("D+gI"); e("D+g-i");
        e("D+g+ya"); e("D+g+y", "D+g+ya"); e("D+g+yi"); e("D+g+yu"); e("D+g+ye"); e("D+g+yo"); e("D+g+yU"); e("D+g+yaM"); e("D+g+yau"); e("D+g+yai"); e("D+g+yI"); e("D+g+y-i");
        e("D+g+ha"); e("D+g+h", "D+g+ha"); e("D+g+hi"); e("D+g+hu"); e("D+g+he"); e("D+g+ho"); e("D+g+hU"); e("D+g+haM"); e("D+g+hau"); e("D+g+hai"); e("D+g+hI"); e("D+g+h-i");
        e("D+g+h+ra"); e("D+g+h+r", "D+g+h+ra"); e("D+g+h+ri"); e("D+g+h+ru"); e("D+g+h+re"); e("D+g+h+ro"); e("D+g+h+rU"); e("D+g+h+raM"); e("D+g+h+rau"); e("D+g+h+rai"); e("D+g+h+rI"); e("D+g+h+r-i");
        e("D+Da"); e("D+D", "D+Da"); e("D+Di"); e("D+Du"); e("D+De"); e("D+Do"); e("D+DU"); e("D+DaM"); e("D+Dau"); e("D+Dai"); e("D+DI"); e("D+D-i");
        e("D+D+ha"); e("D+D+h", "D+D+ha"); e("D+D+hi"); e("D+D+hu"); e("D+D+he"); e("D+D+ho"); e("D+D+hU"); e("D+D+haM"); e("D+D+hau"); e("D+D+hai"); e("D+D+hI"); e("D+D+h-i");
        e("D+D+h+ya"); e("D+D+h+y", "D+D+h+ya"); e("D+D+h+yi"); e("D+D+h+yu"); e("D+D+h+ye"); e("D+D+h+yo"); e("D+D+h+yU"); e("D+D+h+yaM"); e("D+D+h+yau"); e("D+D+h+yai"); e("D+D+h+yI"); e("D+D+h+y-i");
        e("D+na"); e("D+n", "D+na"); e("D+ni"); e("D+nu"); e("D+ne"); e("D+no"); e("D+nU"); e("D+naM"); e("D+nau"); e("D+nai"); e("D+nI"); e("D+n-i");
        e("D+ma"); e("D+m", "D+ma"); e("D+mi"); e("D+mu"); e("D+me"); e("D+mo"); e("D+mU"); e("D+maM"); e("D+mau"); e("D+mai"); e("D+mI"); e("D+m-i");
        e("D+ya"); e("D+y", "D+ya"); e("D+yi"); e("D+yu"); e("D+ye"); e("D+yo"); e("D+yU"); e("D+yaM"); e("D+yau"); e("D+yai"); e("D+yI"); e("D+y-i");
        e("D+ra"); e("D+r", "D+ra"); e("D+ri"); e("D+ru"); e("D+re"); e("D+ro"); e("D+rU"); e("D+raM"); e("D+rau"); e("D+rai"); e("D+rI"); e("D+r-i");
        e("D+wa"); e("D+w", "D+wa"); e("D+wi"); e("D+wu"); e("D+we"); e("D+wo"); e("D+wU"); e("D+waM"); e("D+wau"); e("D+wai"); e("D+wI"); e("D+w-i");
        e("D+ha"); e("D+h", "D+ha"); e("D+hi"); e("D+hu"); e("D+he"); e("D+ho"); e("D+hU"); e("D+haM"); e("D+hau"); e("D+hai"); e("D+hI"); e("D+h-i");
        e("D+h+D+ha"); e("D+h+D+h", "D+h+D+ha"); e("D+h+D+hi"); e("D+h+D+hu"); e("D+h+D+he"); e("D+h+D+ho"); e("D+h+D+hU"); e("D+h+D+haM"); e("D+h+D+hau"); e("D+h+D+hai"); e("D+h+D+hI"); e("D+h+D+h-i");
        e("D+h+ma"); e("D+h+m", "D+h+ma"); e("D+h+mi"); e("D+h+mu"); e("D+h+me"); e("D+h+mo"); e("D+h+mU"); e("D+h+maM"); e("D+h+mau"); e("D+h+mai"); e("D+h+mI"); e("D+h+m-i");
        e("D+h+ya"); e("D+h+y", "D+h+ya"); e("D+h+yi"); e("D+h+yu"); e("D+h+ye"); e("D+h+yo"); e("D+h+yU"); e("D+h+yaM"); e("D+h+yau"); e("D+h+yai"); e("D+h+yI"); e("D+h+y-i");
        e("D+h+ra"); e("D+h+r", "D+h+ra"); e("D+h+ri"); e("D+h+ru"); e("D+h+re"); e("D+h+ro"); e("D+h+rU"); e("D+h+raM"); e("D+h+rau"); e("D+h+rai"); e("D+h+rI"); e("D+h+r-i");
        e("D+h+wa"); e("D+h+w", "D+h+wa"); e("D+h+wi"); e("D+h+wu"); e("D+h+we"); e("D+h+wo"); e("D+h+wU"); e("D+h+waM"); e("D+h+wau"); e("D+h+wai"); e("D+h+wI"); e("D+h+w-i");
        e("N+Ta"); e("N+T", "N+Ta"); e("N+Ti"); e("N+Tu"); e("N+Te"); e("N+To"); e("N+TU"); e("N+TaM"); e("N+Tau"); e("N+Tai"); e("N+TI"); e("N+T-i");
        e("N+Tha"); e("N+Th", "N+Tha"); e("N+Thi"); e("N+Thu"); e("N+The"); e("N+Tho"); e("N+ThU"); e("N+ThaM"); e("N+Thau"); e("N+Thai"); e("N+ThI"); e("N+Th-i");
        e("N+Da"); e("N+D", "N+Da"); e("N+Di"); e("N+Du"); e("N+De"); e("N+Do"); e("N+DU"); e("N+DaM"); e("N+Dau"); e("N+Dai"); e("N+DI"); e("N+D-i");
        e("N+D+Ya"); e("N+D+Y", "N+D+Ya"); e("N+D+Yi"); e("N+D+Yu"); e("N+D+Ye"); e("N+D+Yo"); e("N+D+YU"); e("N+D+YaM"); e("N+D+Yau"); e("N+D+Yai"); e("N+D+YI"); e("N+D+Y-i");
        e("N+D+ra"); e("N+D+r", "N+D+ra"); e("N+D+ri"); e("N+D+ru"); e("N+D+re"); e("N+D+ro"); e("N+D+rU"); e("N+D+raM"); e("N+D+rau"); e("N+D+rai"); e("N+D+rI"); e("N+D+r-i");
        e("N+D+R+ya"); e("N+D+R+y", "N+D+R+ya"); e("N+D+R+yi"); e("N+D+R+yu"); e("N+D+R+ye"); e("N+D+R+yo"); e("N+D+R+yU"); e("N+D+R+yaM"); e("N+D+R+yau"); e("N+D+R+yai"); e("N+D+R+yI"); e("N+D+R+y-i");
        e("N+D+ha"); e("N+D+h", "N+D+ha"); e("N+D+hi"); e("N+D+hu"); e("N+D+he"); e("N+D+ho"); e("N+D+hU"); e("N+D+haM"); e("N+D+hau"); e("N+D+hai"); e("N+D+hI"); e("N+D+h-i");
        e("N+Na"); e("N+N", "N+Na"); e("N+Ni"); e("N+Nu"); e("N+Ne"); e("N+No"); e("N+NU"); e("N+NaM"); e("N+Nau"); e("N+Nai"); e("N+NI"); e("N+N-i");
        e("N+d+ra"); e("N+d+r", "N+d+ra"); e("N+d+ri"); e("N+d+ru"); e("N+d+re"); e("N+d+ro"); e("N+d+rU"); e("N+d+raM"); e("N+d+rau"); e("N+d+rai"); e("N+d+rI"); e("N+d+r-i");
        e("N+ma"); e("N+m", "N+ma"); e("N+mi"); e("N+mu"); e("N+me"); e("N+mo"); e("N+mU"); e("N+maM"); e("N+mau"); e("N+mai"); e("N+mI"); e("N+m-i");
        e("N+ya"); e("N+y", "N+ya"); e("N+yi"); e("N+yu"); e("N+ye"); e("N+yo"); e("N+yU"); e("N+yaM"); e("N+yau"); e("N+yai"); e("N+yI"); e("N+y-i");
        e("N+wa"); e("N+w", "N+wa"); e("N+wi"); e("N+wu"); e("N+we"); e("N+wo"); e("N+wU"); e("N+waM"); e("N+wau"); e("N+wai"); e("N+wI"); e("N+w-i");
        e("t+ka"); e("t+k", "t+ka"); e("t+ki"); e("t+ku"); e("t+ke"); e("t+ko"); e("t+kU"); e("t+kaM"); e("t+kau"); e("t+kai"); e("t+kI"); e("t+k-i");
        e("t+k+ra"); e("t+k+r", "t+k+ra"); e("t+k+ri"); e("t+k+ru"); e("t+k+re"); e("t+k+ro"); e("t+k+rU"); e("t+k+raM"); e("t+k+rau"); e("t+k+rai"); e("t+k+rI"); e("t+k+r-i");
        e("t+k+wa"); e("t+k+w", "t+k+wa"); e("t+k+wi"); e("t+k+wu"); e("t+k+we"); e("t+k+wo"); e("t+k+wU"); e("t+k+waM"); e("t+k+wau"); e("t+k+wai"); e("t+k+wI"); e("t+k+w-i");
        e("t+k+sa"); e("t+k+s", "t+k+sa"); e("t+k+si"); e("t+k+su"); e("t+k+se"); e("t+k+so"); e("t+k+sU"); e("t+k+saM"); e("t+k+sau"); e("t+k+sai"); e("t+k+sI"); e("t+k+s-i");
        e("t+ga"); e("t+g", "t+ga"); e("t+gi"); e("t+gu"); e("t+ge"); e("t+go"); e("t+gU"); e("t+gaM"); e("t+gau"); e("t+gai"); e("t+gI"); e("t+g-i");
        e("t+nya"); e("t+ny", "t+nya"); e("t+nyi"); e("t+nyu"); e("t+nye"); e("t+nyo"); e("t+nyU"); e("t+nyaM"); e("t+nyau"); e("t+nyai"); e("t+nyI"); e("t+ny-i");
        e("t+Tha"); e("t+Th", "t+Tha"); e("t+Thi"); e("t+Thu"); e("t+The"); e("t+Tho"); e("t+ThU"); e("t+ThaM"); e("t+Thau"); e("t+Thai"); e("t+ThI"); e("t+Th-i");
        e("t+ta"); e("t+t", "t+ta"); e("t+ti"); e("t+tu"); e("t+te"); e("t+to"); e("t+tU"); e("t+taM"); e("t+tau"); e("t+tai"); e("t+tI"); e("t+t-i");
        e("t+t+ya"); e("t+t+y", "t+t+ya"); e("t+t+yi"); e("t+t+yu"); e("t+t+ye"); e("t+t+yo"); e("t+t+yU"); e("t+t+yaM"); e("t+t+yau"); e("t+t+yai"); e("t+t+yI"); e("t+t+y-i");
        e("t+t+ra"); e("t+t+r", "t+t+ra"); e("t+t+ri"); e("t+t+ru"); e("t+t+re"); e("t+t+ro"); e("t+t+rU"); e("t+t+raM"); e("t+t+rau"); e("t+t+rai"); e("t+t+rI"); e("t+t+r-i");
        e("t+t+wa"); e("t+t+w", "t+t+wa"); e("t+t+wi"); e("t+t+wu"); e("t+t+we"); e("t+t+wo"); e("t+t+wU"); e("t+t+waM"); e("t+t+wau"); e("t+t+wai"); e("t+t+wI"); e("t+t+w-i");
        e("t+tha"); e("t+th", "t+tha"); e("t+thi"); e("t+thu"); e("t+the"); e("t+tho"); e("t+thU"); e("t+thaM"); e("t+thau"); e("t+thai"); e("t+thI"); e("t+th-i");
        e("t+th+ya"); e("t+th+y", "t+th+ya"); e("t+th+yi"); e("t+th+yu"); e("t+th+ye"); e("t+th+yo"); e("t+th+yU"); e("t+th+yaM"); e("t+th+yau"); e("t+th+yai"); e("t+th+yI"); e("t+th+y-i");
        e("t+na"); e("t+n", "t+na"); e("t+ni"); e("t+nu"); e("t+ne"); e("t+no"); e("t+nU"); e("t+naM"); e("t+nau"); e("t+nai"); e("t+nI"); e("t+n-i");
        e("t+n+ya"); e("t+n+y", "t+n+ya"); e("t+n+yi"); e("t+n+yu"); e("t+n+ye"); e("t+n+yo"); e("t+n+yU"); e("t+n+yaM"); e("t+n+yau"); e("t+n+yai"); e("t+n+yI"); e("t+n+y-i");
        e("t+pa"); e("t+p", "t+pa"); e("t+pi"); e("t+pu"); e("t+pe"); e("t+po"); e("t+pU"); e("t+paM"); e("t+pau"); e("t+pai"); e("t+pI"); e("t+p-i");
        e("t+p+ra"); e("t+p+r", "t+p+ra"); e("t+p+ri"); e("t+p+ru"); e("t+p+re"); e("t+p+ro"); e("t+p+rU"); e("t+p+raM"); e("t+p+rau"); e("t+p+rai"); e("t+p+rI"); e("t+p+r-i");
        e("t+pha"); e("t+ph", "t+pha"); e("t+phi"); e("t+phu"); e("t+phe"); e("t+pho"); e("t+phU"); e("t+phaM"); e("t+phau"); e("t+phai"); e("t+phI"); e("t+ph-i");
        e("t+ma"); e("t+m", "t+ma"); e("t+mi"); e("t+mu"); e("t+me"); e("t+mo"); e("t+mU"); e("t+maM"); e("t+mau"); e("t+mai"); e("t+mI"); e("t+m-i");
        e("t+m+ya"); e("t+m+y", "t+m+ya"); e("t+m+yi"); e("t+m+yu"); e("t+m+ye"); e("t+m+yo"); e("t+m+yU"); e("t+m+yaM"); e("t+m+yau"); e("t+m+yai"); e("t+m+yI"); e("t+m+y-i");
        e("t+ya"); e("t+y", "t+ya"); e("t+yi"); e("t+yu"); e("t+ye"); e("t+yo"); e("t+yU"); e("t+yaM"); e("t+yau"); e("t+yai"); e("t+yI"); e("t+y-i");
        e("t+r+na"); e("t+r+n", "t+r+na"); e("t+r+ni"); e("t+r+nu"); e("t+r+ne"); e("t+r+no"); e("t+r+nU"); e("t+r+naM"); e("t+r+nau"); e("t+r+nai"); e("t+r+nI"); e("t+r+n-i");
        e("t+sa"); e("t+s", "t+sa"); e("t+si"); e("t+su"); e("t+se"); e("t+so"); e("t+sU"); e("t+saM"); e("t+sau"); e("t+sai"); e("t+sI"); e("t+s-i");
        e("t+s+tha"); e("t+s+th", "t+s+tha"); e("t+s+thi"); e("t+s+thu"); e("t+s+the"); e("t+s+tho"); e("t+s+thU"); e("t+s+thaM"); e("t+s+thau"); e("t+s+thai"); e("t+s+thI"); e("t+s+th-i");
        e("t+s+na"); e("t+s+n", "t+s+na"); e("t+s+ni"); e("t+s+nu"); e("t+s+ne"); e("t+s+no"); e("t+s+nU"); e("t+s+naM"); e("t+s+nau"); e("t+s+nai"); e("t+s+nI"); e("t+s+n-i");
        e("t+s+n+ya"); e("t+s+n+y", "t+s+n+ya"); e("t+s+n+yi"); e("t+s+n+yu"); e("t+s+n+ye"); e("t+s+n+yo"); e("t+s+n+yU"); e("t+s+n+yaM"); e("t+s+n+yau"); e("t+s+n+yai"); e("t+s+n+yI"); e("t+s+n+y-i");
        e("t+s+ma"); e("t+s+m", "t+s+ma"); e("t+s+mi"); e("t+s+mu"); e("t+s+me"); e("t+s+mo"); e("t+s+mU"); e("t+s+maM"); e("t+s+mau"); e("t+s+mai"); e("t+s+mI"); e("t+s+m-i");
        e("t+s+m+ya"); e("t+s+m+y", "t+s+m+ya"); e("t+s+m+yi"); e("t+s+m+yu"); e("t+s+m+ye"); e("t+s+m+yo"); e("t+s+m+yU"); e("t+s+m+yaM"); e("t+s+m+yau"); e("t+s+m+yai"); e("t+s+m+yI"); e("t+s+m+y-i");
        e("t+s+ya"); e("t+s+y", "t+s+ya"); e("t+s+yi"); e("t+s+yu"); e("t+s+ye"); e("t+s+yo"); e("t+s+yU"); e("t+s+yaM"); e("t+s+yau"); e("t+s+yai"); e("t+s+yI"); e("t+s+y-i");
        e("t+s+ra"); e("t+s+r", "t+s+ra"); e("t+s+ri"); e("t+s+ru"); e("t+s+re"); e("t+s+ro"); e("t+s+rU"); e("t+s+raM"); e("t+s+rau"); e("t+s+rai"); e("t+s+rI"); e("t+s+r-i");
        e("t+s+wa"); e("t+s+w", "t+s+wa"); e("t+s+wi"); e("t+s+wu"); e("t+s+we"); e("t+s+wo"); e("t+s+wU"); e("t+s+waM"); e("t+s+wau"); e("t+s+wai"); e("t+s+wI"); e("t+s+w-i");
        e("t+r+ya"); e("t+r+y", "t+r+ya"); e("t+r+yi"); e("t+r+yu"); e("t+r+ye"); e("t+r+yo"); e("t+r+yU"); e("t+r+yaM"); e("t+r+yau"); e("t+r+yai"); e("t+r+yI"); e("t+r+y-i");
        e("t+w+ya"); e("t+w+y", "t+w+ya"); e("t+w+yi"); e("t+w+yu"); e("t+w+ye"); e("t+w+yo"); e("t+w+yU"); e("t+w+yaM"); e("t+w+yau"); e("t+w+yai"); e("t+w+yI"); e("t+w+y-i");
        e("t+k+Sha"); e("t+k+Sh", "t+k+Sha"); e("t+k+Shi"); e("t+k+Shu"); e("t+k+She"); e("t+k+Sho"); e("t+k+ShU"); e("t+k+ShaM"); e("t+k+Shau"); e("t+k+Shai"); e("t+k+ShI"); e("t+k+Sh-i");
        e("th+ya"); e("th+y", "th+ya"); e("th+yi"); e("th+yu"); e("th+ye"); e("th+yo"); e("th+yU"); e("th+yaM"); e("th+yau"); e("th+yai"); e("th+yI"); e("th+y-i");
        e("th+wa"); e("th+w", "th+wa"); e("th+wi"); e("th+wu"); e("th+we"); e("th+wo"); e("th+wU"); e("th+waM"); e("th+wau"); e("th+wai"); e("th+wI"); e("th+w-i");
        e("d+ga"); e("d+g", "d+ga"); e("d+gi"); e("d+gu"); e("d+ge"); e("d+go"); e("d+gU"); e("d+gaM"); e("d+gau"); e("d+gai"); e("d+gI"); e("d+g-i");
        e("d+g+ya"); e("d+g+y", "d+g+ya"); e("d+g+yi"); e("d+g+yu"); e("d+g+ye"); e("d+g+yo"); e("d+g+yU"); e("d+g+yaM"); e("d+g+yau"); e("d+g+yai"); e("d+g+yI"); e("d+g+y-i");
        e("d+g+ra"); e("d+g+r", "d+g+ra"); e("d+g+ri"); e("d+g+ru"); e("d+g+re"); e("d+g+ro"); e("d+g+rU"); e("d+g+raM"); e("d+g+rau"); e("d+g+rai"); e("d+g+rI"); e("d+g+r-i");
        e("d+g+ha"); e("d+g+h", "d+g+ha"); e("d+g+hi"); e("d+g+hu"); e("d+g+he"); e("d+g+ho"); e("d+g+hU"); e("d+g+haM"); e("d+g+hau"); e("d+g+hai"); e("d+g+hI"); e("d+g+h-i");
        e("d+g+h+ra"); e("d+g+h+r", "d+g+h+ra"); e("d+g+h+ri"); e("d+g+h+ru"); e("d+g+h+re"); e("d+g+h+ro"); e("d+g+h+rU"); e("d+g+h+raM"); e("d+g+h+rau"); e("d+g+h+rai"); e("d+g+h+rI"); e("d+g+h+r-i");
        e("d+dza"); e("d+dz", "d+dza"); e("d+dzi"); e("d+dzu"); e("d+dze"); e("d+dzo"); e("d+dzU"); e("d+dzaM"); e("d+dzau"); e("d+dzai"); e("d+dzI"); e("d+dz-i");
        e("d+da"); e("d+d", "d+da"); e("d+di"); e("d+du"); e("d+de"); e("d+do"); e("d+dU"); e("d+daM"); e("d+dau"); e("d+dai"); e("d+dI"); e("d+d-i");
        e("d+d+ya"); e("d+d+y", "d+d+ya"); e("d+d+yi"); e("d+d+yu"); e("d+d+ye"); e("d+d+yo"); e("d+d+yU"); e("d+d+yaM"); e("d+d+yau"); e("d+d+yai"); e("d+d+yI"); e("d+d+y-i");
        e("d+d+ra"); e("d+d+r", "d+d+ra"); e("d+d+ri"); e("d+d+ru"); e("d+d+re"); e("d+d+ro"); e("d+d+rU"); e("d+d+raM"); e("d+d+rau"); e("d+d+rai"); e("d+d+rI"); e("d+d+r-i");
        e("d+d+wa"); e("d+d+w", "d+d+wa"); e("d+d+wi"); e("d+d+wu"); e("d+d+we"); e("d+d+wo"); e("d+d+wU"); e("d+d+waM"); e("d+d+wau"); e("d+d+wai"); e("d+d+wI"); e("d+d+w-i");
        e("d+d+ha"); e("d+d+h", "d+d+ha"); e("d+d+hi"); e("d+d+hu"); e("d+d+he"); e("d+d+ho"); e("d+d+hU"); e("d+d+haM"); e("d+d+hau"); e("d+d+hai"); e("d+d+hI"); e("d+d+h-i");
        e("d+d+h+na"); e("d+d+h+n", "d+d+h+na"); e("d+d+h+ni"); e("d+d+h+nu"); e("d+d+h+ne"); e("d+d+h+no"); e("d+d+h+nU"); e("d+d+h+naM"); e("d+d+h+nau"); e("d+d+h+nai"); e("d+d+h+nI"); e("d+d+h+n-i");
        e("d+d+h+ya"); e("d+d+h+y", "d+d+h+ya"); e("d+d+h+yi"); e("d+d+h+yu"); e("d+d+h+ye"); e("d+d+h+yo"); e("d+d+h+yU"); e("d+d+h+yaM"); e("d+d+h+yau"); e("d+d+h+yai"); e("d+d+h+yI"); e("d+d+h+y-i");
        e("d+d+h+ra"); e("d+d+h+r", "d+d+h+ra"); e("d+d+h+ri"); e("d+d+h+ru"); e("d+d+h+re"); e("d+d+h+ro"); e("d+d+h+rU"); e("d+d+h+raM"); e("d+d+h+rau"); e("d+d+h+rai"); e("d+d+h+rI"); e("d+d+h+r-i");
        e("d+d+h+wa"); e("d+d+h+w", "d+d+h+wa"); e("d+d+h+wi"); e("d+d+h+wu"); e("d+d+h+we"); e("d+d+h+wo"); e("d+d+h+wU"); e("d+d+h+waM"); e("d+d+h+wau"); e("d+d+h+wai"); e("d+d+h+wI"); e("d+d+h+w-i");
        e("d+na"); e("d+n", "d+na"); e("d+ni"); e("d+nu"); e("d+ne"); e("d+no"); e("d+nU"); e("d+naM"); e("d+nau"); e("d+nai"); e("d+nI"); e("d+n-i");
        e("d+ba"); e("d+b", "d+ba"); e("d+bi"); e("d+bu"); e("d+be"); e("d+bo"); e("d+bU"); e("d+baM"); e("d+bau"); e("d+bai"); e("d+bI"); e("d+b-i");
        e("d+b+ra"); e("d+b+r", "d+b+ra"); e("d+b+ri"); e("d+b+ru"); e("d+b+re"); e("d+b+ro"); e("d+b+rU"); e("d+b+raM"); e("d+b+rau"); e("d+b+rai"); e("d+b+rI"); e("d+b+r-i");
        e("d+b+ha"); e("d+b+h", "d+b+ha"); e("d+b+hi"); e("d+b+hu"); e("d+b+he"); e("d+b+ho"); e("d+b+hU"); e("d+b+haM"); e("d+b+hau"); e("d+b+hai"); e("d+b+hI"); e("d+b+h-i");
        e("d+b+h+ya"); e("d+b+h+y", "d+b+h+ya"); e("d+b+h+yi"); e("d+b+h+yu"); e("d+b+h+ye"); e("d+b+h+yo"); e("d+b+h+yU"); e("d+b+h+yaM"); e("d+b+h+yau"); e("d+b+h+yai"); e("d+b+h+yI"); e("d+b+h+y-i");
        e("d+b+h+ra"); e("d+b+h+r", "d+b+h+ra"); e("d+b+h+ri"); e("d+b+h+ru"); e("d+b+h+re"); e("d+b+h+ro"); e("d+b+h+rU"); e("d+b+h+raM"); e("d+b+h+rau"); e("d+b+h+rai"); e("d+b+h+rI"); e("d+b+h+r-i");
        e("d+ma"); e("d+m", "d+ma"); e("d+mi"); e("d+mu"); e("d+me"); e("d+mo"); e("d+mU"); e("d+maM"); e("d+mau"); e("d+mai"); e("d+mI"); e("d+m-i");
        e("d+ya"); e("d+y", "d+ya"); e("d+yi"); e("d+yu"); e("d+ye"); e("d+yo"); e("d+yU"); e("d+yaM"); e("d+yau"); e("d+yai"); e("d+yI"); e("d+y-i");
        e("d+r+ya"); e("d+r+y", "d+r+ya"); e("d+r+yi"); e("d+r+yu"); e("d+r+ye"); e("d+r+yo"); e("d+r+yU"); e("d+r+yaM"); e("d+r+yau"); e("d+r+yai"); e("d+r+yI"); e("d+r+y-i");
        e("d+w+ya"); e("d+w+y", "d+w+ya"); e("d+w+yi"); e("d+w+yu"); e("d+w+ye"); e("d+w+yo"); e("d+w+yU"); e("d+w+yaM"); e("d+w+yau"); e("d+w+yai"); e("d+w+yI"); e("d+w+y-i");
        e("d+ha"); e("d+h", "d+ha"); e("d+hi"); e("d+hu"); e("d+he"); e("d+ho"); e("d+hU"); e("d+haM"); e("d+hau"); e("d+hai"); e("d+hI"); e("d+h-i");
        e("d+h+na"); e("d+h+n", "d+h+na"); e("d+h+ni"); e("d+h+nu"); e("d+h+ne"); e("d+h+no"); e("d+h+nU"); e("d+h+naM"); e("d+h+nau"); e("d+h+nai"); e("d+h+nI"); e("d+h+n-i");
        e("d+h+n+ya"); e("d+h+n+y", "d+h+n+ya"); e("d+h+n+yi"); e("d+h+n+yu"); e("d+h+n+ye"); e("d+h+n+yo"); e("d+h+n+yU"); e("d+h+n+yaM"); e("d+h+n+yau"); e("d+h+n+yai"); e("d+h+n+yI"); e("d+h+n+y-i");
        e("d+h+ma"); e("d+h+m", "d+h+ma"); e("d+h+mi"); e("d+h+mu"); e("d+h+me"); e("d+h+mo"); e("d+h+mU"); e("d+h+maM"); e("d+h+mau"); e("d+h+mai"); e("d+h+mI"); e("d+h+m-i");
        e("d+h+ya"); e("d+h+y", "d+h+ya"); e("d+h+yi"); e("d+h+yu"); e("d+h+ye"); e("d+h+yo"); e("d+h+yU"); e("d+h+yaM"); e("d+h+yau"); e("d+h+yai"); e("d+h+yI"); e("d+h+y-i");
        e("d+h+ra"); e("d+h+r", "d+h+ra"); e("d+h+ri"); e("d+h+ru"); e("d+h+re"); e("d+h+ro"); e("d+h+rU"); e("d+h+raM"); e("d+h+rau"); e("d+h+rai"); e("d+h+rI"); e("d+h+r-i");
        e("d+h+r+ya"); e("d+h+r+y", "d+h+r+ya"); e("d+h+r+yi"); e("d+h+r+yu"); e("d+h+r+ye"); e("d+h+r+yo"); e("d+h+r+yU"); e("d+h+r+yaM"); e("d+h+r+yau"); e("d+h+r+yai"); e("d+h+r+yI"); e("d+h+r+y-i");
        e("d+h+wa"); e("d+h+w", "d+h+wa"); e("d+h+wi"); e("d+h+wu"); e("d+h+we"); e("d+h+wo"); e("d+h+wU"); e("d+h+waM"); e("d+h+wau"); e("d+h+wai"); e("d+h+wI"); e("d+h+w-i");
        e("n+ka"); e("n+k", "n+ka"); e("n+ki"); e("n+ku"); e("n+ke"); e("n+ko"); e("n+kU"); e("n+kaM"); e("n+kau"); e("n+kai"); e("n+kI"); e("n+k-i");
        e("n+k+ta"); e("n+k+t", "n+k+ta"); e("n+k+ti"); e("n+k+tu"); e("n+k+te"); e("n+k+to"); e("n+k+tU"); e("n+k+taM"); e("n+k+tau"); e("n+k+tai"); e("n+k+tI"); e("n+k+t-i");
        e("n+g+ha"); e("n+g+h", "n+g+ha"); e("n+g+hi"); e("n+g+hu"); e("n+g+he"); e("n+g+ho"); e("n+g+hU"); e("n+g+haM"); e("n+g+hau"); e("n+g+hai"); e("n+g+hI"); e("n+g+h-i");
        e("n+nga"); e("n+ng", "n+nga"); e("n+ngi"); e("n+ngu"); e("n+nge"); e("n+ngo"); e("n+ngU"); e("n+ngaM"); e("n+ngau"); e("n+ngai"); e("n+ngI"); e("n+ng-i");
        e("n+dza"); e("n+dz", "n+dza"); e("n+dzi"); e("n+dzu"); e("n+dze"); e("n+dzo"); e("n+dzU"); e("n+dzaM"); e("n+dzau"); e("n+dzai"); e("n+dzI"); e("n+dz-i");
        e("n+dz+ya"); e("n+dz+y", "n+dz+ya"); e("n+dz+yi"); e("n+dz+yu"); e("n+dz+ye"); e("n+dz+yo"); e("n+dz+yU"); e("n+dz+yaM"); e("n+dz+yau"); e("n+dz+yai"); e("n+dz+yI"); e("n+dz+y-i");
        e("n+Da"); e("n+D", "n+Da"); e("n+Di"); e("n+Du"); e("n+De"); e("n+Do"); e("n+DU"); e("n+DaM"); e("n+Dau"); e("n+Dai"); e("n+DI"); e("n+D-i");
        e("n+ta"); e("n+t", "n+ta"); e("n+ti"); e("n+tu"); e("n+te"); e("n+to"); e("n+tU"); e("n+taM"); e("n+tau"); e("n+tai"); e("n+tI"); e("n+t-i");
        e("n+t+ya"); e("n+t+y", "n+t+ya"); e("n+t+yi"); e("n+t+yu"); e("n+t+ye"); e("n+t+yo"); e("n+t+yU"); e("n+t+yaM"); e("n+t+yau"); e("n+t+yai"); e("n+t+yI"); e("n+t+y-i");
        e("n+t+ra"); e("n+t+r", "n+t+ra"); e("n+t+ri"); e("n+t+ru"); e("n+t+re"); e("n+t+ro"); e("n+t+rU"); e("n+t+raM"); e("n+t+rau"); e("n+t+rai"); e("n+t+rI"); e("n+t+r-i");
        e("n+t+r+ya"); e("n+t+r+y", "n+t+r+ya"); e("n+t+r+yi"); e("n+t+r+yu"); e("n+t+r+ye"); e("n+t+r+yo"); e("n+t+r+yU"); e("n+t+r+yaM"); e("n+t+r+yau"); e("n+t+r+yai"); e("n+t+r+yI"); e("n+t+r+y-i");
        e("n+t+wa"); e("n+t+w", "n+t+wa"); e("n+t+wi"); e("n+t+wu"); e("n+t+we"); e("n+t+wo"); e("n+t+wU"); e("n+t+waM"); e("n+t+wau"); e("n+t+wai"); e("n+t+wI"); e("n+t+w-i");
        e("n+t+sa"); e("n+t+s", "n+t+sa"); e("n+t+si"); e("n+t+su"); e("n+t+se"); e("n+t+so"); e("n+t+sU"); e("n+t+saM"); e("n+t+sau"); e("n+t+sai"); e("n+t+sI"); e("n+t+s-i");
        e("n+tha"); e("n+th", "n+tha"); e("n+thi"); e("n+thu"); e("n+the"); e("n+tho"); e("n+thU"); e("n+thaM"); e("n+thau"); e("n+thai"); e("n+thI"); e("n+th-i");
        e("n+da"); e("n+d", "n+da"); e("n+di"); e("n+du"); e("n+de"); e("n+do"); e("n+dU"); e("n+daM"); e("n+dau"); e("n+dai"); e("n+dI"); e("n+d-i");
        e("n+d+da"); e("n+d+d", "n+d+da"); e("n+d+di"); e("n+d+du"); e("n+d+de"); e("n+d+do"); e("n+d+dU"); e("n+d+daM"); e("n+d+dau"); e("n+d+dai"); e("n+d+dI"); e("n+d+d-i");
        e("n+d+d+ra"); e("n+d+d+r", "n+d+d+ra"); e("n+d+d+ri"); e("n+d+d+ru"); e("n+d+d+re"); e("n+d+d+ro"); e("n+d+d+rU"); e("n+d+d+raM"); e("n+d+d+rau"); e("n+d+d+rai"); e("n+d+d+rI"); e("n+d+d+r-i");
        e("n+d+ya"); e("n+d+y", "n+d+ya"); e("n+d+yi"); e("n+d+yu"); e("n+d+ye"); e("n+d+yo"); e("n+d+yU"); e("n+d+yaM"); e("n+d+yau"); e("n+d+yai"); e("n+d+yI"); e("n+d+y-i");
        e("n+d+ra"); e("n+d+r", "n+d+ra"); e("n+d+ri"); e("n+d+ru"); e("n+d+re"); e("n+d+ro"); e("n+d+rU"); e("n+d+raM"); e("n+d+rau"); e("n+d+rai"); e("n+d+rI"); e("n+d+r-i");
        e("n+d+ha"); e("n+d+h", "n+d+ha"); e("n+d+hi"); e("n+d+hu"); e("n+d+he"); e("n+d+ho"); e("n+d+hU"); e("n+d+haM"); e("n+d+hau"); e("n+d+hai"); e("n+d+hI"); e("n+d+h-i");
        e("n+d+h+ra"); e("n+d+h+r", "n+d+h+ra"); e("n+d+h+ri"); e("n+d+h+ru"); e("n+d+h+re"); e("n+d+h+ro"); e("n+d+h+rU"); e("n+d+h+raM"); e("n+d+h+rau"); e("n+d+h+rai"); e("n+d+h+rI"); e("n+d+h+r-i");
        e("n+d+h+ya"); e("n+d+h+y", "n+d+h+ya"); e("n+d+h+yi"); e("n+d+h+yu"); e("n+d+h+ye"); e("n+d+h+yo"); e("n+d+h+yU"); e("n+d+h+yaM"); e("n+d+h+yau"); e("n+d+h+yai"); e("n+d+h+yI"); e("n+d+h+y-i");
        e("n+na"); e("n+n", "n+na"); e("n+ni"); e("n+nu"); e("n+ne"); e("n+no"); e("n+nU"); e("n+naM"); e("n+nau"); e("n+nai"); e("n+nI"); e("n+n-i");
        e("n+n+ya"); e("n+n+y", "n+n+ya"); e("n+n+yi"); e("n+n+yu"); e("n+n+ye"); e("n+n+yo"); e("n+n+yU"); e("n+n+yaM"); e("n+n+yau"); e("n+n+yai"); e("n+n+yI"); e("n+n+y-i");
        e("n+pa"); e("n+p", "n+pa"); e("n+pi"); e("n+pu"); e("n+pe"); e("n+po"); e("n+pU"); e("n+paM"); e("n+pau"); e("n+pai"); e("n+pI"); e("n+p-i");
        e("n+p+ra"); e("n+p+r", "n+p+ra"); e("n+p+ri"); e("n+p+ru"); e("n+p+re"); e("n+p+ro"); e("n+p+rU"); e("n+p+raM"); e("n+p+rau"); e("n+p+rai"); e("n+p+rI"); e("n+p+r-i");
        e("n+pha"); e("n+ph", "n+pha"); e("n+phi"); e("n+phu"); e("n+phe"); e("n+pho"); e("n+phU"); e("n+phaM"); e("n+phau"); e("n+phai"); e("n+phI"); e("n+ph-i");
        e("n+ma"); e("n+m", "n+ma"); e("n+mi"); e("n+mu"); e("n+me"); e("n+mo"); e("n+mU"); e("n+maM"); e("n+mau"); e("n+mai"); e("n+mI"); e("n+m-i");
        e("n+b+h+ya"); e("n+b+h+y", "n+b+h+ya"); e("n+b+h+yi"); e("n+b+h+yu"); e("n+b+h+ye"); e("n+b+h+yo"); e("n+b+h+yU"); e("n+b+h+yaM"); e("n+b+h+yau"); e("n+b+h+yai"); e("n+b+h+yI"); e("n+b+h+y-i");
        e("n+tsa"); e("n+ts", "n+tsa"); e("n+tsi"); e("n+tsu"); e("n+tse"); e("n+tso"); e("n+tsU"); e("n+tsaM"); e("n+tsau"); e("n+tsai"); e("n+tsI"); e("n+ts-i");
        e("n+ya"); e("n+y", "n+ya"); e("n+yi"); e("n+yu"); e("n+ye"); e("n+yo"); e("n+yU"); e("n+yaM"); e("n+yau"); e("n+yai"); e("n+yI"); e("n+y-i");
        e("n+ra"); e("n+r", "n+ra"); e("n+ri"); e("n+ru"); e("n+re"); e("n+ro"); e("n+rU"); e("n+raM"); e("n+rau"); e("n+rai"); e("n+rI"); e("n+r-i");
        e("n+wa"); e("n+w", "n+wa"); e("n+wi"); e("n+wu"); e("n+we"); e("n+wo"); e("n+wU"); e("n+waM"); e("n+wau"); e("n+wai"); e("n+wI"); e("n+w-i");
        e("n+w+ya"); e("n+w+y", "n+w+ya"); e("n+w+yi"); e("n+w+yu"); e("n+w+ye"); e("n+w+yo"); e("n+w+yU"); e("n+w+yaM"); e("n+w+yau"); e("n+w+yai"); e("n+w+yI"); e("n+w+y-i");
        e("n+sa"); e("n+s", "n+sa"); e("n+si"); e("n+su"); e("n+se"); e("n+so"); e("n+sU"); e("n+saM"); e("n+sau"); e("n+sai"); e("n+sI"); e("n+s-i");
        e("n+s+ya"); e("n+s+y", "n+s+ya"); e("n+s+yi"); e("n+s+yu"); e("n+s+ye"); e("n+s+yo"); e("n+s+yU"); e("n+s+yaM"); e("n+s+yau"); e("n+s+yai"); e("n+s+yI"); e("n+s+y-i");
        e("n+ha"); e("n+h", "n+ha"); e("n+hi"); e("n+hu"); e("n+he"); e("n+ho"); e("n+hU"); e("n+haM"); e("n+hau"); e("n+hai"); e("n+hI"); e("n+h-i");
        e("n+h+ra"); e("n+h+r", "n+h+ra"); e("n+h+ri"); e("n+h+ru"); e("n+h+re"); e("n+h+ro"); e("n+h+rU"); e("n+h+raM"); e("n+h+rau"); e("n+h+rai"); e("n+h+rI"); e("n+h+r-i");
        e("p+ta"); e("p+t", "p+ta"); e("p+ti"); e("p+tu"); e("p+te"); e("p+to"); e("p+tU"); e("p+taM"); e("p+tau"); e("p+tai"); e("p+tI"); e("p+t-i");
        e("p+t+ya"); e("p+t+y", "p+t+ya"); e("p+t+yi"); e("p+t+yu"); e("p+t+ye"); e("p+t+yo"); e("p+t+yU"); e("p+t+yaM"); e("p+t+yau"); e("p+t+yai"); e("p+t+yI"); e("p+t+y-i");
        e("p+t+r+ya"); e("p+t+r+y", "p+t+r+ya"); e("p+t+r+yi"); e("p+t+r+yu"); e("p+t+r+ye"); e("p+t+r+yo"); e("p+t+r+yU"); e("p+t+r+yaM"); e("p+t+r+yau"); e("p+t+r+yai"); e("p+t+r+yI"); e("p+t+r+y-i");
        e("p+da"); e("p+d", "p+da"); e("p+di"); e("p+du"); e("p+de"); e("p+do"); e("p+dU"); e("p+daM"); e("p+dau"); e("p+dai"); e("p+dI"); e("p+d-i");
        e("p+na"); e("p+n", "p+na"); e("p+ni"); e("p+nu"); e("p+ne"); e("p+no"); e("p+nU"); e("p+naM"); e("p+nau"); e("p+nai"); e("p+nI"); e("p+n-i");
        e("p+n+ya"); e("p+n+y", "p+n+ya"); e("p+n+yi"); e("p+n+yu"); e("p+n+ye"); e("p+n+yo"); e("p+n+yU"); e("p+n+yaM"); e("p+n+yau"); e("p+n+yai"); e("p+n+yI"); e("p+n+y-i");
        e("p+pa"); e("p+p", "p+pa"); e("p+pi"); e("p+pu"); e("p+pe"); e("p+po"); e("p+pU"); e("p+paM"); e("p+pau"); e("p+pai"); e("p+pI"); e("p+p-i");
        e("p+ma"); e("p+m", "p+ma"); e("p+mi"); e("p+mu"); e("p+me"); e("p+mo"); e("p+mU"); e("p+maM"); e("p+mau"); e("p+mai"); e("p+mI"); e("p+m-i");
        e("p+la"); e("p+l", "p+la"); e("p+li"); e("p+lu"); e("p+le"); e("p+lo"); e("p+lU"); e("p+laM"); e("p+lau"); e("p+lai"); e("p+lI"); e("p+l-i");
        e("p+wa"); e("p+w", "p+wa"); e("p+wi"); e("p+wu"); e("p+we"); e("p+wo"); e("p+wU"); e("p+waM"); e("p+wau"); e("p+wai"); e("p+wI"); e("p+w-i");
        e("p+sa"); e("p+s", "p+sa"); e("p+si"); e("p+su"); e("p+se"); e("p+so"); e("p+sU"); e("p+saM"); e("p+sau"); e("p+sai"); e("p+sI"); e("p+s-i");
        e("p+s+n+ya"); e("p+s+n+y", "p+s+n+ya"); e("p+s+n+yi"); e("p+s+n+yu"); e("p+s+n+ye"); e("p+s+n+yo"); e("p+s+n+yU"); e("p+s+n+yaM"); e("p+s+n+yau"); e("p+s+n+yai"); e("p+s+n+yI"); e("p+s+n+y-i");
        e("p+s+wa"); e("p+s+w", "p+s+wa"); e("p+s+wi"); e("p+s+wu"); e("p+s+we"); e("p+s+wo"); e("p+s+wU"); e("p+s+waM"); e("p+s+wau"); e("p+s+wai"); e("p+s+wI"); e("p+s+w-i");
        e("p+s+ya"); e("p+s+y", "p+s+ya"); e("p+s+yi"); e("p+s+yu"); e("p+s+ye"); e("p+s+yo"); e("p+s+yU"); e("p+s+yaM"); e("p+s+yau"); e("p+s+yai"); e("p+s+yI"); e("p+s+y-i");
        e("b+g+ha"); e("b+g+h", "b+g+ha"); e("b+g+hi"); e("b+g+hu"); e("b+g+he"); e("b+g+ho"); e("b+g+hU"); e("b+g+haM"); e("b+g+hau"); e("b+g+hai"); e("b+g+hI"); e("b+g+h-i");
        e("b+dza"); e("b+dz", "b+dza"); e("b+dzi"); e("b+dzu"); e("b+dze"); e("b+dzo"); e("b+dzU"); e("b+dzaM"); e("b+dzau"); e("b+dzai"); e("b+dzI"); e("b+dz-i");
        e("b+da"); e("b+d", "b+da"); e("b+di"); e("b+du"); e("b+de"); e("b+do"); e("b+dU"); e("b+daM"); e("b+dau"); e("b+dai"); e("b+dI"); e("b+d-i");
        e("b+d+dza"); e("b+d+dz", "b+d+dza"); e("b+d+dzi"); e("b+d+dzu"); e("b+d+dze"); e("b+d+dzo"); e("b+d+dzU"); e("b+d+dzaM"); e("b+d+dzau"); e("b+d+dzai"); e("b+d+dzI"); e("b+d+dz-i");
        e("b+d+ha"); e("b+d+h", "b+d+ha"); e("b+d+hi"); e("b+d+hu"); e("b+d+he"); e("b+d+ho"); e("b+d+hU"); e("b+d+haM"); e("b+d+hau"); e("b+d+hai"); e("b+d+hI"); e("b+d+h-i");
        e("b+d+h+wa"); e("b+d+h+w", "b+d+h+wa"); e("b+d+h+wi"); e("b+d+h+wu"); e("b+d+h+we"); e("b+d+h+wo"); e("b+d+h+wU"); e("b+d+h+waM"); e("b+d+h+wau"); e("b+d+h+wai"); e("b+d+h+wI"); e("b+d+h+w-i");
        e("b+ta"); e("b+t", "b+ta"); e("b+ti"); e("b+tu"); e("b+te"); e("b+to"); e("b+tU"); e("b+taM"); e("b+tau"); e("b+tai"); e("b+tI"); e("b+t-i");
        e("b+na"); e("b+n", "b+na"); e("b+ni"); e("b+nu"); e("b+ne"); e("b+no"); e("b+nU"); e("b+naM"); e("b+nau"); e("b+nai"); e("b+nI"); e("b+n-i");
        e("b+ba"); e("b+b", "b+ba"); e("b+bi"); e("b+bu"); e("b+be"); e("b+bo"); e("b+bU"); e("b+baM"); e("b+bau"); e("b+bai"); e("b+bI"); e("b+b-i");
        e("b+b+ha"); e("b+b+h", "b+b+ha"); e("b+b+hi"); e("b+b+hu"); e("b+b+he"); e("b+b+ho"); e("b+b+hU"); e("b+b+haM"); e("b+b+hau"); e("b+b+hai"); e("b+b+hI"); e("b+b+h-i");
        e("b+b+h+ya"); e("b+b+h+y", "b+b+h+ya"); e("b+b+h+yi"); e("b+b+h+yu"); e("b+b+h+ye"); e("b+b+h+yo"); e("b+b+h+yU"); e("b+b+h+yaM"); e("b+b+h+yau"); e("b+b+h+yai"); e("b+b+h+yI"); e("b+b+h+y-i");
        e("b+ma"); e("b+m", "b+ma"); e("b+mi"); e("b+mu"); e("b+me"); e("b+mo"); e("b+mU"); e("b+maM"); e("b+mau"); e("b+mai"); e("b+mI"); e("b+m-i");
        e("b+ha"); e("b+h", "b+ha"); e("b+hi"); e("b+hu"); e("b+he"); e("b+ho"); e("b+hU"); e("b+haM"); e("b+hau"); e("b+hai"); e("b+hI"); e("b+h-i");
        e("b+h+Na"); e("b+h+N", "b+h+Na"); e("b+h+Ni"); e("b+h+Nu"); e("b+h+Ne"); e("b+h+No"); e("b+h+NU"); e("b+h+NaM"); e("b+h+Nau"); e("b+h+Nai"); e("b+h+NI"); e("b+h+N-i");
        e("b+h+na"); e("b+h+n", "b+h+na"); e("b+h+ni"); e("b+h+nu"); e("b+h+ne"); e("b+h+no"); e("b+h+nU"); e("b+h+naM"); e("b+h+nau"); e("b+h+nai"); e("b+h+nI"); e("b+h+n-i");
        e("b+h+ma"); e("b+h+m", "b+h+ma"); e("b+h+mi"); e("b+h+mu"); e("b+h+me"); e("b+h+mo"); e("b+h+mU"); e("b+h+maM"); e("b+h+mau"); e("b+h+mai"); e("b+h+mI"); e("b+h+m-i");
        e("b+h+ya"); e("b+h+y", "b+h+ya"); e("b+h+yi"); e("b+h+yu"); e("b+h+ye"); e("b+h+yo"); e("b+h+yU"); e("b+h+yaM"); e("b+h+yau"); e("b+h+yai"); e("b+h+yI"); e("b+h+y-i");
        e("b+h+ra"); e("b+h+r", "b+h+ra"); e("b+h+ri"); e("b+h+ru"); e("b+h+re"); e("b+h+ro"); e("b+h+rU"); e("b+h+raM"); e("b+h+rau"); e("b+h+rai"); e("b+h+rI"); e("b+h+r-i");
        e("b+h+wa"); e("b+h+w", "b+h+wa"); e("b+h+wi"); e("b+h+wu"); e("b+h+we"); e("b+h+wo"); e("b+h+wU"); e("b+h+waM"); e("b+h+wau"); e("b+h+wai"); e("b+h+wI"); e("b+h+w-i");
        e("m+nya"); e("m+ny", "m+nya"); e("m+nyi"); e("m+nyu"); e("m+nye"); e("m+nyo"); e("m+nyU"); e("m+nyaM"); e("m+nyau"); e("m+nyai"); e("m+nyI"); e("m+ny-i");
        e("m+Na"); e("m+N", "m+Na"); e("m+Ni"); e("m+Nu"); e("m+Ne"); e("m+No"); e("m+NU"); e("m+NaM"); e("m+Nau"); e("m+Nai"); e("m+NI"); e("m+N-i");
        e("m+na"); e("m+n", "m+na"); e("m+ni"); e("m+nu"); e("m+ne"); e("m+no"); e("m+nU"); e("m+naM"); e("m+nau"); e("m+nai"); e("m+nI"); e("m+n-i");
        e("m+n+ya"); e("m+n+y", "m+n+ya"); e("m+n+yi"); e("m+n+yu"); e("m+n+ye"); e("m+n+yo"); e("m+n+yU"); e("m+n+yaM"); e("m+n+yau"); e("m+n+yai"); e("m+n+yI"); e("m+n+y-i");
        e("m+pa"); e("m+p", "m+pa"); e("m+pi"); e("m+pu"); e("m+pe"); e("m+po"); e("m+pU"); e("m+paM"); e("m+pau"); e("m+pai"); e("m+pI"); e("m+p-i");
        e("m+p+ra"); e("m+p+r", "m+p+ra"); e("m+p+ri"); e("m+p+ru"); e("m+p+re"); e("m+p+ro"); e("m+p+rU"); e("m+p+raM"); e("m+p+rau"); e("m+p+rai"); e("m+p+rI"); e("m+p+r-i");
        e("m+pha"); e("m+ph", "m+pha"); e("m+phi"); e("m+phu"); e("m+phe"); e("m+pho"); e("m+phU"); e("m+phaM"); e("m+phau"); e("m+phai"); e("m+phI"); e("m+ph-i");
        e("m+ba"); e("m+b", "m+ba"); e("m+bi"); e("m+bu"); e("m+be"); e("m+bo"); e("m+bU"); e("m+baM"); e("m+bau"); e("m+bai"); e("m+bI"); e("m+b-i");
        e("m+b+ha"); e("m+b+h", "m+b+ha"); e("m+b+hi"); e("m+b+hu"); e("m+b+he"); e("m+b+ho"); e("m+b+hU"); e("m+b+haM"); e("m+b+hau"); e("m+b+hai"); e("m+b+hI"); e("m+b+h-i");
        e("m+b+h+ya"); e("m+b+h+y", "m+b+h+ya"); e("m+b+h+yi"); e("m+b+h+yu"); e("m+b+h+ye"); e("m+b+h+yo"); e("m+b+h+yU"); e("m+b+h+yaM"); e("m+b+h+yau"); e("m+b+h+yai"); e("m+b+h+yI"); e("m+b+h+y-i");
        e("m+ma"); e("m+m", "m+ma"); e("m+mi"); e("m+mu"); e("m+me"); e("m+mo"); e("m+mU"); e("m+maM"); e("m+mau"); e("m+mai"); e("m+mI"); e("m+m-i");
        e("m+la"); e("m+l", "m+la"); e("m+li"); e("m+lu"); e("m+le"); e("m+lo"); e("m+lU"); e("m+laM"); e("m+lau"); e("m+lai"); e("m+lI"); e("m+l-i");
        e("m+wa"); e("m+w", "m+wa"); e("m+wi"); e("m+wu"); e("m+we"); e("m+wo"); e("m+wU"); e("m+waM"); e("m+wau"); e("m+wai"); e("m+wI"); e("m+w-i");
        e("m+sa"); e("m+s", "m+sa"); e("m+si"); e("m+su"); e("m+se"); e("m+so"); e("m+sU"); e("m+saM"); e("m+sau"); e("m+sai"); e("m+sI"); e("m+s-i");
        e("m+ha"); e("m+h", "m+ha"); e("m+hi"); e("m+hu"); e("m+he"); e("m+ho"); e("m+hU"); e("m+haM"); e("m+hau"); e("m+hai"); e("m+hI"); e("m+h-i");
        e("y+Ya"); e("y+Y", "y+Ya"); e("y+Yi"); e("y+Yu"); e("y+Ye"); e("y+Yo"); e("y+YU"); e("y+YaM"); e("y+Yau"); e("y+Yai"); e("y+YI"); e("y+Y-i");
        e("y+ra"); e("y+r", "y+ra"); e("y+ri"); e("y+ru"); e("y+re"); e("y+ro"); e("y+rU"); e("y+raM"); e("y+rau"); e("y+rai"); e("y+rI"); e("y+r-i");
        e("y+wa"); e("y+w", "y+wa"); e("y+wi"); e("y+wu"); e("y+we"); e("y+wo"); e("y+wU"); e("y+waM"); e("y+wau"); e("y+wai"); e("y+wI"); e("y+w-i");
        e("y+sa"); e("y+s", "y+sa"); e("y+si"); e("y+su"); e("y+se"); e("y+so"); e("y+sU"); e("y+saM"); e("y+sau"); e("y+sai"); e("y+sI"); e("y+s-i");
        e("r+kha"); e("r+kh", "r+kha"); e("r+khi"); e("r+khu"); e("r+khe"); e("r+kho"); e("r+khU"); e("r+khaM"); e("r+khau"); e("r+khai"); e("r+khI"); e("r+kh-i");
        e("r+g+ha"); e("r+g+h", "r+g+ha"); e("r+g+hi"); e("r+g+hu"); e("r+g+he"); e("r+g+ho"); e("r+g+hU"); e("r+g+haM"); e("r+g+hau"); e("r+g+hai"); e("r+g+hI"); e("r+g+h-i");
        e("r+g+h+ya"); e("r+g+h+y", "r+g+h+ya"); e("r+g+h+yi"); e("r+g+h+yu"); e("r+g+h+ye"); e("r+g+h+yo"); e("r+g+h+yU"); e("r+g+h+yaM"); e("r+g+h+yau"); e("r+g+h+yai"); e("r+g+h+yI"); e("r+g+h+y-i");
        e("r+ts+ya"); e("r+ts+y", "r+ts+ya"); e("r+ts+yi"); e("r+ts+yu"); e("r+ts+ye"); e("r+ts+yo"); e("r+ts+yU"); e("r+ts+yaM"); e("r+ts+yau"); e("r+ts+yai"); e("r+ts+yI"); e("r+ts+y-i");
        e("r+tsha"); e("r+tsh", "r+tsha"); e("r+tshi"); e("r+tshu"); e("r+tshe"); e("r+tsho"); e("r+tshU"); e("r+tshaM"); e("r+tshau"); e("r+tshai"); e("r+tshI"); e("r+tsh-i");
        e("r+dz+nya"); e("r+dz+ny", "r+dz+nya"); e("r+dz+nyi"); e("r+dz+nyu"); e("r+dz+nye"); e("r+dz+nyo"); e("r+dz+nyU"); e("r+dz+nyaM"); e("r+dz+nyau"); e("r+dz+nyai"); e("r+dz+nyI"); e("r+dz+ny-i");
        e("r+dz+ya"); e("r+dz+y", "r+dz+ya"); e("r+dz+yi"); e("r+dz+yu"); e("r+dz+ye"); e("r+dz+yo"); e("r+dz+yU"); e("r+dz+yaM"); e("r+dz+yau"); e("r+dz+yai"); e("r+dz+yI"); e("r+dz+y-i");
        e("r+Ta"); e("r+T", "r+Ta"); e("r+Ti"); e("r+Tu"); e("r+Te"); e("r+To"); e("r+TU"); e("r+TaM"); e("r+Tau"); e("r+Tai"); e("r+TI"); e("r+T-i");
        e("r+Tha"); e("r+Th", "r+Tha"); e("r+Thi"); e("r+Thu"); e("r+The"); e("r+Tho"); e("r+ThU"); e("r+ThaM"); e("r+Thau"); e("r+Thai"); e("r+ThI"); e("r+Th-i");
        e("r+Da"); e("r+D", "r+Da"); e("r+Di"); e("r+Du"); e("r+De"); e("r+Do"); e("r+DU"); e("r+DaM"); e("r+Dau"); e("r+Dai"); e("r+DI"); e("r+D-i");
        e("r+Na"); e("r+N", "r+Na"); e("r+Ni"); e("r+Nu"); e("r+Ne"); e("r+No"); e("r+NU"); e("r+NaM"); e("r+Nau"); e("r+Nai"); e("r+NI"); e("r+N-i");
        e("r+t+wa"); e("r+t+w", "r+t+wa"); e("r+t+wi"); e("r+t+wu"); e("r+t+we"); e("r+t+wo"); e("r+t+wU"); e("r+t+waM"); e("r+t+wau"); e("r+t+wai"); e("r+t+wI"); e("r+t+w-i");
        e("r+t+ta"); e("r+t+t", "r+t+ta"); e("r+t+ti"); e("r+t+tu"); e("r+t+te"); e("r+t+to"); e("r+t+tU"); e("r+t+taM"); e("r+t+tau"); e("r+t+tai"); e("r+t+tI"); e("r+t+t-i");
        e("r+t+sa"); e("r+t+s", "r+t+sa"); e("r+t+si"); e("r+t+su"); e("r+t+se"); e("r+t+so"); e("r+t+sU"); e("r+t+saM"); e("r+t+sau"); e("r+t+sai"); e("r+t+sI"); e("r+t+s-i");
        e("r+t+s+na"); e("r+t+s+n", "r+t+s+na"); e("r+t+s+ni"); e("r+t+s+nu"); e("r+t+s+ne"); e("r+t+s+no"); e("r+t+s+nU"); e("r+t+s+naM"); e("r+t+s+nau"); e("r+t+s+nai"); e("r+t+s+nI"); e("r+t+s+n-i");
        e("r+t+s+n+ya"); e("r+t+s+n+y", "r+t+s+n+ya"); e("r+t+s+n+yi"); e("r+t+s+n+yu"); e("r+t+s+n+ye"); e("r+t+s+n+yo"); e("r+t+s+n+yU"); e("r+t+s+n+yaM"); e("r+t+s+n+yau"); e("r+t+s+n+yai"); e("r+t+s+n+yI"); e("r+t+s+n+y-i");
        e("r+tha"); e("r+th", "r+tha"); e("r+thi"); e("r+thu"); e("r+the"); e("r+tho"); e("r+thU"); e("r+thaM"); e("r+thau"); e("r+thai"); e("r+thI"); e("r+th-i");
        e("r+th+ya"); e("r+th+y", "r+th+ya"); e("r+th+yi"); e("r+th+yu"); e("r+th+ye"); e("r+th+yo"); e("r+th+yU"); e("r+th+yaM"); e("r+th+yau"); e("r+th+yai"); e("r+th+yI"); e("r+th+y-i");
        e("r+d+d+ha"); e("r+d+d+h", "r+d+d+ha"); e("r+d+d+hi"); e("r+d+d+hu"); e("r+d+d+he"); e("r+d+d+ho"); e("r+d+d+hU"); e("r+d+d+haM"); e("r+d+d+hau"); e("r+d+d+hai"); e("r+d+d+hI"); e("r+d+d+h-i");
        e("r+d+d+h+ya"); e("r+d+d+h+y", "r+d+d+h+ya"); e("r+d+d+h+yi"); e("r+d+d+h+yu"); e("r+d+d+h+ye"); e("r+d+d+h+yo"); e("r+d+d+h+yU"); e("r+d+d+h+yaM"); e("r+d+d+h+yau"); e("r+d+d+h+yai"); e("r+d+d+h+yI"); e("r+d+d+h+y-i");
        e("r+d+ya"); e("r+d+y", "r+d+ya"); e("r+d+yi"); e("r+d+yu"); e("r+d+ye"); e("r+d+yo"); e("r+d+yU"); e("r+d+yaM"); e("r+d+yau"); e("r+d+yai"); e("r+d+yI"); e("r+d+y-i");
        e("r+d+ha"); e("r+d+h", "r+d+ha"); e("r+d+hi"); e("r+d+hu"); e("r+d+he"); e("r+d+ho"); e("r+d+hU"); e("r+d+haM"); e("r+d+hau"); e("r+d+hai"); e("r+d+hI"); e("r+d+h-i");
        e("r+d+h+ma"); e("r+d+h+m", "r+d+h+ma"); e("r+d+h+mi"); e("r+d+h+mu"); e("r+d+h+me"); e("r+d+h+mo"); e("r+d+h+mU"); e("r+d+h+maM"); e("r+d+h+mau"); e("r+d+h+mai"); e("r+d+h+mI"); e("r+d+h+m-i");
        e("r+d+h+ya"); e("r+d+h+y", "r+d+h+ya"); e("r+d+h+yi"); e("r+d+h+yu"); e("r+d+h+ye"); e("r+d+h+yo"); e("r+d+h+yU"); e("r+d+h+yaM"); e("r+d+h+yau"); e("r+d+h+yai"); e("r+d+h+yI"); e("r+d+h+y-i");
        e("r+d+h+ra"); e("r+d+h+r", "r+d+h+ra"); e("r+d+h+ri"); e("r+d+h+ru"); e("r+d+h+re"); e("r+d+h+ro"); e("r+d+h+rU"); e("r+d+h+raM"); e("r+d+h+rau"); e("r+d+h+rai"); e("r+d+h+rI"); e("r+d+h+r-i");
        e("r+pa"); e("r+p", "r+pa"); e("r+pi"); e("r+pu"); e("r+pe"); e("r+po"); e("r+pU"); e("r+paM"); e("r+pau"); e("r+pai"); e("r+pI"); e("r+p-i");
        e("r+b+pa"); e("r+b+p", "r+b+pa"); e("r+b+pi"); e("r+b+pu"); e("r+b+pe"); e("r+b+po"); e("r+b+pU"); e("r+b+paM"); e("r+b+pau"); e("r+b+pai"); e("r+b+pI"); e("r+b+p-i");
        e("r+b+ba"); e("r+b+b", "r+b+ba"); e("r+b+bi"); e("r+b+bu"); e("r+b+be"); e("r+b+bo"); e("r+b+bU"); e("r+b+baM"); e("r+b+bau"); e("r+b+bai"); e("r+b+bI"); e("r+b+b-i");
        e("r+b+ha"); e("r+b+h", "r+b+ha"); e("r+b+hi"); e("r+b+hu"); e("r+b+he"); e("r+b+ho"); e("r+b+hU"); e("r+b+haM"); e("r+b+hau"); e("r+b+hai"); e("r+b+hI"); e("r+b+h-i");
        e("r+m+ma"); e("r+m+m", "r+m+ma"); e("r+m+mi"); e("r+m+mu"); e("r+m+me"); e("r+m+mo"); e("r+m+mU"); e("r+m+maM"); e("r+m+mau"); e("r+m+mai"); e("r+m+mI"); e("r+m+m-i");
        e("R+Ya"); e("R+Y", "R+Ya"); e("R+Yi"); e("R+Yu"); e("R+Ye"); e("R+Yo"); e("R+YU"); e("R+YaM"); e("R+Yau"); e("R+Yai"); e("R+YI"); e("R+Y-i");
        e("R+Wa"); e("R+W", "R+Wa"); e("R+Wi"); e("R+Wu"); e("R+We"); e("R+Wo"); e("R+WU"); e("R+WaM"); e("R+Wau"); e("R+Wai"); e("R+WI"); e("R+W-i");
        e("R+sha"); e("R+sh", "R+sha"); e("R+shi"); e("R+shu"); e("R+she"); e("R+sho"); e("R+shU"); e("R+shaM"); e("R+shau"); e("R+shai"); e("R+shI"); e("R+sh-i");
        e("R+sh+ya"); e("R+sh+y", "R+sh+ya"); e("R+sh+yi"); e("R+sh+yu"); e("R+sh+ye"); e("R+sh+yo"); e("R+sh+yU"); e("R+sh+yaM"); e("R+sh+yau"); e("R+sh+yai"); e("R+sh+yI"); e("R+sh+y-i");
        e("R+Sha"); e("R+Sh", "R+Sha"); e("R+Shi"); e("R+Shu"); e("R+She"); e("R+Sho"); e("R+ShU"); e("R+ShaM"); e("R+Shau"); e("R+Shai"); e("R+ShI"); e("R+Sh-i");
        e("R+Sh+Na"); e("R+Sh+N", "R+Sh+Na"); e("R+Sh+Ni"); e("R+Sh+Nu"); e("R+Sh+Ne"); e("R+Sh+No"); e("R+Sh+NU"); e("R+Sh+NaM"); e("R+Sh+Nau"); e("R+Sh+Nai"); e("R+Sh+NI"); e("R+Sh+N-i");
        e("R+Sh+N+ya"); e("R+Sh+N+y", "R+Sh+N+ya"); e("R+Sh+N+yi"); e("R+Sh+N+yu"); e("R+Sh+N+ye"); e("R+Sh+N+yo"); e("R+Sh+N+yU"); e("R+Sh+N+yaM"); e("R+Sh+N+yau"); e("R+Sh+N+yai"); e("R+Sh+N+yI"); e("R+Sh+N+y-i");
        e("R+Sh+ma"); e("R+Sh+m", "R+Sh+ma"); e("R+Sh+mi"); e("R+Sh+mu"); e("R+Sh+me"); e("R+Sh+mo"); e("R+Sh+mU"); e("R+Sh+maM"); e("R+Sh+mau"); e("R+Sh+mai"); e("R+Sh+mI"); e("R+Sh+m-i");
        e("R+Sh+ya"); e("R+Sh+y", "R+Sh+ya"); e("R+Sh+yi"); e("R+Sh+yu"); e("R+Sh+ye"); e("R+Sh+yo"); e("R+Sh+yU"); e("R+Sh+yaM"); e("R+Sh+yau"); e("R+Sh+yai"); e("R+Sh+yI"); e("R+Sh+y-i");
        e("R+sa"); e("R+s", "R+sa"); e("R+si"); e("R+su"); e("R+se"); e("R+so"); e("R+sU"); e("R+saM"); e("R+sau"); e("R+sai"); e("R+sI"); e("R+s-i");
        e("r+ha"); e("r+h", "r+ha"); e("r+hi"); e("r+hu"); e("r+he"); e("r+ho"); e("r+hU"); e("r+haM"); e("r+hau"); e("r+hai"); e("r+hI"); e("r+h-i");
        e("r+k+Sha"); e("r+k+Sh", "r+k+Sha"); e("r+k+Shi"); e("r+k+Shu"); e("r+k+She"); e("r+k+Sho"); e("r+k+ShU"); e("r+k+ShaM"); e("r+k+Shau"); e("r+k+Shai"); e("r+k+ShI"); e("r+k+Sh-i");
        e("l+g+wa"); e("l+g+w", "l+g+wa"); e("l+g+wi"); e("l+g+wu"); e("l+g+we"); e("l+g+wo"); e("l+g+wU"); e("l+g+waM"); e("l+g+wau"); e("l+g+wai"); e("l+g+wI"); e("l+g+w-i");
        e("l+b+ya"); e("l+b+y", "l+b+ya"); e("l+b+yi"); e("l+b+yu"); e("l+b+ye"); e("l+b+yo"); e("l+b+yU"); e("l+b+yaM"); e("l+b+yau"); e("l+b+yai"); e("l+b+yI"); e("l+b+y-i");
        e("l+ma"); e("l+m", "l+ma"); e("l+mi"); e("l+mu"); e("l+me"); e("l+mo"); e("l+mU"); e("l+maM"); e("l+mau"); e("l+mai"); e("l+mI"); e("l+m-i");
        e("l+ya"); e("l+y", "l+ya"); e("l+yi"); e("l+yu"); e("l+ye"); e("l+yo"); e("l+yU"); e("l+yaM"); e("l+yau"); e("l+yai"); e("l+yI"); e("l+y-i");
        e("l+wa"); e("l+w", "l+wa"); e("l+wi"); e("l+wu"); e("l+we"); e("l+wo"); e("l+wU"); e("l+waM"); e("l+wau"); e("l+wai"); e("l+wI"); e("l+w-i");
        e("l+la"); e("l+l", "l+la"); e("l+li"); e("l+lu"); e("l+le"); e("l+lo"); e("l+lU"); e("l+laM"); e("l+lau"); e("l+lai"); e("l+lI"); e("l+l-i");
        e("l+h+wa"); e("l+h+w", "l+h+wa"); e("l+h+wi"); e("l+h+wu"); e("l+h+we"); e("l+h+wo"); e("l+h+wU"); e("l+h+waM"); e("l+h+wau"); e("l+h+wai"); e("l+h+wI"); e("l+h+w-i");
        e("w+ya"); e("w+y", "w+ya"); e("w+yi"); e("w+yu"); e("w+ye"); e("w+yo"); e("w+yU"); e("w+yaM"); e("w+yau"); e("w+yai"); e("w+yI"); e("w+y-i");
        e("w+ra"); e("w+r", "w+ra"); e("w+ri"); e("w+ru"); e("w+re"); e("w+ro"); e("w+rU"); e("w+raM"); e("w+rau"); e("w+rai"); e("w+rI"); e("w+r-i");
        e("w+na"); e("w+n", "w+na"); e("w+ni"); e("w+nu"); e("w+ne"); e("w+no"); e("w+nU"); e("w+naM"); e("w+nau"); e("w+nai"); e("w+nI"); e("w+n-i");
        e("w+Wa"); e("w+W", "w+Wa"); e("w+Wi"); e("w+Wu"); e("w+We"); e("w+Wo"); e("w+WU"); e("w+WaM"); e("w+Wau"); e("w+Wai"); e("w+WI"); e("w+W-i");
        e("sh+tsa"); e("sh+ts", "sh+tsa"); e("sh+tsi"); e("sh+tsu"); e("sh+tse"); e("sh+tso"); e("sh+tsU"); e("sh+tsaM"); e("sh+tsau"); e("sh+tsai"); e("sh+tsI"); e("sh+ts-i");
        e("sh+ts+ya"); e("sh+ts+y", "sh+ts+ya"); e("sh+ts+yi"); e("sh+ts+yu"); e("sh+ts+ye"); e("sh+ts+yo"); e("sh+ts+yU"); e("sh+ts+yaM"); e("sh+ts+yau"); e("sh+ts+yai"); e("sh+ts+yI"); e("sh+ts+y-i");
        e("sh+tsha"); e("sh+tsh", "sh+tsha"); e("sh+tshi"); e("sh+tshu"); e("sh+tshe"); e("sh+tsho"); e("sh+tshU"); e("sh+tshaM"); e("sh+tshau"); e("sh+tshai"); e("sh+tshI"); e("sh+tsh-i");
        e("sh+Na"); e("sh+N", "sh+Na"); e("sh+Ni"); e("sh+Nu"); e("sh+Ne"); e("sh+No"); e("sh+NU"); e("sh+NaM"); e("sh+Nau"); e("sh+Nai"); e("sh+NI"); e("sh+N-i");
        e("sh+na"); e("sh+n", "sh+na"); e("sh+ni"); e("sh+nu"); e("sh+ne"); e("sh+no"); e("sh+nU"); e("sh+naM"); e("sh+nau"); e("sh+nai"); e("sh+nI"); e("sh+n-i");
        e("sh+pa"); e("sh+p", "sh+pa"); e("sh+pi"); e("sh+pu"); e("sh+pe"); e("sh+po"); e("sh+pU"); e("sh+paM"); e("sh+pau"); e("sh+pai"); e("sh+pI"); e("sh+p-i");
        e("sh+b+ya"); e("sh+b+y", "sh+b+ya"); e("sh+b+yi"); e("sh+b+yu"); e("sh+b+ye"); e("sh+b+yo"); e("sh+b+yU"); e("sh+b+yaM"); e("sh+b+yau"); e("sh+b+yai"); e("sh+b+yI"); e("sh+b+y-i");
        e("sh+ma"); e("sh+m", "sh+ma"); e("sh+mi"); e("sh+mu"); e("sh+me"); e("sh+mo"); e("sh+mU"); e("sh+maM"); e("sh+mau"); e("sh+mai"); e("sh+mI"); e("sh+m-i");
        e("sh+ya"); e("sh+y", "sh+ya"); e("sh+yi"); e("sh+yu"); e("sh+ye"); e("sh+yo"); e("sh+yU"); e("sh+yaM"); e("sh+yau"); e("sh+yai"); e("sh+yI"); e("sh+y-i");
        e("sh+r+ya"); e("sh+r+y", "sh+r+ya"); e("sh+r+yi"); e("sh+r+yu"); e("sh+r+ye"); e("sh+r+yo"); e("sh+r+yU"); e("sh+r+yaM"); e("sh+r+yau"); e("sh+r+yai"); e("sh+r+yI"); e("sh+r+y-i");
        e("sh+la"); e("sh+l", "sh+la"); e("sh+li"); e("sh+lu"); e("sh+le"); e("sh+lo"); e("sh+lU"); e("sh+laM"); e("sh+lau"); e("sh+lai"); e("sh+lI"); e("sh+l-i");
        e("sh+w+ga"); e("sh+w+g", "sh+w+ga"); e("sh+w+gi"); e("sh+w+gu"); e("sh+w+ge"); e("sh+w+go"); e("sh+w+gU"); e("sh+w+gaM"); e("sh+w+gau"); e("sh+w+gai"); e("sh+w+gI"); e("sh+w+g-i");
        e("sh+w+ya"); e("sh+w+y", "sh+w+ya"); e("sh+w+yi"); e("sh+w+yu"); e("sh+w+ye"); e("sh+w+yo"); e("sh+w+yU"); e("sh+w+yaM"); e("sh+w+yau"); e("sh+w+yai"); e("sh+w+yI"); e("sh+w+y-i");
        e("sh+sha"); e("sh+sh", "sh+sha"); e("sh+shi"); e("sh+shu"); e("sh+she"); e("sh+sho"); e("sh+shU"); e("sh+shaM"); e("sh+shau"); e("sh+shai"); e("sh+shI"); e("sh+sh-i");
        e("Sh+ka"); e("Sh+k", "Sh+ka"); e("Sh+ki"); e("Sh+ku"); e("Sh+ke"); e("Sh+ko"); e("Sh+kU"); e("Sh+kaM"); e("Sh+kau"); e("Sh+kai"); e("Sh+kI"); e("Sh+k-i");
        e("Sh+k+ra"); e("Sh+k+r", "Sh+k+ra"); e("Sh+k+ri"); e("Sh+k+ru"); e("Sh+k+re"); e("Sh+k+ro"); e("Sh+k+rU"); e("Sh+k+raM"); e("Sh+k+rau"); e("Sh+k+rai"); e("Sh+k+rI"); e("Sh+k+r-i");
        e("Sh+Ta"); e("Sh+T", "Sh+Ta"); e("Sh+Ti"); e("Sh+Tu"); e("Sh+Te"); e("Sh+To"); e("Sh+TU"); e("Sh+TaM"); e("Sh+Tau"); e("Sh+Tai"); e("Sh+TI"); e("Sh+T-i");
        e("Sh+T+ya"); e("Sh+T+y", "Sh+T+ya"); e("Sh+T+yi"); e("Sh+T+yu"); e("Sh+T+ye"); e("Sh+T+yo"); e("Sh+T+yU"); e("Sh+T+yaM"); e("Sh+T+yau"); e("Sh+T+yai"); e("Sh+T+yI"); e("Sh+T+y-i");
        e("Sh+T+ra"); e("Sh+T+r", "Sh+T+ra"); e("Sh+T+ri"); e("Sh+T+ru"); e("Sh+T+re"); e("Sh+T+ro"); e("Sh+T+rU"); e("Sh+T+raM"); e("Sh+T+rau"); e("Sh+T+rai"); e("Sh+T+rI"); e("Sh+T+r-i");
        e("Sh+T+r+ya"); e("Sh+T+r+y", "Sh+T+r+ya"); e("Sh+T+r+yi"); e("Sh+T+r+yu"); e("Sh+T+r+ye"); e("Sh+T+r+yo"); e("Sh+T+r+yU"); e("Sh+T+r+yaM"); e("Sh+T+r+yau"); e("Sh+T+r+yai"); e("Sh+T+r+yI"); e("Sh+T+r+y-i");
        e("Sh+T+wa"); e("Sh+T+w", "Sh+T+wa"); e("Sh+T+wi"); e("Sh+T+wu"); e("Sh+T+we"); e("Sh+T+wo"); e("Sh+T+wU"); e("Sh+T+waM"); e("Sh+T+wau"); e("Sh+T+wai"); e("Sh+T+wI"); e("Sh+T+w-i");
        e("Sh+Tha"); e("Sh+Th", "Sh+Tha"); e("Sh+Thi"); e("Sh+Thu"); e("Sh+The"); e("Sh+Tho"); e("Sh+ThU"); e("Sh+ThaM"); e("Sh+Thau"); e("Sh+Thai"); e("Sh+ThI"); e("Sh+Th-i");
        e("Sh+Th+ya"); e("Sh+Th+y", "Sh+Th+ya"); e("Sh+Th+yi"); e("Sh+Th+yu"); e("Sh+Th+ye"); e("Sh+Th+yo"); e("Sh+Th+yU"); e("Sh+Th+yaM"); e("Sh+Th+yau"); e("Sh+Th+yai"); e("Sh+Th+yI"); e("Sh+Th+y-i");
        e("Sh+Na"); e("Sh+N", "Sh+Na"); e("Sh+Ni"); e("Sh+Nu"); e("Sh+Ne"); e("Sh+No"); e("Sh+NU"); e("Sh+NaM"); e("Sh+Nau"); e("Sh+Nai"); e("Sh+NI"); e("Sh+N-i");
        e("Sh+N+ya"); e("Sh+N+y", "Sh+N+ya"); e("Sh+N+yi"); e("Sh+N+yu"); e("Sh+N+ye"); e("Sh+N+yo"); e("Sh+N+yU"); e("Sh+N+yaM"); e("Sh+N+yau"); e("Sh+N+yai"); e("Sh+N+yI"); e("Sh+N+y-i");
        e("Sh+Da"); e("Sh+D", "Sh+Da"); e("Sh+Di"); e("Sh+Du"); e("Sh+De"); e("Sh+Do"); e("Sh+DU"); e("Sh+DaM"); e("Sh+Dau"); e("Sh+Dai"); e("Sh+DI"); e("Sh+D-i");
        e("Sh+tha"); e("Sh+th", "Sh+tha"); e("Sh+thi"); e("Sh+thu"); e("Sh+the"); e("Sh+tho"); e("Sh+thU"); e("Sh+thaM"); e("Sh+thau"); e("Sh+thai"); e("Sh+thI"); e("Sh+th-i");
        e("Sh+pa"); e("Sh+p", "Sh+pa"); e("Sh+pi"); e("Sh+pu"); e("Sh+pe"); e("Sh+po"); e("Sh+pU"); e("Sh+paM"); e("Sh+pau"); e("Sh+pai"); e("Sh+pI"); e("Sh+p-i");
        e("Sh+p+ra"); e("Sh+p+r", "Sh+p+ra"); e("Sh+p+ri"); e("Sh+p+ru"); e("Sh+p+re"); e("Sh+p+ro"); e("Sh+p+rU"); e("Sh+p+raM"); e("Sh+p+rau"); e("Sh+p+rai"); e("Sh+p+rI"); e("Sh+p+r-i");
        e("Sh+ma"); e("Sh+m", "Sh+ma"); e("Sh+mi"); e("Sh+mu"); e("Sh+me"); e("Sh+mo"); e("Sh+mU"); e("Sh+maM"); e("Sh+mau"); e("Sh+mai"); e("Sh+mI"); e("Sh+m-i");
        e("Sh+ya"); e("Sh+y", "Sh+ya"); e("Sh+yi"); e("Sh+yu"); e("Sh+ye"); e("Sh+yo"); e("Sh+yU"); e("Sh+yaM"); e("Sh+yau"); e("Sh+yai"); e("Sh+yI"); e("Sh+y-i");
        e("Sh+wa"); e("Sh+w", "Sh+wa"); e("Sh+wi"); e("Sh+wu"); e("Sh+we"); e("Sh+wo"); e("Sh+wU"); e("Sh+waM"); e("Sh+wau"); e("Sh+wai"); e("Sh+wI"); e("Sh+w-i");
        e("Sh+Sha"); e("Sh+Sh", "Sh+Sha"); e("Sh+Shi"); e("Sh+Shu"); e("Sh+She"); e("Sh+Sho"); e("Sh+ShU"); e("Sh+ShaM"); e("Sh+Shau"); e("Sh+Shai"); e("Sh+ShI"); e("Sh+Sh-i");
        e("s+k+sa"); e("s+k+s", "s+k+sa"); e("s+k+si"); e("s+k+su"); e("s+k+se"); e("s+k+so"); e("s+k+sU"); e("s+k+saM"); e("s+k+sau"); e("s+k+sai"); e("s+k+sI"); e("s+k+s-i");
        e("s+kha"); e("s+kh", "s+kha"); e("s+khi"); e("s+khu"); e("s+khe"); e("s+kho"); e("s+khU"); e("s+khaM"); e("s+khau"); e("s+khai"); e("s+khI"); e("s+kh-i");
        e("s+ts+ya"); e("s+ts+y", "s+ts+ya"); e("s+ts+yi"); e("s+ts+yu"); e("s+ts+ye"); e("s+ts+yo"); e("s+ts+yU"); e("s+ts+yaM"); e("s+ts+yau"); e("s+ts+yai"); e("s+ts+yI"); e("s+ts+y-i");
        e("s+Ta"); e("s+T", "s+Ta"); e("s+Ti"); e("s+Tu"); e("s+Te"); e("s+To"); e("s+TU"); e("s+TaM"); e("s+Tau"); e("s+Tai"); e("s+TI"); e("s+T-i");
        e("s+Tha"); e("s+Th", "s+Tha"); e("s+Thi"); e("s+Thu"); e("s+The"); e("s+Tho"); e("s+ThU"); e("s+ThaM"); e("s+Thau"); e("s+Thai"); e("s+ThI"); e("s+Th-i");
        e("s+t+ya"); e("s+t+y", "s+t+ya"); e("s+t+yi"); e("s+t+yu"); e("s+t+ye"); e("s+t+yo"); e("s+t+yU"); e("s+t+yaM"); e("s+t+yau"); e("s+t+yai"); e("s+t+yI"); e("s+t+y-i");
        e("s+t+ra"); e("s+t+r", "s+t+ra"); e("s+t+ri"); e("s+t+ru"); e("s+t+re"); e("s+t+ro"); e("s+t+rU"); e("s+t+raM"); e("s+t+rau"); e("s+t+rai"); e("s+t+rI"); e("s+t+r-i");
        e("s+t+wa"); e("s+t+w", "s+t+wa"); e("s+t+wi"); e("s+t+wu"); e("s+t+we"); e("s+t+wo"); e("s+t+wU"); e("s+t+waM"); e("s+t+wau"); e("s+t+wai"); e("s+t+wI"); e("s+t+w-i");
        e("s+tha"); e("s+th", "s+tha"); e("s+thi"); e("s+thu"); e("s+the"); e("s+tho"); e("s+thU"); e("s+thaM"); e("s+thau"); e("s+thai"); e("s+thI"); e("s+th-i");
        e("s+th+ya"); e("s+th+y", "s+th+ya"); e("s+th+yi"); e("s+th+yu"); e("s+th+ye"); e("s+th+yo"); e("s+th+yU"); e("s+th+yaM"); e("s+th+yau"); e("s+th+yai"); e("s+th+yI"); e("s+th+y-i");
        e("s+n+ya"); e("s+n+y", "s+n+ya"); e("s+n+yi"); e("s+n+yu"); e("s+n+ye"); e("s+n+yo"); e("s+n+yU"); e("s+n+yaM"); e("s+n+yau"); e("s+n+yai"); e("s+n+yI"); e("s+n+y-i");
        e("s+n+wa"); e("s+n+w", "s+n+wa"); e("s+n+wi"); e("s+n+wu"); e("s+n+we"); e("s+n+wo"); e("s+n+wU"); e("s+n+waM"); e("s+n+wau"); e("s+n+wai"); e("s+n+wI"); e("s+n+w-i");
        e("s+pha"); e("s+ph", "s+pha"); e("s+phi"); e("s+phu"); e("s+phe"); e("s+pho"); e("s+phU"); e("s+phaM"); e("s+phau"); e("s+phai"); e("s+phI"); e("s+ph-i");
        e("s+ph+ya"); e("s+ph+y", "s+ph+ya"); e("s+ph+yi"); e("s+ph+yu"); e("s+ph+ye"); e("s+ph+yo"); e("s+ph+yU"); e("s+ph+yaM"); e("s+ph+yau"); e("s+ph+yai"); e("s+ph+yI"); e("s+ph+y-i");
        e("s+ya"); e("s+y", "s+ya"); e("s+yi"); e("s+yu"); e("s+ye"); e("s+yo"); e("s+yU"); e("s+yaM"); e("s+yau"); e("s+yai"); e("s+yI"); e("s+y-i");
        e("s+r+wa"); e("s+r+w", "s+r+wa"); e("s+r+wi"); e("s+r+wu"); e("s+r+we"); e("s+r+wo"); e("s+r+wU"); e("s+r+waM"); e("s+r+wau"); e("s+r+wai"); e("s+r+wI"); e("s+r+w-i");
        e("s+sa"); e("s+s", "s+sa"); e("s+si"); e("s+su"); e("s+se"); e("s+so"); e("s+sU"); e("s+saM"); e("s+sau"); e("s+sai"); e("s+sI"); e("s+s-i");
        e("s+s+wa"); e("s+s+w", "s+s+wa"); e("s+s+wi"); e("s+s+wu"); e("s+s+we"); e("s+s+wo"); e("s+s+wU"); e("s+s+waM"); e("s+s+wau"); e("s+s+wai"); e("s+s+wI"); e("s+s+w-i");
        e("s+ha"); e("s+h", "s+ha"); e("s+hi"); e("s+hu"); e("s+he"); e("s+ho"); e("s+hU"); e("s+haM"); e("s+hau"); e("s+hai"); e("s+hI"); e("s+h-i");
        e("s+w+ya"); e("s+w+y", "s+w+ya"); e("s+w+yi"); e("s+w+yu"); e("s+w+ye"); e("s+w+yo"); e("s+w+yU"); e("s+w+yaM"); e("s+w+yau"); e("s+w+yai"); e("s+w+yI"); e("s+w+y-i");
        e("h+nya"); e("h+ny", "h+nya"); e("h+nyi"); e("h+nyu"); e("h+nye"); e("h+nyo"); e("h+nyU"); e("h+nyaM"); e("h+nyau"); e("h+nyai"); e("h+nyI"); e("h+ny-i");
        e("h+Na"); e("h+N", "h+Na"); e("h+Ni"); e("h+Nu"); e("h+Ne"); e("h+No"); e("h+NU"); e("h+NaM"); e("h+Nau"); e("h+Nai"); e("h+NI"); e("h+N-i");
        e("h+ta"); e("h+t", "h+ta"); e("h+ti"); e("h+tu"); e("h+te"); e("h+to"); e("h+tU"); e("h+taM"); e("h+tau"); e("h+tai"); e("h+tI"); e("h+t-i");
        e("h+na"); e("h+n", "h+na"); e("h+ni"); e("h+nu"); e("h+ne"); e("h+no"); e("h+nU"); e("h+naM"); e("h+nau"); e("h+nai"); e("h+nI"); e("h+n-i");
        e("h+n+ya"); e("h+n+y", "h+n+ya"); e("h+n+yi"); e("h+n+yu"); e("h+n+ye"); e("h+n+yo"); e("h+n+yU"); e("h+n+yaM"); e("h+n+yau"); e("h+n+yai"); e("h+n+yI"); e("h+n+y-i");
        e("h+pa"); e("h+p", "h+pa"); e("h+pi"); e("h+pu"); e("h+pe"); e("h+po"); e("h+pU"); e("h+paM"); e("h+pau"); e("h+pai"); e("h+pI"); e("h+p-i");
        e("h+pha"); e("h+ph", "h+pha"); e("h+phi"); e("h+phu"); e("h+phe"); e("h+pho"); e("h+phU"); e("h+phaM"); e("h+phau"); e("h+phai"); e("h+phI"); e("h+ph-i");
        e("h+ma"); e("h+m", "h+ma"); e("h+mi"); e("h+mu"); e("h+me"); e("h+mo"); e("h+mU"); e("h+maM"); e("h+mau"); e("h+mai"); e("h+mI"); e("h+m-i");
        e("h+ya"); e("h+y", "h+ya"); e("h+yi"); e("h+yu"); e("h+ye"); e("h+yo"); e("h+yU"); e("h+yaM"); e("h+yau"); e("h+yai"); e("h+yI"); e("h+y-i");
        e("h+la"); e("h+l", "h+la"); e("h+li"); e("h+lu"); e("h+le"); e("h+lo"); e("h+lU"); e("h+laM"); e("h+lau"); e("h+lai"); e("h+lI"); e("h+l-i");
        e("h+sa"); e("h+s", "h+sa"); e("h+si"); e("h+su"); e("h+se"); e("h+so"); e("h+sU"); e("h+saM"); e("h+sau"); e("h+sai"); e("h+sI"); e("h+s-i");
        e("h+s+wa"); e("h+s+w", "h+s+wa"); e("h+s+wi"); e("h+s+wu"); e("h+s+we"); e("h+s+wo"); e("h+s+wU"); e("h+s+waM"); e("h+s+wau"); e("h+s+wai"); e("h+s+wI"); e("h+s+w-i");
        e("h+w+ya"); e("h+w+y", "h+w+ya"); e("h+w+yi"); e("h+w+yu"); e("h+w+ye"); e("h+w+yo"); e("h+w+yU"); e("h+w+yaM"); e("h+w+yau"); e("h+w+yai"); e("h+w+yI"); e("h+w+y-i");
        e("k+Sh+Na"); e("k+Sh+N", "k+Sh+Na"); e("k+Sh+Ni"); e("k+Sh+Nu"); e("k+Sh+Ne"); e("k+Sh+No"); e("k+Sh+NU"); e("k+Sh+NaM"); e("k+Sh+Nau"); e("k+Sh+Nai"); e("k+Sh+NI"); e("k+Sh+N-i");
        e("k+Sh+ma"); e("k+Sh+m", "k+Sh+ma"); e("k+Sh+mi"); e("k+Sh+mu"); e("k+Sh+me"); e("k+Sh+mo"); e("k+Sh+mU"); e("k+Sh+maM"); e("k+Sh+mau"); e("k+Sh+mai"); e("k+Sh+mI"); e("k+Sh+m-i");
        e("k+Sh+m+ya"); e("k+Sh+m+y", "k+Sh+m+ya"); e("k+Sh+m+yi"); e("k+Sh+m+yu"); e("k+Sh+m+ye"); e("k+Sh+m+yo"); e("k+Sh+m+yU"); e("k+Sh+m+yaM"); e("k+Sh+m+yau"); e("k+Sh+m+yai"); e("k+Sh+m+yI"); e("k+Sh+m+y-i");
        e("k+Sh+ya"); e("k+Sh+y", "k+Sh+ya"); e("k+Sh+yi"); e("k+Sh+yu"); e("k+Sh+ye"); e("k+Sh+yo"); e("k+Sh+yU"); e("k+Sh+yaM"); e("k+Sh+yau"); e("k+Sh+yai"); e("k+Sh+yI"); e("k+Sh+y-i");
        e("k+Sh+Ra"); e("k+Sh+R", "k+Sh+Ra"); e("k+Sh+Ri"); e("k+Sh+Ru"); e("k+Sh+Re"); e("k+Sh+Ro"); e("k+Sh+RU"); e("k+Sh+RaM"); e("k+Sh+Rau"); e("k+Sh+Rai"); e("k+Sh+RI"); e("k+Sh+R-i");
        e("k+Sh+la"); e("k+Sh+l", "k+Sh+la"); e("k+Sh+li"); e("k+Sh+lu"); e("k+Sh+le"); e("k+Sh+lo"); e("k+Sh+lU"); e("k+Sh+laM"); e("k+Sh+lau"); e("k+Sh+lai"); e("k+Sh+lI"); e("k+Sh+l-i");
        e("k+Sh+wa"); e("k+Sh+w", "k+Sh+wa"); e("k+Sh+wi"); e("k+Sh+wu"); e("k+Sh+we"); e("k+Sh+wo"); e("k+Sh+wU"); e("k+Sh+waM"); e("k+Sh+wau"); e("k+Sh+wai"); e("k+Sh+wI"); e("k+Sh+w-i");
/* FIXME: bug 838566:
        e("a+ya"); e("a+y", "a+ya"); e("a+yi"); e("a+yu"); e("a+ye"); e("a+yo"); e("a+yU"); e("a+yaM"); e("a+yau"); e("a+yai"); e("a+yI"); e("a+y-i");
        e("a+ra"); e("a+r", "a+ra"); e("a+ri"); e("a+ru"); e("a+re"); e("a+ro"); e("a+rU"); e("a+raM"); e("a+rau"); e("a+rai"); e("a+rI"); e("a+r-i");
        e("a+r+ya"); e("a+r+y", "a+r+ya"); e("a+r+yi"); e("a+r+yu"); e("a+r+ye"); e("a+r+yo"); e("a+r+yU"); e("a+r+yaM"); e("a+r+yau"); e("a+r+yai"); e("a+r+yI"); e("a+r+y-i");
*/
    }

    public void testNoKeysCrashUs() {
        enableEWTSKeyboard();
        /* why 130? because we want to try some extended ASCII
           characters to make sure that they don't crash us either */
        char max = (char)130;
        for (char ch = 0; ch < max; ch++) {
            noExceptions("" + ch);
            for (char ch2 = 0; ch2 < max; ch2++) {
                noExceptions("" + ch + ch2);
            }
        }
        // FIXME: test the sambhota and TCC keyboards
        enableSambhotaKeyboard();
        for (char ch = 0; ch < max; ch++) {
            noExceptions("" + ch);
            for (char ch2 = 0; ch2 < max; ch2++) {
                noExceptions("" + ch + ch2);
            }
        }
    }

    public void testDisambiguation() {
        enableEWTSKeyboard();
        ensureKeysGiveCorrectWylie("gya");
        ensureKeysGiveCorrectWylie("g.ya");
        ensureKeysGiveCorrectWylie("bya");
        ensureKeysGiveCorrectWylie("b.ya", "baya");
        ensureKeysGiveCorrectWylie("mya");
        ensureKeysGiveCorrectWylie("m.ya", "maya");
        ensureKeysGiveCorrectWylie("'ya", "'aya");
        ensureKeysGiveCorrectWylie("'.ya", "'aya");
        ensureKeysGiveCorrectWylie("dya",
                                   "daya");
        ensureKeysGiveCorrectWylie("d.ya",
                                   "daya");
        ensureKeysGiveCorrectWylie("grwa");
        ensureKeysGiveCorrectWylie("g.rwa",
                                   "garwa");
        ensureKeysGiveCorrectWylie("gra");
        ensureKeysGiveCorrectWylie("dra");
        ensureKeysGiveCorrectWylie("drwa");
        ensureKeysGiveCorrectWylie("d.rwa",
                                   "darwa");
        ensureKeysGiveCorrectWylie("g.r", "gar");
        ensureKeysGiveCorrectWylie("d.r", "dar");
        ensureKeysGiveCorrectWylie("'.r", "'ar");
        ensureKeysGiveCorrectWylie("b.r", "bar");
        ensureKeysGiveCorrectWylie("m.r", "mar");
    }

    /** Tests performing a few keystrokes in the Extended Wylie
     *  keyboard, turning those into our internal representation (IR),
     *  and then converting the result to Extended Wylie. */
    public void testWylieToIRToWylie() {
        enableEWTSKeyboard();
        // FIXME: test achen when it's not alone -- once Jskad's
        // keyboard supports that!  Right now, you have to type "d
        // a<LEFTARROW><BACKSPACE><RIGHTARROW> " to get EWTS {d.a }.
        // Test a.u, r.u.r, ra.u, g.-i, etc.

        ensureKeysGiveCorrectWylie("gl-i");
        ensureKeysGiveCorrectWylie("gr-i");
        ensureKeysGiveCorrectWylie("ts.ha",
                                   "tsaha");
        ensureKeysGiveCorrectWylie("tsha");

        ensureKeysGiveCorrectWylie("tsa");
        ensureKeysGiveCorrectWylie("t.sa",
                                   "tas");

        ensureKeysGiveCorrectWylie("d.za", "daza");
        ensureKeysGiveCorrectWylie("dza");

        ensureKeysGiveCorrectWylie("s.ha",
                                   "saha");
        ensureKeysGiveCorrectWylie("sha");

        ensureKeysGiveCorrectWylie("kue ");
        ensureKeysGiveCorrectWylie("012345678901234 ");
        if (false) {
            // DLC FIXME: we can no longer express the super- and
            // subscribed numerals.  Not with \\u, \\nu, or regular
            // Wylie.
            ensureKeysGiveCorrectWylie("<8<7<0 ");
            ensureKeysGiveCorrectWylie("ka<7 ",
                                       "ka<7. ");
            ensureKeysGiveCorrectWylie("ka <7 ");
            ensureKeysGiveCorrectWylie("ka>7 ",
                                       "ka>7. ");
            ensureKeysGiveCorrectWylie("ka >7 ");
        }
// DLC FIXME : M^ doesn't work.  nga, na do, k,kh do, why not M, M^?
        ensureKeysGiveCorrectWylie("kuau ");
        ensureKeysGiveCorrectWylie("ku-i ");
        ensureKeysGiveCorrectWylie("kuai ");
        ensureKeysGiveCorrectWylie("cuig ");
        ensureKeysGiveCorrectWylie("kcuig ",
                                   "kacuiga ");
        ensureKeysGiveCorrectWylie("gcuig ");
        ensureKeysGiveCorrectWylie("gcuigs'e'i'i'o'am'ang'e'o'u'am'am ");
        ensureKeysGiveCorrectWylie("nga ");
        ensureKeysGiveCorrectWylie("nga /");

        ensureKeysGiveCorrectWylie("nag");

        ensureKeysGiveCorrectWylie("bkra shis bde legs/");
        ensureKeysGiveCorrectWylie("bakra shisa bade legs/",
                                   "bkra shis bde legs/");

        ensureKeysGiveCorrectWylie("sgom pa'am ");

        ensureKeysGiveCorrectWylie("sgom pe'am ");

        ensureKeysGiveCorrectWylie("le'u'i'o");

        ensureKeysGiveCorrectWylie("la'u'i'o");

        ensureKeysGiveCorrectWylie("la'u'i'o/la'am/pa'ang/pa'am'ang/pe'ang");

        ensureKeysGiveCorrectWylie("bskyar.d'am'ang");

        ensureKeysGiveCorrectWylie("lar.d");
        ensureKeysGiveCorrectWylie("lard",
                                   "larda");

        ensureKeysGiveCorrectWylie("lal.d");
        ensureKeysGiveCorrectWylie("lald",
                                   "lalda");

        ensureKeysGiveCorrectWylie("las.d");
        ensureKeysGiveCorrectWylie("lasd",
                                   "lasda");

        ensureKeysGiveCorrectWylie("b.lar.d", "balarada");
        ensureKeysGiveCorrectWylie("blar.d");
        ensureKeysGiveCorrectWylie("blarad",
                                   "blar.d");
        ensureKeysGiveCorrectWylie("b.lard",
                                   "balarda");

        ensureKeysGiveCorrectWylie("b.lal.d", "balalada");
        ensureKeysGiveCorrectWylie("blald",
                                   "blalda");
        ensureKeysGiveCorrectWylie("b.lald",
                                   "balalda");

        ensureKeysGiveCorrectWylie("b.las.d", "balasada");
        ensureKeysGiveCorrectWylie("blasd",
                                   "blasda");
        ensureKeysGiveCorrectWylie("b.lasd",
                                   "balasda");

        ensureKeysGiveCorrectWylie("b.luHna", "baluHna");

        ensureKeysGiveCorrectWylie("b.lA-iMg", "balA-iMga");

        ensureKeysGiveCorrectWylie("blA-iMg");

        ensureKeysGiveCorrectWylie("b.lag", "balaga");
        ensureKeysGiveCorrectWylie("blga", "balga");

        ensureKeysGiveCorrectWylie("b.las",
                                   "bals");
        ensureKeysGiveCorrectWylie("bl.s",
                                   "blas");
        ensureKeysGiveCorrectWylie("bls",
                                   "bals");

        ensureKeysGiveCorrectWylie("b.rag", "baraga");
        ensureKeysGiveCorrectWylie("brg",
                                   "brga");

        ensureKeysGiveCorrectWylie("b.rtan", "brtan");
        ensureKeysGiveCorrectWylie("brtan");

        ensureKeysGiveCorrectWylie("bars");
        ensureKeysGiveCorrectWylie("b.rs",
                                   "bars");
        ensureKeysGiveCorrectWylie("brs",
                                   "bars");
        ensureKeysGiveCorrectWylie("br.s",
                                   "bras");
        ensureKeysGiveCorrectWylie("bras");

        ensureKeysGiveCorrectWylie("d.wa",
                                   "dawa");
        ensureKeysGiveCorrectWylie("dawa",
                                   "dawa");
        ensureKeysGiveCorrectWylie("dwa");

        ensureKeysGiveCorrectWylie("g.wa",
                                   "gawa");
        ensureKeysGiveCorrectWylie("gawa",
                                   "gawa");
        ensureKeysGiveCorrectWylie("gwa");

        ensureKeysGiveCorrectWylie("'.wa",
                                   "'awa");
        ensureKeysGiveCorrectWylie("'awa",
                                   "'awa");
        ensureKeysGiveCorrectWylie("'wa",
                                   "'awa");

        ensureKeysGiveCorrectWylie("gyg",
                                   "g.yag");
        ensureKeysGiveCorrectWylie("gyug",
                                   "gyug");
        ensureKeysGiveCorrectWylie("gayug",
                                   "g.yug");
        ensureKeysGiveCorrectWylie("g.yag");
        ensureKeysGiveCorrectWylie("gyag");
        ensureKeysGiveCorrectWylie("gy.g",
                                   "gyag");

        ensureKeysGiveCorrectWylie("gys",
                                   "g.yas");
        ensureKeysGiveCorrectWylie("g.yas");
        ensureKeysGiveCorrectWylie("gyas");
        ensureKeysGiveCorrectWylie("gy.s",
                                   "gyas");

        // FIXME: shouldn't this give the four-glyph combo m-a-a-s?
        ensureKeysGiveCorrectWylie("ma.a.asa",
                                   "mas");

        ensureKeysGiveCorrectWylie("'ka",
                                   "'aka");

        ensureKeysGiveCorrectWylie("'gas");

        /* Paul Hackett's e-mail on Feb 27 2005 leads to these test
           cases: */
        {
            // second letter:
            ensureKeysGiveCorrectWylie("gnas");
            ensureKeysGiveCorrectWylie("dgas");
            ensureKeysGiveCorrectWylie("dmas");
            ensureKeysGiveCorrectWylie("'gas");
            ensureKeysGiveCorrectWylie("'bas");

            // TODO(dchandler): Paul Hackett's list says mngas is
            // correct, not mangs.
            ensureKeysGiveCorrectWylie("mngas", "mangs");

            // first letter:
            ensureKeysGiveCorrectWylie("'angs");
            // TODO(dchandler): Paul Hackett's list says dangs is
            // correct, not dngas.
            ensureKeysGiveCorrectWylie("dangs", "dngas");
        }

        /* Chris Fynn's e-mail on Feb 21 2005 leads to these test
           cases: */
        {
            ensureKeysGiveCorrectWylie("dgas");
            ensureKeysGiveCorrectWylie("'gas");
            ensureKeysGiveCorrectWylie("dngas");
            ensureKeysGiveCorrectWylie("gnad");
            ensureKeysGiveCorrectWylie("mnad");
            ensureKeysGiveCorrectWylie("bags");
            ensureKeysGiveCorrectWylie("dbas");
            ensureKeysGiveCorrectWylie("'bas");
            ensureKeysGiveCorrectWylie("mags");
            ensureKeysGiveCorrectWylie("mangs");
            ensureKeysGiveCorrectWylie("dmas");
        }

        ensureKeysGiveCorrectWylie("gangs");

        ensureKeysGiveCorrectWylie("gnags");

        ensureKeysGiveCorrectWylie("'angs");

        ensureKeysGiveCorrectWylie("'ag");

        ensureKeysGiveCorrectWylie("'byung");

        ensureKeysGiveCorrectWylie("'byungs");

        ensureKeysGiveCorrectWylie("b.lags", "balagasa");
        ensureKeysGiveCorrectWylie("blags");

        // DLC FIXME: add b-r-g-s, b-l-g-s, etc.


        ensureKeysGiveCorrectWylie("mngas",
                                   "mangs");
        ensureKeysGiveCorrectWylie("mangs");

        ensureKeysGiveCorrectWylie("mn.gs",
                                   "mnags");
        ensureKeysGiveCorrectWylie("mnags");

        ensureKeysGiveCorrectWylie("lmn.g",
                                   "lamanaga");

        ensureKeysGiveCorrectWylie("l.m.ng",
                                   "lamanga");

        ensureKeysGiveCorrectWylie("b.m.ng",
                                   "bamanga");
        ensureKeysGiveCorrectWylie("bmang",
                                   "bamanga");

        ensureKeysGiveCorrectWylie("gdams");
        ensureKeysGiveCorrectWylie("g.d.m.s.",
                                   "gdams");

        ensureKeysGiveCorrectWylie("'gams");
        ensureKeysGiveCorrectWylie("'.g.m.s",
                                   "'gams");

        ensureKeysGiveCorrectWylie("n+ya");

        {
            // These are correctly handled in terms of
            // makeIllegalTibetanGoEndToEnd:
            ensureKeysGiveCorrectWylie("skalazasa");
            ensureKeysGiveCorrectWylie("jskad",
                                       "jaskada");
            ensureKeysGiveCorrectWylie("jeskad",
                                       "jeskada");
            ensureKeysGiveCorrectWylie("jeskd",
                                       "jesakada");
            ensureKeysGiveCorrectWylie("jesakada",
                                       "jesakada");
        }

        {
            // DLC FIXME: ai gives a.ai, a.i is required to get ai.

            // DLC FIXME: haaa doesn't get you h.a., neither does
            // ha.a; achen is tough to get.
        }

        ensureKeysGiveCorrectWylie("heM hiM h-iM heM haiM hoM hauM hUM ");
        ensureKeysGiveCorrectWylie("hi.M ho.M he.M hu.M",
                                   "hiM hoM heM huM");

        ensureKeysGiveCorrectWylie("brgwU-imd");

        ensureKeysGiveCorrectWylie("pad+me");
        ensureKeysGiveCorrectWylie("pad+men+b+h+yuM");

        ensureKeysGiveCorrectWylie("bskyUMbs");
        ensureKeysGiveCorrectWylie("bskyUMbsHgro ");

        ensureKeysGiveCorrectWylie("gyurd", "gyurda");
        ensureKeysGiveCorrectWylie("gyur.d");

        ensureKeysGiveCorrectWylie("t+r+na");
        ensureKeysGiveCorrectWylie("t+r+ne");

        ensureKeysGiveCorrectWylie("favakakhagangacachajanyatathadanapaphabamatsatshadzawazhaza'ayaralashasahaTaThaDaNaSha");
        ensureKeysGiveCorrectWylie("fevekekhegengecechejenyetethedenepephebemetsetshedzewezheze'eyerelesheseheTeTheDeNeShe");
        ensureKeysGiveCorrectWylie("fuvukukhugungucuchujunyututhudunupuphubumutsutshudzuwuzhuzu'uyurulushusuhuTuThuDuNuShu");
        ensureKeysGiveCorrectWylie("fovokokhogongocochojonyotothodonopophobomotsotshodzowozhozo'oyoroloshosohoToThoDoNoSho");
        ensureKeysGiveCorrectWylie("faivaikaikhaigaingaicaichaijainyaitaithaidainaipaiphaibaimaitsaitshaidzaiwaizhaizai'aiyairailaishaisaihaiTaiThaiDaiNaiShai");
        ensureKeysGiveCorrectWylie("fauvaukaukhaugaungaucauchaujaunyautauthaudaunaupauphaubaumautsautshaudzauwauzhauzau'auyauraulaushausauhauTauThauDauNauShau");
        ensureKeysGiveCorrectWylie("fivikikhigingicichijinyitithidinipiphibimitsitshidziwizhizi'iyirilishisihiTiThiDiNiShi");

        ensureKeysGiveCorrectWylie("don't touch my coffee/that makes me very angry/supersize my drink",
                                   "dona'ata tocha mya cofafe/thata makesa me veraya angaraya/superasize mya drinaka");

        {
            // r-w and r+w are tricky cases in tibwn.ini -- the only ones
            // like this according to bug #800166.
            e("rwa");
            e("R+Wa");
            // e("r+wa"); should be the same as e("rwa") but is not, FIXME
        }
    }

    /** Tests performing a few keystrokes in the ACIP keyboard,
     *  turning those into our internal representation (IR), and then
     *  converting the result to Extended Wylie.  These test cases are
     *  as solid as can be; they just test that we can handle all the
     *  consonants and basic vowels. NOTE WELL: YOU WOULD NOT USE
     *  Jskad's EXISTING KEYBOARD MECHANISM TO IMPLEMENT
     *  ACIP->TIBETAN.  SEE THE BUG LIST AS SF.NET. */
    public void testACIPAlphabetToIRToWylie() {
        enableACIPKeyboard();

        ensureKeysGiveCorrectWylie("KA", "ka");
        ensureKeysGiveCorrectWylie("KHA", "kha");
        ensureKeysGiveCorrectWylie("GA", "ga");
        ensureKeysGiveCorrectWylie("NGA", "nga");
        ensureKeysGiveCorrectWylie("CA", "ca");
        ensureKeysGiveCorrectWylie("CHA", "cha");
        ensureKeysGiveCorrectWylie("JA", "ja");
        ensureKeysGiveCorrectWylie("NYA", "nya");
        ensureKeysGiveCorrectWylie("TA", "ta");
        ensureKeysGiveCorrectWylie("THA", "tha");
        ensureKeysGiveCorrectWylie("DA", "da");
        ensureKeysGiveCorrectWylie("NA", "na");
        ensureKeysGiveCorrectWylie("PA", "pa");
        ensureKeysGiveCorrectWylie("PHA", "pha");
        ensureKeysGiveCorrectWylie("BA", "ba");
        ensureKeysGiveCorrectWylie("MA", "ma");
        ensureKeysGiveCorrectWylie("TZA", "tsa");
        ensureKeysGiveCorrectWylie("TSA", "tsha");
        ensureKeysGiveCorrectWylie("DZA", "dza");
        ensureKeysGiveCorrectWylie("WA", "wa");
        ensureKeysGiveCorrectWylie("ZHA", "zha");
        ensureKeysGiveCorrectWylie("ZA", "za");
        ensureKeysGiveCorrectWylie("'A", "'a");
        ensureKeysGiveCorrectWylie("YA", "ya");
        ensureKeysGiveCorrectWylie("RA", "ra");
        ensureKeysGiveCorrectWylie("LA", "la");
        ensureKeysGiveCorrectWylie("SHA", "sha");
        ensureKeysGiveCorrectWylie("SA", "sa");
        ensureKeysGiveCorrectWylie("HA", "ha");

        ensureKeysGiveCorrectWylie("KI", "ki");
        ensureKeysGiveCorrectWylie("KHI", "khi");
        ensureKeysGiveCorrectWylie("GI", "gi");
        ensureKeysGiveCorrectWylie("NGI", "ngi");
        ensureKeysGiveCorrectWylie("CI", "ci");
        ensureKeysGiveCorrectWylie("CHI", "chi");
        ensureKeysGiveCorrectWylie("JI", "ji");
        ensureKeysGiveCorrectWylie("NYI", "nyi");
        ensureKeysGiveCorrectWylie("TI", "ti");
        ensureKeysGiveCorrectWylie("THI", "thi");
        ensureKeysGiveCorrectWylie("DI", "di");
        ensureKeysGiveCorrectWylie("NI", "ni");
        ensureKeysGiveCorrectWylie("PI", "pi");
        ensureKeysGiveCorrectWylie("PHI", "phi");
        ensureKeysGiveCorrectWylie("BI", "bi");
        ensureKeysGiveCorrectWylie("MI", "mi");
        ensureKeysGiveCorrectWylie("TZI", "tsi");
        ensureKeysGiveCorrectWylie("TSI", "tshi");
        ensureKeysGiveCorrectWylie("DZI", "dzi");
        ensureKeysGiveCorrectWylie("WI", "wi");
        ensureKeysGiveCorrectWylie("ZHI", "zhi");
        ensureKeysGiveCorrectWylie("ZI", "zi");
        ensureKeysGiveCorrectWylie("'I",
                                   // FIXME: should be 'i
                                   "'I");
        ensureKeysGiveCorrectWylie("YI", "yi");
        ensureKeysGiveCorrectWylie("RI", "ri");
        ensureKeysGiveCorrectWylie("LI", "li");
        ensureKeysGiveCorrectWylie("SHI", "shi");
        ensureKeysGiveCorrectWylie("SI", "si");
        ensureKeysGiveCorrectWylie("HI", "hi");
    }

    /** Tests that we can handle the ACIP Tibetanized Sanskrit
     *  stacks. NOTE WELL: YOU WOULD NOT USE Jskad's EXISTING KEYBOARD
     *  MECHANISM TO IMPLEMENT ACIP->TIBETAN.  SEE THE BUG LIST AS
     *  SF.NET. */
    public void testACIPSanskritLettersToIRToWylie() {
        enableACIPKeyboard();

        ensureKeysGiveCorrectWylie("GHA", "g+ha");
        ensureKeysGiveCorrectWylie("tA", "Ta");
        ensureKeysGiveCorrectWylie("thA", "Tha");
        ensureKeysGiveCorrectWylie("dA", "Da");
        ensureKeysGiveCorrectWylie("dHA", "D+ha");
        ensureKeysGiveCorrectWylie("nA", "Na");
        ensureKeysGiveCorrectWylie("DHA", "d+ha");
        ensureKeysGiveCorrectWylie("BHA", "b+ha");
        ensureKeysGiveCorrectWylie("DZHA", "dz+ha");
        ensureKeysGiveCorrectWylie("shA", "Sha");
        ensureKeysGiveCorrectWylie("KshA", "k+Sha");

        ensureKeysGiveCorrectWylie("GHU", "g+hu");
        ensureKeysGiveCorrectWylie("tU", "Tu");
        ensureKeysGiveCorrectWylie("thU", "Thu");
        ensureKeysGiveCorrectWylie("dU", "Du");
        ensureKeysGiveCorrectWylie("dHU", "D+hu");
        ensureKeysGiveCorrectWylie("nU", "Nu");
        ensureKeysGiveCorrectWylie("DHU", "d+hu");
        ensureKeysGiveCorrectWylie("BHU", "b+hu");
        ensureKeysGiveCorrectWylie("DZHU", "dz+hu");
        ensureKeysGiveCorrectWylie("shU", "Shu");
        ensureKeysGiveCorrectWylie("KshU", "k+Shu");
    }

    /** Tests performing a few keystrokes in the ACIP keyboard,
     *  turning those into our internal representation (IR), and then
     *  converting the result to Extended Wylie.  These test cases are
     *  solid; we have some certainty that our test cases are
     *  correct. NOTE WELL: YOU WOULD NOT USE Jskad's EXISTING
     *  KEYBOARD MECHANISM TO IMPLEMENT ACIP->TIBETAN.  SEE THE BUG
     *  LIST AS SF.NET. */
    public void testACIPToIRToWylie() {
        enableACIPKeyboard();

        ensureKeysGiveCorrectWylie("NGA",
                                   "nga");

        ensureKeysGiveCorrectWylie("NYA",
                                   "nya");

        ensureKeysGiveCorrectWylie("TSA",
                                   "tsha");

        ensureKeysGiveCorrectWylie("TZA",
                                   "tsa");

        ensureKeysGiveCorrectWylie("DZA",
                                   "dza");

        ensureKeysGiveCorrectWylie("SHA",
                                   "sha");

        ensureKeysGiveCorrectWylie("KYA",
                                   "kya");

        ensureKeysGiveCorrectWylie("RMA",
                                   "rma");

        ensureKeysGiveCorrectWylie("G-YAS",
                                   "g.yas");
        ensureKeysGiveCorrectWylie("GA-YAS",
                                   "g.yas");
        ensureKeysGiveCorrectWylie("GAYASA",
                                   "g.yas");

        ensureKeysGiveCorrectWylie("GYAS",
                                   "gyas");

        ensureKeysGiveCorrectWylie("nga", // g has no effect; a has no effect.
                                   "Na");

        ensureKeysGiveCorrectWylie("N+YA",
                                   "n+ya");

        ensureKeysGiveCorrectWylie("GHA",
                                   "g+ha");

        ensureKeysGiveCorrectWylie("D+MAR ",
                                   "d+mara ");

        ensureKeysGiveCorrectWylie("KHKHA ",
                                   "kh+kha ");

        ensureKeysGiveCorrectWylie("T+SA",
                                   "t+sa");

        ensureKeysGiveCorrectWylie("PADMA",
                                   "pad+ma");
        ensureKeysGiveCorrectWylie("PA-DMA",
                                   "pad+ma");
        ensureKeysGiveCorrectWylie("P-DMA",
                                   "pad+ma");
        ensureKeysGiveCorrectWylie("P-D+MA",
                                   "pad+ma");
        ensureKeysGiveCorrectWylie("P-D+M ",
                                   "pad+ma ");
    }

    /** Tests performing a few keystrokes in the ACIP keyboard,
     *  turning those into our internal representation (IR), and then
     *  converting the result to Extended Wylie.  These test cases are
     *  iffy, we're either certain that Jskad is doing the wrong
     *  thing, or we don't know what the right thing is.  NOTE WELL:
     *  YOU WOULD NOT USE Jskad's EXISTING KEYBOARD MECHANISM TO
     *  IMPLEMENT ACIP->TIBETAN.  SEE THE BUG LIST AS SF.NET. */
    public void testIffyACIPToIRToWylie() {
        enableACIPKeyboard();

        // NOTE WELL: it's a prefix, it matters -- DMAR is {D}{M}{R}
        // that is, not {D+M}{R} -- so use ACIP->Tibetan technology
        // for a tsheg-bar-by-tsheg-bar ACIP keyboard.

        ensureKeysGiveCorrectWylie("DMAR ",
                                   // FIXME: should be "dmar "
                                   "d+mara ");

        ensureKeysGiveCorrectWylie("GNAS ",
                                   // FIXME: should be "gnas "
                                   "g+nasa ");

        ensureKeysGiveCorrectWylie("PA DMA ",
                                   "pa d+ma "); // FIXME: should be "pa dma"

        ensureKeysGiveCorrectWylie("DMIGS ",
                                   // FIXME: S0526m proves this should be "dmigs "
                                   "d+migasa ");

        ensureKeysGiveCorrectWylie("ZHVA",
                                   "zha"
                                   // FIXME: should be "zhwa", see
                                   // bug list at sf.net
                                   );

        ensureKeysGiveCorrectWylie("GRVA",
                                   "gra"
                                   // FIXME: should be "grwa", see
                                   // bug list at sf.net
                                   );

        /* From
           http://www.asianclassics.org/download/tibetancode/conventions.html:

        The hyphen is used to indicate that two letters appear in
        horizontally adjacent positions with no intervening "tsek" (as
        seen in G-Y, above). Although the hyphen is usually not needed
        for resolving ambiguity (PADMA is just as definite as PA-DMA
        or PA-D+MA), it is sometimes typed in cases other than
        G-Y. [Note that the DMA sequence in PADMA does not strictly
        require a plus-sign since the D can only be interpreted as a
        suffix to PA and not as a prefix to MA; however, if written
        (incorrectly) as a PA DMA, the D could be interpreted as a
        prefix to MA.]

           Thus, PADMA is supposed to make PA-D+MA appear.  That's
           ODD, isn't it?  Because that requires knowledge that PA
           cannot be a prefix.  But that's how it's done; see
           Td4283m.act/Td4283m.rtf if you don't believe.
           ACIP->Tibetan handles all these oddities, and the ACIP
           keyboard really must use its technology.
        */
    }

    /** Tests this part of bug 998476:

<pre>
TMW -> EWTS conversion errors
The following incorrect conversion are happening:

dga'o -> dag'o
'da'i -> 'ad'i

'da'i should produce 'da'i since the genitive particle 'i can
only be appended to syllables that end with vowel or
with a chung ('). For instance sgra + 'i = sgra'i, nam
mkha' + 'i = nam mkha'i. The case here is 'da' + 'i = 'da'i.
syllable 'ad can't take the genitive 'i, so 'ad'i is invalid.

Of course this is a hypothetical syllable with no meaning,
but following the rules 'ad would be correct. 'da would be
mistaken. "In two-lettered words, the first is always the
root letter." (losang thonden's modern tibetan language,
pag 41).
</pre>

     */
    public void testBug998476() {
        enableEWTSKeyboard();
        e("dga'o");
        e("'da'i");
        e("bsad");
    }

}
// FIXME: EWTS needs a list of "native" stacks in it.

