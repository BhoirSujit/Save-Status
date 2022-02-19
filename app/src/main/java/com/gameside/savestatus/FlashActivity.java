package com.gameside.savestatus;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.DocumentsContract;
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

        resultLauncher();

        //check for permission
        if (checkPermission()){
            //permission granted
            Log.d(TAG, "permission granted");
            createDirectories();
            startMainActivity();
        }else{
            Log.d(TAG, "permission denied");
            requestPermission();
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
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    getAccess();
                }else{
                    ActivityCompat.requestPermissions(FlashActivity.this, new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },1);
                }
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
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();

            } else {
                finish();
            }
        }

    }

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

    private void getAccess(){
        uri = DocumentsContract.buildDocumentUri("com.android.externalstorage.documents", "primary:Android/media/com.whatsapp/WhatsApp/Media/.Statuses/");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.putExtra("android.provider.extra.INITIAL_URI", (Parcelable)DocumentsContract.buildDocumentUri((String)"com.android.externalstorage.documents", "primary:Android/media/com.whatsapp/WhatsApp/Media/.Statuses/"));

    }

    public void resultLauncher(){
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();

                            uri = null;
                            uri = Objects.requireNonNull(data).getData();

                            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            Log.d(TAG,"uri is : " + uri.toString());

                            recreate();
                        }
                    }
                }
        );
    }


}
