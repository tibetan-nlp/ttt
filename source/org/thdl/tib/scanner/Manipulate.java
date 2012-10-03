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

/** Miscelaneous static methods for the manipulation of Tibetan text.

    @author Andr&eacute;s Montano Pellegrini
 */

public class Manipulate
{
	private static String endOfParagraphMarks = "/;|!:^@#$%=,";
	private static String bracketMarks = "<>(){}[]";
	private static String endOfSyllableMarks = " _\t\u00A0";
	private static String allStopMarkers = endOfSyllableMarks + endOfParagraphMarks + bracketMarks;
	private static final int TIBETAN_UNICODE_RANGE[] = {3840, 4095};
	private static String JSON_ESCAPABLES = "\"\\/";

	/* public static String[] parseFields (String s, char delimiter)
	{
	    int pos;
	    String field;
	    SimplifiedLinkedList ll = new SimplifiedLinkedList();

	    while ((pos = s.indexOf(delimiter))>=0)
	    {
	        field = s.substring(0, pos).trim();
	        ll.addLast(field);
	        s = s.substring(pos+1);
	    }

	    ll.addLast(s.trim());
	    return ll.toStringArray();
	}*/

	public static int indexOfAnyChar(String str, String chars)
	{
		int i;
		for (i=0; i<str.length(); i++)
		{
			if (chars.indexOf(str.charAt(i))>=0)
				return i;
		}

		return -1;
	}

	public static int indexOfExtendedEndOfSyllableMark(String word)
	{
		return indexOfAnyChar(word, allStopMarkers);
	}

	public static int indexOfBracketMarks(String word)
	{
		return indexOfAnyChar(word, bracketMarks);
	}	

	public static boolean isPunctuationMark(int ch)
	{
		return endOfParagraphMarks.indexOf(ch)>=0 || bracketMarks.indexOf(ch)>=0;
	}

	public static boolean isEndOfParagraphMark(int ch)
	{
		return endOfParagraphMarks.indexOf(ch)>=0;
	}

	public static boolean isEndOfSyllableMark(int ch)
	{
		return endOfSyllableMarks.indexOf(ch)>=0;
	}

	public static boolean isMeaningful(String s)
	{
		for (int i=0; i<s.length(); i++) 
			if (Character.isLetterOrDigit(s.charAt(i))) return true;

		return false;
	}

	public static String replace(String linea, String origSub, String newSub)
	{
		int pos, lenOrig = origSub.length();
		while ((pos = linea.indexOf(origSub))!=-1)
		{
			linea = linea.substring(0, pos).concat(newSub).concat(linea.substring(pos+lenOrig));
		}
		return linea;
	}

	public static String deleteSubstring (String string, int pos, int posEnd)
	{
		if (pos<0) return string;

		if (pos==0)
		{
			return string.substring(posEnd).trim();
		}
		else
		{
			if (posEnd<string.length())
				return string.substring(0, pos).concat(string.substring(posEnd)).trim();
			else
				return string.substring(0, pos).trim();
		}	    
	}

	public static String replace(String string, int pos, int posEnd, String newSub)
	{
		if (pos<0) return string;

		if (pos==0)
		{
			return newSub.concat(string.substring(posEnd)).trim();
		}
		else
		{
			if (posEnd<string.length())
				return string.substring(0, pos).concat(newSub).concat(string.substring(posEnd)).trim();
			else
				return string.substring(0, pos).concat(newSub).trim();
		}	    
	}

	public static String deleteSubstring (String string, String sub)
	{
		int pos = string.indexOf(sub), posEnd = pos + sub.length();
		return deleteSubstring(string, pos, posEnd);
	}

	public static String[] addString(String array[], String s, int n)
	{
		int i;
		String newArray[] = new String[array.length+1];

		for (i=0; i<n; i++)
			newArray[i] = array[i];

		newArray[n] = s;

		for (i=n+1; i<newArray.length; i++)
			newArray[i] = array[i-1];

		return newArray;
	}

	public static String[] deleteString(String array[], int n)
	{
		int i;

		String newArray[] = new String[array.length-1];

		for (i=0; i<n; i++)
			newArray[i] = array[i];

		for (i=n; i<newArray.length; i++)
			newArray[i] = array[i+1];

		return newArray;	    
	}

	public static boolean isVowel (char ch)
	{
		ch = Character.toLowerCase(ch);
		return ch=='a' || ch=='e' || ch=='i' || ch=='o' || ch=='u';
	}	

