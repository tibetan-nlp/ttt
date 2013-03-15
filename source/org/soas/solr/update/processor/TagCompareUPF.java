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

import java.util.Collection;
import java.util.Iterator;

import java.io.IOException;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.AddUpdateCommand;

public class TagCompareUPF extends UpdateRequestProcessorFactory {
    private static final String TAG_PARAM = "datatags";
    private String tagFieldName;

	public static final String DELIMITER = " ";
	public static final String TAGS_PARAM = "tags";
	public static final String GUESS_PARAM = "guess";
	public static final String TOKEN_COUNT_PARAM = "tokenCount";
	public static final String GUESS_COUNT_PARAM = "guessCount";
	public static final String CORRECT_COUNT_PARAM = "correctCount";
  
	private String tagsFieldName = null;
	private String guessFieldName = null;
	private String tokenCountFieldName = null;
	private String guessCountFieldName = null;
	private String correctCountFieldName = null;
  
	public void init(NamedList args) {
		Object o = args.remove(TAGS_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    tagsFieldName = o.toString();
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
                
				        if (doc.containsKey(guessFieldName + "_" + next)) {
				            String guesses[] = ((String)doc.getField(guessFieldName + "_" + next).getValue()).split("\\s+");
        		    
                            if (doc.containsKey(tagsFieldName)) {
                                String tags[] = ((String)doc.getField(tagsFieldName).getValue()).split("\\s+");
                                
                                //should be an argument
                                char delimiter = '|';
                                
                                int correct = 0;
                                //need to make sure tags and guesses are same length
                                for (int i=0; i<tags.length; i++) {
                                    if (tags[i].substring(tags[i].indexOf(delimiter)+1).equals(guesses[i].substring(guesses[i].indexOf(delimiter)+1))) {
                                        correct++;
                                    }
                                }
                                
                                SolrInputField tokenCountField = new SolrInputField(tokenCountFieldName + "_" + next);
                                tokenCountField.addValue(new Integer(tags.length), 1.0f);
                                doc.put(tokenCountFieldName + "_" + next, tokenCountField);
                                
                                SolrInputField correctCountField = new SolrInputField(correctCountFieldName + "_" + next);
                                correctCountField.addValue(new Integer(correct), 1.0f);
                                doc.put(correctCountFieldName + "_" + next, correctCountField);
                            }
                    
                            SolrInputField guessCountField = new SolrInputField(guessCountFieldName + "_" + next);
                            guessCountField.addValue(new Integer(guesses.length), 1.0f);
                            doc.put(guessCountFieldName + "_" + next, guessCountField);
                        }
                    }
                }
				
				super.processAdd(cmd);
			}
		};
	}
}
