package org.thdl.tib.solr;

import org.thdl.tib.solr.util.TshegBarUtils;

import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

import org.apache.solr.update.processor.FieldMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldValueMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldMutatingUpdateProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class TshegBarTaggerUPF extends FieldMutatingUpdateProcessorFactory {
    private static final String SPLIT_INPUT_PARAM = "splitInput";
	private static final String TAG_DELIMITER_PARAM = "tagDelimiter";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
    private static final String TAGSET_PARAM = "tagSet";
    private static final String REPEAT_TAG_PARAM = "repeatTag";
    
    private static final String SPLIT_INPUT_DEFAULT = "\\s+";
    private static final String TAG_DELIMITER_DEFAULT = "|";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
    private static final String TAGSET_DEFAULT = "S, B-E, B-M-E";
    private static final String REPEAT_TAG_DEFAULT = "M";
    
    private String splitInput, tagDelimiter, delimitOutput, repeatTag;
    private String[][] tagSet;

	@SuppressWarnings("unchecked")
	@Override
	public void init(NamedList args) {
	    Object splitInputParam = args.remove(SPLIT_INPUT_PARAM);
	    if (null == splitInputParam || !(splitInputParam instanceof String)) {
	        splitInput = SPLIT_INPUT_DEFAULT;
	    }
	    else {
	        splitInput = (String)splitInputParam;
	    }
	    
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
	    
	    Object repeatTagParam = args.remove(REPEAT_TAG_PARAM);
	    if (null == repeatTagParam || !(repeatTagParam instanceof String)) {
	        repeatTag = REPEAT_TAG_DEFAULT;
	    }
	    else {
	        repeatTag = (String)repeatTagParam;
	    }
	    
	    String tagSetString;
	    Object tagSetParam = args.remove(TAGSET_PARAM);
	    if (null == tagSetParam || !(tagSetParam instanceof String)) {
	        tagSetString = TAGSET_DEFAULT;
	    }
	    else {
	        tagSetString = (String)tagSetParam;
	    }
	    String[] tagSeq = tagSetString.split(",");
	    tagSet = new String[tagSeq.length][];
	    for (int i=0; i<tagSeq.length; i++) {
	        tagSet[i] = tagSeq[i].trim().split("\\-");
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
                    int r = 0;
                    boolean match = false;
                    for ( ; r<tagSet[tagSet.length-1].length; r++) {
                        if (tagSet[tagSet.length-1][r].equals(repeatTag)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) r=0;
        	
                    CharSequence s = (CharSequence)src;
                    String[] tokens = s.toString().split(splitInput);
                    List<String> allSyl = new LinkedList<String>();
                    
                    for (int j=0; j<tokens.length; j++) {
                        int start = 0;
                        List<String> wordSyl = new LinkedList<String>();
                        for (int i=0; i<tokens[j].length(); i++) {
                            if (TshegBarUtils.isTshegBarEdge(tokens[j].charAt(i)) || i == tokens[j].length()-1) {
                                wordSyl.add(tokens[j].subSequence(start, i+1).toString());
                                start = i+1;
                            }
                        }
        
                        int n = wordSyl.size();
                        if (n <= tagSet.length) {
                            int k=0;
                            for (String syl : wordSyl) {
                                allSyl.add(syl.concat(tagDelimiter).concat(tagSet[n-1][k++]));
                            }
                        }
                        else {
                            int m = n - tagSet.length + 1;
                            int k = 0;
                            for (String syl : wordSyl) {
                                allSyl.add(syl.concat(tagDelimiter).concat(tagSet[tagSet.length-1][k]));
                                if (k == r) {
                                    m--;
                                    if (m == 0) {
                                        k++;
                                    }
                                }
                                else {
                                    k++;
                                }
                            }
                        }
                    }
                    return StringUtils.join(allSyl.iterator(), delimitOutput);
                }
                else 
                {
                    return src;
                }
            }
        };
    }
}
