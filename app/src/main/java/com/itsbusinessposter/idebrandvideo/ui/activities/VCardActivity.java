package com.itsbusinessposter.idebrandvideo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.databinding.ActivityVcardBinding;
import com.itsbusinessposter.idebrandvideo.items.ItemVcard;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.VCardViwModel;

import java.util.List;

public class VCardActivity extends AppCompatActivity {

    ActivityVcardBinding binding;

    VCardViwModel vCardViwModel;
    CardRVadpter rvadpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVcardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.toolName.setText("Digital VCard");
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        vCardViwModel = new ViewModelProvider(this).get(VCardViwModel.class);

        vCardViwModel.getVCardsData().observe(this, listResource -> {

            if (listResource != null) {

                Util.showLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {
                            setData(listResource.data);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            setData(listResource.data);
//                                        updateForgotBtnStatus();
                        }

                        break;
                    case ERROR:
                        // Error State


                        break;
                    default:

                        break;
                }

            } else {

                // Init Object or Empty Data
                Util.showLog("Empty Data");

            }

        });

        binding.vpCard.setClipToPadding(false);
        binding.vpCard.setClipChildren(false);
        binding.vpCard.setOffscreenPageLimit(3);
        binding.vpCard.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        binding.vpCard.setPageTransformer(compositePageTransformer);
//        binding.vpCard.setPageTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float pos) {
//                final float scale = pos < 0 ? pos + 1f : Math.abs(1f - pos);
//                page.setScaleX(scale);
//                page.setScaleY(scale);
//                page.setPivotX(page.getWidth() * 0.5f);
//                page.setPivotY(page.getHeight() * 0.5f);
//                page.setAlpha(pos < -1f || pos > 1f ? 0f : 1f - (scale - 1f));
//            }
//        });
        rvadpter = new CardRVadpter(this);
        binding.vpCard.setAdapter(rvadpter);

        new TabLayoutMediator(binding.tabLayout, binding.vpCard, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();

//        binding.tabLayout.setupWithViewPager(binding.vpCard, true);

        vCardViwModel.setObj("new");

        binding.btnContact.setOnClickListener(v -> {
            startActivity(new Intent(this, ContactUsActivity.class));
        });

    }

    private void setData(List<ItemVcard> data) {
        rvadpter.setList(data);
    }

    public class CardRVadpter extends RecyclerView.Adapter<CardRVadpter.MyViewHolder> {

        public Activity context;
        public List<ItemVcard> list;

        public CardRVadpter(Activity context) {
            this.context = context;
        }

        public void setList(List<ItemVcard> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vcard, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            if (position >= 5) {
                holder.premium.setText("PREMIUM");
            } else {
                holder.premium.setText("FREE");
            }

            GlideBinding.bindImage(holder.ivCard, list.get(position).link);
            holder.itemView.setOnClickListener(v -> {
                if (position >= 5) {

                    if (Constant.IS_SUBSCRIBED) {
                        Intent intent = new Intent(context, CreateVCardActivity.class);
                        intent.putExtra(Constant.INTENT_FEST_NAME, list.get(position).cardName);
                        context.startActivity(intent);
                    } else {
                        DialogMsg dialogMsg = new DialogMsg(context, true);
                        dialogMsg.showWarningDialog("Premium", "This is Premium Business Card", "Ok", true);
                        dialogMsg.show();
                        dialogMsg.okBtn.setOnClickListener(vs ->{
                            dialogMsg.cancel();
                            Intent intent = new Intent(context, SubsPlanActivity.class);
                            context.startActivity(intent);
                        });
                    }
                } else {
                    Intent intent = new Intent(context, CreateVCardActivity.class);
                    intent.putExtra(Constant.INTENT_FEST_NAME, list.get(position).cardName);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            if (list != null && list.size() > 0) {
                return list.size();
            }
            return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView ivCard;
            TextView premium;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivCard = itemView.findViewById(R.id.iv_card);
                premium = itemView.findViewById(R.id.textView28);
            }
        }
    }
}

