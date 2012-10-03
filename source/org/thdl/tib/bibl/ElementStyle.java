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


/**
* This wrapper class associates a String of text with a Style (as defined in {@link TibConstants} [MAYBE]) and a particular
* element. It is used for interactivity with the {@link TextPane} so that highlighting or clicking on a string in the
* text pane can be associated with actions for processing the text within an element.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class ElementStyle implements TibConstants
{
	String style;
	org.jdom.Element element;
	String text;

	public String getStyle()
	{
		return style;
	}

	public String getText()
	{
		return text;
	}

	public org.jdom.Element getElement()
	{
		return element;
	}

	public ElementStyle(String t, String s, org.jdom.Element e)
	{
		text = t;
		style = s;
		element = e;
	}
}
