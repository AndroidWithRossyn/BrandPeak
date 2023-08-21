package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.databinding.ItemFontBinding;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.MyViewHolder> {

    Context context;
    List<String> fontList;
    ClickListener<String> listener;

    public FontAdapter(Context context, List<String> fontList, ClickListener<String> listener) {
        this.context = context;
        this.fontList = fontList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFontBinding binding = ItemFontBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.btnFont.setText(context.getResources().getString(R.string.select));
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/" + fontList.get(position));
        holder.binding.btnFont.setTypeface(typeface);
        holder.binding.btnFont.setOnClickListener(v->{
            listener.onClick(fontList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemFontBinding binding;

        public MyViewHolder(ItemFontBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
