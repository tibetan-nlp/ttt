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

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.ResourceBundle;

/** Designed to keep a log of the transactions taking place in the 
    servlet version of the translation tool.

    @author Andr&eacute;s Montano Pellegrini
 */

public class ScannerLogger
{
	private String fileName;
	private String lastIP;
	private boolean enabled;

	public ScannerLogger()
	{
		String temp;
		ResourceBundle rb = ResourceBundle.getBundle("dictionary");
		fileName = rb.getString("remotescannerfilter.log-file-name");
		temp = rb.getString("remotescannerfilter.logging-enabled");
		if (temp==null) enabled = false;
		else enabled = temp.toLowerCase().equals("yes");
		lastIP = null;
	}

	public String getCurrentTime()
	{
		Calendar rightNow = Calendar.getInstance();
		return Integer.toString(rightNow.get(Calendar.YEAR)) + "\t" + Integer.toString(rightNow.get(Calendar.MONTH)) + "\t" + Integer.toString(rightNow.get(Calendar.DAY_OF_MONTH)) + "\t" + Integer.toString(rightNow.get(Calendar.HOUR_OF_DAY)) + "\t" + Integer.toString(rightNow.get(Calendar.MINUTE)) + "\t" + Integer.toString(rightNow.get(Calendar.SECOND));
	}

	public void setUserIP(String lastIP)
	{
		this.lastIP = lastIP;
	}

	synchronized public void writeLog(String s)
	{
		if (!enabled) return;
		PrintStream pw = getPrintStream();
		if (lastIP!=null) pw.print(lastIP);
		else pw.print("-");
		pw.println("\t" + getCurrentTime() + "\t" + s);
		pw.flush();
		pw.close();
	}

	private PrintStream getPrintStream()
	{
		PrintStream pw;
		try
		{
			pw = new PrintStream(new FileOutputStream(fileName, true));
			return pw;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	synchronized public void writeException(Exception e)
	{
		if (!enabled) return;
		PrintStream pw = getPrintStream();
		e.printStackTrace(pw);
		pw.flush();
		pw.close();        
	}
}