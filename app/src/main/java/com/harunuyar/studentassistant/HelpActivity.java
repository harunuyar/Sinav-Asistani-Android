package com.harunuyar.studentassistant;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Method;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

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

        TextView help = (TextView) findViewById(R.id.textViewHelp);

        String str =
                "• Sınav Asistanı, sınavlarınız için bir hatırlatma aracıdır.\n" +
                        "• Sınavların başvuru tarihlerini, sınav tarihlerini ve sonuç tarihlerini bir hafta ve bir gün önceden bildirim yoluyla haber verir.\n" +
                        "• Program, ÖSYM'nin sitesine bağlanıp sınavları ve tarihlerini güncel olarak alır ve listeler.\n" +
                        "• Bildirim almak istediğiniz sınavları seçtikten sonra seçiminizi kaydetmeniz, bildirim almanız için yeterlidir.\n" +
                        "\n\n" +
                        "Harun Uyar";

        help.setText(str);

        ImageButton back = (ImageButton)findViewById(R.id.imageButtonBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
