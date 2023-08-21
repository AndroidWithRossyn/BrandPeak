package com.itsbusinessposter.idebrandvideo.ui.fragments;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itsbusinessposter.idebrandvideo.adapters.NewsAdapter;
import com.itsbusinessposter.idebrandvideo.databinding.FragmentNewsBinding;
import com.itsbusinessposter.idebrandvideo.ui.activities.NewsDetailActivity;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.NewsViewModel;

public class NewsFragment extends Fragment {


    public NewsFragment() {
        // Required empty public constructor
    }

    FragmentNewsBinding binding;
    NewsViewModel newsViewModel;
    NewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(getLayoutInflater());

        newsViewModel = new ViewModelProvider(getActivity()).get(NewsViewModel.class);

        setUpUi();
        setUpViewModel();


        return binding.getRoot();
    }

    private void setUpUi() {
        adapter = new NewsAdapter(getActivity(), item -> {
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra(Constant.INTENT_NEWS_ITEM, item);
            startActivity(intent);
        });
        binding.rvNews.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.rvNews.setVisibility(GONE);
                binding.shimmerViewContainer.startShimmer();
                binding.shimmerViewContainer.setVisibility(View.VISIBLE);
                newsViewModel.setNewsObj(String.valueOf(0));
            }
        });
    }

    public void showVisibility(boolean isVisible) {
        binding.swipeRefresh.setRefreshing(false);
        if (isVisible) {
            binding.rvNews.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
            binding.animationView.setVisibility(View.GONE);
        } else {
            binding.rvNews.setVisibility(GONE);
            binding.animationView.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
        }
    }

    private void setUpViewModel() {

        newsViewModel.setNewsObj(String.valueOf(0));

        newsViewModel.getNews().observe(getActivity(), resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            if (resource.data.size() > 0) {
                                adapter.setData(resource.data);
                                showVisibility(true);
                            }
                        } else {
                            showVisibility(false);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null && resource.data.size() > 0) {
                            adapter.setData(resource.data);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
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
                showVisibility(false);
                Util.showLog("Empty Data");

            }
        });
    }
}