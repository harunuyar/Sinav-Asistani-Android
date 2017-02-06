package com.harunuyar.sinavasistani;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Constants.setStatusBarColor(getWindow());

        TextView help = (TextView) findViewById(R.id.textViewHelp);

        String str ="• Sınav Asistanı, sınavlarınız için bir hatırlatma aracıdır.\n" +
                        "• Sınavların başvuru tarihlerini, sınav tarihlerini ve sonuç tarihlerini bir hafta ve bir gün önceden bildirir.\n" +
                        "• ÖSYM'nin sitesine bağlanıp sınavları ve tarihlerini güncel olarak alabileceğiniz " +
                                                                "gibi kendi sınavlarınızı da ayrıca ekleyebilirsiniz.\n" +
                        "• Bildirim almak istediğiniz sınavları seçmeniz, bildirim almanız için yeterlidir.\n" +
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
