package cnu.mobilesoftware.smartscheduler;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.id.list;
import static android.R.id.mask;

/**
 * Created by ymc12 on 2016-11-12.
 */

public class WebDBHelper {
    ArrayList<HashMap<String, String>> list;
    String temp;
    public WebDBHelper(){
        list = new ArrayList<HashMap<String,String>>();
    }
    public void getUserGroup(String uuid){//all
        class getUserGroupTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try{
                    String link = "http://52.79.193.88/SmartScheduler/UserGroup.php";
                    String uuid = (String)params[0];
                    String data = null;
                    if(!uuid.equals("all"))
                        data  = URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(uuid, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    if(!uuid.equals("all")) {
                        wr.write(data);
                        wr.flush();
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine()) != null)
                        sb.append(line);
                    reader.close();
                    return sb.toString();
                }catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
        getUserGroupTask task = new getUserGroupTask();
        task.execute(uuid);
    }
    public void getUserPlan(String uuid){
        class getUserPlanTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try{
                    String link = "http://52.79.193.88/SmartScheduler/UserPlan.php";
                    String uuid = (String)params[0];
                    String data = null;
                    if(!uuid.equals("all"))
                        data  = URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(uuid, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine()) != null)
                        sb.append(line);
                    reader.close();
                    return sb.toString();
                }catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
        getUserPlanTask task = new getUserPlanTask();
        task.execute(uuid);
    }
    public void insertUserPlan(String uuid, String week, String plan){
        class insertUserPlanTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try{
                    String uuid = (String)params[0];
                    String week = (String)params[1];
                    String plan = (String)params[2];
                    String link="http://52.79.193.88/SmartScheduler/AddUser.php";
                    String data  = URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(uuid, "UTF-8");
                    data  += "&" + URLEncoder.encode("week", "UTF-8") + "=" + URLEncoder.encode(week, "UTF-8");
                    data  += "&" + URLEncoder.encode("plan", "UTF-8") + "=" + URLEncoder.encode(plan, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine()) != null)
                        sb.append(line);

                    return sb.toString();
                }catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        insertUserPlanTask task = new insertUserPlanTask();
        task.execute(uuid, week, plan);
    }
    public void insertGroupInfo(String uuid, String groupid){
        class insertGroupInfoTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try{
                    String uuid = (String)params[0];
                    String groupid = (String)params[1];
                    String link="http://52.79.193.88/SmartScheduler/AddGroup.php";
                    String data  = URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(uuid, "UTF-8");
                    data  += "&" + URLEncoder.encode("groupid", "UTF-8") + "=" + URLEncoder.encode(groupid, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine()) != null)
                        sb.append(line);

                    return sb.toString();
                }catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        insertGroupInfoTask task = new insertGroupInfoTask();
        task.execute(uuid, groupid);
    }
    private String convert(String num){
        int bin = Integer.valueOf(num, 16);
        return Integer.toBinaryString(bin);
    }
}
