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
import java.util.StringTokenizer;
import java.util.Vector;

import org.jdom.DocType;
import org.jdom.Element;

	/**
	* <p>
	* This class extends {@link XMLDoc} and adds methods specific to the {@link TiblEdit}
	* program, primarily by creating a {@link TitleFactory} and an {@link IDFactory} that allow
	* easy access and manipulation of those marked up items. It also contains methods for creating
	* new apparatus or variant reading mark up and entering in translations within Foreign elements
	* contained inside the associated Title element that contains as its text the transliteration
	* of the Tibetan title.
	* </p>
	*
	* @author Than Garson, Tibetan and Himalayan Digital Library
	*/

public class TibDoc extends XMLDoc
{
	// Attributes
	/**
	* <p>
	* Generic variables for headers, parents, elements/self, and children.
	* </p>
	*/
	private org.jdom.Element head, parent, element, child;

	/**
	* <p>
	* The {@link TitleFactory} associated with this Tibbibl XML document.
	* </p>
	*/
	private TitleFactory tf;
	/**
	* <p>
	* The {@link IDFactory} associated with this Tibbibl XML document.
	* </p>
	*/
	private IDFactory idf;


	// Accessory
	/**
	* <p>
	* This method sets the first head element or the text header by searching for the first head element.
	* </p>
	*/
	protected void setHead()
	{
		head = findElement(HEAD);
	}

	/**
	* <p>
	* This returns the text's main head element that contains the text header.
	* </p>
	*
	* @return org.jdom.Element - the head element that contains the text's header.
	*/
	public org.jdom.Element getHead()
	{
		return head;
	}

	/**
	* <p>
	* This method sets the {@link #idf IDFactory variable} and the {@link #tf TitleFactory variable}
	* for this Tibbibl XML document.
	* </p>
	*/
	public void setFactories()
	{
		tf = new TitleFactory(this);
		idf = new IDFactory(this);
	}

	/**
	* <p>
	* Returns the {@link TitleFactory} created for this document that allows easy access to and
	* manipulation of titles.
	* </p>
	*
	* @return TitleFactory - The title factory for this Tibbibl XML document.
	*/
	public TitleFactory getTitleFactory()
	{
		return tf;
	}

	/**
	* <p>
	* Returns the {@link IDFactory} created for this document that allows easy access to and
	* manipulation of ID information, that is the Tibids contained within the text's TibidDecl.
	* </p>
	*
	* @return IDFactory - The ID factory for this Tibbibl XML document.
	*/
	public IDFactory getIDFactory()
	{
		return idf;
	}

	// Helper methods

	/**
	* <p>
	* This method sets the information for the current user (=editor) of the program who is
	* entering in translations and adding critical titles to the text. This information is added
	* in the ControlInfo metadata section of the Tibbibl document. A Resp/Name pair is added with the
	* Name element containing the full name and having an ID of the initials, and a Name element is
	* added to the Source list, that has an ID of "Ng" and whose corresp attribute is set to the initials.
	* Thus, all uses of the corresp="Ng" are linked to the tag under the list of sources with corresp = to
	* the editor's initials and content of the full name. This in turn is linked to the Resp that contains
	* the editor's full name and his/her responsibility regarding the document.
	* </p>
	*
	* @param initials String - the editor's (preferably) three initials, such as snw or dfg.
	* @param fullName String - the editor's full name to be recorded in the metadata.
	* @param today String - the present date.
	*/
	public void addEditor(String initials, String fullName, String today)
	{
		initials = initials.trim();
		fullName = fullName.trim();
		setEditorInfo(initials,fullName);
		org.jdom.Element parent = findElement(CONTROL);
		java.util.List children = parent.getChildren(RESPSTM);
		Iterator it = children.iterator();
		int listIndex = -1;
		org.jdom.Element respstm, contrlSrc;
		contrlSrc = null;
		boolean found = false;

		while(it.hasNext()) {
			// find the RespStmts in the control info section
			respstm = (org.jdom.Element)it.next();
			String nValue = respstm.getAttributeValue(N);
			if(nValue == null) {continue;}
			if(nValue.equals(CONTROL_SOURCE)) {
				// if need to modify the sources
				contrlSrc = respstm;
			} else if(nValue.equals(CRIT_ED_LABEL)) {
				// of if it's already there
				found = true;
			}
		}

		if(!found) {
			org.jdom.Element resp, name, date;
			respstm = new org.jdom.Element(RESPSTM);
			respstm.setAttribute(N,CRIT_ED_LABEL);
			resp = new org.jdom.Element(RESP);
			resp.setText(CRIT_ED_DESC);
			respstm.addContent(resp);
			name = new org.jdom.Element(NAME);
			name.setText(fullName);
			name.setAttribute(ID,initials);
			resp.addContent(name);
			date = new org.jdom.Element(DATE);
			date.setText(today);
			resp.addContent(date);
			parent.addContent(respstm);

			name = new org.jdom.Element(NAME);
			name.setAttribute(ID,NG);
			name.setAttribute(CORRESP,initials);
			name.setText(fullName);
			contrlSrc.addContent(name);

		} else {
			System.out.println(" Need to deal with 2nd time use.\n"+
			 "Check to see if same editor & same date.\n" +
			 "Create new editor or date-range.");
		}

	}

