package com.harunuyar.studentassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.harunuyar.studentassistant.CsvHelper.CsvReader;
import com.harunuyar.studentassistant.CsvHelper.CsvWriter;
import com.harunuyar.studentassistant.ÖsymHelper.CustomTask;
import com.harunuyar.studentassistant.ÖsymHelper.Exam;
import com.harunuyar.studentassistant.ÖsymHelper.ÖsymAdapter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Exam> allExams, selectedExams;
    private ListView listView;
    private Button buttonListele, buttonKaydet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        buttonListele = (Button) findViewById(R.id.buttonListele);
        buttonKaydet = (Button) findViewById(R.id.buttonKaydet);

        listSelectedExams();
        setOnClickListeners();

        startService();
    }

    public void startService() {
        startService(new Intent(getBaseContext(), StudentAssistantService.class));
    }

    public void stopService() {
        stopService(new Intent(getBaseContext(), StudentAssistantService.class));
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
                    CustomTask task = new CustomTask();
                    allExams = task.execute().get();

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
                    Toast.makeText(MainActivity.this, "Seçilen sınavlar kaydedildi.", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

                stopService();
                startService();
            }
        });
    }

}
