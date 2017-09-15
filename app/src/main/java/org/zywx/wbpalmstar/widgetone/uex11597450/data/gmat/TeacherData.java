package org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat;

import android.os.Parcel;
import android.os.Parcelable;

public class TeacherData implements Parcelable {

    private String teacherId;
    private String teacherName;
    private String title;
    private String teacherIamge;
    private String introduce;
    private int randomNumber;

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacherIamge() {
        return teacherIamge;
    }

    public void setTeacherIamge(String teacherIamge) {
        this.teacherIamge = teacherIamge;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.teacherId);
        dest.writeString(this.teacherName);
        dest.writeString(this.title);
        dest.writeString(this.teacherIamge);
        dest.writeString(this.introduce);
        dest.writeInt(this.randomNumber);
    }

    public TeacherData() {
    }

    protected TeacherData(Parcel in) {
        this.teacherId = in.readString();
        this.teacherName = in.readString();
        this.title = in.readString();
        this.teacherIamge = in.readString();
        this.introduce = in.readString();
        this.randomNumber = in.readInt();
    }

    public static final Parcelable.Creator<TeacherData> CREATOR = new Parcelable.Creator<TeacherData>() {
        @Override
        public TeacherData createFromParcel(Parcel source) {
            return new TeacherData(source);
        }

        @Override
        public TeacherData[] newArray(int size) {
            return new TeacherData[size];
        }
    };
}
