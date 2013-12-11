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

public class TagScoreUPF extends UpdateRequestProcessorFactory {
    private static final String TAG_PARAM = "datatags";
    private String tagFieldName;

	public static final String DELIMITER = " ";
	public static final String WORDS_PARAM = "words";
	public static final String GUESS_PARAM = "guess";
	public static final String TOKEN_COUNT_PARAM = "tokenCount";
	public static final String GUESS_COUNT_PARAM = "guessCount";
	public static final String CORRECT_COUNT_PARAM = "correctCount";
	public static final String ERROR_E_FIELD_PARAM = "errorE";
  
	private String wordsFieldName = null;
	private String guessFieldName = null;
	private String tokenCountFieldName = null;
	private String guessCountFieldName = null;
	private String correctCountFieldName = null;
	private String matchCountFieldName = null;
	private String tagCountFieldName = null;
	private String errorEFieldName = null;
  
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
		
		o = args.remove(ERROR_E_FIELD_PARAM);
		if (null == o || !(o instanceof String)) {
		    errorEFieldName = null;
		}
		else {
		    errorEFieldName = o.toString();
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
                        
                        SolrInputField errorEField = null;
                        if (errorEFieldName != null) {
                            errorEField = new SolrInputField(errorEFieldName + "_" + next);
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
                                
                                int w_taglength = 0;
                                int g_taglength = 0;
                        
                                String w_tag = new String("");
                                String g_tag = new String("");
                                
                                List<String> errorEOut = new LinkedList<String>();
         
                                while (wordsMatcher.find()) {
                                    w_start = wordsMatcher.start() - w_count - w_taglength;
                                    
                                    w_tag = words.substring(words.indexOf('|', wordsMatcher.start()), wordsMatcher.end());
                                    w_taglength += w_tag.length();
                                    
                                    w_end = wordsMatcher.end() - w_count - w_taglength;
                                    w_count += 1;
                                    
                                    while (g_end < w_end && guessMatcher.find()) {
                                        g_start = guessMatcher.start() - g_count - g_taglength;
                                        
                                        g_tag = guess.substring(guess.indexOf('|', guessMatcher.start()), guessMatcher.end());
                                        g_taglength += g_tag.length();
                                        
                                        g_end = guessMatcher.end() - g_count - g_taglength;
                                        g_count += 1;
                                        
                                        if (g_start == w_start && g_end == w_end && w_tag.equals(g_tag)) {
                                            if (null != errorEField) {
                                                errorEOut.add("A|" + guess.substring(guessMatcher.start(), guessMatcher.end()));
                                            }
                                            correct += 1;
                                        }
                                        else {
                                            //prefix = "<" + errorTag + " data-correct='" + words.substring(wordsMatcher.start(), wordsMatcher.end()) + "'>";
                                            if (null != errorEField) {
                                                errorEOut.add("E|" + guess.substring(guessMatcher.start(), guessMatcher.end()));
                                            }
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
                                
                                if (null != errorEField) {
                                    errorEField.addValue(StringUtils.join(errorEOut.iterator(), DELIMITER), 1.0f);
                                    doc.put(errorEFieldName + "_" + next, errorEField);
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
