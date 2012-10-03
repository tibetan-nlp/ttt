/*
The contents of this file are subject to the THDL Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the THDL web site 
(http://www.thdl.org/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is the Tibetan and Himalayan Digital
Library (THDL). Portions created by the THDL are Copyright 2001-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.bibl;

import java.io.File;
import java.util.Observable;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
//import org.thdl.tib.bibl.shared.*;

/**
* This class reads in an XML file using the SAXBuilder in org.jdom.input.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class XMLReader extends Observable implements TibConstants
{
	// Attributes
	private org.jdom.Element root;
	private TibDoc doc;
	private String docName;
	private String error;

	public boolean setDoc(File file)
	{
		boolean ok = true;
		if(file == null) {return false;}
		String fn = file.getName();

		if(fn.indexOf(".xml")>-1) {

			try {

				SAXBuilder builder = new SAXBuilder(false);
				builder.setEntityResolver(new TibDTDResolver());
				root = builder.build(file).getRootElement().detach();
				//System.out.println(xop.outputString(root));
				//setChanged();
				//notifyObservers();
				error = "";

			} catch (JDOMException e) {

				e.printStackTrace();
				System.out.println("JDOM Exception thrown and caught! " + e.getMessage());
				error = e.getMessage();
				ok = false;
			}

		} else {
			ok = false;
		}

		return ok;
	}

	public org.jdom.Element getRoot()
	{
		return root;
	}

	public TibDoc getDoc()
	{
		doc = new TibDoc(root);
		return doc;
	}

	public org.jdom.Document getJDOMDoc()
	{
		org.jdom.Document jdomDoc = new org.jdom.Document(root);
		return jdomDoc;
	}

	public void setDocName(String name)
	{
		docName = name;
	}

	public String getDocName()
	{
		return docName;
	}

	public String getError()
	{
		return error;
	}

	// Helper Methods



	// Constructor
	public XMLReader(File f)
	{
		setDoc(f);
	}

	public XMLReader()
	{
		doc = null;
		docName = null;
		error = null;
	}
}
