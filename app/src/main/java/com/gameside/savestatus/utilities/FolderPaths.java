package com.gameside.savestatus.utilities;

import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;

import java.io.File;

public class FolderPaths {
    private final String WAStatusFolderPath = Environment.getExternalStorageDirectory().getPath()+"/WhatsApp/Media/.Statuses";

    private final String WAStatusFolderPathNew = Environment.getExternalStorageDirectory()+"/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
//    private final String SSStatusFolderPath = Environment.getExternalStorageDirectory().getPath()+"/Save Status/Statuses";
    private final String SSStatusFolderPath = Environment.getExternalStoragePublicDirectory("Pictures")+"/Save Status/Statuses";


    public String getStatusFolderPath() {
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
//            return WAStatusFolderPath;
//        }else {
//            return WAStatusFolderPathNew;
//        }

        if (new File(WAStatusFolderPath).exists()){
            return WAStatusFolderPath;
        }else {
            return WAStatusFolderPathNew;
        }
    }

    public String getSSStatusFolderPath() {
        return SSStatusFolderPath;
    }

    public File[] getWAStatusFolderFiles(){
        File statusFolderFiles = new File(getStatusFolderPath());
        //filtering files from object and set in array
        return statusFolderFiles.listFiles((file, s) ->
                s.endsWith(".jpg") || s.endsWith(".mp4"));
    }
    public File[] getSSStatusFolderFiles(){
        File statusFolderFiles = new File(getSSStatusFolderPath());
        //filtering files from object and set in array
        return statusFolderFiles.listFiles((file, s) ->
                s.endsWith(".jpg") || s.endsWith(".mp4"));
    }
}
