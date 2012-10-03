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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.thdl.util.Link;
import org.thdl.util.SimplifiedLinkedList;
import org.thdl.util.SimplifiedListIterator;


/** Converts Tibetan dictionaries stored in text files
	into a binary file tree structure format, to be used
	by some implementations of the SyllableListTree.

<p>Syntax (Dictionary files are assumed to be .txt. Don't include extensions!):<ul>
	<li><b>For one dictionary</b>, to read the definitions stored in <i>
    dic-name.txt</i> and organize them into <i>dic-name.wrd</i> and <i>
    dic-name.def</i>:<pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.BinaryFileGenerator [-delimiter] dict-name</pre>
	</li>
	<li><b>For multiple dictionaries</b>, to read the definitions stored in <i>
    dict-name1.txt</i>, <i>dict-name2.txt</i>, etc.and organize them into <i>
    dest-file-name.wrd</i> and <i>dest-file-name.def</i>:<pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.BinaryFileGenerator dest-file-name [-delimiter1] dict-name1 [[-delimiter2] dict-name2 ...]</pre>
	</li>
</ul>
<p>-delimiter<ul>
<li><b>If this option is omitted</b>, it is assumed that each line is an entry 
(no multiple-line entries) and the definition and definiendum are separated 
by '-' (a dash). Even though it is not 
required, it is highly recommended to include a space before and afterwards 
(to eliminate any possible ambiguity with regards to the transliteration of 
reverse vowels in <a href="http://orion.lib.virginia.edu/thdl/tools/ewts.pdf" target="_blank">
    Extended Wylie</a>). A sample entry for the dictionary is:
    <hr>
    <pre>bkra shis - 1) auspiciousness, good luck, good fortune, goodness, prosperity, happiness. 2) auspicious, favorable, fortunate, successful, felicitous, lucky. 3) verse of auspiciousness; benediction, blessing. 4) a personal name.
bde legs - 1) goodness, happiness, well-being, wellfare, auspiciousness, good fortune. 2) well, fine.</pre>
<hr>
    <p>If this were the content of a file called &quot;<i>my-glossary.txt</i>&quot; the 
    binary tree file would be generated with the command:</p>
    <pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.BinaryFileGenerator my-glossary</pre>
    </li>
<li>-<b>tab</b>: it is assumed that each line is an entry (no multiple-line 
entries) and the definition and definiendum are separated by '\t' (horizontal tabulation). 
One tabulation is enough; don't feel the need to &quot;align&quot; the definitions in your 
word-processor. A sample entry for the dictionary is:<hr>
    <pre>bkra shis	1) auspiciousness, good luck, good fortune, goodness, prosperity, happiness. 2) auspicious, favorable, fortunate, successful, felicitous, lucky. 3) verse of auspiciousness; benediction, blessing. 4) a personal name.
bde legs	1) goodness, happiness, well-being, wellfare, auspiciousness, good fortune. 2) well, fine.</pre>
<hr>
    <p>Here, the 
    binary tree file would be generated with the command:</p>
    <pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.BinaryFileGenerator -tab my-glossary</pre>
</li>
<li>
<b>-<i>string</i></b>: it is assumed that each line is an entry (no multiple-line 
entries) and the definition and definiendum are separated by the character or 
string of characters specified by the user. A sample entry for the dictionary 
is:<hr>
    <pre>bkra shis ** 1) auspiciousness, good luck, good fortune, goodness, prosperity, happiness. 2) auspicious, favorable, fortunate, successful, felicitous, lucky. 3) verse of auspiciousness; benediction, blessing. 4) a personal name.
bde legs ** 1) goodness, happiness, well-being, wellfare, auspiciousness, good fortune. 2) well, fine.</pre>
<hr>
    <p>Here, the 
    binary tree file would be generated with the command:</p>
    <pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.BinaryFileGenerator -** my-glossary</pre>
</li>
<li>-<b>acip</b>: it is assumed that the electronic file is a transliteration of 
a Tibetan dictionary. It is called &quot;acip&quot; because it accepts Acip's comment 
codes ('@' to mark page numbers, brackets to mark comments, etc). Nevertheless, 
it still requires the files to be in <a href="http://orion.lib.virginia.edu/thdl/tools/ewts.pdf" target="_blank">
    Extended Wylie</a>, so if your file is in Acip's transliteration scheme make 
sure to run <i><a href="#org.thdl.tib.scanner.AcipToWylie">org.thdl.tib.scanner.AcipToWylie</a></i> first. Definitions here can 
be of multiple lines, but with no blank lines in between. It is assumed that the 
definiendum starts after a blank line (except at the beginning of a new page 
where it could start with the last part of the previous definition) up to the <i>
shad</i> (except when the <i>shad</i> is omitted because of grammar rules as for 
instance no shad after a &quot;ga&quot; suffix without a secondary suffix). Each 
time a new letter starts, it should be clearly marked in brackets ('[', ']'), 
parenthesis ('(', ')') or llaves ('{','}'). A sample entry for the dictionary is:
<hr>
<pre>&#64;1

(ka)

ka ba/ gdung 'degs don byed nus pa/

rkyen/ grogs byed

&#64;2

(kha)

khyod dngos po dang de byung 'brel/  khyod dngos po las byung
zhing/ dngos po ldog stops kyis khyod ldog pa/

khyod dngos po dang bdag gcig 'brel/ khyod ngos po dang bdag
nyid gcig pa'i sgo nas tha dad gang zhig/ dngos po ldog
stops kyis khyod ldog pa/

khyod dngos po dang 'brel pa/ khyod dngos po dang tha dad gang

&#64;3

zhig/ ngos po ldog stobs kyis khyod ldog pa/

kha dog  mdog du rung ba'am/ sngo ser dkar dmar sogs mdog tu
rung ba'i gzugs/</pre>
<hr>
    <p>Here the 
    binary tree file would be generated with the command:</p>
    <pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.BinaryFileGenerator -acip my-glossary</pre>
<p><i>Comments:</i>&nbsp; Notice in the sample text that at the beginning of page 2, &quot;<i>zhig</i>&quot; is not a 
new definiendum, but still is part of the definition of &quot;<i>khyod dngos po dang 'brel 
pa</i>&quot;. Also the definiendum of the last entry&nbsp; is &quot;<i>kha dog</i>&quot; 
(the <i>shad</i> was omitted after &quot;<i>ga</i>&quot; suffix) and not &quot;<i>kha dog mdog du rung ba'am</i>&quot;. 
Nevertheless the definiendum of the second term is not &quot;<i>khyod dngos po dang bdag</i>&quot; 
since there is no omitted <i>shad</i> after that &quot;<i>ga</i>&quot; suffix; the 
definiedum is &quot;<i>khyod dngos po dang bdag gcig 'brel</i>&quot;. As is clear from the 
sample text, the tool has to make a series of &quot;smart guesses&quot; to try to figure 
out where each definiendum end and it's definition start.&nbsp; Such process is 
not 100% full-proof, so expect some mistakes.<br>
&nbsp;</p>
</li>
  <li>
<p>Dictionaries in different formats can be processed together. For instance the 
command:
<pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.BinaryFileGenerator alldicts ry-dic99 -acip myglossary_uma -tab myglossary_rdzogs-chen</pre>
<p>would generate <i>alldicts.def</i> and <i>alldicts.wrd</i> processing <i>ry-dic99.txt</i> 
as dash-separated, <i>myglossary_rdzogs-chen.txt</i> as tab-separated and <i>
myglossary_uma.txt</i> in the transliteration format explained above.<br>
&nbsp;</li>
</ul>

    @author Andr&eacute;s Montano Pellegrini
    @see SyllableListTree
    @see FileSyllableListTree
    @see CachedSyllableListTree
*/
public class BinaryFileGenerator extends SimplifiedLinkedList
{
    private static final int versionNumber = 3;
    
