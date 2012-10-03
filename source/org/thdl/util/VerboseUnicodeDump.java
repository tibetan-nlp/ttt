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

package org.thdl.util;

/** <p>VerboseUnicodeDump is a utility for reading in a Unicode text
    file and outputting human-readable stuff. This stuff is like the
    following:</p>

<pre>
0f40
0f0d
0020
</pre>

    <p>One might use this to debug ACIP-&gt;Unicode conversions, for
    example.</p>

    @author David Chandler */
public class VerboseUnicodeDump {
    public static void main(String args[]) throws Exception {
        if (args.length != 2) {
            System.err.println("bad args, need filename UTF-8|UTF-16LE|UTF-16|UTF-16BE|US-ASCII|...");
            System.exit(1);
        }
        java.io.Reader fr
	    = new java.io.InputStreamReader(new java.io.FileInputStream(args[0]),
					    java.nio.charset.Charset.forName(args[1]));
        int x;
        while (-1 != (x = fr.read())) {
            System.out.println(org.thdl.tib.text.tshegbar.UnicodeUtils.unicodeCodepointToString((char)x, false, "", false));
        }
        System.exit(0);
    }
}
