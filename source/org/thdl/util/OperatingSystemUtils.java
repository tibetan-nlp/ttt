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

package org.thdl.util;

/** This class contains our operating-system-specific code.  This
 *  class is not instantiable.
 */
public class OperatingSystemUtils {

    /** This means that, because of security restrictions or the like,
     *  we cannot determine the OS. */
    public static final int UNKNOWN = 0;
    /** Not WIN32, not MAC -- maybe a *nix box. */
    public static final int OTHER = 1;
    /** Windows 9x, Me, 200*, or XP */
    public static final int WIN32 = 2;
    /** Mac (OS X or otherwise) */
    public static final int MAC = 3;

    /** Do not instantiate this class. */
    private OperatingSystemUtils() { }

    /** cached result of {@link #getOSName()} */
    private static String OSName = null;

    /** Returns the lowercase name of the operating system, or
     *  "unknown" if the operating system's identity cannot be
     *  determined. */
	public static String getOSName() {
        if (null == OSName) {
            try {
                OSName = System.getProperty("os.name").toLowerCase();
            } catch (SecurityException e) {
                OSName = null;
            }
            if (null == OSName) {
                OSName = "unknown";
            }
        }
        return OSName;
	}

    /** Returns either {@link #UNKNOWN}, {@link #WIN32}, {@link #MAC},
     *  or {@link #OTHER}. */
    public static int getOSType() {
        String os = getOSName();
        if (os.startsWith("mac")) {
            return MAC;
        } else if (os.startsWith("win")) {
            return WIN32;
        } else if (os.equals("unknown")) {
            return UNKNOWN;
        } else {
            return OTHER;
        }
    }
}
