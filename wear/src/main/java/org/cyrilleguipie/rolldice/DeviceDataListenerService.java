package org.cyrilleguipie.rolldice;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class DeviceDataListenerService extends WearableListenerService {
    private static final String START_ACTIVITY_PATH = "/RollDice/wear";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if(messageEvent.getPath().equals(START_ACTIVITY_PATH)) {
            Intent watchIntent = new Intent(this, MenuActivity.class);
            watchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(watchIntent);
        }
    }
}