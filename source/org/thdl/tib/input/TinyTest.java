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
 @author David Chandler

 Tests TMW->EWTS (and indirectly, the EWTS->TMW keyboard).  This test
 is separate from DuffPaneTest for the following reasons:

 <p>This JUnit test is meant to do very little.  That way you can
 change it to do something interesting and turn the logging way up to
 get insight.  Why JUnit?  Because our Ant build system makes this
 very easy to run via 'ant check-one
 -Dsoletest=org.thdl.tib.input.TinyTest.  */
public class TinyTest extends DuffPaneTestBase {
    public TinyTest(String a0) {
        super(a0);
    }

    /** Tests part of bug 998476.
     */
    public void testBug998476() {
        enableEWTSKeyboard();
        e("M");
        e("laM");
        e("lM", "laM");
        
        e("kaH");
        e("gam");
        e("gam?");

        e("?");
        e("la?");

        e("&");
        e("la&");

        e("H");
        e("laH");

        e("HM");
        e("laHM");
    }
}


