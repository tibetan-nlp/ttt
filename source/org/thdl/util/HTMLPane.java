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

package org.thdl.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

/** An HTMLPane is a JScrollPane displaying the contents of an HTML
 *  file.  DLC FIXME: at present, neither internal nor external
 *  hyperlinks work.
 *
 *  @author David Chandler
 */
public class HTMLPane extends JScrollPane {
    /** Creates a JScrollPane displaying the contents of the HTML file
     *  named htmlFileName (which should be a resource packaged in
     *  this application's JAR file).
     *
     *  @throws FileNotFoundException if the resource could not be found.
     *  @param resourceHolder the class associated with the named resource
     *  @param htmlFileName the name of the resource that is the HTML file */
    public HTMLPane(Class resourceHolder, String htmlFileName)
        throws FileNotFoundException
    {
        super();
        InputStream in = resourceHolder.getResourceAsStream(htmlFileName);
        if (in == null) {
            throw new FileNotFoundException(htmlFileName);
        }
        JEditorPane pane = new JEditorPane();
        pane.setContentType("text/html");
        try {
            pane.read(in, "our input stream");
        } catch (IOException e) {
            pane.setText("Error reading HTML file from JAR.\nPlease submit a bug report if this issue has not yet\nbeen resolved in the latest version.");
            ThdlDebug.noteIffyCode();
        }
        pane.setEditable(false);

        setViewportView(pane);
    }
}
