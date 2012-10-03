/* This file is based on something from Apache's Xalan-J 2.4.0.  As is
   our right, we have licensed it under the THDL Open Community
   License.  We list the THDL as the "Initial Developer", because
   Apache doesn't want e-mail about this code.  But colloquially, one
   would say that Apache is the first developer. */

/*
The contents of this file are subject to the THDL Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the THDL web site 
(http://www.thdl.org/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is the Tibetan and Himalayan Digital
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xalan" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, Lotus
 * Development Corporation., http://www.lotus.com.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.thdl.util;


/**
 * A digital search trie for 7-bit ASCII text.  The API is a subset of
 * java.util.Hashtable.  The key must be a 7-bit ASCII string.  The
 * value may be any Java Object.  */
public class Trie
{

    /** Size of the m_nextChar array.  */
    public static final int ALPHA_SIZE = 161;

    /** The root node of the tree.    */
    Node m_Root;

    /**
     * Constructs a trie.
     */
    public Trie()
    {
        m_Root = new Node();
    }

    private static final boolean caseInsensitive = false;


    /**
     * Puts an object into the trie for lookup.
     *
     * @param key must be a 7-bit ASCII string
     * @param value any java object, but not null.
     *
     * @return The old object that matched key, or null.
     * @throws NullPointerException if value is null or if key is null
     */
    public Object put(String key, Object value)
    {
        if (null == value) throw new NullPointerException();

        final int len = key.length();
        Node node = m_Root;

        for (int i = 0; i < len; i++)
            {
                Node nextNode
                    = node.m_nextChar[(caseInsensitive
                                       ? Character.toUpperCase(key.charAt(i))
                                       : key.charAt(i))];

                if (nextNode != null)
                    {
                        node = nextNode;
                    }
                else
                    {
                        for (; i < len; i++)
                            {
                                Node newNode = new Node();

                                node.m_nextChar[(caseInsensitive
                                                 ? Character.toUpperCase(key.charAt(i))
                                                 : key.charAt(i))] = newNode;
                                node.m_isLeaf = false;
                                node = newNode;
                            }

                        break;
                    }
            }

        Object ret = node.m_Value;

        node.m_Value = value;

        return ret;
    }

    /**
     * Gets an object that matches the key.
     *
     * @param key must be a 7-bit ASCII string
     *
     * @return The object that matches the key, or null.
     */
    public Object get(String key)
    {

        final int len = key.length();
        Node node = m_Root;

        for (int i = 0; i < len; i++)
            {
                try
                    {
                        node = node.m_nextChar[(caseInsensitive
                                                ? Character.toUpperCase(key.charAt(i))
                                                : key.charAt(i))];
                    }
                catch (ArrayIndexOutOfBoundsException e)
                    {

                        // the key is not 7-bit ASCII so we won't find it here
                        node = null;

                        ThdlDebug.noteIffyCode();
                    }

                if (node == null)
                    return null;
            }

        return node.m_Value;
    }

    /**
     * Returns true if and only if key is a prefix of another, distinct
     * member of the trie.  Note that key may be in the trie as well as
     * a prefix of something in the trie, but not all keys in the trie
     * are prefixes of other keys.  (In the trie you might naturally
     * construct for the extended Wylie keyboard, "t" is a prefix, as is
     * "ts", but "tsh", though a member of the trie, is not a prefix
     * because there is no string x such that x begins with "tsh" but is
     * not equal to "tsh" that is in the trie.)
     *
     * @param key must be a 7-bit ASCII string
     *
     * @return The object that matches the key, or null.  */
    public boolean hasPrefix(String key)
    {

        final int len = key.length();
        Node node = m_Root;

        for (int i = 0; i < len; i++)
            {
                try
                    {
                        node = node.m_nextChar[(caseInsensitive
                                                ? Character.toUpperCase(key.charAt(i))
                                                : key.charAt(i))];
                    }
                catch (ArrayIndexOutOfBoundsException e)
                    {

                        // the key is not 7-bit ASCII so we won't find it here
                        node = null;

                    }

                if (node == null)
                    return false;
            }

        return !node.m_isLeaf;
    }

    /**
     * The node representation for the trie.
     */
    class Node
    {

        /**
         * Constructor, creates a Node[ALPHA_SIZE].
         */
        Node()
        {
            m_nextChar = new Node[ALPHA_SIZE];
            m_Value = null;
            m_isLeaf = true;
        }

        /** The next nodes.   */
        Node m_nextChar[];

        /** The value.   */
        Object m_Value;

        /** True if and only if this node has no children. */
        boolean m_isLeaf;
    }
}
