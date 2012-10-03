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

package org.thdl.util;

import java.io.PrintStream;

/**
 * All writes to this print stream are copied to two print streams of
 * your choice.
 * @author David Chandler
 */
public class TeeStream extends PrintStream {
	private PrintStream out;
	public TeeStream(PrintStream out1, PrintStream out2) {
		super(out1);
		this.out = out2;
	}
	public void write(byte buf[], int off, int len) {
		try {
			super.write(buf, off, len);
			out.write(buf, off, len);
		} catch (Exception e) {
		}
	}
	public void flush() {
		super.flush();
		out.flush();
	}
};

