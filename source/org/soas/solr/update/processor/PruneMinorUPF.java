package org.soas.solr.update.processor;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;

import org.apache.commons.lang3.StringUtils;

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

public class PruneMinorUPF extends FieldMutatingUpdateProcessorFactory {
    private final static Logger log = LoggerFactory.getLogger(PruneMinorUPF.class);
    
	private static final String TAG_DELIMITER_PARAM = "tagDelimiter";
    private static final String TAG_DELIMITER_DEFAULT = "|";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
	private static final String TAG_MAPPING_PARAM = "tagMapping";
    private String tagDelimiter, delimitOutput;

    private Map<String,String> remap = new HashMap<String,String>();
    
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
	    
	    Object tagMappingParam = args.remove(TAG_MAPPING_PARAM);
	    if (null == tagMappingParam || !(tagMappingParam instanceof String)) {
	        //error
	    }
	    else {
	        String mappingFile = (String)tagMappingParam;
	        
	        try {
	            FileInputStream fstream = new FileInputStream(mappingFile);
	            DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null)   {
                    int n = strLine.indexOf('>');
                    if (!(strLine.trim().isEmpty() || n == -1)) {
                        remap.put(strLine.substring(0, n).trim(), strLine.substring(n+1).trim());
                    }
                }
                in.close();
	        } catch (Exception e) {
	            log.error("error", e);
	        }
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
                    String[] tokens = s.toString().split("\\s+");
                    List<String> words = new LinkedList<String>();
                    Pattern pattern = Pattern.compile("[\\[\\]]+");
                    
                    for (int j=0; j<tokens.length; j++) {
                        int n = tokens[j].indexOf(tagDelimiter);
                        
                        String tagString = tokens[j].substring(n+1);
                        if (tagString.charAt(0) != '[') {
                            String mappedTag = remap.containsKey(tagString) ? remap.get(tagString) : tagString;
                            words.add(tokens[j].substring(0, n) + tagDelimiter + mappedTag);
                        }
                        else {
                            String[] tags = pattern.split(tagString.substring(1));
                            Set<String> major = new TreeSet<String>();
                            for (int k=0; k<tags.length; k++) {
                                major.add(remap.containsKey(tags[k]) ? remap.get(tags[k]) : tags[k]);
                            }
                            words.add(tokens[j].substring(0, n) + tagDelimiter + "[" + StringUtils.join(major.iterator(), "][") + "]");
                        }
                    }
                    return StringUtils.join(words.iterator(), delimitOutput);
                }
                else 
                {
                    return src;
                }
            }
        };
    }
}
