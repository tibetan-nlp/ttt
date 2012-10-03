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

import javax.swing.table.AbstractTableModel;

import org.thdl.tib.text.DuffData;
import org.thdl.tib.text.TibTextUtils;

/** Stores the words being displayed in a DictionaryTable.

    @author Andr&eacute;s Montano Pellegrini
	@see DictionaryTable
*/
public class DictionaryTableModel extends AbstractTableModel
{
	private Word []array;
	private DuffData arrayTibetan[][];
	private boolean tibetanActivated;
	
	public DictionaryTableModel(Word[] array)
	{
	    this.newSearch(array);
		tibetanActivated=false;
	}
	
	public int getRowCount()
	{
		if (array!=null)
			return array.length;
		else return 0;
	}
	
	public int getColumnCount()
	{
		return 2;
	}
	
	public void activateTibetan(boolean activate)
	{
		tibetanActivated = activate;
	}
	
	
	public Object getValueAt(int row, int column)
	{
	    switch(column)
	    {
                case 0:
                if (tibetanActivated) return arrayTibetan[row];
                else return array[row].getWylie();
                case 1: return array[row].getDefPreview();
                default: return array[row].toString();
            }
	}
	
	
    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }
    
    public void newSearch(Word[] array)
	{
	    int i, n=0;
	    
	    this.array = array;
	    
		if (array==null)
		    arrayTibetan = null;
		else
		{
		    arrayTibetan = new DuffData[this.array.length][];
		    try
		    {
    		    for (i=0; i<array.length; i++)
	    	        arrayTibetan[i]=TibTextUtils.getTibetanMachineWebForEWTS(array[i].getWylie());
	    	}
	    	catch (Exception e)
	    	{
	    	    System.out.println(e);
	    	}
		}	
	}
}
