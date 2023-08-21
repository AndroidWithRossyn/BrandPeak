package com.itsbusinessposter.idebrandvideo.ui.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.FFprobe;
import com.arthenica.mobileffmpeg.MediaInformation;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;
import com.itsbusinessposter.idebrandvideo.Ads.InterstitialAdManager;
import com.itsbusinessposter.idebrandvideo.BuildConfig;
import com.itsbusinessposter.idebrandvideo.MyApplication;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.adapters.DetailAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.FontAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.FrameAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.StickerAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.StickerCategoryAdapter;
import com.itsbusinessposter.idebrandvideo.adapters.StickerViewPagerAdapter;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.databinding.ActivityEditorBinding;
import com.itsbusinessposter.idebrandvideo.databinding.Frame1Binding;
import com.itsbusinessposter.idebrandvideo.databinding.Frame2Binding;
import com.itsbusinessposter.idebrandvideo.databinding.Frame3Binding;
import com.itsbusinessposter.idebrandvideo.databinding.Frame4Binding;
import com.itsbusinessposter.idebrandvideo.databinding.Frame5Binding;
import com.itsbusinessposter.idebrandvideo.databinding.Frame6Binding;
import com.itsbusinessposter.idebrandvideo.databinding.Frame7Binding;
import com.itsbusinessposter.idebrandvideo.databinding.Frame8Binding;
import com.itsbusinessposter.idebrandvideo.items.AddTextItem;
import com.itsbusinessposter.idebrandvideo.items.BusinessItem;
import com.itsbusinessposter.idebrandvideo.items.FrameItem;
import com.itsbusinessposter.idebrandvideo.items.PostItem;
import com.itsbusinessposter.idebrandvideo.items.UserItem;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.ui.stickers.ElementInfo;
import com.itsbusinessposter.idebrandvideo.ui.stickers.RelStickerView;
import com.itsbusinessposter.idebrandvideo.ui.stickers.ViewIdGenerator;
import com.itsbusinessposter.idebrandvideo.utils.BitmapUtil;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.ImageUtils;
import com.itsbusinessposter.idebrandvideo.utils.OnDragTouchListener;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.BusinessViewModel;
import com.itsbusinessposter.idebrandvideo.viewmodel.PostViewModel;
import com.itsbusinessposter.idebrandvideo.viewmodel.UserViewModel;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EditorActivity extends AppCompatActivity implements InterstitialAdManager.Listener {

    ActivityEditorBinding binding;
    DetailAdapter adapter;
    List<PostItem> postItemList;
    int pos;
    int screenWidth;
    float sWidth;
    float sHeight;
    FrameAdapter frameAdapter;
    List<FrameItem> frameItemList;
    BusinessItem businessItem;
    BusinessViewModel businessViewModel;
    private FrameLayout fl_logo, fl_name, fl_phone, fl_email, fl_website, fl_address;
    private ImageView iv_logo, iv_image, iv_frame, iv_phone, iv_email, iv_website, iv_address, iv_close_phone, iv_close_name, iv_close_website, iv_close_email, iv_close_address;
    private TextView tv_name, tv_website, tv_phone, tv_email, tv_address;
    boolean isFromUrl;
    int selectedTextPosition = -1;
    ArrayList<AddTextItem> addTextList = new ArrayList<>();
    Uri uri;
    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int SELECT_PICTURE_CAMERA = 805;
    private static final int SELECT_PICTURE_GALLERY = 807;

    ProgressDialog progressDialog;
    private String name = "";
    String fileName = "";
    boolean isImage = false;
    UserItem userItem;
    PrefManager prefManager;
    DialogMsg dialogMsg;
    UserViewModel userViewModel;

    ExoPlayer absPlayerInternal;
    ExoPlayer sharePlayer;

    boolean isVideo = false;
    ProgressDialog progress, progressDD, progressLoading;
    String VideoPath;
    String FOLDER_NAME = "";

    PostViewModel postViewModel;
    StickerCategoryAdapter stickerCategoryAdapter;
    StickerViewPagerAdapter strAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);

        if (prefManager.getString(Constant.FOLDER_NAME).equals("")) {
            FOLDER_NAME = "video_function";
            prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
        } else {
            FOLDER_NAME = prefManager.getString(Constant.FOLDER_NAME);
        }

        dialogMsg = new DialogMsg(this, false);
        progress = new ProgressDialog(this);
        progressDD = new ProgressDialog(this);
        progressLoading = new ProgressDialog(this);
        progressLoading.setMessage("Loading...");
        progressLoading.setCancelable(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.sWidth = (float) displayMetrics.widthPixels;
        this.sHeight = (float) (displayMetrics.heightPixels - ImageUtils.dpToPx(this, 104));

        frameItemList = new ArrayList<>();

        InterstitialAdManager.Interstitial(EditorActivity.this, this);

        setUpUi();
        setUpViewModel();
        setPostData();
        loadFrameData();
        loadSticker();
    }

    private void setUpViewModel() {
        businessViewModel = new ViewModelProvider(this).get(BusinessViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        userViewModel.getDbUserData(prefManager.getString(Constant.USER_ID)).observe(this, result -> {
            if (result != null) {
                userItem = result.user;
            }
        });

        businessItem = businessViewModel.getDefaultBusiness();
        LoadLogo loadLogo = new LoadLogo(businessItem.logo);
        loadLogo.execute();
    }

    private void loadFrameData() {

        frameAdapter = new FrameAdapter(this, position -> {
            if (!userItem.isSubscribed && frameItemList.get(position).is_premium) {
                dialogMsg.showWarningDialog(getString(R.string.premium), getString(R.string.please_subscribe_frame), getString(R.string.subscribe),
                        true);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(view -> {
                    dialogMsg.cancel();
                    startActivity(new Intent(EditorActivity.this, SubsPlanActivity.class));
                });
                return;
            }
            frameAdapter.setSelected(position);
            setFrameData(frameItemList.get(position));
        }, 3, getResources().getDimension(com.intuit.ssp.R.dimen._2ssp));

        binding.rvFrame.setAdapter(frameAdapter);

        userViewModel.getFrameData(prefManager.getString(Constant.USER_ID)).observe(this, result -> {
            if (result != null) {
                if (result.data != null) {
                    frameItemList.clear();
                    frameItemList.add(new FrameItem(false, "", Frame1Binding.inflate(getLayoutInflater()).getRoot(), R.drawable.frame_preview_1, false));
                    frameItemList.add(new FrameItem(false, "", Frame2Binding.inflate(getLayoutInflater()).getRoot(), R.drawable.frame_preview_2, false));
                    frameItemList.add(new FrameItem(false, "", Frame3Binding.inflate(getLayoutInflater()).getRoot(), R.drawable.frame_preview_3, false));
                    frameItemList.add(new FrameItem(false, "", Frame4Binding.inflate(getLayoutInflater()).getRoot(), R.drawable.frame_preview_4, false));
                    frameItemList.add(new FrameItem(false, "", Frame5Binding.inflate(getLayoutInflater()).getRoot(), R.drawable.frame_preview_5, true));
                    frameItemList.add(new FrameItem(false, "", Frame6Binding.inflate(getLayoutInflater()).getRoot(), R.drawable.frame_preview_6, true));
                    frameItemList.add(new FrameItem(false, "", Frame7Binding.inflate(getLayoutInflater()).getRoot(), R.drawable.frame_preview_7, true));
                    frameItemList.add(new FrameItem(false, "", Frame8Binding.inflate(getLayoutInflater()).getRoot(), R.drawable.frame_preview_8, true));
                    for (int i = 0; i < result.data.size(); i++) {
                        frameItemList.add(new FrameItem(true, result.data.get(i).imageUrl, null, 0, true));
                    }
                    frameAdapter.setFrameItemList(frameItemList);
                    setFrameData(frameItemList.get(0));
                }
            }
        });

    }

    private void setFrameData(FrameItem frame) {
        isFromUrl = frame.is_from_url;
        if (frame.is_from_url) {
            binding.ivFrame.setVisibility(VISIBLE);
            binding.flFrame.removeAllViews();
            GlideBinding.bindImage(binding.ivFrame, frame.imageUrl);
        } else {
            binding.ivFrame.setVisibility(GONE);
            binding.flFrame.setVisibility(VISIBLE);
            binding.flFrame.removeAllViews();
            View frameView = frame.layout;
            binding.flFrame.addView(frameView);

            fl_logo = frameView.findViewById(R.id.fl_logo);
            fl_name = frameView.findViewById(R.id.fl_name);
            fl_email = frameView.findViewById(R.id.fl_email);
            fl_website = frameView.findViewById(R.id.fl_website);
            fl_phone = frameView.findViewById(R.id.fl_phone);
            fl_address = frameView.findViewById(R.id.fl_address);

            tv_name = frameView.findViewById(R.id.tv_name);
            tv_website = frameView.findViewById(R.id.tv_website);
            tv_phone = frameView.findViewById(R.id.tv_phone);
            tv_email = frameView.findViewById(R.id.tv_email);
            tv_address = frameView.findViewById(R.id.tv_address);
            iv_logo = frameView.findViewById(R.id.iv_logo);

            iv_phone = frameView.findViewById(R.id.iv_phone);
            iv_email = frameView.findViewById(R.id.ivEmail);
            iv_website = frameView.findViewById(R.id.iv_website);
            iv_address = frameView.findViewById(R.id.iv_address);

            iv_close_phone = frameView.findViewById(R.id.iv_phone_close);
            iv_close_name = frameView.findViewById(R.id.iv_name_close);
            iv_close_email = frameView.findViewById(R.id.iv_email_close);
            iv_close_website = frameView.findViewById(R.id.iv_website_close);
            iv_close_address = frameView.findViewById(R.id.iv_address_close);

            fl_logo.setVisibility(VISIBLE);

            binding.cbName.setChecked(true);
            binding.cbEmail.setChecked(true);
            binding.cbPhone.setChecked(true);
            binding.cbWebsite.setChecked(true);
            binding.cbAdress.setChecked(true);
            binding.cbLogo.setChecked(true);

            tv_name.setText(businessItem.name);
            tv_email.setText(businessItem.email);
            tv_phone.setText(businessItem.phone);
            tv_website.setText(businessItem.website);
            tv_address.setText(businessItem.address);

            GlideBinding.bindImage(iv_logo, businessItem.logo);

            setTextClick();
        }
    }

    private void setTextClick() {
        tv_name.setOnClickListener(v -> {
            removeControl();
            int visible = iv_close_name.getVisibility();
            tv_name.setBackgroundResource(visible == 0 ? 0 : R.drawable.rounded_border);
            iv_close_name.setVisibility(visible == 0 ? GONE : VISIBLE);
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
        });

        tv_address.setOnClickListener(v -> {
            removeControl();
            int visible = iv_close_address.getVisibility();
            tv_address.setBackgroundResource(visible == 0 ? 0 : R.drawable.rounded_border);
            iv_close_address.setVisibility(visible == 0 ? GONE : VISIBLE);
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
        });

        tv_phone.setOnClickListener(v -> {
            removeControl();
            int visible = iv_close_phone.getVisibility();
            tv_phone.setBackgroundResource(visible == 0 ? 0 : R.drawable.rounded_border);
            iv_close_phone.setVisibility(visible == 0 ? GONE : VISIBLE);
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
        });

        tv_website.setOnClickListener(v -> {
            removeControl();
            int visible = iv_close_website.getVisibility();
            tv_website.setBackgroundResource(visible == 0 ? 0 : R.drawable.rounded_border);
            iv_close_website.setVisibility(visible == 0 ? GONE : VISIBLE);
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
        });

        tv_email.setOnClickListener(v -> {
            removeControl();
            int visible = iv_close_email.getVisibility();
            tv_email.setBackgroundResource(visible == 0 ? 0 : R.drawable.rounded_border);
            iv_close_email.setVisibility(visible == 0 ? GONE : VISIBLE);
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
        });
    }

    private void setUpUi() {
        binding.toolbar.toolbarIvMenu.setBackground(getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            if (absPlayerInternal != null) {
                absPlayerInternal.setPlayWhenReady(false);
                absPlayerInternal.stop();
                absPlayerInternal.seekTo(0);
            }
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
            }
            onBackPressed();
        });
        binding.toolbar.toolName.setText(getString(R.string.create));

        screenWidth = MyApplication.getColumnWidth(1, getResources().getDimension(com.intuit.ssp.R.dimen._10ssp));

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.cvBase.getLayoutParams();
        params.width = screenWidth;
        params.height = screenWidth;

        binding.cvBase.setLayoutParams(params);

