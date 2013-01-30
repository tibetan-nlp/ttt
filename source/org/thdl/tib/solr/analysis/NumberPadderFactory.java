package org.thdl.lucene;

import org.apache.lucene.analysis.*;
import org.apache.solr.analysis.*;

public class NumberPadderFactory extends BaseTokenFilterFactory {
    public TokenStream create(TokenStream input) {
        return new NumberPadder(input);
    }
}
