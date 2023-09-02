package com.guet.demo_android;

import com.google.gson.Gson;

public interface VolleyCallback {
    void onSuccess(String body, Gson gson);
}
