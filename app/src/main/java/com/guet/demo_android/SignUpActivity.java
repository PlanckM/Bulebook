package com.guet.demo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText etAccount;
    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etAccount=findViewById(R.id.et_account);
        etPwd=findViewById(R.id.et_pwd);
        findViewById(R.id.tv_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.bt_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPwd.getText().toString();
                String account = etAccount.getText().toString();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(account)) {
                    Toast.makeText(SignUpActivity.this, "Please input account or password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String Url="http://47.107.52.7:88/member/photo/user/register?";
                Map<String,Object > params=new HashMap<String,Object>();
                params.put("password",password);
                params.put("username",account);
                HttpUtils.post(Url,params,true,new VolleyCallback() {
                    @Override
                    public void onSuccess(String body, Gson gson) {
                        Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                        HttpUtils.ResponseBody<Object> result= gson.fromJson(body,jsonType);
                        if(result.getCode()==500){
                            Looper.prepare();
                            Toast.makeText(SignUpActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        else if(result.getCode()==200){
                            Looper.prepare();
                            Toast.makeText(SignUpActivity.this, "注册成功，请前往登陆!", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        else{
                            Looper.prepare();
                            Toast.makeText(SignUpActivity.this, "注册失败，出现未知错误！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                });
            }
        });
        final ImageView ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        ivPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bPwdSwitch = !bPwdSwitch;
                if (bPwdSwitch) {
                    ivPwdSwitch.setImageResource(R.drawable.baseline_visibility_24);
                    //设置 etPwd 的InputType为可见密码模式
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    ivPwdSwitch.setImageResource(R.drawable.baseline_visibility_off_24);
                    //设置 etPwd 的InputType为密码模式
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    //并将字体类型设置为默认字体
                    etPwd.setTypeface(Typeface.DEFAULT);
                }
            }
        });
    }
}