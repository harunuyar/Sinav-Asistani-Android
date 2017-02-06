package com.harunuyar.sinavasistani;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;
import com.harunuyar.sinavasistani.CsvHelper.CsvReader;
import com.harunuyar.sinavasistani.CsvHelper.CsvWriter;
import com.harunuyar.sinavasistani.Receiver.BildirimReceiver;
import com.harunuyar.sinavasistani.ÖsymHelper.Exam;
import com.harunuyar.sinavasistani.ÖsymHelper.ÖsymAdapter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Harun on 4.02.2017.
 */

public abstract class Constants {

    public final static int MAINCOLOR = Color.rgb(51,181,229);
    public final static String SELECTED_EXAMS_FILE = "selected.csv";
    public final static String USER_CREATED_EXAMS_FILE = "user_created.csv";
    public final static String NOTIFICATON_DETAILS_FILE = "notifications.csv";
    public final static String TRUE_STRING = "true";
    public final static String FALSE_STRING = "false";
    public final static String[] SELECTED_EXAMS_COLUMNS = new String[]{"Sınav Adı", "Sınav Tarihi", "İlk Başvuru", "Son Başvuru", "Sonuç Tarihi"};
    public final static String[] USER_CREATED_EXAMS_COLUMNS = new String[]{"Sınav Adı", "Sınav Tarihi", "İlk Başvuru", "Son Başvuru", "Sonuç Tarihi", "Seçili"};
    public final static String[] NOTIFICATION_DETAILS_COLUMNS = new String[]{"Bir Gün Önce", "Bir Hafta Önce"};
    public final static String MANUAL_INTENT = "MANUAL_INTENT";
    public static boolean NOTIFY_A_WEEK_AGO = true;
    public static boolean NOTIFY_A_DAY_AGO = true;

    private Constants(){}

    public static void loadNotificationDetails(Context context) {
        try {
            CsvReader csvReader = new CsvReader(NOTIFICATON_DETAILS_FILE, context);

            String[] line = csvReader.readNext();

            NOTIFY_A_DAY_AGO = line[0].equals(TRUE_STRING);
            NOTIFY_A_WEEK_AGO = line[1].equals(TRUE_STRING);
        }
        catch (Exception e) {
            NOTIFY_A_DAY_AGO = true;
            NOTIFY_A_WEEK_AGO = true;
        }
    }

    public static void saveNotificationDetails(Context context, boolean aDayAgo, boolean aWeekAgo) throws IOException{
        CsvWriter csvWriter = new CsvWriter(NOTIFICATON_DETAILS_FILE, NOTIFICATION_DETAILS_COLUMNS, context);

        csvWriter.write(new String[]{aDayAgo ? TRUE_STRING : FALSE_STRING, aWeekAgo ? TRUE_STRING : FALSE_STRING}, context);

        csvWriter.close();

        NOTIFY_A_DAY_AGO = aDayAgo;
        NOTIFY_A_WEEK_AGO = aWeekAgo;
    }

    public static void setStatusBarColor(Window window){
        if (Build.VERSION.SDK_INT >= 21) {

            try {
                int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = 0x80000000;
                window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                Class<?> cls = window.getClass();
                Method method = cls.getDeclaredMethod("setStatusBarColor", new Class<?>[] { Integer.TYPE });
                method.invoke(window, MAINCOLOR);

            } catch (Exception e) { }
        }
    }

    public static void setScheduler(Context context){

        boolean alarmUp = (PendingIntent.getBroadcast(context, 1,
                new Intent(context, BildirimReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis())
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        long diff = calendar.getTimeInMillis() - System.currentTimeMillis();
        Toast.makeText(context, "Bir sonraki kontrol " + diff/(1000*60*60) + " saat " + (diff/(1000*60))%60 + " dakika sonra.", Toast.LENGTH_SHORT).show();

        if(!alarmUp) {
            Intent intent = new Intent(context, BildirimReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public static void listSavedExams(Context context, ListView listView) {

        ArrayList<Exam> allExams = new ArrayList<>();

        allExams.addAll(loadUserCreatedExams(context));
        allExams.addAll(loadSelectedExams(context));

        ÖsymAdapter adapter = new ÖsymAdapter(context, allExams);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
    }

    public static ArrayList<Exam> loadSelectedExams(Context context) {
        ArrayList<Exam> al = new ArrayList<>();

        try {
            CsvReader csvReader = new CsvReader(SELECTED_EXAMS_FILE, context);

            String[] s;

            while ((s = csvReader.readNext()) != null) {
                Exam e = new Exam(s[0], s[1], s[2], s[3], s[4]);
                e.setSelected(true);
                al.add(e);
            }
        }
        catch (IOException ex){}

        return al;
    }

    public static ArrayList<Exam> loadUserCreatedExams(Context context) {
        ArrayList<Exam> al = new ArrayList<>();

        try {
            CsvReader csvReader = new CsvReader(USER_CREATED_EXAMS_FILE, context);

            String[] s;

            while ((s = csvReader.readNext()) != null) {
                Exam e = new Exam(s[0], s[1], s[2], s[3], s[4]);
                e.setSelected(s[5].equals(TRUE_STRING));
                e.setUserCreated(true);
                al.add(e);
            }
        }
        catch (IOException ex){ }

        return al;
    }

    public static void saveSelectedExams(Context context, ArrayList<Exam> al) throws IOException {
        CsvWriter csvWriter = new CsvWriter(SELECTED_EXAMS_FILE, SELECTED_EXAMS_COLUMNS, context);

        for (Exam e : al){
            if (!e.isUserCreated() & e.isSelected()) {
                csvWriter.write(new String[]{e.getAd(), e.getSınavTarihi(), e.getBaşvuruTarihiFirst(),
                        e.getBaşvuruTarihiLast(), e.getSonuçTarihi()}, context);
            }
        }
        csvWriter.close();
    }

    public static void saveUserCreatedExams(Context context, ArrayList<Exam> al) throws IOException {
        CsvWriter csvWriter = new CsvWriter(USER_CREATED_EXAMS_FILE, USER_CREATED_EXAMS_COLUMNS, context);

        for (Exam e : al){
            if (e.isUserCreated()) {
                csvWriter.write(new String[]{e.getAd(), e.getSınavTarihi(), e.getBaşvuruTarihiFirst(),
                        e.getBaşvuruTarihiLast(), e.getSonuçTarihi(), (e.isSelected() ? TRUE_STRING : FALSE_STRING)}, context);
            }
        }
        csvWriter.close();
    }
}
