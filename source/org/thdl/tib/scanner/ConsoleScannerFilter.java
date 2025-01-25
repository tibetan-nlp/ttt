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

import java.io.*;

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
	private int mode, delimiterType, cols;
	private String delimiter;
	private static final int TRANSLATION=1;
	private static final int CONVERSION=2;
	private static final int COLUMN_CONVERSION=3;

	public ConsoleScannerFilter(String archivo, BufferedReader in, PrintWriter out, int mode, int delimiterType, String delimiter, int cols) throws Exception
	{
		this.in = in;
		this.out = out;
		this.mode = mode;
		this.delimiterType = delimiterType;
		this.delimiter = delimiter;
		this.cols = cols;
		if (mode==CONVERSION || mode==COLUMN_CONVERSION) scanner = new LocalTibetanScanner(archivo, true);
		else scanner = new LocalTibetanScanner(archivo);
		scanner.getDictionaryDescriptions();
	}

	public static void printSyntax()
	{
		System.out.println("Sintaxis: java ConsoleScannerFilter [options] dict-file [source] [destination]");
		System.out.println();
		System.out.println("Valid options:");
		System.out.println("  -encoding code");
		System.out.println("  -mode [translation | conversion | column_conversion]");
		System.out.println("  -tab | -delimiter: delimiter used for first_column_conversion. Default is - (dash).");
		System.out.println("  -cols num: number of columns requiring conversion.");
		System.out.println("  -errorsto file-name");
		System.out.println();
		System.out.println(TibetanScanner.copyrightASCII);
	}

	public static void main (String[] args) throws Exception
	{
		PrintWriter out;
		BufferedReader in=null;
		boolean file=false;
		int i=0, mode=TRANSLATION;
		String encoding=null, option, delimiter="-";
		int delimiterType=BinaryFileGenerator.delimiterDash, cols=1;

		if (args.length==0)
		{
			printSyntax();
			return;
		}

		while (args[i].charAt(0)=='-')
		{
			option=args[i++].substring(1);
			if (option.equals("encoding"))
			{
				encoding=args[i++];
			}
			else if (option.equals("mode"))
			{
				option=args[i++].toLowerCase();
				if (option.equals("conversion")) mode = CONVERSION;
				else if (option.equals("translation")) mode = TRANSLATION;
				else if (option.equals("column_conversion")) mode = COLUMN_CONVERSION;
				else
				{
					System.out.println("Invalid mode!");
					System.out.println();
					printSyntax();
					return;
				}
			}
			else if (option.equals("tab"))
			{
				delimiterType=BinaryFileGenerator.delimiterGeneric;
				delimiter="\t";
			}
			else if (option.equals("cols"))
			{
				cols=Integer.valueOf(args[i++]).intValue();
			}
			else if (option.equals("errorsto"))
			{
				System.setErr(new PrintStream(new FileOutputStream(args[i++])));
			}
			else
			{
				delimiterType=BinaryFileGenerator.delimiterGeneric;
				delimiter=option;
			}
		}

		switch (args.length-i)
		{
		case 1: out = new PrintWriter(System.out);
		in = new BufferedReader(new InputStreamReader(System.in));
		break;
		case 2: out = new PrintWriter(System.out);
		file = true;
		break;
		default:
			if (encoding==null) out = new PrintWriter(new FileOutputStream(args[i+2]));
			else out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[i+2]), encoding));
		file = true;
		}

		if (file) in = BasicTibetanTranscriptionConverter.getBufferedReader(args[i+1], encoding);
		new ConsoleScannerFilter(args[i], in, out, mode, delimiterType, delimiter, cols).run();
	}

	public void printIfSomething(String s)
	{
		if (s!=null && !s.equals(""))
		{
			out.println(s);
			out.flush();
		}
	}

	public boolean printConvertedLine()
	{
		char ch;
		String s;
		Token tokens[] = scanner.getTokenArray();
		Word word;
		boolean notFirst = false, isSyllable = false, needsSpace=false, allOk=true;
		for(int i=0; i<tokens.length; i++)
		{
			if (tokens[i] instanceof Word)
			{
				word = (Word)tokens[i];
				if (notFirst) out.print("\u0F0B");
				else notFirst=true;
				s = word.getDefs().def[0];
				if (s.equals("[not found]"))
				{
					s = word.token;
					out.print(s);
					if (!Character.isDigit((s.charAt(s.length()-1)))) out.print(" ");
					notFirst = false;
					isSyllable = false;
					if (Manipulate.containsLetters(s))
					{
						System.err.println(s);
						allOk=false;
					}
				}
				else
				{
					out.print(s);
					isSyllable = true;
				}
			}
			else
			{
				s = tokens[i].toString().trim();
				if (s==null || s.equals(""))
				{
					allOk=false;
					System.err.println("Token blank?");
					continue;
				}
				ch = s.charAt(0);
				if (",[]".indexOf(ch)>=0)
				{
					if (isSyllable) out.print("\u0F0B");
					out.print(ch);
					needsSpace = (",]".indexOf(ch)>=0);
				}
				else
				{
					out.print(BasicTibetanTranscriptionConverter.wylieToUnicode(s));
					needsSpace = ("<({".indexOf(ch)==-1);
				}
				if (needsSpace) out.print("\u00A0");
				notFirst = false;
				isSyllable = false;
			}
		}
		if (isSyllable) out.print("\u0F0B");
		out.flush();
		return allOk;
	}

	public void printWordsWithDefs()
	{
		Word words[] = scanner.getWordArray();
		for(int i=0; i<words.length; i++) out.println(words[i]);
		out.flush();
	}

	public void run() throws Exception
	{
		String inStr, s1, s2, alternateWords[];
		int i, marker, marker2, len, currentLine=0;
		boolean notFirst, allOk;
		outThere:
			while ((inStr=in.readLine())!=null)
			{
				currentLine++;
				inStr = inStr.trim();
				if (inStr.equals(""))
				{
					scanner.finishUp();
					continue;
				}
				allOk=true;
				if (mode==COLUMN_CONVERSION)
				{
					if (cols==1)
					{
						marker = inStr.indexOf(delimiter);
						if (delimiterType==BinaryFileGenerator.delimiterDash)
						{
							len = inStr.length(); 
							while (marker>=0 && marker<len-1 && Manipulate.isVowel(inStr.charAt(marker+1)) && !Character.isWhitespace(inStr.charAt(marker-1)))
								marker = inStr.indexOf('-', marker+1);
						}
						if (marker<=0)
						{
							System.out.println("Error in delimiter for line " + currentLine + ":");
							System.out.println(inStr);
							continue;
						}
						marker2 = Manipulate.indexOfBracketMarks(inStr.substring(0,marker));
						if (marker2>0) marker = marker2;

						s1 = Manipulate.deleteQuotes(inStr.substring(0,marker).trim()).trim();
						s2 = Manipulate.deleteQuotes(inStr.substring(marker+delimiter.length()).trim()).trim();

						if (!Manipulate.isMeaningful(s2)) continue;
						if (currentLine%5000==0)
						{
							System.out.println("Adding " + s1 + "...");
							System.out.flush();
						}
						marker2 = s1.indexOf(';');
						if (marker2>0)
						{
							alternateWords = s1.split(";");
							for (marker2=0; marker2<alternateWords.length; marker2++)
							{
								//add(alternateWords[marker2],s2, defNum);
								scanner.scanLine(alternateWords[marker2]);
								scanner.finishUp();
								if (!this.printConvertedLine()) allOk=false;
								out.print("\t");
								out.println(s2);
								scanner.clearTokens();
							}
						}
						else
						{
							scanner.scanLine(s1);
							scanner.finishUp();
							if (!this.printConvertedLine()) allOk=false;
							scanner.clearTokens();
							out.print("\t");
							while((marker=s2.indexOf('{'))>=0)
							{
								marker2=s2.indexOf('}', marker);
								if (marker2<marker)
								{
									System.err.print("Curly bracket error at " + currentLine + ":");
									System.err.println(inStr);
									out.println(s2);
									continue outThere;
								}
								out.print(s2.substring(0,marker));
								scanner.scanLine(s2.substring(marker+1, marker2));
								scanner.finishUp();
								if (!this.printConvertedLine()) allOk=false;
								scanner.clearTokens();
								s2=s2.substring(marker2+1);
							}
							out.println(s2);
						}
					}
					else
					{
						alternateWords = inStr.split(delimiter);
						notFirst = false;
						for (i=0; i<alternateWords.length; i++)
						{
							s1 = Manipulate.deleteQuotes(alternateWords[i].trim()).trim();
							if (notFirst) out.print("\t");
							else notFirst = true;
							if (!s1.equals(""))
							{
								if (i<cols)
								{
									scanner.scanLine(s1);
									scanner.finishUp();
									if (!this.printConvertedLine()) allOk=false;
									scanner.clearTokens();
								}
								else out.print(s1);
							}
						}
						out.println();
					}
					out.flush();
				}
				else
				{
					scanner.scanLine(inStr);
					if (mode==CONVERSION) scanner.finishUp();
					if (mode==TRANSLATION) printWordsWithDefs();
					else if (mode==CONVERSION)
					{
						if (!printConvertedLine()) allOk=false;
						out.println();
						out.flush();
						scanner.clearTokens();
					}
				}
				if (!allOk)
				{
					System.err.println("Conversion error at " + currentLine + ": " + inStr);
					System.err.flush();
				}
			}
		if (mode==TRANSLATION)
		{
			scanner.finishUp();
			printWordsWithDefs();
			scanner.clearTokens();
		}
	}
}

