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
* This extension of {@link TibAction} is for Edit menu actions.Its constructor takes a String name and a TibFrame parent. With these it calls its
* super constructor. The action performed takes the following action commands:
* </p>
* <ul>
* <li>{@link TibConstants#TRANS_EDIT TRANS_EDIT} - calls {@link TiblEdit#editTranslation()}
* <li>{@link TibConstants#NORM_EDIT NORM_EDIT} - calls {@link TiblEdit#enterTranslation()}
* <li>{@link TibConstants#REMOVE_APP REMOVE_APP} - calls {@link TiblEdit#removeApp()}
* <li>{@link TibConstants#REMOVE_TITLE REMOVE_TITLE} - calls {@link TiblEdit#removeTitle()}
* </ul>
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class EditAction extends TibAction
{

	public EditAction(String name, TibFrame f)
	{
		super(name,f);
	}

	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();

		if(command.equals(TRANS_EDIT))
		{
			getController().editTranslation();
		} else if(command.equals(NORM_EDIT))
		{
			org.jdom.Element normTitle = getController().getTibDoc().getTitleFactory().getNormalizedTitle();
			getController().setSelectedElement(normTitle);
			getController().enterTranslation();
		} else if(command.equals(REMOVE_APP))
		{
			getController().removeApp();
		} else if(command.equals(REMOVE_TITLE)) {
			getController().removeTitle();
		} else if(command.equals(REMOVE_EDITION)) {
			getFrame().chooseEdition();
		}
	}
}
