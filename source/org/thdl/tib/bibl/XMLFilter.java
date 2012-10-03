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
Library (THDL). Portions created by the THDL are Copyright 2001-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.bibl;

import java.io.File;

import javax.swing.filechooser.FileFilter;
//import org.thdl.tib.bibl.shared.*;

/**
* XMLFilter extends java.io.FileFilter to display only XML files in the open filechooser window.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class XMLFilter extends FileFilter {

	public final static String xml = "xml";

    // Accept all directories and all xml files.

    public boolean accept(File f)
    {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
		if (extension != null && extension.equals(xml))
		{
            return true;
        }
        else
        {
            return false;
    	}
    }

	public String getDescription()
	{
		return "XML files only";
	}

    /*
     * Get the extension of a file.
     */

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
