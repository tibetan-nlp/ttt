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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
*
* This class provides the mechanism to "parse" a Tibbibl title tag and understand its contents. Some title tags
* will contain AP children that represent variant readings. TitleParser can among other things supply the text
* of the main readings in the title, weeding out the alternative one.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class TitleParser implements TibConstants
{
	Object item;
	String stringItem, outText, titleText;
	org.jdom.Element title, foreign, contentItem;
	org.jdom.Text textItem;
	Vector parts, styleElements, toRemove;

	private void clean()
	{
		/**
		* <p>
		* This method cleans the title element of consequetive Text elements so
		* that the {@link #isAppInsertable} method will not mistakenly think that
		* an app cannot be inserted since it crosses a part boundary. The two consequetive
		* jdom.org.Text elements will be combined and the second one removed.
		* </p>
		*/
		org.jdom.Text lastText, textElement;
		Iterator it = title.getContent().iterator();
		Vector toRemove = new Vector();
		lastText = null;
		while(it.hasNext())
		{
			Object item = it.next();
			if(item instanceof org.jdom.Text) {
				if(lastText == null) {
					lastText = (org.jdom.Text)item;
				} else {
					String lastTextString = lastText.getText();
					textElement = (org.jdom.Text)item;
					String thisTextString = textElement.getText();
					if(!lastTextString.endsWith(" ") &&
						!thisTextString.startsWith(" "))
					{
						thisTextString += " ";
					}
					lastText.setText(lastTextString + thisTextString);
					toRemove.add(item);
				}
			} else {
				lastText = null;
			}
		}
		for(it = toRemove.iterator(); it.hasNext();)
		{
			title.getContent().remove(it.next());
		}
	}

	private void parse()
	{
		/**
		* <p>
		* This method parses the title by finding its constituent parts and creating
		* a {@link TitleParser.TitlePart} out of them and adding each TitlePart to
		* the vector {@link #parts}. The title parts contain the text for display, its
		* beginning and end indexes, and the corresponding JDOM part, whether it be
		* an element, text, or string.
		* </p>
		*/
		parts = new Vector();
		outText = new String();
		Iterator it = title.getContent().iterator();
		TitlePart prevPart = null;

		while(it.hasNext())
		{
			boolean isApp = false;
			item = it.next();
			String stringItem = new String();
			if(item instanceof org.jdom.Element) {
				contentItem = (org.jdom.Element)item;
				if(contentItem.getName().equals(AP)) {
					stringItem = cleanString(contentItem.getChild(LM).getText());
					isApp = true;
				} else if(contentItem.getName().equals(FOREIGN)) {
					foreign = contentItem;
					continue;
				} else {
					stringItem = cleanString(contentItem.getText());
					System.out.println("Found an inner element that's not ap or foreign: " + contentItem.toString());
				}
			} else if(item instanceof org.jdom.Text) {
				textItem = (org.jdom.Text)item;
				stringItem = cleanString(textItem.getText());
			} else if(item instanceof String) {
				stringItem = cleanString((String)item);
			} else {
				System.out.println("A kind of title content found that is not recognized!\n" +
						item.toString());
				continue;
			}

			if(!outText.endsWith(" ") && !stringItem.startsWith(" ")) {
				if(isApp) {
					TitlePart tpart = (TitlePart)parts.lastElement();
					tpart.addSpace();
					outText += " ";
				} else {
					stringItem = " " + stringItem;
				}
			}
			int start = outText.length()+1;
			outText += stringItem;
			parts.add(new TitlePart(stringItem,start,item,isApp));
			prevPart = (TitlePart)parts.lastElement();
		}

		titleText = cleanString(outText);
	}

	private void setStyles()
	{
		ElementStyle es;
		styleElements = new Vector();

		Iterator it = parts.iterator();
		while(it.hasNext())
		{
			es = null;
			TitlePart tpart = (TitlePart)it.next();
			String text = tpart.text;
			if(tpart.isApp) {
				org.jdom.Element e = (org.jdom.Element)tpart.associatedItem;
				es = new ElementStyle(text,TextPane.VARIANT,e);
			} else {
				es = new ElementStyle(text,TextPane.REG,title);
			}
			styleElements.add(es);
		}
		if(styleElements.size()>0) {
			try {
				es = (ElementStyle)styleElements.firstElement();
				if(es.getText().equals(" ")) {styleElements.remove(es);}
				es = (ElementStyle)styleElements.lastElement();
				if(es.getText().equals(" ")) {styleElements.remove(es);}
			} catch (NoSuchElementException nsee) {}
		}
	}

	public void setTitle(org.jdom.Element ttl)
	{
		title = ttl;
		parse();
		setStyles();
	}

	public org.jdom.Element getTitle()
	{
		return title;
	}

	public String getTitleText()
	{
		return titleText;
	}

	public org.jdom.Element getTranslation()
	{
		return foreign;
	}

	public Vector getStyles()
	{
		if(styleElements.size()==0) {
			styleElements.add(new ElementStyle("",TextPane.REG,title));
		}
		return styleElements;
	}

/**
* <p>
* This method checks the parts of the title to see if an app (or apparatus) element is insertable. The
* main thing it checks for is that the range of text does not span over an already existing app.
* </p>
*
* @param startInsert the insertion point within the display of the titles text relative to the beginning of the line.
*
* @param txt the selected text which is to be marked up with the app. This is necessary to make sure that
* the text does not fall completely within an already existing app.
*
* @return boolean - whether or not this is an acceptable place to insert an app element.
*/
	public boolean isAppInsertable(int startInsert, String txt)
	{

		if(parts == null) {return false;}
		Iterator it = parts.iterator();
		while(it.hasNext())
		{
			TitlePart tpart = (TitlePart)it.next();

			int tpStart = tpart.start;
			int tpEnd = tpart.end;
			if(startInsert>=tpStart && startInsert<tpEnd) {
				if(tpart.isApp) {
					return false;
				}
				if(tpart.text.indexOf(txt)>-1 || tpart.text.equals(txt)) {
					return true;
				} else {
					TitlePart oldtp = tpart;
					String backText = tpart.text;
					tpart = (TitlePart)it.next();
					if(tpart.isApp) {return false;}
					backText += tpart.text;
					if(backText.indexOf(txt)>-1) {
						System.out.println("Found a text run that crosses boundaries of a non-app elements!\n");
						System.out.println("Firs element: " + oldtp.associatedItem.toString());
						System.out.println("Second element: " + tpart.associatedItem.toString());
					}
				}
			}
		}
		return false;
	}

	public Object getItemAt(int ind)
	{
		if(parts == null) {return null;}
		Iterator it = parts.iterator();
		while(it.hasNext())
		{
			TitlePart tpart = (TitlePart)it.next();
			int tpStart = tpart.start;
			int tpEnd = tpart.end;
			if(ind>=tpStart && ind<tpEnd) {
				return tpart.associatedItem;
			}
		}
		return null;
	}

	public String getTextAt(int ind)
	{
		if(parts == null) {return null;}
		Iterator it = parts.iterator();
		while(it.hasNext())
		{
			TitlePart tpart = (TitlePart)it.next();
			int tpStart = tpart.start;
			int tpEnd = tpart.end;
			if(ind>=tpStart && ind<tpEnd) {
				return tpart.text;
			}
		}
		return null;
	}

	private String cleanString(String text)
	{
		if(text != null) {
			text = text.replace('\n',' ');
		} else {
			text = "";
			return text;
		}

		StringTokenizer parts = new StringTokenizer(text," ");
		text = new String();
		while(parts.hasMoreTokens()) {
			String word = parts.nextToken();
			if(word != null && !word.equals(" ")) {
				text += word;
				if(parts.hasMoreTokens()) { text += " "; }
			}
		}
		return text;
	}

	public TitleParser(org.jdom.Element ttl)
	{
		setTitle(ttl);
		clean();
		parse();
		setStyles();
	}

	protected class TitlePart
	{
		int start, end;
		String text;
		Object associatedItem;
		boolean isApp;
		boolean isText;

		public void addSpace()
		{
			text = text + " ";
			end = start + text.length();
		}

		public String toString()
		{
			String out = "\nText: " + text;
			out += "\nStart: " + start;
			out += "\nEnd: " + end;
			out += "\nisApp: " + isApp;
			out += "\nisText: " + isText;
			out += "\nAssociatedItem: " + associatedItem.toString();
			out += "\n---------end of item --------\n\n";
			return out;
		}

		public TitlePart(String txt, int ind, Object ai, boolean ia)
		{
			text = txt;
			start = ind;
			end = start + text.length()-1;
			associatedItem = ai;
			isApp = ia;
			if(associatedItem instanceof org.jdom.Text || associatedItem instanceof String) {
				isText = true;
			} else {
				isText = false;
			}
		}
	}
}