	/** If more than half of the first letters among the first are 10 characters
	    are uppercase assume its acip */
	    public static boolean guessIfAcip(String line)
	    {
	    	char ch;
	    	int letters=0, upperCase=0, i, n;
	    	n = line.length();
	    	if (n>10) n = 10;
	    	for (i=0; i<n; i++)
	    	{
	    		ch = line.charAt(i);
	    		if (Character.isLetter(ch))
	    		{
	    			letters++;
	    			if (Character.isUpperCase(ch)) upperCase++;
	    		}
	    	}
	    	if (letters==0 || upperCase==0) return false;
	    	else return (letters / upperCase < 2);
	    }

	    public static boolean isTibetanUnicodeCharacter(char ch)
	    {
	    	return ch>=0xF00 && ch<=0xFFF;
	    }
	    		
		public static boolean isTibetanUnicodeLetter(char ch)
		{
			
			return ch>=0xF40 && ch<=0xFBC || ch>=0xF00 && ch<=0xF03;
		}
		
		public static boolean isTibetanUnicodeDigit(char ch)
		{
			
			return ch>=0xF20 && ch<=0xF33;
		}


	    public static boolean guessIfUnicode(String line)
	    {
	    	char ch;
	    	int unicode=0, i, n;
	    	n = line.length();
	    	if (n>10) n = 10;
	    	for (i=0; i<n; i++)
	    	{
	    		ch = line.charAt(i);
	    		if (isTibetanUnicodeCharacter(ch)) unicode++;
	    	}
	    	if (n==0 || unicode==0) return false;
	    	else return (n / unicode < 2);
	    }

	    public static String fixWazur(String linea)
	    {
	    	int i;

	    	for (i=1; i<linea.length(); i++)
	    	{
	    		if (linea.charAt(i)=='W')
	    		{
	    			if (Character.isLetter(linea.charAt(i-1)))
	    				linea = linea.substring(0,i) + 'V' + linea.substring(i+1);					
	    		}
	    	}
	    	return linea;
	    }

	    /** Returns the base letter of a syllable. Does not include the vowel!
	Ignoring cases for now. */
	    public static String getBaseLetter (String sil)
	    {
	    	sil = sil.toLowerCase();

	    	int i=0;
	    	char ch, ch2;

	    	while (!isVowel(sil.charAt(i)))
	    	{
	    		i++;
	    		if (i>=sil.length()) return null;
	    	}
	    	if (i==0) return "";

	    	i--;
	    	if (i==-1) return "";

	    	if (sil.charAt(i)=='-') i--;
	    	if (i>0 && sil.charAt(i)=='w') i--;	    
	    	ch = sil.charAt(i);

	    	// check to see if it is a subscript (y, r, l, w)
	    	if (i>0)
	    	{
	    		switch (ch)
	    		{
	    		case 'r': case 'l': i--;
	    		break;
	    		case 'y':
	    			ch2 = sil.charAt(i-1);
	    			switch (ch2)
	    			{
	    			case '.': return "y";
	    			case 'n': return "ny";
	    			default: i--;
	    			}
	    		}
	    	}
	    	if (sil.charAt(i)=='+') i--;
	    	if (i==0) return sil.substring(i,i+1);
	    	ch = sil.charAt(i);
	    	ch2 = sil.charAt(i-1);

	    	switch(ch)
	    	{
	    	case 'h':
	    		switch (ch2)
	    		{
	    		case 'k': case 'c': case 't': case 'p': case 'z':
	    			return sil.substring(i-1,i+1);
	    		case '+':
	    			return sil.substring(i-2, i-1);
	    		case 's':
	    			if (i-2>=0 && sil.charAt(i-2)=='t') return "tsh";
	    			else return "sh";
	    		default: return "h";
	    		}
	    	case 's':
	    		if (ch2=='t') return "ts";
	    		else return "s";
	    	case 'g':
	    		if (ch2=='n') return "ng";
	    		else return "g";
	    	case 'z':
	    		if (ch2=='d') return "dz";
	    		else return "z";
	    	}
	    	return sil.substring(i,i+1);
	    }

	    public static String deleteQuotes(String s)
	    {
	    	int length = s.length(), pos;
	    	if (length>2)
	    	{
	    		if ((s.charAt(0)=='\"') && (s.charAt(length-1)=='\"'))
	    			s = s.substring(1,length-1);

	    		do
	    		{
	    			pos = s.indexOf("\"\"");
	    			if (pos<0) break;
	    			s = Manipulate.deleteSubstring(s, pos, pos+1);
	    		} while (true);
	    	}

	    	return s;
	    }	



