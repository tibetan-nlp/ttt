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

import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JApplet;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import org.thdl.tib.input.DuffPane;
import org.thdl.util.ThdlOptions;

/** Inputs a Tibetan text and displays the words with
	their definitions through through a graphical interfase using a
	Browser over the Internet. The graphical interfase is provided
	by implementations of the ScannerPanel.
	
	<p>Parameter URL should contain the URL of the
	servlet which is going to handle to the
	looking up of the words in the server.</p>
	<p>Since the applet uses Swing for the THDL inputting
	system, run the HTML file through Sun's Java Plug-in
	HTML Converter to ensure that the browser will
	use a JVM to run the applet.</p>

    @author Andr&eacute;s Montano Pellegrini
    @see RemoteScannerFilter
    @see ScannerPanel
*/
public class AppletScannerFilter extends JApplet implements ActionListener, FocusListener, ItemListener
{
	private JMenuItem mnuSelectAll, aboutItem, mnuClear, mnuCut, mnuCopy, mnuPaste, mnuDelete;
	private JCheckBoxMenuItem tibScript;
	
	private JMenu mnuEdit;
	private Object objModified;
	private AboutDialog diagAbout;
	ScannerPanel sp;
	private Frame fakeFrame;
	
	public void init()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		String url = getParameter("url");
		if (url.indexOf("http://")<0)
		{
			url = getCodeBase() + url;
		}
		
		diagAbout = null;
		
		// sp = new SimpleScannerPanel(url);
		sp = new DuffScannerPanel(url);		
		sp.addFocusListener(this);
		setContentPane(sp);
		
