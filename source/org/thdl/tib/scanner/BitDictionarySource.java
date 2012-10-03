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

    @author Andr&eacute;s Montano Pellegrini
*/
public class BitDictionarySource extends DictionarySource
{
	private int dicts;

	/** Last bit of word; 1 if there are more brothers.*/
	private static final int lastBit=1073741824;
	private static final int allDicts=lastBit-1;

	public BitDictionarySource()
	{
		dicts = 0;
	}
	
	public BitDictionarySource(int dicts)
	{
	    this.dicts = dicts;
	}
	
	public boolean equals(Object obj)
	{
	    BitDictionarySource objB = (BitDictionarySource) obj;
	    return (this.getDicts()==objB.getDicts());
	}
	
    /** Returns an instance of DictionarySource with all dictionaries selected */
	public static BitDictionarySource getAllDictionaries()
	{
		BitDictionarySource ds = new BitDictionarySource();
		ds.setDicts(allDicts);
		return ds;
	}

    /** Marks all dictionaries as selected */
	public void setAllDictionaries()
	{
		dicts = allDicts;
	}

    /** Assumes dicts is an array of bits, and selects the dictionaries marked by
    each bit. */
	public void setDicts(int dicts)
	{
		this.dicts = dicts;
	}

    /** Returns an array of bits representing the selected dictionaries. */
	public int getDicts()
	{
		return dicts;
	}

	private int getBits(int n)
	{
		return 1 << n;
	}

	public boolean contains(int dict)
	{
		return (dicts & getBits(dict))>0;
	}
	
	public boolean contains(BitDictionarySource dicts)
	{
	    return this.intersection(dicts).equals(dicts);
	}

	/** Marks the dictionary "dict" as selected */
    public void add(int dict)
	{
		dicts|= getBits(dict);
	}
	
	public void add(BitDictionarySource dicts)
	{
	    this.dicts|= dicts.dicts;
	}

	/** Write to file using BinaryFileGenerator	*/
	public void print(boolean hasNext, DataOutput raf) throws IOException
	{
		int numDict;
		if (hasNext) numDict = lastBit | dicts;
		else numDict = dicts;
		raf.writeInt(numDict);
	}

	public void read(DataInput raf) throws IOException
	{
		setDicts(raf.readInt());
	}

	public boolean hasBrothers()
	{
		return (dicts & lastBit)>0;
	}

	public int countDefs()
	{
		int n, source;
		for (n=0, source = dicts & allDicts; source>0; source>>=1)
			if (source%2==1) n++;
		return n;
	}

	public DictionarySource intersection(DictionarySource dsO)
	{
		BitDictionarySource ds = new BitDictionarySource(), dsOB = (BitDictionarySource) dsO;
		ds.setDicts(this.dicts & dsOB.dicts);
		return ds;
	}
	
    /** Returns an array containing the indexes for the available dictionaries. Use this
    method when you know exactly how many dictionaries there are! */
	public int[] untangleDefs(int n)
	{
		int arr[], i, pos, source;
		arr = new int[n];
		for (i=0, pos=0, source=dicts & allDicts; pos<n; i++, source>>=1)
			if (source%2==1)
				arr[pos++]=i;
		return arr;
	}

    /** Returns an array containing the indexes for the available dictionaries.*/        
	public int[] untangleDefs()
	{
		return untangleDefs(countDefs());
	}

	public boolean isEmpty()
	{
		return (dicts & allDicts)<=0;
	}

	public void reset()
	{
		dicts = 0;
	}
	public String getTag(int i)
	{
	    int source[] = this.untangleDefs();
		if (defTags==null) return Integer.toString(source[i]+1);
		return defTags[source[i]];
	}	
}