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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
* Like its sister class, {@link TitleFactory}, this class is used to access and display the information
* in the ID section of a TIBBIBL record.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class IDFactory extends GenericTibFactory implements TibConstants
{
	/**
	* <p>
	* IDFactory is a helper class that takes a {@link TibDoc} in its constructor,
	* processes that doc so that certain information (tags and data) within it is
	* easily accessible for display and modification. This information has to do with
	* the identification of the text. ID numbers and so forth.
	* </p>
	**/
	// Attributes
	org.jdom.Element tibidDecl, topTibid, masterTibid, tibid, altid, physdecl, pagination;
	org.jdom.Element num, sourceFiles;
	org.jdom.Element[] ed_tibids, nums;
	org.jdom.Attribute att;
	String sigla, value;
	Vector editions;
	Hashtable ed_names, textNums, vols, vol_lets, num_in_vols, pages;

	/**
	* <p>
	* This methods is called as the initializing method for this class. It takes
	* the {@link TibDoc} given in the constructor and processes it finding the relavant
	* information for its identification. It does this by reading through the TIBID elements
	* contained in the TIBIDDECL and extracting from their children edition name, sigla, volume
	* volume letter, text number, pagination.
	* And from the sources listed in the control info section.
	* </p>
	**/
	protected void processDoc()
	{
		editions = new Vector();
		ed_names = new Hashtable();
		textNums = new Hashtable();
		vols = new Hashtable();
		vol_lets = new Hashtable();
		num_in_vols = new Hashtable();
		pages = new Hashtable();

		tibidDecl = tibDoc.getRoot().getChild(TIDDECL);

		if(tibidDecl == null) {
			System.out.println("Could not set TibidDecl in IDFactory!\n" +
								  TiblEdit.outputString(tibDoc.getRoot()));
			return;
		}

		ed_tibids = tibDoc.toElementArray(tibidDecl.getChildren(TID));

		if(ed_tibids == null) {
			System.out.println("Can't find any children tibids of tibiddecl in IDFactory!\n" +
									TiblEdit.outputString(tibidDecl));
			return;
		}

		for(int n=0; n<ed_tibids.length;n++)
		{
			// the sigla and edition name
			tibid = ed_tibids[n];
				// sigla
			sigla = tibDoc.cleanString(tibid.getAttributeValue(CORRESP));
			if(sigla == null || sigla.equals("")) {
				sigla = tibDoc.cleanString(tibid.getChild(TID).getText());
			}
			value = tibDoc.cleanString(tibid.getText());
			if(value.indexOf("Master")>-1) {
				sigla = NG;
				tibid.setAttribute(CORRESP,NG);
				masterTibid = tibid;
			}
			if(sigla == null || sigla.equals("")) {System.out.println("Null sigla: " + TiblEdit.outputString(tibid)); continue; }
			editions.add(sigla);
			ed_names.put(sigla,value);
			// the edition sigla Tibid is skipped as it is the same as the corresp value
			// instead get it's two children (text num and vol num)
			children = tibid.getChild(TID).getChildren(TID);
			if(children == null) {
				System.out.println("Can't find the children of the sigla tibid in IDFactory!\n" +
										TiblEdit.outputString(ed_tibids[n]));
				continue;
			}

			// deal with ed text number
			tibid = (org.jdom.Element)children.get(0);
			if(tibid == null) {
				System.out.println("No ed text number tibid in IDFactory!\n" +
										TiblEdit.outputString(ed_tibids[n]));
			} else {
				value = tibDoc.cleanString(tibid.getText());
				textNums.put(sigla,value);
			}

			// deal with vol info
			if(children.size()<2) {
				/*System.out.println("Only one children in vol info!\n" +
					TiblEdit.outputString(tibid.getParent()));*/
				continue;
			}
			tibid =(org.jdom.Element)children.get(1);
			if(tibid == null) {
				/*System.out.println("No vol info for tibid in IDFactory!\n" +
										TiblEdit.outputString(ed_tibids[n]));*/
				continue;
			}

				// volume number
			value = tibDoc.cleanString(tibid.getText());
			vols.put(sigla,value);

				// volume letter
			altid = tibid.getChild(ALT);
			if(altid == null) {
				System.out.println("Can't find altID in IDFactory!\n" +
										TiblEdit.outputString(ed_tibids[n]));
			} else {
				value = tibDoc.cleanString(altid.getText());
				vol_lets.put(sigla,value);
			}
				// text number in volume
			tibid = tibid.getChild(TID);
			if(tibid == null) {
				System.out.println("Can't find volume text number TID in IDFactory!\n" +
										TiblEdit.outputString(ed_tibids[n]));
				continue;
			}
			value = tibDoc.cleanString(tibid.getText());
			num_in_vols.put(sigla,value);
		}

		// Find paginations and add to their hashtable
		physdecl = tibDoc.getRoot().getChild(PHYSDEC);
		if(physdecl == null) {
			System.out.println("No physdecl for this text in IDFactory!\n" +
										TiblEdit.outputString(tibDoc.getRoot()));
			return;
		}
		pagination = physdecl.getChild(PAGIN);
		if(pagination == null) {
			System.out.println("No pagination for this text in IDFactory!\n" +
										TiblEdit.outputString(physdecl));
			return;
		}
		nums = tibDoc.toElementArray(pagination.getChildren(NUM));
		if(nums == null)  {
			System.out.println("Can't find any individual num elements in pagination in IDFactory!\n" +
										TiblEdit.outputString(pagination));
			return;
		}
		for(int n=0;n<nums.length;n++)
		{
			sigla = nums[n].getAttributeValue(CORRESP);
			value = tibDoc.cleanString(nums[n].getText());
			if(sigla == null) {
				System.out.println("A num element in a text's pagination does not have a corresp value in IDFactory!\n" +
										TiblEdit.outputString(nums[n]));
				continue;
			}
			pages.put(sigla,value);
		}

	}

	// Accessors


	/**
	* <p>
	* This method returns the String version of the master ID where all the subcategories are
	* delimited by a period, e.g. Ng1.4.2. It does this by recursively looking for a child Tibid
	* within the master Tibid and adding the period delimiter between each of the content strings.
	* </p>
	*
	* @return String - the formatted Master ID string.
	*/
	public String getMasterID()
	{
		String out = new String();
		child = masterTibid.getChild(TID);
		int n=0;
		while(child != null) {
			out += tibDoc.cleanString(child.getText());
			child = child.getChild(TID);
			if(child != null) { out += DOX_DELIM;}
		}
		return out;
	}

	/**
	* <p>
	* This method takes a string as its parameter that is in the usual master ID format with subcategories
	* separated by the period or the {@link TibConstants#DOX_DELIM DOX_DELIM}. It parses it through a
	* StringTokenizer and using the {@link #masterTibid} fills out the information. It does not however
	* set the text's number within the category but it leaves a last Tibid empty ready for that information.
	* </p>
	*
	* @param masterID String - masterID the master ID string
	*
	* @return String - the new file name based on the master ID
	*/
	public void setMasterID(String masterID)
	{
		if(masterID == null) {return;}
		StringTokenizer toker = new StringTokenizer(masterID.trim(),DOX_DELIM);

		// The first child is the Ng1, Ng2, Ng3,or Ng4 sigla.
		child = masterTibid.getChild(TID);
		child.setAttribute(TYPE,ED);
		child.setAttribute(SYS,SIG);

		while(child != null && toker.hasMoreTokens()) {
			child.setText(toker.nextToken());
			child.setAttribute(TYPE,CLASS);
			child.setAttribute(SYS,NUMB);
			parent = child;
			child = child.getChild(TID);
		}

		while(toker.hasMoreTokens()) {
			child = new org.jdom.Element(TID);
			child.setAttribute(TYPE,CLASS);
			child.setAttribute(SYS,NUMB);
			child.setText(toker.nextToken());
			parent.addContent(child);
			parent = child;
			child = null;
		}

		if(child == null) {
			child = new org.jdom.Element(TID);
			parent.addContent(child);
		}

		// For the text number use the first three words of the title for now.
		child.setAttribute(TYPE,TEXT);
		child.setAttribute(SYS,NUMB);

	}

	public org.jdom.Element[] getSourceFiles()
	{
		org.jdom.Element controlinfo = tibDoc.findElement(CONTROL);
		Iterator it = controlinfo.getChildren(RESPSTM).iterator();
		if(it != null) {
			while(it.hasNext()) {
				org.jdom.Element respStmt = (org.jdom.Element)it.next();
				String nAtt = respStmt.getAttributeValue(N);
				if(nAtt != null && nAtt.equals(SRC))
						return tibDoc.toElementArray(respStmt.getChildren(NAME));
			}
		}
		return null;
	}

	public boolean hasEdition(String sigla)
	{
		if(editions.indexOf(sigla)>-1) {
			return true;
		}
		return false;
	}

	public String getEdName(String sigla)
	{
		return (String)ed_names.get(sigla);
	}

	public String getTextNum(String sigla)
	{
		return (String)textNums.get(sigla);
	}

	public String getVolNum(String sigla)
	{
		return (String)vols.get(sigla);
	}

	public String getVolLet(String sigla)
	{
		return (String)vol_lets.get(sigla);
	}

	public String getNumInVol(String sigla)
	{
		return (String)num_in_vols.get(sigla);
	}

	public String getPagination(String sigla)
	{
		return (String)pages.get(sigla);
	}

	public Vector getEditionsConsulted()
	{
		return editions;
	}

	public boolean addEditionConsulted(String[] info)
	{
		//System.out.println("Text's TibidDecl: \n" + TiblEdit.outputString(tibidDecl));
		if(info.length != NEW_ED_INFO_SPECS.length) {
			System.out.println("The information given to add an edition in IDFactory is not the right size: " + info.length);
			return false;
		}
		try{
			sigla = info[1];

			grandparent = new org.jdom.Element(TID);
			grandparent.setAttribute(CORRESP,sigla);
			grandparent.setAttribute(TYPE,ED);
			grandparent.setAttribute(SYS,LET);
			grandparent.setText(info[0]);

			parent = new org.jdom.Element(TID);
			parent.setAttribute(TYPE,ED);
			parent.setAttribute(SYS,SIG);
			parent.setText(sigla);
			grandparent.addContent(parent);

			tibid = new org.jdom.Element(TID);
			tibid.setAttribute(TYPE,TEXT);
			tibid.setAttribute(SYS,NUMB);
			tibid.setText(info[2]);
			parent.addContent(tibid);

			tibid = new org.jdom.Element(TID);
			tibid.setAttribute(TYPE,VOL);
			tibid.setAttribute(SYS,NUMB);
			tibid.setText(info[3]);
			parent.addContent(tibid);

			parent = tibid;
			altid = new org.jdom.Element(ALT);
			altid.setAttribute(SYS,LET);
			altid.setAttribute(LANG,TIB);
			altid.setText(info[4]);
			parent.addContent(altid);

			tibid = new org.jdom.Element(TID);
			tibid.setAttribute(TYPE,TXT);
			tibid.setAttribute(SYS,NUMB);
			tibid.setText(info[5]);
			parent.addContent(tibid);

			tibidDecl.addContent(grandparent);

			num = new org.jdom.Element(NUM);
			num.setAttribute(CORRESP,sigla);
			num.setText(info[6]);
			pagination.addContent(num);

			processDoc();

			return true;

		} catch (org.jdom.IllegalAddException iae) {
			System.out.println("Illegal add exception (JDOM) caught in trying to create new edition tibid in IDFactory!");
		}
		addSourceConsulted(sigla,tibDoc.getEditorID(),tibDoc.getEditorName());
		return false;
	}

	public void removeEd(String sigla)
	{
		tibid = findEdTibid(sigla);
		if(tibid != null)
		{
			tibid.detach();
		}
	}

	public org.jdom.Element findEdTibid(String sigla)
	{
		if(ed_tibids == null) {return null;}
		for(int n=0;n<ed_tibids.length;n++) {
			String edSig = ed_tibids[n].getAttributeValue(CORRESP);
			if(edSig == null || edSig.trim().equals("")) {continue;}
			if(edSig.equals(sigla)) {
				return ed_tibids[n];
			}
		}
		return null;
	}

	public void addSourceConsulted(String sigla, String sourceInitials, String sourceName)
	{
		org.jdom.Element[] sources = getSourceFiles();
		for(int n=0; n<sources.length; n++) {
			String idAtt = sources[n].getAttributeValue(ID);
			if(idAtt != null && idAtt.equals(sigla)) {return;}
		}
		org.jdom.Element newSource = new org.jdom.Element(NAME);
		newSource.setAttribute(ID,sigla);
		newSource.setAttribute(CORRESP,sourceInitials);
		newSource.setText(sourceName);
		sources[0].getParent().addContent(newSource);
	}


	// Constructor
	protected IDFactory(TibDoc td)
	{
		super(td);
	}
}
