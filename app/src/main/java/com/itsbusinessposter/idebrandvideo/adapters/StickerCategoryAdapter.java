package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.databinding.ItemCustomModelCategoryBinding;
import com.itsbusinessposter.idebrandvideo.databinding.ItemStickerCategoryBinding;
import com.itsbusinessposter.idebrandvideo.items.StickerCategory;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.List;

public class StickerCategoryAdapter extends RecyclerView.Adapter<StickerCategoryAdapter.MyViewHolder> {

    public ClickListener<Integer> listener;
    public List<StickerCategory> categoryList;
    public Context mContext;
    private int selectedPos = 0;

    public StickerCategoryAdapter(Context context, ClickListener<Integer> listener) {
        this.listener = listener;
        this.mContext = context;
    }

    public void setCategories(List<StickerCategory> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStickerCategoryBinding binding = ItemStickerCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.setCategoryData(categoryList.get(position));
        holder.itemView.setOnClickListener(v -> {
            setSelected(position);
            listener.onClick(position);
        });
        if (position == selectedPos) {
            holder.binding.txtCategory.setTextColor(Color.BLACK);
            holder.binding.lvText.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        } else {
            holder.binding.txtCategory.setTextColor(Color.WHITE);
            holder.binding.lvText.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.blue_grey_900)));
        }
    }

    public void setSelected(int pos) {
        int oldPos = selectedPos;
        selectedPos = pos;
        notifyItemChanged(oldPos);
        notifyItemChanged(pos);
    }

    public int getSelectedItem() {
        return selectedPos;
    }

    @Override
    public int getItemCount() {
        if (categoryList != null && categoryList.size() > 0) {
            return categoryList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemStickerCategoryBinding binding;

        public MyViewHolder(ItemStickerCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
