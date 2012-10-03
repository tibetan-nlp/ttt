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

package org.thdl.util;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/** Provides an input stream that fixes another RTF input stream so
    that it no longer contains hexadecimal escapes that {@link
    javax.swing.text.rtf#RTFEditorKit} cannot understand.  Instead,
    Unicode escapes (that can be understood) are used.  This is almost
    as fast as possible.

    @author David Chandler */
public class RTFFixerInputStream extends FilterInputStream {
    /** Holds onto the bytes we've read that we haven't yet passed
        along to the user. */
    private ArrayList ourBuffer = new ArrayList(21);

    /** if true, then all hexadecimal escapes are replaced; if false,
        then just the 32 "\\\'8X" and "\\\'9X" escapes are replaced. */
    private boolean replaceAllHexEscapes = false;

    /** The number of bytes in the Unicode RTF escape sequence that we
        will use after processing.  I.e., 7, as in "\\u255 ?" (KEEP THIS IN
        SYNC WITH addASpace) */
    static final int bytesInNewEscape = 7;
    /** True if you want a space in your Unicode RTF escape.  I.e.,
        true if you want "\\u255 ?" instead of "\\u255?" (KEEP THIS IN
        SYNC WITH bytesInNewEscape) */
    private static final boolean addASpace = true;

    private boolean weSubstitutedAtLeastOnce = false;

    /** The number of bytes in the RTF hexadecimal escape.  I.e., 4,
        as in "\\\'ff". */
    static final int bytesInOldEscape = 4;
    /** an instance field just to avoid needless heap allocation */
    private final byte readHelper[] = new byte[bytesInNewEscape];

    /** Constructs an RTFFixerInputStream that will fix in.  No
        hexadecimal escapes will remain. */
    public RTFFixerInputStream(InputStream in) {
        // our read(..) methods may do multiple reads, so buffer this guy:
        super(new BufferedInputStream(in));
        replaceAllHexEscapes = true;
    }

    /** Constructs an RTFFixerInputStream that will fix in.  No
        hexadecimal escapes will remain if replaceAllHexEscapes is
        true; only "\\\'8X" and "\\\'9X" will be replaced otherwise.
        (This is private, but you can make it public if you test it.)  */
    private RTFFixerInputStream(InputStream in, boolean replaceAllHexEscapes) {
        super(in);
        this.replaceAllHexEscapes = replaceAllHexEscapes;
    }
    public int available() throws IOException {
        // We might have to hold on to "\\'9", so report 3 less than
        // the super says.
        int x = super.available();
        if (x > 3)
            return x - 3;
        else
            return 0;
    }
    /** I don't want to think about marking and resetting if I don't
        have to, so this stream doesn't support mark and reset.
        @return false
    */
    public boolean markSupported() {
        return false;
    }
    /** Throws an exception. */
    public void reset() throws IOException {
        throw new IOException("RTFFixerInputStream does not support mark and reset");
    }
    /** Throws an exception. */
    public void mark(int r) {
        throw new Error("RTFFixerInputStream does not support mark and reset");
    }
    /** Throws an exception. */
    public long skip(long n) throws IOException {
        throw new IOException("RTFFixerInputStream does not support skipping.  It could, but that's more work because you have to think about final bytes versus original bytes.");
    }

    public int read() throws IOException {
        if (ourBuffer.size() > 0) {
            int rv = ((Integer)ourBuffer.get(0)).intValue();
            ourBuffer.remove(0);
            return rv;
        }
        int ch1 = super.read();
        if (ch1 == (int)'\\') {
            // we might be staring at an escape sequence.
            
            readHelper[0] = (byte)ch1;
            int x = readAndConvertEscapeSequence(readHelper, 0, 1);
            int j = 0;
            while (x > 0) {
                ourBuffer.add(new Integer(readHelper[j++]));
                --x;
            }
            // Return ourBuffer's first element:
            return read();
        } else {
            return ch1;
        }
    }
    public int read(byte[] bb) throws IOException {
        return read(bb, 0, bb.length);
    }

