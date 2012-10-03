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
* <p>This extension of {@link TibAction} is used for the actions associated with the Insert menu of
* the {@link TibFrame}. Its constructor takes a String name and a TibFrame parent. With these it calls its
* super constructor. The action performed takes the following action commands:
* </p>
* <ul>
* <li>{@link TibConstants#ED_INFO ED_INFO} - calls {@link TiblEdit#insertNewEdition()}
* <li>{@link TibConstants#CRIT_TITLE CRIT_TITLE} - calls {@link TiblEdit#insertCritTitle()}
* <li>{@link TibConstants#ED_TITLE ED_TITLE} - calls {@link TiblEdit#insertEdTitle()}
* <li>{@link TibConstants#TITLE_DISC TITLE_DISC} - calls {@link TiblEdit#insertDiscussion()}
* <li>{@link TibConstants#INSERT_APP INSERT_APP} - calls {@link TiblEdit#insertApp()}
* </ul>
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class InsertAction extends TibAction
{


	public InsertAction(String name, TibFrame f)
	{
		super(name,f);
	}

	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();

		if(command.equals(ED_INFO))
		{
			getController().insertNewEdition();

		} else if(command.equals(CRIT_TITLE))
		{
			getController().insertCritTitle();

		} else if(command.equals(ED_TITLE))
		{
			getController().insertEdTitle();

		} else if(command.equals(TITLE_DISC))
		{
			getController().insertDiscussion();
		} else if(command.equals(INSERT_APP))
		{
			getController().insertApp();
		}
	}
}
