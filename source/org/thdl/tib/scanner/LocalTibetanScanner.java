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
import java.util.Enumeration;
import java.util.Vector;

/** Loads dictionary stored in tree format and searches for words recursively.
 How the the dictionary is loaded depends on which implementation of
 {@link SyllableListTree} is invoked.
 
 @author Andr&eacute;s Montano Pellegrini
 @see SyllableListTree
 */
public class LocalTibetanScanner extends TibetanScanner
{
	public static String archivo;
	private SyllableListTree raiz, silActual, lastCompSil, silAnterior;
	private String wordActual, lastCompWord;
	private int wordActualOffset, lastCompWordOffset;
	private Vector floatingSil;
	
	static
	{
		archivo = null;
	}
	
	public BitDictionarySource getDictionarySource()
	{
		return raiz.getDictionarySourcesWanted();
	}
	
	public LocalTibetanScanner(String arch) throws Exception
	{
		this (arch, true);
	}
	
	public LocalTibetanScanner(String arch, boolean backwardCompatible) throws Exception
	{
		super();
		archivo = arch;
		// raiz = new MemorySyllableListTree(archivo);
		// raiz = new FileSyllableListTree(archivo);
		raiz = new CachedSyllableListTree(archivo, backwardCompatible);
		floatingSil = new Vector();
		resetAll();
	}
	
	private void resetAll()
	{
		silAnterior = silActual = lastCompSil = null;
		wordActual = lastCompWord = null;
	}
	
	private void scanSyllable(Token silToken)
	{
		String sil = silToken.getRawValue();
		
		SyllableListTree resultado=null;
		Enumeration enumeration;
		Word w;
		String silSinDec;
		boolean aadded;
		
		if (silActual==null)
			silActual = raiz;
		
		silAnterior = silActual;
		silActual = silActual.lookUp(sil);
		
		if (silActual != null)
		{
			if (silActual.hasDef())
			{
				if (wordActual == null) {
					lastCompWordOffset = silToken.getOffset();
				}
				else {
					lastCompWordOffset = wordActualOffset;
				}
				lastCompWord = concatWithSpace(wordActual, sil);
				lastCompSil = silActual;
				floatingSil.removeAllElements();
			}
			else
			{
				silSinDec = withOutDec(sil);
				resultado=null;	
				// while to take into account very weird cases like le'u'i'o
				while (resultado == null && silSinDec!=null)
				{
					resultado = silAnterior.lookUp(silSinDec);
					if (resultado == null || !resultado.hasDef())
					{
						silSinDec += "\'";
						resultado = silAnterior.lookUp(silSinDec);
						aadded=true;
					}
					else aadded=false;
					
					if (resultado!=null && resultado.hasDef())
					{
						if (wordActual == null) {
							lastCompWordOffset = silToken.getOffset();
						}
						else {
							lastCompWordOffset = wordActualOffset;
						}
						lastCompWord = concatWithSpace(wordActual, silSinDec);
						lastCompSil = resultado;
						if (wordActual == null) {
							wordActualOffset = silToken.getOffset();
						}
						wordActual = concatWithSpace(wordActual, sil);
						floatingSil.removeAllElements();
					}
					else
					{
						resultado = null;
						if (aadded) silSinDec = silSinDec.substring(0, silSinDec.length()-1);
						silSinDec = withOutDec(silSinDec);
					}
				}
				if (resultado!=null) return;
				
				if (lastCompSil!=null)
					floatingSil.addElement(silToken);
			}
			if (wordActual == null) {
				wordActualOffset = silToken.getOffset();
			}
			wordActual = concatWithSpace(wordActual, sil);
		}
		else
		{
			silSinDec = withOutDec(sil);
			resultado = null;
			// while to take into account very weird cases like le'u'i'o
			while (resultado==null && silSinDec!=null)
			{
				resultado = silAnterior.lookUp(silSinDec);
				/*  here we don't have to worry about being in the middle of a
				 word since the declension marks that it is the end of a
				 word.
				 */
				if (resultado == null || !resultado.hasDef())
				{
					silSinDec += "\'";
					resultado = silAnterior.lookUp(silSinDec);
					aadded=true;
				}
				else aadded=false;
				// si funciona sin declension arreglado problema
				if (resultado!=null && resultado.hasDef())
				{
					if (wordActual == null) {
						wordActualOffset = silToken.getOffset();
					}
					wordList.addLast(new Word(concatWithSpace(wordActual, silSinDec), concatWithSpace(wordActual,sil), resultado.getDefs(), wordActualOffset));
					resetAll();
					floatingSil.removeAllElements();
				}
				else
				{
					resultado = null;
					if (aadded) silSinDec = silSinDec.substring(0, silSinDec.length()-1);
					silSinDec = withOutDec(silSinDec);
				}
				
			}
			if (resultado!=null) return;
			
			if (lastCompSil!=null)
			{
				if (lastCompWord.equals(wordActual)) w = new Word(lastCompWord, lastCompSil.getDefs(), lastCompWordOffset);
				else w = new Word(lastCompWord, wordActual, lastCompSil.getDefs(), lastCompWordOffset);
				wordList.addLast(w);
				this.resetAll();
				
				enumeration = floatingSil.elements();
				floatingSil = new Vector();
				while (enumeration.hasMoreElements())
					scanSyllable((Token)enumeration.nextElement());
				
				scanSyllable(silToken);
			}
			else
			{
				if (silAnterior!=raiz)
				{
					w = new Word(wordActual, "[incomplete word]", wordActualOffset);
					wordList.addLast(w);
					this.resetAll();
					scanSyllable(silToken);
				}
				else
				{
					w = new Word(sil, "[not found]", silToken.getOffset());
					wordList.addLast(w);
					this.resetAll();
				}
			}
		}
	}
	
