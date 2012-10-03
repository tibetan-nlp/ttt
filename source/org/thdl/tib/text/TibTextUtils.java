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
Library (THDL). Portions created by the THDL are Copyright 2001-2004 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.thdl.tib.text.tshegbar.LegalTshegBar;
import org.thdl.tib.text.tshegbar.UnicodeConstants;
import org.thdl.tib.text.tshegbar.UnicodeUtils;
import org.thdl.tib.text.ttt.ACIPTraits;
import org.thdl.tib.text.ttt.EWTSTraits;
import org.thdl.tib.text.ttt.TConverter;
import org.thdl.tib.text.ttt.TTraits;
import org.thdl.util.ThdlDebug;

/**
* Provides methods for converting back and forth between Extended
* Wylie/ACIP and Tibetan represented in TibetanMachineWeb glyphs.
* This class is not instantiable.
*
* <p> The class provides a variety of static methods for converting
* back and forth between Extended Wylie or ACIP and TibetanMachineWeb.
* The Wylie/ACIP can be accessed as a String, while the
* TibetanMachineWeb can be exported as Rich Text Format.
*
* @author Edward Garrett, Tibetan and Himalayan Digital Library */
public class TibTextUtils implements THDLWylieConstants {
    /** Change to true to see various things on System.out and
        System.err. */
    private static final boolean debug = false;

    /** Do not use this contructor. */
    private TibTextUtils() { super(); }


/**
* Converts a list of glyphs into an array of {@link DuffData DuffData}.
* The motivation for this is that most processes - for example using
* TibetanMachineWeb in HTML - only need to know what
* text to output, and when to change fonts. In general, they don't
* need to have an explicit indication for each glyph of the font
* for that glyph.
* @param glyphs the list of TibetanMachineWeb glyphs
* you want to convert
* @return an array of DuffData corresponding to this
* list of glyphs
*/
    public static DuffData[] convertGlyphs(List glyphs) {
        if (glyphs.size() == 0)
            return null;
        List data = new ArrayList();
        StringBuffer sb = new StringBuffer();
        Iterator iter = glyphs.iterator();
        DuffCode dc = (DuffCode)iter.next();
        int lastfont = dc.getFontNum();
        sb.append(dc.getCharacter());

        while (iter.hasNext()) {
            dc = (DuffCode)iter.next();
            if (dc.getFontNum() == lastfont)
                sb.append(dc.getCharacter());
            else {
                data.add(new DuffData(sb.toString(), lastfont));
                lastfont = dc.getFontNum();
                sb = new StringBuffer();
                sb.append(dc.getCharacter());
            }
        }

        data.add(new DuffData(sb.toString(), lastfont));

        DuffData[] dd = new DuffData[0];
        dd = (DuffData[])data.toArray(dd);
        return dd;
    }

/**
* Figures out how to arrange a list of characters into glyphs. For
* example, if the user types 'bsgr' using the Extended Wylie keyboard,
* this method figures out that this should be represented as a 'b'
* glyph followed by a 's-g-r' glyph. If you know that the characters
* do not contain Sanskrit stacks, or do not contain Tibetan stacks,
* then you can specify this to speed the process up. Otherwise, the
* method will first check to see if the characters correspond to any
* Tibetan stacks, and if not, then it will check for Sanskrit stacks.
* @param chars the list of Tibetan characters you want to find glyphs
* for
* @param areStacksOnRight whether stacking should try to maximize from
* right to left (true) or from left to right (false). In the Extended
* Wylie keyboard, you try to stack from right to left. Thus, the
* character sequence r-g-r would be stacked as r followed by gr,
* rather than rg followed by r. In the Sambhota and TCC keyboards, the
* stack direction is reversed.
* @param definitelyTibetan should be true if the characters are known
* to be Tibetan and not Sanskrit
* @param definitelySanskrit should be true if the characters are known
* to be Sanskrit and not Tibetan
*/
    public static List getGlyphs(List chars, boolean areStacksOnRight, boolean definitelyTibetan, boolean definitelySanskrit) {
        StringBuffer tibBuffer, sanBuffer;
        String tibCluster, sanCluster;

        boolean checkTibetan, checkSanskrit;

        if (!(definitelyTibetan || definitelySanskrit)) {
            checkTibetan = true;
            checkSanskrit = true;
        }
        else {
            checkTibetan = definitelyTibetan;
            checkSanskrit = definitelySanskrit;
        }

        int length = chars.size();

        List glyphs = new ArrayList();
        glyphs.clear();

        if (areStacksOnRight) {
            for (int i=0; i<length; i++) {
                tibBuffer = new StringBuffer();
                tibCluster = null;
        
                sanBuffer = new StringBuffer();
                sanCluster = null;

                for (int k=i; k<length; k++) {
                    String s = (String)chars.get(k);

                    if (checkTibetan)
                        tibBuffer.append(s);

                    if (checkSanskrit)
                        sanBuffer.append(s);

                    if (k!=length-1) {
                        if (checkTibetan)
                            tibBuffer.append("-");

                        if (checkSanskrit)
                            sanBuffer.append("+");
                    }
                }

                if (checkTibetan) {
                    tibCluster = tibBuffer.toString();

                    if (TibetanMachineWeb.hasGlyph(tibCluster)) {
                        Iterator iter = chars.iterator();
                        for (int k=0; k<i; k++) //should really check here to make sure glyphs exist FIXME
                            glyphs.add(TibetanMachineWeb.getGlyph((String)iter.next()));

                        glyphs.add(TibetanMachineWeb.getGlyph(tibCluster));
                        return glyphs;
                    }
                }

                if (checkSanskrit) {
                    sanCluster = sanBuffer.toString();

                    if (TibetanMachineWeb.hasGlyph(sanCluster)) {
                        Iterator iter = chars.iterator();
                        for (int k=0; k<i; k++) //should really check here to make sure glyphs exist FIXME
                            glyphs.add(TibetanMachineWeb.getGlyph((String)iter.next()));

                        glyphs.add(TibetanMachineWeb.getGlyph(sanCluster));
                        return glyphs;
                    }
                }
            }
        }
        else {
            for (int i=length-1; i>-1; i--) {
                tibBuffer = new StringBuffer();
                tibCluster = null;

                sanBuffer = new StringBuffer();
                sanCluster = null;

                Iterator iter = chars.iterator();

                for (int k=0; k<i+1; k++) {
                    String s = (String)iter.next();

                    if (checkTibetan)
                        tibBuffer.append(s);

                    if (checkSanskrit)
                        sanBuffer.append(s);

                    if (k!=i) {
                        if (checkTibetan)
                            tibBuffer.append("-");

                        if (checkSanskrit)
                            sanBuffer.append("+");
                    }
                }

                if (checkTibetan) {
                    tibCluster = tibBuffer.toString();

                    if (TibetanMachineWeb.hasGlyph(tibCluster)) {
                        glyphs.add(TibetanMachineWeb.getGlyph(tibCluster));
                        for (int k=i+1; k<length; k++)
                            glyphs.add(TibetanMachineWeb.getGlyph((String)iter.next()));

                        return glyphs;
                    }
                }

                if (checkSanskrit) {
                    sanCluster = sanBuffer.toString();

                    if (TibetanMachineWeb.hasGlyph(sanCluster)) {
                        glyphs.add(TibetanMachineWeb.getGlyph(sanCluster));
                        for (int k=i+1; k<length; k++)
                            glyphs.add(TibetanMachineWeb.getGlyph((String)iter.next()));

                        return glyphs;
                    }
                }
            }
        }

        return null;
    }

/**
* Finds the first meaningful element to occur within a string of
* Extended Wylie.  This could be a character, a vowel, punctuation, or
* formatting. For example, passed the string 'tshapo', this method
* will return 'tsh'.
* @param wylie the String of wylie you want to scan
* @return the next meaningful subpart of this string, or null if
* no meaningful subpart can be found (for example 'x' has no equivalent
* in Extended Wylie)
*/
    public static String getNext(String wylie) {
        boolean hasThereBeenValidity = false;
        boolean isThereValidity = false;
    
        String s;
        int i;
        int offset = 0;

        char c = wylie.charAt(offset);
        int k = (int)c;

        if (k < 32) //return null if character is just formatting
            return String.valueOf(c);

        if (c == WYLIE_DISAMBIGUATING_KEY)
            return String.valueOf(WYLIE_DISAMBIGUATING_KEY);
    
        if (c == WYLIE_SANSKRIT_STACKING_KEY)
            return String.valueOf(WYLIE_SANSKRIT_STACKING_KEY);

        for (i=offset+1; i<wylie.length()+1; i++) {
            s = wylie.substring(offset, i);

            if (!isThereValidity) {
                if (TibetanMachineWeb.isWyliePunc(s) || TibetanMachineWeb.isWylieVowel(s) || TibetanMachineWeb.isWylieChar(s)) {
                    isThereValidity = true;
                    hasThereBeenValidity = true;
                }
            }
            else {
                if (!TibetanMachineWeb.isWyliePunc(s) && !TibetanMachineWeb.isWylieVowel(s) && !TibetanMachineWeb.isWylieChar(s)) {
                    isThereValidity = false;
                    break;
                }
            }
        }

        if (!hasThereBeenValidity)
            s = null;

        else {
            if (isThereValidity) //the whole text region is valid
                s = wylie.substring(offset, wylie.length());

            else //the loop was broken out of
                s = wylie.substring(offset, i-1);
        }

        return s;
    }

