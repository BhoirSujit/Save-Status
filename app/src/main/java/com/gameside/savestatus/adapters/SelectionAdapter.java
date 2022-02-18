package com.gameside.savestatus.adapters;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gameside.savestatus.R;

import java.util.ArrayList;
import java.util.Objects;

public class SelectionAdapter {
    private final RecyclerView recyclerView;
    private final ArrayList<Integer> positionsArray = new ArrayList<>();
    private final String TAG = "SATAG";

    public SelectionAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void selection(int position){
        Log.d(TAG, " position access " + position);
        if (!positionsArray.contains(position)){
            getImageView(position).setVisibility(View.VISIBLE);
            positionsArray.add(positionsArray.size(), position);
        }else {
            getImageView(position).setVisibility(View.INVISIBLE);
            positionsArray.remove(Integer.valueOf(position));
        }
    }

    public void stopSelection(){
        for (int i = 0; i < positionsArray.size(); i++) {
            ImageView imageView;
            imageView = getImageView(positionsArray.get(i));
            if (imageView != null){
                imageView.setVisibility(View.INVISIBLE);
            }
        }
        positionsArray.clear();
    }

    private ImageView getImageView(int position){
//        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
//        Log.d(TAG, "getimage position is " + position);
//        View view = viewHolder.itemView;
//        return view.findViewById(R.id.isSelected);

//        return recyclerView
//                .findViewHolderForLayoutPosition(position)
//                .itemView
//                .findViewById(R.id.isSelected);
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        Log.d(TAG, "getimage position is " + position);
        ImageView imageView = null;
        if (viewHolder != null){
            View view = viewHolder.itemView;
            imageView = view.findViewById(R.id.isSelected);
        }
        return imageView;
    }



    public ArrayList<Integer> getPositions(){
        return positionsArray;
    }

}
