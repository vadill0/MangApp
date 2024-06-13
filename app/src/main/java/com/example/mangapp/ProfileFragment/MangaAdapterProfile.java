package com.example.mangapp.ProfileFragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangapp.ApiClient;
import com.example.mangapp.ApiResponse.MangaData;
import com.example.mangapp.ApiResponse.MangaRelationship;
import com.example.mangapp.ApiService;
import com.example.mangapp.MangaAdapter;
import com.example.mangapp.OnItemClickListener;
import com.example.mangapp.R;

import java.util.List;

public class MangaAdapterProfile extends RecyclerView.Adapter<com.example.mangapp.ProfileFragment.MangaAdapterProfile.MangaViewHolder> {
    private List<MangaData> mangaList;
    private Context context;
    private ApiService apiService;
    private static final String TAG = "APICALL";
    private OnItemClickListener listener;

    public MangaAdapterProfile(Context context, List<MangaData> mangaList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.mangaList = mangaList;
        this.listener = onItemClickListener;
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    @NonNull
    @Override
    public com.example.mangapp.ProfileFragment.MangaAdapterProfile.MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga, parent, false);
        return new com.example.mangapp.ProfileFragment.MangaAdapterProfile.MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.mangapp.ProfileFragment.MangaAdapterProfile.MangaViewHolder holder, int position) {
        MangaData manga = mangaList.get(position);
        holder.title.setText(manga.getAttributes().getTitle().get("en"));

        String mangaId = manga.getId();
        String coverId = null;
        for (MangaRelationship mangaRelationship : manga.getRelationships()) {
            if (mangaRelationship.getType().equals("cover_art")) {
                coverId = mangaRelationship.getId();
                break;
            }
        }

        if (coverId != null) {
            MangaAdapter.loadCoverImage(coverId, holder.coverImage, mangaId, apiService, context);
        } else {
            holder.coverImage.setImageResource(R.drawable.mangaplaceholder); // Set a placeholder image if no cover id is found
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(manga));
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    static class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView title, releaseYear;
        ImageView coverImage;

        MangaViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            coverImage = itemView.findViewById(R.id.coverImage);
            releaseYear = itemView.findViewById(R.id.releaseYear);

            title.setMaxLines(2);
            title.setEllipsize(TextUtils.TruncateAt.END);
        }
    }
}
