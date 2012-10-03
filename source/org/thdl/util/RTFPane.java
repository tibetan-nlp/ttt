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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

/** An RTFPane is a JScrollPane displaying the contents of a rich text
    file (an RTF file). */
public class RTFPane extends JScrollPane {
    /** the default RTF editor kit that each RTFPane instance uses */
    final private static RTFEditorKit rtfKit = new RTFEditorKit();

    /** Creates a JScrollPane displaying the contents of the rich text
     *  file named rtfFileName (which should be a resource packaged in
     *  this application's JAR file).
     *
     *  @throws IOException if the resource exists but cannot be read
     *  @throws BadLocationException if the RTF file is busted
     *  @throws FileNotFoundException if the resource could not be found.
     *  @param resourceHolder the class associated with the named resource
     *  @param rtfFileName the name of the resource that is the rich text file
     */
    public RTFPane(Class resourceHolder, String rtfFileName)
        throws IOException, BadLocationException, FileNotFoundException
    {
        super();
        InputStream in = resourceHolder.getResourceAsStream(rtfFileName);
        if (in == null) {
            throw new FileNotFoundException(rtfFileName);
        }
        DefaultStyledDocument doc = new DefaultStyledDocument();
        try {
            rtfKit.read(in, doc, 0);
        } catch (BadLocationException ioe) {
            throw ioe;
        } catch (IOException ioe) {
            throw ioe;
        }

        JTextPane pane = new JTextPane(doc);
        pane.setEditable(false);

        setViewportView(pane);
    }
}
