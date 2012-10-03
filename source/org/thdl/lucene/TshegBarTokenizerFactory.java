package org.thdl.lucene;

import org.apache.lucene.analysis.*;
import org.apache.solr.analysis.*;
import java.io.*;

public class TshegBarTokenizerFactory extends BaseTokenizerFactory {
    public TshegBarTokenizerFactory() {
    }
    public TokenStream create(Reader input) {
        return new TshegBarTokenizer(input);
    }
}

