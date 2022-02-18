package com.gameside.savestatus.utilities;

import android.os.Environment;

import java.io.File;

public class FolderPaths {
    public String WAStatusFolderPath = Environment.getExternalStorageDirectory().getPath()+"/WhatsApp/Media/.Statuses";
    public String SSStatusFolderPath = Environment.getExternalStorageDirectory().getPath()+"/WhatsManager/Statuses";

    public String getStatusFolderPath() {
        return WAStatusFolderPath;
    }

    public String getWMStatusFolderPath() {
        return SSStatusFolderPath;
    }

    public File[] getWAStatusFolderFiles(){
        File statusFolderFiles = new File(WAStatusFolderPath);
        //filtering files from object and set in array
        return statusFolderFiles.listFiles((file, s) ->
                s.endsWith(".jpg") || s.endsWith(".mp4"));
    }
    public File[] getSSStatusFolderFiles(){
        File statusFolderFiles = new File(SSStatusFolderPath);
        //filtering files from object and set in array
        return statusFolderFiles.listFiles((file, s) ->
                s.endsWith(".jpg") || s.endsWith(".mp4"));
    }
}
