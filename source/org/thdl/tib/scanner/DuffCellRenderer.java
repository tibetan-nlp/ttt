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

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import org.thdl.tib.input.DuffPane;
import org.thdl.tib.text.DuffData;
import org.thdl.tib.text.TibetanDocument;
import org.thdl.util.ThdlDebug;

/** Used by DictionaryTable to display a Tibetan word or phrase
	(in either Roman or Tibetan script) in a single cell.
    
    @author Andr&eacute;s Montano Pellegrini
	@see DictionaryTable
*/
public class DuffCellRenderer extends DuffPane implements TableCellRenderer, Serializable
{        
    public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column)
    {
        setValue(value);
        return this;
    }

	public void setValue(Object value)
	{
	    TibetanDocument doc = (TibetanDocument) getDocument();
	    try
	    {
	        doc.remove(0, doc.getLength());
	    }
	    catch (Exception e)
	    {
	        System.out.println(e);
            ThdlDebug.noteIffyCode();
	    }
	    doc.insertDuff(0, (DuffData []) value);
	}
}