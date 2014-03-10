package org.soas.solr.analysis;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.reverse.ReverseStringFilter;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.util.CharacterUtils;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.Queue;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public final class MagicPrefixTokenFilter extends TokenFilter {
    private CharTermAttribute charTermAttr;
    private PositionIncrementAttribute posIncAttr;
    private Queue<char[]> terms;
    private String prefix;
    private Pattern pattern;

    protected MagicPrefixTokenFilter(TokenStream input, String prefix, String tokenSeparator) {
        super(input);
        this.charTermAttr = addAttribute(CharTermAttribute.class);
        this.posIncAttr = addAttribute(PositionIncrementAttribute.class);
        this.terms = new LinkedList<char[]>();
        this.prefix = prefix;
        this.pattern = pattern.compile(tokenSeparator);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!terms.isEmpty()) {
            char[] buffer = terms.poll();
            charTermAttr.setEmpty();
            charTermAttr.copyBuffer(buffer, 0, buffer.length);
            posIncAttr.setPositionIncrement(1);
            return true;
        }

        if (!input.incrementToken()) {
            return false;
        } else {
            // we reverse the token
            int length = charTermAttr.length();
            char[] buffer = charTermAttr.buffer();
            Matcher matcher = pattern.matcher(new String(buffer));
            
            while (matcher.find()) {
                StringBuilder sb = new StringBuilder();
                sb.append(buffer, matcher.end(), length-matcher.end());
                sb.append(prefix);
                sb.append(buffer, 0, matcher.end());
                terms.add(sb.toString().toCharArray());
            }
            
            // we return true and leave the original token unchanged
            return true;
        }
    }
}
