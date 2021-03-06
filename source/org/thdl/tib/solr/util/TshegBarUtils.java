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

package org.thdl.tib.solr.util;

public class TshegBarUtils {
   public static final String TSHEG = Character.toString((char)3851);
    
    /** Do not instantiate this class. */
   private TshegBarUtils() { }

   public static boolean isTshegBarInternal(int c) {
   	   return Character.isLetterOrDigit(c) || Character.getType(c) == Character.NON_SPACING_MARK;
   }
   
   public static boolean isPunctuation(int c) {
   	   return Character.getType(c) == Character.OTHER_PUNCTUATION; //includes tsheg and shad
   }
   
   public static boolean isTshegBar(int c) {
   	   return c == 3851;
   }
   
   public static boolean isGa(int c) {
       return false;
   }
   
   public static boolean isOpenEnded(int c) {
       return isTshegBarInternal(c) && !isGa(c);
   }
}
