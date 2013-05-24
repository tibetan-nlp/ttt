package org.soas.solr.update.processor;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
//import java.util.Arrays;

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

public class LexiconTaggerUPF extends FieldMutatingUpdateProcessorFactory {
    private final static Logger log = LoggerFactory.getLogger(Tt4jTaggerUpdateProcessor.class);
    
    private static final String LEXICON_PARAM = "lexicon";
    
	private static final String TAG_DELIMITER_PARAM = "tagDelimiter";
    private static final String TAG_DELIMITER_DEFAULT = "|";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
    private String tagDelimiter, delimitOutput;
    private HashMap<String,String> lexicon = new HashMap();
    
    
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
	
	    Object lexiconParam = args.remove(LEXICON_PARAM);
	    if (null == lexiconParam || !(lexiconParam instanceof String)) {
	        //error
	    }
	    else {
	        String lexiconFile = (String)lexiconParam;
	        
	        try {
	            FileInputStream fstream = new FileInputStream(lexiconFile);
	            DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String strLine;
                while ((strLine = br.readLine()) != null)   {
                    String[] tags = strLine.substring(strLine.indexOf('\t')+1).split("\\s+");
                    if (tags.length > 1) {
                        //should perhaps sort tags but they are already sorted by faceting
                        StringBuilder sb = new StringBuilder();
                        for (int i=0; i<tags.length-1; i+=2) {
                            sb.append("[" + tags[i] + "]");
                        }
                        lexicon.put(strLine.substring(0, strLine.indexOf('\t')), sb.toString());
                    }
                }
                in.close();
            } catch (Exception e){
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
                    String[] in = s.toString().split("\\s+");
                    List<String> out = new LinkedList<String>();
                    for (int j=0; j<in.length; j++) {
                        int k = in[j].indexOf('|');
                        if (k == -1) {
                            if (lexicon.containsKey(in[j])) {
                                out.add(in[j] + tagDelimiter + lexicon.get(in[j]));
                            }
                            else {
                                out.add(in[j]);
                            }
                        }
                        else {
                            String word = in[j].substring(0,k);
                            String pos =  in[j].substring(k+1);
                            if (lexicon.containsKey(word)) {
                                out.add(word + tagDelimiter + lexicon.get(word) + tagDelimiter + pos);
                            }
                            else {
                                out.add(word + tagDelimiter + pos);
                            }
                        }
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
