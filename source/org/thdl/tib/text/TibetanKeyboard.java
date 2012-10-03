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
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.thdl.util.Trie;

/**
* An alternate (non-Extended Wylie) keyboard input
* method. A keyboard URL is passed to its constructor. This URL
* must follow a particular format, and include particular subparts.
* For example, the keyboard URL must specify values for various
* input parameters, as well as correspondences for the Wylie
* characters this keyboard allows the user to input. For an example,
* see the file 'sambhota_keyboard.ini' found in the same
* directory as this class.
* <p>
* It is normative practice for a null keyboard to be
* interpreted as the default Wylie keyboard.
* A non-null keyboard defines a transformation of the default
* Wylie keyboard, setting new values for each Wylie value, as
* well as (re-)defining various parameters.
* 
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0
*/
public class TibetanKeyboard {

    /** This addresses bug 624133, "Input freezes after impossible
        character".  We store all valid input sequences here. */
    private Trie validInputSequences;

	private boolean hasDisambiguatingKey;
	private char disambiguatingKey;
	private boolean hasSanskritStackingKey;
	private boolean hasTibetanStackingKey;
	private boolean isStackingMedial;
	private char stackingKey;
	private boolean isAChenRequiredBeforeVowel;
	private boolean isAChungConsonant;
	private boolean hasAVowel;
	private Map charMap;
	private Map vowelMap;
	private Map puncMap;
	private int command;
	private final int NO_COMMAND = 0;
	private final int PARAMETERS = 1;
	private final int CHARACTERS = 2;
	private final int VOWELS = 3;
	private final int PUNCTUATION = 4;

/**
* A generic Exception to indicate an invalid keyboard.
*/
	public class InvalidKeyboardException extends Exception {
	}

