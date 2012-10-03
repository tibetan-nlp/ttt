package org.thdl.tib.scanner;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/** provides a friendly user interface when the command-line
    is not used for choosing a dictionary or connection to open.
*/
abstract class WhichDictionaryFrame extends Dialog implements ActionListener, ItemListener
{
    protected String response;
    protected int dictType;
    protected Button ok, cancel;
    protected Checkbox useOnline, useOffline;
    protected Choice availDictsOnline;
    protected Label localDict;
    protected Button browse;
    protected Frame owner;
    protected String dictsOnline[], dictTypes[];
    protected Checkbox ckbDefault;
    
    public WhichDictionaryFrame(Frame owner)
    {
        super(owner, "Welcome to the Tibetan to English Translation Tool", true);
        
        this.owner = owner;
        response="";
        
        // FIXME: values should not be hardwired
        if (TibetanScanner.mode == TibetanScanner.DEBUG_MODE)
        {
            dictsOnline = new String[2];
        	dictsOnline[1] = "http://localhost:8080/tibetan/org.thdl.tib.scanner.RemoteScannerFilter";
        	dictTypes = new String[3];
        	dictTypes[2] = "development";
        }
        else
        {
        	dictsOnline = new String[1];
            dictTypes = new String[2];        	
        }
        
        dictsOnline[0] = "http://ttt.thlib.org/org.thdl.tib.scanner.RemoteScannerFilter";
        
        dictTypes[0] = "local";
        dictTypes[1] = "public";
        
        availDictsOnline = new Choice();
        //availDictsOnline.add("Local (only on my computer!)");
        
        availDictsOnline.addItemListener(this);
        availDictsOnline.setEnabled(false);
        
        localDict = new Label();
        localDict.setEnabled(true);
        
        dictType = 0;

        browse = new Button("Browse...");
        browse.setEnabled(true);
        browse.addActionListener(this);

        ok = new Button("Ok");
        ok.setEnabled(false);
        ok.addActionListener(this);

        cancel = new Button("Cancel");
        cancel.addActionListener(this);
        
        useOnline=null;
        useOffline=null;
        ckbDefault=null;   
    }

    public abstract void actionPerformed(ActionEvent e);
	
	/** Implement the disabling of other guys here
	*/
	public abstract void itemStateChanged(ItemEvent e);
	
    public String getResponse()
    {
        return response;
    }
    
    public boolean getDefaultOption()
    {
        return ckbDefault.getState();
    }
    
    public String getDictionaryType()
    {
        return dictTypes[dictType];
    }
}