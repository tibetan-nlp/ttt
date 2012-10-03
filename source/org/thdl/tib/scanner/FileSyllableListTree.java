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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.thdl.util.SimplifiedLinkedList;

/** Searches the words directly in a file; not the preferred
	implementation. The search is too slow!
	The preferred implementation is the {@link CachedSyllableListTree}.
		
	<p>The words must be stored in a binary file tree structure format.
	This can be done using the {@link BinaryFileGenerator}.</p>

    @author Andr&eacute;s Montano Pellegrini
    @see TibetanScanner
    @see CachedSyllableListTree
    @see BinaryFileGenerator
*/

public class FileSyllableListTree implements SyllableListTree
{
	protected String sil;
	private long def[];
	protected long posLista;
	protected DictionarySource defSource;
	public static BitDictionarySource defSourcesWanted;
	public static RandomAccessFile wordRaf=null;
	private static RandomAccessFile defRaf=null;
	public static int versionNumber;
	
	/** Creates the root. */
	public FileSyllableListTree(String archivo) throws Exception
	{
		sil = null;
		def = null;
		defSource = null;
		
		this.openFiles(archivo);
		posLista = this.wordRaf.getFilePointer();
	}
	
	/** Used to create each node (except the root)
	*/
	protected FileSyllableListTree(String sil, long []def, DictionarySource defSource, long posLista)
	{
		this.sil=sil;
		this.def=def;
		this.defSource = defSource;
		this.posLista=posLista;
	}

	public String toString()
	{
		return sil;
	}

	public DictionarySource getDictionarySource()
	{
		return defSource;
	}
	
	public BitDictionarySource getDictionarySourcesWanted()
	{
	    return this.defSourcesWanted;
	}
	
	public static void openFiles(String archivo) throws Exception
	{
	    openFiles(archivo, true);
	}
	
