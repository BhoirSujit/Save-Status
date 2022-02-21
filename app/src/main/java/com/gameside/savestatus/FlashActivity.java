package com.gameside.savestatus;

import static android.content.ContentValues.TAG;
import static android.service.notification.Condition.SCHEME;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import com.gameside.savestatus.databinding.ActivityFlashBinding;
import com.gameside.savestatus.utilities.FolderPaths;

import java.io.File;
import java.util.Objects;

public class FlashActivity extends AppCompatActivity {

    ActivityFlashBinding binding;
    private final String TAG = "FATAG";
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(this.getSupportActionBar()).hide();


        //check for permission
        boolean isInstalled = isAppInstalled(this,"com.whatsapp");
        if(!isInstalled){
            Toast.makeText(this, "You Don't have whatsapp, plz install and try again", Toast.LENGTH_LONG).show();
            finish();
        }else if (checkPermission()){
            //permission granted
            Log.d(TAG, "permission granted");
            createDirectories();
            startMainActivity();
        }else{
            Log.d(TAG, "permission denied");
            requestPermission();
        }

    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {

            return false;
        }
    }

    public void startMainActivity(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    public Boolean checkPermission(){
        boolean granted;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            granted = true;
            Log.d(TAG, "permission avalable");
        }
        else {
            Log.d(TAG, "permission not avalable");
            granted = false;
        }
        return granted;
    }
    public void requestPermission(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Storage Permission");
        builder.setMessage("For saving whatsapp status we need storage permission");
        builder.setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(FlashActivity.this, new String[] {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                },1);
            }
        });
        builder.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted. Continue the action or workflow
                // in your app.
                recreate();
            } else {
                // Explain to the user that the feature is unavailable because
                // the features requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
                Log.d(TAG, "deny kel");
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Toast.makeText(this, "allow storage permission", Toast.LENGTH_LONG).show();
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        }
        // Other 'case' lines to check for other
        // permissions this app might request.

    }

    //    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void getWAFolderAccess() {
//        // Choose a directory using the system's file picker.
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//
//        // Optionally, specify a URI for the directory that should be opened in
//        // the system file picker when it loads.
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("primary:Android/media/com.whatsapp/"));
//
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode,
//                                 Intent resultData) {
//        super.onActivityResult(requestCode, resultCode, resultData);
//        if (requestCode == 1
//                && resultCode == Activity.RESULT_OK) {
//            // The result data contains a URI for the document or directory that
//            // the user selected.
//            Uri uri = null;
//            if (resultData != null) {
//                uri = resultData.getData();
//                // Perform operations on the document using its URI.
//                ActivityCompat.requestPermissions(FlashActivity.this, new String[] {
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                },1);
//                Log.d(TAG, "result data " + uri);
//            }
//        }
//    }

    public void createDirectories(){
        FolderPaths folderPaths = new FolderPaths();

        if(!new File(folderPaths.getSSStatusFolderPath()).exists()){
            try {
                boolean result = new File(folderPaths.getSSStatusFolderPath()).mkdirs();
                Log.d(TAG, "result is " + result);
            }catch (Exception e){
                Log.d(TAG, "error in creating path is " + e);
            }
        }else{
            Log.d(TAG, "path already exists");
        }

    }

}
