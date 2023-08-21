package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.databinding.ItemCustomCategoryBinding;
import com.itsbusinessposter.idebrandvideo.databinding.ItemCustomModelCategoryBinding;
import com.itsbusinessposter.idebrandvideo.items.CustomCategory;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.List;

public class CustomCategoryAdapter extends RecyclerView.Adapter {

    public Context context;
    public ClickListener<CustomCategory> listener;
    public List<CustomCategory> CustomCategoryList;
    boolean isHome, isCustom;

    public CustomCategoryAdapter(Context context, ClickListener<CustomCategory> listener, boolean isHome, boolean isCustom) {
        this.context = context;
        this.listener = listener;
        this.isHome = isHome;
        this.isCustom = isCustom;
    }

    public void setCategories(List<CustomCategory> categories) {
        this.CustomCategoryList = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isCustom) {
            ItemCustomModelCategoryBinding binding = ItemCustomModelCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyCustomViewHolder(binding);
        } else {
            ItemCustomCategoryBinding binding = ItemCustomCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).binding.setCategoryData(CustomCategoryList.get(position));
            int index = 0;
            for (int i = 0; i < position; i++) {
                index++;
                if (index == 10) {
                    index = 0;
                }
            }
            String[] colorsTxt = context.getResources().getStringArray(R.array.cat_colors);

           // ((MyViewHolder) holder).binding.catCard.setCardBackgroundColor(Color.parseColor(colorsTxt[index]));

            ((MyViewHolder) holder).itemView.setOnClickListener(v -> {
                listener.onClick(CustomCategoryList.get(position));
            });
        }else{
            ((MyCustomViewHolder) holder).binding.setCategoryData(CustomCategoryList.get(position));
            int index = 0;
            for (int i = 0; i < position; i++) {
                index++;
                if (index == 10) {
                    index = 0;
                }
            }
            String[] colorsTxt = context.getResources().getStringArray(R.array.cat_colors);

           ((MyCustomViewHolder) holder).binding.catCard.setCardBackgroundColor(Color.parseColor(colorsTxt[index]));

            ((MyCustomViewHolder) holder).itemView.setOnClickListener(v -> {
                listener.onClick(CustomCategoryList.get(position));
            });
        }
    }

    @Override
    public int getItemCount() {
        if (CustomCategoryList != null && CustomCategoryList.size() > 0) {
            if (CustomCategoryList.size() > Config.HOME_BUSINESS_CATEGORY_SHOW && isHome) {
                return Config.HOME_BUSINESS_CATEGORY_SHOW;
            } else {
                return CustomCategoryList.size();
            }
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCustomCategoryBinding binding;

        public MyViewHolder(@NonNull ItemCustomCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder {
        ItemCustomModelCategoryBinding binding;

        public MyCustomViewHolder(@NonNull ItemCustomModelCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
