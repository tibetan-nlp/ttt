package org.thdl.lucene;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.Token;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Trims excess tshegs and other punctuation from Tibetan
 * words, leaving them in their proper citation form.
 *
 * @author Edward Garrett
 */
public class EdgeTshegTrimmer extends TokenFilter {
    public EdgeTshegTrimmer(TokenStream input) {
        super(input);
    }
    
    /**
     * @return next token in TokenStream, stripped of superfluous
     * tshegs
     */
    public Token next() throws IOException {
        while (true) {
            Token token = input.next();
            if (token == null)
                return null;
            int length=token.termText().length();
            int start=0;
            while (start<length && !TshegBarTokenizer.isPartOfTshegBar(token.termText().charAt(start))) start++;
            int end=length-1;
            while (end>-1 && !TshegBarTokenizer.isPartOfTshegBar(token.termText().charAt(end))) end--;
            if (start<=end) {
                return new Token(addFinalTshegIfNecessary(token.termText().substring(start,end+1)), token.startOffset(), token.endOffset());
            }
        }
    }
    
    /**
     * Adds a tsheg to a <code>String</code> that doesn't 
     * already end in one.
     *
     * @return original <code>String</code> with final tsheg 
     * added if necessary
     */
    public static String addFinalTshegIfNecessary(String s) {
        if (s.charAt(s.length()-1) == '\u0F0B')
            return s;
        else
            return s += "\u0F0B";
       // if (last == '\u0F42' || last == '\u0F0B')
       //     return s;
       // else
    }
}
