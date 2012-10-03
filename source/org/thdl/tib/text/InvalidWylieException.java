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

/**
* Thrown whenever
* a process runs into invalid Extended Wylie. This is usually thrown
* while converting Wylie to
* TibetanMachineWeb when the converter runs into invalid Wylie.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0
*/
public class InvalidWylieException extends Exception {
	private String wylie;
	private int offset;	

/**
* Creates an InvalidWylieException.
* @param s a string of Wylie text
* @param i the offset where problems for the process began
*/
	public InvalidWylieException(String s, int i) {
		wylie = s;
		offset = i;
	}

/**
* Gets the character that caused the problem.
* @return the character believed to be the problem for the process
*/
	public char getCulprit() {
		return wylie.charAt(offset);
	}

/**
* Gets the character that caused the problem, in context.
* @return the string of text including and following the character
* believed to be the problem for the process
*/
	public String getCulpritInContext() {
		if (wylie.length() < offset+5)
			return wylie.substring(offset, wylie.length());
		else
			return wylie.substring(offset, offset+5);
	}
}
