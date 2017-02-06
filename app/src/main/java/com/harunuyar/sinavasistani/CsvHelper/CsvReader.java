package com.harunuyar.sinavasistani.CsvHelper;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvReader {

    private BufferedReader br;
    public CsvReader(String csvFile, Context context) throws IOException {
        InputStream inputStream = context.openFileInput(csvFile);

        if ( inputStream != null ) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            br = new BufferedReader(inputStreamReader);
            readNext();
        }
    }
    
    public String[] readNext() throws IOException{
        String line = br.readLine();
        if (line == null)
            return null;
        return parse(line);
    }
    
    private String[] parse(String str){
        ArrayList<String> al = new ArrayList();
        
        char[] c = str.toCharArray();
        int i = 0;
        
        while(true){
            StringBuilder sb = new StringBuilder();
            if(i != c.length && c[i] == '\"'){
                i++;
                while(c[i]!='\"'){
                    sb.append(c[i]);
                    i++;
                }
                i++;
            }
            else{
                while(i != c.length){
                    if (c[i]==',')
                        break;
                    sb.append(c[i]);
                    i++;
                }
            }
            al.add(sb.toString());
            if(i==c.length)
                break;
            i++;
        }
        
        return al.toArray(new String[0]);
    }
}
