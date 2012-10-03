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

package org.thdl.tib.text;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.rtf.RTFEditorKit;

import org.thdl.tib.text.tshegbar.UnicodeUtils;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlLazyException;
import org.thdl.util.ThdlOptions;

/** Represents a character meant to be rendered in a certain font.
 *  @author David Chandler
 */
class CharacterInAGivenFont {
    private char character;
    private String fontName;
    public CharacterInAGivenFont(char ch, String font) {
        character = ch;
        fontName = font;
    }
    public CharacterInAGivenFont(String s, String font) {
        if (s.length() != 1)
            throw new Error("character in a given font was given a string "
                            + s + " in a given font");
        character = s.charAt(0);
        fontName = font;
    }
    public boolean equals(Object x) {
        return ((x instanceof CharacterInAGivenFont)
                && ((CharacterInAGivenFont)x).character == character
                && ((CharacterInAGivenFont)x).fontName.equals(fontName));
    }
    public int hashCode() {
        return (int)character + fontName.hashCode();
    }
    public String toString() {
        String characterRepresentation
            = "'" + (('\'' == character)
                     ? "\\'"
                     : new Character(character).toString())
            + "' [decimal " + (int)character + "]";
        if ('\n' == character)
            characterRepresentation
                = "newline [decimal " + (int)character + "]";
        if ('\r' == character)
            characterRepresentation
                = "carriage return" + (int)character + "]";
        return characterRepresentation + " in the font "
            + ((null == fontName)
               ? "_ERROR_FINDING_FONT_"
               : fontName);
    }
}

/**
* A TibetanDocument is a styled document that knows about Tibetan and
* will respect line breaks and the like.  It allows you to insert
* Tibetan also.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0 */
public class TibetanDocument extends DefaultStyledDocument {
	private int tibetanFontSize = 36;

    /** Creates a new TibetanDocument with default styles. */
    public TibetanDocument() { super(); }

    /** Do not use this contructor. */
    private TibetanDocument(AbstractDocument.Content c, StyleContext styles ) {
        super(c, styles);
    }

/**
* Creates a TibetanDocument.
* @param styles a StyleContext, which is simply passed on
* to DefaultStyledDocument's constructor
*/
	public TibetanDocument(StyleContext styles) {
		super(styles);
	} 

/**
* Sets the point size used by default for Tibetan text.
* @param size the point size for Tibetan text
*/
	public void setTibetanFontSize(int size) {
		tibetanFontSize = size;
	}

/**
* Gets the point size for Tibetan text.
* @return the point size used for Tibetan text
*/
	public int getTibetanFontSize() {
		return tibetanFontSize;
	}

/**
* Writes the document to an OutputStream as Rich Text Format (.rtf).
* @param out the OutputStream to write to
*/
	public void writeRTFOutputStream(OutputStream out) throws IOException {
		RTFEditorKit rtf = new RTFEditorKit();

		try {
			rtf.write(out, this, 0, getLength());
		}
		catch (BadLocationException ble) {
            throw new Error("Cannot write RTF output; [0, " + getLength() + ") constitutes a bad position.");
		}
	}

    /** Saves the contents of this RTF document as text on out.  If
     *  any TM or TMW is in the document, the output will be
     *  garbage. */
    public void writeTextOutput(BufferedWriter out) throws IOException {
        // DLC FIXME: try getting blocks of text; I bet it's a huge
        // speedup.
        try {
            for (int i = 0; i < getLength(); i++) {
                out.write(getText(i,1));
            }
        } catch (BadLocationException e) {
            throw new Error("can't happen");
        }
    }

    private static boolean allowColors = false;
    /** Enables the use of colors. */
    public static void enableColors() { allowColors = true; }
    /** Disables the use of colors. */
    public static void disableColors() { allowColors = false; }
    /** Returns true if and only if the use of colors is currently
     *  enabled. */
    public static boolean colorsEnabled() { return allowColors; }


/**
* Inserts Tibetan text into the document. The font size is applied automatically,
* according to the current Tibetan font size.
* @param offset the position at which you want to insert text
* @param s the string you want to insert
* @param attr the attributes to apply, normally a particular TibetanMachineWeb font
* @see #setTibetanFontSize(int size)
*/
	public void appendDuff(int offset, String s, MutableAttributeSet attr) {
        appendDuff(tibetanFontSize, offset, s, attr, Color.black);
    }

/**
* Inserts Latin text into the document. The font size is applied
* automatically, according to the current Roman font size.
* @param offset the position at which you want to insert text
* @param s the string you want to insert
* @param color the color in which to insert, which is used if and only
* if {@link #colorsEnabled() colors are enabled}
* @see #setRomanAttributeSet(MutableAttributeSet)
*/
	public void appendRoman(int offset, String s, Color color) throws BadLocationException {
        ThdlDebug.verify(getRomanAttributeSet() != null);
        if (allowColors)
            StyleConstants.setForeground(getRomanAttributeSet(), color);
        insertString(offset, s, getRomanAttributeSet());
    }

/**
* Inserts Latin text at the end of the document. The font size is
* applied automatically, according to the current Roman font size.
* @param s the string you want to insert
* @param color the color in which to insert, which is used if and only
* if {@link #colorsEnabled() colors are enabled}
* @see #setRomanAttributeSet(MutableAttributeSet)
*/
	public void appendRoman(String s, Color color) {
        try {
            appendRoman(getLength(), s, color);
        } catch (BadLocationException e) {
            throw new Error("can't happen");
        }
    }

    private void appendDuff(int fontSize, int offset, String s, MutableAttributeSet attr, Color color) {
        try {
            StyleConstants.setFontSize(attr, fontSize);
            if (allowColors)
                StyleConstants.setForeground(attr, color);
            insertString(offset, s, attr);
        }
        catch (BadLocationException ble) {
            ThdlDebug.noteIffyCode();
        }
    }

/**
* Inserts a stretch of TibetanMachineWeb data into the document.
* @param pos the position at which you want to insert text
* @param glyphs the array of Tibetan data you want to insert
* @param color the color in which to insert, which is used if and only
* if {@link #colorsEnabled() colors are enabled}
*/
public int insertDuff(int pos, DuffData[] glyphs, Color color) {
    return insertDuff(tibetanFontSize, pos, glyphs, true, color);
}

/**
* Inserts a stretch of TibetanMachineWeb data into the document.
* @param pos the position at which you want to insert text
* @param glyphs the array of Tibetan data you want to insert
*/
public int insertDuff(int pos, DuffData[] glyphs) {
    return insertDuff(tibetanFontSize, pos, glyphs, true, Color.black);
}

/**
* Appends glyph to the end of this document.
* @param loc the position at which to insert these glyphs
* @param glyph the Tibetan glyph you want to insert
* @param color the color in which to insert, which is used if and only
* if {@link #colorsEnabled() colors are enabled}
*/
    public void appendDuffCode(int loc, DuffCode glyph, Color color) {
        // PERFORMANCE FIXME: this isn't so speedy, but it reuses
        // existing code.
        insertDuff(loc,
                   new DuffData[] { new DuffData(new String(new char[] { glyph.getCharacter() }),
                                                 glyph.getFontNum()) },
                   color);
    }


