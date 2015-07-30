package cl.tdc.felipe.tdc.extras;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.InputType;
import android.util.Log;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import cl.tdc.felipe.tdc.AgendaActivity;
import cl.tdc.felipe.tdc.MainActivity;
import cl.tdc.felipe.tdc.R;
import cl.tdc.felipe.tdc.objects.Maintenance.Activity;
import cl.tdc.felipe.tdc.objects.Maintenance.MainSystem;

public class Funciones {

    public static int getInputType(String type) {
        int inputType;

        switch (type) {
            case "TEXT":
                inputType = InputType.TYPE_CLASS_TEXT;
                break;
            default:
                inputType = InputType.TYPE_CLASS_TEXT;
                break;
        }

        return inputType;

    }

    public static String getChecked(List<CheckBox> checkBoxes) {
        String checked = "NO RESPONDE";
        for (CheckBox ch : checkBoxes) {
            if (ch.isChecked()) {
                checked = ch.getText().toString();
                break;
            }
        }
        return checked;
    }

    public static boolean isCorrect(Address location) {
        List<String> paises = new ArrayList<>();
        paises.add("Chile");
        paises.add("Peru");

        for (String pais : paises) {
            if (location.getCountryName().compareTo(pais) == 0)
                return true;
        }
        return false;
    }

    public static void showNotify(Context context, String Code, String MaintenanceID) {
        String message = "";
        Intent notiIntent = null;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent main = new Intent(context, MainActivity.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Resources res = context.getResources();

        PendingIntent contentIntent;

        switch (Code) {
            /*case "0":
                message = "test";

                notiIntent = new Intent(context, AgendaActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(notiIntent);

                builder.setSmallIcon(R.drawable.ic_notify_new)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_tdc))
                        .setAutoCancel(true)
                        .setContentTitle("Notif Test")
                                //.setVibrate(new long[]{1000,1000,1000})
                                //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(message);

                contentIntent = PendingIntent.getActivity(context, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(contentIntent);

                notificationManager.notify(0, builder.build());
                contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                //contentIntent = PendingIntent.getActivity(context, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(contentIntent);
                break;*/
            case "10":
                message = "Sin mantenimientos asignados";
                break;
            case "11":
                message = "Deslize para eliminar";
                break;
            case "20":
                message = "Presione para ver ir a Agenda";
                notiIntent = new Intent(context, AgendaActivity.class);
                builder.setSmallIcon(R.drawable.ic_notify_new)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_tdc))
                        .setAutoCancel(true)
                        .setContentTitle("Nuevo Mantenimiento")
                                //.setVibrate(new long[]{1000,1000,1000})
                                //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(message);

                contentIntent = PendingIntent.getActivity(context, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(contentIntent);

                notificationManager.notify(0, builder.build());
                break;
            case "21":
                message = "Presione para ir a Agenda";
                notiIntent = new Intent(context, AgendaActivity.class);
                builder.setSmallIcon(R.drawable.ic_notify_new)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_tdc))
                        .setAutoCancel(true)
                        .setContentTitle("Nueva Modificación")
                                //.setVibrate(new long[]{1000,1000,1000})
                                //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(message);

                contentIntent = PendingIntent.getActivity(context, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(contentIntent);

                notificationManager.notify(0, builder.build());
                break;
            case "30":
                message = "Ya no tiene asignado el mantenimiento " + MaintenanceID;
                notiIntent = new Intent(context, AgendaActivity.class);
                builder.setSmallIcon(R.drawable.ic_notify_new)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_tdc))
                        .setAutoCancel(true)
                        .setContentTitle("Actualización")
                                //.setVibrate(new long[]{1000,1000,1000})
                                //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(message);

                contentIntent = PendingIntent.getActivity(context, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(contentIntent);

                notificationManager.notify(0, builder.build());
                break;
        }


    }

    public static int getNumActivities(ArrayList<MainSystem> systemList) {
        int i = 0;
        for(MainSystem ms : systemList){
            for(Activity a: ms.getActivitieList()){
                i++;
            }
        }
        return i;
    }
}
