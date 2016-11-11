package cnu.mobilesoftware.smartscheduler;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

/**
 * Created by GwanYongKim on 2016-11-07.
 */

public class SmartSchedulerApplication extends Application{
    private static Context mContext;
    private final static String DEVICE_UUID = "DEVICE_UUID";
    private static String mUUID = null;

    public SmartSchedulerApplication(){
        super();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    public static String getUUID(){
        return mUUID;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        setDeviceUUID();
        Log.i("info", mUUID);
    }

    private void setDeviceUUID(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String cachedDeviceID = sharedPreferences.getString(DEVICE_UUID, "");

        if("" == cachedDeviceID){
            final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            cachedDeviceID = deviceUuid.toString();
        }
        sharedPreferences.edit().putString(DEVICE_UUID, cachedDeviceID);
        mUUID = cachedDeviceID;
    }
}
