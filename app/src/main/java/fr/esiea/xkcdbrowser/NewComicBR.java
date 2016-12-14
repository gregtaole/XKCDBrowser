package fr.esiea.xkcdbrowser;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NewComicBR extends BroadcastReceiver {
    private final String TAG = "NewComicBR";

    public NewComicBR() {
        Log.d(TAG, "constructor");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_menu_fav)
                .setContentTitle("XKCDBrowser")
                .setContentText("New comic(s)!");

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(0, mBuilder.build());
    }
}
