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
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text;

import javax.swing.text.ViewFactory;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.text.DefaultEditorKit ;
import javax.swing.Action ;
import javax.swing.text.TextAction ;
import javax.swing.text.JTextComponent ;
import javax.swing.text.BadLocationException ;
import java.awt.event.ActionEvent ;
import org.thdl.tib.text.TibetanMachineWeb ;

/** An EditorKit that is cognizant of the line-wrapping rules for
 *  Tibetan text.  That is, this class knows about the tsheg and other
 *  Tibetan punctuation.
 *  @author David Chandler */
public class TibetanRTFEditorKit extends RTFEditorKit {
    /** Creates a new TibetanRTFEditorKit. */
    public TibetanRTFEditorKit() {
        super();
    }

    /** the Tibetan-aware ViewFactory */
    private TibetanRTFViewFactory f = null;

    /** Returns a ViewFactory that is cognizant of Tibetan punctuation
     *  and line-breaking rules. */
    public ViewFactory getViewFactory() {
        if (null == f) {
            f = new TibetanRTFViewFactory(super.getViewFactory());
        }
        return f;
    }

    class TmwNextWordAction extends TextAction
    {
        boolean selecting = false ;

        public TmwNextWordAction ( String actionName, boolean select )
        {
            super ( actionName ) ;
            selecting = select ;
        }
                    
        public void actionPerformed ( ActionEvent e )
		{
            JTextComponent target = getTextComponent ( e ) ;
			int offset = target.getCaretPosition () ;
            offset = findNextBreakable ( target, offset ) ;
            if ( -1 != offset )
            {
                if ( !selecting )
                    target.setCaretPosition ( offset + 1 ) ;
                else
			        target.moveCaretPosition ( offset + 1 ) ;
            }
		}
    }

    class TmwPrevWordAction extends TextAction
    {
        boolean selecting = false ;

        public TmwPrevWordAction ( String actionName, boolean select )
        {
            super ( actionName ) ;
            selecting = select ;
        }

        public void actionPerformed ( ActionEvent e )
        {
            JTextComponent target = getTextComponent ( e ) ;
			int offset = target.getCaretPosition () ;
            offset = findPrevBreakable ( target, offset ) ;
            if ( -1 != offset )
            {
                if ( !selecting )
                    target.setCaretPosition ( offset ) ;
                else
			        target.moveCaretPosition ( offset ) ;
            }		
        }
    }
    
    public Action [] getActions ()
    {
	    Action [] actions = super.getActions () ;
	
	    for ( int i = 0; i < actions.length; i++ )
	    {
            String actionName = (String)actions [i].getValue ( Action.NAME ) ;
            boolean select = false ;

	        if ( actionName.equals ( DefaultEditorKit.nextWordAction ) || 
                 ( select = actionName.equals ( DefaultEditorKit.selectionNextWordAction ) ) )
	        {
                actions [i] = new TmwNextWordAction ( actionName, select ) ;
	        }
            else if ( actionName.equals ( DefaultEditorKit.previousWordAction ) || 
                      ( select = actionName.equals ( DefaultEditorKit.selectionPreviousWordAction ) ) )
            {
		        actions [i] = new TmwPrevWordAction ( actionName, select ) ;
            }
	    }
	
	    return actions ;
    }

    private static int findNextBreakable ( JTextComponent target, int offset )
    {
        int lastOffset = target.getDocument ().getLength () - 1 ;
        
        try
        {
            char curChar ;
            do
            {
                curChar = target.getText ( offset++, 1 ).charAt ( 0 ) ;
            }
            while ( TibetanMachineWeb.isTMWFontCharBreakable ( curChar ) && offset <= lastOffset ) ;
            
            while ( offset <= lastOffset )
            {
                curChar = target.getText ( offset, 1 ).charAt ( 0 ) ;
                if ( TibetanMachineWeb.isTMWFontCharBreakable ( curChar ) )
                    return offset ;

                offset++ ;
            }
        }
        catch ( BadLocationException e )
        {
            lastOffset = -1 ;
        }

        return lastOffset ;
    }

    private static int findPrevBreakable ( JTextComponent target, int offset )
    {
        int lastOffset = target.getDocument ().getLength () ;
        
        try
        {
            char curChar ;
            do
            {
                curChar = target.getText ( offset--, 1 ).charAt ( 0 ) ;
            }
            while ( TibetanMachineWeb.isTMWFontCharBreakable ( curChar ) && offset >= 0 ) ;

            while ( offset >= 0 )
            {
                curChar = target.getText ( offset, 1 ).charAt ( 0 ) ;
                if ( TibetanMachineWeb.isTMWFontCharBreakable ( curChar ) )
                    return offset ;

                offset-- ;
            }
        }
        catch ( BadLocationException e )
        {
            lastOffset = -1 ;
        }

        return lastOffset ;
    }
}

