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

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;

import org.jdom.output.XMLOutputter;

/**
* <p>
* TiblEdit is a editor for the master catalog records of the
* <i>rNying ma rgyud 'bum</i>. These XML records are displayed in an editor window
* so that the user can enter certain information into them. It is primarily geared
* toward allowing the user to critically edit and translate titles. Hence, the name.
* However, it also at the same time provides the option to assign doxographical
* genres and other critical information that are then stored in the XML file with
* valid mark-up.
* </p>
*
* @author Than Garson, Tibetan and Himalayan Digital Library
* @version 1.0
*/

public class TiblEdit implements MouseListener, KeyListener, Observer, TibConstants
{
	// Attributes
	/**
	* <p>
	* This is the main window or the view for the <code>TiblEdit</code> class, which acts
	* as the controller.
	* </p>
	*/
	TibFrame tibFrame;

	/**
	* <p>
	* This is a reader for XML files that returns a {@link TibDoc}.
	* </p>
	*/
	XMLReader xreader = new XMLReader();

	/**
	* <p>
	* This instantiation of XMLOutputter is used for writing the XML files. It is different
	* from the static instantiation of the same class, the variable {@link #xop}, that is used
	* for debugging purposes to print out string versions of elements.
	* </p>
	*/
    XMLOutputter XMLOut = new XMLOutputter("");

	/**
	* <p>
	* This is the folder where the data, or unprocessed master catalog records, are held.
	* </p>
	*/
	String dataDirectory;

	/**
	* <p>
	* This is the folder where the files are written to.
	* </p>
	*/
	File outDirectory;

	/**
	* <p>
	* This is the directory currently being worked in, is updated as user navigates to different
	* folders with the <code>JFileChooser</code>.
	* </p>
	*/
	File currentDirectory;

	/**
	* <p>
	* This is the presently open file that is being worked on.
	* </p>
	*/
	File currentFile;


	/**
	* <p>
	* This is the list of recently opened files.
	* </p>
	*/
	Vector recent;

	/**
	* <p>
	* This is the name of the file which is changed when the master ID is changes.
	* </p>
	*/
	String fileName;

	/**
	* <p>
	* This is the {@link TibDoc} that serves as the data model for the open file/text.
	* It contains the XML tibbibl record that is presently being worked on.
	* </p>
	*/
	TibDoc tibbibl;

/**
* The name of the editor using TiblEdit. This is entered
* the first time the program is run and thereafter stored in a preferences file.
* Once entered, it can be changed through the options menu.
*/
	private String editorsName;

/**
* The initials of the editor using TiblEdit. This is entered
* the first time the program is run and thereafter stored in a preferences file.
* Once entered, it can be changed through the options menu.
*/
	private String editorsInitials;

/**
* The present date formatted as yyyy-mm-dd, obtained from the operating system and stored
* as metadata in each XML file written.
*/
	private String todaysDate;
/**
* <p>
* This is the file chooser object that will be modified in {@link #setFileChooser} so
* that it displays the text names instead of the file names when opening a file.
* </p>
*/
	private JFileChooser fc;

	/**
	* These need to be documented.
    */
    private boolean is_file_list;
/**
* <p>
* This <code>ElementList</code> contains a list of elements that are displayed in
* the {@link TextPane}. It connects the elements with the starting and ending position
* of their displayed text so that when a selection of text is made the program can look
* up the associated element in the element list. It is used to set the {@link #selected_element}
* variable.
* </p>
*/
	ElementList elemList;
/**
* <p>
* This is the element that is found at the caret position or the selected text within
* the {@link TextPane}. It is then processed in various methods depending on the
* choice selected. It is set by this object's {@link #mouseClicked} method or else by
* the TextPane itself when a menu option is chosen.
* </p>
*/
	private org.jdom.Element selected_element;
/**
* <p>
* These variables are generic JDOM Elements for parent, grandparent, element (or self),
* and child. They are used throughout the program.
* </p>
*/
	org.jdom.Element parent, grandparent, element, child;

/**
* <p>
* This is a {@link TextPane} that is gotten from {@link TibFrame} and is used in methods for inserting elements.
* </p>
*/
	TextPane tp;
/**
* <p>
* This is the current display/functioning mode of the program. It has four settings:
* <ol><li>{@link TibConstants#NORM} - normal
* <li>{@link TibConstants#ENTER_TRANS} - for entering translations
* <li>{@link TibConstants#NEW_TITLE} - for entering new versions of a title
* <li>{@link TibConstants#NEW_AP} - for entering variant readings within a title.
* </ol></p>
*/
	int mode;
/**
* <p>
* This is a variable that counts how many files have been outputted that have no
* file name. This is just in case something goes wrong and the file needs a name.
* Each time such a file is written, this is appended to the generic name and augmented one
* so the next file will have a unique name. It appears below, but the situation for its
* use never occurs. FIX!
* </p>
*/
	int outCount;

/**
* <p>This is the <code>Thread</code> used to run the splash screen while the program
* loads.
* </p>
*/
	protected static Thread t;


/**
* <p>
* This variable tells whether a text has been saved or not.
* </p>
*/
	boolean hasBeenSaved;

	Hashtable edNames;

	Hashtable fileHash;

	TibDialog editorsDialog;

    // For testing and debugging purposes.
    /**
    * <p>
    * This static instanciation of an {@link org.jdom.output.XMLOutputter} is used by
    * the static method {@link #outputString}, to print XML mark-up for debugging
    * purposes.
    * </p>
    */
    public static XMLOutputter xop = new XMLOutputter();


	//init method
	/**
	* <p>
	* The init method sets the {@link #dataDirectory}, {@link #currentDirectory},
	* and the {@link #tibFrame} variables. It reads in preferences from {@link #readPrefData},
	* performs {@link #setDate} and {@link #setFileChooser}, and sets the {@link #mode}
	* to {@link TibConstants#NORM}.
	* </p>
	*/
	public void init()
	{
		showSplash();
		for(int fxc=0; fxc<100000; fxc++) { int nfx = fxc; }

		setDate();
		// set up log output stream
		File logDirectory = new File(DEFAULT_DIRECTORY + BIN_LOGIN);
		if(!logDirectory.exists()) {
			//System.out.println("Making log directory!");
			logDirectory.mkdir();
		}
		int n = 1;
		String logFileName = "Session_" + getDate() + "_" + n + ".txt";
		File logFile = new File(logDirectory, logFileName);
		while(logFile.exists()) {
			n++;
			logFileName = "Session_" + getDate() + "_" + n + ".txt";
			logFile = new File(logDirectory, logFileName);
			if(n>1000) {break;}
		}
		try {
			FileOutputStream logFos = new FileOutputStream(logFile);
			PrintStream logPs = new PrintStream(logFos, true);
			System.out.println("Resetting System and Error out to: \n\t" + logFile.getPath());
			System.setOut(logPs);
			System.setErr(logPs);
		} catch (FileNotFoundException fnfe) {
			System.out.println("File not found exception is opening log output stream: " + logFile.getName());
		} catch (SecurityException se) {
			System.out.println("Security exception in assigning log output stream!");
		}

		dataDirectory = DEFAULT_DIRECTORY + DATA_DIRECTORY;
		currentDirectory = new File(dataDirectory);
		outDirectory = new File(DEFAULT_DIRECTORY + OUT_DIRECTORY);
		tibFrame = new TibFrame(DEFAULT_HEADER, this);

		mode = NORM;
		hasBeenSaved = false;

		editorsDialog = new TibDialog(tibFrame,EDITORS_INFO,EDITORS_INFO_SPECS);
		editorsDialog.setSize(new Dimension(450,100));
		readPrefData();

		tibFrame.setRecentFiles(recent);

		edNames = new Hashtable();
		edNames.put(NG,EDNAMES[0]);
		edNames.put(TB,EDNAMES[1]);
		edNames.put(TK,EDNAMES[2]);
		edNames.put(DG,EDNAMES[3]);
		edNames.put(BG,EDNAMES[4]);
		edNames.put(KG,EDNAMES[5]);

	}

