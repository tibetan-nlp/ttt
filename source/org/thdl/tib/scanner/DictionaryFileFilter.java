package org.thdl.tib.scanner;

import java.io.File;

class DictionaryFileFilter extends javax.swing.filechooser.FileFilter
{
    public DictionaryFileFilter()
    {
    }
    
    public boolean accept(File file)
    {
		if (file.isDirectory())
		{
			return true;
		}

		String fName = file.getName();
		int i = fName.lastIndexOf('.');

		if (i < 0)
			return false;
		else 
		{
			String ext = fName.substring(i+1).toLowerCase();
			if (ext.equals("wrd"))
				return true;
			else
				return false;
		}
        
    }
    public String getDescription() 
    {
        return "Binary tree dictionary (.wrd)";
    }
}