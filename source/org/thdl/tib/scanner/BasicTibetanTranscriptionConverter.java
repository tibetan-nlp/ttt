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

import org.thdl.tib.text.InvalidTransliterationException;
import org.thdl.tib.text.TibTextUtils;
import org.thdl.tib.text.TibetanDocument;
import org.thdl.tib.text.reverter.Converter;
import org.thdl.tib.text.ttt.EwtsToUnicodeForXslt;
import org.thdl.tib.input.*;
import org.thdl.util.*;
import java.net.*;
import java.io.*;

/**
 * Wrap-up class for the various converters that the Translation Tool needs.
 * All conversions are done by static methods meant to be as straight-forward
 * and simple as possible not caring about error or warning messages.
 * 
 * @author Andres Montano
 *
 */
public class BasicTibetanTranscriptionConverter implements FontConverterConstants
{
	private static BufferedReader in;
	private static PrintWriter out;

	//private static int conversionType=0;
	private static final int ACIP_TO_WYLIE=1;
	private static final int WYLIE_TO_ACIP=2;
	private static final int UNICODE_TO_WYLIE=3;
	private static final int WYLIE_TO_UNICODE=4;
	private static final int TIBETAN_UNICODE_RANGE[] = {3840, 4095};
	
	/** Converts from the Acip transliteration scheme to EWTS.*/
	public static String acipToWylie(String acip)
	{
    	TibetanDocument tibDoc = new TibetanDocument();
    	try
    	{
    		TibTextUtils.insertTibetanMachineWebForTranslit(false, acip, tibDoc, 0, false);
    	}
    	catch (InvalidTransliterationException e)
    	{
    		return null;
    	}
    	return tibDoc.getWylie(new boolean[] { false });
		
		/* char caract[], ch, chP, chN;
		String nuevaLinea;
		int i, len;
		boolean open;
		
		caract = acip.toCharArray();
		len = acip.length();
		for (i=0; i<len; i++)
		{
			if (Character.isLowerCase(caract[i]))
				caract[i] = Character.toUpperCase(caract[i]);
			else if (Character.isUpperCase(caract[i]))
				caract[i] = Character.toLowerCase(caract[i]);
		}
		nuevaLinea = new String(caract);
		
		/* ahora hacer los cambios de Michael Roach ts -> tsh, tz -> ts, v -> w,
		TH -> Th, kSH, kaSH -> k+Sh, SH -> Sh, : -> H, dh -> d+h, gh -> g+h, bh -> b+h, dzh -> dz+h,
	    aa -> a, a'a -> A, ai->i, aee ->ai, au->u, aoo->au, ae->e,
		ao->o, ee->ai, oo->au, 'I->-I I->-i,  a'i->I, a'u->U, a'e->E, a'o->O,
		a'i->I, a'u->U, a'e->E, a'o->O, ,->/, # -> @##, * -> @#, \ -> ?, ` -> !,
		/-/ -> (-), ga-y -> g.y, g-y -> g.y, na-y -> n+y */
		
		/* nuevaLinea = Manipulate.replace(nuevaLinea, "ts", "tq");
		nuevaLinea = Manipulate.replace(nuevaLinea, "tz", "ts");
		nuevaLinea = Manipulate.replace(nuevaLinea, "tq", "tsh");
		nuevaLinea = Manipulate.replace(nuevaLinea, "v", "w");
		nuevaLinea = Manipulate.replace(nuevaLinea, "TH", "Th");
		nuevaLinea = Manipulate.replace(nuevaLinea, "kSH", "k+Sh");
		nuevaLinea = Manipulate.replace(nuevaLinea, "kaSH", "k+Sh");
		nuevaLinea = Manipulate.replace(nuevaLinea, "SH", "Sh");
		nuevaLinea = Manipulate.replace(nuevaLinea, ":", "H");
		nuevaLinea = Manipulate.replace(nuevaLinea, "NH", "NaH");
		nuevaLinea = Manipulate.replace(nuevaLinea, "dh", "d+h");
		nuevaLinea = Manipulate.replace(nuevaLinea, "gh", "g+h");
		nuevaLinea = Manipulate.replace(nuevaLinea, "bh", "b+h");
		nuevaLinea = Manipulate.replace(nuevaLinea, "dzh", "dz+h");
		nuevaLinea = Manipulate.replace(nuevaLinea, "aa", "a");
		nuevaLinea = Manipulate.replace(nuevaLinea, "ai", "i");
		nuevaLinea = Manipulate.replace(nuevaLinea, "aee", "ai");
		nuevaLinea = Manipulate.replace(nuevaLinea, "au", "u");
		nuevaLinea = Manipulate.replace(nuevaLinea, "aoo", "au");
		nuevaLinea = Manipulate.replace(nuevaLinea, "ae", "e");
		nuevaLinea = Manipulate.replace(nuevaLinea, "ao", "o");
		nuevaLinea = Manipulate.replace(nuevaLinea, "ee", "ai");
		nuevaLinea = Manipulate.replace(nuevaLinea, "oo", "au");
		nuevaLinea = Manipulate.replace(nuevaLinea, "\'I", "\'q");
		nuevaLinea = Manipulate.replace(nuevaLinea, "I", "-i");
		nuevaLinea = Manipulate.replace(nuevaLinea, "\'q", "-I");
		nuevaLinea = Manipulate.replace(nuevaLinea, "\\", "?");
		nuevaLinea = Manipulate.replace(nuevaLinea, "`", "!");
		nuevaLinea = Manipulate.replace(nuevaLinea, "ga-y", "g.y");
		nuevaLinea = Manipulate.replace(nuevaLinea, "g-y", "g.y");
		nuevaLinea = Manipulate.replace(nuevaLinea, "na-y", "n+y");

		len = nuevaLinea.length();
		for (i=0; i<len; i++)
		{
		    ch = nuevaLinea.charAt(i);
		    switch(ch)
		    {
		        case '#':
		          nuevaLinea = nuevaLinea.substring(0,i) + "@##" + nuevaLinea.substring(i+1);
		          i+=3;
		          len+=2;
		        break;
		        case '*':
		          nuevaLinea = nuevaLinea.substring(0,i) + "@#" + nuevaLinea.substring(i+1);
		          i+=2;
		          len++;
		        break;
		        case '\'':
		          if (i>0 && i<len-1)
		          {
		            chP = nuevaLinea.charAt(i-1);
		            chN = nuevaLinea.charAt(i+1);
		            if (Manipulate.isVowel(chN))
		            {
		                if (Character.isLetter(chP) && !Manipulate.isVowel(chP))
		                {
		                    nuevaLinea = nuevaLinea.substring(0, i) + Character.toUpperCase(chN) + nuevaLinea.substring(i+2);
		                    len--;
		                }
		                else if (chP=='a' && (i==1 || i>1 && !Character.isLetter(nuevaLinea.charAt(i-2)) || chN == 'a' && (i+2==len || !Character.isLetter(nuevaLinea.charAt(i+2)))))
		                {
		                    nuevaLinea = nuevaLinea.substring(0,i-1) + Character.toUpperCase(chN) + nuevaLinea.substring(i+2);
		                    len-=2;
		                }
		            }
		          }
		    }
		}
		
		open = false;
		for (i=0; i<len; i++)
		{
		    ch = nuevaLinea.charAt(i);
		    if (ch=='/')
		    {
		        if (open)
		        {
		          nuevaLinea = nuevaLinea.substring(0, i) + ")" + nuevaLinea.substring(i+1);
		          open = false;		            
		        }

		        else
		        {
		          nuevaLinea = nuevaLinea.substring(0, i) + "(" + nuevaLinea.substring(i+1);
		          open = true;
		        }
		    }
		}
		nuevaLinea = Manipulate.replace(nuevaLinea, ",", "/");
		
		return nuevaLinea; */
	}
	
