package org.thdl.tib.scanner;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

/** provides a friendly user interface when the command-line
    is not used for choosing a dictionary or connection to open.
*/
class PocketWhichDictionaryFrame extends WhichDictionaryFrame
{
    public PocketWhichDictionaryFrame(Frame owner)
    {
        super(owner);
        Panel p;
        CheckboxGroup cbg = new CheckboxGroup();
        
        this.setLayout(new GridLayout(5, 1));

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(d);

        this.add(new Label("Would you like open:"));
        
        p = new Panel(new BorderLayout());
        
        useOnline = new Checkbox("An on-line dict", false, cbg);
        
        useOnline.addItemListener(this);
        p.add(useOnline, BorderLayout.WEST);

        availDictsOnline.add("Public");
        
        //availDictsOnline.add("Private");        
        
        p.add(availDictsOnline, BorderLayout.EAST);
        this.add(p);
        
        p = new Panel(new BorderLayout());
        
        useOffline = new Checkbox("Local dict: ", true, cbg);
        
        useOffline.addItemListener(this);
        p.add(useOffline, BorderLayout.WEST);
        
        p.add(localDict, BorderLayout.CENTER);
        
        p.add(browse, BorderLayout.EAST);
        this.add(p);

        p = new Panel(new FlowLayout());
        p.add(ok);
        p.add(cancel);
        this.add(p);
        
        p = new Panel(new FlowLayout(FlowLayout.RIGHT));
        
        ckbDefault = new Checkbox("Set as default?", true);
        
        p.add(ckbDefault);
        this.add(p);
	}

    public void actionPerformed(ActionEvent e)	
    {
		Object obj = e.getSource();
		FileDialog fd;
		String fileName;
		int pos;
		
		if (obj == ok)
		{
		    if (!response.equals("")) this.setVisible(false);
		}
		else if (obj == cancel)
		{
		    response = "";
		    this.setVisible(false);
		}
		else if (obj == browse)
		{
    		fd = new FileDialog(owner, "Select dictionary to open", FileDialog.LOAD);
		    fd.setVisible(true);
		    fileName = fd.getFile();
		    if (fileName!= null)
		    {
		        // dropping the extension
    	        pos = fileName.lastIndexOf('.');
	            if (pos>0) fileName = fileName.substring(0, pos);
		        response = fd.getDirectory() + fileName;
		        localDict.setText(response);
		        ok.setEnabled(true);
		    }
		}
	}
	
    /*public void setFont(Font f)
    {
        super.setFont(f);        
        ok.setFont(f);
        cancel.setFont(f);
        useOnline.setFont(f);
        useOffline.setFont(f);
        availDictsOnline.setFont(f);
        localDict.setFont(f);
        browse.setFont(f);
        ckbDefault.setFont(f);
    }*/
	
	/** Implement the disabling of other guys here
	*/
	public void itemStateChanged(ItemEvent e)
	{
	    Object obj = e.getSource();
	    if (obj instanceof Checkbox)
	    {
	        Checkbox chx = (Checkbox) obj;
	        //System.out.println(obj);
	        if (chx == useOnline)
	        {
	            localDict.setEnabled(false);
	            browse.setEnabled(false);
	            availDictsOnline.setEnabled(true);
	            ok.setEnabled(true);
	            dictType = availDictsOnline.getSelectedIndex();
	            response = dictsOnline[dictType];
	        }
	        else if (chx == useOffline)
	        {
	            localDict.setEnabled(true);
	            browse.setEnabled(true);
	            if (availDictsOnline!=null)  availDictsOnline.setEnabled(false);
	            ok.setEnabled(!localDict.getText().equals(""));
	            response = localDict.getText();
	            dictType = dictTypes.length-1;
	        }
	    }
	    else if (obj instanceof Choice)
	    {
	        Choice ch = (Choice) obj;
	        dictType = ch.getSelectedIndex();
	        response = dictsOnline[dictType];
	    }
	}    
    
	/*class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == PocketWhichDictionaryFrame.this)
				PocketWhichDictionaryFrame_MouseEntered(event);
		}
	}

	void PocketWhichDictionaryFrame_MouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
	}*/
}