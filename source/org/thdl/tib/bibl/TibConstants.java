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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JOptionPane;

/**
* This interface contains all the constants used throughout the TiblEdit program. They are roughly organized
* according to type, but the organization could definitely be cleaned up some.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public interface TibConstants
{

	// File Constants
	final String DEFAULT_DIRECTORY 	= System.getProperties().getProperty("user.dir");
	final String BIN				= java.io.File.separatorChar + "bin" + java.io.File.separatorChar;
	final String DATA_DIRECTORY 	= java.io.File.separatorChar + "data" + java.io.File.separatorChar;
	final String OUT_DIRECTORY 		= java.io.File.separatorChar + "data" + java.io.File.separatorChar;
	final String BIN_LOGIN			= java.io.File.separatorChar + "bin" + java.io.File.separatorChar + "logs" + java.io.File.separatorChar;
	final String DIA_DATA			= "dia.dat";
	final String TEMP_DIR			= java.io.File.separatorChar + "bin" + java.io.File.separatorChar + "temp" + java.io.File.separatorChar;
	final String PREFS				= "ttprefs.ini";

	final String PREFS_DELIM			= "=";

	// Frame Constants
	final String PROG_NAME			= "TiblEdit: ";
	final String DEFAULT_HEADER 	= PROG_NAME + "An XML Editor for Tibetan Bibliographic Records";

	// Menu Constants
	// File Menu
	final String FILE			= 	"File";
	final String OPENFILE		=	"Open";
	final String TITLEOPENFILE	=	"Open by Title";
	final String CLOSEFILE		=	"Close";
	final String SAVE			=	"Save";
	final String SAVEAS			=	"Save As";
	final String EXPORT			=	"Export";
	final String EXIT			= 	"Exit";

	// Edit Menu
	final String EDIT			=	"Edit";
	final String TRANS_EDIT		= 	"Translation";
	final String NORM_EDIT		= 	"Normalized Title";
	final String REMOVE_APP		= 	"Remove Variants";
	final String REMOVE_TITLE	= 	"Remove Title";
	final String REMOVE_EDITION = 	"Remove Edition";

	// Insert Menu
	final String INSERT			=	"Insert";
	final String ED_INFO		= 	"New Edition Info";
	final String CRIT_TITLE		= 	"Critical Title";
	final String ED_TITLE		= 	"Edition Title";
	final String TITLE_DISC		=	"Insert Discussion";
	final String INSERT_APP		= 	"Variant Reading";

	// View Menu
	final String VIEW			= "View";
	final String TITLES_VIEW	= "View Titles";
	final String MASTER_ID_VIEW = "Master ID/Doxography";
	final String USER_ID_VIEW	= "Editor's Info";

	final String EDCON			= "Editions Consulted";
	final String DIAC			= "Diacritics";
	final String ABOUT 			= "About TiblEdit";

	// TibAction Constants (should these go in TibAction?
	final String CONTROLLER		= "controller";
	final String FRAME			= "frame";
	final String CURR_DIR		= "current directory";

	final String[] NGDIVS		= { "Atiyoga", "Anuyoga", "Mah\u0101yoga", "Unclassified" };
	final String[] NGSIGS		= { "Ng1", "Ng2", "Ng3", "Ng4" };
	final String OTHER			= "Other Edition";
	final String EDS_CORR		= "Editor's Correction";
	final String[] OTHER_EDS	= { "Tk","Dg","Kg","Wa","Bg",OTHER};
	final int     SELDIV		= 	0;
	final String GEN			= "General";


	// Button labels
	final String SUBMIT 		=	"Submit Translations";
	final String ENTER			=   "Enter";
	final String CANCEL			= 	"Cancel";
	final String ADD_DOX		= 	"Add Doxographical Sub-category";

	// Labels & Headers
	final String LABEL_SPACE			=	"   ";
	final String CROSSREF				= 	"Cross-references:";
	final String NORM_TITLE_HEAD		= "Normalized Title\n";
	final String NORM_TIB_LABEL			= "Normalized Title (Tib): ";
	final String NORM_ENG_LABEL    		= "Normalized Title (Eng): ";
	final String TITLE_LINE_HEAD		= "\nTitle Line\n";
	final String EOC_TITLES_HEAD		= "\nEnd of Chapter Titles\n";
	final String NON_TIB_TITLE_HEAD		= "\nNon-Tibetan Title\n";
	final String CHAP_TITLES_HEAD		= "\nChapter Titles\n";
	final String CLOSE_TITLE_HEAD		= "Back Titles";
	final String NON_TIB_HEAD			= "\nNon-Tibetan Title\n";
	final String SRCS_LABEL				= "Sources: ";
	final String ENTER_PLACE			= "  \n";
	final String ENTER_TRANS_PHRASE		= "\nEnter Translation: ";
	final String ENTER_TITLE_PHRASE		= "\nEnter Title: ";
	final String ENTER_NORMALIZED_PHRASE= "Normalized title: ";
	final String ENTER_NORM_TRANS_PHRASE= "Translation: ";
	final String DOX_WINDOW_TITLE		= "Doxographic Classification/Master ID Number";
	final String DISC_DIA_TITLE			= "Discussion Entry Form";
	final String SEPARATOR				= "------------------------------------\n";

	// Fonts
	final String BASE_FONT_NAME	= 	"Arial Unicode MS";
		  int    BASE_FONT_SIZE	=	14;
		  int 	 HEAD_FONT_SIZE = 	16;
		  int	 TEXT_HEAD_SIZE = 	18;
	final Font DEFAULT_FONT		=	new Font(BASE_FONT_NAME,Font.PLAIN,BASE_FONT_SIZE);
	final Font TITLE_FONT		=	new Font(BASE_FONT_NAME,Font.ITALIC,BASE_FONT_SIZE);
	final Font MENU_FONT		= 	new Font(BASE_FONT_NAME,Font.PLAIN,12);
	final Font BOLD_FONT		=	new Font(BASE_FONT_NAME,Font.BOLD,BASE_FONT_SIZE);
	final Font HEAD_FONT		=   new Font(BASE_FONT_NAME,Font.BOLD,HEAD_FONT_SIZE);

	// Styles (names)
	final String BOLD			= 	"bold";
	final String ITALIC			= 	"italics";

	// Sizes
	final Dimension TIBPANEL_SIZE       = new Dimension(10,10);
	final Dimension BUTTON_SIZE			= new Dimension(100,30);
	final Dimension SHORT_LABEL_SIZE	= new Dimension(100,75);
	final Dimension LABEL_SIZE			= new Dimension(500,75);
	final Dimension ED_SCROLL_SIZE		= new Dimension(400,400);
	final Dimension BUTT_PANEL_SIZE		= new Dimension(500,500);
	final Dimension DEFAULT_PANEL_SIZE 	= new Dimension(550,600);

	final int TTFCOLS					= 30;
	final int TEXT_FIELD_SIZE			= 15;

	// Colors
	final Color DEFAULT_BGCOLOR 		= new Color(250,250,250);// 250,235,215);  // antique white
	final Color DEFAULT_BUTTON_COLOR 	= new Color(200,180,200);
	final Color LIST_COLOR				= Color.blue;
	final Color TEXT_COLOR				= Color.black;

	// Insets & Borders
	final Insets DEFAULT_INSETS			= new Insets(10,0,10,10);
	final Insets LIST_INSETS			= new Insets(10,10,10,1000);
	final Insets MAIN_MARGINS			= new Insets(10,10,10,10);
	final Insets BUT_MARGINS			= new Insets(25,25,25,25);

	// Integer Constants (choices)
	final int NORM				= 1; // Normalized title choice & normal display mode choice
	final int TITLELINE			= 2; // Title line choice
	final int EOC				= 3; // End of chapter titles choice.
	final int CLOSING			= 4; // Closing section titles.
	final int NONTIBET			= 5; // Non-Tibetan title choice.
	final int CHAPS				= 6; // Chapter titles.

	// Display Modes
	final int ENTER_TRANS		= 2; // for entering a translation
	final int NEW_TITLE 		= 3; // for entering a new title
	final int NEW_AP			= 4; // for entering a new app
	final int CANCEL_NEW_APP	= 5; // When a new app entry is cancelled.
	final int SHOW_AP			= 6; // when showing an old app
	final int ENTER_NORMALIZED	= 7; // when entering a normalized title.
	final int CANC				= 8; // for cancelling
	final int DO_TRANS			= 9; // for editing a translation

	final String YES			= "yes";
	final String NO				= "no";
	final String IS_FILE_LIST	= "filelist";
	final String AP_CHECK		= "Checking app!";
	final String ED_TITLE_REM	= "Removing Edition Title";
	// Observer types and hashkeys
	final String TABLE				= "table";
	final String APP_SUBMIT			= "Submit App";
	final String REDISPLAY			= "Redisplay";
		// also uses AP for app and TYPE for type keys.
	final String RECENT				= "recent_file";
	final int RECENT_FILE_SIZE		= 8;
	// XML Constants
	// sigla constants
	final String NG				= "Ng";
	final String TB				= "Tb";
	final String TK				= "Tk";
	final String DG				= "Dg";
	final String BG				= "Bg";
	final String KG				= "Kg";

	final String[] EDNAMES = {"Master","mTshams brag","gTing skyes","sDe dge","Bai ro'i rgyud 'bum","sKyid grong"};

	// Element and Attribute Constants
	final String TIBL			= "tibbibl";		// Element/root name
	final String ID 			= "id";   		// Attribute name
	final String LVL			= "level";		// Attribute name on Tibbibls (either, volume, text, chapter)
	final String ED				= "edition";	// Attribute value
	final String SIG			= "sigla";		// Attribute value
	final String CLASS			= "class";		// Attribute value
	final String LET			= "letter";		// Attribute value
	final String TXT			= "text";		// Attribute value
	final String CHAPTER		= "Chapter";	// Attribute value
	final String DIV 			= "div1";  		// Element name part
	final String TYPE			= "type"; 		// Attribute name
	final String SUBTYPE		= "subtype";	// Attribute name
	final String TEXT			= "text"; 		// Attribute value for DIVs 3 or 4.
	final String TIBBIBL		= "tibbibl"; 	// Element name for text's root element.
	final String TITLE			= "title"; 		// Element name
	final String NORM_TITLE		= "Normalized title"; // Attribute value (TYPE)
	final String FOREIGN		= "foreign";	// Element name
	final String LANG			= "lang"; 		// Attribute name
	final String TIB			= "tib"; 		// Attribute value
	final String ENG			= "eng";		// Attribute value
	final String N				= "n";			// Attribute name
	final String P				= "p";			// Element name
	final String TITLEGRP	    = "titlegrp";	// Element that holds all the titles
	final String TDECL			= "titledecl";	// Element name
	final String TLIST			= "titlelist";	// Element name
	final String HEAD			= "head";		// Element name
	final String CONTROL		= "controlinfo";// ELement name
	final String SID			= "sysid";		// Element name
	final String RESPDECL		= "respdecl";	// Element name
	final String RESPSTM		= "respStmt";	// Element name
	final String RESP			= "resp";		// Element name
	final String PERSNAME		= "persname";	// Element name
	final String NAME			= "name";		// Element name
	final String DATE			= "date";		// Element name
	final String REVDESC		= "revisiondesc";// Element name
	final String TRANS			= "Translator";	// Element content
	final String TRANS_JOB		= "Translated and Edited titles."; // Element content
	final String CHANGE			= "change";		// Element name
	final String LIST			= "list";		// Element name
	final String ITEM			= "item";		// Element name
	final String TIDDECL		= "tibiddecl";	// Element name
	final String TID			= "tibid";		// Element name
	final String ALT			= "altid";		// Element name
	final String PHYSDEC		= "physdecl";	// Element name
	final String PAGIN			= "pagination";	// Element name
	final String AP				= "app";		// Element name
	final String LM				= "lem"; 		// Element name
	final String EXPAN			= "expan";		// Element name
	final String ABBR			= "abbr";		// Element name
	final String INTDECL	    = "intelldecl";	// Element name
	final String DOX			= "doxography"; // Element name
	final String DOXTYPE		= "Doxography"; // Attribute value for doxogrpahy name elements
	final String CATEG			= "category";	// Attribute value for Type of Doxography
	final String RELAT			= "relation";	// Attribute value for Type of Doxography
	final String SRC			= "source";	    // Element name
	final String SYS			= "system";		// Attribute name
	final String NUMB			= "number";		// Attribute value on TIBID
	final String TXTHEAD		= "text header";// Attribute value for TITLE type
	final String TITLELN		= "title line"; // Attribute value for Title subtype
	final String WIT			= "wit";		// Attribute name
	final String RDG			= "rdg";		// Element name
	final String TINFO			= "titleinfo";	// Element name
	final String TDIV			= "titlediv";	// Element name
	final String TITEM			= "titleitem";
	final String DESIG			= "designation";
	final String CORRESP		= "corresp";	// Attribute name
	final String FRONT			= "front";
	final String BODY			= "body";
	final String BACK			= "back";
	final String NONTIB 		= "nontibet";
	final String ORIG_LANG		= "Original language"; // Attribute value
	final String TEXT_HEAD		= "text header";// Attribute value for type of title
	final String RS				= "rs";			// Element name
	final String DISC			= "discussion"; // Element name
	final String SUMMARY		= "summary";    // Disc Element Type attribute
	final String SECTIONS		= "sections";	// Element name
	final String TIBANAL		= "tibanal";	// Element name
	final String NUM			= "num";		// Element name
	final String VOL			= "volume";		// Attribute value;
	final String NOTE			= "note";		// Element name
	final String HI				= "hi";			// Element name
	final String CIT			= "cit";		// Element name (changed to QUOTE)
	final String QUOTE			= "quote";		// Element name
	final String REND			= "rend";		// Attribute name;
	final String INFO			= "info";		// Attribute (rend) value;
	final String CREATOR		= "creator";	// attribute value in control info
	final String CONTROL_SOURCE	= "source";		// attribute value in control info
	final String CRIT_ED_LABEL	= "crited"; 	// Attribute value in control info
	final String CRIT_ED_DESC	= "Critical Editing"; // element text phrase for control info

	final String EDCONS			= "Editions consulted"; // Rend value of Source for editions consulted.
	final String MASTER_SIGLA   = "Ng";			// Sigla for the Master edition
	final String ATIYOGA		= "a ti yo ga ";// Tibetan spelling
	final String ANUYOGA		= "a nu yo ga ";
	final String MAHAYOGA		= "ma h\u0101 yo ga ";
	final String MISC			= "Miscellaneous";
	final String UNCLASSED		= "Unclassified";
	final String NOTFOUND		= "not found";	// Attribute value for type on RDG elements
	// The following three use partial strings that are unique but allow to do indexOf to ignore capitalization and punctuation.
	final String NOT_SPEC		= "specified"; // Contents of a title element when title missing.
	final String NO_TITLE		= "title given"; // Contents of a title element when none is given.1z
	final String UNTITLED		= "ntitled"; // Contents of untitled chapter. U not included to match all cases (Don't know why I didn't use equalsIgnoreCase()? Check it out!)
	final String BRIEF			= "Brief";   // Type attribute value for discussion, brief means no paragraphs.
	final String FULL			= "Full";	 // Type attribute value for discussion, full means with paragraphs (changed value to long, more descriptive).
	final String SIGLA_DELIM	= " ";  		// delimits between sigla in a wit attribute
	final String DATE_DELIM		= "-";
	final String DOX_DELIM		= ".";			// delimits between dox numbers in ID
	final String DOX_CAT_DELIM	= ":";			// delimits between names of dox categories.
	final org.jdom.Element NO_ELEM		= new org.jdom.Element("null");
	// To take up a place in vector when there is no element associated with a string/style run in TextPanel

	// Constants for Edition Attributes

	final String ED_NAME		= "EDITION_LETTER";
	final String ED_SIGLA		= "EDITION_SIGLA";
	final String VOL_NUM	 	= "VOLUME_NUMBER";
	final String VOL_LET		= "TIB_LETTER";
	final String TEXT_NUM		= "TEXT_NUMBER";
	final String VOL_TEXT		= "VOL_TEXT_NUMBER";
	final String PAGE_RANGE		= "PAGINATION";

	final org.jdom.Attribute TYPE_ED = new org.jdom.Attribute(TYPE,ED);
	final org.jdom.Attribute TYPE_VOL = new org.jdom.Attribute(TYPE,VOL);
	final org.jdom.Attribute TYPE_TEXT = new org.jdom.Attribute(TYPE,TXT);
	final org.jdom.Attribute TYPE_CLASS = new org.jdom.Attribute(TYPE,CLASS);
	final org.jdom.Attribute SYS_LET = new org.jdom.Attribute(SYS,LET);
	final org.jdom.Attribute SYS_SIG = new org.jdom.Attribute(SYS,SIG);
	final org.jdom.Attribute SYS_NUM = new org.jdom.Attribute(SYS,NUM);
	final org.jdom.Attribute LANG_TIB = new org.jdom.Attribute(LANG,TIB);
	final org.jdom.Attribute LANG_ENG = new org.jdom.Attribute(LANG,ENG);
	final org.jdom.Attribute CORRESP_NG 	= new org.jdom.Attribute(CORRESP,NG);

	final org.jdom.Attribute TYPE_DOX = new org.jdom.Attribute(TYPE,DOX);

	// Char Constants
	final char  BAD_PARA 		= ((char)(10));	// Para mark in data file is not understood.
	final char  SPACE 			= ' ';			// A space to replace it with.

	// Messages
	final String TTITLE_TITLE 		= "Java Title Translation Program";
	final String RUNTIME_ERROR		= "A runtime error has occurred in ";
	final String EDITOR_NAME_MESSAGE = "Please your initials for identification.";
	final String EDITOR_NAME_TITLE 	= "Editor Identification";
	final String DATE_MESSAGE 		= "Is today's date: ";
	final String DATE_TITLE 		= "Confirm Date";
	final String INPUT_DATE_MESSAGE = "Please enter the date (YYYY-MM-DD):";
	final String INPUT_DATE_TITLE 	= "Enter Today's Date";
	final String OPEN_TITLE 	= "Open XML Tibbibl Record";
	final String TEXT_LIST_HEAD 	= "List of Texts in Tb Volume ";
	final String EX1				= "\"1"+DOX_DELIM + "3" +DOX_DELIM+"6\"";
	final String EX2				= "\"sems sde " + DOX_CAT_DELIM + " sna tshogs\"";

	// JOptionPane Messages
	final String JOP_ERROR			= Integer.toString(JOptionPane.ERROR_MESSAGE);
	final String JOP_INFO			= Integer.toString(JOptionPane.INFORMATION_MESSAGE);
	final String JOP_WARN			= Integer.toString(JOptionPane.WARNING_MESSAGE);
	final String JOP_QUEST			= Integer.toString(JOptionPane.QUESTION_MESSAGE);
	final String JOP_YESNOCANCEL	= Integer.toString(JOptionPane.YES_NO_CANCEL_OPTION);

	final String[] OPEN_ERROR		 = {"Could not open the XML file:\n",
										"Unable to open file!",
										JOP_ERROR};
	final String[] DO_ALL_TITLES 	= {"Add this translation for all identical titles " +
										"in this text?",
										"Use Translation for Identical Titles?",
										JOP_QUEST};
	final String[] ED_EXISTS_MESSAGE 	= {"The sigla you have entered has " +
										   "already been added to the list of sources.",
										   "Duplicate Sigla Error",
										   JOP_ERROR};

	final String   NEW_ED_INFO			= "New Edition Information";
	final String   ED_STRING				= "Edition's name";
	final String   ED_SIG				= "Edition Sigla";
	final String   ED_TEXT_NUM			= "Edition Text Number";
	final String   ED_VOL_NUM			= "Edition Volume Number";
	final String   ED_VOL_LET		   	= "Edition Volume Letter";
	final String   VOL_TEXT_NUM			= "Text Number in Volume";
	final String   FULL_TEXT_PAGE		= "Full Pagination of Text";
	final String   TITLE_PAGE			= "New Title's Pagination";
	final String[] NEW_ED_INFO_SPECS	= {ED_STRING,ED_SIG,ED_TEXT_NUM,ED_VOL_NUM, ED_VOL_LET,
											VOL_TEXT_NUM, FULL_TEXT_PAGE, TITLE_PAGE};

	final String 	NEW_ED_TITLE_INFO 	= "New Edition Title Information";
	final String[] 	NEW_ED_TITLE_INFO_SPECS = {ED_SIG,TITLE_PAGE};
	final String 	GET_DESIG			= "Chapter Designation of Title Source";
	final String 	GET_DESIG_QUESTION  = "Enter the chapter number for the source of this title: ";
	final String[]	GET_DESIG_SPECS		= {GET_DESIG_QUESTION,GET_DESIG,JOP_QUEST};

	final String 	TLIST_WARN_TITLE	= "Insertion Not Allowed!";
	final String 	TLIST_WARN			= "An edition title cannot be inserted into a list of End-of-Chapter titles." +
											"\nUse Insert Title List under the insertion menu instead.";
	final String[]	TLIST_WARNING		= {TLIST_WARN,TLIST_WARN_TITLE,JOP_WARN};

	final String 	EDITORS_INFO		= "Editor's Information";
	final String	EDITORS_NAME		= "Your name";
	final String	EDITORS_INITIALS	= "Your full initials";
	final String[] 	EDITORS_INFO_SPECS	= {EDITORS_NAME,EDITORS_INITIALS};

	final String 	NOT_SPEC_TITLE		= "Not Specified Title";
	final String 	NOT_SPEC_MESSAGE	= "The title you have chosen as the base for a critical title\n" +
										  "is \"Not specified.\" If you wish to enter a title from another source, \n"+
										  "use 'Insert Edition Title' from the Insert Menu. Then, use that as the \n" +
										  "basis of a critical title and translation.";
	final String[] 	NOT_SPEC_SPECS		= {NOT_SPEC_MESSAGE,NOT_SPEC_TITLE,JOP_ERROR};

	final String 	NO_TITLE_TITLE		= "No Title Given";
	final String 	NO_TITLE_MESSAGE	= "The title you have chosen as the base for a critical title\n" +
										  "informs us there was \"No title given.\" If you wish to enter a title from another source, \n"+
										  "use 'Insert Edition Title' from the Insert Menu. Then, use that as the \n" +
										  "basis of a critical title and translation.";
	final String[] 	NO_TITLE_SPECS		= {NO_TITLE_MESSAGE,NO_TITLE_TITLE,JOP_ERROR};

	final String	NOT_SAVED_TITLE		= "Not Saved!";
	final String	NOT_SAVED_MESSAGE	= "Do wish to save changes you have made to this file?";
	final String[]	 SAVE_SPECS			= {NOT_SAVED_MESSAGE,NOT_SAVED_TITLE,JOP_YESNOCANCEL};

	final String 	INV_TRANS_TITLE		= "Cannot Edit Translation";
	final String 	INV_TRANS			= "The cursor is not placed in a valid title or translation for editing.\n" +
										  "The cursor must be positioned in a master version of a title (Ng) or in\n" +
										  "its translation for the Edit Translation option.";
	final String[]	INVALID_TRANS_SPECS = {INV_TRANS,INV_TRANS_TITLE,JOP_ERROR};

	final String 	RENAME_FILE_TITLE	= "Tibbibl File Renamed!";
	final String 	RENAME_FILE			= "The Tibbibl file has been renamed to match the change in its Master ID. \n" +
										  "The new file name is: ";
	final String[]	RENAMING_FILE_SPECS	= {RENAME_FILE,RENAME_FILE_TITLE,JOP_INFO};

	final String	SAVED_FILE_TITLE	= "Tibbibl File Saved!";
	final String	SAVED_FILE			= "The Tibbibl file has been saved under a name that matches its Master ID. \n" +
										  "The file name is: ";
	final String[] 	SAVED_FILE_SPECS	= {SAVED_FILE,SAVED_FILE_TITLE,JOP_INFO};

	final String	FILE_EXISTS_TITLE	= "File Already Exists!";
	final String 	FILE_EXISTS			= "The file name generated for this text already exists.\n" +
										  "Do you wish to replace the existing file?\n" +
										  "(If you choose, 'no' another name will be automatically generated for this text.)\n"+
										  "File name: ";
	final String[]	FILE_EXISTS_SPECS	= {FILE_EXISTS,FILE_EXISTS_TITLE,JOP_YESNOCANCEL};

	final String	ED_EXISTS_TITLE		= "Rewrite Edition Info?";
	final String 	ED_EXISTS			= "The edition you entered is already found in this texts identification information.\n" +
										  "Do you wish to replace this information?";
	final String[] 	ED_EXISTS_SPECS		= {ED_EXISTS,ED_EXISTS_TITLE,JOP_YESNOCANCEL};

	final String	REMOVE_APP_TITLE	= "Remove Variants at Cursor?";
	final String	REMOVE_APP_MESS		= "Do you want to remove the variant readings at the cursor position?\n" +
										  "They will be replaced by the main reading now showing.";
	final String[] 	REMOVE_APP_SPECS	= {REMOVE_APP_MESS,REMOVE_APP_TITLE,JOP_YESNOCANCEL};

	final String	NO_SELECT_TITLE		= "No Text Selected for Variant Readings";
	final String	NO_SELECTION		= "You have not selected any text. You must select a range of text \n"+
										  "to which the variant readings will apply.";
	final String[] 	NO_SELECTION_SPECS	= {NO_SELECTION,NO_SELECT_TITLE,JOP_ERROR};

	final String	CREATE_FILE_LIST_TITLE	= "No File List!";
	final String	CREATE_FILE_LIST	= "No file list has been created.\nWould you like to build the file list?\n" +
										  "This may take some time...";
	final String[] 	CREATE_FILE_LIST_SPECS = {CREATE_FILE_LIST,CREATE_FILE_LIST_TITLE,JOP_YESNOCANCEL};

	final String	REMOVE_ED_TITLE_TITLE = "Remove Added Title?";
	final String	REMOVE_ED_TITLE_MESS  = "Do you wish to remove this title which you have added?";
	final String[]	REMOVE_ED_TITLE_SPECS =  {REMOVE_ED_TITLE_MESS,REMOVE_ED_TITLE_TITLE,JOP_YESNOCANCEL};

	final String	REMOVE_ED_ERROR_TITLE	= "Cannot Remove Title!";
	final String	REMOVE_ED_ERROR_MESS	= "The title selected was not entered by you and so cannot be removed by you.";
	final String[]	REMOVE_ED_TITLE_ERROR 	= {REMOVE_ED_ERROR_MESS,REMOVE_ED_ERROR_TITLE,JOP_INFO};

	final String 	DEL_EDS_TITLE		= "Delete Inserted Edition!";
	final String 	DEL_EDS_MESS		= "In the table below, double click on the row\nfor the edition you wish to remove.\n" +
										  "(You can only remove editions that you have added.)";

	final String	TLIST_ADD_TITLE		= "Cannot Add Edition Title";
	final String	TLIST_ADD_MESS		= "A new edition title cannot be added to end of chapter titles.\n" +
										  "You can still create a master version of this title and provide alternate readings\n" +
										  "By double clicking on the existing title and using the \"Insert Variant Reading\" option.";
	final String[] 	TLIST_ADD_ERROR		= {TLIST_ADD_MESS,TLIST_ADD_TITLE,JOP_ERROR};

	final String	NORM_AP_TITLE		= "Cannot Add Reading to Normalized Title";
	final String	NORM_AP_MESS		= "You cannot add a reading to the normalized title.\n" +
										  "The normalized title is an assigned normative title not necessarily\n" +
										  "Found in the text itself. Thus, variant readings cannot be added to it.";
	final String[] 	NORM_AP_ERROR		= {NORM_AP_MESS,NORM_AP_TITLE,JOP_ERROR};

	final String	ED_ID_TITLE			= "Editor's Identification Information Required!";
	final String	ED_ID_MESS			= "This program cannot function properly unless the user properly identifies him or herself.\n" +
										  "Please enter your full name and your 2 or 3-letter editor initials/ID in the following window.\n" +
										  "(To quit the program without entering your information, press cancel in the next window.)";
	final String[] 	ED_ID_REQUIRED		= {ED_ID_MESS,ED_ID_TITLE,JOP_ERROR};
}
