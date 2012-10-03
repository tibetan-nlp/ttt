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
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;

import org.thdl.tib.input.DuffPane;
import org.thdl.tib.input.JskadKeyboardFactory;
import org.thdl.tib.input.JskadKeyboardManager;
import org.thdl.tib.input.PreferenceWindow;
import org.thdl.util.ThdlLazyException;
import org.thdl.util.ThdlOptions;

/** Graphical interfase to be used by applications and applets
	to input a Tibetan text (in Roman or Tibetan script) and
	display the words (in Roman or Tibetan script) with their
	definitions (in Roman script). Uses the THDL inputting system.
	
    @author Andr&eacute;s Montano Pellegrini
    @see WindowScannerFilter
    @see AppletScannerFilter
*/
public class DuffScannerPanel extends ScannerPanel implements ItemListener
{
	private TextArea fullDef, txtInput;
	private DuffPane duffInput;
	private JPanel inputPanel;
	
	private JScrollPane listDef;
//	private Font tibetanFont;
	private DictionaryTable table;

	private DictionaryTableModel model;
	
	boolean showingTibetan;
	
    private PreferenceWindow prefWindow;
    private Choice script, keyboard;
    
    /** the middleman that keeps code regarding Tibetan keyboards
     *  clean */
    final static JskadKeyboardManager keybdMgr;

    static {
        try {
            keybdMgr
                = new JskadKeyboardManager(JskadKeyboardFactory.getAllAvailableJskadKeyboards());
        } catch (Exception e) {
            throw new ThdlLazyException(e);
        }
    }

	public DuffScannerPanel(String file)
	{
		super(file, true);
		Panel panel1, panel2, toolBar;
		int i;
        Font f;
		
		prefWindow = null;
		
		panel2 = new Panel(new BorderLayout());
		panel1 = getDictPanel();
		if (panel1!=null) panel2.add (panel1, BorderLayout.NORTH);
		panel1 = new Panel(new GridLayout(3,1));
		/* Looks up tibcodes in directory of applet. In order
		to work through a proxy store all the applet classes toghether
		with tibcodes.ini in a jar file. */
		duffInput = new StrictDuffPane();
        f = new Font(duffInput.getRomanFontFamily(), Font.PLAIN, duffInput.getRomanFontSize());
		
		JPanel jpanel = new JPanel(new GridLayout(1,1));
		JScrollPane jsp = new JScrollPane(duffInput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jpanel.add(jsp);
		inputPanel = new JPanel(new CardLayout());
		inputPanel.add(jpanel, "1");

		txtInput = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);		
        txtInput.setFont(f);
		inputPanel.add(txtInput, "2");
		panel1.add(inputPanel);

		fullDef = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		fullDef.setEditable(false);
        fullDef.setFont(f);
		model = new DictionaryTableModel(null);
		table = new DictionaryTable(model, fullDef);
		table.activateTibetan(true);
        table.setRomanFont(f);
		listDef = new JScrollPane(table);

		panel1.add(listDef);
		panel1.add(fullDef);
		panel2.add(panel1, BorderLayout.CENTER);
		add(panel2, BorderLayout.CENTER);
		showingTibetan = true;

		toolBar = getToolBar();
		toolBar.setLayout(new FlowLayout());
		toolBar.add(new Label("Display:"));
		
		script = new Choice();
		script.add("Tibetan script");
		script.add("Roman script");
		toolBar.add(script);
		script.addItemListener(this);
		
		toolBar.add(new Label("Keyboard:"));
		
		keyboard = new Choice();
		String keyboardNames[] = keybdMgr.getIdentifyingStrings();
		for (i=0; i<keyboardNames.length; i++)
		    keyboard.add(keyboardNames[i]);
		
        int initialKeyboard
            = ThdlOptions.getIntegerOption("thdl.default.tibetan.keyboard", 0);
        try {
            keyboard.select(initialKeyboard);
        } catch (IllegalArgumentException e) {
            initialKeyboard = 0; // good ol' Wylie
            keyboard.select(initialKeyboard);
        }
        keybdMgr.elementAt(initialKeyboard).activate(duffInput);

        keyboard.addItemListener(this);
		toolBar.add(keyboard);
	}
	
	public void setFocusToInput()
	{
		duffInput.requestFocusInWindow();
	}

	public void addFocusListener(FocusListener fl)
	{
	    txtInput.addFocusListener(fl);
	    duffInput.addFocusListener(fl);
	    fullDef.addFocusListener(fl);
	}

