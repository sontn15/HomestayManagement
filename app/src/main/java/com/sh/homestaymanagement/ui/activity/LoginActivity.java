package com.sh.homestaymanagement.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.homestaymanagement.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edt_username, edt_password;
    Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        edt_username = this.findViewById(R.id.edt_uername_login);
        edt_password = this.findViewById(R.id.edt_password_login);
        btn_login = this.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String username = edt_username.getText().toString();
        String password = edt_password.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.vui_long_nhap_day_du_thong_tin), Toast.LENGTH_SHORT).show();
            return;
        }
        checkUserLogin(username, password);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    private void checkUserLogin(String username, String password) {
        if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
            startActivity(new Intent(LoginActivity.this, QuanLyPhongActivity.class));
            finish();
        } else {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.sai_thong_tin_dang_nhap), Toast.LENGTH_SHORT).show();
        }
    }
}