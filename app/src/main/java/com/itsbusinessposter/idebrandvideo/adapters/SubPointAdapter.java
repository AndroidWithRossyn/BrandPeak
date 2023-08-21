package com.itsbusinessposter.idebrandvideo.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.databinding.ItemSubPointBinding;

import java.util.List;

public class SubPointAdapter extends RecyclerView.Adapter<SubPointAdapter.MyViewHolder> {

    List<String> subsPointItemList;

    public SubPointAdapter(List<String> subsPointItemList) {
        this.subsPointItemList = subsPointItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubPointBinding binding = ItemSubPointBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setPointdata(subsPointItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return subsPointItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemSubPointBinding binding;

        public MyViewHolder(ItemSubPointBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
