package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.databinding.ItemCustomCategoryBinding;
import com.itsbusinessposter.idebrandvideo.items.BusinessCategoryItem;
import com.itsbusinessposter.idebrandvideo.items.CustomCategory;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.List;

public class BusinessCategoryAdapter extends RecyclerView.Adapter<BusinessCategoryAdapter.MyViewHolder> {

    public Context context;
    public ClickListener<BusinessCategoryItem> listener;
    public List<BusinessCategoryItem> CustomCategoryList;
    boolean isHome;

    public BusinessCategoryAdapter(Context context, ClickListener<BusinessCategoryItem> listener, boolean isHome) {
        this.context = context;
        this.listener = listener;
        this.isHome = isHome;
    }

    public void setCategories(List<BusinessCategoryItem> categories) {
        this.CustomCategoryList = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCustomCategoryBinding binding = ItemCustomCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

               holder.binding.setCategoryData(new CustomCategory(CustomCategoryList.get(position).businessCategoryId,
                CustomCategoryList.get(position).businessCategoryName, CustomCategoryList.get(position).businessCategoryIcon));

        int index = 0;
        for (int i = 0; i < position; i++) {
            index++;
            if (index == 10) {
                index = 0;
            }
        }
        String[] colorsTxt = context.getResources().getStringArray(R.array.cat_colors);

      //  holder.binding.catCard.setCardBackgroundColor(Color.parseColor(colorsTxt[index]));

        holder.itemView.setOnClickListener(v->{
            listener.onClick(CustomCategoryList.get(position));
        });
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
}