	/**
	* <p>
	* This method returns an array of strings.  The array contains 1. the opening string of the header, such as
	* "Bibliographic Record for", and 2. the text contain within each of its children elements, usually
	* there are two of these a lang="tib" and a lang="eng", each one entered separately. The strings
	* are stripped of extraneous paragraph markers and spaces using the {@link #cleanString} method.
	* If it's an English title then it is enclosed in parentheses.
	* </p>
	*
	* @return <code>String[]</code> an array containing the different strings that are the parts
	*               of this header element. They are kept separate so that different styles can be
	*				applied to the opening text (bold) and the title contents (bold-italic).
	*/
	public String[] getHeadStrings()
	{
		Vector strs = new Vector();
		Iterator children = head.getContent().iterator();
		while(children.hasNext()) {
			Object child = children.next();

			if(child instanceof org.jdom.Text)
			{
				String t = ((org.jdom.Text)child).getText();

				if(t != null)
				{
					t = cleanString(t);
					if(t.length()>0 && !t.equals(" ")) strs.add(t);
				}

			} else if(child instanceof org.jdom.Element)
			{
				org.jdom.Element e = (org.jdom.Element)child;
				if(e.getAttributeValue(LANG).equalsIgnoreCase(ENG)) {
					strs.add("(" + cleanString(e.getText()) + ")");
				} else {
					strs.add(cleanString(e.getText()));
				}
			}
		}
		return TiblEdit.toStringArray(strs);
	}

	/**
	* <p>
	* This method returns a string array of the sources used in creating this master document.
	* These sources (for now -- this may change) are included within the controlInfo metadata for the
	* text in a respStmt with n="source". It contains a Resp element with Source Records as the label and then
	* a series of Name elements with their ID attribute set to their sigla and their content set to the full
	* name of the source xml document (no path name). In the case of the master editor, the ID is set to Ng and
	* the corresp attribute is set to the editor's initials. This serves to connect all the various pieces of information to
	* a particular source by using their corresp attribute. The corresp attribute is an IDREF, which means its
	* value must match the value for another element's ID. Thus, all uses of the corresp="Tk" not only identify
	* the piece of information as belonging to the gTing skyes edition but also link each use with the name
	* of the actual source file or the Tk Tibbibl record. In the case of the editor, their is a double IDREF
	* occuring. Each use of the Ng sigla refers to the Name element in the sources that has the ID "Ng" and
	* the corresp = to the editor's initials. This refers to the Resp element above that has an ID of the editor's
	* intials and contains his or her full name.
	* </p>
	*/
	public String[] getSources()
	{
		org.jdom.Element respSt = findElement(RESPSTM,N,"source");
		Iterator children = respSt.getChildren().iterator();
		Vector sources = new Vector();
		while(children.hasNext())
		{
			org.jdom.Element child = (org.jdom.Element)children.next();
			if(child.getName().equals(NAME))
			{
				String childText = child.getText();
				int xmlind = childText.indexOf(".bib.xml");
				if(xmlind > -1)  {
					sources.add(childText.substring(0,xmlind));
				} else  {
					sources.add(childText);
				}
			}
		}
		return TiblEdit.toStringArray(sources);
	}

