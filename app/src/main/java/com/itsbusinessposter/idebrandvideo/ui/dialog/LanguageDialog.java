package com.itsbusinessposter.idebrandvideo.ui.dialog;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.itsbusinessposter.idebrandvideo.adapters.LanguageAdapter;
import com.itsbusinessposter.idebrandvideo.databinding.LanguageDialogBinding;
import com.itsbusinessposter.idebrandvideo.items.LanguageItem;
import com.itsbusinessposter.idebrandvideo.listener.ClickListener;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.LanguageViewModel;

import java.util.List;

public class LanguageDialog extends BottomSheetDialog {

    public Activity activity;
    LanguageAdapter adapter;
    LanguageViewModel languageViewModel;

    LanguageDialogBinding binding;
    LClickListener listener;
    PrefManager prefManager;

    public LanguageDialog(@NonNull Activity activity, LClickListener listener) {
        super(activity);
        this.listener = listener;
        this.activity = activity;
        prefManager = new PrefManager(activity);
        binding = LanguageDialogBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(binding.getRoot());

        languageViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(LanguageViewModel.class);

        binding.rvLanguage.setLayoutManager(new GridLayoutManager(activity, 2));

        adapter = new LanguageAdapter(activity, new ClickListener<LanguageItem>() {
            @Override
            public void onClick(LanguageItem data) {
                setLanguage(data);
            }
        });

        binding.rvLanguage.setAdapter(adapter);
        getSelected();
        binding.tvLangName.setOnClickListener(v -> {
            prefManager.setString(Constant.USER_LANGUAGE, "");
            listener.onClick(selectedLanguage());
            dismiss();
        });

        binding.txtSave.setOnClickListener(v -> {
            Util.showLog(selectedLanguage());
            listener.onClick(selectedLanguage());
            dismiss();
        });
    }

    public String selectedLanguage() {
       /* String selected = "";
        if (adapter.getSelectedItems().size() > 0) {
            for (LanguageItem languages : adapter.getSelectedItems()) {
                selected += "," + languages.id;
            }
        } else {
            selected = "";
        }*/
        return prefManager.getString(Constant.USER_LANGUAGE);
    }

    private void getSelected() {
        List<LanguageItem> languages = adapter.getSelectedItems();
        if (languages.size() > 0) {
            binding.rbCheck.setChecked(false);
        } else {
            binding.rbCheck.setChecked(true);
        }
    }

    private void setLanguage(LanguageItem data) {

        prefManager.setString(Constant.USER_LANGUAGE, data.title);
//        languageViewModel.UpdateLanguage(data.id, data.isChecked ? false : true);
        listener.onClick(selectedLanguage());
        dismiss();
    }

    public void showDialog() {
        languageViewModel.getLanguages().observe((LifecycleOwner) activity, result->{
            if (result.data!=null){
                if (result.data.size()>0){
                    adapter.setLanguageItemList(result.data);
                }else{

                }
            }
        });
        show();
        setSelectedCategories();
    }

    public void setSelectedCategories() {

//        if (!prefManager.getString(Constant.USER_LANGUAGE).equals("") && adapter != null) {
//            List<String> list = Arrays.asList(prefManager.getString(Constant.USER_LANGUAGE).split(","));
//            for (int i = 0; i < list.size(); i++) {
//                languageViewModel.UpdateLanguage(list.get(i), true);
//            }
//        }

    }

    public interface LClickListener {
        void onClick(String selectedLanguage);
    }

}
