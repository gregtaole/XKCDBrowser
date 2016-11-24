package fr.esiea.xkcdbrowser;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by dinervoid on 11/24/16.
 */

public class XKCDBrowser extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
