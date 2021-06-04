package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class Login_Activity extends AppCompatActivity {
    private TextInputLayout name, pass;
    private TextView forgot_pass;
    private Button login;
    private SQLite_Staff sqLite_staff;
    private List<Staff> list_staff;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        sqLite_staff = new SQLite_Staff(this);
        list_staff = sqLite_staff.getAllStaff();
        System.out.println(list_staff.size());
        initView();
        clickForgotPass();
        clickLogin();
    }

    private void clickLogin() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int check = 0;
                    String Name = name.getEditText().getText().toString();
                    String Pass = pass.getEditText().getText().toString();
                    if(Name.equals("") || Pass.equals(""))
                        Toast.makeText(Login_Activity.this, "Nhập thiếu tài khoản hoặc mật khẩu!",
                                Toast.LENGTH_LONG).show();
                    else {
                        for(int i = 0; i < list_staff.size(); i++){
                            if(Name.equals(list_staff.get(i).getName()) &&
                                    Pass.equals(list_staff.get(i).getPassword()) &&
                                    list_staff.get(i).getPosition().equals("Admin")){
                                check = 1;
                                Intent intent = new Intent(Login_Activity.this, MainActivity_Admin.class);
                                intent.putExtra("id_login", list_staff.get(i).getId());
                                startActivity(intent);
                            }
                            if(Name.equals(list_staff.get(i).getName()) &&
                                    Pass.equals(list_staff.get(i).getPassword()) &&
                                    list_staff.get(i).getPosition().equals("Nhân viên")){
                                check = 1;
                                Intent intent = new Intent(Login_Activity.this, MainActivity_Staff.class);
                                intent.putExtra("id_login", list_staff.get(i).getId());
                                startActivity(intent);
                            }
                        }
                        if(check == 0)  Toast.makeText(Login_Activity.this, "Tài khoản hoặc mật khẩu không đúng!",
                                Toast.LENGTH_LONG).show();
                        else Toast.makeText(Login_Activity.this, "Đăng nhập thành công!",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e){

                }
            }
        });
    }

    private void clickForgotPass() {
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, Forgot_Password_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        name = findViewById(R.id.name_login);
        pass = findViewById(R.id.pass_login);
        forgot_pass = findViewById(R.id.forgot_pass);
        login = findViewById(R.id.login_login);
    }
}
