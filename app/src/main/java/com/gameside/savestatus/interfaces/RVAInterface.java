package com.gameside.savestatus.interfaces;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.gameside.savestatus.adapters.RecycleViewAdapter;

public interface RVAInterface {
    void onBindViewHolder(RecycleViewAdapter.ViewHolder holder, int position);
}
