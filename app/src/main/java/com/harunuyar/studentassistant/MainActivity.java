package com.harunuyar.studentassistant;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.harunuyar.studentassistant.Receiver.BildirimReceiver;
import com.harunuyar.studentassistant.ÖsymHelper.ÖsymTask;
import com.harunuyar.studentassistant.ÖsymHelper.Exam;
import com.harunuyar.studentassistant.ÖsymHelper.ÖsymAdapter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button buttonListele;
    private ImageButton buttonHelp, buttonAdd, buttonCheck, buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constants.setStatusBarColor(getWindow());

        listView = (ListView) findViewById(R.id.listView);
        buttonListele = (Button) findViewById(R.id.buttonListele);
        buttonHelp = (ImageButton) findViewById(R.id.helpButton);
        buttonAdd = (ImageButton) findViewById(R.id.addButton);
        buttonCheck = (ImageButton) findViewById(R.id.checkButton);
        buttonSettings = (ImageButton) findViewById(R.id.settingsButton);

        Constants.listSavedExams(this, listView);

        setOnClickListeners();
        Constants.setScheduler(this);
    }

    private void setOnClickListeners(){
        buttonListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ÖsymTask task = new ÖsymTask();
                    ArrayList<Exam> ösymExams = task.execute().get();

                    if (ösymExams == null)
                        throw new Exception("Bağlantı sorunu.");

                    ArrayList<Exam> al = Constants.loadUserCreatedExams(MainActivity.this);
                    al.addAll(ösymExams);

                    for (Exam aSelectedExam : Constants.loadSelectedExams(MainActivity.this)){
                        for (Exam examInTheList : al){
                            if (aSelectedExam.equals(examInTheList)){
                                examInTheList.setSelected(true);
                                break;
                            }
                        }
                    }

                    ÖsymAdapter adapter = new ÖsymAdapter(MainActivity.this, al);
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
                final ÖsymAdapter adapter = (ÖsymAdapter) listView.getAdapter();
                final ArrayList<Exam> allExams = adapter.getArrayList();

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
                            exam.setSelected(false);

                            boolean eklendi = false;
                            for (int i=0; i<allExams.size(); i++){
                                if (!allExams.get(i).isUserCreated()){
                                    allExams.add(i, exam);
                                    eklendi = true;
                                    break;
                                }
                            }
                            if (!eklendi){
                                allExams.add(allExams.size(), exam);
                            }

                            try {
                                Constants.saveUserCreatedExams(MainActivity.this, allExams);
                            } catch (IOException e) { }

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
                new BildirimReceiver().onReceive(MainActivity.this, new Intent(Constants.MANUAL_INTENT));
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.loadNotificationDetails(MainActivity.this);

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.settings_dialog);

                Button button2 = (Button) dialog.findViewById(R.id.button2);
                Button button3 = (Button) dialog.findViewById(R.id.button3);
                TextView title = (TextView) dialog.findViewById(R.id.alertTitle);
                final CheckBox checkBoxDay = (CheckBox) dialog.findViewById(R.id.checkBoxDay);
                final CheckBox checkBoxWeek = (CheckBox) dialog.findViewById(R.id.checkBoxWeek);

                button2.setText("KAYDET");
                button3.setText("İPTAL");
                title.setText("Ayarlar");
                checkBoxDay.setChecked(Constants.NOTIFY_A_DAY_AGO);
                checkBoxWeek.setChecked(Constants.NOTIFY_A_WEEK_AGO);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Constants.saveNotificationDetails(MainActivity.this, checkBoxDay.isChecked(), checkBoxWeek.isChecked());
                            dialog.dismiss();
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, "Ayarlar kaydedilemedi.", Toast.LENGTH_SHORT).show();
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
    }

}
