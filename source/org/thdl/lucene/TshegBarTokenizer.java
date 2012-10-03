package org.thdl.lucene;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.Token;
import org.thdl.tib.scanner.*;
import java.io.*;
import java.util.*;

/**
 * Takes stream of Unicode Tibetan text and tokenizes it
 * into "syllables" or tsheg bars. Note that this is not
 * equivalent to tokenizing into "words" since words frequently
 * consist of more than one tsheg bar.
 * <p>
 * Non-Tibetan text and Tibetan punctuation is ignored by this
 * class.
 *
 * @author Edward Garrett
 */
public class TshegBarTokenizer extends Tokenizer {
    public int offset = 0;
    
    public static void main(String[] args) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("out.txt"), "UTF-8"));
            TshegBarTokenizer tok = new TshegBarTokenizer(new StringReader(args[0]));
            Token next = tok.next();
            while (next != null) {
                out.write(next.termText() + "\n");
                next = tok.next();
            }
            out.flush();
            out.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    /**
     * Constructs a <code>TshegBarTokenizer</code> from a <code>Reader</code>
     */
    public TshegBarTokenizer(Reader in) {
        super(in);
    }
    
    /**
     * Processes a stream of Tibetan text into a sequence of tsheg bars.
     * Note that tsheg bars are returned in citation (dictionary) form,
     * meaning in the normal case that final tshegs are added if not already
     * present.
     *
     * @return the next tsheg bar in the stream
     */
    public Token next() throws IOException {
        int c;
        do {
            c = input.read();
            offset++;
        } while (c!=-1 && !isPartOfTshegBar((char)c));
        if (c==-1) return null; //reached end of stream without finding token
        
        //otherwise, this is the start of the tsheg bar
        int start = offset-1;
        StringBuffer buffy = new StringBuffer();
        buffy.append((char)c);
  
        c = input.read();
        offset++;
        while (c!=-1 && isPartOfTshegBar((char)c)) {
            buffy.append((char)c);
            c = input.read();
            offset++;
        }
        buffy.append('\u0F0B'); //add tsheg to end of token
        
        String token = buffy.toString();
        if (c == '\u0F0B') {
            return new Token(token.toString(), start, offset, "?"); //include tsheg for purposes of highlighting
        } else {
            return new Token(token.toString(), start, offset-1, "?"); //type "?" means not yet tagged
        }
    }
    
    /**
     * Determines whether or not passed character belongs to the "inner" (contentful)
     * part of a Tibetan tsheg bar.
     *
     * @return <code>true</code> if <code>c</code> is both Tibetan and
     *          not a <code>Character.NON_SPACING_MARK</code>; <code>false</code>
     *          otherwise
     */
    public static boolean isPartOfTshegBar(char c) {
        return (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.TIBETAN && 
                (Character.isLetterOrDigit(c) || Character.getType(c) == Character.NON_SPACING_MARK)) 
                ? true : false;
    }
    
    //returns tsheg bar if string represents single tsheg bar; otherwise returns null
    /**
     * Reduces a series of possibly multiple tsheg bars to a single tsheg bar,
     * ignoring all tsheg bars after the first.
     *
     * @return the first tsheg bar within the passed
     *          <code>String</code>
     */
    public static String getSingleTshegBar(String val) throws IOException {
        TshegBarTokenizer tokenizer = new TshegBarTokenizer(new StringReader(val));
        Token next = tokenizer.next();
        if (next!=null) {
            if (tokenizer.next()==null) {
                return next.termText();
            }
        }
        return null;
    }
    
    /**
     * 
     * @return array containing tsheg bars occurring in passed <code>String</String>
     */
    public static String[] getTshegBars(String text_bo) throws IOException {
        TshegBarTokenizer tok = new TshegBarTokenizer(new StringReader(text_bo));
        List<String> tokens = new ArrayList<String>();
        Token next = tok.next();
        while (next != null) {
            tokens.add(next.termText());
            next = tok.next();
        }
        return (String[])tokens.toArray(new String[0]);
    }
}
