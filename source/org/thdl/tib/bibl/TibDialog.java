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
Library (THDL). Portions created by the THDL are Copyright 2001-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.bibl;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
* <p>
*  This class is a generic class for displaying a dialog in TiblEdit. I'm not sure if it is actually used in
*  TiblEdit. It may be a relic. CHECK!
* </p>
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class TibDialog extends JDialog implements ActionListener, TibConstants
{
	// Attributes
	TiblEdit controller;
	TibFrame frame;
	String[] specs;
	String response;
	int num;
	Hashtable results;
	JLabel[] jl;
	JTextField[] jtf;
	String type;
	boolean isStartUp = false;

	transient protected String                value;
    public static final String RESPONSE_PROPERTY = "response";
    public static final String NEW_ED = "new edition";

	private void init()
	{
		GridLayout gl = new GridLayout(0,2);
		gl.setHgap(5);
		gl.setVgap(5);
		JPanel cpane = new JPanel(gl);
		jl = new JLabel[num];
		jtf = new JTextField[num];
		for(int n=0; n<num; n++)
		{
			jl[n] = new JLabel(specs[n]);
			jtf[n] = new JTextField();
			cpane.add(jl[n]);
			cpane.add(jtf[n]);
		}
		JButton enter = new JButton(ENTER);
		JButton cancel = new JButton(CANCEL);
		enter.addActionListener(this);
		cancel.addActionListener(this);
		cpane.add(enter);
		cpane.add(cancel);
		setContentPane(cpane);
		pack();
		setLocation(250,250);
		type = "";
	}

	public void showDialog()
	{
		setVisible(true);
	}

	public String getResponse()
	{
		return response;
	}

    public void setResponse(String newValue) {
        String oldValue = value;
        value = newValue;
        firePropertyChange(RESPONSE_PROPERTY, oldValue, value);
    }

	public Type getType()
	{
		return (Type)results.get(TYPE);
	}

	public void setValue(int index, String value)
	{
		jtf[index].setText(value);
	}

	public String getValue(String key)
	{
		return (String)results.get(key);
	}

	public String[] getResults()
	{
		Vector newResults = new Vector();
		for(int n=0;n<specs.length;n++)
		{
			newResults.add(getValue(specs[n]));
		}
		String[] output = TiblEdit.toStringArray(newResults);
		return output;
	}

	public JTextField getTextField(int index)
	{
		if(index > -1 && index < jtf.length) {
			return jtf[index];
		}
		return null;
	}

	public void clearTextFields() {
		for(int n=0; n<jtf.length; n++) {
			jtf[n].setText("");
			jtf[n].repaint();
		}
	}

	public void clear()
	{
		System.out.println("Clearing tibdialog");
		results = new Hashtable();
		response = new String();
		clearTextFields();
		repaint();
	}

	public void reset()
	{
		specs = null;
		results = new Hashtable();
		response = new String();
	}

	public TibDialog(TibFrame frame, String title, String[] specs)
	{
		super(frame,title,true);
		this.frame = frame;
		this.specs = specs;
		num = specs.length;
		results = new Hashtable();
		results.put(TYPE,title);
		response =new String();
		value = "null";
		if(title.equals(EDITORS_INFO)) { isStartUp = true;} else {isStartUp = false;}
		init();
	}

	public TibDialog(TiblEdit tt, String title, String[] specs)
	{
		this(tt.getFrame(),title,specs);
		controller = tt;
		type = NEW_ED;
	}


	public void actionPerformed(ActionEvent ae)
	{
		String src_text = ((JButton)ae.getSource()).getText();
System.out.println("Action performed in TibDialog: " + src_text);
		if(src_text.equals(ENTER)) {
			for(int n=0; n<num; n++)
			{
				results.put(specs[n],jtf[n].getText());
			}
			clearTextFields();
			setResponse(ENTER);
			setVisible(false);
			if(type.equals(NEW_ED)) {
				controller.submitNewEditionInfo(this);
			} else if(getTitle().equals(NEW_ED_TITLE_INFO)) {
				frame.getController().insertNewEdTitle(this);
			} else if(getTitle().equals(NEW_ED_INFO)) {
				frame.getController().insertNewEdAndTitle(this);
			} else if(getTitle().equals(EDITORS_INFO)) {
System.out.println("It's editors info!");
				frame.getController().setEditor();
			}
		} else {
			setResponse(CANCEL);
			if(isStartUp) {System.exit(0);}
			setVisible(false);
		}

	}
}
