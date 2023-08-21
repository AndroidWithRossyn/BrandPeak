package com.itsbusinessposter.idebrandvideo.ui.activities;

import static com.itsbusinessposter.idebrandvideo.utils.Constant.IS_EDIT_MODE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.itsbusinessposter.idebrandvideo.Ads.BannerAdManager;
import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.bg_remove.BGConfig;
import com.itsbusinessposter.idebrandvideo.bg_remove.MLCropAsyncTask;
import com.itsbusinessposter.idebrandvideo.bg_remove.MLOnCropTaskCompleted;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.databinding.ActivityAddBusinessBinding;
import com.itsbusinessposter.idebrandvideo.items.BusinessItem;
import com.itsbusinessposter.idebrandvideo.items.UserItem;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.ui.fragments.BusinessFragment;
import com.itsbusinessposter.idebrandvideo.utils.Connectivity;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.BusinessViewModel;
import com.itsbusinessposter.idebrandvideo.viewmodel.UserViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class AddBusinessActivity extends AppCompatActivity {

    public static Bitmap eraserResultBmp;
    ActivityAddBusinessBinding binding;
    BusinessViewModel businessViewModel;
    BusinessItem businessItem;
    Uri imageUri, cutUri;
    String profileImagePath, cutImagePath;

    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    Connectivity connectivity;
    PrefManager prefManager;
    ProgressDialog prgDialog;
    DialogMsg dialogMsg;

    UserViewModel userViewModel;
    UserItem userItem;

    private Bitmap selectedBit, cutBit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBusinessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        businessViewModel = new ViewModelProvider(this).get(BusinessViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        connectivity = new Connectivity(this);
        prefManager = new PrefManager(this);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getResources().getString(R.string.login_loading));
        prgDialog.setCancelable(false);

        dialogMsg = new DialogMsg(this, false);
        BannerAdManager.showBannerAds(this, binding.llAdview);
        setUpUi();
        setUpViewModel();
    }

    private void setUpUi() {
        if (!IS_EDIT_MODE) {
            binding.toolbar.toolName.setText(getString(R.string.add_business_titles));
        } else {
            binding.toolbar.toolName.setText(getString(R.string.edit_business_titles));
            businessItem = (BusinessItem) getIntent().getSerializableExtra(Constant.INTENT_BUSINESS);
            setData();
        }
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnSave.setOnClickListener(v -> {
            if (validate()) {

                String name = binding.etBusinessName.getText().toString().trim();
                String email = binding.etBusinessEmail.getText().toString().trim();
                String phone = binding.etBusinessNumber.getText().toString().trim();
                String website = binding.etBusinessWebsite.getText().toString().trim();
                String address = binding.etBusinessAddress.getText().toString().trim();

                if (!connectivity.isConnected()) {
                    Util.showToast(this, getString(R.string.error_message__no_internet));
                    return;
                }

                if (IS_EDIT_MODE) {
                    prgDialog.show();
                    businessViewModel.updateBusinessData(name, profileImagePath, imageUri, email, phone, website, address,
                            false, businessItem.id, getContentResolver()).observe(this,
                            listResource -> {
                                if (listResource != null) {

                                    Util.showLog("Got Data" + listResource.message + listResource.toString());

                                    switch (listResource.status) {
                                        case LOADING:
                                            // Loading State
                                            // Data are from Local DB

                                            break;
                                        case SUCCESS:
                                            // Success State
                                            // Data are from Server
                                            businessViewModel.setLoadingState(false);
                                            prgDialog.cancel();

                                            dialogMsg.showSuccessDialog(getString(R.string.success), getString(R.string.ok));
                                            dialogMsg.show();
                                            dialogMsg.okBtn.setOnClickListener(v1 -> {
                                                Config.BUSINESS_SIZE = businessViewModel.getBusinessCount();
                                                try {
                                                    BusinessFragment businessFragment = new BusinessFragment();
                                                    businessFragment.setUpViewModel();
                                                } catch (Exception e) {

                                                }
                                                dialogMsg.cancel();
                                                onBackPressed();
                                            });

                                            break;
                                        case ERROR:
                                            // Error State

                                            prgDialog.cancel();
                                            dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                                            dialogMsg.show();
                                            businessViewModel.setLoadingState(false);
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
                } else {
                    if (imageUri == null) {
                        Util.showToast(AddBusinessActivity.this, getString(R.string.err_add_image));
                        return;
                    }
                    prgDialog.show();
                    businessViewModel.addBusiness(name, profileImagePath, imageUri, email, phone, website, address,
                            false, prefManager.getString(Constant.USER_ID), getContentResolver()).observe(this,
                            listResource -> {
                                if (listResource != null) {

                                    Util.showLog("Got Data" + listResource.message + listResource.toString());

                                    switch (listResource.status) {
                                        case LOADING:
                                            // Loading State
                                            // Data are from Local DB

                                            break;
                                        case SUCCESS:
                                            // Success State
                                            // Data are from Server
                                            businessViewModel.setLoadingState(false);
                                            prgDialog.cancel();

                                            dialogMsg.showSuccessDialog(getString(R.string.success), getString(R.string.ok));
                                            dialogMsg.show();
                                            dialogMsg.okBtn.setOnClickListener(v1 -> {
                                                dialogMsg.cancel();
                                                Config.BUSINESS_SIZE = businessViewModel.getBusinessCount();
                                                try {
                                                    BusinessFragment businessFragment = new BusinessFragment();
                                                    businessFragment.setUpViewModel();
                                                } catch (Exception e) {

                                                }
                                                onBackPressed();
                                            });

                                            break;
                                        case ERROR:
                                            // Error State

                                            prgDialog.cancel();
                                            dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                                            dialogMsg.show();
                                            businessViewModel.setLoadingState(false);
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
//                onBackPressed();
            }
        });
        binding.btnAddImage.setOnClickListener(v -> {
            Dexter.withContext(this).withPermissions(PERMISSIONS).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        someActivityResultLauncher.launch(i);
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
                    Toast.makeText(AddBusinessActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                }
            }).onSameThread().check();
        });
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

    private void setData() {
        binding.etBusinessName.setText(businessItem.name);
        binding.etBusinessNumber.setText(businessItem.phone);
        binding.etBusinessAddress.setText(businessItem.address);
        binding.etBusinessEmail.setText(businessItem.email);
        binding.etBusinessWebsite.setText(businessItem.website);
        GlideBinding.bindImage(binding.ivBusiness, businessItem.logo);
    }

    private void setUpViewModel() {
        userViewModel.getDbUserData(prefManager.getString(Constant.USER_ID)).observe(this, result -> {
            if (result != null) {
                userItem = result.user;
            }
        });
    }

    private Boolean validate() {
        if (binding.etBusinessName.getText().toString().trim().isEmpty()) {
            binding.etBusinessName.setError(getResources().getString(R.string.hint_business_name));
            binding.etBusinessName.requestFocus();
            return false;
        } else if (binding.etBusinessEmail.getText().toString().trim().isEmpty()) {
            binding.etBusinessEmail.setError(getResources().getString(R.string.hint_business_email));
            binding.etBusinessEmail.requestFocus();
            return false;
        } else if (!isEmailValid(binding.etBusinessEmail.getText().toString())) {
            binding.etBusinessEmail.setError(getString(R.string.invalid_email));
            binding.etBusinessEmail.requestFocus();
            return false;
        } else if (binding.etBusinessWebsite.getText().toString().isEmpty()) {
            binding.etBusinessWebsite.setError(getResources().getString(R.string.hint_business_website));
            binding.etBusinessWebsite.requestFocus();
            return false;
        } else if (binding.etBusinessNumber.getText().toString().trim().isEmpty()) {
            binding.etBusinessNumber.setError(getResources().getString(R.string.hint_business_number));
            binding.etBusinessNumber.requestFocus();
            return false;
        } else if (binding.etBusinessAddress.getText().toString().trim().isEmpty()) {
            binding.etBusinessAddress.setError(getResources().getString(R.string.hint_business_address));
            binding.etBusinessAddress.requestFocus();
            return false;
        }
        if (userItem.businessLimit <= Config.BUSINESS_SIZE && !IS_EDIT_MODE) {
            Util.showToast(AddBusinessActivity.this, getString(R.string.your_business_limit));

            return false;
        /*else if (!isEdit && imageUri == null) {
            Toast.makeText(AddBusinessActivity.this, getString(R.string.err_add_image), Toast.LENGTH_SHORT).show();
            return false;
        }*/
        } else {
            return true;
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && !email.contains(" ");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        if (result.getData() != null) {
                            Uri selectedImage = result.getData().getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            if (selectedImage != null) {
                                Cursor cursor = getContentResolver().query(selectedImage,
                                        null, null, null, null);

                                if (cursor != null) {
                                    cursor.moveToFirst();

                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    profileImagePath = cursor.getString(columnIndex);
                                    cursor.close();

                                    try {
                                        Uri fromFile = Uri.fromFile(new File(getCacheDir(),
                                                "SampleCropImage" + System.currentTimeMillis() + ".png"));
                                        UCrop.Options options2 = new UCrop.Options();
                                        options2.setToolbarColor(getResources().getColor(R.color.white));
                                        options2.setFreeStyleCropEnabled(true);
                                        UCrop.of(selectedImage, fromFile).withOptions(options2).start(AddBusinessActivity.this);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int i3 = requestCode;
        int i4 = resultCode;
        if (i4 == RESULT_OK) {
            if (data != null) {
                if (i4 == -1 && i3 == 69) {
                    handleCropResult(data);
                } else if (i4 == 96) {
                    UCrop.getError(data);
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == 1024) {
            if (eraserResultBmp != null) {
                cutBit = eraserResultBmp;
                dialogMsg.ivRemove.setImageBitmap(cutBit);
            }
        }
    }

    private void handleCropResult(Intent data) {
        imageUri = UCrop.getOutput(data);
        ;

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BGConfig.currentBit = bitmap;
        selectedBit = bitmap;

        dialogMsg.showRemoveBGDialog();
        dialogMsg.show();
        GlideBinding.bindImage(dialogMsg.ivReal, imageUri.getPath());
        dialogMsg.btnReal.setOnClickListener(v -> {

            if (dialogMsg.lvRemove.getVisibility() == View.VISIBLE) {

                dialogMsg.cancel();
                GlideBinding.bindImage(binding.ivBusiness, profileImagePath);

            } else {
                dialogMsg.scanAnimation.setVisibility(View.VISIBLE);
                dialogMsg.btnReal.setEnabled(false);
                dialogMsg.btnNo.setEnabled(false);

                new MLCropAsyncTask(new MLOnCropTaskCompleted() {
                    public void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int left, int top) {
                        /*int[] iArr = {0, 0, selectedBit.getWidth(), selectedBit.getHeight()};
                        int width = selectedBit.getWidth();
                        int height = selectedBit.getHeight();
                        int i = width * height;
                        selectedBit.getPixels(new int[i], 0, width, 0, 0, width, height);
                        int[] iArr2 = new int[i];
                        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        createBitmap.setPixels(iArr2, 0, width, 0, 0, width, height);
                        cutBit = ImageUtils.getMask(AddBusinessActivity.this, selectedBit, createBitmap, width, height);
                       */
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                                bitmap, selectedBit.getWidth(), selectedBit.getHeight(), false);
                        cutBit = resizedBitmap;

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Palette p = Palette.from(cutBit).generate();
                                if (p.getDominantSwatch() == null) {
                                    Toast.makeText(AddBusinessActivity.this, "OKK", Toast.LENGTH_SHORT).show();
                                }
                                Util.showLog("BG COMPLETE");
                                dialogMsg.ivRemove.setImageBitmap(resizedBitmap);
                                dialogMsg.lvRemove.setVisibility(View.VISIBLE);
                                dialogMsg.btnReal.setEnabled(true);
                                dialogMsg.btnNo.setEnabled(true);
                                dialogMsg.btnReal.setText("Use This");
                                dialogMsg.btnNo.setText("Change Image");
                                dialogMsg.scanAnimation.setVisibility(View.GONE);
                            }
                        });


                    }
                }, AddBusinessActivity.this).execute(new Void[0]);
            }

        });
        dialogMsg.btnRemove.setOnClickListener(v -> {
            new LoadDownloadImage().execute();
//            imageUri = cutUri;
        });

        dialogMsg.btnNo.setOnClickListener(v -> {
            if (dialogMsg.lvRemove.getVisibility() == View.VISIBLE) {
                dialogMsg.cancel();
                Intent i = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                someActivityResultLauncher.launch(i);
            } else {
                dialogMsg.cancel();
                GlideBinding.bindImage(binding.ivBusiness, profileImagePath);
            }
        });

        dialogMsg.btnManual.setOnClickListener(v -> {
            EraserActivity.b = selectedBit;
            Intent intent = new Intent(AddBusinessActivity.this, EraserActivity.class);
            intent.putExtra(Constant.KEY_OPEN_FROM, Constant.OPEN_FROM_ADD_BUSINESS);
            startActivityForResult(intent, 1024);
        });

    }

    class LoadDownloadImage extends AsyncTask<String, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "." + "BrandPeak" + File.separator);

            if (!directory.exists()) {
                directory.mkdirs();
            } else {
                directory.delete();
                directory.mkdirs();
            }
            File file = new File(directory, "Image_" + System.currentTimeMillis() + ".png");
            if (file.exists()) {
                file.delete();
            }
            boolean checkMemory;
            try {

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                Bitmap createBitmap = Bitmap.createBitmap(cutBit.getWidth(),
                        cutBit.getHeight(), cutBit.getConfig());
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawColor(-1);
                canvas.drawBitmap(cutBit, 0.0f, 0.0f, (Paint) null);
                checkMemory = createBitmap.compress(Bitmap.CompressFormat.PNG,
                        100, fileOutputStream);
                createBitmap.recycle();
                fileOutputStream.flush();
                fileOutputStream.close();

           /* OutputStream outputStream = new FileOutputStream(file);
            paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Uri returnUri;*/
                MediaScannerConnection.scanFile(AddBusinessActivity.this, new String[]{file.getAbsolutePath()},
                        (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                                Util.showLog("ExternalStorage " + "Scanned " + str + ":");
                                StringBuilder sb = new StringBuilder();
                                sb.append("-> uri=");
                                sb.append(uri);
                                sb.append("-> FILE=");
                                sb.append(file.getAbsolutePath());
                                cutUri = uri;
                            }
                        });
                cutImagePath = file.getAbsolutePath();
                return true;
            } catch (Exception e) {
                Util.showErrorLog(e.getMessage(), e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {

                dialogMsg.cancel();
                imageUri = cutUri;
                profileImagePath = cutImagePath;
                binding.ivBusiness.setImageBitmap(cutBit);

            }else{
                Util.showToast(AddBusinessActivity.this, "Try Again");
            }
        }
    }

    private Uri saveBitmap(Bitmap paramBitmap) {

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "." + "BrandPeak" + File.separator);

        if (!directory.exists()) {
            directory.mkdirs();
        } else {
            directory.delete();
            directory.mkdirs();
        }
        File file = new File(directory, "Image_" + System.currentTimeMillis() + ".png");
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

            Uri returnUri;
            MediaScannerConnection.scanFile(AddBusinessActivity.this, new String[]{file.getAbsolutePath()},
                    (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                            Util.showLog("ExternalStorage " + "Scanned " + str + ":");
                            StringBuilder sb = new StringBuilder();
                            sb.append("-> uri=");
                            sb.append(uri);
                            sb.append("-> FILE=");
                            sb.append(file.getAbsolutePath());
                            Util.showLog("DONE FILE: " + sb);
                            cutUri = uri;
                        }
                    });
            cutImagePath = file.getAbsolutePath();
            return Uri.fromFile(file);
        } catch (Exception e) {
            return null;
        }
    }
}