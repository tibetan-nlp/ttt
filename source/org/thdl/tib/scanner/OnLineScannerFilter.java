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

import java.io.PrintWriter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thdl.util.ThdlOptions;

/** Interfase to provide access to an on-line dictionary through a form in html;
 Inputs Tibetan text (Roman script only) and displays the
 words (Roman or Tibetan script) with their definitions.
 Runs on the server and is called upon through an HTTP request directly
 by the browser.  Requires no additional software installed on the client.
 
 @author Andr&eacute;s Montano Pellegrini
 */
public class OnLineScannerFilter extends HttpServlet
{
	private final static String propertyFile = "dictionary";
	private final static String dictNameProperty = "onlinescannerfilter.dict-file-name";
	private final static String otherLinksProperty = "onlinescannerfilter.links-to-other-stuff";
	private final static String moreLinksProperty = "onlinescannerfilter.links-to-more-stuff";
	private final static String smallerLinksProperty = "onlinescannerfilter.links-to-smaller-stuff";
	private final static String clearStr = "Clear";
	private final static String buttonStr = "button";
	private final static String scriptStr = "script";
	private final static String tibetanStr = "tibetan";
	
	ResourceBundle rb;
	private TibetanScanner scanner;
	private String dictionaries[];
	private ScannerLogger sl;
	
	public OnLineScannerFilter() //throws Exception
	{
		System.setProperty("java.awt.headless","true");
		rb = ResourceBundle.getBundle(propertyFile);
		sl = new ScannerLogger();
		
		try
		{
			scanner = new LocalTibetanScanner(rb.getString(dictNameProperty), false);
		}
		catch (Exception e)
		{
			sl.writeLog("1\t1");
			sl.writeException(e);
		}
		
		dictionaries = scanner.getDictionaryDescriptions();
		sl.writeLog("2\t1");
	}
	
	synchronized public void doGet(HttpServletRequest request,
			HttpServletResponse response) //throws IOException, ServletException
	{
		String answer, parrafo = null, checkboxName;
		try
		{
		  request.setCharacterEncoding("UTF8");
		  response.setCharacterEncoding("UTF8");
		}
		catch(Exception e)
		{
			// do nothing
		}
		// if this line is included in the constructor, it works on the orion server but not on wyllie!
		ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);
		ThdlOptions.setUserPreference("thdl.rely.on.system.tm.fonts", true);
		
		response.setContentType("text/html");
		PrintWriter out;
		sl.setUserIP(request.getRemoteAddr());
		
		try
		{
			out = response.getWriter();
		}
		catch (Exception e)
		{
			sl.writeLog("1\t1");
			sl.writeException(e);
			return;
		}
		