	/** Replacing can be more efficient than inserting and then
        removing. This replaces the glyph at position pos with glyph,
        which is interpreted as a TMW glyph if asTMW is true and a TM
        glyph otherwise.  The font size for the new glyph is
        fontSize. */
    private void replaceDuff(int fontSize, int pos,
                             DuffData glyph, boolean asTMW) {
        replaceDuffs(fontSize, pos, pos + 1, glyph.text,
                     glyph.font, asTMW);
    }

    /** Replacing can be more efficient than inserting and then
        removing. This replaces the glyphs at position [startOffset,
        endOffset) with data, which is interpreted as TMW glyphs if
        asTMW is true and as TM glyphs otherwise.  The font size for
        the new glyph is fontSize; the particular TM or TMW font is
        specified by newFontIndex, which is one-based, not
        zero-based. */
    private void replaceDuffs(int fontSize, int startOffset,
                              int endOffset, String data,
                              int newFontIndex, boolean asTMW) {
        MutableAttributeSet mas
            = ((asTMW)
               ? TibetanMachineWeb.getAttributeSet(newFontIndex)
               : TibetanMachineWeb.getAttributeSetTM(newFontIndex));
        StyleConstants.setFontSize(mas, fontSize);
        try {
            replace(startOffset, endOffset - startOffset, data, mas);
        } catch (BadLocationException ble) {
            ThdlDebug.noteIffyCode();
        }
    }

	/** Replacing can be more efficient than inserting and then
        removing. This replaces the glyphs at position [startOffset,
        endOffset) with unicode.  The font size for the new unicode is
        fontSize.  Which particular Unicode font is used is specified
        by unicodeFont.
    
        @see TibetanMachineWeb#getUnicodeAttributeSet(String) */
    private void replaceDuffsWithUnicode(int fontSize, int startOffset,
                                         int endOffset, String unicode,
                                         String unicodeFont) {
		MutableAttributeSet mas
            = TibetanMachineWeb.getUnicodeAttributeSet(unicodeFont);
        StyleConstants.setFontSize(mas, fontSize);
		try {
            replace(startOffset, endOffset - startOffset, unicode, mas);
        } catch (BadLocationException ble) {
            throw new Error("TMW->Unicode failed because the following constitute a bad position: startOffset " + startOffset + ", endOffset " + endOffset);
		}
    }

private int insertDuff(int fontSize, int pos, DuffData[] glyphs, boolean asTMW) {
    return insertDuff(fontSize, pos, glyphs, asTMW, Color.black);
}
private int insertDuff(int fontSize, int pos, DuffData[] glyphs, boolean asTMW, Color color) {
    if (glyphs == null)
        return pos;

    MutableAttributeSet mas;
    for (int i=0; i<glyphs.length; i++) {
        mas = ((asTMW)
               ? TibetanMachineWeb.getAttributeSet(glyphs[i].font)
               : TibetanMachineWeb.getAttributeSetTM(glyphs[i].font));
        if (null == mas)
            throw new Error("Cannot insert that DuffData; the font number is too low or too high; perhaps the programmer has asTMW set incorrectly?");
        appendDuff(fontSize, pos, glyphs[i].text, mas, color);
        pos += glyphs[i].text.length();
    }
    return pos;
}

/**
* Converts the entire document into Extended Wylie.
* If the document consists of both Tibetan and
* non-Tibetan fonts, however, the conversion stops
* at the first non-Tibetan font.
* @param noSuchWylie an array which will not be touched if this is
* successful; however, if there is no THDL Extended Wylie
* corresponding to one of these glyphs, then noSuchWylie[0] will be
* set to true
* @return the string of Wylie corresponding to this document */
	public String getWylie(boolean noSuchWylie[]) {
		return getWylie(0, getLength(), noSuchWylie);
	}

/**
* Converts the entire document into ACIP.  If the document consists of
* both Tibetan and non-Tibetan fonts, however, the conversion stops at
* the first non-Tibetan font.
* @param noSuchACIP an array which will not be touched if this is
* successful; however, if there is no ACIP corresponding to one of
* these glyphs, then noSuchACIP[0] will be set to true
* @return the string of ACIP corresponding to this document */
    public String getACIP(boolean noSuchACIP[]) {
        return getACIP(0, getLength(), noSuchACIP);
    }

/**
* Does not modify the document.  Returns the EWTS transliteration for
* the given portion of the document.  Basically ignores non-TMW
* characters.
* @param begin the beginning of the region to convert
* @param end the end of the region to convert
* @param noSuchWylie an array which will not be touched if this is
* successful; however, if there is no THDL Extended Wylie
* corresponding to one of these glyphs, then noSuchWylie[0] will be
* set to true
* @return the string of Wylie corresponding to this document */
    public String getWylie(int begin, int end, boolean noSuchWylie[]) {
        return getTranslit(true, begin, end, noSuchWylie);
    }

/**
* Does not modify the document.  Returns the ACIP transliteration for
* the given portion of the document.  Basically ignores non-TMW
* characters.
* @param begin the beginning of the region to convert
* @param end the end of the region to convert
* @param noSuchACIP an array which will not be touched if this is
* successful; however, if there is no ACIP corresponding to one of
* these glyphs, then noSuchACIP[0] will be set to true
* @return the string of ACIP corresponding to this document */
    public String getACIP(int begin, int end, boolean noSuchACIP[]) {
        return getTranslit(false, begin, end, noSuchACIP);
    }

    private String getTranslit(boolean EWTSNotACIP, int begin, int end,
                               boolean noSuch[]) {
        try {
            TranslitList translitBuffer = new TranslitList();
            java.util.List dcs = new ArrayList();
            for (int i = begin; i < end; i++) {
                char ch = getText(i,1).charAt(0);
                int fontNum;
                //current character is formatting
                if (ch == '\n' || ch == '\t') {
                    if (dcs.size() > 0) {
                        translitBuffer.append(TibTextUtils.getTranslit(EWTSNotACIP,
                                                                       (SizedDuffCode[])dcs.toArray(new SizedDuffCode[0]),
                                                                       noSuch));
                        dcs.clear();
                    }
                    translitBuffer.append(ch, 12);
                }
                //current character isn't TMW
                else if ((0 == (fontNum
                                = TibetanMachineWeb.getTMWFontNumber(StyleConstants.getFontFamily(getCharacterElement(i).getAttributes()))))) {
                    if (dcs.size() > 0) {
                        translitBuffer.append(TibTextUtils.getTranslit(EWTSNotACIP,
                                                                       (SizedDuffCode[])dcs.toArray(new SizedDuffCode[0]),
                                                                       noSuch));
                        dcs.clear();
                    }
                    // The current character is sort of nonexistent --
                    // it doesn't go into translitBuffer.
                }
                //current character is convertable
                else {
                    dcs.add(new SizedDuffCode(new DuffCode(fontNum, ch), 12));
                }
            }
            if (dcs.size() > 0) {
                translitBuffer.append(TibTextUtils.getTranslit(EWTSNotACIP,
                                                               (SizedDuffCode[])dcs.toArray(new SizedDuffCode[0]),
                                                               noSuch));
                // we'd clear dcs but we're done.
            }
            String ans = translitBuffer.getString();
            ThdlDebug.verify(begin < end // SPEED_FIXME: make this an assertion
                             || "".equals(ans));
            return ans;
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            ThdlDebug.noteIffyCode();
            return "";
        }
    }

