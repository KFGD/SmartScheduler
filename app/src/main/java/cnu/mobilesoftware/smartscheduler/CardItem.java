package cnu.mobilesoftware.smartscheduler;

/**
 * Created by GwanYongKim on 2016-12-01.
 */

public class CardItem {
    private CardItem(){}

    public enum TAG{
        PEOPLE_HEADER,
        PEOPLE_CONTENT,
        NOTICE_HEADER,
        NOTICE_CONTENT,
    }

    public class BasicCardItem{
        public TAG tag;
        public BasicCardItem(TAG tag){
            this.tag = tag;
        }
    }

    public class PeopleCardItem extends BasicCardItem{
        public String peopleName = "";
        public PeopleCardItem(TAG tag) {
            super(tag);
        }
    }

    public class NoticeCardItem extends BasicCardItem{


        public NoticeCardItem(TAG tag) {
            super(tag);
        }
    }
}