		BitDictionarySource ds=null;
		boolean checkedDicts[], allUnchecked, wantsTibetan;
		// int percent=100;
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<head>");
		out.println(" <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		out.println(" <title>The Online Tibetan to English Dictionary and Translation Tool</title>");
		out.println(" <link rel=\"stylesheet\" type=\"text/css\" href=\"stylesheets/base.css\"/>");
		out.println(" <script src=\"javascripts/base.js\"></script>");
		out.println(" <meta name=\"keywords\" content=\"tibetan, english, dictionary, jim valby, rangjung yeshe, jeffrey hopkins, tsig mdzod chen mo, online, translation, scanner, parser, buddhism, language, processing, font, dharma, chos, tibet\">");
		out.println(" <meta name=\"Description\" content=\"This Java tool takes Tibetan language passages and divides the passages up into their component phrases and words, and displays corresponding dictionary definitions.\">");
		out.println(" <meta name=\"MSSmartTagsPreventParsing\" content=\"TRUE\">");
		try
		{
			out.println(rb.getString(otherLinksProperty));
		}
		catch (MissingResourceException e)
		{
			// do nothing
		}
		answer = request.getParameter(scriptStr);
		/* script==null || makes default tibetan
		 script!=null && makes default roman
		 */
		wantsTibetan = (answer==null || answer.equals(tibetanStr));
		/*if (wantsTibetan)
		{
			out.println("<style>.tmw {font: 28pt TibetanMachineWeb}");
			out.println(".tmw1 {font: 28pt TibetanMachineWeb1}");
			out.println(".tmw2 {font: 28pt TibetanMachineWeb2}");
			out.println(".tmw3 {font: 28pt TibetanMachineWeb3}");
			out.println(".tmw4 {font: 28pt TibetanMachineWeb4}");
			out.println(".tmw5 {font: 28pt TibetanMachineWeb5}");
			out.println(".tmw6 {font: 28pt TibetanMachineWeb6}");
			out.println(".tmw7 {font: 28pt TibetanMachineWeb7}");
			out.println(".tmw8 {font: 28pt TibetanMachineWeb8}");
			out.println(".tmw9 {font: 28pt TibetanMachineWeb9}");
			out.println("</style>");
		}*/
		out.println("</head>");
		out.println("<body>");
		out.println("<form action=\"org.thdl.tib.scanner.OnLineScannerFilter\" method=POST>");
		out.println("<table border=\"0\" width=\"100%\">");
		out.println("  <tr>");
		out.println("    <td width=\"18%\" align=\"left\"><strong>Display results in:</strong></td>");
		out.println("    <td width=\"41%\" align=\"right\">");
		out.println("      <input type=\"radio\" value=\"" + tibetanStr + "\" ");
		if (wantsTibetan) out.println("checked ");
		out.println("name=\"" + scriptStr + "\">Tibetan script (<a href=\"https://github.com/tibetan-nlp/ttt/tree/master/Fonts/TibetanMachineUni\" target=\"_top\">Tibetan Machine Uni</a> font)</td>");
		out.println("    <td width=\"16%\" align=\"left\">");
		out.println("      <input type=\"radio\" value=\"roman\" ");
		if (!wantsTibetan) out.println("checked ");
		out.println("name=\"" + scriptStr + "\">Roman script</td>");
		out.println("    <td width=\"25%\" align=\"right\">");
		out.println("<a href=\"https://github.com/tibetan-nlp/ttt\" target=\"_top\">Help & Offline Installation</a></td>");
		out.println("  </tr>");
		if (dictionaries!=null)
		{
			String checked, label, selected_lang = request.getParameter("lang"), languages[] = {"all","english","tibetan","sanskrit","custom"};
			out.println("  <tr>");
			out.println("    <td><strong>Search in dictionaries:</strong></td><td colspan=\"3\">");
			for (String lang : languages) {
			    label   = Character.toUpperCase(lang.charAt(0)) + lang.substring(1);
			    checked = selected_lang == null && lang.equals("all") || selected_lang != null && selected_lang.equals(lang) ? " checked" : "";
			    out.println("<input type=\"radio\" name=\"lang\" value=\"" + lang + "\"" + checked + "> " + label);
			}
			out.println("  </td></tr>");
			int i;
			ds = scanner.getDictionarySource();
			ds.reset();
			checkedDicts = new boolean[dictionaries.length];
			allUnchecked=true;
			for (i=0; i<dictionaries.length; i++)
			{
				checkboxName = "dict"+ i;
				checkedDicts[i] = (request.getParameter(checkboxName)!=null);
			}
			allUnchecked=true;
			for (i=0; i<dictionaries.length; i++)
			{
				if(checkedDicts[i])
				{
					allUnchecked=false;
					break;
				}
			}
			if (allUnchecked)
			{
				for (i=0; i<dictionaries.length; i++)
					checkedDicts[i] = true;
			}
			out.print("  <tr><td colspan=\"4\">");
			for (i=0; i<dictionaries.length; i++)
			{
				checkboxName = "dict"+ i;
//				out.print("  <td width=\"" + percent + "%\">");
				out.print("<input type=\"checkbox\" name=\"" + checkboxName +"\" value=\""+ checkboxName +"\"");
				if (checkedDicts[i])
				{
					out.print(" checked");
					ds.add(i);
				}
				if (dictionaries[i]!=null)
					out.print(">" + dictionaries[i] + " (" + DictionarySource.defTags[i] + ")&nbsp;&nbsp;&nbsp;");
				else
					out.print(">" + DictionarySource.defTags[i] + "&nbsp;&nbsp;&nbsp;");
//				out.println(" + "</td>");
			}
			out.println("  </td></tr>");
		}
		// fix for updates
		else ds = BitDictionarySource.getAllDictionaries();
//		out.println("</table>");
//		out.println("</p>");
//		out.println("<table border=\"0\" width=\"100%\">");
		out.println("  <tr>");
		out.println("    <td><strong>Input text:</strong></td>");
		out.println("    <td><input type=\"submit\" name=\"" + buttonStr + "\" value=\"Translate\"> <input type=\"submit\" name=\"" + buttonStr + "\" value=\"" + clearStr + "\"></td>");
		out.println("    <td colspan\"2\">&nbsp;</td");
		out.println("  </tr>");
		out.println("</table>");
		answer = request.getParameter(buttonStr);
		String smallerLinks=null;
		if (answer == null || answer != null && !answer.equals(clearStr))
		{
			parrafo = request.getParameter("parrafo");
		}
		if (parrafo==null)
		{
			try
			{
				smallerLinks = rb.getString(smallerLinksProperty);
			}
			catch (MissingResourceException e)
			{
				// do nothing
			}
		}
		if (smallerLinks!=null)
		{
			out.println("<table width=\"100%\">");
			out.println("<tr>");
			out.println("<td>");
		}
		out.print("<textarea rows=\"5\" name=\"parrafo\" cols=\"40\"");
		if (wantsTibetan) out.print(" class=\"tib\"");
		out.println(">");
		
		// Paragraph should be empty if the user just clicked the clear button
		answer = request.getParameter(buttonStr);
		if (parrafo!=null)
		{
			out.print(parrafo);
		}
		out.println("</textarea>");
		if (smallerLinks!=null)
		{
			out.println("</td>");
			out.println("<td>");
			out.println(smallerLinks);
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
		}
		out.println("</form>");
		if (parrafo != null)
		{
			sl.writeLog("4\t1");
			if (ds!=null && !ds.isEmpty())
			{
				desglosar(parrafo, out, wantsTibetan);
			}
		}
		else sl.writeLog("3\t1");
		out.println(TibetanScanner.copyrightHTML);
		out.println("</body>");
		out.println("</html>");
	}
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
	//throws IOException, ServletException
	{
		doGet(request, response);
	}
	
