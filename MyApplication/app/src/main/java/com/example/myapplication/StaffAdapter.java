package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {
    List<Staff> list;
    Activity activity;
    int id_login;
    SQLite_Staff sqLite_staff;
    public StaffAdapter(List<Staff> list, Activity activity, int id_login) {
        this.list = list;
        this.activity = activity;
        this.id_login = id_login;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.view_staff,parent,false);
        return new StaffViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        Staff s = list.get(position);
        Activity activity = this.activity;
        byte[] bytes = s.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.img.setImageBitmap(bitmap);
        holder.name.setText(s.getName());
        holder.phone.setText(s.getPhone());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + s.getPhone()));
                activity.startActivity(intent);
            }
        });
        holder.mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + s.getPhone()));
                activity.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Update_Staff_Admin_Activity.class);
                intent.putExtra("id_login", id_login);
                intent.putExtra("id", s.getId());
                activity.startActivity(intent);
            }
        });

        Staff staff = null;
        sqLite_staff = new SQLite_Staff(activity);
        List<Staff> list_check = sqLite_staff.getAllStaff();
        for(int i = 0; i< list_check.size(); i++){
            if(list_check.get(i).getId() == id_login){
                staff = list_check.get(i);
                break;
            }
        }

        if(staff.getPosition().equals("Admin") && !s.getPosition().equals("Admin")){
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(activity)
                            .setTitle("Cảnh báo")
                            .setMessage("Bạn chắc muốn xóa nhân viên này không?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sqLite_staff = new SQLite_Staff(activity);
                                    sqLite_staff.deleteStaff(s.getId());
                                    list.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
                                }
                            })
                            .setNegativeButton("Không", null)
                            .setIcon(R.drawable.ic_baseline_warning_amber_24)
                            .show();
                }
            });
        } else if(staff.getPosition().equals("Nhân viên") || (staff.getPosition().equals("Admin") && s.getPosition().equals("Admin"))){
            holder.remove.setEnabled(false);
            holder.remove.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(list!=null)  return list.size();
        return 0;
    }

    public class StaffViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView name, phone;
        private ImageView call, mess, remove;
        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            call = itemView.findViewById(R.id.call);
            mess = itemView.findViewById(R.id.message);
            remove = itemView.findViewById(R.id.remove);
        }
    }
}
