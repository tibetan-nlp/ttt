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

//import org.thdl.tib.text.TibetanHTML;

/** Tibetan word with its corresponding definitions.

    @author Andr&eacute;s Montano Pellegrini
*/
public class Word extends Token
{	
	/** Used to rebuild the text the user entered. */
	protected String wordSinDec;
	protected Definitions def;
	
	public Word (Word word)
	{
	    this(word.token, word.wordSinDec, word.def);
	}
	
	public Word (String word, String wordSinDec, String def)
	{
		super(word);
		this.wordSinDec=wordSinDec;
		this.def=new Definitions(def);
	}

	public Word (String word, String wordSinDec, Definitions def)
	{
		super(word);
		this.wordSinDec=wordSinDec;
		this.def=def;
	}
	
	public Word (String word, String def)
	{
		this(word, null, def);
	}

	public Word (String word, Definitions def)
	{
		this(word, null, def);
	}
	
	public boolean equals(Object o)
	{
		Word wO;
		
		if (o instanceof Word)
		{
			wO = (Word) o;
			return super.token.equals(wO.token);
		}
		return false;
	}
	
	public String getWylie()
	{
		return super.token;
	}
	
	public String getDef()
	{
		return def.toString();
	}	
	
	public String getDefPreview()
	{
		return def.getPreview();
	}
	
	public String getWordDefPreview()
	{
	    return super.token + " - " + getDefPreview();
	}
	
	public Definitions getDefs()
	{
		return def;
	}
	
	public String toString()
	{
		return super.token + " - " + def;
	}
	
	/** Called in order to redisplay the text with links keeping
		the returns the user entered.
	*/
	public void makeEnter()
	{
		if (wordSinDec==null)
			wordSinDec=new String(super.token);
		wordSinDec+="</p><p>";
	}	
}
