package com.example.mangapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangapp.ApiResponse.MangaListResponse;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {
    private List<MangaListResponse> mangaList;
    private Context context;

    public MangaAdapter(Context context, List<MangaListResponse> mangaList) {
        this.context = context;
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
        MangaListResponse manga = mangaList.get(position);
        //holder.title.setText(manga.getAttributes().getTitle());

        // Set click listeners for the buttons
        holder.button1.setOnClickListener(v -> {
            // Handle button 1 click
        });

        holder.button2.setOnClickListener(v -> {
            // Handle button 2 click
        });

        holder.button3.setOnClickListener(v -> {
            // Handle button 3 click
        });
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    static class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView button1, button2, button3;

        MangaViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            button1 = itemView.findViewById(R.id.buttonRead);
            button2 = itemView.findViewById(R.id.buttonPending);
            button3 = itemView.findViewById(R.id.buttonReading);
        }
    }
}
