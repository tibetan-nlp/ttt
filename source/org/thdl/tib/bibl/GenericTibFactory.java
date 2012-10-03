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
* This abstract class is extended by both {@link IDFactory} and {@link TitleFactory}, and provides general functionality
* such as setting the document associated with the factory and setting text/styles for the {@link TextPane}.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

abstract class GenericTibFactory implements TibConstants
{
	// Attributes
	protected TibDoc tibDoc;
	protected Vector outStyles;
	protected Iterator it;
	org.jdom.Element grandparent, parent, child;
    protected java.util.List children;

	// Accessors
	protected void setDoc(TibDoc td)
	{
		tibDoc = td;
	}

	// Generic methods for Setting text, styles and elements
	protected void doReg(String st)
	{
		outStyles.add(new ElementStyle(st,TextPane.REG,NO_ELEM));
	}

	protected void doHeader(String st)
	{
		outStyles.add(new ElementStyle(st,TextPane.HEAD,NO_ELEM));
	}

	protected void doLabel(String st)
	{
		outStyles.add(new ElementStyle(st,TextPane.BOLD,NO_ELEM));
	}

	protected void addLineBreak()
	{
		doReg("\n");
	}

	protected void addSeparator()
	{
		doReg(SEPARATOR);
	}

	protected abstract void processDoc();

	// Constructor
	protected GenericTibFactory(TibDoc td)
	{
		setDoc(td);
		processDoc();
	}
}
