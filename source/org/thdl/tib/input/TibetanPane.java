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

package org.thdl.tib.input;

import java.awt.datatransfer.DataFlavor;

import javax.swing.JTextPane;
import javax.swing.text.rtf.RTFEditorKit;

import org.thdl.tib.text.TibetanDocument;
import org.thdl.tib.text.TibetanRTFEditorKit;

/**
* Enables display of Tibetan text using Tibetan Computer Company's
* free cross-platform TibetanMachineWeb fonts.  Breaks lines
* correctly.
*
* <p> Conceptually, we'd like to override getDocument(),
* getStyledDocument(), setStyledDocument(..), and setDocument(..).
* However, JTextPane is not written flexibly enough to allow that.
* You'll get exceptions during construction of a DuffPane if you try.
* </p>
*
* @author Edward Garrett, Tibetan and Himalayan Digital Library */
public class TibetanPane extends JTextPane {
/**
* The document displayed by this object.
*/
	protected TibetanDocument doc;

    protected DataFlavor rtfFlavor;
    protected RTFEditorKit rtfEd = null;

    /** Creates a new TibetanPane with a null document model. */
    TibetanPane() {
        super();
        setupJustTheEditor();
        doc = null;
    }

    /** Creates a new TibetanPane with a document model d. */
    public TibetanPane(TibetanDocument d) {
        super();
        setupJustTheEditor();
        doc = d;
        setDocument(d);
    }

    /** Returns the model of a Tibetan document associated with this
     *  JTextPane. */
    public TibetanDocument getTibDoc() {
        return (TibetanDocument)doc;
    }

/**
* This method sets up the editor.
*/
    private void setupJustTheEditor() {
        rtfFlavor = new DataFlavor("text/rtf", "Rich Text Format");
        rtfEd = new TibetanRTFEditorKit();
        setEditorKit(rtfEd);
    }

} // class TibetanPane
