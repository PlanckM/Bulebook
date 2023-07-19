package com.guet.demo_android;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    public static final String MESSAGE_STRING = "com.guet.gxk.code02.MESSAGE";
    private Boolean bPwdSwitch = false;
    private EditText etPwd;

    private EditText etAccount;
    private CheckBox cbRememberPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //绑定对应控件
        etPwd = findViewById(R.id.et_pwd);
        etAccount = findViewById(R.id.et_account);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);

        getIntent();
        //从 SharedPreferences 中读取保存的账号和密码，并设置到相应的控件上：
        String spFileName = getResources()
                .getString(R.string.shared_preferences_file_name);
        String accountKey = getResources()
                .getString(R.string.login_account_name);
        String passwordKey = getResources()
                .getString(R.string.login_password);
        String rememberPasswordKey = getResources()
                .getString(R.string.login_remember_password);

        SharedPreferences spFile = getSharedPreferences(
                spFileName,
                MODE_PRIVATE);
        //通过key读取数据，defaultValue是默认值，如果找不到对应的键，则返回defaultValue。
        String account = spFile.getString(accountKey, null);
        String password = spFile.getString(passwordKey, null);
        Boolean rememberPassword = spFile.getBoolean(rememberPasswordKey, false);

        //
        if (account != null && !TextUtils.isEmpty(account)) {
            etAccount.setText(account);
        }

        if (password != null && !TextUtils.isEmpty(password)) {
            etPwd.setText(password);
        }

        cbRememberPwd.setChecked(rememberPassword);


        Button btLogin = findViewById(R.id.bt_login);

        //处理点击登录事件
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPwd.getText().toString();
                String account = etAccount.getText().toString();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(account)) {
                    // 创建一个Toast对象
                    Toast toast = Toast.makeText(LoginActivity.this, "Please input account or password!", Toast.LENGTH_SHORT);
                    // 设置Toast的位置为顶部
//                    toast.setGravity(Gravity.TOP, 0, 0);
                    // 自定义Toast的布局
                    LayoutInflater inflater = getLayoutInflater();
                    View customView = inflater.inflate(R.layout.custom_toast, null);
                    // 将自定义布局设置给Toast
                    toast.setView(customView);
                    // 显示Toast
                    toast.show();
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);




                //在string文件下获取键值
                String spFileName = getResources()
                        .getString(R.string.shared_preferences_file_name);
                String accountKey = getResources()
                        .getString(R.string.login_account_name);
                String passwordKey = getResources()
                        .getString(R.string.login_password);
                String rememberPasswordKey = getResources()
                        .getString(R.string.login_remember_password);
                //写入数据
                //Context.MODE_PRIVATE表示该SharedPreferences对象的访问权限为私有，只能被当前应用程序访问。
                //spFileName文件名
                SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
                //通过edit()方法获取SharedPreferences.Editor对象，然后使用该对象的各种putXxx()方法来写入数据。
                SharedPreferences.Editor editor = spFile.edit();

                //用于检查复选框是否被选中
                //选中就存数据到SharedPreferences，否则根据key清除数据
                if (cbRememberPwd.isChecked()) {
                    password = etPwd.getText().toString();
                    account = etAccount.getText().toString();

                    editor.putString(accountKey, account);
                    editor.putString(passwordKey, password);
                    editor.putBoolean(rememberPasswordKey, true);
                    editor.apply();
                } else {
                    editor.remove(accountKey);
                    editor.remove(passwordKey);
                    editor.remove(rememberPasswordKey);
                    editor.apply();
                }
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

