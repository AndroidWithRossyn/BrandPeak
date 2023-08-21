package com.itsbusinessposter.idebrandvideo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.databinding.ItemFeatureBinding;
import com.itsbusinessposter.idebrandvideo.items.CustomInModel;
import com.itsbusinessposter.idebrandvideo.items.PostItem;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;
import com.itsbusinessposter.idebrandvideo.ui.activities.DetailActivity;
import com.itsbusinessposter.idebrandvideo.utils.Constant;

import java.util.List;

public class CustomInModelAdapter extends RecyclerView.Adapter<CustomInModelAdapter.MyViewHolder> {


    Context context;
    public List<CustomInModel> customInModelList;

    public CustomInModelAdapter(Context context) {
        this.context = context;
    }

    public void setCustomInModelList(List<CustomInModel> customInModelList) {
        this.customInModelList = customInModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeatureBinding binding = ItemFeatureBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (position % 2 == 0) {
            holder.binding.mainConstraint.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.binding.txtViewTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Constant.INTENT_TYPE, Constant.CUSTOM);
                intent.putExtra(Constant.INTENT_FEST_ID, customInModelList.get(position).id);
                intent.putExtra(Constant.INTENT_FEST_NAME, customInModelList.get(position).title);
                intent.putExtra(Constant.INTENT_POST_IMAGE, "");
                context.startActivity(intent);
            }
        });

        holder.adapters = new TrendingAdapter(context, new ClickListener<PostItem>() {
            @Override
            public void onClick(PostItem data) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Constant.INTENT_TYPE, data.type);
                intent.putExtra(Constant.INTENT_FEST_ID, data.fest_id);
                intent.putExtra(Constant.INTENT_FEST_NAME, customInModelList.get(position).title);
                intent.putExtra(Constant.INTENT_POST_IMAGE, data.image_url);
                context.startActivity(intent);
            }
        });
        holder.binding.rvFeature.setAdapter(holder.adapters);

        holder.binding.tvFeature.setText(customInModelList.get(position).title);
        holder.adapters.setTrending(customInModelList.get(position).postItemList);

    }

    @Override
    public int getItemCount() {
        if (customInModelList != null && customInModelList.size() > 0) {
            return customInModelList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemFeatureBinding binding;
        TrendingAdapter adapters;

        public MyViewHolder(@NonNull ItemFeatureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
