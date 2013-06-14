package org.soas.solr.update.processor;

import org.chasen.crfpp.Tagger;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

import org.apache.solr.update.processor.UpdateRequestProcessor;

import org.apache.solr.common.params.SolrParams;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;

import org.apache.solr.update.AddUpdateCommand;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CrfppTaggerUpdateProcessor extends UpdateRequestProcessor {
    private final static Logger log = LoggerFactory.getLogger(CrfppTaggerUpdateProcessor.class);
  
    private static final String TAG_PARAM = "datatags";
    private String tagFieldName;
    
    private static final String PATH_PARAM = "path";
    private String pathFieldName;
    
    private static final String MODEL_PARAM = "model";
    
    private static final String SPLIT_INPUT_PARAM = "splitInput";
	private static final String TAG_DELIMITER_PARAM = "tagDelimiter";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
    
    private static final String SPLIT_INPUT_DEFAULT = "\\s+";
    private static final String TAG_DELIMITER_DEFAULT = "|";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
	
    private String model, splitInput, tagDelimiter, delimitOutput;
    
	private static final String UUID_PARAM = "uuidField";
    private String uuidFieldName;
    
	private static final String FOLDS_PREFIXES = "0123456789abcdef";
	private static final String FOLDS_PARAM = "folds";
    private String[] folds = null;
	
	public static final String GUESS_PARAM = "guess";
	private String guessFieldName;
	
	protected SolrParams defaults;
	protected SolrParams appends;
	protected SolrParams invariants;
	
	public CrfppTaggerUpdateProcessor(SolrParams params, UpdateRequestProcessor next) {
	    super(next);
	    initParams(params);
	}
	
	protected void initParams(SolrParams params) {
	    if (params != null) {
	        tagFieldName = params.get(TAG_PARAM, "");
	        pathFieldName = params.get(PATH_PARAM, "");
	        model = params.get(MODEL_PARAM, "");
	        guessFieldName = params.get(GUESS_PARAM, "");
	        uuidFieldName = params.get(UUID_PARAM, "");
	        
	        if (null != params.get(FOLDS_PARAM)) {
                String[] suffixes = params.get(FOLDS_PARAM).split(",");
                folds = new String[16];
                for (int i=0; i<FOLDS_PREFIXES.length(); i++) {
                    char f = FOLDS_PREFIXES.charAt(i);
                    for (int j=0; j<suffixes.length; j++) {
                        if (suffixes[j].indexOf(f) != -1) {
                            folds[i] = "_" + suffixes[j];
                            break;
                        }
                    }
                }
	        }
	        
	        splitInput = params.get(SPLIT_INPUT_PARAM, SPLIT_INPUT_DEFAULT);
	        tagDelimiter = params.get(TAG_DELIMITER_PARAM, TAG_DELIMITER_DEFAULT);
	        delimitOutput = params.get(DELIMIT_OUTPUT_PARAM, DELIMIT_OUTPUT_DEFAULT);
        }
    }

    @Override
	public void processAdd(AddUpdateCommand cmd) throws IOException {
	    final SolrInputDocument doc = cmd.getSolrInputDocument();
	    
	    Collection c = doc.getFieldValues(tagFieldName);
	    
	    if (c != null) {
	        Iterator it = c.iterator();
	        
	        log.info("Node id = " + doc.getFieldValue("entity_id"));
	        
	        while (it.hasNext()) {
                String next = (String)it.next();
                String field = guessFieldName + "_" + next;
                
                if (doc.containsKey(field)) {
                    SolrInputField guessField = doc.getField(field);
                    String val = (String)guessField.getValue();
                    
                    if (!val.equals("")) {
                        String[] syllables = val.split(splitInput);
                                
                        String suffix = "";
                        if (!uuidFieldName.equals("") && folds != null) {
                            SolrInputField uuidField = doc.getField(uuidFieldName);
                            String uuid = (String)uuidField.getValue();
                            suffix = folds[Integer.parseInt(uuid.substring(0,1), 16)];
                        }
                        
                        SolrInputField pathField = doc.getField(pathFieldName + "_" + next);
                        String path = (String)pathField.getValue();
                        String modelFile = path + "/" + model + suffix + ".lm";
                        
                        log.info("Model = " + modelFile);
                        
                        //use lm extension for language model
                        //Tagger tagger = new Tagger("-m " + modelFile + " -v 3 -n2");
                        Tagger tagger = new Tagger("-v1 -m " + modelFile);
                        tagger.clear();

                        for (int i=0; i<syllables.length; i++) {
                            //tagger.add(syllables[i]);
                            tagger.add(syllables[i].replace('|','\t'));
                        }
                        
                        if (tagger.parse()) {
                            List<String> tags = new LinkedList<String>();
                              
                            for (int i = 0; i < tagger.size(); ++i) {
                                String tag = "";
                                for (int j = 0; j < tagger.xsize(); ++j) {
                                    tag = tag + tagger.x(i, j) + tagDelimiter;
                                }
                                tag = tag + tagger.y2(i);
                                tags.add(tag);
                            }
                              
                            String result = StringUtils.join(tags.iterator(), delimitOutput);
                            guessField.setValue(result, 1.0f);
                            doc.put(field, guessField);
                        }
                    }
                }
            }
        }
        
        super.processAdd(cmd);
    }
}
