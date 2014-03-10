package org.soas.solr.analysis;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.Queue;
import java.util.LinkedList;
import java.nio.CharBuffer;

public final class MagicPrefixTokenFilter extends TokenFilter {
    private CharTermAttribute termAttribute;
    private PositionIncrementAttribute posIncAttr;
    private Queue<char[]> terms;
    private char tokenDelimiter;
    private char prefix;

    protected MagicPrefixTokenFilter(TokenStream input) {
        super(input);
        this.termAttribute = addAttribute(CharTermAttribute.class);
        this.posIncAttr = addAttribute(PositionIncrementAttribute.class);
        this.terms = new LinkedList<char[]>();
        this.tokenDelimiter = ' ';
        this.prefix = '#';
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!terms.isEmpty()) {
            char[] buffer = terms.poll();
            termAttribute.setEmpty();
            termAttribute.copyBuffer(buffer, 0, buffer.length);
            posIncAttr.setPositionIncrement(1);
            return true;
        }

        if (!input.incrementToken()) {
            return false;
        } else {
            final char term[] = termAttribute.buffer();
            final int length = termAttribute.length();
            
            for (int i=0; i<length; i++) {
                if (term[i] == tokenDelimiter) {
                    CharBuffer cb = CharBuffer.allocate(length+1);
                    cb.put(term, i+1, length-i-1);
                    cb.put(' ');
                    cb.put(prefix);
                    cb.put(term, 0, i);
                    terms.add(cb.array());
                }
            }
            
            // we return true and leave the original token unchanged
            return true;
        }
    }
}
