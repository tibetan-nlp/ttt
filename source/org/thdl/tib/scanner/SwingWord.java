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

/** Used to make up the list of words to be displayed, not
	to store the dictionary. */
package org.thdl.tib.scanner;

/** Tibetan word with its corresponding definitions.

    @author Andr&eacute;s Montano Pellegrini
*/
public class SwingWord extends Word
{
    public SwingWord(Word word)
    {
        super(word);
    }
    
    public SwingWord (String word, String wordSinDec, String def)
    {
        super (word, wordSinDec, def);
    }
    
    public SwingWord (String word, Definitions def)
    {
        super (word, def);
    }
    
    public SwingWord (String word, String def)
    {
        super (word, def);
    }
    
    public SwingWord (String word, String wordSinDec, Definitions def)
    {
        super (word, wordSinDec, def);
    }
    
	public String getBookmark(boolean tibetan)
	{
	    String localWord;
	    if (tibetan) 
	    {
	        try
	        {
	            // localWord = TibetanHTML.getHTML(super.token + " ");
	        	localWord = BasicTibetanTranscriptionConverter.wylieToHTMLUnicode(super.token + " ");
	        }
	        catch (Exception e)
	        {
	            localWord = "<b>" + super.token + "</b>";
	        }
	    }
	    else localWord = "<b>" + super.token + "</b>";
		return "<a name=\"" + super.token + "\">" + localWord + "</a>";
	}

	public String getLink()
	{
	    return getLink(false);
	}
	
        /** Returns the word marked up as a link.
         *
         */
	public String getLink(boolean tibetan)
	{
	    String localWord, result=null;
	    String className = "";

            if (wordSinDec==null) localWord = super.token;
            else localWord = wordSinDec;
            if (tibetan) 
            {
                try
                {
                    result = BasicTibetanTranscriptionConverter.wylieToHTMLUnicode(localWord + " ");
                    className = " class = \"tib\"";
                }
                catch (Exception e)
                {
                    result = localWord;
                }
            }
            else result = localWord;
/*          result = "<a href=\"#" + word + "\">" + localWord;
            if (tibetan) result+= "</a>";
            else result+= "</a> ";
            return result;*/
            return "<a href=\"#" + super.token + "\"" + className + ">" + result + "</a> ";
	}	
}