    /** Do not call this. */
	private TibetanKeyboard() { }

/**
* Opens the URL specified by the parameter,
* and tries to construct a keyboard from it. If the file is
* missing, or is invalid, an InvalidKeyboardException is
* thrown.
*
* @param url the URL of the keyboard
* @throws InvalidKeyboardException a valid keyboard cannot be
* constructed from this URL
*/
	public TibetanKeyboard(URL url) throws InvalidKeyboardException {
		try {
			InputStreamReader isr = new InputStreamReader(url.openStream());
			BufferedReader in = new BufferedReader(isr);

			System.out.println("Reading Tibetan Keyboard " + url.toString());
			String line;

			charMap = new HashMap();
			vowelMap = new HashMap();
			puncMap = new HashMap();
			validInputSequences = new Trie();

			command = NO_COMMAND;

			boolean bool;

			while ((line = in.readLine()) != null) {

				if (line.startsWith("<?")) { //line is command
					if (line.equalsIgnoreCase("<?parameters?>"))
						command = PARAMETERS;

					else if (line.equalsIgnoreCase("<?characters?>"))
						command = CHARACTERS;

					else if (line.equalsIgnoreCase("<?vowels?>"))
						command = VOWELS;

					else if (line.equalsIgnoreCase("<?punctuation?>"))
						command = PUNCTUATION;
				}
				else if (line.equals("")) //empty string
					;

				else {
					StringTokenizer st = new StringTokenizer(line,"=");
					String left = null, right = null;

					if (st.hasMoreTokens())
						left = st.nextToken();

					if (st.hasMoreTokens())
						right = st.nextToken();

					switch (command) {
						case NO_COMMAND:
								break;

						case PARAMETERS:
								if (left == null)
									throw new InvalidKeyboardException();

								if (right == null)
									break;

								if (left.equals("stack key")) {
									stackingKey = right.charAt(0);
									break;
								}

								if (left.equals("disambiguating key")) {
									disambiguatingKey = right.charAt(0);
									break;
								}

								bool = new Boolean(right).booleanValue();

								if (left.equalsIgnoreCase("has sanskrit stacking"))
									hasSanskritStackingKey = bool;

								if (left.equalsIgnoreCase("has tibetan stacking"))
									hasTibetanStackingKey = bool;

								if (left.equalsIgnoreCase("is stacking medial"))
									isStackingMedial = bool;

								if (left.equalsIgnoreCase("has disambiguating key"))
									hasDisambiguatingKey = bool;

								if (left.equalsIgnoreCase("needs achen before vowels"))
									isAChenRequiredBeforeVowel = bool;

								if (left.equalsIgnoreCase("has 'a' vowel"))
									hasAVowel = bool;

								if (left.equalsIgnoreCase("is achung consonant"))
									isAChungConsonant = bool;

								break;

						case CHARACTERS:
								if (left == null)
									throw new InvalidKeyboardException();

								if (right == null)
									break;

								charMap.put(right, left);
								validInputSequences.put(right, left);
								break;

						case VOWELS:
								if (left == null)
									throw new InvalidKeyboardException();

								if (right == null)
									break;

								vowelMap.put(right, left);
								validInputSequences.put(right, left);
								break;

						case PUNCTUATION:
								if (left == null)
									throw new InvalidKeyboardException();

								if (right == null)
									break;

								puncMap.put(right, left);
								validInputSequences.put(right, left);
								break;
					}
				}
			}
		}
		catch (Exception e) {
			throw new InvalidKeyboardException();
		}
	}

/**
* Does this keyboard have a disambiguating key?
* @return true if this keyboard has a disambiguating key, e.g.
* the period in Wylie 'g.y' vs. Wylie 'gy', false otherwise
*/
	public boolean hasDisambiguatingKey() {
		return hasDisambiguatingKey;
	}

/**
* Gets the disambiguating key for this keyboard.
* @return the disambiguating key, assuming this keyboard has one
*/
	public char getDisambiguatingKey() {
		return disambiguatingKey;
	}

/**
* Does this keyboard require a stacking key for Sanskrit stacks?
* @return true if this keyboard requires a
* stacking key for Sanskrit stacks
*/
	public boolean hasSanskritStackingKey() {
		return hasSanskritStackingKey;
	}

/**
* Does this keyboard require a stacking key for Tibetan stacks?
* @return true if this keyboard requires a
* stacking key for Tibetan stacks
*/
	public boolean hasTibetanStackingKey() {
		return hasTibetanStackingKey;
	}

/**
* Is stacking medial?
* @return true if this keyboard has stacking, and
* if that stacking is medial rather than pre/post.
* In other words, if you want a stack consisting
* of the (Wylie) characters 's', 'g', and 'r', and
* if the stack key is '+', then if you get the 
* stack by typing 's+g+r', then this method returns
* true. If you get it by typing '+sgr' or '+sgr+',
* then the method returns false.
*/
	public boolean isStackingMedial() {
		return isStackingMedial;
	}

/**
* Gets the stacking key.
* @return the stacking key, if there is one
*/
	public char getStackingKey() {
		return stackingKey;
	}

/**
* Must achen be typed first if you want achen plus a vowel?
* @return true if it is necessary in this keyboard to
* type achen plus a vowel to get achen plus a vowel,
* or if you can (as in Wylie), simply type the vowel,
* and then automatically get achen plus the vowel,
* assuming there is no preceding consonant.
*/
	public boolean isAChenRequiredBeforeVowel() {
		return isAChenRequiredBeforeVowel;
	}

/**
* Is achung treated as an ordinary consonant?
* @return true if achung is counted as a consonant,
* and thus treated as stackable like any other
* consonant; false if achung is treated as a vowel,
* as in Wylie.
*/
	public boolean isAChungConsonant() {
		return isAChungConsonant;
	}

/**
* Does the keyboard have a key for the invisible 'a' vowel?
* @return true if this keyboard has a keystroke
* sequence for the invisible Wylie vowel 'a', false
* if there is no way to type this invisible vowel.
*/
	public boolean hasAVowel() {
		return hasAVowel;
	}

/**
* Decides whether or not a string is a character (as opposed to a
* vowel or punctuation) in this keyboard.
* @return true if the parameter is a character
* in this keyboard. This method checks to see
* if the passed string has been mapped to a
* Wylie character - if not, then it returns false.
*
* @param s the possible character */
	public boolean isChar(String s) {
		if (charMap.containsKey(s))
			return true;
		else
			return false;
	}
/**
* Gets the Extended Wylie corresponding to this character.
* @return the Wylie value corresponding to this
* parameter, assuming it is in fact a character
* in this keyboard; if not, returns null.
*
* @param s the possible character
*/
	public String getWylieForChar(String s) {
		if (!charMap.containsKey(s))
			return null;

		return (String)charMap.get(s);
	}

/**
* Decides whether or not a string is a punctuation mark in this keyboard?
* @return true if the parameter is punctuation
* in this keyboard. This method checks to see if the
* passed string has been mapped to Wylie punctuation -
* if not, then it returns false.
*
* @param s the possible punctuation
*/
	public boolean isPunc(String s) {
		if (puncMap.containsKey(s))
			return true;
		else
			return false;
	}

/**
* Gets the Extended Wylie corresponding to this punctuation.
* @return the Wylie value corresponding to this
* parameter, assuming it is in fact punctuation
* in this keyboard; if not, returns null.
*
* @param s the possible punctuation
*/
	public String getWylieForPunc(String s) {
		if (!puncMap.containsKey(s))
			return null;

		return (String)puncMap.get(s);
	}

/**
* Decides whether or not the string is a vowel in this keyboard. 
* @return true if the parameter is a vowel
* in this keyboard. This method checks to see if the
* passed string has been mapped to a Wylie vowel -
* if not, then it returns false.
*
* @param s the possible vowel
*/
	public boolean isVowel(String s) {
		return vowelMap.containsKey(s);
	}

/**
* Gets the Extended Wylie corresponding to this vowel.
* @return the Wylie value corresponding to this
* parameter, assuming it is in fact a vowel
* in this keyboard; if not, returns null.
*
* @param s the possible vowel
*/
	public String getWylieForVowel(String s) {
		if (!vowelMap.containsKey(s))
			return null;

		return (String)vowelMap.get(s);
	}

    /** This addresses bug 624133, "Input freezes after impossible
     *  character".  Returns true iff s is a proper prefix of some
     *  legal input for this keyboard.  In the extended Wylie
     *  keyboard, hasInputPrefix("S") is true because "Sh" is legal
     *  input.  hasInputPrefix("Sh") is false because though "Sh" is
     *  legal input, ("Sh" + y) is not valid input for any non-empty
     *  String y. */
    public boolean hasInputPrefix(String s) {
        return validInputSequences.hasPrefix(s);
    }
}
