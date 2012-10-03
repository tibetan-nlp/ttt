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
import java.util.Hashtable;

import javax.swing.filechooser.FileView;
//import org.thdl.tib.bibl.shared.*;

/**
* <p>
* TTFileView test to see if a file is an .xml file and if so, assumes it's a Tibbibl
* and searches for the first &lt;title&gt; element and sets the file's description to that.
* It then uses this description for the name of the text by overriding the {@link #getName} function.
* This is used in for the {@link javax.swing.JFileChooser} in <code>FileAction</code> so that
* when the open-file window appears it lists the text names instead of the file names.
* </p>
*
* @author Than Garson, Tibetan and Himalayan Digital Library
* @version 1.0
*/

public class TTFileView extends FileView implements TibConstants
{
    private Hashtable fileDescs = new Hashtable(5);
    private XMLReader docHandler = new XMLReader();


	/**
	* <p>
	* Overrides the ancestor's--{@link FileView}--getName() function
	* so that it returns the text title as the name, if it is an XML file. This has the
	* effect of presenting a list of text names in the open dialog rather than their less
	* comprehensible file names.
	* </p>
	*
	* @param fl <code>File</code> whose name is requested.
	* @return <code>String</code> the description of the file which has been set to
	*           the text title if it is an XML file.
	*
	*/
    public String getName(File fl) {
		if(getDescription(fl) == null && fl.getName().indexOf(".xml")>-1)
			putDescription(fl);
		return getDescription(fl);
    }

    /**
     * <p>
     * Adds a description to the file, if the file is an XML file and the <code>docHandler</code>,
     * that is an {@link XMLReader}, successfully processes the document and returns a {@link TibDoc}.
     * Furthermore, it must find a &lt;title&gt; element (the first in the document) with some text.
     * If all these conditions are met, the description of the file is set to the text of the title
     * element. Otherwise, the description is set to <code>null</code>.
     * </p>
     */
    public void putDescription(File fl) {
		String fileDescription;
		if(docHandler.setDoc(fl))
		{
		try {
			org.jdom.Element td = docHandler.getRoot();
			org.jdom.Element th = td.getChild(HEAD).getChild(TITLE);
			fileDescription = th.getText();
		} catch (NullPointerException npe) {
			fileDescription = null;}
		} else {
			fileDescription = null;
		}
		if(fileDescription != null)
 			fileDescs.put(fl,fileDescription.trim());
    }

    /**
     * Returns a human readable description of the file.
     *
     * @see FileView#getDescription
     */
    public String getDescription(File f) {
	return (String) fileDescs.get(f);
    };

}
