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
import java.awt.Font;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.thdl.util.ThdlOptions;

/** Provides a graphical interfase to input Tibetan text (Roman or
    Tibetan script) and displays the words (Roman or Tibetan script)
    with their definitions. Works without Tibetan script in
    platforms that don't support Swing. Can access dictionaries stored
    locally or remotely. It is not meant to be run alone, but through one
    of the inherited childs. Use SwingWindowScannerFilter for platforms
    that support swing. Use PockeWindowScannerFilter for platforms that
    support awt but not swing. 

    @author Andr&eacute;s Montano Pellegrini
*/
public abstract class WindowScannerFilter implements WindowListener, FocusListener, ActionListener, ItemListener
{
	protected static String defOpenOption = "thdl.scanner.default.open";
	protected static String dictOpenType = "thdl.scanner.default.type";
    protected static String defaultFontFace = "thdl.default.roman.font.face";
    protected static String defaultFontSize = "thdl.default.roman.font.size";
    protected static String defaultInputRows = "thdl.scanner.input-pane.rows";
    protected static String defaultOutputRows = "thdl.scanner.output-pane.rows";
	protected static String firstTimeOption = "thdl.scanner.first.time";
    
	protected ScannerPanel sp;
	protected MenuItem mnuExit, mnuCut, mnuCopy, mnuPaste, mnuDelete, mnuSelectAll, mnuAbout, mnuClear, mnuOpen, mnuPreferences, mnuSavePref, mnuTranslate;
	protected CheckboxMenuItem mnuDicts;
	protected Object objModified;
	protected AboutDialog diagAbout;
	protected Frame mainWindow;
	
	/** This class is not meant to be called by itself */
	public WindowScannerFilter(String argument)
	{
		String response, dictType;
		WhichDictionaryFrame wdf;
		mainWindow = null;
		
		if (argument!=null && !argument.equals(""))
		{
			response = argument;
		}
		else
		{
			response = ThdlOptions.getStringOption(defOpenOption);
			
			if (response==null || response.equals(""))
			{
			    mainWindow = new Frame("Tibetan Translation Tool");
			    mainWindow.show();
			    mainWindow.toFront();
			    
			    wdf = getWhichDictionaryFrame();    
	    		response = wdf.getResponse();
		    	wdf.dispose();
			    
			    if (response.equals(""))
			    {
			        mainWindow.dispose();
			        System.exit(0);
			    }
			    else
			    {
			        dictType = wdf.getDictionaryType();
			        mainWindow.setTitle("Tibetan Translation Tool: Connected to " + dictType + " database");
			        if (wdf.getDefaultOption())
			        {
			            ThdlOptions.setUserPreference(defOpenOption, response);
			            ThdlOptions.setUserPreference(dictOpenType, dictType);		            
			            try
			            {
			                ThdlOptions.saveUserPreferences();
			            }
			            catch (Exception e)
			            {
			            }
			        }
			    }	
			}			
		}
    	makeWindow (response);
	}	
	
	public WindowScannerFilter()
	{
		this(null);
	}
	
	protected abstract WhichDictionaryFrame getWhichDictionaryFrame();
	protected abstract void makeWindow(String file);
	
	/**
	 * Cierra la ventana. Invocado unicamente cuando el programa corre
	 * como aplicacion, para que el programa pare su ejecucion cuando
	 * el usuario cierre la ventana principal.
	 */
	 public void windowClosing(WindowEvent e)
	 {
   	 	sp.closingRemarks();
   	 	mainWindow.dispose();   	 	
 	    System.exit(0);
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
	
	/** Added to update the Edit menu in dependence upon
	    which textbox the keyboard focus is at.
	*/
	public abstract void focusGained(FocusEvent e);
	
	/** Added to update the Edit menu in dependence upon
	    which textbox the keyboard focus is at.
	*/
	public void focusLost(FocusEvent e)
	{
	    objModified=null;
		mnuCut.setEnabled(false);
		mnuCopy.setEnabled(false);
		mnuPaste.setEnabled(false);
		mnuDelete.setEnabled(false);
		mnuSelectAll.setEnabled(false);
	}

    public abstract void actionPerformed(ActionEvent event);
    
    public void itemStateChanged(ItemEvent e)
    {
        sp.setWylieInput(e.getStateChange()!=ItemEvent.SELECTED);
    }
    
    public static String getTHDLFontFamilyName()
    {
		String response = ThdlOptions.getStringOption(defaultFontFace);
		int size;
		
		if (response!=null && response.equals("")) return null;
		return response;		
    }
    
    public static void setTHDLFontFamilyName(String font)
    {
        ThdlOptions.setUserPreference(defaultFontFace, font);
    }
    
    public static int getTHDLFontSize()
    {
		return ThdlOptions.getIntegerOption(defaultFontSize, 12);
    }

    public static void setTHDLFontSize(int font)
    {
		ThdlOptions.setUserPreference(defaultFontSize, font);
    }
    
    public static int getInputPaneRows()
    {
		return ThdlOptions.getIntegerOption(defaultInputRows, 4);
    }

    public static void setInputPaneRows(int rows)
    {
		ThdlOptions.setUserPreference(defaultInputRows, rows);
    }
    
    public static int getOutputPaneRows()
    {
		return ThdlOptions.getIntegerOption(defaultOutputRows, 7);
    }

    public static void setOutputPaneRows(int rows)
    {
		ThdlOptions.setUserPreference(defaultOutputRows, rows);
    }
    
    public static Font getTHDLFont()
    {
	    return new Font(WindowScannerFilter.getTHDLFontFamilyName(), Font.PLAIN, WindowScannerFilter.getTHDLFontSize());
    }    
}