	public void finishUp()
	{
		Enumeration enumeration;
		Word w;
		
		while (lastCompSil!=null)
		{
			if (lastCompWord.equals(wordActual)) w = new Word(lastCompWord, lastCompSil.getDefs(), lastCompWordOffset);
			else w = new Word(lastCompWord, wordActual, lastCompSil.getDefs(), lastCompWordOffset);
			wordList.addLast(w);
			this.resetAll();
			
			enumeration = floatingSil.elements();
			floatingSil = new Vector();
			while (enumeration.hasMoreElements())
				scanSyllable((Token)enumeration.nextElement());
		}
		
		if (silActual!=null)
		{
			wordList.addLast(new Word(wordActual, "[incomplete word]", wordActualOffset));
			this.resetAll();
		}
	}
	
	private static String concatWithSpace(String s1, String s2)
	{
		if (s1==null || s1.equals(""))
			return s2;
		else
			return s1 + ' ' + s2;
	}
	
	private static String withOutDec(String sil)
	{
		boolean isDeclined =false;
		int len = sil.length(), apos;
		
		if (len<3) return null;
		
		char lastCar = Character.toLowerCase(sil.charAt(len-1));
		if ((lastCar == 's' || lastCar == 'r') && Manipulate.isVowel(sil.charAt(len-2)))
		{
			isDeclined=true;
			sil = sil.substring(0, len-1);
		}
		else
		{
			apos = sil.lastIndexOf('\'');
			if (apos>0 && apos < len-1 && Manipulate.isVowel(sil.charAt(apos-1)) && sil.charAt(apos+1)!='u')
			{
				isDeclined=true;
				sil = sil.substring(0, apos);		        
			}
			/* if ((lastCar == 'i' || lastCar == 'o') && sil.charAt(len-2)=='\'')
			 {
			 isDeclined=true;
			 sil = sil.substring(0, len-2);
			 }*/
		}
		
		if (!isDeclined) return null;
		return sil;
	}
	
	public void scanBody(String in)
	{
		boolean hayMasLineas=true;
		
		if (in.equals("")) finishUp();
		else
		{
			int init = 0, fin;
			String linea;
			
			while (hayMasLineas)
			{
				fin = in.indexOf("\n",init);
				if (fin<0)
				{
					linea = in.substring(init).trim();
					hayMasLineas=false;
				}
				else
					linea = in.substring(init, fin).trim();
				
				if (linea.equals(""))
				{
					finishUp();
					wordList.addLast(new PunctuationMark('\n',init));
				}
				else
					scanLine(linea, init);
				
				init = fin+1;
			}
		}
	}
	
	public void scanLine(String linea, int adjust)
	{
		int init = 0, fin;
		char ch;
		Token silToken;
		boolean doNotFinishUp;
		
		if (linea.equals(""))
		{
			finishUp();
			wordList.addLast(new PunctuationMark('\n',init+adjust));
			return;
		}
		
		outAHere:
			while(true)
			{
				doNotFinishUp=true;
				
				// Make init skip all punctuation marks
				while (true)
				{
					if (init>=linea.length())
						break outAHere;
					ch = linea.charAt(init);
					if (Manipulate.isPunctuationMark(ch))
					{
						if (doNotFinishUp)
						{
							finishUp();
							doNotFinishUp=false;
						}
						wordList.addLast(new PunctuationMark(ch,init+adjust));
					}
					else if (!Manipulate.isEndOfSyllableMark(ch))
						break;
					
					init++;
				}
				
				doNotFinishUp = true;
				
				/* move fin to the end of the next syllable. If finishing
				 up is necessary it is done after scanSyllable
				 */
				
				fin = init+1;
				while (fin < linea.length())
				{
					ch = linea.charAt(fin);
					if (Manipulate.isPunctuationMark(ch))
					{
						doNotFinishUp = false;
						break;
					}
					else if (Manipulate.isEndOfSyllableMark(ch))
					{
						break;
					}
					else
					{
						fin++;
						if (fin>=linea.length())
							break;
					}
				}
				
				silToken = new Token(linea.substring(init, fin),init+adjust);
				scanSyllable(silToken);
				
				if (!doNotFinishUp)
				{
					finishUp();
					wordList.addLast(new PunctuationMark(ch,fin+adjust));
				}
				init = fin+1;
			}
	}
	
	/** Looks for .dic file, and returns the dictionary descriptions.
	 Also updates the definitionTags in the Definitions class.
	 */
	public String[] getDictionaryDescriptions()
	{
		return FileSyllableListTree.getDictionaryDescriptions(archivo);
	}
	
	public void destroy()
	{
		FileSyllableListTree.closeFiles();
	}
	
}