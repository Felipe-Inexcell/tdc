package cl.tdc.felipe.tdc.webservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cl.tdc.felipe.tdc.objects.Seguimiento.ImagenDia;

public class UploadImage extends AsyncTask<String, String, String> {

    private Context mContext;
    List<ImagenDia> imagenes;
    ProgressDialog dialog;

    public UploadImage(Context mContext, List<ImagenDia> imagenes) {
        this.mContext = mContext;
        this.imagenes = imagenes;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";

        DateFormat timestamp_name = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

        for (ImagenDia img : imagenes) {
            try {
                String fileName = img.getFilename();

                Log.i("ENVIANDO", fileName);
                HttpURLConnection conn;
                DataOutputStream dos;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;

                File done = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
                done.createNewFile();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                img.getBitmap().compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(done);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();


                if (!done.isFile())
                    Log.e("DownloadManager", "no existe");
                else {
                    FileInputStream fileInputStream = new FileInputStream(done);
                    URL url = new URL(params[0]);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);

                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, 1 * 1024 * 1024);
                    byte[] buf = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buf, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buf, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, 1 * 1024 * 1024);
                        bytesRead = fileInputStream.read(buf, 0, bufferSize);

                    }

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();


                    Log.i("UploadManager", "HTTP response is: " + serverResponseMessage + ": " + serverResponseCode);

                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                    InputStream responseStream = new BufferedInputStream(conn.getInputStream());

                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = responseStreamReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    responseStreamReader.close();

                    response = stringBuilder.toString();

                    Log.d("IMAGENES", img.getTimestamp() + "   \n" + response);
                }


            } catch (Exception e) {
                Log.d("TAG", "Error: " + e.getMessage());
                response = "ERROR";
            }
        }
        return response;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(mContext);
        dialog.setMessage("Subiendo imagenes...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        if (dialog.isShowing())
            dialog.dismiss();
        ((Activity)mContext).finish();
        super.onPostExecute(s);
    }


}