	public static void closeFiles()
	{
	    try
	    {
	        wordRaf.close();
	        defRaf.close();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}
	
	/** Initiates all static variables, it is called by the constructor of the root
	    FileSyllableListTree (in the case of a pure file tree) or by the 
	    constructor of CachedSyllableListTree in the case of the root being loaded
	    into memory.
	*/
	public static void openFiles(String archivo, boolean backwardCompatible) throws Exception
	{
	    long fileSize;
	    int pos;
		
		wordRaf = new RandomAccessFile(archivo + ".wrd", "r");
		defRaf = new RandomAccessFile(archivo + ".def", "r");

		fileSize = wordRaf.length();
		wordRaf.seek(fileSize-4L);
		pos = wordRaf.readInt();
		
		if (pos >> 8 == -1)
		{
		    versionNumber = pos & 255;
		    
		    // for now, only version 2 & 3 should be expected
		    if (versionNumber != 3) versionNumber=2;
		    wordRaf.seek(fileSize-8L);
		    pos = wordRaf.readInt();
		}
		else
		{
		    // Updates the dictionary for backward compatibility.		    
		    try
		    {
		        if (backwardCompatible)
		        {
    		        wordRaf.close();
	    	        wordRaf = new RandomAccessFile(archivo + ".wrd", "rw");
    		        wordRaf.seek(fileSize);    	    
		            wordRaf.writeShort(-1);
		            wordRaf.writeByte(-1);
    		        
		            // Because it didn't have a version number, must be version 2.
		            versionNumber = 2;
		            wordRaf.writeByte(versionNumber);
		            wordRaf.close();
		            wordRaf = new RandomAccessFile(archivo + ".wrd", "r");
		        }
		        else
		        {
		            // something is wrong
		            ScannerLogger sl = new ScannerLogger();
		            sl.writeLog("1\tFileSyllableListTree\t" + "size: " + fileSize + "; bytes: " + Integer.toHexString(pos));
		            
		            // try to open again, but not corrupting the file
		            wordRaf = new RandomAccessFile(archivo + ".wrd", "r");

		            fileSize = wordRaf.length();
		            wordRaf.seek(fileSize-8L);
		            pos = wordRaf.readInt();
            		versionNumber = 3;
		        }
		    }
		    catch (Exception e)
		    {
		        // dictionary is stored on a non-writable media. Do nothing.
		    }
		}
		
		defSourcesWanted = BitDictionarySource.getAllDictionaries();
		
	    wordRaf.seek(pos);		
	}
	
	public static String[] getDictionaryDescriptions(String archivo)
	{
		int n;
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo + ".dic")));
			SimplifiedLinkedList ll1 = new SimplifiedLinkedList(), ll2 = new SimplifiedLinkedList();
			String s;
			while ((s=br.readLine())!=null)
			{
				n = s.indexOf(",");
				if (n < 0)
				{
					ll1.addLast(null);
					ll2.addLast(s);
				}
				else
				{
					ll1.addLast(s.substring(0,n).trim());
					ll2.addLast(s.substring(n+1).trim());
				}
			}
			DictionarySource.setTags(ll2.toStringArray());
			return ll1.toStringArray();
		}
		catch (Exception e)
		{
			return null;
		}
	}	

	public String getDef()
	{
		return getDefs().toString();
	}

	public Definitions getDefs()
	{
            if (def==null) return null;
            DictionarySource defSourceAvail = defSource.intersection(defSourcesWanted);
            String defs[];
            int i, n=0;

            if (versionNumber==2)
            {
                int defsAvail[] = ((BitDictionarySource) defSourceAvail).untangleDefs(), defsFound[] = ((BitDictionarySource) defSource).untangleDefs(def.length);

                defs = new String[defsAvail.length];
                try
                {
                    for (i=0; i<defsAvail.length; i++)
                    {
                        while(defsAvail[i]!=defsFound[n]) n++;
                        defRaf.seek(def[n]);
                        defs[i] = defRaf.readUTF();
                    }
                }
                catch (Exception e)
                {
                        System.out.println(e);
                        e.printStackTrace();
                        return null;
                }
            }
            else
            {
                ByteDictionarySource defSourceAvailBy = (ByteDictionarySource) defSourceAvail;
                defs = new String [defSourceAvailBy.countDefs()];

                try
                {
                        for (i=0; i < def.length; i++)
                        {
                                if (!defSourceAvailBy.isEmpty(i))
                                {
                                    defRaf.seek(def[i]);
                                    defs[n] = defRaf.readUTF();
                                    n++;
                                }
                            }
                }
                catch (Exception e)
                {
                        System.out.println(e);
                        e.printStackTrace();
                        return null;
                }
            }
            return new Definitions(defs, defSourceAvail);
	}

	public boolean hasDef()
	{
		if (def==null) return false;
		DictionarySource defSourceAvail = defSource.intersection(defSourcesWanted);
		return !defSourceAvail.isEmpty();
	}

	public SyllableListTree lookUp(String silStr)
	{
		String sil;
		long pos, defSource[];
		DictionarySource sourceDef;
		
		int i;

		if (silStr==null || posLista==-1) return null;
		try
		{
			wordRaf.seek(posLista);
			do
			{
				pos = (long) wordRaf.readInt();
				sil = wordRaf.readUTF();
				
				if (versionNumber==2) sourceDef = new BitDictionarySource();
				else sourceDef = new ByteDictionarySource();
				sourceDef.read(wordRaf);
				
				if (sourceDef.isEmpty()) defSource = null;
				else
				{
					defSource = new long[sourceDef.countDefs()];
					for (i=0; i<defSource.length; i++)
					{
						defSource[i] = (long) wordRaf.readInt();
					}
				}

				if (sil.compareTo(silStr)>0)
					return null;
				if (sil.equals(silStr))
					return new FileSyllableListTree(sil, defSource, sourceDef, pos);

			}while(sourceDef.hasBrothers());
		}
		catch (Exception e)
		{
		}
		return null;
	}	
}
