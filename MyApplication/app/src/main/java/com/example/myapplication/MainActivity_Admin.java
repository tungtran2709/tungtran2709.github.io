package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Admin extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private int id_login;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        checkAndRequestPermissions();
        Intent intent = getIntent();
        id_login = intent.getIntExtra("id_login", 0);
        bottomNavigationView = findViewById(R.id.bottom_navigation_admin);
        createBottomNavigation();
        createFloatingActionButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.info:
                Intent intent_1 = new Intent(this, Personal_Information_Activity.class);
                intent_1.putExtra("id_login", id_login);
                this.startActivity(intent_1);
                break;
            case R.id.change_pass:
                Intent intent_2 = new Intent(this, Change_Pass_Activity.class);
                intent_2.putExtra("id_login", id_login);
                this.startActivity(intent_2);
                break;  
            case R.id.exit:
                Intent intent_3 = new Intent(this, Login_Activity.class);
                startActivity(intent_3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{ Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.SEND_SMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    private void createBottomNavigation() {
        Fragment s = new List_Staff_Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id_login", id_login);
        s.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_list_admin, s).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                switch (item.getItemId()) {
                    case R.id.staffs_admin:
                        selected = new List_Staff_Fragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id_login", id_login);
                        selected.setArguments(bundle);
                        break;
                    case R.id.stat_admin:
                        selected = new Statistical_Fragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_list_admin, selected).commit();
                return true;
            }
        });
    }

    private void createFloatingActionButton() {
        floatingActionButton = findViewById(R.id.fab_admin);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_Admin.this, Add_Staff_Admin_Activity.class);
                startActivity(intent);
            }
        });
    }
}