    /** Prints to standard output a list of all the indices of
        characters that are not in a TMW font within the range [start,
        end).  Using a negative number for end means that this will
        run to the end of the document.  SPEED_FIXME: might be faster
        to run over the elements, if they are one per font.
        @return 1 if at least one non-TMW character was found in
        the specified range, zero if none were, -1 on error. */
    public int findAllNonTMWCharacters(int begin, int end) {
        return findAllNonTMWCharacters(begin, end, System.out);
    }

    /** Configurable so that System.out isn't necessarily used. */
    public int findAllNonTMWCharacters(int begin, int end, PrintStream out) {
        return findCharacters(begin, end, out, "Non-TMW", true);
    }

    /** Prints to standard output a list of all the indices of
        characters that are not in a TM font within the range [start,
        end).  Using a negative number for end means that this will
        run to the end of the document.  SPEED_FIXME: might be faster
        to run over the elements, if they are one per font.
        @return 1 if at least one non-TM character was found in
        the specified range, zero if none were, -1 on error. */
    public int findAllNonTMCharacters(int begin, int end) {
        return findAllNonTMCharacters(begin, end, System.out);
    }

    /** Configurable so that System.out isn't necessarily used. */
    public int findAllNonTMCharacters(int begin, int end, PrintStream out) {
        return findCharacters(begin, end, out, "Non-TM", true);
    }

    /** Finds the first occurrence of a non-TMW character in a given
        font and prints it to System.out.  If you have a Tahoma
        newline and an Arial newline, e.g., the first occurrence of
        each will be reported.
        
        <p>Works within the range [start, end).  Using a negative
        number for end means that this will run to the end of the
        document.  SPEED_FIXME: might be faster to run over the
        elements, if they are one per font.
        @return 1 if at least one non-TMW character was found in
        the specified range, zero if none were, -1 on error. */
    public int findSomeNonTMWCharacters(int begin, int end) {
        return findSomeNonTMWCharacters(begin, end, System.out);
    }

    /** Finds the first occurrence of a non-TM character in a given
        font and prints it to System.out.  If you have a Tahoma
        newline and an Arial newline, e.g., the first occurrence of
        each will be reported.
        
        <p>Works within the range [start, end).  Using a negative
        number for end means that this will run to the end of the
        document.  SPEED_FIXME: might be faster to run over the
        elements, if they are one per font.
        @return 1 if at least one non-TMW character was found in
        the specified range, zero if none were, -1 on error. */
    public int findSomeNonTMCharacters(int begin, int end) {
        return findSomeNonTMCharacters(begin, end, System.out);
    }

    /** Configurable so that System.out isn't necessarily used. */
    public int findSomeNonTMWCharacters(int begin, int end, PrintStream out) {
        return findCharacters(begin, end, out, "Non-TMW", false);
    }

    /** Configurable so that System.out isn't necessarily used. */
    public int findSomeNonTMCharacters(int begin, int end, PrintStream out) {
        return findCharacters(begin, end, out, "Non-TM", false);
    }

    /** Pass in whatKind=="Non-TMW" or whatKind=="Non-TM" for now; see
        callers and the code to understand the semantics.  Pass in all
        == true to find all characters or all == false to report each
        character just once. */
    private int findCharacters(int begin, int end, PrintStream out,
                               String whatKind, boolean all) {
        if (whatKind != "Non-TMW" && whatKind != "Non-TM")
            throw new IllegalArgumentException("You didn't use an interned string.");
        if (end < 0)
            end = getLength();
        if (begin >= end)
            return 0;
        int i = begin;
        int returnValue = 0;
        try {
            HashMap cgfTable = null;
            if (!all) cgfTable = new HashMap();
            while (i < end) {
                AttributeSet attr = getCharacterElement(i).getAttributes();
                String fontName = StyleConstants.getFontFamily(attr);
                if ((whatKind == "Non-TMW"
                     && (0 == TibetanMachineWeb.getTMWFontNumber(fontName)))
                    || (whatKind == "Non-TM"
                        && (0 == TibetanMachineWeb.getTMFontNumber(fontName)))) {
                    returnValue = 1;
                    CharacterInAGivenFont cgf
                        = new CharacterInAGivenFont(getText(i, 1), fontName);
                    boolean doOutput = all;
                    if (!all && !cgfTable.containsKey(cgf)) {
                        cgfTable.put(cgf, "yes this character appears once");
                        doOutput = true;
                    }
                    if (true == doOutput)
                        out.println(whatKind + " character "
                                    + cgf + " appears "
                                    + ((all) ? "" : "first ")
                                    + "at location " + i);
                }
                i++;
            }
        } catch (BadLocationException ble) {
            ble.printStackTrace(out);
            ThdlDebug.noteIffyCode();
            returnValue = -1;
        }
        return returnValue;
    }

    private static final DuffData[] leftCurlyBraceTMW
        = new DuffData[] { new DuffData("{", 1) };
    private static final DuffData[] rightCurlyBraceTMW
        = new DuffData[] { new DuffData("}", 1) };
    private static final DuffData[] backslashTMW
        = new DuffData[] { new DuffData("\\", 2) };
    /** This is a band-aid used to help Jskad fix RTF files that are
        mostly TMW but have some Tahoma characters that should be TMW.
        Replaces '{', '}', and '\\' characters with the correct
        TibetanMachineWeb.  Works within the range [start, end).
        Using a negative number for end means that this will run to
        the end of the document.  Be sure to set the size for Tibetan
        as you like it before using this (well, it usually gets it
        right on its own, but just in case).  SPEED_FIXME: might be
        faster to run over the elements, if they are one per font. */
    public void replaceTahomaCurlyBracesAndBackslashes(int begin, int end) {
        if (end < 0)
            end = getLength();
        if (begin >= end)
            return;
        int i = begin;
        try {
            while (i < end) {
                AttributeSet attr = getCharacterElement(i).getAttributes();
                String fontName = StyleConstants.getFontFamily(attr);
                if (fontName.equals("Tahoma")) {
                    DuffData[] toReplaceWith = null;
                    switch (getText(i, 1).charAt(0)) {
                    case '{':
                        toReplaceWith = leftCurlyBraceTMW;
                        break;
                    case '}':
                        toReplaceWith = rightCurlyBraceTMW;
                        break;
                    case '\\':
                        toReplaceWith = backslashTMW;
                        break;
                    }
                    if (null != toReplaceWith) {
                        // SPEED_FIXME: determining font size might be slow
                        int fontSize = tibetanFontSize;
                        try {
                            fontSize = ((Integer)getCharacterElement(i).getAttributes().getAttribute(StyleConstants.FontSize)).intValue();
                        } catch (Exception e) {
                            // leave it as tibetanFontSize
                        }
                        if (replaceInsteadOfInserting()) {
                            replaceDuff(fontSize, i, toReplaceWith[0], true);
                        } else {
                            if (insertBefore()) {
                                insertDuff(fontSize, i, toReplaceWith, true);
                                remove(i+1, 1);
                            } else {
                                insertDuff(fontSize, i+1, toReplaceWith, true);
                                remove(i, 1);
                            }
                        }
                    }
                }
                i++;
            }
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            ThdlDebug.noteIffyCode();
        }
    }

