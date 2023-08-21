package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.databinding.ItemStoryBinding;
import com.itsbusinessposter.idebrandvideo.items.StoryItem;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.MyViewHolder> {


    public Context context;
    public List<StoryItem> storyItemList;
    public ClickListener<StoryItem> listener;

    public StoryAdapter(Context context, ClickListener<StoryItem> listener) {
        this.listener = listener;
        this.context = context;
    }

    public void setItemList(List<StoryItem> storyItemList) {
        this.storyItemList = storyItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStoryBinding binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setStoryData(storyItemList.get(position));

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(10000);
        rotate.setFillAfter(true);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setInterpolator(new LinearInterpolator());
        holder.binding.ivStoryBgg.startAnimation(rotate);

        holder.itemView.setOnClickListener(v->{
            listener.onClick(storyItemList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (storyItemList != null) {
            return storyItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemStoryBinding binding;

        public MyViewHolder(@NonNull ItemStoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
