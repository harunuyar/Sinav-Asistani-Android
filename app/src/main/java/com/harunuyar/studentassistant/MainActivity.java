package com.harunuyar.studentassistant;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.harunuyar.studentassistant.CsvHelper.CsvReader;
import com.harunuyar.studentassistant.CsvHelper.CsvWriter;
import com.harunuyar.studentassistant.Notifier.Bildirim;
import com.harunuyar.studentassistant.Notifier.BildirimNotifier;
import com.harunuyar.studentassistant.Notifier.Notifier;
import com.harunuyar.studentassistant.Receiver.BildirimReceiver;
import com.harunuyar.studentassistant.ÖsymHelper.ÖsymTask;
import com.harunuyar.studentassistant.ÖsymHelper.Exam;
import com.harunuyar.studentassistant.ÖsymHelper.ÖsymAdapter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Exam> allExams, selectedExams, userCreatedExams;
    private ListView listView;
    private Button buttonListele;
    private ImageButton buttonHelp, buttonAdd, buttonCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();

            try {
                int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = 0x80000000;
                window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                Class<?> cls = window.getClass();
                Method method = cls.getDeclaredMethod("setStatusBarColor", new Class<?>[] { Integer.TYPE });
                method.invoke(window, Color.rgb(51,181,229));

            } catch (Exception e) { }
        }

        listView = (ListView) findViewById(R.id.listView);
        buttonListele = (Button) findViewById(R.id.buttonListele);
        buttonHelp = (ImageButton) findViewById(R.id.helpButton);
        buttonAdd = (ImageButton) findViewById(R.id.addButton);
        buttonCheck = (ImageButton) findViewById(R.id.checkButton);

        listSelectedExams();
        setOnClickListeners();

        setScheduler(this);
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

    private void listSelectedExams(){
        selectedExams = new ArrayList<>();
        userCreatedExams = new ArrayList<>();

        try {
            CsvReader csvReader = new CsvReader("selected.csv", this);

            String[] s;

            while ((s = csvReader.readNext()) != null) {
                Exam e = new Exam(s[0], s[1], s[2], s[3], s[4]);
                e.setSelected(true);
                selectedExams.add(e);
            }
        }
        catch (Exception ex) { }

        try {
            CsvReader csvReader = new CsvReader("user_created.csv", MainActivity.this);

            String[] s;

            while ((s = csvReader.readNext()) != null) {
                Exam e = new Exam(s[0], s[1], s[2], s[3], s[4]);
                e.setSelected(s[5].equals("true"));
                e.setUserCreated(true);
                userCreatedExams.add(e);
            }
        }
        catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Hoşgeldiniz.", Toast.LENGTH_SHORT).show();
        }

        allExams = new ArrayList<>();
        allExams.addAll(userCreatedExams);
        allExams.addAll(selectedExams);

        ÖsymAdapter adapter = new ÖsymAdapter(MainActivity.this, allExams);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
    }

    public static void saveSelectedExams(Context context, ArrayList<Exam> al) throws IOException {
        CsvWriter csvWriter = new CsvWriter("selected.csv",
                new String[]{"Sınav Adı", "Sınav Tarihi", "İlk Başvuru", "Son Başvuru", "Sonuç Tarihi"},
                context);

        for (Exam e : al){
            csvWriter.write(new String[]{e.getAd(), e.getSınavTarihi(), e.getBaşvuruTarihiFirst(),
                    e.getBaşvuruTarihiLast(), e.getSonuçTarihi()}, context);
        }
        csvWriter.close();
    }

    public static void saveUserCreatedExams(Context context, ArrayList<Exam> al) throws IOException {
        CsvWriter csvWriter = new CsvWriter("user_created.csv",
                new String[]{"Sınav Adı", "Sınav Tarihi", "İlk Başvuru", "Son Başvuru", "Sonuç Tarihi","Seçili"},
                context);

        for (Exam e : al){
            csvWriter.write(new String[]{e.getAd(), e.getSınavTarihi(), e.getBaşvuruTarihiFirst(),
                    e.getBaşvuruTarihiLast(), e.getSonuçTarihi(), (e.isSelected() ? "true" : "false")}, context);
        }
        csvWriter.close();
    }

    private void setOnClickListeners(){
        buttonListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ÖsymTask task = new ÖsymTask();
                    allExams = new ArrayList<>();

                    allExams.addAll(userCreatedExams);

                    ArrayList<Exam> osymExams = task.execute().get();

                    if (osymExams == null)
                        throw new Exception("Bağlantı sorunu.");

                    allExams.addAll(osymExams);

                    for (Exam e : selectedExams){
                        for (Exam e2 : allExams){
                            if (e.equals(e2)){
                                e2.setSelected(true);
                                break;
                            }
                        }
                    }

                    ÖsymAdapter adapter = new ÖsymAdapter(MainActivity.this, allExams);
                    listView.setAdapter(adapter);
                    listView.setTextFilterEnabled(true);
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.exam_dialog);

                Button button2 = (Button) dialog.findViewById(R.id.button2);
                Button button3 = (Button) dialog.findViewById(R.id.button3);
                TextView title = (TextView) dialog.findViewById(R.id.alertTitle);
                final EditText ad = (EditText) dialog.findViewById(R.id.nameEditText);
                final EditText first = (EditText) dialog.findViewById(R.id.FirstEditText);
                final EditText last = (EditText) dialog.findViewById(R.id.LastEditText);
                final EditText tarih = (EditText) dialog.findViewById(R.id.sınavEditText);
                final EditText sonuç = (EditText) dialog.findViewById(R.id.sonuçEditText);

                button2.setText("EKLE");
                button3.setText("İPTAL");
                title.setText("Sınav Ekle");
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);

                title.setTextColor(Color.WHITE);
                button2.setTextColor(Color.rgb(51,181,229));
                button3.setTextColor(Color.rgb(51,181,229));
                dialog.findViewById(R.id.topPanel).setBackgroundColor(Color.rgb(51,181,229));
                dialog.findViewById(R.id.parentPanel).setBackgroundColor(Color.WHITE);

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!ad.getText().toString().equals("")) {
                            Exam exam = new Exam(ad.getText().toString(),
                                    first.getText().toString(),
                                    last.getText().toString(),
                                    tarih.getText().toString(),
                                    sonuç.getText().toString());

                            exam.setUserCreated(true);
                            userCreatedExams.add(exam);

                            try {
                                saveUserCreatedExams(MainActivity.this, userCreatedExams);
                            } catch (IOException e) { }

                            for (int i=0; i<allExams.size(); i++){
                                if (!allExams.get(i).isUserCreated()){
                                    allExams.add(i, exam);
                                    break;
                                }
                            }

                            ÖsymAdapter adapter = (ÖsymAdapter) listView.getAdapter();
                            adapter.setArrayList(allExams);
                            adapter.notifyDataSetChanged();

                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Sınav adı boş bırakılamaz.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BildirimReceiver().onReceive(MainActivity.this, new Intent("MANUAL"));
            }
        });
    }

}
