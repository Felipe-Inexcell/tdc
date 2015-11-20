package cl.tdc.felipe.tdc.extras;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.widget.CheckBox;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cl.tdc.felipe.tdc.AgendaActivity;
import cl.tdc.felipe.tdc.MainActivity;
import cl.tdc.felipe.tdc.R;
import cl.tdc.felipe.tdc.objects.Maintenance.Activity;
import cl.tdc.felipe.tdc.objects.Maintenance.MainSystem;

public class Funciones {
    private static final String PATTERN_DATE = "([0-9]{4})-([0-9]{2})-([0-9]{2})";

    public static boolean validateDate(String date) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_DATE);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();

    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

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

    public static boolean isAnyChecked(List<CheckBox> checkBoxes) {

        for (CheckBox ch : checkBoxes) {
            if (ch.isChecked()) {
                return true;
            }
        }
        return false;
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
        paises.add("Perú");

        Log.e("TEST", location.getCountryName());
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
                                .setVibrate(new long[]{1000,1000,1000})
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
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
                                .setVibrate(new long[]{1000,1000,1000})
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
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
                                .setVibrate(new long[]{1000,1000,1000})
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(message);

                contentIntent = PendingIntent.getActivity(context, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(contentIntent);

                notificationManager.notify(0, builder.build());
                break;
        }


    }

    //TODO: str2int
    public static int str2int(String a) {
        char[] b = a.toCharArray();
        int r = 0;
        int i = 1;
        for (char c : b) {
            r += Character.getNumericValue(c) * i;
            i += 10;
        }
        return r;
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

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public static File Update(String apkurl) {
        try {
            URL url = new URL(apkurl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String[] spliit = apkurl.split("/");

            String PATH = Environment.getExternalStorageDirectory() + "/TDC@/update/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, spliit[spliit.length - 1]);
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();//till here, it works fine - .apk is download to my sdcard in download file


            return outputFile;
        } catch (IOException e) {
            return null;
        }
    }



}
