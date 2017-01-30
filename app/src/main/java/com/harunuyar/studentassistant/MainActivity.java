package com.harunuyar.studentassistant;

import android.app.AlarmManager;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.harunuyar.studentassistant.CsvHelper.CsvReader;
import com.harunuyar.studentassistant.CsvHelper.CsvWriter;
import com.harunuyar.studentassistant.Receiver.BildirimReceiver;
import com.harunuyar.studentassistant.ÖsymHelper.ÖsymTask;
import com.harunuyar.studentassistant.ÖsymHelper.Exam;
import com.harunuyar.studentassistant.ÖsymHelper.ÖsymAdapter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Exam> allExams, selectedExams;
    private ListView listView;
    private Button buttonListele, buttonKaydet;
    private ImageButton buttonHelp;

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
                Method method = cls.getDeclaredMethod("setStatusBarColor",
                        new Class<?>[] { Integer.TYPE });

                method.invoke(window, Color.rgb(51,181,229));

            } catch (Exception e) {

            }
        }

        listView = (ListView) findViewById(R.id.listView);
        buttonListele = (Button) findViewById(R.id.buttonListele);
        buttonKaydet = (Button) findViewById(R.id.buttonKaydet);
        buttonHelp = (ImageButton) findViewById(R.id.helpButton);

        listSelectedExams();
        setOnClickListeners();

        setScheduler();
    }

    private void setScheduler(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(getBaseContext(), BildirimReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void listSelectedExams(){
        selectedExams = new ArrayList<>();

        try {
            CsvReader csvReader = new CsvReader("sinavlar.csv", this);

            String[] s;

            while ((s = csvReader.readNext()) != null) {
                Exam e = new Exam(s[0], s[1], s[2], s[3], s[4]);
                e.setSelected(true);
                selectedExams.add(e);
            }
        }
        catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Hoşgeldiniz.", Toast.LENGTH_SHORT).show();
        }
        allExams = new ArrayList<>();
        allExams.addAll(selectedExams);

        ÖsymAdapter adapter = new ÖsymAdapter(MainActivity.this, allExams);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
    }

    private void setOnClickListeners(){
        buttonListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ÖsymTask task = new ÖsymTask();
                    allExams = task.execute().get();

                    if (allExams == null)
                        throw new Exception("Bağlantı sorunu.");

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

        buttonKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allExams = ((ÖsymAdapter) listView.getAdapter()).getArrayList();
                selectedExams = new ArrayList<>();

                try {
                    CsvWriter csvWriter = new CsvWriter("sinavlar.csv",
                            new String[]{"Sınav Adı", "Sınav Tarihi", "İlk Başvuru", "Son Başvuru", "Sonuç Tarihi"},
                            MainActivity.this);
                    for (Exam e : allExams) {
                        if (e.isSelected()) {
                            selectedExams.add(e);
                        }
                    }

                    for (Exam e : selectedExams){
                        csvWriter.write(new String[]{e.getAd(), e.getSınavTarihi(), e.getBaşvuruTarihiFirst(),
                                        e.getBaşvuruTarihiLast(), e.getSonuçTarihi()}, MainActivity.this);
                    }
                    csvWriter.close();
                    Toast.makeText(MainActivity.this, "Seçilen sınavlar kaydedildi.\nZamanı gelince bildirim alacaksınız.", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
    }

}
