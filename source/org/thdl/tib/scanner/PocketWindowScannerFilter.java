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

import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.thdl.util.ThdlOptions;

/** Provides a graphical interfase to input Tibetan text (in Roman script)
    and displays the words (in Roman script) with their definitions. Use
    this version for platforms that support awt but not swing; it was
    re-designed to fit with the implementation of the J2ME Personal Profile
    1.0 (JSR-62) used by some implementations of Java for Pocket pc's. The
    command line to include in the shortcut file (.lnk) in the pocket pc to
    run using IBM's WebSphere Everyplace Micro Environment Personal Profile
    1.0 for Windows Mobile 2003 is:
    <pre>255#"\Program Files\J9\PPRO10\bin\j9w.exe" "-jcl:ppro10" "-cp"  "\program files\TranslationTool\DictionarySearchHandheld.jar" "org.thdl.tib.scanner.PocketWindowScannerFilter"</pre>
    
    Can access dictionaries stored locally or remotely via the command line or
    the wizard window.

    @author Andr&eacute;s Montano Pellegrini
    
     
*/
public class PocketWindowScannerFilter extends WindowScannerFilter
{
    protected static String defaultPortraitParseCols = "thdl.scanner.portrait.parse-pane.cols";
    protected static String defaultLandscapeParseCols = "thdl.scanner.landscape.parse-pane.cols";
    
	public PocketWindowScannerFilter()
	{
	    super();
	}
	
	public PocketWindowScannerFilter(String file)
	{
	    super(file);
	}
	
	protected WhichDictionaryFrame getWhichDictionaryFrame()
	{
		PocketWhichDictionaryFrame wdf = new PocketWhichDictionaryFrame(mainWindow);
		wdf.setFont(WindowScannerFilter.getTHDLFont());
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
	        mainWindow.setFont(WindowScannerFilter.getTHDLFont());
	    }
	    else mainWindow.setVisible(false);
		mainWindow.setLayout(new GridLayout(1,1));
		mainWindow.setBackground(Color.white);
		
	    diagAbout = null;
	    mnuAbout = null;
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		sp = new SimpleScannerPanel(file, d.width >d.height);
		
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

		mb.add(m);
		
        m = new Menu("View");
	    mnuDicts = new CheckboxMenuItem("Dictionaries", false);
	    m.add(mnuDicts);
        mnuDicts.addItemListener(this);
        mb.add(m);
    	
	    m = new Menu("Help");

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

	    if (!ThdlOptions.getBooleanOption(AboutDialog.windowAboutOption))
	    {
	        // hardwiring pocketpc
   	        diagAbout = new AboutDialog(mainWindow, true);
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
		System.out.println("Sintaxis: java PocketWindowScannerFilter [-firsttime] dict-file");
		System.out.println(TibetanScanner.copyrightASCII);
	}	
	
	public static void main(String[] args)
	{
	    PrintStream ps;
	    String response;
	    
	    try
	    {
	    	ps = new PrintStream(new FileOutputStream(System
					.getProperty("user.home") + "/tt.log"));
			System.setOut(ps);
			System.setErr(ps);
		} catch (FileNotFoundException fnfe) {
		}
		
		switch(args.length)
		{
		    case 0:
		    new PocketWindowScannerFilter();
		    break;
		    case 1:
		    if (args[0]==null || args[0].trim().equals("")) new PocketWindowScannerFilter();
		    else new PocketWindowScannerFilter(args[0]);
		    break;
		    case 2:
	    	if (args[0].equals("-firsttime"))
	    	{
	    		response = ThdlOptions.getStringOption(firstTimeOption);
	    		if (response==null || response.equals("") || !response.equals("no"))
				{
	    			ThdlOptions.setUserPreference(defOpenOption, args[1]);
		            ThdlOptions.setUserPreference(dictOpenType, "local");
		            ThdlOptions.setUserPreference(firstTimeOption, "no");
		            try
		            {
		                ThdlOptions.saveUserPreferences();
		            }
		            catch (Exception e)
		            {
		            }
		            new PocketWindowScannerFilter(args[1]);
				}
	    		else new PocketWindowScannerFilter();
	    	}	
	    	else
	    	{
			    System.out.println("Syntax error! Invalid option.");
				printSyntax();
	    	}
		    break;
		    default:
		    System.out.println("Syntax error!");
			printSyntax();
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
		    // hardwiring pocketpc
		    if (diagAbout==null)
		    {
		        diagAbout = new AboutDialog(mainWindow, true);
		    }
		    diagAbout.setFont(WindowScannerFilter.getTHDLFont());
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
            new PocketWindowScannerFilter();
        }
        else if (clicked == mnuExit)
        {
            mainWindow.dispose();
            System.exit(0);
        }
        else if (clicked == mnuPreferences)
        {
            sp.setPreferences(mainWindow);
        }
        else if (clicked == mnuSavePref)
        {
            try
            {
                ThdlOptions.saveUserPreferences();
            }
            catch (Exception e)
            {
            }

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
	
    public static int getParsePaneCols(boolean landscape)
    {
        if (landscape) return ThdlOptions.getIntegerOption(defaultLandscapeParseCols, 47);
		else return ThdlOptions.getIntegerOption(defaultPortraitParseCols, 36);
    }    

    public static void setParsePaneCols(boolean landscape, int cols)
    {
        
        if (landscape) ThdlOptions.setUserPreference(defaultLandscapeParseCols, cols);
        else ThdlOptions.setUserPreference(defaultPortraitParseCols, cols);
    }
}