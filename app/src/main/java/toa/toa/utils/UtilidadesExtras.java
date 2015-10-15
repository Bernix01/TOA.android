/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Creado por : lawliet
 * Cecha: 13/06/2015.
 * Proyecto: Toa.
 * Hora: 12:01.
 */
public class UtilidadesExtras {

    public static String Encriptar(String texto) {

        String spc = "VI$/QW159753!XDR"; //llave para encriptar datos
        String base64EncryptedString = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(spc.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            //byte[] base64Bytes = Base64.encode(buf);
            base64EncryptedString = Base64.encodeToString(buf, Base64.DEFAULT);
        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }


    public static Boolean validEmail(String email) {
        return email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    public static int tryGetInt(JSONObject j, String name) {
        int r = -1;
        try {
            r = j.getInt(name);
            Log.i("int", String.valueOf(r));
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    public static float tryGetFloat(JSONObject j, String name) {
        float r = 0;
        try {
            r = (float) (Double.parseDouble(j.getString(name)) * 1.0f);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String tryGetString(JSONObject j, String name) {
        String r = "";
        try {
            r = j.getString(name);
            Log.i("str", r);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    public static int tryGetInt(JSONArray j, int pos) {
        int r = -1;
        try {
            r = j.getInt(pos);
            Log.i("int", String.valueOf(r));
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
    public static String tryGetString(JSONArray j, int pos) {
        String r = "";
        try {
            r = j.getString(pos);
            Log.i("str", r);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    static Date convertDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            return format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}