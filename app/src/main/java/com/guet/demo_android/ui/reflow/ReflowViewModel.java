package com.guet.demo_android.ui.reflow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//ViewModel负责保存和管理与UI相关的数据和业务逻辑，将其与Fragment或Activity等UI组件解耦。
public class ReflowViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ReflowViewModel() {
//MutableLiveData是LiveData的一种，可以使用其setValue()方法修改值。
        mText = new MutableLiveData<>();
        mText.setValue("This is reflow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}