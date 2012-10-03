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

/** Specifies a generic interface to access and process a subset of 
    dictionaries among a set of dictionaries.

    @author Andr&eacute;s Montano Pellegrini
*/
public abstract class DictionarySource
{
	public static String[] defTags;
	static
	{
		defTags=null;
	}
	
	public static void setTags(String tags[])
	{
		defTags = tags;
	}
	
	/** Get the tag or tags associated to definition number i. */
	public abstract String getTag(int i);
    
    /** Marks all dictionaries as unselected */
    public abstract void reset();
            
    /** Returns an instance of DictionarySource marking as selected all dictionaries
        that were selected in both the current and dsO. */
    public abstract DictionarySource intersection(DictionarySource dsO);
    
    /** Returns true if no dictionaries are selected. */
    public abstract boolean isEmpty();

    /** Writes the dictionary information to a random access file. */
    public abstract void print(boolean hasNext, DataOutput raf) throws IOException;
    
    /** Reads the dictionary information from a random access file, according
        to the way it was written with @print. */
    public abstract void read(DataInput raf) throws IOException;
    
    /** Returns the number of definitions available. */
    public abstract int countDefs();
    
    /** Returns true if the node has brothers. This is used by @FileSyllableListTree. */
    public abstract boolean hasBrothers();
    
    /** Returns true if dict is a selected dictionary. */
    public abstract boolean contains(int dict);
    
    /** Returns an array of bits representing the selected dictionaries. */
    public abstract int getDicts();    
}