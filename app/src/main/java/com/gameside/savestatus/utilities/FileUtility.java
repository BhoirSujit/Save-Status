package com.gameside.savestatus.utilities;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class FileUtility {
    private final String TAG = "FUTAG";

    public void copyFile(File InputFile, File OutputDirectory) {
        try {
            FileInputStream inputDirectory = new FileInputStream(InputFile);
            FileOutputStream outputDirectory = new FileOutputStream(OutputDirectory+"/"+InputFile.getName());
            FileChannel inputChannel = inputDirectory.getChannel();
            FileChannel outputChannel = outputDirectory.getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            outputChannel.close();
            Log.d(TAG, "file saved successfully");

        }catch (Exception e){
            Log.d(TAG,"File saved unsuccessful because :"+e);
        }
    }

    public void scanFile(String path, Context context) {
        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,
                (path1, uri) -> Log.i(TAG, "Finished scanning " + path1));
    }
}
