package org.soas.solr.update.processor;

import org.annolab.tt4j.TreeTaggerWrapper;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TokenHandler;

import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import org.apache.solr.update.processor.UpdateRequestProcessor;

import org.apache.solr.common.params.SolrParams;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;

import org.apache.solr.update.AddUpdateCommand;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Tt4jTaggerUpdateProcessor extends UpdateRequestProcessor {
    private final static Logger log = LoggerFactory.getLogger(Tt4jTaggerUpdateProcessor.class);
  
    private static final String TAG_PARAM = "datatags";
    private String tagFieldName;
    
    private static final String PATH_PARAM = "path";
    private String pathFieldName;
    
    private static final String MODEL_PARAM = "model";
    
	private static final String TAG_DELIMITER_PARAM = "tagDelimiter";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
    
    private static final String TAG_DELIMITER_DEFAULT = "|";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
	
    private String model, tagDelimiter, delimitOutput;
    
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
	
	public Tt4jTaggerUpdateProcessor(SolrParams params, UpdateRequestProcessor next) {
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
                        
                        final List<String> tags = new LinkedList<String>();
                        
                        // Point TT4J to the TreeTagger installation directory. The executable is expected
                        // in the "bin" subdirectory - in this example at "/opt/treetagger/bin/tree-tagger"
                        System.setProperty("treetagger.home", "/opt/treetagger");
                        TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
                        //tt.setPerformanceMode(true);
                        //tt.setStrictMode(true);
                        try {
                            tt.setModel(modelFile); //move to initParams
                            tt.setHandler(new TokenHandler<String>() {
                                    public void token(String token, String pos, String lemma) {
                                        tags.add(token + tagDelimiter + pos);
                                    }
                            });
                            Pattern pattern = Pattern.compile("\\S+");
                            Matcher matcher = pattern.matcher(val);
                            List<String> tokens = new LinkedList(); 
                            while (matcher.find()) {
                                String s = matcher.group().replace('|', '\t').replace('/', '\t');
                                //log.info("Token = " + s);
                                tokens.add(s);
                            }
                            tt.process(tokens);
                        }
                        catch (IOException ioe) {
                            log.error("Failed", ioe);
                        }
                        catch (TreeTaggerException tte) {
                            log.error("Failed", tte);
                        }
                        finally {
                            tt.destroy();
                        }
                              
                        String result = StringUtils.join(tags.iterator(), delimitOutput);
                        guessField.setValue(result, 1.0f);
                        doc.put(field, guessField);
                    }
                }
            }
        }
        
        super.processAdd(cmd);
    }
}
