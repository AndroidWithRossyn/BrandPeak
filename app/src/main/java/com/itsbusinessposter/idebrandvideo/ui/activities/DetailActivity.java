package com.itsbusinessposter.idebrandvideo.ui.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.BUSINESS;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.CATEGORY;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.CUSTOM;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.FESTIVAL;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.INTENT_POS;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.INTENT_POST_IMAGE;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.INTENT_POST_LIST;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.INTENT_TYPE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.tabs.TabLayout;
import com.itsbusinessposter.idebrandvideo.Ads.BannerAdManager;
import com.itsbusinessposter.idebrandvideo.MyApplication;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.adapters.DetailAdapter;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.databinding.ActivityDetailBinding;
import com.itsbusinessposter.idebrandvideo.items.BusinessItem;
import com.itsbusinessposter.idebrandvideo.items.PostItem;
import com.itsbusinessposter.idebrandvideo.items.UserItem;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.ui.dialog.LanguageDialog;
import com.itsbusinessposter.idebrandvideo.utils.Connectivity;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.viewmodel.PostViewModel;
import com.itsbusinessposter.idebrandvideo.viewmodel.UserViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    int screenWidth;
    String type;

    DetailAdapter adapter;

    PostViewModel postViewModel;

    PrefManager prefManager;

    List<PostItem> postItemList;
    int position = 0;

    UserViewModel userViewModel;
    UserItem userItem;
    BusinessItem businessItem;
    Connectivity connectivity;
    DialogMsg dialogMsg;

    ExoPlayer absPlayerInternal;
    public boolean isVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        screenWidth = MyApplication.getColumnWidth(1, getResources().getDimension(com.intuit.ssp.R.dimen._10ssp));
        prefManager = new PrefManager(this);
        connectivity = new Connectivity(this);
        dialogMsg = new DialogMsg(this, false);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getDbUserData(prefManager.getString(Constant.USER_ID)).observe(this, resource -> {
            if (resource != null) {
                userItem = resource.user;
            }
        });

        userViewModel.getDefaultBusiness().observe(this, item -> {
            if (item != null) {
                businessItem = item;
            }
        });

        postItemList = new ArrayList<>();

        BannerAdManager.showBannerAds(this, binding.llAdview);