	/**
	* This reads in the preferences from the preference file that is located in
	* {@link TibConstants#DEFAULT_DIRECTORY} + {@link TibConstants#BIN} +
	* {@link TibConstants#PREFS}. The main preferences that are stored in this file
	* are: the {@link #editorsInitials} and {@link #editorsName}.
	* </p>
	*/
	public void readPrefData()
	{
		is_file_list = false;
		recent = new Vector();
		File prefFile = new File((DEFAULT_DIRECTORY + BIN + PREFS));
		if(prefFile.exists())
		{
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(prefFile));
				String line = new String();
				while(br.ready()) {
					line = br.readLine();
					int ind = line.lastIndexOf(PREFS_DELIM);
					System.out.println("Init line read: {"+line+"} = " +
							(line!=null?Integer.toString(line.length()):"null"));
					if(line == null || line.indexOf("null")>-1
					   || ind == -1 || ind>line.length()-2)
					{
						getEditorInfo();
					} else {

						if(line.indexOf(EDITORS_NAME)>-1) {
							editorsName = line.substring(ind+1);
						} else if (line.indexOf(EDITORS_INITIALS)>-1) {
							editorsInitials = line.substring(ind+1);
						} else if(line.indexOf(IS_FILE_LIST)>-1) {
							String is_there = line.substring(ind+1);
							if(is_there.equals(YES)) {is_file_list = true;}
						} else if(line.indexOf(RECENT)>-1) {
							String recentName = line.substring(ind+1);
							recent.add(new File(recentName));
							if(recent.size()>RECENT_FILE_SIZE) {recent.remove(0);}
						}
					}
				}
				br.close();
			}
			catch(IOException ioe) {
				System.out.println("An io exception occurred while reading in data from the text list.");
				System.out.println("The error was: " + ioe.getMessage() + " " + ioe.getClass().getName());
			}
		} else {
			getEditorInfo();
		}

	}

	/**
	* This method is called if there is no editor information in the preference file.
	* It uses a {@link TibDialog} with {@link TibConstants#EDITORS_INFO} title and
	* {@link TibConstants#EDITORS_INFO_SPECS}. When the <code>TibDialog</code> closes,
	* it calls {@link #setEditor}.
	* </p>
	*/
	public void getEditorInfo()
	{
		stopSplash();

System.out.println("in getEditorINfo!");

		if(editorsInitials != null) {
			editorsDialog.setValue(1,editorsInitials);
		}
		if(editorsName != null) {
			editorsDialog.setValue(0,editorsName);
		}
		editorsDialog.show();
	}

	/**
	* <p>
	* This method is called when the {@link TibDialog} for getting editor information
	* closes. The information is extracted from the <code>TibDialog</code> and
	* the variables {@link #editorsName} and {@link #editorsInitials} are set.
	* </p>
	*
	*/
	public void setEditor()
	{
		editorsName = editorsDialog.getValue(EDITORS_NAME);
		editorsInitials = editorsDialog.getValue(EDITORS_INITIALS);
System.out.println("EdName given: " + editorsName);
System.out.println("EdInits given: " + editorsInitials);

		if(editorsName == null || editorsName.length()<5 || editorsName.equals("null")
		   || editorsInitials == null || editorsInitials.length()<2 || editorsInitials.equals("null"))
		{
			   doMessage(ED_ID_REQUIRED);
			   editorsName = "";
			   editorsInitials = "";

			   getEditorInfo();
	    } else {
			editorsDialog.hide();
			editorsDialog = null;
		}
	}

	/**
	* <p>
	* This sets the {@link #todaysDate} variable to the current date in the form:
	* YYYY-MM-DD.
	* </p>
	*/
	public void setDate()
	{
		Calendar cal = new GregorianCalendar();
		int year, month, day;
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH)+1;
		day = cal.get(Calendar.DAY_OF_MONTH);

		todaysDate = year + DATE_DELIM + (month<10?"0":"") + month + DATE_DELIM
				+ (day<10?"0":"") + day;
	}

	/**
	* <p>
	* This method returns the value of the today's date variable.
	* </p>
	*
	* @return String the present day's date in the form yyyy-mm-dd
	*/
	public String getDate()
	{
		return todaysDate;
	}

	/**
	* <p>
	* Writes the preference data. This is read in and stored upon initialization of TiblEdit, when the
	* program is restarted.
	* </p>
	*/
	public void exit()
	{
		//System.out.print("Exiting ...");
		if(currentFile != null) {closeFile();}
		String outFile = DEFAULT_DIRECTORY + BIN + PREFS;
		File prefsFile = new File(outFile);
		try
		{
			PrintWriter out1 =
				new PrintWriter(
					new BufferedWriter(
						new FileWriter(prefsFile)));
			out1.println(EDITORS_NAME+PREFS_DELIM+editorsName);
			out1.println(EDITORS_INITIALS+PREFS_DELIM+editorsInitials);
			for(Iterator it = recent.iterator(); it.hasNext();)
			{
				File recentFile = (File)it.next();
				if(recentFile != null) {
					out1.println(RECENT+PREFS_DELIM+recentFile.getPath());
				}
			}
			out1.close();
		}

		catch(IOException ioe)
		{
			System.out.println("An I/O Exception occurred in writing the prefs file.");
			System.out.println("Message: " + ioe.getMessage());
			ioe.printStackTrace();
		}

		getFrame().dispose();
		System.exit(0);
	}
	// Accessors
	/**
	* <p>
	*  Sets the current directory to the given path.
	* </p>
	*
	* @param cd A <code>File</code> object that represents the path of the current directory.
	*/
	public void setCurrentDirectory(File cd)
	{
		currentDirectory = cd;
	}

	/**
	* <p>
	*  Returns the path for the current working directory.
	* </p>
	*
	* @return <code>File</code> - The current working directory as a File object.
	*/
	public File getCurrentDirectory()
	{
		return currentDirectory;
	}

	/**
	* <p>
	*  Returns the file that is currently being worked on.
	* </p>
	*
	* @return <code>File</code> - The currently open File object.
	*/
	public File getCurrentFile()
	{
		return currentFile;
	}

	/**
	* <p>
	* This sets the current display mode of the program. The values are:
	* <ol><li>{@link TibConstants#NORM} - normal
	* <li>{@link TibConstants#ENTER_TRANS} - for entering translations
	* <li>{@link TibConstants#NEW_TITLE} - for entering new versions of a title
	* <li>{@link TibConstants#NEW_AP} - for entering variant readings within a title.
	* </ol>
	* </p>
	*
	* @param m int - the value that sets the mode as above.
	*/
	public void setMode(int m)
	{
		mode = m;
	}

	/**
	* <p>
	* This returns the current display mode for the program.
	* </p>
	*
	* @return int - the current display mode
	*
	* @see #setMode
	*/
	public int getMode()
	{
		return mode;
	}

	/**
	* <p>
	* Returns the view or {@link TibFrame} that is the GUI of this program.
	* </p>
	*
	* @return <code>TibFrame</code> - The frame which displays the text information.
	*/
	public TibFrame getFrame()
	{
		return tibFrame;
	}

	/**
	* <p>
	* This returns the {@link TibDoc} presently being worked on.
	* </p>
	*
	* @return The <code>TibDoc</code> that is open.
	*/
	public TibDoc getTibDoc()
	{
		return tibbibl;
	}

	/**
	* <p>
	* Sets the {@link #selected_element} variable to the presently selected element.
	* </p>
	*
	* @param e org.jdom.Element - the selected element.
	*/
	public void setSelectedElement(org.jdom.Element e)
	{
		selected_element = e;
	}

	/**
	* <p>
	* Returns the presently selected element.
	* </p>
	*
	* @return org.jdom.Element - the selected element.
	*/
	public org.jdom.Element getSelectedElement()
	{
		return selected_element;
	}

	/**
	* <p>
	* Returns the editors initials.
	* </p>
	*
	* @return String - the initials.
	*/
	public String getEditorsInitials()
	{
		return editorsInitials;
	}

	/**
	* <p>
	* Returns the editors name.
	* </p>
	*
	* @return String - the editor's name.
	*/
	public String getEditorsName()
	{
		return editorsName;
	}

	/**
	* <p>
	* Recturns the vector containing the recent files opened.
	* </p>
	*
	* @return Vector the list of recent files
	*/
	public Vector getRecent()
	{
		return recent;
	}

	/**
	* <p>
	* This sets the {@link #fc} variable to a <code>JFileChooser</code> and modifies
	* it by adding an {@link XMLFilter} and a {@link TTFileView}. The latter serves
	* to change the <code>getName</code> function in the file chooser so that it
	* displays the Tibbibl's text name rather than the actual file name. The file name
	* is still used when the text is saved.
	* </p>
	*/
	public void setFileChooser()
	{
		fc = new JFileChooser(getCurrentDirectory());
		fc.setFileFilter(new XMLFilter());
		/*TTFileView ttfv = new TTFileView();
		fc.setFileView(ttfv);*/
		fc.setDialogTitle(OPEN_TITLE);
	}


	/**
	* <p>
	* This method sets the name of the file to be saved for a certain Tibbibl. It is invoked
	* when the Master ID is set so that the name of the file reflects the master ID.
	* </p>
	*
	* @param masterID String - the Master ID string that serves as the base of the file name
	*
	*
	*/
	public void setFileName(String masterID)
	{
		if(currentFile == null || masterID == null || masterID.trim().equals("")) {return;}
		removeFromRecent(currentFile);
		String currentFileName = currentFile.getName();
		File presDirectory = currentFile.getParentFile();

		StringTokenizer toker = new StringTokenizer(tibbibl.getNormalizedTitle()," ");
		String suffix = new String();
		for(int n=0;n<3;n++) {
			if(toker.hasMoreTokens()) {suffix += "_" + toker.nextToken();}
		}

		String masterName = masterID + suffix;
		tibbibl.setID(masterName);

		if(presDirectory.equals(outDirectory)) {
			File newFile = getNewFile(outDirectory, masterName);
			System.out.println("Current file deleted in 1: " + currentFile.delete());
			tibbibl.setSysid(newFile.getName());
			save(newFile);
			doMessage(RENAMING_FILE_SPECS, newFile.getName());
		} else {
			File newFile = getNewFile(outDirectory, masterName);
			System.out.println("Current file deleted in 2: " + currentFile.delete());
			tibbibl.setSysid(newFile.getName());
			save(newFile);
			doMessage(SAVED_FILE_SPECS, newFile.getName());
		}

	}