	public void itemStateChanged(ItemEvent e)
	{
	    Object obj = e.getSource();
	    if (obj instanceof Choice)
	    {
	        Choice ch = (Choice) obj;
	        if (ch == script)
	        {
	            boolean useTibetan = (ch.getSelectedIndex()==0); 
	            this.setWylieInput(!useTibetan);
	            keyboard.setEnabled(useTibetan);
	        }
	        else if (ch == keyboard)
	        {
                int ki = ch.getSelectedIndex();
                keybdMgr.elementAt(ki).activate(duffInput);
                ThdlOptions.setUserPreference("thdl.default.tibetan.keyboard", ki);
			}
	    }
	}



/*	public void printAllDefs()
	{

		Word word;
		int i;
		GridBagConstraints gb1 = new GridBagConstraints(), gb2 = new GridBagConstraints();
		JTextArea jtext;
		TextArea text;
		GridBagLayout grid = new GridBagLayout();

		if (panelOutput!=null)
			defPane.remove(panelOutput);

		panelOutput = new Panel(grid);
		gb1.weightx = 1;
		gb2.weightx = 4;
		gb1.gridwidth = GridBagConstraints.RELATIVE;
		gb2.gridwidth = GridBagConstraints.REMAINDER;

		for (i=0; i<array.length; i++)
		{
			word = (Word) array[i];
			jtext = new JTextArea(wd.getDuff(new TibetanString(word.getWylie())));
			jtext.setFont(tibetanFont);
			grid.setConstraints(jtext, gb1);
			panelOutput.add(jtext);

			text = new TextArea(word.getDef());
			text.
			grid.setConstraints(text, gb2);
			panelOutput.add(text);
		}

		defPane.add(panelOutput);
	}*/

    public void clear()
    {
        txtInput.setText("");
        duffInput.setText("");
        fullDef.setText("");
        model.newSearch(null);
        table.tableChanged(new TableModelEvent(model));
    	table.repaint();
    }

    public void translate()
    {
    	String in;
    	setDicts(scanner.getDictionarySource());

    	in = "";
    	if (showingTibetan)
    		in = duffInput.getWylie(new boolean[] { false });
    	else
            in = txtInput.getText();

    	if (!in.equals(""))
    	{
    		doingStatus("Translating...");
    		scanner.scanBody(in);
    		scanner.finishUp();
    		model.newSearch(scanner.getWordArray());
    		//printAllDefs();
    		scanner.clearTokens();
    		returnStatusToNorm();
    		fullDef.setText("");
    		/*ListSelectionModel lsm = (ListSelectionModel)table.getSelectionModel();
    		 if (!lsm.isSelectionEmpty())
    		 {
    		 	int selectedRow = lsm.getMinSelectionIndex();
    		 	//TableModel tm = table.getModel();
    		 	 if (selectedRow<model.getRowCount())
    		 	 	fullDef.setText(model.getValueAt(selectedRow, 1).toString());
    		 }*/
    	}
    	else
    	{
    		model.newSearch(null);
    		fullDef.setText("");
    	}
    	table.tableChanged(new TableModelEvent(model));
    	table.repaint();
    	if (table.getRowCount()>0) table.setRowSelectionInterval(0,0);
    }

	public void setWylieInput(boolean enabled)
	{
	    CardLayout cl = (CardLayout) inputPanel.getLayout();
	    if (!enabled && !showingTibetan)
	    {
			String s = txtInput.getText();
/*			int posEnter = s.indexOf('\n');
			if (posEnter > 0)
				s = s.substring(0,posEnter);*/
			duffInput.newDocument();
			if (!s.equals(""))
			    duffInput.toTibetanMachineWeb(s, 0);
			table.activateTibetan(true);
			cl.first(inputPanel);
			showingTibetan = true;
	    }
	    if (enabled && showingTibetan)
	    {
			txtInput.setText(duffInput.getWylie(new boolean[] { false }));
			table.activateTibetan(false);
			cl.last(inputPanel);
			showingTibetan = false;
	    }
		table.repaint();
	}
	
    public void setPreferences(Frame owner)
    {
        Font f;
        if (prefWindow==null) prefWindow = new PreferenceWindow(this, duffInput);
        prefWindow.show();        
        f = new Font(prefWindow.getRomanFont(), Font.PLAIN, prefWindow.getRomanFontSize());
        fullDef.setFont(f);
        txtInput.setFont(f);
        table.setTibetanFontSize(prefWindow.getTibetanFontSize());
        table.setRomanFont(f);
        table.repaint();
    }
    
    
}
