package com.example.sennevervaecke.server_test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Created by sennevervaecke on 12/19/2017.
 * source: http://www.codejava.net/java-se/networking/ftp/java-ftp-file-download-tutorial-and-example
 */

public class FTPDownloadFile extends AsyncTask<Context, Integer, String> {

    private Context context;
    private View rootview;

    public FTPDownloadFile(Context context, View rootview){
        this.context = context;
        this.rootview = rootview;
    }

    InetAddress server;
    int port = 45788;
    String user = "appUser";
    String pass = "";
    @Override
    protected String doInBackground(Context... contexts) {
        FTPClient ftpClient = new FTPClient();
        this.context = contexts[0];
        try {
            server = InetAddress.getByName("192.168.1.127");
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            Log.e("welcome message", ftpClient.getReplyString());

            // APPROACH #1: using retrieveFile(String, OutputStream)
            String remoteFile1 = "/test/jeno1.jpg";
            File downloadFile1 = new File(contexts[0].getFilesDir(), "jeno.jpg");
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();

            if (success) {
                Log.i("lol", "File #1 has been downloaded successfully.");
            }
            else{
                Log.i("lol", "fail");
            }
            /*
            // APPROACH #2: using InputStream retrieveFileStream(String)
            String remoteFile2 = "/feebe.mp4";
            File downloadFile2 = new File(contexts[0].getFilesDir(), "feebe.mp4");
            OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                outputStream2.write(bytesArray, 0, bytesRead);
            }

            boolean success = ftpClient.completePendingCommand();
            if (success) {
                System.out.println("File #2 has been downloaded successfully.");
            }
            outputStream2.close();
            inputStream.close();
        */
        } catch (IOException ex) {
            Log.i("lol","Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
    protected void onPreExecute(){}
    protected void onProgressUpdate(Integer progress){}
    protected void onPostExecute(String result){
        ImageView img = rootview.findViewById(R.id.testImg);
        File imgFile = new  File(context.getFilesDir() + "/jeno.jpg");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            img.setImageBitmap(myBitmap);

        }

    }
}
