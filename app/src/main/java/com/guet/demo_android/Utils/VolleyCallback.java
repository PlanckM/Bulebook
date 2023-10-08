package com.guet.demo_android.Utils;

import com.google.gson.Gson;

public interface VolleyCallback {
    void onSuccess(String body, Gson gson);
}