	/** Converts from EWTS to the ACIP transliteration scheme. */
	public static String wylieToAcip(String wylie)
	{
		TibetanDocument tibDoc = new TibetanDocument();
    	try
    	{
    		TibTextUtils.insertTibetanMachineWebForTranslit(true, wylie, tibDoc, 0, false);
    	}
    	catch (InvalidTransliterationException e)
    	{
    		return null;
    	}
    	return tibDoc.getACIP(new boolean[] { false });
    	
		/* DLC FIXME: for unknown things, return null. */
		/* if (wylie.equals("@##")) return "#";
		if (wylie.equals("@#")) return "*";
		if (wylie.equals("!")) return "`";
		if (wylie.equals("b+h")) return "BH";
		if (wylie.equals("d+h")) return "DH";
		if (wylie.equals("X")) return null;
                if (wylie.equals("iA")) return null;
                if (wylie.equals("ai")) return "EE";
                if (wylie.equals("au")) return "OO";
                if (wylie.equals("$")) return null;
		if (wylie.startsWith("@") || wylie.startsWith("#"))
			return null; // we can't convert this in isolation!  We need context.
		char []caract;
		int i, j, len;
		String nuevaPalabra;
		
		caract = wylie.toCharArray();
		len = wylie.length();
		for (j=0; j<len; j++)
		{
			i = j;
			//ciclo:
			while(true) // para manejar excepciones; que honda!
			{
			switch(caract[i])
			{
			case 'A': 
			if (i>0)
			{
			i--;
			break;
			}
			default:
			if (Character.isLowerCase(caract[i]))
				caract[i] = Character.toUpperCase(caract[i]);
			else if (Character.isUpperCase(caract[i]))
				caract[i] = Character.toLowerCase(caract[i]);
			//						break ciclo;
			}
			}
		}
		nuevaPalabra = new String(caract);
		//			nuevaPalabra = palabra.toUpperCase();
		
		// ahora hacer los cambios de Michael Roach
		
		nuevaPalabra = Manipulate.replace(nuevaPalabra, "TSH", "TQQ");
		nuevaPalabra = Manipulate.replace(nuevaPalabra, "TS", "TZ");
		nuevaPalabra = Manipulate.replace(nuevaPalabra, "TQQ", "TS");
		nuevaPalabra = Manipulate.replace(nuevaPalabra, "a", "'A");
		nuevaPalabra = Manipulate.replace(nuevaPalabra, "i", "'I");
		nuevaPalabra = Manipulate.replace(nuevaPalabra, "u", "'U");
		nuevaPalabra = Manipulate.replace(nuevaPalabra, "-I", "i");
		nuevaPalabra = Manipulate.replace(nuevaPalabra, "/", ",");
		nuevaPalabra = Manipulate.replace(nuevaPalabra, "_", " ");
		nuevaPalabra = Manipulate.replace(nuevaPalabra, "|", ";");
		nuevaPalabra = Manipulate.fixWazur(nuevaPalabra);
		return nuevaPalabra;*/
	}
	