/**
* <p>
* This method returns the new File created from renaming a file according to its master doxographical
* classification. It is called once an entry has been made through the {@link DoxWindow} and the ID and
* Sysid have been changed. It takes the folder that new files are written to and the master ID name and
* returns the new file. It first checks to make sure the name is unique since it is build from the doxographical
* classification number and the first 3 words of the title. If there is a conflict, it begins to add numbers to
* the end of the name until a unique name is arrived at.
* </p>
*
* @param outDirectory File - this is the directory to which processed files are written.
*
* @param masterName String - the is the master name for the file, e.g. Ng3.1.5.gsang_ba_'dus
*
* @return File - the uniquely named file created by combining the two above and checking for conflicts.
*/
	public File getNewFile(File outDirectory, String masterName)
	{
		File renamedFile = new File(outDirectory, masterName + ".xml");
		int n = 2;
		if(renamedFile.exists()) {
			/*int response = doConfirm(FILE_EXISTS_SPECS,renamedFile.getName());
			if(response == JOptionPane.YES_OPTION) {return renamedFile;}*/
			masterName += "_" + n;
			renamedFile = new File(outDirectory, masterName + ".xml");
		}
		while(renamedFile.exists()) {
			/*int response = doConfirm(FILE_EXISTS_SPECS,renamedFile.getName());
			if(response == JOptionPane.YES_OPTION) {return renamedFile;}*/
			n++;
			masterName = masterName.substring(0,masterName.length()-2) + n;
			renamedFile = new File(outDirectory, masterName + ".xml");
			if(n==25) { break;}
		}
		if(renamedFile.exists()) {System.out.println("Warning checkFile has returned a file name that already exists!");}
		System.out.println("File name from check file: " + renamedFile.getName());
		return renamedFile;
	}

	// Helper methods


	/**
	* <p>
	* This method uses a JFileChooser to get the user to choose a file and then
	* calls {@link #openFile(File)} to open the selected file.
	* </p>
	*
	*/
	public void openFile()
	{
		stopSplash();
		setFileChooser();

		int returnVal = fc.showOpenDialog(getFrame());
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			openFile(fc.getSelectedFile());
		}
	}

	/**
	* <p>
	* With the <code>File</code> sent to it, this method uses
	* an instance of an {@link XMLReader} to read the file. It sets the <code>tibbibl</code>
	* variable to the TibDoc extracted by the reader. It then creates a {@link TextPane}, and
	* sends it to the internal method {@link #showTitles}, which displays titles from the TibDoc
	* in the TextPane. The text pane is then put in a <code>JScrollPane</code> and this is sent
	* to the {@link TibFrame} using its {@link TibFrame#fileOpened} method.
	* </p>
	*
	* @param file <code>File</code> the file to be opened, sent by the file chooser.
	*/

	public void openFile(File file)
	{
		stopSplash();
		if(xreader.setDoc(file))
		{
			System.out.println("Opening file: " + file.getName());
			currentFile = file;
			tibFrame.fileOpened();
			tibbibl = xreader.getDoc();
			tibbibl.addEditor(editorsInitials, editorsName,todaysDate);
			String temp = tibbibl.getNormalizedTitle();
			if(temp.length()>100) {
				int ind = temp.indexOf(' ',100);
				if(ind < 50) { ind = 100;}
				temp = temp.substring(0,ind) + " ...";
			}
			elemList = tibFrame.showTitles(tibbibl.getTitleFactory());
			hasBeenSaved = false;
			tibFrame.getTextPane().setCaretPosition(0);
			addToRecent(file);

		} else {
			doMessage(OPEN_ERROR);
			removeFromRecent(file);
		}
	}

	/**
	* <p>
	* This method takes a file and checks the recent file list to see if it is not already there.
	* If it is not, then it adds it to the list.
	* </p>
	*
	* @param newFile File - the file to be added to the recent list.
	*
	*/
	public void addToRecent(File newFile)
	{
		String nfName = newFile.getName();
		int listIndex = -1;
		for(Iterator it=recent.iterator();it.hasNext();)
		{
			File listFile = (File)it.next();
			if(listFile.equals(newFile) || listFile.getName().equals(nfName)) {
				listIndex = recent.indexOf(listFile);
				break;
			}
		}
		if(listIndex == -1) {
			recent.add(newFile);
			if(recent.size()>RECENT_FILE_SIZE) {recent.remove(0);}
		} else {
			if(recent.remove(recent.get(listIndex))) {recent.add(0,newFile);}
		}

	}

	/**
	* This method removes a file from the recent list if the controller (TiblEdit)
	* cannot open it.
	*/
	public void removeFromRecent(File badFile)
	{
		int index = recent.indexOf(badFile);
		if(index > -1) {recent.remove(index); tibFrame.setRecentFiles(recent);}
	}

	/**
	* <p>
	* Checks to make sure the file has first been saved. If not, then it prompts to save.
	* If it has then, it calls {@link TibFrame#fileClosed} and sets {@link #currentFile} to null.
	* </p>
	*/
	public void closeFile()
	{
		if(!hasBeenSaved) {
			int response = doConfirm(SAVE_SPECS);
			if(response == JOptionPane.YES_OPTION) {
				if(!saveTibbibl()) {return;}
			} else if(response == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		tibFrame.fileClosed();
		currentFile = null;
	}

	/**
	* <p>
	* This method saves the edited {@link TibDoc} which is {@link #tibbibl} as a new
	* document to the {@link TibConstants#OUT_DIRECTORY}, checking first to make sure
	* it does not already exist. If it does, it prompts to overwrite.
	* </p>
	*/
	protected boolean saveTibbibl()
	{
		if(tibbibl == null) {return false;}
		String outPath = DEFAULT_DIRECTORY + OUT_DIRECTORY;
		if(currentFile != null)
		{
			outPath += currentFile.getName();
		} else {
			outPath += "Ng.undef" + (outCount++) + ".xml";
		}

		File outFile = new File(outPath);
		/*if(outFile.exists())
		{
			String mess = "A file by the name of " + outFile.getName() + " already exists!\n" +
						  "Do you wish to write over it?";
			String messTitle = "File Exists! Overwrite?";
			int response = JOptionPane.showConfirmDialog(getFrame(), mess, messTitle,
					JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if(response == JOptionPane.NO_OPTION) {
				return saveAs(outFile);
			}
		}*/
		save(outFile);
		return true;
	}

	/**
	* <p>
	* This method actually writes the given file to disk. It is called from the {@link #saveTibbibl} method and
	* from the {@link #saveAs} method.
	* </p>
	*
	* @param outFile File - the file to be saved
	*/
	public void save(File outFile)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(outFile);
			DataOutputStream dos = new DataOutputStream(fos);
			XMLOut.output(tibbibl,fos);
			hasBeenSaved = true;
			currentFile = outFile;
			dos.close();
			fos.close();
			System.out.println("File, " + outFile.getName() + ", saved!");
			addToRecent(outFile);
		}
		catch (IOException ioe)
		{
			String mess = "An IO Exception occurred, while trying to save file (" +
					outFile.getAbsolutePath() + "). File did not write! ";
			String emess = ioe.getMessage();
			if(emess != null & emess.length()>1) {mess += emess;}
			System.out.println("Error: " + ioe.toString());
			System.out.println(mess);
			ioe.printStackTrace();
			return;
		}
	}


	/**
	* <p>
	* This method displays a Save As window through a JFileChooser. It is invoked from {@link #saveTibbibl} when
	* the user attempts to save a file that exists but choses not to write over it. If the save option is chosen,
	* it then calls {@link #saveTibbibl} which returns the success, creating a loop until either a unique file name is given,
	* the overwrite option is chosen, or it is cancelled.
	* </p>
	*
	* @param outFile File - the file
	*
	* @return boolean - whether the save operation was successful.
	*/
	public boolean saveAs(File outFile)
	{
		try
		{
			fc.setCurrentDirectory(outFile);
			int response = fc.showSaveDialog(tibFrame);
			if(response == JFileChooser.APPROVE_OPTION)
			{
				currentFile = fc.getSelectedFile();
				return saveTibbibl();
			}
		} catch (HeadlessException he) {return false;}
		return false;

	}

	/**
	* <p>
	* This method causes the {@link TibFrame} to display a table with the
	* information on variant readings at the bottom of its text pane. It is
	* invoked by double clicking on section of the {@link TextPane} that displays
	* in yellow background, indicating there is an app element there. The clicking
	* sets the selected element to that app element. If the area is not associated with
	* an app, nothing happens. If it is, this method first locates the associated
	* pagination and then calls the {@link TibFrame#showTable} method that takes
	* a {@link TibTable}. The TibTable is constructed using the selected element or app,
	* the {@link IDFactory} of the Tibbibl, and the pagination element.
	* </p>
	*/
	public void showApp()
	{
		if(selected_element == null) {
			// return if there is no selected element
			return;
		}

		// find the associated pagination element
		org.jdom.Element page = null;
		parent = selected_element.getParent().getParent().getParent();
		if(parent.getName().equals(TDIV)) {
			page = parent.getChild(PAGIN);
		} else if(parent.getName().equals(TIBBIBL)) {
			page = parent.getChild(PHYSDEC).getChild(PAGIN);
		}
		// if the pagination is found
		if(page != null) {
			// If it's a new app call the appropriate method to insert the yellow backgroun
			if(mode == NEW_AP) {tibFrame.displayNewApp();}
			//
			tibFrame.showTable(new TibTable(selected_element,tibbibl.getIDFactory(),page));
		}
	}

	public void insertNewEdition()
	{
		if(tibbibl != null)
		{

			TibDialog tdialog = new TibDialog(this,NEW_ED_INFO,NEW_ED_INFO_SPECS);
			tdialog.showDialog();
		}
	}

	public void submitNewEditionInfo(TibDialog tdia)
	{
		IDFactory idf = tibbibl.getIDFactory();
		String sigla = tdia.getValue(ED_SIG);
		if(idf.hasEdition(sigla)) {
			int response = doConfirm(ED_EXISTS_SPECS);
			if(response == JOptionPane.YES_OPTION)
			{
				idf.removeEd(sigla);
			}
		}
		idf.addEditionConsulted(tdia.getResults());
		idf.addSourceConsulted(sigla,editorsInitials,editorsName);
	}

	/**
	* <p>
	* This method inserts a title to be critically edited that is based on a particular
	* edition title. It is called when an edition title is double clicked or when
	* the corresponding insert command is chosen from the menu. It first locates the
	* element at the caret, makes sure it is a title and then calls {@link #enterTranslation}.
	* (Should it check for the title's type here?)
	* </p>
	*/
	public void insertCritTitle()
	{
		if(elemList == null) {return;}
		element = elemList.getElementAt(tibFrame.getCaretPosition());
		if(element != null && element.getName().equals(TITLE)) {
			selected_element = element;
			enterTranslation();
		}
	}

	/**
	* <p>
	* This method is for inserting a new edition title. Edition titles cannot be
	* critically edited. They represent titles as they appear in particular editions.
	* One choses a particular edition title as the basis for a critical title which
	* then can have app elements collating the various readings. This method uses
	* a {@link TibDialog} with {@link TibConstants#NEW_ED_TITLE_INFO} and
	* {@link TibConstants#NEW_ED_TITLE_INFO_SPECS} to get the title and its pagination.
	* When the <code>TibDialog</code> is submitted, the {@link #insertNewEdTitle} method
	* is called with that <code>TibDialog</code>.
	* </p>
	*/
	public void insertEdTitle()
	{
		if(tibbibl != null && checkCaretPosition(ED_TITLE))
		{
			TibDialog tdialog = new TibDialog(getFrame(),NEW_ED_TITLE_INFO,NEW_ED_TITLE_INFO_SPECS);
			tdialog.showDialog();
		}
	}

	/**
	* <p>
	* This is called when a {@link TibDialog} is submitted with new edition title information.
	* It checks to see if the sigla of the new title (i.e., the edition it is from) already
	* exists in the texts TibidDecl, using the tibbibl's {@link IDFactory}. If it does not,
	* exist, another {@link TibDialog} is called with {@link TibConstants#NEW_ED_INFO} and
	* {@link TibConstants#NEW_ED_INFO_SPECS} to get all the relevant information. When that
	* second <code>TibDialog</code> is closed, the {@link #insertNewEdAndTitle} method
	* is invoked. Otherwise, if the edition is known, it calls {@link TibFrame#showTitles}
	* with an {@link TitleFactory} and a {@link #mode} equal to {@link TibConstants#NEW_TITLE}
	* to display a place to enter the new title.
	* </p>
	*
	* @param tdia TibDialog - The TibDialog with the information of the new title's sigla and pagination.
	*/
	public void insertNewEdTitle(TibDialog tdia)
	{
		String sigla = tdia.getValue(ED_SIG);
		IDFactory idf = tibbibl.getIDFactory();
		if(idf.hasEdition(sigla)) {
			setSelectedElement(tibbibl.addTitle(selected_element,sigla,tdia.getValue(TITLE_PAGE)));
			mode = NEW_TITLE;
			elemList = tibFrame.showTitles(tibbibl.getTitleFactory());
			setCaretPosition();
		} else {
			String page = tdia.getValue(TITLE_PAGE);
			tdia = new TibDialog(getFrame(),NEW_ED_INFO,NEW_ED_INFO_SPECS);
			String edName = (String)edNames.get(sigla);
			if(edName != null) {tdia.setValue(0,edName);}
			tdia.setValue(1,sigla);
			tdia.setValue(7,page);
			tdia.showDialog();
		}
	}

	/**
	* <p>
	* This enters the information from the {@link TibDialog} called by {@link #insertNewEdTitle}
	* into the TibidDecl of the Tibbibl and then calls {@link TibFrame#showTitles} with
	* the {@link TitleFactory} of the Tibbibl and a {@link #mode} set to {@link TibConstants#NEW_TITLE}.
	* This displays an entry place for the new title in the {@link TextPane}.
	* </p>
	*
	* @param tdia TibDialog - the TibDialog with the information concerning the new edition.
	*/
	public void insertNewEdAndTitle(TibDialog tdia)
	{
		IDFactory idf = tibbibl.getIDFactory();
		idf.addEditionConsulted(tdia.getResults());
		idf.addSourceConsulted(tdia.getValue(ED_SIG),editorsInitials,editorsName);
		tdia.dispose();
		setSelectedElement(tibbibl.addTitle(selected_element,tdia.getValue(ED_SIG),tdia.getValue(TITLE_PAGE)));
		mode = NEW_TITLE;
		elemList = tibFrame.showTitles(tibbibl.getTitleFactory());
		setCaretPosition();
	}

	/**
	* <p>
	* This method is called when one either double clicks on a title or chooses enter a translation
	* from the menu. It first determines if the selected element is the normalized title, in which case
	* it sets the {@link #mode} to {@link TibConstants#ENTER_NORMALIZED ENTER_NORMALIZED} and redisplays with the appropriate prompts.
	* Otherwise, it makes sure there is a Tibetan title there and not "No title given" or "Not specified" and
	* if it's a valid title, asks the user if they want to create a critical title with the selected title
	* as its base. If so, it redisplays with the appropriate prompts.
	*
	*</p>
	*/

	public void enterTranslation() {
		if(selected_element.getName().equals(TITLE)) {
			String type = selected_element.getAttributeValue(TYPE);
			if(type != null && type.indexOf("Normalized")>-1) {
				mode = ENTER_NORMALIZED;
			} else {
				parent = selected_element.getParent();
				child = parent.getChild(TITLE);
				if(child != null) {
					// If there are a list of Title elements in this titleDecl, the first
					// one will always be the critical edition of the title with corresp=Ng
					// if it is not there, then it needs to be added.
					String corresp = child.getAttributeValue(CORRESP);
					if(corresp != null && corresp.equals(NG)) {
						selected_element = child;
					} else {
						if(corresp == null) {
							System.out.println("No corresp element: \n" + outputString(child));
						}

						String text = selected_element.getText();
						if(text.indexOf(NOT_SPEC)>-1) {
							doMessage(NOT_SPEC_SPECS);
							mode = CANC;
							tibFrame.getTextPane().addCaretListener(tibFrame);
							return;
						}
						if(text.indexOf(NO_TITLE)>-1) {
							doMessage(NO_TITLE_SPECS);
							mode = CANC;
							tibFrame.getTextPane().addCaretListener(tibFrame);
							return;
						}

						String mess = "Do you wish to create a critical edition for this title, \n" +
										"using the following title as the base: \n";
						String title = TibDoc.cleanString(selected_element.getText());
						if(title.length()>50) {
							int half = title.length()/2;
							int index = title.indexOf(" ",half);
							if(index == -1) {mess += title;} else {
								mess += title.substring(0,index) + "\n" + title.substring(index+1);
							}
						} else {
							mess += title;
						}
						mess += "\nEdition: " + selected_element.getAttributeValue(CORRESP);
						String[] specs = {mess,"Create Critical Title",
											Integer.toString(JOptionPane.YES_NO_OPTION)};
						int resp = doConfirm(specs);
						if(resp == JOptionPane.YES_OPTION) {
							mode = ENTER_TRANS;
							selected_element = tibbibl.createCriticalTitle(selected_element,editorsInitials);
						}
					}
				}
			}

			elemList = tibFrame.showTitles(tibbibl.getTitleFactory());
			//setCaretPosition();
		}
	}


	/**
	* <p>
	* This method is called when a new translation has been entered into the
	* {@link TextPane} and Enter has been pressed. It locates the text of the new
	* translation in the TextPane from the {@link TibConstants#ENTER_TRANS_PHRASE}
	* prompt to the next paragraph mark "/n", and then calls the {@link TibDoc#addTranslation}
	* method with this translation <code>String</code> and the {@link #selected_element}.
	* The display mode ({@link #mode}) is then set to {@link TibConstants#NORM} and
	* the text is redisplayed using {@link TibFrame#showTitles} which returns
	* an {@link ElementList} that is assigned to {@link #elemList}.
	* </p>
	*/
	private void insertTranslation()
	{
		tp = tibFrame.getTextPane();
		String trans = tp.getText();

		try {
			trans = trans.substring(trans.indexOf(ENTER_TRANS_PHRASE) + ENTER_TRANS_PHRASE.length());
			trans = trans.substring(0,trans.indexOf("\n"));
			if(trans != null) {trans = trans.trim();}
			tibbibl.addTranslation(trans,selected_element);
		} catch (IndexOutOfBoundsException ibe)
		{
			System.out.println("Index out of bounds setting translation:\n" + trans + "\n" +
				ibe.getMessage());
				ibe.printStackTrace();
		}

		mode = NORM;
		selected_element = null;
		elemList = tibFrame.showTitles(tibbibl.getTitleFactory());
		//setCaretPosition();
	}

	/**
	* <p>
	* This method is called when inserting an Edition title within a title decl. This is a title
	* that belongs to a particular edition and is to be used as a source for a critical title.
	* The infromation is received from a {@link TibDialog} and then a prompt for the title in the
	* textpane, which is then read and placed in the title element.
	* </p>
	*/
	private void insertNewTitleAndTranslation()
	{

		String title_text, trans, full_text;
		tp = tibFrame.getTextPane();
		full_text = tp.getText();
		trans = new String();
		try {
			int enterPhraseindex = full_text.indexOf(ENTER_TITLE_PHRASE);
			if(enterPhraseindex == -1) {
				enterPhraseindex = full_text.indexOf(ENTER_TITLE_PHRASE.substring(0,ENTER_TITLE_PHRASE.length()-2));
			}
			if(enterPhraseindex>-1) {

				title_text = full_text.substring(enterPhraseindex+ENTER_TITLE_PHRASE.length()-2);
				title_text = title_text.substring(0,title_text.indexOf("\n"));
				if(title_text.substring(0,1).equals(":")) {title_text = title_text.substring(1);}
				if(title_text != null) {
					selected_element.setText(title_text.trim());
				}
			}

		} catch (IndexOutOfBoundsException ibe)
		{
			System.out.println("Index out of bounds setting translation:\n" + trans + "\n" +
				ibe.getMessage());
			ibe.printStackTrace();
		}

		mode = NORM;
		selected_element = null;
		elemList = tibFrame.showTitles(tibbibl.getTitleFactory());
		//setCaretPosition();
	}