    public int read(byte[] bb, int off, int len) throws IOException {
        if (ourBuffer.size() > 0) {
            // return as much as we can of this.
            int i;
            for (i = 0; (i < len) && (ourBuffer.size() > 0); i++) {
                bb[off + i] = (byte)((Integer)ourBuffer.get(0)).intValue();
                ourBuffer.remove(0);
            }
            return i;
        }
        int superRV = super.read(bb, off, len);
        ThdlDebug.verify(superRV == -1 || len == 0 || superRV > 0);
        if (superRV == -1 || superRV == 0)
            return superRV;
        byte temp[]
            = new byte[(superRV*bytesInNewEscape)/bytesInOldEscape + bytesInNewEscape + 10];
        // number of elements used in temp:
        int len_used = superRV;
        System.arraycopy(bb, off, temp, 0, superRV);

        // Now see if the last characters might be part of a hex
        // escape sequence by reading ahead enough to say one way or
        // the other.  We do this before we replace the full hex
        // escape sequences so that we never have parts of a hex
        // escape sequence living in ourBuffer.  If the last
        // characters might be, put them into temp.  Eventually, we'll
        // take the part of temp that can't fit in bb and put it into
        // ourBuffer.  We return as many bytes as super.read(..) did
        // so that we don't ever return 0.

        // Three cases: (1) \\'8 ends the writable space of bb
        //              (2) \\' ends it
        //              (3) \\ ends it
        for (int k = 3; k >= 1; k--) {
            if (len_used >= k) {
                if (temp[len_used - k] == '\\') {
                    len_used += readAndConvertEscapeSequence(temp, len_used - k, k) - k;
                }
            }
        }

        // Now replace any full escape sequences in temp.
        for (int i = 0; i < len_used - (bytesInOldEscape - 1); i++) {
            // Replace the escape sequence beginning at i, if there is
            // one we care about beginning there.
            if (temp[i] == '\\' && temp[i+1] == '\'') {
                for (int h = 0; h < bytesInOldEscape; h++) {
                    readHelper[h] = temp[i + h];
                }
                int y = readAndConvertEscapeSequence(readHelper, 0, bytesInOldEscape);
                ThdlDebug.verify(y == bytesInOldEscape
                                 || y == bytesInNewEscape);
                if (y == bytesInNewEscape) {
                    len_used += (bytesInNewEscape - bytesInOldEscape);
                    // Shift everything past the old escape down by one:
                    System.arraycopy(temp,
                                     i+bytesInOldEscape,
                                     temp,
                                     i+bytesInNewEscape,
                                     len_used - (i+bytesInOldEscape));
                    System.arraycopy(readHelper,
                                     0,
                                     temp,
                                     i,
                                     bytesInNewEscape);
                }
            }
        }
        // Copy temp into bb, with overflow going into ourBuffer:
        System.arraycopy(temp,
                         0,
                         bb,
                         off,
                         superRV);
        int g = 0;
        ThdlDebug.verify(ourBuffer.size() == 0);
        while (len_used-- > superRV) {
            ourBuffer.add(new Integer(temp[superRV + g++]));
        }
        return superRV;
    }

    /** Given that old[offset] through old[offset+num_valid-1]
        contain valid characters, the first being a backslash,
        this routine reads as much as is needed from in to
        determine whether we're looking at a hex escape we care
        about.  If we are, then old[offset] through old[offset+4]
        will hold it.  old better be able to hold up to 4 more
        it's currently using, or you'll get an
        ArrayBoundsException.
        @return the number now valid in old on or after offset */
    private int readAndConvertEscapeSequence(byte old[], int offset,
                                             int num_valid)
        throws IOException {
        ThdlDebug.verify(old[offset] == '\\');

        while (num_valid < 4) {
            int x = super.read();
            if (x == -1)
                return num_valid;
            old[offset+num_valid++] = (byte)x;
        }
        if (old[offset+1] != (int)'\'') {
            // This is not a hex escape we care about.
            return num_valid;
        }
        if (replaceAllHexEscapes) {
            if (old[offset+2] != (int)'8'
                && old[offset+2] != (int)'9') {
                // This is not a hex escape we care about.
                return num_valid;
            }
        } else {
            if (!isHexChar(old[offset+2])) {
                // This is not a hex escape we care about.
                return num_valid;
            }
        }
        if (!isHexChar(old[offset+3])) {
            // This is not a hex escape we care about.
            return num_valid;
        }

        // We got here, so it's a hex escape we care about.
        weSubstitutedAtLeastOnce = true;

        // Go from \\' to \\u:
        old[offset+1] = 'u';

        int decimalValue = getHexValue(old[offset+2])*16 + getHexValue(old[offset+3]);
        // We'll use \\uXXX, i.e. we'll always use three digits.
        // \\u156, \\u200, \\u049, \\u001, e.g.
        int hundreds, tens, ones;
        char saved2 = (char)old[offset+2];
        char saved3 = (char)old[offset+3];
        old[offset+2] = (byte)('0' + (hundreds = (decimalValue / 100))); // replace
        old[offset+3] = (byte)('0' + (tens = ((decimalValue - 100*hundreds) / 10))); // replace
        old[offset+4] = (byte)('0' + (ones = (decimalValue - 100*hundreds - 10*tens))); ++num_valid;
        if (addASpace)
            old[offset+5] = (byte)' '; ++num_valid;
        old[offset+6] = (byte)'?'; ++num_valid;
        return num_valid;
    }
        
    /** Returns true iff (char)e is one of
        [0123456789abcdefABCDEF]. */
    private static final boolean isHexChar(int e) {
        return (getHexValue(e) != -1);
    }
    
    /** Returns the decimal value of (char)e, interpreting (char)e as
        a hex character.  Returns -1 if e is not a hex character. */
    private static final int getHexValue(int e) {
        if ((e >= (int)'0' && e <= (int)'9'))
            return e - (int)'0';
        if ((e >= (int)'a' && e <= (int)'f'))
            return 10 + e - (int)'a';
        if ((e >= (int)'A' && e <= (int)'F'))
            return 10 + e - (int)'A';
        return -1;
    }

    /** Returns true if and only if at least one substitution was
        performed.  Returns false if none have been performed. */
    public boolean performedSubstitutions() {
        return weSubstitutedAtLeastOnce;
    }
}
