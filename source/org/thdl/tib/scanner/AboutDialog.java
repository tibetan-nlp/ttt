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

import java.io.*;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.thdl.util.ThdlOptions;

/** Window that displays copyright stuff.
    
    @author Andr&eacute;s Montano Pellegrini
	@see WindowScannerFilter
*/
public class AboutDialog extends Dialog implements ActionListener, WindowListener
{
   	public static String windowAboutOption = "thdl.scanner.omit.about.window";
   	public static String aboutDocument = "org/thdl/tib/scanner/about.txt"; 
    private Checkbox chkOmitNextTime;
    private Button close;
    
    public String getAboutText()
    {
    	BufferedReader input;
    	input = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(aboutDocument)));
        
        String line;
        StringBuffer text;
    	text = new StringBuffer(TibetanScanner.version);
    	text.append("\n\n");
    	text.append(TibetanScanner.copyrightUnicode);
    	text.append('\n');
        try
        {
	        while ((line=input.readLine())!=null)
	        {
	        	text.append('\n');
	        	text.append(line);
	        }
        }
        catch (IOException ioe)
        {
        	ioe.printStackTrace();
        	return "";
        }
        return text.toString();
    }
    
    public AboutDialog(Frame parent, boolean pocketpc)
    {
        super(parent, "About...", true);
        this.setFont(WindowScannerFilter.getTHDLFont());
        Panel p  = new Panel(new BorderLayout());
        chkOmitNextTime = new Checkbox("Don't show this window at startup", ThdlOptions.getBooleanOption(windowAboutOption));
        p.add(chkOmitNextTime, BorderLayout.CENTER);
        close = new Button("Close this window");
        p.add(close, BorderLayout.EAST);
        add(p, BorderLayout.NORTH);
        close.addActionListener(this);
        TextArea ta = new TextArea(getAboutText(),0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
        ta.setEditable(false);
        addWindowListener(this);
        add(ta, BorderLayout.CENTER);
        if (pocketpc)
        {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(d); // the size ipaq's window.
        }
        else setSize(480,400);
    }
    
/*    public void setDefaultFont(Font f)
    {
        this.setFont(f);
        chkOmitNextTime.setFont(f);
        close.setFont(f);
    }*/
    
    public boolean omitNextTime()
    {
        return chkOmitNextTime.getState();
    }
    
	/* FIXME: what happens if this throws an exception?  We'll just
       see it on the console--it won't terminate the program.  And the
       user may not see the console! See ThdlActionListener. -DC */
    public void actionPerformed(ActionEvent e)
    {
        dispose();
    }
    
	/**
	 * Cierra la ventana. Invocado unicamente cuando el programa corre
	 * como aplicacion, para que el programa pare su ejecucion cuando
	 * el usuario cierre la ventana principal.
	 */
	 public void windowClosing(WindowEvent e)
	 {
   	 	dispose();
	 }

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowActivated(WindowEvent e)
	{
	}

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowClosed(WindowEvent e)
	{
	}

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowDeactivated(WindowEvent e)
	{
	}

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowDeiconified(WindowEvent e)
	{
	}

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowIconified(WindowEvent e)
	{
	}

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowOpened(WindowEvent e)
	{
	}    
}
