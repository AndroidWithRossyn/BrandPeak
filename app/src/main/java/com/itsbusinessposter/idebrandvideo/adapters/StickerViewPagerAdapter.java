package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.databinding.LayoutStickerBinding;
import com.itsbusinessposter.idebrandvideo.items.StickerItem;
import com.itsbusinessposter.idebrandvideo.items.StickerModel;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.List;

public class StickerViewPagerAdapter extends RecyclerView.Adapter<StickerViewPagerAdapter.MyViewHolder> {

    public Context context;
    public List<StickerModel> stickerModelList;
    public ClickListener<StickerItem> listener;

    public StickerViewPagerAdapter(Context context, ClickListener<StickerItem> listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<StickerModel> stickerModelList) {
        this.stickerModelList = stickerModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutStickerBinding binding = LayoutStickerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        StickerAdapter adapter = new StickerAdapter(context, stickerModelList.get(position).stickers, new ClickListener<StickerItem>() {
            @Override
            public void onClick(StickerItem data) {
                listener.onClick(data);
            }
        });
        holder.binding.rvSticker.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        if (stickerModelList != null && stickerModelList.size() > 0) {
            return stickerModelList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutStickerBinding binding;

        public MyViewHolder(LayoutStickerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
