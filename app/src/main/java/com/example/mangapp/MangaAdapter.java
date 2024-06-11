package com.example.mangapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangapp.ApiResponse.CoverData;
import com.example.mangapp.ApiResponse.CoverResponseModel;
import com.example.mangapp.ApiResponse.MangaData;
import com.example.mangapp.ApiResponse.MangaRelationship;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {
    private List<MangaData> mangaList;
    private Context context;
    private ApiService apiService;
    private static final String TAG = "APICALL";
    private OnItemClickListener listener;

    public MangaAdapter(Context context, List<MangaData> mangaList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.mangaList = mangaList;
        this.listener = onItemClickListener;
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        MangaData manga = mangaList.get(position);
        holder.title.setText(manga.getAttributes().getTitle().get("en"));
        if(manga.getAttributes().getYear() == 0){
            Calendar calendar = Calendar.getInstance();
            holder.releaseYear.setText(new StringBuilder().append("Release year:\n").append(calendar.get(Calendar.YEAR)));
        }else{
            holder.releaseYear.setText(new StringBuilder().append("Release year:\n").append(manga.getAttributes().getYear()).toString());
        }



        String mangaId = manga.getId();
        String coverId = null;
        for (MangaRelationship mangaRelationship : manga.getRelationships()) {
            if (mangaRelationship.getType().equals("cover_art")) {
                coverId = mangaRelationship.getId();
                break;
            }
        }

        if (coverId != null) {
            loadCoverImage(coverId, holder.coverImage, mangaId, apiService, context);
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


    public static void loadCoverImage(String coverId, ImageView coverImageView, String mangaId, ApiService apiService, Context context) {
        Call<CoverResponseModel> call = apiService.getCover(coverId);

        call.enqueue(new Callback<CoverResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<CoverResponseModel> call, @NonNull Response<CoverResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CoverData cover = response.body().getData();
                    String coverFileName = cover.getAttributes().getFileName();
                    String imageURL = ApiClient.COVER_URL + mangaId + "/" + coverFileName;
                    Glide.with(context).load(imageURL).into(coverImageView);
                } else {
                    Log.d(TAG, "Cover response not successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoverResponseModel> call, @NonNull Throwable t) {
                Log.e(TAG, String.valueOf(t.getCause()));
            }
        });
    }

}
