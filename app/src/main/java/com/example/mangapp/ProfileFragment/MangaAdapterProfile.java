package com.example.mangapp.ProfileFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangapp.ApiResponse.MangaData;
import com.example.mangapp.OnItemClickListener;
import com.example.mangapp.R;

import java.util.List;

public class MangaAdapterProfile extends RecyclerView.Adapter<MangaAdapterProfile.MangaViewHolder> {

    private List<MangaData> mangaList;
    private Context context;
    private OnItemClickListener listener;

    public MangaAdapterProfile(Context context, List<MangaData> mangaList, OnItemClickListener listener) {
        this.context = context;
        this.mangaList = mangaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manga, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        MangaData manga = mangaList.get(position);
        holder.title.setText(manga.getAttributes().getTitle().get("en"));

        // Load cover image using Glide or any other library
        // Glide.with(context).load(manga.getCoverUrl()).into(holder.coverImage);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(manga));
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    static class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView coverImage;

        MangaViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            coverImage = itemView.findViewById(R.id.coverImage);
        }
    }
}
