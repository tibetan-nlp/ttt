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
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import org.thdl.tib.input.DuffPane;
import org.thdl.tib.input.JskadKeyboard;
import org.thdl.util.SimpleFrame;
import org.thdl.util.ThdlActionListener;
import org.thdl.util.ThdlOptions;

/** Provides a graphical interfase to input Tibetan text (Roman or
    Tibetan script) and displays the words (Roman or Tibetan script)
    with their definitions. Works without Tibetan script in
    platforms that don't support Swing. Can access dictionaries stored
    locally or remotely. For example, to access the public dictionary database run the command:</p>
    <pre>java -jar DictionarySearchStandalone.jar http://orion.lib.virginia.edu/tibetan/servlet/org.thdl.tib.scanner.RemoteScannerFilter</pre>
  <p>If the JRE you installed does not support <i> Swing</i> classes but supports
    <i>
    AWT</i> (as the JRE for handhelds), run the command: </p>
    <pre>java -jar DictionarySearchHandheld.jar -simple ry-dic99</pre>

    @author Andr&eacute;s Montano Pellegrini
*/
public class SwingWindowScannerFilter extends WindowScannerFilter
{		
	public SwingWindowScannerFilter()
	{
	    super();
	}    
    
	public SwingWindowScannerFilter(String file)
	{
	    super(file);
	}
	
	protected WhichDictionaryFrame getWhichDictionaryFrame()
	{
		WhichDictionaryFrame wdf = new SwingWhichDictionaryFrame(mainWindow);
		wdf.setVisible(true);
		return wdf;
	}	
	
