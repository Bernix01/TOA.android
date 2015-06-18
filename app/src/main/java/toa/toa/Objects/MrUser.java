package toa.toa.Objects;

/**
 * Created by Guillermo on 6/9/2015.
 */
public class MrUser {
    private static int _id;
    private static String _name;
    private static String _uname;
    private static String _email;
    private static String _bio;
    private static int _gender;

    public MrUser() {
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
}