	/**
	* <p>
	* This creates a critical version of any title within a TitleDecl by adding a title element
	* as its first child with a Corresp=Ng. It does this by cloning one of the existing title elements
	* to get one with identical attribute values but changes the corresp attribute to Ng. It adds
	* a Foreign element with Lang = "eng as part of this title's content for the translation. It
	* also "cleans" the title of an Expan element, which were used to add editor's interpretations of
	* standard abbreviations such as thaMd for thams cad. These are replaced with the original abbreviated form.
	* Finally, it adds a Num element within the pagination that has an ID = Ng and contains the initals of the
	* editor who is the source of this critical title.
	* </p>
	*
	* @param el org.jdom.Element - the Title element that is to serve as the basis of the critical title. This is
	* the element that is cloned to create the critical version.
	*
	* @param initials String - the initials of the editor creating the critical title.
	*/
	public org.jdom.Element createCriticalTitle(org.jdom.Element el, String initials)
	{

		org.jdom.Element app, contItem;
		Object item;
		parent = el.getParent(); // the tdecl
		child =((org.jdom.Element)el.clone()).detach();
		String base = child.getAttributeValue(CORRESP);
		if(base == null) {
			base = findTitleListEd(parent);
			el.setAttribute(CORRESP,base);
		}

		child.setAttribute(CORRESP,NG);
		child.setAttribute(TYPE,base);
		if(child.getChild(FOREIGN) == null) {
			child.addContent(new org.jdom.Element(FOREIGN));
		}
		java.util.List children = child.getContent();
		Iterator it = children.iterator();
		contItem = null;
		app = null;
		while(it.hasNext()) {
			item = it.next();
			if(item instanceof org.jdom.Element) {
				contItem = (org.jdom.Element)item;
				if(contItem.getName().equals(EXPAN)) {
					String resp = contItem.getAttributeValue(RESP);
					String abbr = contItem.getAttributeValue(ABBR);
					if(resp == null) { resp = new String();}
					String expanText = contItem.getText();
					app = newApp(resp,expanText);
					app.getChild(RDG).setAttribute(WIT,base);
					app.getChild(RDG).setText(abbr);
					break;
				}
			}
		}
		if(contItem != null && app != null) {
			children.set(children.indexOf(contItem),app);
		}
		if(parent != null) {
			parent.getChildren().add(0,child);
		}

		org.jdom.Element grandparent = parent.getParent();
		org.jdom.Element pagination = grandparent.getChild(PAGIN);  // the normal way for a titleDiv other than titlelists

		if(pagination == null) {
			if(grandparent.getName().equals(TIBL)) // then it's a chapter
			{
				pagination = parent.getChild(SRC);
			}
		}

		if(pagination != null) {
			org.jdom.Element newSource = new org.jdom.Element(NUM);
			newSource.setAttribute(CORRESP,NG);
			newSource.setText(initials);
			pagination.getContent().add(0,newSource);
		} else {
			System.out.println("No pagination element found in creating critical title of " + TiblEdit.outputString(parent));
		}

		return child;
	}


	/**
	* <p>
	* This method sets the master doxogrpahy elements within the IntellDecl section of the Tibbibl.
	* It takes two strings the Tibetan doxographical statement the English version of that. These
	* list the categories separated by colons and are supplied by the {@link DoxWindow}. The master
	* doxography element with type = "category" and n = "0" is located or else inserted and its
	* text is set to the Tibetan doxographical statement and a Foreign element is included within that
	* containing the English translation of it.
	* </p>
	*
	* @param tibDox String - the Tibetan doxographical statement
	*
	* @param engDox String - the English doxographical statement.
	*/
	public void setDoxography(String tibDox, String engDox)
	{
		org.jdom.Element masterDox, edDox;
		masterDox = null;
		edDox = null;
		org.jdom.Element[] doxs = findElements(DOX);
		for(int n=0;n<doxs.length;n++)
		{
			String type = doxs[n].getAttributeValue(TYPE);
			if(type != null && type.equals(CATEG)) {
				String nVal = doxs[n].getAttributeValue(N);
				if(nVal.equals("0")) {
					masterDox = doxs[n];
				} else {
					edDox = doxs[n];
				}
			}
		}

		if(masterDox == null) {
			masterDox = new org.jdom.Element(DOX);
			masterDox.setAttribute(TYPE,CATEG);
			masterDox.setAttribute(N,"0");
			if(edDox != null) {
				java.util.List doxKids = edDox.getParent().getChildren();
				int ind = doxKids.indexOf(edDox);
				doxKids.add(ind,masterDox);
			} else {
				parent = findElement(INTDECL);
				if(parent != null) {
					parent.addContent(masterDox);
				} else {
					System.out.println("Couldn't fine the intelldecl to add master doxography! Quitting!");
					return;
				}
			}
		}

		masterDox.setText(tibDox);
		element = new org.jdom.Element(FOREIGN);
		element.setAttribute(LANG,ENG);
		element.setText(engDox);
		masterDox.addContent(element);
	}

