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

package org.thdl.tib.bibl;

 import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/* FIXMEDOC.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/
 public class TibDTDResolver implements EntityResolver, TibConstants {
   public InputSource resolveEntity (String publicId, String systemId)
   {
     if (systemId.indexOf("dtd")>-1) {
              // return a special input source

       int lastSlash = systemId.lastIndexOf("/");
       String fname = "file:/" + DEFAULT_DIRECTORY + BIN + systemId.substring(lastSlash+1);
       fname = fname.replace('\\','/');
       InputSource inSrc = new InputSource(fname);
       return inSrc;
     } else {
              // use the default behaviour
       return null;
     }
   }
 }
