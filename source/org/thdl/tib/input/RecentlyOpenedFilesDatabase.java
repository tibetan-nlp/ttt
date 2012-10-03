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
Library (THDL). Portions created by the THDL are Copyright 2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.input;

import java.io.File;
import java.util.Vector;

import org.thdl.util.ThdlOptions;

/** A database of the files most recently opened in Jskad.  The client
    must call {@link #storeRecentlyOpenedFilePreferences()} before
    exiting.
    @author David Chandler
*/
class RecentlyOpenedFilesDatabase {

    /** Tells ThdlOptions about the recently opened files.  Call this before program exit. */
    public static void storeRecentlyOpenedFilePreferences() {
        int n = 0;
        // We store 2*N files in the preferences in case some are deleted.
        for (int k = 0; k < recentlyOpenedFiles.size() && n < 2*N; k++) {
            File f = (File)recentlyOpenedFiles.elementAt(k);
            if (f.isFile()) {
                ThdlOptions.setUserPreference("thdl.recently.opened.file." + n++,
                                              f.getAbsolutePath());
            }
        }
    }

    /** the number of recently opened files to display on the File
        menu */
    private static int N;

    /** the maximum number of characters to display in a label shown
        to the user for a recently opened file */
    private static int maxCharsToShow;

    /** Returns the maximum number of recently opened files that the
        user wishes to see in the File menu. */
    public static int getNumberOfFilesToShow() {
        if (uninitialized) init();
        return N;
    }

    /** false iff we are in the process of initializing or have
        already initialized */
    private static boolean uninitialized = true;

    /** Reads the user's preferences and fills in {@link
        #recentlyOpenedFiles} based on them. */
    private static void init() {
        if (uninitialized) {
            uninitialized = false; // must come first!

            maxCharsToShow
                = ThdlOptions.getIntegerOption("thdl.max.chars.in.recently.opened.file.name", 40);
            N = ThdlOptions.getIntegerOption("thdl.number.of.recently.opened.files.to.show", 4);

            // Add the N most recently opened files.

            if (maxCharsToShow < 5)
                maxCharsToShow = 5;
            for (int i = 2*N - 1; i >= 0; i--) {
                setMostRecentlyOpenedFile(getNthRecentlyOpenedFileFromPreferences(i));
            }
        }
    }

    /** Returns the <code>n</code>th most recently opened file, given
        that we care about <code>N</code> recently opened files in
        total.  When <code>n</code> is zero, the most recently opened
        file is returned.  This file does exist.  Returns null if we
        haven't kept track of enough files to say. */
    public static File getNthRecentlyOpenedFile(int n) {
        if (uninitialized) init();
        while (n < recentlyOpenedFiles.size()) {
            File f = (File)recentlyOpenedFiles.elementAt(n);
            if (f.isFile())
                return f;
            ++n;
        }
        return null;
    }

    /** Returns the nth most recently opened, existing file listed in
        the user's preferences.  Returns null if their preferences do
        not contain n+1 existing files. */
    private static File getNthRecentlyOpenedFileFromPreferences(int n) {
        if (uninitialized) init();
        for (int i = n; i < N*2; i++) {
            String x = ThdlOptions.getStringOption("thdl.recently.opened.file." + i);
            if (null == x)
                return null;
            File f = new File(x);
            if (f.isFile())
                return f;
        }
        return null;
    }

    /** Notes the fact that fileChosen was the file most recently
        opened by Jskad. */
    public static void setMostRecentlyOpenedFile(File fileChosen) {
        if (null != fileChosen) {
            if (uninitialized) init();
            // the first element is the most recently opened.
            int index = recentlyOpenedFiles.indexOf(fileChosen);
            if (index > -1) {
                recentlyOpenedFiles.remove(index);
            }
            recentlyOpenedFiles.add(0, fileChosen);
        }
    }

    /** a vector with the most recently opened file at its
        beginning, the least recently opened at its end. */
    private static Vector recentlyOpenedFiles = new Vector();

    /** Prints debugging information to System.err. */
    public static void printDebuggingInfo() {
        if (uninitialized) init();
        System.err.println("<RecentlyOpenedFilesDatabase>");
        for (int i = 0; i < recentlyOpenedFiles.size(); i++)
            System.err.println("File " + i + " (where 0 is most recent) is " + ((File)recentlyOpenedFiles.elementAt(i)).getAbsolutePath());
        System.err.println("</RecentlyOpenedFilesDatabase>");
    }

    /** Returns a user-friendly label for f that is not longer than
        the user's preferences allow. */
    public static String getLabel(File f) {
        if (uninitialized) init();
        String path = f.getAbsolutePath();
        int l;
        if ((l = path.length()) <= maxCharsToShow)
            return path;
        return "..." + path.substring(l - maxCharsToShow + 3);
    }
}
