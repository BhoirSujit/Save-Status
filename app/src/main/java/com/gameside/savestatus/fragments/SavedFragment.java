package com.gameside.savestatus.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gameside.savestatus.MediaPlayerActivity;
import com.gameside.savestatus.R;
import com.gameside.savestatus.adapters.RecycleViewAdapter;
import com.gameside.savestatus.adapters.SelectionAdapter;
import com.gameside.savestatus.interfaces.RVAInterface;
import com.gameside.savestatus.utilities.FileUtility;
import com.gameside.savestatus.utilities.FolderPaths;

import java.io.File;
import java.util.ArrayList;

public class SavedFragment extends Fragment implements RVAInterface{
    View view;
    RecycleViewAdapter recycleViewAdapter;
    private final String TAG = "SavedFTAG";
    File[] savedFolderFiles;
    RecyclerView recyclerView;
    SelectionAdapter selectionAdapter;
    ActionMode actionMode;
    FileUtility fileUtility;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_saved, container, false);

        FolderPaths folderPaths = new FolderPaths();
        savedFolderFiles = folderPaths.getSSStatusFolderFiles();

        //call important methods

        fileUtility = new FileUtility();

        setRecycleViewAdapter();


        selectionAdapter = new SelectionAdapter(recyclerView);

        return view;
    }

    private void setRecycleViewAdapter() {
        //Recycle view adapter
        recycleViewAdapter = new RecycleViewAdapter(savedFolderFiles, this);

        //recyclerView
        recyclerView = view.findViewById(R.id.savedRecycleView);
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
                intent.putExtra("filepath", savedFolderFiles[position]);
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
            menu.findItem(R.id.toolbar_delete_button).setVisible(true);
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
                        filelist.add(Uri.fromFile(savedFolderFiles[selectionAdapter.getPositions().get(i)]));
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
                        filelist2.add(Uri.fromFile(savedFolderFiles[selectionAdapter.getPositions().get(i)]));
                    }

                    Intent intent2 = new Intent(Intent.ACTION_SEND);
                    intent2.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filelist2);
                    intent2.setType("image/* video/*");
                    startActivity(Intent.createChooser(intent2, "share with"));
                    actionMode.finish();
                    return true;

                case R.id.toolbar_delete_button:
                    for (int i = 0; i < selectionAdapter.getPositions().size(); i++) {
                        boolean delete = savedFolderFiles[selectionAdapter.getPositions().get(i)].delete();
                        if (!delete){
                            Log.d(TAG, "error in delete");
                        }else{
                            Log.d(TAG, "success delete");
                        }
                    }
                    actionMode.finish();
                    refresh();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode _actionMode) {
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "resume");
        refresh();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh(){
        FolderPaths folderPaths = new FolderPaths();
        savedFolderFiles = folderPaths.getSSStatusFolderFiles();
        recycleViewAdapter.setNewFiles(savedFolderFiles);
        recycleViewAdapter.notifyDataSetChanged();
    }
}