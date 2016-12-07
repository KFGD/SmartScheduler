package cnu.mobilesoftware.smartscheduler;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ymc12 on 2016-11-12.
 */

public class WebDBHelper {

    String temp;
    public WebDBHelper(){}

    //MAKE GROUP
    public static synchronized StringBuilder MAKEGROUP(String paradeadline, String parauuid){
        StringBuilder stringBuilder = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try{

            String link="http://52.79.193.88/SmartScheduler/MAKEGROUP.php";
            String data  = URLEncoder.encode("deadline", "UTF-8") + "=" + URLEncoder.encode(paradeadline, "UTF-8");
            data  += "&" + URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(parauuid, "UTF-8");
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream wr = conn.getOutputStream();
            wr.write(data.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);
        }catch (Exception e){
            stringBuilder = null;
        }finally{
            if(conn != null)
                conn.disconnect();
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return stringBuilder;
    }

    public static synchronized StringBuilder SELECTWEBDB(String query, String paraid) {//all
        StringBuilder stringBuilder = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try{
            stringBuilder = new StringBuilder();
            String link = "http://52.79.193.88/SmartScheduler/"+query+".php";
            String id = paraid;
            String data = null;
            if(query.equals("SELECTUSERINFO")||query.equals("SELECTUSERPLAN")||query.equals("SELECTUSERGROUP"))
                data = URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            if(query.equals("SELECTGROUPINFO")||query.equals("SELECTBOARD"))
                data = URLEncoder.encode("groupid", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            Log.d("fuck", query);
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream wr = conn.getOutputStream();
            wr.write(data.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            Log.d("asd", "asd");
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);
        }catch (Exception e){
            stringBuilder = null;
        }finally{
            if(conn != null)
                conn.disconnect();
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return stringBuilder;
    }

    public static synchronized StringBuilder INSERTUSERINFO(String parauuid, String paraname){
        StringBuilder stringBuilder = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try{
            String uuid = parauuid;
            String name = paraname;
            String link="http://52.79.193.88/SmartScheduler/INSERTUSERINFO.php";
            String data  = URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(uuid, "UTF-8");
            data  += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream wr = conn.getOutputStream();
            wr.write(data.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);
        }catch (Exception e){
            stringBuilder = null;
        }finally{
            if(conn != null)
                conn.disconnect();
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return stringBuilder;
    }

    public static synchronized StringBuilder INSERTUSERPLAN(String parauuid, String paraweek, String paraplan){
        StringBuilder stringBuilder = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try{
            String uuid = parauuid;
            String week = paraweek;
            String plan = paraplan;
            String link="http://52.79.193.88/SmartScheduler/INSERTUSERPLAN.php";
            String data  = URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(uuid, "UTF-8");
            data  += "&" + URLEncoder.encode("week", "UTF-8") + "=" + URLEncoder.encode(week, "UTF-8");
            data  += "&" + URLEncoder.encode("plan", "UTF-8") + "=" + URLEncoder.encode(plan, "UTF-8");
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream wr = conn.getOutputStream();
            wr.write(data.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);
        }catch (Exception e){
            stringBuilder = null;
        }finally{
            if(conn != null)
                conn.disconnect();
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return stringBuilder;
    }

    public static synchronized StringBuilder INSERTUSERGROUP(String parauuid, String paragroupid){
        StringBuilder stringBuilder = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try{
            String uuid = parauuid;
            String groupid = paragroupid;
            String link="http://52.79.193.88/SmartScheduler/INSERTUSERGROUP.php";
            String data  = URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(uuid, "UTF-8");
            data  += "&" + URLEncoder.encode("groupid", "UTF-8") + "=" + URLEncoder.encode(groupid, "UTF-8");
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream wr = conn.getOutputStream();
            wr.write(data.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);
        }catch (Exception e){
            stringBuilder = null;
        }finally{
            if(conn != null)
                conn.disconnect();
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return stringBuilder;
    }
    public static synchronized StringBuilder INSERTGROUPINFO(String paragroupid, String paraleader, String paradeadline){
        StringBuilder stringBuilder = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try{
            String groupid = paragroupid;
            String leader = paraleader;
            String deadline = paradeadline;
            String link="http://52.79.193.88/SmartScheduler/INSERTGROUPINFO.php";
            String data  = URLEncoder.encode("groupid", "UTF-8") + "=" + URLEncoder.encode(groupid, "UTF-8");
            data  += "&" + URLEncoder.encode("leader", "UTF-8") + "=" + URLEncoder.encode(leader, "UTF-8");
            data  += "&" + URLEncoder.encode("deadline", "UTF-8") + "=" + URLEncoder.encode(deadline, "UTF-8");
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream wr = conn.getOutputStream();
            wr.write(data.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);
        }catch (Exception e){
            stringBuilder = null;
        }finally{
            if(conn != null)
                conn.disconnect();
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return stringBuilder;
    }
    public static synchronized StringBuilder INSERTBOARD(String paragroupid, String paraname, String paracontent){
        StringBuilder stringBuilder = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try{
            String groupid = paragroupid;
            String name = paraname;
            String content = paracontent;
            String link="http://52.79.193.88/SmartScheduler/INSERTBOARD.php";
            String data  = URLEncoder.encode("groupid", "UTF-8") + "=" + URLEncoder.encode(groupid, "UTF-8");
            data  += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data  += "&" + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream wr = conn.getOutputStream();
            wr.write(data.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);
        }catch (Exception e){
            stringBuilder = null;
        }finally{
            if(conn != null)
                conn.disconnect();
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return stringBuilder;
    }
}