//        binding.tabLayout.selectTab(binding.tabImage.);



        setShimmerEffect();

        setUpUi();
        loadImages();
    }

    private void loadImages() {
        if (type.equals(Constant.FESTIVAL)) {
            loadFestival(false);
            if (!getIntent().getStringExtra(INTENT_POST_IMAGE).equals("")) {
                GlideBinding.bindImage(binding.ivShow, getIntent().getStringExtra(INTENT_POST_IMAGE));
            }
        } else if (type.equals(CATEGORY)) {
            loadCategory(false);
            if (!getIntent().getStringExtra(INTENT_POST_IMAGE).equals("")) {
                GlideBinding.bindImage(binding.ivShow, getIntent().getStringExtra(INTENT_POST_IMAGE));
            }
        } else if (type.equals(BUSINESS)) {
            loadBusinessCategory(false);
            if (!getIntent().getStringExtra(INTENT_POST_IMAGE).equals("")) {
                GlideBinding.bindImage(binding.ivShow, getIntent().getStringExtra(INTENT_POST_IMAGE));
            }
        } else if (type.equals(CUSTOM)) {
            binding.tabLayout.setVisibility(GONE);
            loadCustom(false);
            if (!getIntent().getStringExtra(INTENT_POST_IMAGE).equals("")) {
                GlideBinding.bindImage(binding.ivShow, getIntent().getStringExtra(INTENT_POST_IMAGE));
            }
        }
    }

    public void loadVideos() {
        if (getIntent() != null) {
            type = getIntent().getStringExtra(Constant.INTENT_TYPE);
        }
        if (type.equals(Constant.FESTIVAL)) {
            loadFestival(true);
        } else if (type.equals(CATEGORY)) {
            loadCategory(true);
        } else if (type.equals(BUSINESS)) {
            loadBusinessCategory(true);
        } else if (type.equals(CUSTOM)) {

        }
    }

    private void setUpUi() {

        if (getIntent() != null) {
            type = getIntent().getStringExtra(Constant.INTENT_TYPE);
            isVideo = getIntent().getBooleanExtra(Constant.INTENT_VIDEO, false);
        }

        binding.toolbar.toolbarIvMenu.setBackground(getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            if (absPlayerInternal != null) {
                absPlayerInternal.setPlayWhenReady(false);
                absPlayerInternal.stop();
                absPlayerInternal.seekTo(0);
            }
            onBackPressed();
        });

        if (type.equals(BUSINESS)) {
            binding.toolbar.toolbarIvLanguage.setVisibility(GONE);
        } else {
            binding.toolbar.toolbarIvLanguage.setVisibility(VISIBLE);
        }
        binding.toolbar.txtEdit.setVisibility(VISIBLE);

        binding.toolbar.toolbarIvLanguage.setOnClickListener(v -> {
            LanguageDialog dialog = new LanguageDialog(this, data -> {
                postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID), type,
                        prefManager.getString(Constant.USER_LANGUAGE), isVideo);
            });
            dialog.showDialog();
        });

        binding.toolbar.txtEdit.setOnClickListener(v -> {
            if (postItemList != null && postItemList.size() > 0) {

                if (!connectivity.isConnected()) {
                    dialogMsg.showErrorDialog(getString(R.string.error_message__no_internet), getString(R.string.ok));
                    dialogMsg.show();
                    return;
                }

                if (!prefManager.getBoolean(Constant.IS_LOGIN)) {
                    dialogMsg.showWarningDialog(getString(R.string.please_login), getString(R.string.login_first_login), getString(R.string.login_login), false);
                    dialogMsg.show();
                    dialogMsg.okBtn.setOnClickListener(view -> {
                        dialogMsg.cancel();
                        startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                    });
                    return;
                }

                if (businessItem == null) {
                    dialogMsg.showWarningDialog(getString(R.string.add_business_titles), getString(R.string.please_add_business_titles), getString(R.string.add_business),
                            true);
                    dialogMsg.show();
                    dialogMsg.okBtn.setOnClickListener(view -> {
                        dialogMsg.cancel();
                        startActivity(new Intent(DetailActivity.this, AddBusinessActivity.class));
                    });
                    return;
                }

                if (!userItem.isSubscribed && postItemList.get(position).is_premium) {
                    dialogMsg.showWarningDialog(getString(R.string.premium), getString(R.string.please_subscribe), getString(R.string.subscribe),
                            true);
                    dialogMsg.show();
                    dialogMsg.okBtn.setOnClickListener(view -> {
                        dialogMsg.cancel();
                        startActivity(new Intent(DetailActivity.this, SubsPlanActivity.class));
                    });
                    return;
                }

                if (absPlayerInternal != null) {
                    absPlayerInternal.setPlayWhenReady(false);
                    absPlayerInternal.stop();
                    absPlayerInternal.seekTo(0);
                }

                Intent intent = new Intent(this, EditorActivity.class);
                intent.putExtra(INTENT_POST_LIST, (Serializable) postItemList);
                intent.putExtra(INTENT_POS, position);
                intent.putExtra(INTENT_TYPE, postItemList.get(position).type);
                startActivity(intent);
            }
        });

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.ivShow.getLayoutParams();
        params.width = screenWidth;
        params.height = screenWidth;

        binding.ivShow.setLayoutParams(params);

        ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) binding.videoPlayer.getLayoutParams();
        params2.width = screenWidth;
        params2.height = screenWidth;

        binding.videoPlayer.setLayoutParams(params2);

        adapter = new DetailAdapter(this, new ClickListener<Integer>() {
            @Override
            public void onClick(Integer data) {
                if (postItemList != null) {
                    position = data;
                    setImageShow(postItemList.get(data));
                }
            }
        }, 3, getResources().getDimension(com.intuit.ssp.R.dimen._2ssp));
        binding.rvPost.setAdapter(adapter);

        postViewModel.getById().observe(this, resource -> {

            if (resource != null) {

                com.itsbusinessposter.idebrandvideo.utils.Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB
                        if (resource.data != null) {

                            if (resource.data.size() > 0) {
                                setData(resource.data);
                                binding.executePendingBindings();
                            } else {
                                showError();
                            }

                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null && resource.data.size() > 0) {
                            setData(resource.data);
                            binding.executePendingBindings();
                        } else {
                            showError();
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
                com.itsbusinessposter.idebrandvideo.utils.Util.showLog("Empty Data");

            }
        });

        binding.ivPlayVideo.setOnClickListener(v -> {
            binding.ivPlayVideo.setVisibility(GONE);
            absPlayerInternal.seekTo(0);
            absPlayerInternal.setPlayWhenReady(true);
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.tabLayout.selectTab(tab);
                com.itsbusinessposter.idebrandvideo.utils.Util.showLog("" + tab.getId()
                 + tab.getText());
                if (tab.getText() == getString(R.string.image)) {
                    showLoading();
                    loadImages();
                    if (absPlayerInternal != null) {
                        absPlayerInternal.setPlayWhenReady(false);
                        absPlayerInternal.stop();
                        absPlayerInternal.seekTo(0);
                    }
                } else {
                    showLoading();
                    loadVideos();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void loadFestival(boolean isVideo) {
        binding.toolbar.toolName.setText(getIntent().getStringExtra(Constant.INTENT_FEST_NAME));

        postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID), FESTIVAL,
                "", isVideo);

    }

    private void loadBusinessCategory(boolean isVideo) {
        binding.toolbar.toolName.setText(getIntent().getStringExtra(Constant.INTENT_FEST_NAME));
        postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID),
                BUSINESS, "", isVideo);

    }

    private void loadCategory(boolean isVideo) {
        binding.toolbar.toolName.setText(getIntent().getStringExtra(Constant.INTENT_FEST_NAME));
        postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID),
                CATEGORY, "", isVideo);

    }

    private void loadCustom(boolean isVideo) {
        binding.toolbar.toolName.setText(getIntent().getStringExtra(Constant.INTENT_FEST_NAME));
        postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID),
                CUSTOM, "", isVideo);
    }

    private void showError() {
        binding.animationView.setVisibility(VISIBLE);
        binding.shimmerViewContainer.setVisibility(GONE);
        binding.videoPlayer.setVisibility(GONE);
        binding.tabLayout.setVisibility(GONE);
        binding.ivShow.setVisibility(View.GONE);
        binding.rvPost.setVisibility(GONE);
    }

    public void showLoading() {
        binding.animationView.setVisibility(GONE);
        binding.shimmerViewContainer.setVisibility(VISIBLE);
        binding.tabLayout.setVisibility(GONE);
        binding.videoPlayer.setVisibility(GONE);
        binding.ivShow.setVisibility(View.GONE);
        binding.rvPost.setVisibility(GONE);
    }

    private void setData(List<PostItem> data) {
        if(!isVideo){
            binding.tabLayout.setVisibility(GONE);
        }else{
            binding.tabLayout.setVisibility(VISIBLE);
        }
        postItemList.clear();
        postItemList.addAll(data);
        binding.ivShow.setVisibility(VISIBLE);
        binding.rvPost.setVisibility(VISIBLE);
        binding.shimmerViewContainer.setVisibility(GONE);
        binding.animationView.setVisibility(View.GONE);

        adapter.setData(data);

        if (type.equals(FESTIVAL) || type.equals(CATEGORY) || type.equals(CUSTOM) || type.equals(BUSINESS)) {
            if (getIntent().getStringExtra(INTENT_POST_IMAGE).equals("")) {
                position = new Random().nextInt(data.size());
                setImageShow(data.get(position));
            }
            if(!getIntent().getStringExtra(INTENT_POST_IMAGE).equals("") && isVideo){
                position = new Random().nextInt(data.size());
                setImageShow(data.get(position));
            }
        }
    }

    private void setImageShow(PostItem postItem) {
        if (postItem.is_video) {
            binding.ivCrossVideo.setVisibility(postItem.is_premium == true ? VISIBLE : GONE);
            if (userItem != null && userItem.isSubscribed) {
                binding.ivCrossVideo.setVisibility(GONE);
            }
            if (absPlayerInternal != null) {
                absPlayerInternal.setPlayWhenReady(false);
                absPlayerInternal.stop();
                absPlayerInternal.seekTo(0);
            }
            loadVideo(postItem.image_url);
        } else {
            binding.videoPlayer.setVisibility(GONE);
            binding.ivCross.setVisibility(postItem.is_premium == true ? VISIBLE : GONE);
            if (userItem != null && userItem.isSubscribed) {
                binding.ivCross.setVisibility(GONE);
            }
            binding.ivShow.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
            GlideBinding.bindImage(binding.ivShow, postItem.image_url);
        }
    }

    private void loadVideo(String videoURL) {
        binding.videoPlayer.setVisibility(VISIBLE);
        binding.videoPlayer.setUseController(false);
        binding.videoPlayer.setControllerHideOnTouch(true);
        binding.videoPlayer.setShowBuffering(true);
        TrackSelector trackSelectorDef = new DefaultTrackSelector();
        absPlayerInternal = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef); //creating a player instance

        int appNameStringRes = R.string.app_name;
        String userAgent = Util.getUserAgent(this, this.getString(appNameStringRes));
        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
        Uri uriOfContentUrl = Uri.parse(videoURL);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source

        absPlayerInternal.prepare(mediaSource);
        absPlayerInternal.setPlayWhenReady(true); // start loading video and play it at the moment a chunk of it is available offline

        binding.videoPlayer.setPlayer(absPlayerInternal); // attach surface to the view
        binding.videoPlayer.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
        absPlayerInternal.addListener(new Player.EventListener() {

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Player.EventListener.super.onLoadingChanged(isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);

                Log.e("EXXO", "Player: " + playbackState);
                switch (playbackState) {
                    case ExoPlayer.STATE_ENDED:
                        binding.ivPlayVideo.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    private void setShimmerEffect() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) binding.place.imageView2.getLayoutParams();
        lp.width = screenWidth;
        lp.height = screenWidth;

        binding.place.imageView2.setLayoutParams(lp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (absPlayerInternal != null) {
            absPlayerInternal.setPlayWhenReady(false);
            absPlayerInternal.stop();
            absPlayerInternal.seekTo(0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (absPlayerInternal != null) {
            absPlayerInternal.setPlayWhenReady(false);
            absPlayerInternal.stop();
            absPlayerInternal.seekTo(0);
        }
    }
}