	/**
	* <p>
	* This method takes a string ID where the subcategories are delimited by a period (".") and
	* invokes the Tibbibl's {@link IDFactory#setMasterID(String)} method to parse the ID and create
	* the nested Tibid mark-up.
	* </p>
	*
	* @param idString String - the new ID string delimited by periods.
	*/
	public void setMasterID(String idString)
	{
		idf.setMasterID(idString);
	}

	/**
	* <p>
	* This method returns the normalized title of the text using the tibbibl's {@link TitleFactory}.
	* </p>
	*
	* @return String - the normalized title of this text.
	*/
	public String getNormalizedTitle()
	{
		// the title factory returns the title element, not the text..
		element = tf.getNormalizedTitle();
		if(element == null) {return new String();}
		return cleanString(element.getText());
	}

	/**
	* <p>
	* This method takes a titledecl from a title list and finds the edition listed in the first titleitem element.
	* Because the same version of a title could span many chapters in different editions, the Corresp attribute is
	* not set for the title itself in a title list but is set on the individual titleItems. This method finds the first
	* title item and returns the sigla contained in its corresp element so this can be assigned as the type attribute
	* of the critical version of the title. (The type attribute of a critical title indicates its original source title.)
	*
	* </p>
	*
	* @param tdecl org.jdom.Element - the title list's titledecl.
	*
	* @return String - the sigla of the edition that is the source for the title, derrived from the first titleitem element.
	*/
	public String findTitleListEd(org.jdom.Element tdecl)
	{
		org.jdom.Element tlist,titem;
		String edSig;
		tlist = tdecl.getParent();
		if(!tlist.getName().equals(TLIST)) {return null;}
		titem = tlist.getChild(TITEM);
		edSig = titem.getAttributeValue(CORRESP);
		return edSig;
	}

	/**
	* <p>
	* This method takes a text string and a title element and uses it to insert an
	* English translation for a title, by adding a Foreign element to the title element, setting
	* its Lang=eng, and putting the text in its content.
	* </p>
	*
	* @param text String - the text of the translation
	* @param el org.jdom.Element - the Title element (lang=tib) that contains the title being translated.
	*/
	public void addTranslation(String text, org.jdom.Element el)
	{
		if(el == null || el.getName().equals(NO_ELEM)) {return;}
		org.jdom.Element foreign = el.getChild(FOREIGN);
		if(foreign == null) {
			System.out.println("Foreign is null: \n" + TiblEdit.outputString(el));
			foreign = new org.jdom.Element(FOREIGN);
			foreign.setAttribute((org.jdom.Attribute)LANG_ENG.clone());
			el.addContent(foreign);
		}
		foreign.setText(text);
	}

