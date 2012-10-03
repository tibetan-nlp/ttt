package org.thdl.tib.scanner;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JFileChooser;

import org.thdl.util.SimplifiedLinkedList;

class CreateDatabaseWizard extends Dialog implements ActionListener, ItemListener
{
    private Label dictNum;
    private int numDict, delimiterType;
    private Label fileName;
    private TextField dicDesc, dicAbb, delimiter;
    private Choice dicType;
    private List listDicts;
    private Button add, ok, cancel, browse;
    private Frame owner;
    private SimplifiedLinkedList dicts;
    private String response;
    
    CreateDatabaseWizard(Frame owner)
    {
        super(owner, "Generation of Binary Tree Dictionary", true);
        this.owner = owner;
        response = "";
        dicts = new SimplifiedLinkedList();
        
        this.setLayout(new BorderLayout());
        this.setSize(400,300);
        
        Panel p1, p2, p3;
        p1 = new Panel(new GridLayout(4, 1));
        p2 = new Panel(new FlowLayout(FlowLayout.LEFT));
        p2.add(new Label("Dictionary "));
        numDict = 1;
        dictNum = new Label(Integer.toString(numDict));
        p2.add(dictNum);
        p2.add(new Label(":"));
        p1.add(p2);
        p2 = new Panel(new BorderLayout());
        p2.add(new Label("File name:"), BorderLayout.WEST);
        fileName = new Label();
        p2.add(fileName, BorderLayout.CENTER);
        browse = new Button("Browse...");
        browse.addActionListener(this);
        p2.add(browse, BorderLayout.EAST);
        p1.add(p2);
        p2 = new Panel(new BorderLayout());
        p2.add(new Label("Description:"), BorderLayout.WEST);
        dicDesc = new TextField(20);
        p2.add(dicDesc, BorderLayout.CENTER);
        p3 = new Panel(new FlowLayout());
        p3.add(new Label("Abbreviation:"));
        dicAbb = new TextField(6);
        p3.add(dicAbb);
        p2.add(p3, BorderLayout.EAST);
        p1.add(p2);
        p2 = new Panel(new BorderLayout());
        p2.add(new Label("Type:"), BorderLayout.WEST);
        dicType = new Choice();
        dicType.add("Dash separated");
        dicType.add("Tab separated");
        dicType.add("Transcribed ACIP");
        dicType.add("Other:");
        dicType.addItemListener(this);
        p2.add(dicType, BorderLayout.CENTER);
        p3 = new Panel(new FlowLayout());
        delimiter = new TextField(" - ");
        delimiter.setEnabled(false);
        p3.add(delimiter);
        add = new Button("Add");
        add.setEnabled(false);
        add.addActionListener(this);
        p3.add(add);
        p2.add(p3, BorderLayout.EAST);
        p1.add(p2);
        this.add(p1, BorderLayout.NORTH);
        listDicts = new List();
        this.add(listDicts, BorderLayout.CENTER);
        
        p1 = new Panel(new FlowLayout());
        ok = new Button("Generate database");
        ok.addActionListener(this);
        ok.setEnabled(false);
        p1.add(ok);
        cancel = new Button("Cancel");
        cancel.addActionListener(this);
        p1.add(cancel);
        this.add(p1, BorderLayout.SOUTH);
    }
    
    public void actionPerformed(ActionEvent event)
    {
        FileDialog fd;
        DictionaryRecord dr;
		Button obj = (Button) event.getSource();
	    String fileName, path="";
	    int pos;
		
		if (obj == cancel)
		{
		    this.setVisible(false);
		}
		if (obj == browse)
		{
		    fd = new FileDialog(owner, "Select dictionary to open", FileDialog.LOAD);
	        fd.show();
	        fileName = fd.getFile();
	        if (fileName != null)
	        {
	            path = fd.getDirectory() + fileName;
	            this.fileName.setText(path);
	            // strip extension
	            pos = fileName.lastIndexOf('.');
	            if (pos>0) fileName = fileName.substring(0, pos);
	            this.dicDesc.setText(fileName);
	            if (fileName.length()>4) this.dicAbb.setText(fileName.substring(0,4));
	            else this.dicAbb.setText(fileName);
	            add.setEnabled(true);
	        }
		}
		else if (obj == add)
		{
            dr = new DictionaryRecord(dicDesc.getText()+ "," + dicAbb.getText(), this.fileName.getText(), delimiterType, delimiter.getText());
		    listDicts.add(dr.header + " - " + dicType.getSelectedItem() + "; " + dr.fileName);
		    ok.setEnabled(true);
            numDict++;
            dictNum.setText(Integer.toString(numDict));            
            dicts.addLast(dr);
            
            dicDesc.setText("");
            dicAbb.setText("");
            this.fileName.setText("");
         
            add.setEnabled(false);
		}
		else if (obj == ok)
		{
		    BinaryFileGenerator bfg = new BinaryFileGenerator();
		    AcipToWylie a2w=null;

		    // getting the database name
		    DictionaryFileFilter dicFilter = new DictionaryFileFilter();
		    JFileChooser fileChooser = new JFileChooser("Save database to");
		    fileChooser.addChoosableFileFilter(dicFilter);
		    /*setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }*/
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                path = fileChooser.getSelectedFile().getPath();
    	        pos = path.lastIndexOf('.');
	            if (pos>0) path = path.substring(0, pos);
            }
		    
		    PrintWriter pr=null;
		    Object dictsArray[] = dicts.toArray();
		    try
		    {
                pr = new PrintWriter(new FileOutputStream(path + ".dic"));
            }
            catch (Exception e)
            {
            }

		    for (pos=0; pos<dictsArray.length; pos++)
		    {
		        dr = (DictionaryRecord) dictsArray[pos];
		        if (dr.delimiterType==BinaryFileGenerator.delimiterAcip)
		        {
		            try
		            {
		                a2w = new AcipToWylie(new BufferedReader(new InputStreamReader(new FileInputStream(dr.fileName))), new PrintWriter(new FileOutputStream(dr.fileName + ".temp")));
    		            a2w.run();
		            }
		            catch (Exception e)
		            {
		            }
		            
		            dr.fileName = dr.fileName + ".temp";
		        }
		        
		        try
		        {
		            bfg.addFile(dr.fileName, dr.delimiterType, dr.delimiter, pos);
		        }
		        catch(Exception e)
		        {
		        }
		        
		        pr.println(dr.header);
		    }
		    pr.flush();
		    
		    try
		    {
                bfg.generateDatabase(path);
            }
            catch (Exception e)
            {
            }
            response = path;
            this.setVisible(false);
		}
	}
	
	public void itemStateChanged(ItemEvent e)
	{
	    Choice ch = (Choice) e.getSource();
	    int sel = ch.getSelectedIndex();
	    
	    switch (sel)
	    {
	        case 0: delimiterType = BinaryFileGenerator.delimiterDash;
	        break;
	        case 1: delimiterType = BinaryFileGenerator.delimiterGeneric;
	        delimiter.setText("\t");
	        break;
	        case 2: delimiterType = BinaryFileGenerator.delimiterAcip;
	        break;
	        case 3: delimiterType = BinaryFileGenerator.delimiterGeneric;
	        delimiter.setEnabled(true);
	    }
	}
	
	public String getResponse()
	{
	    return response;
	}
}