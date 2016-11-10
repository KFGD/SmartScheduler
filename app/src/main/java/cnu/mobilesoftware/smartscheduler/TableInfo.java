package cnu.mobilesoftware.smartscheduler;

import android.provider.BaseColumns;

/**
 * Created by GwanYongKim on 2016-11-07.
 */

public class TableInfo {
    public static final class SCHEDULE_ITEM_LIST implements BaseColumns{
        public static final String TABLE_NAME = "SCHEDULE_ITEM_LIST";
        public static final String _ID = "_id";
        public static final String day = "DAY";
        public static final String startTime = "START_TIME";
        public static final String endTime = "END_TIME";
        public static final String subjectName = "SUBJECT_NAME";
        public static final String classNum = "CLASS_NUN";
        public static final String professor = "PROFESSOR";
        public static final String colorOfCell = "COLOR_OF_CELL";
    }

    public static final class MEMO_ITEM_LIST implements BaseColumns{
        public static final String TABLE_NAME = "MEMO_ITEM_LIST";
        public static final String _ID = "_id";
        public static final String DATE_TIME = "DATE_TIME";
        public static final String CONTENT = "CONTENT";
    }
}
