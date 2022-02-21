package com.gameside.savestatus.adapters;

import android.content.Context;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.DefaultSelectionTracker;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.OperationMonitor;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gameside.savestatus.R;
import com.gameside.savestatus.fragments.SavedFragment;
import com.gameside.savestatus.interfaces.RVAInterface;
import com.gameside.savestatus.utilities.FolderPaths;

import java.io.File;
import java.util.Arrays;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    File[] files;
    private final String TAG = "RVATAG";
    RVAInterface rvaInterface;

    public void setNewFiles(File[] files){
        this.files = files;
    }

    public RecycleViewAdapter(File[] files, RVAInterface rvaInterface) {
        this.files = files;
        Log.d(TAG, "status file is " + Arrays.toString(files));
        File[] files1 = new File(new FolderPaths().getStatusFolderPath()).listFiles();
        Log.d(TAG, "status file is " + Arrays.toString(files1));
        Log.d(TAG, "status exist " + new File(new FolderPaths().getStatusFolderPath()).exists());


        this.rvaInterface = rvaInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.square_media_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File mediaFile = files[position];
        Context context = holder.itemView.getContext();

        //set image or thumbnail on grid view
        Glide.with(context)
                .load(mediaFile.getAbsoluteFile())
                .into(holder.getImageView());

        //adding video icon on thumbnail
        if (mediaFile.getAbsoluteFile().toString().endsWith(".jpg")) {
            holder.getIsVideoIV().setVisibility(View.INVISIBLE);
        } else if (mediaFile.getAbsoluteFile().toString().endsWith(".mp4")) {
            holder.getIsVideoIV().setVisibility(View.VISIBLE);
        }

        rvaInterface.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return files.length;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final ImageView isVideoIV;
        private final ImageView selectionIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gImageView);
            isVideoIV = itemView.findViewById(R.id.isVideoIV);
            selectionIV = itemView.findViewById(R.id.isSelected);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public ImageView getIsVideoIV() {
            return isVideoIV;
        }

        public ImageView getSelectionIV() {
            return selectionIV;
        }
    }

}
