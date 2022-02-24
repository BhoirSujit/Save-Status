package com.gameside.savestatus.utilities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.work.impl.utils.EnqueueRunnable;

import com.gameside.savestatus.BuildConfig;
import com.google.android.gms.common.api.internal.RegisterListenerMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUtility {
    final private String TAG = "UTAG";

    public UpdateUtility(Context context){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Version").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else if(String.valueOf(BuildConfig.VERSION_CODE).equals(String.valueOf(task.getResult().getValue()))) {
                    databaseReference.child("UpdateLink").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.e(TAG, "success "+ task.getResult().getValue());
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Update Available");
                                builder.setMessage("For latest feature and improvement, you need to update this app");
                                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.valueOf(task.getResult().getValue())));
                                        request.setTitle("Save Status");
                                        request.setDescription("Save Status Update");
                                        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "saveStatus.apk");
                                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                        downloadManager.enqueue(request);
                                    }
                                });

                                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context, "as you wish", Toast.LENGTH_LONG).show();
                                    }
                                });
                                builder.setCancelable(false);
                                builder.show();
                            }else {
                                Log.d(TAG, String.valueOf(task.getException()));
                            }
                        }
                    });

                }
            }
        });
    }

}
