/*
The contents of this file are subject to the AMP Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the AMP web site 
(http://www.tibet.iteso.mx/Guatemala/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is Andres Montano Pellegrini. Portions
created by Andres Montano Pellegrini are Copyright 2001 Andres Montano
Pellegrini. All Rights Reserved. 

Contributor(s): ______________________________________.
*/
package org.thdl.tib.scanner;

import java.awt.TextArea;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

/** Used by the DictionaryTable to display the full definition
	of Tibetan word displayed in a table when its row is clicked.
	
    @author Andr&eacute;s Montano Pellegrini
    @see DictionaryTable
*/
public class DictionaryListSelectionListener implements ListSelectionListener
{
	private TableModel table;
	private TextArea fullDef;
	
	public DictionaryListSelectionListener(TableModel table, TextArea fullDef)
	{
		this.table = table;
		this.fullDef = fullDef;
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting()) return;
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		if (!lsm.isSelectionEmpty())
		{
			int selectedRow = lsm.getMinSelectionIndex();
			//TableModel tm = table.getModel();
			fullDef.setText(table.getValueAt(selectedRow, 2).toString());
		}
	}    
}