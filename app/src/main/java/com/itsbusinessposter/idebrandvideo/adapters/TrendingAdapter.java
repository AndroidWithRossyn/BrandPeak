package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.MyApplication;
import com.itsbusinessposter.idebrandvideo.databinding.ItemPostBinding;
import com.itsbusinessposter.idebrandvideo.items.PostItem;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder> {

    public Context context;
    public List<PostItem> postItemList;
    private int itemWidth = 0;
    ClickListener<PostItem> listener;

    public TrendingAdapter(Context context, ClickListener<PostItem> listener) {
        this.context = context;
        this.listener = listener;
        itemWidth = MyApplication.getColumnWidth(2, context.getResources().getDimension(com.intuit.ssp.R.dimen._15ssp));
    }

    public void setTrending(List<PostItem> postItemList){
        this.postItemList = postItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.setPostdata(postItemList.get(position));
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) ((MyViewHolder) holder).binding.cvBase.getLayoutParams();
        params.width = itemWidth;
        params.height = itemWidth;

        holder.binding.cvBase.setLayoutParams(params);

        holder.itemView.setOnClickListener(v->{
            listener.onClick(postItemList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if(postItemList!=null && postItemList.size()>0){
            return postItemList.size();
        }else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;
        public MyViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
