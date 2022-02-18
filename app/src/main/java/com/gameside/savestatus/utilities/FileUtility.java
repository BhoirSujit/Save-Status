package com.gameside.savestatus.utilities;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class FileUtility {
    private final String TAG = "FUTAG";

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

    public void scanFile(String path, Context context) {
        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,
                (path1, uri) -> Log.i(TAG, "Finished scanning " + path1));
    }
}
