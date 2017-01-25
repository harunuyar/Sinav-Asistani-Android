package com.harunuyar.studentassistant.Scheduler;

import com.harunuyar.studentassistant.Notifier.Bildirim;
import com.harunuyar.studentassistant.Notifier.Notifier;
import com.harunuyar.studentassistant.ÖsymHelper.Exam;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by Harun on 25.01.2017.
 */

public class MyTimerTask extends TimerTask {

    private Notifier notifier;
    private ArrayList<Exam> al;

    public MyTimerTask(Notifier notifier, ArrayList<Exam> al){
        this.al = al;
        this.notifier = notifier;
    }

    @Override
    public void run(){
        for (Exam e : al) {
            notifyIfClose(e);
        }
        System.out.println("Kontrol edildi.");
    }


    public void notifyIfClose(Exam exam){
        if (notifier == null)
            return;

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date d1 = new SimpleDateFormat("dd.MM.yyyy").parse(dateFormat.format(new Date()));
            Date dateExam = new SimpleDateFormat("dd.MM.yyyy").parse(exam.getSınavTarihi());
            Date dateFirst = new SimpleDateFormat("dd.MM.yyyy").parse(exam.getBaşvuruTarihiFirst());
            Date dateLast = new SimpleDateFormat("dd.MM.yyyy").parse(exam.getBaşvuruTarihiLast());
            Date dateResult = new SimpleDateFormat("dd.MM.yyyy").parse(exam.getSonuçTarihi());

            long diffExam = dateExam.getTime() - d1.getTime();
            diffExam /= (24 * 60 * 60 * 1000);

            long diffFirst = dateFirst.getTime() - d1.getTime();
            diffFirst /= (24 * 60 * 60 * 1000);

            long diffLast = dateLast.getTime() - d1.getTime();
            diffLast /= (24 * 60 * 60 * 1000);

            long diffResult = dateResult.getTime() - d1.getTime();
            diffResult /= (24 * 60 * 60 * 1000);

            if (diffExam == 1 && notifier.isaDayAgo()){
                String text = "Yarın sınavınız var!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else if (diffExam == 7 && notifier.isaWeekAgo()){
                String text = "Haftaya sınavınız var!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            if (diffFirst == 1 && notifier.isaDayAgo()){
                String text = "Yarın sınav başvuruları başlıyor!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else if (diffFirst == 7 && notifier.isaWeekAgo())
            {
                String text = "Haftaya sınav başvuruları başlıyor!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            if (diffLast == 1 && notifier.isaDayAgo()){
                String text = "Yarın sınav başvuruları sona eriyor!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else if (diffLast == 7 && notifier.isaWeekAgo())
            {
                String text = "Haftaya sınav başvuruları sona eriyor!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            if (diffResult == 1 && notifier.isaDayAgo()){
                String text = "Yarın sınav sonuçları açıklanıyor!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else if (diffResult == 7 && notifier.isaWeekAgo())
            {
                String text = "Haftaya sınav sonuçları açıklanıyor!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