/**
* <p>
* This method updates the information for the normalized title after it has been entered into
* the {@link TextPane} by the editor and Enter has been pressed. It replaces the text within
* the Title tag for the Tibetan and its child Foreign tag for the english translation. It then
* calls {@link TibDoc#updateTextHeader() the updateTextHeader} method in {@link TibDoc} and
* redisplays.
* </p>
*/
	private void updateNormalizedTitle()
	{
		String title_text, trans, full_text;
		tp = tibFrame.getTextPane();
		full_text = tp.getText();
		trans = new String();
		try {
			title_text = full_text.substring(full_text.indexOf(ENTER_NORMALIZED_PHRASE));
			title_text = title_text.substring(ENTER_NORMALIZED_PHRASE.length());
			title_text = title_text.substring(0,title_text.indexOf("\n"));
			if(title_text.substring(0,1).equals(":")) {title_text = title_text.substring(1);}
			if(title_text != null) {
				selected_element.setText(title_text.trim());
			}
			org.jdom.Element foreign = selected_element.getChild(FOREIGN);
			if(foreign == null) {
				foreign = new org.jdom.Element(FOREIGN);
				foreign.setAttribute(LANG,ENG);
				selected_element.addContent(foreign);
			}
			trans = full_text.substring(full_text.indexOf(ENTER_NORM_TRANS_PHRASE));
			trans = trans.substring(ENTER_NORM_TRANS_PHRASE.length());
			trans = trans.substring(0,trans.indexOf("\n"));
			if(trans.substring(0,1).equals(":")) {trans = trans.substring(1);}
			if(trans != null) {
				foreign.setText(trans.trim());
			}


		} catch (IndexOutOfBoundsException ibe)
		{
			System.out.println("Index out of bounds setting translation:\n" + trans + "\n" +
				ibe.getMessage());
			ibe.printStackTrace();
		}

		mode = NORM;
		selected_element = null;
		tibbibl.updateTextHeader();
		elemList = tibFrame.showTitles(tibbibl.getTitleFactory());
		//setCaretPosition();

	}

	/**
	* <p>
	* This method is called when a selection of title text is made and the insert
	* variant reading choice is taken from the Insert menu. It first checks to make
	* sure the caret is in the appropriate place using {@link #checkCaretPosition} with
	* the param {@link TibConstants#AP}. If it is a viable insertion point, it calculates
	* the offset of the selection from the previous paragraph mark and calls
	* {@link TibDoc#insertApp} using the {@link #selected_element}, the selected text,
	* and this offset from the last paragraph integer. The {@link #mode} is then set
	* to {@link TibConstants#NEW_AP} and the {@link #showApp} method is called.
	* </p>
	*/
	public void insertApp()
	{
		if(checkCaretPosition(AP)) {
			if(selected_element == null) {
				System.out.println("Selected element is null in insertApp!");
				return;
			}
			TextPane tp = tibFrame.getTextPane();
			String docText = "";
			try {
				docText = tp.getDocument().getText(0,tp.getDocument().getLength());
			} catch (BadLocationException ble) {
				docText = tp.getText();
				System.out.println("Can't get documents text!");
			}
			String selText = tibFrame.getTextPane().getSelectedText();
			if(selText == null || selText.length()==0) {
				System.out.println("Nothing selected!");
				doMessage(NO_SELECTION_SPECS);
			} else {

				int selStart = tp.getSelectionStart();
				int prevPara = docText.lastIndexOf("\n",selStart); // had a +1
				int postPara = docText.indexOf("\n",selStart);
				int offset = selStart - prevPara;
				selected_element = tibbibl.insertApp(selected_element,selText,offset);
				setMode(NEW_AP);
				tibFrame.hideEditions();
				showApp();
			}
		}
	}

	/**
	* <p>
	* This method inserts a particular type of discussion depending on the setting of the
	* type parameter (FIXME: this routine no longer takes this parameter). When type equals {@link TibConstants#TITLE TITLE}, a discussion will be inserted
	* for the title at the cursor position of the {@link TextPane}. When type equals {@link TibConstants#GEN GEN}
	* the user will be prompted with a list of options to choose from.
	* </p>
	*
	* The parameter type is the string indicating which typeof discussion to insert.
	*/
	public void insertDiscussion()
	{
		DiscDialog discDia = tibFrame.getDiscussionDialog(tibbibl);
		discDia.showDiscussionDialog();
	}

	/**
	* This method takes a TitleDecl or any grouping element that might contain a discussion element and
	* checks to see if it has a prose discussion within it. If so, this prose discussion element (i.e.,
	* one that has type=brief or type=full) is returned. Otherwise, null is return. It is used also by {@link DiscDialog}
	* and so needs to be static.
	*/

	public static org.jdom.Element locateDisc(org.jdom.Element parent)
	{
		org.jdom.Element outDisc = null;
		List discEls = parent.getChildren(DISC);
		for(Iterator it = discEls.iterator(); it.hasNext();) {
			outDisc = (org.jdom.Element)it.next();
			String discType = outDisc.getAttributeValue(TYPE);
			if(discType.equalsIgnoreCase(BRIEF) || discType.equalsIgnoreCase(FULL)) {
				return outDisc;
			}
		}
		return null;
	}

	/**
	* <p>
	* This method is used to display a text's variant titles. It first calls
	* {@link TibFrame#hideEditions} to hide any table displaying at the bottom.
	* It then sets the mode to {@link TibConstants#NORM} and calls {@link TibFrame#showTitles}
	* </p>
	*/
	public void showTitles()
	{
		tibFrame.hideEditions();
		if(tibbibl == null) {return;}
		if(mode == CANCEL_NEW_APP) {
			tibbibl.removeApp(selected_element);
		}
		mode = NORM;
		elemList = tibFrame.showTitles(tibbibl.getTitleFactory());
		//setCaretPosition();
	}

	/**
	* <p>
	*  This method is called from the {@link TibTable} which is observed by this
	*  controller. When it is submitted, it notifies this observer and this method is run.
	*  It takes a hashtable of arguments, as follows:
	* 	<ul><li><b>Key:</b> {@link TibConstants#AP} -- the App that is being modified
	*  <li><b>Key: </b> {@link TibConstants#TABLE} -- the {@link TibTable.TibTableModel} that contains the data.
	*  <li><b>Key:</b> {@link TibConstants#PAGIN} -- the hashtable of paginations keyed on edition sigla, used to create table
	*	</ul>
	*  First, it sets the information for the main reading found in the {@link TibConstants#LM} or lemma element.
	*  Then, it correlates the reading witnesses already in the app element with the siglas
	*  for the lines of data in the table and replaces the information in existing reading elements
	*  or adds new ones where necessary.
	* </p>
	**/
	public void doAppSubmission(Hashtable args)
	{
		org.jdom.Element app, rdg, pagination, numEl;
		TibTable.TibTableModel ttm;
		Hashtable pageNums = new Hashtable();
		java.util.List readings;
		org.jdom.Element lemma, reading, newElem;
		String sigla, page;

		// Get info from hash table
		app = (org.jdom.Element)args.get(AP);
		ttm = (TibTable.TibTableModel)args.get(TABLE);
		pagination = (org.jdom.Element)args.get(PAGIN);
		if(app == null || ttm == null || pagination == null) {
			System.out.println("Not enough arguments sent to doAppSubmission in Trans Title. Returning with nothing done!");
			return;
		}

		app.removeChildren();

		for(Iterator it=pagination.getChildren(NUM).iterator();it.hasNext();) {
			numEl = (org.jdom.Element)it.next();
			sigla = numEl.getAttributeValue(CORRESP);
			if(sigla == null || sigla.equals("")) {
				System.out.println("Pagination with no sigla while trying to write APP!");
				continue;
			}
			pageNums.put(sigla,numEl);
		}

		newElem = new org.jdom.Element(RDG);

		for(int r=0;r<ttm.getRowCount();r++) {
			if(ttm.isValidRow(r)) {
				sigla = ttm.getStringAt(r,0);
				if(ttm.isMainRow(r)) {
					lemma = new org.jdom.Element(LM);
					lemma.setAttribute(WIT,sigla);
					lemma.setText(ttm.getStringAt(r,3));
					app.getChildren().add(0,lemma);
				} else {
					newElem = new org.jdom.Element(RDG);
					newElem.setAttribute(WIT,sigla);
					newElem.setText(ttm.getStringAt(r,3));
					app.addContent(newElem);
				}
				if(!pageNums.containsKey(sigla)) {
					page = ttm.getStringAt(r,2);
					if(page == null || page.equals("") || page.equals(" ")) { page = getEditorsInitials();}
					numEl = new org.jdom.Element(NUM);
					numEl.setAttribute(CORRESP,sigla);
					numEl.setText(page);
					pagination.addContent(numEl);
				} else {
					pageNums.remove(sigla);
				}
			}
		}

		for(Enumeration en=pageNums.elements();en.hasMoreElements();) {
			numEl = (org.jdom.Element)en.nextElement();
			numEl.detach();
		}

		/*
		// Assign to hash.
		appReads = new Hashtable();
		Vector toRemove = new Vector();
		it = readings.iterator();
		while(it.hasNext())
		{
			reading = (org.jdom.Element)it.next();
			sigla = reading.getAttributeValue(WIT);
			if(sigla == null) {
				toRemove.add(reading);
			} else {
				appReads.put(sigla,reading);
			}
		}


		// Create Page hash from pagination element
		pageNums = new Hashtable();
		it = pagination.getChildren(NUM).iterator();
		while(it.hasNext()) {
			num = (org.jdom.Element)it.next();
			sigla = num.getAttributeValue(CORRESP);
			pageNums.put(sigla,num);
		}

		// Do the main row which is the lemma
		mainr = ttm.getMainRow();
		lemma = app.getChild(LM);
		if(lemma == null) {
			lemma = new org.jdom.Element(LM);
			app.addContent(lemma);
		}
		sigla = ttm.getStringAt(mainr,0);
		read = ttm.getStringAt(mainr,3);
		if(sigla == null) {
			// there must be a lemma, so if there's an error put Ng (master sigla).
			sigla=NG;
			System.out.println("There is no sigla for the lemma. Assigning to 'Ng'.");
		}
		if(read == null) {read = "";}
		lemma.setAttribute(WIT,sigla.trim());
		lemma.setText(read);

		// do its pagination
		num = (org.jdom.Element)pageNums.get(sigla);
		if(num == null) {
			num = new org.jdom.Element(NUM);
			num.setAttribute(CORRESP,sigla);
			pagination.addContent(num);
		}

		num.setText(ttm.getStringAt(mainr,2));
		if(!tibbibl.getIDFactory().hasEdition(sigla)) {
			num.setAttribute(N,"v-"+ttm.getStringAt(mainr,1));
		}

		// go through table and assign values to rdgs
		for(int r=0; r<ttm.getRowCount(); r++) {
			sigla = ttm.getStringAt(r,0);
			if(sigla == null || sigla.trim().equals("")) {continue;}
			if(r == mainr) {
				rdg = (org.jdom.Element)appReads.get(sigla);
				if(rdg != null) {app.removeContent(rdg);}
				continue;
			}

			sigla = sigla.trim();
			vol = ttm.getStringAt(r,1);
			page = ttm.getStringAt(r,2);
			read = ttm.getStringAt(r,3);
			if(read != null && !read.trim().equals(""))
			{
				rdg = (org.jdom.Element)appReads.get(sigla);
				if(rdg == null) {
					rdg = new org.jdom.Element(RDG);
					rdg.setAttribute(WIT,sigla);
					app.addContent(rdg);
				}
				rdg.setText(read);

				// do the num pagination
				num = (org.jdom.Element)pageNums.get(sigla);
				if(num == null) {
					num = new org.jdom.Element(NUM);
					num.setAttribute(CORRESP,sigla);
					pagination.addContent(num);
				}
				num.setText(page);
				if(!tibbibl.getIDFactory().hasEdition(sigla)) {
					num.setAttribute(N,"v-"+ttm.getStringAt(mainr,1));
				}
			} else {
				if(appReads.containsKey(sigla)) {
					try {
						rdg = (org.jdom.Element)appReads.get(sigla);
						app.removeContent(rdg);
						if(pageNums.containsKey(sigla)) {
							num = (org.jdom.Element)pageNums.get(sigla);
							pagination.removeContent(num);
						}
					} catch (NullPointerException npe) {
					}
				}
			}
			appReads.remove(sigla);

			pageNums.remove(sigla);
		}

		// Deal with any rdgs left over in appReads and pageNums....
		if(appReads.size()>0) {
			System.out.println("There are apps left over in Appread in TiblEdit\n" +
				"While doing table submission!\nThe Readings are: ");
				int c=1;
				for(Enumeration keys = appReads.elements();keys.hasMoreElements(); )
				{
					String item = (String)appReads.get(keys.nextElement());
					System.out.println(c + ": " + item);
				}
		}*/
		showTitles();
	}

	/**
	* <p>
	* This method is invoked from the Edit menu and allows the user to edit an already
	* existing translation for a title by inserting the Enter translation prompt prior to
	* the text of the translation and allowing the {@link TextPane} to be editable.
	* </p>
	*/
	public void editTranslation()
	{
		if(checkCaretPosition(TRANS_EDIT))
		{
			int caretPos = tibFrame.getTextPane().getCaretPosition();
			mode = DO_TRANS;
			elemList = tibFrame.showTitles(tibbibl.getTitleFactory());
			//tibFrame.getTextPane().setCaretPosition(caretPos);
		} else {
			doMessage(INVALID_TRANS_SPECS);
		}
	}

	/**
	* <p>
	* This method is invoked after a translation has been edited and the Enter key
	* has been pressed. It replaces the text of the Foreign element containing the
	* translation with the next text between the Enter translation prompt and the
	* subsequent paragraph return.
	* </p>
	*
	* @see #keyPressed - the implementation of the KeyListener that invokes this method.
	*/
	public void updateTranslation()
	{
		String trans, full_text;
		org.jdom.Element foreign;
		if(selected_element == null) {return;}
		tp = tibFrame.getTextPane();
		full_text = tp.getText();
		trans = new String();
		try {
			if(selected_element.getName().equals(TITLE)) {
				foreign = selected_element.getChild(FOREIGN);
			} else if(selected_element.getName().equals(FOREIGN)) {
				foreign = selected_element;
			} else {
				return;
			}

			trans = full_text.substring(full_text.indexOf(ENTER_TRANS_PHRASE));
			trans = trans.substring(ENTER_TRANS_PHRASE.length());
			trans = trans.substring(0,trans.indexOf("\n"));
			if(trans.substring(0,1).equals(":")) {trans = trans.substring(1);}
			if(trans != null) {
				foreign.setText(trans.trim());
			}


		} catch (IndexOutOfBoundsException ibe)
		{
			System.out.println("Index out of bounds setting translation:\n" + trans + "\n" +
				ibe.getMessage());
			ibe.printStackTrace();
		}

		mode = NORM;
		selected_element = null;
		elemList = tibFrame.showTitles(tibbibl.getTitleFactory());

	}

	public void removeApp()
	{
		int pos = getFrame().getTextPane().getCaretPosition();
		element = elemList.getElementAt(pos);
		if(element != null && element.getName().equals(AP))
		{
			int response = doConfirm(REMOVE_APP_SPECS);
			if(response == JOptionPane.YES_OPTION) {
				tibbibl.removeApp(element);
			}
			showTitles();
		}
		getFrame().getTextPane().setCaretPosition(pos);
	}

	public void removeTitle()
	{
		int pos = getFrame().getTextPane().getCaretPosition();
		element = elemList.getElementAt(pos);
		parent = element.getParent();
		if(element != null && element.getName().equals(TITLE))
		{
			String corspVal = element.getAttributeValue(CORRESP);
			org.jdom.Element sourceListing = tibbibl.findElement(NAME,ID,corspVal);
			String srcCorrVal = (sourceListing==null?null:sourceListing.getAttributeValue(CORRESP));
			if((corspVal != null && corspVal.equals(editorsInitials)) ||
			   (srcCorrVal != null && srcCorrVal.equals(editorsInitials))) {
				int response = doConfirm(REMOVE_ED_TITLE_SPECS);
				if(response == JOptionPane.YES_OPTION) {
					element.detach();
					removePageRefs(parent,corspVal);
					showTitles();
				}
			} else {
				doMessage(REMOVE_ED_TITLE_ERROR);
			}
		}
		getFrame().getTextPane().setCaretPosition(pos);
	}

	public void removePageRefs(org.jdom.Element parentEl, String corrsp)
	{
		if(parentEl.getChild(SRC) != null) {
			org.jdom.Element src = parentEl.getChild(SRC);
			int childIndex = -1;
			for(Iterator it=src.getChildren(NUM).iterator();it.hasNext();) {
				child = (org.jdom.Element) it.next();
				String crsAtt = child.getAttributeValue(CORRESP);
				if(corrsp.equals(crsAtt)) { childIndex = src.getChildren().indexOf(child); break; }
			}
			if(childIndex > -1) { child.detach(); childIndex = -1;}
			grandparent = parent.getParent();
			if(grandparent.getChild(PHYSDEC) != null) {
				grandparent = grandparent.getChild(PHYSDEC);
				parent = grandparent.getChild(PAGIN);
				for(Iterator it=parent.getChildren(NUM).iterator();it.hasNext();) {
					child = (org.jdom.Element) it.next();
					String crsAtt = child.getAttributeValue(CORRESP);
					if(corrsp.equals(crsAtt)) { childIndex = parent.getChildren().indexOf(child); break; }
				}
			}
			if(childIndex > -1) { child.detach(); childIndex = -1;}
		} else {
			grandparent = parentEl.getParent();
			int childIndex = -1;
			if(grandparent.getChild(PAGIN) != null) {
				parent = grandparent.getChild(PAGIN);
				for(Iterator it=parent.getChildren(NUM).iterator();it.hasNext();) {
					child = (org.jdom.Element) it.next();
					String crsAtt = child.getAttributeValue(CORRESP);
					if(corrsp.equals(crsAtt)) { childIndex = parent.getChildren().indexOf(child); break; }
				}
				if(childIndex > -1) { child.detach(); childIndex = -1;}
			}
		}
	}

	public void deleteEdition(org.jdom.Element edToDie)
	{
		if(getTibDoc() == null || edToDie == null) { return; }
		String corrsp = edToDie.getAttributeValue(CORRESP);
		org.jdom.Element edNameEl = tibbibl.findElement(NAME,ID,corrsp);
		boolean failure = false;
		String mess = new String();
		if(edNameEl != null)
		{
			String edNameCor = edNameEl.getAttributeValue(CORRESP);
			if(corrsp == null || edNameCor.equals(editorsInitials)) {
				edToDie.detach();
				edNameEl.detach();
				org.jdom.Element physdecl = tibbibl.findElement(PHYSDEC);
				org.jdom.Element numFound = null;
				for(Iterator it = physdecl.getChild(PAGIN).getChildren(NUM).iterator();it.hasNext();) {
					org.jdom.Element num = (org.jdom.Element)it.next();
					String numCorr = num.getAttributeValue(CORRESP);
					if(numCorr != null && numCorr.equals(corrsp)) {
						numFound = num;
						break;
					}
				}
				if(numFound != null) {
					numFound.detach();
					System.out.println("Detaching num in physdecl\n" + outputString(physdecl));
				}
				org.jdom.Element[] others = tibbibl.findElements(CORRESP,corrsp);
				for(int n=0; n<others.length;n++)
				{
					others[n].setAttribute(CORRESP,editorsInitials);
				}
			} else {
				String edName = edNameEl.getText();
				mess = "You did not insert this edition information. \n";
				if(edNameCor != null) {
					mess += "It was inserted by: " + edNameCor +
					   " (" + edName + ")";
				} else {
					mess += "It is from the catalog record: " + edName;
				}
				failure = true;
			}
		} else {
			mess = "Could not find edition information for " + corrsp;
			failure = true;
		}

		if(failure) {
			JOptionPane.showMessageDialog(getFrame(),mess,"Could Not Delete Edition Info",JOptionPane.ERROR_MESSAGE);
		} else {
			tibbibl.setFactories();
			JOptionPane.showMessageDialog(getFrame(),"Edition Info Removed!", "Edition Deleted",JOptionPane.INFORMATION_MESSAGE);
			tibFrame.hideEditions();
			tibFrame.showTitles(tibbibl.getTitleFactory());
			tibFrame.showEditions();
		}
	}

	public void showMasterDox()
	{
		if(tibbibl != null) {
			String masterID = tibbibl.getIDFactory().getMasterID();
			DoxWindow dw = new DoxWindow(DOX_WINDOW_TITLE,masterID,this);
		}
	}

	/**
	* <p>
	* This method is invoked when the {@link DoxWindow} is submitted. It calls upon the
	* {@link TibDoc} to {@link TibDoc#setDoxography(String, String) setDoxography} and then
	* to {@link TibDoc#setMasterID(String) setMasterID}.
	* </p>
	*
	* @param dw DoxWindow - the doxography selection window that supplies the users input.
	*/
	public void enterDox(DoxWindow dw)
	{
		tibbibl.setDoxography(dw.getDoxString(TIB),dw.getDoxString(ENG));
		tibbibl.setMasterID(dw.getID());
		setFileName(dw.getID());
	}

	// methods for displaying messages and confirms.
	/**
	* <p>
	* This is a generic method to display a {@link javax.swing.JOptionPane} message
	* dialog it takes a <code>String</code> array with three specs.
	* <ol><li>the message
	* <li>the title
	* <li>the message type as defined in <code>JOptionPane</code>.
	* </ol>
	* The latter spec is converted to an integer.
	* </p>
	*
	* @param specs String[] - the specifications for the confirm dialog.
	*/

	public void doMessage(String[] specs)
	{
		String mess, title;
		int type;
		mess = specs[0];
		title = specs[1];
		type = Integer.parseInt(specs[2]);
		try{
			JOptionPane.showMessageDialog(tibFrame,mess,title,type);
		} catch(HeadlessException he) {}
	}

	public void doMessage(String[] specs, String append)
	{
		specs[0] += " " + append;
		doMessage(specs);
	}

	/**
	* <p>
	* This is a generic method to display a {@link javax.swing.JOptionPane} confirm
	* dialog it takes a <code>String</code> array with three specs.
	* <ol><li>the message
	* <li>the title
	* <li>the message type as defined in <code>JOptionPane</code>.
	* </ol>
	* The latter spec is converted to an integer.
	*
	* @param specs String[] - the specifications for the confirm dialog.
	*
	* @return int - the response
	* </p>
	*/
	public int doConfirm(String[] specs)
	{

		String mess, title;
		int type;
		mess = specs[0];
		title = specs[1];
		type = Integer.parseInt(specs[2]);
		int response = JOptionPane.showConfirmDialog(tibFrame,mess,title,type);
		return response;
	}

	public int doConfirm(String[] specs, String append)
	{
		specs[0] += " " + append;
		return doConfirm(specs);
	}

