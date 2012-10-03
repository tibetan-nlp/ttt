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
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/** A non-Swing graphical interfase to be used by applications
    running on platforms that don't support Swing,
    to input a Tibetan text (in Roman script only) and
	display the words (in Roman script only) with their
	definitions (in Roman script).
	
    @author Andr&eacute;s Montano Pellegrini
    @see WindowScannerFilter
*/

public class SimpleScannerPanel extends ScannerPanel implements ItemListener
{
	private TextArea txtInput, txtOutput;
	private Panel cardPanel;
	private List listDefs;
	private Word wordArray[];
	private int lenPreview;
	private boolean landscape;
	
	public SimpleScannerPanel(String file, boolean landscape)
	{
		super(file);
		Panel panel1, panel2;
		Font defFont;
		
		this.landscape = landscape;
		cardPanel = new Panel(new CardLayout());
		
		lenPreview = PocketWindowScannerFilter.getParsePaneCols(landscape);
		wordArray=null;

		// panel1 = new Panel(new GridLayout(3, 1));
		
		// txtInput = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
		
    	panel1 = new Panel(new BorderLayout());
		panel2 = new Panel(new GridLayout(2, 1));
		
		defFont = WindowScannerFilter.getTHDLFont();
		    
		txtInput = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		txtInput.setFont(defFont);
		    
    	listDefs = new List();
    	
    	txtOutput = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		txtOutput.setFont(defFont);
    	
		if (landscape)
		{
		    txtOutput.setRows(WindowScannerFilter.getOutputPaneRows());
		    
    		panel2.add(txtInput);
    		panel2.add(listDefs);
    		
		    panel1.add(panel2, BorderLayout.CENTER);
		    panel1.add(txtOutput, BorderLayout.SOUTH);
		}
		else
		{
		    txtInput.setRows(WindowScannerFilter.getInputPaneRows());
		    
    		panel2.add(txtInput);
    		panel2.add(listDefs);
    		
		    panel1.add(panel2, BorderLayout.NORTH);
		    panel1.add(txtOutput, BorderLayout.CENTER);
		}

		listDefs.setMultipleMode(false);
		listDefs.addItemListener(this);
		
		txtOutput.setEditable(false);
		
		cardPanel.add(panel1, "1");
		
		// FIXME: values shouldn't be hardwired
		if (landscape) panel2 = getDictPanel(2);
		else panel2 = getDictPanel(1);
		if (panel2!=null)
		{
		    cardPanel.add(panel2, "2");
		}
		
    	add(cardPanel, BorderLayout.CENTER);
	}
	
	public void setFocusToInput()
	{
		txtInput.requestFocusInWindow();
	}
	
    public void itemStateChanged(ItemEvent e)
    {
        int n = listDefs.getSelectedIndex();
        if (n>-1) txtOutput.setText(wordArray[n].toString());
    }
    
	
	public void setWylieInput(boolean enabled)
	{
	    CardLayout cl = (CardLayout) cardPanel.getLayout();
	    if (enabled) cl.first(cardPanel);
	    else cl.last(cardPanel);
	}
	

	public void addFocusListener(FocusListener fl)
	{
	    txtInput.addFocusListener(fl);
	    txtOutput.addFocusListener(fl);
	}

	public void printAllDefs()
	{
		int i;
		wordArray = scanner.getWordArray();
		String preview;
		
		listDefs.removeAll();
		
		for(i=0; i<wordArray.length; i++)
		{
		    preview = wordArray[i].getWordDefPreview();
		    if (preview.length()>lenPreview) preview = preview.substring(0,lenPreview);
		    listDefs.add(preview);
		}
	}
	
	public void clear()
	{
    	txtInput.setText("");
		txtOutput.setText("");
		listDefs.removeAll();
	}
	
	public void translate()
	{
		String in;
		setDicts(scanner.getDictionarySource());
		in = txtInput.getText();
		doingStatus("Translating...");
		if (!in.equals(""))
		{
			txtOutput.setText("");
			scanner.scanBody(in);
			scanner.finishUp();
			printAllDefs();
			scanner.clearTokens();
			returnStatusToNorm();
		}
    }
    
    public void setPreferences(Frame owner)
    {
        PocketPreferenceWindow ppw = new PocketPreferenceWindow(owner, landscape);
        Font font;
        int i;
        
        if (ppw.dataInputed())
        {
            lenPreview = PocketWindowScannerFilter.getParsePaneCols(landscape);
            font = WindowScannerFilter.getTHDLFont();
            if (landscape) txtOutput.setRows(WindowScannerFilter.getOutputPaneRows());
            else txtInput.setRows(WindowScannerFilter.getInputPaneRows());
            status.setFont(font);
            cmdTranslate.setFont(font);
            txtInput.setFont(font);
            listDefs.setFont(font);
            txtOutput.setFont(font);
            for (i=0; i<chkDicts.length; i++)
                chkDicts[i].setFont(font);
            setFont(font);
            owner.setFont(font);
            validate();
            owner.validate();
        }
        ppw.dispose();
    }
}