		// setup the menu. Almost identical to WindowScannerFilter, but using swing.
		JMenuBar mb = new JMenuBar();
		mnuEdit = new JMenu ("Edit");
		mnuCut = new JMenuItem("Cut");
		mnuCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, java.awt.Event.CTRL_MASK));
		mnuEdit.add(mnuCut);
		mnuCut.addActionListener(this);
		mnuCopy = new JMenuItem("Copy");
		mnuCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, java.awt.Event.CTRL_MASK));
		mnuEdit.add(mnuCopy);
		mnuCopy.addActionListener(this);
		mnuPaste = new JMenuItem("Paste");
		mnuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, java.awt.Event.CTRL_MASK));
		mnuEdit.add(mnuPaste);
		mnuPaste.addActionListener(this);		
		mnuDelete = new JMenuItem("Delete");
		mnuEdit.add(mnuDelete);
		mnuDelete.addActionListener(this);
		mnuEdit.addSeparator();
		mnuSelectAll = new JMenuItem("Select all");
		mnuSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, java.awt.Event.CTRL_MASK));
		mnuEdit.add(mnuSelectAll);
		mnuSelectAll.addActionListener(this);
		mnuClear = new JMenuItem("Clear all");
		mnuEdit.add(mnuClear);
		mnuClear.addActionListener(this);		
		mb.add(mnuEdit);
		
		JMenu m;
		
   		/* m = new JMenu("View");
    	tibScript = new JCheckBoxMenuItem("Tibetan Script", true);
	    m.add(tibScript);
   		tibScript.addItemListener(this);
   		mb.add(m);*/
		
		aboutItem = new JMenuItem("About...");
		aboutItem.addActionListener(this);
		
		m = new JMenu("Help");
		m.add(aboutItem);
		mb.add(m);
		setJMenuBar(mb);

        //mnuEdit.setEnabled(false);
	
		//{{REGISTER_LISTENERS
		SymComponent aSymComponent = new SymComponent();
		this.addComponentListener(aSymComponent);
		//}}
				
		fakeFrame = new Frame();
	    if (!ThdlOptions.getBooleanOption(AboutDialog.windowAboutOption))
	    {
            diagAbout = new AboutDialog(fakeFrame, true);
            
	        diagAbout.show();
	        if (diagAbout.omitNextTime())
	        {
	            ThdlOptions.setUserPreference(AboutDialog.windowAboutOption, true);
	            try
	            { 
	                ThdlOptions.saveUserPreferences();
	            }
	            catch(Exception e)
	            {
	            }
	        }
	    }
	}
	
	/** Added to update the Edit menu in dependence upon
	    which textbox the keyboard focus is at.
	*/
	public void focusGained(FocusEvent e)
	{
	    objModified = e.getSource();
	    boolean isEditable=false;
	    
	    if (objModified instanceof TextComponent)
	    {
	        TextComponent t = (TextComponent) objModified;
	        isEditable = t.isEditable();
	    }
	    else if (objModified instanceof JTextComponent)
	    {
	        JTextComponent j = (JTextComponent) objModified;
	        isEditable = j.isEditable();
	    }
	    
	    //mnuEdit.setEnabled(true);
        mnuCut.setEnabled(isEditable);
        if (isEditable)
        {
            if (Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this)!=null)
                mnuPaste.setEnabled(true);
        }
        else mnuPaste.setEnabled(false);
        mnuDelete.setEnabled(isEditable);
		mnuCopy.setEnabled(true);
		mnuSelectAll.setEnabled(true);
	}
	
	/** Added to update the Edit menu in dependence upon
	    which textbox the keyboard focus is at.
	*/
	public void focusLost(FocusEvent e)
	{
/*	    objModified=null;
		mnuCut.setEnabled(false);
		mnuCopy.setEnabled(false);
		mnuPaste.setEnabled(false);
		mnuDelete.setEnabled(false);
		mnuSelectAll.setEnabled(false);*/
	}
	/* FIXME: what happens if this throws an exception?  We'll just
       see it on the console--it won't terminate the program.  And the
       user may not see the console! See ThdlActionListener. -DC */
    public void actionPerformed(ActionEvent event)	
    {
		Object clicked = event.getSource();
		StringSelection ss;
		String s = null;
		
		if (clicked==aboutItem)
		{
		    if (diagAbout==null)
		    {
		        diagAbout = new AboutDialog(fakeFrame, true);
		    }
		    diagAbout.show();
		    ThdlOptions.setUserPreference(AboutDialog.windowAboutOption, diagAbout.omitNextTime());
		    try
	        { 
	            ThdlOptions.saveUserPreferences();
	        }
	        catch(Exception e)
	        {
	        }		    
		}
		else if (clicked == mnuClear)
		{
		    sp.clear();
		}
		else
		{
    		if (objModified==null) return;
		
	    	if (objModified instanceof TextArea)
		    {
		        TextArea t = (TextArea) objModified;
		    
        		if (clicked == mnuCut)
	        	{
	        	    ss = new StringSelection(t.getSelectedText());
	    	        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,ss);
	    	        t.replaceRange("", t.getSelectionStart(), t.getSelectionEnd());
                }
                else if (clicked == mnuCopy)
                {                    
	    	        ss = new StringSelection(t.getSelectedText());
	    	        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,ss);
                }
                else if (clicked == mnuPaste)
                {
		            Transferable data = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
        		    try 
            		{
            		    s = (String)(data.getTransferData(DataFlavor.stringFlavor));
            		}
        	    	catch (Exception ex)
        		    {
    			        s = data.toString();
	    	        }
	    	        t.replaceRange(s, t.getSelectionStart(), t.getSelectionEnd());
                }
                else if (clicked == mnuDelete)
                {
                    t.replaceRange("", t.getSelectionStart(), t.getSelectionEnd());
                }
                else if (clicked == mnuSelectAll)
                {
                    t.selectAll();
                }
            }   
            else if (objModified instanceof DuffPane)
            {
                DuffPane t = (DuffPane) objModified;
            
        		if (clicked == mnuCut)
	        	{
	        	    t.cut();
                }
                else if (clicked == mnuCopy)
                {
                    t.copy();
                }
                else if (clicked == mnuPaste)
                {
                    t.paste(t.getCaret().getDot());
                }
                else if (clicked == mnuDelete)
                {
                    try
                    {
                        t.getDocument().remove(t.getSelectionStart(), t.getSelectionEnd());
                    }
                    catch (Exception ex)
                    {
                        System.out.println(ex);
                    }
                }
                else if (clicked == mnuSelectAll)
                {
                    t.selectAll();
                }    
            }
        }
    }	
    public void itemStateChanged(ItemEvent e)
    {
        sp.setWylieInput(e.getStateChange()!=ItemEvent.SELECTED);
    }

	class SymComponent extends java.awt.event.ComponentAdapter
	{
		public void componentMoved(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == AppletScannerFilter.this)
				AppletScannerFilter_componentMoved(event);
		}
	}

	void AppletScannerFilter_componentMoved(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
	}
}
