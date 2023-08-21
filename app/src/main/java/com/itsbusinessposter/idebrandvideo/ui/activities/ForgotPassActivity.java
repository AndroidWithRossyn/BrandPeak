package com.itsbusinessposter.idebrandvideo.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.databinding.ActivityForgotPassBinding;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.utils.Connectivity;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.UserViewModel;

public class ForgotPassActivity extends AppCompatActivity {

    ActivityForgotPassBinding binding;
    UserViewModel userViewModel;
    ProgressDialog progressDialog;
    Connectivity connectivity;
    DialogMsg dialogMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        dialogMsg = new DialogMsg(this, false);
        connectivity = new Connectivity(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_loading));
        progressDialog.setCancelable(false);

        setUpUi();
    }

    private void setUpUi() {
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolName.setText(getResources().getString(R.string.forgot_password));

        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnSend.setOnClickListener(v -> {
            if (valid()) {
                if (!connectivity.isConnected()) {
                    Util.showToast(this, getString(R.string.error_message__no_internet));
                    return;
                }
                userViewModel.isLoading = true;

                userViewModel.forgotPassword(binding.etEmailAddress.getText().toString()).observe(this,
                        listResource -> {

                            if (listResource != null) {

                                Util.showLog("Got Data " + listResource.message + listResource.toString());

                                switch (listResource.status) {
                                    case LOADING:
                                        // Loading State
                                        // Data are from Local DB

                                        progressDialog.show();
                                        updateForgotBtnStatus();

                                        break;
                                    case SUCCESS:
                                        // Success State
                                        // Data are from Server

                                        if (listResource.data != null) {
                                            progressDialog.cancel();
                                            dialogMsg.showSuccessDialog(listResource.data.message, getString(R.string.ok));
                                            dialogMsg.show();
                                            userViewModel.isLoading = false;
                                            updateForgotBtnStatus();
                                        }

                                        break;
                                    case ERROR:
                                        // Error State

                                        progressDialog.cancel();
                                        dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                                        dialogMsg.show();
                                        userViewModel.isLoading = false;
                                        updateForgotBtnStatus();

                                        break;
                                    default:

                                        break;
                                }

                            } else {

                                // Init Object or Empty Data
                                Util.showLog("Empty Data");

                            }
                        });
            }
        });
    }

    private void updateForgotBtnStatus() {
        if (userViewModel.isLoading) {
            binding.btnSend.setText(getResources().getString(R.string.login_loading));
        } else {
            binding.btnSend.setText(getResources().getString(R.string.send));
        }
    }

    private boolean valid() {
        if (binding.etEmailAddress.getText().toString().trim().isEmpty()) {
            binding.etEmailAddress.setError(getResources().getString(R.string.email));
            binding.etEmailAddress.requestFocus();
            return false;
        } else if (!isEmailValid(binding.etEmailAddress.getText().toString())) {
            binding.etEmailAddress.setError(getString(R.string.invalid_email));
            binding.etEmailAddress.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && !email.contains(" ");
    }
}