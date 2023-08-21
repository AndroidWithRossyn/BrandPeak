package com.itsbusinessposter.idebrandvideo.ui.fragments;

import static com.itsbusinessposter.idebrandvideo.utils.Constant.BUSINESS;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.CATEGORY;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.FESTIVAL;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.SUBS_PLAN;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;

import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.adapters.BusinessCategoryAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.CategoryAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.FeatureAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.FestivalAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.NewsAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.StoryAdapter;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.databinding.FragmentHomeBinding;
import com.itsbusinessposter.idebrandvideo.items.HomeItem;
import com.itsbusinessposter.idebrandvideo.ui.activities.BusinessCategoryActivity;
import com.itsbusinessposter.idebrandvideo.ui.activities.CategoryActivity;
import com.itsbusinessposter.idebrandvideo.ui.activities.DetailActivity;
import com.itsbusinessposter.idebrandvideo.ui.activities.FestivalActivity;
import com.itsbusinessposter.idebrandvideo.ui.activities.NewsDetailActivity;
import com.itsbusinessposter.idebrandvideo.ui.activities.SubsPlanActivity;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.HomeViewModel;
import com.itsbusinessposter.idebrandvideo.viewmodel.NewsViewModel;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    public HomeFragment() {
    }

    HomeViewModel homeViewModel;

    StoryAdapter storyAdapter;

    FestivalAdapter festivalAdapter;

    CategoryAdapter categoryAdapter;

    FeatureAdapter featureAdapter;

    BusinessCategoryAdapter businessCategoryAdapter;

    NewsAdapter newsAdapter;
    NewsViewModel newsViewModel;

    PrefManager prefManager;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        Util.fadeIn(binding.getRoot(), getContext());

        prefManager = new PrefManager(getActivity());

        initViewModel();
        setupUi();
        setData();

        return binding.getRoot();
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        newsViewModel = new ViewModelProvider(getActivity()).get(NewsViewModel.class);

        homeViewModel.getLoadingState().observe(getActivity(), loadingState -> {

            if (loadingState != null && loadingState) {
                binding.swipeRefresh.setRefreshing(true);
                binding.shimmerViewContainer.startShimmer();
                binding.shimmerViewContainer.setVisibility(View.VISIBLE);
                binding.mainContainer.setVisibility(View.GONE);
            } else {
                binding.swipeRefresh.setRefreshing(false);
                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.mainContainer.setVisibility(View.VISIBLE);
            }

        });
    }

    private void setupUi() {
        // Story Region
        storyAdapter = new StoryAdapter(getContext(), item -> {
            if (item.type.equals("externalLink")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.externalLink));
                startActivity(intent);
            } else if (item.type.equals(SUBS_PLAN)) {
                Intent intent = new Intent(getActivity(), SubsPlanActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constant.INTENT_TYPE, item.type);
                intent.putExtra(Constant.INTENT_FEST_ID, item.festivalId);
                intent.putExtra(Constant.INTENT_FEST_NAME, item.title);
                intent.putExtra(Constant.INTENT_POST_IMAGE, "");
                intent.putExtra(Constant.INTENT_VIDEO, item.video);
                startActivity(intent);
            }
        });
        binding.rvStory.setAdapter(storyAdapter);

        //Festival Region
        festivalAdapter = new FestivalAdapter(getContext(), item -> {
            if (!item.isActive) {
                DialogMsg dialogMsg = new DialogMsg(getActivity(), true);
                dialogMsg.showWarningDialog(getContext().getString(R.string.no_festival_image), getContext().getString(R.string.festival_image_create),
                        getContext().getString(R.string.ok), false);
                dialogMsg.show();
                return;
            }
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Constant.INTENT_TYPE, FESTIVAL);
            intent.putExtra(Constant.INTENT_FEST_ID, item.id);
            intent.putExtra(Constant.INTENT_FEST_NAME, item.name);
            intent.putExtra(Constant.INTENT_POST_IMAGE, "");
            intent.putExtra(Constant.INTENT_VIDEO, item.video);
            startActivity(intent);
        }, true);
        binding.rvFestival.setAdapter(festivalAdapter);

        //Category Region
        categoryAdapter = new CategoryAdapter(getContext(), item -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Constant.INTENT_TYPE, CATEGORY);
            intent.putExtra(Constant.INTENT_FEST_ID, item.id);
            intent.putExtra(Constant.INTENT_FEST_NAME, item.name);
            intent.putExtra(Constant.INTENT_POST_IMAGE, "");
            intent.putExtra(Constant.INTENT_VIDEO, item.video);
            startActivity(intent);
        }, true);
        binding.rvCategory.setAdapter(categoryAdapter);
        binding.rvCategory.setNestedScrollingEnabled(false);
        binding.rvCategory.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

        //Custom Category
        businessCategoryAdapter = new BusinessCategoryAdapter(getContext(), item -> {

            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Constant.INTENT_TYPE, BUSINESS);
            intent.putExtra(Constant.INTENT_FEST_ID, item.businessCategoryId);
            intent.putExtra(Constant.INTENT_FEST_NAME, item.businessCategoryName);
            intent.putExtra(Constant.INTENT_POST_IMAGE, "");
            intent.putExtra(Constant.INTENT_VIDEO, item.video);
            startActivity(intent);

        }, true);
        binding.rvBusinessCategory.setAdapter(businessCategoryAdapter);

        featureAdapter = new FeatureAdapter(getContext());
        binding.rvHomeFeature.setAdapter(featureAdapter);
        binding.rvHomeFeature.setNestedScrollingEnabled(false);
        binding.rvHomeFeature.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

        binding.swipeRefresh.setOnRefreshListener(() -> {
            homeViewModel.setHomeObj("New");
            newsViewModel.setNewsObj(String.valueOf(0));
        });

        newsAdapter = new NewsAdapter(getContext(), data -> {
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra(Constant.INTENT_NEWS_ITEM, data);
            getContext().startActivity(intent);
        });
        binding.rvNews.setAdapter(newsAdapter);

        binding.txtViewFestival.setOnClickListener(v -> {
            getContext().startActivity(new Intent(getActivity(), FestivalActivity.class));
        });

        binding.txtViewCategory.setOnClickListener(v -> {
            getContext().startActivity(new Intent(getActivity(), CategoryActivity.class));
        });

        binding.txtViewBusiness.setOnClickListener(v -> {
            getContext().startActivity(new Intent(getActivity(), BusinessCategoryActivity.class));
        });

        if (Config.offerItem!=null && !Config.offerItem.banner.equals("")) {
            GlideBinding.bindImage(binding.ivOffer, Config.offerItem.banner);
            binding.cvOffer.setVisibility(View.VISIBLE);
            ViewPropertyAnimator animate = binding.cvOffer.animate();
            animate.scaleY(0.9f);
            animate.scaleX(0.9f);
            animate.setDuration(1000);
            animate.start();
            animate.setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animation.setRepeatMode(ValueAnimator.REVERSE);
                    animation.setRepeatCount(Animation.INFINITE);
                }
            });

            binding.cvOffer.setOnClickListener(v->{
                startActivity(new Intent(getActivity(), SubsPlanActivity.class));
            });
        }

    }

    private void setData() {

        homeViewModel.setLoadingState(true);

        homeViewModel.setHomeObj("home");
        homeViewModel.getHomeData().observe(getActivity(), resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            setHomeData(resource.data);

                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {
                            setHomeData(resource.data);
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

        newsViewModel.setNewsObj(String.valueOf(1));
        newsViewModel.getNews().observe(getActivity(), resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            if (resource.data.size() > 0) {
                                newsAdapter.setData(resource.data);
                                binding.executePendingBindings();
                            }

                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null && resource.data.size() > 0) {

                            newsAdapter.setData(resource.data);
                            binding.executePendingBindings();
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

    private void setHomeData(HomeItem data) {
        homeViewModel.setLoadingState(false);

        storyAdapter.setItemList(data.storyItemList);

        festivalAdapter.setFestData(data.festivalItemList);

        categoryAdapter.setCategories(data.categoryList);

        featureAdapter.setFeatureItemList(data.featureItemList);

        businessCategoryAdapter.setCategories(data.businessCategoryList);
    }
}