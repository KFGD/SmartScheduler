package cnu.mobilesoftware.smartscheduler;

import android.app.Application;
import android.content.Context;

/**
 * Created by GwanYongKim on 2016-11-07.
 */

public class SmartSchedulerApplication extends Application{
    static Context context;

    public SmartSchedulerApplication(){
        super();
        context = this;
    }

    public static Context getContext(){
        return context;
    }
}
