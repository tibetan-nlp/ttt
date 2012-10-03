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

import javax.swing.AbstractAction;

/**
* This extension of javax.swing.AbstractAction is used for the various menus in {@link TibFrame}. It assigns two
* special key/value pairs: the frame, which points to the TibFrame, and the controller which is the main program,
* TiblEdit.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

abstract class TibAction extends AbstractAction implements TibConstants
{
	// Accessors
	public void setKeys(TibFrame f)
	{
		putValue(FRAME,f);
		putValue(CONTROLLER,f.getController());
	}

	public TibFrame getFrame()
	{
		return (TibFrame)getValue(FRAME);
	}

	public TiblEdit getController()
	{
		return (TiblEdit)getValue(CONTROLLER);
	}

	// Constructor
	TibAction(String name, TibFrame f)
	{
		super(name);
		setKeys(f);
	}
}
