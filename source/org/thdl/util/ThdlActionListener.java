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
import java.awt.event.ActionListener;

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
 * @see ThdlAbstractAction
 *
 * @author David Chandler
 *
 * Here is a pertinent Usenet thread from <code>http://groups.google.com/groups?hl=en&lr=&ie=UTF-8&oe=utf-8&threadm=6ntgl6%244hl%241%40tarantula.europe.shiva.com&rnum=2&prev=/groups%3Fq%3Dexception%2BactionPerformed%26hl%3Den%26lr%3D%26ie%3DUTF-8%26oe%3Dutf-8%26selm%3D6ntgl6%25244hl%25241%2540tarantula.europe.shiva.com%26rnum%3D2</code>:


<pre>
From: Denis (denisb@europe.shiva.com)
Subject: Exception in event handlers
 
View this article only
Newsgroups: comp.lang.java.gui, comp.lang.java.help, comp.lang.java.programmer
Date: 1998/07/07

Hi everyone !

I've got a wee question regarding propagation of exceptions in event
handlers :
here's the fun :

Say, you've got a method A() that is called from within an event handler
(mostly it'll be in actionPerformed(ActionEvent) from ActionListener
interface).
Now this A() method produces an exception. This exception will be propagated
up to the caller of actionPerformed which is into the event handler queue.
This exception never gets propagated higher than the handler (and produces a
message "exception occured in event handler")

Now I would like this exception NOT to be caught there but to propagate
until the program terminates or it is caught somewhere else.

Is there a way to do that ? There's no mean to re-throw that exception as
the event handler is part of the core java.

Basically what I would like to do is catch ANY exception that occurs in a
java application in order to display a message box, without having to put
try{} catch{} blocks all other the place...

Thanks for any help on that

Chunk of code to illustrate :

class MyClass extends Object implements ActionListener {
// blah blah

    public void actionPerformed(ActionEvent event) {
        A(); //May throw an exception here but the actionPerformed is called
        // from the event handler and the exception is caught into it

        //So the actionPerformed finishes because of the exception but the
        // rest of the app is likely to be in an unstable state, but still running
    }

   // So here the exception has been caught without an explicit try{}
   // catch{} WHICH I DONT WANT

   //You then can happily do something else....even if A() failed....

}//MyClass

Message 2 in thread
From: David Holmes (dholmes@mri.mq.edu.au)
Subject: Re: Exception in event handlers
 
View this article only
Newsgroups: comp.lang.java.gui, comp.lang.java.help, comp.lang.java.programmer
Date: 1998/07/09

Denis &lt;denisb@europe.shiva.com&gt; wrote in article
&lt;6ntgl6$4hl$1@tarantula.europe.shiva.com&gt;...
&gt; Now I would like this exception NOT to be caught there but to propagate
> until the program terminates or it is caught somewhere else.

Once an exception is caught it is up to the catcher what to do with it. The
event dispatch thread simply catches the exception and tells you about it.
There is no way to make this exception go any further - indeed there is
nowhere further for it to go. Exceptions occur per-thread and if the thread
doesn't catch it the thread will terminate. A more sophisticated event
mechanism might allow you to register a handler/listener to take some
action when this occurred but the current AWT does not.

David
</pre>

 */
public class ThdlActionListener implements ActionListener {
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
               class.  Handle it well so that users know what's up: */
            ThdlDebug.handleClasspathError(null, err);
        } catch (Throwable t) {
            /* FIXME: make aborting optional, and have it off by default */
            System.err.println("THDL_ERR 106: This application failed due to the following exception: ");
            t.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    /** Subclasses should override this method to do the real action
     *  performed by this action listener.
     *  @see #actionPerformed(ActionEvent)
     */
    protected void theRealActionPerformed(ActionEvent e)
        throws Throwable
    {
        /* do nothing */
    }
}