/**
* <p>
* This method sets the caret position to just after the enter information prompt
* depending on the {@link #mode} at the time it is called. The mode gives an indication
* which prompt is being used and should be searched for to determine the corresponding index
* at which to set the caret. This is not working correctly. 11 needs to be added to the index
* for it to come out even approximately correct. Why is this?
* </p>
*/
	public void setCaretPosition()
	{
		String searchString = null;
		if(mode == NORM) {
			return;
		} else if(mode == ENTER_TRANS || mode == DO_TRANS) {
			searchString = ENTER_TRANS_PHRASE;
		} else if(mode == ENTER_NORMALIZED) {
			searchString = ENTER_NORMALIZED_PHRASE;
		} else if(mode == NEW_TITLE) {
			searchString = ENTER_TITLE_PHRASE;
		}
		if(searchString == null) { System.out.println("Null mode in set Caret Position of TiblEdit!"); return;}

		TextPane tp = tibFrame.getTextPane();
		String text = tp.getText();
		int index = text.indexOf(searchString);
		if(index>0) {
			tp.setCaretPosition(index);
		}
	}

	/**
	* <p>
	* This method checks the position of the caret to see if it is an appropriate
	* place to perform certain actions depending on the Type parameter supplied.
	* The types available are:
	* <ul><li>{@link TibConstants#ED_TITLE ED_TITLE} - for adding an edition title.
	* <li>{@link TibConstants#AP AP} - for adding variant readings (apparatus).
	* <li>{@link TibConstants#TRANS_EDIT TRANS_EDIT} - for editing the translation of a title.
	*</ul>
	* </p>
	*/
	public boolean checkCaretPosition(String type)
	{
		if(elemList == null) {return false;}
		element = elemList.getElementAt(tibFrame.getCaretPosition());
		String elemName = new String();
		if(element != null && !element.equals(NO_ELEM))
		{
			elemName = element.getName();
		} else {
			return false;
		}


		if(type.equals(ED_TITLE)) {
			if(elemName.equals(TITLE)
					&& (element.getAttributeValue(TYPE)==null
						|| !element.getAttributeValue(TYPE).equalsIgnoreCase(NORM_TITLE)))
			{
				grandparent = element.getParent().getParent();
				if(grandparent != null) {
					if(grandparent.getName().equals(TLIST)) {
						doMessage(TLIST_ADD_ERROR);
						setSelectedElement(null);
						return false;
					}
					setSelectedElement(element.getParent());
					return true;
				}
			} else {
				setSelectedElement(null);
				return false;
			}
		} else if(type.equals(AP)) {
			if(elemName.equals(TITLE))
			{
				String titleType = element.getAttributeValue(TYPE);
				if(titleType!=null && titleType.equals(NORM_TITLE)) {doMessage(NORM_AP_ERROR);return false;}
				String cor = element.getAttributeValue(CORRESP);
				if(cor != null && cor.equals(NG))
				{
					setSelectedElement(element);
					return true;
				}
			}
		} else if(type.equals(AP_CHECK)) {
			if(elemName.equals(TITLE))
			{
				String cor = element.getAttributeValue(CORRESP);
				if(cor != null && cor.equals(NG))
				{
					return true;
				}
			}
		} else if(type.equals(TRANS_EDIT)) {
			while(elemName != null && (elemName.equals(AP) ||elemName.equals(LM) || elemName.equals(RDG))) {
				element = element.getParent();
				elemName = element.getName();
			}

			if(elemName.equals(TITLE)) {
				String cor = element.getAttributeValue(CORRESP);
				if(cor != null && cor.equals(NG))
				{
					child = element.getChild(FOREIGN);
					if(child == null) {
						child = new org.jdom.Element(FOREIGN);
						child.setAttribute(LANG,ENG);
						element.addContent(child);
					}
					setSelectedElement(child);
					return true;
				}
			} else if(elemName.equals(FOREIGN)) {
				parent = element.getParent();
				elemName = parent.getName();
				String cor = parent.getAttributeValue(CORRESP);
				if(elemName != null && elemName.equals(TITLE)
					&& cor != null && cor.equals(NG))
				{
					setSelectedElement(element);
					return true;
				}
			}
		} else if(type.equals(ED_TITLE_REM)) {
			if(elemName.equals(TITLE)) {
				String cor = element.getAttributeValue(CORRESP);
				if(cor != null && !cor.equals(NG)) {return true;}
			}
		}

		return false;
	}

	// Static methods
	/**
	* <p>
	* This is a static method to display a {@link javax.swing.JOptionPane} input
	* dialog it takes a <code>String</code> array with three specs.
	* <ol><li>the message
	* <li>the title
	* <li>the message type as defined in <code>JOptionPane</code>.
	* </ol>
	* The latter spec is converted to an integer. It returns the <code>String</code>
	* that was inputed.
	* </p>
	* @param specs String[] - the specifications for the input dialog
	*
	* @return String - the string entered by the user.
	*
	*/
	public static String promptInput(String[] specs)
	{
		return JOptionPane.showInputDialog(null,specs[0],specs[1],Integer.parseInt(specs[2]));
	}

	/**
	* <p>
	* A public static method that converts a <code>Vector</code> into an array of <code>Strings</code>.
	* </p>
	*
	* @param v <code>Vector</code> The vector to be converted.
	*
	* @return <code>String[]</code> The resulting array of <code>Strings</code>.
	*/
	public static String[] toStringArray(Vector v)
	{
		Object[] objs = v.toArray();
		String[] out = new String[objs.length];
		for(int n=0; n<objs.length; n++)
			out[n] = (String)objs[n];
		return out;
	}

	/**
	* <p>
	* A public static method that converts an <code>Object</code> array into an array of <code>Strings</code>.
	* </p>
	*
	* @param objs <code>Object[]</code> The object array to be converted.
	*
	* @return <code>String[]</code> The resulting array of <code>Strings</code>.
	*/
	public static String[] toStringArray(Object[] objs)
	{
		String[] out = new String[objs.length];
		for(int n=0; n<objs.length; n++)
			out[n] = (String)objs[n];
		return out;
	}

	// for debugging
	/**
	* <p>
	* A method for debugging, it takes an {@link org.jdom.Element} and converts it to a
	* string, the way it would be viewed in a plain text editor, angle brackets, attributes, and all.
	* This is a public, static method that can be called from anywhere with this classes prefix.
	* </p>
	*
	* @param e <code>org.jdom.Element</code> The element to be turned into a string.
	*
	* @return <code>String</code> The resulting string version of the element.
	*/
	public static String outputString(org.jdom.Element e)
	{
		if(e == null) {return "No element!\n";}
		return xop.outputString(e);
	}


	// Constructor

	/**
	* <p>
	* The only constructor takes no arguments and simply calls the {@link #init} method.
	* </p>
	*/
	public TiblEdit()
	{
		init();

		// this is for the number of the written out file when there is no currentFile defined
		// If this is kept (which it probably won't be), it must be recorded upon the
		// program closing and read it upon reopening.
		outCount = 1;
	}

	public static void showSplash()
	{
		IntroSign is = new IntroSign();
		t = new Thread(is,"intro");
		t.start();
	}

	/**
	* <p>
	* This static method stops the splash screen from displaying by interrupting
	* the thread it is on {@link #t} and setting it to null.
	* </p>
	*/
	public static void stopSplash()
	{
		if(t != null) {t.interrupt();t=null;}
	}

	public static void main(String[] args)
	{
		try {
			UIManager.setLookAndFeel(
				    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage() + "\n" +
				e.getClass().getName());
		}
		TiblEdit app = new TiblEdit();
	}

	// Listener Methods

	// MouseEvent listener
	/**
	* <p>
	* This implementation of the mouse listener checks to make sure the source is
	* the {@link TextPane}. Then, it makes sure the {@link #mode} is {@link TibConstants#NORM}
	* to make sure action is viable. Thus, it only responds when the TextPane
	* is click when nothing else is being inserted etc. It makes sure its a double click
	* and there is an {@link ElementList} defined to check. It checks the {@link #elemList}
	* for the selected element, if it is a {@link TibConstants#TITLE}, then it calls
	* {@link #enterTranslation}. If it is an {@link TibConstants#AP}, then it calls
	* {@link #showApp}.
	* </p>
	*
	* @param me MouseEvent - the required parameter for this abstract method.
	*/
	public void mouseClicked(MouseEvent me)
	{
		if(t !=null) stopSplash();
		if(me.getSource() instanceof TextPane)
		{
			TextPane tp = (TextPane)me.getSource();
			int noc = me.getClickCount();
			int pos = tp.getCaretPosition();
			if(mode == NORM && elemList != null) {
				if(noc>1 ) {
					setSelectedElement(elemList.getElementAt(pos));
					if(selected_element != null) {
						String name = selected_element.getName();
						if(name.equals(TITLE)) {
							tp.removeCaretListener(tibFrame);
							enterTranslation();
						} else if(name.equals(AP)) {
							tp.removeCaretListener(tibFrame);
							mode = SHOW_AP;
							showApp();
						}
					} else {
						selected_element = null;
						tibFrame.getTextPane().setCaretPosition(pos);
					}
				} else {
					element = elemList.getElementAt(pos);
					if(element.getName().equals(AP)) {
						getFrame().removeAppItem.setEnabled(true);
					} else {
						getFrame().removeAppItem.setEnabled(false);
					}
				}
			} else if(mode == CANC) {
				mode = NORM;
				tibFrame.getTextPane().setCaretPosition(pos);
			}

		}
	}

	public void mousePressed(MouseEvent me) {}
	public void mouseReleased(MouseEvent me) {}
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}

	//Key Event Listner
	/**
	* <p>
	* This implementation of the KeyListener interface is used when information
	* is being added directly to the {@link TextPane} being displayed. If the
	* Enter key is pressed, the it checks the {@link #mode}. If the mode is
	* {@link TibConstants#ENTER_TRANS}, then it calls {@link #insertTranslation}.
	* If the mode is {@link #NEW_TITLE}, it calls {@link #insertNewTitleAndTranslation}.
	* </p>
	*
	* @param ke KeyEvent - the required parameter for this abstract method.
	*/
	public void keyPressed(KeyEvent ke) {
		if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
			int pos = tibFrame.getTextPane().getCaretPosition();
			if(mode == ENTER_TRANS) {
				insertTranslation();
				hasBeenSaved = false;
			} else if(mode == NEW_TITLE) {
				insertNewTitleAndTranslation();
				hasBeenSaved = false;
			} else if(mode == ENTER_NORMALIZED) {
				updateNormalizedTitle();
				hasBeenSaved = false;
			} else if(mode == DO_TRANS) {
				updateTranslation();
				hasBeenSaved = false;
			}
			setMode(NORM);

			tibFrame.getTextPane().setCaretPosition(pos);
		}
	}
	public void keyReleased(KeyEvent ke) {}
	public void keyTyped(KeyEvent ke) {
	}

	// Observer for TibTable App entries
	/**
	* <p>
	* The implementation of the <code>Observer</code> interface is used for listening
	* to the {@link TibTable} that displays variant reading information. That table
	* is displayed along with a button panel for submitting and canceling. When either
	* of those buttons are pressed. The TibTable notifies its observers (the TiblEdit
	* controller) with an argument of a <code>Hashtable</code> with keyed specs.
	* If the type key equals {@link TibConstants#APP_SUBMIT} then the {@link #doAppSubmission}
	* is called with the arguments. If it is {@link TibConstants#CANCEL}, then
	* it calls the {@link #showTitles} method with mode {@link TibConstants#CANCEL_NEW_APP}.
	* </p>
	*/
	public void update(Observable obs, Object arg)
	{
		if(obs instanceof TibTable)
		{
			int pos = tibFrame.getTextPane().getCaretPosition();
			Hashtable args = (Hashtable)arg;
			String type = (String)args.get(TYPE);
			if(type.equals(APP_SUBMIT)) {
				doAppSubmission(args);
				hasBeenSaved = false;
			} else if(type.equals(CANCEL)) {
				if(mode == NEW_AP) {setMode(CANCEL_NEW_APP);}
				showTitles();
			} else if(type.equals(REDISPLAY)) {
				showApp();
			}
			tibFrame.getTextPane().setCaretPosition(pos);
		}
	}
}