    /** Converts all TibetanMachineWeb glyphs in the document to
        TibetanMachine.  Works within the range [start, end).  Using a
        negative number for end means that this will run to the end of
        the document.  Be sure to set the size for Tibetan as you like
        it before using this (well, it usually gets it right on its
        own, but just in case).  SPEED_FIXME: might be faster to run
        over the elements, if they are one per font.
        @return false on 100% success, true if any exceptional case
        was encountered
        @param errors if non-null, then notes about all exceptional
        cases will be appended to this StringBuffer
        @param numAttemptedReplacements an array that contains one
        element; this first element will be, upon exit, incremented by
        the number of TMW glyphs that we encountered and attempted to
        convert to TM
    */
    public boolean convertToTM(int begin, int end, StringBuffer errors,
                               long numAttemptedReplacements[]) {
        return convertHelper(begin, end, "TMW->TM", errors, null,
                             numAttemptedReplacements);
    }

    /** Converts all TibetanMachine glyphs in the document to
        TibetanMachineWeb.  Works within the range [start, end).
        Using a negative number for end means that this will run to
        the end of the document.  Be sure to set the size for Tibetan
        as you like it before using this (well, it usually gets it
        right on its own, but just in case).  SPEED_FIXME: might be
        faster to run over the elements, if they are one per font.
        @return false on 100% success, true if any exceptional case
        was encountered
        @param errors if non-null, then notes about all exceptional
        cases will be appended to this StringBuffer
        @param numAttemptedReplacements an array that contains one
        element; this first element will be, upon exit, incremented by
        the number of TM glyphs that we encountered and attempted to
        convert to TMW
    */
    public boolean convertToTMW(int begin, int end, StringBuffer errors,
                                long numAttemptedReplacements[]) {
        return convertHelper(begin, end, "TM->TMW", errors, null,
                             numAttemptedReplacements);
    }

    /** Converts all TibetanMachineWeb glyphs in the document to
        Unicode.  Works within the range [start, end).  Using a
        negative number for end means that this will run to the end of
        the document.  Be sure to set the size for Tibetan as you like
        it before using this (well, it usually gets it right on its
        own, but just in case).  SPEED_FIXME: might be faster to run
        over the elements, if they are one per font.
        @return false on 100% success, true if any exceptional case
        was encountered
        @param errors if non-null, then notes about all exceptional
        cases will be appended to this StringBuffer
        @param unicodeFont the name of the Unicode font to use;
        defaults to Tibetan Machine Uni if null
        @param numAttemptedReplacements an array that contains one
        element; this first element will be, upon exit, incremented by
        the number of TMW glyphs that we encountered and attempted to
        convert to Unicode
    */
    public boolean convertToUnicode(int begin, int end, StringBuffer errors,
                                    String unicodeFont,
                                    long numAttemptedReplacements[]) {
        return convertHelper(begin, end, "TMW->Unicode", errors, unicodeFont,
                             numAttemptedReplacements);
    }

    /** This setting determines whether the formatting is preserved,
        but with infinite loops in it, or is not preserved, but works
        well.  Inserting + removing must be used rather than replacing
        because you get the same exception otherwise.  FIXME: try Java
        1.5 -- maybe it beats Java 1.4.

     [java] javax.swing.text.StateInvariantError: infinite loop in formatting
     [java] 	at javax.swing.text.FlowView$FlowStrategy.layout(FlowView.java:404)
     [java] 	at javax.swing.text.FlowView.layout(FlowView.java:182)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.text.BoxView.updateChildSizes(BoxView.java:348)
     [java] 	at javax.swing.text.BoxView.setSpanOnAxis(BoxView.java:330)
     [java] 	at javax.swing.text.BoxView.layout(BoxView.java:682)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.plaf.basic.BasicTextUI$RootView.setSize(BasicTextUI.java:1598)
     [java] 	at javax.swing.plaf.basic.BasicTextUI.getPreferredSize(BasicTextUI.java:800)
     [java] 	at javax.swing.JComponent.getPreferredSize(JComponent.java:1272)
     [java] 	at javax.swing.JEditorPane.getPreferredSize(JEditorPane.java:1206)
     [java] 	at javax.swing.ScrollPaneLayout.layoutContainer(ScrollPaneLayout.java:769)
     [java] 	at java.awt.Container.layout(Container.java:1017)
     [java] 	at java.awt.Container.doLayout(Container.java:1007)
     [java] 	at java.awt.Container.validateTree(Container.java:1089)
     [java] 	at java.awt.Container.validate(Container.java:1064)
     [java] 	at javax.swing.RepaintManager.validateInvalidComponents(RepaintManager.java:353)
     [java] 	at javax.swing.SystemEventQueueUtilities$ComponentWorkRequest.run(SystemEventQueueUtilities.java:116)
     [java] 	at java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:178)
     [java] 	at java.awt.EventQueue.dispatchEvent(EventQueue.java:448)
     [java] 	at java.awt.EventDispatchThread.pumpOneEventForHierarchy(EventDispatchThread.java:197)
     [java] 	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:150)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:144)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:136)
     [java] 	at java.awt.EventDispatchThread.run(EventDispatchThread.java:99)
     [java] javax.swing.text.StateInvariantError: infinite loop in formatting
     [java] 	at javax.swing.text.FlowView$FlowStrategy.layout(FlowView.java:404)
     [java] 	at javax.swing.text.FlowView.layout(FlowView.java:182)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.text.BoxView.updateChildSizes(BoxView.java:348)
     [java] 	at javax.swing.text.BoxView.setSpanOnAxis(BoxView.java:316)
     [java] 	at javax.swing.text.BoxView.layout(BoxView.java:683)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.plaf.basic.BasicTextUI$RootView.setSize(BasicTextUI.java:1598)
     [java] 	at javax.swing.plaf.basic.BasicTextUI.getPreferredSize(BasicTextUI.java:800)
     [java] 	at javax.swing.JComponent.getPreferredSize(JComponent.java:1272)
     [java] 	at javax.swing.JEditorPane.getPreferredSize(JEditorPane.java:1206)
     [java] 	at javax.swing.ScrollPaneLayout.layoutContainer(ScrollPaneLayout.java:769)
     [java] 	at java.awt.Container.layout(Container.java:1017)
     [java] 	at java.awt.Container.doLayout(Container.java:1007)
     [java] 	at java.awt.Container.validateTree(Container.java:1089)
     [java] 	at java.awt.Container.validate(Container.java:1064)
     [java] 	at javax.swing.RepaintManager.validateInvalidComponents(RepaintManager.java:353)
     [java] 	at javax.swing.SystemEventQueueUtilities$ComponentWorkRequest.run(SystemEventQueueUtilities.java:116)
     [java] 	at java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:178)
     [java] 	at java.awt.EventQueue.dispatchEvent(EventQueue.java:448)
     [java] 	at java.awt.EventDispatchThread.pumpOneEventForHierarchy(EventDispatchThread.java:197)
     [java] 	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:150)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:144)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:136)
     [java] 	at java.awt.EventDispatchThread.run(EventDispatchThread.java:99)
     [java] javax.swing.text.StateInvariantError: infinite loop in formatting
     [java] 	at javax.swing.text.FlowView$FlowStrategy.layout(FlowView.java:404)
     [java] 	at javax.swing.text.FlowView.layout(FlowView.java:182)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.text.BoxView.updateChildSizes(BoxView.java:348)
     [java] 	at javax.swing.text.BoxView.setSpanOnAxis(BoxView.java:316)
     [java] 	at javax.swing.text.BoxView.layout(BoxView.java:683)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.plaf.basic.BasicTextUI$RootView.setSize(BasicTextUI.java:1598)
     [java] 	at javax.swing.plaf.basic.BasicTextUI.modelToView(BasicTextUI.java:934)
     [java] 	at javax.swing.text.DefaultCaret.repaintNewCaret(DefaultCaret.java:1044)
     [java] 	at javax.swing.text.DefaultCaret$1.run(DefaultCaret.java:1023)
     [java] 	at java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:178)
     [java] 	at java.awt.EventQueue.dispatchEvent(EventQueue.java:448)
     [java] 	at java.awt.EventDispatchThread.pumpOneEventForHierarchy(EventDispatchThread.java:197)
     [java] 	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:150)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:144)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:136)
     [java] 	at java.awt.EventDispatchThread.run(EventDispatchThread.java:99)
     [java] javax.swing.text.StateInvariantError: infinite loop in formatting
     [java] 	at javax.swing.text.FlowView$FlowStrategy.layout(FlowView.java:404)
     [java] 	at javax.swing.text.FlowView.layout(FlowView.java:182)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.text.BoxView.updateChildSizes(BoxView.java:348)
     [java] 	at javax.swing.text.BoxView.setSpanOnAxis(BoxView.java:316)
     [java] 	at javax.swing.text.BoxView.layout(BoxView.java:683)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.plaf.basic.BasicTextUI$RootView.setSize(BasicTextUI.java:1598)
     [java] 	at javax.swing.plaf.basic.BasicTextUI.getPreferredSize(BasicTextUI.java:800)
     [java] 	at javax.swing.JComponent.getPreferredSize(JComponent.java:1272)
     [java] 	at javax.swing.JEditorPane.getPreferredSize(JEditorPane.java:1206)
     [java] 	at javax.swing.ScrollPaneLayout.layoutContainer(ScrollPaneLayout.java:769)
     [java] 	at java.awt.Container.layout(Container.java:1017)
     [java] 	at java.awt.Container.doLayout(Container.java:1007)
     [java] 	at java.awt.Container.validateTree(Container.java:1089)
     [java] 	at java.awt.Container.validate(Container.java:1064)
     [java] 	at javax.swing.RepaintManager.validateInvalidComponents(RepaintManager.java:353)
     [java] 	at javax.swing.SystemEventQueueUtilities$ComponentWorkRequest.run(SystemEventQueueUtilities.java:116)
     [java] 	at java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:178)
     [java] 	at java.awt.EventQueue.dispatchEvent(EventQueue.java:448)
     [java] 	at java.awt.EventDispatchThread.pumpOneEventForHierarchy(EventDispatchThread.java:197)
     [java] 	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:150)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:144)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:136)
     [java] at java.awt.EventDispatchThread.run(EventDispatchThread.java:99) */
    private static boolean insertBefore() {
        return !ThdlOptions.getBooleanOption("thdl.insert.rtf.after.not.before");
    }
    private static boolean replaceInsteadOfInserting() {
        return !ThdlOptions.getBooleanOption("thdl.insert.and.remove.instead.of.replacing");
    }