	private long posHijos;
	private String sil, def[];
    public final static int delimiterGeneric=0;
    public final static int delimiterAcip=1;
    public final static int delimiterDash=2;

	/** Number of dictionary. If 0, partial word (no definition).
	*/
	private ByteDictionarySource sourceDef;
	public static RandomAccessFile wordRaf;
	private static RandomAccessFile defRaf;

	static
	{
		wordRaf = null;
		defRaf = null;
	}

	public BinaryFileGenerator()
	{
		super();
		sil = null;
		def = null;
		posHijos=-1;
		sourceDef = null;
	}

	private BinaryFileGenerator(String sil, String def, int numDef)
	{
		super();
		int marker;
		while (true)
		{
		    marker = Manipulate.indexOfExtendedEndOfSyllableMark(sil);
		    if (marker==0) sil = sil.substring(1);
		    else if (marker==sil.length()-1) sil = sil.substring(0,sil.length()-1);
		    else break;
		}
		
		// fix for updates
		this.sourceDef = new ByteDictionarySource();

		if (marker<0)
		{
			this.sil = sil;
			this.def = new String[1];
			this.def[0] = def;
			this.sourceDef.addNewDef(numDef);
		}
		else
		{
			this.sil = sil.substring(0, marker);
			this.def = null;
			addLast(new BinaryFileGenerator(sil.substring(marker+1).trim(), def, numDef));
		}
		posHijos=-1;
	}

