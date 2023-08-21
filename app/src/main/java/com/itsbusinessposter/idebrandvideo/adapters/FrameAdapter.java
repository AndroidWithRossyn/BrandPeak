package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.MyApplication;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.databinding.ItemFrameBinding;
import com.itsbusinessposter.idebrandvideo.items.FrameItem;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.List;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.MyViewHolder> {

    public Context context;
    public List<FrameItem> frameItemList;
    public ClickListener<Integer> listener;
    private int selectedPos = 0;
    private int itemWidth = 0;
    int column;
    float width;

    public FrameAdapter(Context context, ClickListener<Integer> listener, int column, float width) {
        this.context = context;
        this.listener = listener;
        itemWidth = MyApplication.getColumnWidth(column, width);
    }

    public void setFrameItemList(List<FrameItem> frameItemList) {
        this.frameItemList = frameItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFrameBinding binding = ItemFrameBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.cvBase.getLayoutParams();
//        params.width = itemWidth;
//        params.height = itemWidth;
//        holder.binding.cvBase.setLayoutParams(params);
        holder.binding.setFrameData(frameItemList.get(position));
        if (frameItemList.get(position).is_from_url){
            GlideBinding.bindImage(holder.binding.ivPost, frameItemList.get(position).imageUrl);
        }else{
            holder.binding.flPost.removeAllViews();
            //holder.binding.flPost.addView(frameItemList.get(position).layout);
            holder.binding.ivPost.setImageResource(frameItemList.get(position).previewImage);
        }
        if (selectedPos == holder.getAdapterPosition()) {
            holder.binding.cvBase.setCardBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.active_color)));
        } else {
            holder.binding.cvBase.setCardBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.transparent_color)));
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onClick(position);
        });
    }
    public void setSelected(int pos) {
        int oldPos = selectedPos;
        selectedPos = pos;
        notifyItemChanged(oldPos);
        notifyItemChanged(pos);
    }

    public int getSelectedPos() {
        return selectedPos;
    }


    @Override
    public int getItemCount() {
        if (frameItemList != null && frameItemList.size() > 0) {
            return frameItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemFrameBinding binding;

        public MyViewHolder(ItemFrameBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
