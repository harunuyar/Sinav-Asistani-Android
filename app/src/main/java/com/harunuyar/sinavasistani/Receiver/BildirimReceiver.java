package com.harunuyar.sinavasistani.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.harunuyar.sinavasistani.Constants;
import com.harunuyar.sinavasistani.Notifier.Bildirim;
import com.harunuyar.sinavasistani.Notifier.BildirimNotifier;
import com.harunuyar.sinavasistani.Notifier.Notifier;
import com.harunuyar.sinavasistani.ÖsymHelper.Exam;
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
    ArrayList<Exam> examsToBeNotified;

    @Override
    public void onReceive(Context context, Intent intent) {

        Constants.loadNotificationDetails(context);

        notifier = new BildirimNotifier(context);
        examsToBeNotified = new ArrayList<>();

        examsToBeNotified.addAll(Constants.loadSelectedExams(context));
        for (Exam e : Constants.loadUserCreatedExams(context)) {
            if (e.isSelected()) {
                examsToBeNotified.add(e);
            }
        }

        String controlMessage = Constants.MANUAL_INTENT.equals(intent.getAction()) ? "Manuel kontrol yapıldı.\n" : "Otomatik kontrol yapıldı.\n";
        Toast.makeText(context, controlMessage + examsToBeNotified.size() + " sınav kontrol edildi.", Toast.LENGTH_SHORT).show();

        for (Exam e : examsToBeNotified){
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

            if (diffExam == 1 && Constants.NOTIFY_A_DAY_AGO){
                String text = "Yarın sınavınız var!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else if (diffExam == 7 && Constants.NOTIFY_A_WEEK_AGO){
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

            if (diffFirst == 1 && Constants.NOTIFY_A_DAY_AGO){
                String text = "Yarın sınav başvuruları başlıyor!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else if (diffFirst == 7 && Constants.NOTIFY_A_WEEK_AGO)
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

            if (diffLast == 1 && Constants.NOTIFY_A_DAY_AGO){
                String text = "Yarın sınav başvuruları sona eriyor!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else if (diffLast == 7 && Constants.NOTIFY_A_WEEK_AGO)
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

            if (diffResult == 1 && Constants.NOTIFY_A_DAY_AGO){
                String text = "Yarın sınav sonuçları açıklanıyor!";

                try {
                    notifier.notifyUser(new Bildirim(new String[]{text, exam.getAd()}));
                    System.out.println(text);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else if (diffResult == 7 && Constants.NOTIFY_A_WEEK_AGO)
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
