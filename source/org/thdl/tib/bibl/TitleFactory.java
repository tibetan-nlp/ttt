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
import java.util.List;
import java.util.Vector;

/**
* The TitleFactory class extends the GenericTibFactory to provide means for locating and manipulating title elements
* within a {@link TibDoc}.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class TitleFactory extends GenericTibFactory implements TibConstants
{
	// Attributes
	private org.jdom.Element titlegrp, titleInfo, tdecl, normDecl, normTib, normEng, foreign;
	private org.jdom.Element tdiv, pagination, title, element, rs;
	private org.jdom.Element sections, num;
    private	org.jdom.Element[] titles, tdivs, titleLists, titleItems, elements;
    private org.jdom.Element[] tibanals, chapters, chapterTitles;
    private Hashtable nums;
    private String type, subtype, text;
    int entryIndex;
    private TitleParser titleParser;


	// Accessors
	protected void processDoc()
	{
		// getting root elements for titles and chapters
		titlegrp = tibDoc.findElement(TITLEGRP);
		sections = tibDoc.findElement(SECTIONS);

		// setting Normalized title
		normDecl = titlegrp.getChild(TDECL);
		String normEngText = new String();
		normDecl.removeChildren(FOREIGN);
		normTib = normDecl.getChild(TITLE);
		if(normTib == null) {
			System.out.println("No title element found in normalized title decl!");
			System.out.println("Decl: " + TiblEdit.outputString(normDecl));
			return;
		}
		String lang = normTib.getAttributeValue(LANG);
		if(lang == null || !lang.equals(TIB)) {
			System.out.println("The first title element in Normalized title decl is not a Tib title!");
			System.out.println("Lang: " + lang);
			System.out.println("Title: " + TiblEdit.outputString(normTib));
		}
		foreign = normTib.getChild(FOREIGN);
		if(foreign == null) {
			foreign = new org.jdom.Element(FOREIGN);
			foreign.setAttribute(LANG,ENG);
			normTib.addContent(foreign);
		}

		children = normDecl.getChildren(TITLE);
		if (children.size()>1) {
			it = children.iterator();
			while(it.hasNext()) {
				element = (org.jdom.Element)it.next();
				if(element.getAttributeValue(LANG).equalsIgnoreCase(ENG)) {
					foreign.setText(element.getText());
					element.detach();
					normEng = foreign;
				}
			}

		}

		normTib.setAttribute(CORRESP,NG);

		element = normTib.getChild(TITLE);

		if(element != null && (foreign.getText() == null || foreign.getText().trim().equals("")))
		{
			foreign.setText(element.getText());
			element.detach();
		}
		// Setting title info
		titleInfo = titlegrp.getChild(TINFO);
		tdivs = tibDoc.toElementArray(titleInfo.getChildren(TDIV));
		for(int n=0;n<tdivs.length;n++)
		{
			if(tdivs[n].getAttributeValue(TYPE).equalsIgnoreCase(BODY)) {
				titleLists = tibDoc.toElementArray(tdivs[n].getChildren(TLIST));
				break;
			}
		}

		// Setting chapters
		if(sections == null) {return;}
		tibanals = tibDoc.toElementArray(sections.getChildren(TIBANAL));
		if(tibanals.length<2 || tibanals[1] == null) {return;}
		chapters = tibDoc.toElementArray(tibanals[1].getChildren(TIBBIBL));
	}

	// Helper Methods

	public org.jdom.Element getNormalizedTitle()
	{
		return normTib;
	}

	public org.jdom.Element[] getHeaderTitles()
	{
		element = tibDoc.findElement(HEAD);
		return tibDoc.toElementArray(element.getChildren(TITLE));
	}

	public String[] getHeadStrings()
	{
		return tibDoc.getHeadStrings();
	}

	private void doNormalizedTitle()
	{
		doHeader(NORM_TITLE_HEAD);
		if(normTib != null) {
			doTitle(normTib);
		} else { addLineBreak();}

	}

	private void doTitleLine()
	{
		org.jdom.Element tdv = findTitleDiv(SUBTYPE,TITLELN);
		doHeader(TITLE_LINE_HEAD);
		if(tdv == null)
		{
			outStyles.add(new ElementStyle("{Error: Title Line TitleDiv not found!}\n",TextPane.ITALIC,NO_ELEM));
		} else {
			tdecl = tdv.getChild(TDECL);
			doMultipleTitles(tdecl);
		}
	}

	private void doEOCtitles()
	{
		doHeader(EOC_TITLES_HEAD);
		if(titleLists == null) {
			doLabel("No EOC Titles listed");
			addLineBreak();
			System.out.println("Mark-up problem with " + tibDoc.getSysid() + ": no TitleDivs!");
			return;
		}
		for(int n=0; n<titleLists.length; n++)
		{
			tdecl = titleLists[n].getChild(TDECL);
			titles = tibDoc.toElementArray(tdecl.getChildren(TITLE));
			for(int m=0; m<titles.length; m++)
			{
				String cr = titles[m].getAttributeValue(CORRESP);
				String src = new String("");
				if(cr != null && cr.equals(NG)) {src = " (" + cr + ")";}
				doTitle(titles[m], src);
				if(cr != null && cr.equals(NG)) {addLineBreak();}
			}

			String srcs = new String("(");
			elements = tibDoc.toElementArray(titleLists[n].getChildren(TITEM));
			if(elements != null) {
				for(int i=0; i<elements.length; i++) {
					element = elements[i].getChild(DESIG);
					srcs += elements[i].getAttributeValue(CORRESP) + ".b" + tibDoc.cleanString(element.getText());
					if(i == elements.length-1) {
						srcs += ")\n\n";
					} else {
						srcs += ", ";
					}
				}
			}
			if(srcs != null) doReg(srcs);
			addSeparator();
		}
	}

	private void doClosingTitles()
	{
		if(tdivs == null) {return;}
		doHeader(CLOSE_TITLE_HEAD);
		for(int n=0; n<tdivs.length;n++)
		{
			type = tdivs[n].getAttributeValue(TYPE);
			subtype = tdivs[n].getAttributeValue(SUBTYPE);
			if(type != null && type.equalsIgnoreCase(BACK))
			{
				addLineBreak();
				tdecl= tdivs[n].getChild(TDECL);
				title = tdecl.getChild(TITLE);
				if(title != null) {
					if(subtype == null) subtype = "????";
					doHeader(subtype +"\n");
					doMultipleTitles(tdecl);
				}
			}
		}
	}

	private void doNonTibetan()
	{
		org.jdom.Element tdv = findTitleDiv(TYPE,NONTIB);
		if(tdv == null) {return;}
		tdecl = tdv.getChild(TDECL);
		doHeader(NON_TIB_HEAD);
		if(tdv == null)
		{
			outStyles.add(new ElementStyle("{No non-Tibetan Title found!}\n",TextPane.ITALIC,NO_ELEM));
			return;
		} else {
			doMultipleTitles(tdecl);
		}
		doLabel(ORIG_LANG+": ");
		element = tdecl.getChild(DISC);
		if(element.getAttributeValue(TYPE).equalsIgnoreCase("Original language"))
		{
			rs = element.getChild(RS);
			if(rs == null) {return;}
			String orig_lang = tibDoc.cleanString(rs.getText());
			String src = element.getChild(RS).getAttributeValue(CORRESP);
			elements = tibDoc.toElementArray(element.getChildren(RS));
			for(int n=1; n<elements.length; n++)
			{
				String thisLang = tibDoc.cleanString(elements[n].getText());
				if(thisLang.equals(orig_lang))
				{
					src += ", " + elements[n].getAttributeValue(CORRESP);
				} else {
					orig_lang += "(" + src + "), " + thisLang;
					src = elements[n].getAttributeValue(CORRESP);
				}
			}
			orig_lang += " (" + src + ")";
			doReg(orig_lang);
		}
	}


	private void doChapters()
	{
		if(chapters == null) {return;}
		addLineBreak();
		chapterTitles = new org.jdom.Element[chapters.length];
		for(int n=0;n<chapters.length;n++)
		{
			addLineBreak();
			String chapHead = chapters[n].getChild(HEAD).getText();
			doHeader(tibDoc.cleanString(chapHead)+"\n");
			tdecl = chapters[n].getChild(TDECL);
			if(tdecl == null) {continue;}
			chapterTitles[n] = tdecl.getChild(TITLE);

			titles = tibDoc.toElementArray(tdecl.getChildren(TITLE)); // the list of titles in the titledecl
			pagination = tdecl.getChild(SRC);  						  // pagination for titles is in source element within titledecl for chapters
			nums = setPageHash(pagination.getChildren(NUM));
			for(int m=0; m<titles.length; m++)
			{
				String cr = titles[m].getAttributeValue(CORRESP);
				String lang = titles[m].getAttributeValue(LANG);
				if(lang!= null && lang.equals(ENG)) {
					String text = titles[m].getText();
					if(text!=null && text.indexOf(UNTITLED)==-1) {
						tdecl.removeContent(titles[m]); continue;
					}
				}
				String src = " (" + cr;
				if(cr != null) src += ", " + (String)nums.get(cr);
				src += ")";
				doTitle(titles[m], src);
				if(cr.equals(NG)) {doLabel("Individual Edition Titles\n");}
			}
		}
	}
	// Methods for assiting title processing
	// Finding a particular title div
	private org.jdom.Element findTitleDiv(String attr, String value)
	{
		if(tdivs == null) {System.out.println("No titledivs for " + tibDoc.getSysid().getText() + "!"); return null;}
		for(int n=0; n<tdivs.length; n++)
		{
			String tdivAttr = tdivs[n].getAttributeValue(attr);
			if(tdivAttr != null && tdivAttr.equalsIgnoreCase(value))
			{
				return tdivs[n];
			}
		}
		return null;
	}

	// Processing multiple titles in one titleDecl
	private void doMultipleTitles(org.jdom.Element tdecl)
	{
		titles = tibDoc.toElementArray(tdecl.getChildren(TITLE));
		pagination = tdecl.getParent().getChild(PAGIN);
		nums = setPageHash(pagination.getChildren(NUM));
		for(int n=0; n<titles.length; n++)
		{
			String cr = titles[n].getAttributeValue(CORRESP);
			String src = " (" + cr;
			if(cr != null) src += ", " + (String)nums.get(cr);
			src += ")";
			doTitle(titles[n], src);
			if(cr.equals(NG)) {doLabel("Individual Edition Titles\n");}
		}
	}


	private Hashtable setPageHash(java.util.List lst)
	{
		Hashtable corr = new Hashtable();
		Iterator nums = lst.iterator();
		while(nums.hasNext()) {
			org.jdom.Element num = (org.jdom.Element)nums.next();
			String sig = num.getAttributeValue(CORRESP);
			if(sig == null) {
				System.out.println("A Num element without a corresponding edition has been used (" +
										CORRESP + "): \n" + TiblEdit.outputString(num));
			} else {
				corr.put(sig,tibDoc.cleanString(num.getText()));
			}
		}
		return corr;
	}

	// Generic methods for Setting text, styles and elements
	private void doTitle(org.jdom.Element el)
	{
		doTitle(el, new String());
	}

	private void doTitle(org.jdom.Element el, String suffix)
	{

		titleParser = new TitleParser(el);
		Vector stys = titleParser.getStyles();
		String typ = el.getAttributeValue(TYPE);
		outStyles.addAll(stys);
		outStyles.add(new ElementStyle(suffix, TextPane.REG, NO_ELEM));
		addLineBreak();
		org.jdom.Element foreign = el.getChild(FOREIGN);
		if(foreign != null && foreign.getText() != null &&
			foreign.getText().trim().length()>1)
		{
			outStyles.add(new ElementStyle(tibDoc.cleanString(foreign.getText())+"\n", TextPane.REG, foreign));
		}
		List discList = el.getParent().getChildren(DISC);   // Get Discussion element if there
		org.jdom.Element disc, discItem;
		disc = null;
        for(Iterator it=discList.iterator();it.hasNext();) {
			discItem = (org.jdom.Element)it.next();
			String type = discItem.getAttributeValue(TYPE);
			if(type.equals(BRIEF) || type.equals(FULL)) {
				disc = discItem;
				break;
			}
		}

		List titleChilds = el.getParent().getChildren(TITLE);    // Get all the title children
		if(titleChilds != null && titleChilds.size()>0 &&
		    el.equals((org.jdom.Element)titleChilds.get(titleChilds.size()-1))) {
			if(disc!=null && disc.getText()!=null &&
				disc.getText().trim().length()>1) {
					outStyles.add(new ElementStyle("Show Discussion\n", TextPane.RED, disc));
			}
		}
	}

	// Public Helpers

	public java.util.Collection getAllTitles()
	{
		outStyles = new Vector();

		doNormalizedTitle();
		doTitleLine();
		doEOCtitles();
		doClosingTitles();
		doNonTibetan();
		doChapters();

		return ((java.util.Collection)outStyles);
	}

	// Constructor
	protected TitleFactory(TibDoc td)
	{
		super(td);
	}
}
