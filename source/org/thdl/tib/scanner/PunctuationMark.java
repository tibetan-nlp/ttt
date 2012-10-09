/*
The contents of this file are subject to the AMP Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the AMP web site 
(http://www.tibet.iteso.mx/Guatemala/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is Andres Montano Pellegrini. Portions
created by Andres Montano Pellegrini are Copyright 2001 Andres Montano
Pellegrini. All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.scanner;

/** Right now, it only used by {@link LocalTibetanScanner} to
    separate &quot;paragraphs&quot;; eventually it will be one
    of many tokens representing grammatical parts of the sentences that will be
    interpreted by the parser.
    
    @author Andr&eacute;s Montano Pellegrini
    @see LocalTibetanScanner
*/
public class PunctuationMark extends Token
{
    public PunctuationMark(char ch)
    {
        super(String.valueOf(ch));
    }
    
    public PunctuationMark(char ch, int offset)
    {
        super(String.valueOf(ch), offset);
    }
    
    public String toString()
    {
        return super.token;
    }
}