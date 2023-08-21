package com.itsbusinessposter.idebrandvideo.ui.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.itsbusinessposter.idebrandvideo.MyApplication;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.adapters.DownloadAdapter;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.databinding.FragmentDownloadBinding;
import com.itsbusinessposter.idebrandvideo.items.DownloadItem;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DownloadFragment extends Fragment {

    public DownloadFragment() {
        // Required empty public constructor
    }

    FragmentDownloadBinding binding;
    DownloadAdapter adapter;
    List<DownloadItem> uriList;
    int DELETE_REQUEST_URI_R = 11, DELETE_REQUEST_URI_Q = 12;
    DownloadItem downloadItem;
    DialogMsg dialogMsg;
    ExoPlayer sharePlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDownloadBinding.inflate(getLayoutInflater());
        uriList = new ArrayList<>();
        dialogMsg = new DialogMsg(getActivity(), false);
        setData();

        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(false);
            setData();
        });

        return binding.getRoot();
    }

    private void setData() {
        new LoadImages().execute();
    }

    public class LoadImages extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (adapter == null) {
                adapter = new DownloadAdapter(getActivity(), uriList, item -> {
                    downloadItem = item;
                    showPreviewDialog();
                });
                binding.rvDownload.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            if (uriList.size() > 0) {
                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.rvDownload.setVisibility(View.VISIBLE);
            } else {
                binding.shimmerViewContainer.stopShimmer();
                binding.rvDownload.setVisibility(View.GONE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.animationView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            uriList.clear();
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String selection = MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME + " like? ";
                String[] selectionArgs = {getContext().getString(R.string.app_name)};

                if (getActivity() != null) {
                    Cursor cursor = getActivity().getContentResolver().query(
                            MediaStore.AUTHORITY_URI,
                            new String[]{MediaStore.Files.FileColumns._ID},
                            selection,
                            selectionArgs,
                            null);

                    if (cursor != null && cursor.moveToFirst()) {
                        for (int i = 0; i < cursor.getCount(); i++) {
                            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));

                            uriList.add(Uri.withAppendedPath(MediaStore.AUTHORITY_URI, id));

                            cursor.moveToNext();
                        }
                        Collections.reverse(uriList);

                        cursor.close();
                    }
                }
            }
            else {*/
            try {
                File root = new File(Environment.getExternalStorageDirectory() + File.separator
                        + Environment.DIRECTORY_PICTURES + File.separator + getContext().getString(R.string.app_name) + "/");
                if (root.exists()) {
                    String[] okFileExtensions = new String[]{"jpg", "jpeg", "png"};
                    File[] files = root.listFiles();

                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getName().contains("jpg") || files[i].getName().contains("jpeg") || files[i].getName().contains("png")) {
                            uriList.add(new DownloadItem(getImageContentUri(files[i], false), false));
                            Util.showLog("URI IMAGE: " + getImageContentUri(files[i], false));
                        }
                        if (files[i].getName().contains("mp4")) {
                            Util.showLog("URI VIDEO: " + Uri.parse(files[i].getAbsolutePath()));
                            uriList.add(new DownloadItem(Uri.parse(files[i].getAbsolutePath()), true));
                        }
                    }
                    Collections.reverse(uriList);
                }
            } catch (Exception e) {
                Util.showErrorLog(e.getMessage(), e);
            }
            return null;
        }
    }

    public Uri getImageContentUri(File imageFile, boolean isVideo) {
        if (isVideo) {
            String filePath = imageFile.getAbsolutePath();
            Cursor cursor = getActivity().getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.Media._ID},
                    MediaStore.Video.Media.DATA + "=? ",
                    new String[]{filePath}, null);
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                cursor.close();
                return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "" + id);
            }
        } else {
            try {
                String filePath = imageFile.getAbsolutePath();
                Cursor cursor = getActivity().getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media._ID},
                        MediaStore.Images.Media.DATA + "=? ",
                        new String[]{filePath}, null);
                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                    cursor.close();
                    return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
                }
            } catch (Exception e) {
                Util.showErrorLog(e.getMessage(), e);
            }
        }
        return null;
    }

    private void showPreviewDialog() {
        int screenWidth;
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
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

        iv_download.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));

        GlideBinding.setTextSize(title, "font_title_size");
        title.setText(getContext().getString(R.string.share_via));

        PlayerView videoPlayer = dialog.findViewById(R.id.videoPlayer);
        ImageView ivPlayVideo = dialog.findViewById(R.id.iv_play_video);

        ivPlayVideo.setOnClickListener(v -> {
            ivPlayVideo.setVisibility(GONE);
            if (sharePlayer != null) {
                sharePlayer.seekTo(0);
                sharePlayer.setPlayWhenReady(true);
            }
        });

        if (downloadItem.isVideo) {
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
            sharePlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelectorDef); //creating a player instance

            int appNameStringRes = R.string.app_name;
            String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(getContext(), this.getString(appNameStringRes));
            DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
            Uri uriOfContentUrl = downloadItem.uri;
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
                    .load(downloadItem.uri)
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
            showDeleteDialog();
            dialog.dismiss();
        });
        iv_whatsapp.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            shareFileImageUri(downloadItem.uri, "", "whtsapp");
            dialog.dismiss();
        });
        iv_facebook.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            shareFileImageUri(downloadItem.uri, "", "fb");
            dialog.dismiss();
        });
        iv_instagram.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            shareFileImageUri(downloadItem.uri, "", "insta");
            dialog.dismiss();
        });
        iv_twitter.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            shareFileImageUri(downloadItem.uri, "", "twter");
            dialog.dismiss();
        });
        iv_share.setOnClickListener(v -> {
            if (sharePlayer != null) {
                sharePlayer.setPlayWhenReady(false);
                sharePlayer.stop();
                sharePlayer.seekTo(0);
                ivPlayVideo.setVisibility(VISIBLE);
            }
            shareFileImageUri(downloadItem.uri, "", "Share Via");
            dialog.dismiss();
        });
        dialog.show();
    }

    private void showDeleteDialog() {
        dialogMsg.showConfirmDialog(getString(R.string.delete), getString(R.string.sure_delete), getString(R.string.delete), getString(R.string.cancel));
        dialogMsg.show();
        dialogMsg.okBtn.setOnClickListener(v -> {
            dialogMsg.cancel();
            try {
                if (downloadItem.isVideo) {
                    File fdelete = new File(downloadItem.uri.getPath());
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            Util.showLog("file Deleted :" + downloadItem.uri.getPath());
                            Toast.makeText(getContext(), getResources().getString(R.string.video_deleted), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    int delete = getActivity().getContentResolver().delete(downloadItem.uri, null, null);
                    if (delete == 1) {
                        Toast.makeText(getContext(), getResources().getString(R.string.image_deleted), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.error_delete), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch(Exception exception){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && exception instanceof RecoverableSecurityException) {
                        try {
                            ArrayList<Uri> arrayListUri = new ArrayList<>();
                            arrayListUri.add(downloadItem.uri);
                            PendingIntent editPendingIntent = MediaStore.createDeleteRequest(getActivity().getContentResolver(), arrayListUri);
                            getActivity().startIntentSenderForResult(editPendingIntent.getIntentSender(), DELETE_REQUEST_URI_R, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Util.showErrorLog(e.getMessage(), e);
                        }
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && exception instanceof RecoverableSecurityException) {
                        try {
                            getActivity().startIntentSenderForResult(((RecoverableSecurityException) exception).getUserAction().getActionIntent().getIntentSender(), DELETE_REQUEST_URI_Q, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Util.showErrorLog(e.getMessage(), e);
                        }
                    }
                }
        });
    }

    public void shareFileImageUri(Uri path, String name, String shareTo) {
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
        if (downloadItem.isVideo) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == DELETE_REQUEST_URI_R) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getContext(), getResources().getString(R.string.image_deleted), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == DELETE_REQUEST_URI_Q) {
            if (resultCode == RESULT_OK) {
                int delete = getContext().getContentResolver().delete(downloadItem.uri, null, null);
                if (delete == 1) {
                    Toast.makeText(getContext(), getResources().getString(R.string.image_deleted), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.error_delete), Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}