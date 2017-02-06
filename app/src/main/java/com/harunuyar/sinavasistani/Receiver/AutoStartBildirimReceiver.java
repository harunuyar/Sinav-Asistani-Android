package com.harunuyar.sinavasistani.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.harunuyar.sinavasistani.Constants;

/**
 * Created by Harun on 30.01.2017.
 */

public class AutoStartBildirimReceiver extends BroadcastReceiver {
    private final String BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(BOOT_COMPLETED_ACTION)) {
            Constants.setScheduler(context);
        }
    }
}
