package org.thdl.lucene;

import org.apache.lucene.analysis.*;
import org.apache.solr.analysis.*;
import java.io.*;

public class EdgeTshegTrimmerFactory extends BaseTokenFilterFactory {
    public TokenStream create(TokenStream input) {
        return new EdgeTshegTrimmer(input);
    }
}

