package com.calypso.pedometer.application;

import android.app.Application;
import android.util.Log;

import com.calypso.pedometer.BuildConfig;
import com.calypso.pedometer.greendao.GreenDaoManager;
import com.calypso.pedometer.utils.FakeCrashLibrary;
import com.calypso.pedometer.utils.Timber;


public class PedometerApplication extends Application {

    private static PedometerApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        GreenDaoManager.getInstance();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    public static PedometerApplication getInstance() {
        return instance;
    }
    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }
}
