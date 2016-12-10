package cnu.mobilesoftware.smartscheduler;

/**
 * Created by GwanYongKim on 2016-12-01.
 */

public class CardItem {
    private CardItem(){}

    public enum TAG{
        NONE,
        PEOPLE_HEADER,
        PEOPLE_CONTENT,
        NOTICE_HEADER,
        NOTICE_CONTENT,
    }

    public static TAG convertNumToTag(int num){
        switch (num){

            case 1:
                return TAG.PEOPLE_HEADER;

            case 2:
                return TAG.PEOPLE_CONTENT;

            case 3:
                return TAG.NOTICE_HEADER;

            case 4:
                return TAG.NOTICE_CONTENT;

            default:
                return TAG.NONE;
        }
    }

    public static class BasicCardItem{
        public TAG tag;
        public BasicCardItem(TAG tag){
            this.tag = tag;
        }
    }

    public static class PeopleCardItem extends BasicCardItem{
        public String peopleName = "";
        public String peopleRank = "";
        public PeopleCardItem(TAG tag) {
            super(tag);
        }

        public PeopleCardItem(TAG tag, String peopleName, String peopleRank){
            super(tag);
            this.peopleName = peopleName;
            this.peopleRank = peopleRank;
        }
    }

    public static class NoticeCardItem extends BasicCardItem{
        public String meetingDate = "";
        public String meetingTopic = "";
        public NoticeCardItem(TAG tag) {
            super(tag);
        }
        public NoticeCardItem(TAG tag, String meetingDate, String meetingTopic){
            super(tag);
            this.meetingDate = meetingDate;
            this.meetingTopic = meetingTopic;
        }
    }
}