    /** An array containing one boolean value.  Pass this to
        TibetanMachineWeb.getWylieForGlyph(..) if you don't care if a
        certain glyph has corresponding Wylie or not. */
    public static final boolean[] weDoNotCareIfThereIsCorrespondingWylieOrNot
        = new boolean[] { false };

/**
* Converts a string of transliteration into TibetanMachineWeb and
* inserts that into tdoc at offset loc.
* @param EWTSNotACIP true if you want THDL Extended Wylie, false if
* you want ACIP
* @param translit the transliteration you want to convert
* @param tdoc the document in which to insert the TMW
* @param loc the offset inside the document at which to insert the TMW
* @param withWarnings true if and only if you want warnings to appear
* in the output, such as "this could be a mistranscription of blah..."
* @throws InvalidTransliterationException if the transliteration is
* deemed invalid, i.e. if it does not conform to the transcription
* rules (those in the official document and the subtler rules pieced
* together by David Chandler through study and private correspondence
* with Robert Chilton (for ACIP), Than Garson, David Germano, Chris
* Fynn, and others)
* @return the number of characters inserted into tdoc */
    public static int insertTibetanMachineWebForTranslit(boolean EWTSNotACIP,
                                                         String translit,
                                                         TibetanDocument tdoc,
                                                         int loc,
                                                         boolean withWarnings)
        throws InvalidTransliterationException
    {
        StringBuffer errors = new StringBuffer();
        String warningLevel = withWarnings ? "All" : "None";
        
        TTraits traits = (EWTSNotACIP
                          ? (TTraits)EWTSTraits.instance()
                          : (TTraits)ACIPTraits.instance());
        ArrayList al = traits.scanner().scan(translit, errors, 500,
                                             false, warningLevel);
        if (null == al || errors.length() > 0) {
            if (errors.length() > 0)
                throw new InvalidTransliterationException(errors.toString());
            else
                throw new InvalidTransliterationException("Fatal error converting "
                                                          + traits.shortTranslitName()
                                                          + " to TMW.");
        }
        boolean colors = withWarnings;
        boolean putWarningsInOutput = false;
        if ("None" != warningLevel) {
            putWarningsInOutput = true;
        }
        try {
            int tloc[] = new int[] { loc };
            TConverter.convertToTMW(traits, al, tdoc, null, null,
                                    null, putWarningsInOutput, warningLevel,
                                    false, colors, tloc);
            return tloc[0] - loc;
        } catch (IOException e) {
            throw new Error("Can't happen: " + e);
        }
    }

/**
* Converts a string of Extended Wylie into {@link DuffData DuffData}.
* @param wylie the Wylie you want to convert
* @return an array of TibetanMachineWeb data
* corresponding to the Wylie text
* @throws InvalidWylieException if the Wylie is deemed invalid,
* i.e. if it does not conform to the Extended Wylie standard
* @deprecated by insertTibetanMachineWebForTranslit
*/
    public static DuffData[] getTibetanMachineWebForEWTS(String wylie) throws InvalidWylieException {
        ThdlDebug.noteIffyCode();  // deprecated method!
                                   // TODO(dchandler): remove it and
                                   // hopefully a ton of code that
                                   // only it uses.
        List chars = new ArrayList();
        DuffCode dc;
        int start = 0;
        boolean isSanskrit = false;
        boolean wasLastSanskritStackingKey = false;
        LinkedList glyphs = new LinkedList();

        while (start < wylie.length()) {
            String next = getNext(wylie.substring(start));

            if (next == null) {
                if (!chars.isEmpty()) {
                    glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));
                    chars.clear();
                    isSanskrit = false;
                }
                else { //could not convert - throw exception
                    // FIXME: we're printing to stdout!
                    if (start+5 <= wylie.length()) {
                        System.out.println("Bad wylie: "
                                           + wylie.substring(start,
                                                             start + 5));
                    } else {
                        System.out.println("Bad wylie: "+wylie.substring(start));
                    }
                    throw new InvalidWylieException(wylie, start);
                }
            }

            else if (TibetanMachineWeb.isWyliePunc(next)) {
                if (!chars.isEmpty())
                    glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));

                chars.clear();

                if (next.equals(BINDU)) {
                    if (glyphs.isEmpty())
                        dc = null;
                    else 
                        dc = (DuffCode)glyphs.removeLast(); //LinkedList implementation

                    getBindu(glyphs, dc);
                }                    

                else {
                    dc = TibetanMachineWeb.getGlyph(next);
                    glyphs.add(dc);
                }

                isSanskrit = false;
            }

            else if (TibetanMachineWeb.isWylieVowel(next)) {
                if (!chars.isEmpty()) {
                    glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));
                    dc = (DuffCode)glyphs.removeLast(); //LinkedList implementation
                    getVowel(glyphs, dc, next);
                    chars.clear();
                }
                else { //if previous is punctuation or null, then achen plus vowel - otherwise, previous could be vowel
                    int size = glyphs.size();

                    vowel_block: {
                        if (size > 1) {
                            dc = (DuffCode)glyphs.get(glyphs.size()-1);
                            if (!TibetanMachineWeb.isWyliePunc(TibetanMachineWeb.getWylieForGlyph(dc, weDoNotCareIfThereIsCorrespondingWylieOrNot))) {
                                DuffCode dc_2 = (DuffCode)glyphs.removeLast();
                                DuffCode dc_1 = (DuffCode)glyphs.removeLast();
                                getVowel(glyphs, dc_1, dc_2, next);
                                break vowel_block;
                            }
                        }
                        DuffCode[] dc_array = (DuffCode[])TibetanMachineWeb.getTibHash().get(ACHEN);
                        dc = dc_array[TibetanMachineWeb.TMW];
                        getVowel(glyphs, dc, next);
                    }

                    chars.clear();
                }

                isSanskrit = false;
            }

            else if (TibetanMachineWeb.isWylieChar(next)) {
                if (!isSanskrit) //add char to list - it is not sanskrit
                    chars.add(next);

                else if (wasLastSanskritStackingKey) { //add char to list - it is still part of sanskrit stack
                    chars.add(next);
                    wasLastSanskritStackingKey = false;
                }

                else { //char is no longer part of sanskrit stack, therefore compute and add previous stack
                    glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));
                    chars.clear();
                    chars.add(next);
                    isSanskrit = false;
                    wasLastSanskritStackingKey = false;
                }
            }

            else if (next.equals(String.valueOf(WYLIE_DISAMBIGUATING_KEY))) {
                if (!chars.isEmpty())
                    glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));

                chars.clear();
                isSanskrit = false;
            }

            else if (next.equals(String.valueOf(WYLIE_SANSKRIT_STACKING_KEY))) {
                if (!isSanskrit) { //begin sanskrit stack
                    switch (chars.size()) {
                        case 0:
                            break; //'+' is not "pre-stacking" key

                        case 1:
                            isSanskrit = true;
                            wasLastSanskritStackingKey = true;
                            break;

                        default:
                            String top_char = (String)chars.get(chars.size()-1);
                            chars.remove(chars.size()-1);
                                                        // DLC PERFORMANCE FIXME: make glyphs a parameter
                            glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));
                            chars.clear();
                            chars.add(top_char);
                            isSanskrit = true;
                            wasLastSanskritStackingKey = true;
                            break;
                    }
                }
            }

            else if (TibetanMachineWeb.isFormatting(next.charAt(0))) {
                if (!chars.isEmpty())
                    glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));

                dc = new DuffCode(1,next.charAt(0));
                glyphs.add(dc);
                chars.clear();
                isSanskrit = false;
            }

            if (next != null)
                start += next.length();
        }

        if (!chars.isEmpty()) {
            glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));
            chars.clear();
        }

        DuffData[] dd = convertGlyphs(glyphs);
        return dd;
    }

/**
* Gets the bindu sequence for a given context.  In the
* TibetanMachineWeb fonts, bindu (anusvara) is realized differently
* depending on which vowel it attaches to. Although the default bindu
* glyph is affixed to consonants and subscript vowels, for superscript
* vowels (i, e, o, etc), there is a single glyph which merges the
* bindu and that vowel together. When you pass this method a glyph
* context and a list, it will append to that list glyphs which will either consist
* of the original glyph followed by the default bindu glyph, or a
* composite vowel+bindu glyph.  Note that there is only one glyph in
* the context. This means that bindus will not affix properly if
* superscript vowels are allowed to directly precede subscript vowels
* (e.g. pou).
* @param list a List of DuffCode glyphs to which will be appended the
* original dc (if non-null) as well as a bindu, or the one glyph that
* represents both
* @param dc the DuffCode of the glyph you want to attach a bindu to,
* or null */
    public static void getBindu(List list, DuffCode dc) {
        if (null == dc) {
            list.add(TibetanMachineWeb.getGlyph(BINDU));
        } else {
            if (!TibetanMachineWeb.getBinduMap().containsKey(dc)) {
                list.add(dc);
                list.add(TibetanMachineWeb.getGlyph(BINDU));
            } else {
                list.add((DuffCode)TibetanMachineWeb.getBinduMap().get(dc));
            }
        }
    }

