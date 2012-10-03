/*
 * The contents of this file are subject to the THDL Open Community License
 * Version 1.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License on the THDL web site
 * (http://www.thdl.org/).
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific terms governing rights and limitations under the License.
 * 
 * The Initial Developer of this software is the Tibetan and Himalayan Digital
 * Library (THDL).
 * 
 * Copyright 2001 Tashi Tsering All Rights Reserved
 * 
 * Contributor(s): Andres Montano. 
 */

package org.thdl.tib.text;

/** This is the java version of the class of TibetanSyllable represented by
* Wylie translateration system. Use the class, Tibetan syllables can be
* compared. And also one can sort Tibetan syllables, words, phrases and
* sentences by comparison of two syllables. The order of the consonants and
* the order of the vowels are based on "tshig mdzod chen mo" (The Big
* Dictionary).

* The interface of the class of TibetanSyllable:
* 
* class name: TibetanSyllable 
* constructor: TibetanSyllable ( String S )
* String S is the representation of a Tibetan syllable by string.
* 
* method: 
* int compareTo(Object thatSyllable)
* return: 0 if this syllable is the same with thatSyllable; 
* 1 if this syllable is bigger than thatSyllable,
* i.e. this syllable goes after thatSyllable in the order of a
* dictionary;
* -1 if this syllable is less than thatSyllable,
* i.e. this syllable goes before thatSyllable in the order of a dictionary;
* Those invalid syllables will be treated as the biggest syllable, that
* they are always bigger than valid syllables.
*/
public class TibetanSyllable implements Comparable
{

	/**	 The character String of the syllable. */
	private String theSyllable;

	/** True for Tibetan syllable, false for Sanskrit syllable. */
	private boolean TibetanSyllableFlag;

	/** Number of Tibetan characters represented by Wylie system
	 *  in the syllable. */
	private int nComponents; 

	/** The number of vowels in the syllable. */
	private int nVowels;

	/** Components of a syllable consists of Tibetan Wylie "characters". */
	private String[][] Components; 

	// Components[0][0]-----Base letter
	// Components[0][1]-----Superscript
	// Components[0][2]-----Prefix
	// Components[0][3]-----Subscript
	// Components[0][4]-----vowel
	// Components[0][5]-----Suffix
	// Components[0][6]-----Second suffix
	//You can add your own method to return different component of a syllable.

	/** The constructor */
	public TibetanSyllable(String s) {
		//Filter the spaces that are at the beginning or end of the syllable.
		/*
		 * while (s.length()>0) if(s.charAt(0) == ' ' && s.length()>1) s =
		 * s.substring(1,s.length()); else if(s.charAt(s.length()-1) == ' ' &&
		 * s.length()>1) s = s.substring(0, s.length()-1); else break;
		 */
		theSyllable = s.trim();
		ItsComponents();
	}

