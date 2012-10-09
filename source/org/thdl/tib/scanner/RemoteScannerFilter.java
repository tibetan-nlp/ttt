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
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/** Running on the server, receives the tibetan text from applet/applications running on
    the client and sends them the words with their definitions through the Internet.
    Requests are made through {@link RemoteTibetanScanner}.

    @author Andr&eacute;s Montano Pellegrini
    @see RemoteTibetanScanner
 */
public class RemoteScannerFilter extends GenericServlet
{
	private TibetanScanner scanner;
	private BitDictionarySource ds;
	private ScannerLogger sl;
	private static final int INTERNAL = 1;
	private static final int JSON = 2;

	public RemoteScannerFilter()
	{
		System.setProperty("java.awt.headless","true");
		ResourceBundle rb = ResourceBundle.getBundle("dictionary");
		sl = new ScannerLogger();

		try
		{
			scanner = new LocalTibetanScanner(rb.getString("onlinescannerfilter.dict-file-name"),false);
		}
		catch (Exception e)
		{
			sl.writeLog("1\t2");
			sl.writeException(e);
		}
		scanner.getDictionaryDescriptions();
		ds = scanner.getDictionarySource();
		sl.writeLog("Creation\t2");
	}

	public void service(ServletRequest req, ServletResponse res) //throws ServletException, IOException
	{
		BufferedReader br;
		int format, i, j, k;
		try
		{
		  req.setCharacterEncoding("UTF8");
		  res.setCharacterEncoding("UTF8");
		}
		catch(Exception e)
		{
			// do nothing
		}
		String linea = null, dicts = req.getParameter("dicts"), dicDescrip[], jwf = req.getParameter("jwf"), tag;
		Definitions defs;
		ByteDictionarySource dict_source;
		if (jwf!=null) format = JSON;
		else format = INTERNAL;
		switch (format)
		{
		case INTERNAL:
		  res.setContentType ("text/plain");
		  break;
		case JSON:
		  res.setContentType ("text/x-json");
		}
		sl.setUserIP(req.getRemoteAddr());

		Word word = null, words[] = null;
		PrintWriter out;

		try
		{
			out = res.getWriter();
		}
		catch (Exception e)
		{
			sl.writeLog("1\t2");
			sl.writeException(e);
			return;
		}		


		if (dicts!=null)
		{
			if (dicts.equals("names"))
			{
				sl.writeLog("3\t2");
				dicDescrip = scanner.getDictionaryDescriptions();
				if (dicDescrip==null)
				{
					out.close();
					return;
				}

				for (i=0; i<dicDescrip.length; i++)
				{
					out.println(dicDescrip[i] + "," + DictionarySource.defTags[i]);
				}
				out.close();
				return;
			}
			else
			{
				ds.setDicts(Integer.parseInt(dicts));
			}
		}

		if (format==JSON)
		{
			out.println(jwf + "({\"words\":{");
		}
		scanner.clearTokens();
		try
		{
			switch (format)
			{
			case INTERNAL:
				br = req.getReader();
				sl.writeLog("4\t2");
				while((linea = br.readLine())!= null)
					scanner.scanLine(linea,0);
				br.close();
			break;
			case JSON:
				linea = req.getParameter("text");
				if (linea!=null)
				{
					linea = Manipulate.NCR2UnicodeString(linea);
					if (Manipulate.guessIfUnicode(linea)) linea = BasicTibetanTranscriptionConverter.unicodeToWylie(linea);
					else if (Manipulate.guessIfAcip(linea)) linea = BasicTibetanTranscriptionConverter.acipToWylie(linea);
					scanner.scanLine(linea,0);
				}
			}
		}
		catch (Exception e)
		{
			if (linea!=null) sl.writeLog("1\t2\t" + linea);
			sl.writeException(e);
		}
		scanner.finishUp();
		try
		{
			words = scanner.getWordArray();
			if (words!=null)
			{
				for (i=0; i<words.length; i++)
				{
					word = words[i];
					linea = word.getDef();
					if (linea == null) continue;
					switch (format)
					{
					case INTERNAL:
						out.println(word.getWylie());
						out.println(linea);
						out.println();
						break;
					case JSON:
						defs = word.getDefs();
						dict_source = (ByteDictionarySource)defs.getDictionarySource();
						if(dict_source==null) out.println("\"" + word.token + "\": [");
						else
						{
							out.println("\"" + BasicTibetanTranscriptionConverter.wylieToHTMLUnicode(word.token) + "\": [");
							k=0;
							for (j=0; j<defs.def.length; j++)
							{
								while (dict_source.isEmpty(k)) k++;
								tag = dict_source.getTag(k);
								k++;
								out.println("\"" + tag + "\",");
								out.print("\"" + Manipulate.toJSON(defs.def[j]) + "\"");
								if (j==defs.def.length-1) out.println();
								else out.println(",");
							}							
						}
						out.print("]");
						if (i<words.length-1) out.println(",");
					}
				}				
			}
			if (format==JSON) out.println("}});");
		}
		catch (Exception e)
		{
			if (word!=null) sl.writeLog("1\t2\t" + word.getWylie());
			sl.writeException(e);
		}
		scanner.clearTokens();
		out.close();
	}

	public void destroy()
	{
		super.destroy();
		sl.setUserIP(null);
		sl.writeLog("5\t2");
		scanner.destroy();
	}
}