	private static int getTibetanUnicodeStart(String unicode, int pos)
	{
		for(; pos < unicode.length(); pos++ ) if(unicode.codePointAt(pos)>=TIBETAN_UNICODE_RANGE[0] && unicode.codePointAt(pos)<=TIBETAN_UNICODE_RANGE[1]) return pos;
		return -1;
	}
	
	private static int getTibetanUnicodeEnd(String unicode, int pos)
	{
		for(; pos < unicode.length(); pos++ ) if(unicode.codePointAt(pos)<TIBETAN_UNICODE_RANGE[0] || unicode.codePointAt(pos)>TIBETAN_UNICODE_RANGE[1]) return pos;
		return pos;
	}
    
	/** Converts Tibetan Unicode to EWTS. */
    public static String unicodeToWylie(String unicode)
    {
    	
    	String machineWylie, startString, tibetanString, endString;
    	TibetanDocument tibDoc;
    	StringBuffer errors;
    	int posStart=0, posEnd;
    	while((posStart = getTibetanUnicodeStart(unicode, posStart))>=0)
    	{
    		posEnd = getTibetanUnicodeEnd(unicode, posStart+1);
    		startString = unicode.substring(0, posStart);
    		tibetanString = unicode.substring(posStart, posEnd);
    		endString = unicode.substring(posEnd);
    		
        	tibDoc = new TibetanDocument();
        	errors = new StringBuffer();
        	machineWylie = Converter.convertToEwtsForComputers(tibetanString, errors);
        	try
        	{
        		TibTextUtils.insertTibetanMachineWebForTranslit(true, machineWylie, tibDoc, 0, false);
        	}
        	catch (InvalidTransliterationException e)
        	{
        		return null;
        	}
        	unicode = startString + tibDoc.getWylie(new boolean[] { false }) + endString;   	
    	}
    	return unicode;
    }
    
    /** Converts EWTS to Tibetan Unicode. */
    public static String wylieToUnicode(String wylie)
    {
    	return EwtsToUnicodeForXslt.convertEwtsTo(wylie);
    }
    
    /** Converts EWTS to Tibetan Unicode represented in NCR. */
    public static String wylieToHTMLUnicode(String wylie)
    {
    	return Manipulate.UnicodeString2NCR(wylieToUnicode(wylie));
    }
    
	/** Converts Tibetan Unicode represented in NCR to EWTS. */
    public static String HTMLUnicodeToWylie(String unicode)
    {
    	return unicodeToWylie(Manipulate.NCR2UnicodeString(unicode));
    }
    
    public static void printSyntax()
    {
    	System.out.println("Syntax: BasicTibetanTranscriptionConverter [-format format-of-files | [-fi format-of-input-file] [-fo format-of-output-file]] [-it acip | wylie | UTF16] [-ot acip | wylie | UTF16] input-file [output-file]");
    }
	
