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
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

public class Add_Product_Staff_Activity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private Spinner type;
    private ImageView img;
    private EditText name, count, price, date, des;
    private Button save, back;
    private DatePickerDialog picker;
    private String[] arr_type = {"Máy tính", "Điện thoại", "Phụ kiện"};
    private SQLite_Product sqLite_product;
    private byte[] Img;
    private String Type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_staff);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        sqLite_product = new SQLite_Product(this);
        initView();
        clickType();
        clickImg();
        clickDate();
        clickSave();
        clickBack();
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
    private boolean checkProduct(Product p){
        List<Product> list = sqLite_product.getAllProduct();
        for(int i = 0; i < list.size(); i++) {
            if (p.getName().equals(list.get(i).getName()))
                return false;
        }
        return true;
    }
    private void clickSave() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addImg();
                    String Name = name.getText().toString();
                    int Count = Integer.parseInt(count.getText().toString());
                    String Price = price.getText().toString();
                    String Date = date.getText().toString();
                    String Des = des.getText().toString();
                    if(!Name.equals("") && !Price.equals("") && !Date.equals("") && !Des.equals("")){
                        Product p = new Product(Img, Type, Name, Count, Price, Date, Des);
                        if(checkProduct(p)){
                            sqLite_product.addProduct(p);
                            finish();
                            Toast.makeText(Add_Product_Staff_Activity.this, "Tạo sản phẩm thành công!", Toast.LENGTH_LONG).show();
                        }else Toast.makeText(Add_Product_Staff_Activity.this, "Sản phẩm này đã tồn tại!", Toast.LENGTH_LONG).show();
                    } else Toast.makeText(Add_Product_Staff_Activity.this, "Nhập thiếu thông tin!", Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    Toast.makeText(Add_Product_Staff_Activity.this, "Nhập thiếu thông tin!", Toast.LENGTH_LONG).show();
                }
            }
        });
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

    private void clickDate() {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                picker = new DatePickerDialog(Add_Product_Staff_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }

    private void initView() {
        img = findViewById(R.id.img_pro);
        type = findViewById(R.id.type_pro);
        name = findViewById(R.id.name_pro);
        count = findViewById(R.id.count_pro);
        price = findViewById(R.id.price_pro);
        date = findViewById(R.id.date_pro);
        des = findViewById(R.id.des_pro);
        save = findViewById(R.id.save_pro);
        back = findViewById(R.id.back_pro);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Add_Product_Staff_Activity.this,
                android.R.layout.simple_spinner_item, arr_type);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(arrayAdapter);
    }
}
