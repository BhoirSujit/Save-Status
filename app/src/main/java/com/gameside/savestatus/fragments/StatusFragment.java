package com.gameside.savestatus.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gameside.savestatus.MediaPlayerActivity;
import com.gameside.savestatus.R;
import com.gameside.savestatus.adapters.RecycleViewAdapter;
import com.gameside.savestatus.adapters.SelectionAdapter;
import com.gameside.savestatus.interfaces.RVAInterface;
import com.gameside.savestatus.utilities.FileUtility;
import com.gameside.savestatus.utilities.FolderPaths;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class StatusFragment extends Fragment implements RVAInterface{
    View view;
    RecycleViewAdapter recycleViewAdapter;
    private final String TAG = "StatusFTAG";
    File[] statusFolderFiles;
    RecyclerView recyclerView;
    SelectionAdapter selectionAdapter;
    ActionMode actionMode;
    FileUtility fileUtility = new FileUtility();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_status, container, false);
        FolderPaths folderPaths = new FolderPaths();
        statusFolderFiles = folderPaths.getWAStatusFolderFiles();

        setRecycleViewAdapter();
        selectionAdapter = new SelectionAdapter(recyclerView);

        return view;
    }

    private void setRecycleViewAdapter() {
        //Recycle view adapter
        recycleViewAdapter = new RecycleViewAdapter(statusFolderFiles, this);

        //recyclerView
        recyclerView = view.findViewById(R.id.statusRecycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(recycleViewAdapter);
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder holder, int position) {

        holder.getImageView().setOnClickListener(view1 -> {
            if (actionMode != null) {
                selectionAdapter.selection(position);
                actionMode.setTitle(selectionAdapter.getPositions().size() + " selected");
            }else{
                Log.d(TAG, " position is " + position);
                Intent intent = new Intent(getContext(), MediaPlayerActivity.class);
                intent.putExtra("filepath", statusFolderFiles[position]);
                startActivity(intent);
            }
        });

        holder.getImageView().setOnLongClickListener(view1 -> {
            if (actionMode != null) {
                return false;
            }
            // Start the CAB using the ActionMode.Callback defined above
            actionMode = requireActivity().startActionMode(multiChoiceModeListener);
            selectionAdapter.selection(position);
            actionMode.setTitle(selectionAdapter.getPositions().size() + " selected");
            view.isSelected();
            return true;
        });

        if(selectionAdapter.getPositions().contains(position)){
            holder.getSelectionIV().setVisibility(View.VISIBLE);
        }else{
            holder.getSelectionIV().setVisibility(View.INVISIBLE);
        }
    }
    private final AbsListView.MultiChoiceModeListener multiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(android.view.ActionMode actionMode, int i, long l, boolean b) {
            Log.d(TAG, "action mode " + actionMode + " int " + i + " long " + " boolean " + b);
        }

        @Override
        public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.saved_context_menu, menu);
            menu.findItem(R.id.toolbar_save_button).setVisible(true);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
            return false;
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {
            // Respond to clicks on the actions in the CAB
            switch (menuItem.getItemId()) {
                case R.id.toolbar_whatsapp_button:
                    ArrayList<Uri> filelist = new ArrayList<>();
                    for (int i = 0; i < selectionAdapter.getPositions().size(); i++) {
                        filelist.add(Uri.fromFile(statusFolderFiles[selectionAdapter.getPositions().get(i)]));
                    }

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filelist);
                    intent.setType("image/* video/*");
                    intent.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(intent, "share with"));
                    actionMode.finish(); // Action picked, so close the CAB
                    return true;

                case R.id.toolbar_share_button:
                    ArrayList<Uri> filelist2;
                    filelist2 = new ArrayList<>();
                    for (int i = 0; i < selectionAdapter.getPositions().size(); i++) {
                        filelist2.add(Uri.fromFile(statusFolderFiles[selectionAdapter.getPositions().get(i)]));
                    }

                    Intent intent2 = new Intent(Intent.ACTION_SEND);
                    intent2.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filelist2);
                    intent2.setType("image/* video/*");
                    startActivity(Intent.createChooser(intent2, "share with"));
                    actionMode.finish();
                    return true;

                case R.id.toolbar_save_button:
                    Log.d(TAG, "i am save");
                    for (int i = 0; i < selectionAdapter.getPositions().size(); i++) {
                        fileUtility.copyFile(statusFolderFiles[selectionAdapter.getPositions().get(i)],new File(new FolderPaths().getSSStatusFolderPath()));
                        fileUtility.scanFile(new FolderPaths().getSSStatusFolderPath()+"/" +statusFolderFiles[selectionAdapter.getPositions().get(i)].getName().toString(), getContext());

                    }
                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode _actionMode) {
            Log.d(TAG, "i am rip");
            actionMode = null;
            selectionAdapter.stopSelection();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "pause");

        //stop action mode
        if(actionMode != null ){
            actionMode.finish();
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void refresh(){
        FolderPaths folderPaths = new FolderPaths();
        statusFolderFiles = folderPaths.getWAStatusFolderFiles();
        recycleViewAdapter.setNewFiles(statusFolderFiles);
        recycleViewAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "resume");
        refresh();
    }
}