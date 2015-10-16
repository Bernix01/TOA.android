package toa.toa;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import toa.toa.Objects.MrEvent;

/**
 * Helper class for showing and canceling new event
 * notifications.
 * <p/>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class NewEventNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "NewEvent";
    private static int ID;

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * presentation of new event notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context, int)
     */
    public static void notify(final Context context,
                              final MrEvent event) {
        String text = "¡Está próximo a comenzar!";
        if (event.getDateStart().before(new Date())) {
            if (event.getDateEnd().after(new Date())) {
                //event is ongoing!
                text = "¡Está en curso!";
            } else {
                //event was in the past

                return;
            }
        }
        final Resources res = context.getResources();
        ID = event.getId();
        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        final Bitmap picture = getBitmapFromURL(event.getEventsportimg());


        final String ticker = event.getName();
        final String title = event.getName();


        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style

                .bigText(event.getDescr())
                .setBigContentTitle(title)
                .setSummaryText("Organiza: " + event.getOrganizador());
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)

                        // Set required fields, including the small icon, the
                        // notification title, and text.
                .setSmallIcon(R.drawable.ic_event_white_36dp)
                .setContentTitle(title)
                .setContentText(text)
                .setVibrate(new long[]{0, 50, 100, 50, 100, 50, 100, 400, 100, 300, 100, 350, 50, 200, 100, 100, 50, 600})

                        // All fields below this line are optional.

                        // Use a default priority (recognized on devices running Android
                        // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        // Provide a large icon, shown with the notification in the
                        // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)

                        // Set ticker text (preview) information for this notification.
                .setTicker(ticker)

                        // Show a number. This is useful when stacking notifications of
                        // a single type.


                        // Set when the notification will be displayed in milis.
                        //  .setWhen(event.getDateStart().getTime()-1800000)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                (new Intent(context, DetailEventActivity.class).putExtra("event", event)),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                        // Show an expanded list of items on devices running Android 4.1
                        // or later.
                .setStyle(style)

                        // Example additional actions for this notification. These will
                        // only show on devices running Android 4.1 or later, so you
                        // should ensure that the activity in this notification's
                        // content intent provides access to the same actions in
                        // another way.


                        // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);
        if (event.getY() != 0 && event.getX() != 0) {
            Log.i("x,y", event.getX() + "," + event.getY());
            Uri uri = Uri.parse("google.navigation:q=" + event.getX() + "," + event.getY());
            Intent navIntent = new Intent(Intent.ACTION_VIEW, uri);
            navIntent.setPackage("com.google.android.apps.maps");
            builder.addAction(
                    R.drawable.ic_navigation_white_24dp,
                    res.getString(R.string.navigate),
                    PendingIntent.getActivity(
                            context,
                            0,
                            navIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }
        notify(context, builder.build());
    }


    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, ID, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context, int id) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, id);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
