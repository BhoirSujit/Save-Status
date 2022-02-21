package com.gameside.savestatus.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gameside.savestatus.MainActivity;
import com.gameside.savestatus.R;
import com.gameside.savestatus.utilities.FileUtility;
import com.gameside.savestatus.utilities.FolderPaths;

import java.io.File;


public class ToolsFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tools, container, false);

        FolderPaths folderPaths = new FolderPaths();
        FileUtility fileUtility = new FileUtility();

        view.findViewById(R.id.save_all_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < folderPaths.getWAStatusFolderFiles().length; i++) {
                    fileUtility.copyFile(folderPaths.getWAStatusFolderFiles()[i], new File(folderPaths.getSSStatusFolderPath()));
                    fileUtility.scanFile(folderPaths.getSSStatusFolderPath()+"/"+folderPaths.getWAStatusFolderFiles()[i].getName(), getContext());
                }
                ((MainActivity) requireActivity()).admobUtility.loadInterstitialAd(getContext());
                Toast.makeText(getContext(), "File Saved successfully", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}