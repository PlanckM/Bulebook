package com.guet.demo_android;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.guet.demo_android.ui.SharedViewModel;

public class SharedViewModelFactory implements ViewModelProvider.Factory {
    private AppContext appContext;
    public SharedViewModelFactory(AppContext appContext) {
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SharedViewModel.class)) {
            // 如果请求的是 SharedViewModel 类型的 ViewModel，创建并返回它
            return (T) new SharedViewModel(appContext);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}