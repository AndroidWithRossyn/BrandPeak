package com.itsbusinessposter.idebrandvideo.ui.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.itsbusinessposter.idebrandvideo.Ads.BannerAdManager;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.databinding.ActivityProfileEditBinding;
import com.itsbusinessposter.idebrandvideo.items.UserLogin;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.utils.Connectivity;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.UserViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class ProfileEditActivity extends AppCompatActivity {

    ActivityProfileEditBinding binding;
    UserViewModel userViewModel;
    PrefManager prefManager;
    Uri imageUri;
    String profileImagePath;
    ProgressDialog prgDialog;
    DialogMsg dialogMsg;

    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    Connectivity connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
        prgDialog = new ProgressDialog(this);
        connectivity = new Connectivity(this);
        prgDialog.setMessage(getString(R.string.login_loading));
        prgDialog.setCancelable(false);

        dialogMsg = new DialogMsg(this, false);

        BannerAdManager.showBannerAds(this, binding.llAdview);
        setUpUi();
        setUpViewModel();
    }

    private void setUpViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getLoginUser(prefManager.getString(Constant.USER_ID)).observe(this, data -> {
            if (data != null) {
                setData(data);
            }
        });
    }

    private void setData(UserLogin data) {
        binding.etName.setText(data.user.userName);
        binding.etEmail.setText(data.user.email);
        binding.etPhone.setText(data.user.phone);

        if (!data.user.userImage.equals("")) {
            GlideBinding.bindImage(binding.ivBusiness, data.user.userImage);
        }

    }

    private void setUpUi() {
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolName.setText(getResources().getString(R.string.edit_profile));

        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnSave.setOnClickListener(v -> {
            if (validate()) {
                if (!connectivity.isConnected()) {
                    Util.showToast(this, getString(R.string.error_message__no_internet));
                    return;
                }
                prgDialog.show();
                userViewModel.uploadImage(this, profileImagePath, imageUri, prefManager.getString(Constant.USER_ID),
                        binding.etName.getText().toString().trim(),
                        binding.etEmail.getText().toString().trim(),
                        binding.etPhone.getText().toString().trim(),
                        getContentResolver()).observe(this, listResource -> {
                    if (listResource != null && listResource.data != null) {
                        Util.showLog("Got Data" + listResource.message + listResource.toString());
                        prgDialog.cancel();

                        dialogMsg.showSuccessDialog(getString(R.string.success_profile), getString(R.string.ok));
                        dialogMsg.show();
                        dialogMsg.okBtn.setOnClickListener(view -> {
                            onBackPressed();
                        });

                    } else if (listResource != null && listResource.message != null) {
                        Util.showLog("Message from server.");

                        dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                        dialogMsg.show();

                        prgDialog.cancel();
                    } else {

                        Util.showLog("Empty Data");

                    }
                });
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
                    Toast.makeText(ProfileEditActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                }
            }).onSameThread().check();
        });

        binding.etPhone.setEnabled(false);
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

    private Boolean validate() {
        if (binding.etName.getText().toString().trim().isEmpty()) {
            binding.etName.setError(getResources().getString(R.string.hint_name));
            binding.etName.requestFocus();
            return false;
        } else if (binding.etEmail.getText().toString().trim().isEmpty()) {
            binding.etEmail.setError(getResources().getString(R.string.email));
            binding.etEmail.requestFocus();
            return false;
        } else if (!isEmailValid(binding.etEmail.getText().toString())) {
            binding.etEmail.setError(getString(R.string.invalid_email));
            binding.etEmail.requestFocus();
            return false;
        } else if (binding.etPhone.getText().toString().isEmpty()) {
            binding.etPhone.setError(getResources().getString(R.string.hint_phone_number));
            binding.etPhone.requestFocus();
            return false;
        } /*else if (imageUri == null) {
            Util.showToast(ProfileEditActivity.this, getString(R.string.err_add_image));
            return false;
        }*/ else {
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

                                    imageUri = selectedImage;

                                    GlideBinding.bindImage(binding.ivBusiness, profileImagePath);
                                }
                            }
                        }
                    }
                }
            });
}