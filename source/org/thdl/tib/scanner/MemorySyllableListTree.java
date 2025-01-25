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
import java.io.PrintStream;

import org.thdl.util.SimplifiedLinkedList;
import org.thdl.util.SimplifiedListIterator;

/** Loads the whole dictionary into memory; not the preferred
	implementation. Provides the fastest search, but takes up to
	much resources.	The preferred implementation is the
	{@link CachedSyllableListTree}.
		
	<p>The words must be stored in a binary file tree structure format.
	This can be done using the {@link BinaryFileGenerator}.</p>

    @author Andr&eacute;s Montano Pellegrini
    @see TibetanScanner
    @see CachedSyllableListTree
    @see BinaryFileGenerator
*/

public class MemorySyllableListTree extends SimplifiedLinkedList implements SyllableListTree
{
	protected String sil, def;
	
	public String toString()
	{
		return sil;
	}
	
	/** Null because it does not support multiple dictionaries.
	*/
	public DictionarySource getDictionarySource()
	{
		return null;
	}

	/** Null because it does not support multiple dictionaries.
	*/
	public BitDictionarySource getDictionarySourcesWanted()
	{
	    return null;
	}


	
	public MemorySyllableListTree(String sil, String def)
	{
		super();
		int marker = sil.indexOf(" ");
		
		if (marker<0)
		{
			this.sil = sil;
			this.def = def;
		}
		else
		{
			this.sil = sil.substring(0, marker);
			this.def = null;
			addLast(new MemorySyllableListTree(sil.substring(marker+1).trim(), def));
		}		
	}	
	
	public MemorySyllableListTree(String archivo) throws Exception
	{
		super();
		sil = null;
		def = null;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo)));
		String entrada;
	
		while ((entrada = br.readLine())!=null)
		{
			entrada = entrada.trim();
			if (!entrada.equals(""))
				add(entrada);
		}
	}
	
	private void add(String entrada)
	{
		int marker = entrada.indexOf("-");
		if (marker<0)
			throw new RuntimeException("Error de sintaxis; diccionario no pudo ser cargado!");
		
		add(entrada.substring(0,marker).trim(), entrada.substring(marker+1).trim());
	}	
	
	private void add(String word, String def)
	{
		MemorySyllableListTree prefijo;
		String firstSillable;
		int marker = word.indexOf(" ");
		
		if (marker<0)
			firstSillable = word;
		else firstSillable = word.substring(0,marker);
		
/*
		Para orden "normal" con sanscrito transliterado traslapado
		entre el tibetano
		if (isEmpty() || (prefijo = lookUp(firstSillable))==null) */
		
//		Para orden modificado de silabas
		if (isEmpty() || !(prefijo = (MemorySyllableListTree)getLast()).equals(firstSillable))
			super.addLast(new MemorySyllableListTree(word, def));
		else
			if (marker<0) // ya estaba en el diccionario!
				prefijo.addMoreDef(def);
			else
				prefijo.add(word.substring(marker+1).trim(), def);
	}
	
	private void addMoreDef(String def)
	{
		if (this.def==null || this.def.equals(""))
			this.def=def;
		else this.def = this.def + ". " + def;
	}
	
	public boolean equals (Object o)
	{
		if (o instanceof String)
		{
			return sil.equals((String)o);
		}
		else return false;
	}
	
	public String getDef()
	{
		return def;
	}
	
	public Definitions getDefs()
	{
		return new Definitions(def);
	}
	
	public Definitions getDefs(Boolean includeDefault)
	{
		return getDefs();
	}
	
	public boolean hasDef()
	{
		return def!=null;
	}

	private void println(PrintStream ps)
	{
		println(ps, "");
	}
	
	private void println(PrintStream ps, String prefijo)
	{

		if (sil!=null)
		{
			if (prefijo.equals(""))
				prefijo = sil;
			else
				prefijo = prefijo + " " + sil;
			
			if (def!=null)
				ps.println(prefijo + " - " + def);
		}
		
		MemorySyllableListTree silHijos;
		SimplifiedListIterator i = listIterator();
		while (i.hasNext())
		{
			silHijos = (MemorySyllableListTree) i.next();
			silHijos.println(ps, prefijo);
		}
	}
	
	public SyllableListTree lookUp(String silStr)
	{
		MemorySyllableListTree sil;
		SimplifiedListIterator i = listIterator();
		while (i.hasNext())
		{
			sil = (MemorySyllableListTree) i.next();
			if (sil.equals(silStr))
				return sil;
		}
		return null;
	}

	public static void main(String args[]) throws Exception
	{
		if (args.length!=1)
		{
			System.out.println("Programa de prueba para verificar almacenamiento de diccionario.");
			System.out.println("Sintaxis: java MemorySyllableListTree arch-dict");
			return;
		}
		MemorySyllableListTree sl = new MemorySyllableListTree(args[0]);
		sl.println(System.out);
	}
}