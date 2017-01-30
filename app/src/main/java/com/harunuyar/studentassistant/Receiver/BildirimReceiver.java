package com.harunuyar.studentassistant.Receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import java.util.Calendar;
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
            CsvReader csvReader = new CsvReader("sinavlar.csv", context);

            String[] s;

            while ((s = csvReader.readNext()) != null) {
                Exam e = new Exam(s[0], s[1], s[2], s[3], s[4]);
                e.setSelected(true);
                selectedExams.add(e);
            }

            Toast.makeText(context, "Sınav Asistanı:\n" + selectedExams.size() + " sınav kontrol edildi.", Toast.LENGTH_SHORT).show();
            //notifier.notifyUser(new Bildirim(new String[]{selectedExams.size() + " sınav kontrol edildi"}));
        }
        catch (Exception ex) { }

        for (Exam e : selectedExams){
            notifyIfClose(e);
        }
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