	/**
	* <p>
	* This method takes a Title element, a sigla, and a pagination and creates a new
	* edition title within a particular TitleDecl. The element is one of the future siblings of the
	* new edition title and is cloned to create one with identical attributes. The corresp value of the
	* clone is then changed to the new sigla, children and text are removed, and the element is added
	* to the active list of the TitleDecl's children. The pagination is located and a corresponding
	* Num element is added with the corresp=sigla set and the text set to the given pagination. The
	* resulting title element is then returned.
	*
	* THIS IS A TEMPORARY ADHOC FIX SO EDITORS CAN DEAL WITH EOC TITLES JUST LIKE OTHER TITLES
	* One exception is if the new edition title is an EOC title
	* to be added to a title list. In this case, the title is added to the titledecl as usual, but the
	* pagination is included in a separate titleItem with a blank designation. This is a questionable practice
	* because the notion of critically editing EOC titles is a contradition. The way we mark up EOC titles
	* they are by nature variants, differences between them are catalogued by creating a new TitleList. But
	* some of the title lists could be collapsed by considering them as variants on a critical title...
	* The approach for this needs to be contemplated and fixed. Until then, they are treated like all other
	* titles, and new edition titles added record their pagination in a separate title item element.
	*
	* </p>
	*
	* @param elem org.jdom.Element - the title element to be cloned.
	* @param sigla String - the sigla of the new title element.
	* @param pagination String - the pagination of the new title.
	*
	* @return org.jdom.Element - the new edition title with corresp=sigla that is a sibling of the originally given title element.
	*/
	public org.jdom.Element addTitle(org.jdom.Element elem, String sigla, String pagination)
	{
		org.jdom.Element parent, grandparent, pagin, returnElem;
		parent = elem;
		if(elem.getName().equals(TITLE)) {
			parent = elem.getParent();
		} else {
			elem = parent.getChild(TITLE);
		}
		java.util.List children = parent.getChildren(TITLE);
		java.util.List allChildren = parent.getChildren();
		elem = ((org.jdom.Element)elem.clone()).detach();
		elem.setAttribute(CORRESP,sigla);
		elem.removeAttribute(TYPE);
		elem.removeChildren();
		elem.setText(" ");
		int tIndex = (children.size()>0?allChildren.indexOf(children.get(children.size()-1)):allChildren.size()-1);
		allChildren.add(tIndex+1,elem);
		returnElem = elem;

		grandparent = parent.getParent();
		if(grandparent != null)
		{
			if(grandparent.getName().equals(TDIV)) { // either its a title div

				pagin = grandparent.getChild(PAGIN);
				if(pagin == null) {
					System.out.println("Can't find pagination while inserting title element for:\n" +
							TiblEdit.outputString(grandparent) +"\n Adding directly to this element");
					pagin = grandparent;
				} else {
					elem = new org.jdom.Element(NUM);
					elem.setAttribute(CORRESP,sigla);
					elem.setText(pagination);
					pagin.addContent(elem);
				}

			} else if(grandparent.getName().equals(TLIST)) { // or a title list

				parent = new org.jdom.Element(TITEM);
				parent.setAttribute(CORRESP,sigla);
				child = new org.jdom.Element(DESIG);
				String[] specs = {"Which chapter in the " + sigla + " edition of the text is the source for this title?",
								  "Enter Chapter Number",JOP_INFO};
				String sectionNum = TiblEdit.promptInput(specs);
				child.setText(sectionNum);
				parent.addContent(child);
				child = new org.jdom.Element(PAGIN);
				child.setText(pagination);
				parent.addContent(child);
				grandparent.addContent(parent);

			} else if(grandparent.getName().equals(TIBL)) { // or else it's a chapter TIBBIBL

				children = parent.getChildren();
				pagin = parent.getChild(SRC);
				int srcIndex = children.indexOf(pagin);
				if(srcIndex > -1) {
					parent.removeContent(elem);
					parent.getChildren().add(srcIndex,elem);
				} else {
					System.out.println("Could not find source in chapter title decl: " + TiblEdit.outputString(parent));
					pagin = new org.jdom.Element(SRC);
					parent.addContent(pagin);
				}
				child = new org.jdom.Element(NUM);
				child.setAttribute(CORRESP,sigla);
				child.setText(pagination);
				pagin.addContent(child);
			}
		}
		return returnElem;
	}

	/**
	* <p>
	* This returns the text of a title element as it is displayed in the {@link TextPane}. It gets this in the
	* same way the text pane does by using a {@link TitleParser} that finds the constituent parts of a title and
	* determines which are to be displayed. For instance, if there is an app element, it displays only the text of
	* the Lem child (i.e., the main reading).
	* </p>
	*
	* @param title org.jdom.Element - the title whose text is desired.
	*
	* @return String - the display text for that title element.
	*/
	public String getTitleText(org.jdom.Element title)
	{
		TitleParser tparse = new TitleParser(title);
		return tparse.getTitleText();
	}

