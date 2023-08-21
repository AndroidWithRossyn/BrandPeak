package com.itsbusinessposter.idebrandvideo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.databinding.IntroScreen1Binding;
import com.itsbusinessposter.idebrandvideo.databinding.IntroScreen2Binding;
import com.itsbusinessposter.idebrandvideo.databinding.IntroScreen3Binding;

public class IndicatorAdapter extends PagerAdapter {
    int size;

    public IndicatorAdapter(int size) {
        this.size = size;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (position == 0) {
            IntroScreen1Binding binding = IntroScreen1Binding.inflate(LayoutInflater.from(container.getContext()), container, false);
            container.addView(binding.getRoot());
            return binding.getRoot();
        } else if (position == 1) {
            IntroScreen2Binding binding = IntroScreen2Binding.inflate(LayoutInflater.from(container.getContext()), container, false);
            container.addView(binding.getRoot());
            return binding.getRoot();
        } else {
            IntroScreen3Binding binding = IntroScreen3Binding.inflate(LayoutInflater.from(container.getContext()), container, false);
            container.addView(binding.getRoot());
            return binding.getRoot();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
