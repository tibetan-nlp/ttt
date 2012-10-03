package org.thdl.tib.scanner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

class FilterHTMLDictionary
{
	private BufferedReader in;
	private PrintWriter out[];
	private int currentLine;
	private String linea;
	
	private static final int GETTING_TERM=1;
	private static final int GETTING_DEFINITION=2;
							
	public FilterHTMLDictionary(BufferedReader in, PrintWriter out[])
	{
		this.in = in;
		this.out = out;
	}
	
	public static void main (String[] args) throws Exception
	{
		PrintWriter out[];
		BufferedReader in=null;
		int argNum = args.length, currentArg=0, i;
		String option;
		boolean file=false, split=false;
		
		if (argNum<=currentArg)
		{
		    System.out.println("Syntax: FilterHTMLDictionary [-split] [input-file] [output-file1] [output-file2]");
		    return;
		}
		
		if (args[currentArg].charAt(0)=='-')
		{
		    option = args[currentArg].substring(1);		        
		    currentArg++;
		    if (option.equals("split"))
		    {
		        split = true;
		    }
		}

		switch (args.length-currentArg)
		{
		case 0: out = new PrintWriter[1];
		        out[0] = new PrintWriter(System.out);
				in = new BufferedReader(new InputStreamReader(System.in));
				break;
		case 1: out = new PrintWriter[1];
		        out[0] = new PrintWriter(System.out);
				file = true;
				break;
		default:
		        if (split)
		        {
		            out = new PrintWriter[args.length-currentArg-1];
		            for (i=0; i<out.length; i++)
       	                out[i] = new PrintWriter(new FileOutputStream(args[currentArg + i + 1]));
       	        }
       	        else
       	        {
       	            out = new PrintWriter[1];
		            out[0] = new PrintWriter(new FileOutputStream(args[currentArg + 1]));
       	        }
		    	file = true;		
		}
			    
	    if (file)
		{
			if (args[currentArg].indexOf("http://") >= 0) 
				in = new BufferedReader(new InputStreamReader(new BufferedInputStream((new URL(args[currentArg])).openStream())));
			else 
				in = new BufferedReader(new InputStreamReader(new FileInputStream(args[currentArg])));
		}

		new FilterHTMLDictionary(in, out).run();
	}
	
	public void run() throws Exception
	{
		String lineaLC, term, definition="", modTerm;
		int status = GETTING_TERM, start, end, ps, pe;
		
		currentLine=0;
		while ((linea=in.readLine())!=null)
		{
			currentLine++;
		    linea = linea.trim();
		    lineaLC = linea.toLowerCase();
		    
		    switch (status)
		    {
		        case GETTING_TERM:

		            if (!lineaLC.startsWith("<p><b>")) reportProblem("<p><b> expected.");
        			
			        end = lineaLC.indexOf("</b>");
		            if (end<=0)
		            {
		                reportProblem("</b> expected.");
		                continue;
		            }
        			
			        term = linea.substring(6, end).trim().toLowerCase();
			        ps = term.indexOf('(');
			        if (ps<0) ps = term.indexOf('[');
			        if (ps>=0)
			        {
			            pe = term.indexOf(')', ps);
			            if (pe<0) pe = term.indexOf(']', ps);
			            definition = "<p>Original entry: " + term + ". ";
			            if (ps>0) modTerm = term.substring(0, ps);
			            else modTerm = "";
			            if (pe+1<term.length()) modTerm = modTerm + term.substring(pe+1);
			            term = modTerm;
			        }
			        else definition = "<p>";
			        
			        if (out.length==1) out[0].print(term + "\t");
			        else out[0].println(term.trim());

                    status = GETTING_DEFINITION;
			        definition = definition + linea.substring(end+4).trim();

			    continue;
			    case GETTING_DEFINITION:
			        if (lineaLC.equals(""))
			        {
			            if (out.length==1) out[0].println(definition);
			            else out[1].println(definition);
			            status = GETTING_TERM;
			        }
			        else definition = definition + " " + linea;
			}
		}
		
		for (start=0; start<out.length; start++)
            out[start].flush();
        System.out.flush();
	}
	
	private void reportProblem(String error)
	{
	    System.out.println("Error in line " + currentLine + ": " + linea + ". " + error);
	}
}