    /** Helper function.  Converts TMW->TM, TM->TMW, TMW->Unicode, or
        TMW-> the very same TMW [just for testing Java's RTF support]
        depending on mode.
        @param mode one of "TMW->TMW-identity" (a null conversion for
        testing), "TM->TMW", "TMW->TM", or "TMW->Unicode"
        @param errors if non-null, then notes about all exceptional
        cases will be appended to this StringBuffer
        @return false on 100% success, true if any exceptional case
        was encountered
        @see #convertToUnicode(int,int,StringBuffer,String,long[])
        @see #convertToTMW(int,int,StringBuffer,long[]) 
        @see #convertToTM(int,int,StringBuffer,long[]) */
    private boolean convertHelper(int begin, int end, String mode,
                                  StringBuffer errors,
                                  String unicodeFont,
                                  long numAttemptedReplacements[]) {
        // To preserve formatting, we go paragraph by paragraph.

        // Use positions, not offsets, because our work on paragraph K
        // will affect the offsets of paragraph K+1.

        ThdlDebug.verify("TMW->TMW-identity" == mode || "TMW->Unicode" == mode
                         || "TM->TMW" == mode || "TMW->TM" == mode);

        Position finalEndPos;
        if (end < 0) {
            end = getLength();
        }
        try {
            finalEndPos = createPosition(end);
        } catch (BadLocationException e) {
            throw new Error("BAD LOCATION DURING CONVERSION");
        }

        ConversionErrorHelper ceh = new ConversionErrorHelper();
        int pl = 0;
        pl = getParagraphs(begin, finalEndPos.getOffset()).length;
        boolean warn = false;
        int lastTimeWeExamined = -1; // must be -1
        boolean noMore = false;
        
        while (!noMore
               && lastTimeWeExamined != ceh.lastOffsetExamined) {
            lastTimeWeExamined = ceh.lastOffsetExamined;
            Element thisParagraph
                = getParagraphElement(lastTimeWeExamined + 1);
            int p_end = thisParagraph.getEndOffset();
            if (p_end >= finalEndPos.getOffset()) {
                noMore = true;
                ceh.doErrorWrapup = true;
            }
            convertParagraph(thisParagraph.getStartOffset(),
                             ((finalEndPos.getOffset() < p_end)
                              ? finalEndPos.getOffset()
                              : p_end),
                             mode, errors, ceh,
                             unicodeFont,
                             numAttemptedReplacements);
        }
        if (!ceh.errorReturn
            && pl != getParagraphs(begin, finalEndPos.getOffset()).length) {
            System.err.println("Conversion WARNING: the number of paragraphs changed from "
                               + pl + " to " + getParagraphs(begin, finalEndPos.getOffset()).length
                               + ", indicating that formatting may have been lost.");
            /* You'll see this with this document:
               
               {\rtf1\ansi\deff0\deftab720{\fonttbl{\f10\fnil\fprq2 TibetanMachine;}}
               \deflang1033\pard\plain\f10\fs48\cf0 \u0156\par }
               
               You'll see it coming (TM->TMW) and going (if you do
               TMW->TM again).  I wonder if finalEndPos isn't one shy
               of where you'd think it would be.  FIXME */
        }
        return ceh.errorReturn;
    }

