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

/** Stores the multiple definitions (corresponding to
	various dictionaries) for a single Tibetan word.
	
    @see Word
    @author Andr&eacute;s Montano Pellegrini
*/

public class Definitions
{
	public String[] def;
	private DictionarySource source;
		
	public Definitions(String[] def, DictionarySource source)
	{
		this.def = def;
		this.source = source;
	}
	
	public Definitions(String def)
	{
		source = null;
		this.def = new String[1];
		this.def[0] = def;
	}
	
	public String getPreview()
	{
	    String s;
	    int i;
	    
	    if (def==null) return "";
	    
	    s = def[0];
	    
	    for (i=1; i<def.length; i++)
	        s = s + ". " + def[i];
	        
	    return s;
	}
		
	public String toString()
	{
		int i,j;
		String s;
		if (def==null) return null;
		if (source==null) return def[0];
		
		if (FileSyllableListTree.versionNumber==2)
		{
		    s = "(" + source.getTag(0) + ") " + def[0];
		    for (i=1; i<def.length; i++)
			    s += "\n" + "(" + source.getTag(i) + ") " + def[i];
		}
		else
		{
		    ByteDictionarySource sourceb = (ByteDictionarySource) source;
		    j=0;
		    while (sourceb.isEmpty(j)) j++;
		    s = "(" + sourceb.getTag(j) + ") " + def[0];
		    for (i=1; i<def.length; i++)
		    {
		        j++;
		        while (sourceb.isEmpty(j)) j++;
		        s += "\n" + "(" + sourceb.getTag(j) + ") " + def[i];
		    }
		}

		return s;
	}
	
	public DictionarySource getDictionarySource()
	{
	    return source;
	}
}