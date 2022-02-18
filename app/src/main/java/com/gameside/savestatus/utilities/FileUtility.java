package com.gameside.savestatus.utilities;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class FileUtility {
    private final String TAG = "FUTAG";

    public boolean copyFile(File[] InputFiles, File OutputDirectory){
        boolean Result = true;
        for (int i = 0; i < InputFiles.length; i++) {
            if (!copyFile(InputFiles[i], OutputDirectory)){
                Result = false;
            }
        }
        return Result;
    }


    public boolean copyFile(File InputFile, File OutputDirectory) {
        boolean Result;
        try {
            FileInputStream inputDirectory = new FileInputStream(InputFile);
            FileOutputStream outputDirectory = new FileOutputStream(OutputDirectory+"/"+InputFile.getName());
            FileChannel inputChannel = inputDirectory.getChannel();
            FileChannel outputChannel = outputDirectory.getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            outputChannel.close();
            Result = true;
            Log.d(TAG, "file saved successfully");

        }catch (Exception e){
            Log.d(TAG,"File saved unsuccessful because :"+e);
            Result = false;
        }
        return Result;
    }

    public void scanFile(String[] paths, Context context) {
        for (int i = 0; i < paths.length; i++) {
            scanFile(paths[i], context);
        }
    }

    public void scanFile(String path, Context context) {
        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,
                (path1, uri) -> Log.i(TAG, "Finished scanning " + path1));
    }
}
