package com.gameside.savestatus;

import static com.gameside.savestatus.R.color.black;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gameside.savestatus.adapters.MediaPlayerAdapter;
import com.gameside.savestatus.databinding.ActivityMediaPlayerBinding;
import com.gameside.savestatus.utilities.FileUtility;
import com.gameside.savestatus.utilities.FolderPaths;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MediaPlayerActivity extends AppCompatActivity {
    private final String TAG = "MPATAG";

    ActivityMediaPlayerBinding binding;
    MediaPlayerAdapter mediaPlayerAdapter;

    private ArrayList<File> filesList;
    private ViewPager2 viewPager;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMediaPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //getting data from activities
        Intent intent = getIntent();
        String filepath = intent.getStringExtra("filepath");

        Log.d(TAG, "filepath is " + filepath);

        //setting toolbar
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        this.getWindow().setStatusBarColor(getResources().getColor(black));
        this.getWindow().setNavigationBarColor(getResources().getColor(black));


        //initialize
        viewPager = binding.ViewPager;
        TextView saveTextView = binding.deleteButton;
        ImageView saveImageView = binding.deleteImageView;

        //convert String to file
        File mediaFile = new File(filepath);

        //parent path
        File fileParentPath = new File(Objects.requireNonNull(mediaFile.getParent()));
        Log.d(TAG, "fileParentPath " + fileParentPath);

        //filter files
        File[] filteredFiles = fileParentPath.listFiles((file, s) ->
                s.endsWith(".jpg") || s.endsWith(".mp4"));

        //Array of FileList
        filesList = new ArrayList<>();
        assert filteredFiles != null;
        filesList.addAll(Arrays.asList(filteredFiles));
        Log.d(TAG, "fileList " + filesList);

        //index of clicked file
        int clickedFileIndex = filesList.indexOf(mediaFile);
        Log.d(TAG, "clicked file index " + clickedFileIndex);

        //setup with viewPager with MediaPlayerAdapter
        mediaPlayerAdapter = new MediaPlayerAdapter(this, filepath);
        mediaPlayerAdapter.setTextView(binding.titleText);

        //viewpager
        viewPager.setAdapter(mediaPlayerAdapter);

        //set user on clicked position
        viewPager.setCurrentItem(clickedFileIndex, false);

        //delete or save?
        if (fileParentPath.toString().equals( new FolderPaths().getStatusFolderPath())) {
            saveTextView.setText("SAVE");
            saveImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
            saveMethod();
        } else {
            deleteMethod();
        }

        //others methods
        repostMethod();
        shareMethod();

        //setting custom toolbar
        binding.backButton.setOnClickListener(view -> {
            finish();
        });
    }

    private void repostMethod(){
        binding.repostLayout.setOnClickListener(view -> {
            String resLink = filesList.get(viewPager.getCurrentItem()).toString();
            Intent washareIntent = new Intent(Intent.ACTION_SEND);
            if (resLink.endsWith(".jpg")) {
                washareIntent.setType("image/jpeg");
            } else {
                washareIntent.setType("video/mp4");
            }
            washareIntent.setPackage("com.whatsapp");
            washareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(resLink));
            startActivity(Intent.createChooser(washareIntent, "Share using"));
        });
    }
    private void saveMethod(){
        binding.deleteLayout.setOnClickListener(view -> {
            FileUtility fileUtility = new FileUtility();

            //input file
            File resLink = new File(filesList.get(viewPager.getCurrentItem()).toString());
            Log.d(TAG, "reslink is " + resLink);

            //outputFile
            File outputFile = new File(new FolderPaths().getSSStatusFolderPath());

            //copy file
            //check weather file is exist or not
            if (!new File(outputFile+"/"+resLink.getName()).exists()){
                try {
                    fileUtility.copyFile(resLink, outputFile);
                    Toast.makeText(this,"Saved Successfully", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Something went wrong", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(this, "Already saved", Toast.LENGTH_LONG).show();
            }

            //scanFile
            fileUtility.scanFile(new File(outputFile+"/"+resLink.getName()).toString(), this);
        });
    }
    private void shareMethod(){
        binding.shareLayout.setOnClickListener(view -> {
            String resLink = filesList.get(viewPager.getCurrentItem()).toString();

            //share media file using intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            if (resLink.endsWith(".jpg")) {
                shareIntent.setType("image/jpeg");
            } else {
                shareIntent.setType("video/mp4");
            }
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(resLink));
            startActivity(Intent.createChooser(shareIntent, "Share using"));
        });
    }

    private void deleteMethod(){
        binding.deleteLayout.setOnClickListener(view -> {
            String resLink = filesList.get(viewPager.getCurrentItem()).toString();

            new AlertDialog.Builder(view.getContext(), R.style.Theme_DialogBox)
                    .setTitle("Delete the File")
                    .setMessage("Are you sure to delete this file?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        boolean delete = new File(resLink).delete();
                        if (delete){
                            Toast.makeText(view.getContext(), "Deleted successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();
        });
    }

}