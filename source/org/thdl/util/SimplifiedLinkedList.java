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

import java.io.PrintWriter;

/** Implementation of a simple dynamic link list. Be careful with word order!
    Why not just use java.util.LinkedList? It is not supported for the
    handheld's virtual machine.

    @author Andr&eacute;s Montano Pellegrini
    @see Link
    @see SimplifiedListIterator
*/
public class SimplifiedLinkedList
{	
	protected Link cabeza;
	
	public SimplifiedLinkedList()
	{
		cabeza=null;
	}
	
	private SimplifiedLinkedList(Link cabeza)
	{
		this.cabeza = cabeza;
	}
	
	public void addLast(Object o)
	{
		if (cabeza==null)
		{
			cabeza = new Link(o);
		}
		else cabeza = cabeza.createPrevious(o);
	}
	
	public void addSorted(Comparable o)
	{
	    Link previous, currentLink, temp;
	    if (cabeza==null || o.compareTo(cabeza.get())<0)
	    {
	        addLast(o);
	    }	    
	    else
	    {
	        currentLink = cabeza;
	        do
	        {
                previous = currentLink;
                currentLink = currentLink.next();
	        } while ((currentLink != null) && (o.compareTo(currentLink.get())>=0));
	        temp = new Link (o);
	        previous.siguiente = temp;
	        temp.siguiente = currentLink;
        }
    }
    
    /** If the object is already in the list return. It is more efficient since it
        assumes that the list is sorted. */
    public Object getSorted(Comparable o)
    {
        Link currentLink = cabeza;
        Object currentObject;
        int comparison;
        
        while(currentLink != null)
        {
            currentObject = currentLink.get();
            comparison = ((Comparable) currentObject).compareTo(o);
            if (comparison==0) return currentObject;
            else if(comparison>0) return null;
            currentLink = currentLink.next();
        }
        return null;
    }

    /** Does not add repetitions. */
	public void addSortedUnique (Comparable o)
	{
	    Link previous, currentLink, temp;
	    int comp;
	    
	    if (cabeza==null || o.compareTo(cabeza.get())<0)
	    {
	        addLast(o);
	    }	    
	    else
	    {
	        if (o.compareTo(cabeza.get())==0) return;
	        currentLink = cabeza;
	        while (true)
	        {
                previous = currentLink;
                currentLink = currentLink.next();
                if (currentLink != null)
                {
                    comp = o.compareTo(currentLink.get());
                    if (comp==0) return;
                    else if (comp<0) break;
                }
                else break;
	        }
	        
	        temp = new Link (o);
	        previous.siguiente = temp;
	        temp.siguiente = currentLink;
        }
    }	
	
	public Object getLast()
	{
	    if (cabeza==null) return null;
		else return cabeza.get();
	}

	public SimplifiedListIterator listIterator()
	{
		return new SimplifiedListIterator(cabeza);
	}	

	public SimplifiedListIterator listIterator(int n)
	{
		return new SimplifiedListIterator(cabeza, n);
	}
	
	public boolean isEmpty()
	{
		return cabeza==null;
	}
	
	public boolean contains(Object o)
	{
		Link current=cabeza;
		while (current!=null)
		{
			if (current.get().equals(o)) return true;
			current = current.next();
		}
		return false;
	}
	
	public int size()
	{
		if (cabeza==null) return 0;
		return cabeza.size();
	}
	
	public SimplifiedLinkedList sort()
	{
		return new SimplifiedLinkedList(cabeza.sort());
	}
	
	public Object[] toArray()
	{
		int n = size();
		if (n==0) return null;
		Object array[] = new Object[n];
		SimplifiedListIterator li = listIterator();
		while (li.hasNext())
		{
			n--;
			array[n] = li.next();
		}
		return array;
	}
	
	public String[] toStringArray()
	{
		int n = size();
		if (n==0) return null;
		Object o;
		String array[] = new String[n];
		SimplifiedListIterator li = listIterator();
		while (li.hasNext())
		{
			n--;
			o = li.next();
			if (o==null) array[n]=null;
			else array[n] = o.toString();
		}
		return array;		
	}
	
	public String[] toReverseStringArray()
	{
		int n = size();
		if (n==0) return null;
		Object o;
		String array[] = new String[n];
		SimplifiedListIterator li = listIterator();
		n=0;
		while (li.hasNext())
		{
			o = li.next();
			if (o==null) array[n]=null;
			else array[n] = o.toString();
			n++;
		}
		return array;		
	}	
	
	public void write(PrintWriter pw)
	{
		SimplifiedListIterator li = listIterator();
		while (li.hasNext())
			pw.println(li.next());
	}
}
