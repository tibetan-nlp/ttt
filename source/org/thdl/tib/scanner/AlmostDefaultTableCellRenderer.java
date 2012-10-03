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
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/** Used by DictionaryTable to display multiple lines of
    text (in Roman script) in a single cell.
    
    @author Andr&eacute;s Montano Pellegrini
	@see DictionaryTable
*/
public class AlmostDefaultTableCellRenderer extends JTextArea
    implements TableCellRenderer, Serializable
{

    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1); 
    
    // We need a place to store the color the JTextArea should be returned 
    // to after its foreground and background colors have been set 
    // to the selection background color. 
    // These ivars will be made protected when their names are finalized. 
    private Color unselectedForeground; 
    private Color unselectedBackground; 


    public AlmostDefaultTableCellRenderer()
    {
    	super();
    	setLineWrap(true);
    	setWrapStyleWord(true);
    	
    	setOpaque(true);
        setBorder(noFocusBorder);
    }
    
        /**
     * Overrides <code>JComponent.setForeground</code> to assign
     * the unselected-foreground color to the specified color.
     * 
     * @param c set the foreground color to this value
     */
    public void setForeground(Color c) {
        super.setForeground(c); 
        unselectedForeground = c; 
    }
    
    /**
     * Overrides <code>JComponent.setForeground</code> to assign
     * the unselected-background color to the specified color.
     *
     * @param c set the background color to this value
     */
    public void setBackground(Color c) {
        super.setBackground(c); 
        unselectedBackground = c; 
    }

    /**
     * Notification from the <code>UIManager</code> that the look and feel
     * [L&F] has changed.
     * Replaces the current UI object with the latest version from the 
     * <code>UIManager</code>.
     *
     * @see JComponent#updateUI
     */
    public void updateUI() {
        super.updateUI(); 
	    setForeground(null);
	    setBackground(null);
    }



    public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column)
    {
	    if (isSelected)
	    {
	        super.setForeground(table.getSelectionForeground());
	        super.setBackground(table.getSelectionBackground());
	    }
	    else
	    {
	        super.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
	        super.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
	    }

    	if (hasFocus) {
	        setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
	        if (table.isCellEditable(row, column)) {
	            super.setForeground( UIManager.getColor("Table.focusCellForeground") );
	            super.setBackground( UIManager.getColor("Table.focusCellBackground") );
    	    }
	    } else {
	        setBorder(noFocusBorder);
	    }
	    
	            
        setValue(value);
        
        // ---- begin optimization to avoid painting background ----
    	Color back = getBackground();
	    boolean colorMatch = (back != null) && ( back.equals(table.getBackground()) ) && table.isOpaque();
        setOpaque(!colorMatch);
	    // ---- end optimization to aviod painting background ----

        return this;
    }

	/** 
		Este metodo no funciona, pero es llamado.
		que hago para devolver el valor correcto!
    */
    public int getLineCount(Object value, int width)
    {
    	setValue(value);
        setSize(width, Integer.MAX_VALUE);
        return getLineCount();
    }
    

    protected void setValue(Object value)
    {
        setText((value==null)? "" : value.toString());
    }
    
}