    /** See the sole caller, {@link #convertHelper}.  begin and end
        should specify the bounds of a paragraph. */
    private void convertParagraph(int begin, int end, String mode,
                                  StringBuffer errors,
                                  ConversionErrorHelper ceh,
                                  String unicodeFont,
                                  long numAttemptedReplacements[]) {
        final int debug = 0;
        if (debug > 0)
            System.out.println("convertParagraph: [" + begin + ", " + end + ")");
        // DLC FIXME: here's an idea, a compressor -- use the '-' (ord
        // 45) or ' ' (ord 32) glyph from the same font as the
        // preceding glyph, never others.  This reduces the size of a
        // TMW RTF file by a factor of 3 sometimes.  To do it, use
        // this routine, but give it the ability to go from
        // TMW->compressed-TMW and TM->compressed-TM.

        boolean toStdout = ThdlOptions.getBooleanOption("thdl.debug");
        if (end < 0)
            end = getLength();
        if (begin >= end)
            return; // nothing to do

        // For speed, do as few replaces as possible.  To preserve
        // formatting, we'll try to replace one paragraph at a time.
        // But we *must* replace when we hit a different font (TMW3 as
        // opposed to TMW2, e.g.), so we'll likely replace many times
        // per paragraph.  One very important optimization is that we
        // don't have to treat TMW3.45 or TMW3.32 as a different font
        // than TMW.33 -- that's because each of the ten TMW fonts has
        // the same glyph at position 32 (space) and the same glyph at
        // position 45 (tsheg).  Note that we're building up a big
        // StringBuffer; we're trading space for time.
        try {
            int replacementStartIndex = begin;
            StringBuffer replacementQueue = new StringBuffer();
            int replacementFontIndex = 0;
            int replacementFontSize = -1;

            int i = begin;
            Position endPos = createPosition(end);
            DuffData[] equivalent = new DuffData[1];
            equivalent[0] = new DuffData();
            boolean mustReplace = false;
            int mustReplaceUntil = -1;
            while (i < endPos.getOffset()) {
                AttributeSet attr = getCharacterElement(i).getAttributes();
                String fontName = StyleConstants.getFontFamily(attr);
                int fontNum
                    = (("TMW->TM" == mode
                        || "TMW->Unicode" == mode
                        || "TMW->TMW-identity" == mode)
                       ? TibetanMachineWeb.getTMWFontNumber(fontName)
                       : TibetanMachineWeb.getTMFontNumber(fontName));

                if (0 != fontNum) {
                    ++numAttemptedReplacements[0];

                    // SPEED_FIXME: determining font size might be slow, allow an override.
                    int fontSize = tibetanFontSize;
                    try {
                        fontSize = ((Integer)getCharacterElement(i).getAttributes().getAttribute(StyleConstants.FontSize)).intValue();
                    } catch (Exception e) { /* leave it as tibetanFontSize */ }

                    DuffCode dc = null;
                    String unicode = null;
                    if ("TMW->Unicode" == mode) {
                        unicode = TibetanMachineWeb.mapTMWtoUnicode(fontNum - 1,
                                                                    getText(i,1).charAt(0));
                    } else {
                        if ("TMW->TM" == mode) {
                            dc = TibetanMachineWeb.mapTMWtoTM(fontNum - 1,
                                                              getText(i,1).charAt(0),
                                                              replacementFontIndex);
                        } else if ("TMW->TMW-identity" == mode) {
                            dc = TibetanMachineWeb.mapTMWtoItself(fontNum - 1,
                                                                  getText(i,1).charAt(0),
                                                                  replacementFontIndex);
                        } else {
                            dc = TibetanMachineWeb.mapTMtoTMW(fontNum - 1,
                                                              getText(i,1).charAt(0),
                                                              replacementFontIndex);
                        }
                    }
                    if (replacementQueue.length() > 0
                        && (mustReplace
                            || (("TMW->Unicode" != mode
                                 && null != dc
                                 && dc.getFontNum() != replacementFontIndex)
                                || fontSize != replacementFontSize))) {
                        // We must replace now, because the attribute
                        // set has changed.

                        // We have two choices: replace or
                        // insert-and-remove.  We replace, because
                        // that preserves formatting.

                        // this if-else statement is duplicated below; beware!
                        int endIndex = mustReplace ? mustReplaceUntil : i;
                        if ("TMW->Unicode" == mode) {
                            UnicodeUtils.fixSomeOrderingErrorsInTibetanUnicode(replacementQueue);
                            replaceDuffsWithUnicode(replacementFontSize,
                                                    replacementStartIndex,
                                                    endIndex,
                                                    replacementQueue.toString(),
                                                    unicodeFont);
                        } else {
                            replaceDuffs(replacementFontSize,
                                         replacementStartIndex,
                                         endIndex,
                                         replacementQueue.toString(),
                                         replacementFontIndex,
                                         mode != "TMW->TM");
                        }

                        // i += numnewchars - numoldchars;
                        if (debug > 10)
                            System.out.println("Incrementing i by " + (replacementQueue.length()
                                                                       - (endIndex - replacementStartIndex)) + "; replaced a patch with font size " + replacementFontSize + ", fontindex " + replacementFontIndex);
                        i += (replacementQueue.length()
                              - (endIndex - replacementStartIndex));

                        replacementQueue.delete(0, replacementQueue.length());
                        mustReplace = false;
                    }

                    if (null != dc || null != unicode) {
                        if (0 == replacementQueue.length()) {
                            replacementFontSize = fontSize;
                            replacementStartIndex = i;
                            if ("TMW->Unicode" != mode) {
                                replacementFontIndex = dc.getFontNum();
                            }
                        }
                        if ("TMW->Unicode" == mode) {
                            replacementQueue.append(unicode);
                            if (debug > 0)
                                System.out.println("unicode rq.append: " + org.thdl.tib.text.tshegbar.UnicodeUtils.unicodeStringToString(unicode));
                        } else {
                            replacementQueue.append(dc.getCharacter());
                        }
                    } else {
                        // For now, on error, we insert the alphabet
                        // in a big font in TMW to try and get some
                        // attention.  We also put the oddballs at the
                        // start of the document.  But then we delete
                        // the alphabet usually.
                        
                        ceh.errorReturn = true;
                        CharacterInAGivenFont cgf
                            = new CharacterInAGivenFont(getText(i,1), fontName);
                        if (!ceh.problemGlyphsTable.containsKey(cgf)) {
                            ceh.problemGlyphsTable.put(cgf, "yes this character appears once");
                            if (null != errors) {
                                String err
                                    = mode
                                    + " conversion failed for a glyph:\nFont is "
                                    + fontName + ", glyph number is "
                                    + (int)getText(i,1).charAt(0)
                                    + "; first position found (from zero) is "
                                    + i + "\n";
                                errors.append(err);
                                if (toStdout) {
                                    System.out.print(err);
                                }

                                // Now also put this problem glyph at
                                // the beginning of the document,
                                // after a 'a' character (i.e.,
                                // \tm0062 or \tmw0063):
                                equivalent[0].setData((("TMW->Unicode" == mode
                                                        || "TMW->TM" == mode)
                                                       ? (char)63 : (char)62),
                                                      1);
                                insertDuff(72, ceh.errorGlyphLocation++,
                                           equivalent,
                                           ("TMW->Unicode" == mode
                                            || "TMW->TMW-identity" == mode
                                            || "TMW->TM" == mode));
                                ++i;
                                // Don't later replace this last guy:
                                if (replacementStartIndex < ceh.errorGlyphLocation) {
                                    ++replacementStartIndex;
                                }
                                equivalent[0].setData(getText(i,1), fontNum);
                                insertDuff(72, ceh.errorGlyphLocation++,
                                           equivalent,
                                           ("TMW->Unicode" == mode
                                            || "TMW->TMW-identity" == mode
                                            || "TMW->TM" == mode));
                                ++i;
                                // Don't later replace this last guy:
                                if (replacementStartIndex < ceh.errorGlyphLocation) {
                                    ++replacementStartIndex;
                                }
                            }
                        }

                        if (ThdlOptions.getBooleanOption("thdl.leave.bad.tm.tmw.conversions.in.place")) {
                            String trickyTMW
                                = "!-\"-#-$-%-&-'-(-)-*-+-,-.-/-0-1-2-3-4-5-6-7-8-9-:-;-<-=->-?-";
                            equivalent[0].setData(trickyTMW, 1);
                            insertDuff(72, i, equivalent, true);
                            i += trickyTMW.length();
                        }
                    }
                } else {
                    // FIXME: are we doing the right thing here?  I
                    // think so -- I think we're just not replacing
                    // the current character, but I'm not at all sure.
                    if (debug > 0) System.out.println("some font we're not converting found at offset " + i + "; font=" + fontName + " ord " + (int)getText(i,1).charAt(0));
                    if (replacementQueue.length() > 0) {
                        if (!mustReplace) {
                            mustReplaceUntil = i;
                            mustReplace = true;
                        }
                    }
                    // TODO(dchandler): If this is a TMW->*
                    // conversion, generate a warning if TM is found.
                    // If this is a TM->* conversion, generate a
                    // warning if TMW is found.
                }
                i++;
            }
            if (replacementQueue.length() > 0) {
                // this if-else statement is duplicated above; beware!
                int endIndex = mustReplace ? mustReplaceUntil : i;
                if ("TMW->Unicode" == mode) {
                    UnicodeUtils.fixSomeOrderingErrorsInTibetanUnicode(replacementQueue);
                    replaceDuffsWithUnicode(replacementFontSize,
                                            replacementStartIndex,
                                            endIndex,
                                            replacementQueue.toString(),
                                            unicodeFont);
                    if (debug > 0)
                        System.out.println("unicode rq: " + org.thdl.tib.text.tshegbar.UnicodeUtils.unicodeStringToString(replacementQueue.toString()));
                } else {
                    replaceDuffs(replacementFontSize,
                                 replacementStartIndex,
                                 endIndex,
                                 replacementQueue.toString(),
                                 replacementFontIndex,
                                 "TMW->TM" != mode);
                }
            }
            ceh.lastOffsetExamined = endPos.getOffset() - 1;

            if (ceh.doErrorWrapup && ceh.errorGlyphLocation > 0) {
                // Bracket the bad stuff with U+0F3C on the left
                // and U+0F3D on the right:
                if (!("TMW->Unicode" == mode
                      || "TMW->TM" == mode
                      || "TMW->TMW-identity" == mode)) {
                    equivalent[0].setData((char)209, 1);
                    insertDuff(72, ceh.errorGlyphLocation++,
                               equivalent, false);
                    equivalent[0].setData((char)208, 1);
                    insertDuff(72, 0,
                               equivalent, false);
                    ceh.errorGlyphLocation++;
                } else {
                    equivalent[0].setData((char)94, 9);
                    insertDuff(72, ceh.errorGlyphLocation++,
                               equivalent, true);
                    equivalent[0].setData((char)93, 9);
                    insertDuff(72, 0,
                               equivalent, true);
                    ceh.errorGlyphLocation++;
                }
            }
            if (!ThdlOptions.getBooleanOption("thdl.leave.bad.tm.tmw.conversions.in.place")) {
                // Remove all characters other than the oddballs:
                if (ceh.doErrorWrapup && ceh.errorGlyphLocation > 0) {
                    remove(ceh.errorGlyphLocation, getLength()-ceh.errorGlyphLocation-1);
                }
            }
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            ThdlDebug.noteIffyCode();
        }
    }
    
