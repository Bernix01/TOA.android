/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Guillermo on 6/9/2015.
 */
public class MrUser implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MrUser createFromParcel(Parcel in) {
            return new MrUser(in);
        }

        public MrUser[] newArray(int size) {
            return new MrUser[size];
        }
    };
    private static int _id;
    private static String _name;
    private static String _uname;
    private static String _email;
    private static String _bio;
    private static int _gender;
    private static int _age;
    private static String _pimage;

    public MrUser() {
    }

    public MrUser(Parcel in) {
        _bio = in.readString();
        _email = in.readString();
        _gender = in.readInt();
        _id = in.readInt();
        _name = in.readString();
        _uname = in.readString();

        _pimage = in.readString();
        _age = in.readInt();

    }

    public static int get_age() {
        return _age;
    }

    public static void set_age(int _age) {
        MrUser._age = _age;
    }

    public static String get_pimage() {
        return _pimage;
    }

    public static void set_pimage(String _pimage) {
        MrUser._pimage = _pimage;
    }

    public static String get_uname() {
        return _uname;
    }

    public static void set_uname(String _uname) {
        MrUser._uname = _uname;
    }

    public static int get_id() {
        return _id;
    }

    public static void set_id(int _id) {
        MrUser._id = _id;
    }

    public static String get_name() {
        return _name;
    }

    public static void set_name(String _name) {
        MrUser._name = _name;
    }

    public static String get_email() {
        return _email;
    }

    public static void set_email(String _email) {
        MrUser._email = _email;
    }

    public static String get_bio() {
        return _bio;
    }

    public static void set_bio(String _bio) {
        MrUser._bio = _bio;
    }

    public static int get_gender() {
        return _gender;
    }

    public static void set_gender(int _gender) {
        MrUser._gender = _gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_bio);
        dest.writeString(_email);
        dest.writeInt(_gender);
        dest.writeInt(_id);
        dest.writeString(_name);
        dest.writeString(_uname);
        dest.writeString(_pimage);
        dest.writeInt(_age);
    }
}