//        binding.ivFestImage.setOnClickListener(v -> {
//            removeControl();
//        });

        progressDialog = new ProgressDialog(EditorActivity.this);

        binding.toolbar.txtEdit.setText(getResources().getString(R.string.save));
        binding.toolbar.txtEdit.setVisibility(VISIBLE);
        binding.toolbar.txtEdit.setOnClickListener(v -> {
            setBackImage();
            removeControl();
            Dexter.withContext(this).withPermissions(PERMISSIONS).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {

                        if (absPlayerInternal != null) {
                            absPlayerInternal.setPlayWhenReady(false);
                            absPlayerInternal.stop();
                            absPlayerInternal.seekTo(0);
                        }
                        if (isVideo) {
                            DownloadTask downloadTask = new DownloadTask(EditorActivity.this);
                            downloadTask.execute(postItemList.get(pos).image_url);
                        } else {
                            fileName = System.currentTimeMillis() + ".jpeg";
                            new LoadSaveImage().execute();
                        }
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        showSettingsDialog();
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(new PermissionRequestErrorListener() {
                public void onError(DexterError dexterError) {
                    Toast.makeText(EditorActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                }
            }).onSameThread().check();
        });

        // ****** Bottom Menu Click *******

        binding.tvFrame.setOnClickListener(v -> {
            removeControl();
            int visible = binding.rlFrame.getVisibility();
            binding.rlFrame.setVisibility(visible == 0 ? GONE : VISIBLE);
            binding.rlBusiness.setVisibility(GONE);
            binding.rlTextSize.setVisibility(GONE);
            binding.rlTextFont.setVisibility(GONE);
            binding.rlSticker.setVisibility(GONE);
        });

        binding.tvBusiness.setOnClickListener(v -> {
            removeControl();
            int visible = binding.rlBusiness.getVisibility();
            binding.rlBusiness.setVisibility(visible == 0 ? GONE : VISIBLE);
            binding.rlFrame.setVisibility(GONE);
            binding.rlTextSize.setVisibility(GONE);
            binding.rlTextFont.setVisibility(GONE);
            binding.rlSticker.setVisibility(GONE);
        });

        binding.tvTextSize.setOnClickListener(v -> {
            removeControl();
            int visible = binding.rlTextSize.getVisibility();
            binding.rlTextSize.setVisibility(visible == 0 ? GONE : VISIBLE);
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
            binding.rlTextFont.setVisibility(GONE);
            binding.rlSticker.setVisibility(GONE);
        });

        binding.tvAddText.setOnClickListener(v -> {
            removeControl();
            addText(-1);
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
            binding.rlTextSize.setVisibility(GONE);
            binding.rlTextFont.setVisibility(GONE);
            binding.rlSticker.setVisibility(GONE);
        });

        binding.tvTextColor.setOnClickListener(v -> {
            removeControl();
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
            binding.rlTextSize.setVisibility(GONE);
            binding.rlTextFont.setVisibility(GONE);
            binding.rlSticker.setVisibility(GONE);
            ColorPickerDialog pickerDialog = ColorPickerDialog.newBuilder()
                    .setDialogId(1)
                    .setColor(Color.BLACK)
                    .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                    .setDialogTitle(R.string.app_name)
                    .setAllowCustom(true)
                    .setPresets(getResources().getIntArray(R.array.colorlist))
                    .setShowAlphaSlider(false)
                    .setColorShape(0)
                    .create();

            pickerDialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
                @Override
                public void onColorSelected(int dialogId, int color) {
                    if (!isFromUrl) {
                        if (!isTextSelected() && iv_close_name.getVisibility() == View.GONE && iv_close_website.getVisibility() == View.GONE && iv_close_email.getVisibility() == View.GONE &&
                                iv_close_address.getVisibility() == View.GONE && iv_close_phone.getVisibility() == View.GONE) {
                            tv_address.setTextColor(color);
                            tv_email.setTextColor(color);
                            tv_phone.setTextColor(color);
                            tv_website.setTextColor(color);
                            tv_name.setTextColor(color);

                            iv_phone.setImageTintList(ColorStateList.valueOf(color));
                            iv_email.setImageTintList(ColorStateList.valueOf(color));
                            iv_website.setImageTintList(ColorStateList.valueOf(color));
                            iv_address.setImageTintList(ColorStateList.valueOf(color));

                            for (int i = 0; i < addTextList.size(); i++) {
                                addTextList.get(i).tv_text.setTextColor(color);
                            }
                        } else {
                            if (iv_close_name.getVisibility() == View.VISIBLE) {
                                tv_name.setTextColor(color);
                            }
                            if (iv_close_phone.getVisibility() == View.VISIBLE) {
                                tv_phone.setTextColor(color);
                                iv_phone.setImageTintList(ColorStateList.valueOf(color));
                            }
                            if (iv_close_email.getVisibility() == View.VISIBLE) {
                                tv_email.setTextColor(color);
                                iv_email.setImageTintList(ColorStateList.valueOf(color));
                            }
                            if (iv_close_address.getVisibility() == View.VISIBLE) {
                                tv_address.setTextColor(color);
                                iv_address.setImageTintList(ColorStateList.valueOf(color));
                            }
                            if (iv_close_website.getVisibility() == View.VISIBLE) {
                                tv_website.setTextColor(color);
                                iv_website.setImageTintList(ColorStateList.valueOf(color));
                            }
                            if (isTextSelected() && addTextList.size() > 0) {
                                addTextList.get(selectedTextPosition).tv_text.setTextColor(color);
                            }
                        }
                    } else {
                        if (isTextSelected() && addTextList.size() > 0) {
                            addTextList.get(selectedTextPosition).tv_text.setTextColor(color);
                        }
                    }
                }

                @Override
                public void onDialogDismissed(int dialogId) {

                }
            });
            pickerDialog.show(Objects.requireNonNull(getSupportFragmentManager()), "color");
        });

        binding.tvFont.setOnClickListener(v -> {
            removeControl();
            int visible = binding.rlTextFont.getVisibility();
            binding.rlTextFont.setVisibility(visible == 0 ? GONE : VISIBLE);
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
            binding.rlTextSize.setVisibility(GONE);
            binding.rlSticker.setVisibility(GONE);
            showFont();
        });

        binding.tvAddImage.setOnClickListener(v -> {
            removeControl();
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
            binding.rlTextSize.setVisibility(GONE);
            binding.rlTextFont.setVisibility(GONE);
            binding.rlSticker.setVisibility(GONE);
            addImage();
        });

        binding.tvAddSticker.setOnClickListener(v -> {
            int visible = binding.rlSticker.getVisibility();
            binding.rvSticker.setVisibility(GONE);
            binding.vpStickers.setVisibility(VISIBLE);
            binding.rvStickerCategory.setVisibility(VISIBLE);
            binding.etSearchStickers.setText("");
            binding.rlSticker.setVisibility(visible == 0 ? GONE : VISIBLE);
            SlideUpAnimation(binding.rlSticker);
            binding.rlFrame.setVisibility(GONE);
            binding.rlBusiness.setVisibility(GONE);
            binding.rlTextSize.setVisibility(GONE);
            binding.rlTextFont.setVisibility(GONE);
            binding.vpStickers.setCurrentItem(0);
        });

        binding.ivCancel.setOnClickListener(v -> {
            SlideDownAnimation(binding.rlSticker);
            binding.rlSticker.setVisibility(GONE);
        });

        // ********* Check Box Region ******

        binding.cbName.setOnClickListener(v -> {
            int visible = fl_name.getVisibility();
            fl_name.setVisibility(visible == 0 ? GONE : VISIBLE);
            visible = fl_name.getVisibility();
            binding.cbName.setChecked(visible == 0 ? true : false);
        });

        binding.cbAdress.setOnClickListener(v -> {
            int visible = fl_address.getVisibility();
            fl_address.setVisibility(visible == 0 ? GONE : VISIBLE);
            visible = fl_address.getVisibility();
            binding.cbAdress.setChecked(visible == 0 ? true : false);
        });

        binding.cbPhone.setOnClickListener(v -> {
            int visible = fl_phone.getVisibility();
            fl_phone.setVisibility(visible == 0 ? GONE : VISIBLE);
            visible = fl_phone.getVisibility();
            binding.cbPhone.setChecked(visible == 0 ? true : false);
        });

        binding.cbWebsite.setOnClickListener(v -> {
            int visible = fl_website.getVisibility();
            fl_website.setVisibility(visible == 0 ? GONE : VISIBLE);
            visible = fl_website.getVisibility();
            binding.cbWebsite.setChecked(visible == 0 ? true : false);
        });

        binding.cbEmail.setOnClickListener(v -> {
            int visible = fl_email.getVisibility();
            fl_email.setVisibility(visible == 0 ? GONE : VISIBLE);
            visible = fl_email.getVisibility();
            binding.cbEmail.setChecked(visible == 0 ? true : false);
        });

        binding.cbLogo.setOnClickListener(v -> {
            int visible = fl_logo.getVisibility();
            fl_logo.setVisibility(visible == 0 ? GONE : VISIBLE);
            visible = fl_logo.getVisibility();
            binding.cbLogo.setChecked(visible == 0 ? true : false);
        });

        // ***** Sliders ******

        binding.sdSize.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                changeFontSize(value);
            }
        });
    }

    public void SlideUpAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_up);
        view.startAnimation(animation);
    }

    public void SlideDownAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_down);
        view.startAnimation(animation);
    }

    @Override
    public void onAdFailedToLoad() {

    }

    @Override
    public void onAdDismissed() {
        showPreviewDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!InterstitialAdManager.isLoaded()) {
            InterstitialAdManager.LoadAds();
        }
    }

    public class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;

        public DownloadTask(Context context) {
            this.context = context;
            progress.setMessage("Downloading...");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            try {
                progress.setIndeterminate(true);
            } catch (Exception e) {

            }
            progress.setProgress(0);
            progress.show();

        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                java.net.URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                input = connection.getInputStream();

                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/"
                        + "." + FOLDER_NAME + "/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdir();
                } else {
                    for (File file : dir.listFiles()) {
                        file.delete();
                    }
                    dir.mkdir();
                }

                File filename = new File(dir, "video.mp4");

                if (filename.exists()) {
                    getContentResolver().delete(Uri.fromFile(filename), null, null);
                }
                output = new FileOutputStream(filename);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0)
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progres) {
            super.onProgressUpdate(progres);
            progress.setIndeterminate(false);
            progress.setMax(100);
            progress.setProgress(progres[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            MediaScannerConnection.scanFile(EditorActivity.this,
                    new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" +
                            "." + FOLDER_NAME + "/" + "video.mp4"}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String newpath, Uri newuri) {
                            Util.showLog("ExternalStorage: Scanned " + newpath + ":");
                            Util.showLog("ExternalStorage: -> uri=" + newuri);

                            progress.dismiss();
                            startCreating();
                        }
                    });