	public String toString()
	{
		return sil;
	}
    
	public void addFile(String archivo, int delimiterType, String delimiter, int defNum) throws Exception
	{
	    final short newDefiniendum=1, halfDefiniendum=2, definition=3;
	    short status=newDefiniendum;
	    int marker, len, marker2, currentPage=0, currentLine=1;
	    char ch;	    
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo)));
		String entrada="", s1="", s2="", currentLetter="", temp="", lastWeirdDefiniendum="", alternateWords[];
		boolean markerNotFound;
        
        // used for acip dict 
        switch(delimiterType)
        {
            case delimiterAcip:
    		    outAHere:
	    	    while (true)
		        {
		            entrada=br.readLine();
		            if (entrada==null) break;
    		        currentLine++;
    		    
	    	        entrada = entrada.trim();
		            len = entrada.length();
		            if (len<=0) continue;
    		    
                    // get page number
    	            if (entrada.charAt(0)=='@')
	                {
	                    marker = 1;
	                    while(marker<len && Character.isDigit(entrada.charAt(marker)))
	                        marker++;
    	                temp = entrada.substring(1, marker);
	                    if (temp.length()>0)
	                    currentPage=Integer.parseInt(temp);
	                    if (marker<len)
	                    {
	                        entrada = entrada.substring(marker).trim();
	                        len = entrada.length();
    	                }
	                    else continue;   
		            }

	                // get current letter
    	            if (entrada.charAt(0)=='(' || entrada.charAt(0)=='{' || entrada.charAt(0)=='?')
	                {
	                    currentLetter = entrada.substring(1, entrada.length()-2);		            
	                    /*out.println(currentPage + ": " + currentLetter);
	                    n++;*/
	                    continue;
    	            }

	                if (entrada.charAt(0)=='[')
	                {
	                    marker=1;
	                    markerNotFound=true;
	                    do
    	                {
        	                while (marker<len && markerNotFound)
	                        {
	                            if (entrada.charAt(marker)==']') markerNotFound=false;
	                            else marker++;
	                        }
	                        if (markerNotFound)
                            {
                		        entrada=br.readLine();
		                        if (entrada==null) break outAHere;
		                        currentLine++;
            	    	        len = entrada.length();
            		            marker=0;
                            }
                            else break;
	                    } while (true);
	                    if (marker<len)
	                    {
	                        entrada = entrada.substring(marker+1).trim();
	                        len = entrada.length();
	                        if (len<=0) continue;
    	                }
	                    else continue;
	                }
    		    
    		        // skip stuff. Add to previous definition.
	    	        if (entrada.startsWith("..."))
		            {
		                entrada=entrada.substring(3);
		                len = entrada.length();
		                if (len<=0) continue;
    		        }
		        
    		        // find definiendum
	    	        ch = entrada.charAt(0);
                    if (Character.isLetter(ch) || ch=='\'')
                    {
                        /* first criteria: if it is not the root letter of section it is part of the
                        previous definition, probably a page change, else go for it with following
                        code: */
                    
                        // get first syllable to check base letter
                        marker=1;
                        while (marker<len)
                        {
                            ch = entrada.charAt(marker);
                            if (Manipulate.isEndOfSyllableMark(ch) || Manipulate.isEndOfParagraphMark(ch)) break;
                            marker++;
                        }
                    
                        if (status!=halfDefiniendum) temp = Manipulate.getBaseLetter(entrada.substring(0, marker));
                        
                        // if line begins with current letter, probably it is a definiendum
                        if (status==halfDefiniendum || currentLetter.equals(temp))
   	                    {
   	                        /* Since new definiendum was found, update last and collect new. No need to update
   	                        status because it will be updated below. */
   	                        if (status==definition)
   	                        {
                                add(s1, s2, defNum);
		                        s1=""; s2="";
       	                    }
   	                    
           	                marker=marker2=1;
   	                        markerNotFound=true;
       	                    
   	                        while (marker < len)
   	                        {
       	                        ch = entrada.charAt(marker);
       	                        
       	                        if (Manipulate.isEndOfParagraphMark(ch))
       	                        {
   	                                markerNotFound=false;
   	                                marker2=marker+1;
       	                        }
       	                        else if (Manipulate.isEndOfSyllableMark(ch))
       	                        {
           	                        if (marker+1<len && Manipulate.isEndOfSyllableMark(entrada.charAt(marker+1))) // verify "  "
       	                            {
   	                                    markerNotFound=false;
   	                                    marker2=++marker;
   	                                }
       	                        }
       	                        else
       	                        {
       	                            switch(ch)
   	                                {
   	                                    case '(': case '<':
   	                                        markerNotFound=false;
       	                                    marker2=marker;
   	                                    break;
   	                                    case 'g': // verify "g "
       	                                    if (marker+1<len && Manipulate.isVowel(entrada.charAt(marker-1)) && Manipulate.isEndOfSyllableMark(entrada.charAt(marker+1)))
       	                                    {
       	                                        temp = entrada.substring(0, marker+1);
       	                                        if (!lastWeirdDefiniendum.startsWith(temp))
           	                                    {
   	                                                markerNotFound=false;
   	                                                marker2=++marker;
                                                    lastWeirdDefiniendum=temp;
                                                }
   	                                        }
   	                                    break;
   	                                    case '.':
       	                                    if (marker+2<len && entrada.charAt(marker+1)=='.' && entrada.charAt(marker+2)=='.')
   	                                        {
   	                                            markerNotFound=false;
   	                                            marker2=marker;
   	                                        }
   	                                    break;
       	                                default:
   	                                        if (Character.isDigit(ch))
   	                                        {
   	                                            markerNotFound=false;
   	                                            marker2=marker;
       	                                    }
   	                                }
   	                            }
   	                            if (markerNotFound) marker++;
   	                            else break;
   	                        }
       	                
   	                        /* either this is a definiendum that consists of several lines or
       	                    it is part of the last definition. */
       	                    if (markerNotFound) 
           	                {
   	                            /* assume that the definiendum goes on to the next line. */
   	                            s1 = s1 + entrada + " ";
   	                            status=halfDefiniendum;
   	                        }
       	                    else
   	                        {
       	                        s1 = s1 + entrada.substring(0,marker).trim();
   	                            s2 = "[" + currentPage + "] " + entrada.substring(marker2).trim();
   	                            status=definition;
   	                            
   	                            while (true)
   	                            {
            		                entrada=br.readLine();
            		                
		                            if (entrada==null)
    		                        {
	    	                            add(s1, s2, defNum);
		                                break outAHere;
		                            }
		                        
                		            currentLine++;
                		            entrada = entrada.trim();
            		            
                		            if (entrada.equals("")) break;
                		            else
                		            {
		                                s2 = s2 + " " + entrada;
		                            }
		                        }
   	                        
       	                    }   	            
	                    }
	                    else // last line did not start with the current letter, it must still be part of the definition
	                    {
                            s2 = s2 + " " + entrada;
   	                        while (true)
   	                        {
            		            entrada=br.readLine();
            		                
    		                    if (entrada==null)
	    	                    {
		                            add(s1, s2, defNum);
		                            break outAHere;
		                        }
		                            
            		            currentLine++;
            		            entrada = entrada.trim();
            		            
                		        if (entrada.equals("")) break;
                		        else
                		        {
		                            s2 = s2 + " " + entrada;
		                        }
		                    }
    	                }
	                }
	                else // if first character was not a letter, it must still be part of definition
    	            {
                        s2 = s2 + " " + entrada;
   	                    while (true)
   	                    {
            	    	    entrada=br.readLine();
            		                
		                    if (entrada==null)
    		                {
	    	                    add(s1, s2, defNum);
		                        break outAHere;
		                    }
		                            
            		        currentLine++;
            		        entrada = entrada.trim();
            		            
                		    if (entrada.equals("")) break;
                		    else
                		    {
		                        s2 = s2 + " " + entrada;
		                    }
		                }
	                }
		        }
		    break;
		    default:
        		while ((entrada = br.readLine())!=null)
	        	{
		        	entrada = entrada.trim();
			        if (!entrada.equals(""))
        			{
        			    switch(delimiterType)
        			    {
        			        /* this is needed to make sure that the dash used in reverse vowels with extended
        			        wylie is not confused with the dash that separates definiendum and definition. */
        			        case delimiterDash:
        			            marker=entrada.indexOf('-');
        			            len = entrada.length(); 
        			            while (marker>=0 && marker<len-1 && Manipulate.isVowel(entrada.charAt(marker+1)) && !Character.isWhitespace(entrada.charAt(marker-1)))
        			            {
        			                marker = entrada.indexOf('-', marker+1);
        			            }
        			        break;
        			        default:
	        		        marker = entrada.indexOf(delimiter);
	        		    }
		                if (marker<=0)
		                {
		                    System.out.println("Error loading line " + currentLine + ", in file " + archivo + ":");
		                    System.out.println(entrada);
        		        }
	        	        else
		                {
		                    marker2 = Manipulate.indexOfBracketMarks(entrada.substring(0,marker));
		                    if (marker2>0) marker = marker2;
		                    
		                    s1 = Manipulate.deleteQuotes(entrada.substring(0,marker).trim());
		                    s2 = Manipulate.deleteQuotes(entrada.substring(marker+delimiter.length())).trim();
		                    
		                    if (Manipulate.isMeaningful(s2))
		                    {
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
		                                add(alternateWords[marker2],s2, defNum);
		                            }
    		                        
		                        }
		                        else add(s1, s2 , defNum);
		                    }
    		            }
    		        }
		    	    currentLine++;            
	    		}
        }
	}


	private void add(String word, String def, int defNum)
	{
		Link link, newLink;
		BinaryFileGenerator ultimo;
		String firstSillable;
		int marker, comp;
		
		while (true)
		{
		    marker = Manipulate.indexOfExtendedEndOfSyllableMark(word);
		    if (marker==0) word = word.substring(1);
		    else if (marker==word.length()-1) word = word.substring(0,word.length()-1);
		    else break;
		}	
				
		if (marker<0)
			firstSillable = word;
		else firstSillable = word.substring(0,marker);

		/* usa orden alfabetico */
		if (isEmpty() || ((comp = firstSillable.compareTo((ultimo = (BinaryFileGenerator) getLast()).toString()))<0))
		{
			super.addLast(new BinaryFileGenerator(word, def, defNum));
		}
		else
		{
			if (comp==0)
				if (marker<0) ultimo.addMoreDef(def, defNum);
				else ultimo.add(word.substring(marker+1).trim(), def, defNum);
			else
			{
				link = cabeza;
				while(link.siguiente!=null)
				{
					comp = firstSillable.compareTo(link.siguiente.toString());
					if (comp<0)
					{
						newLink = new Link(new BinaryFileGenerator(word, def, defNum));
						newLink.siguiente = link.siguiente;
						link.siguiente = newLink;
						return;
					}
					else
						if (comp==0)
						{
							ultimo = (BinaryFileGenerator) link.siguiente.get();
							if (marker<0) ultimo.addMoreDef(def, defNum);
							else ultimo.add(word.substring(marker+1).trim(), def, defNum);
							return;
						}
					link = link.siguiente;
				}
				newLink = new Link(new BinaryFileGenerator(word, def, defNum));
				link.siguiente = newLink;
			}
		}
	}
	
	private void reGroup (int n)
	{
	    int i, pos, posEnd;
	    
	    for (i=0; i<def.length; i++)
	    {
	        if (i!=n)
	        {
	            // deal with repetitions of definitions
	            if (def[i].length()>=def[n].length())
	            {
	                pos = def[i].indexOf(def[n]);
	                
	                // if it is the same String exactly
	                if (pos==0 && def[i].length()==def[n].length())
	                {
	                    if (i<n)
	                    {
	                        sourceDef.addDictToDef(sourceDef.getDef(n), i);
	                        def = Manipulate.deleteString(def, n);
	                        sourceDef.deleteDef(n);
	                        n = i;
	                        continue;
	                    }
	                    else
	                    {
	                        sourceDef.addDictToDef(sourceDef.getDef(i), n);
	                        def = Manipulate.deleteString(def, i);
	                        sourceDef.deleteDef(i);
	                        i--;
	                        continue;
	                    }
	                }
	                else
	                {
	                    posEnd = pos + def[n].length();

	                    if ((pos==0 || (pos>0 && !Character.isLetter(def[i].charAt(pos-1)))) && (posEnd==def[i].length() || !Character.isLetter(def[i].charAt(posEnd))))
	                    {
	                        if(sourceDef.getDef(i).contains(sourceDef.getDef(n)))
	                        {
	                            def = Manipulate.deleteString(def, n);
	                            sourceDef.deleteDef(n);
	                            return;
	                        }
	                        
	                        // else
	                        sourceDef.addDictToDef(sourceDef.getDef(i), n);
	                        
	                        do
	                        {
	                            def[i] = Manipulate.replace(def[i], pos, posEnd, "*");
	                            pos = def[i].indexOf(def[n]);
	                            posEnd = pos + def[n].length();
	                        } while ((pos==0 || (pos>0 && !Character.isLetter(def[i].charAt(pos-1)))) && (posEnd==def[i].length() || !Character.isLetter(def[i].charAt(posEnd))));
	                    
	                        if (i<n)
	                        {
	                            def = Manipulate.addString(def, def[n], i);
	                            def = Manipulate.deleteString(def, n+1);
	                            sourceDef.insertDef(sourceDef.getDef(n), i);
	                            sourceDef.deleteDef(n+1);
	                            n = i;
	                            reGroup(i+1);
	                        }
	                        else
	                        {
	                            reGroup(i);
	                        }
	                    }
	                }
	            }
	            else
	            {
	                pos = def[n].indexOf(def[i]);	                
	                posEnd = pos + def[i].length();
	                
	                if ((pos==0 || (pos>0 && !Character.isLetter(def[n].charAt(pos-1)))) && (posEnd==def[n].length() || !Character.isLetter(def[n].charAt(posEnd))))
	                {
	                    if (sourceDef.getDef(n).contains(sourceDef.getDef(i)))
	                    {
	                        def = Manipulate.deleteString(def, i);
	                        sourceDef.deleteDef(i);
	                        i--;
	                        continue;
	                    }	                        

                        sourceDef.addDictToDef(sourceDef.getDef(n), i);
	                    
	                    do
	                    {
	                        def[n] = Manipulate.replace(def[n], pos, posEnd, "*");
	                        pos = def[n].indexOf(def[i]);
	                        posEnd = pos + def[i].length();
	                    } while ((pos==0 || (pos>0 && !Character.isLetter(def[n].charAt(pos-1)))) && (posEnd==def[n].length() || !Character.isLetter(def[n].charAt(posEnd))));
	                
	                    i=-1; // start over
	                    continue;
	                }
	            }
	            
	            // deal with repetition of dictionaries
	            
	            if (sourceDef.getDef(i).equals(sourceDef.getDef(n)))
	            {
	                if (i<n)
	                {
	                    def[i] = def[i] + ". " + def[n];
	                    def = Manipulate.deleteString(def, n);
	                    sourceDef.deleteDef(n);
	                    n = i;
	                    continue;
	                }
	                else
	                {
	                    def[n] = def[n] + ". " + def[i];
	                    def = Manipulate.deleteString(def, i);
	                    sourceDef.deleteDef(i);
	                }
	            }
	        }
	    }
	}

	private void addMoreDef(String def, int numDef)
	{
	    String temp;
	    boolean notAlreadyThere, changed;
	    int i, pos, posEnd;
	    
		if (this.def==null)
		{
		    // add a new definition for this dictionary
			this.def = new String[1];
			this.def[0] = def;
			//sourceDef.add(numDef);
			sourceDef.addNewDef(numDef);
		}
		else
		{
			notAlreadyThere = true;
			do
			{
			    i=0;
    			changed = false;
			        
			    while (notAlreadyThere && i<this.def.length)
			    {
			        if (this.def[i].length()>=def.length())
			        {
			            pos = this.def[i].indexOf(def);
			            posEnd = pos + def.length();
			            if ((pos==0 || (pos>0 && !Character.isLetter(this.def[i].charAt(pos-1)))) && (posEnd==this.def[i].length() || !Character.isLetter(this.def[i].charAt(posEnd))))
			            {
			                if (!sourceDef.isDictInDef(numDef, i))
			                {
			                    if (this.def[i].length()>def.length())
			                    {
			                        //temp = Manipulate.deleteSubstring(this.def[i], pos, posEnd);
			                        temp = this.def[i];
			                        do
			                        {
			                            temp = Manipulate.replace(temp, pos, posEnd, "*");
			                            pos = temp.indexOf(def);
			                            posEnd = pos + def.length();
			                        } while ((pos==0 || (pos>0 && !Character.isLetter(temp.charAt(pos-1)))) && (posEnd==temp.length() || !Character.isLetter(temp.charAt(posEnd))));
			                        
			                        this.def[i] = def;
			                        this.def = Manipulate.addString(this.def, temp, i+1);
			                        sourceDef.dubDef(i);
			                        sourceDef.addDictToDef(numDef, i);
			                        
			                        reGroup(i);
			                        if (i+1<this.def.length) reGroup(i+1);
			                        else reGroup(this.def.length-1);
			                    }
			                    else sourceDef.addDictToDef(numDef, i);
			                }
			                notAlreadyThere = false;
			                changed = false;
			            }
			        }
			        else
			        {
			            pos = def.indexOf(this.def[i]);
			            posEnd = pos + this.def[i].length();
			            
			            if ((pos==0 || (pos>0 && !Character.isLetter(def.charAt(pos-1)))) && (posEnd==def.length() || !Character.isLetter(def.charAt(posEnd))))
			            {
			                if (sourceDef.isDictInDefAlone(numDef, i))
			                {
			                    this.def[i] = def;
			                    reGroup(i);
			                }
			                else
			                {
    			                sourceDef.addDictToDef(numDef, i);
			                    do
    			                {
    			                    //def = Manipulate.deleteSubstring(def, pos, posEnd);
    			                    def = Manipulate.replace(def, pos, posEnd, "*");
    			                    pos = def.indexOf(this.def[i]);
	    		                    posEnd = pos + this.def[i].length();	                
			                    } while ((pos==0 || (pos>0 && !Character.isLetter(def.charAt(pos-1)))) && (posEnd==def.length() || !Character.isLetter(def.charAt(posEnd))));
			                }
			                changed = true;
			            }
			        }
			        i++;
			    }
			} while (changed);
			    
			if (notAlreadyThere)
			{
			    // check if it is a duplicate for the same dictionary.
			    i = sourceDef.containsAlone(numDef);
			    if (i>-1)
			    {
			        this.def[i] = this.def[i] + ". " + def;
			        reGroup(i);
			    }
			    else
			    {
			        this.def = Manipulate.addString(this.def, def, this.def.length);
				    sourceDef.addNewDef(numDef);
				    reGroup(this.def.length-1);
				}
			}
		}
	}

	public boolean equals (Object o)
	{
		if (o instanceof String)
		{
			return sil.equals((String)o);
		}
		else return false;
	}


	private void printMe(boolean hasNext) throws Exception
	{
		int i;

		wordRaf.writeInt((int) posHijos);
		wordRaf.writeUTF(sil);
		sourceDef.print(hasNext, wordRaf);

		if (def!=null)
			for (i=0; i<def.length; i++)
			{
			    try
			    {
				    wordRaf.writeInt((int)defRaf.getFilePointer());
				    defRaf.writeUTF(def[i]);
				}
				catch (Exception e)
				{
				    System.out.println(def[i]);
				}
			}
	}

	private void print() throws Exception
	{
		long pos;
		SimplifiedListIterator i = listIterator();
		
		BinaryFileGenerator silHijos;
		boolean hasNext;

		while (i.hasNext())
		{
			silHijos = (BinaryFileGenerator) i.next();
			if (!silHijos.isEmpty()) silHijos.print();
		}
		pos = wordRaf.getFilePointer();
		if (!isEmpty())
		{
			posHijos=pos;
			i = listIterator();
			hasNext = true;
			while (hasNext)
			{
				silHijos = (BinaryFileGenerator) i.next();
				hasNext=i.hasNext();
				silHijos.printMe(hasNext);
			}
		}
	}

    private static void printSintax()
    {
		System.out.println("Stores multiple dictionaries into a binary tree file.");
        System.out.println("Sintaxis:");
		System.out.println("-For multiple dictionary sources:");
		System.out.println("  java BinaryFileGenerator arch-dest [-delimiter1] arch-dict1");
		System.out.println("                                    [[-delimiter2] arch-dict2 ...]");
		System.out.println("-For one dictionary");
		System.out.println("  java BinaryFileGenerator [-delimiter] arch-dict");
		System.out.println("Dictionary files are assumed to be .txt. Don't include extensions!");
		System.out.println("  -delimiter: default value is \'-\'. -tab takes \'\\t\' as delimiter.");
		System.out.println("  -acip: use this to process dictionaries entered using the ACIP standard");
		System.out.println("         to mark page numbers, comments, etc. Make sure to convert it to");
		System.out.println("         THDL's extended Wylie scheme first using the AcipToWylie class.");
    }
    
    public void generateDatabase(String name) throws Exception
    {
        File wordF = new File(name + ".wrd"), defF = new File(name + ".def");
        wordF.delete();
		defF.delete();
		wordRaf = new RandomAccessFile(wordF,"rw");
		defRaf = new RandomAccessFile(defF,"rw");
		print();
		wordRaf.writeInt((int)posHijos);
        
        // write version marker
        wordRaf.writeShort(-1);
        wordRaf.writeByte(-1);
        
        // write version number
        wordRaf.writeByte(versionNumber);
    }

	public static void main(String args[]) throws Exception
	{
	    int delimiterType;
	    String delimiter;
	    
	    int i, n=0, a;
	    
        delimiter = "-";
        delimiterType=delimiterDash;

		if (args.length==0)
		{
		    printSintax();
		    return;
		}
		BinaryFileGenerator sl = new BinaryFileGenerator();
        if (args[0].charAt(0)=='-')
        {
            if (args[0].equals("-tab"))
            {
                delimiterType = delimiterGeneric;
                delimiter="\t";
            }
            else if (args[0].equals("-acip"))
                delimiterType=delimiterAcip;
            else
            {
                delimiterType=delimiterGeneric;
                delimiter=args[0].substring(1);
            }
            if (args.length>2)
            {
                printSintax();
                return;
            }
            sl.addFile(args[1] + ".txt",delimiterType, delimiter, 0);
            a=1;
        }
        else
        {
            a=0;
		    if (args.length==1)
		    {
                sl.addFile(args[0] + ".txt", delimiterType, delimiter, 0);
		    }
		    else
            {
                i=1;

                while(i< args.length)
                {
                    if (args[i].charAt(0)=='-')
                    {
                        if (args[i].equals("-tab"))
                        {
                            delimiterType=delimiterGeneric;
                            delimiter="\t";
                        }
                        else if (args[i].equals("-acip"))
                            delimiterType=delimiterAcip;
                        else
                        {
                            delimiterType=delimiterGeneric;
                            delimiter=args[i].substring(1);
                        }
                        i++;
                    }
                    else 
                    {
                        delimiterType=delimiterDash;
                    }
                    System.out.println("\nProcessing " + args[i] + "...");
                    sl.addFile(args[i] + ".txt", delimiterType, delimiter, n);
                    n++; i++;
                }
            }
		}
		System.out.println("Writing to file " + args[a] + "...");
		System.out.flush();
		sl.generateDatabase(args[a]);
	}
}
