package com.harunuyar.studentassistant;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Timer;
import com.harunuyar.studentassistant.CsvHelper.CsvReader;
import com.harunuyar.studentassistant.Notifier.BildirimNotifier;
import com.harunuyar.studentassistant.Notifier.Notifier;
import com.harunuyar.studentassistant.Scheduler.MyTimerTask;
import com.harunuyar.studentassistant.ÖsymHelper.Exam;

/**
 * Created by Harun on 25.01.2017.
 */

public class StudentAssistantService extends Service {

    private Timer timer;
    private MyTimerTask myTimerTask;
    private Notifier notifier;
    private ArrayList<Exam> selectedExams;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notifier = new BildirimNotifier(true, true, this);
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
        catch (Exception ex) { }

        setScheduler();
    }

    public void setScheduler(){
        if(timer != null){
            timer.cancel();
        }

        timer = new Timer();
        myTimerTask = new MyTimerTask(notifier, selectedExams);
        timer.schedule(myTimerTask, 1000, 1000*60*60*24);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Student Assistant Service Started", Toast.LENGTH_LONG).show();
        setScheduler();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Student Assistant Service Destroyed", Toast.LENGTH_LONG).show();
        if(timer != null){
            timer.cancel();
        }
    }
}
