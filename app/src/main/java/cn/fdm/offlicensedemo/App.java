package cn.fdm.offlicensedemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
