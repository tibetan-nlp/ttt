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

import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

/**
* This class extends org.jdom.Document to provide added XML functionality, such as locating specific elements.
* Some of its methods are specific to the TIBBIBL DTD, others are more general in function. XMLDoc is in turn
* extended by {@link TibDoc}, which adds more specific functionality and is the class used in the TiblEdit program.
* This class needs to be cleaned up so that only the most general XML functions remain and the functions that are
* specific to our DTD are relegated to TibDoc.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class XMLDoc extends org.jdom.Document implements TibConstants
{
	// Attributes
	private org.jdom.Element root, sysid, temp;
	private org.jdom.Attribute id;
	private Vector tagList = new Vector(100);

	private String editorId, editorsName;

	private org.jdom.output.XMLOutputter xop = new org.jdom.output.XMLOutputter();

	// Accessors

	public void setRoot()
	{
		root = getRootElement();
		id = root.getAttribute(ID);
		if(id == null) {
			id = new org.jdom.Attribute(ID,"");
			root.setAttribute(id);
		}
	}

	public org.jdom.Element getRoot()
	{
		return root;
	}

	public void setID(String id)
	{
		this.id.setValue(id);
	}

	public String getID()
	{
		return (String)id.getValue();
	}

	public void setSysid(String sysidString)
	{
		sysid = getSysid();
		if(sysid == null) {
			temp = findElement(CONTROL);
			sysid = new org.jdom.Element(SID);
			temp.getChildren().add(0,sysid);
		}
		sysid.setText(sysidString);
	}

	public org.jdom.Element getSysid()
	{
		return findElement(SID);
	}

	public org.jdom.Element[] getTagList()
	{
		refreshTagList();
		Object[] tempList = tagList.toArray();
		org.jdom.Element[] elList = new org.jdom.Element[tempList.length];
		for(int c=0; c<tempList.length; c++)
		{
			elList[c] = (org.jdom.Element)tempList[c];
		}
		return elList;
	}

	public void setEditorInfo(String id, String name)
	{
		editorId = id;
		editorsName = name;
	}

/**
* <p>
* This returns the id string for the editor currently set for this document. This ID string
* is the same as the editor's initials referred to elsewhere, e.g ndg, gah...
* </p>
*
* @return String - the editor's id string generally 3 letters long
*/
	public String getEditorID()
	{
		return editorId;
	}

	public String getEditorName()
	{
		return editorsName;
	}

	// Helper

	public void processDoc(org.jdom.Element element)
	{
		String elemName = element.getName();
		tagList.add(element);
		Iterator child = element.getChildren().iterator();
		while(child.hasNext())
			processDoc((Element)child.next());
	}

	public org.jdom.Element findID(String fid)
	{
		refreshTagList();
		Iterator elList = tagList.iterator();
		org.jdom.Element item;

		while(elList.hasNext())
		{
			item = (org.jdom.Element)elList.next();
			String itid = item.getAttributeValue(ID);
			if(itid == fid) {return item;}
		}

		return null;
	}

	static public org.jdom.Element getAncestor(String elName, org.jdom.Element child)
	{
		org.jdom.Element retElem = child.getParent();
		while(retElem != null) {
			if(retElem.getName().equals(elName)) {
				return retElem;
			}
			retElem = retElem.getParent();
		}
		return null;
	}


	public org.jdom.Element findElement(String elName)
	{
		refreshTagList();
		Iterator elList = tagList.iterator();
		org.jdom.Element item;

		while(elList.hasNext())
		{
			item = (org.jdom.Element)elList.next();
			if(item.getName().equalsIgnoreCase(elName)) {return item;}
		}

		return null;
	}

	public org.jdom.Element findElement(String elName, String att, String val)
	{
		if(elName == null || att == null || val == null) {return null;}
		refreshTagList();
		org.jdom.Element[] elList = findElements(elName);

		for(int c=0; c<elList.length; c++)
		{
			if(val.equalsIgnoreCase(elList[c].getAttributeValue(att)))
			{
				return elList[c];
			}
		}

		return null;
	}

	public org.jdom.Element[] findElements(String elName)
	{
		refreshTagList();
		Vector finds = new Vector(20);
		Iterator elList = tagList.iterator();
		Object item;
		org.jdom.Element anElement;
		while(elList.hasNext())
		{
			item = elList.next();
			String itemName = item.getClass().getName();
			if(itemName.indexOf("org.jdom.Element")>-1)
			{
				anElement = (org.jdom.Element)item;
				String elementName = anElement.getName();
				if(elementName.equals(elName))
				{
					finds.add(anElement);
				}
			}
		}

		return TibDoc.toElementArray(finds);
	}

	public org.jdom.Element[] findElements(String att, String val)
	{
		if(att == null) {return null;}
		Vector finds = new Vector(20);
		refreshTagList();
		for(Iterator it = tagList.iterator();it.hasNext();)
		{
			org.jdom.Element el = (org.jdom.Element)it.next();
			String attVal = el.getAttributeValue(att);
			if((attVal != null && attVal.equals(val)) || (val == null && attVal == null)) {
				finds.add(el);
			}
		}
		return TibDoc.toElementArray(finds);
	}

	public void refreshTagList()
	{
		tagList = new Vector(100);
		processDoc(getRoot());
	}

	// Constructor

	public XMLDoc(org.jdom.Element rootElement)
	{
		super(rootElement);
		setRoot();
		processDoc(getRoot());
	}
}
