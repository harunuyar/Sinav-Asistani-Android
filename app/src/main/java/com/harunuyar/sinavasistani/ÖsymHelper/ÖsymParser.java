package com.harunuyar.sinavasistani.ÖsymHelper;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class ÖsymParser {
    
    private static ÖsymParser parser = null;
    private final String link;

    private ÖsymParser() {
        link = "http://www.osym.gov.tr/TR,8797/takvim.html";
    }
    
    public ArrayList<Exam> getList() throws Exception{
        
        ArrayList<Exam> al = new ArrayList<>();
        
        try {
            for (Element e: Jsoup.connect(link).get().select("div.table > div.row")){
                String[] firstLast = e.select("div.col-sm-2").get(1).text().split(" ");
                String lastApp = (firstLast.length == 2) ? firstLast[1] : firstLast[2];
                Exam exam = new Exam(e.select("div.col-sm-6").first().text(),
                        e.select("div.col-sm-2").get(0).text().split(" ")[0],
                        e.select("div.col-sm-2").get(1).text().split(" ")[0],
                        lastApp,
                        e.select("div.col-sm-2").get(2).text().split(" ")[0]);
                exam.setUserCreated(false);
                exam.setSelected(false);
                al.add(exam);
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        return al;
    }
    
    public static ÖsymParser getParser(){ // Singleton
        if (parser == null)
            parser = new ÖsymParser();
        return parser;
    }
}
