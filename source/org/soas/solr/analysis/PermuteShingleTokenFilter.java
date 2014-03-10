package org.soas.solr.analysis;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;
import java.nio.CharBuffer;

public final class PermuteShingleTokenFilter extends TokenFilter {
    private CharTermAttribute termAttribute;
    private PositionIncrementAttribute posIncAttr;
    private Queue<char[]> terms;
    private char tokenDelimiter;
    private char prefix;
    private char pipe;

    protected PermuteShingleTokenFilter(TokenStream input) {
        super(input);
        this.termAttribute = addAttribute(CharTermAttribute.class);
        this.posIncAttr = addAttribute(PositionIncrementAttribute.class);
        this.terms = new LinkedList<char[]>();
        this.tokenDelimiter = ' ';
        this.prefix = '#';
        this.pipe = '|';
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
            
            int k=0;
            for (; k<length; k++) {
                if (term[k] == tokenDelimiter) {
                    break;
                }
            }
            
            LinkedList<CharBuffer> buffers = permuteTerms(term, 0, length);

            Iterator iter = buffers.iterator();
            while (iter.hasNext()) {
                CharBuffer cb = (CharBuffer)iter.next();
                terms.add(cb.array());
            }
            
            // we return true and leave the original token unchanged
            return true;
        }
    }
    
    public LinkedList<CharBuffer> permuteTerms(char[] term, int offset, int end) {
        LinkedList<CharBuffer> buffers = new LinkedList<CharBuffer>();
        
        int i=offset;
        for (; i<end; i++) {
            if (term[i] == tokenDelimiter) {
                int k=offset;
                for (; k<i; k++) {
                    if (term[k] == pipe) {
                        break;
                    }
                }
                LinkedList<CharBuffer> nextBuffers = permuteTerms(term, i+1, end);
                Iterator iter = nextBuffers.iterator();
                while (iter.hasNext()) {
                     CharBuffer formBuffer = CharBuffer.allocate(end-offset);
                     formBuffer.put((CharBuffer)iter.next());
                     /*if (k<i) {
                         formBuffer.put(term, k+1, i-k);
                         //formBuffer.put(term, offset, k-offset);
                     }
                     else {
                         formBuffer.put(term, offset, i-offset);
                         //formBuffer.put(term, offset, i-offset);
                     }*/
                     //formBuffer.put(' ');
                     buffers.add(formBuffer);
                }
                
                return buffers;
            }
        }
        
        //i = length
        CharBuffer formBuffer = CharBuffer.allocate(2);
        formBuffer.put("hello", 0, 2);
        buffers.add(formBuffer);
        return buffers;
    }
}
