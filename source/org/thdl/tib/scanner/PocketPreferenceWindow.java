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
import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

class PocketPreferenceWindow extends Dialog implements ActionListener, ItemListener
{
    private Frame owner;
    private Choice chFontNames, fontSizes, paneRows, parsePaneCols;
    private Button ok, cancel;
    private boolean gotStuff, landscape;
    
    PocketPreferenceWindow(Frame owner, boolean landscape)
    {
        super(owner, "Preferences", true);
        
        String s;
        this.setFont(WindowScannerFilter.getTHDLFont());
        this.owner = owner;
        this.landscape = landscape;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(d);
        
        this.gotStuff = false;
        
   		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = genv.getAvailableFontFamilyNames();
		
		Panel p1, p2;
		
		p1 = new Panel(new BorderLayout());
		p1.add(new Label("Font: "), BorderLayout.WEST);
		
		chFontNames = new Choice();
		int i;
		
		chFontNames.add("");
		for (i=0; i<fontNames.length; i++)
		    chFontNames.add(fontNames[i]);
		s = WindowScannerFilter.getTHDLFontFamilyName();
		if (s==null) chFontNames.select(0);
		else chFontNames.select(s);
		chFontNames.addItemListener(this);
		
		p1.add(chFontNames, BorderLayout.CENTER);
		
		p2 = new Panel(new GridLayout(1,2));
		p2.add (p1);
		
		p1 = new Panel(new BorderLayout());
		p1.add(new Label("Size: "), BorderLayout.WEST);
		
		fontSizes = new Choice();
		for (i=10; i<=19; i++)
		    fontSizes.add(Integer.toString(i));
		fontSizes.select(Integer.toString(WindowScannerFilter.getTHDLFontSize()));
		fontSizes.addItemListener(this);
		
		p1.add(fontSizes, BorderLayout.CENTER);
		p2.add(p1);
		
		setLayout(new GridLayout(4,1));
		add(p2);
		
		p1 = new Panel(new BorderLayout());
		
		paneRows = new Choice();
		for (i=1; i<=10; i++)
		    paneRows.add(Integer.toString(i));
		paneRows.addItemListener(this);
		
		if (landscape)
		{
		    p1.add(new Label("Rows in output pane: "), BorderLayout.WEST);
		    paneRows.select(Integer.toString(WindowScannerFilter.getOutputPaneRows()));
		}
		else
		{
		    p1.add(new Label("Rows in input pane: "), BorderLayout.WEST);
    		paneRows.select(Integer.toString(WindowScannerFilter.getInputPaneRows()));
		}
		
		p1.add(paneRows, BorderLayout.CENTER);
		add(p1);
		
		p1 = new Panel(new BorderLayout());
		p1.add(new Label("Cols in parsing pane:"), BorderLayout.WEST);

		parsePaneCols = new Choice();
		for (i=20; i<=75; i++)
		    parsePaneCols.add(Integer.toString(i));
		parsePaneCols.select(Integer.toString(PocketWindowScannerFilter.getParsePaneCols(landscape)));
		parsePaneCols.addItemListener(this);
		
		p1.add(parsePaneCols, BorderLayout.CENTER);
		add(p1);
		
		ok = new Button("OK");
        ok.setEnabled(false);
        ok.addActionListener(this);
        
		cancel = new Button("Cancel");
        cancel.addActionListener(this);
		
		p1 = new Panel(new FlowLayout());

		p1.add(ok);		
		p1.add(cancel);
		
		add(p1);
    }
    
    public boolean dataInputed()
    {
        this.show();
        return gotStuff;
    }
    
    public void actionPerformed(ActionEvent e)    
    {
		Object obj = e.getSource();
		if (obj == ok)
		{
		    gotStuff = true;
		    WindowScannerFilter.setTHDLFontFamilyName(chFontNames.getSelectedItem());
		    WindowScannerFilter.setTHDLFontSize(Integer.parseInt(fontSizes.getSelectedItem()));
		    PocketWindowScannerFilter.setParsePaneCols(landscape, Integer.parseInt(parsePaneCols.getSelectedItem()));
		    
		    if (landscape) WindowScannerFilter.setOutputPaneRows(Integer.parseInt(paneRows.getSelectedItem()));
		    else PocketWindowScannerFilter.setInputPaneRows(Integer.parseInt(paneRows.getSelectedItem()));
		    
		    owner.setFont(WindowScannerFilter.getTHDLFont());
		    owner.repaint();
		}
        this.setVisible(false);
    }
    
    public void itemStateChanged(ItemEvent e)
    {
        ok.setEnabled(true);
    }
}