package com.itsbusinessposter.idebrandvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.databinding.ItemLanguageBinding;
import com.itsbusinessposter.idebrandvideo.items.LanguageItem;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;

import java.util.ArrayList;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {

    public Context context;
    public ClickListener<LanguageItem> listener;

    public List<LanguageItem> languageItemList;

    public LanguageAdapter(Context context, ClickListener<LanguageItem> listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setLanguageItemList(List<LanguageItem> languageItemList) {
        this.languageItemList = languageItemList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLanguageBinding binding = ItemLanguageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.setLanguageData(languageItemList.get(position));
        int index = 0;
        for (int i = 0; i < position; i++) {
            index++;
            if (index == 10) {
                index = 0;
            }
        }
        String[] colorsTxt = context.getResources().getStringArray(R.array.cat_colors);

//        holder.binding.cvBase.setCardBackgroundColor(Color.parseColor(colorsTxt[index]));

        holder.itemView.setOnClickListener(v->{
            listener.onClick(languageItemList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (languageItemList != null && languageItemList.size() > 0) {
            return languageItemList.size();
        } else {
            return 0;
        }
    }

    public List<LanguageItem> getSelectedItems() {

        List<LanguageItem> selectedItems = new ArrayList<>();
        if (languageItemList!=null) {
            for (LanguageItem item : languageItemList) {
                if (item.isChecked) {
                    selectedItems.add(item);
                }
            }
        }
        return selectedItems;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemLanguageBinding binding;

        public MyViewHolder(ItemLanguageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
