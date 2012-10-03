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
 * Tests {@link org.thdl.tib.text.tshegbar.UnicodeGraphemeCluster} at the unit level.
 */
public class UnicodeGraphemeClusterTest extends TestCase implements UnicodeConstants {
	/**
	 * Plain vanilla constructor for UnicodeGraphemeClusterTest.
	 * @param arg0
	 */
	public UnicodeGraphemeClusterTest(String arg0) {
		super(arg0);
	}
    /** Invokes a text UI and runs all this class's tests. */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(UnicodeGraphemeClusterTest.class);
	}

    /** Tests the getTopToBottomCodePoints() method and the
     * UnicodeGraphemeCluster(String) constructor. */
    public void testGetTopToBottomCodepointsAndMore() {
        assertTrue(new UnicodeGraphemeCluster("\u0F00").getTopToBottomCodepoints().equals("\u0F7E\u0F7C\u0F68"));
        assertTrue(new UnicodeGraphemeCluster("\u0F66\u0F93\u0F91\u0FA7\u0F92\u0FAD\u0F77\u0F83\u0F86\u0F84").getTopToBottomCodepoints().equals("\u0F86\u0F83\u0F80\u0F66\u0F92\u0FB7\u0F91\u0FA6\u0FB7\u0F92\u0FB2\u0FAD\u0F71\u0F84"));
    }
}
