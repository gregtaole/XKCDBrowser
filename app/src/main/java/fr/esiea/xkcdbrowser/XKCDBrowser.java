package fr.esiea.xkcdbrowser;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class XKCDBrowser extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
