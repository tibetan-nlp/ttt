package org.thdl.lucene;

import org.apache.lucene.analysis.*;
import java.text.DecimalFormat;
import java.io.*;

public class NumberPadder extends TokenFilter {
    public static final String NUMBER_TYPE = "Number";
    private static final DecimalFormat formatter = new DecimalFormat("0000000000");

    public static String pad(int n) {
        return formatter.format(n);
    }    

    public NumberPadder(TokenStream input) {
        super(input);
    }
    
    public Token next() throws IOException {
        Token token = input.next();
        if (token == null) 
            return null;
        try {
            int i = Integer.parseInt(token.termText());
            Token replace = new Token(pad(i), token.startOffset(), token.endOffset(), NUMBER_TYPE);
            replace.setPositionIncrement(token.getPositionIncrement());
            return replace;
        } catch (NumberFormatException nfe) {
            return token;
        }
    }
}
