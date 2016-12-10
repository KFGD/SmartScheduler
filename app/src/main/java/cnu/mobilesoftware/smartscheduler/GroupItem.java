package cnu.mobilesoftware.smartscheduler;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GwanYongKim on 2016-11-10.
 */

public class GroupItem implements Parcelable{
    public String group_id;
    public String group_title = "";
    public int img_res = R.drawable.cat;

    public GroupItem(String group_title){
        this.group_title = group_title;
    }

    public GroupItem(String group_id, String group_title){
        this.group_id = group_id;
        this.group_title = group_title;
    }

    protected GroupItem(Parcel in) {
        group_id = in.readString();
        group_title = in.readString();
        img_res = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group_id);
        dest.writeString(group_title);
        dest.writeInt(img_res);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupItem> CREATOR = new Creator<GroupItem>() {
        @Override
        public GroupItem createFromParcel(Parcel in) {
            return new GroupItem(in);
        }

        @Override
        public GroupItem[] newArray(int size) {
            return new GroupItem[size];
        }
    };
}
