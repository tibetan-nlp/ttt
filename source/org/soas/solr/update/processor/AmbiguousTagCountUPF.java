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
import java.util.Arrays;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.AddUpdateCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmbiguousTagCountUPF extends UpdateRequestProcessorFactory {
    private final static Logger log = LoggerFactory.getLogger(AmbiguousTagCountUPF.class);
    
    private static final String TAG_PARAM = "datatags";
    private String tagFieldName;

	public static final String DELIMITER = " ";
	public static final String WORDS_PARAM = "words";
	public static final String GUESS_PARAM = "guess";
	public static final String TOKEN_COUNT_PARAM = "tokenCount";
	public static final String MATCH_COUNT_PARAM = "matchCount";
	public static final String CORRECT_COUNT_PARAM = "correctCount";
	public static final String TAG_COUNT_PARAM = "tagCount";
  
	private String wordsFieldName = null;
	private String guessFieldName = null;
	private String tokenCountFieldName = null;
	private String matchCountFieldName = null;
	private String correctCountFieldName = null;
	private String tagCountFieldName = null;
  
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
		
		o = args.remove(MATCH_COUNT_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    matchCountFieldName = o.toString();
		}
		
		o = args.remove(CORRECT_COUNT_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    correctCountFieldName = o.toString();
		}
		
		o = args.remove(TAG_COUNT_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    tagCountFieldName = o.toString();
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

				if (doc.containsKey(wordsFieldName)) {
				    String[] words = ((String)doc.getField(wordsFieldName).getValue()).trim().split("\\s+");
                               				
				    //Pattern tagPattern = Pattern.compile("\\[[^\\]]+\\]");
				    
				    Pattern tagPattern = Pattern.compile("\\]\\[");
				    
                    Collection c = doc.getFieldValues(tagFieldName);
                    if (c != null) {
                        Iterator it = c.iterator();
                
                        while (it.hasNext()) {
                            String next = (String)it.next();
                            
                            if (doc.containsKey(guessFieldName + "_" + next)) {
                                String[] guess = ((String)doc.getField(guessFieldName + "_" + next).getValue()).trim().split("\\s+");
                                
                                int match_count = 0;
                                int correct_count = 0;
                                int tag_count = 0;
    
                                //assume all tags include brackets
                                for (int i=0; i<words.length; i++) {
                                    String wTag = words[i].substring(words[i].indexOf('|')+1);
                                    
                                    int gIndex = guess[i].indexOf('|');
                                    if (gIndex != -1) {
                                        String gTag = guess[i].substring(gIndex+1);
                                        if (gTag.length() > 0) {
                                            List<String> wordTags = Arrays.asList(tagPattern.split(wTag.substring(1,wTag.length()-1)));
                                            List<String> guessedTags = Arrays.asList(tagPattern.split(gTag.substring(1,gTag.length()-1)));
                                            
                                            if (guessedTags.equals(wordTags)) {
                                                correct_count++;
                                                match_count++;
                                            }
                                            else if (guessedTags.containsAll(wordTags)) {
                                                match_count++;
                                            }
                                            
                                            tag_count += guessedTags.size();
                                        }
                                    }
                                }
                                
                                SolrInputField tokenCountField = new SolrInputField(tokenCountFieldName + "_" + next);
                                tokenCountField.addValue(new Integer(words.length), 1.0f);
                                doc.put(tokenCountFieldName + "_" + next, tokenCountField);
                                    
                                SolrInputField matchCountField = new SolrInputField(matchCountFieldName + "_" + next);
                                matchCountField.addValue(new Integer(match_count), 1.0f);
                                doc.put(matchCountFieldName + "_" + next, matchCountField);
                                    
                                SolrInputField correctCountField = new SolrInputField(correctCountFieldName + "_" + next);
                                correctCountField.addValue(new Integer(correct_count), 1.0f);
                                doc.put(correctCountFieldName + "_" + next, correctCountField);
                                    
                                SolrInputField tagCountField = new SolrInputField(tagCountFieldName + "_" + next);
                                tagCountField.addValue(new Integer(tag_count), 1.0f);
                                doc.put(tagCountFieldName + "_" + next, tagCountField);
                            }
                        }
                    }
                }
				
				super.processAdd(cmd);
			}
		};
	}
}