	public int compareTo(Object o)
	{
		TibetanSyllable s = (TibetanSyllable) o;
		int getnV = s.GetnVowels(), n = (nVowels > getnV) ? nVowels : getnV, valueOfThis, valueOfThat;

		String[][] temp;

		temp = s.GetComponents();

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 10; j++) {
				valueOfThis = ValueOfTibetanCharacter(Components[i][j]);
				valueOfThat = ValueOfTibetanCharacter(temp[i][j]);
				if (valueOfThis > valueOfThat)
					return 1; // This syllable is bigger than that syllable s.

				else if (valueOfThis < valueOfThat)
					return -1; // This syllable is smaller than that syllable s.
			}
		}

		return 0; // They are the same syllable.
	}

	/** Return the base letter of a syllable. */
	public String BaseLetter() {
		return Components[0][0];
	}

	public boolean IsTibetanSyllable() {
		return TibetanSyllableFlag;
	}

	public String GetTheSyllable() {
		return theSyllable;
	}
	
	public String toString()
	{
		return theSyllable;
	}

	public void SetTheSyllable(String s) {
		theSyllable = s;
	}

	private String[][] GetComponents() {
		return Components;
	}

	private int GetnComponents() {
		return nComponents;
	}

	private int ItsLength() {
		return nComponents;
	}

	private int GetnVowels() {
		return nVowels;
	}

	/** To examine a component to see if it is a vowel. Return true if the
	  * component is vowel.*/
	public static boolean IsTibetanVowel(String thecomponent) {

		if ((thecomponent.equals("a")) || (thecomponent.equals("i"))
				|| (thecomponent.equals("u")) || (thecomponent.equals("e"))
				|| (thecomponent.equals("o")))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a prefix. Return true if the
	 * component is prefix. */
	public static boolean IsPrefix(String thecomponent) {

		if ((thecomponent.equals("g")) || (thecomponent.equals("d"))
				|| (thecomponent.equals("b")) || (thecomponent.equals("m"))
				|| (thecomponent.equals("'")))
			return true;
		else
			return false;

	}

	/** To examine a component, see if it is a base letter.
	 *  Return true if it is.
	 */
	public static boolean IsBaseLetter(String thecomponent) {

		if ((thecomponent.equals("k")) || (thecomponent.equals("kh"))
				|| (thecomponent.equals("g")) || (thecomponent.equals("ng"))
				|| (thecomponent.equals("c")) || (thecomponent.equals("ch"))
				|| (thecomponent.equals("j")) || (thecomponent.equals("ny"))
				|| (thecomponent.equals("t")) || (thecomponent.equals("th"))
				|| (thecomponent.equals("d")) || (thecomponent.equals("n"))
				|| (thecomponent.equals("p")) || (thecomponent.equals("ph"))
				|| (thecomponent.equals("b")) || (thecomponent.equals("m"))
				|| (thecomponent.equals("ts")) || (thecomponent.equals("tsh"))
				|| (thecomponent.equals("dz")) || (thecomponent.equals("w"))
				|| (thecomponent.equals("zh")) || (thecomponent.equals("z"))
				|| (thecomponent.equals("'")) || (thecomponent.equals("y"))
				|| (thecomponent.equals(".y")) || //Special for making "g.ya"
												  // different from gya.
				(thecomponent.equals("r")) || (thecomponent.equals("l"))
				|| (thecomponent.equals("sh")) || (thecomponent.equals("s"))
				|| (thecomponent.equals("h")) || (thecomponent.equals("a")))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a supperscript.
	 * Return true if it is, otherwise false.*/
	public static boolean IsSuperscript(String thecomponent) {

		if ((thecomponent.equals("r")) || (thecomponent.equals("l"))
				|| (thecomponent.equals("s")))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a subscript.
	 * Return true if it is, otherwise false. */
	public static boolean IsSubscript(String thecomponent) {

		if ((thecomponent.equals("w")) || (thecomponent.equals("y"))
				|| (thecomponent.equals("r")) || (thecomponent.equals("l")))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a suffix.
	 * Return true if it is, otherwise false. */
	public static boolean IsSuffix(String thecomponent) {

		if ((thecomponent.equals("g")) || (thecomponent.equals("ng"))
				|| (thecomponent.equals("d")) || (thecomponent.equals("n"))
				|| (thecomponent.equals("b")) || (thecomponent.equals("m"))
				|| (thecomponent.equals("'")) || (thecomponent.equals("r"))
				|| (thecomponent.equals("l")) || (thecomponent.equals("s")))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a the second suffix. 
	 * Return true if it is, otherwise false. */
	public static boolean IsSecondSuffix(String thecomponent) {
		if (thecomponent.equals("s"))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a Sanskrit consonant.
	 * Return true if it is, otherwise false. */
	public static boolean IsSanskritConsonant(String thecomponent) {

		if ((thecomponent.equals("T")) || (thecomponent.equals("Th"))
				|| (thecomponent.equals("D")) || (thecomponent.equals("N"))
				|| (thecomponent.equals("Sh")) || (thecomponent.equals("M"))
				|| (thecomponent.equals("`")) || (thecomponent.equals("f"))
				|| (thecomponent.equals("v")))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a Sanskrit vowel.
	 * Return true if it is, otherwise false.
	 */
	public static boolean IsSanskritVowel(String thecomponent) {

		if ((thecomponent.equals("A")) || (thecomponent.equals("I"))
				|| (thecomponent.equals("U")) || (thecomponent.equals("-i"))
				|| (thecomponent.equals("-I")) || (thecomponent.equals("ai"))
				|| (thecomponent.equals("au")))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a Sanskrit symbole.
	 * Return true if it is, otherwise false.
	 */
	public static boolean IsSanskritSpecialSymbol(String thecomponent) {
		if ((thecomponent.equals("+")))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a Tibetan symbol.
	 * Return true if it is, otherwise false.
	 */
	public static boolean IsThisTibetanSymbol(String thecomponent) {
		if (IsTibetanVowel(thecomponent) || IsBaseLetter(thecomponent))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a Sanskrit symbol.
	 * Return true if it is, otherwise false.
	 */
	public static boolean IsThisSanskritSymbol(String thecomponent) {
		if (IsSanskritVowel(thecomponent) || IsSanskritConsonant(thecomponent)
				|| IsSanskritSpecialSymbol(thecomponent))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a Tibetan symbol.
	 * Return true if it is, otherwise false.
	 */
	public static boolean IsThisTibetanOrSanskritSymbol(String thecomponent) {
		if (IsThisTibetanSymbol(thecomponent)
				|| IsThisSanskritSymbol(thecomponent))
			return true;
		else
			return false;
	}

	/** To examine a component, see if it is a Tibetan or Sanskrit symbol.
	 * Return true if it is, otherwise false.
	 * @return
	 */
	public static boolean IsThisTibetanOrSanskritVowel(String thecomponent) {
		if (IsSanskritVowel(thecomponent) || IsTibetanVowel(thecomponent))
			return true;
		else
			return false;
	}

	/** To examine a pair of components, see if one of them is a prefix
	 * and the other one is a base letter that can follow the prefix.
	 * Return true if it is, otherwise false.
	 */
	public static boolean PrefixBaseletterMatch(String prefix, String baseletter) {
		char c;
		if (prefix.length() != 1)
			return false; //No prefix.
		else
			c = prefix.charAt(0);

		switch (c) {
		case 'g':
			if ((baseletter.equals("c")) || (baseletter.equals("ny"))
					|| (baseletter.equals("t")) || (baseletter.equals("d"))
					|| (baseletter.equals("n")) || (baseletter.equals("ts"))
					|| (baseletter.equals("zh")) || (baseletter.equals("z"))
					|| (baseletter.equals("sh")) || (baseletter.equals("s"))
					|| (baseletter.equals(".y")))
				return true;
			else
				return false;

		case 'd':
			if ((baseletter.equals("k")) || (baseletter.equals("p"))
					|| (baseletter.equals("g")) || (baseletter.equals("b"))
					|| (baseletter.equals("ng")) || (baseletter.equals("m")))
				return true;
			else
				return false;

		case 'b':
			if ((baseletter.equals("c")) || (baseletter.equals("g"))
					|| (baseletter.equals("t")) || (baseletter.equals("d"))
					|| (baseletter.equals("ts")) || (baseletter.equals("zh"))
					|| (baseletter.equals("z")) || (baseletter.equals("sh"))
					|| (baseletter.equals("s")) || (baseletter.equals("k")))
				return true;
			else
				return false;

		case 'm':
			if ((baseletter.equals("kh")) || (baseletter.equals("ch"))
					|| (baseletter.equals("th")) || (baseletter.equals("tsh"))
					|| (baseletter.equals("g")) || (baseletter.equals("j"))
					|| (baseletter.equals("d")) || (baseletter.equals("dz"))
					|| (baseletter.equals("ng")) || (baseletter.equals("ny"))
					|| (baseletter.equals("n")))
				return true;
			else
				return false;

		case '\'':
			if ((baseletter.equals("kh")) || (baseletter.equals("ch"))
					|| (baseletter.equals("th")) || (baseletter.equals("ph"))
					|| (baseletter.equals("tsh")) || (baseletter.equals("g"))
					|| (baseletter.equals("j")) || (baseletter.equals("d"))
					|| (baseletter.equals("b")) || (baseletter.equals("dz")))
				return true;
			else
				return false;

		}
		return false;
	}

	/** To examine a pair of components, see if one of them is a subscript
	 * and the other one is a base letter that can be followed by the subscript.
	 * Return true if it is, otherwise false.
	 */
	public static boolean BaseletterSubscriptMatch(String baseletter, String subscript) {
		char c;
		if (subscript.length() != 1)
			return false; //No subscript.
		else
			c = subscript.charAt(0);

		switch (c) {
		case 'y':
			if ((baseletter.equals("k")) || (baseletter.equals("kh"))
					|| (baseletter.equals("g")) || (baseletter.equals("p"))
					|| (baseletter.equals("ph")) || (baseletter.equals("b"))
					|| (baseletter.equals("m")))
				return true;
			else
				return false;

		case 'r':
			if ((baseletter.equals("k")) || (baseletter.equals("t"))
					|| (baseletter.equals("p")) || (baseletter.equals("kh"))
					|| (baseletter.equals("ph")) || (baseletter.equals("g"))
					|| (baseletter.equals("d")) || (baseletter.equals("b"))
					|| (baseletter.equals("h")) || (baseletter.equals("m"))
					|| (baseletter.equals("s")))
				return true;
			else
				return false;

		case 'l':
			if ((baseletter.equals("k")) || (baseletter.equals("g"))
					|| (baseletter.equals("b")) || (baseletter.equals("r"))
					|| (baseletter.equals("s")) || (baseletter.equals("z")))
				return true;
			else
				return false;

		case 'w':
			if ((baseletter.equals("k")) || (baseletter.equals("kh"))
					|| (baseletter.equals("g")) || (baseletter.equals("ny"))
					|| (baseletter.equals("d")) || (baseletter.equals("ch"))
					|| (baseletter.equals("zh")) || (baseletter.equals("z"))
					|| (baseletter.equals("r")) || (baseletter.equals("l"))
					|| (baseletter.equals("sh")) || (baseletter.equals("s"))
					|| (baseletter.equals("h")))
				return true;
			else
				return false;

		}
		return false;
	}

	/** To examine a pair of components, see if one of them is a superscript 
	 *  and the other one is a base letter that can follow the superscript.
	 * Return true if it is, otherwise false.
	 */
	public static boolean SuperscriptBaseletterMatch(String superscript, String baseletter) {
		char c;
		if (superscript.length() != 1)
			return false; //No superscript.
		else
			c = superscript.charAt(0);

		switch (c) {
		case 'r':
			if ((baseletter.equals("k")) || (baseletter.equals("t"))
					|| (baseletter.equals("ts")) || (baseletter.equals("g"))
					|| (baseletter.equals("j")) || (baseletter.equals("d"))
					|| (baseletter.equals("b")) || (baseletter.equals("dz"))
					|| (baseletter.equals("ng")) || (baseletter.equals("ny"))
					|| (baseletter.equals("n")) || (baseletter.equals("m")))
				return true;
			else
				return false;

		case 'l':
			if ((baseletter.equals("k")) || (baseletter.equals("c"))
					|| (baseletter.equals("t")) || (baseletter.equals("p"))
					|| (baseletter.equals("g")) || (baseletter.equals("j"))
					|| (baseletter.equals("d")) || (baseletter.equals("b"))
					|| (baseletter.equals("ng")) || (baseletter.equals("h")))
				return true;
			else
				return false;

		case 's':
			if ((baseletter.equals("k")) || (baseletter.equals("t"))
					|| (baseletter.equals("p")) || (baseletter.equals("ts"))
					|| (baseletter.equals("g")) || (baseletter.equals("d"))
					|| (baseletter.equals("b")) || (baseletter.equals("ng"))
					|| (baseletter.equals("ny")) || (baseletter.equals("n"))
					|| (baseletter.equals("m")))
				return true;
			else
				return false;

		}
		return false;
	}

	/** Assign values for Tibetan Wylie characters for comparison.
	 */
	private int ValueOfTibetanCharacter(String theCharacter) {

		if (theCharacter == null)
			return 0;
		if (theCharacter.equals("$"))
			return 0; // For non-presence.

		if (theCharacter.equals("k"))
			return 1;
		if (theCharacter.equals("kh"))
			return 2;
		if (theCharacter.equals("g"))
			return 3;
		if (theCharacter.equals("ng"))
			return 4;
		if (theCharacter.equals("c"))
			return 5;
		if (theCharacter.equals("ch"))
			return 6;
		if (theCharacter.equals("j"))
			return 7;
		if (theCharacter.equals("ny"))
			return 8;

		if (theCharacter.equals("T"))
			return 9;
		if (theCharacter.equals("Th"))
			return 10;
		if (theCharacter.equals("D"))
			return 11;
		if (theCharacter.equals("N"))
			return 12;

		if (theCharacter.equals("t"))
			return 13;
		if (theCharacter.equals("th"))
			return 14;
		if (theCharacter.equals("d"))
			return 15;
		if (theCharacter.equals("n"))
			return 16;
		if (theCharacter.equals("p"))
			return 17;
		if (theCharacter.equals("ph"))
			return 18;
		if (theCharacter.equals("b"))
			return 19;
		if (theCharacter.equals("m"))
			return 20;
		if (theCharacter.equals("ts"))
			return 21;
		if (theCharacter.equals("tsh"))
			return 22;
		if (theCharacter.equals("dz"))
			return 23;
		if (theCharacter.equals("w"))
			return 24;
		if (theCharacter.equals("zh"))
			return 25;
		if (theCharacter.equals("z"))
			return 26;
		if (theCharacter.equals("'"))
			return 27;
		if (theCharacter.equals("y"))
			return 28;
		if (theCharacter.equals(".y"))
			return 28;
		if (theCharacter.equals("r"))
			return 29;
		if (theCharacter.equals("l"))
			return 30;
		if (theCharacter.equals("sh"))
			return 31;
		if (theCharacter.equals("Sh"))
			return 32;
		if (theCharacter.equals("s"))
			return 33;
		if (theCharacter.equals("h"))
			return 34;
		if (theCharacter.equals("a"))
			return 35;

		//	if(theCharacter.equals("a")) return 41;
		if (theCharacter.equals("A"))
			return 42;
		if (theCharacter.equals("i"))
			return 43;
		if (theCharacter.equals("I"))
			return 44;
		if (theCharacter.equals("u"))
			return 47;
		if (theCharacter.equals("U"))
			return 48;
		if (theCharacter.equals("-i"))
			return 45;
		if (theCharacter.equals("-I"))
			return 46;
		if (theCharacter.equals("e"))
			return 49;
		if (theCharacter.equals("ai"))
			return 50;
		if (theCharacter.equals("o"))
			return 51;
		if (theCharacter.equals("au"))
			return 52;

		if (theCharacter.equals("invalid"))
			return 100;

		return 100;
	}

	/** This is the key function in the class, which
	 * extracts the components of a syllable from the
	 * Wylie string of the syllable and put them into
	 * the order in that we compare syllables each other.
	 */
	private void ItsComponents() {

		String thisString;
		String SyllableByComponents[] = new String[100]; // Syllable consist of
														 // and ordered by
														 // components
														 // represented
		// by Tibetan Wylie characters. Assume there are no more than
		Components = new String[10][20]; // 20 components in a syllable.

		int s = 0;
		nComponents = 0; // Number of Tibetan characters represented by Wylie
						 // system in the syllable.
		int i = 0;

		//Cut the String of the syllable into the consequence of Tibetan Wylie
		// characters of the syllable.
		while (i < theSyllable.length()) {
			for (int j = 1; theSyllable.length() >= (i + j); j++) {
				thisString = theSyllable.substring(i, i + j);
				if (IsThisTibetanOrSanskritSymbol(thisString)) {
					s = j;
					continue;
				}
				if (theSyllable.length() > (i + j) && j < 3)
					continue;
				if (s != 0)
					break;
				else {
					InValidSyllable();
					return;
				}
			}
			if (s == 0) {
				InValidSyllable();
				return;
			}

			if (!theSyllable.substring(i, i + s).equals("+"))
				SyllableByComponents[nComponents++] = theSyllable.substring(i, i + s); 
				
				/*{
				// InValidSyllable();
				s = 0;
				continue;
			} //Take off the Sanskrit stacking symbol "+" from the String.*/

			i = i + s;
			s = 0;
		}

		int nVowel = 0; // Number of vowels in a syllable.

		int nCBV[] = new int[6]; // Number of components before a vowel, assume
								 // there are 5 vowels in the syllable.
		// Normallly, there is only one vowel, sometimes two vowels in a
		// syllable.
		int nCAV[] = new int[6]; // Number of components after vowel, assume
								 // there are 5 vowels in the syllable.
		// Normallly, there is only one vowel, sometimes two vewls in a
		// syllable.
		boolean SanskritFlag = false; // Is the syllable Sanskrit?

		TibetanSyllableFlag = true;

		//Calculate nVowel, nCBV and nCAV.
		for (i = 0; i < nComponents; i++) {
			if (IsTibetanVowel(SyllableByComponents[i])) {
				nVowel++;
			} else if (IsSanskritVowel(SyllableByComponents[i])) {
				SanskritFlag = true;
				TibetanSyllableFlag = false;
				nVowel++;
			} else {
				nCBV[nVowel + 1]++;
				nCAV[nVowel]++;

				if (IsThisSanskritSymbol(SyllableByComponents[i])) {
					SanskritFlag = true;
					TibetanSyllableFlag = false;
				}
			}
		}

		if (nVowel == 0) {
			InValidSyllable();
			return;
		}

		nVowels = nVowel;

		for (i = 0; i < 10; i++)
			for (int j = 0; j < 20; j++)
				Components[i][j] = "$"; //Assume there are at most 20
										// components before a vowel.

		if (!SanskritFlag && nVowel < 3) { //For Tibetan syllable (Tibetan
										   // syllable has no more than 2
										   // vowels):

			if (nVowel == 1) {

				switch (nCBV[1]) {

				case 0: //Special case for "a", the last letter in Tibetan
						// letter list, and the sequences led by its "i",
					//"o", "u" and "e".
					Components[0][0] = "a";
					Components[0][1] = "$";
					Components[0][2] = "$";
					Components[0][3] = "$";
					Components[0][4] = SyllableByComponents[0];
					Components[0][5] = SyllableByComponents[1];
					Components[0][6] = SyllableByComponents[2];
					break;

				case 1:
					Components[0][0] = SyllableByComponents[0];
					Components[0][1] = "$";
					Components[0][2] = "$";
					Components[0][3] = "$";
					Components[0][4] = SyllableByComponents[1];
					Components[0][5] = SyllableByComponents[2];
					Components[0][6] = SyllableByComponents[3];
					break;

				case 2:

					if (PrefixBaseletterMatch(SyllableByComponents[0],
							SyllableByComponents[1])) {

						Components[0][0] = SyllableByComponents[1];
						Components[0][1] = "$";
						Components[0][2] = SyllableByComponents[0];
						Components[0][3] = "$";
						Components[0][4] = SyllableByComponents[2];
						Components[0][5] = SyllableByComponents[3];
						Components[0][6] = SyllableByComponents[4];
					}

					else if (BaseletterSubscriptMatch(SyllableByComponents[0],
							SyllableByComponents[1])) {

						Components[0][0] = SyllableByComponents[0];
						Components[0][1] = "$";
						Components[0][2] = "$";
						Components[0][3] = SyllableByComponents[1];
						Components[0][4] = SyllableByComponents[2];
						Components[0][5] = SyllableByComponents[3];
						Components[0][6] = SyllableByComponents[4];
					}

					else if (SuperscriptBaseletterMatch(
							SyllableByComponents[0], SyllableByComponents[1])) {

						Components[0][0] = SyllableByComponents[1];
						Components[0][1] = SyllableByComponents[0];
						Components[0][2] = "$";
						Components[0][3] = "$";
						Components[0][4] = SyllableByComponents[2];
						Components[0][5] = SyllableByComponents[3];
						Components[0][6] = SyllableByComponents[4];
					}

					else
						InValidSyllable();

					break;

				case 3:

					if (PrefixBaseletterMatch(SyllableByComponents[0],
							SyllableByComponents[2])
							&& SuperscriptBaseletterMatch(
									SyllableByComponents[1],
									SyllableByComponents[2])) {

						Components[0][0] = SyllableByComponents[2];
						Components[0][1] = SyllableByComponents[1];
						Components[0][2] = SyllableByComponents[0];
						Components[0][3] = "$";
						Components[0][4] = SyllableByComponents[3];
						Components[0][5] = SyllableByComponents[4];
						Components[0][6] = SyllableByComponents[5];
					}

					else if (PrefixBaseletterMatch(SyllableByComponents[0],
							SyllableByComponents[1])
							&& BaseletterSubscriptMatch(
									SyllableByComponents[1],
									SyllableByComponents[2])) {

						Components[0][0] = SyllableByComponents[1];
						Components[0][1] = "$";
						Components[0][2] = SyllableByComponents[0];
						Components[0][3] = SyllableByComponents[2];
						Components[0][4] = SyllableByComponents[3];
						Components[0][5] = SyllableByComponents[4];
						Components[0][6] = SyllableByComponents[5];
					}

					else if (SuperscriptBaseletterMatch(
							SyllableByComponents[0], SyllableByComponents[1])
							&& BaseletterSubscriptMatch(
									SyllableByComponents[1],
									SyllableByComponents[2])) {

						Components[0][0] = SyllableByComponents[1];
						Components[0][1] = SyllableByComponents[0];
						Components[0][2] = "$";
						Components[0][3] = SyllableByComponents[2];
						Components[0][4] = SyllableByComponents[3];
						Components[0][5] = SyllableByComponents[4];
						Components[0][6] = SyllableByComponents[5];
					}
					//For special cases of "brja", "bsnya", "brla", "bsna", ...
					else if ((SyllableByComponents[0]).equals("b")) {

						if (SuperscriptBaseletterMatch(SyllableByComponents[1],
								SyllableByComponents[2])) {
							Components[0][0] = SyllableByComponents[2];
							Components[0][1] = SyllableByComponents[1];
							Components[0][2] = SyllableByComponents[0];
							Components[0][3] = "$";
							Components[0][4] = SyllableByComponents[3];
							Components[0][5] = SyllableByComponents[4];
							Components[0][6] = SyllableByComponents[5];
						} else if (BaseletterSubscriptMatch(
								SyllableByComponents[1],
								SyllableByComponents[2])) {

							Components[0][0] = SyllableByComponents[1];
							Components[0][1] = "$";
							Components[0][2] = SyllableByComponents[0];
							Components[0][3] = SyllableByComponents[2];
							Components[0][4] = SyllableByComponents[3];
							Components[0][5] = SyllableByComponents[4];
							Components[0][6] = SyllableByComponents[5];
						} else
							InValidSyllable();

					}

					else
						InValidSyllable();

					break;

				case 4:
					Components[0][0] = SyllableByComponents[2];
					Components[0][1] = SyllableByComponents[1];
					Components[0][2] = SyllableByComponents[0];
					Components[0][3] = SyllableByComponents[3];
					Components[0][4] = SyllableByComponents[4];
					Components[0][5] = SyllableByComponents[5];
					Components[0][6] = SyllableByComponents[6];
					break;

				}

			}

			else if (nVowel >= 2) { //For more than two vowel Tibetan syllable,
									// like "nga'i", "tshu'u":

				int StartPoint = nCBV[0];

				for (int j = 0; j < nVowel; j++) {

					for (i = 0; i < j + 1; i++)
						StartPoint += nCBV[i];
					StartPoint += j;

					switch (nCBV[j + 1]) {

					case 0: //Special case for "a", the last letter in Tibetan
							// letter list, and the sequence led by its "i",
						//"o", "u" and "e".
						Components[j][0] = "a";
						Components[j][1] = "$";
						Components[j][2] = "$";
						Components[j][3] = "$";
						Components[j][4] = SyllableByComponents[StartPoint + 0];
						Components[j][5] = SyllableByComponents[StartPoint + 1];
						Components[j][6] = SyllableByComponents[StartPoint + 2];
						break;

					case 1:
						Components[j][0] = SyllableByComponents[StartPoint + 0];
						Components[j][1] = "$";
						Components[j][2] = "$";
						Components[j][3] = "$";
						Components[j][4] = SyllableByComponents[StartPoint + 1];
						Components[j][5] = "$";
						Components[j][6] = "$";
						break;

					case 2:

						if (PrefixBaseletterMatch(
								SyllableByComponents[StartPoint + 0],
								SyllableByComponents[StartPoint + 1])) {

							Components[j][0] = SyllableByComponents[StartPoint + 1];
							Components[j][1] = "$";
							Components[j][2] = SyllableByComponents[StartPoint + 0];
							Components[j][3] = "$";
							Components[j][4] = SyllableByComponents[StartPoint + 2];
							Components[j][5] = "$";
							Components[j][6] = "$";
						}

						else if (BaseletterSubscriptMatch(
								SyllableByComponents[StartPoint + 0],
								SyllableByComponents[StartPoint + 1])) {

							Components[j][0] = SyllableByComponents[StartPoint + 0];
							Components[j][1] = "$";
							Components[j][2] = "$";
							Components[j][3] = SyllableByComponents[StartPoint + 1];
							Components[j][4] = SyllableByComponents[StartPoint + 2];
							Components[j][5] = "$";
							Components[j][6] = "$";
						}

						else if (SuperscriptBaseletterMatch(
								SyllableByComponents[StartPoint + 0],
								SyllableByComponents[StartPoint + 1])) {

							Components[j][0] = SyllableByComponents[StartPoint + 1];
							Components[0][1] = SyllableByComponents[StartPoint + 0];
							Components[j][2] = "$";
							Components[j][3] = "$";
							Components[j][4] = SyllableByComponents[StartPoint + 2];
							Components[j][5] = "$";
							Components[j][6] = "$";
						}

						else
							InValidSyllable();

						break;

					case 3:

						if (PrefixBaseletterMatch(
								SyllableByComponents[StartPoint + 0],
								SyllableByComponents[StartPoint + 2])
								&& SuperscriptBaseletterMatch(
										SyllableByComponents[StartPoint + 1],
										SyllableByComponents[StartPoint + 2])) {

							Components[j][0] = SyllableByComponents[StartPoint + 2];
							Components[j][1] = SyllableByComponents[StartPoint + 1];
							Components[j][2] = SyllableByComponents[StartPoint + 0];
							Components[j][3] = "$";
							Components[j][4] = SyllableByComponents[StartPoint + 3];
							Components[j][5] = "$";
							Components[j][6] = "$";
						}

						else if (PrefixBaseletterMatch(
								SyllableByComponents[StartPoint + 0],
								SyllableByComponents[StartPoint + 1])
								&& BaseletterSubscriptMatch(
										SyllableByComponents[StartPoint + 1],
										SyllableByComponents[StartPoint + 2])) {

							Components[j][0] = SyllableByComponents[StartPoint + 1];
							Components[j][1] = "$";
							Components[j][2] = SyllableByComponents[StartPoint + 0];
							Components[j][3] = SyllableByComponents[StartPoint + 2];
							Components[j][4] = SyllableByComponents[StartPoint + 3];
							Components[j][5] = "$";
							Components[j][6] = "$";
						}

						else if (SuperscriptBaseletterMatch(
								SyllableByComponents[StartPoint + 0],
								SyllableByComponents[StartPoint + 1])
								&& BaseletterSubscriptMatch(
										SyllableByComponents[StartPoint + 1],
										SyllableByComponents[StartPoint + 2])) {

							Components[j][0] = SyllableByComponents[StartPoint + 1];
							Components[j][1] = SyllableByComponents[StartPoint + 0];
							Components[j][2] = "$";
							Components[j][3] = SyllableByComponents[StartPoint + 2];
							Components[j][4] = SyllableByComponents[StartPoint + 3];
							Components[j][5] = "$";
							Components[j][6] = "$";
						}
						//For special cases of "brja", "bsnya", "brla", "bsna",
						// ...
						else if ((SyllableByComponents[StartPoint + 0])
								.equals("b")) {

							if (SuperscriptBaseletterMatch(
									SyllableByComponents[StartPoint + 1],
									SyllableByComponents[StartPoint + 2])) {
								Components[j][0] = SyllableByComponents[StartPoint + 2];
								Components[j][1] = SyllableByComponents[StartPoint + 1];
								Components[j][2] = SyllableByComponents[StartPoint + 0];
								Components[j][3] = "$";
								Components[j][4] = SyllableByComponents[StartPoint + 3];
								Components[j][5] = "$";
								Components[j][6] = "$";
							} else if (BaseletterSubscriptMatch(
									SyllableByComponents[StartPoint + 1],
									SyllableByComponents[StartPoint + 2])) {

								Components[j][0] = SyllableByComponents[StartPoint + 1];
								Components[j][1] = "$";
								Components[j][2] = SyllableByComponents[StartPoint + 0];
								Components[j][3] = SyllableByComponents[StartPoint + 2];
								Components[j][4] = SyllableByComponents[StartPoint + 3];
								Components[j][5] = "$";
								Components[j][6] = "$";
							} else
								InValidSyllable();
						} else
							InValidSyllable();

						break;

					case 4:
						Components[j][0] = SyllableByComponents[StartPoint + 2];
						Components[j][1] = SyllableByComponents[StartPoint + 1];
						Components[j][2] = SyllableByComponents[StartPoint + 0];
						Components[j][3] = SyllableByComponents[StartPoint + 3];
						Components[j][4] = SyllableByComponents[StartPoint + 4];
						Components[j][5] = "$";
						Components[j][6] = "$";
						break;

					}

				}
			} else {
				InValidSyllable();
				return;
			}
		}

		else if (SanskritFlag) { //For Sanskrit syllable :
			int StartPoint = nCBV[0];
			for (int j = 0; j < nVowel; j++) {

				for (i = 0; i < j + 1; i++)
					StartPoint += nCBV[i];
				StartPoint += j;

				if (nCBV[j + 1] == 0) { //Special case for "a", the last letter
										// in Tibetan letter list, and the
										// sequences led by its "i",
					//"o", "u" and "e".
					Components[j][0] = SyllableByComponents[StartPoint
							+ nCBV[j + 1]];
					Components[j][1] = "$";
					Components[j][2] = "$";
					Components[j][3] = "$";
					Components[j][4] = SyllableByComponents[StartPoint
							+ nCBV[j + 1]];
					for (i = 0; i < nCBV[j + 1]; i++)
						Components[j][i + 1 + 4] = SyllableByComponents[StartPoint
								+ i];
				}

				else {
					Components[j][0] = SyllableByComponents[StartPoint + 0];
					Components[j][1] = "$";
					Components[j][2] = "$";
					Components[j][3] = "$";
					Components[j][4] = SyllableByComponents[StartPoint
							+ nCBV[j + 1]];
					for (i = 0; i < nCBV[j + 1]; i++)
						Components[j][i + 1 + 4] = SyllableByComponents[StartPoint
								+ i];
				}
			}

		}

		else {
			InValidSyllable();
			return;
		}
	}

	/** For cleaning up the invalid syllables by throwing them at the end of the list.
	 * 
	 * @throws RuntimeException
	 */ 
	private void InValidSyllable() throws RuntimeException {
		nVowels = 1;
		TibetanSyllableFlag = true;
		// System.out.println("This is not a valid Tibetan syllable: "+theSyllable);
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 20; j++)
				Components[i][j] = "invalid";
		throw new RuntimeException("This is not a valid Tibetan syllable: "
				+ theSyllable);
	}

} //End of the class