    /** the attribute set applied to Roman text in this
        document */
    private MutableAttributeSet romanAttributeSet = null;
    
    /** Gets the attribute set applied to Roman text in this
        document. */
    public MutableAttributeSet getRomanAttributeSet() {
        return romanAttributeSet;
    }

    /** Gets a copy of the attribute set applied to Roman text in this
        document but with the font size set to fontSize. */
    public MutableAttributeSet getCopyOfRomanAttributeSet(int fontSize) {
        SimpleAttributeSet s
            = new SimpleAttributeSet(getRomanAttributeSet());
        StyleConstants.setFontSize(s, fontSize);
        return s;
    }

    /** Sets the attribute set applied to Roman text in this
        document. */
    public void setRomanAttributeSet(MutableAttributeSet ras) {
        romanAttributeSet = ras;
    }

    /** Sets the attribute set applied to Roman text in this
        document. */
    public void setRomanAttributeSet(String font, int size) {
        SimpleAttributeSet ras = new SimpleAttributeSet();
        StyleConstants.setFontFamily(ras, font);
        StyleConstants.setFontSize(ras, size);
        setRomanAttributeSet(ras);
    }

/**
* Converts the specified portion of this document to THDL Extended
* Wylie.
*
* @param start the point from which to begin converting to Wylie
* @param end the point at which to stop converting to Wylie
* @param numAttemptedReplacements an array that contains one element;
* this first element will be, upon exit, incremented by the number of
* TMW glyphs that we encountered and attempted to convert to Wylie
* @return true if entirely successful, false if we put some
* "<<[[JSKAD_TMW_TO_WYLIE_ERROR_NO_SUCH_WYLIE: Cannot convert
* DuffCode..." text into the document */
    public boolean toWylie(int start, int end,
                           long numAttemptedReplacements[]) {
        return toTranslit(true, start, end, numAttemptedReplacements);
    }

    // DLC DOC just like {@link #toWylie(int,int,long[])} but uses
    // ACIP instead of EWTS
    public boolean toACIP(int start, int end,
                          long numAttemptedReplacements[]) {
        return toTranslit(false, start, end, numAttemptedReplacements);
    }

    private boolean toTranslit(boolean EWTSNotACIP, int start, int end,
                               long numAttemptedReplacements[]) {
        if (start >= end)
            return true;

        try {
            boolean noSuchWylie[] = new boolean[] { false };
            Position endPos = createPosition(end);
            int i = start;
            java.util.List dcs = new ArrayList();

            while (i < endPos.getOffset()+1) {
                AttributeSet attr
                    = getCharacterElement(i).getAttributes();
                String fontName = StyleConstants.getFontFamily(attr);
                int fontNum;
                if ((0 == (fontNum
                           = TibetanMachineWeb.getTMWFontNumber(fontName)))
                    || i==endPos.getOffset()) {
                    if (i != start) {
                        if (i < start) throw new Error("Oops!");
                        SizedDuffCode[] sdc_array
                            = (SizedDuffCode[])dcs.toArray(new SizedDuffCode[0]);

                        remove(start, i-start);
                        ThdlDebug.verify(getRomanAttributeSet() != null);
                        TranslitList tb
                            = TibTextUtils.getTranslit(EWTSNotACIP,
                                                       sdc_array,
                                                       noSuchWylie);
                        int lastFontSize = -1;
                        for (int j = 0; j < tb.length(); j++) {
                            TranslitTuple tt = tb.get(j);
                            int thisFontSize;
                            String ttt = tt.getTranslit();
                            insertString(start,
                                         ttt,
                                         getCopyOfRomanAttributeSet(thisFontSize
                                                                    = tt.getFontSize()));
                            start += ttt.length();
                            if (thisFontSize == lastFontSize)
                                throw new Error("FIXME: make this an assertion");
                            lastFontSize = thisFontSize;
                        }
                        i = start - 1; // i++ below will execute.
                        dcs.clear();
                    } else {
                        start = i+1;
                    }
                } else {
                    int iFontSize = 72; /* the failure ought to be obvious
                                           at this size */
                    try {
                        iFontSize
                            = ((Integer)attr.getAttribute(StyleConstants.FontSize)).intValue();
                    } catch (Exception e) {
                        // leave it as 72
                    }
                    char ch = getText(i,1).charAt(0);
                    dcs.add(new SizedDuffCode(new DuffCode(fontNum, ch),
                                              iFontSize));
                    ++numAttemptedReplacements[0];
                }

                i++;
            }
            return !noSuchWylie[0];
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            ThdlDebug.noteIffyCode();
            return false;
        }
    }

