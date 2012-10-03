package org.thdl.tib.scanner;

/** Wrapping class
*/
class DictionaryRecord
{
    public String header, fileName, delimiter;
    public int delimiterType;
    
    public DictionaryRecord(String header, String fileName, int delimiterType, String delimiter)
    {
        this.header = header;
        this.fileName = fileName;
        this.delimiterType = delimiterType;
        this.delimiter = delimiter;
        
    }
}