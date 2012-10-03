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

import java.awt.event.ActionEvent;

/**
* <p>
* This extension of {@link TibAction} is for View menu actions.Its constructor takes a String name and a TibFrame parent. With these it calls its
* super constructor. The action performed takes the following action commands:
* </p>
* <ul>
* <li>{@link TibConstants#MASTER_ID_VIEW MASTER_ID_VIEW} - calls {@link TiblEdit#showMasterDox()}
* <li>{@link TibConstants#USER_ID_VIEW USER_ID_VIEW} - calls {@link TiblEdit#getEditorInfo()}
* <li>{@link TibConstants#ABOUT ABOUT} - calls {@link TiblEdit#showSplash()}
* </ul>
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class ViewAction extends TibAction
{

	public ViewAction(String name, TibFrame f)
	{
		super(name,f);
	}

	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		if(command.equals(MASTER_ID_VIEW))
		{
			getController().showMasterDox();
		} else if(command.equals(USER_ID_VIEW))
		{
			getController().getEditorInfo();
		} else if(command.equals(ABOUT))
		{
			getController().showSplash();
		}
	}
}
