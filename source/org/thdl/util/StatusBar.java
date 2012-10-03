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

import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** A StatusBar can be added to a component, typically to the bottom
    of it, in order to show the user the status of the program.  There
    are methods to change the status, and there are actually a LIFO
    stack of status messages if you wish to use them. */
public class StatusBar extends JPanel {
    /** The current status is the String on top of the stack. */
    private Stack statuses;

    private JLabel label;

    /** Creates a status bar. */
    public StatusBar() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        statuses = new Stack();
        label = new JLabel("Visit 'http://www.thdl.org/'.");
        label.setHorizontalAlignment(SwingConstants.LEFT);
        add(label);
    }

    /** Creates a status bar with the initial message msg. */
    public StatusBar(String msg) {
        this();
        pushStatus(msg);
    }

    /** Sets the status to msg, replacing the current status message. */
    public void replaceStatus(String msg) throws NullPointerException {
        if (msg == null)
            throw new NullPointerException();
        if (!statuses.empty())
            statuses.pop();
        statuses.push(msg);
        updateLabel();
    }

    /** Sets the status to msg, leaving the previous status on the
        stack. */
    public void pushStatus(String msg) throws NullPointerException {
        if (msg == null)
            throw new NullPointerException();
        statuses.push(msg);
        updateLabel();
    }

    /** Removes the current status message.  If the stack is then
        empty, the message will be the empty string.  Otherwise, the
        message will be the message then topmost on the stack.
        
        @throws NullPointerException if msg is null
    */
    public void popStatus() {
        if (!statuses.empty()) {
            statuses.pop();
            updateLabel();
        }
    }
    
    /** Returns the String currently displayed in the status bar. */
    public String currentStatus() {
        return getMsgOnTopOfStack();
    }

    /** Returns the status message on top of the stack, or "" if the
        stack is empty. */
    private String getMsgOnTopOfStack() {
        if (statuses.empty())
            return "";
        else
            return (String)statuses.peek();
    }

    /** Sets the displayed text to what's on top of the stack, or ""
        if the stack is empty. */
    private void updateLabel() {
        label.setText("  " + // FIXME: nasty hack to make everything show up
                      getMsgOnTopOfStack());
    }
}