	    /** Syntax: java Manipulate [word-file] < source-dic-entries > dest-dic-entries

    Takes the output of ConsoleScannerFilter
	(in RY format), converts the Wylie to Acip
	and displays the result in csv format.	 
	arch-palabras es usado solo cuando deseamos las palabras cambiadas
	a otro archivo.


	public static void main (String[] args) throws Exception												   
	{
		String linea, palabra, definicion, nuevaPalabra;
		int marker;
		PrintWriter psPalabras = null;

		BufferedReader keyb = new BufferedReader(new InputStreamReader(System.in));

		if (args.length==1)
			psPalabras = new PrintWriter(new FileOutputStream(args[0]));		

		while ((linea=keyb.readLine())!=null)
		{
			if (linea.trim().equals("")) continue;
			marker = linea.indexOf('-');
			if (marker<0) // linea tiene error
			{
				palabra = linea;
				definicion = "";
			}
			else
			{
				palabra = linea.substring(0, marker).trim();
				definicion = linea.substring(marker+1).trim();
			}

			nuevaPalabra = wylieToAcip(palabra);

			if (psPalabras!=null)
				psPalabras.println(nuevaPalabra);
			else System.out.print(nuevaPalabra + '\t');
			if (definicion.equals(""))
				System.out.println(palabra);
			else
				System.out.println(palabra + '\t' + definicion);
		}
      if (psPalabras!=null) psPalabras.flush();
	}*/

	    /** From http://www.i18nfaq.com/2005/07/how-do-i-convert-ncr-format-to-java.html */
	    public static String NCR2UnicodeString(String str)
	    {
	    	StringBuffer ostr = new StringBuffer();
	    	int i1=0;
	    	int i2=0;

	    	while(i2<str.length())
	    	{
	    		i1 = str.indexOf("&#",i2);
	    		if (i1 == -1 ) {
	    			ostr.append(str.substring(i2, str.length()));
	    			break ;
	    		}
	    		ostr.append(str.substring(i2, i1));
	    		i2 = str.indexOf(";", i1);
	    		if (i2 == -1 ) {
	    			ostr.append(str.substring(i1, str.length()));
	    			break ;
	    		}

	    		String tok = str.substring(i1+2, i2);
	    		try {
	    			int radix = 10 ;
	    			if (tok.trim().charAt(0) == 'x') {
	    				radix = 16 ;
	    				tok = tok.substring(1,tok.length());
	    			}
	    			ostr.append((char) Integer.parseInt(tok, radix));
	    		} catch (NumberFormatException exp) {
	    			ostr.append('?') ;
	    		}
	    		i2++ ;
	    	}
	    	return new String(ostr) ;
	    }

	    public static String UnicodeString2NCR(String str)
	    {
	    	StringBuffer ncr = new StringBuffer();
	    	int i;
	    	for (i=0; i<str.length(); i++)
	    	{
	    		ncr.append("&#" + Integer.toString(str.charAt(i)) + ";");
	    	}
	    	return ncr.toString();
	    }

	    public static String toJSON(String str)
	    {
	    	int pos, i, len;
	    	for (i=0; i<str.length(); i++)
	    	{
	    		pos = JSON_ESCAPABLES.indexOf(str.charAt(i));
	    		if (pos>=0)
	    		{
	    			len = str.length();
	    			str = str.substring(0, i) + "\\" + str.substring(i, len);
	    			i++;
	    		}
	    	}
	    	str = replace(str, "\b", "\\b");
	    	str = replace(str, "\f", "\\f");
	    	str = replace(str, "\n", "\\n");
	    	str = replace(str, "\r", "\\r");
	    	str = replace(str, "\t", "\\t");
	    	return str;
	    }

	    public static boolean containsLetters(String str)
	    {
	    	int i=0;
	    	if (str==null) return false;
	    	while (i<str.length()) if (Character.isLetter(str.charAt(i++))) return true;
	    	return false;
	    }
	    
	        public static String unescape(String s) {
    	int i=0,len=s.length();
    	char c;
    	StringBuffer sb = new StringBuffer(len);
    	while (i<len) {
    		c = s.charAt(i++);
    		if (c=='\\') {
    			if (i<len) {
    				c = s.charAt(i++);
    				if (c=='u') {
    					c = (char) Integer.parseInt(s.substring(i,i+4),16);
    					i += 4;
    				} // add other cases here as desired...
    			}} // fall through: \ escapes itself, quotes any character but u
    		sb.append(c);
    	}
    	return sb.toString();
    }
    
	public static int getTibetanUnicodeStart(String unicode, int pos)
	{
		for(; pos < unicode.length(); pos++ ) if(unicode.codePointAt(pos)>=TIBETAN_UNICODE_RANGE[0] && unicode.codePointAt(pos)<=TIBETAN_UNICODE_RANGE[1]) return pos;
		return -1;
	}
	
	public static int getTibetanUnicodeEnd(String unicode, int pos)
	{
		for(; pos < unicode.length(); pos++ ) if(unicode.codePointAt(pos)<TIBETAN_UNICODE_RANGE[0] || unicode.codePointAt(pos)>TIBETAN_UNICODE_RANGE[1]) return pos;
		return pos;
	}
}
