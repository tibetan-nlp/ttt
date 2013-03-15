/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.soas.solr.update.processor;

import org.apache.solr.update.processor.UpdateRequestProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;

import java.io.IOException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.AddUpdateCommand;

public class SegScoreUPF extends UpdateRequestProcessorFactory {
    private static final String TAG_PARAM = "datatags";
    private String tagFieldName;

	public static final String DELIMITER = " ";
	public static final String WORDS_PARAM = "words";
	public static final String GUESS_PARAM = "guess";
	public static final String TOKEN_COUNT_PARAM = "tokenCount";
	public static final String GUESS_COUNT_PARAM = "guessCount";
	public static final String CORRECT_COUNT_PARAM = "correctCount";
	public static final String HIGHLIGHT_PARAM = "highlight";
	public static final String ERROR_TAG_PARAM = "errorTag";
	public static final String DEFAULT_ERROR_TAG = "em";
  
	private String wordsFieldName = null;
	private String guessFieldName = null;
	private String tokenCountFieldName = null;
	private String guessCountFieldName = null;
	private String correctCountFieldName = null;
	
	private String highlightFieldName = null;
	private String errorTag = null;
   
	public void init(NamedList args) {
		Object o = args.remove(WORDS_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    wordsFieldName = o.toString();
		}
		
		o = args.remove(GUESS_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    guessFieldName = o.toString();
		}
		
		o = args.remove(TOKEN_COUNT_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    tokenCountFieldName = o.toString();
		}
		
		o = args.remove(GUESS_COUNT_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    guessCountFieldName = o.toString();
		}
		
		o = args.remove(CORRECT_COUNT_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    correctCountFieldName = o.toString();
		}
		
		o = args.remove(HIGHLIGHT_PARAM);
		if (null == o || !(o instanceof String)) {
		    highlightFieldName = null;
		}
		else {
		    highlightFieldName = o.toString();
		}
		
		o = args.remove(ERROR_TAG_PARAM);
		if (null == o || !(o instanceof String)) {
		    errorTag = null;
		}
		else {
		    errorTag = o.toString();
		}
		
		o = args.remove(TAG_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    tagFieldName = o.toString();
		}
	}
	    
	@Override
	public final UpdateRequestProcessor getInstance(SolrQueryRequest req, SolrQueryResponse rsp, UpdateRequestProcessor next) 
    {
		return new UpdateRequestProcessor(next) {
			@Override
			public void processAdd(AddUpdateCommand cmd) throws IOException {

				final SolrInputDocument doc = cmd.getSolrInputDocument();
				
				Collection c = doc.getFieldValues(tagFieldName);
	    
				if (c != null) {
				    Iterator it = c.iterator();
	        
				    while (it.hasNext()) {
				        String next = (String)it.next();
				        
                        // preserve initial values and boost (if any)
                        SolrInputField highlightField = null;
                        String prefix = null;
                        String suffix = null;
                        if (highlightFieldName != null) {
                            highlightField = new SolrInputField(highlightFieldName + "_" + next);
                            if (errorTag == null) errorTag = DEFAULT_ERROR_TAG;
                            prefix = "<" + errorTag + ">";
                            suffix = "</" + errorTag + ">";
                        }
				      		
                        if (doc.containsKey(guessFieldName + "_" + next)) {
                            String guess = ((String)doc.getField(guessFieldName + "_" + next).getValue()).trim();
                            Pattern pattern = Pattern.compile("\\S+");
                            Matcher guessMatcher = pattern.matcher(guess);
                            
                            int g_count = 0;
                            
                            if (doc.containsKey(wordsFieldName)) {
                                String words = ((String)doc.getField(wordsFieldName).getValue()).trim();
                                Matcher wordsMatcher = pattern.matcher(words);
                                
                                int correct = 0;
                                int w_count = 0;
                                
                                int w_start = 0;
                                int w_end = 0;
                                int g_start = 0;
                                int g_end = 0;
                                
                                List<String> out = new LinkedList<String>();
                 
                                while (wordsMatcher.find()) {
                                    w_start = wordsMatcher.start() - w_count;
                                    w_end = wordsMatcher.end() - w_count;
                                    w_count += 1;
                                    
                                    //debug: out = out.concat(" w{" + w_start + "," + w_end + "}:" + words.substring(wordsMatcher.start(), wordsMatcher.end()));
                                    
                                    while (g_end < w_end && guessMatcher.find()) {
                                        g_start = guessMatcher.start() - g_count;
                                        g_end = guessMatcher.end() - g_count;
                                        g_count += 1;
                                        //debug: out = out.concat(" g{" + g_start + "," + g_end + "}:" + guess.substring(guessMatcher.start(), guessMatcher.end()));
                                        if (g_start == w_start && g_end == w_end) {
                                            //debug: out = out.concat("|Y");
                                            if (null != highlightField) {
                                                out.add(guess.substring(guessMatcher.start(), guessMatcher.end()));
                                            }
                                            correct += 1;
                                        }
                                        else if (null != highlightField) {
                                            out.add(prefix + guess.substring(guessMatcher.start(), guessMatcher.end()) + suffix);
                                        }
                                    }
                                }
                        
                                SolrInputField tokenCountField = new SolrInputField(tokenCountFieldName + "_" + next);
                                tokenCountField.addValue(new Integer(w_count), 1.0f);
                                doc.put(tokenCountFieldName + "_" + next, tokenCountField);
                                
                                SolrInputField guessCountField = new SolrInputField(guessCountFieldName + "_" + next);
                                guessCountField.addValue(new Integer(g_count), 1.0f);
                                doc.put(guessCountFieldName + "_" + next, guessCountField);
                                
                                SolrInputField correctCountField = new SolrInputField(correctCountFieldName + "_" + next);
                                correctCountField.addValue(new Integer(correct), 1.0f);
                                doc.put(correctCountFieldName + "_" + next, correctCountField);
                                
                                if (null != highlightField) {
                                    highlightField.addValue(StringUtils.join(out.iterator(), DELIMITER), 1.0f);
                                    doc.put(highlightFieldName + "_" + next, highlightField);
                                } 
                            }
                            else {
                                while (guessMatcher.find()) {
                                    g_count++;
                                }
                                SolrInputField guessCountField = new SolrInputField(guessCountFieldName + "_" + next);
                                guessCountField.addValue(new Integer(g_count), 1.0f);
                                doc.put(guessCountFieldName + "_" + next, guessCountField);
                            }
                        }
                    }
                }
                        
                super.processAdd(cmd);
			}
		};
	}
}
