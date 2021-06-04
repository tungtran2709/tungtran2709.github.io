package com.example.myapplication;

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
import java.util.List;
import java.util.Random;

public class Add_Staff_Admin_Activity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageView img;
    private EditText name, phone, mail, date;
    private Spinner gender, position;
    private Button save, back;
    private DatePickerDialog picker;
    private String str_gender[] = {"Nam", "Nữ"}, str_position[] = {"Admin", "Nhân viên"};
    private String Name, Phone, Mail, Date, Gender, Position, Pass;
    private byte[] Img;
    private SQLite_Staff sqLite_staff;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_staff_admin);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        sqLite_staff = new SQLite_Staff(this);
        initView();
        clickImg();
        clickDate();
        clickGender();
        clickPosition();
        clickSave();
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
        img = findViewById(R.id.img_add);
        name = findViewById(R.id.name_add);
        phone = findViewById(R.id.phone_add);
        mail = findViewById(R.id.mail_add);
        date = findViewById(R.id.date_add);
        gender = findViewById(R.id.gender_add);
        save = findViewById(R.id.save_add);
        back = findViewById(R.id.back_add);
        position = findViewById(R.id.position_add);
        //spinner gender
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(Add_Staff_Admin_Activity.this, android.R.layout.simple_spinner_item, str_gender);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(arrayAdapter1);
        // spinner position
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(Add_Staff_Admin_Activity.this, android.R.layout.simple_spinner_item, str_position);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position.setAdapter(arrayAdapter2);
    }

    private void clickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void clickSave() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // thêm ảnh
                    addImg();
                    Name = name.getText().toString();
                    Phone = phone.getText().toString();
                    Mail = mail.getText().toString();
                    Date = date.getText().toString();
                    // tạo pass random
                    createPass();
                    if(!Name.equals("") && !Phone.equals("") && !Mail.equals("") && !Date.equals("")){
                        if(Mail.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
                            Staff s = new Staff(Img, Name, Pass, Phone, Mail, Date, Gender, Position);
                            // check tài khoản tồn tại chưa
                            if(checkStaff(s)){
                                System.out.println(Mail);
                                GMailSender sender = new GMailSender(Add_Staff_Admin_Activity.this, Mail, "Chào " + Name + "!", "Mật khẩu của bạn là: " + Pass + ".");
                                sender.execute();
                                sqLite_staff.addStaff(s);
                                finish();
                                Toast.makeText(Add_Staff_Admin_Activity.this, "Tạo nhân viên thành công!", Toast.LENGTH_LONG).show();
                            } else Toast.makeText(Add_Staff_Admin_Activity.this, "Tên, hoặc số điện thoại, hoặc mail đã dược đăng ký!", Toast.LENGTH_LONG).show();
                        } else Toast.makeText(Add_Staff_Admin_Activity.this, "Sai định dạng Email!", Toast.LENGTH_LONG).show();
                    } else Toast.makeText(Add_Staff_Admin_Activity.this, "Nhập thiếu thông tin!", Toast.LENGTH_LONG).show();
                }catch (Exception e){

                }
            }
        });
    }

    private boolean checkStaff(Staff s){
        List<Staff> list = sqLite_staff.getAllStaff();
        for(int i = 0; i < list.size(); i++) {
            if (s.getName().equals(list.get(i).getName()) || s.getPhone().equals(list.get(i).getPhone()) || s.getMail().equals(list.get(i).getMail()))
                return false;
        }
            return true;
    }

    private void addImg(){
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

    private void createPass(){
        StringBuffer random = new StringBuffer();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        Random r = new Random();
        for (int i = 0; i < 8; i++){
            int index = (int) (r.nextFloat() * chars.length());
            random.append(chars.charAt(index));
        }
        Pass = random.toString();
        System.out.println(Pass);
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

                picker = new DatePickerDialog(Add_Staff_Admin_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }
}