/**
* Gets the vowel sequence for a given vowel in a given context.  Given
* a context, this method affixes a vowel and returns the context (iff
* context_added[0] is false) plus the vowel. Generally, it is enough
* to provide just one glyph for context.
* @param context the glyph preceding the vowel you want to affix
* @param vowel the vowel you want to affix, in Wylie
* @param context_added an array of one boolean, an input/output
* parameter that, if true, means that only the vowel will be added to
* l, not the context, and if false, means that the context and the
* vowel will be added and that context_added[0] will be updated to be
* true
* @return a List of glyphs equal to the vowel in context
* @throws IllegalArgumentException if the given combination is not
* supported */
    public static void getVowel(List l, DuffCode context, String vowel, boolean context_added[]) {
        getVowel(l, null, context, vowel, context_added);
    }
    /** Wrapper that calls for adding context to l. */
    public static void getVowel(List l, DuffCode context, String vowel) {
        getVowel(l, null, context, vowel, new boolean[] { false });
    }
    /** Wrapper that calls for adding context to l. */
    public static void getVowel(List l, DuffCode context_1, DuffCode context_2, String vowel) {
        getVowel(l, context_1, context_2, vowel, new boolean[] { false });
    }

/**
* Gets the vowel sequence for a given vowel in a given context and
* appends it to l.  Given a context, this method affixes a vowel and
* appends the context (iff context_added[0] is false) plus the vowel
* to l.  Since the choice of vowel glyph depends on the consonant to
* which it is attached, generally it is enough to provide just the
* immediately preceding context. However, in some cases, double vowels
* are allowed - for example 'buo'. To find the correct glyph for 'o',
* we need 'b' in this case, not 'u'. Note also that some Extended
* Wylie vowels correspond to multiple glyphs in TibetanMachineWeb. For
* example, the vowel I consists of both an achung and a reverse
* gigu. All required glyphs are appended to l.
* @param context_1 the glyph occurring two glyphs before the vowel you
* want to affix
* @param context_2 the glyph immediately before the vowel you want to
* affix
* @param vowel the vowel you want to affix, in Wylie
* @param context_added an array of one boolean, an input/output
* parameter that, if true, means that only the vowel will be added to
* l, not the context, and if false, means that the context and the
* vowel will be added and that context_added[0] will be updated to be
* true
* @throws IllegalArgumentException if the given combination is not
* supported */

    public static void getVowel(List l, DuffCode context_1, DuffCode context_2,
                                String vowel, boolean context_added[])
        throws IllegalArgumentException
    {
        //this vowel doesn't correspond to a glyph -
        //so you just return the original context

        if (vowel.equals(WYLIE_aVOWEL)
            || TibetanMachineWeb.isTopVowel(context_2)) {
            if (TibetanMachineWeb.isTopVowel(context_2))
                throw new IllegalArgumentException("dropping vowels is bad1:" + vowel);
            if (!context_added[0]) {
                context_added[0] = true;
                if (context_1 != null)
                    l.add(context_1);

                l.add(context_2);
            }
            return;
        }

        //first, the three easiest cases: ai, au, and <i
        //these vowels have one invariant form - therefore,
        //dc_context is just returned along with that form

        if (vowel.equals(ai_VOWEL)) {
            if (!context_added[0]) {
                context_added[0] = true;
                if (context_1 != null)
                    l.add(context_1);

                l.add(context_2);
            }
            DuffCode[] dc_v = (DuffCode[])TibetanMachineWeb.getTibHash().get(ai_VOWEL);
            l.add(dc_v[TibetanMachineWeb.TMW]);
            return;
        }

        if (vowel.equals(au_VOWEL)) {
            if (!context_added[0]) {
                context_added[0] = true;
                if (context_1 != null)
                    l.add(context_1);

                l.add(context_2);
            }
            DuffCode[] dc_v = (DuffCode[])TibetanMachineWeb.getTibHash().get(au_VOWEL);
            l.add(dc_v[TibetanMachineWeb.TMW]);
            return;
        }

        if (vowel.equals(reverse_i_VOWEL)) {
            if (!context_added[0]) {
                context_added[0] = true;
                if (context_1 != null)
                    l.add(context_1);

                l.add(context_2);
            }
            
            if (!TibetanMachineWeb.isTopVowel(context_2)) {
                DuffCode[] dc_v = (DuffCode[])TibetanMachineWeb.getTibHash().get(reverse_i_VOWEL);
                l.add(dc_v[TibetanMachineWeb.TMW]);
            } else throw new IllegalArgumentException("dropping vowels is bad2:" + vowel);

            return;
        }

        //second, the vowels i, e, and o
        //these vowels have many different glyphs each,
        //whose correct selection depends on the
        //preceding context. therefore, dc_context is
        //returned along with the vowel appropriate to
        //that context

        if (vowel.equals(i_VOWEL)) {
            String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
            DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_i);
            if (null == dc_v && null != context_1) {
                hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_1);
                dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_i);
            }

            if (!context_added[0]) {
                context_added[0] = true;
                if (context_1 != null)
                    l.add(context_1);

                l.add(context_2);
            }
            
            if (null != dc_v)
                l.add(dc_v);
            else throw new IllegalArgumentException("dropping vowels is bad3:" + vowel);

            return;
        }
        // DLC perfect TMW->Wylie wouldn't produce o'i for an input file containing merely TMW9.61 -- it would produce \u0f7c,\u0f60,\u0f72 -- round-trip shows why.

        if (vowel.equals(e_VOWEL)) {
            String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
            DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_e);
            if (null == dc_v && null != context_1) {
                hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_1);
                dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_e);
            }

            if (!context_added[0]) {
                context_added[0] = true;
                if (context_1 != null)
                    l.add(context_1);

                l.add(context_2);
            }
            
            if (null != dc_v)
                l.add(dc_v);
            else throw new IllegalArgumentException("dropping vowels is bad4:" + vowel);

            return;
        }

        if (vowel.equals(o_VOWEL)) {
            String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
            DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_o);
            if (null == dc_v && null != context_1) {
                hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_1);
                dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_o);
            }

            if (!context_added[0]) {
                context_added[0] = true;
                if (context_1 != null)
                    l.add(context_1);

                l.add(context_2);
            }

            if (null != dc_v)
                l.add(dc_v);
            else throw new IllegalArgumentException("dropping vowels is bad5:" + vowel);

            return;
        }

        //next come the vowels u, A, and U
        //these three vowels are grouped together because they all
        //can cause the preceding context to change. in particular,
        //both u and A cannot be affixed to ordinary k or g, but
        //rather the shortened versions of k and g - therefore,

        if (vowel.equals(u_VOWEL)) {
            String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
            DuffCode halfHeight = TibetanMachineWeb.getHalfHeightGlyph(hashKey_context);
            DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_u);

            if (!context_added[0]) {
                context_added[0] = true;
                if (null != context_1)
                    l.add(context_1);

                if (null == halfHeight)
                    l.add(context_2);
                else
                    l.add(halfHeight);
            }
            
            if (null != dc_v)
                l.add(dc_v);
            else throw new IllegalArgumentException("dropping vowels is bad6:" + vowel);

            return;
        }

        if (vowel.equals(A_VOWEL)) {
            String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
            DuffCode halfHeight = TibetanMachineWeb.getHalfHeightGlyph(hashKey_context);
            DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_A);

            if (!context_added[0]) {
                context_added[0] = true;
                if (null != context_1)
                    l.add(context_1);

                if (null == halfHeight)
                    l.add(context_2);
                else
                    l.add(halfHeight);
            }
            
            if (null != dc_v)
                l.add(dc_v);
            else throw new IllegalArgumentException("dropping vowels is bad7:" + vowel);

            return;
        }

        if (vowel.equals(U_VOWEL)) {
            String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
            DuffCode halfHeight = TibetanMachineWeb.getHalfHeightGlyph(hashKey_context);
            DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_U);

            if (!context_added[0]) {
                context_added[0] = true;
                if (null != context_1)
                    l.add(context_1);

                if (null == halfHeight)
                    l.add(context_2);
                else
                    l.add(halfHeight);
            }
            
            if (null != dc_v && !TibetanMachineWeb.isTopVowel(context_2))
                l.add(dc_v);
            else throw new IllegalArgumentException("dropping vowels is bad8:" + vowel);

            return;
        }

        //finally, the vowels I and <I
        //these vowels are unique in that they both
        //require a change from the previous character,
        //and consist of two glyphs themselves

        if (vowel.equals(I_VOWEL)) {
            String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
            DuffCode halfHeight = TibetanMachineWeb.getHalfHeightGlyph(hashKey_context);
            DuffCode dc_v_sub = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_A);
            DuffCode dc_v_sup = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_i);

            if (!context_added[0]) {
                context_added[0] = true;
                if (null != context_1)
                    l.add(context_1);

                if (null == halfHeight)
                    l.add(context_2);
                else
                    l.add(halfHeight);
            }
            
            if (null != dc_v_sub && null != dc_v_sup) {
                l.add(dc_v_sub);
                l.add(dc_v_sup);
            } else throw new IllegalArgumentException("dropping vowels is bad9:" + vowel);

            return;
        }

        if (vowel.equals(reverse_I_VOWEL)) {
            String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
            DuffCode halfHeight = TibetanMachineWeb.getHalfHeightGlyph(hashKey_context);
            DuffCode dc_v_sub = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_A);
            DuffCode[] tv_array = (DuffCode[])TibetanMachineWeb.getTibHash().get(reverse_i_VOWEL);
            DuffCode dc_v_sup = tv_array[TibetanMachineWeb.TMW];

            if (!context_added[0]) {
                context_added[0] = true;
                if (null != context_1)
                    l.add(context_1);

                if (null == halfHeight)
                    l.add(context_2);
                else
                    l.add(halfHeight);
            }
            
            if (null != dc_v_sub && null != dc_v_sup) {
                l.add(dc_v_sub);
                l.add(dc_v_sup);
            } else throw new IllegalArgumentException("dropping vowels is bad10:" + vowel);

            return;
        }

        throw new IllegalArgumentException("bad vowel " + vowel);
    }

    /**
     * True if you want TibetanMachineWeb-to-Extended-Wylie conversion
     * to produce Wylie that, if typed, will produce the same sequence
     * of TibetanMachineWeb glyphs.  Without it, converting the glyphs
     * you get from typing jskad, skaska, skaskaska, skaskaskaska,
     * etc. will not give you Wylie, that, if typed in again, will
     * produce the original glyphs.  Hence, if this is true, then you
     * get working, end-to-end Wylie for syntactically illegal
     * sequences of glyphs. */
    private static final boolean makeIllegalTibetanGoEndToEnd = true;


    /** Returns "a"/"A", unless wylie (which really is EWTS, not ACIP)
        is already "a". */
    private static String aVowelToUseAfter(boolean EWTSNotACIP, String wylie) {
        if (wylie.equals(ACHEN) && EWTSNotACIP) {
            /* it's EWTS{a}, not EWTS{aa}, for achen alone. But it's
               ACIP{AA}. */
            return "";
        } else
            return ((EWTSNotACIP)
                    ? WYLIE_aVOWEL : "A" /* hard-coded ACIP constant */);
    }

    private static String unambiguousPostAVowelTranslit(boolean EWTSNotACIP,
                                                        String wylie1,
                                                        String wylie2,
                                                        String acip1,
                                                        String acip2) {
        String disambiguator = "";
        // type "lard" vs. "lar.d", and you'll see the need for this
        // disambiguation of suffix and postsuffix.  sa doesn't take
        // any head letters, so only da needs to be considered.
        if (TibetanMachineWeb.isWylieTop(wylie1)
            && wylie2.equals(/* FIXME: hard-coded */ "d"))
            disambiguator = (EWTSNotACIP) ? WYLIE_DISAMBIGUATING_KEY_STRING : "-";
        if (EWTSNotACIP)
            return wylie1 + disambiguator + wylie2;
        else
            return acip1 + disambiguator + acip2;
    }

