package com.guet.demo_android.UI.PersonalInfo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.guet.demo_android.AppContext;
import com.guet.demo_android.Entity.User;

public class PersonalInfoViewModel extends AndroidViewModel {
    private MutableLiveData<String> avatar = new MutableLiveData<>();
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<String> id = new MutableLiveData<>();
    private MutableLiveData<String> introduction = new MutableLiveData<>();
    private MutableLiveData<String> sex = new MutableLiveData<>();

    public MutableLiveData<String> getAvatar() {
        return avatar;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getId() {
        return id;
    }

    public MutableLiveData<String> getIntroduction() {
        return introduction;
    }

    public MutableLiveData<String> getSex() {
        return sex;
    }

    public PersonalInfoViewModel(@NonNull Application application) {
        super(application);
        AppContext app=(AppContext)application;
        User user=app.user;
        avatar.setValue(user.getAvatar());
        username.setValue(user.getUsername());
        id.setValue(user.getId());
        introduction.setValue(user.getIntroduce());
        sex.setValue(user.getSex()+"");
    }
}