	synchronized public void desglosar(String in, PrintWriter pw, boolean tibetan)
	{
		//boolean hayMasLineas=true;
		//int init = 0, fin;
		String tmp;
		
		if (!in.equals(""))
		{
			/*	while (hayMasLineas)
			 {
			 fin = in.indexOf("\n",init);
			 if (fin<0)
			 {
			 linea = in.substring(init).trim();
			 hayMasLineas=false;
			 }
			 else
			 linea = in.substring(init, fin).trim();
			 
			 scanner.scanBody(linea);
			 
			 init = fin+1;
			 }	*/
			scanner.clearTokens();
			in = Manipulate.NCR2UnicodeString(in);
			if (Manipulate.guessIfUnicode(in)) in = BasicTibetanTranscriptionConverter.unicodeToWylie(in);
			else if (Manipulate.guessIfAcip(in)) in = BasicTibetanTranscriptionConverter.acipToWylie(in);
			scanner.scanBody(in);
			scanner.finishUp();
			printText(pw, tibetan);
			try
			{
				tmp = rb.getString(moreLinksProperty);
				pw.println("<p>");
				pw.println(tmp);
				pw.println("</p>");
			}
			catch (MissingResourceException e)
			{
				// do nothing
			}
			printAllDefs(pw, tibetan);
			scanner.clearTokens();
		}
	}
	
	public void printText(PrintWriter pw, boolean tibetan)
	{
		Token words[] = scanner.getTokenArray();
		SwingWord word;
		char pm;
		int i;
		
		if (words==null) return;
		
		pw.print("<p>");
		for (i=0; i < words.length; i++)
		{
			
			if (words[i] instanceof Word)
			{
				word = new SwingWord((Word)words[i]);
				// if (word.getDefs().getDictionarySource()!=null)
				pw.print(word.getLink(tibetan));
				// else pw.print(word.getWylie() + " ");
			}
			else
			{
				if (words[i] instanceof PunctuationMark)
				{
					pm = words[i].toString().charAt(0);
					switch (pm)
					{
					case '\n':
						pw.println("</p>");
						pw.print("<p>");
						break;
					case '<':
						pw.print("&lt; ");
						break;
					case '>':
						pw.print("&gt; ");
						break;
					default:
						pw.print(pm + " ");
					}
				}
			}
		}
		pw.println("</p>");
	}
	
	public void printAllDefs(PrintWriter pw, boolean tibetan) {
		int i, j, k=0;
		Word words[];
		SwingWord word = null;
		Definitions defs;
		String tag;
		DictionarySource ds;
		ByteDictionarySource sourceb=null;
		
		words = scanner.getWordArray(false);
		
		if (words == null) return;
		pw.println("<table border=\"1\" width=\"100%\">");
		for (j = 0; j < words.length; j++) {
			try {
				word = new SwingWord(words[j]);
				defs = word.getDefs();
				ds = defs.getDictionarySource();
				pw.println("  <tr>");
				if (ds == null) {
					tag = "&nbsp;";
				}
				else {
					if (FileSyllableListTree.versionNumber==2) {
						tag = ds.getTag(0);
					}
					else {
						sourceb = (ByteDictionarySource) ds;
						k=0;
						while (sourceb.isEmpty(k)) k++;
						tag = sourceb.getTag(k);
						k++;
					}
				}
				pw.print("    <td width=\"20%\" rowspan=\"" + defs.def.length
						+ "\" valign=\"top\"");
				if (tibetan) pw.print(" class=\"tib\"");
				pw.println(">" + word.getBookmark(tibetan) + "</td>");
				pw.println("    <td width=\"12%\">" + tag + "</td>");
				pw.println("    <td width=\"68%\">" + defs.def[0] + "</td>");
				pw.println("  </tr>");
				for (i = 1; i < defs.def.length; i++) {
					pw.println("  <tr>");
					if (FileSyllableListTree.versionNumber==2) {
						tag = ds.getTag(i);
					}
					else {
						while (sourceb.isEmpty(k)) k++;
						tag = sourceb.getTag(k);
						k++;
					}
					pw.println("    <td width=\"12%\">" + tag + "</td>");
					pw.println("    <td width=\"68%\">" + defs.def[i] + "</td>");
					//else pw.println("    <td width=\"80%\" colspan=\"2\">" + defs.def[i] + "</td>");
					pw.println("  </tr>");
				}
			} catch (Exception e) {
				sl.writeLog("1\t1\t" + word.getWylie());
				sl.writeException(e);
			}
		}
		pw.println("</table>");
	}
	
	public void destroy()
	{
		super.destroy();
		sl.setUserIP(null);
		sl.writeLog("5\t1");
		scanner.destroy();
	}
}
