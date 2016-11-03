package cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI;

/**
 * Created by Administrator on 2016-11-01.
 */

public class SchedulerUtils {
    public enum DAY_TAG{
        NONE,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY};
    public static DAY_TAG convertStringToDAY_TAG(String text){
        switch (text){
            case "MONDAY":
                return DAY_TAG.MONDAY;
            case "TUESDAY":
                return DAY_TAG.TUESDAY;
            case "WEDNESDAY":
                return DAY_TAG.WEDNESDAY;
            case "THURSDAY":
                return DAY_TAG.THURSDAY;
            case "FRIDAY":
                return DAY_TAG.FRIDAY;
            default:
                return DAY_TAG.NONE;
        }
    }
}
