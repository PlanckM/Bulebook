package com.guet.demo_android;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.guet.demo_android.ui.SharedViewModel;

public class SharedViewModelFactory implements ViewModelProvider.Factory {

    private AppContext appContext;
    private String URL;

    public SharedViewModelFactory(AppContext appContext, String URL) {
        this.appContext = appContext;
        this.URL = URL;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SharedViewModel.class)) {
            // 如果请求的是 SharedViewModel 类型的 ViewModel，创建并返回它
            return (T) new SharedViewModel(appContext, URL);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}