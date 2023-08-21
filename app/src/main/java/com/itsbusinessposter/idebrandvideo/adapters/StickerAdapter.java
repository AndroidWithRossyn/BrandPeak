package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.MyApplication;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.items.StickerItem;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.MyViewHolder> {

    Context context;
    List<StickerItem> stickers;
    ClickListener<StickerItem> listener;
    private int itemWidth = 0;

    public StickerAdapter(Context context, List<StickerItem> stickers, ClickListener<StickerItem> listener) {
        this.context = context;
        this.stickers = stickers;
        this.listener = listener;
        itemWidth = MyApplication.getColumnWidth(4, context.getResources().getDimension(com.intuit.ssp.R.dimen._2ssp));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GlideBinding.bindImage(holder.mImageView, stickers.get(position).stickerImage);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mImageView.getLayoutParams();
        params.height = itemWidth;
        params.width = itemWidth;

        holder.mImageView.setLayoutParams(params);

        holder.itemView.setOnClickListener(v -> {
            listener.onClick(stickers.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imgSticker);
        }
    }
}
