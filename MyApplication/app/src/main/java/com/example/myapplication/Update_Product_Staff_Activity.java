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

public class Update_Product_Staff_Activity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageView img;
    private Spinner type;
    private EditText name, count, price, date, des;
    private Button save, back;
    private String[] arr_type = {"Máy tính", "Điện thoại", "Phụ kiện"};
    private DatePickerDialog picker;
    private SQLite_Product sqLite_product;
    private byte[] Img;
    private String Type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_product_staff);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        initView();
        sqLite_product = new SQLite_Product(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        Product p = sqLite_product.getProduct(id);
        byte[] bytes = p.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(bitmap);
        if(p.getType().equals("Máy tính")) type.setSelection(0);
        else if(p.getType().equals("Điện thoại")) type.setSelection(1);
        else if(p.getType().equals("Phụ kiện")) type.setSelection(2);
        name.setText(p.getName());
        count.setText(String.valueOf(p.getCount()));
        price.setText(p.getPrice());
        date.setText(p.getDate());
        des.setText(p.getDes());
        clickImg();
        clickType();
        clickDate();
        clickUpdate(id);
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

    private void clickUpdate(int id) {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    updateImg();
                    String Name = name.getText().toString();
                    int Count = Integer.parseInt(count.getText().toString());
                    String Price = price.getText().toString();
                    String Date = date.getText().toString();
                    String Des = des.getText().toString();
                    if(!Name.equals("") && !Price.equals("") && !Date.equals("") && !Des.equals("")){
                        Product p = new Product(id, Img, Type, Name, Count, Price, Date, Des);
                        sqLite_product.updateProduct(p);
                        finish();
                        Toast.makeText(Update_Product_Staff_Activity.this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_LONG).show();
                    } else Toast.makeText(Update_Product_Staff_Activity.this, "Nhập thiếu thông tin!", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(Update_Product_Staff_Activity.this, "Nhập thiếu thông tin!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clickType() {
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Type = arr_type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void clickDate() {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                picker = new DatePickerDialog(Update_Product_Staff_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
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

    private void initView() {
        img = findViewById(R.id.img_pro_up);
        type = findViewById(R.id.type_pro_up);
        name = findViewById(R.id.name_pro_up);
        count = findViewById(R.id.count_pro_up);
        price = findViewById(R.id.price_pro_up);
        date = findViewById(R.id.date_pro_up);
        des = findViewById(R.id.des_pro_up);
        save = findViewById(R.id.save_pro_up);
        back = findViewById(R.id.back_pro_up);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Update_Product_Staff_Activity.this,
                android.R.layout.simple_spinner_item, arr_type);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(arrayAdapter);
    }
}
