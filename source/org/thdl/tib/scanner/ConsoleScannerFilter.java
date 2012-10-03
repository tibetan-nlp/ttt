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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import org.thdl.util.SimplifiedLinkedList;
import org.thdl.util.SimplifiedListIterator;

/** Inputs a Tibetan text and displays the words with their
    definitions through the console over a shell. Use when no
    graphical interfase is supported or for batch processes. For instance:</p>
    <pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.ConsoleScannerFilter ry-dic99</pre>
    <p>It reads from the standard input and prints the results to the
    standard output. For example if you want to parse a text stored in <i>puja.txt</i>
    and save the results in <i>puja_words.txt</i>, you can run the command:</p>
    <pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.ConsoleScannerFilter ry-dic99 &lt; puja.txt &gt; puja_words.txt</pre>

    @author Andr&eacute;s Montano Pellegrini
*/

public class ConsoleScannerFilter
{
	private BufferedReader in;
	private PrintWriter out;
	private TibetanScanner scanner;

	public ConsoleScannerFilter(String archivo, BufferedReader in, PrintWriter out) throws Exception
	{
		this.in = in;
		this.out = out;
		scanner = new LocalTibetanScanner(archivo);
		scanner.getDictionaryDescriptions();
	}

	public static void main (String[] args) throws Exception
	{
		PrintWriter out;
		BufferedReader in=null;
		boolean file=false;

		if (args.length==0)
		{
			System.out.println("Sintaxis: java ConsoleScannerFilter arch-dict [orig] [dest]");
			System.out.println(TibetanScanner.copyrightASCII);
			return;
		}

		switch (args.length)
		{
		case 1: out = new PrintWriter(System.out);
				in = new BufferedReader(new InputStreamReader(System.in));
				break;
		case 2: out = new PrintWriter(System.out);
				file = true;
				break;
		default: out = new PrintWriter(new FileOutputStream(args[2]));
				 file = true;
		}

		if (file)
		{
			if (args[1].indexOf("http://") >= 0)
				in = new BufferedReader(new InputStreamReader(new BufferedInputStream((new URL(args[1])).openStream())));
			else
				in = new BufferedReader(new InputStreamReader(new FileInputStream(args[1])));
		}

		new ConsoleScannerFilter(args[0], in, out).run();
	}

	public void printIfSomething(String s)
	{
		if (s!=null && !s.equals(""))
		{
			out.println(s);
			out.flush();
		}
	}

	public void printWords()
	{
		SimplifiedLinkedList words = scanner.getTokenLinkedList();
		SimplifiedListIterator i = words.listIterator();
		Token token;
		while (i.hasNext())
		{
			token = (Token)i.next();
			if (token instanceof Word)
			    out.println(token);
		}
		out.flush();
	}

	public void run() throws Exception
	{
		String inStr;

		while ((inStr=in.readLine())!=null)
		{
			if (inStr.equals(""))
				scanner.finishUp();
			else
				scanner.scanBody(inStr);

			printWords();
			scanner.clearTokens();
		}
		scanner.finishUp();
		printWords();
		scanner.clearTokens();
	}
}

