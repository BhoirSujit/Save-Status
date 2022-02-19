package com.gameside.savestatus.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gameside.savestatus.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MediaPlayerAdapter extends RecyclerView.Adapter<MediaPlayerAdapter.ViewHolderNew> {

    Context context;
    ArrayList<File> arrayList;
    String filelink;
    private final String TAG = "MPATAG";
    TextView titleTextView;

    public void setTextView(TextView textView){
        this.titleTextView = textView;
    }

    public MediaPlayerAdapter(Context context,String filelink) {
        this.filelink = filelink;
        this.context = context;
        Log.d(TAG, "filelink " + filelink);

        //convert String to file
        File mediaFile = new File(filelink);

        //parent path
        File fileParentPath = new File(Objects.requireNonNull(mediaFile.getParent()));
        Log.d(TAG, "fileParentPath " + fileParentPath);

        //filter files
        File[] filteredFiles = fileParentPath.listFiles((file, s) ->
                s.endsWith(".jpg") || s.endsWith(".mp4"));

        //Array of FileList
        assert filteredFiles != null;
        ArrayList<File> filesList = new ArrayList<>(Arrays.asList(filteredFiles));
        Log.d(TAG, TAG + filesList);

        this.arrayList = filesList;
    }

    @NonNull
    @Override
    public ViewHolderNew onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.media_adaptor_layout, parent, false);
        return new ViewHolderNew(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNew holder, int position) {
        //set views
        ImageView imageView = holder.imageView;
        VideoView videoView = holder.videoView;

        String mediapath = arrayList.get(position).toString();
        Log.d(TAG , arrayList.get(position).toString()+" and position is : "+position);


        //check weather file is image or video
        if (mediapath.endsWith(".mp4")) {
            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            videoView.setVideoPath(mediapath);
        }
        if (mediapath.endsWith(".jpg")) {
            videoView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(mediapath)
                    .into(imageView);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolderNew holder) {
        super.onViewAttachedToWindow(holder);
        Log.d(TAG, "onViewAttachedToWindow");
        holder.videoView.start();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d(TAG, "onAttachedToRecyclerView");
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        Log.d(TAG, "registerAdapterDataObserver");

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolderNew extends RecyclerView.ViewHolder{
        ImageView imageView;
        VideoView videoView;

        public ViewHolderNew(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imaVidImageView);
            videoView = itemView.findViewById(R.id.imaVidVideoView);
        }
    }


}
