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

//import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.AddUpdateCommand;

public class WhereDifferentUPF extends UpdateRequestProcessorFactory {
    private static final String TAG_PARAM = "datatags";
    private String tagFieldName;

	public static final String DELIMITER = " ";
	public static final String POS_PARAM = "pos";
	public static final String COMPARE_PARAM = "compare";
	public static final String DIFFERENT_PARAM = "different";
	public static final String CHANGE_PARAM = "change";
	public static final String DIFF_DELIM_PARAM = "diffDelim";
  
	private String posFieldName = null;
	private String compareFieldName = null;
	private String differentFieldName = null;
	private String changeFieldName = null;
	private String diffDelim = null;
  
	public void init(NamedList args) {
		Object o = args.remove(POS_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    posFieldName = o.toString();
		}
		
		o = args.remove(COMPARE_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    compareFieldName = o.toString();
		}
		
		o = args.remove(DIFFERENT_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    differentFieldName = o.toString();
		}
		
		o = args.remove(CHANGE_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    changeFieldName = o.toString();
		}
		
		o = args.remove(TAG_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    tagFieldName = o.toString();
		}
		
		o = args.remove(DIFF_DELIM_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    diffDelim = o.toString();
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
                        
				        if (doc.containsKey(posFieldName + "_" + next) && doc.containsKey(compareFieldName + "_" + next)) {
                            String posFieldValue = (String)doc.getFieldValue(posFieldName + "_" + next);
                            String[] pos = posFieldValue.split("\\s+");
				            String compareFieldValue = (String)doc.getFieldValue(compareFieldName + "_" + next);
				            String[] compare = compareFieldValue.split("\\s+");

				            if (compare.length == pos.length && compare.length > 0) {
				                //Pattern oneTag = Pattern.compile("\\[?([^\\]]+)\\]?");
				                Pattern splitter = Pattern.compile("\\]\\[");
                                StringBuffer sbDiff = new StringBuffer();
                                StringBuffer sbChange = new StringBuffer();
                                for (int i=0; i<compare.length; i++) {
                                    sbDiff.append(pos[i]);
                                    String tags = compare[i].substring(compare[i].indexOf('|')+1);
                                    if (tags.charAt(0) == '[') {
                                        tags = tags.substring(1, tags.length()-1); //strip [ and ]
                                    }
                                    
                                    //Matcher m = oneTag.matcher(tags);
                                    //if (m.matches()) {
                                    
                                    String[] tagList = splitter.split(tags);
                                    String posRef = pos[i].substring(pos[i].indexOf('|')+1);
                                    boolean match = false;
                                    for (int k=0; k<tagList.length; k++) {
                                        //String tag = m.group(1); //tags.substring(1, tags.length()-1);
                                        //if (!tag.equals(pos[i].substring(pos[i].indexOf('|')+1))) {
                                        if (tagList[k].equals(posRef)) {
                                            match = true;
                                            break;
                                        }
                                    }
                                    
                                    if (!match) {
                                        sbDiff.append(diffDelim);
                                        sbDiff.append(StringUtils.join(tagList, "~"));
                                    }
                                    /*
                                            sbDiff.append(diffDelim);
                                            sbDiff.append(tag);
                                            sbChange.append(pos[i].substring(0, pos[i].indexOf('|')));
                                            sbChange.append(diffDelim);
                                            sbChange.append(tag);
                                        }
                                        else {
                                            sbChange.append(pos[i]);
                                        }
                                    }
                                    else {
                                        sbChange.append(pos[i]);
                                    }
                                    */
                                    sbDiff.append(' ');
                                    sbChange.append(' ');
                                }
                                sbDiff.deleteCharAt(sbDiff.length()-1); //remove final space  
                                sbChange.deleteCharAt(sbChange.length()-1); //remove final space
                                
                                if (differentFieldName != null) {
                                    SolrInputField differentField = new SolrInputField(differentFieldName + "_" + next);
                                    differentField.setValue(sbDiff.toString(), 1.0f);
                                    doc.put(differentFieldName + "_" + next, differentField);
                                }
                                
                                if (changeFieldName != null) {
                                    SolrInputField changeField = new SolrInputField(changeFieldName + "_" + next);
                                    changeField.setValue(sbChange.toString(), 1.0f);
                                    doc.put(changeFieldName + "_" + next, changeField);
                                }
				            }
				        }
                    }
                }
				
				super.processAdd(cmd);
			}
		};
	}
}
