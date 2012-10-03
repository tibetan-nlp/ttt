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

package org.thdl.tib.text.ttt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
* A TTshegBarScanner is able to break up Strings of transliterated
* Tibetan text (for example, an entire sutra) into bite-sized
* components like tsheg bars.  This is an abstract class.
*
* @author David Chandler */
public abstract class TTshegBarScanner {

    /** Default constructor. */
    public TTshegBarScanner() { }

    /** Scans a transliteration file with path fname into tsheg bars.
     *  If errors is non-null, error messages will be appended to it.
     *  Returns a list of TStrings that is the scan.  Warning and
     *  error messages in the result will be long and self-contained
     *  unless shortMessages is true.
     *
     *  <p>This is not so efficient; copies the whole file into memory
     *  first.
     *
     *  @param warningLevel controls which lexical warnings you will
     *  encounter
     *
     *  @throws IOException if we cannot read in the input file
     *  */
    public final ArrayList scanFile(String fname, StringBuffer errors,
                                    int maxErrors, boolean shortMessages,
                                    String warningLevel)
        throws IOException
    {
        return scanStream(new FileInputStream(fname),
                          errors, maxErrors, shortMessages, warningLevel);
    }

    /** Scans a stream of transliteration into tsheg bars.  If errors
     *  is non-null, error messages will be appended to it.  You can
     *  recover both errors and (optionally) warnings (modulo offset
     *  information) from the result, though.  They will be short
     *  messages iff shortMessages is true.  Returns a list of
     *  TStrings that is the scan, or null if maxErrors is nonnegative
     *  and more than maxErrors occur.
     *
     *  <p>This is not so efficient; copies the whole stream into
     *  memory first.
     *
     *  @param warningLevel controls which lexical warnings you will
     *  encounter
     *
     *  @throws IOException if we cannot read the whole stream */
    public final ArrayList scanStream(InputStream stream,
                                      StringBuffer errors,
                                      int maxErrors,
                                      boolean shortMessages,
                                      String warningLevel)
        throws IOException
    {
        StringBuffer s = new StringBuffer();
        char ch[] = new char[8192];
        BufferedReader in
            = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));

        int amt;
        while (-1 != (amt = in.read(ch))) {
            s.append(ch, 0, amt);
        }
        in.close();
        return scan(s.toString(), errors, maxErrors, shortMessages,
                    warningLevel);
    }

    /** Returns a list of {@link TString TStrings} corresponding
     *  to s, possibly the empty list (when the empty string is the
     *  input).  Each String is either a Latin comment, some Latin
     *  text, a tsheg bar (minus the tsheg or shad or whatever), a
     *  String of inter-tsheg-bar punctuation, etc.
     *
     *  <p>This may do more than scan; it may find some errors and
     *  warnings you'd normally think of a parser (not a scanner)
     *  finding.  If so, it puts those in as TStrings with type {@link
     *  TString#ERROR} or {@link TString#WARNING}, and also, if errors
     *  is non-null, appends helpful messages to errors, each followed
     *  by a '\n'.
     *  @param s the transliterated text
     *  @param errors if non-null, the buffer to which to append error
     *  messages (FIXME: kludge, just get this info by scanning
     *  the result for TString.ERROR (and maybe TString.WARNING,
     *  if you care about warnings), but then we'd have to put the
     *  Offset info in the TString)
     *  @param maxErrors if nonnegative, then scanning will stop when
     *  more than maxErrors errors occur.  In this event, null is
     *  returned.
     *  @param shortMessages true iff you want short error and warning
     *  messages instead of long, self-contained error messages
     *  @return null if more than maxErrors errors occur, or the scan
     *  otherwise */
    public abstract ArrayList scan(String s, StringBuffer errors, int maxErrors,
                                   boolean shortMessages, String warningLevel);
}
