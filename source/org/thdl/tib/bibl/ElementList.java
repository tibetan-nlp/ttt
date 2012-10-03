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
import java.util.Vector;

/**
* <p>
* Element List is a list of elements and ranges. The ranges refer to the {@link TextPane} locations
* where the text for that element is found. The {@link #getElementAt(int)} method takes an integer location
* in the TextPane and returns the <code>org.jdom.Element</code> that is associated with that location
* <i>if there is one</i>! If not, it returns <code>null</code>, indicating there is nothing to edit at
* that location. This is created by the TextPane and given to the {@link TiblEdit} controller so
* it can turn on and off menu options and functionality, depending on the position of the cursor in
* the TextPane.
* </p>
*
* @author Than Garson, Tibetan and Himalayan Digital Library
* @version 1.0
*/

public class ElementList implements TibConstants
{
	//
	Vector entries;
	int start, end, leng;
	org.jdom.Element element;
	Entry ent, lastEntry;

	public void addEntry(int st, int en, ElementStyle es)
	{
		ent = new Entry(st,en,es);
		entries.add(ent);
		lastEntry = ent;
	}

	public org.jdom.Element getElementAt(int pos)
	{
		org.jdom.Element returnElement = NO_ELEM;
		Iterator ents = entries.iterator();
		while(ents.hasNext()) {
			Entry ent = (Entry)ents.next();
			if(ent.start<=pos && ent.end>=pos) {
				//System.out.println("returning a found element: " + TiblEdit.outputString(ent.element));
				return ent.element;
			}
		}

		return returnElement;
	}

	// Contructor

	public ElementList()
	{
		entries = new Vector();
		lastEntry = new Entry(-1,-1,new ElementStyle("","",NO_ELEM));
	}


	protected class Entry
	{
		//
		int start, end;
		org.jdom.Element element;

		protected Entry(int start, int end, ElementStyle es)
		{
			this.start = start;
			this.end = end;
			element = es.getElement();
		}
	}
}
