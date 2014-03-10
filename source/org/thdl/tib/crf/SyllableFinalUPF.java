package org.thdl.tib.crf;

import java.util.List;
import java.util.LinkedList;
import java.util.regex.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
//http://commons.apache.org/proper/commons-lang/javadocs/api-3.1/org/apache/commons/lang3/BooleanUtils.html#toBoolean(java.lang.String)

import org.apache.solr.update.processor.FieldMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldValueMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldMutatingUpdateProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.thdl.tib.solr.util.TshegBarUtils;

public class SyllableFinalUPF extends FieldMutatingUpdateProcessorFactory {
    private final static Logger log = LoggerFactory.getLogger(SyllableFinalUPF.class);
    
	private static final String TAG_DELIMITER_PARAM = "tagDelimiter";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
	private static final String LAST_PARAM = "last";
	private static final String TSHEG_PARAM = "tsheg";
	private static final String LENGTH_PARAM = "length";
	private static final String SYLLABLECOUNT_PARAM = "syllablecount";
	private static final String SYLLABLES_PARAM = "syllables";
    
    
    private static final String TAG_DELIMITER_DEFAULT = "|";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
    private static final boolean LAST_DEFAULT = true;
    private static final boolean TSHEG_DEFAULT = true;
    private static final boolean LENGTH_DEFAULT = true;
    private static final boolean SYLLABLECOUNT_DEFAULT = true;
    private static final boolean SYLLABLES_DEFAULT = true;
    
    private String tagDelimiter, delimitOutput;
    private boolean enableLast, enableLength, enableTsheg, enableSyllablecount, enableSyllables;
    
    public static Pattern finalpattern = Pattern.compile("\\S+([^\\p{Mn}\\p{P}]\\p{Mn}*)\u0f0b?");
    
    
	@SuppressWarnings("unchecked")
	@Override
	public void init(NamedList args) {
	    Object tagDelimiterParam = args.remove(TAG_DELIMITER_PARAM);
	    if (null == tagDelimiterParam || !(tagDelimiterParam instanceof String)) {
	        tagDelimiter = TAG_DELIMITER_DEFAULT;
	    }
	    else {
	        tagDelimiter = (String)tagDelimiterParam;
	    }
	    
	    Object delimitOutputParam = args.remove(DELIMIT_OUTPUT_PARAM);
	    if (null == delimitOutputParam || !(delimitOutputParam instanceof String)) {
	        delimitOutput = DELIMIT_OUTPUT_DEFAULT;
	    }
	    else {
	        delimitOutput = (String)delimitOutputParam;
	    }
	    
	    Object lastParam = args.remove(LAST_PARAM);
	    if (null == lastParam || !(lastParam instanceof Boolean)) {
	        enableLast = LAST_DEFAULT;
	    }
	    else {
	        enableLast = ((Boolean)lastParam).booleanValue();
	    }
	    
	    Object tshegParam = args.remove(TSHEG_PARAM);
	    if (null == tshegParam || !(tshegParam instanceof Boolean)) {
	        enableTsheg = TSHEG_DEFAULT;
	    }
	    else {
	        enableTsheg = ((Boolean)tshegParam).booleanValue();
	    }
	    
	    Object lengthParam = args.remove(LENGTH_PARAM);
	    if (null == lengthParam || !(lengthParam instanceof Boolean)) {
	        enableLength = LENGTH_DEFAULT;
	    }
	    else {
	        enableLength = ((Boolean)lengthParam).booleanValue();
	    }
	    
	    Object syllablecountParam = args.remove(SYLLABLECOUNT_PARAM);
	    if (null == syllablecountParam || !(syllablecountParam instanceof Boolean)) {
	        enableSyllablecount = SYLLABLECOUNT_DEFAULT;
	    }
	    else {
	        enableSyllablecount = ((Boolean)syllablecountParam).booleanValue();
	    }
	    
	    Object syllablesParam = args.remove(SYLLABLES_PARAM);
	    if (null == syllablesParam || !(syllablesParam instanceof Boolean)) {
	        enableSyllables = SYLLABLES_DEFAULT;
	    }
	    else {
	        enableSyllables = ((Boolean)syllablesParam).booleanValue();
	    }
	    
        super.init(args);
    }
  
    @Override
    public FieldMutatingUpdateProcessor.FieldNameSelector getDefaultSelector(final SolrCore core) {
        return FieldMutatingUpdateProcessor.SELECT_NO_FIELDS;
    }
  
    @Override
    public UpdateRequestProcessor getInstance(SolrQueryRequest req, SolrQueryResponse rsp, UpdateRequestProcessor next) {
        return new FieldValueMutatingUpdateProcessor(getSelector(), next) {
            protected Object mutateValue(final Object src) {
                if (src instanceof CharSequence) {
                    CharSequence s = (CharSequence)src;
                    String[] in = s.toString().split("\\s+");
                    List<String> out = new LinkedList<String>();
                    for (int j=0; j<in.length; j++) {
                        int k = in[j].indexOf('|'); //should really be tagDelimiter
                        String rest = (k == -1) ? "" : in[j].substring(k);
                        String word = (k == -1) ? in[j] : in[j].substring(0,k);
                        
                        StringBuilder sb = new StringBuilder();
                        sb.append(word);
                        int tsheg = TshegBarUtils.isTshegBar(word.charAt(word.length()-1)) ? 1 : 0;
                        if (enableLast) {
                            Matcher m = finalpattern.matcher(word);
                            sb.append(tagDelimiter + (m.matches() ? m.group(1) : "0"));
                            //sb.append(tagDelimiter + word.substring(word.length()-1-tsheg, word.length()-tsheg)); //last character of word
                        }
                        if (enableTsheg) {
                            sb.append(tagDelimiter + Integer.toString(tsheg)); //whether or not word ends in tsheg
                        }
                        String[] syllables = word.split(TshegBarUtils.TSHEG);
                        if (enableSyllablecount) {
                            sb.append(tagDelimiter + Integer.toString(syllables.length)); //word length in syllables
                        }
                        if (enableSyllables) {
                            String sils =
                                syllables[0] + tagDelimiter +
                                (syllables.length > 1 ? syllables[1] : "0") + tagDelimiter +
                                (syllables.length > 2 ? syllables[2] : "0");
                            sb.append(tagDelimiter + sils); //syllables 1-3
                        }
                        sb.append(rest); //whatever else is already there
                        out.add(sb.toString());
                    }
                    
                    return StringUtils.join(out.iterator(), delimitOutput);
                }
                else 
                {
                    return src;
                }
            }
        };
    }
}