	/**
	* <p>
	* This method takes a title element, a string of selected text, and an integer indicating the offset from
	* the start of the text's title where the apparatus element is to be inserted. It then uses a
	* {@link TitleParser} to figure out the display text of the title, and with the offset integer determines
	* where in the title the apparatus element is to be inserted. It receives that part from the TitleParser, splits it,
	* and adds the app element. If all succeeds, the app element is then returned.
	* </p>
	*
	* @param title org.jdom.Element - the title in which the app is to be inserted.
	* @param selText String - the text selected in the {@link TextPane} that indicates where the app is to be inserted.
	* @param offset int - the offset from the beginning of the text's display title, where the insertion point is.
	*
	* @return org.jdom.Element - the app element that has been successfully inserted or null if unsuccessfull.
	*/
	public org.jdom.Element insertApp(org.jdom.Element title, String selText, int offset)
	{
		// if title does not have corresp == to NG then return
		// can only add aps to critical versions of the title
		if(!title.getAttributeValue(CORRESP).equals(NG)) {
			System.out.println("The Title's corresp attribute does not equal Ng!\n" +
				TiblEdit.outputString(title));
			return null;
		}

		TitleParser tparser = new TitleParser(title);
		if(tparser.isAppInsertable(offset, selText))
		{
System.out.println("selected text is: " + selText);
			java.util.List children = title.getContent();
			Vector newContents = new Vector();
			Object child = tparser.getItemAt(offset);
			String text = tparser.getTextAt(offset);
System.out.println("TExt at title offset is: " + text);
			int index = text.indexOf(selText);

			String sigla = title.getAttributeValue(TYPE);
			if(sigla == null) { sigla = title.getAttributeValue(CORRESP);}
			if(sigla == null) { sigla = NG;}

			if(child != null && index > -1 && (index + selText.length())<=text.length()) {
				int childIndex = children.indexOf(child);
				if(childIndex > -1) {
					newContents.add(new org.jdom.Text(text.substring(0,index-1)+" "));
					org.jdom.Element app = newApp(sigla,selText);
					newContents.add(app);
					newContents.add(new org.jdom.Text(text.substring(index+selText.length())));
					children.remove(child);
					children.addAll(childIndex,newContents);
					return app;
				} else {
					System.out.println("Couldn't locate child in content list of element!");
					System.out.println("Title: " + TiblEdit.outputString(title));
					System.out.println("Child: " + child.toString());
				}

			} else {
				System.out.println("Could not split up text run properly! in Tib Doc insertApp! Or child was null");
				System.out.println("Title: " + TiblEdit.outputString(title));
				System.out.println("Text run: " + text);
				System.out.println("Sel text: " + selText);
				System.out.println("Index of: " + index);
				System.out.println("Child: " + child);
			}
		} else {
			System.out.println("Tparser rejected insert App!");
		}

		return null;
	}

	/**
	* <p>
	* This method removes an existing App element, replacing it with the text of its lemma or main reading child.
	* </p>
	*
	* @param appToRemove org.jdom.Element - the app to be removed.
	*/
	public void removeApp(org.jdom.Element appToRemove)
	{
		parent = appToRemove.getParent();
		if(parent == null) {return;}
		int index = parent.getContent().indexOf(appToRemove);
		element = appToRemove.getChild(LM);
		String text = new String(" ");
		if(element != null) {text = element.getText();}
		System.out.println("Replacing app with " + text);
		parent.getContent().set(index,new org.jdom.Text(text));
	}

	/**
	* <p>
	* This method is called when the Normalized title and/or translation is changed. It locates the text's
	* main Head element, which contains the text's header with its title, and changes the content of the
	* two title elements (tib and eng) in the header to accurately reflect the modified normalized title
	* and translation.
	* </p>
	*/
	public void updateTextHeader()
	{
		org.jdom.Element head, normTitle, foreign;
		org.jdom.Element[] heads;

		normTitle = tf.getNormalizedTitle();
		foreign = normTitle.getChild(FOREIGN);
		heads = tf.getHeaderTitles();
		if(heads != null) {
			heads[0].setText(normTitle.getText());
			if(heads.length>1) {
				heads[1].setText(foreign.getText());
			} else {
				element = new org.jdom.Element(TITLE);
				element.setAttribute(TYPE,TEXT_HEAD);
				element.setAttribute(LANG,ENG);
				element.setText(foreign.getText());
				heads[1].getParent().addContent(element);
			}
		} else {
			head = findElement(HEAD);
			element = new org.jdom.Element(TITLE);
			element.setAttribute(TYPE,TEXT_HEAD);
			element.setAttribute(LANG,TIB);
			element.setText(normTitle.getText());
			head.addContent(element);
			element = new org.jdom.Element(TITLE);
			element.setAttribute(TYPE,TEXT_HEAD);
			element.setAttribute(LANG,ENG);
			element.setText(foreign.getText());
			head.addContent(element);
		}

	}