/**
* Gets the Extended Wylie for the given sequence of glyphs if
* EWTSNotACIP is true, or the ACIP otherwise.
* @param EWTSNotACIP true if you want THDL Extended Wylie, false if
* you want ACIP
* @param dcs an array of TMW glyphs
* @param noSuch an array which will not be touched if this is
* successful; however, if there is no THDL Extended Wylie/ACIP
* corresponding to these glyphs, then noSuch[0] will be set to true
* @return the Extended Wylie/ACIP corresponding to these glyphs (with
* font size info), or null */
    public static TranslitList getTranslit(boolean EWTSNotACIP,
                                           SizedDuffCode[] dcs,
                                           boolean noSuch[]) {
        StringBuffer warnings = (debug ? new StringBuffer() : null);
        TranslitList ans
            = getTranslitImplementation(EWTSNotACIP, dcs, noSuch, warnings);
        if (debug && warnings.length() > 0)
            System.out.println("DEBUG:     warnings in TMW->Wylie: " + warnings);
        return ans;
    }

    /** True for and only for ma and nga because 'am and 'ang are
        appendages. */
    private static final boolean isAppendageNonVowelWylie(String wylie) {
        return (MA.equals(wylie) /* 'AM */
                || NGA.equals(wylie) /* 'ANG, 'UNG */
                || SA.equals(wylie) /* 'OS, 'US, maybe 'IS */
                || RA.equals(wylie) /* 'UR */
                );
    }

    // DLC FIXME: {H}, U+0F7F, is part of a grapheme cluster!
    // David Chapman and I both need a comprehensive list of these
    // guys.  Get it from Unicode 4.0 spec?
    /** Scans the TMW glyphs in glyphList and creates the returned
        list of grapheme clusters based on them.  A grapheme cluster
        is a consonant or consonant stack with optional adornment or a
        number (possibly super- or subscribed) or some other glyph
        alone. */
    private static TGCList breakTshegBarIntoGraphemeClusters(java.util.List glyphList,
                                                             boolean noSuchWylie[]) {
        // Definition: adornment means vowels and achungs and bindus.
        // It should be this, though (FIXME): any combining
        // characters.

        int sz = glyphList.size();
        ThdlDebug.verify(sz > 0);

        // A list of grapheme clusters (see UnicodeGraphemeCluster).
        // sz is an overestimate (speeds us up, wastes some memory).
        TMWGCList gcs = new TMWGCList(sz);

        StringBuffer buildingUpVowel = new StringBuffer(); // for {cui}, we append to this guy twice.
        String nonVowelWylie = null; // for the "c" in {cui}
        int pairType = TGCPair.TYPE_OTHER;

        for (int i = 0; i < sz; i++) {
            DuffCode dc = ((SizedDuffCode)glyphList.get(i)).getDuffCode();
            String wylie = TibetanMachineWeb.getWylieForGlyph(dc, noSuchWylie);
            boolean buildingUpSanskritNext = false;
            if ((buildingUpSanskritNext
                 = (TibetanMachineWeb.isWylieSanskritConsonantStack(wylie)
                    ||
                    /* U+0FAD, which should become ACIP "V", not "W",
                       though the EWTS is "w" just as it is for
                       TMW(fontNum==1).53: */
                    (8 == dc.getFontNum() && 69 == dc.getCharNum())))
                || TibetanMachineWeb.isWylieTibetanConsonantOrConsonantStack(wylie)) {
                if (buildingUpVowel.length() > 0 || null != nonVowelWylie) {
                    gcs.add(new TGCPair(nonVowelWylie,
                                        buildingUpVowel.toString(),
                                        pairType));
                    buildingUpVowel.delete(0, buildingUpVowel.length());
                }
                // We want {p-y}, not {py}.
                nonVowelWylie
                    = TibetanMachineWeb.getHashKeyForGlyph(dc.getFontNum(), dc.getCharNum());
                pairType = (buildingUpSanskritNext
                            ? TGCPair.TYPE_SANSKRIT
                            : TGCPair.TYPE_TIBETAN);
            } else if (TibetanMachineWeb.isWylieAdornmentAndContainsVowel(wylie)
                       || TibetanMachineWeb.isWylieAdornment(wylie)) {
                buildingUpVowel.append(wylie);
                // DLC FIXME: I bet three or four vowels together
                // breaks TMW->ACIP and TMW->EWTS.  Test it.  When it
                // does, revamp TGCPair to have a set of vowels.  The
                // output order should be consistent with the
                // Unicode-imposed order on vowels.  (Maybe modulo the
                // CCV bug in Unicode w.r.t. above- and below-base
                // vowels?)
            } else {
                // number or weird thing:

                if (buildingUpVowel.length() > 0 || null != nonVowelWylie) {
                    gcs.add(new TGCPair(nonVowelWylie,
                                        buildingUpVowel.toString(),
                                        pairType));
                    buildingUpVowel.delete(0, buildingUpVowel.length());
                    nonVowelWylie = null;
                }
                gcs.add(new TGCPair(wylie, null, TGCPair.TYPE_OTHER));
                pairType = TGCPair.TYPE_OTHER;
            }
        }
        if (buildingUpVowel.length() > 0 || null != nonVowelWylie) {
            gcs.add(new TGCPair(nonVowelWylie,
                                buildingUpVowel.toString(),
                                pairType));
        }
        return gcs;
    }


    /** Returns a string that classifies gcs as a legal Tibetan tsheg
     *  bar, a single Sanskrit grapheme cluster
     *  ("single-sanskrit-gc"), or invalid ("invalid").  If
     *  noPrefixTests is true, then ggyi will be seen as a
     *  "prefix-root", even though gya doesn't take a ga prefix. */
    public static String getClassificationOfTshegBar(TGCList gcs,
                                                     // DLC the warnings are Wylie-specific
                                                     StringBuffer warnings,
                                                     boolean noPrefixTests) {
        String candidateType = null;
        // Now that we have grapheme clusters, see if they match any
        // of the "legal tsheg bars":
        int sz = gcs.size();
        if (sz == 1) {
            TGCPair tp = gcs.get(0);
            int cls = tp.classification;
            if (TGCPair.SANSKRIT_WITHOUT_VOWEL == cls
                || TGCPair.SANSKRIT_WITH_VOWEL == cls)
                return "single-sanskrit-gc";
        }
        TGCPair lastPair = null;
        for (int i = 0; i < sz; i++) {
            TGCPair tp = gcs.get(i);
            int cls = tp.classification;
            String wylie = tp.getWylie();
            if (TGCPair.OTHER == cls) {
                if (TibetanMachineWeb.isWylieNumber(wylie)) {
                    if (null == candidateType) {
                        candidateType = "number";
                    } else {
                        if ("number" != candidateType) {
                            if (null != warnings)
                                warnings.append("Found something odd; the wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    }
                } else {
                    if (null != warnings)
                        warnings.append("Found something odd; the wylie is " + wylie + "\n");
                    candidateType = "invalid";
                    break;
                }
            } else if (TGCPair.SANSKRIT_WITHOUT_VOWEL == cls
                       || TGCPair.SANSKRIT_WITH_VOWEL == cls) {
                candidateType = "invalid";
                break;
            } else if (TGCPair.CONSONANTAL_WITHOUT_VOWEL == cls
                       || TGCPair.CONSONANTAL_WITH_VOWEL == cls) {
                if (null == candidateType) {
                    if (TibetanMachineWeb.isWylieLeft(wylie)) {
                        candidateType = "prefix/root";
                    } else {
                        candidateType = "root";
                    }
                } else {
                    if ("prefix/root" == candidateType) {
                        if (ACHUNG.equals(wylie)) {
                            // peek ahead to distinguish between ba's,
                            // ba'ala and ba'am:
                            TGCPair nexttp = (i+1 < sz) ? gcs.get(i+1) : null;
                            String nextwylie = (nexttp == null) ? "" : nexttp.getWylie();
                            if (isAppendageNonVowelWylie(nextwylie)) {
                                candidateType = "maybe-appendaged-prefix/root";
                            } else {
                                if (noPrefixTests
                                    || isLegalPrefixRootCombo(lastPair.getConsonantWylie(),
                                                              tp.getConsonantWylie()))
                                    candidateType = "prefix/root-root/suffix";
                                else
                                    candidateType = "root-suffix";
                            }
                        } else if (TibetanMachineWeb.isWylieRight(wylie)) {
                            if (noPrefixTests
                                || isLegalPrefixRootCombo(lastPair.getConsonantWylie(),
                                                          tp.getConsonantWylie()))
                                candidateType = "prefix/root-root/suffix";
                            else
                                candidateType = "root-suffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-prefix/root";
                        } else {
                            if (noPrefixTests
                                || isLegalPrefixRootCombo(lastPair.getConsonantWylie(),
                                                          tp.getConsonantWylie()))
                                candidateType = "prefix-root";
                            else {
                                if (null != warnings)
                                    warnings.append("Found what would be a prefix-root combo, but the root stack with wylie " + wylie + " does not take the prefix with wylie " + lastPair.getConsonantWylie());
                                candidateType = "invalid";
                                break;
                            }
                        }
                    } else if ("root" == candidateType) {
                        if (ACHUNG.equals(wylie)) {
                            // peek ahead to distinguish between pa's,
                            // pa'ala and pa'am:
                            TGCPair nexttp = (i+1 < sz) ? gcs.get(i+1) : null;
                            String nextwylie = (nexttp == null) ? "" : nexttp.getWylie();
                            if (isAppendageNonVowelWylie(nextwylie)) {
                                candidateType = "maybe-appendaged-root";
                            } else {
                                candidateType = "root-suffix";
                            }
                        } else if (TibetanMachineWeb.isWylieRight(wylie)) {
                            candidateType = "root-suffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-root";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a non-prefix consonant or consonant stack followed by a consonant or consonant stack that is not simply a suffix; that thing's wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("prefix-root" == candidateType) {
                        if (ACHUNG.equals(wylie)) {
                            // peek ahead to distinguish between bpa's,
                            // bpa'ala and bpa'am:
                            TGCPair nexttp = (i+1 < sz) ? gcs.get(i+1) : null;
                            String nextwylie = (nexttp == null) ? "" : nexttp.getWylie();
                            if (isAppendageNonVowelWylie(nextwylie)) {
                                candidateType = "maybe-appendaged-prefix-root";
                            } else {
                                candidateType = "prefix-root-suffix";
                            }
                        } else if (TibetanMachineWeb.isWylieRight(wylie)) {
                            candidateType = "prefix-root-suffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-prefix-root";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a prefix plus a root stack plus a non-suffix consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("prefix/root-root/suffix" == candidateType) {
                        // this has no peekahead, gag'am works.
                        if (ACHUNG.equals(wylie)) {
                            // peek ahead to distinguish between
                            // gga'am and gaga'ala:
                            TGCPair nexttp = (i+1 < sz) ? gcs.get(i+1) : null;
                            String nextwylie = (nexttp == null) ? "" : nexttp.getWylie();
                            if (isAppendageNonVowelWylie(nextwylie)) {
                                candidateType = "maybe-appendaged-prefix/root-root/suffix";
                            } else {
                                candidateType = "prefix-root-suffix";
                            }
                        } else if (TibetanMachineWeb.isWylieFarRight(wylie)) {
                            candidateType = "prefix/root-root/suffix-suffix/postsuffix";
                        } else if (TibetanMachineWeb.isWylieRight(wylie)) {
                            candidateType = "prefix-root-suffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-prefix/root-root/suffix";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a prefix/root stack plus a suffix/root stack plus a non-suffix, non-postsuffix consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("root-suffix" == candidateType) {
                        // This has no peekahead w.r.t. 'am and 'ang,
                        // but it needs none because we peeked to be
                        // sure that this was root-suffix and not
                        // maybe-appendaged-root.
                        if (TibetanMachineWeb.isWylieFarRight(wylie)) {
                            candidateType = "root-suffix-postsuffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-root-suffix";
                        } else if (ACHUNG.equals(wylie)) {
                            candidateType = "maybe-appendaged-root-suffix";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a root stack plus a suffix plus a non-postsuffix consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("prefix/root-root/suffix-suffix/postsuffix" == candidateType
                               || "prefix-root-suffix" == candidateType) {
                        // this has no peekahead and needs none.
                        if (TibetanMachineWeb.isWylieFarRight(wylie)) {
                            candidateType = "prefix-root-suffix-postsuffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            // if we simply prepended to
                            // candidateType, we wouldn't get interned
                            // strings.
                            candidateType = ("appendaged-" + candidateType).intern();
                        } else if (ACHUNG.equals(wylie)) {
                            candidateType = ("maybe-appendaged-" + candidateType).intern();
                        } else {
                            if (null != warnings)
                                warnings.append("Found a prefix/root stack plus a suffix/root stack plus a suffix/postsuffix plus a non-postsuffix consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("prefix-root-suffix-postsuffix" == candidateType) {
                        // this has no peekahead and needs none.
                        if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-prefix-root-suffix-postsuffix";
                        } else if (ACHUNG.equals(wylie)) {
                            candidateType = "maybe-appendaged-prefix-root-suffix-postsuffix";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a prefix plus root stack plus suffix plus postsuffix; then found yet another consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("root-suffix-postsuffix" == candidateType) {
                        // this has no peekahead and needs none.
                        if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-root-suffix-postsuffix";
                        } else if (ACHUNG.equals(wylie)) {
                            candidateType = "maybe-appendaged-root-suffix-postsuffix";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a root stack plus suffix plus postsuffix; then found yet another consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if (candidateType.startsWith("maybe-appendaged-")) {
                        if (isAppendageNonVowelWylie(wylie)) {
                            candidateType
                                = candidateType.substring("maybe-".length()).intern();
                        } else {
                            if (null != warnings)
                                warnings.append("Found a tsheg bar that has an achung (" + ACHUNG + ") tacked on, followed by some other thing whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if (candidateType.startsWith("appendaged-")) {
                        if (TibetanMachineWeb.isWylieAchungAppendage(wylie)
                            // 'ang:
                            || TibetanMachineWeb.isWylieAchungAppendage(lastPair.getWylie() + wylie)
                            // 'ongs, as in ma'ongs:
                            || (i > 1
                                && TibetanMachineWeb.isWylieAchungAppendage(gcs.get(i-2).getWylie() + lastPair.getWylie() + wylie))) {
                            // candidateType stays what it is.
                        } else if (ACHUNG.equals(wylie)) {
                            candidateType = ("maybe-" + candidateType).intern();
                        } else {
                            if (null != warnings)
                                warnings.append("Found a tsheg bar that has a 'i, 'e, 'o, 'u, or 'ang 'am appendage already and then found yet another consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else {
                        if ("invalid" == candidateType)
                            throw new Error("forgot to break out of the for loop after diagnosing invalidity.");
                        if ("number" != candidateType)
                            throw new Error("missed a case; case is " + candidateType);
                        if (null != warnings)
                            warnings.append("Found a consonant or consonant stack after something odd; the consonantish thing has wylie " + wylie + "\n");
                        candidateType = "invalid";
                        break;
                    }
                }
            } else if (TGCPair.LONE_VOWEL == cls) {
                if (null != warnings)
                    warnings.append("Found a vowel that did not follow either a Tibetan consonant or consonant stack or another vowel.");
                candidateType = "invalid";
                break;
            } else {
                throw new Error("bad cls");
            }
            lastPair = tp;
        }
        if (null == candidateType) candidateType = "invalid";
        if (candidateType.startsWith("maybe-appendaged-")) {
            if (null != warnings)
                warnings.append("Found a tsheg bar that has an extra achung (" + ACHUNG + ") tacked on\n");
            candidateType = "invalid";
        }
        return candidateType;
    }

    /** Appends to translitBuffer the EWTS/ACIP for the glyph list
        glyphList (which should be an ArrayList for speed).  The font
        size of the transliteration will be fontSize.  The
        transliteration will be very user-friendly for "legal tsheg
        bars" and will be valid, but possibly ugly (interspersed with
        disambiguators or extra vowels, etc.) Wylie/ACIP for other
        things, such as Sanskrit transliteration.  Updates warnings
        and noSuch like the caller does.

        <p>What constitutes a legal, non-punctuation, non-whitespace
        tsheg bar?  The following are the only such:</p>
        <ul>
          <li>one or more numbers</li>

          <li>a legal "tyllable" appended with zero or more particles
              from the set { 'i, 'o, 'u, 'e, 'ang, 'am }</li>
        </ul>

        <p>A "tyllable" is, by definition, one of the following:</p>

        <ul>
          <li>a single, possibly adorned consonant stack</li>

          <li>two consonant stacks where one is a single,
              unadorned consonant (and is a prefix it it is first and
              a suffix if it is last) and the other is possibly
              adorned</li>

          <li>three consonant stacks where at most one has adornment.
              If the second has adornment, then the first must be an
              unadorned prefix consonant and the last must be an
              unadorned suffix consonant.  If the first has adornment,
              then the second must be an unadorned suffix consonant
              and the third must be an unadorned secondary suffix
              consonant.</li>

          <li>four consonant stacks where either none is adorned or
              only the second consonant stack is adorned, the first is
              an unadorned prefix consonant, the third is an unadorned
              suffix consonant, and the fourth is an unadorned
              secondary suffix consonant.</li>

        </ul>
        
        <p>When there are three unadorned consonant stacks in a
           tyllable, a hard-coded list of valid Tibetan tsheg bars is
           relied upon to determine if the 'a'/'A' vowel comes after
           the first or the second consonant.</p> */
    private static void getTshegBarTranslit(boolean EWTSNotACIP,
                                            java.util.List glyphList,
                                            boolean noSuch[],
                                            StringBuffer warnings,
                                            TranslitList translitBuffer) {
        // FIXME: If font size changes within a tsheg-bar, we don't
        // handle that.
        int fontSize = ((SizedDuffCode)glyphList.get(0)).getFontSize();
        TGCList gcs
            = breakTshegBarIntoGraphemeClusters(glyphList, noSuch);
        String candidateType = getClassificationOfTshegBar(gcs, warnings, false);
        if (debug) System.out.println("DEBUG: tsheg bar classification is " + candidateType);
        int sz = gcs.size();
        if (candidateType == "invalid"
            || candidateType == "single-sanskrit-gc") {
            // Forget beauty and succintness -- just be sure to
            // generate transliteration that can be converted
            // unambiguously into Tibetan.  Use a disambiguator or
            // vowel after each grapheme cluster.
            //
            // If we truly didn't care about beauty, we'd just lump
            // SANSKRIT_WITHOUT_VOWEL and SANSKRIT_WITH_VOWEL into
            // OTHER.

            for (int i = 0; i < sz; i++) {
                TGCPair tp = (TGCPair)gcs.get(i);
                int cls = tp.classification;
                String wylie = tp.getWylie();
                String translit = (EWTSNotACIP) ? wylie : tp.getACIP();
                if (TibetanMachineWeb.isWylieVowel(wylie) && i > 0) {
                    // au would be achen with au vowel, so use a.u; ai
                    // would be achen with ai vowel, so use a.i; l-i
                    // won't happen, you'd see la-i or gla-i, not l-i
                    // or gl-i; similarly for r-i, r-I, and l-I.

                    // Even though we only need it for ka.u and ka.i
                    // and a.u and a.i, we always do it (see Rule 10
                    // of the September 1, 2003 draft of EWTS
                    // standard).
                    translitBuffer.append(WYLIE_DISAMBIGUATING_KEY, fontSize);
                }
                translitBuffer.append(translit, fontSize);
                if (TibetanMachineWeb.isWylieTibetanConsonantOrConsonantStack(wylie)
                    || TibetanMachineWeb.isWylieSanskritConsonantStack(wylie)) {
                    translitBuffer.append(aVowelToUseAfter(EWTSNotACIP, wylie), fontSize);
                } else if (i + 1 < sz) {
                    if (TGCPair.CONSONANTAL_WITH_VOWEL != cls
                        && TGCPair.SANSKRIT_WITH_VOWEL != cls)
                        translitBuffer.append(EWTSNotACIP
                                              ? WYLIE_DISAMBIGUATING_KEY : '-',
                                              fontSize);
                }
            }
        } else {
            // Generate perfect, beautiful transliteration, using the
            // minimum number of vowels and disambiguators.

            int leftover = sz + 1;

            // Appendaged vs. not appendaged?  it affects nothing at
            // this stage except for pa'm vs. pa'am and
            // appendaged-prefix/root-root/suffix (e.g., 'ad'i
            // (incorrect) vs. 'da'i (correct)).
            boolean appendaged = (candidateType.startsWith("appendaged-"));
            candidateType = getCandidateTypeModuloAppendage(candidateType);

            if ("prefix/root-root/suffix-suffix/postsuffix" == candidateType) {
                /* Update: Chris Fynn wrote this in response to an
e-mail from David Chapman on Feb 21, 2005:

<quote Chris Fynn feb 21 2005>
When working out the rules for Tibetan and Dzongkha
collation in Bhutan we came up with the following sequences
that could be ambiguous:

0F51 0F42 0F66
0F60 0F42 0F66
0F51 0F44 0F66
0F42 0F53 0F51
0F58 0F53 0F51
0F56 0F42 0F66
0F51 0F56 0F66
0F60 0F56 0F66
0F58 0F42 0F66
0F58 0F44 0F66
0F51 0F58 0F66

After much consultation with experts in Bhutan it was
decided these should always be read as follows:

0F51 0F42 0F66  dgas
0F60 0F42 0F66  'gas
0F51 0F44 0F66  dngas *
0F42 0F53 0F51  gnad
0F58 0F53 0F51  mnad *
0F56 0F42 0F66  bags
0F51 0F56 0F66  dbas
0F60 0F56 0F66  'bas *
0F58 0F42 0F66  mags
0F58 0F44 0F66  mangs
0F51 0F58 0F66  dmas

In most cases it was found that only one of the two possible
readings actually existed as words. 0F51 0F44 0F66 , 0F58
0F53 0F51, and 0F60 0F56 0F66 were not found as syllables in
any known words, but the experts felt that *if* they
occurred in Tibetan or Dzongkha text then dngas, mnad, and
'bas would be the most likely reading.
</quote>



    Because of this e-mail, dbas and dngas were added to the list of
    exceptions.  */
                /* Yes, this is ambiguous. How do we handle it?  See
                 * this from Andres (but note that only 4 of the 14 in
                 * the second list are ambiguous because ra na sa and
                 * la are not prefixes):
                 *
                 * <quote>
                 * I'm posting this upon David Chandler's
                 * request. According to Lobsang Thonden in Modern
                 * Tibetan Grammar Language (page 42), with regards to
                 * identifying the root letter in 3 lettered words
                 * there are only 23 ambiguous cases. He writes:
                 *
                 * If the last letter is 'sa' and the first two
                 * letters are affixes, then the SECOND ONE is the
                 * root letter in the following 9 WORDS ONLY:
                 *
                 * gdas gnas gsas dgas dmas bdas mdas 'gas 'das [NOTE:
                 * Andres later came across 'bad, so we handle it this
                 * way also]
                 *
                 * And the FIRST is the root letter in the following
                 * 14 WORDS ONLY:
                 *
                 * rags lags nags bags bangs gangs rangs langs nangs
                 * sangs babs rabs rams nams
                 *
                 * As I mentioned before, I think that the best
                 * solution for now is to hard-wire these cases. Even
                 * if the list is not exhaustive, at least we'll have
                 * most cases covered.
                 * </quote>
                 *
                 * But there's more to the rule, as bug 998476 shows:
                 * bsad is correct, not bas.d, so we have to interpret
                 * as prefix-root-suffix. */

                leftover = 3;
                /* FIXME: these constants are hard-wired here, rather
                 * than in THDLWylieConstants, because I'm lazy. */
                String wylie1 = ((TGCPair)gcs.get(0)).getWylie();
                String wylie2 = ((TGCPair)gcs.get(1)).getWylie();
                String wylie3 = ((TGCPair)gcs.get(2)).getWylie();
                String acip1 = (EWTSNotACIP) ? null : ((TGCPair)gcs.get(0)).getACIP();
                String acip2 = (EWTSNotACIP) ? null : ((TGCPair)gcs.get(1)).getACIP();
                String acip3 = (EWTSNotACIP) ? null : ((TGCPair)gcs.get(2)).getACIP();
                if (wylie3.equals("d")
                    || ((wylie1.equals("g") && (wylie2.equals("d")
                                                || wylie2.equals("n")
                                                || wylie2.equals("s")))
                        || (wylie1.equals("d") && (wylie2.equals("g")
                                                   || wylie2.equals("m")
                                                   || wylie2.equals("b")
                                                   || wylie2.equals("ng")))
                        || (wylie1.equals("b") && wylie2.equals("d"))
                        || (wylie1.equals("m") && wylie2.equals("d"))
                        || (wylie1.equals("'") && (wylie2.equals("g")
                                                   || wylie2.equals("d")
                                                   || wylie2.equals("b"))))) {
                    // prefix-root-suffix
                    if (TibetanMachineWeb.isAmbiguousWylie(wylie1, wylie2)) {
                        if (EWTSNotACIP) {
                            translitBuffer.append(wylie1
                                                  + WYLIE_DISAMBIGUATING_KEY
                                                  + wylie2,
                                                  fontSize);
                        } else {
                            translitBuffer.append(acip1 + '-' + acip2,
                                                  fontSize);
                        }
                    } else {
                        if (EWTSNotACIP) {
                            translitBuffer.append(wylie1 + wylie2,
                                                  fontSize);
                        } else {
                            translitBuffer.append(acip1 + acip2,
                                                  fontSize);
                        }
                    }

                    translitBuffer.append(aVowelToUseAfter(EWTSNotACIP, wylie2)
                                          + (EWTSNotACIP ? wylie3 : acip3),
                                          fontSize);
                } else {
                    // root-suffix-postsuffix
                    if (EWTSNotACIP)
                        translitBuffer.append(wylie1
                                              + aVowelToUseAfter(EWTSNotACIP, wylie1)
                                              + unambiguousPostAVowelTranslit(EWTSNotACIP,
                                                                              wylie2,
                                                                              wylie3,
                                                                              acip2,
                                                                              acip3),
                                              fontSize);
                    else
                        translitBuffer.append(acip1
                                              + aVowelToUseAfter(EWTSNotACIP, wylie1)
                                              + unambiguousPostAVowelTranslit(EWTSNotACIP,
                                                                              wylie2,
                                                                              wylie3,
                                                                              acip2,
                                                                              acip3),
                                              fontSize);
                }
            } else if ("root" == candidateType
                       || (!appendaged
                           && "prefix/root-root/suffix" == candidateType)
                       || "prefix/root" == candidateType
                       || "root-suffix-postsuffix" == candidateType
                       || "root-suffix" == candidateType) {
                String wylie1 = ((TGCPair)gcs.get(0)).getWylie();
                String acip1 = (EWTSNotACIP) ? null : ((TGCPair)gcs.get(0)).getACIP();
                leftover = 1;
                translitBuffer.append((EWTSNotACIP) ? wylie1 : acip1, fontSize);
                if (((TGCPair)gcs.get(0)).classification
                    != TGCPair.CONSONANTAL_WITH_VOWEL) {
                    ThdlDebug.verify(TGCPair.CONSONANTAL_WITHOUT_VOWEL
                                     == ((TGCPair)gcs.get(0)).classification);
                    translitBuffer.append(aVowelToUseAfter(EWTSNotACIP, wylie1),
                                          fontSize);
                    if (debug) System.out.println("DEBUG: appending vowel 2");
                } else {
                    if (debug) System.out.println("DEBUG: already has vowel 2");
                }
                if ("root-suffix-postsuffix" == candidateType) {
                    leftover = 3;
                    String wylie2 = ((TGCPair)gcs.get(1)).getWylie();
                    String wylie3 = ((TGCPair)gcs.get(2)).getWylie();
                    String acip2 = (EWTSNotACIP) ? null : ((TGCPair)gcs.get(1)).getACIP();
                    String acip3 = (EWTSNotACIP) ? null : ((TGCPair)gcs.get(2)).getACIP();
                    translitBuffer.append(unambiguousPostAVowelTranslit(EWTSNotACIP,
                                                                        wylie2,
                                                                        wylie3,
                                                                        acip2,
                                                                        acip3),
                                          fontSize);
                }
            } else if ("prefix-root-suffix" == candidateType
                       || "prefix-root" == candidateType
                       || (appendaged
                           && "prefix/root-root/suffix" == candidateType)
                       || "prefix-root-suffix-postsuffix" == candidateType) {
                String wylie1 = ((TGCPair)gcs.get(0)).getWylie();
                String wylie2 = ((TGCPair)gcs.get(1)).getWylie();
                String acip1 = (EWTSNotACIP) ? null : ((TGCPair)gcs.get(0)).getACIP();
                String acip2 = (EWTSNotACIP) ? null : ((TGCPair)gcs.get(1)).getACIP();
                leftover = 2;
                if (TibetanMachineWeb.isAmbiguousWylie(wylie1, wylie2))
                    if (EWTSNotACIP)
                        translitBuffer.append(wylie1 + WYLIE_DISAMBIGUATING_KEY + wylie2,
                                              fontSize);
                    else
                        translitBuffer.append(acip1 + '-' + acip2,
                                              fontSize);
                else
                    if (EWTSNotACIP)
                        translitBuffer.append(wylie1 + wylie2, fontSize);
                    else
                        translitBuffer.append(acip1 + acip2, fontSize);

                if (((TGCPair)gcs.get(1)).classification
                    != TGCPair.CONSONANTAL_WITH_VOWEL) {
                    ThdlDebug.verify(TGCPair.CONSONANTAL_WITHOUT_VOWEL
                                     == ((TGCPair)gcs.get(1)).classification);
                    if (debug) System.out.println("DEBUG: appending vowel 1");
                    translitBuffer.append(aVowelToUseAfter(EWTSNotACIP, wylie2),
                                          fontSize);
                } else {
                    if (debug) System.out.println("DEBUG: already has vowel 1");
                }
                if ("prefix-root-suffix-postsuffix" == candidateType) {
                    leftover = 4;
                    String wylie3 = ((TGCPair)gcs.get(2)).getWylie();
                    String wylie4 = ((TGCPair)gcs.get(3)).getWylie();
                    String acip3 = (EWTSNotACIP) ? null : ((TGCPair)gcs.get(2)).getACIP();
                    String acip4 = (EWTSNotACIP) ? null : ((TGCPair)gcs.get(3)).getACIP();
                    translitBuffer.append(unambiguousPostAVowelTranslit(EWTSNotACIP,
                                                                        wylie3,
                                                                        wylie4,
                                                                        acip3,
                                                                        acip4),
                                          fontSize);
                }
            } else if ("number" == candidateType) {
                leftover = 0;
            } else {
                throw new Error("missed a case down here");
            }

            // append the wylie/ACIP left over:
            String lastPairTranslit = null;
            if (appendaged && leftover >= 1) {
                TGCPair tp = (TGCPair)gcs.get(leftover-1);
                lastPairTranslit = (EWTSNotACIP
                                    ? tp.getWylie(null)
                                    : tp.getACIP(null));
                if ((translitBuffer.length() == 0)
                    || !translitBuffer.get(translitBuffer.length() - 1).getTranslit().endsWith(lastPairTranslit)) {
                    int l;
                    if ((l = translitBuffer.length()) > 0) {
                        String s = translitBuffer.get(l - 1).getTranslit();
                        char lc = s.charAt(s.length() - 1);
                        ThdlDebug.verify(lc == ((EWTSNotACIP) ? 'a' : 'A') /* hard-coded ACIP and EWTS values */);
                        lastPairTranslit = lastPairTranslit + lc; /* 'da'i can cause this */
                    } else {
                        ThdlDebug.verify(false); // this better never happen.
                        lastPairTranslit = null;
                    }
                }
            }
            for (int i = leftover; i < sz; i++) {
                TGCPair tp = (TGCPair)gcs.get(i);
                String y;
                translitBuffer.append(EWTSNotACIP
                                      ? (y = tp.getWylie(lastPairTranslit))
                                      : (y = tp.getACIP(lastPairTranslit)),
                                      fontSize);
                if (appendaged)
                    lastPairTranslit = y;
            }
        }
    }

/**
* Gets the Extended Wylie/ACIP for a sequence of glyphs.  This works
* as follows:
*
* <p>We run along until we hit whitespace or punctuation.  We take
* everything before that and we see if it's a legal Tibetan tsheg bar,
* either a number or a word fragment.  If it is, we insert only one
* vowel in the correct place.  If not, then we throw a disambiguating
* key or a vowel after each stack.
*
* @param EWTSNotACIP true if you want THDL Extended Wylie, false if
* you want ACIP
* @param dcs an array of glyphs
* @param noSuch an array which will not be touched if this is
* successful; however, if there is no THDL Extended Wylie/ACIP
* corresponding to these glyphs, then noSuch[0] will be set to true
* @param warnings either null or a buffer to which will be appended
* warnings about illegal tsheg bars
* @return the Extended Wylie/ACIP corresponding to these glyphs (with
* font size info), or null */
    private static TranslitList getTranslitImplementation(boolean EWTSNotACIP,
                                                          SizedDuffCode[] dcs,
                                                          boolean noSuch[],
                                                          StringBuffer warnings) {
        // DLC FIXME: "    " should become " " for ACIP
        ArrayList glyphList = new ArrayList();
        TranslitList translitBuffer = new TranslitList();
        for (int i = 0; i < dcs.length; i++) {
            char ch = dcs[i].getDuffCode().getCharacter();
            int fsz = dcs[i].getFontSize();
            if ((int)ch < 32) { // 32 is space, ' '
                if (!glyphList.isEmpty()) {
                    getTshegBarTranslit(EWTSNotACIP, glyphList, noSuch,
                                        warnings, translitBuffer);
                    glyphList.clear();
                    if (null != warnings)
                        warnings.append("Some glyphs came right before a newline; they did not have a tsheg or shad come first.");
                }
                // In ACIP, \n\n (or \r\n\r\n with DOS line feeds)
                // indicates a real line break.
                if (!EWTSNotACIP && '\n' == ch) {
                    if (i > 0
                        && dcs[i - 1].getDuffCode().getCharacter() == '\r')
                        translitBuffer.append("\r\n", fsz);
                    else
                        translitBuffer.append(ch, fsz);
                }
                translitBuffer.append(ch, fsz);
            } else { // (int)ch >= 32
                String wylie
                    = TibetanMachineWeb.getWylieForGlyph(dcs[i].getDuffCode(),
                                                         noSuch);
                String acip = null;
                if (!EWTSNotACIP) {
                    // U+0F04 and U+0F05 -- these require lookahead to
                    // see if the ACIP is # (two shishes) or * (one
                    // swish)
                    int howManyConsumed[] = new int[] { -1 /* invalid */ };
                    acip = TibetanMachineWeb.getACIPForGlyph(dcs[i].getDuffCode(),
                                                             ((i+1<dcs.length)
                                                              ? dcs[i+1].getDuffCode()
                                                              : null),
                                                             ((i+2<dcs.length)
                                                              ? dcs[i+2].getDuffCode()
                                                              : null),
                                                             noSuch,
                                                             howManyConsumed);
                    ThdlDebug.verify(howManyConsumed[0] <= 3
                                     && howManyConsumed[0] >= 1);
                    i += howManyConsumed[0] - 1;
                }
                if (TibetanMachineWeb.isWyliePunc(wylie)
                    && !TibetanMachineWeb.isWylieAdornment(wylie)) {
                    if (!glyphList.isEmpty()) {
                        getTshegBarTranslit(EWTSNotACIP, glyphList, noSuch,
                                            warnings, translitBuffer);
                        glyphList.clear();
                    }
                    //append the punctuation:
                    translitBuffer.append(EWTSNotACIP ? wylie : acip, fsz);
                } else {
                    glyphList.add(dcs[i]);
                }
            }
        } // for

        // replace remaining TMW with transliteration
        if (!glyphList.isEmpty()) {
            getTshegBarTranslit(EWTSNotACIP, glyphList, noSuch,
                                warnings, translitBuffer);
            // glyphList.clear() if we weren't about to exit...
            if (null != warnings)
                warnings.append("The stretch of Tibetan ended without final punctuation.");
        }
        return ((translitBuffer.length() > 0) ? translitBuffer : null);
    }

    /** Returns "root" instead of "appendaged-root", for example. */
    private static final String getCandidateTypeModuloAppendage(String candidateType) {
        if (candidateType.startsWith("appendaged-")) {
            candidateType
                = candidateType.substring("appendaged-".length()).intern();
        }
        return candidateType;
    }

    /** Returns an array of size 2 that lists all the possible indices
     *  of the root stack given the chosen candidate type.  A negative
     *  number appears if there are not that many possible positions
     *  for the root.  (You'll get two negative numbers if there is no
     *  root stack.) */
    public static final int[] getIndicesOfRootForCandidateType(String candidateType) {
        // Appendaged vs. not appendaged?  it affects nothing.
        candidateType = getCandidateTypeModuloAppendage(candidateType);

        int[] rv = new int[] { -1, -1 };
        if (candidateType == "prefix/root"
            || candidateType.startsWith("root")) {
            rv[0] = 0;
        } else if (candidateType.startsWith("prefix/root-")) {
            rv[0] = 0;
            rv[1] = 1;
        } else if (candidateType.startsWith("prefix-root")) {
            rv[0] = 1;
        }
        return rv;
    }

    /** Returns true if and only if the stack with Wylie <i>root</i>
     *  can take the prefix <i>prefix</i>. */
    private static boolean isLegalPrefixRootCombo(String prefix, String root) {
        // This will be decomposed enough.  If you can decompose it,
        // then it doesn't take a prefix!
        if (!TibetanMachineWeb.isKnownHashKey(root)) {
            root = root.replace('+', '-');
            if (!TibetanMachineWeb.isKnownHashKey(root)) {
                // If the glyph isn't even in TibetanMachine, then
                // it's not able to take any prefix.
                return false;
            }
        }
        String ru = TibetanMachineWeb.getUnicodeForWylieForGlyph(root);

        // ru may be for (head, root, sub), (head, root), (root), or
        // (root, sub).  Try all possibilities that are possible with
        // a String of length ru.  If there's a wa-zur, then we say
        // (FIXME: do we say correctly?) that a stack with wa-zur can
        // take a prefix if and only if the stack without can take a
        // prefix.

        if (ru == null) throw new Error("how? root is " + root); // FIXME: make this an assertion
        int rl = ru.length();
        if (ru.charAt(rl - 1) == UnicodeConstants.EWSUB_wa_zur)
            --rl; // forget about wa-zur: see above.
        if (rl == 2) {
            char ch0 = ru.charAt(0);
            char ch1 = UnicodeUtils.getNominalRepresentationOfSubscribedConsonant(ru.charAt(1));

            // (head, root) and (root, sub) are possibilities.
            if (ACHUNG.equals(prefix)) {
                return LegalTshegBar.takesAchungPrefix(ch0, ch1, UnicodeConstants.EW_ABSENT)
                    || LegalTshegBar.takesAchungPrefix(UnicodeConstants.EW_ABSENT, ch0, ch1);
            } else if ("b".equals(prefix)) {
                return LegalTshegBar.takesBao(ch0, ch1, UnicodeConstants.EW_ABSENT)
                    || LegalTshegBar.takesBao(UnicodeConstants.EW_ABSENT, ch0, ch1);
            } else if ("m".equals(prefix)) {
                return LegalTshegBar.takesMao(ch0, ch1, UnicodeConstants.EW_ABSENT)
                    || LegalTshegBar.takesMao(UnicodeConstants.EW_ABSENT, ch0, ch1);
            } else if ("g".equals(prefix)) {
                return LegalTshegBar.takesGao(ch0, ch1, UnicodeConstants.EW_ABSENT)
                    || LegalTshegBar.takesGao(UnicodeConstants.EW_ABSENT, ch0, ch1);
            } else if ("d".equals(prefix)) {
                return LegalTshegBar.takesDao(ch0, ch1, UnicodeConstants.EW_ABSENT)
                    || LegalTshegBar.takesDao(UnicodeConstants.EW_ABSENT, ch0, ch1);
            } else {
                throw new IllegalArgumentException("prefix is " + prefix);
            }
        } else if (rl == 1) {
            char ch0 = ru.charAt(0);
            // (root) is the only choice.
            if (ACHUNG.equals(prefix)) {
                return LegalTshegBar.takesAchungPrefix(UnicodeConstants.EW_ABSENT, ch0, UnicodeConstants.EW_ABSENT);
            } else if ("b".equals(prefix)) {
                return LegalTshegBar.takesBao(UnicodeConstants.EW_ABSENT, ch0, UnicodeConstants.EW_ABSENT);
            } else if ("m".equals(prefix)) {
                return LegalTshegBar.takesMao(UnicodeConstants.EW_ABSENT, ch0, UnicodeConstants.EW_ABSENT);
            } else if ("g".equals(prefix)) {
                return LegalTshegBar.takesGao(UnicodeConstants.EW_ABSENT, ch0, UnicodeConstants.EW_ABSENT);
            } else if ("d".equals(prefix)) {
                return LegalTshegBar.takesDao(UnicodeConstants.EW_ABSENT, ch0, UnicodeConstants.EW_ABSENT);
            } else {
                throw new IllegalArgumentException("prefix is " + prefix);
            }
        } else if (rl == 3) {
            char ch0 = ru.charAt(0);
            char ch1 = UnicodeUtils.getNominalRepresentationOfSubscribedConsonant(ru.charAt(1));
            char ch2 = UnicodeUtils.getNominalRepresentationOfSubscribedConsonant(ru.charAt(2));
            // (head, root, sub) is the only choice.
            if (ACHUNG.equals(prefix)) {
                return LegalTshegBar.takesAchungPrefix(ch0, ch1, ch2);
            } else if ("b".equals(prefix)) {
                return LegalTshegBar.takesBao(ch0, ch1, ch2);
            } else if ("m".equals(prefix)) {
                return LegalTshegBar.takesMao(ch0, ch1, ch2);
            } else if ("g".equals(prefix)) {
                return LegalTshegBar.takesGao(ch0, ch1, ch2);
            } else if ("d".equals(prefix)) {
                return LegalTshegBar.takesDao(ch0, ch1, ch2);
            } else {
                throw new IllegalArgumentException("prefix is " + prefix);
            }
        } else {
            return false;
        }
    }
}
