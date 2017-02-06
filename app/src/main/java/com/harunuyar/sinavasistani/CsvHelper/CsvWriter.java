package com.harunuyar.sinavasistani.CsvHelper;

import android.content.Context;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CsvWriter {

    private String fileName;
    private OutputStreamWriter outputStreamWriter;
    
    public CsvWriter(String fileName, String[] columns, Context context) throws IOException{
        outputStreamWriter = null;

        this.fileName = fileName;

        if (columns.length == 0){
            throw new IOException("En azından bir sütun adı olmalı.");
        }
        
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            write(columns, context);
	    }
        catch (IOException e) {

            if (outputStreamWriter != null)
                outputStreamWriter.close();

            throw e;
	    }
    }
    
    public void write(String[] contents, Context context) throws IOException{
        String s = "";
        if (contents.length == 0){
            outputStreamWriter.write("\n");
        }
        else{
            s += contents[0];
            for(int i=1; i<contents.length;i++){
                s += ","+contents[i];
            }
            s += '\n';
            outputStreamWriter.write(s);
        }
    }
    
    public void close() throws IOException{
        if (outputStreamWriter != null)
            outputStreamWriter.close();
    }

}
