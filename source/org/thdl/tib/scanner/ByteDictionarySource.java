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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/** Specifies a subset of dictionaries among a set of
	dictionaries. Supports a maximum of 30 dictionaries.
	Unlike @BitDictionarySource, it provides the infrastructure
	to group definitions from various dictionaries.

    @author Andr&eacute;s Montano Pellegrini
*/
public class ByteDictionarySource extends DictionarySource
{
	//private BitDictionarySource dicts[];
	private BitDictionarySource dicts[];
	private boolean hasBrother;

	/** Last bit of word; 1 if there are more brothers.*/
	private static final int lastBit = 64;
	private static final int allDicts=lastBit-1;

	public ByteDictionarySource()
	{
		dicts = null;
		hasBrother = false;
	}
	
	public ByteDictionarySource(BitDictionarySource dicts[], boolean hasBrother)
	{
		this.dicts = dicts;
		this.hasBrother = hasBrother;
	}
	
	public void insertDef(BitDictionarySource newDef, int n)
	{
	    int i;
	    BitDictionarySource newDicts[] = new BitDictionarySource[dicts.length+1];

	    for (i=0; i<n; i++)
	        newDicts[i] = dicts[i];
	    
	    newDicts[n] = newDef;
	    
	    for (i=n+1; i<newDicts.length; i++)
	        newDicts[i] = dicts[i-1];
	    
	    dicts = newDicts;
	}
	
	public void deleteDef(int n)
	{
	    int i;
	    BitDictionarySource newDicts[] = new BitDictionarySource[dicts.length-1];

	    for (i=0; i<n; i++)
	        newDicts[i] = dicts[i];

	    for (i=n+1; i<dicts.length; i++)
	        newDicts[i-1] = dicts[i];
	    
	    dicts = newDicts;
	}
	
	public void addNewDef(int dictNum)
	{
	    if (dicts==null)
	    {
	        dicts = new BitDictionarySource[1];
	    }
	    else
	    {
	        BitDictionarySource newDicts[] = new BitDictionarySource[dicts.length+1];
	        int i;
	        
	        for (i=0; i<dicts.length; i++)
	            newDicts[i] = dicts[i];
	        
	        dicts = newDicts;
	    }
        dicts[dicts.length-1] = new BitDictionarySource();
        dicts[dicts.length-1].add(dictNum);
	}
	
	public void addDictToDef(int dict, int def)
	{
	    dicts[def].add(dict);
	}
	
	public void addDictToDef(BitDictionarySource dicts, int def)
	{
	    this.dicts[def].add(dicts);
	}
	
	public BitDictionarySource getDef(int i)
	{
	    return dicts[i];
	}
	
	public int getDicts()
	{
	    int i;
	    BitDictionarySource availableDicts = new BitDictionarySource();
	    
	    if (dicts == null) return 0;
	    
	    for (i=0; i< dicts.length; i++)
	        availableDicts.add(dicts[i]);
	    
	    return availableDicts.getDicts();
	}
	
	public void dubDef(int n)
	{
	    BitDictionarySource newDicts[] = new BitDictionarySource[dicts.length+1];
	    int i;
	        
	    for (i=0; i<=n; i++)
	        newDicts[i] = dicts[i];
	        
	    newDicts[n+1] = new BitDictionarySource(newDicts[n].getDicts());
	        
	    for (i=n+2; i<newDicts.length; i++)
	        newDicts[i] = dicts[i-1];
	        
	    dicts = newDicts;
	}
	
	public boolean isDictInDef (int dict, int def)
	{
	    return dicts[def].contains(dict);
	}
		
	public boolean isDictInDefAlone (int dict, int def)
	{
	    return dicts[def].contains(dict) && dicts[def].countDefs()==1;
	}
	
	public int containsAlone(int dict)
	{
	    int i;
	    if (dicts == null) return -1;
	    for (i=0; i<dicts.length; i++)
	    {
	        if (isDictInDefAlone(dict,i))
	            return i;
	    }
	    return -1;
	}
	