	protected void makeWindow(String file)
	{
	    if (mainWindow==null)
	    {
	        String dictType=null;
	        dictType = ThdlOptions.getStringOption(dictOpenType);
	        if (dictType!=null && !dictType.equals(""))
	            mainWindow = new Frame("Tibetan Translation Tool: Connected to " + dictType + " database");
	        else
	            mainWindow = new Frame("Tibetan Translation Tool");
	    }
	    else mainWindow.setVisible(false);
		mainWindow.setLayout(new GridLayout(1,1));
		// mainWindow.setBackground(Color.white);
		
	    diagAbout = null;
	    mnuAbout = null;
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		sp = new DuffScannerPanel(file);
		
		MenuBar mb = new MenuBar();
		Menu m;
	    m = new Menu ("File");
		mnuOpen = new MenuItem("Open...");
		mnuOpen.addActionListener(this);
		m.add(mnuOpen);
		mnuExit = new MenuItem("Exit");
		mnuExit.addActionListener(this);
		m.add(mnuExit);
		mb.add(m);

		m = new Menu ("Edit");		
		mnuCut = new MenuItem("Cut");
		//mnuCut.setShortcut(new MenuShortcut(KeyEvent.VK_X));
		m.add(mnuCut);
		mnuCut.addActionListener(this);
		mnuCopy = new MenuItem("Copy");
		//mnuCopy.setShortcut(new MenuShortcut(KeyEvent.VK_C));
		m.add(mnuCopy);
		mnuCopy.addActionListener(this);
		mnuPaste = new MenuItem("Paste");
		//mnuPaste.setShortcut(new MenuShortcut(KeyEvent.VK_V));
		m.add(mnuPaste);
		mnuPaste.addActionListener(this);		
		mnuDelete = new MenuItem("Delete");
		m.add(mnuDelete);
		mnuDelete.addActionListener(this);		
		m.add("-");
		mnuSelectAll = new MenuItem("Select all");
		//mnuSelectAll.setShortcut(new MenuShortcut(KeyEvent.VK_A));
		m.add(mnuSelectAll);
		mnuSelectAll.addActionListener(this);
		mnuClear = new MenuItem("Clear all");
		m.add(mnuClear);
		mnuClear.addActionListener(this);

		m.add("-");
		mnuPreferences = new MenuItem("Preferences...");
		m.add(mnuPreferences);
		mnuPreferences.addActionListener(this);
		mnuSavePref = new MenuItem("Save preferences to " + ThdlOptions.getUserPreferencesPath());
		m.add(mnuSavePref);
		mnuSavePref.addActionListener(this);
		mnuDicts=null;

		mb.add(m);
		m = new Menu("Tools");
		mnuTranslate  = new MenuItem ("Translate");
		mnuTranslate.setShortcut(new MenuShortcut(KeyEvent.VK_T));
		mnuTranslate.addActionListener(this);
		m.add(mnuTranslate);
		mb.add(m);
		
	    m = new Menu("Help");

        for (int i = 0; i < DuffScannerPanel.keybdMgr.size(); i++)
        {
            final JskadKeyboard kbd = DuffScannerPanel.keybdMgr.elementAt(i);
            if (kbd.hasQuickRefFile())
            {
                MenuItem keybdItem = new MenuItem(kbd.getIdentifyingString());
                keybdItem.addActionListener(new ThdlActionListener()
                {
                    public void theRealActionPerformed(ActionEvent e) 
                    {
                        new SimpleFrame(kbd.getIdentifyingString(),
                                        kbd.getQuickRefPane());
                        /* DLC FIXME -- pressing the "Extended
                        Wylie" menu item (for example) twice
                        causes the first pane to become dead.
                        We should check to see if the first
                        pane exists and raise it rather than
                        creating a second pane. */
                    }
                });
                m.add(keybdItem);
            }
        }   		    
   		m.add("-");

       	mnuAbout = new MenuItem("About...");
        m.add(mnuAbout);
	    mnuAbout.addActionListener(this);
       	mb.add(m);
		
		// disable menus
        focusLost(null);        
        
		sp.addFocusListener(this);
		
		mainWindow.setMenuBar(mb);
		mainWindow.add(sp);
		mainWindow.addWindowListener(this);
		mainWindow.setSize(d);
		// mainWindow.setSize(240,320);
		//else mainWindow.setSize(500,600);
		mainWindow.setVisible(true);
		mainWindow.toFront();
		sp.setFocusToInput();

	    if (!ThdlOptions.getBooleanOption(AboutDialog.windowAboutOption))
	    {
   	        diagAbout = new AboutDialog(mainWindow, false);
	        diagAbout.setVisible(true);
	        
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
	
	public static void printSyntax()
	{
		System.out.println("Sintaxis: java SwingWindowScannerFilter [-firsttime] [arch-dict]");
		System.out.println(TibetanScanner.copyrightASCII);
	}
	
	public static void main(String[] args)
	{
	    String response, dictionaryName=null;
	    PrintStream ps;	    
	    try
	    {
	    	ps = new PrintStream(new FileOutputStream(System
					.getProperty("user.home") + "/tt.log"));
			System.setOut(ps);
			System.setErr(ps);
		} catch (FileNotFoundException fnfe) {
		}
	    
		if (args.length==0)
		{
			new SwingWindowScannerFilter();
		}
		else
		{
			int current=0;
			while(current<args.length)
			{
		    	if (args[current].equals("-firsttime"))
		    	{
		    		current++;
		    		if (current==args.length)
		    		{
		    			System.out.println("Dictionary name expected after -firsttime option!");
		    			printSyntax();
		    			return;
		    		}
		    		response = ThdlOptions.getStringOption(firstTimeOption);
		    		if (response==null || response.equals("") || !response.equals("no"))
					{
		    			ThdlOptions.setUserPreference(defOpenOption, args[current]);
			            ThdlOptions.setUserPreference(dictOpenType, "local");
			            ThdlOptions.setUserPreference(firstTimeOption, "no");
			            try
			            {
			                ThdlOptions.saveUserPreferences();
			            }
			            catch (Exception e)
			            {
			            }
			            dictionaryName = args[current];
					}
		    		current++;
		    	}
		    	else if (args[current].equals("-debug"))
		    	{
		    		TibetanScanner.mode = TibetanScanner.DEBUG_MODE;
		    		current++;
		    	}
		    	else
		    	{
		    		dictionaryName = args[current];
		    		current++;
		    	}
			}
			if (dictionaryName==null) new SwingWindowScannerFilter();
			else new SwingWindowScannerFilter(dictionaryName);
		}
	}
	
	/* FIXME: what happens if this throws an exception?  We'll just
       see it on the console--it won't terminate the program.  And the
       user may not see the console! See ThdlActionListener. -DC */
    public void actionPerformed(ActionEvent event)	
    {
		Object clicked = event.getSource();
		StringSelection ss;
		String s = null;
		
		if (clicked == mnuAbout)
		{
		    if (diagAbout==null) diagAbout = new AboutDialog(mainWindow, false);
		    diagAbout.setVisible(true);
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
		else if (clicked == mnuOpen)
        {
            mainWindow.setVisible(false);
            mainWindow.dispose();
            ThdlOptions.setUserPreference(defOpenOption, "");
            new SwingWindowScannerFilter();
        }
        else if (clicked == mnuExit)
        {
            mainWindow.dispose();
            System.exit(0);
        }
        else if (clicked == mnuPreferences)
        {
            sp.setPreferences(null);
        }
        else if (clicked == mnuSavePref)
        {
            try 
            {
                if (!ThdlOptions.saveUserPreferences()) {
                    JOptionPane.showMessageDialog(mainWindow,
                                                  "You previously cleared preferences,\nso you cannot now save them.",
                                                  "Cannot Save User Preferences",
                                                  JOptionPane.PLAIN_MESSAGE);
                }
            } 
            catch (IOException ioe)
            {
                JOptionPane.showMessageDialog(mainWindow,
                                              "Could not save to your preferences file!",
                                              "Error Saving Preferences",
                                              JOptionPane.ERROR_MESSAGE);
            }
            
        }
        else if (clicked == mnuTranslate)
        {
        	sp.translate();
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
                    //t.insert(s, t.getCaretPosition());
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
}
