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

package org.thdl.util;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * This ActionListener is like any other except in the way that it
 * handles exceptions or errors thrown during the execution of
 * <code>actionPerformed()</code>.  Because event listeners are on
 * threads, an exception during <code>actionPerformed()</code> is just
 * printed out on the console by
 * <code>java.awt.EventDispatchThread.run()</code>.  It does not cause
 * the program to terminate.  In our code, it helps developers more
 * quickly get to the root of a problem if the program terminates as
 * soon after a problem as possible.
 *
 * Thus, this class calls <code>System.exit(1)</code> when an
 * exception is throw by <code>theRealActionPerformed()</code>, which
 * is the method that subclasses should implement.
 *
 * @see ThdlActionListener
 *
 * @author David Chandler
 *
 * There is a pertinent Usenet thread at <code>http://groups.google.com/groups?hl=en&lr=&ie=UTF-8&oe=utf-8&threadm=6ntgl6%244hl%241%40tarantula.europe.shiva.com&rnum=2&prev=/groups%3Fq%3Dexception%2BactionPerformed%26hl%3Den%26lr%3D%26ie%3DUTF-8%26oe%3Dutf-8%26selm%3D6ntgl6%25244hl%25241%2540tarantula.europe.shiva.com%26rnum%3D2</code>.
 */
public class ThdlAbstractAction extends AbstractAction {

	/** Just calls the super's constructor with the same args. */
	public ThdlAbstractAction(String s, Icon i) {
		super(s, i);
	}

    /** Subclasses don't override this.  Instead, they override
     *  <code>theRealActionPerformed()</code>.
     *  @see #actionPerformed(ActionEvent)
     */
    public final void actionPerformed(ActionEvent e) {
        try {
            theRealActionPerformed(e);
        } catch (NoClassDefFoundError err) {
            /* This is an especially common exception, and is in fact
               the motivating force behind the ThdlActionListener
               class and this class.  Handle it well so that users
               know what's up: */
            ThdlDebug.handleClasspathError(null, err);
        } catch (Throwable t) {
            System.err.println("THDL_ERR 107: This application failed due to the following exception: ");
            t.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    /** Subclasses should override this method to do the real action
     *  performed.
     *  @see #actionPerformed(ActionEvent)
     */
    protected void theRealActionPerformed(ActionEvent e)
        throws Throwable
    {
        /* do nothing */
    }
}

