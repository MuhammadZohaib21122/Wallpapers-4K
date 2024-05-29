package com.example.a4k8kwallpaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final List<Wallpaper> imageList;
    private final OnItemClickListener onItemClickListener;
    private final Context context;

    public interface OnItemClickListener {
        void onItemClick(String imageResId);
    }

    public ImageAdapter(Context context, List<Wallpaper> imageList, OnItemClickListener onItemClickListener) {
        this.imageList = imageList;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Wallpaper wallpaper = imageList.get(position);
        Glide.with(context)
                .load(wallpaper.getImageToShow())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image))
                .thumbnail(0.5f)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(wallpaper.getImageToUse()));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void addItems(List<Integer> newItems) {
        int startPosition = imageList.size();
        notifyItemRangeInserted(startPosition, newItems.size());
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}