package com.example.mangapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangapp.ApiResponse.Data;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {
    private List<Data> mangaList;

    public MangaAdapter(List<Data> mangaList) {
        this.mangaList = mangaList;
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Data manga = mangaList.get(position);
        holder.mangaTitle.setText(manga.getAttributes().getTitle().get("property1"));
        // Set other attributes as needed
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    static class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView mangaTitle;
        ImageView mangaCover;
        // Other views

        MangaViewHolder(View itemView) {
            super(itemView);
            mangaTitle = itemView.findViewById(R.id.mangaTitle);
            mangaCover = itemView.findViewById(R.id.mangaCover);
            // Initialize other views
        }
    }
}