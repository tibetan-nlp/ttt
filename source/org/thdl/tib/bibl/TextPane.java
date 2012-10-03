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

import java.awt.Color;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

//import org.thdl.tib.bibl.shared.*;


/**
* An extension of Swing's JPanel that is used to create the display for a
* text's bibliographic information. The panel is created using the text's information
* contained in a {@link TibDoc} and then is added or set to the content pane
* of the main frame which is an {@link TibFrame}.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class TextPane extends JTextPane implements TibConstants
{
	TiblEdit controller;

	/**
	* <p>
	* This is the <code>TibDoc</code> that is the XML document from which the information for
	* the <code>JTextPane</code>'s document comes. It needs to be here so we can use it functionality
	* to navigate the XML hierarchy and extract certain information.
	* </p>
	*/
	TibDoc tibDoc;

	/**
	* <p>
	* This is a two-dimensional array of {@link org.jdom.Element}s that holds the titles displayed in
	* this text pane, with the strings starting position, and length. It is used to reference the
	* element for editing when the cursor is positioned on a specific title.
	* </p>
	*/
	Object[][] titleRefs;

	int caretPos;

	/**
	* <p>
	* This and the following are static, style constants.  I.e., they're names of the styles.
	*</p>
	*/
	public static String REG = "regular";
	public static String HEAD = "header";
	public static String HEAD_ITALIC = "headitalic";
	public static String TEXT_HEAD = "textheader";
	public static String TEXT_HEAD_ITALIC = "textheaderItalic";
	public static String ITALIC = "italic";
	public static String BOLD = "bold";
	public static String ADD = "addition";
	public static String VARIANT = "variant";
	public static String RED = "red";
	public static String SMALL = "small";
	public static String LARGE = "large";

	public void setController(TiblEdit tt)
	{
		controller = tt;
	}

	public TiblEdit getController()
	{
		return controller;
	}

	/**
	* <p>
	* This method takes a string array of phrases and a string array of associated style names
	* iterates through them and inserts the strings with their styles one by one into
	* this <code>TextPane</code>'s document.
	* </p>
	*
	* @param phrases <code>String[]</code> - The array of text phrases to be added to the document
	* @param styles <code>String[]</code> - The array of styles associated with the respective text phrases.
	*/
	public void setTextPane(String[] phrases, String[] styles)
	{
        javax.swing.text.StyledDocument doc = new DefaultStyledDocument();
		setDocument(doc);
		caretPos = 0;
        try {
            for (int i=0; i < phrases.length; i++) {
				int lng = doc.getLength();
                doc.insertString(lng, phrases[i],
                                 this.getStyle(styles[i]));
                if(phrases[i].indexOf(ENTER_TRANS_PHRASE)>-1 || phrases[i].indexOf(ENTER_NORM_TRANS_PHRASE)>-1) {
					caretPos = doc.getLength();
				}
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text.");
		}
		if(caretPos > -1) {
			try {
				setCaretPosition(caretPos);
				getCaret().setVisible(true);
			} catch (IllegalArgumentException ia) {System.out.println("Illegal argument in setting caret pos: " + caretPos);}
		} else {
			getCaret().setVisible(false);
		}
	}

	// Document this: it overrides the above, adding a list of elements that are associated with
	// the text runs and returns and {@link ElementList} for referencing.
	public ElementList setTextPane(Vector styleElements)
	{
        javax.swing.text.Document doc = this.getDocument();
        ElementList elemList = new ElementList();
        caretPos=0;
        int len = styleElements.size();
		String phrase = new String();
		String style = new String();
		Iterator it = styleElements.iterator();
		int n = 0;
		while(it.hasNext())
		{
			ElementStyle es = (ElementStyle)it.next();
			phrase = es.getText();
			style = es.getStyle();

			try {
					int lng = doc.getLength();
					int end = lng + phrase.length();
					doc.insertString(lng, phrase, this.getStyle(style));
					elemList.addEntry(lng,end,es);
					if(phrase.indexOf(ENTER_TRANS_PHRASE)>-1 || phrase.indexOf(ENTER_NORM_TRANS_PHRASE)>-1) {
						caretPos = doc.getLength();
					}

			} catch (BadLocationException ble) {
				System.err.println("Couldn't insert initial text.");
			}
		}
		if(caretPos > -1) {
			try {
				setCaretPosition(caretPos);
				getCaret().setVisible(true);
			} catch (IllegalArgumentException ia) {System.out.println("Illegal argument in setting caret pos: " + caretPos);}
		} else {
			getCaret().setVisible(false);
		}

		return elemList;
	}

	/*
	*<p>
	* This method serves to reset the text
	* pane with a blank document with initialized styles ready to open another
	* tibbibl.
	* </p>
	*/
	public void reset()
	{
		setDocument(new DefaultStyledDocument());
		initStyles();
	}

    // Helper Methods
    /**
    * <p>
    * This method initializes the styles used for the given {@link JTextPane}.
    * It adds a regular, italic, bold, small, and large styles, plus styles that
    * change the background color to green, yellow, or red to indicate different
    * types of annotations. Green = addition; yellow = alternative reading or variant;
    * red = omission/deletion.
    * </p>
    */
	protected void initStyles() {

        Style def = StyleContext.getDefaultStyleContext().
                                        getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontFamily(def, BASE_FONT_NAME);
        StyleConstants.setFontSize(def,BASE_FONT_SIZE);

        Style regular = this.addStyle(REG, def);

        Style header = this.addStyle(HEAD, regular);
        StyleConstants.setBold(header, true);
        StyleConstants.setUnderline(header, true);
        StyleConstants.setFontSize(header,HEAD_FONT_SIZE);

        Style s = this.addStyle(HEAD_ITALIC, header);
        StyleConstants.setItalic(s, true);

        Style textHeader= this.addStyle(TEXT_HEAD,regular);
        StyleConstants.setBold(textHeader, true);
        StyleConstants.setFontSize(textHeader,TEXT_HEAD_SIZE);

        s = this.addStyle(TEXT_HEAD_ITALIC,textHeader);
        StyleConstants.setItalic(s,true);

        s = this.addStyle(ITALIC, regular);
        StyleConstants.setItalic(s, true);

        s = this.addStyle(BOLD, regular);
        StyleConstants.setBold(s, true);

        s = this.addStyle(ADD,regular);
        StyleConstants.setBackground(s,Color.green);

        s = this.addStyle(VARIANT,regular);
        StyleConstants.setBackground(s,Color.yellow);

        s = this.addStyle(RED,regular);
        StyleConstants.setForeground(s,Color.red);

        s = this.addStyle(SMALL, regular);
        StyleConstants.setFontSize(s, 10);

        s = this.addStyle(LARGE, regular);
        StyleConstants.setFontSize(s, 16);

    }

	public int getOffset()
	{
		return caretPos;
	}

	public int getPreviousParagraphIndex()
	{
		int start = getSelectionStart();
		if(start>-1) {
			String fulltxt = getText();
			int lastP = fulltxt.lastIndexOf("\n",start);
			return lastP;
		}
		return start;
	}

	public JScrollPane getScrollPane()
	{
		JScrollPane jsp = new JScrollPane(this,
								JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
								JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return jsp;
	}

	// Constructors

	public TextPane(TiblEdit tt)
	{
		super();
		setController(tt);
		initStyles();
		addMouseListener(controller);
		setEditable(false);
		setCaretPosition(0);
		caretPos = 0;
		setMargin(new Insets(10,10,10,10));
	}
}

