/*
The contents of this file are subject to the AMP Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the AMP web site 
(http://www.tibet.iteso.mx/Guatemala/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is Andres Montano Pellegrini. Portions
created by Andres Montano Pellegrini are Copyright 2001 Andres Montano
Pellegrini. All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.util;

/** Used by {@link SimplifiedLinkedList} to provide the implementation of a
    simple dynamic link list.

    @author Andr&eacute;s Montano Pellegrini
    @see SimplifiedLinkedList
    @see SimplifiedListIterator
*/

public class Link
{
	private Object obj;
	public Link siguiente;

	public Link(Object obj)
	{
		this.obj = obj;
		siguiente = null;
	}

	public String toString()
	{
		return obj.toString();
	}

	public Link createNext(Object obj)
	{
		return siguiente = new Link(obj);
	}

	public Link createPrevious(Object obj)
	{
		Link l = new Link(obj);
		l.siguiente=this;
		return l;
	}

	public Object get()
	{
		return obj;
	}

	public Link next()
	{
		return this.siguiente;
	}
	public int size()
	{
		int n=0;
		Link actual = this;
		while (actual != null)
		{
			n++;
			actual = actual.next();
		}
		return n;
	}

	public Object clone()
	{
		return new Link(obj);
	}

 	public Link sort()
 	{
 		Link newCabeza = (Link) clone(), next = next(), newLink;
 		while (next!=null)
 		{
 			newLink = (Link) next.clone();
 			if (newLink.toString().compareTo(newCabeza.toString())<=0)
 			{
 				newLink.siguiente = newCabeza;
 				newCabeza = newLink;
 			}
 			else newCabeza.insertSorted(newLink);
                         next = next.next();
 		}
 		return newCabeza;
 	}

	public void insertSorted(Link link)
	{
		if (siguiente==null)
			siguiente = link;
		else
			if (link.toString().compareTo(siguiente.toString())<=0)
			{
				link.siguiente = siguiente;
				siguiente = link;
			}
			else siguiente.insertSorted(link);
	}

}
