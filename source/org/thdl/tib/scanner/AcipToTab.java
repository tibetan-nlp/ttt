package org.thdl.tib.scanner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

class AcipToTab
{
	private BufferedReader in;
	private PrintWriter out;
    private String currentDefiniendum, currentDefinition;
							
	public AcipToTab(BufferedReader in, PrintWriter out)
	{
		this.in = in;
		this.out = out;
	}
	
	public void add()
	{
	    out.println(currentDefiniendum + '\t' + currentDefinition);
	}
	
	public static void main (String[] args) throws Exception
	{
		PrintWriter out;
		BufferedReader in=null;
		boolean file=false;
		
		switch (args.length)
		{
		case 0: out = new PrintWriter(System.out);
				in = new BufferedReader(new InputStreamReader(System.in));
				break;
		case 1: out = new PrintWriter(System.out);
				file = true;
				break;
		default: out = new PrintWriter(new FileOutputStream(args[1]));
				 file = true;		
		}

		if (file)
		{
			if (args[0].indexOf("http://") >= 0) 
				in = new BufferedReader(new InputStreamReader(new BufferedInputStream((new URL(args[0])).openStream())));
			else 
				in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
		}
		
		new AcipToTab(in, out).run();
	}
	
	public void run() throws Exception
	{
	    final short newDefiniendum=1, halfDefiniendum=2, definition=3;
	    short status=newDefiniendum;
	    int marker, len, marker2, n=0, total=0, currentPage=0, currentLine=1, pos;
	    char ch;
		String entrada="", currentLetter="", temp="", lastDefiniendum="", lastWeirdDefiniendum="";
		boolean markerNotFound;
		currentDefiniendum="";
		currentDefinition="";
		outAHere:
		while (true)
		{
		    entrada=in.readLine();
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
	            {
	              currentPage=Integer.parseInt(temp);
	            }
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
            		    entrada=in.readLine();
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
                            // compare lastDefiniendum with currentDefiniendum
		                    total++;
		                    
		                    // add here
		                    add();
		                    
		                    /* include this to not include transliterated sanskrit
		                     */
                            //if (currentDefiniendum.indexOf("+")<0 && lastDefiniendum.indexOf("+")<0 && new TibetanString(lastDefiniendum).compareTo(new TibetanString(currentDefiniendum))>0) n++;
		                    lastDefiniendum=currentDefiniendum;
		                    currentDefiniendum="";
		                    currentDefinition="";
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
                                                //n++;
                                                // out.println(currentPage + ": " + entrada);
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
   	                    
   	                    /* check to see if the is a possible problem with the g suffix.

                        pos = entrada.indexOf("g ");
                        if (pos>0 && Manipulate.isVowel(entrada.charAt(pos-1)) && (markerNotFound || entrada.substring(0,pos+1).length() < entrada.substring(0, marker).trim().length()))
                        {
                            // out.println(currentPage + ": " + entrada);
                            n++;
                        }*/
       	            
   	                    /* either this is a definiendum that consists of several lines or
   	                    it is part of the last definition. */
   	                    if (markerNotFound) 
       	                {
   	                        /* assume that the definiendum goes on to the next line. */
   	                        currentDefiniendum = currentDefiniendum + " ";
   	                        status=halfDefiniendum;
   	                    }
       	                else
   	                    {
   	                        // total++;
   	                        
   	                        currentDefiniendum = currentDefiniendum + entrada.substring(0,marker).trim();
   	                        currentDefinition = "[" + currentPage + "] " + entrada.substring(marker2).trim();
   	                        
   	                        status=definition;
   	                        
   	                        while (true)
   	                        {
            		            entrada=in.readLine();
            		            
		                        if (entrada==null)
		                        {
		                            // add here
        		                    add();
		                            
                                    // if (new TibetanString(lastDefiniendum).compareTo(new TibetanString(currentDefiniendum))>0) n++;
		                            break outAHere;
		                        }
		                        
            		            currentLine++;
            		            entrada = entrada.trim();
            		            
            		            if (entrada.equals("")) break;
            		            else
            		            {
            		                currentDefinition = currentDefinition + " " + entrada;
            		            }
		                    }
   	                        
   	                    }
                    }
	                else // last line did not start with the current letter, it must still be part of the definition
	                {
	                    currentDefinition = currentDefinition + " " + entrada;
   	                    while (true)
   	                    {
            		        entrada=in.readLine();
            		            
		                    if (entrada==null)
		                    {
		                        // add here
    		                    add();
		                        
                                // if (new TibetanString(lastDefiniendum).compareTo(new TibetanString(currentDefiniendum))>0) n++;
		                        break outAHere;
		                    }
		                        
            		        currentLine++;
            		        entrada = entrada.trim();
            		            
            		        if (entrada.equals("")) break;
            		        {
            		            currentDefinition = currentDefinition + " " + entrada;
            		        }
		                }
	                }
	            
	        } else // if first character was not a letter, it must still be part of definition
	        {
	                currentDefinition = currentDefinition + " " + entrada;
   	                while (true)
   	                {
            		    entrada=in.readLine();
            		            
		                if (entrada==null)
		                {
		                    // add here
		                    add();
		                    
		                    break outAHere;
		                }
		                        
            		    currentLine++;
            		    entrada = entrada.trim();
            		            
            		    if (entrada.equals("")) break;
            		    else
            		    {
            		        currentDefinition = currentDefinition + " " + entrada;
            		    }
		            }
	        }
		}
//		out.println(n + " / " + total);
		out.flush();
	}
}