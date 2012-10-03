package org.thdl.tib.scanner;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
// import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
/* import java.awt.Cursor;
import java.awt.FileDialog;
import java.io.FilenameFilter;
import java.io.File; */

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/** provides a friendly user interface when the command-line
    is not used for choosing a dictionary or connection to open.
*/
class SwingWhichDictionaryFrame extends WhichDictionaryFrame
{
    private CreateDatabaseWizard cdw;
    // private DictionaryFileFilter dicFilter;
    private Checkbox createNewDictDB;
    
    public SwingWhichDictionaryFrame(Frame owner)
    {
        super(owner);
        Panel p;
        CheckboxGroup cbg = new CheckboxGroup();

        // dicFilter = null;
	    // this.setBackground(Color.white);
        this.setLayout(new GridLayout(6, 1));
        
        this.setSize(400, 200);
        this.add(new Label("Would you like to:"));
        
        p = new Panel(new BorderLayout());
        
        useOnline = new Checkbox("Access an on-line dictionary", false, cbg);
        useOnline.addItemListener(this);
        p.add(useOnline, BorderLayout.WEST);

        availDictsOnline.add("Public version");
        
        if (TibetanScanner.mode == TibetanScanner.DEBUG_MODE)
        	availDictsOnline.add("Local version (development)");
        
        p.add(availDictsOnline, BorderLayout.EAST);
        this.add(p);
        
        p = new Panel(new BorderLayout());
        
        useOffline = new Checkbox("Access a local dictionary: ", true, cbg);
        useOffline.addItemListener(this);
        p.add(useOffline, BorderLayout.WEST);
        
        p.add(localDict, BorderLayout.CENTER);
        
        p.add(browse, BorderLayout.EAST);
        this.add(p);
        
        createNewDictDB = new Checkbox("Create a new dictionary...", false, cbg);
        createNewDictDB.addItemListener(this);
        this.add(createNewDictDB);
        
        p = new Panel(new FlowLayout());

        p.add(ok);
        p.add(cancel);
        this.add(p);
        
        p = new Panel(new FlowLayout(FlowLayout.RIGHT));
        ckbDefault = new Checkbox("Use these settings as default", true);
        p.add(ckbDefault);
        this.add(p);
    }

    public void actionPerformed(ActionEvent e)	
    {
		Object obj = e.getSource();
		
		if (obj == ok)
		{
		    if (response.equals(""))
		    {
		        if (cdw==null) cdw = new CreateDatabaseWizard(owner);
		        cdw.setVisible(true);
		        response = cdw.getResponse();
		    }
		    if (!response.equals("")) this.setVisible(false);
		}
		else if (obj == cancel)
		{
		    response = "";
		    this.setVisible(false);
		}
		else if (obj == browse)
		{
			/* FileDialog fileDialog = new FileDialog(this, "Select dictionary to open", FileDialog.LOAD);
			fileDialog.setFilenameFilter(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".wrd"); 
				}
			});
			fileDialog.setFile("*.wrd");
            fileDialog.setVisible(true);
            response = fileDialog.getFile();
            if (response != null)
            {
		    if (dicFilter == null) dicFilter = new DictionaryFileFilter(); */
		    JFileChooser fileChooser = new JFileChooser("Select dictionary to open");
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary tree dictionary (.wrd)", "wrd");
		    fileChooser.setFileFilter(filter);
		    /* fileChooser.addChoosableFileFilter(dicFilter);
		    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            } */
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                response = fileChooser.getSelectedFile().getPath();
                response = response.substring(0, response.length()-4);
                // response = fileDialog.getDirectory() + System.getProperty("file.separator") + response.substring(0, response.length()-4);
                localDict.setText(response);
		        ok.setEnabled(true);
            }
		}
	}
	
	/** Implement the disabling of other guys here
	*/
	public void itemStateChanged(ItemEvent e)
	{
	    Object obj = e.getSource();
	    int dictNumber;
	    
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
	            dictNumber = availDictsOnline.getSelectedIndex();
	            dictType = dictNumber + 1;
	            response = dictsOnline[dictNumber];
	        }
	        else if (chx == useOffline)
	        {
	            localDict.setEnabled(true);
	            browse.setEnabled(true);
	            if (availDictsOnline!=null)  availDictsOnline.setEnabled(false);
	            ok.setEnabled(!localDict.getText().equals(""));
	            response = localDict.getText();
	            dictType = 0;
	        }
	        else if (chx == createNewDictDB)
	        {
	            localDict.setEnabled(false);
	            browse.setEnabled(false);
	            if (availDictsOnline!=null) availDictsOnline.setEnabled(false);
	            ok.setEnabled(true);
	            response="";
	            dictType = 0;
	        }
	    }
	    else if (obj instanceof Choice)
	    {
	        Choice ch = (Choice) obj;
	        dictNumber = ch.getSelectedIndex();
	        dictType = dictNumber + 1;
	        response = dictsOnline[dictNumber];
	    }
	}

}