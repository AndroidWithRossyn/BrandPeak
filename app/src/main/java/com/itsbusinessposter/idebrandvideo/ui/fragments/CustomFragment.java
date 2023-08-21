package com.itsbusinessposter.idebrandvideo.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.itsbusinessposter.idebrandvideo.adapters.CustomCategoryAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.CustomInModelAdapter;
import com.itsbusinessposter.idebrandvideo.databinding.FragmentCustomBinding;
import com.itsbusinessposter.idebrandvideo.items.CustomCategory;
import com.itsbusinessposter.idebrandvideo.items.CustomModel;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;
import com.itsbusinessposter.idebrandvideo.ui.activities.CustomCategoryActivity;
import com.itsbusinessposter.idebrandvideo.ui.activities.DetailActivity;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.CategoryViewModel;

public class CustomFragment extends Fragment {

    FragmentCustomBinding binding;

    CategoryViewModel categoryViewModel;

    CustomCategoryAdapter customCategoryAdapter;
    CustomInModelAdapter customInModelAdapter;

    public CustomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentCustomBinding.inflate(getLayoutInflater());

        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);

        setUi();
        setViewModel();

        return binding.getRoot();
    }

    private void setViewModel() {
        categoryViewModel.setCustomModelObj("custom");
        categoryViewModel.getCustomModel().observe(getActivity(), resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            setData(resource.data);

                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {
                            setData(resource.data);
                        }

                        break;
                    case ERROR:
                        // Error State
                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Util.showLog("Empty Data");

            }
        });
    }

    private void setData(CustomModel data) {
        binding.swipeRefresh.setRefreshing(false);
        binding.shimmerViewContainer.stopShimmer();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        if(data.categories.size() > 0) {
            binding.mainContainer.setVisibility(View.VISIBLE);

            customCategoryAdapter.setCategories(data.categories);

            customInModelAdapter.setCustomInModelList(data.customInModelList);
        }else{
            binding.animationView.setVisibility(View.VISIBLE);
            binding.mainContainer.setVisibility(View.GONE);
        }

    }

    private void setUi() {
        customCategoryAdapter = new CustomCategoryAdapter(getActivity(), new ClickListener<CustomCategory>() {
            @Override
            public void onClick(CustomCategory data) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constant.INTENT_TYPE, Constant.CUSTOM);
                intent.putExtra(Constant.INTENT_FEST_ID, data.id);
                intent.putExtra(Constant.INTENT_FEST_NAME, data.title);
                intent.putExtra(Constant.INTENT_POST_IMAGE, "");
                getActivity().startActivity(intent);
            }
        }, true, true);
        binding.rvCustomCategory.setAdapter(customCategoryAdapter);

        customInModelAdapter = new CustomInModelAdapter(getActivity());
        binding.rvCustom.setAdapter(customInModelAdapter);
        binding.rvCustom.setNestedScrollingEnabled(false);
        binding.rvCustom.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        binding.txtViewCategory.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), CustomCategoryActivity.class));
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryViewModel.setCustomModelObj("new");
            }
        });
    }
}