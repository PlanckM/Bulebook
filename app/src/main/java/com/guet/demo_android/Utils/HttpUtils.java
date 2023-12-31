package com.guet.demo_android.Utils;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private static final Gson gson = new Gson();
    // 请求头
    static Headers headers = new Headers.Builder()
            .add("appId", "4d30717bfe774414a81c1198b1fe7d22")
            .add("appSecret", "266335a3f6cde181842a1a34f62a633f927f5")
            .add("Accept", "application/json, text/plain, */*")
            .build();

    public static void post(String url, Map<String, Object> params, boolean inBody, final VolleyCallback callback) {
        new Thread(() -> {
            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
            String strUrl = url;
            String body = "";
            if (!inBody) {
                for (String key : params.keySet()) {
                    strUrl = strUrl + key + '=' + params.get(key) + '&';
                }
                strUrl = strUrl.substring(0, strUrl.length() - 1);
            } else {
                body = gson.toJson(params);
            }
            //请求组合创建
            Request request = new Request.Builder()
                    .url(strUrl)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();

            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, IOException e) {
                        //TODO 请求失败处理
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, Response response) throws IOException {
                        //TODO 请求成功处理
                        // 获取响应体的json串
                        String body = response.body().string();
                        callback.onSuccess(body, gson);
                    }
                });
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static void post(String url, RequestBody body, final VolleyCallback callback) {
        new Thread(() -> {
            String strUrl = url;
            //请求组合创建
            Request request = new Request.Builder()
                    .url(strUrl)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(body)
                    .build();

            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, IOException e) {
                        //TODO 请求失败处理
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, Response response) throws IOException {
                        //TODO 请求成功处理
                        // 获取响应体的json串
                        String body = response.body().string();
                        callback.onSuccess(body, gson);
                    }
                });
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static void get(String url, Map<String, String> params, final VolleyCallback callback) {
        new Thread(() -> {
            String strUrl = url + "?";
            if (params != null) {
                for (String key : params.keySet()) {
                    strUrl = strUrl + key + '=' + params.get(key) + '&';
                }
                strUrl = strUrl.substring(0, strUrl.length() - 1);
                Log.d(" fffff",strUrl);
            }
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "4d30717bfe774414a81c1198b1fe7d22")
                    .add("appSecret", "266335a3f6cde181842a1a34f62a633f927f5")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            //请求组合创建
            Request request = new Request.Builder()
                    .url(strUrl)
                    // 将请求头加至请求中
                    .headers(headers)
                    .get()
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, IOException e) {
                        //TODO 请求失败处理
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, Response response) throws IOException {
                        //TODO 请求成功处理
                        Type jsonType = new TypeToken<ResponseBody<Object>>() {
                        }.getType();
                        // 获取响应体的json串
                        String body = response.body().string();
                        Log.d("body",body);
                        callback.onSuccess(body, gson);
                    }
                });
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */

    /**
     * http响应体的封装协议
     *
     * @param <T> 泛型
     */
    public static class ResponseBody<T> {

        /**
         * 业务响应码
         */
        private int code;
        /**
         * 响应提示信息
         */
        private String msg;
        /**
         * 响应数据
         */
        private T data;

        public ResponseBody() {
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public T getData() {
            return data;
        }

        @NonNull
        @Override
        public String toString() {
            return "ResponseBody{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
}

