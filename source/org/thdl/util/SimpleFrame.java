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

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

/** An SimpleFrame is a top-level window displaying a JScrollPane. */
public class SimpleFrame extends JFrame {
    /** Creates a visible JFrame displaying the contents of the
     *  component comp.
     *
     *  @param frameTitle the title (on the title bar) of the window
     *  @param comp the component to be placed within this frame */
    public SimpleFrame(String frameTitle, final Component comp)
    {
        super(frameTitle);
        setSize(500,400);
        Container c = getContentPane();
        c.addComponentListener(new ComponentAdapter()
            {
                public void componentResized(ComponentEvent e)
                {
                    comp.setSize(e.getComponent().getSize());
                }
            });
        comp.setSize(c.getSize());
        c.add(comp);
        setLocation(100,100);
        setVisible(true);
    }
}
