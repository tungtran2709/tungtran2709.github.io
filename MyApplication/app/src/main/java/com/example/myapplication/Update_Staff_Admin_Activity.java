package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class Update_Staff_Admin_Activity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private EditText name, phone, mail, date;
    private ImageView img;
    private Spinner gender, position;
    private Button update, back;
    private SQLite_Staff sqLite_staff;
    private String str_gender[] = {"Nam", "Nữ"}, str_position[] = {"Admin", "Nhân viên"};
    private String Gender, Position;
    private DatePickerDialog picker;
    private byte[] Img = new byte[1024];
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_staff_admin);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        initView();

        sqLite_staff = new SQLite_Staff(this);
        Intent intent = getIntent();
        int id_login = intent.getIntExtra("id_login", 0);
        int id = intent.getIntExtra("id", 0);
        System.out.println(id_login + " " + id);
        Staff s = sqLite_staff.getStaff(id);
        Staff S = sqLite_staff.getStaff(id_login);
        if(S.getPosition().equals("Nhân viên") || (S.getPosition().equals("Admin") && s.getPosition().equals("Admin"))){
            img.setEnabled(false);
            name.setTextColor(R.color.red);
            phone.setEnabled(false);
            phone.setTextColor(R.color.red);
            mail.setEnabled(false);
            mail.setTextColor(R.color.red);
            date.setEnabled(false);
            date.setTextColor(R.color.red);
            gender.setEnabled(false);
            position.setEnabled(false);
            update.setEnabled(false);
        }
        byte[] bytes = s.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(bitmap);
        name.setText(s.getName());
        phone.setText(s.getPhone());
        mail.setText(s.getMail());
        date.setText(s.getDate());
        if(s.getGender().equals("Nam"))     gender.setSelection(0);
        else gender.setSelection(1);
        if(s.getPosition().equals("Admin"))  position.setSelection(0);
        else if(s.getPosition().equals("Nhân viên"))   position.setSelection(1);
        clickImg();
        clickDate();
        clickGender();
        clickPosition();
        clickUpdate(id);
        clickBack();
    }

    private void clickImg() {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImg = data.getData();
            img.setImageURI(selectedImg);
        }
    }

    private void clickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void clickUpdate(int id) {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    // cập nhật img
                    updateImg();
                    String Phone = phone.getText().toString();
                    String Mail = mail.getText().toString();
                    String Date = date.getText().toString();
                    if(!Phone.equals("") && !Mail.equals("") && !Date.equals("")){
                        if(Mail.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
                            Staff s = new Staff(id, Img, Phone, Mail, Date, Gender, Position);
                            sqLite_staff.updateStaff(s);
                            finish();
                            Toast.makeText(Update_Staff_Admin_Activity.this, "Cập nhật nhân viên thành công!", Toast.LENGTH_LONG).show();
                        } else Toast.makeText(Update_Staff_Admin_Activity.this, "Sai định dạng Email", Toast.LENGTH_LONG).show();
                    } else Toast.makeText(Update_Staff_Admin_Activity.this, "Nhập thiếu thông tin", Toast.LENGTH_LONG).show();
                } catch (Exception e){

                }
            }
        });
    }

    private void updateImg(){
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Img = stream.toByteArray();
        // nén ảnh
        if (Img.length > 100000){
            bitmap = BitmapFactory.decodeByteArray(Img, 0, Img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.2), (int)(bitmap.getHeight()*0.2), true);
            stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Img = stream.toByteArray();
        }
    }

    private void clickGender() {
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Gender = str_gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void clickDate() {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                picker = new DatePickerDialog(Update_Staff_Admin_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }

    private void clickPosition() {
        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Position = str_position[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void initView() {
        img = findViewById(R.id.img_up);
        name = findViewById(R.id.name_up);
        phone = findViewById(R.id.phone_up);
        mail = findViewById(R.id.mail_up);
        date = findViewById(R.id.date_up);
        gender = findViewById(R.id.gender_up);
        position = findViewById(R.id.position_up);
        update = findViewById(R.id.update_up);
        back = findViewById(R.id.back_up);
        //spinner gender
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(Update_Staff_Admin_Activity.this, android.R.layout.simple_spinner_item, str_gender);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(arrayAdapter1);
        //spinner relationship
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(Update_Staff_Admin_Activity.this, android.R.layout.simple_spinner_item, str_position);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position.setAdapter(arrayAdapter2);
    }
}
