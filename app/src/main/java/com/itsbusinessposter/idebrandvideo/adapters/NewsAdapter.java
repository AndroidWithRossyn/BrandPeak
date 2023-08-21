package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.databinding.ItemNewsBinding;
import com.itsbusinessposter.idebrandvideo.items.NewsItem;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context context;
    ClickListener<NewsItem> listener;

    public NewsAdapter(Context context, ClickListener<NewsItem> listener) {
        this.context = context;
        this.listener = listener;
    }

    List<NewsItem> newsItems;

    public void setData(List<NewsItem> newsItems) {
        this.newsItems = newsItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setNewdata(newsItems.get(position));
        holder.itemView.setOnClickListener(v->{
            listener.onClick(newsItems.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (newsItems != null && newsItems.size() > 0) {
            return newsItems.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemNewsBinding binding;

        public MyViewHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