	// Static Methods

	/**
	* <p>
	* This static method is used to clean up an string derrived from XML content that may have paragraph returns
	* in the middle of it and preceding or trailing spaces, etc. It replaces all the "\n" with spaces and then
	* reduces all double spaces to single spaces as well as trimming the string of preceding or following spaces. If null,
	* is sent to this method, it returns an empty String.
	* </p>
	*
	* @param text String - the text to be cleaned.
	*
	* @return String - the clean text.
	*/
	public static String cleanString(String text)
	{
		if(text != null) {
			text = text.replace('\n',' ');
			StringTokenizer parts = new StringTokenizer(text," ");
			text = new String();
			while(parts.hasMoreTokens()) {
				String word = parts.nextToken();
				if(word != null && !word.equals(" ")) {
					text += word;
					if(parts.hasMoreTokens()) { text += " "; }
				}
			}
		} else {
			text = "";
		}
		return text;
	}

	/**
	* <p>
	* This static method is used to convert standard lists, such as Vectors, into arrays of org.jdom.Elements. This
	* is useful because the getContent method of Element returns a List and in iteration each item must be cast
	* back to an Element. This method returns a fixed array of Elements that do not require such casting.
	* </p>
	*
	* @param childs java.util.List - the list of content or children of an element.
	*
	* @return org.jdom.Element[] - the array of element children derrived from the list.
	*/
	public static org.jdom.Element[] toElementArray(java.util.List childs)
	{
		Object[] objs = childs.toArray();
		org.jdom.Element[] out = new org.jdom.Element[objs.length];
		for(int n=0; n<objs.length; n++)
			out[n] = (org.jdom.Element)objs[n];
		return out;
	}

/**
* <p>
* This generic static method simply creates an unattached version of an app element with the main reading set.
* It takes to parameters: the sigla of the main reading (lemma), and the text of the lemma. It returns an app
* element with the first child being this lem element and one empty rdg (reading) element for non-main-readings.
* </p>
*
* @param mainWit String - the sigla of the lemma's witness.
* @param lemma String - the text of the lemma's reading.
*
* @return org.jdom.Element - the app element created.
*/
	public static org.jdom.Element newApp(String mainWit, String lemma)
	{
		org.jdom.Element app, lem, rdg;
		app = new org.jdom.Element(AP);
		lem = new org.jdom.Element(LM);
		rdg = new org.jdom.Element(RDG);
		lem.setAttribute(WIT,mainWit);
		lem.setText(lemma);
		app.addContent(lem);
		app.addContent(rdg);
		return app;
	}

	public static org.jdom.Element makeDiscussion(DiscDialog dd, String inits)
	{
		org.jdom.Element disc = dd.getDiscussion(inits);
		disc.setAttribute(TYPE,dd.getDiscussionType());
		disc.setAttribute(RESP,inits);
		return disc;
	}


	public org.jdom.Element makeHI(String rend)
	{
		org.jdom.Element hi = new Element(HI);
		return hi.setAttribute(REND,rend);
	}

	// Constructor

/**
* <p>
* TibDoc has a single constructor that takes a root element for the document.
* </p>
*
* @param rootElement org.jdom.Element - the new TibDoc's root element.
*/
	public TibDoc(org.jdom.Element rootElement)
	{
		super(rootElement);
		setHead();
		setFactories();
		setDocType(new DocType(TIBL,".." + java.io.File.separatorChar + "bin" + java.io.File.separatorChar + "xtibbibl2.dtd"));
		org.jdom.Attribute idAt = getRootElement().getAttribute(ID);
		if(idAt != null && (idAt.getValue() == null || idAt.getValue().equals(""))) {
			idAt.detach();
		}
	}
}