    /**
       To test Java's RTF support, it's helpful to just try and do an
       identity TMW->TMW transformation (you can think of it as a
       converter that converts nothing).  I'm curious to see if the
       problem we have with TMW->Unicode conversions failing to
       preserve whitespace is a bug in our code or a bug in Java's RTF
       support, and this provides one data point.
       
       @return false on 100% success, true if any exceptional case was
       encountered
       @exception Error if start or end is out of range */
    public boolean identityTmwToTmwConversion(int start,
                                              int end,
                                              long numAttemptedReplacements[]) {
        StringBuffer errors = new StringBuffer();
        boolean r = convertHelper(start, end, "TMW->TMW-identity",
                                  errors, "Unicode Font should not be used",
                                  numAttemptedReplacements);
        System.err.println("<TMW_TO_SAME_TWM-errors>");
        System.err.println(errors.toString());
        System.err.println("</TMW_TO_SAME_TWM-errors>");
        return r;
    }

    /** Returns all the paragraph elements in this document that
     *  contain glyphs with offsets in the range [start, end) where
     *  end < 0 is treated as the document's length.  Note that roman,
     *  TM, Tibetan Machine Uni, and TMW text can all be intermingled
     *  within a paragraph.  It's the correct level of abstraction to
     *  use, however, because the next finer grain is roughly one
     *  Element per glyph. */
    private Element[] getParagraphs(int start, int end) {
        if (end < 0)
            end = getLength();
        Element arrayType[] = new Element[0];
        ArrayList v = new ArrayList();
        int pos = start;
        while (pos <= end) {
            Element pe = getParagraphElement(pos);
            v.add(pe);
            int peo = pe.getEndOffset();
            if (peo == pos) {
                // Avoids an infinite loop I've run into:
                if (getParagraphElement(peo + 1).getEndOffset() > pos)
                    pos = peo + 1;
                break;
            } else
                pos = peo;
        }
        return (Element[])v.toArray(arrayType);
    }

    /** Appends to sb a text representation of the characters (glyphs)
        in this document in the range [begin, end).  In this
        representation, \tmwXYYY and \tmXYYY are used for TMW and TM
        glyphs, respectively.  \otherYYY is used for all other
        characters.  X is zero-based; Y is the decimal glyph number.
        After every 10 characters, '\n' is added.  Note well that some
        TM oddballs (see TibetanMachineWeb.getUnusualTMtoTMW(int,
        int)) are not handled well, so you may get \tm08222 etc. */
    public void getTextRepresentation(int begin, int end, StringBuffer sb) {
        if (end < 0)
            end = getLength();
        if (begin >= end)
            return; // nothing to do

        // For speed, do as few replaces as possible.  To preserve
        // formatting, we'll try to replace one paragraph at a time.
        // But we *must* replace when we hit a different font (TMW3 as
        // opposed to TMW2, e.g.), so we'll likely replace many times
        // per paragraph.  One very important optimization is that we
        // don't have to treat TMW3.45 or TMW3.32 as a different font
        // than TMW.33 -- that's because each of the ten TMW fonts has
        // the same glyph at position 32 (space) and the same glyph at
        // position 45 (tsheg).  Note that we're building up a big
        // StringBuffer; we're trading space for time.
        try {
            int i = begin;
            int tenCount = 0;
            while (i < end) {
                AttributeSet attr = getCharacterElement(i).getAttributes();
                String fontName = StyleConstants.getFontFamily(attr);
				int tmwFontNum
                    = TibetanMachineWeb.getTMWFontNumber(fontName);
                int tmFontNum;
                if (tmwFontNum != 0) {
                    sb.append("\\tmw" + (tmwFontNum - 1));
                } else if ((tmFontNum
                            = TibetanMachineWeb.getTMFontNumber(fontName))
                           != 0) {
                    sb.append("\\tm" + (tmFontNum - 1));
                } else {
                    // non-tmw, non-tm character:
                    sb.append("\\other");
                }
                int ordinal = (int)getText(i,1).charAt(0);
                if (ordinal < 100)
                    sb.append('0');
                if (ordinal < 10)
                    sb.append('0');
                sb.append("" + ordinal);
                if ((++tenCount) % 10 == 0) {
                    tenCount = 0;
                    sb.append('\n');
                }
                i++;
            }
        } catch (BadLocationException e) {
            throw new ThdlLazyException(e);
        }
    }

    /** For debugging only.  Start with an empty document, and call
        this on it.  You'll get all the TibetanMachine glyphs
        inserted, in order, into your document. */
    private void insertAllTMGlyphs() {
        int font;
        int ord;
        DuffData[] equivalent = new DuffData[1];
        equivalent[0] = new DuffData();

        int count = 0;
        for (font = 0; font < 5; font++) {
            for (ord = 32; ord < 255; ord++) {
                if (TibetanMachineWeb.mapTMtoTMW(font, ord, 0) != null) {
                    equivalent[0].setData((char)ord, font + 1);
                    try {
                        insertDuff(tibetanFontSize, count++, equivalent, false);
                    } catch (NullPointerException e) {
                        System.err.println("nullpointerexception happened: font is " + font + " ord is " + ord);
                    }
                }
            }
        }
    }

    /** I used this to create a document that helped me validate the
        TM->TMW conversion. */
    private void insertAllTMGlyphs2() {
        int font;
        int ord;
        DuffData[] equivalent = new DuffData[1];
        equivalent[0] = new DuffData();
        DuffData[] tmwEquivalent = new DuffData[1];
        tmwEquivalent[0] = new DuffData();
        DuffData[] achen = new DuffData[1];
        achen[0] = new DuffData();
        achen[0].setData((char)62, 1);
        DuffData[] newline = new DuffData[1];
        newline[0] = new DuffData();
        newline[0].setData((char)10, 1);
        DuffData[] space = new DuffData[1];
        space[0] = new DuffData();
        space[0].setData((char)32, 1);

        int count = 0;
        for (font = 0; font < 5; font++) {
            for (ord = 32; ord < 255; ord++) {
                DuffCode tmw;
                if ((tmw = TibetanMachineWeb.mapTMtoTMW(font, ord, 0)) != null) {
                    equivalent[0].setData((char)ord, font + 1);
                    tmwEquivalent[0].setData(tmw.getCharacter(), tmw.getFontNum());
                    try {
                        insertDuff(72, count++, achen, false);
                        insertDuff(72, count++, equivalent, false);
                        insertDuff(72, count++, achen, false);
                        insertDuff(72, count++, tmwEquivalent, true);

                    } catch (NullPointerException e) {
                        System.err.println("nullpointerexception happened: font is " + font + " ord is " + ord);
                    }
                    try {
                        String s = " font " + (font+1) + "; ord " + ord + "\n";
                        insertString(count, s, getRomanAttributeSet());
                        count += s.length();
                    } catch (BadLocationException e) {
                        throw new Error("badness");
                    }
                }
            }
        }
    }
}

/** A helper class used by TibetanDocument.convertHelper(..). */
class ConversionErrorHelper {
    boolean errorReturn;
    /** one more than the location of the last error glyph, or zero if no
     *  error glyphs yet exist */
    int errorGlyphLocation;
    boolean doErrorWrapup;
    int lastOffsetExamined;
    HashMap problemGlyphsTable;
    ConversionErrorHelper() {
        errorReturn = false;
        errorGlyphLocation = 0;
        doErrorWrapup = false;
        lastOffsetExamined = 0;
        problemGlyphsTable = new HashMap();
    }
}
