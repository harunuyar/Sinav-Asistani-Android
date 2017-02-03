package com.harunuyar.studentassistant.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.harunuyar.studentassistant.CsvHelper.CsvReader;
import com.harunuyar.studentassistant.Notifier.Bildirim;
import com.harunuyar.studentassistant.Notifier.BildirimNotifier;
import com.harunuyar.studentassistant.Notifier.Notifier;
import com.harunuyar.studentassistant.ÖsymHelper.Exam;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Harun on 26.01.2017.
 */

public class BildirimReceiver extends BroadcastReceiver {
    Notifier notifier;
    ArrayList<Exam> selectedExams;

    @Override
    public void onReceive(Context context, Intent intent) {

        notifier = new BildirimNotifier(true, true, context);
        selectedExams = new ArrayList<>();

        try {
            CsvReader csvReader = new CsvReader("selected.csv", context);

            String[] s;

            while ((s = csvReader.readNext()) != null) {
                Exam e = new Exam(s[0], s[1], s[2], s[3], s[4]);
                e.setSelected(true);
                selectedExams.add(e);
            }
        }
        catch (Exception ex) { }

        try {
            CsvReader csvReader = new CsvReader("user_created.csv", context);

            String[] s;

            while ((s = csvReader.readNext()) != null) {
                Exam e = new Exam(s[0], s[1], s[2], s[3], s[4]);
                if (s[5].equals("true")) {
                    e.setSelected(true);
                    selectedExams.add(e);
                }
            }
        }
        catch (Exception ex) { }

        String string = intent.getAction().equals("MANUAL") ? "Manuel kontrol yapıldı.\n" : "Otomatik kontrol yapıldı.\n";
        Toast.makeText(context, string + selectedExams.size() + " sınav kontrol edildi.", Toast.LENGTH_SHORT).show();

        for (Exam e : selectedExams){
            notifyIfClose(e);
        }
    }

    private void notifyIfClose(Exam exam){
        if (notifier == null)
            return;

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date today = null;

        try {
            today = new SimpleDateFormat("dd.MM.yyyy").parse(dateFormat.format(new Date()));
        } catch (ParseException e) { }

        try {
            Date dateExam = new SimpleDateFormat("dd.MM.yyyy").parse(exam.getSınavTarihi());
            long diffExam = dateExam.getTime() - today.getTime();
            diffExam /= (24 * 60 * 60 * 1000);

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
        }
        catch (ParseException ex) { }

        try{
            Date dateFirst = new SimpleDateFormat("dd.MM.yyyy").parse(exam.getBaşvuruTarihiFirst());
            long diffFirst = dateFirst.getTime() - today.getTime();
            diffFirst /= (24 * 60 * 60 * 1000);

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
        }
        catch (ParseException ex) { }

        try{
            Date dateLast = new SimpleDateFormat("dd.MM.yyyy").parse(exam.getBaşvuruTarihiLast());
            long diffLast = dateLast.getTime() - today.getTime();
            diffLast /= (24 * 60 * 60 * 1000);

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
        }
        catch (ParseException ex) { }

        try{
            Date dateResult = new SimpleDateFormat("dd.MM.yyyy").parse(exam.getSonuçTarihi());
            long diffResult = dateResult.getTime() - today.getTime();
            diffResult /= (24 * 60 * 60 * 1000);

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
        catch (ParseException ex) { }
    }
}
