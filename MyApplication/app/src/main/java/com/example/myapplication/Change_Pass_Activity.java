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

public class Change_Pass_Activity extends AppCompatActivity {
    private TextInputLayout pass, pass_new, pass_new_again;
    private Button change, back;
    private int id_login;
    private SQLite_Staff sqLite_staff;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        sqLite_staff = new SQLite_Staff(this);
        Intent intent = getIntent();
        id_login = intent.getIntExtra("id_login", 0);
        initView();
        clickChange();
        clickBack();
    }

    private void clickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void clickChange() {
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String Pass = pass.getEditText().getText().toString();
                    String Pass_new = pass_new.getEditText().getText().toString();
                    String Pass_new_again = pass_new_again.getEditText().getText().toString();
                    Staff s = sqLite_staff.getStaff(id_login);
                    if(Pass.equals(s.getPassword())){
                        if(Pass_new.equals(Pass_new_again)){
                            sqLite_staff.changePassStaff(s, Pass_new);
                            Toast.makeText(Change_Pass_Activity.this, "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show();
                            finish();
                        }else Toast.makeText(Change_Pass_Activity.this, "Mật khẩu mới không khớp", Toast.LENGTH_LONG).show();
                    }else Toast.makeText(Change_Pass_Activity.this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_LONG).show();
                }catch (Exception e){

                }
            }
        });
    }

    private void initView() {
        pass = findViewById(R.id.pass_change);
        pass_new = findViewById(R.id.pass_new_change);
        pass_new_again = findViewById(R.id.pass_new_agian_change);
        change = findViewById(R.id.change_change);
        back = findViewById(R.id.back_change);
    }
}