	public BasicTibetanTranscriptionConverter(BufferedReader in, PrintWriter out)
	{
		BasicTibetanTranscriptionConverter.in = in;
		BasicTibetanTranscriptionConverter.out = out;
	}
	
	
	public static void main (String[] args) throws Exception
	{
		PrintWriter out;
		BufferedReader in=null;
		int argNum = args.length, currentArg=0;
		String option;
		String formatIn = null, formatOut = null, inputTransSyst="wylie", outputTransSyst="wylie";
		boolean file = false;
		int conversionType=0;
				
		if (argNum<=currentArg)
		{
		    printSyntax();
		    return;
		}
		
		while (args[currentArg].charAt(0)=='-')
		{
		    option = args[currentArg++].substring(1);
		    if (option.equals("format"))
		    {
		        formatIn = formatOut = args[currentArg];
		    } else if (option.equals("fi"))
		    {
		        formatIn = args[currentArg];
		    } else if (option.equals("fo"))
		    {
		        formatOut = args[currentArg];
		    } else if (option.equals("it"))
			{
				inputTransSyst = args[currentArg];
			} else if (option.equals("ot"))
			{
				outputTransSyst = args[currentArg];
			}
		    currentArg++;
		}
		
		if (!inputTransSyst.equals(outputTransSyst))
		{
			if (inputTransSyst.equals("wylie"))
			{
				if (outputTransSyst.equals("acip")) conversionType = WYLIE_TO_ACIP;
				else conversionType = WYLIE_TO_UNICODE; 
			}
			else if (inputTransSyst.equals("acip")) conversionType = ACIP_TO_WYLIE;
			else conversionType = UNICODE_TO_WYLIE;
		}
		
		switch (args.length-currentArg)
		{
		case 0: 
		    if (formatIn != null)
		    {
		        System.out.println("Syntax error: input file name expected.");
		        return;
		    }
            out = new PrintWriter(System.out);
            in = new BufferedReader(new InputStreamReader(System.in));
            break;
		case 1: 
		    if (formatOut != null)
		    {
		        System.out.println("Syntax error: output file name expected.");
		        return;
		    }
		    out = new PrintWriter(System.out);
		    file = true;
		    break;
		default:
		    if (formatOut != null)
                out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[currentArg + 1]), formatOut));
            else
                out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[currentArg + 1])));
		    file = true;		
		}
   		if (file)
		{
		    in = getBufferedReader (args[currentArg], formatIn);
		}
		
		new BasicTibetanTranscriptionConverter(in, out).run(conversionType);
	}
	
	/**
	 *  This method was added for compatibility's sake with the FontConverterConstants interfase.
	 *  
	 * @param conversionType
	 * @throws IOException
	 */
	public void run(String conversionType) throws IOException
	{
		int conversionTypeInt=0;
		if (conversionType==ACIP_TO_WYLIE_TEXT) conversionTypeInt = ACIP_TO_WYLIE;
		if (conversionType==WYLIE_TO_ACIP_TEXT) conversionTypeInt = WYLIE_TO_ACIP;
		if (conversionType==UNI_TO_WYLIE_TEXT) conversionTypeInt = UNICODE_TO_WYLIE;
		if (conversionType==WYLIE_TO_UNI_TEXT) conversionTypeInt = WYLIE_TO_UNICODE;
		run(conversionTypeInt);
	}
	
	public void run(int conversionType) throws IOException
	{
		String linea, result;
		
		while ((linea=in.readLine())!=null)
		{
			switch(conversionType)
			{
			case ACIP_TO_WYLIE:
				result = acipToWylie(linea); 
			break;
			case WYLIE_TO_ACIP: 
				result = wylieToAcip(linea);
			break;			
			case UNICODE_TO_WYLIE: 
				result = unicodeToWylie(linea);
			break;
			case WYLIE_TO_UNICODE: 
				result = wylieToUnicode(linea);
			break;
			default: result = linea;
			}
			if (result!=null)
			{
				out.println(result);
				out.flush();
			}
		}
	}
	
	public static BufferedReader getBufferedReader(String s, String format) throws Exception
	{
	    InputStream is;
	    
    	if (s.indexOf("http://") >= 0) 
			is = new BufferedInputStream((new URL(s)).openStream());
		else
		    is = new FileInputStream(s);
		    
		if (format==null)
		    return new BufferedReader(new InputStreamReader(is));
		else
		    return new BufferedReader(new InputStreamReader(is, format));			
		
	}
}