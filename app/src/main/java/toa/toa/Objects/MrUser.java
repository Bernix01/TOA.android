/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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
    private int _id;
    private String _name;
    private String _uname;
    private String _email;
    private String _bio;
    private int _gender;
    private int _age;
    private String _pimage;
    private ArrayList<MrCommunity> _sports;

    public MrUser(int id, String name, String uname, String email, String bio, int gender, int age, String pimage) {
        this._id = id;
        this._name = name;
        this._email = email;
        this._bio = bio;
        this._uname = uname;
        this._gender = gender;
        this._age = age;
        this._pimage = pimage;
    }

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

    public MrUser withSports(ArrayList<MrCommunity> sports) {
        this._sports = sports;
        return this;
    }

    public ArrayList<MrCommunity> get_sports() {
        return this._sports;
    }

    public int get_age() {
        return _age;
    }

    public void set_age(int _age) {
        this._age = _age;
    }

    public String get_pimage() {
        return _pimage;
    }

    public void set_pimage(String _pimage) {
        this._pimage = _pimage;
    }

    public String get_uname() {
        return _uname;
    }

    public void set_uname(String _uname) {
        this._uname = _uname;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_bio() {
        return _bio;
    }

    public void set_bio(String _bio) {
        this._bio = _bio;
    }

    public int get_gender() {
        return _gender;
    }

    public void set_gender(int _gender) {
        this._gender = _gender;
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
