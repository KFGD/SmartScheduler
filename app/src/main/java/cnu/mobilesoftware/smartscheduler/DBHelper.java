package cnu.mobilesoftware.smartscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import cnu.mobilesoftware.smartscheduler.KFGD_MemoUI.Memo;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.ScheduleItem;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SchedulerUtils;

/**
 * Created by GwanYongKim on 2016-11-07.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 1;
    private static final String DM_NAME = "SmartScheduler_schema.db";
    private static DBHelper dbHelper;

    private DBHelper(){
        super(SmartSchedulerApplication.getContext(), DM_NAME, null, DB_VERSION);
    }

    public DBHelper(Context context, String DBName){
        super(context, DBName, null, 1);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static DBHelper getInstance(){
        if(null == dbHelper )
            dbHelper = new DBHelper();
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder tableScheduleItemList = new StringBuilder();
        tableScheduleItemList.append(" CREATE TABLE " + TableInfo.SCHEDULE_ITEM_LIST.TABLE_NAME)
                .append(" (")
                .append(TableInfo.SCHEDULE_ITEM_LIST._ID + " INTEGER primary key autoincrement ,")
                .append(TableInfo.SCHEDULE_ITEM_LIST.day + " TEXT ,")
                .append(TableInfo.SCHEDULE_ITEM_LIST.startTime + " INTEGER ,")
                .append(TableInfo.SCHEDULE_ITEM_LIST.endTime + " INTEGER ,")
                .append(TableInfo.SCHEDULE_ITEM_LIST.subjectName + " TEXT ,")
                .append(TableInfo.SCHEDULE_ITEM_LIST.classNum + " TEXT ,")
                .append(TableInfo.SCHEDULE_ITEM_LIST.professor + " TEXT ,")
                .append(TableInfo.SCHEDULE_ITEM_LIST.colorOfCell + " TEXT")
                .append(" );");
        sqLiteDatabase.execSQL(tableScheduleItemList.toString());

        StringBuilder tableMemoItemList = new StringBuilder();
        tableMemoItemList.append(" CREATE TABLE " + TableInfo.MEMO_ITEM_LIST.TABLE_NAME)
                .append(" (")
                .append(TableInfo.MEMO_ITEM_LIST._ID + " INTEGER primary key autoincrement ,")
                .append(TableInfo.MEMO_ITEM_LIST.DATE_TIME + " TEXT ,")
                .append(TableInfo.MEMO_ITEM_LIST.CONTENT + " TEXT")
                .append(" );");
        sqLiteDatabase.execSQL(tableMemoItemList.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public ArrayList<ScheduleItem> getScheduleItemWithDay_Tag(SchedulerUtils.DAY_TAG day_tag){

        ArrayList<ScheduleItem> items = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String[] columns = {
                TableInfo.SCHEDULE_ITEM_LIST._ID,
                TableInfo.SCHEDULE_ITEM_LIST.startTime,
                TableInfo.SCHEDULE_ITEM_LIST.endTime,
                TableInfo.SCHEDULE_ITEM_LIST.subjectName,
                TableInfo.SCHEDULE_ITEM_LIST.classNum,
                TableInfo.SCHEDULE_ITEM_LIST.professor,
                TableInfo.SCHEDULE_ITEM_LIST.colorOfCell
        };
        String[] params = {day_tag.name()};
        try{
            db = getReadableDatabase();
            cursor = db.query(
                    TableInfo.SCHEDULE_ITEM_LIST.TABLE_NAME,
                    columns,
                    TableInfo.SCHEDULE_ITEM_LIST.day+"=?",
                    params,
                    null, null, null
                    );
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                items.add(new ScheduleItem(
                        cursor.getInt(cursor.getColumnIndex(columns[0])),
                        day_tag,
                        cursor.getInt(cursor.getColumnIndex(columns[1])),
                        cursor.getInt(cursor.getColumnIndex(columns[2])),
                        cursor.getString(cursor.getColumnIndex(columns[3])),
                        cursor.getString(cursor.getColumnIndex(columns[4])),
                        cursor.getString(cursor.getColumnIndex(columns[5])),
                        cursor.getString(cursor.getColumnIndex(columns[6]))
                        ));
                cursor.moveToNext();
            }
        }catch (Exception e){
            Log.e("DB_ERROR", "getScheduleItemWithDay_Tag()");
            e.printStackTrace();
        }finally {
            closeResource(db, cursor);
        }
        return items;
    }
    public boolean insertScheduleItemsOfDay(ArrayList<ScheduleItem> items){

        if(!deleteScheduleItemsOfDay(SchedulerUtils.convertStringToDAY_TAG(items.get(0).day)))
            return false;

        boolean bReturn = true;
        SQLiteDatabase db = null;
        try{
            db = getWritableDatabase();
            for(ScheduleItem item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TableInfo.SCHEDULE_ITEM_LIST.day, item.day);
                contentValues.put(TableInfo.SCHEDULE_ITEM_LIST.startTime, item.startTime);
                contentValues.put(TableInfo.SCHEDULE_ITEM_LIST.endTime, item.endTime);
                contentValues.put(TableInfo.SCHEDULE_ITEM_LIST.subjectName, item.subjectName);
                contentValues.put(TableInfo.SCHEDULE_ITEM_LIST.classNum, item.classNum);
                contentValues.put(TableInfo.SCHEDULE_ITEM_LIST.professor, item.professor);
                contentValues.put(TableInfo.SCHEDULE_ITEM_LIST.colorOfCell, item.colorOfCell);
                db.insert(TableInfo.SCHEDULE_ITEM_LIST.TABLE_NAME, null, contentValues);
            }
        }catch (Exception e){
            Log.e("DB_ERROR", "insertScheduleItemsOfDay()");
            bReturn = false;
        }finally {
            closeResource(db);
        }

        return bReturn;
    }

    private boolean deleteScheduleItemsOfDay(SchedulerUtils.DAY_TAG day_tag){
        boolean bReturn = true;
        SQLiteDatabase db = null;
        String[] params = {day_tag.name()};
        try{
            db = getWritableDatabase();
            db.delete(TableInfo.SCHEDULE_ITEM_LIST.TABLE_NAME, TableInfo.SCHEDULE_ITEM_LIST.day + "=?", params);
        }catch (Exception e){
            Log.e("DB_ERROR", "deleteScheduleItemOfDay()");
            bReturn = false;
        }finally {
            closeResource(db);
        }
        return bReturn;
    }

    private void closeResource(SQLiteDatabase db){
        if(null != db)
            db.close();
    }

    private void closeResource(SQLiteDatabase db, Cursor cursor){
        if(null != cursor)
            cursor.close();
        if(null != db)
            db.close();
    }

    public HashMap<String, Memo> getMemoListFromDB(){
        HashMap<String, Memo> memoList = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            //String[] columnNames = {"datetime", "content"};
            db = getReadableDatabase();
            cursor = db.query(TableInfo.MEMO_ITEM_LIST.TABLE_NAME, null, null, null, null, null, null);
            memoList = new HashMap<>();
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                int _ID = cursor.getInt(cursor.getColumnIndex(TableInfo.MEMO_ITEM_LIST._ID));
                String DATE_TIME = cursor.getString(cursor.getColumnIndex(TableInfo.MEMO_ITEM_LIST.DATE_TIME));
                String CONTENT = cursor.getString(cursor.getColumnIndex(TableInfo.MEMO_ITEM_LIST.CONTENT));
                memoList.put(DATE_TIME, new Memo(_ID, DATE_TIME, CONTENT));
                cursor.moveToNext();
            }
        } catch (Exception e){
            Log.e("DB_ERROR", "getMemoListFromDB()");
            e.printStackTrace();
        } finally{
            closeResource(db, cursor);
        }
        return memoList;
    }

    public void insertMemoInDB(Memo memo){
        SQLiteDatabase db = null;
        try{
            db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableInfo.MEMO_ITEM_LIST.DATE_TIME, memo.getDataTimeToString());
            contentValues.put(TableInfo.MEMO_ITEM_LIST.CONTENT, memo.getContent());
            db.insert(TableInfo.MEMO_ITEM_LIST.TABLE_NAME, null, contentValues);
        } catch (Exception e){
            Log.e("DB_ERROR", "insertMemoInDB");
        } finally{
            closeResource(db);
        }
    }

    public void updateMemoInDB(Memo memo){
        SQLiteDatabase db = null;
        try{
            db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableInfo.MEMO_ITEM_LIST.CONTENT, memo.getContent());
            String[] params = {String.valueOf(memo.getID())};
            db.update(TableInfo.MEMO_ITEM_LIST.TABLE_NAME,
                    contentValues,
                    TableInfo.MEMO_ITEM_LIST._ID + "=?",
                    params);
        } catch (Exception e){
            Log.e("DB_ERROR", "upateMemoInDB");
        } finally{
            closeResource(db);
        }
    }
    public void deleteMemoinDB(Memo memo){
        SQLiteDatabase db = null;
        try{
            db = getWritableDatabase();
            db.delete(TableInfo.MEMO_ITEM_LIST.TABLE_NAME, TableInfo.MEMO_ITEM_LIST._ID + "=?", new String[]{String.valueOf(memo.getID())});
        }catch (Exception e){
            e.printStackTrace();
            Log.e("error", "deleteMemoinDB Error");
        }finally {
            closeResource(db);
        }
    }

}