//            CreatingTask creatingTask = new CreatingTask(EditorActivity.this);
//            creatingTask.execute(postItemList.get(pos).image_url);
        }
    }

    private void startCreating() {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!userItem.isSubscribed) {
                    binding.ivFrameWatermark.setVisibility(VISIBLE);
                } else {
                    binding.ivFrameWatermark.setVisibility(GONE);
                }
                if (!frameItemList.get(frameAdapter.getSelectedPos()).is_from_url) {
                    removeControl();
                    tv_name.setBackground(null);
                    tv_email.setBackground(null);
                    tv_phone.setBackground(null);
                    tv_website.setBackground(null);
                    tv_address.setBackground(null);

                    iv_close_name.setVisibility(View.GONE);
                    iv_close_email.setVisibility(View.GONE);
                    iv_close_phone.setVisibility(View.GONE);
                    iv_close_website.setVisibility(View.GONE);
                    iv_close_address.setVisibility(View.GONE);

                    if (isTextSelected()) {
                        setTextSelected(addTextList.get(selectedTextPosition).tv_text,
                                addTextList.get(selectedTextPosition).iv_close, addTextList.get(selectedTextPosition).iv_edit, false);
                    }
                } else {
                    if (isTextSelected()) {
                        setTextSelected(addTextList.get(selectedTextPosition).tv_text,
                                addTextList.get(selectedTextPosition).iv_close, addTextList.get(selectedTextPosition).iv_edit, false);
                    }
                }

                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/"
                        + FOLDER_NAME + "BrandPeakVideos.mp4";
                File dir = new File(path);
                if (dir.exists()) {
                    dir.delete();
                }

                binding.flForVideo.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createScaledBitmap(viewToBitmap(binding.flForVideo), 1080,
                        1080, true);
                String strPath = saveImage(bitmap);

                StringBuilder outputDir = new StringBuilder();
                File file4 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                outputDir.append(file4.getAbsolutePath());
                outputDir.append("/" + "." + FOLDER_NAME + "/");
                outputDir.append("BrandPeakVideos.mp4");

                StringBuilder inputDir = new StringBuilder();
                File file5 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                inputDir.append(file5.getAbsolutePath());
                inputDir.append("/" + "." + FOLDER_NAME + "/");
                inputDir.append("video.mp4");

                MediaInformation info = FFprobe.getMediaInformation(inputDir.toString());
                if (info != null) {
                    Util.showLog("" + info.getFilename() + info.getMediaProperties());
                } else {
                    Util.showToast(EditorActivity.this, "Try Again");
                    if (FOLDER_NAME.equals("video_function")) {
                        FOLDER_NAME = "video_" + System.currentTimeMillis();
                        prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                    } else if (FOLDER_NAME.equals("video_function_a")) {
                        FOLDER_NAME = "video_function_b";
                        prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                    }
                    return;
                }
                progressDD.setMessage("Creating...");
                progressDD.setCancelable(false);
                progressDD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDD.setIndeterminate(true);
                progressDD.setProgress(0);
                progressDD.show();

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(inputDir.toString());
                long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                try {
                    retriever.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                com.arthenica.mobileffmpeg.Config.enableStatisticsCallback(new StatisticsCallback() {
                    @Override
                    public void apply(Statistics statistics) {
                        float progresss = Float.parseFloat(String.valueOf(statistics.getTime())) / duration;
                        float progressFinal = progresss * 100;
                        try {
                            progressDD.setIndeterminate(false);
                        } catch (Exception e) {

                        }
                        progressDD.setMax(100);
                        progressDD.setProgress((int) progressFinal);
                    }
                });

                long executionId = FFmpeg.executeAsync(new String[]{"-i", info.getFilename(), "-i", strPath,
                        "-filter_complex", /*"overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2"*/
                        "overlay", "-r", "150",
                        "-vb", "20M",
                        "-y", outputDir.toString()}, new ExecuteCallback() {
                    @Override
                    public void apply(long executionId, int returnCode) {
                        Util.showLog("FFM: " + executionId + " " + returnCode);
                        if (returnCode == 1) {
                            FFmpeg.cancel(executionId);
                            progressDD.dismiss();
                            Util.showToast(EditorActivity.this, "Try Again!!");
                            if (FOLDER_NAME.equals("video_function")) {
                                FOLDER_NAME = "video_" + System.currentTimeMillis();
                                prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                            } else if (FOLDER_NAME.equals("video_function_a")) {
                                FOLDER_NAME = "video_function_b";
                                prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                            } else if (FOLDER_NAME.equals("video_function_b")) {
                                FOLDER_NAME = "video_function_c";
                                prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                            } else if (FOLDER_NAME.equals("video_function_c")) {
                                FOLDER_NAME = "video_function_d";
                                prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                            } else if (FOLDER_NAME.equals("video_function_d")) {
                                FOLDER_NAME = "video_function_e";
                                prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                            } else if (FOLDER_NAME.equals("video_function_e")) {
                                FOLDER_NAME = "video_function_f";
                                prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                            } else if (FOLDER_NAME.equals("video_function_f")) {
                                FOLDER_NAME = "video_function_g";
                                prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                            } else if (FOLDER_NAME.equals("video_function_g")) {
                                FOLDER_NAME = "video_function_h";
                                prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                            } else if (FOLDER_NAME.equals("video_function_h")) {
                                FOLDER_NAME = "video_function_i";
                                prefManager.setString(Constant.FOLDER_NAME, FOLDER_NAME);
                            }
                        }
                        if (returnCode == 0) {
                            FFmpeg.cancel(executionId);
                            progressDD.dismiss();
                            binding.ivFrameWatermark.setVisibility(GONE);
                            if (InterstitialAdManager.isLoaded() && prefManager.getInt(Constant.CLICK) >= prefManager.getInt(Constant.INTERSTITIAL_AD_CLICK)) {
                                prefManager.setInt(Constant.CLICK, 0);
                                InterstitialAdManager.showAds();
                            } else {
                                prefManager.setInt(Constant.CLICK, prefManager.getInt(Constant.CLICK) + 1);
                                showPreviewDialog();
                            }
                        } else if (returnCode == 255) {
                            Log.e("mobile-ffmpeg", "Command execution cancelled by user.");
                        } else {
                            String str = String.format("Command execution failed with rc=%d and the output below.",
                                    Arrays.copyOf(new Object[]{Integer.valueOf(returnCode)}, 1));
                            Log.i("mobile-ffmpeg", str);
                        }
                    }
                });

                binding.flForVideo.setDrawingCacheEnabled(false);

                VideoPath = outputDir.toString();
            }
        });
    }

    public class CreatingTask extends AsyncTask<String, Integer, String> {

        private Context context;

        public CreatingTask(Context context) {
            this.context = context;
            progress.setMessage("Creating...");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!userItem.isSubscribed) {
                binding.ivFrameWatermark.setVisibility(VISIBLE);
            } else {
                binding.ivFrameWatermark.setVisibility(GONE);
            }
            if (!frameItemList.get(frameAdapter.getSelectedPos()).is_from_url) {
                removeControl();
                tv_name.setBackground(null);
                tv_email.setBackground(null);
                tv_phone.setBackground(null);
                tv_website.setBackground(null);
                tv_address.setBackground(null);

                iv_close_name.setVisibility(View.GONE);
                iv_close_email.setVisibility(View.GONE);
                iv_close_phone.setVisibility(View.GONE);
                iv_close_website.setVisibility(View.GONE);
                iv_close_address.setVisibility(View.GONE);

                if (isTextSelected()) {
                    setTextSelected(addTextList.get(selectedTextPosition).tv_text,
                            addTextList.get(selectedTextPosition).iv_close, addTextList.get(selectedTextPosition).iv_edit, false);
                }
            } else {
                if (isTextSelected()) {
                    setTextSelected(addTextList.get(selectedTextPosition).tv_text,
                            addTextList.get(selectedTextPosition).iv_close, addTextList.get(selectedTextPosition).iv_edit, false);
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int i = Integer.parseInt(s);
            if (i == 0) {
                progress.dismiss();
                binding.ivFrameWatermark.setVisibility(GONE);
                if (InterstitialAdManager.isLoaded() && prefManager.getInt(Constant.CLICK) >= prefManager.getInt(Constant.INTERSTITIAL_AD_CLICK)) {
                    prefManager.setInt(Constant.CLICK, 0);
                    InterstitialAdManager.showAds();
                } else {
                    prefManager.setInt(Constant.CLICK, prefManager.getInt(Constant.CLICK) + 1);
                    showPreviewDialog();
                }
            } else if (i == 255) {
                Log.e("mobile-ffmpeg", "Command execution cancelled by user.");
            } else {
                String str = String.format("Command execution failed with rc=%d and the output below.",
                        Arrays.copyOf(new Object[]{Integer.valueOf(i)}, 1));
                Log.i("mobile-ffmpeg", str);
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/"
                    + "video_function/" + "BrandPeakVideos.mp4";
            File dir = new File(path);
            if (dir.exists()) {
                dir.delete();
            }

            binding.flForVideo.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createScaledBitmap(viewToBitmap(binding.flForVideo), 1080,
                    1080, true);
            String strPath = saveImage(bitmap);

            StringBuilder outputDir = new StringBuilder();
            File file4 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            outputDir.append(file4.getAbsolutePath());
            outputDir.append("/video_function/");
            outputDir.append("BrandPeakVideos.mp4");

            StringBuilder inputDir = new StringBuilder();
            File file5 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            inputDir.append(file5.getAbsolutePath());
            inputDir.append("/video_function/");
            inputDir.append("video.mp4");

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(inputDir.toString());
            long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            try {
                retriever.release();
            } catch (IOException e) {
                e.printStackTrace();
            }

            com.arthenica.mobileffmpeg.Config.enableStatisticsCallback(new StatisticsCallback() {
                @Override
                public void apply(Statistics statistics) {
                    float progresss = Float.parseFloat(String.valueOf(statistics.getTime())) / duration;
                    float progressFinal = progresss * 100;
                    try {
                        progress.setIndeterminate(false);
                    } catch (Exception e) {

                    }
                    progress.setMax(100);
                    progress.setProgress((int) progressFinal);
                }
            });

            int i = FFmpeg.execute(new String[]{"-i", inputDir.toString(), "-i", strPath,
                    "-filter_complex", /*"overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2"*/
                    "overlay", "-r", "150",
                    "-vb", "20M",
                    "-y", outputDir.toString()});

            binding.flForVideo.setDrawingCacheEnabled(false);

            VideoPath = outputDir.toString();

            return String.valueOf(i);
        }
    }

    private String saveImage(Bitmap paramBitmap) {

        File directory = new File(Environment.getExternalStorageDirectory().toString()
                + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + "." + FOLDER_NAME + File.separator);

        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, "Image-Bitmap.png");
        if (file.exists()) {
            file.delete();
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            return file.getAbsolutePath();
        } catch (Exception e) {
            return "";
        }
    }

    class LoadSaveImage extends AsyncTask<String, Boolean, Boolean> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            if (!userItem.isSubscribed) {
                binding.ivFrameWatermark.setVisibility(VISIBLE);
            } else {
                binding.ivFrameWatermark.setVisibility(GONE);
            }
            if (!frameItemList.get(frameAdapter.getSelectedPos()).is_from_url) {
                removeControl();
                tv_name.setBackground(null);
                tv_email.setBackground(null);
                tv_phone.setBackground(null);
                tv_website.setBackground(null);
                tv_address.setBackground(null);

                iv_close_name.setVisibility(View.GONE);
                iv_close_email.setVisibility(View.GONE);
                iv_close_phone.setVisibility(View.GONE);
                iv_close_website.setVisibility(View.GONE);
                iv_close_address.setVisibility(View.GONE);

                if (isTextSelected()) {
                    setTextSelected(addTextList.get(selectedTextPosition).tv_text,
                            addTextList.get(selectedTextPosition).iv_close, addTextList.get(selectedTextPosition).iv_edit, false);
                }
            } else {
                if (isTextSelected()) {
                    setTextSelected(addTextList.get(selectedTextPosition).tv_text,
                            addTextList.get(selectedTextPosition).iv_close, addTextList.get(selectedTextPosition).iv_edit, false);
                }
            }
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
//            binding.videoPlayer.setDrawingCacheEnabled(true);
                binding.ivFestImage.setDrawingCacheEnabled(true);
                binding.ivFrame.setDrawingCacheEnabled(true);
                binding.flBackSticker.setDrawingCacheEnabled(true);
                binding.flSticker.setDrawingCacheEnabled(true);
                binding.flFrame.setDrawingCacheEnabled(true);
                binding.ivFrameWatermark.setDrawingCacheEnabled(true);
                binding.flForVideo.setDrawingCacheEnabled(true);
                binding.cvBase.setDrawingCacheEnabled(true);

                Bitmap bitmap = viewToBitmap(binding.cvBase);
                int multiplier = (int) getResources().getDimension(com.intuit.ssp.R.dimen._1ssp);
                Util.showLog("Multiplier: " + multiplier);
                bitmap = Bitmap.createScaledBitmap(bitmap, 1024 * multiplier,
                        1024 * multiplier, true);
                Constant.bitmap = bitmap;

//           binding.videoPlayer.setDrawingCacheEnabled(false);
                binding.ivFestImage.setDrawingCacheEnabled(false);
                binding.ivFrame.setDrawingCacheEnabled(false);
                binding.flBackSticker.setDrawingCacheEnabled(false);
                binding.flSticker.setDrawingCacheEnabled(false);
                binding.flFrame.setDrawingCacheEnabled(false);
                binding.ivFrameWatermark.setDrawingCacheEnabled(false);
                binding.flForVideo.setDrawingCacheEnabled(false);
                binding.cvBase.setDrawingCacheEnabled(false);
                progressDialog.dismiss();
                return Constant.bitmap != null;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if (s) {
                binding.cvBase.setDrawingCacheEnabled(false);
                binding.ivFrameWatermark.setVisibility(GONE);
                if (InterstitialAdManager.isLoaded() && prefManager.getInt(Constant.CLICK) >= prefManager.getInt(Constant.INTERSTITIAL_AD_CLICK)) {
                    prefManager.setInt(Constant.CLICK, 0);
                    InterstitialAdManager.showAds();
                } else {
                    prefManager.setInt(Constant.CLICK, prefManager.getInt(Constant.CLICK) + 1);
                    showPreviewDialog();
                }
            } else {
                Toast.makeText(EditorActivity.this, getString(R.string.err_creating_image), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }

    private void showPreviewDialog() {
        int screenWidth;
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.download_dialog);
        dialog.setCancelable(false);
        screenWidth = MyApplication.getColumnWidth(1, getResources().getDimension(com.intuit.ssp.R.dimen._10ssp));

        CardView cv_base = dialog.findViewById(R.id.cv_base);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) cv_base.getLayoutParams();
        params.width = screenWidth;
        params.height = screenWidth;

        cv_base.setLayoutParams(params);

        TextView title = dialog.findViewById(R.id.save_title);
        ImageView iv_cancel = dialog.findViewById(R.id.iv_close);
        ImageView iv_preview = dialog.findViewById(R.id.iv_save_image);
        ImageView iv_download = dialog.findViewById(R.id.iv_download);
        ImageView iv_whatsapp = dialog.findViewById(R.id.ic_whatsapp);
        ImageView iv_facebook = dialog.findViewById(R.id.ic_facebook);
        ImageView iv_instagram = dialog.findViewById(R.id.ic_instagram);
        ImageView iv_twitter = dialog.findViewById(R.id.ic_twitter);
        ImageView iv_share = dialog.findViewById(R.id.ic_share);
        PlayerView videoPlayer = dialog.findViewById(R.id.videoPlayer);
        ImageView ivPlayVideo = dialog.findViewById(R.id.iv_play_video);

        ivPlayVideo.setOnClickListener(v -> {
            ivPlayVideo.setVisibility(GONE);
            if (sharePlayer != null) {
                sharePlayer.seekTo(0);
                sharePlayer.setPlayWhenReady(true);
            }
        });

        GlideBinding.setTextSize(title, "font_title_size");

        if (isVideo) {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
            }

            videoPlayer.setVisibility(VISIBLE);
            videoPlayer.setUseController(false);
            videoPlayer.setControllerHideOnTouch(true);
            videoPlayer.setShowBuffering(true);
            TrackSelector trackSelectorDef = new DefaultTrackSelector();
            sharePlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef); //creating a player instance

            int appNameStringRes = R.string.app_name;
            String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(this, this.getString(appNameStringRes));
            DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
            Uri uriOfContentUrl = Uri.parse(VideoPath);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source

            sharePlayer.prepare(mediaSource);
            sharePlayer.setPlayWhenReady(true); // start loading video and play it at the moment a chunk of it is available offline

            videoPlayer.setPlayer(sharePlayer); // attach surface to the view
            videoPlayer.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
            sharePlayer.addListener(new Player.EventListener() {

                @Override
                public void onLoadingChanged(boolean isLoading) {
                    Player.EventListener.super.onLoadingChanged(isLoading);
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);
                    switch (playbackState) {
                        case ExoPlayer.STATE_ENDED:
                            ivPlayVideo.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            });

        } else {
            iv_preview.setVisibility(VISIBLE);
            Glide.with(this)
                    .load(Constant.bitmap)
                    .into(iv_preview);
        }
        iv_cancel.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            dialog.dismiss();
        });
        iv_download.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            new LoadDownloadImage("download").execute();
            dialog.dismiss();
        });
        iv_whatsapp.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            new LoadDownloadImage("whtsapp").execute();
            dialog.dismiss();
        });
        iv_facebook.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            new LoadDownloadImage("fb").execute();
            dialog.dismiss();
        });
        iv_instagram.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            new LoadDownloadImage("insta").execute();
            dialog.dismiss();
        });
        iv_twitter.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            new LoadDownloadImage("twter").execute();
            dialog.dismiss();
        });
        iv_share.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            new LoadDownloadImage("Share Via").execute();
            dialog.dismiss();
        });
        dialog.show();
    }

    class LoadDownloadImage extends AsyncTask<String, Boolean, Boolean> {
        String type = "";
        String filePath;
        boolean checkMemory;

        LoadDownloadImage(String type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            if (isVideo) {
                fileName = getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".mp4";
                filePath = Environment.getExternalStorageDirectory() + File.separator
                        + Environment.DIRECTORY_PICTURES + File.separator + getResources().getString(R.string.app_name)
                        + File.separator + fileName;

                File sourceLocation = new File(VideoPath);

                boolean success = false;

                if (!new File(filePath).exists()) {
                    try {
                        File file = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES
                        ), "/" + getResources().getString(R.string.app_name));
                        if (!file.exists()) {
                            if (!file.mkdirs()) {
                                Util.showLog("Can't create directory to save image.");
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.don_t_create),
                                        Toast.LENGTH_LONG).show();
                                success = false;
                            }
                        }

                        if (sourceLocation.exists()) {

                            InputStream in = new FileInputStream(sourceLocation);
                            OutputStream out = new FileOutputStream(filePath);

                            // Copy the bits from instream to outstream
                            byte[] buf = new byte[1024];
                            int len;

                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }

                            in.close();
                            out.close();
                            success = true;

                        } else {
                            Util.showLog("Copy file failed. Source file missing.");
                        }

                    } catch (Exception e) {

                    }
                }
                return success;
            } else {

                filePath = Environment.getExternalStorageDirectory() + File.separator
                        + Environment.DIRECTORY_PICTURES + File.separator + getResources().getString(R.string.app_name)
                        + File.separator + fileName;

                boolean success = false;

                if (!new File(filePath).exists()) {
                    try {
                        File file = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES
                        ), "/" + getResources().getString(R.string.app_name));
                        if (!file.exists()) {
                            if (!file.mkdirs()) {
                                Util.showLog("Can't create directory to save image.");
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.don_t_create),
                                        Toast.LENGTH_LONG).show();
                                success = false;
                            }
                        }
                        File file2 = new File(file.getAbsolutePath() + "/" + fileName);
                        if (file2.exists()) {
                            file2.delete();
                        }
                        Bitmap bitmap = Constant.bitmap;
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file2);
                            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                                    bitmap.getHeight(), bitmap.getConfig());
                            Canvas canvas = new Canvas(createBitmap);
                            canvas.drawColor(-1);
                            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
                            checkMemory = createBitmap.compress(Bitmap.CompressFormat.PNG,
                                    100, fileOutputStream);
                            createBitmap.recycle();
                            fileOutputStream.flush();
                            fileOutputStream.close();

                            MediaScannerConnection.scanFile(EditorActivity.this, new String[]{file2.getAbsolutePath()},
                                    (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                        public void onScanCompleted(String str, Uri uri) {
                                            Util.showLog("ExternalStorage " + "Scanned " + str + ":");
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("-> uri=");
                                            sb.append(uri);
                                            sb.append("-> FILE=");
                                            sb.append(file2.getAbsolutePath());
                                            Uri muri = Uri.fromFile(file2);
                                        }
                                    });
                            success = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            success = false;
                        }

                        progressDialog.dismiss();
                    } catch (Exception unused2) {
                    }
                }
                return success;
            }
        }

        @Override
        protected void onPostExecute(Boolean s) {
//                progressDialog.dismiss();
            if (s) {
                if (type.equals("download")) {
                    if (isVideo) {
                        Util.showToast(EditorActivity.this, getString(R.string.video_saved));
                    } else {
                        Toast.makeText(EditorActivity.this, getString(R.string.image_saved), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    shareFileImageUri(getImageContentUri(new File(filePath)), "", type, isVideo);
                }
            } else {
                Toast.makeText(EditorActivity.this, getString(R.string.err_save_image), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }

    public Uri getImageContentUri(File imageFile) {
        if (isVideo) {
            return Uri.parse(imageFile.getAbsolutePath());
        } else {
            return Uri.parse(imageFile.getAbsolutePath());
            /*String filePath = imageFile.getAbsolutePath();
            Cursor cursor = this.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + "=? ",
                    new String[]{filePath}, null);
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                cursor.close();
                return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
            }*/
        }
//        return null;
    }

    private Bitmap viewToBitmap(View view) {
        Bitmap createBitmap = null;
        try {
            createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            view.draw(new Canvas(createBitmap));
            return createBitmap;
        } catch (Exception e) {
            Util.showErrorLog(e.getMessage(), e);
            return createBitmap;
        } finally {
            view.destroyDrawingCache();
        }
    }

    private void loadSticker() {

        stickerCategoryAdapter = new StickerCategoryAdapter(this, position -> {
            binding.vpStickers.setCurrentItem(position);
        });
        binding.rvStickerCategory.setAdapter(stickerCategoryAdapter);

        strAdapter = new StickerViewPagerAdapter(this, data -> {

            progressLoading.show();
            LoadLogo loadLogo = new LoadLogo(data.stickerImage);
            loadLogo.execute();
//            addSticker("", "", bitmap, false);
        });

        binding.vpStickers.setAdapter(strAdapter);
        binding.vpStickers.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                stickerCategoryAdapter.setSelected(position);
                binding.rvStickerCategory.smoothScrollToPosition(position);
            }
        });

        postViewModel.getStickers().observe(this, listResource -> {
            if (listResource != null) {

                if (listResource.data != null) {

                    stickerCategoryAdapter.setCategories(listResource.data.strCategories);
                    strAdapter.setData(listResource.data.strModelList);

                }
            }
        });

        binding.ivSearch.setOnClickListener(v -> {
            if (binding.etSearchStickers.getText().toString().equals("")) {
                Util.showToast(EditorActivity.this, "Please Enter Keyword");
                return;
            }
            loadSearchStickers();
        });

        binding.etSearchStickers.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    loadSearchStickers();
                }
                return false;
            }
        });

    }

    public void loadSearchStickers() {
        binding.vpStickers.setVisibility(GONE);
        binding.rvStickerCategory.setVisibility(GONE);
        binding.rvSticker.setVisibility(VISIBLE);

        postViewModel.getStickersByKeyword(binding.etSearchStickers.getText().toString()).observe(this,
                listResource -> {
                    if (listResource != null) {

                        if (listResource.data != null && listResource.data.size() > 0) {

                            StickerAdapter strAdapter = new StickerAdapter(this, listResource.data, data -> {
                                binding.rvSticker.setVisibility(GONE);
                                binding.rlSticker.setVisibility(GONE);
                                binding.vpStickers.setVisibility(VISIBLE);
                                binding.rvStickerCategory.setVisibility(VISIBLE);
                                progressLoading.show();
                                LoadLogo loadLogo = new LoadLogo(data.stickerImage);
                                loadLogo.execute();
                            });
                            binding.rvSticker.setAdapter(strAdapter);
                            strAdapter.notifyDataSetChanged();

                        }

                    }
                });
    }

    private void addImage() {
//        checkPer();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_image_dialog);
        dialog.setCancelable(false);
        TextView textView = (TextView) dialog.findViewById(R.id.txtTitle);
        TextView fitEditText = (TextView) dialog.findViewById(R.id.auto_fit_edit_text);
        Button btnGallery = (Button) dialog.findViewById(R.id.btnGallery);
        Button btnCamera = (Button) dialog.findViewById(R.id.btnCamera);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        GlideBinding.setTextSize(textView, "font_title_size");
        GlideBinding.setTextSize(fitEditText, "font_body_size");

        btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Dexter.withContext(this).withPermissions(PERMISSIONS).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        onCameraButtonClick();
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        showSettingsDialog();
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(new PermissionRequestErrorListener() {
                public void onError(DexterError dexterError) {
                    Toast.makeText(EditorActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                }
            }).onSameThread().check();
        });

        btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            Dexter.withContext(this).withPermissions(PERMISSIONS).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        onGalleryButtonClick();
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        showSettingsDialog();
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(new PermissionRequestErrorListener() {
                public void onError(DexterError dexterError) {
                    Toast.makeText(EditorActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                }
            }).onSameThread().check();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }

    public void shareFileImageUri(Uri path, String name, String shareTo, boolean isVideo) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        switch (shareTo) {
            case "whtsapp":
                shareIntent.setPackage("com.whatsapp");
                break;
            case "fb":
                shareIntent.setPackage("com.facebook.katana");
                break;
            case "insta":
                shareIntent.setPackage("com.instagram.android");
                break;
            case "twter":
                shareIntent.setPackage("com.twitter.android");
                break;
        }

        if (isVideo) {
            shareIntent.setDataAndType(path, "video/*");
        } else {
            shareIntent.setDataAndType(path, "image/*");
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, path);
        if (!name.equals("")) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, name);
        }
        startActivity(Intent.createChooser(shareIntent, MyApplication.context.getString(R.string.share_via)));
    }

    public void removeControl() {
        int childCount = binding.flSticker.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = binding.flSticker.getChildAt(i);
            if (childAt instanceof RelStickerView) {
                ((RelStickerView) childAt).setBorderVisibility(false);
            }
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), (String) null));
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void onGalleryButtonClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image).toString()), SELECT_PICTURE_GALLERY);
    }

    private void onCameraButtonClick() {
        uri = FileProvider.getUriForFile(
                getApplicationContext(),
                BuildConfig.APPLICATION_ID + "." + "provider",
                createCameraFile()
        );
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", uri);
        startActivityForResult(intent, SELECT_PICTURE_CAMERA);
    }

    private File createCameraFile() {
        File image = null;
        String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + dateTime + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int i3 = requestCode;
        int i4 = resultCode;
        if (i4 == -1) {
            if (data != null || i3 == SELECT_PICTURE_CAMERA) {
                if (i3 == SELECT_PICTURE_GALLERY) {
                    try {
                        Uri fromFile = Uri.fromFile(new File(getCacheDir(),
                                "SampleCropImage" + System.currentTimeMillis() + ".png"));
                        StringBuilder sb = new StringBuilder();
                        sb.append("====");
                        sb.append(data.getData().getPath());
                        Log.e("downaload", sb.toString());
                        Log.e("downaload", "====" + fromFile.getPath());
                        UCrop.Options options2 = new UCrop.Options();
                        options2.setToolbarColor(getResources().getColor(R.color.white));
                        options2.setFreeStyleCropEnabled(true);
                        UCrop.of(data.getData(), fromFile).withOptions(options2).start(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (i3 == SELECT_PICTURE_CAMERA) {
                    try {
                        Uri fromFile2 = Uri.fromFile(new File(MyApplication.context.getCacheDir(),
                                "SampleCropImage" + System.currentTimeMillis() + ".png"));
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("====");
                        sb2.append(fromFile2.getPath());
                        Log.e("downaload", sb2.toString());
                        UCrop.Options options3 = new UCrop.Options();
                        options3.setToolbarColor(getResources().getColor(R.color.white));
                        options3.setFreeStyleCropEnabled(true);
                        UCrop.of(uri, fromFile2).withOptions(options3).start(this);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (i4 == -1 && i3 == 69) {
                    handleCropResult(data);
                } else if (i4 == 96) {
                    UCrop.getError(data);
                }
            }
        }
    }

    private void handleCropResult(Intent data) {
        Bitmap bmp = BitmapUtil.decodeBitmapFromUri(this, UCrop.getOutput(data), 456,
                456, BitmapUtil.getInSampleSizePower2());
        addSticker("", "", bmp, isImage);
    }

    private void addSticker(String str, String str2, Bitmap bitmap2, boolean isImage) {
        ElementInfo elementInfo = new ElementInfo();
        elementInfo.setPOS_X((float) ((binding.cvBase.getWidth() / 2) - ImageUtils.dpToPx(MyApplication.context, 70)));
        elementInfo.setPOS_Y((float) ((binding.cvBase.getHeight() / 2) - ImageUtils.dpToPx(MyApplication.context, 70)));
        elementInfo.setWIDTH(ImageUtils.dpToPx(MyApplication.context, 140));
        elementInfo.setHEIGHT(ImageUtils.dpToPx(MyApplication.context, 140));
        elementInfo.setROTATION(0.0f);
        elementInfo.setRES_ID(str);
        elementInfo.setBITMAP(bitmap2);
        elementInfo.setCOLORTYPE("colored");
        elementInfo.setTYPE("STICKER");
        elementInfo.setSTC_OPACITY(255);
        elementInfo.setSTC_COLOR(0);
        elementInfo.setSTKR_PATH(str2);
        elementInfo.setSTC_HUE(1);
        elementInfo.setFIELD_TWO("0,0");
        RelStickerView relStickerView = new RelStickerView(MyApplication.context, isImage);
        relStickerView.optimizeScreen(sWidth, sHeight);
        relStickerView.setMainLayoutWH((float) binding.cvBase.getWidth(), (float) binding.cvBase.getHeight());
        relStickerView.setComponentInfo(elementInfo);
        relStickerView.setId(ViewIdGenerator.generateViewId());
        binding.flSticker.addView(relStickerView);
        relStickerView.setOnTouchCallbackListener(rtouchlistener);
        relStickerView.setBorderVisibility(true);
    }

    RelStickerView.TouchEventListener rtouchlistener = new RelStickerView.TouchEventListener() {
        @Override
        public void onDelete() {
        }

        @Override
        public void onEdit(View view, Uri uri) {

        }

        @Override
        public void onRotateDown(View view) {
            touchDown(view, "viewboder");
        }

        @Override
        public void onRotateMove(View view) {
            touchMove(view);
        }

        @Override
        public void onRotateUp(View view) {
            touchUp(view);
        }

        @Override
        public void onScaleDown(View view) {
            touchDown(view, "viewboder");
        }

        @Override
        public void onScaleMove(View view) {
            touchMove(view);
        }

        @Override
        public void onScaleUp(View view) {
            touchUp(view);
        }

        @Override
        public void onTouchDown(View view) {
            touchDown(view, "viewboder");
        }

        @Override
        public void onTouchMove(View view) {
            touchMove(view);
        }

        @Override
        public void onTouchUp(View view) {
            touchUp(view);
        }

        @Override
        public void onMainClick(View view) {
            Log.e("TOUCH", "MAIN TOUCH");
            setBackImage();
        }
    };

    RelStickerView.TouchEventListener newtouchlistener = new RelStickerView.TouchEventListener() {
        @Override
        public void onDelete() {
        }

        @Override
        public void onEdit(View view, Uri uri) {

        }

        @Override
        public void onRotateDown(View view) {
            touchDown(view, "viewboder");
        }

        @Override
        public void onRotateMove(View view) {
            touchMove(view);
        }

        @Override
        public void onRotateUp(View view) {
            touchUp(view);
        }

        @Override
        public void onScaleDown(View view) {
            touchDown(view, "viewboder");
        }

        @Override
        public void onScaleMove(View view) {
            touchMove(view);
        }

        @Override
        public void onScaleUp(View view) {
            touchUp(view);
        }

        @Override
        public void onTouchDown(View view) {
            touchDown(view, "viewboder");
        }

        @Override
        public void onTouchMove(View view) {
            touchMove(view);
        }

        @Override
        public void onTouchUp(View view) {
            touchUp(view);
        }

        @Override
        public void onMainClick(View view) {
            Log.e("TOUCH", "MAIN TOUCH");
            setBackImage();
        }
    };

    public void setBackImage() {
        int childCount = binding.flSticker.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = binding.flSticker.getChildAt(i);
            if (childAt instanceof RelStickerView) {
                if (((RelStickerView) childAt).getBorderVisbilty() && ((RelStickerView) childAt).getIsImage()) {
                    binding.flSticker.removeView(childAt);
                    binding.flBackSticker.addView(childAt);
                    ((RelStickerView) childAt).setOnTouchCallbackListener(newtouchlistener);
                    ((RelStickerView) childAt).setBorderVisibility(false);
                }
            }
        }
    }

    private void touchDown(View view, String str) {
        if (view instanceof RelStickerView) {
            RelStickerView relStickerView = (RelStickerView) view;
//            setBackImage();
        }
    }

    private void touchMove(View view) {
        boolean z = view instanceof RelStickerView;
        if (z) {
            RelStickerView relStickerView = (RelStickerView) view;
        } else {

        }
    }

    private void touchUp(final View view) {
        if (view instanceof RelStickerView) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            RelStickerView relStickerView = (RelStickerView) view;
            sb.append(relStickerView.getColorType());
            relStickerView.setBorderVisibility(true);
            if (isImage && view.getParent() == binding.flBackSticker) {
                binding.flBackSticker.removeView(relStickerView);
                binding.flSticker.addView(relStickerView);
                relStickerView.setOnTouchCallbackListener(rtouchlistener);
            }
        }
    }

    class LoadLogo extends AsyncTask<String, String, String> {

        private String message = "", verifyStatus = "0", urls;
        Drawable drawable;
        Bitmap bitmap;

        public LoadLogo(String urls) {
            this.urls = urls;
        }

        @Override
        protected String doInBackground(String... strings) {
            bitmap = null;
            Log.e("URL", urls);
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(urls).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
            return "0";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressLoading.dismiss();
            binding.rlSticker.setVisibility(GONE);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            addSticker("", "", bitmap, false);
        }
    }

    private void showFont() {
        FontAdapter fontAdapter = new FontAdapter(this, fontList(), fontName -> {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "font/" +
                    fontName);
            if (!isFromUrl) {
                if (!isTextSelected() && iv_close_name.getVisibility() == View.GONE && iv_close_website.getVisibility() == View.GONE && iv_close_email.getVisibility() == View.GONE &&
                        iv_close_address.getVisibility() == View.GONE && iv_close_phone.getVisibility() == View.GONE) {
                    tv_name.setTypeface(typeface);
                    tv_email.setTypeface(typeface);
                    tv_phone.setTypeface(typeface);
                    tv_website.setTypeface(typeface);
                    tv_address.setTypeface(typeface);

                    for (int i = 0; i < addTextList.size(); i++) {
                        addTextList.get(i).tv_text.setTypeface(typeface);
                    }
                } else {
                    if (iv_close_name.getVisibility() == View.VISIBLE) {
                        tv_name.setTypeface(typeface);
                    }
                    if (iv_close_phone.getVisibility() == View.VISIBLE) {
                        tv_phone.setTypeface(typeface);
                    }
                    if (iv_close_email.getVisibility() == View.VISIBLE) {
                        tv_email.setTypeface(typeface);
                    }
                    if (iv_close_address.getVisibility() == View.VISIBLE) {
                        tv_address.setTypeface(typeface);
                    }
                    if (iv_close_website.getVisibility() == View.VISIBLE) {
                        tv_website.setTypeface(typeface);
                    }
                    if (iv_close_website.getVisibility() == View.VISIBLE) {
                        tv_website.setTypeface(typeface);
                    }
                    if (isTextSelected() && addTextList.size() > 0) {
                        addTextList.get(selectedTextPosition).tv_text.setTypeface(typeface);
                    }
                }
            } else {
                if (isTextSelected() && addTextList.size() > 0) {
                    addTextList.get(selectedTextPosition).tv_text.setTypeface(typeface);
                }
            }
        });
        binding.rvFont.setAdapter(fontAdapter);
    }

    private List<String> fontList() {
        String[] list;
        List<String> fonts_array = new ArrayList<>();
        list = getResources().getStringArray(R.array.fonts);
        if (list != null && list.length > 0) {
            // This is a folder
            fonts_array.clear();
            for (String file : list) {
                if (file.endsWith(".ttf") || file.endsWith("otf")) {
                    fonts_array.add(file);
                }
            }
        }
        return fonts_array;
    }

    private void addText(int pos) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_text_dialog);
        dialog.setCancelable(false);
        TextView textView = (TextView) dialog.findViewById(R.id.txtTitle);
        final EditText fitEditText = (EditText) dialog.findViewById(R.id.auto_fit_edit_text);
        Button button = (Button) dialog.findViewById(R.id.btnCancelDialog);
        Button button2 = (Button) dialog.findViewById(R.id.btnAddTextSDialog);

        GlideBinding.setTextSize(textView, "font_title_size");
        GlideBinding.setTextSize(fitEditText, "edit_text");
        if (pos != -1) {
            fitEditText.setText(addTextList.get(selectedTextPosition).tv_text.getText().toString());
            textView.setText(getString(R.string.edit_text));
            button2.setText(getString(R.string.save));
        }
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!fitEditText.getText().toString().trim().equals("")) {
                    if (pos != -1) {
                        addTextList.get(selectedTextPosition).tv_text.setText(fitEditText.getText().toString());
                    } else {
                        addTextView(fitEditText.getText().toString());
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(EditorActivity.this, getString(R.string.add_text), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void addTextView(String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view_text = inflater.inflate(R.layout.add_text, null);

        TextView tv_new = view_text.findViewById(R.id.tv_add_text);
        ImageView iv_remove = view_text.findViewById(R.id.iv_add_text_remove);
        ImageView iv_edit = view_text.findViewById(R.id.iv_add_text_edit);

        tv_new.setTag(String.valueOf(addTextList.size()));
//        tv_new.setTypeface(typeface);
        tv_new.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        tv_new.setText(text);
//        tv_new.setTextColor(arrayListColor.get(7));
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        view_text.setLayoutParams(params);

//        tv_new.setShadowLayer(padding, 0f, 0f, 0);
//        tv_new.setPadding(padding, padding, padding, padding);

        binding.flFrame.addView(view_text);
        view_text.bringToFront();
        view_text.setTranslationX(binding.flFrame.getWidth() / 2);
        view_text.setTranslationY(binding.flFrame.getHeight() / 2);

        OnDragTouchListener.OnDragActionListener onDragActionListener = new OnDragTouchListener.OnDragActionListener() {
            @Override
            public void onDragStart(View view) {
                if (selectedTextPosition != Integer.parseInt(tv_new.getTag().toString())) {
                    if (isTextSelected()) {
                        setTextSelected(addTextList.get(selectedTextPosition).tv_text,
                                addTextList.get(selectedTextPosition).iv_close,
                                addTextList.get(selectedTextPosition).iv_edit, false);
                    }
                }

                view_text.bringToFront();
                setTextSelected(tv_new, iv_remove, iv_edit, true);
            }

            @Override
            public void onDragEnd(View view, Boolean delete) {
            }
        };

        view_text.setOnTouchListener(new OnDragTouchListener(view_text, binding.flFrame, 50, onDragActionListener));
        tv_new.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        view_text.bringToFront();

        view_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTextPosition != Integer.parseInt(tv_new.getTag().toString())) {
                    if (isTextSelected()) {
                        addTextList.get(selectedTextPosition).tv_text.setBackground(null);
                    }
                    setTextSelected(tv_new, iv_remove, iv_edit, true);
                    view_text.bringToFront();
                } else {
                    if (isTextSelected()) {
                        setTextSelected(tv_new, iv_remove, iv_edit, false);
                    }
                }
//                setLayouts(true);
            }
        });

        view_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("aaa", "long click");
                return true;
            }
        });

        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addTextList.get(selectedTextPosition).viewLayout.setOnTouchListener(null);

                    binding.flFrame.removeView(addTextList.get(selectedTextPosition).viewLayout);
                    addTextList.remove(selectedTextPosition);

                    if (selectedTextPosition < addTextList.size()) {
                        for (int i = selectedTextPosition; i < addTextList.size(); i++) {
                            addTextList.get(i).tv_text.setTag(String.valueOf(i));
                        }
                    } else {
                        selectedTextPosition = -1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addText(selectedTextPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addTextList.add(new AddTextItem(view_text, tv_new, iv_remove, iv_edit));
    }

    private void setTextSelected(TextView tv_new, ImageView iv_remove, ImageView iv_edit, boolean isSelected) {
        if (isSelected) {
            selectedTextPosition = Integer.parseInt(tv_new.getTag().toString());
            tv_new.setBackgroundResource(R.drawable.rounded_border);
            iv_remove.setVisibility(View.VISIBLE);
            iv_edit.setVisibility(View.VISIBLE);
        } else {
            tv_new.setBackground(null);
            iv_remove.setVisibility(View.GONE);
            iv_edit.setVisibility(View.GONE);
            selectedTextPosition = -1;
        }
    }

    private void changeFontSize(float value) {
        if (!isFromUrl) {
            if (iv_close_name.getVisibility() == View.VISIBLE) {
                tv_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, value);
            }
            if (iv_close_email.getVisibility() == View.VISIBLE) {
                tv_email.setTextSize(TypedValue.COMPLEX_UNIT_PX, value);
            }
            if (iv_close_phone.getVisibility() == View.VISIBLE) {
                tv_phone.setTextSize(TypedValue.COMPLEX_UNIT_PX, value);
            }
            if (iv_close_website.getVisibility() == View.VISIBLE) {
                tv_website.setTextSize(TypedValue.COMPLEX_UNIT_PX, value);
            }
            if (iv_close_address.getVisibility() == View.VISIBLE) {
                tv_address.setTextSize(TypedValue.COMPLEX_UNIT_PX, value);
            }
            if (isTextSelected() && addTextList.size() > 0) {
                addTextList.get(selectedTextPosition).tv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, value);
            }
        } else {
            if (isTextSelected() && addTextList.size() > 0) {
                addTextList.get(selectedTextPosition).tv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, value);
            }
        }
    }

    private boolean isTextSelected() {
        return selectedTextPosition >= 0;
    }

    private void setPostData() {
        if (getIntent() != null) {
            postItemList = (List<PostItem>) getIntent().getSerializableExtra(Constant.INTENT_POST_LIST);
            if (getIntent().getStringExtra(Constant.INTENT_TYPE).equals(Constant.CUSTOM)) {
                isImage = true;
            }
            pos = getIntent().getIntExtra(Constant.INTENT_POS, 0);
            adapter = new DetailAdapter(this, pos -> {
                if (!userItem.isSubscribed && postItemList.get(pos).is_premium) {
                    dialogMsg.showWarningDialog(getString(R.string.premium), getString(R.string.please_subscribe), getString(R.string.subscribe), true);
                    dialogMsg.show();
                    dialogMsg.okBtn.setOnClickListener(view -> {
                        dialogMsg.cancel();
                        startActivity(new Intent(EditorActivity.this, SubsPlanActivity.class));
                    });
                    return;
                }
                this.pos = pos;
                setImageShow(postItemList.get(pos));
            }, 3, getResources().getDimension(com.intuit.ssp.R.dimen._2ssp));
            adapter.setData(postItemList);
            binding.rvPost.setAdapter(adapter);
            setImageShow(postItemList.get(pos));
        }
    }

    private void setImageShow(PostItem postItem) {
        isVideo = postItem.is_video;
        if (postItem.is_video) {
            if (absPlayerInternal != null) {
                absPlayerInternal.setPlayWhenReady(false);
                absPlayerInternal.stop();
                absPlayerInternal.seekTo(0);
            }
            binding.ivFestImage.setVisibility(GONE);
            loadVideo(postItem.image_url);
        } else {
            binding.ivFestImage.setVisibility(VISIBLE);
            binding.videoPlayer.setVisibility(GONE);
            GlideBinding.bindImage(binding.ivFestImage, postItem.image_url);
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
        String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(this, this.getString(appNameStringRes));
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

                switch (playbackState) {
                    case ExoPlayer.STATE_ENDED:
                        absPlayerInternal.seekTo(0);
                        absPlayerInternal.setPlayWhenReady(true);
                        break;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (binding.rlSticker.getVisibility() == VISIBLE) {
            binding.rlSticker.setVisibility(GONE);
        } else if (binding.rlFrame.getVisibility() == VISIBLE) {
            binding.rlFrame.setVisibility(GONE);
        } else if (binding.rlBusiness.getVisibility() == VISIBLE) {
            binding.rlBusiness.setVisibility(GONE);
        } else if (binding.rlTextFont.getVisibility() == VISIBLE) {
            binding.rlTextFont.setVisibility(GONE);
        } else if (binding.rlTextSize.getVisibility() == VISIBLE) {
            binding.rlTextSize.setVisibility(GONE);
        } else {
            super.onBackPressed();
        }
    }
}