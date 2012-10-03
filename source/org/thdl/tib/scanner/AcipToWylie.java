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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

/** Provides an interfase to convert from tibetan text transliterated in the Acip scheme to THDL's <a href="http://orion.lib.virginia.edu/thdl/tools/ewts.pdf" target="_blank">Extended Wylie</a> scheme.

<p>If no arguments are sent, it takes the Acip text from the standard input and sends the 
Wylie text to the standard output. If one argument is sent, it interprets it as the
file name for the input. If two arguments are sent, it interprets the first one as the file name for the input and
the second one as the file name for the output. For example, the following 
command converts the <i>lam-rim-chen-mo.act</i> storing the results in <i>
lam-rim-chen-mo.txt</i>:</p>
<pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.AcipToWylie lam-rim-chen-mo.act lam-rim-chen-mo.txt</pre>
<p>Alternatively by redirecting the standard input/output you perform the same 
job:</p>
<pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.AcipToWylie &lt; lam-rim-chen-mo.act &gt; lam-rim-chen-mo.txt</pre>
<p>If you only want to display the results to the screen, you can run:</p>
<pre>java -cp DictionarySearchStandalone.jar org.thdl.tib.scanner.AcipToWylie lam-rim-chen-mo.act | more</pre>
    
    @author Andr&eacute;s Montano Pellegrini
	@see WindowScannerFilter
	@see Manipulate
*/

public class AcipToWylie
{
	private BufferedReader in;
	private PrintWriter out;

	public AcipToWylie(BufferedReader in, PrintWriter out)
	{
		this.in = in;
		this.out = out;
	}
	
	/** Simply constructs object and invokes #run().
	    @param args If no arguments are sent, it takes the ACIP text
	    from the standard input and sends the wylie text to the standard
	    output. If one argument is sent, it interprets it as the
	    file name for the input. If two arguments are sent, it
	    interprets the first one as the file name for the input and
	    the second one as the file name for the output.
	*/
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
		
		new AcipToWylie(in, out).run();
	}
	
	public void run() throws Exception
	{
		String linea;
		while ((linea=in.readLine())!=null)
		{
			out.println(BasicTibetanTranscriptionConverter.acipToWylie(linea));
		}
		out.flush();
	}
}
