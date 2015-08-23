/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.utils;


import android.util.Base64;

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

    public static Date convertDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy hh:mm", Locale.getDefault());
            return format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}