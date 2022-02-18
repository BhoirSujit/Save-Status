package com.gameside.savestatus.utilities;

import android.os.Environment;

import java.io.File;

public class FolderPaths {
    private String WAStatusFolderPath = Environment.getExternalStorageDirectory().getPath()+"/WhatsApp/Media/.Statuses";

    private String WAStatusFolderPathNew = Environment.getExternalStorageDirectory().getPath()+"Android/Media/WhatsApp/Media/.Statuses";
    private String SSStatusFolderPath = Environment.getExternalStorageDirectory().getPath()+"/WhatsManager/Statuses";

    public String getStatusFolderPath() {
        return WAStatusFolderPath;
    }

    public String getSSStatusFolderPath() {
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