	/** Write to file using BinaryFileGenerator	*/
	public void print(boolean hasNext, DataOutput raf) throws IOException
	{
		int i, j, eachDict[], n;
		
		/*  first write how many definitions are, using the first bit to mark
		    the brothers of the node. */
		if (dicts==null)
		{
		    if (hasNext) raf.writeByte(lastBit);
		    else raf.writeByte(0);
		    return;
		}
		if (hasNext) n = lastBit | dicts.length;
		else n = dicts.length;
		raf.writeByte(n);
		
		/*  Then write the dictionaries associated with each definition, using the
		    first bit to mark for more dicts. */
		for (i=0; i<dicts.length; i++)
		{
		    eachDict = dicts[i].untangleDefs();
		    n = eachDict.length-1;
		    for (j=0; j<n; j++)
		        raf.writeByte(lastBit | eachDict[j]);
		    raf.writeByte(eachDict[n]);
		}
	}

	public void read(DataInput raf) throws IOException
	{
	    int i, n;
	    
	    n = (int) raf.readByte();
	    if ((n & lastBit)>0)
	    {
	        hasBrother = true;
	        n = n & allDicts;
	    }
	    else hasBrother = false;
	    
	    if (n==0)
	    {
	        dicts = null;
	        return;
	    }
	    
	    dicts = new BitDictionarySource[n];
	    
	    for (i=0; i< dicts.length; i++)
	    {
	        dicts[i] = new BitDictionarySource();
	        do
	        {
	            n = (int) raf.readByte();
	            dicts[i].add(n & allDicts);
	        } while((n & lastBit)>0);
	    }
	}

	public boolean hasBrothers()
	{
		return this.hasBrother;
	}

	public boolean contains(int dict)
	{
		int i;
		
		if (dicts==null) return false;
		
		for (i=0; i<dicts.length; i++)
		    if (dicts[i].contains(i)) return true;
		    
		return false;
	}
	
	public int containsDict(BitDictionarySource dict)
	{
	    int i;
	    if (dicts==null) return -1;
	    for (i=0; i<dicts.length; i++)
	        if (dicts[i].equals(dict)) return i;
	    return -1;
	}

	public int countDefs()
	{
	    int i, n;
	    
	    if (dicts==null) return 0;
	    
	    n=0;
	    for (i=0; i<dicts.length; i++)
	        if (!dicts[i].isEmpty()) n++;

		return n;
	}

	public void reset()
	{
		dicts = null;
	}
	
	public boolean isEmpty(int def)
	{
	    return dicts[def].isEmpty();
	}

	public boolean isEmpty()
	{	
	    int i;
	    
	    if (dicts == null) return true;
	    
	    for (i=0; i< dicts.length; i++)
	        if (!dicts[i].isEmpty()) return false;
	    
	    return true;
	}
	
	public DictionarySource intersection(DictionarySource dsO)
	{
	    BitDictionarySource newDicts[], dsOB;
	    int i;
	    
	    newDicts = new BitDictionarySource[dicts.length];
	    dsOB = (BitDictionarySource) dsO;
	    
	    for (i=0; i<dicts.length; i++)
	        newDicts[i] = (BitDictionarySource) dicts[i].intersection(dsOB);
	    
		return new ByteDictionarySource(newDicts, hasBrother);
	}

	public String getTag(int n)
	{
	    int i, source[] = dicts[n].untangleDefs();
	    String tag;
	    
	    if (defTags==null) tag = Integer.toString(source[0]+1);
	    else
	    {
	    	if (source[0]<0 || source[0]>=defTags.length) return null;
	    	tag = defTags[source[0]];	    
	    }
	    
	    for (i=1; i<source.length; i++)
	    {
	        tag += ", ";
	        
    	    if (defTags==null) tag += Integer.toString(source[i]+1);
	        else
	        {
	        	if (source[i]<0 || source[i]>=defTags.length) return null;
	        	tag += defTags[source[i]];
	        }
	    }

		return tag;
	}
}