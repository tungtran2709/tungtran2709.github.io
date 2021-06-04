package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class Forgot_Password_Activity extends AppCompatActivity {
    private TextInputLayout name, phone, mail;
    private Button search, back;
    private SQLite_Staff sqLite_staff;
    private List<Staff> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        sqLite_staff = new SQLite_Staff(this);
        list = sqLite_staff.getAllStaff();
        initView();
        clickSearch();
        clickBack();
    }

    private void clickSearch() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String Name = name.getEditText().getText().toString();
                    String Phone = phone.getEditText().getText().toString();
                    String Mail = mail.getEditText().getText().toString();
                    if(Name.equals("") || Phone.equals("") || Mail.equals("") || !Mail.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
                        Toast.makeText(Forgot_Password_Activity.this, "Nhập thiếu hoặc sai định dạng!", Toast.LENGTH_LONG).show();
                    }else {
                        int check = 0;
                      //  if(Mail.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
                            for(int i = 0; i < list.size(); i++){
                                if(Name.equals(list.get(i).getName()) && Phone.equals(list.get(i).getPhone()) && Mail.equals(list.get(i).getMail())){
                                    check = 1;
                                    GMailSender sender = new GMailSender(Forgot_Password_Activity.this, Mail, "Chào " + Name + "!", "Mật khẩu của bạn là: " + list.get(i).getPassword() + ".");
                                    sender.execute();
                                    break;
                                }
                            }
                            if(check == 0) Toast.makeText(Forgot_Password_Activity.this, "Tài khoản này không tồn tại!", Toast.LENGTH_LONG).show();
                            else Toast.makeText(Forgot_Password_Activity.this, "Mật khẩu được gửi qua mail!", Toast.LENGTH_LONG).show();
                        }
                  //  }

                }catch (Exception e){

                }
            }
        });
    }

    private void clickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        name = findViewById(R.id.name_forgot);
        phone = findViewById(R.id.phone_forgot);
        mail = findViewById(R.id.mail_forgot);
        search = findViewById(R.id.search_forgot);
        back = findViewById(R.id.agian_login_forgot);
    }
}
