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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.TextArea;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/** Table of two columns that displays a Tibetan word or
	phrase (in either Tibetan or Roman script) and the
	first couple of lines of its definitions. Clicking on
	a word displays full definition on a separate text box.

    @author Andr&eacute;s Montano Pellegrini
*/
public class DictionaryTable extends JTable
{
	private DuffScannerPanel padre;
	private int tibetanHeight, normalHeight;
	private TableCellRenderer duffRenderer, normalRenderer;
	public DictionaryTable(DictionaryTableModel td, TextArea fullDef)
	{
		this.setModel(td);
		this.setRowHeight(40);
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
		//tableHeader = null;
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		ListSelectionModel rowSM = getSelectionModel();
		rowSM.addListSelectionListener(new DictionaryListSelectionListener(td, fullDef));
		
		TableColumnModel tcm = this.getColumnModel();
		normalRenderer = new AlmostDefaultTableCellRenderer();
		duffRenderer = new DuffCellRenderer();

		TableColumn tc = tcm.getColumn(0);
		tc.setPreferredWidth(10);
		tc.setCellRenderer(normalRenderer);
		tc.setHeaderValue("Word");

		tc = tcm.getColumn(1);
		tc.setCellRenderer(normalRenderer);
		tc.setHeaderValue("Definition");
		tibetanHeight = normalHeight = -1;
	}
	
	public void resizeRows()
	{
		int i, n = getRowCount(), visibleHeight, width;
		JLabel jl;
		AlmostDefaultTableCellRenderer ja;
		FontMetrics fm;

		if (n>0)
		{
			ja = (AlmostDefaultTableCellRenderer) normalRenderer;
			if (tibetanHeight<0)
			{
				jl = (JLabel) duffRenderer;
                fm = jl.getFontMetrics(jl.getFont());
				tibetanHeight = fm.getHeight();
				fm = ja.getFontMetrics(ja.getFont());
				normalHeight = fm.getHeight();
			}
			
			width = getColumnModel().getColumn(1).getWidth();
			
			for (i=0; i<n; i++)
			{
				visibleHeight = normalHeight * ja.getLineCount(getValueAt(i,1), width);
				if (visibleHeight > tibetanHeight)
					setRowHeight(i, visibleHeight);
			}
		}
	}
	
	public void activateTibetan(boolean activate)
	{
		TableColumnModel tcm = this.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		if (activate)
			tc.setCellRenderer(duffRenderer);
		else
			tc.setCellRenderer(normalRenderer);
		DictionaryTableModel dtm = (DictionaryTableModel) getModel();
		dtm.activateTibetan(activate);
	}
	
	public void setTibetanFontSize(int size)
	{
	    DuffCellRenderer dcr = (DuffCellRenderer) duffRenderer;
	    dcr.setTibetanFontSize(size);
	}
	
	public void setRomanFont(Font f)
	{
	    AlmostDefaultTableCellRenderer tcr = (AlmostDefaultTableCellRenderer) normalRenderer;
	    tcr.setFont(f);
	}
}