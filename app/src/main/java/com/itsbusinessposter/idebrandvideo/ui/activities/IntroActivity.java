package com.itsbusinessposter.idebrandvideo.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.adapters.IndicatorAdapter;
import com.itsbusinessposter.idebrandvideo.databinding.ActivityIntroBinding;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    ActivityIntroBinding binding;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);

        setUpUi();
        addIndicator(0);
    }

    private void setUpUi() {

        binding.viewPager.setAdapter(new IndicatorAdapter(3));
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                addIndicator(position);
                if (position < 2) {
                    binding.txtNext.setVisibility(View.VISIBLE);
                    binding.txtSkip.setVisibility(View.VISIBLE);
                    binding.llIndicator.setVisibility(View.VISIBLE);
                    binding.txtNext.setText("NEXT");
                    binding.checkbox.setVisibility(View.GONE);
                } else {
                    binding.checkbox.setVisibility(View.VISIBLE);
                    binding.txtSkip.setVisibility(View.GONE);
                    binding.llIndicator.setVisibility(View.GONE);
                    binding.txtNext.setText("LET'S START");
                }

                if (position == 0) {
                    Util.StatusBarColor(getWindow(), getResources().getColor(R.color.intro_1));
                }else if(position==1){
                    Util.StatusBarColor(getWindow(), getResources().getColor(R.color.intro_2));
                }else{
                    Util.StatusBarColor(getWindow(), getResources().getColor(R.color.intro_3));
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.txtNext.setOnClickListener(v -> {
            if (binding.txtNext.getText().equals("NEXT")) {
                int current = getItem(+1);
                if (current < 3) {
                    binding.viewPager.setCurrentItem(current);
                }
            } else {
                prefManager.setBoolean(Constant.IS_FIRST_TIME_LAUNCH, binding.checkbox.isChecked());
                gotoLoginActivity();
            }
        });

        binding.txtSkip.setOnClickListener(v -> {
            gotoLoginActivity();
        });

    }

    private void gotoLoginActivity() {
        Util.StatusBarColor(getWindow(), getResources().getColor(R.color.primary_color));
        Intent intent;
        if(prefManager.getBoolean(Constant.IS_LOGIN)){
            intent = new Intent(this, MainActivity.class);
        }else{
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
    }

    private int getItem(int i) {
        return binding.viewPager.getCurrentItem() + i;
    }

    private void addIndicator(int position) {
        List<TextView> pages = new ArrayList<>();
        pages.clear();
        pages.add(binding.textView2);
        pages.add(binding.textView3);
        pages.add(binding.textView4);

        for (int i = 0; i < pages.size(); i++) {
            pages.get(i).setText(Html.fromHtml("&#8226;"));
            if (i == position) {
                pages.get(i).setTextColor(getResources().getColor(R.color.amber_500));
            } else {
                pages.get(i).setTextColor(getResources().getColor(R.color.grey_300));
            }
        }
    }
}